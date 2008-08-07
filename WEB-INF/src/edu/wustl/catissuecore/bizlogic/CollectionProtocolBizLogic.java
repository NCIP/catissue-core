/**
 * <p>Title: CollectionProtocolBizLogic Class>
 * <p>Description:	CollectionProtocolBizLogic is used to add CollectionProtocol information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 09, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import edu.wustl.catissuecore.TaskTimeCalculater;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.CollectionProtocolDTO;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CollectionProtocolAuthorization;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.ParticipantRegistrationCacheManager;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exceptionformatter.DefaultExceptionFormatter;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * CollectionProtocolBizLogic is used to add CollectionProtocol information into the database using Hibernate.
 * @author Mandar Deshmukh
 */
public class CollectionProtocolBizLogic extends SpecimenProtocolBizLogic implements Roles
{

	/**
	 * Saves the CollectionProtocol object in the database.
	 * @param obj The CollectionProtocol object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 * @throws HibernateException Exception thrown during hibernate operations.
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		CollectionProtocol collectionProtocol = null;
		Map<String,SiteUserRolePrivilegeBean> rowIdMap =null;
		if (obj instanceof CollectionProtocolDTO)
		{
			CollectionProtocolDTO CpDto = (CollectionProtocolDTO) obj;
			collectionProtocol = CpDto.getCollectionProtocol();
			rowIdMap = CpDto.getRowIdBeanMap();
		}
		else
		{
		    collectionProtocol = (CollectionProtocol) obj;
		}
		
		checkStatus(dao, collectionProtocol.getPrincipalInvestigator(), "Principal Investigator");
		validateCollectionProtocol(dao, collectionProtocol, "Principal Investigator");
		insertCollectionProtocol(dao, sessionDataBean, collectionProtocol,rowIdMap);
	}

	private void insertCollectionProtocol(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocol collectionProtocol,Map<String,SiteUserRolePrivilegeBean> rowIdMap) throws DAOException, UserNotAuthorizedException
	{
		setPrincipalInvestigator(dao, collectionProtocol);
		setCoordinatorCollection(dao, collectionProtocol);
		dao.insert(collectionProtocol, sessionDataBean, true, true);
		insertCPEvents(dao, sessionDataBean, collectionProtocol);
		insertchildCollectionProtocol(dao, sessionDataBean, collectionProtocol,rowIdMap);
		HashSet<CollectionProtocol> protectionObjects = new HashSet<CollectionProtocol>();
		protectionObjects.add(collectionProtocol);

		CollectionProtocolAuthorization collectionProtocolAuthorization = new CollectionProtocolAuthorization();
		collectionProtocolAuthorization.authenticate(collectionProtocol, protectionObjects, rowIdMap);
	}

	private void validateCollectionProtocol(DAO dao, CollectionProtocol collectionProtocol,
			String errorName) throws DAOException
	{
		Collection childCPCollection = collectionProtocol.getChildCollectionProtocolCollection();
		Iterator cpIterator = childCPCollection.iterator();
		while (cpIterator.hasNext())
		{
			CollectionProtocol cp = (CollectionProtocol) cpIterator.next();
			checkStatus(dao, cp.getPrincipalInvestigator(), "Principal Investigator");
		}
	}

	/**
	 * This function used to insert collection protocol events and specimens 
	 * for the collectionProtocol object. 
	 * @param dao used to insert events and specimens into database.
	 * @param sessionDataBean Contains session information
	 * @param collectionProtocol collection protocol for which events & specimens 
	 * to be added
	 * 
	 * @throws DAOException If fails to insert events or its specimens 
	 * @throws UserNotAuthorizedException If user is not authorized or session information 
	 * is incorrect.
	 */
	private void insertCPEvents(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocol collectionProtocol) throws DAOException, UserNotAuthorizedException
	{

		Iterator it = collectionProtocol.getCollectionProtocolEventCollection().iterator();
		RequirementSpecimenBizLogic bizLogic = new RequirementSpecimenBizLogic();
		while (it.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) it.next();
			collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
			Collection<SpecimenRequirement> reqSpecimenCollection = collectionProtocolEvent
					.getSpecimenRequirementCollection();

			insertCollectionProtocolEvent(dao, sessionDataBean, collectionProtocolEvent);

			insertSpecimens(bizLogic, dao, reqSpecimenCollection, sessionDataBean,
					collectionProtocolEvent);
		}
	}

	/**
	 * This function will insert child Collection Protocol. 
	 * @param dao
	 * @param sessionDataBean
	 * @param collectionProtocol
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 */
	private void insertchildCollectionProtocol(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocol collectionProtocol,Map<String,SiteUserRolePrivilegeBean> rowIdMap) throws DAOException, UserNotAuthorizedException
	{

		Collection childCPCollection = collectionProtocol.getChildCollectionProtocolCollection();
		Iterator cpIterator = childCPCollection.iterator();
		while (cpIterator.hasNext())
		{
			CollectionProtocol cp = (CollectionProtocol) cpIterator.next();
			cp.setParentCollectionProtocol(collectionProtocol);
			insertCollectionProtocol(dao, sessionDataBean, cp,rowIdMap);
		}

	}


	/**
	 * 
	 * @param bizLogic used to call business logic of Specimen.
	 * @param dao Data access object to insert Specimen Collection groups
	 * and specimens.
	 * @param collectionRequirementGroup 
	 * @param sessionDataBean object containing session information which 
	 * is required for authorization.
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 */
	private void insertSpecimens(RequirementSpecimenBizLogic bizLogic, DAO dao,
			Collection<SpecimenRequirement> reqSpecimenCollection, SessionDataBean sessionDataBean,
			CollectionProtocolEvent collectionProtocolEvent) throws DAOException,
			UserNotAuthorizedException
	{
		TaskTimeCalculater specimenInsert = TaskTimeCalculater.startTask("Insert specimen for CP",
				CollectionProtocolBizLogic.class);
		Iterator<SpecimenRequirement> specIter = reqSpecimenCollection.iterator();
		Collection specimenMap = new LinkedHashSet();
		while (specIter.hasNext())
		{
			SpecimenRequirement SpecimenRequirement = specIter.next();
			SpecimenRequirement.setCollectionProtocolEvent(collectionProtocolEvent);
			if (SpecimenRequirement.getParentSpecimen() != null)
			{
				addToParentSpecimen(SpecimenRequirement);
			}
			else
			{
				specimenMap.add(SpecimenRequirement);
			}
		}
		//bizLogic.setCpbased(true);
		bizLogic.insertMultiple(specimenMap, (AbstractDAO) dao, sessionDataBean);
		TaskTimeCalculater.endTask(specimenInsert);
	}

	/**
	 * This function adds specimen object to its parent's childrenCollection
	 * if not already added.
	 * 
	 * @param SpecimenRequirement The object to be added to it's parent childrenCollection
	 */
	private void addToParentSpecimen(SpecimenRequirement SpecimenRequirement)
	{
		Collection<AbstractSpecimen> childrenCollection = SpecimenRequirement.getParentSpecimen()
				.getChildSpecimenCollection();
		if (childrenCollection == null)
		{
			childrenCollection = new LinkedHashSet<AbstractSpecimen>();
			//childrenCollection = new HashSet<AbstractSpecimen>();
		}
		if (!childrenCollection.contains(SpecimenRequirement))
		{
			childrenCollection.add(SpecimenRequirement);
		}
	}

	

	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		System.out.println("PostInsert called");
		CollectionProtocol collectionProtocol = null;
		if (obj instanceof CollectionProtocolDTO)
		{
			CollectionProtocolDTO CpDto = (CollectionProtocolDTO) obj;
			collectionProtocol = CpDto.getCollectionProtocol();
		}
		else
		{
		  collectionProtocol = (CollectionProtocol) obj;
		}
		ParticipantRegistrationCacheManager participantRegistrationCacheManager = new ParticipantRegistrationCacheManager();
		participantRegistrationCacheManager.addNewCP(collectionProtocol.getId(), collectionProtocol
				.getTitle(), collectionProtocol.getShortTitle());

	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		CollectionProtocol collectionProtocol =null;
		if (obj instanceof CollectionProtocolDTO)
		{
			CollectionProtocolDTO cpDto = (CollectionProtocolDTO) obj;
			collectionProtocol = cpDto.getCollectionProtocol();
			HashSet<CollectionProtocol> protectionObjects = new HashSet<CollectionProtocol>();
			protectionObjects.add(collectionProtocol);
		}
		else
		{
			collectionProtocol = (CollectionProtocol)obj;
		}
		isCollectionProtocolLabelUnique(collectionProtocol);
		modifyCPObject(dao, sessionDataBean, collectionProtocol);
	}

	/**
	 * @param dao
	 * @param sessionDataBean
	 * @param collectionProtocol
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 */
	private void modifyCPObject(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocol collectionProtocol) throws DAOException, UserNotAuthorizedException
	{
		
		CollectionProtocol collectionProtocolOld;
		Session session = null;
		try
		{
			session = DBUtil.getCleanSession();
			collectionProtocolOld = getOldCollectionProtocol(session, collectionProtocol.getId());
			setCPRCollection(collectionProtocol);
			if (collectionProtocolOld.getCollectionProtocolRegistrationCollection().size() > 0
					&& collectionProtocol.getActivityStatus().equals(Constants.ACTIVITY_STATUS_ACTIVE))
			{
				setCPEventCollection(collectionProtocol);
				editCPObj(dao, sessionDataBean, collectionProtocol, collectionProtocolOld);
			}
			else
			{			
				Collection<ConsentTier> oldConsentTierCollection = collectionProtocolOld.getConsentTierCollection();
				Collection<ConsentTier> newConsentTierCollection = collectionProtocol.getConsentTierCollection();
				checkConsents(dao, oldConsentTierCollection,newConsentTierCollection);
				editEvents(dao, sessionDataBean, collectionProtocol, collectionProtocolOld);
				editCPObj(dao, sessionDataBean, collectionProtocol, collectionProtocolOld);
			}
		}
		catch (BizLogicException e)
		{
			throw new DAOException("Failed to create Session Object"+ e.getMessage());
		} 
		finally
		{
			session.close();
		}
	}

	/**
	 * @param dao
	 * @param collectionProtocol
	 * @throws DAOException
	 */
	private void setCPRCollection(CollectionProtocol collectionProtocol)
			throws DAOException
	{
		String cprHQL ="select elements(cp.collectionProtocolRegistrationCollection)"+ 
		" from edu.wustl.catissuecore.domain.CollectionProtocol as cp" +
		" where cp.id="+ collectionProtocol.getId();
		Collection cprCollection = executeHQL(cprHQL);
		collectionProtocol.setCollectionProtocolRegistrationCollection(cprCollection);
	}
	
	/**
	 * @param dao
	 * @param collectionProtocol
	 * @throws DAOException
	 */
	private void setCPEventCollection(CollectionProtocol collectionProtocol)
			throws DAOException
	{
		String cpEventHQL ="select elements(cp.collectionProtocolEventCollection)"+ 
		" from edu.wustl.catissuecore.domain.CollectionProtocol as cp" +
		" where cp.id="+ collectionProtocol.getId();
		Collection cpEventCollection = executeHQL(cpEventHQL);
		collectionProtocol.setCollectionProtocolEventCollection(cpEventCollection);
	}
	/**
	 * @param dao
	 * @param cprHQL
	 * @return
	 * @throws DAOException
	 */
	private Collection executeHQL(String cprHQL) throws DAOException
	{
		Collection cprCollection = new LinkedHashSet();
		try
		{
			DefaultBizLogic bizLogic = new DefaultBizLogic();
			List list = bizLogic.executeQuery(cprHQL);
			cprCollection.addAll(list);
		}
		catch (ClassNotFoundException e)
		{
			throw new DAOException("Class Not found");
		}
		return cprCollection;
	}
	

	/**
	 * @param dao DAO Object
	 * @param obj Transient Object
	 * @param oldObj Persistent object
	 * @param sessionDataBean Session data
	 * @param collectionProtocol  Transient CP Object
	 * @param collectionProtocolOld Persistent CP Object
	 * @throws DAOException Database related exception
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 */
	private void editCPObj(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocol collectionProtocol, CollectionProtocol collectionProtocolOld)
			throws DAOException, UserNotAuthorizedException
	{
		if (!collectionProtocol.getPrincipalInvestigator().getId().equals(
				collectionProtocolOld.getPrincipalInvestigator().getId()))
		{
			checkStatus(dao, collectionProtocol.getPrincipalInvestigator(),
					"Principal Investigator");
		}
		checkCoordinatorStatus(dao, collectionProtocol, collectionProtocolOld);
		checkForChangedStatus(collectionProtocol, collectionProtocolOld);
		dao.update(collectionProtocol, sessionDataBean, true, true, false);
		//Audit of Collection Protocol
		dao.audit(collectionProtocol, collectionProtocolOld, sessionDataBean, true);
		disableRelatedObjects(dao, collectionProtocol);
		updatePIAndCoordinatorGroup(dao, collectionProtocol, collectionProtocolOld);
	}
	
	/**
	 * @param dao DAO object
	 * @param oldConsentTierCollection old Consent tier collection 
	 * @param newConsentTierCollection New Consent tier collection
	 * @throws DAOException
	 */
	private void checkConsents(DAO dao,Collection<ConsentTier> oldConsentTierCollection,Collection<ConsentTier> newConsentTierCollection) throws DAOException
	{
		Iterator<ConsentTier> itr = oldConsentTierCollection.iterator();
		while(itr.hasNext())
		{
			ConsentTier cTier = itr.next();
			Object obj = getCorrespondingOldObject(newConsentTierCollection, cTier.getId());
			if(obj == null)
			{
				deleteConsents(dao,cTier);
			}
		}
	}
	/**
	 * 
	 * @param dao DAO Object
	 * @param cTier ConsentTier object
	 * @throws DAOException DAO Exception
	 */
	private void deleteConsents(DAO dao, ConsentTier cTier) throws DAOException
	{
		ConsentTier consentTier = (ConsentTier)dao.retrieve(ConsentTier.class.getName(),cTier.getId());
		dao.delete(consentTier);
	}

	/**
	 * @param dao DAO Object
	 * @param sessionDataBean Session data
	 * @param collectionProtocol Transient CP object
	 * @param collectionProtocolOld Old CP Object
	 * @throws DAOException Database related exception
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 */
	private void editEvents(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocol collectionProtocol, CollectionProtocol collectionProtocolOld)
			throws DAOException, UserNotAuthorizedException
	{
		Collection oldCollectionProtocolEventCollection = collectionProtocolOld
			.getCollectionProtocolEventCollection();
		updateCollectionProtocolEvents(dao, sessionDataBean, collectionProtocol,
			oldCollectionProtocolEventCollection);
	}

	/**
	 * @param dao
	 * @param collectionProtocol
	 * @param collectionProtocolOld
	 * @throws DAOException
	 */
	private void updatePIAndCoordinatorGroup(DAO dao, CollectionProtocol collectionProtocol,
			CollectionProtocol collectionProtocolOld) throws DAOException
	{
		try
		{
			CollectionProtocolAuthorization collectionProtocolAuthorization= new CollectionProtocolAuthorization();
			collectionProtocolAuthorization.updatePIAndCoordinatorGroup(dao, collectionProtocolOld, true);

			Long csmUserId = collectionProtocolAuthorization.getCSMUserId(dao, collectionProtocol.getPrincipalInvestigator());
			if (csmUserId != null)
			{
				collectionProtocol.getPrincipalInvestigator().setCsmUserId(csmUserId);
				Logger.out.debug("PI ....."
						+ collectionProtocol.getPrincipalInvestigator().getCsmUserId());
				collectionProtocolAuthorization.updatePIAndCoordinatorGroup(dao, collectionProtocol, false);
			}
		}
		catch (SMException smExp)
		{
			throw handleSMException(smExp);
		}
	}

	/**
	 * @param collectionProtocol
	 * @return
	 * @throws DAOException
	 */
	private CollectionProtocol getOldCollectionProtocol(Session session, Long identifier)
			throws DAOException
	{
		CollectionProtocol collectionProtocolOld;
		try
		{
			collectionProtocolOld = (CollectionProtocol) session.load(
					CollectionProtocol.class, identifier);
		}
		catch (Exception e)
		{
			throw new DAOException(e.getMessage(), e);
		}
		return collectionProtocolOld;
	}

	/**
	 * @param dao
	 * @param collectionProtocol
	 * @param collectionProtocolOld
	 * @throws DAOException
	 */
	private void checkCoordinatorStatus(DAO dao, CollectionProtocol collectionProtocol,
			CollectionProtocol collectionProtocolOld) throws DAOException
	{
		Iterator it = collectionProtocol.getCoordinatorCollection().iterator();
		while (it.hasNext())
		{
			User coordinator = (User) it.next();
			if (!coordinator.getId().equals(collectionProtocol.getPrincipalInvestigator().getId()))
			{
				CollectionProtocolAuthorization collectionProtocolAuthorization = new CollectionProtocolAuthorization();
				if (!collectionProtocolAuthorization.hasCoordinator(coordinator, collectionProtocolOld)) 
					checkStatus(dao, coordinator, "Coordinator");
			}
			else
			{
				it.remove();
			}
		}
	}

	/**
	 * @param dao
	 * @param collectionProtocol
	 * @throws DAOException
	 */
	private void disableRelatedObjects(DAO dao, CollectionProtocol collectionProtocol)
			throws DAOException
	{
		//Disable related Objects
		Logger.out.debug("collectionProtocol.getActivityStatus() "
				+ collectionProtocol.getActivityStatus());
		if (collectionProtocol.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("collectionProtocol.getActivityStatus() "
					+ collectionProtocol.getActivityStatus());
			Long collectionProtocolIDArr[] = {collectionProtocol.getId()};

			CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic) BizLogicFactory
					.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
			bizLogic.disableRelatedObjectsForCollectionProtocol(dao, collectionProtocolIDArr);
		}
	}

	/**
	 * @param dao DAO Object
	 * @param sessionDataBean Session data
	 * @param collectionProtocol Transient CP object
	 * @param collectionProtocolOld Old CP Object
	 * @throws DAOException Database related exception
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 */
	private void updateCollectionProtocolEvents(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocol collectionProtocol, Collection<CollectionProtocolEvent> oldCPEventCollection)
			throws DAOException, UserNotAuthorizedException
	{
		RequirementSpecimenBizLogic reqSpBiz = new RequirementSpecimenBizLogic();
		Iterator it = collectionProtocol.getCollectionProtocolEventCollection().iterator();
		while (it.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) it.next();
			collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
			CollectionProtocolEvent oldCollectionProtocolEvent = null;
			if (collectionProtocolEvent.getId() == null || collectionProtocolEvent.getId() <= 0)
			{
				insertCollectionProtocolEvent(dao, sessionDataBean, collectionProtocolEvent);
			}
			else
			{
				dao.update(collectionProtocolEvent, sessionDataBean, true, false, false);
				oldCollectionProtocolEvent = (CollectionProtocolEvent) getCorrespondingOldObject(
						oldCPEventCollection, collectionProtocolEvent.getId());
				dao.audit(collectionProtocolEvent, oldCollectionProtocolEvent, sessionDataBean,
						true);
			}
			//Audit of collectionProtocolEvent
			reqSpBiz.updateSpecimens(dao, sessionDataBean, oldCollectionProtocolEvent,
					collectionProtocolEvent);
		}
		Collection<CollectionProtocolEvent> newCPEventCollection = collectionProtocol.getCollectionProtocolEventCollection();
		checkEventDelete(dao,reqSpBiz,newCPEventCollection,oldCPEventCollection);
	}
	
	/**
	 * This method check for Event to delete or not
	 * @param dao DAO Object
	 * @param reqSpBiz Object of requirement Sp Bizlogic
	 * @param cpEventCollection CP Event Collection 
	 * @param oldCpEventCollection Old Cp Event Collection
	 * @throws DAOException Database related exception
	 */
	private void checkEventDelete(DAO dao,RequirementSpecimenBizLogic reqSpBiz,Collection<CollectionProtocolEvent> cpEventCollection, 
			Collection<CollectionProtocolEvent> oldCpEventCollection) throws DAOException
	{
		CollectionProtocolEvent oldCpEvent = null;
		Iterator<CollectionProtocolEvent> itr = oldCpEventCollection.iterator();
		while(itr.hasNext())
		{
			oldCpEvent = (CollectionProtocolEvent) itr.next();
			Object cpe = getCorrespondingOldObject(cpEventCollection, oldCpEvent.getId());
			if(cpe == null)
			{
				deleteEvent(dao,oldCpEvent,reqSpBiz);
			}
		}
	}
	
	/**
	 * This method will delete the event and the req spe under that event 
	 * @param dao DAO object
	 * @param oldCpEvent Persistent CP Event
	 * @param reqSpBiz Requirement Specimen biz logic object
	 * @throws DAOException Database related exception
	 */
	private void deleteEvent(DAO dao, CollectionProtocolEvent oldCpEvent,RequirementSpecimenBizLogic reqSpBiz) throws DAOException
	{
		CollectionProtocolEvent cpEvent = (CollectionProtocolEvent) dao.retrieve(CollectionProtocolEvent.class.getName(), oldCpEvent.getId());
		SpecimenRequirement oldSpReq = null;
		Collection<SpecimenRequirement> spReqColl = cpEvent.getSpecimenRequirementCollection();
		Iterator<SpecimenRequirement> spReqItr = spReqColl.iterator();
		while(spReqItr.hasNext())
		{
			oldSpReq = (SpecimenRequirement)spReqItr.next();
			if(Constants.NEW_SPECIMEN.equals(oldSpReq.getLineage()))
			{
				reqSpBiz.deleteRequirementSpecimen(dao, oldSpReq);	
			}
		}
		dao.delete(cpEvent);
	}
	/**
	 * 
	 * @param dao DAO object
	 * @param sessionDataBean Session details
	 * @param collectionProtocolEvent CPE Object
	 * @throws DAOException Database related exception
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 */
	private void insertCollectionProtocolEvent(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolEvent collectionProtocolEvent) throws DAOException,
			UserNotAuthorizedException
	{
		dao.insert(collectionProtocolEvent, sessionDataBean, true, false);
	}
	
	//This method sets the Principal Investigator.
	private void setPrincipalInvestigator(DAO dao, CollectionProtocol collectionProtocol)
			throws DAOException
	{
		//List list = dao.retrieve(User.class.getName(), "id", collectionProtocol.getPrincipalInvestigator().getId());
		//if (list.size() != 0)
		Object obj = dao.retrieve(User.class.getName(), collectionProtocol
				.getPrincipalInvestigator().getId());
		if (obj != null)
		{
			User pi = (User) obj;//list.get(0);
			collectionProtocol.setPrincipalInvestigator(pi);
			collectionProtocol.getAssignedProtocolUserCollection().add(pi);
			System.out.println();
		}
	}

	//This method sets the User Collection.
	private void setCoordinatorCollection(DAO dao, CollectionProtocol collectionProtocol)
			throws DAOException
	{
		Long piID = collectionProtocol.getPrincipalInvestigator().getId();
		Logger.out
				.debug("Coordinator Size " + collectionProtocol.getCoordinatorCollection().size());
		Collection coordinatorColl = new HashSet();

		Iterator it = collectionProtocol.getCoordinatorCollection().iterator();
		while (it.hasNext())
		{
			User aUser = (User) it.next();
			if (!aUser.getId().equals(piID))
			{
				Logger.out.debug("Coordinator ID :" + aUser.getId());
				Object obj = dao.retrieve(User.class.getName(), aUser.getId());
				if (obj != null)
				{
					User coordinator = (User) obj;//list.get(0);

					//checkStatus(dao, coordinator, "coordinator");
					String activityStatus = coordinator.getActivityStatus();
					if (activityStatus.equals(Constants.ACTIVITY_STATUS_CLOSED))
					{
						throw new DAOException("Coordinator " + coordinator.getLastName() + " "
								+ coordinator.getFirstName() + " "
								+ ApplicationProperties.getValue("error.object.closed"));
					}

					coordinatorColl.add(coordinator);
					coordinator.getCollectionProtocolCollection().add(collectionProtocol);
					collectionProtocol.getAssignedProtocolUserCollection().add(coordinator);
				}
			}
		}
		collectionProtocol.setCoordinatorCollection(coordinatorColl);
	}

	public void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds,
			Long userId, String roleId, boolean assignToUser, boolean assignOperation)
			throws SMException, DAOException
	{
		super.setPrivilege(dao, privilegeName, objectType, objectIds, userId, roleId, assignToUser,
				assignOperation);

		//		CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		//		bizLogic.assignPrivilegeToRelatedObjectsForCP(dao,privilegeName,objectIds,userId, roleId, assignToUser);
	}


	/**
	 * Executes hql Query and returns the results.
	 * @param hql String hql
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	private List executeHqlQuery(DAO dao, String hql) throws DAOException, ClassNotFoundException
	{
		List list = dao.executeQuery(hql, null, false, null);
		return list;
	}

	protected boolean isSpecimenExists(DAO dao, Long cpId) throws DAOException,
			ClassNotFoundException
	{

		String hql = " select" + " elements(scg.specimenCollection) " + "from "
				+ " edu.wustl.catissuecore.domain.CollectionProtocol as cp"
				+ ", edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr"
				+ ", edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg"
				+ ", edu.wustl.catissuecore.domain.Specimen as s" + " where cp.id = " + cpId
				+ "  and" + " cp.id = cpr.collectionProtocol.id and"
				+ " cpr.id = scg.collectionProtocolRegistration.id and"
				+ " scg.id = s.specimenCollectionGroup.id and " + " s.activityStatus = '"
				+ Constants.ACTIVITY_STATUS_ACTIVE + "'";

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
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{

		//Added by Ashish
		//setAllValues(obj);
		//END

		CollectionProtocol protocol =null;
		if (obj instanceof CollectionProtocolDTO)
		{
			CollectionProtocolDTO CpDto = (CollectionProtocolDTO) obj;
			protocol = CpDto.getCollectionProtocol();
		}
		else
		{
		    protocol = (CollectionProtocol) obj;
		}
		Collection eventCollection = protocol.getCollectionProtocolEventCollection();

		/**
		 * Start: Change for API Search   --- Jitendra 06/10/2006
		 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
		 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
		 * So we removed default class level initialization on domain object and are initializing in method
		 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
		 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
		 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
		 */
		ApiSearchUtil.setSpecimenProtocolDefault(protocol);
		//End:-  Change for API Search 

		//Added by Ashish
		Validator validator = new Validator();
		String message = "";
		if (protocol == null)
		{
			throw new DAOException(ApplicationProperties.getValue("domain.object.null.err.msg",
					"Collection Protocol"));
		}

		if (validator.isEmpty(protocol.getTitle()))
		{
			message = ApplicationProperties.getValue("collectionprotocol.protocoltitle");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}
		if (validator.isEmpty(protocol.getShortTitle()))
		{
			message = ApplicationProperties.getValue("collectionprotocol.shorttitle");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		//		if (validator.isEmpty(protocol.getIrbIdentifier()))
		//		{
		//		message = ApplicationProperties.getValue("collectionprotocol.irbid");
		//		throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		//		}		

		if (protocol.getStartDate() != null)
		{
			String errorKey = validator.validateDate(protocol.getStartDate().toString(), false);
			//			if(errorKey.trim().length() >0  )		
			//			{
			//			message = ApplicationProperties.getValue("collectionprotocol.startdate");
			//			throw new DAOException(ApplicationProperties.getValue(errorKey,message));	
			//			}
		}
		else
		{
			message = ApplicationProperties.getValue("collectionprotocol.startdate");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}
		if (protocol.getEndDate() != null)
		{
			String errorKey = validator.validateDate(protocol.getEndDate().toString(), false);
			//			if(errorKey.trim().length() >0  )		
			//			{
			//			message = ApplicationProperties.getValue("collectionprotocol.enddate");
			//			throw new DAOException(ApplicationProperties.getValue(errorKey,message));	
			//			}
		}

		if (protocol.getPrincipalInvestigator().getId() == -1)
		{
			//message = ApplicationProperties.getValue("collectionprotocol.specimenstatus");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",
					"Principal Investigator"));
		}

		Collection protocolCoordinatorCollection = protocol.getCoordinatorCollection();
		//		if(protocolCoordinatorCollection == null || protocolCoordinatorCollection.isEmpty())
		//		{
		//		//message = ApplicationProperties.getValue("collectionprotocol.specimenstatus");
		//		throw new DAOException(ApplicationProperties.getValue("errors.one.item.required","Protocol Coordinator"));
		//		}
		if (protocolCoordinatorCollection != null && !protocolCoordinatorCollection.isEmpty())
		{
			Iterator protocolCoordinatorItr = protocolCoordinatorCollection.iterator();
			while (protocolCoordinatorItr.hasNext())
			{
				User protocolCoordinator = (User) protocolCoordinatorItr.next();
				if (protocolCoordinator.getId() == protocol.getPrincipalInvestigator().getId())
				{
					throw new DAOException(ApplicationProperties
							.getValue("errors.pi.coordinator.same"));
				}
			}
		}

		if (eventCollection != null && eventCollection.size() != 0)
		{
			CDEManager manager = CDEManager.getCDEManager();

			if (manager == null)
			{
				throw new DAOException("Failed to get CDE manager object. "
						+ "CDE Manager is not yet initialized.");
			}

			List specimenClassList = manager.getPermissibleValueList(
					Constants.CDE_NAME_SPECIMEN_CLASS, null);

			//	    	NameValueBean undefinedVal = new NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
			List tissueSiteList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_TISSUE_SITE, null);

			List pathologicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_PATHOLOGICAL_STATUS, null);
			List clinicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_CLINICAL_STATUS, null);

			Iterator eventIterator = eventCollection.iterator();

			while (eventIterator.hasNext())
			{
				CollectionProtocolEvent event = (CollectionProtocolEvent) eventIterator.next();

				if (event == null)
				{
					throw new DAOException(ApplicationProperties
							.getValue("collectionProtocol.eventsEmpty.errMsg"));
				}
				else
				{
					if (!Validator.isEnumeratedValue(clinicalStatusList, event.getClinicalStatus()))
					{
						throw new DAOException(ApplicationProperties
								.getValue("collectionProtocol.clinicalStatus.errMsg"));
					}

					//Added for Api Search
					if (event.getStudyCalendarEventPoint() == null)
					{
						message = ApplicationProperties
								.getValue("collectionprotocol.studycalendartitle");
						throw new DAOException(ApplicationProperties.getValue(
								"errors.item.required", message));
					}
					// Added by Vijay for API testAddCollectionProtocolWithWrongCollectionPointLabel
					if (validator.isEmpty(event.getCollectionPointLabel()))
					{
						message = ApplicationProperties
								.getValue("collectionprotocol.collectionpointlabel");
						throw new DAOException(ApplicationProperties.getValue(
								"errors.item.required", message));
					}

					Collection<SpecimenRequirement> reqCollection = event
							.getSpecimenRequirementCollection();

					if (reqCollection != null && reqCollection.size() != 0)
					{
						Iterator<SpecimenRequirement> reqIterator = reqCollection.iterator();

						while (reqIterator.hasNext())
						{

							SpecimenRequirement SpecimenRequirement = reqIterator.next();
							if (SpecimenRequirement == null)
							{
								throw new DAOException(ApplicationProperties
										.getValue("protocol.spReqEmpty.errMsg"));
							}
							ApiSearchUtil.setReqSpecimenDefault(SpecimenRequirement);
							String specimenClass = SpecimenRequirement.getClassName();
							if (!Validator.isEnumeratedValue(specimenClassList, specimenClass))
							{
								throw new DAOException(ApplicationProperties
										.getValue("protocol.class.errMsg"));
							}
							if (!Validator.isEnumeratedValue(Utility
									.getSpecimenTypes(specimenClass), SpecimenRequirement
									.getSpecimenType()))
							{
								throw new DAOException(ApplicationProperties
										.getValue("protocol.type.errMsg"));
							}
							//							TODO:Abhijit Checkout valid values for tisse site and Pathalogy status.
							//							if (!Validator.isEnumeratedValue(tissueSiteList, specimen
							//							.getTissueSite()))
							//							{
							//							throw new DAOException(ApplicationProperties
							//							.getValue("protocol.tissueSite.errMsg"));
							//							}

							//							if (!Validator.isEnumeratedValue(pathologicalStatusList,
							//							specimen.getActivityStatus()))
							//							{
							//							throw new DAOException(ApplicationProperties
							//							.getValue("protocol.pathologyStatus.errMsg"));
							//							}

						}

					}
					else
					{
						throw new DAOException(ApplicationProperties
								.getValue("protocol.spReqEmpty.errMsg"));
					}
				}
			}
		}
		else
		{
			//commented by vaishali... it' not necessary condition
			//throw new DAOException(ApplicationProperties
			//	.getValue("collectionProtocol.eventsEmpty.errMsg"));
		}

		if (operation.equals(Constants.ADD))
		{

			validateCPTitle(protocol);
			if (!Constants.ACTIVITY_STATUS_ACTIVE.equals(protocol.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties
						.getValue("activityStatus.active.errMsg"));
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, protocol
					.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
			}
		}
		System.out.println("=========================================================");
		System.out.println("=================VALIDATING COLLECTION COMPLETE==========");

		//		check the activity status of all the specimens associated to the Collection Protocol
		if (protocol.getActivityStatus() != null
				&& protocol.getActivityStatus().equalsIgnoreCase(Constants.DISABLED))
		{
			try
			{

				boolean isSpecimenExist = (boolean) isSpecimenExists(dao, (Long) protocol.getId());
				if (isSpecimenExist)
				{
					throw new DAOException(ApplicationProperties
							.getValue("collectionprotocol.specimen.exists"));
				}

			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}

		return true;
	}

	/**
	 * @param protocol Collection protocol
	 * @throws DAOException
	 */
	private void validateCPTitle(CollectionProtocol protocol) throws DAOException
	{
		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDao.openSession(null);
		String queryStr = "select title from catissue_specimen_protocol where title = '" + protocol.getTitle()+ "'";
		try
		{
			List<String> titleList = jdbcDao.executeQuery(queryStr, null, false, null);
			if(!titleList.isEmpty())
			{
				throw new DAOException("Collection Protocol with the same Title already exists");
			}
		}
		catch (ClassNotFoundException e1)
		{
			throw new DAOException("Error while checking Title");
		}
	}

	public void postUpdate(DAO dao, Object currentObj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException, UserNotAuthorizedException
	{
		CollectionProtocol collectionProtocol =null;
		if (currentObj instanceof CollectionProtocolDTO)
		{
			CollectionProtocolDTO cpDto = (CollectionProtocolDTO)currentObj;
			collectionProtocol = cpDto.getCollectionProtocol();
		}
		else
		{
			collectionProtocol = (CollectionProtocol)currentObj;
		}
		
		ParticipantRegistrationCacheManager participantRegistrationCacheManager = new ParticipantRegistrationCacheManager();
			participantRegistrationCacheManager.updateCPTitle(collectionProtocol.getId(),
					collectionProtocol.getTitle());

			participantRegistrationCacheManager.updateCPShortTitle(collectionProtocol.getId(),
					collectionProtocol.getShortTitle());

		if (collectionProtocol.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			participantRegistrationCacheManager.removeCP(collectionProtocol.getId());
		}

	}

	//	mandar : 31-Jan-07 ----------- consents tracking
	private void verifyConsentsWaived(CollectionProtocol collectionProtocol)
	{
		//check for consentswaived
		if (collectionProtocol.getConsentsWaived() == null)
		{
			collectionProtocol.setConsentsWaived(new Boolean(false));
		}
	}

	/**
	 * Patch Id : FutureSCG_7
	 * Description : method to validate the CPE against uniqueness
	 */
	/**
	 * Method to check whether CollectionProtocolLabel is unique for the given collection protocol 
	 * @param CollectionProtocol CollectionProtocol
	 * @throws DAOException daoException with proper message 
	 */
	protected void isCollectionProtocolLabelUnique(CollectionProtocol collectionProtocol)
			throws DAOException
	{
		if (collectionProtocol != null)
		{
			HashSet cpLabelsSet = new HashSet();
			Collection collectionProtocolEventCollection = collectionProtocol
					.getCollectionProtocolEventCollection();
			if (!collectionProtocolEventCollection.isEmpty())
			{
				Iterator iterator = collectionProtocolEventCollection.iterator();
				while (iterator.hasNext())
				{
					CollectionProtocolEvent event = (CollectionProtocolEvent) iterator.next();
					String label = event.getCollectionPointLabel();
					if (cpLabelsSet.contains(label))
					{
						String arguments[] = null;
						arguments = new String[]{"Collection Protocol Event",
								"Collection point label"};
						String errMsg = new DefaultExceptionFormatter().getErrorMessage(
								"Err.ConstraintViolation", arguments);
						Logger.out.debug("Unique Constraint Violated: " + errMsg);
						//throw new DAOException(errMsg);
					}
					else
					{
						cpLabelsSet.add(label);
					}
				}
			}
		}
	}

	/**
	 *Added by Baljeet : To retrieve all collection Protocol list
	 * @return
	 */

	public List getCollectionProtocolList()
	{
		List cpList = new ArrayList();

		return cpList;
	}
	
	/**
	 * this function retrieves collection protocol and all nested child objects and 
	 * populates bean objects.
	 * @param id id of CP
	 * @return list with collection protocol object and hashmap of collection protocol events
	 * @throws DAOException 
	 */
	/**
	 * this function retrieves collection protocol and all nested child objects and 
	 * populates bean objects.
	 * @param id id of CP
	 * @return list with collection protocol object and hashmap of collection protocol events
	 * @throws DAOException 
	 * TO DO: Move to CollectionProtocolUtil
	 */
	public List retrieveCP(Long id) throws DAOException
	{

		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			dao.openSession(null);

			Object object = dao.retrieve(CollectionProtocol.class.getName(), id);
			if (object == null)
			{
				throw new DAOException("Cannot retrieve Collection protocol, incorrect id "
						+ id);
			}
			CollectionProtocol collectionProtocol = (CollectionProtocol) object;
			CollectionProtocolBean collectionProtocolBean = CollectionProtocolUtil
					.getCollectionProtocolBean(collectionProtocol);
			Collection collectionProtocolEventColl = collectionProtocol
					.getCollectionProtocolEventCollection();
			List cpEventList = new LinkedList(collectionProtocolEventColl);
			CollectionProtocolUtil.getSortedCPEventList(cpEventList);
			LinkedHashMap<String, CollectionProtocolEventBean> eventMap = CollectionProtocolUtil
					.getCollectionProtocolEventMap(cpEventList,dao);

			List cpList = new ArrayList<Object>();
			cpList.add(collectionProtocolBean);
			cpList.add(eventMap);

			return cpList;
		}
		
		catch (DAOException daoExp)
		{
			throw daoExp;
		}
		finally
		{
			dao.commit();
			dao.closeSession();
		}
	}
	public List<CollectionProtocol> retrieve(String className, String colName, Object colValue)
			throws DAOException
	{

		List<CollectionProtocol> cpList = null;
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			dao.openSession(null);
			cpList = dao.retrieve(className, colName, colValue);
			if (cpList == null || cpList.isEmpty())
			{
				throw new DAOException("Cannot retrieve Collection protocol incorrect parameter "
						+ colName + " = " + colValue);
			}

			Iterator<CollectionProtocol> iterator = cpList.iterator();
			while (iterator.hasNext())
			{
				loadCP(iterator.next());
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
		return cpList;
	}
	
	
	public CollectionProtocol retrieve(String className, Long id)
	throws DAOException
	{
		CollectionProtocol collectionProtocol = null;
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			dao.openSession(null);
			Object object = dao.retrieve(className, id);
			if (object == null)
			{
				throw new DAOException("Cannot retrieve Collection protocol incorrect id "
						+ id);
			}

			collectionProtocol = (CollectionProtocol)object; 
			loadCP(collectionProtocol);
			
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
		
		return collectionProtocol;

	}
	
	
	/**
	 * populate the CP with associations which otherwise will be loaded lazily
	 * 
	 * @param collectionProtocol
	 */
	private void loadCP(CollectionProtocol collectionProtocol)
	{
		User user = collectionProtocol.getPrincipalInvestigator();
		Long id = user.getCsmUserId();
		Collection<CollectionProtocolEvent> collectionEventCollection = collectionProtocol
				.getCollectionProtocolEventCollection();
		Iterator<CollectionProtocolEvent> cpIterator = collectionEventCollection.iterator();
		Collection collectinProtocolRegistration = collectionProtocol
				.getCollectionProtocolRegistrationCollection();
		while (cpIterator.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent = cpIterator.next();
			Collection specimenCollection = collectionProtocolEvent.getSpecimenRequirementCollection();
			Iterator<SpecimenRequirement> specimenIterator = specimenCollection.iterator();
			while (specimenIterator.hasNext())
			{
				specimenIterator.next();
			}
		}
		collectionProtocol.getCollectionProtocolRegistrationCollection();
	
	}

	/**
	 * Custom method for Collection Protocol case
	 * @param dao
	 * @param domainObject
	 * @param sessionDataBean
	 * @return
	 */
	private String getObjectId(AbstractDAO dao, Object domainObject, SessionDataBean sessionDataBean) 
	{
		User user = null;
		
		try 
		{
			user = (User) dao.retrieve(User.class.getName(), sessionDataBean.getUserId());
		} 
		catch (DAOException e) 
		{
			Logger.out.debug(e.getMessage(), e);
		}
		
			Collection<Site> siteCollection = user.getSiteCollection();

			StringBuffer sb = new StringBuffer();
			boolean hasProtocolAdministrationPrivilege = false;
			
			if (siteCollection != null && !siteCollection.isEmpty())
			{
				sb.append(Constants.SITE_CLASS_NAME);
				for (Site site : siteCollection)
				{
					if (site.getId()!=null)
					{
						sb.append(Constants.UNDERSCORE).append(site.getId());
						hasProtocolAdministrationPrivilege = true;
					}
				}
			}
			
			if(hasProtocolAdministrationPrivilege)
			{
				return sb.toString();
			}
				
		return null;
	}
	
	/**
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */ 
	protected String getPrivilegeKey(Object domainObject)
    {
    	return Constants.ADD_EDIT_CP;
    }

	public String getShortTitle(Long cpId) throws DAOException 
	{
		String shortTitle = null;
		
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			dao.openSession(null);
			Object object = dao.retrieveAttribute(CollectionProtocol.class.getName(), cpId, Constants.shortTitle);
			
			if (object == null)
			{
				throw new DAOException("Cannot retrieve Short Title incorrect id "
						+ cpId);
			}

			shortTitle = (String) object; 			
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
		return shortTitle;
	}
	
	/**
	 * Over-ridden for the case of Site Admin user should be able to create
	 * Collection Protocol for Site to which he belongs
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.common.dao.AbstractDAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
	public boolean isAuthorized(AbstractDAO dao, Object domainObject, SessionDataBean sessionDataBean)  
	{
		boolean isAuthorized = false;
		
		if(sessionDataBean != null && sessionDataBean.isAdmin())
		{
			return true;
		}
		
		String privilegeName = getPrivilegeName(domainObject);
		String protectionElementName = getObjectId(dao, domainObject, sessionDataBean);
		PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(sessionDataBean.getUserName());
		
		if (protectionElementName != null)
		{
			String [] prArray = protectionElementName.split(Constants.UNDERSCORE);
			String baseObjectId = prArray[0];
			StringBuffer objId = new StringBuffer();
			
			for (int i = 1 ; i < prArray.length;i++)
			{
				objId.append(baseObjectId).append(Constants.UNDERSCORE).append(prArray[i]);
				isAuthorized = privilegeCache.hasPrivilege(objId.toString(),privilegeName);
				if (!isAuthorized)
				{
					break;
				}
			}	
		   
			return isAuthorized;
		} 
		else
		{
			return false;
		}
	}
	
	
	public Collection<Site> getRelatedSites(Long cpId) 
	{
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		CollectionProtocol cp = null;
		
		try 
		{
			dao.openSession(null);
			cp = (CollectionProtocol) dao.retrieve(CollectionProtocol.class.getName(), cpId);		
		} 
		catch (DAOException e) 
		{
			Logger.out.debug(e.getMessage(), e);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				Logger.out.error(e.getMessage(), e);
			}
		}
		
		if(cp == null)
		{
			return null;
		}
		
		return cp.getSiteCollection();
	}
}
