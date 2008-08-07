/*
 * Created on Jul 29, 2005
 *<p>SpecimenEventParametersBizLogic Class</p>
 * This class contains the Biz Logic for all EventParameters Classes.
 * This will be the class which will be used for datatransactions of the EventParameters. 
 */

package edu.wustl.catissuecore.bizlogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh</p>
 * This class contains the Business Logic for all EventParameters Classes.
 * This will be the class which will be used for data transactions of the EventParameters. 
 */
public class SpecimenEventParametersBizLogic extends DefaultBizLogic
{

	/**
	 * Saves the FrozenEventParameters object in the database.
	 * @param obj The FrozenEventParameters object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		NewSpecimenBizLogic newSpecimenBizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(
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

	private void insertEvent(Object obj, DAO dao, SessionDataBean sessionDataBean, NewSpecimenBizLogic newSpecimenBizLogic,List specimenIds) throws DAOException, UserNotAuthorizedException
	{
		try
		{
			SpecimenEventParameters specimenEventParametersObject = (SpecimenEventParameters) obj;

			Object object = dao.retrieve(User.class.getName(), specimenEventParametersObject.getUser().getId());
			if (object != null)
			{
				User user = (User) object;

				// check for closed User
				checkStatus(dao, user, "User");

				specimenEventParametersObject.setUser(user);
			}
//			Ashish - 6/6/07 - performance improvement
			Object specimenObject = dao.retrieve(Specimen.class.getName(), specimenEventParametersObject.getSpecimen().getId());
			Specimen specimen = (Specimen) specimenObject;
			//(Specimen) dao.retrieveAttribute(SpecimenEventParameters.class.getName(), specimenEventParametersObject.getSpecimen().getId(),"specimen");
			//(Specimen.class.getName(), specimenEventParametersObject.getSpecimen().getId(),Constants.SYSTEM_IDENTIFIER);
			// check for closed Specimen
			
			checkStatus(dao, specimen, "Specimen");

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
							whereColumnName = new String[]{"name"}; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
							whereColumnCondition = new String[]{"="};
							whereColumnValue = new Object[]{transferEventParameters.getToStorageContainer().getName()};
							joinCondition = null;
							stNamelist = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue,
									joinCondition);
							if (!stNamelist.isEmpty())
							{
								storageContainerObj.setId((Long) (stNamelist.get(0)));
							}
						}
						else
						{
							//storageContainerObj.setId(transferEventParameters.getToStorageContainer().getId());
							selectColumnName = new String[]{"name"};
							whereColumnName = new String[]{"id"}; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
							whereColumnCondition = new String[]{"="};
							whereColumnValue = new Object[]{transferEventParameters.getToStorageContainer().getId()};
							joinCondition = null;
							stNamelist = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue,
									joinCondition);
							if (!stNamelist.isEmpty())
							{
								storageContainerObj.setName((String) stNamelist.get(0));
							}
							
						}
						
						// check for closed StorageContainer
						checkStatus(dao, storageContainerObj, "Storage Container");
	
						StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(
								Constants.STORAGE_CONTAINER_FORM_ID);
	
						// --- check for all validations on the storage container.
						storageContainerBizLogic.checkContainer(dao, storageContainerObj.getId().toString(), transferEventParameters
								.getToPositionDimensionOne().toString(), transferEventParameters.getToPositionDimensionTwo().toString(), sessionDataBean,false);
						
					
						//storageContainerObj.setHoldsSpecimenClassCollection(storageContainerBizLogic.getSpecimenClassList(storageContainerObj.getId().toString()));
						//storageContainerObj.setCollectionProtocolCollection(storageContainerBizLogic.getCollectionProtocolList(storageContainerObj.getId().toString()));
						SpecimenCollectionGroup scg = (SpecimenCollectionGroup) dao.retrieveAttribute(Specimen.class.getName(),specimen.getId(),"specimenCollectionGroup");
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
					
					
					dao.update(specimen, sessionDataBean, true, true, false);
					transferEventParameters.setToStorageContainer(storageContainerObj);
				}
				if (specimenEventParametersObject instanceof DisposalEventParameters)
				{
					DisposalEventParameters disposalEventParameters = (DisposalEventParameters) specimenEventParametersObject;
					if (disposalEventParameters.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
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
						objectContainer = dao.retrieve(StorageContainer.class.getName(), specimen.getSpecimenPosition().getStorageContainer().getId());
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
					dao.update(specimen, sessionDataBean, true, true, false);
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

					}

				}

			}

			dao.insert(specimenEventParametersObject, sessionDataBean, true, true);
		}
		catch (SMException e)
		{
			throw handleSMException(e);
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

	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
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
				Logger.out.debug(e.getMessage());
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
					StorageContainer storageContainerFrom = (StorageContainer) dao.retrieve(StorageContainer.class.getName(), transferEventParameters
							.getFromStorageContainer().getId());
					StorageContainerUtil.insertSinglePositionInContainerMap(storageContainerFrom, containerMap, transferEventParameters
							.getFromPositionDimensionOne().intValue(), transferEventParameters.getFromPositionDimensionTwo().intValue());

				}

				StorageContainer storageContainerTo = (StorageContainer) dao.retrieve(StorageContainer.class.getName(), transferEventParameters
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

		}
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) obj;
		SpecimenEventParameters oldSpecimenEventParameters = (SpecimenEventParameters) oldObj;

		//Check for Closed Specimen
		//checkStatus(dao, specimenEventParameters.getSpecimen(), "Specimen" );

		// Check for different User
		if (!specimenEventParameters.getUser().getId().equals(oldSpecimenEventParameters.getUser().getId()))
		{
			checkStatus(dao, specimenEventParameters.getUser(), "User");
		}

		// check for transfer event
		//			if (specimenEventParameters.getSpecimen() != null)
		//			{
		//			    if (specimenEventParameters instanceof TransferEventParameters)
		//			    {
		//			        TransferEventParameters transferEventParameters = (TransferEventParameters)specimenEventParameters;
		//			        TransferEventParameters oldTransferEventParameters = (TransferEventParameters)oldSpecimenEventParameters;
		//				    
		//				    StorageContainer storageContainer = transferEventParameters.getToStorageContainer();
		//				    StorageContainer oldstorageContainer = oldTransferEventParameters.getToStorageContainer();
		//				    Logger.out.debug("StorageContainer match : " + storageContainer.equals(oldstorageContainer ) );
		//				    
		//				    // check for closed StorageContainer
		//				    if(!storageContainer.getId().equals(oldstorageContainer.getId()) )
		//				    	checkStatus(dao, storageContainer, "Storage Container" );
		//			    }
		//			}

		//Update registration
		dao.update(specimenEventParameters, sessionDataBean, true, true, false);

		//Audit.
		dao.audit(obj, oldObj, sessionDataBean, true);
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
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

	private boolean validateSingleEvent(Object obj, DAO dao, String operation, int numberOfEvent) throws DAOException
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
		
		switch (Utility.getEventParametersFormId(eventParameter))
		{
			case Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID :
				String storageStatus = ((CheckInCheckOutEventParameter) eventParameter).getStorageStatus();
				if (!Validator.isEnumeratedValue(Constants.STORAGE_STATUS_ARRAY, storageStatus))
				{
					throw new DAOException(ApplicationProperties.getValue("events.storageStatus.errMsg"));
				}
				break;

			case Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID :
				String procedure = ((CollectionEventParameters) eventParameter).getCollectionProcedure();
				List procedureList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
				if (!Validator.isEnumeratedValue(procedureList, procedure))
				{
					throw new DAOException(ApplicationProperties.getValue("events.collectionProcedure.errMsg"));
				}

				String container = ((CollectionEventParameters) eventParameter).getContainer();
				List containerList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CONTAINER, null);
				if (!Validator.isEnumeratedOrNullValue(containerList, container))
				{
					throw new DAOException(ApplicationProperties.getValue("events.container.errMsg"));
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
					throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
				}
				/** -- patch ends here --*/
				break;

