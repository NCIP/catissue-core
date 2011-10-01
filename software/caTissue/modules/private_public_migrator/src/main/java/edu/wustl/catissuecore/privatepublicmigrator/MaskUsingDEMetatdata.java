
package edu.wustl.catissuecore.privatepublicmigrator;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Collection;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.wustl.catissuecore.util.global.Constants;

/**
 *
 * @author prashant_bandal
 *
 */
public class MaskUsingDEMetatdata
{

	/**
	 * Specify random Number.
	 */
	private static int randomNumber;

	/**
	 * main method.
	 * @param args arguments.
	 */
	public static void main(String[] args)
	{
		final MaskUsingDEMetatdata mask = new MaskUsingDEMetatdata();
		mask.maskIdentifiedData();
	}

	/**
	 * This method mask Identified Data.
	 */
	public void maskIdentifiedData()
	{
		final Random generator = new Random();
		while (randomNumber == 0)
		{
			randomNumber = generator.nextInt(200);
		}

		try
		{

			final EntityManagerInterface entityManager = EntityManager.getInstance();
			final Collection<EntityInterface> entities = entityManager.getAllEntities();
			final int totalNoOfEntities = entities.size();
			System.out.println("No Of entities:" + totalNoOfEntities);

			final Configuration cfg = new Configuration();
			final File file = new File(".//classes//hibernate.cfg.xml");
			final File file1 = new File(file.getAbsolutePath());
			System.out.println(file.getAbsolutePath());
			cfg.configure(file1);

			final SessionFactory sf = cfg.buildSessionFactory();
			final Session session = sf.openSession();
			final Transaction tx = session.beginTransaction();

			for (final EntityInterface entity : entities)
			{
				final Collection<AttributeInterface> attributeCollection = entity
						.getAttributeCollection();


				for (final AttributeInterface attribute : attributeCollection)
				{

					// updated code for derived attributes
					if (!this.isTagPresent(attribute, "Derived")) //Please verify the tag value used to identify whether the attributes is inherited or not
					{
						System.out.println("entity.getName  &&&&&&&&&&&&&&&"+entity.getName());
						if (!entity.getName().contains("Deprecated") && !entity.getName().contains("Password"))
											//.getTableProperties().getName().equalsIgnoreCase("CATISSUE_PASSWORD"))
						{
						//System.out.println("entity.getTableProperties().getName()  &&&&&&&&&&&&&&&"+entity.getTableProperties().getName());

								if (attribute.getIsIdentified() != null
										&& attribute.getIsIdentified() == true
										&& attribute.getAttributeTypeInformation().getDataType()
												.equalsIgnoreCase("String"))
								{
								//System.out.println("entity.getTableProperties().getName()  &&&&&&&&&&&&&&&"+entity.getTableProperties().getName());
									this.maskString(attribute.getColumnProperties().getName(), entity
											.getTableProperties().getName(), session);
								}

								else if (attribute.getAttributeTypeInformation().getDataType()
										.equalsIgnoreCase("Date"))
								{
								//System.out.println("entity.getTableProperties().getName()  &&&&&&&&&&&&&&&"+entity.getTableProperties().getName());
									this.maskDate(attribute.getColumnProperties().getName(), entity
											.getTableProperties().getName(), session);
								}
								else if (this.isCommentFiled(attribute))
								{
								//System.out.println("entity.getTableProperties().getName()  &&&&&&&&&&&&&&&"+entity.getTableProperties().getName());
									this.maskString(attribute.getColumnProperties().getName(), entity
											.getTableProperties().getName(), session);
								}

						}
					}
				}
			}


			// sql String to delete  ReportQueue table
			String sqlString = "truncate table CATISSUE_REPORT_QUEUE";
			System.out.println("done ########### : "+sqlString);
			this.executeQuery(sqlString, session);

			// sql String to delete ReportQueue table
			sqlString = "truncate table CATISSUE_REPORT_PARTICIP_REL";
			this.executeQuery(sqlString, session);

			/* Delete audit related tables starts */

			// Disable constraints on audit tables.
			this.disableAuditTables(session);

			sqlString = "truncate table catissue_bulk_operation";
			this.executeQuery(sqlString, session);

			sqlString = "truncate table job_details";
			this.executeQuery(sqlString, session);

			sqlString = "truncate table catissue_data_audit_event_log";
			this.executeQuery(sqlString, session);

			sqlString = "truncate table catissue_audit_event_query_log";
			this.executeQuery(sqlString, session);

			sqlString = "truncate table catissue_audit_event_details";
			this.executeQuery(sqlString, session);

			sqlString = "truncate table catissue_audit_event_log";
			this.executeQuery(sqlString, session);

			sqlString = "update catissue_audit_event set USER_ID=null";
			this.executeQuery(sqlString, session);

			sqlString = "truncate table catissue_audit_event";
			this.executeQuery(sqlString, session);

			// Enable constraints on audit tables.
			this.enableAuditTables(session);

			/* Delete audit related tables ends */

			this.maskReportText(session);

			tx.commit();
			session.close();
		}
		catch (final Throwable e)
		{
			e.printStackTrace();
			throw new RuntimeException("Error in maskIdentifiedData");
		}
	}

