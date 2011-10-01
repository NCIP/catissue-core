
package edu.wustl.catissuecore.annotations.xmi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.xmi.XMIConfiguration;
import edu.common.dynamicextensions.xmi.importer.AbstractXMIImporter;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.StudyFormContext;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

/**
 * @author ashish_gupta
 *
 */
public class ImportXmi extends AbstractXMIImporter
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	private static Logger logger = Logger.getCommonLogger(ImportXmi.class);

	private static String staticEntity;
	private static String hookEntity;

	/**
	 * main method
	 * @param args command line arguments
	 */
	public static void main(String[] args)
	{
		ImportXmi xmiImporter = new ImportXmi();

		if (args.length >= 4)
		{
			if (AnnotationConstants.ENTITY_NAME_PARTICIPANT.equals(args[3]))
			{
				args[3] = AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY;
				staticEntity = AnnotationConstants.ENTITY_NAME_PARTICIPANT;
				hookEntity = AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY;
			}
			else if (AnnotationConstants.ENTITY_NAME_SPECIMEN.equals(args[3]))
			{
				args[3] = AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY;
				staticEntity = AnnotationConstants.ENTITY_NAME_SPECIMEN;
				hookEntity = AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY;
			}
			else if (AnnotationConstants.ENTITY_NAME_SPECIMEN_COLLN_GROUP.equals(args[3]))
			{
				args[3] = AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY;
				staticEntity = AnnotationConstants.ENTITY_NAME_SPECIMEN_COLLN_GROUP;
				hookEntity = AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY;
			}
			else if (AnnotationConstants.SOP_HOOKING_ENTITY.equals(args[3]))
			{
				args[3] = AnnotationConstants.ENTITY_NAME_ACTION_APP_REC_ENTRY;
				staticEntity = AnnotationConstants.SOP_HOOKING_ENTITY;
				hookEntity = AnnotationConstants.ENTITY_NAME_ACTION_APP_REC_ENTRY;
			}
		}
		xmiImporter.importXMI(args);
	}

	/**
	 * @param isEntityGroupSystemGenerated true if entity group is system generated
	 * @return XMIConfiguration object.
	 */
	protected XMIConfiguration getXMIConfigurationObject()
	{
		final XMIConfiguration xmiConfiguration = XMIConfiguration.getInstance();
		xmiConfiguration.setEntityGroupSystemGenerated(false);
		xmiConfiguration.setDefaultPackagePrefix("");
		xmiConfiguration.setCreateTable(true);
		xmiConfiguration.setAddIdAttr(true);
		xmiConfiguration.setAddColumnForInherianceInChild(true);
		xmiConfiguration.setAddInheritedAttribute(true);
		// Not to validate XMI for caCore errors
		xmiConfiguration.setValidateXMI(true);
		return xmiConfiguration;
	}

	/**
	 * @see edu.common.dynamicextensions.xmi.importer.AbstractXMIImporter#getAssociationListForCurratedPath(edu.wustl.dao.HibernateDAO)
	 * @param hibernatedao
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	@Override
	protected List<AssociationInterface> getAssociationListForCurratedPath(HibernateDAO hibernatedao)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		List<AssociationInterface> associationList = new ArrayList<AssociationInterface>();
		EntityManagerInterface entityManager = EntityManager.getInstance();

		long staticEntityId = entityManager.getEntityId(staticEntity);
		long hookEntityId = entityManager.getEntityId(hookEntity);
		AssociationInterface recordEntryAssociation = getAssociationByTargetRole(staticEntityId,
				hookEntityId, "recordEntryCollection", hibernatedao);
		if (recordEntryAssociation == null)
		{
			throw new DynamicExtensionsApplicationException(
					"Associations For Currated Path Not Found");
		}
		associationList.add(recordEntryAssociation);
		return associationList;
	}

	/**
	 * It will search the DE association whose source & target entity is as given in parameters &
	 * target role of the association also as given. else will return null
	 * @param srcEntityId source entity id of association
	 * @param tgtEntityId target entity id of association
	 * @param targetRoleName target role of association
	 * @param hibernatedao dao used for retrieving the association.
	 * @return the found association
	 * @throws DynamicExtensionsSystemException exception
	 * @throws DynamicExtensionsApplicationException exception
	 */
	private static AssociationInterface getAssociationByTargetRole(Long srcEntityId,
			Long tgtEntityId, String targetRoleName, HibernateDAO hibernatedao)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		AssociationInterface deAsssociation = null;
		Collection<AssociationInterface> associations = null;
		EntityManagerInterface entityManager = EntityManager.getInstance();

		if (hibernatedao == null)
		{
			associations = entityManager.getAssociations(srcEntityId, tgtEntityId);
		}
		else
		{
			associations = entityManager.getAssociations(srcEntityId, tgtEntityId, hibernatedao);
		}

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
	 *
	 * @see edu.common.dynamicextensions.xmi.importer.AbstractXMIImporter#postProcess(boolean, java.lang.String, java.util.List, java.lang.String)
	 * @param isEditedXmi specifies weather the Edit XMI case or not.
	 * @param coRecObjCsvFName name of the file
	 * @param mainContainerList main container list.
	 * @param domainModelName domain model name.
	 * @throws BizLogicException exception.
	 * @throws DAOException exception.
	 * @throws DynamicExtensionsApplicationException exception.
	 */
	@Override
	protected void postProcess(boolean isEditedXmi, String coRecObjCsvFName,
			List<ContainerInterface> mainContainerList, String domainModelName)
			throws BizLogicException, DAOException, DynamicExtensionsApplicationException
	{
		final AnnotationBizLogic annotation = new AnnotationBizLogic();

		List<CollectionProtocol> conditionObjectCPs;
		try
		{
			conditionObjectCPs = getConditionCPs(coRecObjCsvFName);
		}
		catch (Exception e)
		{
			logger.error("Exception while getting CollectionProtocols. Main containers "
					+ "will not be associated with CollectionProtocols.", e);
			conditionObjectCPs = new ArrayList<CollectionProtocol>();
		}

		Set<CollectionProtocol> cpSet = new HashSet<CollectionProtocol>();
		cpSet.addAll(conditionObjectCPs);

		for (ContainerInterface container : mainContainerList)
		{
			List<StudyFormContext> studyFormList = annotation.executeQuery("from "
					+ StudyFormContext.class.getName() + " sfc where sfc.containerId="
					+ container.getId());

			StudyFormContext sfc = null;

			if (studyFormList == null || studyFormList.isEmpty())
			{
				InstanceFactory<StudyFormContext> studyFormInstFact = DomainInstanceFactory.getInstanceFactory(StudyFormContext.class);
				sfc = studyFormInstFact.createObject();//new StudyFormContext();
				sfc.setCollectionProtocolCollection(cpSet);
				sfc.setActivityStatus("Active");
				sfc.setHideForm(false);
				sfc.setContainerId(container.getId());
				annotation.insert(sfc);
			}
			else
			{
				if (!conditionObjectCPs.isEmpty())
				{
					sfc = studyFormList.get(0);
					/* TODO Need to verify requirement whether to update existing collection
					 * or add new collection of CPs. Currently updating existing collection.
					 */
					if(sfc.getCollectionProtocolCollection()==null)
					{
						sfc.setCollectionProtocolCollection(new HashSet<CollectionProtocol>());
					}
					sfc.getCollectionProtocolCollection().addAll(cpSet);
					annotation.update(sfc);
				}
			}
		}
	}

	/**
	 * Method returns the list of CollectonProtocols to be associated with main containers
	 * @param conditionRecordObjectCsvFileName
	 * @return
	 * @throws IOException
	 * @throws BizLogicException
	 * @throws DynamicExtensionsSystemException
	 */
	private List<CollectionProtocol> getConditionCPs(String conditionRecordObjectCsvFileName)
			throws IOException, BizLogicException, DynamicExtensionsSystemException
	{
		final AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
		final List<CollectionProtocol> conditionObjectCPs = new ArrayList<CollectionProtocol>();
		if (!conditionRecordObjectCsvFileName.equals(""))
		{
			final List<String> conditionObjectNames = readFile(conditionRecordObjectCsvFileName);
			for (final String conditionObjName : conditionObjectNames)
			{
				logger.info("conditionObjName = " + conditionObjName);

				List<CollectionProtocol> cpList = annotationBizLogic.executeQuery("from "
						+ CollectionProtocol.class.getName() + " cp where cp.title='"
						+ conditionObjName + "'");

				if (cpList != null && !cpList.isEmpty())
				{
					conditionObjectCPs.add(cpList.get(0));
				}
				else
				{
					throw new DynamicExtensionsSystemException(
							"Specified Collection Protocol does not exist: " + conditionObjName);
				}
			}
		}

		return conditionObjectCPs;
	}

}
