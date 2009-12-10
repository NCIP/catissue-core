
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
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

/**
 *
 * @author deepali_ahirrao
 *
 */
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
	 * @throws DynamicExtensionsApplicationException
	 */
	public static void main(String[] args) throws SQLException, ClassNotFoundException,
			DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		AddDEIntegrationMetadata.configureDBConnection(args);
		connection = AddDEIntegrationMetadata.getDBConnection();

		restructureData();
		connection.close();
		logger.info("------DONE--------");
	}

	/*
	 * LOGIC :
	 *  Get Containers for Participant/Specimen/SCG
	 *  Check for entity and category
	 *  Get entity Ids for containers
	 *  Get DB tables for containers (DE_E) and get their foreign keys
	 *  For each DE table record insert record in Record Entry and update the foreign key
	 *     of DE_E table and if has parent then update foreign key of the parent DE_E table
	 */
	/**
	 *
	 * @throws DynamicExtensionsSystemException
	 * @throws SQLException
	 * @throws DynamicExtensionsApplicationException
	 */
	private static void restructureData() throws DynamicExtensionsSystemException, SQLException,
			DynamicExtensionsApplicationException
	{
		final EntityManagerInterface entityManager = EntityManager.getInstance();

		// get record entry entity id for static entity
		populateRecordEntryEntityMap();
		logger.info("Populated record entry entity map --");
		// get list of all containerIds for a static entity
		populateStaticEntityContainerIDsMap();
		logger.info("Populated StaticEntity ContainerID map --");
		final Iterator<String> iterator = staticEntityContainerIdMap.keySet().iterator();
		Long recordEntryId = Long.valueOf(1);

		while (iterator.hasNext())
		{
			final String staticEntityId = iterator.next();
			final String staticEntityDetails = getStaticEntityTableName(Long
					.valueOf(staticEntityId));
			final String[] details = staticEntityDetails.split(":");
			final String tableName = details[0];
			final String foreignKey = details[1];
			final List<Long> containerIDs = staticEntityContainerIdMap.get(staticEntityId);
			logger.info("Record Entry entity ID------------------"
					+ recordEntryEntityMap.get(staticEntityId));
			logger.info("Record Entry Containers------------------" + containerIDs);
			for (Long contId : containerIDs)
			{
				logger.info("containerId : " + contId);
				Long abstractFormContextId = getAbstractFormContextId(contId);
				Long categoryEntityId = entityManager.isCategory(contId);

				// Not a category
				Long containerId = contId;
				if (categoryEntityId != null)
				{
					// get container ID of associated entity
					containerId = entityManager.getCategoryRootContainerId(contId);
				}
				Long entityId = entityManager.getEntityIdByContainerId(Long.valueOf(containerId));
				String deTableName = entityManager.getDynamicTableName(containerId);

				String foreignKeyName = getforeignKeyName(recordEntryEntityMap.get(staticEntityId),
						entityId);

				Long parentEntityId = getParentEntityId(entityId);
				String parentDeTableName = null;
				String parentEntityForeignKeyName = null;
				if (parentEntityId != null && parentEntityId != 0)
				{
					parentDeTableName = getDETableName(parentEntityId);
					logger.info("parentDeTableName : " + parentDeTableName);
					parentEntityForeignKeyName = getforeignKeyName(recordEntryEntityMap
							.get(staticEntityId), parentEntityId);
				}

				// Get DE table IDs
				String sql = "select rec.DYNAMIC_ENTITY_RECORD_ID,rec.STATIC_ENTITY_RECORD_ID,"
						+ "rec.CREATED_DATE,rec.CREATED_BY from dyextn_entity_map_record rec,"
						+ "dyextn_form_context fc,dyextn_entity_map em where "
						+ "rec.FORM_CONTEXT_ID = fc.identifier and em.identifier=fc.entity_map_id "
						+ "and em.container_id=" + contId
						+ " order by rec.DYNAMIC_ENTITY_RECORD_ID";

				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql);
				while (resultSet.next())
				{
					Long deRecordId = resultSet.getLong(1);
					Long staticEntityRecordId = resultSet.getLong(2);
					Date modifiedDate = resultSet.getDate(3);

					String formaatedDate = getFormattedDateFunction(modifiedDate);
					String createdBy = resultSet.getString(4);

					try
					{
						logger.info("deTableName:" + deTableName + " -- deRecordId:" + deRecordId
								+ " -- staticEntityRecordId:" + staticEntityRecordId
								+ " -- recordEntryId:" + recordEntryId);

						// Insert row in dyextn_abstract_record_entry table
						sql = "insert into dyextn_abstract_record_entry (IDENTIFIER,MODIFIED_DATE,"
								+ "MODIFIED_BY,ACTIVITY_STATUS,ABSTRACT_FORM_CONTEXT_ID) values ("
								+ recordEntryId + "," + formaatedDate + ",'" + createdBy
								+ "','Active'," + abstractFormContextId + ")";
						executeUpdate(sql);

						// Insert row in child table of dyextn_abstract_record_entry
						sql = "insert into " + tableName + "(identifier," + foreignKey
								+ ") values (" + recordEntryId + "," + staticEntityRecordId + ")";
						executeUpdate(sql);

						// update foreign key of DE_E table
						sql = "update " + deTableName + " set " + foreignKeyName + "="
								+ recordEntryId + " where identifier = " + deRecordId;
						executeUpdate(sql);

						// update foreign key of parent DE_E table, if exists
						if (parentEntityId != null && parentEntityId != 0)
						{
							sql = "update " + parentDeTableName + " set "
									+ parentEntityForeignKeyName + "=" + recordEntryId
									+ " where identifier = " + deRecordId;
							executeUpdate(sql);
						}
					}
					catch (Exception e)
					{
						logger.error("ERROR: deTableName:" + deTableName + " -- deRecordId:"
								+ deRecordId + " -- staticEntityRecordId:" + staticEntityRecordId
								+ " -- recordEntryId:" + recordEntryId);
					}

					recordEntryId++;
				}
				resultSet.close();
				statement.close();
			}
			logger.info("------------------------------------------------------");
		}

	}

	/**
	 *
	 * @param entityId
	 * @return
	 * @throws SQLException
	 */
	private static String getDETableName(Long entityId) throws SQLException
	{
		String deTableName = null;
		String sql = "select dp.NAME from DYEXTN_DATABASE_PROPERTIES dp,DYEXTN_TABLE_PROPERTIES tp"
				+ " where dp.IDENTIFIER=tp.IDENTIFIER and tp.ABSTRACT_ENTITY_ID=" + entityId;
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		if (resultSet.next())
		{
			deTableName = resultSet.getString(1);
		}
		return deTableName;
	}

	/**
	 *
	 * @param sql
	 * @throws SQLException
	 */
	private static void executeUpdate(String sql) throws SQLException
	{
		Statement statement = connection.createStatement();
		statement.executeUpdate(sql);
		statement.close();
	}

	/**
	 *
	 * @param entityId
	 * @return
	 * @throws SQLException
	 */
	private static Long getParentEntityId(Long entityId) throws SQLException

	{
		logger.info("Getting parent entity");
		Long parentEntityId = null;
		String sql = "select parent_entity_id from dyextn_entity where identifier=" + entityId;
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		if (resultSet.next())
		{
			parentEntityId = resultSet.getLong(1);
		}
		logger.info("parentEntityId : " + parentEntityId);
		return parentEntityId;
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	private static String getFormattedDateFunction(Date date)
	{
		DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
		String formattedDate = format.format(date);

		if ("mysql".equals(AddDEIntegrationMetadata.DATABASE_TYPE))
		{
			formattedDate = "STR_TO_DATE('" + formattedDate + "','%m-%d-%Y')";
		}
		else if ("oracle".equals(AddDEIntegrationMetadata.DATABASE_TYPE))
		{
			formattedDate = "TO_DATE('" + formattedDate + "','mm-dd-yyyy')";
		}
		else
		{
			logger.info("WRONG DB SPECIFIED-----------");
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
		if (participantEntityId.equals(staticEntityId.toString()))
		{
			tableName = "catissue_participant_rec_ntry:participant_id";
		}
		else if (specimenEntityId.equals(staticEntityId.toString()))
		{
			tableName = "catissue_specimen_rec_ntry:specimen_id";
		}
		else if (scgEntityId.equals(staticEntityId.toString()))
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
				//+ " and da.identifier=dt.identifier "
				+ "and da.target_entity_id= " + entityId + " and dt.entiy_id= " + staticEntityId;

		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		String foreignKeyColumn = null;
		if (resultSet.next())
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

			if (participantEntityId.equals(staticEntityId.toString()))
			{
				participantContainerIDs.add(containerId);
			}
			else if (specimenEntityId.equals(staticEntityId.toString()))
			{
				specimenContainerIDs.add(containerId);
			}
			else if (scgEntityId.equals(staticEntityId.toString()))
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

		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next())
		{
			Long entityId = resultSet.getLong(1);
			String entityName = resultSet.getString(2);
			if ("edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry"
					.equals(entityName))
			{
				recordEntryEntityMap.put(participantEntityId, entityId);
			}
			else if ("edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry"
					.equals(entityName))
			{
				recordEntryEntityMap.put(specimenEntityId, entityId);
			}
			else
			{
				recordEntryEntityMap.put(scgEntityId, entityId);
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
		logger.info(" SQL : " + sql);
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
