/**
 * <p>Title: UserHDAO Class>
 * <p>Description:	UserHDAO is used to add user information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ajay Sharma
 * @version 1.00
 * Created on Apr 13, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.springframework.dao.PessimisticLockingFailureException;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.NameGeneratorException;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CollectionProtocolSeqComprator;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.WithdrawConsentUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * UserHDAO is used to add user information into the database using Hibernate.
 * 
 * @author kapil_kaveeshwar
 */
public class SpecimenCollectionGroupBizLogic extends DefaultBizLogic
{
	/**
	 * Saves the user object in the database.
	 * 
	 * @param obj
	 *            The user object to be saved.
	 * @param session
	 *            The session in which the object is saved.
	 * @throws DAOException
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;

		if (specimenCollectionGroup.getSpecimenCollectionSite() != null)
		{
			Object siteObj = dao.retrieve(Site.class.getName(), specimenCollectionGroup.getSpecimenCollectionSite().getId());
			if (siteObj != null)
			{
				// check for closed Site
				checkStatus(dao, specimenCollectionGroup.getSpecimenCollectionSite(), "Site");

				specimenCollectionGroup.setSpecimenCollectionSite((Site) siteObj);
			}
		}
		Object collectionProtocolEventObj = dao.retrieve(CollectionProtocolEvent.class.getName(), specimenCollectionGroup
				.getCollectionProtocolEvent().getId());


		Collection specimenCollection = null;
		Long userId = Utility.getUserID(dao, sessionDataBean);
		if (collectionProtocolEventObj != null)
		{
			CollectionProtocolEvent cpe = (CollectionProtocolEvent) collectionProtocolEventObj;

			// check for closed CollectionProtocol
			checkStatus(dao, cpe.getCollectionProtocol(), "Collection Protocol");

			specimenCollectionGroup.setCollectionProtocolEvent(cpe);
			
			SpecimenCollectionRequirementGroup specimenCollectionRequirementGroup = 
				(SpecimenCollectionRequirementGroup) cpe.getRequiredCollectionSpecimenGroup(); 
			
			specimenCollection = getCollectionSpecimen(specimenCollectionGroup, specimenCollectionRequirementGroup, userId );
			
		}

		setCollectionProtocolRegistration(dao, specimenCollectionGroup, null);
		generateSCGLabel(specimenCollectionGroup);
		dao.insert(specimenCollectionGroup, sessionDataBean, true, true);
		
		if (specimenCollection != null)
		{
	       new NewSpecimenBizLogic().insert(specimenCollection, dao, sessionDataBean);
		}

	}

	/**
	 * @param specimenCollectionGroup
	 * @param specimenCollectionRequirementGroup
	 * @param userId
	 * @return
	 */
	private Collection getCollectionSpecimen(SpecimenCollectionGroup specimenCollectionGroup,
			SpecimenCollectionRequirementGroup specimenCollectionRequirementGroup, Long userId)
	{

		
		Collection cloneSpecimenCollection = null;
		//Long userId = null;
        try {
		
		Collection specimenCollection = specimenCollectionRequirementGroup.getSpecimenCollection();
		//Collection specimenList = CollectionProtocolUtil.sortJuber(specimenCollection);
		List specimenList = new LinkedList(specimenCollection);
		CollectionProtocolUtil.getSortedCPEventList(specimenList);

		if (specimenList != null && !specimenList.isEmpty())
		{
			//userId = getUserID(dao, sessionDataBean);
			cloneSpecimenCollection = new LinkedHashSet();
			Iterator itSpecimenCollection = specimenList.iterator();
			while (itSpecimenCollection.hasNext())
			{
				Specimen specimen = (Specimen) itSpecimenCollection.next();
				if (Constants.NEW_SPECIMEN.equals(specimen.getLineage()))
				{
					Specimen cloneSpecimen = getCloneSpecimen(specimen, null, specimenCollectionGroup, userId);
					//kalpana : bug #6224
					if (edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl)
					{
						LabelGenerator specimenLableGenerator = LabelGeneratorFactory
								.getInstance(Constants.SPECIMEN_LABEL_GENERATOR_PROPERTY_NAME);
						specimenLableGenerator.setLabel(cloneSpecimen);
					}
					cloneSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
					cloneSpecimenCollection.add(cloneSpecimen);
				}
			}
		}
		//				specimenCollectionGroup.setOffset(collectionProtocolRegistration.getOffset());
	//	specimenCollectionGroup.setSpecimenCollection(cloneSpecimenCollection);
		
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        return cloneSpecimenCollection;

	}

	/**
	 * @param specimen
	 * @param pSpecimen
	 * @param specimenCollectionGroup
	 * @param userId
	 * @return
	 */
	private Specimen getCloneSpecimen(Specimen specimen, Specimen pSpecimen, SpecimenCollectionGroup specimenCollectionGroup, Long userId)
	{

		Specimen newSpecimen = specimen.createClone();
		newSpecimen.setParentSpecimen(pSpecimen);
		newSpecimen.setDefaultSpecimenEventCollection(userId);
		newSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
		newSpecimen.setConsentTierStatusCollectionFromSCG(specimenCollectionGroup);

		Collection childrenSpecimenCollection = specimen.getChildrenSpecimen();
		if (childrenSpecimenCollection != null && !childrenSpecimenCollection.isEmpty())
		{
			Collection<Specimen> childrenSpecimen = new LinkedHashSet<Specimen>();
			Iterator<Specimen> it = childrenSpecimenCollection.iterator();
			while (it.hasNext())
			{
				Specimen childSpecimen = it.next();
				Specimen newchildSpecimen = getCloneSpecimen(childSpecimen, newSpecimen, specimenCollectionGroup, userId);
				childrenSpecimen.add(newchildSpecimen);
			}
			newSpecimen.setChildrenSpecimen(childrenSpecimen);
		}

		return newSpecimen;
	}
	
	/**
	 * @param specimenCollectionGroup
	 * @throws DAOException
	 */
	private void generateSCGLabel(
			SpecimenCollectionGroup specimenCollectionGroup)
			throws DAOException
	{
		try
		{
			LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory
			.getInstance(Constants.SPECIMEN_COLL_GROUP_LABEL_GENERATOR_PROPERTY_NAME);
			specimenCollectionGroupLableGenerator.setLabel(specimenCollectionGroup);
		}
		catch(NameGeneratorException nameGeneratorException)
		{
			throw new DAOException(nameGeneratorException.getMessage(),nameGeneratorException);
		}
	}

	
	/**
	 * This function used to get specimenCollectionGroup object from id and 
	 * populate all its associated entities.
	 * @param scgId 				SpecimenCollectionGroup id
	 * @param bean 					SessionDataBean
	 * @param retrieveAssociates	flag for retrieve associated entities or not.
	 * @return object of CollectionProtocol
	 * @throws BizLogicException If fails to retrieve any of the required entity.
	 */
	public SpecimenCollectionGroup getSCGFromId(Long scgId, SessionDataBean bean, boolean retrieveAssociates) throws BizLogicException
	{
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			dao.openSession(bean);

			List cpList = dao.retrieve(SpecimenCollectionGroup.class.getName(), Constants.SYSTEM_IDENTIFIER, scgId);

			if (cpList == null || cpList.isEmpty())
			{
				throw new BizLogicException("Cannot find CP. Failed to find " + "SCG for id " + scgId);
			}
			SpecimenCollectionGroup specCollGroup = (SpecimenCollectionGroup) cpList.get(0);
			if (retrieveAssociates)
			{
				retreiveAssociates(scgId, specCollGroup);
			}
			return specCollGroup;
		}
		catch (DAOException exception)
		{
			throw new BizLogicException(exception.getMessage(), exception);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException daoException)
			{
				Logger.out.fatal("Failed to close session due to " + daoException.getMessage(), daoException);
			}
		}
	}

	/**
	 * 
	 * @param scgId
	 * @param specCollGroup
	 * @throws BizLogicException
	 */
	private void retreiveAssociates(Long scgId, SpecimenCollectionGroup specCollGroup) throws BizLogicException
	{
		CollectionProtocolRegistration collProtReg = specCollGroup.getCollectionProtocolRegistration();

		if (collProtReg == null)
		{
			throw new BizLogicException("Cannot find CP. CPR for " + "SCG id " + scgId + " is unexpectedly null.");
		}
		CollectionProtocol collProt = collProtReg.getCollectionProtocol();
		if (collProt == null)
		{
			throw new BizLogicException("Cannot find CP. for SCG id " + scgId);
		}
		Long id = collProt.getId();

		Collection<Specimen> specimenCollection = specCollGroup.getSpecimenCollection();
		retrieveSpecimens(specimenCollection);

	}

	/**
	 * @param specimenCollection
	 */
	private void retrieveSpecimens(Collection<Specimen> specimenCollection)
	{
		if (specimenCollection == null)
		{
			return;
		}

		Iterator<Specimen> specIterator = specimenCollection.iterator();
		while (specIterator.hasNext())
		{
			Specimen specimen = specIterator.next();
			Collection<Specimen> childSpecimenCollection = specimen.getChildrenSpecimen();
			retrieveSpecimens(childSpecimenCollection);
		}
	}

	private Set getProtectionObjects(AbstractDomainObject obj)
	{
		Set protectionObjects = new HashSet();

		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
		protectionObjects.add(specimenCollectionGroup);

		Logger.out.debug(protectionObjects.toString());
		return protectionObjects;
	}

	protected String[] getDynamicGroups(AbstractDomainObject obj) throws SMException
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
		String[] dynamicGroups = new String[1];

		dynamicGroups[0] = SecurityManager.getInstance(this.getClass()).getProtectionGroupByName(
				specimenCollectionGroup.getCollectionProtocolRegistration(), Constants.getCollectionProtocolPGName(null));
		Logger.out.debug("Dynamic Group name: " + dynamicGroups[0]);
		return dynamicGroups;

	}

	/**
	 * Updates the persistent object in the database.
	 * 
	 * @param obj
	 *            The object to be updated.
	 * @param session
	 *            The session in which the object is saved.
	 * @throws DAOException
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
		SpecimenCollectionGroup oldspecimenCollectionGroup = (SpecimenCollectionGroup) oldObj;
		//lazy false change
		
		List scgList = dao.retrieve(SpecimenCollectionGroup.class.getName(), Constants.ID,
								oldspecimenCollectionGroup.getId());
		SpecimenCollectionGroup persistentSCG = null;
		if(scgList!=null && !scgList.isEmpty())
		{
			persistentSCG = (SpecimenCollectionGroup)scgList.get(0);
		}
		
		// Adding default events if they are null from API
		Collection spEventColl = specimenCollectionGroup.getSpecimenEventParametersCollection();
		if (spEventColl == null || spEventColl.isEmpty())
		{
			setDefaultEvents(specimenCollectionGroup, sessionDataBean);
		}
		else
		{
			Collection pEvtPrmColl = persistentSCG.getSpecimenEventParametersCollection();
			Iterator evntIterator = pEvtPrmColl.iterator();
			if(pEvtPrmColl==null || pEvtPrmColl.isEmpty())
			{
				persistentSCG.setSpecimenEventParametersCollection(spEventColl);
			}
			while(evntIterator.hasNext())
			{
				SpecimenEventParameters event= (SpecimenEventParameters) evntIterator.next();
				SpecimenEventParameters newEvent =(SpecimenEventParameters)
												getCorrespondingObject(spEventColl, event.getClass());
				updateEvent(event, newEvent);
				//spEventColl.remove(newEvent);
				
			}
		}		
		// Check for different closed site
		Site oldSite = oldspecimenCollectionGroup.getSpecimenCollectionSite();
		Site site = specimenCollectionGroup.getSpecimenCollectionSite();
		if (oldSite == null && site != null)
		{
			checkStatus(dao, site, "Site");
		}
		else if (!site.getId().equals(oldSite.getId()))
		{
			checkStatus(dao, site, "Site");
		}
		
		// site check complete
		Long oldEventId = oldspecimenCollectionGroup.getCollectionProtocolEvent().getId();
		Long eventId = specimenCollectionGroup.getCollectionProtocolEvent().getId();
		if (oldEventId.longValue() != eventId.longValue())
		{
			// -- check for closed CollectionProtocol
			List list = dao.retrieve(CollectionProtocolEvent.class.getName(), Constants.SYSTEM_IDENTIFIER, specimenCollectionGroup
					.getCollectionProtocolEvent().getId());
			if (!list.isEmpty())
			{
				// check for closed CollectionProtocol
				CollectionProtocolEvent cpe = (CollectionProtocolEvent) list.get(0);
				if (!cpe.getCollectionProtocol().getId().equals(
						oldspecimenCollectionGroup.getCollectionProtocolEvent().getCollectionProtocol().getId()))
					checkStatus(dao, cpe.getCollectionProtocol(), "Collection Protocol");

				specimenCollectionGroup.setCollectionProtocolEvent((CollectionProtocolEvent) list.get(0));
			}
		}
		// CollectionProtocol check complete.

		//setCollectionProtocolRegistration(dao, specimenCollectionGroup, oldspecimenCollectionGroup);

		// Mandar 22-Jan-07 To disable consents accordingly in SCG and
		// Specimen(s) start

		if (!Constants.WITHDRAW_RESPONSE_NOACTION.equalsIgnoreCase(specimenCollectionGroup.getConsentWithdrawalOption()))
		{
			verifyAndUpdateConsentWithdrawn(specimenCollectionGroup, oldspecimenCollectionGroup, dao, sessionDataBean);
			persistentSCG.setConsentTierStatusCollection(specimenCollectionGroup.getConsentTierStatusCollection());

		}
		// Mandar 22-Jan-07 To disable consents accordingly in SCG and
		// Specimen(s) end
		// Mandar 24-Jan-07 To update consents accordingly in SCG and
		// Specimen(s) start
		else if (!specimenCollectionGroup.getApplyChangesTo().equalsIgnoreCase(Constants.APPLY_NONE))
		{
			WithdrawConsentUtil.updateSpecimenStatusInSCG(specimenCollectionGroup, oldspecimenCollectionGroup, dao);
		}
		// Mandar 24-Jan-07 To update consents accordingly in SCG and
		// Specimen(s) end
		specimenCollectionGroup.setCollectionProtocolRegistration(
				persistentSCG.getCollectionProtocolRegistration());
		
		Integer offset = specimenCollectionGroup.getOffset();
		if (offset != null)
		{
			
			if (oldspecimenCollectionGroup.getOffset() != null)
				offset = offset - oldspecimenCollectionGroup.getOffset();
			if (offset != 0)
			{
				//updateOffset(offset, specimenCollectionGroup, sessionDataBean, dao);
				getDetailsOfCPRForSCG(specimenCollectionGroup.getCollectionProtocolRegistration(), dao);
				CollectionProtocolRegistrationBizLogic cprBizLogic = new CollectionProtocolRegistrationBizLogic();
				cprBizLogic.checkAndUpdateChildOffset(dao, sessionDataBean, oldspecimenCollectionGroup.getCollectionProtocolRegistration(), offset
						.intValue());
				cprBizLogic.updateForOffset(dao, sessionDataBean, specimenCollectionGroup.getCollectionProtocolRegistration(), offset.intValue());
			}

		}
		
		persistentSCG.setSpecimenCollectionSite(site);
		persistentSCG.setOffset(offset);
		persistentSCG.setCollectionStatus(specimenCollectionGroup.getCollectionStatus());
		persistentSCG.setComment(specimenCollectionGroup.getComment());
		persistentSCG.setActivityStatus(specimenCollectionGroup.getActivityStatus());
		persistentSCG.setSurgicalPathologyNumber(specimenCollectionGroup.getSurgicalPathologyNumber());
		persistentSCG.setClinicalDiagnosis(specimenCollectionGroup.getClinicalDiagnosis());
		persistentSCG.setClinicalStatus(specimenCollectionGroup.getClinicalStatus());
		persistentSCG.setName(specimenCollectionGroup.getName());
		persistentSCG.setConsentTierStatusCollection(specimenCollectionGroup.getConsentTierStatusCollection());
		dao.update(persistentSCG, sessionDataBean, true, true, false);
		/**
		 * Name : Ashish Gupta Reviewer Name : Sachin Lale Bug ID: 2741 Patch
		 * ID: 2741_6 Description: Method to update events in all specimens
		 * related to this scg
		 */
		// Populating Events in all specimens
		if (specimenCollectionGroup.isApplyEventsToSpecimens())
		{
			updateEvents(specimenCollectionGroup, oldspecimenCollectionGroup, dao, sessionDataBean);
		}
		// Audit.
		dao.audit(obj, oldObj, sessionDataBean, true);
		SpecimenCollectionGroup oldSpecimenCollectionGroup = (SpecimenCollectionGroup) oldObj;

		// Disable the related specimens to this specimen group
		Logger.out.debug("specimenCollectionGroup.getActivityStatus() " + specimenCollectionGroup.getActivityStatus());
		if (specimenCollectionGroup.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("specimenCollectionGroup.getActivityStatus() " + specimenCollectionGroup.getActivityStatus());
			Long specimenCollectionGroupIDArr[] = {specimenCollectionGroup.getId()};

			NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			bizLogic.disableRelatedObjectsForSpecimenCollectionGroup(dao, specimenCollectionGroupIDArr);
		}

	}
	
	private Object getCorrespondingObject(Collection objectCollection, Class eventClass)
	{
		Iterator iterator = objectCollection.iterator();
		while (iterator.hasNext())
		{
			AbstractDomainObject abstractDomainObject = (AbstractDomainObject) iterator.next();
			if (abstractDomainObject.getClass().hashCode()== eventClass.hashCode())
			{
				return abstractDomainObject;
			}
		}

		return null;
	}
	
	private void updateEvent(SpecimenEventParameters event , SpecimenEventParameters newEvent)
	{
		if(event instanceof CollectionEventParameters)
		{
			CollectionEventParameters toChangeCollectionEventParameters = (CollectionEventParameters)event;
			CollectionEventParameters newCollectionEventParameters = (CollectionEventParameters)newEvent;
			
			toChangeCollectionEventParameters.setUser(newCollectionEventParameters.getUser());
			toChangeCollectionEventParameters.setTimestamp(newCollectionEventParameters.getTimestamp());
			toChangeCollectionEventParameters.setCollectionProcedure(newCollectionEventParameters.getCollectionProcedure());
			toChangeCollectionEventParameters.setComment(newCollectionEventParameters.getComment());
			toChangeCollectionEventParameters.setContainer(newCollectionEventParameters.getContainer());
			
		}
		else
		{
			ReceivedEventParameters toChanagereceivedEventParameters = (ReceivedEventParameters)event;
			ReceivedEventParameters newreceivedEventParameters = (ReceivedEventParameters)newEvent;
			
			toChanagereceivedEventParameters.setComment(newreceivedEventParameters.getComment());
			toChanagereceivedEventParameters.setReceivedQuality(newreceivedEventParameters.getReceivedQuality());
			toChanagereceivedEventParameters.setTimestamp(newreceivedEventParameters.getTimestamp());
			toChanagereceivedEventParameters.setUser(newreceivedEventParameters.getUser());
			
		}
	}
	
	/**
	 * @param specimenCollectionGroup
	 * @param sessionDataBean
	 *            Sets the default events if they are not specified
	 */
	private void setDefaultEvents(SpecimenCollectionGroup specimenCollectionGroup, SessionDataBean sessionDataBean)
	{
		Collection specimenEventColl = new HashSet();
		User user = new User();
		user.setId(sessionDataBean.getUserId());
		CollectionEventParameters collectionEventParameters = EventsUtil.populateCollectionEventParameters(user);
		collectionEventParameters.setSpecimenCollectionGroup(specimenCollectionGroup);
		specimenEventColl.add(collectionEventParameters);

		ReceivedEventParameters receivedEventParameters = EventsUtil.populateReceivedEventParameters(user);
		receivedEventParameters.setSpecimenCollectionGroup(specimenCollectionGroup);
		specimenEventColl.add(receivedEventParameters);

		specimenCollectionGroup.setSpecimenEventParametersCollection(specimenEventColl);
	}

	/**
	 * @param specimenCollectionGroup
	 * @param oldspecimenCollectionGroup
	 * @param dao
	 * @param sessionDataBean
	 * @throws UserNotAuthorizedException
	 * @throws DAOException
	 */
	private void updateEvents(SpecimenCollectionGroup specimenCollectionGroup, SpecimenCollectionGroup oldspecimenCollectionGroup, DAO dao,
			SessionDataBean sessionDataBean) throws UserNotAuthorizedException, DAOException
	{
		CollectionEventParameters scgCollectionEventParameters = null;
		ReceivedEventParameters scgReceivedEventParameters = null;
		Collection newEventColl = specimenCollectionGroup.getSpecimenEventParametersCollection();
		if (newEventColl != null && !newEventColl.isEmpty())
		{
			Iterator newEventCollIter = newEventColl.iterator();
			while (newEventCollIter.hasNext())
			{
				Object newEventCollObj = newEventCollIter.next();
				if (newEventCollObj instanceof CollectionEventParameters)
				{
					scgCollectionEventParameters = (CollectionEventParameters) newEventCollObj;
					continue;
				}
				else if (newEventCollObj instanceof ReceivedEventParameters)
				{
					scgReceivedEventParameters = (ReceivedEventParameters) newEventCollObj;
				}
			}
		}
		// populateEventsInSpecimens(oldspecimenCollectionGroup,)
		Collection specimenColl = oldspecimenCollectionGroup.getSpecimenCollection();
		if (specimenColl != null && !specimenColl.isEmpty())
		{
			SpecimenEventParametersBizLogic specimenEventParametersBizLogic = (SpecimenEventParametersBizLogic) BizLogicFactory.getInstance()
					.getBizLogic(Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID);
			Iterator iter = specimenColl.iterator();
			while (iter.hasNext())
			{
				Specimen specimen = (Specimen) iter.next();
				Collection eventColl = specimen.getSpecimenEventCollection();
				if (eventColl != null && !eventColl.isEmpty())
				{
					Iterator eventIter = eventColl.iterator();
					while (eventIter.hasNext())
					{
						Object eventObj = eventIter.next();
						if (eventObj instanceof CollectionEventParameters)
						{
							CollectionEventParameters collectionEventParameters = (CollectionEventParameters) eventObj;
							CollectionEventParameters newcollectionEventParameters = populateCollectionEventParameters(eventObj,
									scgCollectionEventParameters, collectionEventParameters);
							specimenEventParametersBizLogic.update(dao, newcollectionEventParameters, collectionEventParameters, sessionDataBean);
							continue;
						}
						else if (eventObj instanceof ReceivedEventParameters)
						{
							ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters) eventObj;
							ReceivedEventParameters newReceivedEventParameters = populateReceivedEventParameters(receivedEventParameters,
									scgReceivedEventParameters);
							specimenEventParametersBizLogic.update(dao, newReceivedEventParameters, receivedEventParameters, sessionDataBean);
						}
					}
				}
			}
		}
	}

	/**
	 * @param eventObj
	 * @param scgCollectionEventParameters
	 */
	private CollectionEventParameters populateCollectionEventParameters(Object eventObj, CollectionEventParameters scgCollectionEventParameters,
			CollectionEventParameters collectionEventParameters)
	{
		// CollectionEventParameters newcollectionEventParameters =
		// collectionEventParameters;
		CollectionEventParameters newcollectionEventParameters = new CollectionEventParameters();
		newcollectionEventParameters.setCollectionProcedure(scgCollectionEventParameters.getCollectionProcedure());
		newcollectionEventParameters.setContainer(scgCollectionEventParameters.getContainer());
		newcollectionEventParameters.setTimestamp(scgCollectionEventParameters.getTimestamp());
		newcollectionEventParameters.setUser(scgCollectionEventParameters.getUser());

		newcollectionEventParameters.setComment(scgCollectionEventParameters.getComment());
		newcollectionEventParameters.setSpecimen(collectionEventParameters.getSpecimen());
		newcollectionEventParameters.setSpecimenCollectionGroup(collectionEventParameters.getSpecimenCollectionGroup());
		newcollectionEventParameters.setId(collectionEventParameters.getId());

		return newcollectionEventParameters;
	}

	/**
	 * @param receivedEventParameters
	 * @param scgReceivedEventParameters
	 * @return
	 */
	private ReceivedEventParameters populateReceivedEventParameters(ReceivedEventParameters receivedEventParameters,
			ReceivedEventParameters scgReceivedEventParameters)
	{
		// ReceivedEventParameters newReceivedEventParameters =
		// receivedEventParameters;
		ReceivedEventParameters newReceivedEventParameters = new ReceivedEventParameters();
		newReceivedEventParameters.setReceivedQuality(scgReceivedEventParameters.getReceivedQuality());
		newReceivedEventParameters.setTimestamp(scgReceivedEventParameters.getTimestamp());
		newReceivedEventParameters.setUser(scgReceivedEventParameters.getUser());

		newReceivedEventParameters.setId(receivedEventParameters.getId());
		newReceivedEventParameters.setComment(scgReceivedEventParameters.getComment());
		newReceivedEventParameters.setSpecimen(receivedEventParameters.getSpecimen());
		newReceivedEventParameters.setSpecimenCollectionGroup(receivedEventParameters.getSpecimenCollectionGroup());
		return newReceivedEventParameters;
	}

	private void setCollectionProtocolRegistration(DAO dao, 
			SpecimenCollectionGroup specimenCollectionGroup,
			SpecimenCollectionGroup oldSpecimenCollectionGroup) throws DAOException
	{
		CollectionProtocolRegistration cpr = 
				specimenCollectionGroup.getCollectionProtocolRegistration();
		if( cpr == null)
		{
			cpr = oldSpecimenCollectionGroup.getCollectionProtocolRegistration();
		}
		if(cpr == null)
		{
			throw new DAOException("CPR cannot be null for SCG");
		}
		Serializable id = null;

		if(cpr.getId()!=null && cpr.getId().longValue()>0)
		{
			id = (Serializable)(cpr.getId());
		}
		else
		{
			id = getCPRIDFromParticipant(dao, specimenCollectionGroup,
				oldSpecimenCollectionGroup);
		}
		cpr =(CollectionProtocolRegistration)
		dao.retrieve(cpr.getClass().getName(), id);
		specimenCollectionGroup.setCollectionProtocolRegistration(cpr);
	}

	/**
	 * @param dao
	 * @param specimenCollectionGroup
	 * @param oldSpecimenCollectionGroup
	 * @throws DAOException
	 */
	private Long getCPRIDFromParticipant(DAO dao,
			SpecimenCollectionGroup specimenCollectionGroup,
			SpecimenCollectionGroup oldSpecimenCollectionGroup)throws DAOException
	{
		String sourceObjectName = CollectionProtocolRegistration.class.getName();
		String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnName = new String[2];
		String[] whereColumnCondition = {"=", "="};
		Object[] whereColumnValue = new Object[2];
		String joinCondition = Constants.AND_JOIN_CONDITION;
		CollectionProtocolRegistration cpr = 
			specimenCollectionGroup.getCollectionProtocolRegistration();

		whereColumnName[0] = "collectionProtocol." + Constants.SYSTEM_IDENTIFIER;
		whereColumnValue[0] = specimenCollectionGroup.getCollectionProtocolRegistration().getCollectionProtocol().getId();
		if (cpr.getParticipant() != null)
		{
			// check for closed Participant
			Participant participantObject = cpr.getParticipant();

			if (oldSpecimenCollectionGroup != null)
			{
				Participant participantObjectOld = oldSpecimenCollectionGroup.getCollectionProtocolRegistration().getParticipant();
				if (!participantObject.getId().equals(participantObjectOld.getId()))
					checkStatus(dao, participantObject, "Participant");
			}
			else
				checkStatus(dao, participantObject, "Participant");

			whereColumnName[1] = "participant." + Constants.SYSTEM_IDENTIFIER;
			whereColumnValue[1] = specimenCollectionGroup.getCollectionProtocolRegistration().getParticipant().getId();
		}
		else
		{
			whereColumnName[1] = "protocolParticipantIdentifier";
			whereColumnValue[1] = specimenCollectionGroup.getCollectionProtocolRegistration().getProtocolParticipantIdentifier();
			Logger.out.debug("Value returned:" + whereColumnValue[1]);
		}

		List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
		if (!list.isEmpty())
		{
			return ((Long) list.get(0));
		}
		return null;
	}

	private void setCollectionProtocolRegistrationOld(DAO dao, SpecimenCollectionGroup specimenCollectionGroup,
			SpecimenCollectionGroup oldSpecimenCollectionGroup) throws DAOException
	{
		String sourceObjectName = CollectionProtocolRegistration.class.getName();
		String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnName = new String[2];
		String[] whereColumnCondition = {"=", "="};
		Object[] whereColumnValue = new Object[2];
		String joinCondition = Constants.AND_JOIN_CONDITION;

		whereColumnName[0] = "collectionProtocol." + Constants.SYSTEM_IDENTIFIER;
		whereColumnValue[0] = specimenCollectionGroup.getCollectionProtocolRegistration().getCollectionProtocol().getId();

		if (specimenCollectionGroup.getCollectionProtocolRegistration().getParticipant() != null)
		{
			// check for closed Participant
			Participant participantObject = (Participant) specimenCollectionGroup.getCollectionProtocolRegistration().getParticipant();

			if (oldSpecimenCollectionGroup != null)
			{
				Participant participantObjectOld = oldSpecimenCollectionGroup.getCollectionProtocolRegistration().getParticipant();
				if (!participantObject.getId().equals(participantObjectOld.getId()))
					checkStatus(dao, participantObject, "Participant");
			}
			else
				checkStatus(dao, participantObject, "Participant");

			whereColumnName[1] = "participant." + Constants.SYSTEM_IDENTIFIER;
			whereColumnValue[1] = specimenCollectionGroup.getCollectionProtocolRegistration().getParticipant().getId();
		}
		else
		{
			whereColumnName[1] = "protocolParticipantIdentifier";
			whereColumnValue[1] = specimenCollectionGroup.getCollectionProtocolRegistration().getProtocolParticipantIdentifier();
			Logger.out.debug("Value returned:" + whereColumnValue[1]);
		}

		List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
		if (!list.isEmpty())
		{
			// check for closed CollectionProtocolRegistration
			CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

			/**
			 * Start: Change for API Search --- Jitendra 06/10/2006 In Case of
			 * Api Search, previoulsy it was failing since there was default
			 * class level initialization on domain object. For example in User
			 * object, it was initialized as protected String lastName=""; So we
			 * removed default class level initialization on domain object and
			 * are initializing in method setAllValues() of domain object. But
			 * in case of Api Search, default values will not get set since
			 * setAllValues() method of domainObject will not get called. To
			 * avoid null pointer exception, we are setting the default values
			 * same as we were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setCollectionProtocolRegistrationDefault(collectionProtocolRegistration);
			// End:- Change for API Search

			collectionProtocolRegistration.setId((Long) list.get(0));
			if (oldSpecimenCollectionGroup != null)
			{
				CollectionProtocolRegistration collectionProtocolRegistrationOld = oldSpecimenCollectionGroup.getCollectionProtocolRegistration();

				if (!collectionProtocolRegistration.getId().equals(collectionProtocolRegistrationOld.getId()))
					checkStatus(dao, collectionProtocolRegistration, "Collection Protocol Registration");
			}
			else
				checkStatus(dao, collectionProtocolRegistration, "Collection Protocol Registration");

			specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);
		}
	}

	// public void disableRelatedObjects(DAO dao, Long collProtRegIDArr[])
	// throws
	// DAOException
	// {
	// List listOfSubElement = super.disableObjects(dao,
	// SpecimenCollectionGroup.class, "collectionProtocolRegistration",
	// "CATISSUE_SPECIMEN_COLL_GROUP", "COLLECTION_PROTOCOL_REG_ID",
	// collProtRegIDArr);
	// if (!listOfSubElement.isEmpty())
	// {
	// NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic)
	// BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
	// bizLogic.disableRelatedObjectsForSpecimenCollectionGroup(dao,
	// Utility.toLongArray(listOfSubElement));
	// }
	// }

	public void disableRelatedObjects(DAO dao, Long collProtRegIDArr[]) throws DAOException
	{
		List listOfSubElement = getRelatedObjects(dao, SpecimenCollectionGroup.class, "collectionProtocolRegistration", collProtRegIDArr);
		dao.disableRelatedObjects("CATISSUE_ABS_SPECI_COLL_GROUP", Constants.SYSTEM_IDENTIFIER_COLUMN_NAME, Utility.toLongArray(listOfSubElement));
		auditDisabledObjects(dao, "CATISSUE_SPECIMEN_COLL_GROUP", listOfSubElement);
		if (!listOfSubElement.isEmpty())
		{
			NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			bizLogic.disableRelatedObjectsForSpecimenCollectionGroup(dao, Utility.toLongArray(listOfSubElement));
		}
	}

	/**
	 * @param dao
	 * @param privilegeName
	 * @param objectIds
	 * @param assignToUser
	 * @param roleId
	 * @param longs
	 * @throws DAOException
	 * @throws SMException
	 */
	public void assignPrivilegeToRelatedObjects(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId, boolean assignToUser,
			boolean assignOperation) throws SMException, DAOException
	{
		List listOfSubElement = super.getRelatedObjects(dao, SpecimenCollectionGroup.class, "collectionProtocolRegistration", objectIds);
		if (!listOfSubElement.isEmpty())
		{
			super.setPrivilege(dao, privilegeName, SpecimenCollectionGroup.class, Utility.toLongArray(listOfSubElement), userId, roleId,
					assignToUser, assignOperation);
			NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			bizLogic.assignPrivilegeToRelatedObjectsForSCG(dao, privilegeName, Utility.toLongArray(listOfSubElement), userId, roleId, assignToUser,
					assignOperation);
		}

	}

	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#setPrivilege(DAO, String, Class,
	 *      Long[], Long, String, boolean)
	 */
	protected void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser,
			boolean assignOperation) throws SMException, DAOException
	{
		super.setPrivilege(dao, privilegeName, objectType, objectIds, userId, roleId, assignToUser, assignOperation);

		NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
		bizLogic.assignPrivilegeToRelatedObjectsForSCG(dao, privilegeName, objectIds, userId, roleId, assignToUser, assignOperation);
	}

	/**
	 * check for the specimen associated with the SCG
	 * 
	 * @param obj
	 * @param dao
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	protected boolean isSpecimenExists(Object obj, DAO dao, Long scgId) throws DAOException, ClassNotFoundException
	{

		String hql = " select" + " elements(scg.specimenCollection) " + " from " + " edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg , "
				+ " edu.wustl.catissuecore.domain.Specimen as s" + " where scg.id = " + scgId + " and"
				+ " scg.id = s.specimenCollectionGroup.id and " + " s.activityStatus = '" + Constants.ACTIVITY_STATUS_ACTIVE + "'";

		List specimenList = (List) executeHqlQuery(dao, hql);
		if ((specimenList != null) && (specimenList).size() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute
	 * values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		SpecimenCollectionGroup group = (SpecimenCollectionGroup) obj;

		// Added by Ashish

		if (group == null)
		{
			throw new DAOException(ApplicationProperties.getValue("domain.object.null.err.msg", "SpecimenCollectionGroup"));
		}

		Validator validator = new Validator();
		String message = "";

		if (group.getCollectionProtocolRegistration() == null)
		{
			message = ApplicationProperties.getValue("errors.specimenCollectionGroup.collectionprotocolregistration");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		if (group.getCollectionProtocolRegistration().getCollectionProtocol() == null
				|| group.getCollectionProtocolRegistration().getCollectionProtocol().getId() == null)
		{
			message = ApplicationProperties.getValue("errors.specimenCollectionGroup.collectionprotocol");
			throw new DAOException(ApplicationProperties.getValue("errors.invalid", message));
		}

		if ((group.getCollectionProtocolRegistration().getProtocolParticipantIdentifier() == null && (group.getCollectionProtocolRegistration()
				.getParticipant() == null || group.getCollectionProtocolRegistration().getParticipant().getId() == null)))
		{
			throw new DAOException(ApplicationProperties.getValue("errors.collectionprotocolregistration.atleast"));
		}

		if (group.getSpecimenCollectionSite() == null || group.getSpecimenCollectionSite().getId() == null
				|| group.getSpecimenCollectionSite().getId() == 0)
		{
			message = ApplicationProperties.getValue("specimenCollectionGroup.site");
			throw new DAOException(ApplicationProperties.getValue("errors.item.invalid", message));
		}

		// Check what user has selected Participant Name / Participant Number

		// if participant name field is checked.
		// if(group.getCollectionProtocolRegistration().getParticipant().getId()
		// == -1)
		// {
		// message =
		// ApplicationProperties.getValue("specimenCollectionGroup.protocoltitle");
		// throw new
		// DAOException(ApplicationProperties.getValue("errors.item.required",message));
		//				
		// String message =
		// ApplicationProperties.getValue("specimenCollectionGroup.collectedByParticipant");
		// throw new DAOException("errors.item.selected", new
		// String[]{message});
		//				
		// }

		// if(!validator.isValidOption(group.getCollectionProtocolRegistration().getProtocolParticipantIdentifier()))
		// {
		// String message =
		// ApplicationProperties.getValue("specimenCollectionGroup.collectedByProtocolParticipantNumber");
		// throw new DAOException("errors.item.selected", new
		// String[]{message});
		//				
		// }

		if (validator.isEmpty(group.getName()))
		{
			message = ApplicationProperties.getValue("specimenCollectionGroup.groupName");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		// Mandatory Field : Study Calendar event point
		if (group.getCollectionProtocolEvent() == null || group.getCollectionProtocolEvent().getId() == null)
		{
			message = ApplicationProperties.getValue("specimenCollectionGroup.studyCalendarEventPoint");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		// Mandatory Field : clinical Diagnosis
		if (validator.isEmpty(group.getClinicalDiagnosis()))
		{
			message = ApplicationProperties.getValue("specimenCollectionGroup.clinicalDiagnosis");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		// Mandatory Field : clinical Status
		if (validator.isEmpty(group.getClinicalStatus()))
		{
			message = ApplicationProperties.getValue("specimenCollectionGroup.clinicalStatus");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		// Condition for medical Record Number.

		// END

		List clinicalDiagnosisList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CLINICAL_DIAGNOSIS, null);
		if (!Validator.isEnumeratedValue(clinicalDiagnosisList, group.getClinicalDiagnosis()))
		{
			throw new DAOException(ApplicationProperties.getValue("spg.clinicalDiagnosis.errMsg"));
		}

		// NameValueBean undefinedVal = new
		// NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
		List clinicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CLINICAL_STATUS, null);
		if (!Validator.isEnumeratedValue(clinicalStatusList, group.getClinicalStatus()))
		{
			throw new DAOException(ApplicationProperties.getValue("collectionProtocol.clinicalStatus.errMsg"));
		}

		if (operation.equals(Constants.ADD))
		{
			if (!Constants.ACTIVITY_STATUS_ACTIVE.equals(group.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.active.errMsg"));
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, group.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
			}
		}

		// check the activity status of all the specimens associated to the
		// Specimen Collection Group
		if (group.getActivityStatus().equalsIgnoreCase(Constants.DISABLED))
		{
			try
			{

				boolean isSpecimenExist = (boolean) isSpecimenExists(obj, dao, (Long) group.getId());
				if (isSpecimenExist)
				{
					throw new DAOException(ApplicationProperties.getValue("specimencollectiongroup.specimen.exists"));
				}

			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		/*
		 * Bug ID: 4165 Patch ID: 4165_11 Description: Validation added to check
		 * incorrect events added through API
		 */
		// Events Validation
		if (group.getSpecimenEventParametersCollection() != null && !group.getSpecimenEventParametersCollection().isEmpty())
		{
			Iterator specimenEventCollectionIterator = group.getSpecimenEventParametersCollection().iterator();
			while (specimenEventCollectionIterator.hasNext())
			{
				Object eventObject = specimenEventCollectionIterator.next();
				EventsUtil.validateEventsObject(eventObject, validator);
			}
		}
		else
		{
			throw new DAOException(ApplicationProperties.getValue("error.specimenCollectionGroup.noevents"));
		}

		return true;
	}

	public String getPageToShow()
	{
		return new String();
	}

	public List getMatchingObjects()
	{
		return new ArrayList();
	}

	public int getNextGroupNumber() throws DAOException
	{
		String sourceObjectName = "CATISSUE_SPECIMEN_COLL_GROUP";
		String[] selectColumnName = {"max(IDENTIFIER) as MAX_IDENTIFIER"};
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);

		dao.openSession(null);

		List list = dao.retrieve(sourceObjectName, selectColumnName);

		dao.closeSession();

		if (!list.isEmpty())
		{
			List columnList = (List) list.get(0);
			if (!columnList.isEmpty())
			{
				String str = (String) columnList.get(0);
				if (!str.equals(""))
				{
					int no = Integer.parseInt(str);
					return no + 1;
				}
			}
		}

		return 1;
	}

	/**
	 * This function gets the specimen coll group and specimens under that SCG.
	 * 
	 * @param cpId
	 * @param participantId
	 * @return
	 * @throws Exception
	 */
	/*
	 * public Vector getSCGandSpecimens(Long cpId, Long participantId) throws
	 * Exception { String hql = null; if (participantId.longValue() == -1) { hql =
	 * "select scg.id,scg.name,sp.id,sp.label,sp.parentSpecimen.id
	 * ,scg.activityStatus,sp.activityStatus from " + Specimen.class.getName() + "
	 * as sp right outer join sp.specimenCollectionGroup as scg where
	 * scg.collectionProtocolRegistration.collectionProtocol.id= " +
	 * cpId.toString() + " and scg.id = sp.specimenCollectionGroup.id order by
	 * scg.id,sp.id"; } else { hql = "select
	 * scg.id,scg.name,sp.id,sp.label,sp.parentSpecimen.id,scg.activityStatus,sp.activityStatus
	 * from " + Specimen.class.getName() + " as sp right outer join
	 * sp.specimenCollectionGroup as scg where
	 * scg.collectionProtocolRegistration.collectionProtocol.id= " +
	 * cpId.toString() + " and
	 * scg.collectionProtocolRegistration.participant.id= " +
	 * participantId.toString() + " and scg.id = sp.specimenCollectionGroup.id
	 * order by scg.id,sp.id"; } HibernateDAO dao = (HibernateDAO)
	 * DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
	 * dao.openSession(null);
	 * 
	 * List list = dao.executeQuery(hql, null, false, null);
	 * Logger.out.info("list size -----------:" + list.size());
	 * dao.closeSession(); //Map map1 = new TreeMap(); Vector treeData = new
	 * Vector(); if (list != null) { for (int i = 0; i < list.size(); i++) {
	 * //Getitng participants for a particular CP. Object[] obj = (Object[])
	 * list.get(i); Long scgId = (Long) obj[0]; String scgName = (String)
	 * obj[1]; String scgActivityStatus = (String) obj[5];
	 * 
	 * setQueryTreeNode(scgId.toString(), Constants.SPECIMEN_COLLECTION_GROUP,
	 * scgName, "0", null, null, null, scgActivityStatus, treeData);
	 * 
	 * for (int j = i; j < list.size(); j++, i++) { Object[] obj1 = (Object[])
	 * list.get(j); Long scgId1 = (Long) obj1[0];
	 * 
	 * if (scgId.longValue() == scgId1.longValue()) { Long spId1 = (Long)
	 * obj1[2]; String spLabel1 = (String) obj1[3]; Long parentSpecimenId =
	 * (Long) obj1[4]; String spActivityStatus = (String) obj1[6];
	 * 
	 * if (spId1 != null) { if (parentSpecimenId != null) {
	 * setQueryTreeNode(spId1.toString(), Constants.SPECIMEN, spLabel1,
	 * parentSpecimenId.toString(), Constants.SPECIMEN, null, null,
	 * spActivityStatus, treeData); } else { setQueryTreeNode(spId1.toString(),
	 * Constants.SPECIMEN, spLabel1, scgId1.toString(),
	 * Constants.SPECIMEN_COLLECTION_GROUP, null, null, spActivityStatus,
	 * treeData); } } } else { i--; break; } } } }
	 * 
	 * return treeData; }
	 */
	/**
	 * Patch Id : FutureSCG_8 Description : method to get SCGTree ForCPBasedView
	 */
	/**
	 * Creates tree which consists of SCG nodes and specimen nodes under each
	 * SCG if available. For a CPR if there is any SCG created those are shown
	 * with its details like '<# event day>_<Event point label>_<SCG_recv_date>'.
	 * When user clicks on this node ,Edit SCG page will be shown on right side
	 * panel of the screen, where now user can edit this SCG. But if there are
	 * no SCGs present for a CPR , a future(dummy) SCG is shown in tree as '<#
	 * event day>_<Event point label>'. When user clicks on this node , Add SCG
	 * page will be shown on right side panel of the screen, where now user can
	 * actually add new SCG and specimens for this CPR.
	 * 
	 * @param cpId
	 *            id of collection protocol
	 * @param participantId
	 *            id of participant
	 * @return Vector tree data structure
	 * @throws DAOException
	 *             daoException
	 * @throws ClassNotFoundException
	 *             classNotFoundException
	 */
	Integer offsetForCPOrEvent = null;

	Integer offsetForArmCP = null;

	public String getSCGTreeForCPBasedView(Long cpId, Long participantId) throws DAOException, ClassNotFoundException
	{
		offsetForCPOrEvent = null;
		Logger.out.debug("Start of getSCGTreeForCPBasedView");

		Date regDate = null;
		Integer offset = null;
		String hql1 = "select cpr.registrationDate,cpr.offset from " + CollectionProtocolRegistration.class.getName()
				+ " as cpr where cpr.collectionProtocol.id = " + cpId.toString() + " and cpr.participant.id = " + participantId.toString();
		List list = executeQuery(hql1);
		if (list != null && list.size() > 0)
		{
			Object[] obj = (Object[]) list.get(0);
			regDate = (Date) obj[0];
			offset = (Integer) obj[1];

		}
		offsetForCPOrEvent = offset;
		// creating XML String rep of SCGs,specimens & child specimens ::Addded
		// by baljeet
		StringBuffer xmlString = new StringBuffer();

		xmlString.append("<node>");

		childCPtree(xmlString, cpId, participantId, regDate,offsetForCPOrEvent);
		xmlString.append("</node>");
		return xmlString.toString();
	}

	/**
	 * Creates a tree which consists of ChildCollectionProtocol under Parent
	 * Collection Protocol if ChildCollectionProtocol is available The function
	 * is called recursively and also SCG for Parent Collection Protocol and
	 * Child COllection Protocol are created in tree structure
	 * 
	 * @param xmlString
	 *            String buffer to generate XML code
	 * @param cpId
	 *            id of collection protocol
	 * @param participantId
	 *            id of participant
	 * @throws DAOException
	 *             daoException
	 * @throws ClassNotFoundException
	 *             classNotFoundException
	 */

	public void childCPtree(StringBuffer xmlString, Long cpId, Long participantId, Date regDate, Integer parentOffset) throws DAOException, ClassNotFoundException
	{

		Date evtLastDate = SCGTreeForCPBasedView(xmlString, cpId, participantId, regDate,parentOffset);

		String hql = "select  cp." + Constants.CHILD_COLLECTION_PROTOCOL_COLLECTION + " from " + CollectionProtocol.class.getName()
				+ " as cp where cp.id= " + cpId.toString();
		List cpchildList = executeQuery(hql);
		CollectionProtocolSeqComprator comparator = new CollectionProtocolSeqComprator();
		Collections.sort(cpchildList, comparator);
		if (cpchildList.size() != 0)
		{
			for (int count = 0; count < cpchildList.size(); count++)
			{
				CollectionProtocol cp = (CollectionProtocol) cpchildList.get(count);
				
				String dispName = cp.getShortTitle();
				Date participantRegDate = getRegDateForCP(cp, participantId, regDate, evtLastDate,parentOffset);
				String anticipatoryDate = "";
				if (participantRegDate != null)
				{
					//bug no:6526 date format changed to mm-dd-yyyy
					anticipatoryDate = Utility.parseDateToString(participantRegDate, "MM-dd-yyyy");
					dispName = cp.getShortTitle() + ":" + anticipatoryDate;

				}
				CollectionProtocolRegistration cpr = chkParticipantRegisteredToCP(participantId, cp.getId(), cp.getType());
				String participantRegStatus = "Pending";
				if (cpr != null)
					participantRegStatus = "Registered";

				xmlString.append("<node id= \"" + Constants.SUB_COLLECTION_PROTOCOL + "_" + cp.getId() + "\" " + "name=\"" + dispName + "\" "
						+ "toolTip=\"" + cp.getTitle() + "\" " + "type=\"" + cp.getType() + "\" " + "cpType=\"" + cp.getType() + "\" " + "regDate=\""
						+ anticipatoryDate + "\" " + "participantRegStatus=\"" + participantRegStatus + "\">");
				if (cpr != null)
				{
					if(cpr.getOffset() != null)
						addOffset(cpr.getOffset());
				}
				childCPtree(xmlString, cp.getId(), participantId, participantRegDate,offsetForCPOrEvent);
				xmlString.append("</node>");
			}

		}

	}

	private void addOffset(Integer offsetToAdd)
	{
		Integer offset = null;
		if(offsetForCPOrEvent != null)
		{
			offset = new Integer(offsetForCPOrEvent.intValue()+offsetToAdd.intValue());
			offsetForCPOrEvent = offset;
		}
		else
		{
			offset = new Integer(offsetToAdd.intValue());
			offsetForCPOrEvent = offset;
		}
	}
	private Date getRegDateForCP(CollectionProtocol cp, Long participantId, Date parentRegDate, Date eventLastDate,Integer parentOffset) throws DAOException,
			ClassNotFoundException
	{
		Date participantRegDate = null;
		//		bug 6560 fix

		Long cpId = cp.getId();
		Double eventPoint = cp.getStudyCalendarEventPoint();
		CollectionProtocolRegistration cpr = chkParticipantRegisteredToCP(participantId, cpId, cp.getType());

		if (cpr == null)
		{
			if (cp.getSequenceNumber() != null)
			{
				int noOfDaysToAdd = 0;
				if (eventPoint != null)
				{
					if (parentRegDate != null)
					{
						noOfDaysToAdd = eventPoint.intValue();
						if (offsetForCPOrEvent != null)
							noOfDaysToAdd = noOfDaysToAdd + offsetForCPOrEvent.intValue();
						//This check is beaause child cp take there anticipated dates according to there parent cp and if in parent date already offset is captured then no need ot take that offeet in child
						if(parentOffset != null)
							noOfDaysToAdd = noOfDaysToAdd - parentOffset.intValue();
						// offset logic
						/*
						 * if there is offset given to the previous seq number protocols
						 * then this shud apply here
						 */
						/*Integer offset = getOffsetFromPreviousSeqNoCP(cp, participantId);
						 if (offset != null)
						 noOfDaysToAdd = noOfDaysToAdd + offset.intValue();*/

						/*if (Constants.ARM_CP_TYPE.equals(cp.getType()))
						 noOfDaysToAdd = noOfDaysToAdd + getNoOfDaysAccordingToOffset(offset, offsetForArmCP);
						 else
						 noOfDaysToAdd = noOfDaysToAdd + getNoOfDaysAccordingToOffset(offset, offsetForCPOrEvent);*/

						/*if (offsetForCPOrEvent != null && offset != null && offsetForCPOrEvent.intValue() > offset.intValue())
						 noOfDaysToAdd = noOfDaysToAdd + offsetForCPOrEvent.intValue() - offset.intValue();

						 if (offsetForCPOrEvent != null && offset == null)
						 noOfDaysToAdd = noOfDaysToAdd + offsetForCPOrEvent.intValue();
						 */
						participantRegDate = Utility.getNewDateByAdditionOfDays(parentRegDate, noOfDaysToAdd);

					}
					else if (eventLastDate != null)
						participantRegDate = Utility.getNewDateByAdditionOfDays(eventLastDate, Constants.DAYS_TO_ADD_CP);
					else if (parentRegDate != null)
						participantRegDate = Utility.getNewDateByAdditionOfDays(parentRegDate, Constants.DAYS_TO_ADD_CP);
				}
			}
		}
		else
		{
			participantRegDate = cpr.getRegistrationDate();
		}

		return participantRegDate;
	}

	private int getNoOfDaysAccordingToOffset(Integer offset, Integer offsetForCPOrEvent)
	{
		int noOfDays = 0;
		if (offsetForCPOrEvent != null && offset != null && offsetForCPOrEvent.intValue() > offset.intValue())
			noOfDays = offsetForCPOrEvent.intValue() - offset.intValue();

		if (offsetForCPOrEvent != null && offset == null)
			noOfDays = offsetForCPOrEvent.intValue();

		return noOfDays;
	}

	private Integer getOffsetFromPreviousSeqNoCP(CollectionProtocol cp, Long participantId) throws DAOException, ClassNotFoundException
	{
		if (cp != null && participantId != null)
		{
			Integer seqNo = cp.getSequenceNumber();
			if (seqNo != null)
			{
				Long parentCpId = cp.getParentCollectionProtocol().getId();
				if (parentCpId != null)
				{
					// get the previous cp's offset if present.
					String hql = "select  cpr.offset from " + CollectionProtocolRegistration.class.getName()
							+ " as cpr where cpr.collectionProtocol.parentCollectionProtocol.id = " + parentCpId.toString()
							+ " and cpr.participant.id = " + participantId.toString() + " order by cpr.collectionProtocol.sequenceNumber desc";
					List offsetList = executeQuery(hql);
					if (offsetList != null)
					{
						for (int i = 0; i < offsetList.size(); i++)
						{
							Integer offset = (Integer) offsetList.get(i);
							if (offset != null)
								return offset;
						}
					}
				}

			}
		}
		return null;
	}

	public CollectionProtocolRegistration chkParticipantRegisteredToCP(Long participantId, Long collectionProtocolId, String type)
			throws ClassNotFoundException, DAOException
	{
		//Date regDate = null;
		CollectionProtocolRegistration cpr = null;
		String cprHqlQuery = "select cpr.id,cpr.registrationDate,cpr.offset from " + CollectionProtocolRegistration.class.getName()
				+ " as cpr where cpr.participant.id = " + participantId.toString() + " and cpr.collectionProtocol.id = "
				+ collectionProtocolId.toString();
		List cprList = executeQuery(cprHqlQuery);
		if (cprList != null && !cprList.isEmpty())
		{
			cpr = new CollectionProtocolRegistration();
			Object[] obj = (Object[]) cprList.get(0);
			Date regDate = (Date) obj[1];
			cpr.setRegistrationDate(regDate);
			if (obj[2] != null)
			{
				Integer offset = (Integer) obj[2];
				//offsetForCPOrEvent = offset;
				/*if (!Constants.ARM_CP_TYPE.equals(type))
				 offsetForArmCP = offsetForCPOrEvent;*/
				cpr.setOffset(offset);
			}

			return cpr;
		}
		return null;
	}

	/**
	 * Creates tree which consists of SCG nodes and specimen nodes under each
	 * SCG if available.This method generates tree hierarchy of specimen for
	 * each Collection Protocol for Parent as well as Child Collection Protocol
	 * 
	 * @param xmlString
	 *            String buffer to generate XML code
	 * @param cpId
	 *            id of collection protocol
	 * @param participantId
	 *            id of participant
	 * @throws DAOException
	 *             daoException
	 * @throws ClassNotFoundException
	 *             classNotFoundException
	 */
	public Date SCGTreeForCPBasedView(StringBuffer xmlString, Long cpId, Long participantId, Date regDate,Integer parentOffset) throws DAOException,
			ClassNotFoundException
	{

		Date eventLastDate = null;
		String hql = "select  cpe.id, cpe.studyCalendarEventPoint,cpe.collectionPointLabel from " + CollectionProtocolEvent.class.getName()
				+ " as cpe where cpe.collectionProtocol.id= " + cpId.toString() + " order by cpe.studyCalendarEventPoint";
		List cpeList = executeQuery(hql);
		Logger.out.debug("After executeQuery");
		for (int count = 0; count < cpeList.size(); count++)
		{
			Object[] obj = (Object[]) cpeList.get(count);
			Long eventId = (Long) obj[0];
			Double eventPoint = (Double) obj[1];
			String collectionPointLabel = (String) obj[2];
			List scgList = getSCGsForCPRAndEventId(eventId, cpId, participantId);
			if (scgList != null && !scgList.isEmpty())
			{
				eventLastDate = createTreeNodeForExistingSCG(xmlString, eventPoint, collectionPointLabel, scgList, regDate,parentOffset);
			}

		}
		return eventLastDate;
	}

	/**
	 * Name : Deepti Shelar Bug id : 4268 Patch id : 4268_1
	 */
	/*
	 *//**
	 * Gets all scgs under given cp for all participants.
	 * 
	 * @param eventId
	 *            studycalendareventpoint
	 * @param cpId
	 *            collection protocol id
	 * @return List of scgs
	 * @throws DAOException
	 *             DAOException
	 * @throws ClassNotFoundException
	 *             ClassNotFoundException
	 */
	/*
	 * private List getSCGsForTechnicians(Long eventId, Long cpId) throws
	 * DAOException, ClassNotFoundException { String hql = "select
	 * scg.id,scg.name,scg.activityStatus from " +
	 * SpecimenCollectionGroup.class.getName() + " as scg where
	 * scg.collectionProtocolRegistration.id = (select cpr.id from " +
	 * CollectionProtocolRegistration.class.getName() +" as cpr where
	 * cpr.collectionProtocol.id = " + cpId + ") and
	 * scg.collectionProtocolEvent.id = "+eventId ;
	 * 
	 * String hql = "select scg.id,scg.name,scg.activityStatus from " +
	 * SpecimenCollectionGroup.class.getName() + " as scg where
	 * scg.collectionProtocolEvent.id = "+eventId ; List list =
	 * executeQuery(hql); return list; }
	 */

	/*
	 * private StringBuffer createSCGAndSP(Long cpId, Long participantId,
	 * StringBuffer xmlString) throws DAOException, ClassNotFoundException {
	 * String hql = "select cpe.id,
	 * cpe.studyCalendarEventPoint,cpe.collectionPointLabel from " +
	 * CollectionProtocolEvent.class.getName() + " as cpe where
	 * cpe.collectionProtocol.id= "+ cpId.toString() +" order by
	 * cpe.studyCalendarEventPoint"; List cpeList = executeQuery(hql);
	 * Logger.out.debug("After executeQuery"); for(int count = 0; count <
	 * cpeList.size() ; count++) { Object[] obj = (Object[]) cpeList.get(count);
	 * Long eventId = (Long)obj[0]; Double eventPoint = (Double) obj[1]; String
	 * collectionPointLabel = (String)obj[2]; List scgList =
	 * getSCGsForCPRAndEventId(eventId, cpId,participantId); if (scgList != null &&
	 * !scgList.isEmpty()) { createTreeNodeForExistingSCG(xmlString, eventPoint,
	 * collectionPointLabel, scgList); } } return xmlString; }
	 */

	/**
	 * Patch Id : FutureSCG_9 Description : method to create TreeNode For
	 * FutureSCG
	 */
	/**
	 * Creates tree node for a SCG which does not exist in database, but user
	 * can create it by clicking on this node.
	 * 
	 * @param treeData
	 *            data structure for tree data
	 * @param eventId
	 *            id of studyCalendarEvent
	 * @param eventPoint
	 *            event point in no of days
	 * @param collectionPointLabel
	 *            String label of collection event point
	 */
	/*
	 * private void createTreeNodeForFutureSCG(StringBuffer xmlString, Long
	 * eventId, Double eventPoint, String collectionPointLabel) { Long
	 * futureSCGId = new Long(0); String futureSCGName = "T"+eventPoint + ": "
	 * +collectionPointLabel; String toolTipText =
	 * futureSCGName;//getToolTipText(eventPoint.toString(),collectionPointLabel,null);
	 * String nodeId =
	 * futureSCGId.toString()+":"+eventId.toString()+":"+Constants.FUTURE_SCG;
	 * 
	 * xmlString.append("<node
	 * id=\""+Constants.SPECIMEN_COLLECTION_GROUP+"_"+nodeId+"\"
	 * "+"name=\""+futureSCGName+"\" "+"toolTip=\""+toolTipText+"\"
	 * "+"type=\""+Constants.SPECIMEN_COLLECTION_GROUP+"\"></node>"); }
	 */
	/**
	 * Patch Id : FutureSCG_10 Description : method to create TreeNode For
	 * ExistingSCG
	 */
	/**
	 * Creates a tree node for existing SCgs
	 * 
	 * @param xmlString
	 *            data structure for storing tree data
	 * @param eventPoint
	 *            event point in no of days
	 * @param collectionPointLabel
	 *            String label of collection event point
	 * @param scgList
	 *            list of scgs
	 * @throws DAOException
	 *             DAOException
	 * @throws ClassNotFoundException
	 *             ClassNotFoundException
	 */
	private Date createTreeNodeForExistingSCG(StringBuffer xmlString, Double eventPoint, String collectionPointLabel, List scgList, Date regDate, Integer parentOffset)
			throws DAOException, ClassNotFoundException
	{
		Date eventLastDate = null;
		for (int i = 0; i < scgList.size(); i++)
		{
			Object[] obj1 = (Object[]) scgList.get(i);
			Long scgId = (Long) obj1[0];
			String scgCollectionStatus = (String) obj1[3];
			Integer offset = (Integer) obj1[4];
			String scgNodeLabel = "";
			// String scgActivityStatus = (String) obj1[2];

			/**
			 * Name: Vijay Pande Reviewer Name: Aarti Sharma recievedEvent
			 * related to scg is trieved from db and, proper receivedDate and
			 * scgNodeLabel are set to set toolTip of the node
			 */
			if(offset !=null)
			{
				addOffset(offset);
			}
			String receivedDate = "";
			if (scgId != null && scgId > 0)
			{
				String sourceObjName = CollectionEventParameters.class.getName();
				String columnName = Constants.COLUMN_NAME_SCG_ID;
				Long ColumnValue = scgId;
				Collection eventsColl = retrieve(sourceObjName, columnName, ColumnValue);

				// Collection eventsColl =
				// specimenCollectionGroup.getSpecimenEventParametersCollection();
				if (eventsColl != null && !eventsColl.isEmpty())
				{
					receivedDate = "";
					Iterator iter = eventsColl.iterator();
					while (iter.hasNext())
					{
						CollectionEventParameters collectionEventParameters = (CollectionEventParameters) iter.next();
						eventLastDate = collectionEventParameters.getTimestamp();
						//bug no:6526 date format changed to mm-dd-yyyy
						receivedDate = Utility.parseDateToString(collectionEventParameters.getTimestamp(), "MM-dd-yyyy");
						scgNodeLabel = "T" + eventPoint + ": " + collectionPointLabel + ": " + receivedDate;
						break;
					}
				}
				if (scgNodeLabel.equalsIgnoreCase("") && receivedDate.equalsIgnoreCase(""))
				{
					int noOfDaysToAdd = 0;
					if (eventPoint != null)
						noOfDaysToAdd += eventPoint.intValue();
					if (offsetForCPOrEvent != null)
					{
						noOfDaysToAdd += offsetForCPOrEvent.intValue();
					}
					// This check is beaause event take there anticipated dates according to there parent and if in parent date already offset is captured then no need ot take that offeet in child
					if(parentOffset != null)
					{
						noOfDaysToAdd -= parentOffset.intValue();
					}
										
					Date evtDate = Utility.getNewDateByAdditionOfDays(regDate, noOfDaysToAdd);
					eventLastDate = evtDate;
					//bug no:6526 date format changed to mm-dd-yyyy
					receivedDate = Utility.parseDateToString(evtDate, "MM-dd-yyyy");
					// String toolTipText = scgNodeLabel+ ": "+scgName;//
					scgNodeLabel = "T" + eventPoint + ": " + collectionPointLabel + ": " + receivedDate;
				}
			}
			String toolTipText = getToolTipText(eventPoint.toString(), collectionPointLabel, receivedDate);

			// creating SCG node
			xmlString.append("<node id= \"" + Constants.SPECIMEN_COLLECTION_GROUP + "_" + scgId.toString() + "\" " + "name=\"" + scgNodeLabel + "\" "
					+ "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN_COLLECTION_GROUP + "\" " + "scgCollectionStatus=\""
					+ scgCollectionStatus + "\" " + "evtDate=\"" + receivedDate + "\">");

			// Adding specimen Nodes to SCG tree
			addSpecimenNodesToSCGTree(xmlString, scgId);
			xmlString.append("</node>");

		}
		return eventLastDate;
	}

	/**
	 * Patch Id : FutureSCG_11 Description : method to get SCGs For CPR And
	 * EventId
	 */
	/**
	 * Returns the list of SCGs present for given CPR and eventId.
	 * 
	 * @param eventId
	 *            id of collection protocol event
	 * @param cpId
	 *            id of collection protocol
	 * @param participantId
	 *            id of participant
	 * @return list of SCGs.
	 * @throws DAOException
	 *             daoException
	 * @throws ClassNotFoundException
	 *             classNotFoundException
	 */
	private List getSCGsForCPRAndEventId(Long eventId, Long cpId, Long participantId) throws DAOException, ClassNotFoundException
	{
		String hql = "select scg.id,scg.name,scg.activityStatus,scg.collectionStatus,scg.offset from " + SpecimenCollectionGroup.class.getName()
				+ " as scg where scg.collectionProtocolRegistration.id = (select cpr.id from " + CollectionProtocolRegistration.class.getName()
				+ " as cpr where cpr.collectionProtocol.id = " + cpId + " and cpr.participant.id = " + participantId
				+ ") and scg.collectionProtocolEvent.id = " + eventId + " and scg.activityStatus <> '" + Constants.ACTIVITY_STATUS_DISABLED + "'";
		List list = executeQuery(hql);
		return list;
	}

	/**
	 * Patch Id : FutureSCG_12 Description : method to add SpecimenNodes To
	 * SCGTree
	 */
	/**
	 * Adds specimen nodes to SCG node
	 * 
	 * @param xmlString
	 *            vector to store XML rep of tree data
	 * @param scgId
	 *            id of specimen collection group
	 */
	private void addSpecimenNodesToSCGTree(StringBuffer xmlString, Long scgId) throws DAOException, ClassNotFoundException
	{
		/**
		 * kalpana Bug #5906 Reviewer : vaishali To display the children
		 * specimens of the specimen in acending order. changed to order by
		 * specimen label.
		 */
		String hql = "select sp.id,sp.label,sp.parentSpecimen.id,sp.activityStatus,sp.type,sp.collectionStatus	from " + Specimen.class.getName()
				+ " as sp where sp.specimenCollectionGroup.id = " + scgId + " and sp.activityStatus <> '" + Constants.ACTIVITY_STATUS_DISABLED
				+ "' order by sp.label";
		List specimenList = executeQuery(hql);

		// here we have two Lists to separate out Specimens and child Specimens
		List<Object[]> finalList = new ArrayList<Object[]>();
		List<Object[]> childrenList = new ArrayList<Object[]>();

		// Stack
		Stack<Object[]> spStack = new Stack<Object[]>();

		// Here iterate over specimenList to separate out Specimens and child
		// Specimens
		for (int i = 0; i < specimenList.size(); i++)
		{
			Object[] obj = (Object[]) specimenList.get(i);
			Long spId = (Long) obj[0];
			Long parentSpecimenId = (Long) obj[2];

			// Long peekSpecimenId = null;
			if (spId != null)
			{
				if (parentSpecimenId == null)
				{
					// if parentSpecimenId is null then it's going to be parent
					// specimen
					finalList.add((Object[]) specimenList.get(i));
				}
				else
				{
					childrenList.add((Object[]) specimenList.get(i));
				}
			}
		}

		/**
		 * kalpana Bug #5906 Reviewer : vaishali To display the children
		 * specimens of the specimen in acending order. Map hold parent specimen
		 * id and the count of all the childern under that parent specimen.
		 */

		Map countOfChildSpecimenMap = new HashMap<Long, Integer>();
		Integer countOfChildSpecimen = 1;
		for (int i = 0; i < childrenList.size(); i++)
		{
			Object[] obj = (Object[]) childrenList.get(i);
			Long parentSpecimenId = (Long) obj[2];

			/*
			 * fetching the count of children specimen if it's parent specimen
			 * Id is there in the Map else setting it to 1.
			 */

			if (countOfChildSpecimenMap.containsKey(parentSpecimenId))
			{
				countOfChildSpecimen = (Integer) countOfChildSpecimenMap.get(parentSpecimenId);
			}
			else
			{
				countOfChildSpecimen = 1;
			}

			for (int j = 0; j < finalList.size(); j++)
			{
				Object[] obj1 = (Object[]) finalList.get(j);
				Long spId = (Long) obj1[0];
				Long parentOfParentspId = (Long) obj1[2];

				// This if statement is not working.......convert Long objects
				// to long values
				if (parentSpecimenId.longValue() == spId.longValue())
				{

					finalList.add(j + countOfChildSpecimen, childrenList.get(i));
					countOfChildSpecimen++;
					countOfChildSpecimenMap.put(parentSpecimenId, countOfChildSpecimen);

					/*
					 * kalpana Update the children specimen count of all the
					 * parent specimens accept the immediate parent
					 */
					updateChildSpecimenCount(finalList, parentOfParentspId, countOfChildSpecimenMap, countOfChildSpecimen);
					break;
					// Here break is important ....once parent is found
				}
			}
		}

		// Here create XML String rep. of parent/child specimen tree from
		// finalList
		for (int i = 0; i < finalList.size(); i++)
		{
			Object[] specimens = (Object[]) finalList.get(i);
			Long spId = (Long) specimens[0];
			Long parentId = (Long) specimens[2];
			String spLabel1 = (String) specimens[1];
			// String spActivityStatus = (String) specimens[3];
			String type = (String) specimens[4];
			String spCollectionStatus = (String) specimens[5];

			// Added later for toolTip text for specimens
			String toolTipText = "Label : " + spLabel1 + " ; Type : " + type;

			String hqlCon = "select colEveParam.container from " + CollectionEventParameters.class.getName()
					+ " as colEveParam where colEveParam.specimen.id = " + spId;

			List container = executeQuery(hqlCon);
			for (int k = 0; k < container.size(); k++)
			{
				String con = (String) container.get(k);
				toolTipText += " ; Container : " + con;
			}

			// If parent id is null, then it's going to be a new specimens
			if (parentId == null)
			{
				while (!spStack.isEmpty()) // closes all specimens node
				// when a new specimens starts
				{
					spStack.pop();
					xmlString.append("</node>");
				}

				if (i == finalList.size() - 1) // If last element & parent
				// specimen
				{
					xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1 + "\" " + "toolTip=\""
							+ toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" " + "collectionStatus=\"" + spCollectionStatus + "\">");
					xmlString.append("</node>");
				}
				else
				// If not the last element and parent specimen
				{
					xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1 + "\" " + "toolTip=\""
							+ toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" " + "collectionStatus=\"" + spCollectionStatus + "\">");
					spStack.push((Object[]) finalList.get(i));
				}
			}

			else
			{
				Object[] previousSp = (Object[]) finalList.get(i - 1);
				Long previousSpId = (Long) previousSp[0];

				// If immediate prevoius node is parent of current node
				if (parentId.longValue() == previousSpId.longValue())
				{
					if (i != finalList.size() - 1) // if not the last element
					// in specimen list
					{
						Object[] nextSp = (Object[]) finalList.get(i + 1); // Getting
						// next
						// sp
						Long nextSpPid = (Long) nextSp[2];

						if (nextSpPid != null)
						{
							// Check if current specimen have children
							// specimens
							if (spId.longValue() == nextSpPid.longValue())
							{
								xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1 + "\" "
										+ "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" " + "collectionStatus=\""
										+ spCollectionStatus + "\">");
								spStack.push((Object[]) finalList.get(i));
							}
							else
							// if current specimen doesn't have
							// children specimens
							{
								xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1 + "\" "
										+ "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" " + "collectionStatus=\""
										+ spCollectionStatus + "\"></node> ");
							}
						}
						else
						{
							xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1 + "\" "
									+ "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" " + "collectionStatus=\""
									+ spCollectionStatus + "\"></node> ");
						}
					}

					else
					// last element in specimen List
					{
						xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1 + "\" "
								+ "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" " + "collectionStatus=\""
								+ spCollectionStatus + "\"></node> ");
						while (!spStack.isEmpty())
						{
							spStack.pop();
							xmlString.append("</node>");
						}
					}
				}
				else
				// current node has parent but not the immediate
				// prevoius node. Parent may be on the stack
				{
					Long peekSpecimenId2 = null;
					do
					{
						Object[] peekSp = (Object[]) spStack.peek(); // Retrieving
						// the
						// peek
						// element
						// at
						// stack
						Long peekSpId = (Long) peekSp[0];
						if (parentId.longValue() == peekSpId.longValue())// If
						// current
						// node
						// has
						// parent
						// as
						// peek
						// (last)
						// element
						// at
						// stack
						{
							if (i != finalList.size() - 1) // if not the last
							// element
							{
								Object[] nextSp = (Object[]) finalList.get(i + 1);
								Long nextSpPid = (Long) nextSp[2];

								// Here nextSpPid may be null
								if (nextSpPid != null)
								{
									// If it has children
									if (spId.longValue() == nextSpPid.longValue())
									{
										xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1
												+ "\" " + "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" "
												+ "collectionStatus=\"" + spCollectionStatus + "\">");
										spStack.push((Object[]) finalList.get(i));
									}
									else
									// if it doesn't has children
									{
										xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1
												+ "\" " + "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" "
												+ "collectionStatus=\"" + spCollectionStatus + "\"></node> ");
										break; // Note this break is imp
									}

								}
								else
								// next node parent id is null then
								// , then it's parent specimen
								{
									xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1
											+ "\" " + "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" "
											+ "collectionStatus=\"" + spCollectionStatus + "\"></node> ");
									break;
								}

							}
							else
							// If last element
							{
								xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1 + "\" "
										+ "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" " + "collectionStatus=\""
										+ spCollectionStatus + "\"></node> ");
								while (!spStack.isEmpty())
								{
									spStack.pop();
									xmlString.append("</node>");
								}

								break; // If last element,& stack is empty,
								// simply break
							}

						}
						else
						// If node has parent but at peek element of
						// stack
						{
							// So iterate till, node has parent element at
							// stack
							Long peekSpecimenId1 = null;
							do
							{
								spStack.pop();
								xmlString.append("</node>");

								Object[] peekSp1 = (Object[]) spStack.peek(); // Retrieving
								// the
								// peek
								// element
								// at
								// stack
								peekSpecimenId1 = (Long) peekSp1[0];

							}
							while (parentId.longValue() != peekSpecimenId1.longValue() && !spStack.isEmpty());
						}

						if (!spStack.empty())
						{
							Object[] peekSp2 = (Object[]) spStack.peek(); // Retrieving
							// the
							// peek
							// element
							// at
							// stack
							peekSpecimenId2 = (Long) peekSp2[0];
						}
					}
					while (parentId.longValue() == peekSpecimenId2.longValue() && !spStack.empty());

				}

			}
		}
	}

	/**
	 * kalpana Bug #5906 Reviewer : vaishali Update the children specimen count
	 * of all the parent specimens accept the immediate parent
	 */

	private void updateChildSpecimenCount(List finalList, Long spId, Map countOfChildSpecimenMap, Integer countOfChildSpecimen)
	{

		for (int j = 0; j < finalList.size(); j++)
		{
			Object[] obj1 = (Object[]) finalList.get(j);
			Long specimenId = (Long) obj1[0];
			Long parentSpecimenId = (Long) obj1[2];

			if (specimenId != null && specimenId.equals(spId))
			{
				if (countOfChildSpecimenMap.containsKey(specimenId))
				{
					Integer newChildCount = (Integer) countOfChildSpecimenMap.get(specimenId);
					newChildCount = newChildCount + 1;
					countOfChildSpecimenMap.put(specimenId, newChildCount);

					updateChildSpecimenCount(finalList, parentSpecimenId, countOfChildSpecimenMap, countOfChildSpecimen);
					return;
				}
			}

		}

	}

	/**
	 * Patch Id : FutureSCG_13 Description : method to executeQuery
	 */
	/**
	 * Executes hql Query and returns the results.
	 * 
	 * @param hql
	 *            String hql
	 * @throws DAOException
	 *             DAOException
	 * @throws ClassNotFoundException
	 *             ClassNotFoundException
	 */
	private List executeQuery(String hql) throws DAOException, ClassNotFoundException
	{
		HibernateDAO dao = (HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		dao.openSession(null);
		List list = dao.executeQuery(hql, null, false, null);
		dao.closeSession();
		return list;
	}

	private List executeHqlQuery(DAO dao, String hql) throws DAOException, ClassNotFoundException
	{
		List list = dao.executeQuery(hql, null, false, null);
		return list;
	}

	/**
	 * This function sets the data in QuertTreeNodeData object adds in a list of
	 * these nodes.
	 * 
	 * @param identifier
	 * @param objectName
	 * @param displayName
	 * @param parentIdentifier
	 * @param parentObjectName
	 * @param combinedParentIdentifier
	 * @param combinedParentObjectName
	 * @param activityStatus
	 * @param vector
	 */

	// Commented out as it is not required after Flex related chages
	/*
	 * private void setQueryTreeNode(String identifier, String objectName,
	 * String displayName, String parentIdentifier, String parentObjectName,
	 * String combinedParentIdentifier, String combinedParentObjectName, String
	 * activityStatus, String toolTipText, Vector vector) { if
	 * (!activityStatus.equals(Constants.ACTIVITY_STATUS_DISABLED)) {
	 * QueryTreeNodeData treeNode = new QueryTreeNodeData();
	 * treeNode.setIdentifier(identifier); treeNode.setObjectName(objectName);
	 * treeNode.setDisplayName(displayName);
	 * treeNode.setParentIdentifier(parentIdentifier);
	 * treeNode.setParentObjectName(parentObjectName);
	 * treeNode.setCombinedParentIdentifier(combinedParentIdentifier);
	 * treeNode.setCombinedParentObjectName(combinedParentObjectName);
	 * treeNode.setToolTipText(toolTipText); vector.add(treeNode); } }
	 */
	/**
	 * Patch Id : FutureSCG_14 Description : method to getToolTipText
	 */
	/**
	 * Generates tooltip for SCGs in c based views
	 * 
	 * @param eventDays
	 *            no of days
	 * @param collectionPointLabel
	 *            label of CPE
	 * @param receivedDate
	 *            received date for specimens
	 * @return String Tooltip text
	 */
	private static String getToolTipText(String eventDays, String collectionPointLabel, String receivedDate)
	{
		StringBuffer toolTipText = new StringBuffer();
		toolTipText.append("Event point : ");
		toolTipText.append(eventDays);
		toolTipText.append(";  Collection point label : ");
		toolTipText.append(collectionPointLabel);
		if (receivedDate != null)
		{
			toolTipText.append(";  Received date : ");
			toolTipText.append(receivedDate);
		}
		return toolTipText.toString();
	}

	// Mandar : 15-Jan-07 For Consent Tracking Withdrawal -------- start
	/*
	 * This method verifies and updates SCG and child elements for withdrawn
	 * consents
	 */
	private void verifyAndUpdateConsentWithdrawn(SpecimenCollectionGroup specimenCollectionGroup, SpecimenCollectionGroup oldspecimenCollectionGroup,
			DAO dao, SessionDataBean sessionDataBean) throws DAOException
	{
		Collection newConsentTierStatusCollection = specimenCollectionGroup.getConsentTierStatusCollection();
		Iterator itr = newConsentTierStatusCollection.iterator();
		while (itr.hasNext())
		{
			ConsentTierStatus consentTierStatus = (ConsentTierStatus) itr.next();
			if (consentTierStatus.getStatus().equalsIgnoreCase(Constants.WITHDRAWN))
			{
				long consentTierID = consentTierStatus.getConsentTier().getId().longValue();
				String cprWithdrawOption = specimenCollectionGroup.getConsentWithdrawalOption();
				WithdrawConsentUtil.updateSCG(specimenCollectionGroup, oldspecimenCollectionGroup, consentTierID, cprWithdrawOption, dao,
						sessionDataBean);
				// break;
			}
		}
	}

	public String retriveSCGNameFromSCGId(String id) throws DAOException
	{
		String scgName = "";
		String[] selectColumnName = {"name"};
		String[] whereColumnName = {"id"};
		String[] whereColumnCondition = {"="};
		Object[] whereColumnValue = {id};
		HibernateDAO dao = (HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		dao.openSession(null);
		List scgList = dao.retrieve(SpecimenCollectionGroup.class.getName(), selectColumnName, whereColumnName, whereColumnCondition,
				whereColumnValue, null);
		if (scgList != null && !scgList.isEmpty())
		{

			scgName = (String) scgList.get(0);
		}

		dao.closeSession();
		return scgName;
	}

	public Map getSpecimenList(Long id) throws DAOException
	{

		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			dao.openSession(null);
			List scgList = dao.retrieve(SpecimenCollectionGroup.class.getName(), "id", id);

			if (scgList != null && !scgList.isEmpty())
			{
				SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) scgList.get(0);
				Collection specimenCollection = specimenCollectionGroup.getSpecimenCollection();
				return CollectionProtocolUtil.getSpecimensMap(specimenCollection, "E1");
			}
			else
			{
				return null;
			}
		}
		catch (DAOException exception)
		{
			throw exception;
		}
		finally
		{
			dao.commit();
			dao.closeSession();
		}
	}

	private void updateOffset(Integer offset, SpecimenCollectionGroup specimenCollectionGroup, SessionDataBean sessionDataBean, DAO dao)
			throws DAOException, UserNotAuthorizedException
	{
		//CollectionProtocolRegistration cpr = getCollectionProtocolRegForSCG(specimenCollectionGroup, dao);
		//specimenCollectionGroup.setCollectionProtocolRegistration(cpr);
		CollectionProtocolEvent event = (CollectionProtocolEvent) specimenCollectionGroup.getCollectionProtocolEvent();
		CollectionProtocolRegistration cpr = specimenCollectionGroup.getCollectionProtocolRegistration();
		if (cpr != null)
		{
			String sourceObjName = SpecimenCollectionGroup.class.getName();
			String whereColName[] = {"collectionProtocolEvent.id", "collectionProtocolRegistration.id"};
			String whereColCond[] = {">", "="};
			Object whereColVal[] = {event.getId(), cpr.getId()};
			List specimenCollectionGroupCollection = (List) dao.retrieve(sourceObjName, null, whereColName, whereColCond, whereColVal,
					Constants.AND_JOIN_CONDITION);
			/*Collection specimenCollectionGroupCollection = (Collection) dao.retrieveAttribute(CollectionProtocolRegistration.class.getName(), cpr
			 .getId(), Constants.COLUMN_NAME_SCG_COLL);*/
			if (specimenCollectionGroupCollection != null && !specimenCollectionGroupCollection.isEmpty())
			{
				Iterator specimenCollectionGroupIterator = specimenCollectionGroupCollection.iterator();
				while (specimenCollectionGroupIterator.hasNext())
				{
					SpecimenCollectionGroup scg = (SpecimenCollectionGroup) specimenCollectionGroupIterator.next();
					//if(scg.getId().longValue() > specimenCollectionGroup.getId().longValue())
					//{
					if (scg.getOffset() != null)
						scg.setOffset(scg.getOffset() + offset);
					else
						scg.setOffset(offset);
					dao.update(scg, sessionDataBean, true, true, false);
					//}
				}
			}
		}

	}

	private void getDetailsOfCPRForSCG(CollectionProtocolRegistration cpr, DAO dao) throws DAOException
	{
		if (cpr != null && cpr.getId() != null)
		{
			String sourceObjName = CollectionProtocolRegistration.class.getName();
			String[] selectColName = {"participant", "collectionProtocol"};
			String[] whereColName = {"id"};
			String[] whereColCond = {"="};
			Object[] whereColVal = {cpr.getId()};

			List list = dao.retrieve(sourceObjName, selectColName, whereColName, whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);
			if (list != null && !list.isEmpty())
			{
				Object[] obj = (Object[]) list.get(0);
				Participant participant = (Participant) obj[0];
				CollectionProtocol collectionProtocol = (CollectionProtocol) obj[1];
				cpr.setParticipant(participant);
				cpr.setCollectionProtocol(collectionProtocol);
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	

}