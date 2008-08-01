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
                	if(attribute.getIsIdentified()!=null && attribute.getIsIdentified()==true && attribute.getAttributeTypeInformation().getDataType().equalsIgnoreCase("String"))
                	{
      					maskString(attribute.getColumnProperties().getName(),entity.getTableProperties().getName(), session);
					}
					else if(attribute.getAttributeTypeInformation().getDataType().equalsIgnoreCase("Date"))
					{
						maskDate(attribute.getColumnProperties().getName(),entity.getTableProperties().getName(), session);
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
			
			maskReportText(session);
			
			tx.commit();
			session.close();
		}
		catch (Exception e) 
		{
			System.out.println(e);
		}
	}

	private void maskDate(String columnName, String tableName, Session session) throws SQLException
	{
		String sqlString=null;
		String dbType=session.connection().getMetaData().getDatabaseProductName();

		if(dbType.equalsIgnoreCase("oracle"))
		{
			sqlString="update "+tableName+" set "+columnName+"=add_months("+columnName+", -"+randomNumber+")";
		}
		if(dbType.equalsIgnoreCase("mysql"))
		{
			sqlString="update "+tableName+" set "+columnName+"=date_add("+columnName+", INTERVAL -"+randomNumber+" MONTH);";
		}
		executeQuery(sqlString, session);
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

		if(dbType.equalsIgnoreCase("oracle"))
		{
			sqlString="update catissue_report_content set report_data=NULL where identifier in(select a.identifier from catissue_report_content a join catissue_report_textcontent b on a.identifier=b.identifier join catissue_pathology_report c on c.identifier=b.report_id where c.REPORT_STATUS in ('DEIDENTIFIED','DEID_PROCESS_FAILED','PENDING_FOR_DEID'))";
		}
		if(dbType.equalsIgnoreCase("mysql"))
		{
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
}
