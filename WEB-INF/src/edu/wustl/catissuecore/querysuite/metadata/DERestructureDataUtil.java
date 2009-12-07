
package edu.wustl.catissuecore.querysuite.metadata;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

public class DERestructureDataUtil
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(DERestructureDataUtil.class);

	private static Connection connection = null;
	private static String participantEntityId = "844";
	private static String specimenEntityId = "4";
	private static String scgEntityId = "379";
	private static Map<String, List<Long>> staticEntityContainerIdMap = new HashMap<String, List<Long>>();
	private static Map<String, Long> recordEntryEntityMap = new HashMap<String, Long>();

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws DynamicExtensionsSystemException
	 */
	public static void main(String[] args) throws SQLException, ClassNotFoundException,
			DynamicExtensionsSystemException
	{
		AddDEIntegrationMetadata.configureDBConnection(args);
		connection = AddDEIntegrationMetadata.getDBConnection();

		restructureData();
		connection.close();
		logger.info("------DONE--------");
	}

	/**
	 *
	 * @throws DynamicExtensionsSystemException
	 * @throws SQLException
	 */
	private static void restructureData() throws DynamicExtensionsSystemException, SQLException
	{

		// Get Containers for Participant/Specimen/SCG
		// Check for entity and category
		// Get entity Ids for containers
		// Get DB tables for containers (DE_E) and get their foreign keys
		// For each DE table record insert record in Record Entry and change the foreign key

		EntityManagerInterface entityManager = EntityManager.getInstance();
		/*String sql = "select IDENTIFIER,ABSTRACT_ENTITY_ID from DYEXTN_CONTAINER where IDENTIFIER in " +
		"(select container_id from dyextn_entity_map where static_entity_id=844)";*/

		// get record entry entity id for static entity
		populateRecordEntryEntityMap();
		// get list of all containerIds for a static entity
		populateStaticEntityContainerIDsMap();
		Iterator<String> iterator = staticEntityContainerIdMap.keySet().iterator();
		Long recordEntryId = Long.valueOf(1);


		while (iterator.hasNext())
		{
			String staticEntityId = iterator.next();
			String staticEntityDetails = getStaticEntityTableName(Long.valueOf(staticEntityId));
			String[] details = staticEntityDetails.split(":");
			String tableName = details[0];
			String foreignKey = details[1];
			List<Long> containerIDs = staticEntityContainerIdMap.get(staticEntityId);
			for (Long contId : containerIDs)
			{
				Long abstractFormContextId = getAbstractFormContextId(contId);
				Long categoryEntityId = entityManager.isCategory(contId);

				// Not a category
				Long containerId = contId;
				if (categoryEntityId != null)
				{
					containerId = entityManager.getCategoryRootContainerId(contId);
				}
				Long entityId = entityManager.getEntityIdByContainerId(Long.valueOf(containerId));
				String deTableName = entityManager.getDynamicTableName(containerId);
				System.out.println("------------------------" + recordEntryEntityMap.get(staticEntityId));
				String foreignKeyName = getforeignKeyName(recordEntryEntityMap.get(staticEntityId),
						entityId);

				// Get DE table IDs
				String sql = "select rec.DYNAMIC_ENTITY_RECORD_ID,rec.STATIC_ENTITY_RECORD_ID,rec.CREATED_DATE,rec.CREATED_BY "
						+ "from dyextn_entity_map_record rec,dyextn_form_context fc,dyextn_entity_map em where "
						+ " rec.FORM_CONTEXT_ID = fc.identifier and em.identifier=fc.entity_map_id and em.container_id="
						+ contId + " order by rec.DYNAMIC_ENTITY_RECORD_ID";

				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql);
				while (resultSet.next())
				{
					Long deRecordId = resultSet.getLong(1);
					Long staticEntityRecordId = resultSet.getLong(2);
					Date modifiedDate = resultSet.getDate(3);

					String formaatedDate = getFormattedDateFunction(modifiedDate);
					String createdBy = resultSet.getString(4);
					sql = "insert into dyextn_abstract_record_entry (IDENTIFIER,MODIFIED_DATE,MODIFIED_BY,ACTIVITY_STATUS,ABSTRACT_FORM_CONTEXT_ID) "
							+ "values ("
							+ recordEntryId
							+ ","
							+ formaatedDate
							+ ",'"
							+ createdBy
							+ "','Active'," + abstractFormContextId + ")";

					System.out.println("Insert SQL : " + sql);

					Statement statement1 = connection.createStatement();
					statement1.executeUpdate(sql);

					sql = "insert into " + tableName + "(identifier," + foreignKey + ") values ("
							+ recordEntryId + "," + staticEntityRecordId + ")";
					System.out.println("Insert SQL : " + sql);

					statement1.close();
					statement1 = connection.createStatement();
					statement1.executeUpdate(sql);

					sql = "update " + deTableName + " set " + foreignKeyName + "=" + recordEntryId
							+ " where identifier = " + deRecordId;
					System.out.println("Update SQL : " + sql);

					statement1.close();
					statement1 = connection.createStatement();
					statement1.executeUpdate(sql);
					statement1.close();

					recordEntryId ++;
				}
				resultSet.close();
				statement.close();
			}

		}


	}


	private static String getFormattedDateFunction(Date date)
	{
		DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
		String formattedDate = format.format(date);

		if("mysql".equals(AddDEIntegrationMetadata.DATABASE_TYPE))
		{
			formattedDate = "STR_TO_DATE('" + formattedDate + "','%m-%d-%Y')";
		}
		else if("oracle".equals(AddDEIntegrationMetadata.DATABASE_TYPE))
		{
			formattedDate = "TO_DATE('" + formattedDate + "','mm-dd-yyyy')";
		}
		else
		{
			System.out.println("WRONG DB SPECIFIED-----------");
		}

		return formattedDate;
	}

	/**
	 *
	 * @param staticEntityId
	 * @return
	 */
	private static String getStaticEntityTableName(Long staticEntityId)
	{

		String tableName = null;
		if (participantEntityId.equals(staticEntityId))
		{
			tableName = "catissue_participant_rec_ntry:participant_id";
		}
		else if (specimenEntityId.equals(staticEntityId))
		{
			tableName = "catissue_specimen_rec_ntry:specimen_id";
		}
		else
		{
			tableName = "catissue_scg_rec_ntry:specimen_collection_group_id";
		}
		return tableName;
	}

	/**
	 *
	 * @param staticEntityId
	 * @param entityId
	 * @return
	 * @throws SQLException
	 */
	private static String getforeignKeyName(Long staticEntityId, Long entityId) throws SQLException
	{
		String sql = "select dbP.name from DYEXTN_DATABASE_PROPERTIES dbP, DYEXTN_COLUMN_PROPERTIES colP, "
				+ "DYEXTN_CONSTRAINTKEY_PROP ckP, dyextn_constraint_properties conP, dyextn_association da, "
				+ "dyextn_attribute dt where dbP.identifier=colP.identifier and colP.CNSTR_KEY_PROP_ID=ckP.identifier and "
				+ "ckP.TGT_CONSTRAINT_KEY_ID=conP.identifier and conP.association_id=da.identifier "
				+ " and da.identifier=dt.identifier and da.target_entity_id= "
				+ entityId
				+ " and dt.entiy_id= " + staticEntityId;

		System.out.println("Foreign Key SQL : " + sql);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		String foreignKeyColumn = null;
		while (resultSet.next())
		{
			foreignKeyColumn = resultSet.getString(1);
		}

		resultSet.close();
		statement.close();

		return foreignKeyColumn;
	}

	/**
	 *
	 * @throws SQLException
	 */
	private static void populateStaticEntityContainerIDsMap() throws SQLException
	{
		String sql = "select container_id,static_entity_id from dyextn_entity_map";

		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);

		List<Long> participantContainerIDs = new ArrayList<Long>();
		List<Long> specimenContainerIDs = new ArrayList<Long>();
		List<Long> scgContainerIDs = new ArrayList<Long>();
		staticEntityContainerIdMap.put(participantEntityId, participantContainerIDs);
		staticEntityContainerIdMap.put(specimenEntityId, specimenContainerIDs);
		staticEntityContainerIdMap.put(scgEntityId, scgContainerIDs);
		while (resultSet.next())
		{
			Long containerId = resultSet.getLong(1);
			Long staticEntityId = resultSet.getLong(2);

			if (participantEntityId.equals(staticEntityId))
			{
				participantContainerIDs.add(containerId);
			}
			else if (specimenEntityId.equals(staticEntityId))
			{
				specimenContainerIDs.add(containerId);
			}
			else
			{
				scgContainerIDs.add(containerId);
			}
		}

		resultSet.close();
		statement.close();

	}

	/**
	 *
	 * @throws SQLException
	 */
	private static void populateRecordEntryEntityMap() throws SQLException
	{
		String sql = "select identifier,name from dyextn_abstract_metadata where name in "
				+ "('edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry',"
				+ "'edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry',"
				+ "'edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry')";

		System.out.println(" SQL : " + sql);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next())
		{

			Long entityId = resultSet.getLong(1);
			String entityName = resultSet.getString(2);
			System.out.println("entityName : " + entityName);
			if ("edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry"
					.equals(entityName))
			{
				recordEntryEntityMap.put(participantEntityId, entityId);
				System.out.println("entityId : " + entityId);
			}
			else if ("edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry"
					.equals(entityName))
			{
				recordEntryEntityMap.put(specimenEntityId, entityId);
				System.out.println("entityId : " + entityId);
			}
			else
			{
				recordEntryEntityMap.put(scgEntityId, entityId);
				System.out.println("entityId : " + entityId);
			}
		}
		resultSet.close();
		statement.close();

	}

	/**
	 *
	 * @param containerId
	 * @return
	 * @throws SQLException
	 */
	private static Long getAbstractFormContextId(Long containerId) throws SQLException
	{
		String sql = "select identifier from dyextn_abstract_form_context where CONTAINER_ID="
				+ containerId;
		System.out.println(" SQL : " + sql);
		Statement statement1 = connection.createStatement();
		ResultSet resultSet1 = statement1.executeQuery(sql);
		Long abstractFormContextId = null;
		while (resultSet1.next())
		{
			abstractFormContextId = resultSet1.getLong(1);
		}
		resultSet1.close();
		statement1.close();
		return abstractFormContextId;
	}

}
