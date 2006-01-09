/*
 * Created on Jul 29, 2005
 *<p>SpecimenEventParametersBizLogic Class</p>
 * This class contains the Biz Logic for all EventParameters Classes.
 * This will be the class which will be used for datatransactions of the EventParameters. 
 */
package edu.wustl.catissuecore.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
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
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
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
		SpecimenEventParameters specimenEventParametersObject = (SpecimenEventParameters)obj;

		List list = dao.retrieve(User.class.getName(), Constants.SYSTEM_IDENTIFIER, specimenEventParametersObject.getUser().getSystemIdentifier());
		if (!list.isEmpty())
		{
		    User user = (User) list.get(0);

		    // check for closed User
			checkStatus(dao, user, "User" );

		    specimenEventParametersObject.setUser(user);
		}
		Specimen specimen = (Specimen) dao.retrieve(Specimen.class.getName(), specimenEventParametersObject.getSpecimen().getSystemIdentifier());
		
		// check for closed Specimen
		checkStatus(dao, specimen, "Specimen" );

		if (specimen != null)
		{
		    specimenEventParametersObject.setSpecimen(specimen);
		    if (specimenEventParametersObject instanceof TransferEventParameters)
		    {
		        TransferEventParameters transferEventParameters = (TransferEventParameters)specimenEventParametersObject;
		        
			    specimen.setPositionDimensionOne(transferEventParameters.getToPositionDimensionOne());
			    specimen.setPositionDimensionTwo(transferEventParameters.getToPositionDimensionTwo());
			    
			    StorageContainer storageContainer = (StorageContainer)dao.retrieve(StorageContainer.class.getName(), transferEventParameters.getToStorageContainer().getSystemIdentifier());
			    
				// check for closed StorageContainer
				checkStatus(dao, storageContainer, "Storage Container" );

			    if (storageContainer != null)
			    {
			        specimen.setStorageContainer(storageContainer);
			    }
			    dao.update(specimen, sessionDataBean, true, true, false);
		    }
		}
		
		dao.insert(specimenEventParametersObject,sessionDataBean, true, true);
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
		if(!specimenEventParameters.getUser().getSystemIdentifier().equals( oldSpecimenEventParameters.getUser().getSystemIdentifier()))
		{
			checkStatus(dao,specimenEventParameters.getUser(), "User" );
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
//				    if(!storageContainer.getSystemIdentifier().equals(oldstorageContainer.getSystemIdentifier()) )
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
		SpecimenEventParameters eventParameter = (SpecimenEventParameters)obj;
		
		switch(Utility.getEventParametersFormId(eventParameter))
		{
			case Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID:
				String storageStatus = ((CheckInCheckOutEventParameter)eventParameter).getStorageStatus();
				if(!Validator.isEnumeratedValue(Constants.STORAGE_STATUS_ARRAY,storageStatus))
				{
					throw new DAOException(ApplicationProperties.getValue("events.storageStatus.errMsg"));
				}
				break;
				
			case Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID:
				String procedure = ((CollectionEventParameters)eventParameter).getCollectionProcedure();
				List procedureList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_COLLECTION_PROCEDURE,null);
				if(!Validator.isEnumeratedValue(procedureList,procedure))
				{
					throw new DAOException(ApplicationProperties.getValue("events.collectionProcedure.errMsg"));
				}
				
				String container = ((CollectionEventParameters)eventParameter).getContainer();
				List containerList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_CONTAINER,null);
				if(!Validator.isEnumeratedOrNullValue(containerList,container))
				{
					throw new DAOException(ApplicationProperties.getValue("events.container.errMsg"));
				}
				break;
				
			case Constants.EMBEDDED_EVENT_PARAMETERS_FORM_ID:
				String embeddingMedium = ((EmbeddedEventParameters)eventParameter).getEmbeddingMedium();
				List embeddingMediumList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_EMBEDDING_MEDIUM,null);
				if(!Validator.isEnumeratedValue(embeddingMediumList,embeddingMedium))
				{
					throw new DAOException(ApplicationProperties.getValue("events.embeddingMedium.errMsg"));
				}
				break;
				
			case Constants.FIXED_EVENT_PARAMETERS_FORM_ID:
				String fixationType = ((FixedEventParameters)eventParameter).getFixationType();
				List fixationTypeList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_FIXATION_TYPE,null);
				if(!Validator.isEnumeratedValue(fixationTypeList,fixationType))
				{
					throw new DAOException(ApplicationProperties.getValue("events.fixationType.errMsg"));
				}
				break;
			
			case Constants.FROZEN_EVENT_PARAMETERS_FORM_ID:
				String method = ((FrozenEventParameters)eventParameter).getMethod();
				List methodList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_METHOD,null);
				if(!Validator.isEnumeratedValue(methodList,method))
				{
					throw new DAOException(ApplicationProperties.getValue("events.method.errMsg"));
				}
				break;
			
			case Constants.RECEIVED_EVENT_PARAMETERS_FORM_ID:
				String quality = ((ReceivedEventParameters)eventParameter).getReceivedQuality();
				List qualityList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_RECEIVED_QUALITY,null);
				if(!Validator.isEnumeratedValue(qualityList,quality))
				{
					throw new DAOException(ApplicationProperties.getValue("events.receivedQuality.errMsg"));
				}
				break;
			
			case Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID:
				String histQuality = ((TissueSpecimenReviewEventParameters)eventParameter).getHistologicalQuality();
				if(!Validator.isEnumeratedOrNullValue(Constants.HISTOLOGICAL_QUALITY_ARRAY,histQuality))
				{
					throw new DAOException(ApplicationProperties.getValue("events.histologicalQuality.errMsg"));
				}
				break;
		}
		return true;
    }
}