			case Constants.EMBEDDED_EVENT_PARAMETERS_FORM_ID :
				String embeddingMedium = ((EmbeddedEventParameters) eventParameter).getEmbeddingMedium();
				List embeddingMediumList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_EMBEDDING_MEDIUM, null);
				if (!Validator.isEnumeratedValue(embeddingMediumList, embeddingMedium))
				{
					throw new DAOException(ApplicationProperties.getValue("events.embeddingMedium.errMsg"));
				}
				break;

			case Constants.FIXED_EVENT_PARAMETERS_FORM_ID :
				String fixationType = ((FixedEventParameters) eventParameter).getFixationType();
				List fixationTypeList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_FIXATION_TYPE, null);
				if (!Validator.isEnumeratedValue(fixationTypeList, fixationType))
				{
					throw new DAOException(ApplicationProperties.getValue("events.fixationType.errMsg"));
				}
				break;

			case Constants.FROZEN_EVENT_PARAMETERS_FORM_ID :
				String method = ((FrozenEventParameters) eventParameter).getMethod();
				List methodList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_METHOD, null);
				if (!Validator.isEnumeratedValue(methodList, method))
				{
					throw new DAOException(ApplicationProperties.getValue("events.method.errMsg"));
				}
				break;

			case Constants.RECEIVED_EVENT_PARAMETERS_FORM_ID :
				String quality = ((ReceivedEventParameters) eventParameter).getReceivedQuality();
				List qualityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RECEIVED_QUALITY, null);
				if (!Validator.isEnumeratedValue(qualityList, quality))
				{
					throw new DAOException(ApplicationProperties.getValue("events.receivedQuality.errMsg"));
				}
				break;

			case Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID :
				String histQuality = ((TissueSpecimenReviewEventParameters) eventParameter).getHistologicalQuality();
				List histologicalQualityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_HISTOLOGICAL_QUALITY, null);
				if (!Validator.isEnumeratedOrNullValue(histologicalQualityList, histQuality))
				{
					throw new DAOException(ApplicationProperties.getValue("events.histologicalQuality.errMsg"));
				}
				break;

			case Constants.TRANSFER_EVENT_PARAMETERS_FORM_ID :
				TransferEventParameters parameter = (TransferEventParameters) eventParameter;
				Specimen specimen = (Specimen)dao.retrieve(Specimen.class.getName(), parameter.getSpecimen().getId());
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
					throw new DAOException("Specimen "+specimen.getLabel()+" had been moved to another location! Updated the locations. Please redo the transfers.");
				}
				else if((fromContainerId != null && parameter.getFromStorageContainer() != null) && 
						!((fromContainerId.equals(parameter.getFromStorageContainer().getId()) && 
								pos1.equals(parameter.getFromPositionDimensionOne())
										&& pos2.equals(parameter.getFromPositionDimensionTwo()))))
				{
					throw new DAOException("Specimen "+specimen.getLabel()+" had been moved to another location! Updated the locations. Please redo the transfers.");
				}
				if (parameter.getToStorageContainer() != null && parameter.getToStorageContainer().getName() != null)				
				{
					
					StorageContainer storageContainerObj = parameter.getToStorageContainer();			
					String sourceObjectName = StorageContainer.class.getName();
					String[] selectColumnName = {"id"};
					String[] whereColumnName = {"name"};
					String[] whereColumnCondition = {"="};
					Object[] whereColumnValue = {parameter.getToStorageContainer().getName()};
					String joinCondition = null;

					List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
					
					if (!list.isEmpty())
					{
						storageContainerObj.setId((Long) list.get(0));
						parameter.setToStorageContainer(storageContainerObj);
					}
					else
					{
						String message = ApplicationProperties.getValue("transfereventparameters.toposition");
						throw new DAOException(ApplicationProperties.getValue("errors.invalid", message+" for specimen: "+specimen.getLabel()));
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
						throw new DAOException("The Storage Container you specified is full");
					}
					else if (xPos == null || yPos == null || xPos.intValue() < 0 || yPos.intValue() < 0)
					{
						throw new DAOException(ApplicationProperties.getValue("errors.item.format", ApplicationProperties
								.getValue("transfereventparameters.toposition")));
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

	private void validateTransferEventParameters(SpecimenEventParameters eventParameter) throws DAOException
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
				throw new DAOException(ApplicationProperties.getValue("events.toPosition.errMsg"));
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
				childSpecimen.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED);
				setDisableToSubSpecimen(childSpecimen);
			}
		}
	}

	private void disableSubSpecimens(DAO dao, String speID,List specimenIds) throws DAOException
	{
		String sourceObjectName = Specimen.class.getName();
		String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnName = {"parentSpecimen.id",Constants.ACTIVITY_STATUS};
		String[] whereColumnCondition = {"=", "!="};
		Object[] whereColumnValue = {new Long(speID), Constants.ACTIVITY_STATUS_DISABLED};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		List listOfSpecimenIDs = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue,
				joinCondition);
			listOfSpecimenIDs = Utility.removeNull(listOfSpecimenIDs);
			//getRelatedObjects(dao, Specimen.class, "parentSpecimen", speIDArr);
			
			if (!listOfSpecimenIDs.isEmpty())
			{
				if(specimenIds.containsAll(listOfSpecimenIDs))
				{
					return;
				}
				throw new DAOException(ApplicationProperties.getValue("errors.specimen.contains.subspecimen"));
			}
			else
			{
				return;
			}	
	}

	public List getRelatedObjects(DAO dao, Class sourceClass, String[] whereColumnName, String[] whereColumnValue, String[] whereColumnCondition)
			throws DAOException
	{
		String sourceObjectName = sourceClass.getName();
		String joinCondition = Constants.AND_JOIN_CONDITION;
		String selectColumnName[] = {Constants.SYSTEM_IDENTIFIER};
		List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);

		list = Utility.removeNull(list);
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
	
	public List getSpecimenDataForBulkOperations(List specimenIds, SessionDataBean sessionDataBean, String queryString) throws ClassNotFoundException, DAOException
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
		
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(edu.wustl.common.util.global.Constants.JDBC_DAO);
		dao.openSession(null);
		specimenDataList = dao.executeQuery(sql,sessionDataBean, false, new HashMap());
		dao.closeSession();
		return specimenDataList;
	}
	
	public List getSpecimenDataForBulkOperations(String operation, List specimenIds, SessionDataBean sessionDataBean) throws ClassNotFoundException, DAOException
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
	
	public List getSpecimenDataForBulkTransfers(List specimenIds, SessionDataBean sessionDataBean) throws ClassNotFoundException, DAOException
	{
		
		String sql ="Select specimen.IDENTIFIER, specimen.LABEL, abstractSpecimen.SPECIMEN_CLASS, Container.NAME , abstractposition.POSITION_DIMENSION_ONE, abstractposition.POSITION_DIMENSION_TWO, Container.IDENTIFIER " 
			+" from catissue_specimen specimen left join catissue_abstract_specimen abstractSpecimen on (abstractSpecimen.identifier=specimen.identifier )  "
			+ "left join catissue_specimen_position specimenPosition on (specimen.identifier = specimenPosition.specimen_id) "
			+" left join catissue_container Container on (specimenPosition.Container_id=Container.IDENTIFIER) "
			+" left join catissue_abstract_position  abstractposition  on (abstractposition.Identifier=specimenPosition.IDENTIFIER )"
			+" where specimen.IDENTIFIER in ";
		return getSpecimenDataForBulkOperations(specimenIds, sessionDataBean, sql);
	}
	
	public List getSpecimenDataForBulkDisposals(List specimenIds, SessionDataBean sessionDataBean) throws ClassNotFoundException, DAOException
	{
		String sql = "Select specimen.IDENTIFIER, specimen.LABEL "
			+"from catissue_specimen specimen "
			+"where specimen.IDENTIFIER in ";
		return getSpecimenDataForBulkOperations(specimenIds, sessionDataBean, sql);
	}
	
	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.AbstractDAO, java.lang.Object)
	 */
	public String getObjectId(AbstractDAO dao, Object domainObject) 
	{
		String objectId = "";
		//TODO Optimize This code with HQL
		if(domainObject instanceof SpecimenEventParameters)
		{
			SpecimenCollectionGroup scg = null;
			SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) domainObject;
			Specimen specimen = (Specimen) specimenEventParameters.getSpecimen();
			
			if(specimen.getSpecimenCollectionGroup() == null)
			{
				try 
				{
					specimen = (Specimen) dao.retrieve(Specimen.class.getName(), specimen.getId());
					// specimen = Utility.getSpecimen(specimen.getId().toString());
				} 
				catch (DAOException e) 
				{
					Logger.out.error(e.getMessage(), e);
				}
				scg = specimen.getSpecimenCollectionGroup();
			}
			else
			{
				scg = specimen.getSpecimenCollectionGroup();
			}
			
			CollectionProtocolRegistration cpr = scg.getCollectionProtocolRegistration();
			CollectionProtocol cp = cpr.getCollectionProtocol();
			objectId = Constants.COLLECTION_PROTOCOL_CLASS_NAME+"_"+cp.getId();
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
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.common.dao.AbstractDAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 * 
	 */
	public boolean isAuthorized(AbstractDAO dao, Object domainObject, SessionDataBean sessionDataBean)  
	{
		boolean isAuthorized = false;
		String protectionElementName = null;
		
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
				protectionElementName = getObjectId(dao, domainObject2);
			}
		}
		else	
		{
			protectionElementName = getObjectId(dao, domainObject);
		}

		if(protectionElementName.equals(Constants.allowOperation))
		{
			return true;
		}
		//Get the required privilege name which we would like to check for the logged in user.
		String privilegeName = getPrivilegeName(domainObject);
		PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(sessionDataBean.getUserName());
		//Checking whether the logged in user has the required privilege on the given protection element
		isAuthorized = privilegeCache.hasPrivilege(protectionElementName,privilegeName);
		
		if(isAuthorized)
		{
			return isAuthorized;
		}
		else
		// Check for ALL CURRENT & FUTURE CASE
		{
			isAuthorized = edu.wustl.catissuecore.util.global.Utility.checkForAllCurrentAndFutureCPs(dao,privilegeName, sessionDataBean);
		}
		return isAuthorized;			
	}
} 