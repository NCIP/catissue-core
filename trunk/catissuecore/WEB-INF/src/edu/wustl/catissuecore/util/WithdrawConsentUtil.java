/**
 * This class is designed to contain common methods for the Consent Withdraw process.
 * This class will be used in CollectionProtocolRegistration, SpecimenCollectionGroup and Specimen Bizlogic classes.
 * 
 * @author mandar_deshmukh
 *  
 */
package edu.wustl.catissuecore.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.ReturnEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAO;

/**
 * This class is designed to contain common methods for the Consent Withdraw process.
 * This class will be used in CollectionProtocolRegistration, SpecimenCollectionGroup and Specimen Bizlogic classes.
 * 
 * @author mandar_deshmukh
 *  
 */

public class WithdrawConsentUtil
{
	/**
	 * This method updates the SpecimenCollectionGroup instance by setting all the consent tierstatus to with 
	 * @param scg Instance of SpecimenCollectionGroup to be updated.
	 * @param consentTierID Identifier of ConsentTier to be withdrawn.
	 * @param withdrawOption Action to be performed on the withdrawn collectiongroup.
	 * @param dao DAO instance. Used for inserting disposal event. 
	 * @param sessionDataBean SessionDataBean instance. Used for inserting disposal event.
	 */
	public static void updateSCG(SpecimenCollectionGroup scg, long consentTierID, String withdrawOption,  DAO dao, SessionDataBean sessionDataBean)
	{
		Collection newScgStatusCollection = new HashSet();
		Iterator itr = scg.getConsentTierStatusCollection().iterator() ;
		while(itr.hasNext() )
		{
			ConsentTierStatus consentTierstatus = (ConsentTierStatus)itr.next();
			//compare consent tier id of scg with cpr consent tier of response
			if(consentTierstatus.getConsentTier().getId().longValue() == consentTierID)
			{
				consentTierstatus.setStatus( Constants.WITHDRAWN);
				updateSpecimensInSCG(scg,consentTierID,withdrawOption , dao, sessionDataBean );
			}
			newScgStatusCollection.add(consentTierstatus );	// set updated consenttierstatus in scg
		}
		scg.setConsentTierStatusCollection( newScgStatusCollection);
	}
	
	/*
	 * This method updates the specimens for the given SCG and sets the consent status to withdraw.
	 */
	private static void updateSpecimensInSCG(SpecimenCollectionGroup scg,long consentTierID, String consentWithdrawalOption,  DAO dao, SessionDataBean sessionDataBean)
	{
		Collection specimenCollection = scg.getSpecimenCollection();
		Collection updatedSpecimenCollection = new HashSet();
		Iterator specimenItr = specimenCollection.iterator() ;
		while(specimenItr.hasNext() )
		{
			Specimen specimen = (Specimen)specimenItr.next();
			updateSpecimenStatus(specimen, consentWithdrawalOption, consentTierID, dao, sessionDataBean);
			updatedSpecimenCollection.add(specimen );
		}
		scg.setSpecimenCollection(updatedSpecimenCollection );
	}
	
	/**
	 * This method updates the Specimen instance by setting all the consenttierstatus to withdraw. 
	 * @param specimen  Instance of Specimen to be updated. 
	 * @param consentWithdrawalOption Action to be performed on the withdrawn specimen.
	 * @param consentTierID Identifier of ConsentTier to be withdrawn.
	 * @param dao DAO instance. Used for inserting disposal event. 
	 * @param sessionDataBean SessionDataBean instance. Used for inserting disposal event.
	 */
	public static void updateSpecimenStatus(Specimen specimen, String consentWithdrawalOption, long consentTierID,  DAO dao, SessionDataBean sessionDataBean)
	{
		Collection consentTierStatusCollection = specimen.getConsentTierStatusCollection();
		Collection updatedSpecimenStatusCollection = new HashSet();
		Iterator specimenStatusItr = consentTierStatusCollection.iterator() ;
		while(specimenStatusItr.hasNext() )
		{
			ConsentTierStatus consentTierstatus = (ConsentTierStatus)specimenStatusItr.next() ;
			if(consentTierstatus.getConsentTier().getId().longValue() == consentTierID)
			{
				if(consentWithdrawalOption != null)
				{					
					consentTierstatus.setStatus(Constants.WITHDRAWN );
					withdrawResponse(specimen, consentWithdrawalOption,   dao,  sessionDataBean);
				}
			}
			updatedSpecimenStatusCollection.add(consentTierstatus );
		}
		specimen.setConsentTierStatusCollection( updatedSpecimenStatusCollection);
		updateChildSpecimens(specimen, consentWithdrawalOption, consentTierID, dao, sessionDataBean);
	}

