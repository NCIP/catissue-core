import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.wustl.catissuecore.util.global.Constants;



public class MaskUsingDEMetatdata
{
	private static int randomNumber;
	
	public static void main(String[] args) 
	{	
		MaskUsingDEMetatdata mask=new MaskUsingDEMetatdata();
		mask.maskIdentifiedData();
	}
	
	public void maskIdentifiedData()
	{
		Random generator = new Random();
		while(randomNumber==0)
		{
			randomNumber=generator.nextInt(200);
		}
		
		try
		{
			
			EntityManagerInterface entityManager = EntityManager.getInstance();
			Collection<EntityInterface> entities = entityManager.getAllEntities();
			int totalNoOfEntities = entities.size();
			System.out.println("No Of entities:"+totalNoOfEntities);
		
			Configuration cfg = new Configuration();
			File file = new File(".//classes//hibernate.cfg.xml");
			File file1 = new File(file.getAbsolutePath());
			System.out.println(file.getAbsolutePath());
			cfg.configure(file1);
			
			SessionFactory sf = cfg.buildSessionFactory();
			Session session = sf.openSession();
			Transaction tx = session.beginTransaction();
			
			for(EntityInterface entity: entities)
            {
                Collection<AttributeInterface> attributeCollection = entity.getAttributeCollection();
                for (AttributeInterface attribute: attributeCollection)
                {                	
                	// updated code for derived attributes
                	if(!isTagPresent(attribute, "Derived"))    //Please verify the tag value used to identify whether the attributes is inherited or not
                	{
	                	if(attribute.getIsIdentified()!=null && attribute.getIsIdentified()==true && attribute.getAttributeTypeInformation().getDataType().equalsIgnoreCase("String"))
	                	{
	      					maskString(attribute.getColumnProperties().getName(),entity.getTableProperties().getName(), session);
						}
						else if(attribute.getAttributeTypeInformation().getDataType().equalsIgnoreCase("Date"))
						{
							maskDate(attribute.getColumnProperties().getName(),entity.getTableProperties().getName(), session);
						}
						else if(isCommentFiled(attribute))
						{
							maskString(attribute.getColumnProperties().getName(),entity.getTableProperties().getName(), session);
						}
                	}
                }
            }
			
			// sql String to update ParticipantMedicalIdentifier table
			String sqlString="truncate table CATISSUE_PART_MEDICAL_ID";
			executeQuery(sqlString, session);
			
			// sql String to delete  ReportQueue table
			sqlString="truncate table CATISSUE_REPORT_QUEUE";
			executeQuery(sqlString, session);
			
			// sql String to delete ReportQueue table
			sqlString="truncate table CATISSUE_REPORT_PARTICIP_REL";
			executeQuery(sqlString, session);
			
			/* Delete audit related tables starts */
			
			// Disable constraints on audit tables.
			disableAuditTables(session);
			
			sqlString="truncate table catissue_audit_event_details";
			executeQuery(sqlString, session);

			sqlString="truncate table catissue_audit_event_query_log";
			executeQuery(sqlString, session);
			
			sqlString="truncate table catissue_audit_event_log";
			executeQuery(sqlString, session);
			
			sqlString="update catissue_audit_event set USER_ID=null";
			executeQuery(sqlString, session);
			sqlString="truncate table catissue_audit_event";
			executeQuery(sqlString, session);
			
			// Enable constraints on audit tables.
			enableAuditTables(session);
			
			/* Delete audit related tables ends */
			
			maskReportText(session);
			
			tx.commit();
			session.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private void maskDate(String columnName, String tableName, Session session) throws SQLException
	{
		String sqlString=null;
		if(!tableName.equalsIgnoreCase("CATISSUE_PASSWORD"))
		{
			String dbType=session.connection().getMetaData().getDatabaseProductName();
	
			if(dbType.equalsIgnoreCase("oracle"))
			{
				sqlString="update "+tableName+" set "+columnName+"=add_months("+columnName+", -"+randomNumber+")";
			}
			if(dbType.equalsIgnoreCase("mysql"))
			{
				sqlString="update "+tableName+" set "+columnName+"=date_add("+columnName+", INTERVAL -"+randomNumber+" MONTH);";
			}
			if(dbType.equalsIgnoreCase(Constants.MSSQLSERVER_DATABASE)) {
				sqlString="update " + tableName + " set " + columnName + "=dateadd(\"MONTH\", " + randomNumber + ", " +  columnName + ");";
			}
			executeQuery(sqlString, session);
		}		
	}

	private void maskString(String columnName, String tableName, Session session) 
	{
		String sqlString="update "+tableName+" set "+columnName+"=null";
		executeQuery(sqlString, session);
	}
	
	private void maskReportText(Session session) throws SQLException
	{
		String sqlString=null;
		String dbType=session.connection().getMetaData().getDatabaseProductName();

		if(dbType.equalsIgnoreCase("oracle") || dbType.equalsIgnoreCase(Constants.MSSQLSERVER_DATABASE)) {
			sqlString="update catissue_report_content set report_data=NULL where identifier in(select a.identifier from catissue_report_content a join catissue_report_textcontent b on a.identifier=b.identifier join catissue_pathology_report c on c.identifier=b.report_id where c.REPORT_STATUS in ('DEIDENTIFIED','DEID_PROCESS_FAILED','PENDING_FOR_DEID'))";
		}
		if(dbType.equalsIgnoreCase("mysql")) {
			sqlString="update CATISSUE_REPORT_CONTENT as rc, CATISSUE_REPORT_TEXTCONTENT as rt, CATISSUE_PATHOLOGY_REPORT as pr set rc.REPORT_DATA=NULL where pr.IDENTIFIER=rt.report_id and rt.IDENTIFIER=rc.IDENTIFIER and pr.REPORT_STATUS in ('DEIDENTIFIED','DEID_PROCESS_FAILED','PENDING_FOR_DEID')";
		}
		executeQuery(sqlString, session);
	}
	
	private void executeQuery(String sqlString, Session session)
	{
		try
		{
			System.out.println(sqlString);
			Connection con=session.connection();
			Statement stmt=con.createStatement();
			stmt.execute(sqlString);
		}
		catch (Exception e) 
		{
			System.out.println("Error in maskString ");
			e.printStackTrace();
		}
	}
	
	private boolean isCommentFiled(AttributeInterface attribute)
	{
		boolean flag = false;
		if(attribute.getEntity().getName().equals("edu.wustl.catissuecore.domain.Specimen") && attribute.getName().equals("comment"))
		{
			flag = true;
		}
		else if(attribute.getEntity().getName().equals("edu.wustl.catissuecore.domain.SpecimenCollectionGroup") && attribute.getName().equals("comment"))
		{
			flag = true;
		}
		return flag;
	}
	
	/**
	 * Disable audit related tables constraints.
	 * 
	 * @param session
	 * @throws SQLException
	 */
	private void disableAuditTables(Session session) throws SQLException {
		String dbType=session.connection().getMetaData().getDatabaseProductName();

		if(dbType.equalsIgnoreCase(Constants.ORACLE_DATABASE)) {
			// oracle db.
			disableAuditTablesOracleConstraints(session);
		} else if(dbType.equalsIgnoreCase(Constants.MSSQLSERVER_DATABASE)) {
			// mssqlserver db.
			disableAuditTablesMsSqlServerConstraints(session);
		}
	}

	/**
	 * Enable audit related tables constraints.
	 * 
	 * @param session
	 * @throws SQLException
	 */
	private void enableAuditTables(Session session) throws SQLException {
		String dbType=session.connection().getMetaData().getDatabaseProductName();

		if(dbType.equalsIgnoreCase(Constants.ORACLE_DATABASE)) {
			// oracle db.
			enableAuditTablesOracleConstraints(session);
		} else if(dbType.equalsIgnoreCase(Constants.MSSQLSERVER_DATABASE)) {
			// mssqlserver db.
			enableAuditTablesMsSqlServerConstraints(session);
		}
	}
	
	/**
	 * Disable constraints on audit tables for oracle db.
	 *  
	 * @param session
	 * @throws SQLException
	 */
	private void disableAuditTablesOracleConstraints(Session session) throws SQLException {
		String sqlString="ALTER TABLE catissue_audit_event_details DISABLE CONSTRAINT FK5C07745D34FFD77F";
		executeQuery(sqlString, session);

		sqlString="ALTER TABLE catissue_audit_event_query_log DISABLE CONSTRAINT FK62DC439DBC7298A9";
		executeQuery(sqlString, session);
		
		sqlString="ALTER TABLE CATISSUE_AUDIT_EVENT_LOG DISABLE CONSTRAINT FK8BB672DF77F0B904";
		executeQuery(sqlString, session);
		
		sqlString="ALTER TABLE CATISSUE_AUDIT_EVENT DISABLE CONSTRAINT FKACAF697A2206F20F";
		executeQuery(sqlString, session);
	}
	
	private void enableAuditTablesOracleConstraints(Session session) throws SQLException {
		String sqlString="ALTER TABLE catissue_audit_event_details ENABLE CONSTRAINT FK5C07745D34FFD77F";
		executeQuery(sqlString, session);

		sqlString="ALTER TABLE catissue_audit_event_query_log ENABLE CONSTRAINT FK62DC439DBC7298A9";
		executeQuery(sqlString, session);
		
		sqlString="ALTER TABLE CATISSUE_AUDIT_EVENT_LOG ENABLE CONSTRAINT FK8BB672DF77F0B904";
		executeQuery(sqlString, session);
		
		sqlString="ALTER TABLE CATISSUE_AUDIT_EVENT ENABLE CONSTRAINT FKACAF697A2206F20F";
		executeQuery(sqlString, session);
	}
	
	/**
	 * Disable audit related tables constraints if MsSQLServer DB.
	 * To disable all constraints - ALTER TABLE <Table_Name> NOCHECK CONSTRAINT ALL
	 *  
	 * @param session
	 * @exception SQLException
	 */
	private void disableAuditTablesMsSqlServerConstraints(Session session) throws SQLException {
		String sqlString="ALTER TABLE catissue_audit_event_details NOCHECK CONSTRAINT ALL";
		executeQuery(sqlString, session);

		sqlString="ALTER TABLE catissue_audit_event_query_log NOCHECK CONSTRAINT ALL";
		executeQuery(sqlString, session);
		
		sqlString="ALTER TABLE CATISSUE_AUDIT_EVENT_LOG NOCHECK CONSTRAINT ALL";
		executeQuery(sqlString, session);
		
		sqlString="ALTER TABLE CATISSUE_AUDIT_EVENT NOCHECK CONSTRAINT ALL";
		executeQuery(sqlString, session);
	}
	
	/**
	 * Enable audit related tables constraints if MsSQLServer DB.
	 * To enable all constraints - ALTER TABLE <Table_Name> CHECK CONSTRAINT ALL
	 *  
	 * @param session
	 * @exception SQLException
	 */
	private void enableAuditTablesMsSqlServerConstraints(Session session) throws SQLException {
		String sqlString="ALTER TABLE catissue_audit_event_details CHECK CONSTRAINT ALL";
		executeQuery(sqlString, session);

		sqlString="ALTER TABLE catissue_audit_event_query_log CHECK CONSTRAINT ALL";
		executeQuery(sqlString, session);
		
		sqlString="ALTER TABLE CATISSUE_AUDIT_EVENT_LOG CHECK CONSTRAINT ALL";
		executeQuery(sqlString, session);
		
		sqlString="ALTER TABLE CATISSUE_AUDIT_EVENT CHECK CONSTRAINT ALL";
		executeQuery(sqlString, session);
	}
	
	/**
	 * updated code for derived attributes
	 * @param entity
	 * @param tag
	 * @return
	 */
	private boolean isTagPresent(AbstractMetadataInterface entity, String tag)
	{
		boolean isTagPresent = false;
		Collection<TaggedValueInterface> taggedValueCollection = entity.getTaggedValueCollection();
		//System.out.println(taggedValueCollection.size());
		for (TaggedValueInterface tagValue : taggedValueCollection)
		{
			//System.out.println(tagValue.getKey());
			if (tagValue.getKey().equalsIgnoreCase(tag))
			{
				isTagPresent = true;
				break;
			}
		}		
		//System.out.println("\nisTagPresent:"+isTagPresent);
		
		return isTagPresent;
	}
}
