
package edu.wustl.catissuecore.querysuite.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;

/**
 * This class adds curated path between the Participant, Specimen, SCG and the DE forms.
 * @author deepali_ahirrao
 *
 */
public class AddCuratedPathsForRecordEntry
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}

	private static final Logger LOGGER = Logger
			.getCommonLogger(AddCuratedPathsForRecordEntry.class);

	private static Long participantREEntityId = null;
	private static Long specimenREEntityId = null;
	private static Long scgREEntityId = null;
	private static Long participantEntityId = null;
	private static Long specimenEntityId = null;
	private static Long scgEntityId = null;
	private static EntityManagerInterface entityManager = null;

	/**
	 * @param args
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws ApplicationException
	 */
	public static void main(String[] args) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, ApplicationException
	{
		AddCuratedPathsForRecordEntry addPaths = new AddCuratedPathsForRecordEntry();
		addPaths.init();
		addPaths.addCuratedPath();
		LOGGER.info("Added curated paths successfully----------------");
	}

	/**
	 * Method to add curated paths
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws ApplicationException
	 */
	private void addCuratedPath() throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, ApplicationException
	{
		Collection<NameValueBean> entityGrpLst = entityManager.getAllEntityGroupBeans();
		Iterator<NameValueBean> iterator = entityGrpLst.iterator();

		JDBCDAO jdbcDao = AppUtility.openJDBCSession();

		Integer maxId = getMaxPathId(jdbcDao);

		while (iterator.hasNext())
		{
			NameValueBean groupBean = iterator.next();
			if (groupBean != null)
			{
				LOGGER.info("Setting path for entity group==" + groupBean.getValue() + "=="
						+ groupBean.getName());
			}

			Long entityGroupId = Long.valueOf(groupBean.getValue());
			LOGGER.info("entityGroupId : " + entityGroupId);
			List<Long> newEntitiesIds = this.getAllEntitiesForEntityGroup(entityGroupId);
			LOGGER.info("newEntitiesIds : " + newEntitiesIds);
			String hookEntity = this.getHookEntity(entityGroupId, jdbcDao);
			Long hookEntityId = getHookEntityId(hookEntity);
			// Participant/Specimen/SCG entity ID
			Long assoEntityId = getAssociatedEntityId(hookEntityId);

			String intPath = getIntermediatePath(assoEntityId, hookEntityId, jdbcDao);
			LOGGER.info("intPath : " + intPath);
			for (Long entityId : newEntitiesIds)
			{
				LOGGER.info("Checking intermediate path for entity : " + entityId);
				String oldIntPath = getIntermediatePath(assoEntityId, entityId, jdbcDao);
				LOGGER.info("oldIntPath : " + oldIntPath);
				updateOldPath(assoEntityId, oldIntPath, intPath, jdbcDao);

				maxId = maxId + 1;
				addPathForHookEntityAndDE(hookEntityId, entityId, oldIntPath, maxId, jdbcDao);
			}
			jdbcDao.commit();
		}

		AppUtility.closeDAOSession(jdbcDao);
	}

	/**
	 *
	 * @param jdbcDao
	 * @return
	 * @throws DAOException
	 */
	private Integer getMaxPathId(JDBCDAO jdbcDao) throws DAOException
	{
		String sql = "select max(PATH_ID) from path";
		List list = jdbcDao.executeQuery(sql);
		List objs = (List) list.get(0);
		return Integer.valueOf(objs.get(0).toString());
	}

	/**
	 *
	 * @param hookEntityId
	 * @return
	 */
	private Long getAssociatedEntityId(Long hookEntityId)
	{
		Long assoEntityId = null;

		if (participantREEntityId.equals(hookEntityId))
		{
			assoEntityId = participantEntityId;
		}
		else if (specimenREEntityId.equals(hookEntityId))
		{
			assoEntityId = specimenEntityId;
		}
		else if (scgREEntityId.equals(hookEntityId))
		{
			assoEntityId = scgEntityId;
		}
		return assoEntityId;
	}

	/**
	 * Method to update curated paths which existed between old hook entities (Participant,
	 * Specimen, SCG) and DE forms
	 * @param firstEntityId
	 * @param oldIntPath
	 * @param newIntPath
	 * @param jdbcDao
	 * @throws DAOException
	 */
	private void updateOldPath(Long firstEntityId, String oldIntPath, String newIntPath,
			JDBCDAO jdbcDao) throws DAOException
	{
		String sql = "select path_id,intermediate_path from path where first_entity_id="
				+ firstEntityId + " and intermediate_path like '" + oldIntPath + "%'";

		List list = jdbcDao.executeQuery(sql);
		if (list != null && !list.isEmpty())
		{
			for (Object obj : list)
			{
				List objs = (List) obj;
				if (objs != null && !objs.isEmpty())
				{
					String pathId = objs.get(0).toString();
					String intermediatePath = objs.get(1).toString();
					if (intermediatePath != null)
					{
						LOGGER.info("Got intermediatePath : " + intermediatePath + "  pathId : "
								+ pathId);
						String newPath = newIntPath + "_" + intermediatePath;
						sql = "update path set intermediate_path='" + newPath + "' where path_id="
								+ pathId;

						jdbcDao.executeUpdate(sql);
					}
				}
			}
		}
	}

	/**
	 *
	 * @param hookEntityName
	 * @return
	 */
	private Long getHookEntityId(String hookEntityName)
	{
		Long hookEntityId = null;

		if (AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY.equals(hookEntityName))
		{
			hookEntityId = participantREEntityId;
		}
		else if (AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY.equals(hookEntityName))
		{
			hookEntityId = specimenREEntityId;
		}
		else if (AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY.equals(hookEntityName))
		{
			hookEntityId = scgREEntityId;
		}

		return hookEntityId;
	}

	/**
	 * Add curated path
	 * @param firstEntityId
	 * @param lastEntityId
	 * @param intermediatePath
	 * @param maxId
	 * @param jdbcDao
	 * @throws DAOException
	 */
	private void addPathForHookEntityAndDE(Long firstEntityId, Long lastEntityId,
			String intermediatePath, Integer maxId, JDBCDAO jdbcDao) throws DAOException
	{
		String sql = "insert into path (PATH_ID,FIRST_ENTITY_ID,INTERMEDIATE_PATH,LAST_ENTITY_ID)"
				+ " values (" + maxId + "," + firstEntityId + ",'" + intermediatePath + "',"
				+ lastEntityId + ")";

		jdbcDao.executeUpdate(sql);
	}

	/**
	 * Method returns list of entity IDs for main containers in a Entity Group
	 * @param entityGroupId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private List<Long> getAllEntitiesForEntityGroup(Long entityGroupId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		List<Long> newEntitiesIds = new ArrayList<Long>();

		// Get main containers
		Collection<NameValueBean> mainContainers = entityManager.getMainContainer(entityGroupId);
		Iterator<NameValueBean> iterator = mainContainers.iterator();

		while (iterator.hasNext())
		{
			NameValueBean groupBean = iterator.next();
			// Get entity ID
			Long entityId = entityManager.getEntityIdByContainerId(Long.valueOf(groupBean
					.getValue()));
			newEntitiesIds.add(entityId);
		}

		return newEntitiesIds;
	}

	/**
	 * Fetch the entity IDs
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private void init() throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		entityManager = EntityManager.getInstance();
		participantEntityId = entityManager
				.getEntityId(AnnotationConstants.ENTITY_NAME_PARTICIPANT);
		participantREEntityId = entityManager
				.getEntityId(AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY);

		LOGGER.info("participantREEntityId : " + participantREEntityId);

		specimenEntityId = entityManager.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN);
		specimenREEntityId = entityManager
				.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY);
		LOGGER.info("specimenREEntityId : " + specimenREEntityId);

		scgEntityId = entityManager
				.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_COLLN_GROUP);
		scgREEntityId = entityManager.getEntityId(AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY);

		LOGGER.info("scgREEntityId : " + scgREEntityId);
	}

	/**
	 * Get hook entity for Entity Group
	 * @param entityGroupId
	 * @param jdbcDao
	 * @return
	 * @throws ApplicationException
	 */
	private String getHookEntity(Long entityGroupId, JDBCDAO jdbcDao) throws ApplicationException
	{
		String sql = "select distinct meta.name from DYEXTN_ASSOCIATION asso,DYEXTN_ENTITY de, "
				+ "DYEXTN_ATTRIBUTE attr, DYEXTN_ABSTRACT_METADATA meta where "
				+ " asso.IDENTIFIER=attr.IDENTIFIER and de.IDENTIFIER=asso.TARGET_ENTITY_ID "
				+ " and meta.IDENTIFIER = attr.ENTIY_ID and de.ENTITY_GROUP_ID=" + entityGroupId
				+ " and attr.ENTIY_ID in (" + participantREEntityId + "," + specimenREEntityId
				+ "," + scgREEntityId + ")";

		List list = jdbcDao.executeQuery(sql);
		List objs = (List) list.get(0);
		String hookEntity = objs.get(0).toString();

		LOGGER.info("hookEntity : " + hookEntity);
		return hookEntity;
	}

	/**
	 *
	 * @param firstEntityId
	 * @param lastEntityId
	 * @param jdbcDao
	 * @return
	 * @throws ApplicationException
	 */
	private String getIntermediatePath(Long firstEntityId, Long lastEntityId, JDBCDAO jdbcDao)
			throws ApplicationException
	{
		String sql = "select intermediate_path from path where first_entity_id=" + firstEntityId
				+ " and last_entity_id=" + lastEntityId;

		List list = jdbcDao.executeQuery(sql);
		List objs = (List) list.get(0);
		String intermediatePath = objs.get(0).toString();

		return intermediatePath;
	}

}
