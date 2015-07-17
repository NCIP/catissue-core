package com.krishagni.openspecimen.custom.sgh.init;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.common.PdfUtil;
import com.krishagni.catissueplus.core.common.service.impl.ConfigurationServiceImpl;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;

public class MigrateSurgicalPathologyReports implements InitializingBean {

	private static Log logger = LogFactory.getLog(MigrateSurgicalPathologyReports.class);

	private PlatformTransactionManager txnMgr;

	private JdbcTemplate jdbcTemplate;
	
	private ConfigurationServiceImpl cfgSvc;
	
	public void setTxnMgr(PlatformTransactionManager txnMgr) {
		this.txnMgr = txnMgr;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void setCfgSvc(ConfigurationServiceImpl cfgSvc) {
		this.cfgSvc = cfgSvc;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		migrate();
	}
	
	public void migrate() throws Exception {
		Date startTime = Calendar.getInstance().getTime();
		logger.info("Surgical pathology reports migration start time: " + startTime);
		
		/**
		 * Surgical pathology reports exist in two places 
		 * 1. In DE form i.e de table de_e_2009 
		 * 2. In caTissue (NCI 1.2), there was a specific module for sgh.
		 * hence need to migrate reports from both places    
		 */
		migrateFromDeTable();
		migrateFromCaTissueTable();
		
		Date endTime = Calendar.getInstance().getTime();
		logger.info("Surgical pathology report migration end time: " + endTime);
		logger.info("Total time for migration: " + 
				(endTime.getTime() - startTime.getTime()) / (1000 * 60) + " minutes");		
	}

	private void migrateFromDeTable() throws Exception {		
		Date startTime = Calendar.getInstance().getTime();
		logger.info("Migration from DE table start time: " + startTime);
		
		int totalMigratedReportsCnt = 0;
		final int maxResult = 100;
		boolean moreRecords = true;
		while (moreRecords) {
			TransactionTemplate txnTmpl = new TransactionTemplate(txnMgr);
			Integer migrtedReportsCnt =	
				txnTmpl.execute(new TransactionCallback<Integer>() {
					@Override
					public Integer doInTransaction(TransactionStatus status) {
						final List<SprDetail> sprDetails  = getSprDetailsFromDeTable(maxResult);
						if (CollectionUtils.isEmpty(sprDetails)) {
							logger.info("No surgical pathology reports to migrate from DE table");
							return 0;
						}
					
						for (final SprDetail detail : sprDetails) {
							OutputStream outputStream = null;
							try {
								File sprFile = getSprFile(detail.getVisitId());
								String sprText = PdfUtil.getText(detail.getInputStream());
								FileUtils.writeStringToFile(sprFile, sprText, (String) null, false);
							
								String sprName = detail.getFileName(); 
								sprName = sprName.substring(0, sprName.lastIndexOf(".")) + ".txt";
								jdbcTemplate.update(UPDATE_SPR_DOCUMENT_NAME_SQL, sprName, detail.getVisitId());
								
								jdbcTemplate.update(DISABLE_OLD_MIGRATED_SPR_IN_DE_SQL, detail.getRecordId());
							} catch (Exception e) {
								logger.error("Error while migrating records from DE table: ", e);
							} finally {
								IOUtils.closeQuietly(outputStream);
								IOUtils.closeQuietly(detail.getInputStream());
							}
						} 
						return sprDetails.size();
					}
				});
			
			totalMigratedReportsCnt = totalMigratedReportsCnt + migrtedReportsCnt;
			logger.info("Number of migrated reports from DE, till now: " + totalMigratedReportsCnt);
			
			if (migrtedReportsCnt < maxResult) {
				moreRecords = false;
			}
		}
		
		Date endTime = Calendar.getInstance().getTime();
		logger.info("Migration from DE table end time: " + endTime);
		logger.info("Total time for migration from DE table: " + 
				(endTime.getTime() - startTime.getTime()) / (1000 * 60) + " minutes");		
	}
	
	private void migrateFromCaTissueTable() {
		Date startTime = Calendar.getInstance().getTime();
		logger.info("Migration from static table start time: " + startTime);
		
		int totalMigratedReportsCnt = 0;
		final int maxResult = 100;
		boolean moreRecords = true;
		while (moreRecords) {
			TransactionTemplate txnTmpl = new TransactionTemplate(txnMgr);
			Integer migrtedReportsCnt = 
				txnTmpl.execute(new TransactionCallback<Integer>() {
					@Override
					public Integer doInTransaction(TransactionStatus status) {
						final List<SprDetail> sprDetails  = getSprDetailsFromStaticTable(maxResult);
						if (CollectionUtils.isEmpty(sprDetails)) {
							logger.info("No surgical pathology reports in static table to migrate from static table");
							return 0;
						}
						
						for (final SprDetail detail : sprDetails) {
							OutputStream outputStream = null;
							try {
								File sprFile = getSprFile(detail.getVisitId());
								FileUtils.writeStringToFile(sprFile, detail.getReportContent(), (String) null, false);
								
								String sprName = detail.getVisitName() + ".txt"; 
								boolean sprLocked = SPR_LOCKED.equals(detail.getActivityStatus());
								jdbcTemplate.update(UPDATE_SPR_DOC_NAME_AND_LOCK_STATUS_SQL, new Object[] {sprName, sprLocked, detail.getVisitId()});
								
								jdbcTemplate.update(DISABLE_OLD_MIGRATED_SPR_IN_CATISSUE_SQL, detail.getRecordId());
							} catch (Exception e) {
								logger.error("Error while migrating records from static table: ", e);
							} finally {
								IOUtils.closeQuietly(outputStream);
							}
						} 
						return sprDetails.size();
					}
				});
			
			totalMigratedReportsCnt = totalMigratedReportsCnt + migrtedReportsCnt;
			logger.info("Number of reports migrated from static table, till now: " + totalMigratedReportsCnt);
			
			if (migrtedReportsCnt < maxResult) {
				moreRecords = false;
			}
		}
		
		Date endTime = Calendar.getInstance().getTime();
		logger.info("Migration from static table end time: " + endTime);
		logger.info("Total time for migration from static table: " + 
				(endTime.getTime() - startTime.getTime()) / (1000 * 60) + " minutes");		
	}

	private List<SprDetail> getSprDetailsFromDeTable(final int maxResult) {
		return jdbcTemplate.query(
				GET_SPR_DETAILS_FROM_DE_SQL,
				new Object[] {maxResult},
				new RowMapper<SprDetail>() {
					public SprDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
						SprDetail sprDetail = new SprDetail();
						sprDetail.setVisitId(rs.getLong("visitId"));
						sprDetail.setFileName(rs.getString("fileName"));
						sprDetail.setInputStream(rs.getBlob("fileContent").getBinaryStream());
						sprDetail.setRecordId(rs.getLong("recordId"));
						return sprDetail;
					}
				});					
	}
	