	/*
	 * This method performs an action on specimen based on user response.
	 */
	private static void withdrawResponse(Specimen specimen, String consentWithdrawalOption,  DAO dao, SessionDataBean sessionDataBean)
	{
		if(consentWithdrawalOption.equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_DISCARD))
		{
			addDisposalEvent(specimen, dao, sessionDataBean);
		}
		else if(consentWithdrawalOption.equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_RETURN))
		{
			addReturnEvent(specimen, dao, sessionDataBean);
		}
		//only if consentWithdrawalOption is not reset or noaction.
		if(!consentWithdrawalOption.equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_RESET) && !consentWithdrawalOption.equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_NOACTION) )
		{
			specimen.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED);
			specimen.setAvailable(new Boolean(false) );

			if(specimen.getStorageContainer() !=null)		// locations cleared
			{
				Map containerMap = null;
				try
				{
					containerMap = StorageContainerUtil.getContainerMapFromCache();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				StorageContainerUtil.insertSinglePositionInContainerMap(specimen.getStorageContainer(),containerMap,specimen.getPositionDimensionOne().intValue(), specimen.getPositionDimensionTwo().intValue()    );
			}
			specimen.setPositionDimensionOne(null );
			specimen.setPositionDimensionTwo(null );
			specimen.setStorageContainer(null );
			specimen.setAvailableQuantity(null );
			specimen.setInitialquantity(null );
		}
	}
	
	private static void addReturnEvent(Specimen specimen, DAO dao, SessionDataBean sessionDataBean)
	{
		try
		{
			Collection eventCollection = specimen.getSpecimenEventCollection();
			if(!isEventAdded(eventCollection, "ReturnEventParameters"))
			{
				ReturnEventParameters returnEvent = new ReturnEventParameters();
				returnEvent.setSpecimen(specimen );
				dao.insert(returnEvent,sessionDataBean,true,true) ;
				
				eventCollection.add(returnEvent);
				specimen.setSpecimenEventCollection(eventCollection);
			}
		}
		catch(Exception excp)
		{
			excp.printStackTrace(); 
		}
	}

	/*
	 * This method adds a disposal event to the specimen.
	 */
	private static void addDisposalEvent(Specimen specimen, DAO dao, SessionDataBean sessionDataBean)
	{
		try
		{
			Collection eventCollection = specimen.getSpecimenEventCollection();
			if(!isEventAdded(eventCollection, "DisposalEventParameters"))
			{
				DisposalEventParameters disposalEvent = new DisposalEventParameters();
				disposalEvent.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED );
				disposalEvent.setReason(Constants.WITHDRAW_RESPONSE_REASON);
				disposalEvent.setSpecimen(specimen );
				dao.insert(disposalEvent,sessionDataBean,true,true) ;
				
				eventCollection.add(disposalEvent);
				specimen.setSpecimenEventCollection(eventCollection);
			}
		}
		catch(Exception excp)
		{
			excp.printStackTrace(); 
		}
	}
	
	private static void updateChildSpecimens(Specimen specimen, String consentWithdrawalOption, long consentTierID, DAO dao, SessionDataBean sessionDataBean)
	{
		Collection childSpecimens = specimen.getChildrenSpecimen();
		Iterator childItr = childSpecimens.iterator();  
		while(childItr.hasNext() )
		{
			Specimen childSpecimen = (Specimen)childItr.next();
			consentWithdrawForchildSpecimens(childSpecimen , dao,  sessionDataBean, consentWithdrawalOption, consentTierID);
		}
	}
	
	private static void consentWithdrawForchildSpecimens(Specimen specimen, DAO dao, SessionDataBean sessionDataBean, String consentWithdrawalOption, long consentTierID)
	{
		if(specimen!=null)
		{
			updateSpecimenStatus(specimen,  consentWithdrawalOption, consentTierID, dao, sessionDataBean);
			Collection childSpecimens = specimen.getChildrenSpecimen();
			Iterator itr = childSpecimens.iterator();  
			while(itr.hasNext() )
			{
				Specimen childSpecimen = (Specimen)itr.next();
				consentWithdrawForchildSpecimens(childSpecimen, dao, sessionDataBean, consentWithdrawalOption, consentTierID);
			}
		}
	}

	/**
	 * This method is used to copy the consents from parent specimen to the child specimen.
	 * 
	 * @param specimen Instance of specimen. It is the child specimen to which the consents will be set.
	 * @param parentSpecimen Instance of specimen. It is the parent specimen from which the consents will be copied.
	 */
	public static void setConsentsFromParent(Specimen specimen, Specimen parentSpecimen)
	{
		Collection consentTierStatusCollection = new HashSet();
		Collection parentStatusCollection = parentSpecimen.getConsentTierStatusCollection();
		Iterator parentStatusCollectionIterator = parentStatusCollection.iterator();
		while(parentStatusCollectionIterator.hasNext() )
		{
			ConsentTierStatus cts = (ConsentTierStatus)parentStatusCollectionIterator.next();
			ConsentTierStatus newCts = new ConsentTierStatus();
			newCts.setStatus(cts.getStatus());
			newCts.setConsentTier(cts.getConsentTier());
			consentTierStatusCollection.add(newCts);
		}
		specimen.setConsentTierStatusCollection( consentTierStatusCollection);
	}
	
	/*
	 * This method checks if the given event is already added to the specimen.
	 */
	private static boolean isEventAdded(Collection eventCollection, String eventType)
	{
		boolean result = false;
		Iterator eventCollectionIterator = eventCollection.iterator();
		while(eventCollectionIterator.hasNext() )
		{
			Object event = eventCollectionIterator.next();
			if(event.getClass().getSimpleName().equals(eventType)  )
			{
				result= true;
				break;
			}
		}
		return result;
	}
	// ----------------WITHDRAW functionality end
	//--------Mandar : - 24-Jan-07 ------------------ApplyChanges Functionality start
	 
	/**
	 * This method updates the specimens status for the given SCG.
	 * @param specimenCollectionGroup
	 * @param oldSpecimenCollectionGroup
	 * @param dao
	 * @param sessionDataBean
	 */
	public static void updateSpecimenStatusInSCG(SpecimenCollectionGroup specimenCollectionGroup,SpecimenCollectionGroup oldSpecimenCollectionGroup)
	{
		Collection newConsentTierStatusCollection = specimenCollectionGroup.getConsentTierStatusCollection();
		Collection oldConsentTierStatusCollection = oldSpecimenCollectionGroup.getConsentTierStatusCollection();
		
		String applyChangesTo =  specimenCollectionGroup.getApplyChangesTo();
		
		Iterator itr = newConsentTierStatusCollection.iterator() ;
		while(itr.hasNext() )
		{
			ConsentTierStatus consentTierStatus = (ConsentTierStatus)itr.next();
			String statusValue = consentTierStatus.getStatus();
			long consentTierID = consentTierStatus.getConsentTier().getId().longValue();
			updateSCGSpecimenCollection(specimenCollectionGroup, consentTierID, statusValue, newConsentTierStatusCollection, oldConsentTierStatusCollection);	
		}
	}

	/*
	 * This method updates the specimen consent status. 
	 */
	private static void updateSCGSpecimenCollection(SpecimenCollectionGroup specimenCollectionGroup, long consentTierID, String  statusValue, Collection newSCGConsentCollection, Collection oldSCGConsentCollection)
	{
		Collection specimenCollection = specimenCollectionGroup.getSpecimenCollection();
		Collection updatedSpecimenCollection = new HashSet();
		String applyChangesTo =  specimenCollectionGroup.getApplyChangesTo(); 
		Iterator specimenItr = specimenCollection.iterator() ;
		while(specimenItr.hasNext() )
		{
			Specimen specimen = (Specimen)specimenItr.next();
			updateSpecimenConsentStatus(specimen, applyChangesTo, consentTierID, statusValue, newSCGConsentCollection, oldSCGConsentCollection );
			updatedSpecimenCollection.add(specimen );
		}
		specimenCollectionGroup.setSpecimenCollection(updatedSpecimenCollection );
	}
	
	public static void updateSpecimenConsentStatus(Specimen specimen, String applyChangesTo, long consentTierID, String  statusValue, Collection newConsentCollection, Collection oldConsentCollection )
	{
		if(applyChangesTo.equalsIgnoreCase(Constants.APPLY_ALL))
			updateSpecimenConsentStatus(specimen, consentTierID, statusValue, applyChangesTo );
		else if(applyChangesTo.equalsIgnoreCase(Constants.APPLY))
		{
			//To pass both collections
			checkConflictingConsents(newConsentCollection, oldConsentCollection, specimen );
		}
	}
	
	/**
	 * This method updates the Specimen instance by setting all the consenttierstatus to withdraw. 
	 * @param specimen  Instance of Specimen to be updated. 
	 * @param consentWithdrawalOption Action to be performed on the withdrawn specimen.
	 * @param consentTierID Identifier of ConsentTier to be withdrawn.
	 */
	private static void updateSpecimenConsentStatus(Specimen specimen, long consentTierID, String statusValue, String applyChangesTo )
	{
		Collection consentTierStatusCollection = specimen.getConsentTierStatusCollection();
		Collection updatedSpecimenStatusCollection = new HashSet();
		Iterator specimenStatusItr = consentTierStatusCollection.iterator() ;
		while(specimenStatusItr.hasNext() )
		{
			ConsentTierStatus consentTierstatus = (ConsentTierStatus)specimenStatusItr.next() ;
			if(consentTierstatus.getConsentTier().getId().longValue() == consentTierID)
			{
				consentTierstatus.setStatus(statusValue);
			}
			updatedSpecimenStatusCollection.add(consentTierstatus );
		}
		specimen.setConsentTierStatusCollection( updatedSpecimenStatusCollection);
		
		//to update child specimens
		Collection childSpecimens = specimen.getChildrenSpecimen();
		Iterator childItr = childSpecimens.iterator();  
		while(childItr.hasNext() )
		{
			Specimen childSpecimen = (Specimen)childItr.next();
			consentStatusUpdateForchildSpecimens(childSpecimen , consentTierID, statusValue ,applyChangesTo);
		}
	}

	private static void consentStatusUpdateForchildSpecimens(Specimen specimen, long consentTierID, String statusValue, String applyChangesTo)
	{
		if(specimen!=null)
		{
			updateSpecimenConsentStatus(specimen, consentTierID, statusValue, applyChangesTo );
			Collection childSpecimens = specimen.getChildrenSpecimen();
			Iterator itr = childSpecimens.iterator();  
			while(itr.hasNext() )
			{
				Specimen childSpecimen = (Specimen)itr.next();
				consentStatusUpdateForchildSpecimens(childSpecimen, consentTierID, statusValue, applyChangesTo );
			}
		}
	}

	/*
	 * This method verifies the consents of SCG and specimen for any conflicts.
	 */
	private static void checkConflictingConsents(Collection newConsentCollection, Collection oldConsentCollection, Specimen specimen )
	{
/*		 if oldSCG.c1 == S.c1 then update specimen with new SCG.c1
 * 			OR
 *		 if oldS.c1 == cS.c1 then update child specimen with new S.c1
 */		
		Iterator oldConsentItr = oldConsentCollection.iterator();
		while(oldConsentItr.hasNext() )
		{
			ConsentTierStatus oldConsentStatus = (ConsentTierStatus)oldConsentItr.next() ;
			Collection specimenConsentStatusCollection =  specimen.getConsentTierStatusCollection();
			Iterator specimenConsentStatusItr = specimenConsentStatusCollection.iterator() ;
			Collection updatedSpecimenConsentStatusCollection = new HashSet();
			while(specimenConsentStatusItr.hasNext() )
			{
				ConsentTierStatus specimenConsentStatus = (ConsentTierStatus)specimenConsentStatusItr.next() ;
				if(oldConsentStatus.getConsentTier().getId().longValue() == specimenConsentStatus.getConsentTier().getId().longValue() )
				{
					if(oldConsentStatus.getStatus().equals(specimenConsentStatus.getStatus()))
					{
						Iterator newConsentItr = newConsentCollection.iterator();
						while(newConsentItr.hasNext() )
						{
							ConsentTierStatus newConsentStatus = (ConsentTierStatus)newConsentItr.next() ;
							if(newConsentStatus.getConsentTier().getId().longValue() == specimenConsentStatus.getConsentTier().getId().longValue() )
							{
								specimenConsentStatus.setStatus(newConsentStatus.getStatus()); 
							}
						}
					}
				}
				updatedSpecimenConsentStatusCollection.add(specimenConsentStatus);
			}
			specimen.setConsentTierStatusCollection(updatedSpecimenConsentStatusCollection);
		}

		//to update child specimens
		Collection childSpecimens = specimen.getChildrenSpecimen();
		Iterator childItr = childSpecimens.iterator();  
		while(childItr.hasNext() )
		{
			Specimen childSpecimen = (Specimen)childItr.next();
			consentStatusUpdateForchildSpecimens(childSpecimen , newConsentCollection, oldConsentCollection);
		}
	}
	
	private static void consentStatusUpdateForchildSpecimens(Specimen specimen, Collection newConsentCollection, Collection oldConsentCollection)
	{
		if(specimen!=null)
		{
			checkConflictingConsents(newConsentCollection, oldConsentCollection, specimen );
			Collection childSpecimens = specimen.getChildrenSpecimen();
			Iterator itr = childSpecimens.iterator();  
			while(itr.hasNext() )
			{
				Specimen childSpecimen = (Specimen)itr.next();
				consentStatusUpdateForchildSpecimens(childSpecimen, newConsentCollection, oldConsentCollection);
			}
		}
	}
	// ------------------------ Mandar : 24-Jan-07 Apply changes --------- end
}
