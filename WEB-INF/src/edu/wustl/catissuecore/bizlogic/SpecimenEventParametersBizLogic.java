/*
 * Created on Jul 29, 2005
 *<p>SpecimenEventParametersBizLogic Class</p>
 * This class contains the Biz Logic for all EventParameters Classes.
 * This will be the class which will be used for datatransactions of the EventParameters. 
 */

package edu.wustl.catissuecore.bizlogic;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.sf.ehcache.CacheException;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
import edu.wustl.catissuecore.domain.FixedEventParameters;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
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
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;

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
		try
		{
			SpecimenEventParameters specimenEventParametersObject = (SpecimenEventParameters) obj;

			List list = dao.retrieve(User.class.getName(), Constants.SYSTEM_IDENTIFIER, specimenEventParametersObject.getUser().getId());
			if (!list.isEmpty())
			{
				User user = (User) list.get(0);

				// check for closed User
				checkStatus(dao, user, "User");

				specimenEventParametersObject.setUser(user);
			}
			Specimen specimen = (Specimen) dao.retrieve(Specimen.class.getName(), specimenEventParametersObject.getSpecimen().getId());

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
					storageContainerObj.setId(transferEventParameters.getToStorageContainer().getId());
					String sourceObjectName = StorageContainer.class.getName();
					String[] selectColumnName = {"name"};
					String[] whereColumnName = {"id"}; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
					String[] whereColumnCondition = {"="};
					Object[] whereColumnValue = {transferEventParameters.getToStorageContainer().getId()};
					String joinCondition = null;

					List stNamelist = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue,
							joinCondition);

					if (!stNamelist.isEmpty())
					{
						storageContainerObj.setName((String) stNamelist.get(0));
					}
					// check for closed StorageContainer
					checkStatus(dao, storageContainerObj, "Storage Container");

					StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(
							Constants.STORAGE_CONTAINER_FORM_ID);

					// --- check for all validations on the storage container.
					storageContainerBizLogic.checkContainer(dao, storageContainerObj.getId().toString(), transferEventParameters
							.getToPositionDimensionOne().toString(), transferEventParameters.getToPositionDimensionTwo().toString(), sessionDataBean,false);
					
					//				if (storageContainer != null)
					//				{
					//					NewSpecimenBizLogic newSpecimenBizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(
					//							Constants.NEW_SPECIMEN_FORM_ID);
					//newSpecimenBizLogic.chkContainerValidForSpecimen(storageContainer, specimen);
					specimen.setStorageContainer(storageContainerObj);
					specimen.setPositionDimensionOne(transferEventParameters.getToPositionDimensionOne());
					specimen.setPositionDimensionTwo(transferEventParameters.getToPositionDimensionTwo());
					//				}
					dao.update(specimen, sessionDataBean, true, true, false);
				}
				if (specimenEventParametersObject instanceof DisposalEventParameters)
				{
					DisposalEventParameters disposalEventParameters = (DisposalEventParameters) specimenEventParametersObject;
					if (disposalEventParameters.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
					{
						disableSubSpecimens(dao, specimen.getId().toString());

					}
					Map disabledCont = new TreeMap();
					if (specimen.getStorageContainer() != null)
					{
						addEntriesInDisabledMap(specimen, specimen.getStorageContainer(), disabledCont);
					}
					specimen.setPositionDimensionOne(null);
					specimen.setPositionDimensionTwo(null);
					specimen.setStorageContainer(null);

					specimen.setAvailable(new Boolean(false));
					specimen.setActivityStatus(disposalEventParameters.getActivityStatus());
					dao.update(specimen, sessionDataBean, true, true, false);

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
		containerDetails.put(pos1Key, specimen.getPositionDimensionOne());
		containerDetails.put(pos2Key, specimen.getPositionDimensionTwo());

		disabledConts.put(container.getId().toString(), containerDetails);

	}

	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
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
						throw new DAOException(ApplicationProperties.getValue("errors.invalid", message));
					}
					
					
						//Long storageContainerId = specimen.getStorageContainer().getId();
						Integer xPos = parameter.getToPositionDimensionOne();
						Integer yPos = parameter.getToPositionDimensionTwo();
						boolean isContainerFull = false;
						/**
						 *  Following code is added to set the x and y dimension in case only storage container is given 
						 *  and x and y positions are not given 
						 */
						
						if (xPos == null || yPos == null)
						{
						isContainerFull = true;
						Map containerMapFromCache = null;
						try
						{
							containerMapFromCache = (TreeMap) StorageContainerUtil.getContainerMapFromCache();
						}
						catch (CacheException e)
						{
							e.printStackTrace();
						}
						
						if (containerMapFromCache != null)
						{
							Iterator itr = containerMapFromCache.keySet().iterator();
							while (itr.hasNext())
							{
								NameValueBean nvb = (NameValueBean) itr.next();
								if(nvb.getValue().toString().equals(storageContainerObj.getId().toString()))
								{
								
									Map tempMap = (Map) containerMapFromCache.get(nvb);
									Iterator tempIterator = tempMap.keySet().iterator();;
									NameValueBean nvb1 = (NameValueBean) tempIterator.next();
									
									list = (List) tempMap.get(nvb1);
									NameValueBean nvb2 = (NameValueBean) list.get(0);
													
									parameter.setToPositionDimensionOne(new Integer(nvb1.getValue()));
									parameter.setToPositionDimensionTwo(new Integer(nvb2.getValue()));
								    isContainerFull = false;
								    break;
								}
								
							}
						}
						
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

	private void validateTransferEventParameters(SpecimenEventParameters eventParameter) throws DAOException
	{
		TransferEventParameters parameter = (TransferEventParameters) eventParameter;
		List list = (List) retrieve(TransferEventParameters.class.getName(), Constants.SYSTEM_IDENTIFIER, parameter.getId());
		if (list.size() != 0)
		{
			TransferEventParameters parameterCopy = (TransferEventParameters) list.get(0);
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
			Iterator iterator = specimen.getChildrenSpecimen().iterator();
			while (iterator.hasNext())
			{
				Specimen childSpecimen = (Specimen) iterator.next();
				childSpecimen.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED);
				setDisableToSubSpecimen(childSpecimen);
			}
		}
	}

	private void disableSubSpecimens(DAO dao, String speID) throws DAOException
	{
		String sourceObjectName = Specimen.class.getName();
		String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnName = {"parentSpecimen", Constants.ACTIVITY_STATUS};
		String[] whereColumnCondition = {"=", "!="};
		String[] whereColumnValue = {speID, Constants.ACTIVITY_STATUS_DISABLED};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		List listOfSpecimenIDs = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue,
				joinCondition);
		listOfSpecimenIDs = Utility.removeNull(listOfSpecimenIDs);
		//getRelatedObjects(dao, Specimen.class, "parentSpecimen", speIDArr);

		if (!listOfSpecimenIDs.isEmpty())
		{
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
		

}
