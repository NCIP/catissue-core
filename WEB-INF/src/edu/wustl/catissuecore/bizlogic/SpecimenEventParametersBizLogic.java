/*
 * Created on Jul 29, 2005
 *<p>SpecimenEventParametersBizLogic Class</p>
 * This class contains the Biz Logic for all EventParameters Classes.
 * This will be the class which will be used for datatransactions of the EventParameters. 
 */

package edu.wustl.catissuecore.bizlogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.sf.ehcache.CacheException;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
import edu.wustl.catissuecore.domain.FixedEventParameters;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.condition.NotEqualClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * @author mandar_deshmukh</p>
 * This class contains the Business Logic for all EventParameters Classes.
 * This will be the class which will be used for data transactions of the EventParameters. 
 */
public class SpecimenEventParametersBizLogic extends CatissueDefaultBizLogic
{

	private transient Logger logger = Logger.getCommonLogger(SpecimenEventParametersBizLogic.class);
	/**
	 * Saves the FrozenEventParameters object in the database.
	 * @param obj The FrozenEventParameters object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws BizLogicException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
	{
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		NewSpecimenBizLogic newSpecimenBizLogic = (NewSpecimenBizLogic) factory.getBizLogic(
				Constants.NEW_SPECIMEN_FORM_ID);
		//For bulk operations 
		List specimenIds=new ArrayList();
		if (obj instanceof List)
		{
			List eventObjectsList = (List) obj;
			
			for(int i=0; i<eventObjectsList.size(); i++)
			{
				specimenIds.add(((SpecimenEventParameters)eventObjectsList.get(i)).getSpecimen().getId());
			}
			
			for(int i=0; i<eventObjectsList.size(); i++)
			{
				insertEvent(eventObjectsList.get(i), dao, sessionDataBean, newSpecimenBizLogic,specimenIds);
			}
		}
		else
		{
			insertEvent(obj, dao, sessionDataBean, newSpecimenBizLogic,specimenIds);
		}
	}

	private void insertEvent(Object obj, DAO dao, SessionDataBean sessionDataBean, NewSpecimenBizLogic newSpecimenBizLogic,List specimenIds) throws BizLogicException
	{
		try
		{			
			SpecimenEventParameters specimenEventParametersObject = (SpecimenEventParameters) obj;

			Object object = dao.retrieveById(User.class.getName(), specimenEventParametersObject.getUser().getId());
			if (object !=null)
			{
				User user = (User) object;
				// check for closed User
				checkStatus(dao, user, "User");

				specimenEventParametersObject.setUser(user);
			}
//			Ashish - 6/6/07 - performance improvement
			Object specimenObject = dao.retrieveById(Specimen.class.getName(), specimenEventParametersObject.getSpecimen().getId());
			Specimen specimen = (Specimen) specimenObject;
			//(Specimen) dao.retrieveAttribute(SpecimenEventParameters.class.getName(), specimenEventParametersObject.getSpecimen().getId(),"specimen");
			//(Specimen.class.getName(), specimenEventParametersObject.getSpecimen().getId(),Constants.SYSTEM_IDENTIFIER);
			// check for closed Specimen
			
			if (specimenEventParametersObject instanceof DisposalEventParameters)
			{
				checkStatus(dao, specimen, Constants.DISPOSAL_EVENT_PARAMETERS);
			}
			else
			{
				checkStatus(dao, specimen, Constants.SPECIMEN);
			}

			if (specimen != null)
			{
				specimenEventParametersObject.setSpecimen(specimen);
				if (specimenEventParametersObject instanceof TransferEventParameters)
				{
					TransferEventParameters transferEventParameters = (TransferEventParameters) specimenEventParametersObject;

					/*specimen.setPositionDimensionOne(transferEventParameters.getToPositionDimensionOne());
					 specimen.setPositionDimensionTwo(transferEventParameters.getToPositionDimensionTwo());*/

					//				StorageContainer storageContainer = (StorageContainer) dao.retrieve(StorageContainer.class.getName(), transferEventParameters
					//						.getToStorageContainer().getId());
					StorageContainer storageContainerObj = new StorageContainer();
					
					List stNamelist = null;
					String sourceObjectName = StorageContainer.class.getName();
					String[] selectColumnName = null;
					String[] whereColumnName = null; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
					String[] whereColumnCondition = null;
					Object[] whereColumnValue = null;
					String joinCondition = null;
					
