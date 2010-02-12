
package edu.wustl.catissuecore.querysuite.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.xmi.UpdateCSRToEntityPath;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.JDBCDAO;

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

	private static List<AssociationInterface> partAssociationList = null;
	private static List<AssociationInterface> specAssociationList = null;
	private static List<AssociationInterface> scgAssociationList = null;

	private static Long participantREEntityId = null;
	private static Long specimenREEntityId = null;
	private static Long scgREEntityId = null;

	/**
	 * @param args
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws ApplicationException
	 */
	public static void main(String[] args) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, NumberFormatException, ApplicationException
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
		EntityManagerInterface entityManager = EntityManager.getInstance();
		Collection<NameValueBean> entityGrpLst = entityManager.getAllEntityGroupBeans();
		Iterator<NameValueBean> iterator = entityGrpLst.iterator();

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
			String hookEntity = this.getHookEntity(entityGroupId);
			addCuratedPathForHookEntity(hookEntity, newEntitiesIds);
		}
	}

	/**
	 *
	 * @param hookEntity
	 * @param newEntitiesIds
	 */
	private void addCuratedPathForHookEntity(String hookEntity, List<Long> newEntitiesIds)
	{
		if (AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY.equals(hookEntity))
		{
			UpdateCSRToEntityPath.addCuratedPathsFromToAllEntities(partAssociationList,
					newEntitiesIds);
		}
		else if (AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY.equals(hookEntity))
		{
			UpdateCSRToEntityPath.addCuratedPathsFromToAllEntities(specAssociationList,
					newEntitiesIds);
		}
		else if (AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY.equals(hookEntity))
		{
			UpdateCSRToEntityPath.addCuratedPathsFromToAllEntities(scgAssociationList,
					newEntitiesIds);
		}
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
		EntityManagerInterface entityManager = EntityManager.getInstance();
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
	 * Fetch the association lists and the entity IDs
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private void init() throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		long staticEntityId = entityManager
				.getEntityId(AnnotationConstants.ENTITY_NAME_PARTICIPANT);
		participantREEntityId = entityManager
				.getEntityId(AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY);

		LOGGER.info("participantREEntityId : " + participantREEntityId);

		partAssociationList = this.getAssociationListForCurratedPath(staticEntityId,
				participantREEntityId);

		staticEntityId = entityManager.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN);
		specimenREEntityId = entityManager
				.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY);
		LOGGER.info("specimenREEntityId : " + specimenREEntityId);

		specAssociationList = this.getAssociationListForCurratedPath(staticEntityId,
				specimenREEntityId);

		staticEntityId = entityManager
				.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_COLLN_GROUP);
		scgREEntityId = entityManager.getEntityId(AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY);

		LOGGER.info("scgREEntityId : " + scgREEntityId);
		scgAssociationList = this.getAssociationListForCurratedPath(staticEntityId, scgREEntityId);
	}

	/**
	 * Method returns the association between static entity and hook entity
	 * @param srcEntityId
	 * @param tgtEntityId
	 * @param targetRoleName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private AssociationInterface getAssociationByTargetRole(Long srcEntityId, Long tgtEntityId,
			String targetRoleName) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		AssociationInterface deAsssociation = null;
		EntityManagerInterface entityManager = EntityManager.getInstance();
		Collection<AssociationInterface> associations = entityManager.getAssociations(srcEntityId,
				tgtEntityId);

		for (AssociationInterface association : associations)
		{
			if (association.getTargetRole().getName().equalsIgnoreCase(targetRoleName))
			{
				deAsssociation = association;
				break;
			}
		}
		return deAsssociation;
	}

	/**
	 * Return the association list
	 * @param staticEntityId
	 * @param hookEntityId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private List<AssociationInterface> getAssociationListForCurratedPath(Long staticEntityId,
			Long hookEntityId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		AssociationInterface recordEntryAssociation = getAssociationByTargetRole(staticEntityId,
				hookEntityId, "recordEntryCollection");
		if (recordEntryAssociation == null)
		{
			throw new DynamicExtensionsApplicationException(
					"Associations For Currated Path Not Found");
		}

		List<AssociationInterface> associationList = new ArrayList<AssociationInterface>();
		associationList.add(recordEntryAssociation);
		return associationList;
	}

	/**
	 * Get hook entity for Entity Group
	 * @param entityGroupId
	 * @return
	 * @throws ApplicationException
	 */
	private String getHookEntity(Long entityGroupId) throws ApplicationException
	{
		JDBCDAO jdbcDao = AppUtility.openJDBCSession();

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
		AppUtility.closeDAOSession(jdbcDao);

		return hookEntity;
	}

}
