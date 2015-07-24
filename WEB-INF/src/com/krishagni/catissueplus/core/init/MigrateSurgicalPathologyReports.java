package com.krishagni.catissueplus.core.init;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.openspecimen.core.migration.domain.Migration.Status;
import com.krishagni.openspecimen.core.migration.domain.Migration.Version;
import com.krishagni.openspecimen.core.migration.events.MigrationDetail;
import com.krishagni.openspecimen.core.migration.services.MigrationService;

public class MigrateSurgicalPathologyReports implements InitializingBean {
	
	private static Log logger = LogFactory.getLog(MigrateSurgicalPathologyReports.class);

	private PlatformTransactionManager txnMgr;
	
	private DaoFactory daoFactory;
	
	private ConfigurationService cfgSvc;
	
	private Properties migrationProps;
	
	private MigrationService migrationSvc;
	
	public void setTxnMgr(PlatformTransactionManager txnMgr) {
		this.txnMgr = txnMgr;
	}

	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}

	public void setMigrationProps(Properties migrationProps) {
		this.migrationProps = migrationProps;
	}

	public void setMigrationSvc(MigrationService migrationSvc) {
		this.migrationSvc = migrationSvc;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		migrate();
	}
	
	private void migrate() {
		String srcDirPath = migrationProps.getProperty("visit.old_spr_dir");
		if (StringUtils.isBlank(srcDirPath)) {
			logger.info("Old spr directory path not spcified, either no need of migration for this version or missed it.");
			return;
		}
		
		MigrationDetail migrationDetail = migrationSvc.getMigration(
				MIGRATION_NAME, 
				Version.OS_V11.name(),
				Version.OS_V21.name());
		if (migrationDetail != null && migrationDetail.getStatus().equals(Status.SUCCESS)) {
			logger.info("Migration was done successfully.");
			return;
		}
		
		Date startTime = Calendar.getInstance().getTime();
		logger.info("Surgical pathology reports migration from version OS v1.1 to OS v2.1 start time: " + startTime);
		
		File srcDir = new File(srcDirPath);
		List<File[]> fileArray = splitArray(srcDir.listFiles(), 100);
		
		Status status = Status.SUCCESS;
		TransactionTemplate txnTmpl = new TransactionTemplate(txnMgr);
		try {
			for (final File[] files : fileArray) {
				Boolean success = txnTmpl.execute(new TransactionCallback<Boolean>() {
					@Override
					public Boolean doInTransaction(TransactionStatus status) {
						try {
							migrateSprs(files);
						} catch (Exception e) {
							logger.error("Surgical pathology report migration fails with error", e);
							return false;
						}
						return true;
					}
				});
				
				if (!success) {
					status = Status.FAIL;
					break;
				}
			} 			
		} catch(Exception e) {
			status = Status.FAIL;
			logger.error("Surgical pathology report migration fails with error", e);
		} finally {
			saveMigration(migrationDetail, status);
			Date endTime = Calendar.getInstance().getTime();
			logger.info("Surgical pathology report migration for version OS v1.1 to OS v2.1 end time: " + endTime);
			logger.info("Total time for migration: " + 
					(endTime.getTime() - startTime.getTime()) / (1000 * 60) + " minutes");
		}
	}

	private void migrateSprs(final File[] files) throws Exception {
		for (File srcFile : files) {
			try {
				if (srcFile.isDirectory()) {
					continue;
				}
				
				String[] arr = srcFile.getName().split("_", 2);
				Long visitId = Long.parseLong(arr[0]);
				String sprName = arr[1];
				
				Visit visit = daoFactory.getVisitsDao().getById(visitId);
				if (visit == null || visit.getActivityStatus().equals("Disabled")) {
					logger.info("Visit does not exist with idenifier: " + visitId);
					continue;
				}
				
				String sprText = Utility.getFileText(srcFile);
				FileUtils.writeStringToFile(getSprFile(visitId), sprText, (String) null, false);
				visit.setSprName(sprName);
				logger.info("Surgical pathology report migrated successfully for visit id "+ visitId);
			} catch (Exception e) {
				logger.error("Error while migrating file:" + srcFile.getName(), e);
				throw e;
			}
		}
	}

	private void saveMigration(MigrationDetail detail, Status status) {
		if (detail == null) {
			detail = new MigrationDetail();
			detail.setName(MIGRATION_NAME);
			detail.setVersionFrom(Version.OS_V11);
			detail.setVersionTo(Version.OS_V21);
		}
		
		detail.setStatus(status);
		detail.setDate(new Date());
		migrationSvc.saveMigration(detail);
	}
	
	private static List<File[]> splitArray(File[] array, int chunkSize) {
		List<File[]> result = new ArrayList<File[]>();
		int noOfChunks = array.length / chunkSize;
		     
		for (int chunk = 0; chunk < noOfChunks; ++chunk) {
			int startIdx = chunk * chunkSize;
			result.add(Arrays.copyOfRange(array, startIdx, startIdx + chunkSize));
		}
		
		int remaining  = array.length % chunkSize;
		if (remaining > 0) {
			result.add(Arrays.copyOfRange(array, array.length - remaining, array.length));
		}
		
		return result;
	}
	
	private File getSprFile(Long visitId) {
		String path = cfgSvc.getStrSetting(
				ConfigParams.MODULE, 
				ConfigParams.SPR_DIR, 
				getDefaultVisitSprDir());
		path = path + File.separator + visitId + File.separator + "spr.txt";
		
		return new File(path);
	}
	
	private String getDefaultVisitSprDir() {
		return cfgSvc.getDataDir() + File.separator + "visit-sprs";
	}

	private static final String MIGRATION_NAME = "Surgical Pathology Reports";
}