					storageContainerObj = transferEventParameters.getToStorageContainer();
					if (storageContainerObj != null && (storageContainerObj.getId()!=null || storageContainerObj.getName()!=null))
					{
						if(storageContainerObj.getId() == null)
						{
							//storageContainerObj.setId(storageContainerObj.getId());
							selectColumnName = new String[]{"id"};
							/*	whereColumnName = new String[]{"name"}; //"storageContainer."+edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER
							whereColumnCondition = new String[]{"="};
							whereColumnValue = new Object[]{transferEventParameters.getToStorageContainer().getName()};
							joinCondition = null;*/

							QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
							queryWhereClause.addCondition(new EqualClause("name",transferEventParameters.getToStorageContainer().getName()));

							stNamelist = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
							if (!stNamelist.isEmpty())
							{
								storageContainerObj.setId((Long) (stNamelist.get(0)));
							}
						}
						else
						{
							//storageContainerObj.setId(transferEventParameters.getToStorageContainer().getId());
							selectColumnName = new String[]{"name"};
							/*whereColumnName = new String[]{"id"}; //"storageContainer."+edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER
							whereColumnCondition = new String[]{"="};
							whereColumnValue = new Object[]{transferEventParameters.getToStorageContainer().getId()};
							joinCondition = null;*/

							QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
							queryWhereClause.addCondition(new EqualClause("id",transferEventParameters.getToStorageContainer().getId()));

							stNamelist = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
							if (!stNamelist.isEmpty())
							{
								storageContainerObj.setName((String) stNamelist.get(0));
							}

						}
						
						// check for closed StorageContainer
						checkStatus(dao, storageContainerObj, "Storage Container");
	
						IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
						StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) factory.getBizLogic(
								Constants.STORAGE_CONTAINER_FORM_ID);
	
						// --- check for all validations on the storage container.
						storageContainerBizLogic.checkContainer(dao, storageContainerObj.getId().toString(), transferEventParameters
								.getToPositionDimensionOne().toString(), transferEventParameters.getToPositionDimensionTwo().toString(), sessionDataBean,false,null);
						
					
						//storageContainerObj.setHoldsSpecimenClassCollection(storageContainerBizLogic.getSpecimenClassList(storageContainerObj.getId().toString()));
						//storageContainerObj.setCollectionProtocolCollection(storageContainerBizLogic.getCollectionProtocolList(storageContainerObj.getId().toString()));
						SpecimenCollectionGroup scg = (SpecimenCollectionGroup) dao.retrieveAttribute(Specimen.class,Constants.SYSTEM_IDENTIFIER,specimen.getId(),"specimenCollectionGroup");
						specimen.setSpecimenCollectionGroup(scg);
						newSpecimenBizLogic.chkContainerValidForSpecimen(storageContainerObj, specimen, dao);
						
					}
					else
					{
						storageContainerObj = null;
					}
					
					SpecimenPosition specimenPosition = specimen.getSpecimenPosition();
					if(specimenPosition==null)
					{
						// trasfering from virtual location
						specimenPosition = new SpecimenPosition();
						specimenPosition.setSpecimen(specimen);
						specimen.setSpecimenPosition(specimenPosition);
					}
					specimenPosition.setStorageContainer(storageContainerObj);
					specimenPosition.setPositionDimensionOne(transferEventParameters.getToPositionDimensionOne());
					specimenPosition.setPositionDimensionTwo(transferEventParameters.getToPositionDimensionTwo());
					
					
					dao.update(specimen);
					transferEventParameters.setToStorageContainer(storageContainerObj);
				}
				if (specimenEventParametersObject instanceof DisposalEventParameters)
				{
					DisposalEventParameters disposalEventParameters = (DisposalEventParameters) specimenEventParametersObject;
					if (disposalEventParameters.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED))
					{
						disableSubSpecimens(dao, specimen.getId().toString(),specimenIds);

					}
					Map disabledCont = null;
					try
					{
						disabledCont = getContForDisabledSpecimenFromCache();
					}
					catch (Exception e1)
					{
						logger.debug(e1.getMessage(), e1);
						e1.printStackTrace();
					}
					if(disabledCont == null)
					{
						disabledCont = new TreeMap();
					}
					/**
					 * Name: Virender Mehta
					 * Reviewer: Sachin
					 * Retrive Storage Container from specimen
					 */
					Object objectContainer = null;
					if(specimen.getSpecimenPosition() != null && specimen.getSpecimenPosition().getStorageContainer()!=null && specimen.getSpecimenPosition().getStorageContainer().getId()!=null)
					{
						objectContainer = dao.retrieveById(StorageContainer.class.getName(), specimen.getSpecimenPosition().getStorageContainer().getId());
					}
					if(objectContainer != null)
					{
						
						StorageContainer storageContainer = (StorageContainer)objectContainer;
						addEntriesInDisabledMap(specimen, storageContainer, disabledCont);
					}
					SpecimenPosition prevPosition = specimen.getSpecimenPosition(); 
					specimen.setSpecimenPosition(null);
