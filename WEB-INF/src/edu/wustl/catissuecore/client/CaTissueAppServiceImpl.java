
package edu.wustl.catissuecore.client;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.CategoryManagerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DataValueMapUtility;
import edu.common.dynamicextensions.validation.ValidatorUtil;
import edu.wustl.bulkoperator.appservice.AbstractBulkOperationAppService;
import edu.wustl.bulkoperator.metadata.HookingInformation;
import edu.wustl.bulkoperator.util.BulkOperationConstants;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.deintegration.DEIntegration;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

public class CaTissueAppServiceImpl extends AbstractBulkOperationAppService
{

	private CaCoreAppServicesDelegator appService;
	private String userName;
	/**
	 * logger.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(CaTissueAppServiceImpl.class);

	public CaTissueAppServiceImpl(boolean isAuthenticationRequired, String userName, String password)
			throws Exception
	{
		super(isAuthenticationRequired, userName, password);
	}

	@Override
	public void authenticate(String userName, String password) throws BulkOperationException
	{
		try
		{
			if (isAuthRequired && password != null)
			{
				if (!appService.delegateLogin(userName, password))
				{
					throw new BulkOperationException(
							"Could not login with given username/password.Please check the credentials");
				}
			}
			this.userName = userName;

		}
		catch (Exception appExp)
		{
			throw new BulkOperationException(appExp.getMessage(), appExp);
		}
	}

	@Override
	public void initialize(String userName, String password) throws BulkOperationException
	{
		appService = new CaCoreAppServicesDelegator();
		authenticate(userName, password);
	}

	@Override
	public void deleteObject(Object arg0) throws BulkOperationException
	{
	}

	@Override
	protected Object insertObject(Object domainObject) throws Exception
	{
		try
		{
			Object returnedObject = appService.delegateAdd(userName, domainObject);
			return returnedObject;
		}
		catch (ApplicationException appExp)
		{
			throw appExp;
		}
		catch (Exception exp)
		{
			throw exp;
		}
	}

	@Override
	protected Object searchObject(Object str) throws Exception
	{
		Object returnedObject = null;
		try
		{
			String hql = (String) str;
			List result = AppUtility.executeQuery(hql);

			if (!result.isEmpty())
			{
				returnedObject = result.get(0);
			}
		}
		catch (Exception appExp)
		{
			throw new Exception(appExp.getMessage(), appExp);
		}
		return returnedObject;
	}

	@Override
	protected Object updateObject(Object domainObject) throws Exception
	{
		try
		{
			Object returnedObject = appService.delegateEdit(userName, domainObject);
			return returnedObject;
		}
		catch (ApplicationException appExp)
		{
			throw appExp;
		}
		catch (Exception exp)
		{
			throw exp;
		}
	}

	/**
	 * Hook Static Dyn Ext Object.
	 * @param hookInformationObject Object
	 * @return List of Objects
	 * @throws Exception Exception
	 */
	@Override
	protected Long hookStaticDynExtObject(Object hookInformationObject)
			throws DynamicExtensionsSystemException, ApplicationException
	{
		HookingInformation hookInformation = (HookingInformation) hookInformationObject;
		Long dynExtObjectId = hookInformation.getDynamicExtensionObjectId();
		Long containerId;
		NameValueBean hookEntityBean;
		AnnotationBizLogic bizLogic = new AnnotationBizLogic();
		if (hookInformation.getEntityGroupName()!=null && !"".equals(hookInformation.getEntityGroupName()))
		{
			EntityGroupInterface entityGroup = EntityCache.getInstance().getEntityGroupByName(
					hookInformation.getEntityGroupName());
			EntityInterface entity = entityGroup.getEntityByName(hookInformation.getEntityName());
			ContainerInterface container = (ContainerInterface) entity.getContainerCollection()
					.iterator().next();
			containerId = container.getId();
			hookEntityBean = bizLogic.getHookEntiyNameValueBean(entity.getId(), hookInformation
					.getEntityName());
		}
		else
		{
			DEIntegration deIntegration = new DEIntegration();
			containerId = deIntegration.getRootCategoryContainerIdByName(hookInformation
					.getCategoryName());
			hookEntityBean = bizLogic.getHookEntityNameValueBeanForCategory(containerId,
					hookInformation.getCategoryName());
		}

		//write logic to find exact hook entity
		Long selectedStaticEntityRecordId = getSelectedStaticEntityRecordId(hookEntityBean,
				hookInformation);
		Long recordEntryId = bizLogic.createHookEntityObject(dynExtObjectId.toString(), containerId.toString(),
				hookEntityBean.getName(), selectedStaticEntityRecordId.toString(), hookEntityBean
						.getValue(), hookInformation.getSessionDataBean());

		return recordEntryId;
	}