	private List<SprDetail> getSprDetailsFromStaticTable(final int maxResult) {
		return jdbcTemplate.query(
				GET_SPR_DETAILS_FROM_CATISSUE_SQL,
				new Object[] {maxResult},
				new RowMapper<SprDetail>() {
					public SprDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
						SprDetail sprDetail = new SprDetail();
						sprDetail.setVisitId(rs.getLong("visitId"));
						sprDetail.setVisitName(rs.getString("visitName"));
						sprDetail.setActivityStatus(rs.getString("activityStatus"));
						sprDetail.setReportContent(rs.getString("reportContent"));
						sprDetail.setRecordId(rs.getLong("recordId"));
						return sprDetail;
					}
			 });					
	}
	
	private File getSprFile(Long visitId) {
		String path = cfgSvc.getStrSetting(
				ConfigParams.MODULE, 
				ConfigParams.SPR_DIR, 
				getDefaultVisitSprDir());
		path = path + File.separator + visitId + File.separator + "spr.txt";
		
		return new File(path);
	}
	
	private static String getDefaultVisitSprDir() {
		return ConfigUtil.getInstance().getDataDir() + File.separator + "visit-sprs";
	}
	
	private static final String GET_SPR_DETAILS_FROM_DE_SQL = 
			"select " + 
			  "spr.identifier recordId, scg_re.specimen_collection_group_id visitId, " +
			  "spr.DE_AT_1904_file_name fileName, spr.de_at_1904 fileContent " +
			"from " + 
			  "dyextn_abstract_form_context afc " + 
			  "inner join dyextn_abstract_record_entry re on re.abstract_form_context_id = afc.identifier " + 
			  "inner join catissue_scg_rec_ntry scg_re on scg_re.identifier = re.identifier " + 
			  "inner join de_e_1905 spr on spr.dyextn_as_1945_1960 = re.identifier " +
			  "inner join catissue_specimen_coll_group scg on scg.identifier = scg_re.specimen_collection_group_id " +
			"where "+
			  "afc.identifier = 47 and spr.de_at_1904_file_name is not null " +
			  "and spr.activity_status is null and re.activity_status = 'Active' and scg.activity_status <> 'Disabled' limit ?";
	
	
	private static final String GET_SPR_DETAILS_FROM_CATISSUE_SQL =
			"select "+ 
			  "pr.identifier recordId, pr.activity_status activityStatus, " +
			  "dr.scg_id visitId, scg.name visitName, rc.report_data reportContent " + 
			"from " +
			  "catissue_pathology_report pr " + 
			  "join catissue_deidentified_report dr on dr.identifier = pr.identifier " + 
			  "join catissue_report_textcontent rtc on rtc.report_id = pr.identifier " + 
			  "join catissue_report_content rc on rc.identifier = rtc.identifier " +
			  "join catissue_specimen_coll_group scg on dr.scg_id = scg.identifier " +
			"where " +
			  "pr.report_status is null and scg.activity_status <> 'Disabled' limit ?";
	
	private static final String UPDATE_SPR_DOCUMENT_NAME_SQL =
			"update " +
			  "catissue_specimen_coll_group " +
			"set " +
			  "spr_name = ? " +
			"where identifier = ? " ;
	
	private static final String UPDATE_SPR_DOC_NAME_AND_LOCK_STATUS_SQL =
			"update " +
			  "catissue_specimen_coll_group " +
			"set " +
			  "spr_name = ? , spr_locked = ? " +
			"where " +
			  "identifier = ? " ;
	
	private static final String DISABLE_OLD_MIGRATED_SPR_IN_DE_SQL = 
			"update " +
			  "de_e_1905 " +
			"set " +
			  "activity_status = 'Disabled' " +
			"where " + 
			  "identifier = ? " ;
	
	private static final String DISABLE_OLD_MIGRATED_SPR_IN_CATISSUE_SQL = 
			"update " +
			  "catissue_pathology_report " +
			"set " +
			  "report_status = 'Disabled' " +
			"where "+
			  "identifier = ?" ;

	
	private static String SPR_LOCKED = "Locked";
	
	private class SprDetail {
		private Long visitId;
		 
		private String visitName;
		 
		private String fileName;
		 
		private InputStream inputStream;
		 
		private Long recordId;
		 
		private String activityStatus;
		 
		private String reportContent;
		 
		public Long getVisitId() {
			return visitId;
		}

		public void setVisitId(Long visitId) {
			this.visitId = visitId;
		}

		public String getVisitName() {
			return visitName;
		}

		public void setVisitName(String visitName) {
			this.visitName = visitName;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public InputStream getInputStream() {
			return inputStream;
		}

		public void setInputStream(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		public Long getRecordId() {
			return recordId;
		}

		public void setRecordId(Long recordId) {
			this.recordId = recordId;
		}

		public String getActivityStatus() {
			return activityStatus;
		}

		public void setActivityStatus(String activityStatus) {
			this.activityStatus = activityStatus;
		}

		public String getReportContent() {
			return reportContent;
		}

		public void setReportContent(String reportContent) {
			this.reportContent = reportContent;
		}
	}
}
