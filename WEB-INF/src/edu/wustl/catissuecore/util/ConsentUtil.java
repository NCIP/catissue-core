/**
 * This class is designed to contain common methods for the Consent Withdraw process.
 * This class will be used in CollectionProtocolRegistration, SpecimenCollectionGroup and Specimen Bizlogic classes.
 * 
 * @author mandar_deshmukh
 *  
 */
package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.bean.ConsentBean;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.ReturnEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

/**
 * This class is designed to contain common methods for the Consent Withdraw process.
 * This class will be used in CollectionProtocolRegistration, SpecimenCollectionGroup and Specimen Bizlogic classes.
 * 
 * @author mandar_deshmukh
 *  
 */

public final class ConsentUtil
{
	private static Logger logger = Logger.getCommonLogger(ConsentUtil.class);
	/*
	 * creates a singleton object
	 * 
	 */
	private static ConsentUtil consentUtil= new ConsentUtil();
	/*
	 * Private constructor.
	 */
	private ConsentUtil()
	{
		
	}
	/*
	 *returns the single object 
	 */
	public static ConsentUtil getInstance()
	{
		return consentUtil;
	}
	
	/**
	 * This method updates the SpecimenCollectionGroup instance by setting all the consent tierstatus to with 
	 * @param scg Instance of SpecimenCollectionGroup to be updated.
	 * @param oldScg Instance of OldSpecimenCollectionGroup to be updated.
	 * @param consentTierID Identifier of ConsentTier to be withdrawn.
	 * @param withdrawOption Action to be performed on the withdrawn collectiongroup.
	 * @param dao DAO instance. Used for inserting disposal event. 
	 * @param sessionDataBean SessionDataBean instance. Used for inserting disposal event.
	 * @throws ApplicationException 
	 * @throws BizLogicException 
	 */
	public static void updateSCG(SpecimenCollectionGroup scg, SpecimenCollectionGroup oldscg, long consentTierID, String withdrawOption,DAO dao, SessionDataBean sessionDataBean) throws ApplicationException 
	{
		Collection newScgStatusCollection = new HashSet();
		Collection consentTierStatusCollection =scg.getConsentTierStatusCollection();
		Iterator itr = consentTierStatusCollection.iterator() ;
		while(itr.hasNext() )
		{
			ConsentTierStatus consentTierstatus = (ConsentTierStatus)itr.next();
			//compare consent tier id of scg with cpr consent tier of response
			if(consentTierstatus.getConsentTier().getId().longValue() == consentTierID)
			{
				consentTierstatus.setStatus(Constants.WITHDRAWN);
				updateSpecimensInSCG(scg,oldscg,consentTierID,withdrawOption , dao, sessionDataBean );
			}
			newScgStatusCollection.add(consentTierstatus );	// set updated consenttierstatus in scg
		}
		scg.setConsentTierStatusCollection( newScgStatusCollection);
		if(!(withdrawOption.equals(Constants.WITHDRAW_RESPONSE_RESET)))
		{	
			scg.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
		}
	}
	
	/**
	 * This method updates the SpecimenCollectionGroup instance by setting all the consent tierstatus to with 
	 * @param scg Instance of SpecimenCollectionGroup to be updated.
	 * @param consentTierID Identifier of ConsentTier to be withdrawn.
	 * @param withdrawOption Action to be performed on the withdrawn collectiongroup.
	 * @param dao DAO instance. Used for inserting disposal event. 
	 * @param sessionDataBean SessionDataBean instance. Used for inserting disposal event.
	 * @throws ApplicationException 
	 *  
	 */
	public static void updateSCG(SpecimenCollectionGroup scg, long consentTierID, String withdrawOption,  DAO dao, SessionDataBean sessionDataBean) throws ApplicationException 
	{
		updateSCG(scg, scg, consentTierID,withdrawOption,dao, sessionDataBean);
	}
	/*
	 * This method updates the specimens for the given SCG and sets the consent status to withdraw.
	 */
	private static void updateSpecimensInSCG(SpecimenCollectionGroup scg, SpecimenCollectionGroup oldscg, long consentTierID, String consentWithdrawalOption,  DAO dao, SessionDataBean sessionDataBean) throws ApplicationException
	{
		try
		{
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		
		Collection specimenCollection =(Collection)bizLogic.retrieveAttribute(SpecimenCollectionGroup.class,scg.getId(),"elements(specimenCollection)"); 
		Collection updatedSpecimenCollection = new HashSet();
		Iterator specimenItr = specimenCollection.iterator() ;
		while(specimenItr.hasNext())
		{
			Specimen specimen = (Specimen)specimenItr.next();
			updateSpecimenStatus(specimen, consentWithdrawalOption, consentTierID, dao, sessionDataBean);
			updatedSpecimenCollection.add(specimen );
		}
		scg.setSpecimenCollection(updatedSpecimenCollection);
		}
		catch(DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw AppUtility.getApplicationException(daoExp, "dao.error", "");
		}
	}
	
