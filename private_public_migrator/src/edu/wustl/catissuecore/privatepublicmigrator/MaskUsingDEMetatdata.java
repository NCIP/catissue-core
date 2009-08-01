
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
		randomNumber = generator.nextInt(50);

		try
		{
			System.out.println("In maskIdentifiedData ");
			final EntityManagerInterface entityManager = EntityManager.getInstance();
			final Collection<EntityInterface> entities = entityManager.getAllEntities();
			final int totalNoOfEntities = entities.size();
			System.out.println("No Of entities:" + totalNoOfEntities);
			if (totalNoOfEntities == 0)
			{
				throw new Exception();
			}
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
						//	System.out.println("Name "+entity.getName());
						if (!entity.getName().contains("Deprecated"))
						{
							if (attribute.getIsIdentified() != null
									&& attribute.getIsIdentified() == true
									&& attribute.getAttributeTypeInformation().getDataType()
											.equalsIgnoreCase("String"))
							{
								this.maskString(attribute.getColumnProperties().getName(), entity
										.getTableProperties().getName(), session);
							}
							else if (attribute.getAttributeTypeInformation().getDataType()
									.equalsIgnoreCase("Date"))
							{
								this.maskDate(attribute.getColumnProperties().getName(), entity
										.getTableProperties().getName(), session);
							}
						}
					}
				}
			}

			// sql String to update ParticipantMedicalIdentifier table
			String sqlString = "truncate table CATISSUE_PART_MEDICAL_ID";
			this.executeQuery(sqlString, session);

			// sql String to delete  ReportQueue table
			sqlString = "truncate table CATISSUE_REPORT_QUEUE";
			this.executeQuery(sqlString, session);

			// sql String to delete ReportQueue table
			sqlString = "truncate table CATISSUE_REPORT_PARTICIP_REL";
			this.executeQuery(sqlString, session);

			this.maskReportText(session);

			tx.commit();
			session.close();
		}
		catch (final Throwable e)
		{
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
		final String dbType = session.connection().getMetaData().getDatabaseProductName();

		if (dbType.equalsIgnoreCase("oracle"))
		{
			sqlString = "update " + tableName + " set " + columnName + "=add_months(" + columnName
					+ ", " + randomNumber + ")";
		}
		if (dbType.equalsIgnoreCase("mysql"))
		{
			sqlString = "update " + tableName + " set " + columnName + "=date_add(" + columnName
					+ ", INTERVAL " + randomNumber + " MONTH);";
		}
		this.executeQuery(sqlString, session);
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

		if (dbType.equalsIgnoreCase("oracle"))
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
		catch (final Exception e)
		{
			System.out.println("Error in executeQuery ");
			throw e;
		}
	}

	/**
	 * updated code for derived attributes.
	 * @param entity AbstractMetadataInterface
	 * @param tag tag
	 * @return true or false
	 * @throws Exception generic Exception
	 */
	private boolean isTagPresent(AbstractMetadataInterface entity, String tag) throws Exception
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