	/**
	 * This method mask Date.
	 * @param columnName column Name
	 * @param tableName table Name
	 * @param session Session object.
	 * @throws Exception generic Exception.
	 */
	private void maskDate(String columnName, String tableName, Session session) throws Exception
	{
		String sqlString = null;
		if (!tableName.equalsIgnoreCase("CATISSUE_PASSWORD"))
		{
			final String dbType = session.connection().getMetaData().getDatabaseProductName();

			if (dbType.equalsIgnoreCase("oracle"))
			{
				sqlString = "update " + tableName + " set " + columnName + "=add_months("
						+ columnName + ", -" + randomNumber + ")";
			}
			if (dbType.equalsIgnoreCase("mysql"))
			{
				sqlString = "update " + tableName + " set " + columnName + "=date_add("
						+ columnName + ", INTERVAL -" + randomNumber + " MONTH);";
			}
			if (dbType.equalsIgnoreCase(Constants.MSSQLSERVER_DATABASE))
			{
				sqlString = "update " + tableName + " set " + columnName + "=dateadd(\"MONTH\", "
						+ randomNumber + ", " + columnName + ");";
			}
			this.executeQuery(sqlString, session);
		}
	}

	/**
	 * This method mask string.
	 * @param columnName column Name
	 * @param tableName table Name
	 * @param session Session object.
	 * @throws Exception generic Exception.
	 */
	private void maskString(String columnName, String tableName, Session session) throws Exception
	{
		String sqlString = "update " + tableName + " set " + columnName + "=null";
		if (tableName.equals("CATISSUE_PART_MEDICAL_ID"))
		{
			sqlString = "truncate table CATISSUE_PART_MEDICAL_ID";
		}
		this.executeQuery(sqlString, session);
	}

	/**
	 * This method mask report text.
	 * @param session Session object.
	 * @throws Exception generic Exception.
	 */
	private void maskReportText(Session session) throws Exception
	{
		String sqlString = null;
		final String dbType = session.connection().getMetaData().getDatabaseProductName();

		if (dbType.equalsIgnoreCase("oracle")
				|| dbType.equalsIgnoreCase(Constants.MSSQLSERVER_DATABASE))
		{
			sqlString = "update catissue_report_content set report_data=NULL where identifier in(select a.identifier from catissue_report_content a join catissue_report_textcontent b on a.identifier=b.identifier join catissue_pathology_report c on c.identifier=b.report_id where c.REPORT_STATUS in ('DEIDENTIFIED','DEID_PROCESS_FAILED','PENDING_FOR_DEID'))";
		}
		if (dbType.equalsIgnoreCase("mysql"))
		{
			sqlString = "update CATISSUE_REPORT_CONTENT as rc, CATISSUE_REPORT_TEXTCONTENT as rt, CATISSUE_PATHOLOGY_REPORT as pr set rc.REPORT_DATA=NULL where pr.IDENTIFIER=rt.report_id and rt.IDENTIFIER=rc.IDENTIFIER and pr.REPORT_STATUS in ('DEIDENTIFIED','DEID_PROCESS_FAILED','PENDING_FOR_DEID')";
		}
		this.executeQuery(sqlString, session);
	}

	/**
	 * This method executes Query.
	 * @param sqlString sql String
	 * @param session Session object.
	 * @throws Exception generic Exception
	 */
	private void executeQuery(String sqlString, Session session) throws Exception
	{
		try
		{
			System.out.println(sqlString);
			final Connection con = session.connection();
			final Statement stmt = con.createStatement();
			stmt.execute(sqlString);
		}
		catch (final Exception exception)
		{
			System.out.println("Error in executeQuery ");
			throw exception;
		}
	}

	/**
	 * Checks is Comment Filed.
	 * @param attribute AttributeInterface
	 * @return true/false.
	 */
	private boolean isCommentFiled(AttributeInterface attribute)
	{
		boolean flag = false;
		if (attribute.getEntity().getName().equals("edu.wustl.catissuecore.domain.Specimen")
				&& attribute.getName().equals("comment"))
		{
			flag = true;
		}
		else if (attribute.getEntity().getName().equals(
				"edu.wustl.catissuecore.domain.SpecimenCollectionGroup")
				&& attribute.getName().equals("comment"))
		{
			flag = true;
		}
		return flag;
	}

	/**
	 * Disable audit related tables constraints.
	 * @param session session
	 * @throws Exception Exception
	 */
	private void disableAuditTables(Session session) throws Exception
	{
		final String dbType = session.connection().getMetaData().getDatabaseProductName();

		if (dbType.equalsIgnoreCase(Constants.ORACLE_DATABASE))
		{
			// oracle db.
			this.disableAuditTablesOracleConstraints(session);
		}
		else if (dbType.equalsIgnoreCase(Constants.MSSQLSERVER_DATABASE))
		{
			// mssqlserver db.
			this.disableAuditTablesMsSqlServerConstraints(session);
		}
	}

