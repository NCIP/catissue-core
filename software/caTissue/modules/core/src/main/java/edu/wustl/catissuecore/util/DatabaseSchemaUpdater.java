/**
 * 
 */
package edu.wustl.catissuecore.util;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;

import org.springframework.util.ReflectionUtils;

import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.connectionmanager.IConnectionManager;
import edu.wustl.dao.daofactory.DAOConfigFactory;

/**
 * Temporary class for updating database schema straight from the app, until
 * issues with Hudson are resolved.
 * 
 * @author Denis G. Krylov
 * 
 */
public final class DatabaseSchemaUpdater {

	public static void update() {

		try {
			final String msg = "DatabaseSchemaUpdater: Begin";
			log(msg);

			final String applicationName = CommonServiceLocator.getInstance()
					.getAppName();
			DAO dao = DAOConfigFactory.getInstance()
					.getDAOFactory(applicationName).getDAO();
			Method getConnectionManagerMethod = ReflectionUtils.findMethod(
					dao.getClass(), "getConnectionManager");
			ReflectionUtils.makeAccessible(getConnectionManagerMethod);
			IConnectionManager connectionManager = (IConnectionManager) ReflectionUtils
					.invokeMethod(getConnectionManagerMethod, dao);

			Connection c = null;
			try {
				c = connectionManager.getConnection();
				update(c);
			} finally {
				try {
					connectionManager.commit();
				} catch (Exception e) {
				}
				connectionManager.closeConnection();
			}

		} catch (Exception e) {
			final String msg = "DatabaseSchemaUpdater: Failed with "
					+ e.getMessage();
			log(msg);
		}

	}

	/**
	 * @param msg
	 */
	private static void log(final String msg) {
		System.out.println(msg);
		// System.err.println(msg);
	}

