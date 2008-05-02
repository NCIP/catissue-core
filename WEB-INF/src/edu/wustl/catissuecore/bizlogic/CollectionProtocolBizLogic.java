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
import java.util.Set;
import java.util.Vector;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import edu.wustl.catissuecore.TaskTimeCalculater;

import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.ParticipantRegistrationCacheManager;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SecurityDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exceptionformatter.DefaultExceptionFormatter;
import edu.wustl.common.security.SecurityManager;
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
		TaskTimeCalculater cpInsert = TaskTimeCalculater.startTask
		("Complete Collection Protocol Insert", CollectionProtocolBizLogic.class);
		
		CollectionProtocol collectionProtocol = (CollectionProtocol) obj;
		checkStatus(dao, collectionProtocol.getPrincipalInvestigator(), "Principal Investigator");
		validateCollectionProtocol(dao, collectionProtocol, "Principal Investigator");
		insertCollectionProtocol(dao, sessionDataBean, collectionProtocol);
		TaskTimeCalculater.endTask(cpInsert);
	}

	private void insertCollectionProtocol(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocol collectionProtocol) throws DAOException, UserNotAuthorizedException
	{
		 
		setPrincipalInvestigator(dao, collectionProtocol);
		setCoordinatorCollection(dao, collectionProtocol);
		/**
		 * Patch Id : FutureSCG_6
		 * Description : Calling method to validate the CPE against uniqueness
		 */
		//isCollectionProtocolLabelUnique(collectionProtocol);
		System.out.println("ID = " + collectionProtocol.getId());
		dao.insert(collectionProtocol, sessionDataBean, true, true);
		
		TaskTimeCalculater eventInsert = TaskTimeCalculater.startTask
		("Insert all events in CP", CollectionProtocolBizLogic.class);	
		insertCPEvents(dao, sessionDataBean, collectionProtocol);
		//This method will insert Child CP Associated with parent CP.
		insertchildCollectionProtocol(dao, sessionDataBean, collectionProtocol);
		TaskTimeCalculater.endTask(eventInsert);
		HashSet<CollectionProtocol> protectionObjects = new HashSet<CollectionProtocol>();
		protectionObjects.add(collectionProtocol);

		authenticate(collectionProtocol, protectionObjects);
	}

	private void validateCollectionProtocol(DAO dao, CollectionProtocol collectionProtocol, String errorName) throws DAOException
	{
		Collection childCPCollection = collectionProtocol.getChildCollectionProtocolCollection();
		Iterator cpIterator = childCPCollection.iterator();
		while(cpIterator.hasNext())
		{
			CollectionProtocol cp = (CollectionProtocol)cpIterator.next();
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
		CollectionProtocol collectionProtocol) throws 
		DAOException, UserNotAuthorizedException {

		Iterator it = collectionProtocol.getCollectionProtocolEventCollection().iterator();
		NewSpecimenBizLogic  bizLogic = new NewSpecimenBizLogic();

		while (it.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) it.next();
			collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
			SpecimenCollectionRequirementGroup collectionRequirementGroup =
							collectionProtocolEvent.getRequiredCollectionSpecimenGroup();
			
			insertCollectionProtocolEvent(dao, sessionDataBean, collectionProtocolEvent,
					collectionRequirementGroup);
			
			insertSpecimens(bizLogic, dao,  collectionRequirementGroup,
					 sessionDataBean);
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
			CollectionProtocol collectionProtocol) throws 
			DAOException, UserNotAuthorizedException 
	{
		
		Collection childCPCollection = collectionProtocol.getChildCollectionProtocolCollection();
		Iterator cpIterator = childCPCollection.iterator();
		while(cpIterator.hasNext())
		{
			CollectionProtocol cp = (CollectionProtocol)cpIterator.next();
			cp.setParentCollectionProtocol(collectionProtocol);
			insertCollectionProtocol(dao, sessionDataBean,cp);
		}

	}

	
	private Set getProtectionObjects(AbstractDomainObject obj)
	{
		Set protectionObjects = new HashSet();

		SpecimenCollectionRequirementGroup specimenCollectionGroup = (SpecimenCollectionRequirementGroup) obj;
		protectionObjects.add(specimenCollectionGroup);

		Logger.out.debug(protectionObjects.toString());
		return protectionObjects;
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
	private void insertSpecimens(NewSpecimenBizLogic  bizLogic, DAO dao,
			SpecimenCollectionRequirementGroup collectionRequirementGroup,
			 SessionDataBean sessionDataBean) throws DAOException,
			UserNotAuthorizedException {
		TaskTimeCalculater specimenInsert = TaskTimeCalculater.startTask
		("Insert specimen for CP", CollectionProtocolBizLogic.class);
		Collection specimenCollection = collectionRequirementGroup.getSpecimenCollection();
		Iterator<Specimen> specIter = specimenCollection.iterator();
		
		//Abhishek Mehta : Performance related Changes
		
		Collection specimenMap = new LinkedHashSet();

		while(specIter.hasNext())
		{
			Specimen specimen = specIter.next();									
		
			specimen.setSpecimenCollectionGroup(collectionRequirementGroup);
			if (specimen.getParentSpecimen() != null)
			{
				addToParentSpecimen(specimen);				
			}
			else
			{
				specimenMap.add(specimen);
			}
		}
		bizLogic.setCpbased(true);
		bizLogic.insert(specimenMap, dao, sessionDataBean);
		TaskTimeCalculater.endTask(specimenInsert);
	}

	/**
	 * This function adds specimen object to its parent's childrenCollection
	 * if not already added.
	 * 
	 * @param specimen The object to be added to it's parent childrenCollection
	 */
	private void addToParentSpecimen(Specimen specimen) {
		Collection childrenCollection = 
			specimen.getParentSpecimen().getChildrenSpecimen();
		
		if (childrenCollection == null)
		{
			childrenCollection = new HashSet();
		}
		if (!childrenCollection.contains(specimen) )
		{
			childrenCollection.add(specimen);
		}
	}


	private void authenticate(CollectionProtocol collectionProtocol,
			HashSet protectionObjects) throws DAOException {
		
		TaskTimeCalculater cpAuth = TaskTimeCalculater.startTask
		("CP insert Authenticatge", CollectionProtocolBizLogic.class);
		try
		{

			
			SecurityManager.getInstance(this.getClass()).insertAuthorizationData(
					getAuthorizationData(collectionProtocol), protectionObjects,
					getDynamicGroups(collectionProtocol));
			
			String objectId = collectionProtocol.getObjectId();
			edu.wustl.common.util.Utility.addObjectToPrivilegeCaches(objectId);
			
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		} finally{
			TaskTimeCalculater.endTask(cpAuth);	
		}
		
	}


	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		System.out.println("PostInsert called");
		CollectionProtocol collectionProtocol = (CollectionProtocol) obj;
		ParticipantRegistrationCacheManager participantRegistrationCacheManager = new ParticipantRegistrationCacheManager();
		participantRegistrationCacheManager.addNewCP(collectionProtocol.getId(),collectionProtocol.getTitle(),collectionProtocol.getShortTitle());

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
		CollectionProtocol collectionProtocol = (CollectionProtocol) obj;
		/**
		 * Patch Id : FutureSCG_7
		 * Description : Calling method to validate the CPE against uniqueness
		 */
		
		isCollectionProtocolLabelUnique(collectionProtocol);
		CollectionProtocol collectionProtocolOld = getOldCollectionProtocol(collectionProtocol);
		int consentCount = collectionProtocol.getConsentTierCollection().size();
		int consentCountOld = collectionProtocolOld.getConsentTierCollection().size();
		if(collectionProtocolOld.getCollectionProtocolRegistrationCollection().size()>0&&collectionProtocol.getActivityStatus().equals(Constants.ACTIVITY_STATUS_ACTIVE)&& consentCount==consentCountOld)
		{
			throw new DAOException("Unable to update, Only Consents can be added under this Collection protocol");
		}
		if (!collectionProtocol.getPrincipalInvestigator().getId().equals(collectionProtocolOld.getPrincipalInvestigator().getId()))
		{
			checkStatus(dao, collectionProtocol.getPrincipalInvestigator(),"Principal Investigator");
		}

		checkCoordinatorStatus(dao, collectionProtocol, collectionProtocolOld);

		checkForChangedStatus(collectionProtocol, collectionProtocolOld);
		

		Collection oldCollectionProtocolEventCollection = collectionProtocolOld
		.getCollectionProtocolEventCollection();
		Logger.out
		.debug("collectionProtocol.getCollectionProtocolEventCollection Size................ : "
				+ collectionProtocol.getCollectionProtocolEventCollection().size());

		updateCollectionProtocolEvents(dao, sessionDataBean,
				collectionProtocol, oldCollectionProtocolEventCollection);

		dao.update(collectionProtocol, sessionDataBean, true, true, false);

		//Audit of Collection Protocol.
		dao.audit(obj, oldObj, sessionDataBean, true);

		disableRelatedObjects(dao, collectionProtocol);

		try
		{
			updatePIAndCoordinatorGroup(dao, collectionProtocolOld, true);

			Long csmUserId = getCSMUserId(dao, collectionProtocol.getPrincipalInvestigator());
			if (csmUserId != null)
			{
				collectionProtocol.getPrincipalInvestigator().setCsmUserId(csmUserId);
				Logger.out.debug("PI ....."
						+ collectionProtocol.getPrincipalInvestigator().getCsmUserId());
				updatePIAndCoordinatorGroup(dao, collectionProtocol, false);
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
	private CollectionProtocol getOldCollectionProtocol(
			CollectionProtocol collectionProtocol) throws DAOException {
		CollectionProtocol collectionProtocolOld;
		try{
			Session sessionClean = DBUtil.getCleanSession();
			collectionProtocolOld =(CollectionProtocol) sessionClean.load(CollectionProtocol.class, collectionProtocol.getId());
		}catch(Exception e){
			throw new DAOException (e.getMessage(),e);
		}
		return collectionProtocolOld;
	}


	/**
	 * @param dao
	 * @param collectionProtocol
	 * @param collectionProtocolOld
	 * @throws DAOException
	 */
	private void checkCoordinatorStatus(DAO dao,
			CollectionProtocol collectionProtocol,
			CollectionProtocol collectionProtocolOld) throws DAOException {
		Iterator it = collectionProtocol.getCoordinatorCollection().iterator();
		while (it.hasNext())
		{
			User coordinator = (User) it.next();
			if (!coordinator.getId().equals(collectionProtocol.getPrincipalInvestigator().getId()))
			{
				if (!hasCoordinator(coordinator, collectionProtocolOld))
					checkStatus(dao, coordinator, "Coordinator");
			}
			else
				it.remove();
		}
	}


	/**
	 * @param dao
	 * @param collectionProtocol
	 * @throws DAOException
	 */
	private void disableRelatedObjects(DAO dao,
			CollectionProtocol collectionProtocol) throws DAOException {
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
	 * @param dao
	 * @param sessionDataBean
	 * @param collectionProtocol
	 * @param oldCollectionProtocolEventCollection
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 */
	private void updateCollectionProtocolEvents(DAO dao, SessionDataBean sessionDataBean, CollectionProtocol collectionProtocol,
			Collection oldCollectionProtocolEventCollection)
			throws DAOException, UserNotAuthorizedException 
	{
		Iterator it =  collectionProtocol.getCollectionProtocolEventCollection().iterator();
		while (it.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) it.next();
			Logger.out.debug("CollectionProtocolEvent Id ............... : "
					+ collectionProtocolEvent.getId());
			collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
			SpecimenCollectionRequirementGroup collectionRequirementGroup;
			CollectionProtocolEvent oldCollectionProtocolEvent = null;

			if (collectionProtocolEvent.getId()== null || collectionProtocolEvent.getId()<=0)
			{
				collectionRequirementGroup = collectionProtocolEvent.getRequiredCollectionSpecimenGroup();
				insertCollectionProtocolEvent(dao, sessionDataBean, collectionProtocolEvent,
						collectionRequirementGroup);
			}
			else
			{
				collectionRequirementGroup = collectionProtocolEvent.getRequiredCollectionSpecimenGroup();
				dao.update(collectionRequirementGroup, sessionDataBean, false, false, false);
				dao.update(collectionProtocolEvent, sessionDataBean, true, false, false);
				oldCollectionProtocolEvent = (CollectionProtocolEvent) getCorrespondingOldObject(
						oldCollectionProtocolEventCollection, collectionProtocolEvent.getId());
				dao.audit(collectionProtocolEvent, oldCollectionProtocolEvent, sessionDataBean, true);
				
			}
			
			//Audit of collectionProtocolEvent
			updateSpecimens(dao, sessionDataBean, collectionRequirementGroup,
					oldCollectionProtocolEvent);
		}
	}


	private void insertCollectionProtocolEvent(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolEvent collectionProtocolEvent,
			SpecimenCollectionRequirementGroup collectionRequirementGroup) throws DAOException,
			UserNotAuthorizedException
	{
		dao.insert(collectionProtocolEvent, sessionDataBean, true, false);
		dao.insert(collectionRequirementGroup, sessionDataBean, true, false);
		TaskTimeCalculater eventAuth = TaskTimeCalculater.startTask
		("CP: Specimen collection req. group insert Authenticatge", CollectionProtocolBizLogic.class);

	}


	/**
	 * @param dao
	 * @param sessionDataBean
	 * @param collectionRequirementGroup
	 * @param oldCollectionProtocolEvent
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 */
	private void updateSpecimens(DAO dao, SessionDataBean sessionDataBean,
			SpecimenCollectionRequirementGroup collectionRequirementGroup,
			CollectionProtocolEvent oldCollectionProtocolEvent)
			throws DAOException, UserNotAuthorizedException {
		Iterator srIt = collectionRequirementGroup.getSpecimenCollection().iterator();
		NewSpecimenBizLogic specimenBizLogic = new NewSpecimenBizLogic ();
		Collection oldSpecimenCollection = null;
		
		if (oldCollectionProtocolEvent != null)
		{
			oldSpecimenCollection = oldCollectionProtocolEvent
												.getRequiredCollectionSpecimenGroup()
													.getSpecimenCollection();
		}
		while (srIt.hasNext())
		{
			Specimen specimen = (Specimen) srIt.next();
			if(specimen.getSpecimenCollectionGroup() == null)
			{
				specimen.setSpecimenCollectionGroup(collectionRequirementGroup);
			}
			specimen.setSpecimenCollectionGroup(collectionRequirementGroup);
			Logger.out.debug("specimenRequirement " + specimen);
			if (specimen.getId() ==null || specimen.getId()<=0)
			{
				specimenBizLogic.insert(specimen, dao, sessionDataBean);
			}
			else
			{
				Specimen oldSpecimen =(Specimen) getCorrespondingOldObject(oldSpecimenCollection, specimen.getId()); 
				dao.update(specimen, sessionDataBean, true, false, false);
				dao.audit(specimen, oldSpecimen, sessionDataBean, true);

			}						
		}
	}


	private void updatePIAndCoordinatorGroup(DAO dao, CollectionProtocol collectionProtocol,
			boolean operation) throws SMException, DAOException
			{
		Long principalInvestigatorId = collectionProtocol.getPrincipalInvestigator().getCsmUserId();
		Logger.out.debug("principalInvestigatorId.........................."
				+ principalInvestigatorId);
		String userGroupName = Constants.getCollectionProtocolPIGroupName(collectionProtocol
				.getId());
		Logger.out.debug("userGroupName.........................." + userGroupName);
		if (operation)
		{
			SecurityManager.getInstance(CollectionProtocolBizLogic.class).removeUserFromGroup(
					userGroupName, principalInvestigatorId.toString());
		}
		else
		{
			SecurityManager.getInstance(CollectionProtocolBizLogic.class).assignUserToGroup(
					userGroupName, principalInvestigatorId.toString());
		}

		userGroupName = Constants.getCollectionProtocolCoordinatorGroupName(collectionProtocol
				.getId());

		UserBizLogic userBizLogic = (UserBizLogic) BizLogicFactory.getInstance().getBizLogic(
				Constants.USER_FORM_ID);
		Iterator iterator = collectionProtocol.getCoordinatorCollection().iterator();
		while (iterator.hasNext())
		{
			User user = (User) iterator.next();
			if (operation)
			{
				SecurityManager.getInstance(CollectionProtocolBizLogic.class).removeUserFromGroup(
						userGroupName, user.getCsmUserId().toString());
			}
			else
			{
				Long csmUserId = getCSMUserId(dao, user);
				if (csmUserId != null)
				{
					Logger.out.debug("Co-ord ....." + csmUserId);
					SecurityManager.getInstance(CollectionProtocolBizLogic.class)
					.assignUserToGroup(userGroupName, csmUserId.toString());
				}
			}
		}
			}

	/**
	 * @param collectionProtocol
	 * @return
	 * @throws DAOException
	 */
	private Long getCSMUserId(DAO dao, User user) throws DAOException
	{
		String[] selectColumnNames = {Constants.CSM_USER_ID};
		String[] whereColumnNames = {Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnCondition = {"="};
		String[] whereColumnValues = {user.getId().toString()};
		List csmUserIdList = dao.retrieve(User.class.getName(), selectColumnNames,
				whereColumnNames, whereColumnCondition, whereColumnValues,
				Constants.AND_JOIN_CONDITION);
		Logger.out.debug("csmUserIdList##########################Size........."
				+ csmUserIdList.size());

		if (csmUserIdList.isEmpty() == false)
		{
			Long csmUserId = (Long) csmUserIdList.get(0);

			return csmUserId;
		}

		return null;
	}

	/**
	 * This method returns collection of UserGroupRoleProtectionGroup objects that speciefies the 
	 * user group protection group linkage through a role. It also specifies the groups the protection  
	 * elements returned by this class should be added to.
	 * @return
	 */
	private Vector getAuthorizationData(AbstractDomainObject obj) throws SMException
	{
		Logger.out.debug("--------------- In here ---------------");

		Vector authorizationData = new Vector();
		Set group = new HashSet();

		CollectionProtocol collectionProtocol = (CollectionProtocol) obj;

		String userId = String
		.valueOf(collectionProtocol.getPrincipalInvestigator().getCsmUserId());
		Logger.out.debug(" PI ID: " + userId);
		gov.nih.nci.security.authorization.domainobjects.User user = SecurityManager.getInstance(
				this.getClass()).getUserById(userId);
		Logger.out.debug(" PI: " + user.getLoginName());
		group.add(user);

		// Protection group of PI
		String protectionGroupName = new String(Constants
				.getCollectionProtocolPGName(collectionProtocol.getId()));
		SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
		userGroupRoleProtectionGroupBean.setUser(userId);
		userGroupRoleProtectionGroupBean.setRoleName(PI);
		userGroupRoleProtectionGroupBean.setGroupName(Constants
				.getCollectionProtocolPIGroupName(collectionProtocol.getId()));
		userGroupRoleProtectionGroupBean.setProtectionGroupName(protectionGroupName);
		userGroupRoleProtectionGroupBean.setGroup(group);
		authorizationData.add(userGroupRoleProtectionGroupBean);

		// Protection group of coordinators
		Collection coordinators = collectionProtocol.getCoordinatorCollection();
		group = new HashSet();
		for (Iterator it = coordinators.iterator(); it.hasNext();)
		{
			User aUser = (User) it.next();
			userId = String.valueOf(aUser.getCsmUserId());
			Logger.out.debug(" COORDINATOR ID: " + userId);
			user = SecurityManager.getInstance(this.getClass()).getUserById(userId);
			Logger.out.debug(" COORDINATOR: " + user.getLoginName());
			group.add(user);
		}

		protectionGroupName = new String(Constants.getCollectionProtocolPGName(collectionProtocol
				.getId()));
		userGroupRoleProtectionGroupBean = new SecurityDataBean();
		userGroupRoleProtectionGroupBean.setUser(userId);
		userGroupRoleProtectionGroupBean.setRoleName(COORDINATOR);
		userGroupRoleProtectionGroupBean.setGroupName(Constants
				.getCollectionProtocolCoordinatorGroupName(collectionProtocol.getId()));
		userGroupRoleProtectionGroupBean.setProtectionGroupName(protectionGroupName);
		userGroupRoleProtectionGroupBean.setGroup(group);
		authorizationData.add(userGroupRoleProtectionGroupBean);

		Logger.out.debug(authorizationData.toString());
		return authorizationData;
	}

	//    public Set getProtectionObjects(AbstractDomainObject obj)
	//    {
	//        Set protectionObjects = new HashSet();
	//        CollectionProtocolEvent collectionProtocolEvent;
	//        SpecimenRequirement specimenRequirement;
	//        
	//        CollectionProtocol collectionProtocol = (CollectionProtocol)obj;
	//        protectionObjects.add(collectionProtocol);
	//        Collection collectionProtocolEventCollection = collectionProtocol.getCollectionProtocolEventCollection();
	//        if(collectionProtocolEventCollection != null)
	//        {
	//           for(Iterator it = collectionProtocolEventCollection.iterator(); it.hasNext();)
	//           {
	//               collectionProtocolEvent = (CollectionProtocolEvent) it.next();
	//               if(collectionProtocolEvent !=null)
	//               {
	//                   protectionObjects.add(collectionProtocolEvent);
	//                   for(Iterator it2=collectionProtocolEvent.getSpecimenRequirementCollection().iterator();it2.hasNext();)
	//                   {
	//                       protectionObjects.add(it2.next());
	//                   }
	//               }
	//               
	//           }
	//        }
	//        Logger.out.debug(protectionObjects.toString());
	//        return protectionObjects;
	//    }

	private String[] getDynamicGroups(AbstractDomainObject obj)
	{
		String[] dynamicGroups = null;
		return dynamicGroups;
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
		}
	}

	//This method sets the User Collection.
	private void setCoordinatorCollection(DAO dao, CollectionProtocol collectionProtocol)
	throws DAOException
	{
		Long piID = collectionProtocol.getPrincipalInvestigator().getId();
		Logger.out.debug("Coordinator Size " + collectionProtocol.getCoordinatorCollection().size());
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
						throw new DAOException("Coordinator " + coordinator.getLastName() +" "+ coordinator.getFirstName()+" " + ApplicationProperties.getValue("error.object.closed"));
					}

					coordinatorColl.add(coordinator);
					coordinator.getCollectionProtocolCollection().add(collectionProtocol);
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

	private boolean hasCoordinator(User coordinator, CollectionProtocol collectionProtocol)
	{
		Iterator it = collectionProtocol.getCoordinatorCollection().iterator();
		while (it.hasNext())
		{
			User coordinatorOld = (User) it.next();
			if (coordinator.getId().equals(coordinatorOld.getId()))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Executes hql Query and returns the results.
	 * @param hql String hql
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	private List executeHqlQuery(DAO dao,String hql) throws DAOException, ClassNotFoundException
	{
		List list = dao.executeQuery(hql, null, false, null);
		return list;
	}
	
	protected boolean isSpecimenExists(DAO dao,Long cpId) throws DAOException, ClassNotFoundException
	{
		
		String hql = " select" +
        " elements(scg.specimenCollection) " +
        "from " +
        " edu.wustl.catissuecore.domain.CollectionProtocol as cp" +
        ", edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr" +
        ", edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg" +
        ", edu.wustl.catissuecore.domain.Specimen as s" +
        " where cp.id = "+cpId+"  and"+
        " cp.id = cpr.collectionProtocol.id and" +
        " cpr.id = scg.collectionProtocolRegistration.id and" +
        " scg.id = s.specimenCollectionGroup.id and " +
        " s.activityStatus = '"+Constants.ACTIVITY_STATUS_ACTIVE+"'";

		
		List specimenList=(List)executeHqlQuery(dao,hql);
		if((specimenList!=null) && (specimenList).size()>0)
		{
			return true;
		}	
		else
		{
			return false;
		}
		
	
	}
	
	
	
	//Added by Ashish
	/*	Map values = null;
	Map innerLoopValues = null;
	int outerCounter = 1;
	long protocolCoordinatorIds[];
	boolean aliqoutInSameContainer = false;

	public void setAllValues(Object obj)
	{

		CollectionProtocol cProtocol = (CollectionProtocol) obj;
		Collection protocolEventCollection = cProtocol.getCollectionProtocolEventCollection();

		if (protocolEventCollection != null)
		{
			List eventList = new ArrayList(protocolEventCollection);
			Collections.sort(eventList);
			protocolEventCollection = eventList;

			values = new HashMap();
			innerLoopValues = new HashMap();

			int i = 1;
			Iterator it = protocolEventCollection.iterator();
			while (it.hasNext())
			{
				CollectionProtocolEvent cpEvent = (CollectionProtocolEvent) it.next();

				String keyClinicalStatus = "CollectionProtocolEvent:" + i + "_clinicalStatus";
				String keyStudyCalendarEventPoint = "CollectionProtocolEvent:" + i
						+ "_studyCalendarEventPoint";
				String keyCPEId = "CollectionProtocolEvent:" + i + "_id";

				values.put(keyClinicalStatus, Utility.toString(cpEvent.getClinicalStatus()));
				values.put(keyStudyCalendarEventPoint, Utility.toString(cpEvent
						.getStudyCalendarEventPoint()));
				values.put(keyCPEId, Utility.toString(cpEvent.getId()));
				Logger.out.debug("In Form keyCPEId..............." + values.get(keyCPEId));
				Collection specimenRequirementCollection = cpEvent
						.getSpecimenRequirementCollection();

				populateSpecimenRequirement(specimenRequirementCollection, i);

				i++;
			}

			outerCounter = protocolEventCollection.size();
		}

		//At least one outer row should be displayed in ADD MORE therefore
		if (outerCounter == 0)
			outerCounter = 1;

		//Populating the user-id array
		Collection userCollection = cProtocol.getUserCollection();

		if (userCollection != null)
		{
			protocolCoordinatorIds = new long[userCollection.size()];
			int i = 0;

			Iterator it = userCollection.iterator();
			while (it.hasNext())
			{
				User user = (User) it.next();
				protocolCoordinatorIds[i] = user.getId().longValue();
				i++;
			}
		}
		if (cProtocol.getAliqoutInSameContainer() != null)
		{
			aliqoutInSameContainer = cProtocol.getAliqoutInSameContainer().booleanValue();
		}
	}

	private void populateSpecimenRequirement(Collection specimenRequirementCollection, int counter)
	{
		int innerCounter = 0;
		if (specimenRequirementCollection != null)
		{
			int i = 1;

			Iterator iterator = specimenRequirementCollection.iterator();
			while (iterator.hasNext())
			{
				SpecimenRequirement specimenRequirement = (SpecimenRequirement) iterator.next();
				String key[] = {
						"CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + i
								+ "_specimenClass",
						"CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + i
								+ "_unitspan",
						"CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + i
								+ "_specimenType",
						"CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + i
								+ "_tissueSite",
						"CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + i
								+ "_pathologyStatus",
						"CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + i
								+ "_quantity_value",
						"CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + i + "_id"};
				this.values = setSpecimenRequirement(key, specimenRequirement);
				i++;
			}
			innerCounter = specimenRequirementCollection.size();
		}

		//At least one inner row should be displayed in ADD MORE therefore
		if (innerCounter == 0)
			innerCounter = 1;

		String innerCounterKey = String.valueOf(counter);
		innerLoopValues.put(innerCounterKey, String.valueOf(innerCounter));
	}
	
	//END
	
	
	
	
	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{

		//Added by Ashish
		//setAllValues(obj);
		//END
		
		CollectionProtocol protocol = (CollectionProtocol) obj; 
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
		String message="";
		if (protocol == null)
		{			
			throw new DAOException(ApplicationProperties.getValue("domain.object.null.err.msg","Collection Protocol"));	
		}	

		if (validator.isEmpty(protocol.getTitle()))
		{
			message = ApplicationProperties.getValue("collectionprotocol.protocoltitle");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}

		if (validator.isEmpty(protocol.getShortTitle()))
		{
			message = ApplicationProperties.getValue("collectionprotocol.shorttitle");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}

//		if (validator.isEmpty(protocol.getIrbIdentifier()))
//		{
//		message = ApplicationProperties.getValue("collectionprotocol.irbid");
//		throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
//		}		

		if(protocol.getStartDate() != null)
		{
			String errorKey = validator.validateDate(protocol.getStartDate().toString() ,false);
//			if(errorKey.trim().length() >0  )		
//			{
//			message = ApplicationProperties.getValue("collectionprotocol.startdate");
//			throw new DAOException(ApplicationProperties.getValue(errorKey,message));	
//			}
		}
		else
		{
			message = ApplicationProperties.getValue("collectionprotocol.startdate");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}
		if(protocol.getEndDate() != null)
		{
			String errorKey = validator.validateDate(protocol.getEndDate().toString() ,false);
//			if(errorKey.trim().length() >0  )		
//			{
//			message = ApplicationProperties.getValue("collectionprotocol.enddate");
//			throw new DAOException(ApplicationProperties.getValue(errorKey,message));	
//			}
		}

		if(protocol.getPrincipalInvestigator().getId() == -1)
		{
			//message = ApplicationProperties.getValue("collectionprotocol.specimenstatus");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required","Principal Investigator"));	
		}

		Collection protocolCoordinatorCollection = protocol.getCoordinatorCollection();
//		if(protocolCoordinatorCollection == null || protocolCoordinatorCollection.isEmpty())
//		{
//		//message = ApplicationProperties.getValue("collectionprotocol.specimenstatus");
//		throw new DAOException(ApplicationProperties.getValue("errors.one.item.required","Protocol Coordinator"));
//		}
		if(protocolCoordinatorCollection != null && !protocolCoordinatorCollection.isEmpty())
		{
			Iterator protocolCoordinatorItr = protocolCoordinatorCollection.iterator();
			while(protocolCoordinatorItr.hasNext())
			{
				User protocolCoordinator = (User) protocolCoordinatorItr.next();
				if(protocolCoordinator.getId() == protocol.getPrincipalInvestigator().getId())
				{
					throw new DAOException(ApplicationProperties.getValue("errors.pi.coordinator.same"));
				}
			}
		}		

		if (eventCollection != null && eventCollection.size() != 0)
		{
			CDEManager manager = CDEManager.getCDEManager();

			if (manager == null){
				throw new DAOException("Failed to get CDE manager object. " +
						"CDE Manager is not yet initialized.");
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
						message = ApplicationProperties.getValue("collectionprotocol.studycalendartitle");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));
					}
					// Added by Vijay for API testAddCollectionProtocolWithWrongCollectionPointLabel
					if(validator.isEmpty(event.getCollectionPointLabel()))
					{
						message = ApplicationProperties.getValue("collectionprotocol.collectionpointlabel");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));
					}

					SpecimenCollectionRequirementGroup collectionRequirementGroup =
						event.getRequiredCollectionSpecimenGroup();
					
					Collection<Specimen> reqCollection = collectionRequirementGroup.getSpecimenCollection();

					if (reqCollection != null && reqCollection.size() != 0)
					{
						Iterator<Specimen> reqIterator = reqCollection.iterator();

						while (reqIterator.hasNext())
						{							

							Specimen specimen = (Specimen) reqIterator
							.next();
							if (specimen == null)
							{
								throw new DAOException(ApplicationProperties
										.getValue("protocol.spReqEmpty.errMsg"));
							}

							/**
							 * Start: Change for API Search   --- Jitendra 06/10/2006
							 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
							 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
							 * So we removed default class level initialization on domain object and are initializing in method
							 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
							 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
							 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
							 */
							ApiSearchUtil.setSpecimenDefault(specimen);
							//End:- Change for API Search 

							String specimenClass = specimen.getClassName();

							if (!Validator.isEnumeratedValue(specimenClassList, specimenClass))
							{
								throw new DAOException(ApplicationProperties
										.getValue("protocol.class.errMsg"));
							}

							if (!Validator.isEnumeratedValue(Utility
									.getSpecimenTypes(specimenClass), specimen
									.getType()))
							{
								throw new DAOException(ApplicationProperties
										.getValue("protocol.type.errMsg"));
							}
//TODO:Abhijit Checkout valid values for tisse site and Pathalogy status.
//								if (!Validator.isEnumeratedValue(tissueSiteList, specimen
//										.getTissueSite()))
//								{
//									throw new DAOException(ApplicationProperties
//											.getValue("protocol.tissueSite.errMsg"));
//								}
//
//								if (!Validator.isEnumeratedValue(pathologicalStatusList,
//										specimen.getActivityStatus()))
//								{
//									throw new DAOException(ApplicationProperties
//											.getValue("protocol.pathologyStatus.errMsg"));
//								}

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
		if(protocol.getActivityStatus()!=null && protocol.getActivityStatus().equalsIgnoreCase(Constants.DISABLED))
		{
			try {
				
				boolean isSpecimenExist=(boolean)isSpecimenExists(dao,(Long)protocol.getId());
				if(isSpecimenExist)
				{
					throw new DAOException(ApplicationProperties.getValue("collectionprotocol.specimen.exists"));
				}
		
			
			} catch (ClassNotFoundException e) {
					e.printStackTrace();
			}
		}

		return true;
	}

	public void postUpdate(DAO dao, Object currentObj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException,
	UserNotAuthorizedException 
	{
		CollectionProtocol collectionProtocol = (CollectionProtocol) currentObj;
		CollectionProtocol collectionProtocolOld = (CollectionProtocol) oldObj;
		ParticipantRegistrationCacheManager participantRegistrationCacheManager = new ParticipantRegistrationCacheManager();
		if(!collectionProtocol.getTitle().equals(collectionProtocolOld.getTitle()))
		{
			participantRegistrationCacheManager.updateCPTitle(collectionProtocol.getId(),collectionProtocol.getTitle());
		}
		
		if(!collectionProtocol.getShortTitle().equals(collectionProtocolOld.getShortTitle()))
		{
			participantRegistrationCacheManager.updateCPShortTitle(collectionProtocol.getId(),collectionProtocol.getShortTitle());
		}

		if(collectionProtocol.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			participantRegistrationCacheManager.removeCP(collectionProtocol.getId());
		}


	}	  

//mandar : 31-Jan-07 ----------- consents tracking
	private void verifyConsentsWaived(CollectionProtocol collectionProtocol)
	{
		//check for consentswaived
		if(collectionProtocol.getConsentsWaived() == null )
		{
			collectionProtocol.setConsentsWaived(new Boolean(false) );
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
	protected void isCollectionProtocolLabelUnique(CollectionProtocol collectionProtocol ) throws DAOException
	{
		if (collectionProtocol != null)
		{
			HashSet cpLabelsSet = new HashSet();
			Collection collectionProtocolEventCollection = collectionProtocol.getCollectionProtocolEventCollection();
			if(!collectionProtocolEventCollection.isEmpty())
			{
				Iterator iterator = collectionProtocolEventCollection.iterator();
				while(iterator.hasNext())
				{
					CollectionProtocolEvent event = (CollectionProtocolEvent)iterator.next();
					String label = event.getCollectionPointLabel();
					if(cpLabelsSet.contains(label))
					{ 
						String arguments[] = null;
						arguments = new String[]{"Collection Protocol Event", "Collection point label"};
						String errMsg = new DefaultExceptionFormatter().getErrorMessage("Err.ConstraintViolation", arguments);
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
	 * @param className 
	 * @param colName Column name
	 * @param colValue
	 * @param CollectionProtocol 
	 * @return list with collection protocol object and hashmap of collection protocol events
	 * @throws DAOException 
	 */
	public List retrieveCP(String className, String colName, Object colValue)
			throws DAOException {
		
		List cpList;
			if (!(CollectionProtocol.class.getName().equals(className)))
			{
				throw new DAOException("Cannot retrieve Collection protocol from class " + className);
			}

			AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
			try
			{
				dao.openSession(null);

				cpList = dao.retrieve(className, colName, colValue);
				if (cpList == null || cpList.isEmpty())
				{
					throw new DAOException("Cannot retrieve Collection protocol incorrect parameter " +
							 colName + " = " + colValue);
				}
				CollectionProtocol collectionProtocol = (CollectionProtocol) cpList.get(0);
				CollectionProtocolBean collectionProtocolBean = 
					CollectionProtocolUtil.getCollectionProtocolBean(collectionProtocol);
				Collection collectionProtocolEventColl = 
					collectionProtocol.getCollectionProtocolEventCollection();
				List cpEventList = new LinkedList(collectionProtocolEventColl);
				CollectionProtocolUtil.getSortedCPEventList(cpEventList);
				LinkedHashMap<String, CollectionProtocolEventBean> eventMap = 
						CollectionProtocolUtil.getCollectionProtocolEventMap(cpEventList);
				
				cpList = new ArrayList<Object>();
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
	throws DAOException {
		
		List<CollectionProtocol> cpList = null;
		
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			dao.openSession(null);

			cpList = dao.retrieve(className, colName, colValue);
			if (cpList == null || cpList.isEmpty())
			{
				throw new DAOException("Cannot retrieve Collection protocol incorrect parameter " +
						 colName + " = " + colValue);
			}
			Iterator<CollectionProtocol> iterator = cpList.iterator();
			while(iterator.hasNext())
			{
				CollectionProtocol collectionProtocol = iterator.next();
				User user = collectionProtocol.getPrincipalInvestigator();
				Long id = user.getCsmUserId();
				Collection<CollectionProtocolEvent> collectionEventCollection = 
					collectionProtocol.getCollectionProtocolEventCollection();
				Iterator<CollectionProtocolEvent> cpIterator = collectionEventCollection.iterator();
				Collection collectinProtocolRegistration = collectionProtocol.getCollectionProtocolRegistrationCollection();
				while (cpIterator.hasNext())
				{
					CollectionProtocolEvent collectionProtocolEvent =
						cpIterator.next();
					SpecimenCollectionRequirementGroup requirementGroup = 
						collectionProtocolEvent.getRequiredCollectionSpecimenGroup();
					Collection specimenCollection = requirementGroup.getSpecimenCollection();
					Iterator<Specimen> specimenIterator = specimenCollection.iterator();
					while(specimenIterator.hasNext())
					{
						specimenIterator.next();
					}
				}
			}
			
		}catch(DAOException exception)
		{
			throw exception;
		}finally
		{
			dao.commit();
			dao.closeSession();			
		}
		return cpList;
	}
	
}	