	/**
	 * Enable audit related tables constraints.
	 * @param session session
	 * @throws Exception Exception
	 */
	private void enableAuditTables(Session session) throws Exception
	{
		final String dbType = session.connection().getMetaData().getDatabaseProductName();

		if (dbType.equalsIgnoreCase(Constants.ORACLE_DATABASE))
		{
			// oracle db.
			this.enableAuditTablesOracleConstraints(session);
		}
		else if (dbType.equalsIgnoreCase(Constants.MSSQLSERVER_DATABASE))
		{
			// mssqlserver db.
			this.enableAuditTablesMsSqlServerConstraints(session);
		}
	}

	/**
	 * Disable constraints on audit tables for oracle db.
	 * @param session session
	 * @throws Exception Exception
	 */
	private void disableAuditTablesOracleConstraints(Session session) throws Exception
	{
		String sqlString = "ALTER TABLE catissue_audit_event_details DISABLE CONSTRAINT FK5C07745D34FFD77F";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE catissue_audit_event_query_log DISABLE CONSTRAINT FK62DC439DBC7298A9";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE CATISSUE_AUDIT_EVENT_LOG DISABLE CONSTRAINT FK8BB672DF77F0B904";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE CATISSUE_AUDIT_EVENT DISABLE CONSTRAINT FKACAF697A2206F20F";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE catissue_data_audit_event_log DISABLE CONSTRAINT FK5C07745DC62F96A411";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE catissue_data_audit_event_log DISABLE CONSTRAINT FK5C07745DC62F96A412";
		this.executeQuery(sqlString, session);

	}

	/**
	 * Enable Audit Tables Oracle Constraints
	 * @param session session
	 * @throws Exception Exception
	 */
	private void enableAuditTablesOracleConstraints(Session session) throws Exception
	{
		String sqlString = "ALTER TABLE catissue_audit_event_details ENABLE CONSTRAINT FK5C07745D34FFD77F";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE catissue_audit_event_query_log ENABLE CONSTRAINT FK62DC439DBC7298A9";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE CATISSUE_AUDIT_EVENT_LOG ENABLE CONSTRAINT FK8BB672DF77F0B904";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE CATISSUE_AUDIT_EVENT ENABLE CONSTRAINT FKACAF697A2206F20F";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE catissue_data_audit_event_log ENABLE CONSTRAINT FK5C07745DC62F96A411";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE catissue_data_audit_event_log ENABLE CONSTRAINT FK5C07745DC62F96A412";
		this.executeQuery(sqlString, session);
	}

	/**
	 * Disable audit related tables constraints if MsSQLServer DB.
	 * To disable all constraints - ALTER TABLE <Table_Name> NOCHECK CONSTRAINT ALL
	 * @param session session
	 * @exception Exception Exception
	 */
	private void disableAuditTablesMsSqlServerConstraints(Session session) throws Exception
	{
		String sqlString = "ALTER TABLE catissue_audit_event_details NOCHECK CONSTRAINT ALL";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE catissue_audit_event_query_log NOCHECK CONSTRAINT ALL";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE CATISSUE_AUDIT_EVENT_LOG NOCHECK CONSTRAINT ALL";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE CATISSUE_AUDIT_EVENT NOCHECK CONSTRAINT ALL";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE catissue_data_audit_event_log NOCHECK CONSTRAINT ALL";
		this.executeQuery(sqlString, session);
	}

	/**
	 * Enable audit related tables constraints if MsSQLServer DB.
	 * To enable all constraints - ALTER TABLE <Table_Name> CHECK CONSTRAINT ALL
	 * @param session session
	 * @exception Exception Exception
	 */
	private void enableAuditTablesMsSqlServerConstraints(Session session) throws Exception
	{
		String sqlString = "ALTER TABLE catissue_audit_event_details CHECK CONSTRAINT ALL";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE catissue_audit_event_query_log CHECK CONSTRAINT ALL";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE CATISSUE_AUDIT_EVENT_LOG CHECK CONSTRAINT ALL";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE CATISSUE_AUDIT_EVENT CHECK CONSTRAINT ALL";
		this.executeQuery(sqlString, session);

		sqlString = "ALTER TABLE catissue_data_audit_event_log CHECK CONSTRAINT ALL";
		this.executeQuery(sqlString, session);
	}

	/**
	 * updated code for derived attributes.
	 * @param entity entity
	 * @param tag tag
	 * @return is Tag Present boolean value true/false
	 */
	private boolean isTagPresent(AbstractMetadataInterface entity, String tag)
	{

		boolean isTagPresent = false;

		final Collection<TaggedValueInterface> taggedValueCollection = entity
				.getTaggedValueCollection();

		for (final TaggedValueInterface tagValue : taggedValueCollection)
		{

			if (tagValue.getKey().equalsIgnoreCase(tag))
			{
				isTagPresent = true;
				break;
			}
		}
		return isTagPresent;
	}
}