//					specimen.setPositionDimensionOne(null);
//					specimen.setPositionDimensionTwo(null);
//					specimen.setStorageContainer(null);

					specimen.setIsAvailable(new Boolean(false));
					specimen.setActivityStatus(disposalEventParameters.getActivityStatus());
					/**
					 * Name : Virender
					 * Reviewer: Sachin lale
					 * Calling Domain object from Proxy Object
					 */
					//Specimen proxySpecimen = (Specimen)HibernateMetaData.getProxyObjectImpl(specimen);
					dao.update(specimen);
					if(prevPosition!=null)
					{
						dao.delete(prevPosition);
					}	
					try
					{
						CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
						catissueCoreCacheManager.addObjectToCache(Constants.MAP_OF_CONTAINER_FOR_DISABLED_SPECIEN, (Serializable) disabledCont);
					}
					catch (CacheException e)
					{
						logger.debug(e.getMessage(), e);
					}

				}

			}
			
			specimen.getSpecimenEventCollection().add(specimenEventParametersObject);
			dao.insert(specimenEventParametersObject, true);
		}
		catch (DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	/**
	 * Method overridden from DefaultBizLogic.java. This method checks for the specimens status
	 * wheather it is closed. If the event is disposal only then it does not throws exception.
	 * @param dao DAo object.
	 * @param specimen Specimen object on which event is happened.
	 * @param objectType for knowing which event is happened.
	 * @throws BizLogicException DAO Exception.
	 */
	protected void checkStatus(DAO dao, Specimen specimen, String objectType) throws BizLogicException
	{
		if (specimen != null)
		{
			Long identifier = specimen.getId();
			if (identifier != null)
			{
				String className = specimen.getClass().getName();
				String activityStatus = specimen.getActivityStatus();
				if(activityStatus == null)
				{
					activityStatus = getActivityStatus(dao, className, identifier);
				}
				
				if (Status.ACTIVITY_STATUS_CLOSED.equals(activityStatus) &&
						(!Constants.DISPOSAL_EVENT_PARAMETERS.equals(objectType)))
				{
					throw getBizLogicException(null, "error.object.closed", objectType);
				}
			}
		}
	}
	
	private void addEntriesInDisabledMap(Specimen specimen, StorageContainer container, Map disabledConts)
	{
		String contNameKey = "StorageContName";
		String contIdKey = "StorageContIdKey";
		String pos1Key = "pos1";
		String pos2Key = "pos2";
		Map containerDetails = new TreeMap();
		containerDetails.put(contNameKey, container.getName());
		containerDetails.put(contIdKey, container.getId());
		containerDetails.put(pos1Key, specimen.getSpecimenPosition().getPositionDimensionOne());
		containerDetails.put(pos2Key, specimen.getSpecimenPosition().getPositionDimensionTwo());

		disabledConts.put(specimen.getId().toString(), containerDetails);

	}

	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
	{
		//For bulk operations
		if (obj instanceof List)
		{
			List events = (List)obj;
			for(int i =0; i<events.size();i++)
			{
				postInsertPerEvent(events.get(i), dao);
			}
			CatissueCoreCacheManager catissueCoreCacheManager;
			try
			{
				catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
				catissueCoreCacheManager.removeObjectFromCache(Constants.MAP_OF_CONTAINER_FOR_DISABLED_SPECIEN);
			}
			catch (CacheException e)
			{
				logger.debug(e.getMessage(),e);
			}
			
		}
		else
		{
			postInsertPerEvent(obj, dao);
		}
		

	}

	private void postInsertPerEvent(Object obj, DAO dao)
	{
		SpecimenEventParameters specimenEventParametersObject = (SpecimenEventParameters) obj;
		try
		{
			if (specimenEventParametersObject instanceof TransferEventParameters)
			{
				TransferEventParameters transferEventParameters = (TransferEventParameters) specimenEventParametersObject;

				Map containerMap = StorageContainerUtil.getContainerMapFromCache();

				if (transferEventParameters.getFromStorageContainer() != null)
				{
					StorageContainer storageContainerFrom = (StorageContainer) dao.retrieveById(StorageContainer.class.getName(), transferEventParameters
							.getFromStorageContainer().getId());
					StorageContainerUtil.insertSinglePositionInContainerMap(storageContainerFrom, containerMap, transferEventParameters
							.getFromPositionDimensionOne().intValue(), transferEventParameters.getFromPositionDimensionTwo().intValue());

				}

				StorageContainer storageContainerTo = (StorageContainer) dao.retrieveById(StorageContainer.class.getName(), transferEventParameters
						.getToStorageContainer().getId());
				StorageContainerUtil.deleteSinglePositionInContainerMap(storageContainerTo, containerMap, transferEventParameters
						.getToPositionDimensionOne().intValue(), transferEventParameters.getToPositionDimensionTwo().intValue());

			}
			if (specimenEventParametersObject instanceof DisposalEventParameters)
			{

				DisposalEventParameters disposalEventParameters = (DisposalEventParameters) specimenEventParametersObject;
				Map containerMap = StorageContainerUtil.getContainerMapFromCache();
				if (disposalEventParameters.getSpecimen() != null)
				{

					Map disabledConts = getContForDisabledSpecimenFromCache();

					Set keySet = disabledConts.keySet();
					Iterator itr = keySet.iterator();
					while (itr.hasNext())
					{
						String Id = (String) itr.next();
						Map disabledContDetails = (TreeMap) disabledConts.get(Id);
						String contNameKey = "StorageContName";
						//String contIdKey = "StorageContIdKey";
						String pos1Key = "pos1";
						String pos2Key = "pos2";

						StorageContainer cont = new StorageContainer();
						cont.setId(new Long(Id));
						cont.setName((String) disabledContDetails.get(contNameKey));
						int x = ((Integer) disabledContDetails.get(pos1Key)).intValue();
						int y = ((Integer) disabledContDetails.get(pos2Key)).intValue();

						StorageContainerUtil.insertSinglePositionInContainerMap(cont, containerMap, x, y);
					}

					/*StorageContainer storageContainer = disposalEventParameters.getSpecimen().getStorageContainer();
					 if (storageContainer != null)
					 {
					 storageContainerBizLogic.insertSinglePositionInContainerMap(storageContainer, containerMap, storageContainer
					 .getPositionDimensionOne().intValue(), storageContainer.getPositionDimensionTwo().intValue());
					 }*/

				}

			}
		}
		catch (Exception e)
		{
			logger.debug(e.getMessage(), e);
		}
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws BizLogicException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) obj;
			SpecimenEventParameters oldSpecimenEventParameters = (SpecimenEventParameters) oldObj;
			// Check for different User
			if (!specimenEventParameters.getUser().getId().equals(oldSpecimenEventParameters.getUser().getId()))
			{
				checkStatus(dao, specimenEventParameters.getUser(), "User");
			}
			Specimen specimen = (Specimen)HibernateMetaData.getProxyObjectImpl(specimenEventParameters.getSpecimen());
			List specimenIds=new ArrayList();
			if (specimenEventParameters instanceof DisposalEventParameters)
			{
				DisposalEventParameters disposalEventParameters =
					(DisposalEventParameters) specimenEventParameters;
				SpecimenPosition prevPosition = specimen.getSpecimenPosition(); 
				specimen.setSpecimenPosition(null);
				specimen.setIsAvailable(new Boolean(false));
				specimen.setActivityStatus(((DisposalEventParameters)specimenEventParameters).getActivityStatus());
				//Update Specimen
				if (disposalEventParameters.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED))
				{
					disableSubSpecimens(dao, specimen.getId().toString(), specimenIds);
				}
				dao.update(specimen);
				if(prevPosition!=null)
				{
					dao.delete(prevPosition);
				}
			}
			//Update registration
			dao.update(specimenEventParameters);
			//Audit.
			((HibernateDAO)dao).audit(obj, oldObj);
		}
		catch(DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, "dao.error", "");
		}

	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		// For bulk operations
		if (obj instanceof List)
		{
			List events = (List) obj;

			//This map maintains the current event no being added per container
			Map containerEventNumberMap = new HashMap();

			SpecimenEventParameters specimenEventParameters = null;

			//This is the event number corresponding to the current container. 
			//This number is required to get the next location in the container that should be available 
			Integer eventNumber = null;

			for (int i = 0; i < events.size(); i++)
			{
				specimenEventParameters = (SpecimenEventParameters) events.get(i);

				if (specimenEventParameters instanceof TransferEventParameters)
				{
					TransferEventParameters trEvent = (TransferEventParameters) specimenEventParameters;
					eventNumber = (Integer) containerEventNumberMap.get(trEvent.getToStorageContainer().getName());
					if (eventNumber == null)
					{
						eventNumber = new Integer(0);
					}
					containerEventNumberMap.put(trEvent.getToStorageContainer().getName(), new Integer(eventNumber.intValue() + 1));
				}
				else
				{
					if (eventNumber == null)
					{
						eventNumber = new Integer(0);
					}
					else
					{
						eventNumber++;
					}
				}

				if (!validateSingleEvent(specimenEventParameters, dao, operation, eventNumber.intValue()))
				{
					return false;
				}
			}
		}
		else
		{
			return validateSingleEvent(obj, dao, operation, 0);
		}
		return true;
	}

	private boolean validateSingleEvent(Object obj, DAO dao, String operation, int numberOfEvent) throws BizLogicException
	{
		SpecimenEventParameters eventParameter = (SpecimenEventParameters) obj;

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
		Validator validator = new Validator();
		/** -- patch ends here --*/
		
		switch (AppUtility.getEventParametersFormId(eventParameter))
		{
			case Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID :
				String storageStatus = ((CheckInCheckOutEventParameter) eventParameter).getStorageStatus();
				if (!Validator.isEnumeratedValue(Constants.STORAGE_STATUS_ARRAY, storageStatus))
				{
					throw getBizLogicException(null, "events.storageStatus.errMsg", "");
				}
				break;

			case Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID :
				String procedure = ((CollectionEventParameters) eventParameter).getCollectionProcedure();
				List procedureList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
				if (!Validator.isEnumeratedValue(procedureList, procedure))
				{
					throw getBizLogicException(null, "events.collectionProcedure.errMsg", "");
				}

				String container = ((CollectionEventParameters) eventParameter).getContainer();
				List containerList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CONTAINER, null);
				if (!Validator.isEnumeratedOrNullValue(containerList, container))
				{
					throw getBizLogicException(null, "events.container.errMsg", "");
				}
				/**
				 * Name : Vijay_Pande
				 * Patch ID: 4041_2 
				 * See also: 1-3
				 * Description: Validation for container field.
				 */
				if (!validator.isValidOption(container))
				{
					String message = ApplicationProperties.getValue("collectioneventparameters.container");
					throw getBizLogicException(null, "errors.item.required", message);
				}
				/** -- patch ends here --*/
				break;

			case Constants.EMBEDDED_EVENT_PARAMETERS_FORM_ID :
				String embeddingMedium = ((EmbeddedEventParameters) eventParameter).getEmbeddingMedium();
				List embeddingMediumList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_EMBEDDING_MEDIUM, null);
				if (!Validator.isEnumeratedValue(embeddingMediumList, embeddingMedium))
				{
					throw getBizLogicException(null, "events.embeddingMedium.errMsg", "");
				}
				break;

			case Constants.FIXED_EVENT_PARAMETERS_FORM_ID :
				String fixationType = ((FixedEventParameters) eventParameter).getFixationType();
				List fixationTypeList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_FIXATION_TYPE, null);
				if (!Validator.isEnumeratedValue(fixationTypeList, fixationType))
				{
					
					throw getBizLogicException(null, "events.fixationType.errMsg", "");
				}
				break;

			case Constants.FROZEN_EVENT_PARAMETERS_FORM_ID :
				String method = ((FrozenEventParameters) eventParameter).getMethod();
				List methodList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_METHOD, null);
				if (!Validator.isEnumeratedValue(methodList, method))
				{
				
					throw getBizLogicException(null,"events.method.errMsg", "");
				}
				break;

			case Constants.RECEIVED_EVENT_PARAMETERS_FORM_ID :
				String quality = ((ReceivedEventParameters) eventParameter).getReceivedQuality();
				List qualityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RECEIVED_QUALITY, null);
				if (!Validator.isEnumeratedValue(qualityList, quality))
				{
					
					throw getBizLogicException(null,"events.receivedQuality.errMsg", "");
				}
				break;

			case Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID :
				String histQuality = ((TissueSpecimenReviewEventParameters) eventParameter).getHistologicalQuality();
				List histologicalQualityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_HISTOLOGICAL_QUALITY, null);
				if (!Validator.isEnumeratedOrNullValue(histologicalQualityList, histQuality))
				{
					
					throw getBizLogicException(null,"events.histologicalQuality.errMsg", "");
				}
				break;

			case Constants.TRANSFER_EVENT_PARAMETERS_FORM_ID :
				TransferEventParameters parameter = (TransferEventParameters) eventParameter;
				Specimen specimen = getSpecimenObject(dao, parameter);