	/**
	 * It will retrieve the ID of the hook entity with which the DE record should be
	 * hooked depending of the hookEntity Bean & hooking information given by user.
	 * @param hookEntityBean bean of the hooking entity details.
	 * @param hookInformation hooking information given by user in data csv.
	 * @return id of the hook entity.
	 * @throws BizLogicException exception.
	 */
	private Long getSelectedStaticEntityRecordId(NameValueBean hookEntityBean,
			HookingInformation hookInformation) throws BizLogicException
	{
		String cpLabel = (String) hookInformation.getDataHookingInformation().get(
				BulkOperationConstants.COLLECTION_PROTOCOL_LABEL);
		Long selectedEntityId = null;

		if (hookEntityBean.getName().equals(AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY))
		{
			selectedEntityId = getParticipantIdForHooking(hookInformation, cpLabel);
		}
		else if (hookEntityBean.getName().equals(AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY))
		{
			selectedEntityId = getSCGIdforHooking(hookInformation);

		}
		else
		{
			selectedEntityId = getSpecimenIdForHooking(hookInformation);
		}
		return selectedEntityId;
	}

	/**
	 * It will return the Id of the specimen retrieving it from the given hooking information.
	 * @param hookInformation hooking information given by user.
	 * @return id of the specimen.
	 * @throws BizLogicException exception.
	 */
	private Long getSpecimenIdForHooking(HookingInformation hookInformation)
			throws BizLogicException
	{
		Long selectedEntityId = (Long) hookInformation.getDataHookingInformation().get(
				BulkOperationConstants.SPECIMEN_ID);
		AnnotationBizLogic bizLogic = new AnnotationBizLogic();
		if (selectedEntityId == null)
		{
			String specimenLabel = (String) hookInformation.getDataHookingInformation().get(
					BulkOperationConstants.SPECIMEN_LABEL);
			String specimenBarcode = (String) hookInformation.getDataHookingInformation().get(
					BulkOperationConstants.SPECIMEN_BARCODE);

			if (specimenLabel != null && !specimenLabel.trim().equals(""))
			{

				//get the scgId on the basis of cp label & scg label
				selectedEntityId = bizLogic.getSpecimenByLabel(specimenLabel);
			}
			else

			{
				selectedEntityId = bizLogic.getSpecimenByBarcode(specimenBarcode);
			}
			if (selectedEntityId == null)
			{
				throw new BizLogicException(ErrorKey.getErrorKey("invalid.param.bo.specimen"),
						null, null);
			}
		}
		else
		{
			bizLogic.isSpecimenExists(selectedEntityId.toString());
		}
		return selectedEntityId;
	}

	/**
	 * It will return the Id of the SCG retrieving it from the given hooking information.
	 * @param hookInformation hooking information given by user.
	 * @return id of the SCG.
	 * @throws BizLogicException exception.
	 */
	private Long getSCGIdforHooking(HookingInformation hookInformation) throws BizLogicException
	{
		Long selectedEntityId = (Long) hookInformation.getDataHookingInformation().get(
				BulkOperationConstants.SCG_ID);
		SpecimenCollectionGroupBizLogic bizLogic = new SpecimenCollectionGroupBizLogic();
		if (selectedEntityId == null)
		{
			String scgLabel = (String) hookInformation.getDataHookingInformation().get(
					BulkOperationConstants.SCG_NAME);
			String scgBarcode = (String) hookInformation.getDataHookingInformation().get(
					BulkOperationConstants.SCG_BARCODE);

			if (scgLabel != null && !scgLabel.trim().equals(""))
			{

				//get the scgId on the basis of cp label & scg label
				selectedEntityId = bizLogic.getScgIdFromName(scgLabel);
			}
			else

			{
				selectedEntityId = bizLogic.getScgIdFromBarcode(scgBarcode);
			}
			if (selectedEntityId == null)
			{
				throw new BizLogicException(ErrorKey.getErrorKey("invalid.param.bo.scg"), null,
						null);
			}
		}
		else
		{
			bizLogic.isSCGExists(selectedEntityId.toString());
		}
		return selectedEntityId;
	}

	/**
	 * It will return the Id of the participant retrieving it from the given hooking information.
	 * @param hookInformation hooking information given by user.
	 * @return id of the participant.
	 * @throws BizLogicException exception.
	 */
	private Long getParticipantIdForHooking(HookingInformation hookInformation, String cpLabel)
			throws BizLogicException
	{
		Long selectedEntityId;
		ParticipantBizLogic bizLogic = new ParticipantBizLogic();
		selectedEntityId = (Long) hookInformation.getDataHookingInformation().get(
				BulkOperationConstants.PARTICIPANT_ID);
		String ppi = (String) hookInformation.getDataHookingInformation().get(
				BulkOperationConstants.PPI);
		if (selectedEntityId == null)
		{
			selectedEntityId = bizLogic.getParticipantIdByPPI(cpLabel, ppi);
			if (selectedEntityId == null)
			{
				throw new BizLogicException(ErrorKey.getErrorKey("invalid.param.bo.participant"),
						null, null);
			}
		}
		else
		{
			bizLogic.isParticipantExists(selectedEntityId.toString());
		}
		return selectedEntityId;
	}