	private static void update(Connection c) {
/*		// #1
		execute(c, "alter table CATISSUE_CCTS_NOTIF drop column DATE_TIME");
		execute(c,
				"ALTER TABLE CATISSUE_CCTS_NOTIF ADD DATE_SENT datetime not null");
		execute(c,
				"ALTER TABLE CATISSUE_CCTS_NOTIF ADD DATE_RECEIVED datetime not null");
		execute(c,
				"ALTER TABLE CATISSUE_CCTS_NOTIF ADD DATE_SENT date not null");
		execute(c,
				"ALTER TABLE CATISSUE_CCTS_NOTIF ADD DATE_RECEIVED date not null");
		// #2
		execute(c,
				"ALTER TABLE catissue_participant ADD ( GRID_ID VARCHAR2(128))");
		execute(c, "ALTER TABLE catissue_participant ADD GRID_ID varchar(128)");

		// #3
		execute(c,
				"alter table CATISSUE_CCTS_DATA_QUEUE add PARTICIPANT_ID number(19,0) null");
		execute(c,
				"alter table CATISSUE_CCTS_DATA_QUEUE add PARTICIPANT_ID bigint null");
		execute(c,
				"alter table CATISSUE_CCTS_DATA_QUEUE add constraint CCTSFK00011 foreign key (PARTICIPANT_ID) references CATISSUE_PARTICIPANT (IDENTIFIER)");

		// #4
		execute(c,
				"ALTER TABLE CATISSUE_CCTS_NOTIF ADD OBJECT_ID_TYPE number(19,0)");
		execute(c,
				"ALTER TABLE CATISSUE_CCTS_NOTIF ADD OBJECT_ID_VALUE varchar(128)");

		// #5 - GSID
		execute(c,
				"ALTER TABLE CATISSUE_SPECIMEN ADD GLOBAL_SPECIMEN_IDENTIFIER VARCHAR(50)");

		// #6 - Site
		execute(c, "ALTER TABLE catissue_site ADD CTEP_ID varchar2(50)");
		execute(c,
				"ALTER TABLE catissue_site add constraint CTEP_ID UNIQUE (CTEP_ID)");
		execute(c, "ALTER TABLE catissue_site ADD CTEP_ID varchar(50)");
		execute(c,
				"ALTER TABLE catissue_site add constraint CTEP_ID UNIQUE KEY (CTEP_ID)");

		// #7
		execute(c,
				"ALTER TABLE catissue_coll_prot_reg ADD GRID_ID varchar2(128)");
		execute(c,
				"alter table CATISSUE_CCTS_DATA_QUEUE add registration_id number(19,0) null");
		execute(c,
				"alter table CATISSUE_CCTS_DATA_QUEUE add constraint CCTSFK00012 foreign key (registration_id) references catissue_coll_prot_reg (IDENTIFIER)");
		execute(c,
				"ALTER TABLE catissue_coll_prot_reg ADD GRID_ID varchar(128)");
		execute(c,
				"alter table CATISSUE_CCTS_DATA_QUEUE add registration_id bigint null");
		execute(c,
				"alter table CATISSUE_CCTS_DATA_QUEUE add constraint CCTSFK00012 foreign key (registration_id) references catissue_coll_prot_reg (IDENTIFIER)");

		// #8 -- IRB Site
		execute(c,
				"alter table catissue_specimen_protocol add irb_site_id number(19,0) null");
		execute(c,
				"alter table catissue_specimen_protocol add constraint CCTSFK00013 foreign key (irb_site_id) references catissue_site (IDENTIFIER)");
		execute(c,
				"alter table catissue_specimen_protocol add irb_site_id bigint null");
		execute(c,
				"alter table catissue_specimen_protocol add constraint CCTSFK00013 foreign key (irb_site_id) references catissue_site (IDENTIFIER)");

		execute(c,
				"alter table CATISSUE_CCTS_DATA_QUEUE add incoming BOOLEAN NOT NULL DEFAULT true");
		execute(c,
				"alter table CATISSUE_CCTS_DATA_QUEUE add incoming NUMBER(1) DEFAULT 1 NOT NULL");

		// #9 -- Institution
		// MYSQL
		execute(c,
				"ALTER TABLE catissue_institution "
						+ " ADD COLUMN DIRTY_EDIT_FLAG TINYINT NULL AFTER NAME, "
						+ "	ADD COLUMN REMOTE_IDENTIFIER BIGINT NULL AFTER DIRTY_EDIT_FLAG, "
						+ " ADD COLUMN REMOTE_MANAGED_FLAG TINYINT NULL AFTER REMOTE_IDENTIFIER");
		execute(c,
				"ALTER TABLE catissue_institution add constraint REMOTE_IDENTIFIER UNIQUE KEY (REMOTE_IDENTIFIER)");

		// Oracle
		execute(c, "ALTER TABLE catissue_institution "
				+ " ADD DIRTY_EDIT_FLAG NUMBER(1,0) NULL");
		execute(c, "ALTER TABLE catissue_institution "
				+ "	ADD REMOTE_IDENTIFIER NUMBER(19,0) NULL");
		execute(c, "ALTER TABLE catissue_institution "
				+ "	ADD REMOTE_MANAGED_FLAG NUMBER(1,0) NULL");
		execute(c,
				"ALTER TABLE catissue_institution add constraint REMOTE_IDENTIFIER UNIQUE (REMOTE_IDENTIFIER)");
		// #10 -- User
		// MYSQL
		execute(c, "ALTER TABLE catissue_user "
				+ " ADD DIRTY_EDIT_FLAG TINYINT NULL");
		execute(c, "ALTER TABLE catissue_user "
				+ "	ADD REMOTE_IDENTIFIER BIGINT NULL");
		execute(c, "ALTER TABLE catissue_user "
				+ "	ADD REMOTE_MANAGED_FLAG TINYINT NULL");
		execute(c,
				"ALTER TABLE catissue_user add constraint REMOTE_IDENTIFIER UNIQUE KEY (REMOTE_IDENTIFIER)");

		// Oracle
		execute(c, "ALTER TABLE catissue_user "
				+ " ADD DIRTY_EDIT_FLAG NUMBER(1,0) NULL");
		execute(c, "ALTER TABLE catissue_user "
				+ "	ADD REMOTE_IDENTIFIER NUMBER(19,0) NULL");
		execute(c, "ALTER TABLE catissue_user "
				+ "	ADD REMOTE_MANAGED_FLAG NUMBER(1,0) NULL");
		execute(c,
				"ALTER TABLE catissue_user add constraint REMOTE_IDENTIFIER UNIQUE (REMOTE_IDENTIFIER)");

		// #11 -- Collection Protocol
		// MYSQL
		execute(c, "ALTER TABLE catissue_collection_protocol "
				+ " ADD DIRTY_EDIT_FLAG TINYINT NULL");
		execute(c, "ALTER TABLE catissue_collection_protocol "
				+ "	ADD REMOTE_IDENTIFIER BIGINT NULL");
		execute(c, "ALTER TABLE catissue_collection_protocol "
				+ "	ADD REMOTE_MANAGED_FLAG TINYINT NULL");
		execute(c,
				"ALTER TABLE catissue_collection_protocol add constraint REMOTE_IDENTIFIER UNIQUE KEY (REMOTE_IDENTIFIER)");

		// Oracle
		execute(c, "ALTER TABLE catissue_collection_protocol "
				+ " ADD DIRTY_EDIT_FLAG NUMBER(1,0) NULL");
		execute(c, "ALTER TABLE catissue_collection_protocol "
				+ "	ADD REMOTE_IDENTIFIER NUMBER(19,0) NULL");
		execute(c, "ALTER TABLE catissue_collection_protocol "
				+ "	ADD REMOTE_MANAGED_FLAG NUMBER(1,0) NULL");
		execute(c,
				"ALTER TABLE catissue_collection_protocol add constraint REMOTE_IDENTIFIER UNIQUE (REMOTE_IDENTIFIER)");
*/	}

	private static void execute(Connection c, String sql) {
		Statement st = null;
		try {
			st = c.createStatement();
			st.execute(sql);
			final String msg = "DatabaseSchemaUpdater: OK: " + sql;
			log(msg);
		} catch (Exception e) {
			final String msg = "DatabaseSchemaUpdater: could not execute "
					+ sql + ": " + e.getMessage();
			log(msg);
		} finally {
			try {
				st.close();
			} catch (Exception e) {
			}
		}

	}

}
