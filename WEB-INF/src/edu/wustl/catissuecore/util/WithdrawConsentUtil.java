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
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.util.logger.Logger;

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
		Logger.out.debug(">>>>>>>>. Update Specimen >>>>>>>>" );
		Collection specimenCollection = scg.getSpecimenCollection();
		Collection updatedSpecimenCollection = new HashSet();
		Iterator specimenItr = specimenCollection.iterator() ;
		while(specimenItr.hasNext() )
		{
			Specimen specimen = (Specimen)specimenItr.next();
			updateSpecimenStatus(specimen, consentWithdrawalOption, consentTierID, dao, sessionDataBean);
		//	updateChildSpecimens(specimen, consentWithdrawalOption, consentTierID, dao, sessionDataBean);
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
				consentTierstatus.setStatus(Constants.WITHDRAWN );
				withdrawResponse(specimen, consentWithdrawalOption,   dao,  sessionDataBean);
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
			specimen.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED);
			specimen.setAvailable(new Boolean(false) );
			addDisposalEvent(specimen, dao, sessionDataBean);
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
			specimen.setQuantity(null );
		}
		else if(consentWithdrawalOption.equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_RETURN))
		{
			addReturnEvent(specimen);
		}
	}
	
	//TODO
	//method for return event.
	private static void addReturnEvent(Specimen specimen)
	{
	}

	/*
	 * This method adds a disposal event to the specimen.
	 */
	private static void addDisposalEvent(Specimen specimen, DAO dao, SessionDataBean sessionDataBean)
	{
		try
		{
			Collection eventCollection = specimen.getSpecimenEventCollection();
			DisposalEventParameters disposalEvent = new DisposalEventParameters();
			disposalEvent.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED );
			disposalEvent.setReason(Constants.WITHDRAW_RESPONSE_REASON);
			disposalEvent.setSpecimen(specimen );
			dao.insert(disposalEvent,sessionDataBean,true,true) ;
			
			eventCollection.add(disposalEvent);
			specimen.setSpecimenEventCollection(eventCollection);
		}
		catch(Exception excp)
		{
			excp.printStackTrace(); 
		}
	}
	
	//TODO
	//Check and integrate the two methods if possible.  
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

}
