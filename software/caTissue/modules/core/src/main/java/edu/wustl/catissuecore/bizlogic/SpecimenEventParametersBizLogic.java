/*

 * Created on Jul 29, 2005
 *<p>SpecimenEventParametersBizLogic Class</p>
 * This class contains the Biz Logic for all EventParameters Classes.
 * This will be the class which will be used for datatransactions of the EventParameters.
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.uiobject.DisposalEventParametersUIObject;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.Position;
import edu.wustl.catissuecore.util.SpecimenUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.condition.NotEqualClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * @author mandar_deshmukh
 * This class contains the Business Logic for all EventParameters Classes.
 * This will be the class which will be used for data transactions of the EventParameters.
 */
public class SpecimenEventParametersBizLogic extends CatissueDefaultBizLogic
{

	/**
	 * Logger object.
	 */
	private static final Logger LOGGER = Logger
	.getCommonLogger(SpecimenEventParametersBizLogic.class);
	/**
	 * storageContainerIds.
	 */
	private HashSet<String> storageContainerIds = new HashSet<String>();
	
	
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		DisposalEventParametersUIObject disposalEventParametersUIObject=new DisposalEventParametersUIObject();
		this.insert(obj,disposalEventParametersUIObject,dao,sessionDataBean);
	}
	
	
	/**
	 * Saves the FrozenEventParameters object in the database.
	 * @param obj The FrozenEventParameters object to be saved.
	 * @param dao DAO Object
	 * @param sessionDataBean SessionDataBean object
	 * @throws BizLogicException throws BizLogicException.
	 */
	@Override
	protected void insert(Object obj,Object uiObject, DAO dao, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final NewSpecimenBizLogic newSpecimenBizLogic = (NewSpecimenBizLogic) factory
		.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
		final List specimenIds = new ArrayList();
		if (obj instanceof List)
		{
			final List eventObjectsList = (List) obj;

			for (int i = 0; i < eventObjectsList.size(); i++)
			{
				specimenIds.add(((SpecimenEventParameters) eventObjectsList.get(i)).getSpecimen()
						.getId());
			}

			for (int i = 0; i < eventObjectsList.size(); i++)
			{
				this.insertEvent(eventObjectsList.get(i), dao, sessionDataBean,
						newSpecimenBizLogic, specimenIds);
				/*if(eventObjectsList.get(i) instanceof TransferEventParameters)
				{
					TransferEventParameters transferEventParameters = (TransferEventParameters) eventObjectsList.get(i);
					User user= new User();
					user.setId(sessionDataBean.getUserId());
					Map<BaseAbstractAttributeInterface, Object> dataValueMap = new HashMap<BaseAbstractAttributeInterface, Object>();

					Specimen specimen = (Specimen) ((SpecimenEventParameters) eventObjectsList.get(i)).getSpecimen();
					SpecimenPosition oldSpecimenPosition =null;
					StringBuilder fromStorageContainer = new StringBuilder(transferEventParameters.getFromStorageContainer().getName());

					Integer posDimenOne=transferEventParameters.getFromPositionDimensionOne();
					Integer posDimenTwo=transferEventParameters.getFromPositionDimensionTwo();

					insertDynamicEventForTransferEvent(sessionDataBean, transferEventParameters,
							dataValueMap, specimen, oldSpecimenPosition, fromStorageContainer,
							posDimenOne, posDimenTwo);
				}
				else if(eventObjectsList.get(i) instanceof DisposalEventParameters)
				{
					DisposalEventParameters disposalEventParameters=(DisposalEventParameters) eventObjectsList.get(i);
					User user= new User();
					user.setId(sessionDataBean.getUserId());
					Map<BaseAbstractAttributeInterface, Object> dataValueMap = new HashMap<BaseAbstractAttributeInterface, Object>();
					Specimen specimen = (Specimen) ((SpecimenEventParameters) eventObjectsList.get(i)).getSpecimen();

					insertDynamicDataForDisposalEvent(sessionDataBean, disposalEventParameters,
							dataValueMap, specimen);
				}*/
			}
		}
		else
		{
			this.insertEvent(obj, dao, sessionDataBean, newSpecimenBizLogic, specimenIds);
		}
	}

	/**
	 * @param sessionDataBean
	 * @param transferEventParameters
	 * @param dataValueMap
	 * @param specimen
	 * @param oldSpecimenPosition
	 * @param fromStorageContainer
	 * @param posDimenOne
	 * @param posDimenTwo
	 */
	/*public void insertDynamicEventForTransferEvent(SessionDataBean sessionDataBean,
			TransferEventParameters transferEventParameters,
			Map<BaseAbstractAttributeInterface, Object> dataValueMap, Specimen specimen,
			SpecimenPosition oldSpecimenPosition, StringBuilder fromStorageContainer,
			Integer posDimenOne, Integer posDimenTwo)
	{
		User user;
		try{
			EntityInterface dynamicTransferEventParameters = null;
			dynamicTransferEventParameters= EntityCache.getInstance().getEntityGroupByName("SpecimenEvents").getEntityByName("TransferEventParameters");

			if (dynamicTransferEventParameters.getAttributeCollection() != null)
			{
				// Iterate over all the attributes of the DE event object.
				for (AttributeInterface attribute : dynamicTransferEventParameters.getAttributeCollection())
				{
					// Ignore the Id attribute as data won't be inserted for it.
					if (!attribute.getName().equalsIgnoreCase("id")&& (!attribute.getName().equalsIgnoreCase("fromStorageContainer")) && !attribute
							.getName().equalsIgnoreCase("toStorageContainer"))
					{
						Class UIHostClass =transferEventParameters.getClass();
						String methodName = "get" + org.apache.commons.lang.StringUtils.capitalise(attribute.getName());
						java.lang.reflect.Method callableMethod = UIHostClass.getMethod(methodName);

						// Object passed is of static Event.
						Object methodValue = callableMethod.invoke(transferEventParameters);
						if(oldSpecimenPosition!= null)
						{
							if(attribute.getName().equalsIgnoreCase("fromPositionDimensionOne"))
							{
								methodValue = posDimenOne;
							}
							else if(attribute.getName().equalsIgnoreCase("fromPositionDimensionTwo"))
							{
								methodValue = posDimenTwo;
							}
						}
						// Populate datavalue map with attribute and its value from static event.
						dataValueMap.put(attribute, methodValue);
					}
					else if (attribute.getName().equalsIgnoreCase("fromStorageContainer"))
					{
						dataValueMap.put(attribute, fromStorageContainer);
					}
					else if (attribute.getName().equalsIgnoreCase("toStorageContainer"))
					{
						StringBuilder toStorageContainer2 = new StringBuilder(transferEventParameters
								.getToStorageContainer().getName());
						toStorageContainer2.append(':').append("pos(").append(
								transferEventParameters.getToPositionDimensionOne()).append(',').append(
								transferEventParameters.getToPositionDimensionTwo()).append(')');
						toStorageContainer2.toString();
						dataValueMap.put(attribute, toStorageContainer2);
					}
				}
			}

			Map<String, Long> dynamicEventMap = new HashMap<String, Long>();
			new SPPBizLogic().getAllSPPEventFormNames(dynamicEventMap);
			long containerId = dynamicEventMap.get("Transfer Event Parameters");

			// This is required, because of changes in DE for doing data entry.
			ContainerInterface container = EntityCache.getInstance().getContainerById(
					containerId);

			// API used in DE for doing data entry.
			ApplyDataEntryFormProcessor applDataEntryFormProcessor = ApplyDataEntryFormProcessor
					.getInstance();
			Long recordIdentifier = Long.valueOf(applDataEntryFormProcessor.insertDataEntryForm(container, dataValueMap, null));

			DefaultAction defaultAction=null;
			ActionApplicationRecordEntry actionAppRecordEntry=null;
			actionAppRecordEntry=new ActionApplicationRecordEntry();

			final IBizLogic defaultBizLogic =  new CatissueDefaultBizLogic();
			List<DefaultAction> defaultActionList=defaultBizLogic.retrieve(DefaultAction.class.getName(),  "containerId", String.valueOf(containerId));

			if(defaultActionList==null ||defaultActionList.isEmpty())
			{
				defaultAction=new DefaultAction();
				defaultAction.setContainerId(containerId);
				defaultBizLogic.insert(defaultAction);
			}
			else
			{
				defaultAction=(DefaultAction) defaultActionList.get(0);
				defaultAction.setContainerId(containerId);
			}
			actionAppRecordEntry.setFormContext(defaultAction);
			actionAppRecordEntry.setActivityStatus("Active");

			ActionApplication  actionApplication= new ActionApplication();

			actionApplication.setTimestamp(Calendar.getInstance().getTime());

			InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
			  user = instFact.createObject();
			user= (User) retrieve(User.class.getName(), sessionDataBean.getUserId())  ;

			actionApplication.setPerformedBy(user);

			actionApplication.setSpecimen(specimen);
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic actionAppBizLogic = (ActionApplicationBizLogic) factory
			.getBizLogic(Constants.ACTION_APP_FORM_ID);

			actionAppBizLogic.insert(actionAppRecordEntry);
			actionAppBizLogic.insert(actionApplication);

			actionApplication.setApplicationRecordEntry(actionAppRecordEntry);
			actionAppBizLogic.update(actionApplication);

			IntegrateDEData integrateDEData=new IntegrateDEData();
			integrateDEData.associateRecords(containerId, actionAppRecordEntry.getId(),recordIdentifier);


			}catch (Exception exp) {
				exp.printStackTrace();
			}
	}
*/
	/**
	 * @param sessionDataBean
	 * @param disposalEventParameters
	 * @param dataValueMap
	 * @param specimen
	 */
	/*public void insertDynamicDataForDisposalEvent(SessionDataBean sessionDataBean,
			DisposalEventParameters disposalEventParameters,
			Map<BaseAbstractAttributeInterface, Object> dataValueMap, Specimen specimen)
	{
		User user;
		try{
			EntityInterface dynamicDisposeEvent = null;
			dynamicDisposeEvent= EntityCache.getInstance().getEntityGroupByName("SpecimenEvents").getEntityByName("DisposalEventParameters");

			Collection<AttributeInterface> attributes =dynamicDisposeEvent.getAttributeCollection();
			LinkedList<AbstractAttributeInterface> methods = new LinkedList<AbstractAttributeInterface>();

			Map<String, Object> oldDataValueMap = new HashMap<String, Object>();
			if (dynamicDisposeEvent.getAttributeCollection() != null)
			{
				String hostClassName = dynamicDisposeEvent.getClass().getName();
				// Iterate over all the attributes of the DE event object.
				for (AttributeInterface attribute : dynamicDisposeEvent.getAttributeCollection())
				{
					// Ignore the Id attribute as data won't be inserted for it.
					if (!attribute.getName().equalsIgnoreCase("id"))
					{

						Class hostClass = Class.forName(hostClassName);
						Class UIHostClass =disposalEventParameters.getClass();
						String methodName = "get" + org.apache.commons.lang.StringUtils.capitalise(attribute.getName());
						java.lang.reflect.Method callableMethod = UIHostClass.getMethod(methodName);

						// Object passed is of static Event.
						Object methodValue = callableMethod.invoke(disposalEventParameters);

						// Populate datavalue map with attribute and its value from static event.
						dataValueMap.put(attribute, methodValue);
					}
				}
			}

			//Long deId= EntityManager.getInstance().insertData(dynamicDisposeEvent, dataValueMap,
					//null, new ArrayList<FileQueryBean>(), null);


			Map<String, Long> dynamicEventMap = new HashMap<String, Long>();
			new SPPBizLogic().getAllSPPEventFormNames(dynamicEventMap);
			long containerId = dynamicEventMap.get("Disposal Event Parameters");

			// This is required, because of changes in DE for doing data entry.
			ContainerInterface container = EntityCache.getInstance().getContainerById(
					containerId);

			// API used in DE for doing data entry.
			ApplyDataEntryFormProcessor applDataEntryFormProcessor = ApplyDataEntryFormProcessor
					.getInstance();
			Long recordIdentifier = Long.valueOf(applDataEntryFormProcessor.insertDataEntryForm(container, dataValueMap, null));


			DefaultAction defaultAction=null;
			ActionApplicationRecordEntry actionAppRecordEntry=null;
			actionAppRecordEntry=new ActionApplicationRecordEntry();

			final IBizLogic defaultBizLogic =  new CatissueDefaultBizLogic();
			List<DefaultAction> defaultActionList=defaultBizLogic.retrieve(DefaultAction.class.getName(),  "containerId", String.valueOf(containerId));

			if(defaultActionList==null ||defaultActionList.isEmpty())
			{
				defaultAction=new DefaultAction();
				defaultAction.setContainerId(containerId);
				defaultBizLogic.insert(defaultAction);
			}
			else
			{
				defaultAction=(DefaultAction) defaultActionList.get(0);
				defaultAction.setContainerId(containerId);
			}
			actionAppRecordEntry.setFormContext(defaultAction);
			actionAppRecordEntry.setActivityStatus("Active");

			ActionApplication  actionApplication= new ActionApplication();
			// TO  ask gaurav actionApplication.setReasonDeviation(dynamicEventForm.getReasonDeviation());
//						final Calendar calendar = Calendar.getInstance();
//						final Date date = CommonUtilities.parseDate(new Date().toString(), CommonUtilities
//								.datePattern(new Date().toString()));
//						calendar.setTime(date);
//						calendar.set(Calendar.HOUR_OF_DAY, Calendar.HOUR);
//
//						calendar.set(Calendar.MINUTE, Calendar.SECOND);

			actionApplication.setTimestamp(Calendar.getInstance().getTime());

			InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
			  user = instFact.createObject();//new User();
			user= (User) retrieve(User.class.getName(), sessionDataBean.getUserId())  ;
			//user.setId(sessionDataBean.getUserId());

			actionApplication.setPerformedBy(user);
//						Specimen specimen;
//						specimen = new Specimen();
//						specimen.setId(new Long(specimenId));
			actionApplication.setSpecimen(specimen);

			//defaultBizLogic.update(user, user, sessionDataBean);

			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic actionAppBizLogic = (ActionApplicationBizLogic) factory
			.getBizLogic(Constants.ACTION_APP_FORM_ID);

			actionAppBizLogic.insert(actionAppRecordEntry);
			actionAppBizLogic.insert(actionApplication);

			actionApplication.setApplicationRecordEntry(actionAppRecordEntry);
			actionAppBizLogic.update(actionApplication);

			IntegrateDEData integrateDEData=new IntegrateDEData();
			integrateDEData.associateRecords(containerId, actionAppRecordEntry.getId(),recordIdentifier);
			//Collection<ActionApplication> actionApplicationCollection=(Collection<ActionApplication>) retrieveAttribute(Specimen.class.getName(), specimen.getId(), "actionApplicationCollection");
			//actionApplicationCollection.add(actionApplication);
			//specimen.setActionApplicationCollection(actionApplicationCollection);

			//update(specimen);

			}catch (Exception exp) {
				exp.printStackTrace();
				*//**
				 * required Exception  handling
				 *//*
//							throw this.getBizLogicException(exp, exp.getErrorKeyName(),
//									exp.getMsgValues());
			}
	}*/

	/**
	 * @param obj - Object.
	 * @param dao - DAO object.
	 * @param sessionDataBean - SessionDataBean object
	 * @param newSpecimenBizLogic - NewSpecimenBizLogic object
	 * @param specimenIds - List of specimenIds
	 * @throws BizLogicException throws BizLogicException
	 */
	private void insertEvent(Object obj, DAO dao, SessionDataBean sessionDataBean,
			NewSpecimenBizLogic newSpecimenBizLogic, List specimenIds) throws BizLogicException
			{
		try
		{
			SpecimenEventParameters specimenEvent = (SpecimenEventParameters) obj;
			checkStatusAndGetUserId(specimenEvent, dao);
			//			Ashish - 6/6/07 - performance improvement
			Object specimenObject = null;
			specimenObject = retrieveSpecimenLabelName(dao, specimenEvent);

			final Specimen specimen = (Specimen) specimenObject;
			// check for closed Specimen

			if (specimenEvent instanceof DisposalEventParameters)
			{
				this.checkStatus(dao, specimen, Constants.DISPOSAL_EVENT_PARAMETERS);
			}
			else
			{
				this.checkStatus(dao, specimen, Constants.SPECIMEN);
			}
			if(Status.ACTIVITY_STATUS_DISABLED.getStatus().equals(specimen.getActivityStatus()))
			{
				throw this.getBizLogicException(null, "error.object.disabled",
				"Specimen");
			}

			if (specimen != null)
			{
				specimenEvent.setSpecimen(specimen);
				if (specimenEvent instanceof TransferEventParameters)
				{
					final TransferEventParameters transferEvent = (TransferEventParameters) specimenEvent;
					InstanceFactory<StorageContainer> scInstFact = DomainInstanceFactory.getInstanceFactory(StorageContainer.class);
					StorageContainer storageContainerObj = scInstFact.createObject();//new StorageContainer();

					List stNamelist = null;
					final String sourceObjectName = StorageContainer.class.getName();
					String[] selectColumnName = null;
					storageContainerObj = transferEvent.getToStorageContainer();
					if (storageContainerObj != null
							&& (storageContainerObj.getId() != null || storageContainerObj
									.getName() != null))
					{
						if (storageContainerObj.getId() == null)
						{
							//storageContainerObj.setId(storageContainerObj.getId());
							selectColumnName = new String[]{"id"};
							final QueryWhereClause queryWhereClause = new QueryWhereClause(
									sourceObjectName);
							queryWhereClause.addCondition(new EqualClause("name",
									transferEvent.getToStorageContainer().getName()));
							stNamelist = dao.retrieve(sourceObjectName, selectColumnName,
									queryWhereClause);
							if (!stNamelist.isEmpty())
							{
								storageContainerObj.setId((Long) (stNamelist.get(0)));
							}
						}
						else
						{
							selectColumnName = new String[]{"name"};
							final QueryWhereClause queryWhereClause = new QueryWhereClause(
									sourceObjectName);
							queryWhereClause.addCondition(new EqualClause("id",
									transferEvent.getToStorageContainer().getId()));

							stNamelist = dao.retrieve(sourceObjectName, selectColumnName,
									queryWhereClause);
							if (!stNamelist.isEmpty())
							{
								storageContainerObj.setName((String) stNamelist.get(0));
							}
						}
						// check for closed StorageContainer
						this.checkStatus(dao, storageContainerObj, "Storage Container");

						final IFactory factory = AbstractFactoryConfig.getInstance()
						.getBizLogicFactory();
						final StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) factory
						.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);

						final String contId = storageContainerObj.getId().toString();
						final String posOne = transferEvent.getToPositionDimensionOne()
						.toString();
						final String posTwo = transferEvent.getToPositionDimensionTwo()
						.toString();
						storageContainerBizLogic.checkContainer(dao, StorageContainerUtil
								.setparameterList(contId, posOne, posTwo, false), sessionDataBean,
								null);
						final SpecimenCollectionGroup scg = (SpecimenCollectionGroup) this
						.retrieveAttribute(dao, Specimen.class, specimen.getId(),
						"specimenCollectionGroup");
						specimen.setSpecimenCollectionGroup(scg);
						newSpecimenBizLogic.chkContainerValidForSpecimen(storageContainerObj,
								specimen, dao);
					}
					else
					{
						storageContainerObj = null;
					}

					SpecimenPosition specimenPosition = specimen.getSpecimenPosition();
					if (specimenPosition == null)
					{
						// trasfering from virtual location
						InstanceFactory<SpecimenPosition> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenPosition.class);
						specimenPosition = instFact.createObject();//new SpecimenPosition();
						specimenPosition.setSpecimen(specimen);
						specimen.setSpecimenPosition(specimenPosition);
					}
					specimenPosition.setStorageContainer(storageContainerObj);
					specimenPosition.setPositionDimensionOne(transferEvent
							.getToPositionDimensionOne());
					specimenPosition.setPositionDimensionTwo(transferEvent
							.getToPositionDimensionTwo());
					dao.update(specimen,null);
					transferEvent.setToStorageContainer(storageContainerObj);
				}
				if (specimenEvent instanceof DisposalEventParameters)
				{
					final DisposalEventParameters disposalEventParameters = (DisposalEventParameters) specimenEvent;
					if (disposalEventParameters.getActivityStatus().equals(
							Status.ACTIVITY_STATUS_DISABLED.getStatus()))
					{
						this.disableSubSpecimens(dao, specimen.getId().toString(), specimenIds);
					}
					final SpecimenPosition prevPosition = specimen.getSpecimenPosition();
					specimen.setSpecimenPosition(null);
					specimen.setIsAvailable(Boolean.FALSE);
					specimen.setActivityStatus(disposalEventParameters.getActivityStatus());
					dao.update(specimen);
					if (prevPosition != null)
					{
						dao.delete(prevPosition);
					}
				}

			}
			if(specimen.getSpecimenEventCollection()==null)
			{
				specimen.setSpecimenEventCollection(new HashSet<SpecimenEventParameters>());
			}
			specimen.getSpecimenEventCollection().add(specimenEvent);
			//specimenEvent.doRoundOff();
			//SpecimenEventParametersUtility.doRoundOff(specimenEvent);
			dao.insert(specimenEvent);
		}
		catch (final DAOException daoExp)
		{
			this.LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
			.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
			}

	/**
	 * Retrieve specimen by label name.
	 * @param dao DAO
	 * @param specimenEventParametersObject SpecimenEventParameters
	 * @return Object
	 * @throws DAOException DAOException
	 * @throws BizLogicException BizLogicException
	 */
	private Object retrieveSpecimenLabelName(DAO dao,
			SpecimenEventParameters specimenEventParametersObject) throws DAOException,
			BizLogicException
			{
		Object specimenObject = null;
		if (specimenEventParametersObject.getSpecimen().getId() != null)
		{
			specimenObject = dao.retrieveById(Specimen.class.getName(),
					specimenEventParametersObject.getSpecimen().getId());
		}
		else if (specimenEventParametersObject.getSpecimen().getLabel() != null
				&& specimenEventParametersObject.getSpecimen().getLabel().length() > 0)
		{
			String column = "label";
			List list = dao.retrieve(Specimen.class.getName(), column, specimenEventParametersObject
					.getSpecimen().getLabel());
			if (list.isEmpty())
			{
				throw this.getBizLogicException(null, "invalid.label",
						specimenEventParametersObject.getSpecimen().getLabel());
			}
			else
			{
				specimenObject = list.get(0);

			}
		}
		else if (specimenEventParametersObject.getSpecimen() instanceof Specimen
				&& ((Specimen)specimenEventParametersObject.getSpecimen()).getBarcode() != null
				&& ((Specimen)specimenEventParametersObject.getSpecimen()).getBarcode().length() > 0)
		{
			final Specimen specimen= (Specimen) specimenEventParametersObject.getSpecimen();
			String column = "barcode";
			List list = dao.retrieve(Specimen.class.getName(), column, specimen.getBarcode());
			if (list.isEmpty())
			{
				throw this.getBizLogicException(null, "invalid.barcode",
						specimen.getBarcode());
			}
			else
			{
				specimenObject = list.get(0);

			}
		}

		return specimenObject;
			}

	/**
	 * Check Status And Get UserId.
	 * @param specimenEventParameter SpecimenEventParameters
	 * @param dao DAO
	 * @throws DAOException DAOException
	 * @throws BizLogicException BizLogicException
	 */
	private void checkStatusAndGetUserId(SpecimenEventParameters specimenEventParameter, DAO dao)
	throws DAOException, BizLogicException
	{
		List list = null;
		String message = "";
		if (specimenEventParameter.getUser().getId() != null)
		{
			list = dao.executeQuery("select id,activityStatus from edu.wustl.catissuecore.domain.User where id="+specimenEventParameter.getUser()
					.getId());
			message = ApplicationProperties.getValue("app.UserID");
		}
		else if (specimenEventParameter.getUser().getLoginName() != null
				|| specimenEventParameter.getUser().getLoginName().length() > 0)
		{
			list = dao.executeQuery("select id,activityStatus from edu.wustl.catissuecore.domain.User where loginName='"+specimenEventParameter.getUser()
					.getLoginName()+"'");
			message = ApplicationProperties.getValue("user.loginName");
		}
		if(list!=null&&!list.isEmpty())
		{
			Object[] object = (Object[])list.get(0);
			InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
			final User user = instFact.createObject();
			user.setId((Long)object[0]);
			user.setActivityStatus((String)object[1]);
			// check for closed User
			this.checkStatus(dao, user, "User");
			specimenEventParameter.setUser(user);
		}
		else
		{
			throw this.getBizLogicException(null, "errors.item.forboformat", message);
		}

	}

	/**
	 * Method overridden from DefaultBizLogic.java. This method checks for the specimens status
	 * whether it is closed. If the event is disposal only then it does not throws exception.
	 * @param dao DAo object.
	 * @param specimen Specimen object on which event is happened.
	 * @param objectType for knowing which event is happened.
	 * @throws BizLogicException DAO Exception.
	 */
	protected void checkStatus(DAO dao, Specimen specimen, String objectType)
	throws BizLogicException
	{
		if (specimen != null)
		{
			final Long identifier = specimen.getId();
			if (identifier != null)
			{
				final String className = specimen.getClass().getName();
				String activityStatus = specimen.getActivityStatus();
				if (activityStatus == null)
				{
					activityStatus = this.getActivityStatus(dao, className, identifier);
				}

				if (Status.ACTIVITY_STATUS_CLOSED.toString().equals(activityStatus)
						&& (!Constants.DISPOSAL_EVENT_PARAMETERS.equals(objectType)))
				{
					throw this.getBizLogicException(null, "error.object.closed", objectType);
				}
			}
		}
	}

	/**
	 * calling post insert of parent class
	 * @param dao DAO Object
	 * @param sessionDataBean SessionDataBean object
	 * @param obj Domain object
	 */
	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		super.postInsert(obj, dao, sessionDataBean);
	}
	
	/**
	 * Updates the persistent object in the database.
	 * @param dao - DAO object
	 * @param obj The object to be updated.
	 * @param oldObj - Object
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws BizLogicException throws BizLogicException
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
	
		DisposalEventParametersUIObject disposalEventParametersUIObject=new DisposalEventParametersUIObject();
		update(dao,obj,oldObj,disposalEventParametersUIObject,sessionDataBean);

	}

	/**
	 * Updates the persistent object in the database.
	 * @param dao - DAO object
	 * @param obj The object to be updated.
	 * @param oldObj - Object
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws BizLogicException throws BizLogicException
	 */
	protected void update(DAO dao, Object obj, Object oldObj,Object uiObject, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		try
		{
			final SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) obj;
			final SpecimenEventParameters oldSpecimenEventParameters = (SpecimenEventParameters) oldObj;
			if (!specimenEventParameters.getUser().getId().equals(
					oldSpecimenEventParameters.getUser().getId()))
			{
				this.checkStatus(dao, specimenEventParameters.getUser(), "User");
			}

			if (specimenEventParameters instanceof DisposalEventParameters)
			{
				updateDisposalEvent(dao, (DisposalEventParameters) specimenEventParameters);
			}
			//specimenEventParameters.doRoundOff();
			//SpecimenEventParametersUtility.doRoundOff(specimenEventParameters);
			//Update registration
			dao.update(specimenEventParameters, oldSpecimenEventParameters);
		}
		catch (final DAOException daoExption)
		{
			this.LOGGER.error(daoExption.getMessage(), daoExption);
			throw this.getBizLogicException(daoExption, daoExption.getErrorKeyName(), daoExption
					.getMsgValues());
		}

	}

	/**
	 * @param dao
	 * @param specimenEventParameters
	 * @param specimen
	 * @param specimenIds
	 * @throws BizLogicException
	 * @throws DAOException
	 */
	private void updateDisposalEvent(DAO dao, final DisposalEventParameters disposalEventParameters)
	throws BizLogicException, DAOException
	{
		final Specimen specimen = (Specimen) HibernateMetaData
		.getProxyObjectImpl(disposalEventParameters.getSpecimen());
		final SpecimenPosition prevPosition = specimen.getSpecimenPosition();
		specimen.setSpecimenPosition(null);
		specimen.setIsAvailable(Boolean.FALSE);
		specimen.setActivityStatus(((DisposalEventParameters) disposalEventParameters)
				.getActivityStatus());
		//Update Specimen
		if (disposalEventParameters.getActivityStatus().equals(
				Status.ACTIVITY_STATUS_DISABLED.toString()))
		{
			final List specimenIds = new ArrayList();
			this.disableSubSpecimens(dao, specimen.getId().toString(), specimenIds);
		}
		dao.update(specimen);
		if (prevPosition != null)
		{
			dao.delete(prevPosition);
		}
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values.
	 * @param obj Domain object
	 * @param dao DAO object
	 * @param operation operation add/edit
	 *
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		// For bulk operations
		if (obj instanceof List)
		{
			final List events = (List) obj;

			//This map maintains the current event no being added per container
			final Map containerEventNumberMap = new HashMap();

			SpecimenEventParameters specimenEventParameters = null;

			//This is the event number corresponding to the current container.
			//This number is required to get the next location in the container that should be available
			Integer eventNumber = null;
			//Bug 15392
			Integer pos1 = null;
			Integer pos2 = null;
			for (int i = 0; i < events.size(); i++)
			{
				specimenEventParameters = (SpecimenEventParameters) events.get(i);

				if (specimenEventParameters instanceof TransferEventParameters)
				{
					final TransferEventParameters trEvent = (TransferEventParameters) specimenEventParameters;
					//Bug 15392
					pos1 = trEvent.getToPositionDimensionOne();
					pos2 = trEvent.getToPositionDimensionTwo();
					if(((pos1 == null || pos2 == null) && i!=0))
					{
						String prevToStrContName = ((TransferEventParameters)(events.get(i-1))).
						getToStorageContainer().getName();
						String toStrContName = trEvent.getToStorageContainer().getName();
						if((!Validator.isEmpty(prevToStrContName))
								&& (!Validator.isEmpty(toStrContName))
								&& (toStrContName.equals(prevToStrContName)))
						{
							pos1 = ((TransferEventParameters)(events.get(i-1))).getToPositionDimensionOne();
							pos2 = ((TransferEventParameters)(events.get(i-1))).getToPositionDimensionTwo();
						}
					}
					eventNumber = (Integer) containerEventNumberMap.get(trEvent
							.getToStorageContainer().getName());
					if (eventNumber == null)
					{
						eventNumber = Integer.valueOf(0);
					}
					containerEventNumberMap.put(trEvent.getToStorageContainer().getName(), Integer
							.valueOf(eventNumber.intValue() + 1));
				}
				else
				{
					if (eventNumber == null)
					{
						eventNumber = Integer.valueOf(0);
					}
					else
					{
						eventNumber++;
					}
				}
				boolean isValidEvent = true;
				//bug 15258 and 15260
				if (specimenEventParameters instanceof TransferEventParameters)
				{
					isValidEvent = this.validateSingleTransferEvent(specimenEventParameters, dao, operation,pos1,pos2);
				}
				else
				{
					isValidEvent = this.validateSingleEvent(specimenEventParameters, dao, operation);
				}
				if(!isValidEvent)
				{
					return false;
				}
			}
		}
		else
		{
			return this.validateSingleEvent(obj, dao, operation);
		}
		return true;
	}
	/**
	 * validates transfer event
	 * @param obj - obj
	 * @param dao - dao
	 * @param operation - operation
	 * @param numberOfEvent - numberOfEvent
	 * @param pos1 - pos1
	 * @param pos2 - pos2
	 * @return boolean
	 * @throws BizLogicException - BizLogicException
	 */
	//Bug 15392
	private boolean validateSingleTransferEvent(Object obj, DAO dao, String operation,Integer pos1,Integer pos2)
	throws BizLogicException
	{
		final SpecimenEventParameters eventParameter = (SpecimenEventParameters) obj;
		final TransferEventParameters parameter = (TransferEventParameters) eventParameter;

		Specimen specimen;
		try
		{
			specimen = (Specimen)this.retrieveSpecimenLabelName(dao, eventParameter);
			//this.getSpecimenObject(dao, parameter);
			Long fromContainerId = null;
			Integer fromPos1 =  null;
			Integer fromPos2 =  null;
			if (specimen.getSpecimenPosition() != null)
			{
				fromContainerId = specimen.getSpecimenPosition().getStorageContainer().getId();
				fromPos1 = specimen.getSpecimenPosition().getPositionDimensionOne();
				fromPos2 = specimen.getSpecimenPosition().getPositionDimensionTwo();
			}

			if ((fromContainerId == null && parameter.getFromStorageContainer() != null)
					|| (fromContainerId != null && parameter.getFromStorageContainer() == null))
			{
				throw this.getBizLogicException(null, "spec.moved.diff.loc", specimen
						.getLabel());
			}
			/*else if ((fromContainerId != null && parameter.getFromStorageContainer() != null)
					&& !((fromContainerId.equals(parameter.getFromStorageContainer().getId())
							&& fromPos1.equals(parameter.getFromPositionDimensionOne()) && fromPos2
							.equals(parameter.getFromPositionDimensionTwo()))))
			{

				throw this.getBizLogicException(null, "spec.moved.diff.loc", specimen
						.getLabel());
			}*/
			if (parameter.getToStorageContainer() != null
					&& parameter.getToStorageContainer().getName() != null)
			{

				final StorageContainer storageContainerObj = parameter.getToStorageContainer();
				final List list = this.retrieveStorageContainers(dao, parameter);

				if (!list.isEmpty())
				{
					storageContainerObj.setId((Long) list.get(0));
					parameter.setToStorageContainer(storageContainerObj);
				}
				else
				{
					//bug 15083
					final String message = ApplicationProperties
					.getValue("transfereventparameters.storageContainer.name");
					throw this.getBizLogicException(null, "errors.invalid", message+": " + specimen.getLabel());
				}

				Integer xPos = parameter.getToPositionDimensionOne();
				Integer yPos = parameter.getToPositionDimensionTwo();

				/**
				 *  Following code is added to set the x and y dimension in case only storage container is given
				 *  and x and y positions are not given
				 */
				if (yPos == null || xPos == null)
				{
					String storageValue = null;
					Position position;
					try
					{
						position = StorageContainerUtil
						.getFirstAvailablePositionsInContainer(storageContainerObj,
								this.storageContainerIds, dao,pos1,pos2);
						storageValue =
							StorageContainerUtil.getStorageValueKey
							(storageContainerObj.getName(), null,position.getXPos(),
									position.getYPos());
						storageContainerIds.add(storageValue);
					}
					catch (ApplicationException e)
					{
						this.LOGGER.error(e.getMessage(), e);
						throw new
						BizLogicException(e.getErrorKey(),e,e.getMsgValues());
					}
					if (position != null)
					{
						parameter.setToPositionDimensionOne(position.getXPos());
						parameter.setToPositionDimensionTwo(position.getYPos());
					}
					else
					{
						throw this.getBizLogicException(null, "storage.specified.full", "");
					}
					xPos = parameter.getToPositionDimensionOne();
					yPos = parameter.getToPositionDimensionTwo();
				}
				if (xPos == null || yPos == null || xPos.intValue() < 0 || yPos.intValue() < 0)
				{

					throw this.getBizLogicException(null, "errors.item.format",
							ApplicationProperties
							.getValue("transfereventparameters.toposition"));
				}
			}
			else if(parameter.getToStorageContainer() == null || ("").equals(parameter.getToStorageContainer()))
			{
				throw this.getBizLogicException(null, "errors.item.format",ApplicationProperties
						.getValue("transfereventparameters.toposition"));
			}
			if (Constants.EDIT.equals(operation))
			{
				//validateTransferEventParameters(eventParameter);
			}
		}
		catch (DAOException exp)
		{

			LOGGER.error(exp.getMessage(),exp);
			throw new
			BizLogicException(exp.getErrorKey(),exp,exp.getMsgValues());
		}
		return true;
	}

	/**
	 * @param obj - Object.
	 * @param dao - DAO object
	 * @param operation - String
	 * @param numberOfEvent - int
	 * @return boolean value
	 * @throws BizLogicException throws BizLogicException.
	 */
	private boolean validateSingleEvent(Object obj, DAO dao, String operation)
	throws BizLogicException
	{
		final SpecimenEventParameters eventParameter = (SpecimenEventParameters) obj;

		/**
		 * Start: Change for API Search   --- Jitendra 06/10/2006
		 * In Case of Api Search, previoulsy it was failing since there was default class level initialization
		 * on domain object. For example in User object, it was initialized as protected String lastName="";
		 * So we removed default class level initialization on domain object and are initializing in method
		 * setAllValues() of domain object. But in case of Api Search, default values will not get set
		 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
		 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
		 */
		ApiSearchUtil.setEventParametersDefault(eventParameter);
		//End:-  Change for API Search

		/**
		 * Name : Vijay_Pande
		 * Reviewer Name: Sachin_Lale
		 * Bug ID: 4041
		 * Patch ID: 4041_1
		 * See also: 1-3
		 * Description: Container field was not shown as mandatory on SpecimenEventsParameter page.
		 * Now container field is made mandatory. Also validation added for the same.
		 */
		final Validator validator = new Validator();
		/** -- patch ends here --*/

		switch (AppUtility.getEventParametersFormId(eventParameter))
		{
			//Case added for disposal event for bug #15185
		case Constants.DISPOSAL_EVENT_PARAMETERS_FORM_ID :
			validateDisposalEvent(dao, eventParameter);
			break;
			//case added for transfer event validation
		case Constants.TRANSFER_EVENT_PARAMETERS_FORM_ID:
			Integer pos1= null;
			Integer pos2=null;
			TransferEventParameters trEvent = (TransferEventParameters)eventParameter;
			pos1 = trEvent.getToPositionDimensionOne();
			pos2 = trEvent.getToPositionDimensionTwo();
			validateSingleTransferEvent(obj, dao, operation,pos1, pos2);
		}
		return true;
	}

	/**
	 * @param dao
	 * @param eventParameter
	 * @throws BizLogicException
	 */
	private void validateDisposalEvent(DAO dao, final SpecimenEventParameters eventParameter)
	throws BizLogicException
	{
		final DisposalEventParameters disposalEventParameters = (DisposalEventParameters) eventParameter;

		if (!disposalEventParameters.getActivityStatus().equalsIgnoreCase(
				Constants.ACTIVITY_STATUS_VALUES[2])
				&& !disposalEventParameters.getActivityStatus().equalsIgnoreCase(
						Constants.ACTIVITY_STATUS_VALUES[3]))
		{
			LOGGER.info("Invalid Activity Status");
			throw this.getBizLogicException(null, "errors.item.selected", ApplicationProperties
					.getValue("disposaleventparameters.activityStatus"));
		}
		long specimenid;
		try
		{
			specimenid = SpecimenUtil.retrieveSpecimenIdByLabelName(dao, eventParameter);

			if (specimenid != 0)
			{
				SpecimenUtil.validateSpecimenStatus( specimenid, dao);
			}
			else
			{
				throw this.getBizLogicException(null, "Invalid specimen label ", "");
			}
		}
		catch (DAOException e)
		{
			LOGGER.info(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyAsString(), e.getMessage());
		}
	}

	/**
	 * @param dao - DAO object.
	 * @param parameter - TransferEventParameters object
	 * @return List of StorageContainers objects.
	 * @throws BizLogicException throws BizLogicException
	 */
	private List retrieveStorageContainers(DAO dao, TransferEventParameters parameter)
	throws BizLogicException
	{
		try
		{
			final String sourceObjectName = StorageContainer.class.getName();
			final String[] selectColumnName = {"id"};
			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("name", parameter.getToStorageContainer()
					.getName()));
			//			final List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
			return dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
		}
		catch (final DAOException daoExp)
		{
			this.LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
			.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}
	//removed unused methods
	//	/**
	//	 * @param dao : DAO object.
	//	 * @param parameter - TransferEventParameters object
	//	 * @return Specimen obejct
	//	 * @throws BizLogicException throws BizLogicException
	//	 */
	//	private Specimen getSpecimenObject(DAO dao, SpecimenEventParameters parameter)
	//			throws BizLogicException
	//	{
	//		try
	//		{
	//			final Specimen specimen = (Specimen) dao.retrieveById(Specimen.class.getName(),
	//					parameter.getSpecimen().getId());
	//			return specimen;
	//		}
	//		catch (final DAOException daoExp)
	//		{
	//			this.LOGGER.error(daoExp.getMessage(), daoExp);
	//			throw this
	//					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
	//		}
	//	}

	/**
	 * @param dao - DAO object.
	 * @param speID - String
	 * @param specimenIds - List of specimenIds
	 * @throws BizLogicException throws BizLogicException
	 * @throws DAOException throws DAOException
	 */
	private void disableSubSpecimens(DAO dao, String speID, List specimenIds)
	throws BizLogicException, DAOException
	{
		final String sourceObjectName = Specimen.class.getName();
		final String[] selectColumnName = {edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER};
		final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
		queryWhereClause.addCondition(new EqualClause("parentSpecimen.id", Long.valueOf(speID)))
		.andOpr().addCondition(
				new NotEqualClause(Status.ACTIVITY_STATUS.toString(),
						Status.ACTIVITY_STATUS_DISABLED.toString()));

		List listOfSpecimenIDs = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
		listOfSpecimenIDs = CommonUtilities.removeNull(listOfSpecimenIDs);
		//getRelatedObjects(dao, Specimen.class, "parentSpecimen", speIDArr);

		if (!listOfSpecimenIDs.isEmpty())
		{
			if (specimenIds.containsAll(listOfSpecimenIDs))
			{
				return;
			}

			final ErrorKey errorKey = ErrorKey.getErrorKey("errors.specimen.contains.subspecimen");
			throw new BizLogicException(errorKey, null, "");
		}
		else
		{
			return;
		}
	}

	@Override
	public List getRelatedObjects(DAO dao, Class sourceClass, String[] whereColumnName,
			String[] whereColumnValue, String[] whereColumnCondition) throws BizLogicException
			{
		List list = null;
		try
		{
			final String sourceObjectName = sourceClass.getName();
			final String joinCondition = Constants.AND_JOIN_CONDITION;
			final String selectColumnName[] = {edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER};
			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);

			queryWhereClause.getWhereCondition(whereColumnName, whereColumnCondition,
					whereColumnValue, joinCondition);

			list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);

			list = CommonUtilities.removeNull(list);
		}
		catch (final DAOException e)
		{
			this.LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		return list;
			}

	/**
	 * @return Map of disabled specimen from cache.
	 * @throws Exception throws Exception
	 */
	public Map getContForDisabledSpecimenFromCache() throws Exception
	{
		// TODO if map is null
		// TODO move all code to common utility

		// getting instance of catissueCoreCacheManager and getting participantMap from cache
		final CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager
		.getInstance();
		final Map disabledconts = (TreeMap) catissueCoreCacheManager
		.getObjectFromCache(Constants.MAP_OF_CONTAINER_FOR_DISABLED_SPECIEN);
		return disabledconts;
	}

	/**
	 * @param specimenIds - List of specimen ids.
	 * @param sessionDataBean - SessionDataBean object
	 * @param queryString String
	 * @return List of specimen data
	 * @throws BizLogicException throws BizLogicException
	 */
	public List getSpecimenDataForBulkOperations(List specimenIds, SessionDataBean sessionDataBean,
			String queryString) throws BizLogicException
			{
		List specimenDataList = null;
		final JDBCDAO jdbcDao = this.openJDBCSession();
		try
		{
			final StringBuffer specimenIdsString = new StringBuffer();
			specimenIdsString.append('(');
			for (int i = 0; i < specimenIds.size(); i++)
			{
				if (i == (specimenIds.size() - 1))
				{
					specimenIdsString.append(specimenIds.get(i));
				}
				else
				{
					specimenIdsString.append(specimenIds.get(i) + ", ");
				}
			}
			specimenIdsString.append(')');

			final String sql = queryString + specimenIdsString;

			specimenDataList = jdbcDao.executeQuery(sql);

		}
		catch (final DAOException daoExp)
		{
			this.LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
			.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		finally
		{
			this.closeJDBCSession(jdbcDao);
		}
		return specimenDataList;
			}

	/**
	 * @param operation - String.
	 * @param specimenIds - List of specimen ids.
	 * @param sessionDataBean - SessionDataBean object
	 * @return List of specimen data.
	 * @throws BizLogicException throws BizLogicException
	 */
	public List getSpecimenDataForBulkOperations(String operation, List specimenIds,
			SessionDataBean sessionDataBean) throws BizLogicException
			{
		if (operation.equals(Constants.BULK_TRANSFERS))
		{
			return this.getSpecimenDataForBulkTransfers(specimenIds, sessionDataBean);
		}
		else
		{
			return this.getSpecimenDataForBulkDisposals(specimenIds, sessionDataBean);
		}
			}

	/**
	 * @param specimenIds - List of specim ids.
	 * @param sessionDataBean - SessionDataBean  object
	 * @return List of specimen data
	 * @throws BizLogicException throws BizLogicException
	 */
	public List getSpecimenDataForBulkTransfers(List specimenIds, SessionDataBean sessionDataBean)
	throws BizLogicException
	{

		final String sql = "Select specimen.IDENTIFIER, specimen.LABEL,abstractSpecimen.SPECIMEN_CLASS, Container.NAME ,"
			+ " abstractposition.POSITION_DIMENSION_ONE, abstractposition.POSITION_DIMENSION_TWO, Container.IDENTIFIER,"
			+ " abstractSpecimen.SPECIMEN_TYPE,specimen.AVAILABLE_QUANTITY "
			+ " from catissue_specimen specimen left join catissue_abstract_specimen abstractSpecimen on (abstractSpecimen.identifier=specimen.identifier )  "
			+ " left join catissue_specimen_position specimenPosition on (specimen.identifier = specimenPosition.specimen_id) "
			+ " left join catissue_container Container on (specimenPosition.Container_id=Container.IDENTIFIER) "
			+ " left join catissue_abstract_position  abstractposition  on (abstractposition.Identifier=specimenPosition.IDENTIFIER )"
			+ " where specimen.IDENTIFIER in ";
		return this.getSpecimenDataForBulkOperations(specimenIds, sessionDataBean, sql);
	}

	/**
	 * @param specimenIds - List of specimen ids.
	 * @param sessionDataBean - SessionDataBean object
	 * @return List of specimen data
	 * @throws BizLogicException throws BizLogicException
	 */
	public List getSpecimenDataForBulkDisposals(List specimenIds, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		final String sql = "Select specimen.IDENTIFIER, specimen.LABEL "
			+ "from catissue_specimen specimen " + "where specimen.IDENTIFIER in ";
		return this.getSpecimenDataForBulkOperations(specimenIds, sessionDataBean, sql);
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
	 */
	@Override
	public String getObjectId(DAO dao, Object domainObject) throws BizLogicException
	{
		String objectId = "";
		Long cpId = null;
		try
		{
			if (domainObject instanceof SpecimenEventParameters)
			{
				final SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) domainObject;
				final AbstractSpecimen specimen = specimenEventParameters.getSpecimen();

				// bug 13455 start
				if (cpId == null)
				{
					final IFactory factory = AbstractFactoryConfig.getInstance()
					.getBizLogicFactory();
					final NewSpecimenBizLogic newSpecimenBizLogic = (NewSpecimenBizLogic) factory
					.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
					cpId = newSpecimenBizLogic.getCPId(dao, cpId, specimen);
				}

				objectId = Constants.COLLECTION_PROTOCOL_CLASS_NAME + "_" + cpId;
			}
		}
		catch (final DAOException e)
		{
			this.LOGGER.error(e.getMessage(), e);
			throw new BizLogicException(e);
		}
		return objectId;
	}

	/**
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	@Override
	protected String getPrivilegeKey(Object domainObject)
	{
		return Constants.ADD_EDIT_SPECIMEN_EVENTS;
	}
	@Override
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		return isAuthorized(dao, domainObject, sessionDataBean, null);
	}
	/**
	 * (non-Javadoc)
	 * @throws UserNotAuthorizedException
	 * @throws BizLogicException
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.common.dao.DAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 *
	 */
	@Override
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean,Object uiObject)
	throws BizLogicException
	{
		boolean isAuthorized = false;
		boolean validOperation;
		String protectionElementName = null;
		String privilegeName = null;

		try
		{
			if (sessionDataBean != null && sessionDataBean.isAdmin())
			{
				return true;
			}

			//	Get the base object id against which authorization will take place
			if (domainObject instanceof List)
			{
				final List list = (List) domainObject;
				for (final Object domainObject2 : list)
				{
					if (domainObject2 instanceof TransferEventParameters)
					{
						this.checkPrivilegeOnDestinationSite(dao, domainObject2, sessionDataBean);
					}
					protectionElementName = this.getObjectId(dao, domainObject2);
					this.checkPrivilegeOnSourceSite(dao, domainObject2, sessionDataBean);
					privilegeName = this.getPrivilegeName(domainObject2);
					validOperation = AppUtility.checkPrivilegeOnCP(domainObject2,
							protectionElementName, privilegeName, sessionDataBean);
					if (!validOperation)
					{
						isAuthorized = false;
						break;
					}
					else
					{
						isAuthorized = true;
					}
				}
			}
			else
			{
				// Handle for SERIAL CHECKS, whether user has access to source site or not
				if (domainObject instanceof TransferEventParameters)
				{
					this.checkPrivilegeOnDestinationSite(dao, domainObject, sessionDataBean);
				}
				this.checkPrivilegeOnSourceSite(dao, domainObject, sessionDataBean);
				privilegeName = this.getPrivilegeName(domainObject);
				protectionElementName = this.getObjectId(dao, domainObject);
				isAuthorized = AppUtility.checkPrivilegeOnCP(domainObject, protectionElementName,
						privilegeName, sessionDataBean);
			}
		}
		catch (final ApplicationException daoExp)
		{
			this.LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
			.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		if (!isAuthorized)
		{
			throw AppUtility.getUserNotAuthorizedException(privilegeName, protectionElementName,
					domainObject.getClass().getSimpleName()); //bug 11611 and 11659
		}
		return isAuthorized;

	}

	/**
	 * @param dao : DAO object.
	 * @param domainObject - Object
	 * @param sessionDataBean -SessionDataBean object
	 * @throws BizLogicException throws BizLogicException
	 */
	private void checkPrivilegeOnDestinationSite(DAO dao, Object domainObject,
			SessionDataBean sessionDataBean) throws BizLogicException
			{
		try
		{
			final TransferEventParameters tep = (TransferEventParameters) domainObject;
			StorageContainer container = null;

			if (tep.getToStorageContainer().getName() == null
					&& tep.getToStorageContainer().getId() == null)
			{
				return; // Case when To & FROM Storage containers are the same
			}
			List list = null;
			if (tep.getToStorageContainer().getId() == null)
			{
				list = dao.retrieve(StorageContainer.class.getName(), Constants.NAME, tep
						.getToStorageContainer().getName());
			}
			else
			{
				list = dao.retrieve(StorageContainer.class.getName(), Constants.ID, tep
						.getToStorageContainer().getId());
			}
			if (list.isEmpty())
			{
				throw this.getBizLogicException(null, "sc.unableToFindContainer", "");
			}
			container = (StorageContainer) list.get(0);

			final Site site = container.getSite();
			final Set<Long> siteIdSet = new UserBizLogic().getRelatedSiteIds(sessionDataBean
					.getUserId());

			if (!siteIdSet.contains(site.getId()))
			{
				throw AppUtility.getUserNotAuthorizedException(Constants.Association, site
						.getObjectId(), domainObject.getClass().getSimpleName());
			}
		}
		catch (final ApplicationException appExp)
		{
			this.LOGGER.error(appExp.getMessage(), appExp);
			throw this
			.getBizLogicException(appExp, appExp.getErrorKeyName(), appExp.getMsgValues());
		}
			}

	/**
	 * @param dao - DAO object.
	 * @param domainObject - Object
	 * @param sessionDataBean - BizLogicException obejct
	 * @throws BizLogicException throws BizLogicException
	 */
	private void checkPrivilegeOnSourceSite(DAO dao, Object domainObject,
			SessionDataBean sessionDataBean) throws BizLogicException
			{
		final SpecimenEventParameters spe = (SpecimenEventParameters) domainObject;
		final AbstractSpecimen specimen = spe.getSpecimen();
		List<Long> list = null;
		Long siteId = null;
		try
		{
			if(specimen.getId()!=null)
			{
				// bug id 13887
				ColumnValueBean cvBean= new ColumnValueBean("id", specimen.getId());
				List specimenPositionlist =((HibernateDAO)dao).retrieveAttribute(Specimen.class, cvBean, "specimenPosition");
				if (!specimenPositionlist.isEmpty()) // Specimen is NOT Virtually Located
				{
					// bug id #13455 start
					final String query = "select specimen.specimenPosition.storageContainer.site.id from edu.wustl.catissuecore.domain.Specimen as specimen where "
						+ "specimen.id = '" + specimen.getId() + "'";
					list = dao.executeQuery(query);
					final Iterator<Long> itr = list.iterator();
					while (itr.hasNext())
					{
						siteId = itr.next();
					}
					final Set<Long> siteIdSet = new UserBizLogic().getRelatedSiteIds(sessionDataBean
							.getUserId());

					if (!siteIdSet.contains(siteId))
					{
						throw AppUtility.getUserNotAuthorizedException(Constants.Association, specimen
								.getObjectId(), domainObject.getClass().getSimpleName());
					}
				}
			}
		}
		catch (final DAOException e)
		{
			this.LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}

			}
}