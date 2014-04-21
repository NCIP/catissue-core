package krishagni.catissueplus.upgrade;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.ResultExtractor;
import edu.common.dynamicextensions.ndao.TransactionManager;
import edu.common.dynamicextensions.nutility.DEApp;


public class MigrateForms {
	
	private static final Logger logger = Logger.getLogger(MigrateForms.class);

	private static final String GET_CONTAINER_IDS_SQL = 
		"select " +
		"	c.identifier, cfc.collection_protocol_id, map.static_entity_id , fc.identifier, count(pre.identifier), " +
		"	count(scg_re.identifier), count(sp_re.identifier) " +
		"from dyextn_container c " + 
		"	inner join dyextn_abstract_form_context fc on fc.container_id = c.identifier " +
		"	inner join dyextn_abstract_record_entry re on re.abstract_form_context_id = fc.identifier " + 
		"	left join catissue_participant_rec_ntry pre on pre.identifier = re.identifier " +
		"	left join catissue_scg_rec_ntry scg_re on scg_re.identifier = re.identifier " + 
		"	left join catissue_specimen_rec_ntry sp_re on sp_re.identifier = re.identifier " +
		"	left join catissue_cp_studyformcontext cfc on cfc.study_form_context_id = fc.identifier " + 
		"	left join dyextn_entity_map map on map.container_id = c.identifier " +
		"group by c.identifier, cfc.collection_protocol_id, fc.identifier, map.static_entity_id" ;
	
	
	private static final String GET_USER_ID = 
			"select" +
			"	identifier " +
			"from catissue_user " +
			"	where login_name = ? AND activity_status = 'Active'";
	
	
	public static void main(String[] args) 
	throws Exception {
		
		if (args.length == 0) {
			logger.error("Requires admin username as input. Exiting migration");
			return;
		}
		
		setupDataSource();
		UserContext usrCtx = getUserContext(args[0]);
		
		Date startTime = Calendar.getInstance().getTime();
		logger.info("Migration start time: " + startTime);
		Thread.sleep(5000);
		Map<Long, List<FormInfo>> containerInfo  = getAllFormsInfo();	
				
		logger.info("Old container ids : " + containerInfo);
		
		migrateContainers(containerInfo, usrCtx);

		Date endTime = Calendar.getInstance().getTime();
		logger.info("Migration end time: " + endTime);
		logger.info("Total time for migration: " + 
				(endTime.getTime() - startTime.getTime()) / (1000 * 60) + " minutes");
		
	}

	private static void migrateContainers(Map<Long, List<FormInfo>> containerInfo, UserContext usrCtx) {
		int count = 0;

		for (Entry<Long, List<FormInfo>> entry : containerInfo.entrySet()) {
			try {
				long t1 = System.currentTimeMillis();
				MigrateForm migrateForm = new MigrateForm(usrCtx);
				migrateForm.migrateForm(entry.getKey(), entry.getValue());
				++count;

				logger.info("Time taken to migrate " + entry.getKey() + " is " + (System.currentTimeMillis() - t1));
				logger.info("Migrated count: " + count);
			} catch (Exception e) {
				logger.error("Error migrating container: " + entry.getKey(), e);
			}	
		}
		logger.info("finished migrating " + count + " forms");
	}

	private static void setupDataSource() throws Exception{
		BasicDataSource ds = new BasicDataSource();
		
		Properties prop = new Properties();
		prop.load(new FileInputStream("caTissueInstall.properties"));
		
		String driverClass = null;
		String databaseType = prop.getProperty("database.type");
		
		if (databaseType.equals("mysql")) {
			driverClass = "com.mysql.jdbc.Driver";
			databaseType = databaseType + "://";

		} else if (databaseType.equals("oracle")) {
			driverClass = "oracle.jdbc.driver.OracleDriver";
			databaseType = databaseType + ":thin:@";
		}
		
		
		ds.setDriverClassName(driverClass);
		ds.setUsername(prop.getProperty("database.username"));
		ds.setPassword(prop.getProperty("database.password"));
		
		String jdbcUrl = "jdbc:" + databaseType +  prop.getProperty("database.host") 
							+ ":" + prop.getProperty("database.port") + "/" + prop.getProperty("database.name");
		ds.setUrl(jdbcUrl);
		
		String fileUploadDir = prop.getProperty("de.fileUploadDir");
		DEApp.init(ds, fileUploadDir);
	}

	private static UserContext getUserContext(final String username) {
		JdbcDao jdbcDao = JdbcDaoFactory.getJdbcDao();
		List<Object> params = new ArrayList<Object>();
		params.add(username);
		
		Long userId = jdbcDao.getResultSet(GET_USER_ID, params, new ResultExtractor<Long>() {
			@Override
			public Long extract(ResultSet rs) throws SQLException {
				return rs.next() ? rs.getLong(1) : null;
			}			
		});
		
		if (userId == null) {
			logger.error("No active user with login name: " + username);
			return null;
		}
			
		final Long finalUserId = userId;
		return new UserContext() {				
			@Override
			public String getUserName() {
				return username;
			}
			
			@Override
			public Long getUserId() {
				return finalUserId;
			}
			
			@Override
			public String getIpAddress() {
				return null;
			}
		};
	}
	
	private static Map<Long, List<FormInfo>> getAllFormsInfo() {
		try {
			JdbcDao jdbcDao = JdbcDaoFactory.getJdbcDao();
			return jdbcDao.getResultSet(GET_CONTAINER_IDS_SQL, null,  new ResultExtractor<Map<Long, List<FormInfo>>>() {
				@Override
				public Map<Long, List<FormInfo>> extract(ResultSet rs) throws SQLException {
					return extractFormInfo(rs);
				}				
			});
		} catch (Exception e) {
			throw new RuntimeException("Error while retrieving info of all forms ",e);
		}
	}
	
	protected static Map<Long, List<FormInfo>> extractFormInfo(ResultSet rs) throws SQLException {
		Map<Long, List<FormInfo>> containerInfo = new HashMap<Long, List<FormInfo>>();
		
		while(rs.next()) {
			// rs(1)               : containerId
			// rs(2)               : Collection protocol id
			// rs(3)               : Static entity id (Exists only for default forms)
			// rs(4)               : Old form fontext id
			// rs(5), rs(6), rs(7) : Determines to which entity type formContext belongs
			
			List<FormInfo> formInfos = containerInfo.get(rs.getLong(1));
			if (formInfos == null) {
				formInfos = new ArrayList<FormInfo>();
				containerInfo.put(rs.getLong(1), formInfos);
			} 
			
			FormInfo info = new FormInfo();
			info.setCpId((Long)rs.getLong(2) > 0 ? rs.getLong(2) : -1);
			info.setDefaultForm((Long)rs.getLong(3) > 0 ? true : false);
			info.setOldFormCtxId(rs.getLong(4));
			info.setParticipant((Long)rs.getLong(5) > 0 ? true : false);
			info.setScg((Long)rs.getLong(6) > 0 ? true : false);
			info.setSpecimen((Long)rs.getLong(7) > 0 ? true : false);

			formInfos.add(info);
		}
		return containerInfo;
	}
}