	/**
	 * Insert Dyn Ext Object.
	 * @param dynExtObject Object
	 * @param catissueStaticObject Object
	 * @return Object
	 * @throws Exception Exception
	 */
	public Long insertDEObject(String entityGroupName, String entityName,
			final Map<String, Object> dataValue) throws Exception
	{
		Long recordIdentifier = null;
		CategoryManager.getInstance();
		EntityGroupInterface entityGroup = EntityCache.getInstance().getEntityGroupByName(
				entityGroupName);

		if (entityGroup == null)
		{
			LOGGER.error("Entity group with the name '" + entityGroupName + "' does not exist.");
			throw new BulkOperationException("Entity group with name '" + entityGroupName
					+ "' does not exist.");
		}
		EntityInterface entity = entityGroup.getEntityByName(entityName);
		if (entity == null)
		{
			LOGGER.error("Entity with the name '" + entityName + "' does not exist.");
			throw new BulkOperationException("Entity with name '" + entityName
					+ "' does not exist.");
		}
		ContainerInterface containerInterface = (ContainerInterface) entity
				.getContainerCollection().toArray()[0];
		Map<BaseAbstractAttributeInterface, Object> attributeToValueMap = DataValueMapUtility
				.getAttributeToValueMap(dataValue, entity);
		List<String> errorList = ValidatorUtil.validateEntity(attributeToValueMap,
				new ArrayList<String>(), containerInterface, true);
		if (errorList.isEmpty())
		{
			EntityManagerInterface entityManager = EntityManager.getInstance();
			Map map = attributeToValueMap;
			recordIdentifier = entityManager.insertData(entity, map, null, null);
		}
		else
		{
			updateErrorMessages(errorList);
		}
		//TODO pass sessionDataBean instead of null, so that it gets audited.
		return recordIdentifier;
	}

	/**
	 * Insert Data for Category.
	 *
	 * @param categoryName the category name
	 * @param dataValue Map of Long and Object type.
	 * @return Long Identifier of the inserted data
	 * @throws Exception Exception
	 */
	@Override
	public Long insertData(final String categoryName, final Map<String, Object> dataValue)
			throws ApplicationException, DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, ParseException
	{
		Long recordIdentifier = null;
		CategoryManager.getInstance();
		CategoryInterface categoryInterface = EntityCache.getInstance().getCategoryByName(
				categoryName);

		if (categoryInterface == null)
		{
			LOGGER.error("Category with the name '" + categoryName + "' does not exist.");
			throw new BulkOperationException("Category with name '" + categoryName
					+ "' does not exist.");
		}
		ContainerInterface containerInterface = (ContainerInterface) categoryInterface
				.getRootCategoryElement().getContainerCollection().toArray()[0];
		Map<BaseAbstractAttributeInterface, Object> attributeToValueMap = DataValueMapUtility
				.getAttributeToValueMap(dataValue, categoryInterface);
		List<String> errorList = ValidatorUtil.validateEntity(attributeToValueMap,
				new ArrayList<String>(), containerInterface, true);
		if (errorList.isEmpty())
		{
			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			Long categoryRecordId = categoryManager.insertData(categoryInterface,
					attributeToValueMap, null);
			recordIdentifier = categoryManager.getEntityRecordIdByRootCategoryEntityRecordId(
					categoryRecordId, categoryInterface.getRootCategoryElement()
							.getTableProperties().getName());
		}
		else
		{
			updateErrorMessages(errorList);
		}
		//TODO pass sessionDataBean instead of null, so that it gets audited.
		return recordIdentifier;
	}

	/**
	 * It will form a string of errors seen in the data & will throw a exception with
	 * message as given in the errorList.
	 * @param errorList list of validation errors.
	 * @throws DynamicExtensionsApplicationException exception.
	 */
	private void updateErrorMessages(List<String> errorList)
			throws DynamicExtensionsApplicationException
	{
		StringBuffer buffer = new StringBuffer();
		int count = 1;
		Iterator<String> errorListIterator = errorList.iterator();
		while (errorListIterator.hasNext())
		{
			buffer.append(count).append(')').append(errorListIterator.next());
			count++;
		}
		throw new DynamicExtensionsApplicationException(buffer.toString());
	}
}