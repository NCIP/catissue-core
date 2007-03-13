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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.ParticipantRegistrationCacheManager;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SecurityDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
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
		CollectionProtocol collectionProtocol = (CollectionProtocol) obj;

		checkStatus(dao, collectionProtocol.getPrincipalInvestigator(), "Principal Investigator");

		setPrincipalInvestigator(dao, collectionProtocol);
		setCoordinatorCollection(dao, collectionProtocol);
		verifyConsentsWaived(collectionProtocol);
		dao.insert(collectionProtocol, sessionDataBean, true, true);

		Iterator it = collectionProtocol.getCollectionProtocolEventCollection().iterator();
		while (it.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) it.next();
			collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
			dao.insert(collectionProtocolEvent, sessionDataBean, true, true);

			Iterator srIt = collectionProtocolEvent.getSpecimenRequirementCollection().iterator();
			while (srIt.hasNext())
			{
				SpecimenRequirement specimenRequirement = (SpecimenRequirement) srIt.next();
				specimenRequirement.getCollectionProtocolEventCollection().add(
						collectionProtocolEvent);
				dao.insert(specimenRequirement, sessionDataBean, true, true);
			}
		}

		HashSet protectionObjects = new HashSet();
		protectionObjects.add(collectionProtocol);

		try
		{
			SecurityManager.getInstance(this.getClass()).insertAuthorizationData(
					getAuthorizationData(collectionProtocol), protectionObjects,
					getDynamicGroups(collectionProtocol));
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}
	}

	
	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		CollectionProtocol collectionProtocol = (CollectionProtocol) obj;
		ParticipantRegistrationCacheManager participantRegistrationCacheManager = new ParticipantRegistrationCacheManager();
		participantRegistrationCacheManager.addNewCP(collectionProtocol.getId(),collectionProtocol.getTitle());
		
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
		CollectionProtocol collectionProtocolOld = (CollectionProtocol) oldObj;
		Logger.out.debug("PI OB*****************8" + collectionProtocol.getPrincipalInvestigator());
		Logger.out.debug("PI Identifier................."
				+ collectionProtocol.getPrincipalInvestigator().getId());
		Logger.out.debug("Email Address*****************8"
				+ collectionProtocol.getPrincipalInvestigator().getEmailAddress());
		Logger.out.debug("Principal Investigator*****************8"
				+ collectionProtocol.getPrincipalInvestigator().getCsmUserId());
		if (!collectionProtocol.getPrincipalInvestigator().getId().equals(
				collectionProtocolOld.getPrincipalInvestigator().getId()))
			checkStatus(dao, collectionProtocol.getPrincipalInvestigator(),
					"Principal Investigator");

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

		checkForChangedStatus(collectionProtocol, collectionProtocolOld);
		//consenttracking
		verifyConsentsWaived(collectionProtocol);
		dao.update(collectionProtocol, sessionDataBean, true, true, false);

		//Audit of Collection Protocol.
		dao.audit(obj, oldObj, sessionDataBean, true);

		Collection oldCollectionProtocolEventCollection = collectionProtocolOld
				.getCollectionProtocolEventCollection();
		Logger.out
				.debug("collectionProtocol.getCollectionProtocolEventCollection Size................ : "
						+ collectionProtocol.getCollectionProtocolEventCollection().size());

		it = collectionProtocol.getCollectionProtocolEventCollection().iterator();
		while (it.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) it.next();
			Logger.out.debug("CollectionProtocolEvent Id ............... : "
					+ collectionProtocolEvent.getId());
			collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
			dao.update(collectionProtocolEvent, sessionDataBean, true, true, false);

			//Audit of collectionProtocolEvent
			CollectionProtocolEvent oldCollectionProtocolEvent = (CollectionProtocolEvent) getCorrespondingOldObject(
					oldCollectionProtocolEventCollection, collectionProtocolEvent.getId());
			dao.audit(collectionProtocolEvent, oldCollectionProtocolEvent, sessionDataBean, true);

			Iterator srIt = collectionProtocolEvent.getSpecimenRequirementCollection().iterator();
			while (srIt.hasNext())
			{
				SpecimenRequirement specimenRequirement = (SpecimenRequirement) srIt.next();

				Logger.out.debug("specimenRequirement " + specimenRequirement);

				specimenRequirement.getCollectionProtocolEventCollection().add(
						collectionProtocolEvent);
				dao.update(specimenRequirement, sessionDataBean, true, true, false);
			}
		}

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

					checkStatus(dao, coordinator, "coordinator");

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
//			message = ApplicationProperties.getValue("collectionprotocol.irbid");
//			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
//		}		
		
		if(protocol.getStartDate() != null)
		{
			String errorKey = validator.validateDate(protocol.getStartDate().toString() ,false);
//			if(errorKey.trim().length() >0  )		
//			{
//				message = ApplicationProperties.getValue("collectionprotocol.startdate");
//				throw new DAOException(ApplicationProperties.getValue(errorKey,message));	
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
//				message = ApplicationProperties.getValue("collectionprotocol.enddate");
//				throw new DAOException(ApplicationProperties.getValue(errorKey,message));	
//			}
		}
		
		if(protocol.getPrincipalInvestigator() == null)
		{
			//message = ApplicationProperties.getValue("collectionprotocol.specimenstatus");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required","Principal Investigator"));	
		}
		
		Collection protocolCoordinatorCollection = protocol.getCoordinatorCollection();
//		if(protocolCoordinatorCollection == null || protocolCoordinatorCollection.isEmpty())
//		{
//			//message = ApplicationProperties.getValue("collectionprotocol.specimenstatus");
//			throw new DAOException(ApplicationProperties.getValue("errors.one.item.required","Protocol Coordinator"));
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
			List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(
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

					Collection reqCollection = event.getSpecimenRequirementCollection();

					if (reqCollection != null && reqCollection.size() != 0)
					{
						Iterator reqIterator = reqCollection.iterator();

						while (reqIterator.hasNext())
						{							
							
							SpecimenRequirement requirement = (SpecimenRequirement) reqIterator
									.next();

							if (requirement == null)
							{
								throw new DAOException(ApplicationProperties
										.getValue("protocol.spReqEmpty.errMsg"));
							}
							else
							{
								/**
								 * Start: Change for API Search   --- Jitendra 06/10/2006
								 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
								 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
								 * So we removed default class level initialization on domain object and are initializing in method
								 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
								 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
								 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
								 */
								ApiSearchUtil.setSpecimenRequirementDefault(requirement);
								//End:- Change for API Search 
								
								String specimenClass = requirement.getSpecimenClass();

								if (!Validator.isEnumeratedValue(specimenClassList, specimenClass))
								{
									throw new DAOException(ApplicationProperties
											.getValue("protocol.class.errMsg"));
								}

								if (!Validator.isEnumeratedValue(Utility
										.getSpecimenTypes(specimenClass), requirement
										.getSpecimenType()))
								{
									throw new DAOException(ApplicationProperties
											.getValue("protocol.type.errMsg"));
								}

								if (!Validator.isEnumeratedValue(tissueSiteList, requirement
										.getTissueSite()))
								{
									throw new DAOException(ApplicationProperties
											.getValue("protocol.tissueSite.errMsg"));
								}

								if (!Validator.isEnumeratedValue(pathologicalStatusList,
										requirement.getPathologyStatus()))
								{
									throw new DAOException(ApplicationProperties
											.getValue("protocol.pathologyStatus.errMsg"));
								}
							}
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
			throw new DAOException(ApplicationProperties
					.getValue("collectionProtocol.eventsEmpty.errMsg"));
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
}