	/**
	 * This method updates the Specimen instance by setting all the consenttierstatus to withdraw. 
	 * @param specimen  Instance of Specimen to be updated. 
	 * @param consentWithdrawalOption Action to be performed on the withdrawn specimen.
	 * @param consentTierID Identifier of ConsentTier to be withdrawn.
	 * @param dao DAO instance. Used for inserting disposal event. 
	 * @param sessionDataBean SessionDataBean instance. Used for inserting disposal event.
	 * @throws ApplicationException 
	 * @throws BizLogicException 
	 */
	public static void updateSpecimenStatus(Specimen specimen, String consentWithdrawalOption, long consentTierID,  DAO dao, SessionDataBean sessionDataBean) throws ApplicationException 
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
	/**
	 * @param specimen
	 * @param consentWithdrawalOption
	 * @param dao
	 * @param sessionDataBean
	 */
	private static void withdrawResponse(Specimen specimen, String consentWithdrawalOption,  DAO dao, SessionDataBean sessionDataBean)
	{
		if(Constants.WITHDRAW_RESPONSE_DISCARD.equalsIgnoreCase(consentWithdrawalOption)||Constants.WITHDRAW_RESPONSE_RETURN.equalsIgnoreCase(consentWithdrawalOption))
		{
			addDisposalEvent(specimen, dao, sessionDataBean);
		}
		//only if consentWithdrawalOption is not reset or noaction.
		if(!consentWithdrawalOption.equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_RESET) && !consentWithdrawalOption.equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_NOACTION) )
		{
			updateSpecimen(specimen);

		}
	}
	/**
	 * 
	 * @param specimen
	 */
	private static void updateSpecimen(Specimen specimen) 
	{
		specimen.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
		specimen.setIsAvailable(Boolean.FALSE);

		if(specimen.getSpecimenPosition() != null && specimen.getSpecimenPosition().getStorageContainer() !=null)		// locations cleared
		{
			Map containerMap = null;
			try
			{
				containerMap = StorageContainerUtil.getContainerMapFromCache();
			}
			catch (Exception e)
			{
				logger.error(e);
			}
			StorageContainerUtil.insertSinglePositionInContainerMap(specimen.getSpecimenPosition().getStorageContainer(),containerMap,specimen.getSpecimenPosition().getPositionDimensionOne().intValue(), specimen.getSpecimenPosition().getPositionDimensionTwo().intValue()    );
		}
		specimen.setSpecimenPosition(null);
		//			specimen.setPositionDimensionOne(null);
		//			specimen.setPositionDimensionTwo(null);
		//			specimen.setStorageContainer(null);
		specimen.setAvailableQuantity(null);
		specimen.setInitialQuantity(null);
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
				dao.insert(returnEvent,true) ;
				
				eventCollection.add(returnEvent);
				specimen.setSpecimenEventCollection(eventCollection);
			}
		}
		catch(Exception excp)
		{
			logger.error(excp);
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
				new NewSpecimenBizLogic().disposeSpecimen(sessionDataBean, specimen, dao, Constants.SPECIMEN_DISCARD_DISPOSAL_REASON);
			}
		}
		catch(Exception excp)
		{
			logger.error(excp);
		}
	}
	
	private static void updateChildSpecimens(Specimen specimen, String consentWithdrawalOption, long consentTierID, DAO dao, SessionDataBean sessionDataBean) throws ApplicationException 
	{
		try 
		{
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
			Long specimenId = (Long)specimen.getId();	
			Collection childSpecimens = (Collection)bizLogic.retrieveAttribute(Specimen.class,specimenId, "elements(childSpecimenCollection)");

			//Collection childSpecimens = specimen.getChildrenSpecimen();
			if(childSpecimens!=null)
			{	
				Iterator childItr = childSpecimens.iterator();  
				while(childItr.hasNext() )
				{
					Specimen childSpecimen = (Specimen)childItr.next();
					consentWithdrawForchildSpecimens(childSpecimen , dao,  sessionDataBean, consentWithdrawalOption, consentTierID);
				}
			}
		} catch (DAOException e) {
			logger.debug(e.getMessage(), e);
			throw AppUtility.getApplicationException(e, "dao.error", "");
		}
	}
	
	private static void consentWithdrawForchildSpecimens(Specimen specimen, DAO dao, SessionDataBean sessionDataBean, String consentWithdrawalOption, long consentTierID) throws ApplicationException 
	{
		if(specimen!=null)
		{
			updateSpecimenStatus(specimen,  consentWithdrawalOption, consentTierID, dao, sessionDataBean);
			Collection childSpecimens = specimen.getChildSpecimenCollection();
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
	 * @throws BizLogicException 
	 */
	public static void setConsentsFromParent(Specimen specimen, Specimen parentSpecimen, DAO dao) throws BizLogicException
	{
		Collection consentTierStatusCollection = new HashSet();
		//Lazy Resolved ----  parentSpecimen.getConsentTierStatusCollection();
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		Collection parentStatusCollection = (Collection)bizLogic.retrieveAttribute(Specimen.class, parentSpecimen.getId(), "elements(consentTierStatusCollection)"); 
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
			if(event.getClass().getSimpleName().equals(eventType))
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
	 * @throws BizLogicException 
	 */
	public static void updateSpecimenStatusInSCG(SpecimenCollectionGroup specimenCollectionGroup,SpecimenCollectionGroup oldSpecimenCollectionGroup, DAO dao) throws BizLogicException
	{
		Collection newConsentTierStatusCollection = specimenCollectionGroup.getConsentTierStatusCollection();
		Collection oldConsentTierStatusCollection =  oldSpecimenCollectionGroup.getConsentTierStatusCollection();
		Iterator itr = newConsentTierStatusCollection.iterator() ;
		while(itr.hasNext() )
		{
			ConsentTierStatus consentTierStatus = (ConsentTierStatus)itr.next();
			String statusValue = consentTierStatus.getStatus();
			long consentTierID = consentTierStatus.getConsentTier().getId().longValue();
			updateSCGSpecimenCollection(specimenCollectionGroup, oldSpecimenCollectionGroup, consentTierID, statusValue, newConsentTierStatusCollection, oldConsentTierStatusCollection,dao);	
		}
	}

	/*
	 * This method updates the specimen consent status. 
	 */
	private static void updateSCGSpecimenCollection(SpecimenCollectionGroup specimenCollectionGroup, SpecimenCollectionGroup oldSpecimenCollectionGroup, long consentTierID, String  statusValue, Collection newSCGConsentCollection, Collection oldSCGConsentCollection,DAO dao) throws BizLogicException
	{
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		Collection specimenCollection = (Collection)bizLogic.retrieveAttribute(SpecimenCollectionGroup.class, specimenCollectionGroup.getId(),"elements(specimenCollection)");  
		//oldSpecimenCollectionGroup.getSpecimenCollection();
		Collection updatedSpecimenCollection = new HashSet();
		String applyChangesTo =  specimenCollectionGroup.getApplyChangesTo(); 
		Iterator specimenItr = specimenCollection.iterator() ;
		while(specimenItr.hasNext() )
		{
			Specimen specimen = (Specimen)specimenItr.next();
			updateSpecimenConsentStatus(specimen, applyChangesTo, consentTierID, statusValue, newSCGConsentCollection, oldSCGConsentCollection, dao );
			updatedSpecimenCollection.add(specimen );
		}
		specimenCollectionGroup.setSpecimenCollection(updatedSpecimenCollection );
	}
	
	public static void updateSpecimenConsentStatus(Specimen specimen, String applyChangesTo, long consentTierID, String  statusValue, Collection newConsentCollection, Collection oldConsentCollection,DAO dao) throws BizLogicException
	{
		if(applyChangesTo.equalsIgnoreCase(Constants.APPLY_ALL))
			updateSpecimenConsentStatus(specimen, consentTierID, statusValue, applyChangesTo, dao);
		else if(applyChangesTo.equalsIgnoreCase(Constants.APPLY))
		{
			//To pass both collections
			checkConflictingConsents(newConsentCollection, oldConsentCollection, specimen, dao);
		}
	}
	
	/**
	 * This method updates the Specimen instance by setting all the consenttierstatus to withdraw. 
	 * @param specimen  Instance of Specimen to be updated. 
	 * @param consentWithdrawalOption Action to be performed on the withdrawn specimen.
	 * @param consentTierID Identifier of ConsentTier to be withdrawn.
	 * @throws BizLogicException 
	 */
	private static void updateSpecimenConsentStatus(Specimen specimen, long consentTierID, String statusValue, String applyChangesTo, DAO dao) throws BizLogicException
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
			updatedSpecimenStatusCollection.add(consentTierstatus);
		}
		specimen.setConsentTierStatusCollection(updatedSpecimenStatusCollection);
		
		//to update child specimens
		Collection childSpecimens = specimen.getChildSpecimenCollection();
		Iterator childItr = childSpecimens.iterator();  
		while(childItr.hasNext() )
		{
			Specimen childSpecimen = (Specimen)childItr.next();
			consentStatusUpdateForchildSpecimens(childSpecimen , consentTierID, statusValue ,applyChangesTo, dao);
		}
	}

	private static void consentStatusUpdateForchildSpecimens(Specimen specimen, long consentTierID, String statusValue, String applyChangesTo, DAO dao) throws BizLogicException
	{
		if(specimen!=null)
		{
			updateSpecimenConsentStatus(specimen, consentTierID, statusValue, applyChangesTo, dao);
			Collection childSpecimens = specimen.getChildSpecimenCollection();
			Iterator itr = childSpecimens.iterator();  
			while(itr.hasNext() )
			{
				Specimen childSpecimen = (Specimen)itr.next();
				consentStatusUpdateForchildSpecimens(childSpecimen, consentTierID, statusValue, applyChangesTo, dao);
			}
		}
	}

	/*
	 * This method verifies the consents of SCG and specimen for any conflicts.
	 * 1. Check for conflciting consent status
	 * 		a. Create a Map<ConsentTiedId,ConsentStatus> of curent specimen consents
	 * 		b. Iterate over a oldStatusCollection and check the status against above map
	 *  	c. If all are matching then set is changable true, else if any obne status failt ot match 
	 *  	   set flag false adn break
	 * 2. Set new status to specimen if no conflict from above step
	 * 		a.  Iterate over specimen consent colelction
	 *      b. Iterate over nEw consent collection
	 *      c. check if Consenttier id of specimen and new colelction is macth if yes change 
	 *         the status value of specimen consent
	 *      d. Put all changed Sepcimen ConsnetStatus object in new collection updatedSpecimenConsentStatusCollection 
	 *         and set updatedSpecimenConsentStatusCollection in specimen.
	 */
	private static void checkConflictingConsents(Collection newConsentCollection, Collection oldConsentCollection, Specimen specimen, DAO dao ) throws BizLogicException
	{
/*		 if oldSCG.c1 == S.c1 then update specimen with new SCG.c1
 * 			OR
 *		 if oldS.c1 == cS.c1 then update child specimen with new S.c1
 */		
		//bug 8381 start
		
		Map<Long,ConsentTierStatus> specimenConsentsMap = new HashMap<Long,ConsentTierStatus>();
		Collection specimenConsentStatusCollection = specimen.getConsentTierStatusCollection();
		Iterator specimenConsentStatusItr = specimenConsentStatusCollection.iterator() ;
		
		while(specimenConsentStatusItr.hasNext() )
		{
			ConsentTierStatus specimenConsentStatus = (ConsentTierStatus)specimenConsentStatusItr.next() ;
			specimenConsentsMap.put(specimenConsentStatus.getConsentTier().getId(), specimenConsentStatus);
		}
		boolean isChangable = true;
		
		Iterator oldConsentItr = oldConsentCollection.iterator();
		while(oldConsentItr.hasNext())
		{
			ConsentTierStatus oldConsentStatus = (ConsentTierStatus)oldConsentItr.next() ;
			Long consentTierid = oldConsentStatus.getConsentTier().getId();			
			ConsentTierStatus specimenConsentStatus = specimenConsentsMap.get(consentTierid);
			if(!oldConsentStatus.getStatus().equals(specimenConsentStatus.getStatus()))
			{
				isChangable = false;
				break;
			}
		}
		
		if(isChangable)
		{
			Collection updatedSpecimenConsentStatusCollection = new HashSet();			
			Iterator specimenConsentItr = specimenConsentStatusCollection.iterator() ;
			while(specimenConsentItr.hasNext())
			{
				ConsentTierStatus specimenConsent = (ConsentTierStatus) specimenConsentItr.next();
				Iterator newConsentItr = newConsentCollection.iterator();
				while(newConsentItr.hasNext() )
				{
					ConsentTierStatus newConsentStatus = (ConsentTierStatus)newConsentItr.next() ;
					if(newConsentStatus.getConsentTier().getId().longValue() == specimenConsent.getConsentTier().getId().longValue())
					{
						specimenConsent.setStatus(newConsentStatus.getStatus()); 
					}
				}
				updatedSpecimenConsentStatusCollection.add(specimenConsent);
			}
			specimen.setConsentTierStatusCollection(updatedSpecimenConsentStatusCollection);			
		}
		//bug 8381 end
		//to update child specimens
		Collection childSpecimens = specimen.getChildSpecimenCollection();
		Iterator childItr = childSpecimens.iterator();  
		while(childItr.hasNext() )
		{
			Specimen childSpecimen = (Specimen)childItr.next();
			consentStatusUpdateForchildSpecimens(childSpecimen , newConsentCollection, oldConsentCollection, dao);
		}
	}
	
	private static void consentStatusUpdateForchildSpecimens(Specimen specimen, Collection newConsentCollection, Collection oldConsentCollection, DAO dao) throws BizLogicException
	{
		if(specimen!=null)
		{
			checkConflictingConsents(newConsentCollection, oldConsentCollection, specimen, dao);
			Collection childSpecimens = specimen.getChildSpecimenCollection();
			Iterator itr = childSpecimens.iterator();  
			while(itr.hasNext() )
			{
				Specimen childSpecimen = (Specimen)itr.next();
				consentStatusUpdateForchildSpecimens(childSpecimen, newConsentCollection, oldConsentCollection, dao);
			}
		}
	}
	// ------------------------ Mandar : 24-Jan-07 Apply changes --------- end
	/**
	 * This function is used for retriving Specimen collection group  from Collection protocol registration Object
	 * @param specimenObj
	 * @param finalDataList
	 * @throws BizLogicException 
	 */
	public static void getSpecimenDetails(CollectionProtocolRegistration collectionProtocolRegistration, List finalDataList) throws ApplicationException
	{
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		Collection specimencollectionGroup = (Collection)bizLogic.retrieveAttribute(CollectionProtocolRegistration.class.getName(),collectionProtocolRegistration.getId(), "elements(specimenCollectionGroupCollection)");
		//Collection specimencollectionGroup = collectionProtocolRegistration.getSpecimenCollectionGroupCollection();
		Iterator specimenCollGroupIterator = specimencollectionGroup.iterator();
		while(specimenCollGroupIterator.hasNext())
		{
			SpecimenCollectionGroup specimenCollectionGroupObj =(SpecimenCollectionGroup)specimenCollGroupIterator.next(); 
			getDetailsOfSpecimen(specimenCollectionGroupObj, finalDataList);
		}		
	}
	/**
	 * This function is used for retriving specimen and sub specimen's attributes.
	 * @param specimenObj
	 * @param finalDataList
	 * @throws BizLogicException 
	 */
	private static void getDetailsOfSpecimen(SpecimenCollectionGroup specimenCollGroupObj, List finalDataList) throws ApplicationException
	{
		// lazy Resolved specimenCollGroupObj.getSpecimenCollection();
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		Collection specimenCollection = (Collection)bizLogic.retrieveAttribute(SpecimenCollectionGroup.class.getName(), specimenCollGroupObj.getId(), "elements(specimenCollection)");
		Iterator specimenIterator = specimenCollection.iterator();
		while(specimenIterator.hasNext())
		{
			Specimen specimenObj =(Specimen)specimenIterator.next();
			List specimenDetailList=new ArrayList();
			if(specimenObj.getActivityStatus().equals(Status.ACTIVITY_STATUS_ACTIVE.toString()))
			{
				specimenDetailList.add(specimenObj.getLabel());
				specimenDetailList.add(specimenObj.getSpecimenType());
				if(specimenObj.getSpecimenPosition()==null)
				{
					specimenDetailList.add(Constants.VIRTUALLY_LOCATED);
				}
				else
				{
					SpecimenPosition position=	(SpecimenPosition)bizLogic.retrieveAttribute(Specimen.class.getName(), specimenObj.getId(),"specimenPosition");
					String storageLocation=position.getStorageContainer().getName()+": X-Axis-"+position.getPositionDimensionOne()+", Y-Axis-"+position.getPositionDimensionTwo();
					specimenDetailList.add(storageLocation);
				}
				specimenDetailList.add(specimenObj.getClassName());
				finalDataList.add(specimenDetailList);
			}
		}
		
	}
	/**
	 * Adding name,value pair in NameValueBean for Witness Name
	 * @param collProtId Get Witness List for this ID
	 * @return consentWitnessList
	 */ 
	public static List witnessNameList(String collProtId) throws ApplicationException
	{		
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		
		Object object = bizLogic.retrieve(CollectionProtocol.class.getName(), Long.valueOf(collProtId));		
		CollectionProtocol collectionProtocol = (CollectionProtocol) object;
		//Setting the consent witness
		String witnessFullName="";
		List consentWitnessList = new ArrayList();
		consentWitnessList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
		Collection userCollection = null;
		if(collectionProtocol.getId()!= null)
		{ 
			userCollection = (Collection)bizLogic.retrieveAttribute(CollectionProtocol.class.getName(),collectionProtocol.getId(), "elements(coordinatorCollection)");
		}
		
		Iterator iter = userCollection.iterator();
		while(iter.hasNext())
		{
			User user = (User)iter.next();
			witnessFullName = user.getLastName()+", "+user.getFirstName();
			consentWitnessList.add(new NameValueBean(witnessFullName,user.getId()));
		}
		//Setting the PI
		User principalInvestigator = (User)bizLogic.retrieveAttribute(CollectionProtocol.class.getName(),collectionProtocol.getId(), "principalInvestigator");
		String piFullName=principalInvestigator.getLastName()+", "+principalInvestigator.getFirstName();
		consentWitnessList.add(new NameValueBean(piFullName,principalInvestigator.getId()));
		return consentWitnessList;
	}	
	/**
	 * This function adds the columns to the List
	 * @return columnList 
	 */
	public static List<String> columnNames()
	{
		List<String> columnList = new ArrayList<String>();
		columnList.add(Constants.LABLE);
		columnList.add(Constants.TYPE);
		columnList.add(Constants.STORAGE_CONTAINER_LOCATION);
		columnList.add(Constants.CLASS_NAME);
		return columnList; 
	}
	/**
	 * Adding name,value pair in NameValueBean for Witness Name
	 * @param collProtId Get Witness List for this ID
	 * @return consentWitnessList
	 */ 
	public static Collection getConsentList(String collectionProtocolID) throws ApplicationException
    {   	
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
    	CollectionProtocolBizLogic collectionProtocolBizLogic = (CollectionProtocolBizLogic)factory.getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
    	Object object  = collectionProtocolBizLogic.retrieve(CollectionProtocol.class.getName(), Long.valueOf(collectionProtocolID));		
		CollectionProtocol collectionProtocol = (CollectionProtocol) object;
		Collection consentTierCollection = (Collection)collectionProtocolBizLogic.retrieveAttribute(CollectionProtocol.class.getName(), collectionProtocol.getId(), "elements(consentTierCollection)");
		return consentTierCollection;
    }
	
	/**
	* For ConsentTracking Preparing consentResponseForScgValues for populating Dynamic contents on the UI  
	* @param partiResponseCollection This Containes the collection of ConsentTier Response at CPR level
	* @param statusResponseCollection This Containes the collection of ConsentTier Response at Specimen level 
	* @return tempMap
	*/
    public static Map prepareSCGResponseMap(Collection statusResponseCollection,
    		Collection partiResponseCollection,String statusResponse, String statusResponseId)
	   {
	    	//Map tempMap = new HashMap();
    	    Map tempMap = new LinkedHashMap();//8905
    	    Map returnMap = null;
    	    Set sortedStatusSet = new LinkedHashSet();
	    	List sortStatus = new ArrayList();
	    	//bug 8905
	    	sortStatus.addAll(statusResponseCollection);
	    	Collections.sort(sortStatus,new IdComparator());
	    	sortedStatusSet.addAll(sortStatus);
//	    	bug 8905
	    	Long consentTierID;
			Long consentID;
			if(partiResponseCollection!=null ||sortedStatusSet!=null)
			{
				int i = 0;
				Iterator statusResponsIter = sortedStatusSet.iterator();			
				while(statusResponsIter.hasNext())
				{
					ConsentTierStatus consentTierstatus=(ConsentTierStatus)statusResponsIter.next();
					consentTierID=consentTierstatus.getConsentTier().getId();
					Iterator participantResponseIter = partiResponseCollection.iterator();
					while(participantResponseIter.hasNext())
					{
						ConsentTierResponse consentTierResponse=(ConsentTierResponse)participantResponseIter.next();
						consentID=consentTierResponse.getConsentTier().getId();
						if(consentTierID.longValue()==consentID.longValue())						
						{
							ConsentTier consent = consentTierResponse.getConsentTier();
							String idKey="ConsentBean:"+i+"_consentTierID";
							String statementKey="ConsentBean:"+i+"_statement";
							String participantResponsekey = "ConsentBean:"+i+"_participantResponse";
							String participantResponceIdKey="ConsentBean:"+i+"_participantResponseID";
							String statusResponsekey  = "ConsentBean:"+i+statusResponse;
							String statusResponseIDkey ="ConsentBean:"+i+statusResponseId;
							
							tempMap.put(idKey, consent.getId());
							tempMap.put(statementKey,consent.getStatement());
							tempMap.put(participantResponsekey, consentTierResponse.getResponse());
							tempMap.put(participantResponceIdKey, consentTierResponse.getId());
							tempMap.put(statusResponsekey, consentTierstatus.getStatus());
							tempMap.put(statusResponseIDkey, consentTierstatus.getId());
							i++;
							break;
						}
					}
				}
				returnMap=tempMap;
			}		
				return returnMap;
	   }
    
    /**
	 * @param consentTierResponseCollection
	 * @param iter
	 */
	public static void createConsentResponseColl(Collection consentTierResponseCollection, Iterator iter)
	{
		List<ConsentTierResponse> consentsList = new ArrayList<ConsentTierResponse>();
		while(iter.hasNext())
		{
			ConsentBean consentBean = (ConsentBean)iter.next();
			ConsentTierResponse consentTierResponse = new ConsentTierResponse();
			//Setting response
			consentTierResponse.setResponse(consentBean.getParticipantResponse());
			if(consentBean.getParticipantResponseID()!=null&&consentBean.getParticipantResponseID().trim().length()>0)
			{
				consentTierResponse.setId(Long.parseLong(consentBean.getParticipantResponseID()));
			}
			//Setting consent tier
			ConsentTier consentTier = new ConsentTier();
			consentTier.setId(Long.parseLong(consentBean.getConsentTierID()));
			consentTier.setStatement(consentBean.getStatement());
			consentTierResponse.setConsentTier(consentTier);
			//consentTierResponseCollection.add(consentTierResponse);
			consentsList.add(consentTierResponse);
		}
//	      bug 8905
		Comparator consentTierComparator = new IdComparator();
		Collections.sort(consentsList, consentTierComparator);
		Iterator iterList = consentsList.iterator();
		while(iterList.hasNext())
        {
        	ConsentTierResponse consentTierResponse = (ConsentTierResponse)iterList.next();
        	consentTierResponseCollection.add(consentTierResponse);
        }				
//		bug 8905		
	}
}