//				Long fromContainerId = (Long) dao.retrieveAttribute(Specimen.class.getName(),parameter.getSpecimen().getId(),"specimenPosition");
//				Integer pos1 = (Integer) dao.retrieveAttribute(Specimen.class.getName(),parameter.getSpecimen().getId(),"specimenPosition.positionDimensionOne");
//				Integer pos2 = (Integer) dao.retrieveAttribute(Specimen.class.getName(),parameter.getSpecimen().getId(),"specimenPositionpositionDimensionTwo");
				Long fromContainerId=null;
				Integer pos1=null;
				Integer pos2=null;
				if(specimen.getSpecimenPosition()!=null)
				{
					fromContainerId = specimen.getSpecimenPosition().getStorageContainer().getId();
					pos1 = specimen.getSpecimenPosition().getPositionDimensionOne();
					pos2 = specimen.getSpecimenPosition().getPositionDimensionTwo();
				}
				
				if((fromContainerId == null && parameter.getFromStorageContainer() != null) || (fromContainerId != null && parameter.getFromStorageContainer() == null))
				{
					throw getBizLogicException(null,"dao.error", "Specimen "+specimen.getLabel()+" " +
							"had been moved to another location! Updated the locations. Please redo the transfers.");
				}
				else if((fromContainerId != null && parameter.getFromStorageContainer() != null) && 
						!((fromContainerId.equals(parameter.getFromStorageContainer().getId()) && 
								pos1.equals(parameter.getFromPositionDimensionOne())
										&& pos2.equals(parameter.getFromPositionDimensionTwo()))))
				{
					
					throw getBizLogicException(null,"dao.error", "Specimen "+specimen.getLabel()+" " +
							"had been moved to another location! Updated the locations. Please redo the transfers.");
				}
				if (parameter.getToStorageContainer() != null && parameter.getToStorageContainer().getName() != null)				
				{
					
					StorageContainer storageContainerObj = parameter.getToStorageContainer();			
					List list = retrieveStorageContainers(dao, parameter);
					
					if (!list.isEmpty())
					{
						storageContainerObj.setId((Long) list.get(0));
						parameter.setToStorageContainer(storageContainerObj);
					}
					else
					{
						String message = ApplicationProperties.getValue("transfereventparameters.toposition");
						throw getBizLogicException(null, "errors.invalid", message+" for specimen: "+specimen.getLabel());
					}

					Integer xPos = parameter.getToPositionDimensionOne();
					Integer yPos = parameter.getToPositionDimensionTwo();
					boolean isContainerFull = false;
					/**
					 *  Following code is added to set the x and y dimension in case only storage container is given 
					 *  and x and y positions are not given 
					 */
					if (yPos == null || xPos == null)
					{
						isContainerFull = true;
						Map containerMapFromCache = null;
						containerMapFromCache = (TreeMap) StorageContainerUtil.getContainerMapFromCache();
						isContainerFull = setPositions(numberOfEvent, parameter,
								storageContainerObj, isContainerFull, containerMapFromCache);
							xPos = parameter.getToPositionDimensionOne();
						    yPos = parameter.getToPositionDimensionTwo();
					}
					if(isContainerFull)
					{
						
						throw getBizLogicException(null, "dao.error","The Storage Container you specified is full");
					}
					else if (xPos == null || yPos == null || xPos.intValue() < 0 || yPos.intValue() < 0)
					{
						
						throw getBizLogicException(null, "errors.item.format",ApplicationProperties
								.getValue("transfereventparameters.toposition"));
					}
				}
				if (Constants.EDIT.equals(operation))
				{
					//validateTransferEventParameters(eventParameter);
				}
				break;
		}
		return true;
	}

	private List retrieveStorageContainers(DAO dao,
			TransferEventParameters parameter) throws BizLogicException 
	{
		try
		{
			String sourceObjectName = StorageContainer.class.getName();
			String[] selectColumnName = {"id"};
			/*String[] whereColumnName = {"name"};
		String[] whereColumnCondition = {"="};
		Object[] whereColumnValue = {parameter.getToStorageContainer().getName()};
		String joinCondition = null;*/

			QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("name",parameter.getToStorageContainer().getName()));
			List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
			return list;
		}
		catch(DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	private Specimen getSpecimenObject(DAO dao,
			TransferEventParameters parameter) throws BizLogicException
	{
		try
		{
			Specimen specimen = (Specimen)dao.retrieveById(Specimen.class.getName(), parameter.getSpecimen().getId());
			return specimen;
		}
		catch(DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	/**
	 * @param numberOfEvent
	 * @param parameter
	 * @param storageContainerObj
	 * @param isContainerFull
	 * @param containerMapFromCache
	 * @return
	 */
	private boolean setPositions(int numberOfEvent, TransferEventParameters parameter,
			StorageContainer storageContainerObj, boolean isContainerFull, Map containerMapFromCache)
	{
		List list;
		if (containerMapFromCache != null)
		{
				Iterator itr = containerMapFromCache.keySet().iterator();
				while (itr.hasNext())
				{
					NameValueBean nvb = (NameValueBean) itr.next();
					if(nvb.getValue().toString().equals(storageContainerObj.getId().toString()))
					{
						Map tempMap = (Map) containerMapFromCache.get(nvb);
						Iterator tempIterator = tempMap.keySet().iterator();
						while (tempIterator.hasNext())
						{
							NameValueBean nvb1 = (NameValueBean) tempIterator.next();
							list = (List) tempMap.get(nvb1);
							NameValueBean nvb2;
							//To get the next available location for this event number assuming the pervious ones were allocated to previous events in the list
							if(numberOfEvent >= list.size())
							{
								numberOfEvent= numberOfEvent - list.size();
								continue;
							}
							nvb2 = (NameValueBean) list.get(numberOfEvent);
							parameter.setToPositionDimensionOne(new Integer(nvb1.getValue()));
							parameter.setToPositionDimensionTwo(new Integer(nvb2.getValue()));
						    isContainerFull = false;
						    break;
						}
						break;
					}
				}
			}
		return isContainerFull;
	}

	private void validateTransferEventParameters(SpecimenEventParameters eventParameter) throws BizLogicException
	{
		TransferEventParameters parameter = (TransferEventParameters) eventParameter;
		Object object =  retrieve(TransferEventParameters.class.getName(), parameter.getId());
		if (object != null)
		{
			TransferEventParameters parameterCopy = (TransferEventParameters) object;
			String positionDimensionOne = parameterCopy.getToPositionDimensionOne().toString();
			String positionDimensionTwo = parameterCopy.getToPositionDimensionTwo().toString();
			String storageContainer = parameterCopy.getToStorageContainer().getId().toString();

			if (!positionDimensionOne.equals(parameter.getToPositionDimensionOne().toString())
					|| !positionDimensionTwo.equals(parameter.getToPositionDimensionTwo().toString())
					|| !storageContainer.equals(parameter.getToStorageContainer().getId().toString()))
			{
				throw getBizLogicException(null, "events.toPosition.errMsg", "");
			}
		}
	}

	private void setDisableToSubSpecimen(Specimen specimen)
	{
		if (specimen != null)
		{
			Iterator iterator = specimen.getChildSpecimenCollection().iterator();
			while (iterator.hasNext())
			{
				Specimen childSpecimen = (Specimen) iterator.next();
				childSpecimen.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
				setDisableToSubSpecimen(childSpecimen);
			}
		}
	}

	private void disableSubSpecimens(DAO dao, String speID,List specimenIds) throws BizLogicException,DAOException
	{
		String sourceObjectName = Specimen.class.getName();
		String[] selectColumnName = {edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER};
		/*String[] whereColumnName = {"parentSpecimen.id",Constants.ACTIVITY_STATUS};
		String[] whereColumnCondition = {"=", "!="};
		Object[] whereColumnValue = {new Long(speID), Status.ACTIVITY_STATUS_DISABLED.toString()};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		*/
		QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
		queryWhereClause.addCondition(new EqualClause("parentSpecimen.id",Long.valueOf(speID))).andOpr().
		addCondition(new NotEqualClause(Status.ACTIVITY_STATUS.toString(),Status.ACTIVITY_STATUS_DISABLED.toString()));
		
		List listOfSpecimenIDs = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
			listOfSpecimenIDs = edu.wustl.common.util.Utility.removeNull(listOfSpecimenIDs);
			//getRelatedObjects(dao, Specimen.class, "parentSpecimen", speIDArr);
			
			if (!listOfSpecimenIDs.isEmpty())
			{
				if(specimenIds.containsAll(listOfSpecimenIDs))
				{
					return;
				}
			
				ErrorKey errorKey = ErrorKey.getErrorKey("errors.specimen.contains.subspecimen");
				throw new BizLogicException(errorKey,new Exception() ,"SpecimenEventParametersBizLogic.java :");
			}
			else
			{
				return;
			}	
	}

	public List getRelatedObjects(DAO dao, Class sourceClass, String[] whereColumnName, String[] whereColumnValue, String[] whereColumnCondition)
			throws BizLogicException
	{
		List list = null;
		try 
		{
			String sourceObjectName = sourceClass.getName();
			String joinCondition = Constants.AND_JOIN_CONDITION;
			String selectColumnName[] = {edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER};
			QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);

			queryWhereClause.getWhereCondition(whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);


			list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);

			list = edu.wustl.common.util.Utility.removeNull(list);
		} catch (DAOException e) {
			logger.debug(e.getMessage(), e);
			throw getBizLogicException(e, "dao.error", "SpecimenEventParametersBizLogic.java :");
		}
		return list;
	}

	public Map getContForDisabledSpecimenFromCache() throws Exception
	{
		// TODO if map is null
		// TODO move all code to common utility

		// getting instance of catissueCoreCacheManager and getting participantMap from cache
		CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
		Map disabledconts = (TreeMap) catissueCoreCacheManager.getObjectFromCache(Constants.MAP_OF_CONTAINER_FOR_DISABLED_SPECIEN);
		return disabledconts;
	}
	
	public List getSpecimenDataForBulkOperations(List specimenIds, SessionDataBean sessionDataBean, String queryString) throws BizLogicException
	{
		List specimenDataList = null;
		StringBuffer specimenIdsString = new StringBuffer();
		specimenIdsString.append("(");
		for (int i=0; i< specimenIds.size(); i++)
		{
			if(i == (specimenIds.size()-1))
			{
				specimenIdsString.append(specimenIds.get(i));
			}
			else
			{
				specimenIdsString.append(specimenIds.get(i)+", ");
			}
		}
		specimenIdsString.append(")");
		
		String sql =queryString+specimenIdsString;
		specimenDataList = executeQuery(sql);
		return specimenDataList;
	}
	
	public List getSpecimenDataForBulkOperations(String operation, List specimenIds, SessionDataBean sessionDataBean) throws BizLogicException
	{
		if(operation.equals(Constants.BULK_TRANSFERS))
		{
			return getSpecimenDataForBulkTransfers(specimenIds, sessionDataBean);
		}
		else
		{
			return getSpecimenDataForBulkDisposals(specimenIds, sessionDataBean);
		}
	}
	
	public List getSpecimenDataForBulkTransfers(List specimenIds, SessionDataBean sessionDataBean) throws BizLogicException
	{
		
		String sql ="Select specimen.IDENTIFIER, specimen.LABEL, abstractSpecimen.SPECIMEN_CLASS, Container.NAME , abstractposition.POSITION_DIMENSION_ONE, abstractposition.POSITION_DIMENSION_TWO, Container.IDENTIFIER " 
			+" from catissue_specimen specimen left join catissue_abstract_specimen abstractSpecimen on (abstractSpecimen.identifier=specimen.identifier )  "
			+ "left join catissue_specimen_position specimenPosition on (specimen.identifier = specimenPosition.specimen_id) "
			+" left join catissue_container Container on (specimenPosition.Container_id=Container.IDENTIFIER) "
			+" left join catissue_abstract_position  abstractposition  on (abstractposition.Identifier=specimenPosition.IDENTIFIER )"
			+" where specimen.IDENTIFIER in ";
		return getSpecimenDataForBulkOperations(specimenIds, sessionDataBean, sql);
	}
	
	public List getSpecimenDataForBulkDisposals(List specimenIds, SessionDataBean sessionDataBean) throws BizLogicException
	{
		String sql = "Select specimen.IDENTIFIER, specimen.LABEL "
			+"from catissue_specimen specimen "
			+"where specimen.IDENTIFIER in ";
		return getSpecimenDataForBulkOperations(specimenIds, sessionDataBean, sql);
	}
	
	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
	 */
	public String getObjectId(DAO dao, Object domainObject) 
	{
		String objectId = "";
		try 
		{

			//TODO Optimize This code with HQL
			if(domainObject instanceof SpecimenEventParameters)
			{
				SpecimenCollectionGroup scg = null;
				SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) domainObject;
				AbstractSpecimen specimen = (AbstractSpecimen) specimenEventParameters.getSpecimen();


				specimen = (Specimen) dao.retrieveById(Specimen.class.getName(), specimen.getId());
				Specimen specimen1 = (Specimen) specimen;
				scg = specimen1.getSpecimenCollectionGroup();

				CollectionProtocolRegistration cpr = scg.getCollectionProtocolRegistration();
				CollectionProtocol cp = cpr.getCollectionProtocol();
				objectId = Constants.COLLECTION_PROTOCOL_CLASS_NAME+"_"+cp.getId();


			}
		} 
		catch (DAOException e) 
		{
			logger.debug(e.getMessage(), e);
		}
		return objectId;
	}
	
	/**
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	protected String getPrivilegeKey(Object domainObject)
    {
    	return Constants.ADD_EDIT_SPECIMEN_EVENTS;
    }
	
	/**
	 * (non-Javadoc)
	 * @throws UserNotAuthorizedException 
	 * @throws BizLogicException 
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.common.dao.DAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 * 
	 */
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean) throws BizLogicException
	{
		boolean isAuthorized = false;
		boolean validOperation;
		String protectionElementName = null;
		String privilegeName = null;
		
		try
		{
		if(sessionDataBean != null && sessionDataBean.isAdmin())
		{
			return true;
		}
		
		//	Get the base object id against which authorization will take place 
		if(domainObject instanceof List)
		{
		    List list = (List) domainObject;
			for(Object domainObject2 : list)
			{
				if(domainObject2 instanceof TransferEventParameters)
				{
					checkPrivilegeOnDestinationSite(dao, domainObject2, sessionDataBean);
				}
				protectionElementName = getObjectId(dao, domainObject2);
				checkPrivilegeOnSourceSite(dao, domainObject2, sessionDataBean);
				privilegeName = getPrivilegeName(domainObject2);
				validOperation = AppUtility.checkPrivilegeOnCP(domainObject2, protectionElementName, privilegeName, sessionDataBean);
				if(!validOperation)
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
			if(domainObject instanceof TransferEventParameters)
			{
				checkPrivilegeOnDestinationSite(dao, domainObject, sessionDataBean);
			}
			checkPrivilegeOnSourceSite(dao, domainObject, sessionDataBean);
			privilegeName = getPrivilegeName(domainObject);
			protectionElementName = getObjectId(dao, domainObject);
			isAuthorized = AppUtility.checkPrivilegeOnCP(domainObject, protectionElementName, privilegeName, sessionDataBean);
		}

		if (!isAuthorized)
        {
			throw AppUtility.getUserNotAuthorizedException(privilegeName, protectionElementName);   //bug 11611 and 11659
        }
		return isAuthorized;	
		}
		catch(ApplicationException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, "dao.error", "");
		}

	}

	private void checkPrivilegeOnDestinationSite(DAO dao, Object domainObject, SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			TransferEventParameters tep = (TransferEventParameters) domainObject;
			StorageContainer sc = null;

			if(tep.getToStorageContainer().getName() == null && tep.getToStorageContainer().getId() == null)
			{
				return; // Case when To & FROM Storage containers are the same
			}
			List list = null;
			if(tep.getToStorageContainer().getId() == null)
			{
				list = dao.retrieve(StorageContainer.class.getName(), Constants.NAME, tep.getToStorageContainer().getName());
			}
			else
			{
				list = dao.retrieve(StorageContainer.class.getName(), Constants.ID, tep.getToStorageContainer().getId());
			}
			if(list.isEmpty())
			{
				throw getBizLogicException(null, "sc.unableToFindContainer", "");
			}
			sc = (StorageContainer) list.get(0);

			Site site = sc.getSite();
			Set<Long> siteIdSet = new UserBizLogic().getRelatedSiteIds(sessionDataBean.getUserId());

			if(!siteIdSet.contains(site.getId()))
			{
				throw AppUtility.getUserNotAuthorizedException(Constants.Association, site.getObjectId());
			}	
		}
		catch(ApplicationException appExp)
		{
			logger.debug(appExp.getMessage(), appExp);
			throw getBizLogicException(appExp, "dao.error", "");
		}
	}

	private void checkPrivilegeOnSourceSite(DAO dao, Object domainObject, SessionDataBean sessionDataBean) throws BizLogicException
	{
		SpecimenPosition specimenPosition = null;
	
		SpecimenEventParameters spe = (SpecimenEventParameters) domainObject;
		AbstractSpecimen specimen = (AbstractSpecimen) spe.getSpecimen();
		
		try 
		{
			
			specimen = (Specimen) dao.retrieveById(Specimen.class.getName(), specimen.getId());
			Specimen specimen1 = (Specimen) specimen;
			specimenPosition = specimen1.getSpecimenPosition();
			
			if(specimenPosition != null) // Specimen is NOT Virtually Located
			{
				StorageContainer sc = specimenPosition.getStorageContainer();
				Site site = sc.getSite();
				
				Set<Long> siteIdSet = new UserBizLogic().getRelatedSiteIds(sessionDataBean.getUserId());
				
				if(!siteIdSet.contains(site.getId()))
				{
					//bug 11611 and 11659 start
					UserNotAuthorizedException ex = AppUtility.getUserNotAuthorizedException(Constants.Association,specimen.getObjectId());//
					if(ex.getBaseObject()==null)
					{
						ex.setBaseObject("Specimen");
					}
					
					throw getBizLogicException(ex, "dao.error", "");
					//bug 11611 and 11659 end
				}
			}
		} 
		catch (DAOException e) 
		{
			logger.debug(e.getMessage(), e);
		}
		finally
		{
			
		}		
	}
} 