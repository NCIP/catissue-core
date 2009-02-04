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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.ParticipantRegistrationCacheManager;
import edu.wustl.catissuecore.util.ParticipantRegistrationInfo;
import edu.wustl.catissuecore.util.WithdrawConsentUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exceptionformatter.DefaultExceptionFormatter;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * UserHDAO is used to add user information into the database using Hibernate.
 * @author kapil_kaveeshwar
 */
public class CollectionProtocolRegistrationBizLogic extends DefaultBizLogic
{
	/**
	 * Saves the user object in the database.
	 * @param obj The user object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;

		// check for closed Collection Protocol
		checkStatus(dao, collectionProtocolRegistration.getCollectionProtocol(), "Collection Protocol");

		// Check for closed Participant
		checkStatus(dao, collectionProtocolRegistration.getParticipant(), "Participant");
		checkUniqueConstraint(dao, collectionProtocolRegistration, null);
		registerParticipantAndProtocol(dao, collectionProtocolRegistration, sessionDataBean);

		dao.insert(collectionProtocolRegistration, sessionDataBean, true, true);

		try
		{
			SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, getProtectionObjects(collectionProtocolRegistration),
					getDynamicGroups(collectionProtocolRegistration));
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}
	}

	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;
		ParticipantRegistrationCacheManager participantRegCacheManager = new ParticipantRegistrationCacheManager();
		participantRegCacheManager.registerParticipant(collectionProtocolRegistration.getCollectionProtocol().getId(), collectionProtocolRegistration
				.getParticipant().getId(), collectionProtocolRegistration.getProtocolParticipantIdentifier());
		/*ParticipantCacheUtil.addParticipantRegInfo(collectionProtocolRegistration.getCollectionProtocol().getId(), collectionProtocolRegistration
		 .getCollectionProtocol().getTitle(), collectionProtocolRegistration.getParticipant().getId());*/

	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;

		CollectionProtocolRegistration oldCollectionProtocolRegistration = (CollectionProtocolRegistration) oldObj;

		// Check for different Collection Protocol
		if (!collectionProtocolRegistration.getCollectionProtocol().getId().equals(oldCollectionProtocolRegistration.getCollectionProtocol().getId()))
		{
			checkStatus(dao, collectionProtocolRegistration.getCollectionProtocol(), "Collection Protocol");
		}

		// -- Check for different Participants and closed participant
		// old and new values are not null
		if (collectionProtocolRegistration.getParticipant() != null && oldCollectionProtocolRegistration.getParticipant() != null
				&& collectionProtocolRegistration.getParticipant().getId() != null
				&& oldCollectionProtocolRegistration.getParticipant().getId() != null)
		{
			if (!collectionProtocolRegistration.getParticipant().getId().equals(oldCollectionProtocolRegistration.getParticipant().getId()))
			{
				checkStatus(dao, collectionProtocolRegistration.getParticipant(), "Participant");
			}
		}

		//when old participant is null and new is not null
		if (collectionProtocolRegistration.getParticipant() != null && oldCollectionProtocolRegistration.getParticipant() == null)
		{
			if (collectionProtocolRegistration.getParticipant().getId() != null)
			{
				checkStatus(dao, collectionProtocolRegistration.getParticipant(), "Participant");
			}
		}

		/**
		 * Case: While updating the registration if the participant is deselected then 
		 * we need to maintain the link between registration and participant by adding a dummy participant
		 * for query module. 
		 */
		if (collectionProtocolRegistration.getParticipant() == null)
		{
			Participant oldParticipant = oldCollectionProtocolRegistration.getParticipant();

			//Check for if the older participant was also a dummy, if true use the same participant, 
			//otherwise create an another dummay participant
			if (oldParticipant != null)
			{
				String firstName = Utility.toString(oldParticipant.getFirstName());
				String lastName = Utility.toString(oldParticipant.getLastName());
				String birthDate = Utility.toString(oldParticipant.getBirthDate());
				String ssn = Utility.toString(oldParticipant.getSocialSecurityNumber());
				if (firstName.trim().length() == 0 && lastName.trim().length() == 0 && birthDate.trim().length() == 0 && ssn.trim().length() == 0)
				{
					collectionProtocolRegistration.setParticipant(oldParticipant);
				}
				else
				{
					//create dummy participant.
					Participant participant = addDummyParticipant(dao, sessionDataBean);
					collectionProtocolRegistration.setParticipant(participant);
				}

			} // oldpart != null
			else
			{
				//create dummy participant.
				Participant participant = addDummyParticipant(dao, sessionDataBean);
				collectionProtocolRegistration.setParticipant(participant);
			}
		}
		checkUniqueConstraint(dao, collectionProtocolRegistration, oldCollectionProtocolRegistration);
		//Mandar 22-Jan-07 To disable consents accordingly in SCG and Specimen(s) start
		if(!collectionProtocolRegistration.getConsentWithdrawalOption().equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_NOACTION) )
		{
			verifyAndUpdateConsentWithdrawn(collectionProtocolRegistration, dao, sessionDataBean);
		}
		//Mandar 22-Jan-07 To disable consents accordingly in SCG and Specimen(s) end
		//Update registration
		dao.update(collectionProtocolRegistration, sessionDataBean, true, true, false);

		//Audit.
		dao.audit(obj, oldObj, sessionDataBean, true);

		//Disable all specimen Collection group under this registration. 
		Logger.out.debug("collectionProtocolRegistration.getActivityStatus() " + collectionProtocolRegistration.getActivityStatus());
		if (collectionProtocolRegistration.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("collectionProtocolRegistration.getActivityStatus() " + collectionProtocolRegistration.getActivityStatus());
			Long collectionProtocolRegistrationIDArr[] = {collectionProtocolRegistration.getId()};

			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			bizLogic.disableRelatedObjects(dao, collectionProtocolRegistrationIDArr);
		}
	}

	public void postUpdate(DAO dao, Object currentObj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException,
			UserNotAuthorizedException
	{
		ParticipantRegistrationCacheManager participantRegCacheManager = new ParticipantRegistrationCacheManager();
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) currentObj;
		CollectionProtocolRegistration oldCollectionProtocolRegistration = (CollectionProtocolRegistration) oldObj;

		Long oldCPId = oldCollectionProtocolRegistration.getCollectionProtocol().getId();
		Long newCPId = collectionProtocolRegistration.getCollectionProtocol().getId();
		Long oldParticipantId = oldCollectionProtocolRegistration.getParticipant().getId();
		Long newParticipantId = collectionProtocolRegistration.getParticipant().getId();
		String oldProtocolParticipantId = oldCollectionProtocolRegistration.getProtocolParticipantIdentifier();

		if (oldProtocolParticipantId == null)
			oldProtocolParticipantId = "";

		String newProtocolParticipantId = collectionProtocolRegistration.getProtocolParticipantIdentifier();

		if (newProtocolParticipantId == null)
			newProtocolParticipantId = "";

		if (oldCPId.longValue() != newCPId.longValue() || oldParticipantId.longValue() != newParticipantId.longValue()
				|| !oldProtocolParticipantId.equals(newProtocolParticipantId))
		{
			participantRegCacheManager.deRegisterParticipant(oldCPId, oldParticipantId, oldProtocolParticipantId);
			participantRegCacheManager.registerParticipant(newCPId, newParticipantId, newProtocolParticipantId);
		}

		if (collectionProtocolRegistration.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			participantRegCacheManager.deRegisterParticipant(newCPId, newParticipantId, newProtocolParticipantId);
		}

	}

	private Set getProtectionObjects(AbstractDomainObject obj)
	{
		Set protectionObjects = new HashSet();

		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;
		protectionObjects.add(collectionProtocolRegistration);

		Participant participant = null;
		//Case of registering Participant on its participant ID
		if (collectionProtocolRegistration.getParticipant() != null)
		{
			protectionObjects.add(collectionProtocolRegistration.getParticipant());
		}

		Logger.out.debug(protectionObjects.toString());
		return protectionObjects;
	}

	private String[] getDynamicGroups(AbstractDomainObject obj)
	{
		String[] dynamicGroups = null;
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;
		dynamicGroups = new String[1];
		dynamicGroups[0] = Constants.getCollectionProtocolPGName(collectionProtocolRegistration.getCollectionProtocol().getId());
		return dynamicGroups;
	}

	private void registerParticipantAndProtocol(DAO dao, CollectionProtocolRegistration collectionProtocolRegistration,
			SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		//Case of registering Participant on its participant ID
		Participant participant = null;

		if (collectionProtocolRegistration.getParticipant() != null)
		{
			Object participantObj = dao.retrieve(Participant.class.getName(), collectionProtocolRegistration.getParticipant().getId());

			if (participantObj != null)
			{
				participant = (Participant) participantObj;
			}
		}
		else
		{
			participant = addDummyParticipant(dao, sessionDataBean);
		}

		collectionProtocolRegistration.setParticipant(participant);

		Object collectionProtocolObj = dao.retrieve(CollectionProtocol.class.getName(), collectionProtocolRegistration.getCollectionProtocol()
				.getId());
		if (collectionProtocolObj != null)
		{
			CollectionProtocol collectionProtocol = (CollectionProtocol) collectionProtocolObj;
			collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);
		}
	}

	/** Add a dummy participant when participant is registed to a protocol using 
	 * participant protocol id */
	private Participant addDummyParticipant(DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		Participant participant = new Participant();

		participant.setLastName("");
		participant.setFirstName("");
		participant.setMiddleName("");
		participant.setSocialSecurityNumber(null);
		participant.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);

		//Create a dummy participant medical identifier.
		Set partMedIdentifierColl = new HashSet();
		ParticipantMedicalIdentifier partMedIdentifier = new ParticipantMedicalIdentifier();
		partMedIdentifier.setMedicalRecordNumber(null);
		partMedIdentifier.setSite(null);
		partMedIdentifierColl.add(partMedIdentifier);

		dao.insert(participant, sessionDataBean, true, true);

		partMedIdentifier.setParticipant(participant);
		dao.insert(partMedIdentifier, sessionDataBean, true, true);

		/* inserting dummy participant in participant cache */
		ParticipantRegistrationCacheManager participantRegCache = new ParticipantRegistrationCacheManager();
		participantRegCache.addParticipant(participant);
		return participant;
	}

	/**
	 * Disable all the related collection protocol regitration for a given array of participant ids. 
	 **/
	public void disableRelatedObjectsForParticipant(DAO dao, Long participantIDArr[]) throws DAOException
	{
		List listOfSubElement = super.disableObjects(dao, CollectionProtocolRegistration.class, "participant", "CATISSUE_COLL_PROT_REG",
				"PARTICIPANT_ID", participantIDArr);
		if (!listOfSubElement.isEmpty())
		{
			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			bizLogic.disableRelatedObjects(dao, Utility.toLongArray(listOfSubElement));
		}
	}

	/**
	 * Disable all the related collection protocol regitrations for a given array of collection protocol ids. 
	 **/
	public void disableRelatedObjectsForCollectionProtocol(DAO dao, Long collectionProtocolIDArr[]) throws DAOException
	{
		List listOfSubElement = super.disableObjects(dao, CollectionProtocolRegistration.class, "collectionProtocol", "CATISSUE_COLL_PROT_REG",
				"COLLECTION_PROTOCOL_ID", collectionProtocolIDArr);
		if (!listOfSubElement.isEmpty())
		{
			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			bizLogic.disableRelatedObjects(dao, Utility.toLongArray(listOfSubElement));
		}
	}

	/**
	 * @param dao
	 * @param objectIds
	 * @param assignToUser
	 * @param roleId
	 * @throws DAOException
	 * @throws SMException
	 */
	public void assignPrivilegeToRelatedObjectsForParticipant(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId,
			boolean assignToUser, boolean assignOperation) throws SMException, DAOException
	{
		List listOfSubElement = super.getRelatedObjects(dao, CollectionProtocolRegistration.class, "participant", objectIds);

		if (!listOfSubElement.isEmpty())
		{
			super.setPrivilege(dao, privilegeName, CollectionProtocolRegistration.class, Utility.toLongArray(listOfSubElement), userId, roleId,
					assignToUser, assignOperation);
			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			bizLogic.assignPrivilegeToRelatedObjects(dao, privilegeName, Utility.toLongArray(listOfSubElement), userId, roleId, assignToUser,
					assignOperation);
		}
	}

	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#setPrivilege(DAO, String, Class, Long[], Long, String, boolean)
	 * @param dao
	 * @param privilegeName
	 * @param objectIds
	 * @param userId
	 * @param roleId
	 * @param assignToUser
	 * @throws SMException
	 * @throws DAOException
	 */
	public void assignPrivilegeToRelatedObjectsForCP(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId,
			boolean assignToUser, boolean assignOperation) throws SMException, DAOException
	{
		List listOfSubElement = super.getRelatedObjects(dao, CollectionProtocolRegistration.class, "collectionProtocol", objectIds);
		if (!listOfSubElement.isEmpty())
		{
			super.setPrivilege(dao, privilegeName, CollectionProtocolRegistration.class, Utility.toLongArray(listOfSubElement), userId, roleId,
					assignToUser, assignOperation);
			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			bizLogic.assignPrivilegeToRelatedObjects(dao, privilegeName, Utility.toLongArray(listOfSubElement), userId, roleId, assignToUser,
					assignOperation);

			ParticipantBizLogic participantBizLogic = (ParticipantBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.PARTICIPANT_FORM_ID);
			participantBizLogic.assignPrivilegeToRelatedObjectsForCPR(dao, privilegeName, Utility.toLongArray(listOfSubElement), userId, roleId,
					assignToUser, assignOperation);
		}
	}

	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#setPrivilege(DAO, String, Class, Long[], Long, String, boolean)
	 */
	public void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser,
			boolean assignOperation) throws SMException, DAOException
	{
		super.setPrivilege(dao, privilegeName, objectType, objectIds, userId, roleId, assignToUser, assignOperation);

		SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance().getBizLogic(
				Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
		bizLogic.assignPrivilegeToRelatedObjects(dao, privilegeName, objectIds, userId, roleId, assignToUser, assignOperation);

		ParticipantBizLogic participantBizLogic = (ParticipantBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.PARTICIPANT_FORM_ID);
		participantBizLogic.assignPrivilegeToRelatedObjectsForCPR(dao, privilegeName, objectIds, userId, roleId, assignToUser, assignOperation);

	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		CollectionProtocolRegistration registration = (CollectionProtocolRegistration) obj;

		/**
		 * Start: Change for API Search   --- Jitendra 06/10/2006
		 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
		 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
		 * So we removed default class level initialization on domain object and are initializing in method
		 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
		 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
		 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
		 */
		ApiSearchUtil.setCollectionProtocolRegistrationDefault(registration);
		//End:-  Change for API Search 

		//Added by Ashish
		if (registration == null)
		{
			throw new DAOException(ApplicationProperties.getValue("domain.object.null.err.msg", "Collection Protocol Registration"));
		}
		Validator validator = new Validator();
		String message = "";
		if (registration.getCollectionProtocol() == null || registration.getCollectionProtocol().getId() == null)
		{
			message = ApplicationProperties.getValue("collectionprotocolregistration.protocoltitle");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		String errorKey = validator.validateDate(Utility.parseDateToString(registration.getRegistrationDate(), Constants.DATE_PATTERN_MM_DD_YYYY),
				true);
		if (errorKey.trim().length() > 0)
		{
			message = ApplicationProperties.getValue("collectionprotocolregistration.date");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		if (validator.isEmpty(registration.getProtocolParticipantIdentifier()))
		{
			if (registration.getParticipant() == null || registration.getParticipant().getId() == null)
			{
				throw new DAOException(ApplicationProperties.getValue("errors.collectionprotocolregistration.atleast"));
			}
		}
		//		if (checkedButton == true)
		//{
		/*if (registration.getParticipant() == null || registration.getParticipant().getId() == null)
		 {
		 message = ApplicationProperties.getValue("collectionProtocolReg.participantName");
		 throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		 }*/
		//		} // name selected
		//else
		//		{
		/*
		 if (validator.isEmpty(registration.getParticipant().getId().toString()))
		 {
		 String message = ApplicationProperties.getValue("collectionProtocolReg.participantProtocolID");
		 throw new DAOException("errors.item.required", new String[]{message});
		 
		 }
		 //		}
		 //  date validation according to bug id 707, 722 and 730
		 String errorKey = validator.validateDate(Utility.parseDateToString(registration.getRegistrationDate(),Constants.DATE_PATTERN_MM_DD_YYYY),true );
		 if(errorKey.trim().length() >0  )
		 {
		 String message = ApplicationProperties.getValue("collectionprotocolregistration.date");
		 throw new DAOException("errors.item.required", new String[]{message});
		 
		 }
		 

		 //
		 if (!validator.isValidOption(registration.getActivityStatus()))
		 {
		 String message = ApplicationProperties.getValue("collectionprotocolregistration.activityStatus");
		 throw new DAOException("errors.item.required", new String[]{message});
		 
		 }
		 */
		//End
		if (operation.equals(Constants.ADD))
		{
			if (!Constants.ACTIVITY_STATUS_ACTIVE.equals(registration.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.active.errMsg"));
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, registration.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
			}
		}

		return true;
	}

	public void checkUniqueConstraint(DAO dao, CollectionProtocolRegistration collectionProtocolRegistration,
			CollectionProtocolRegistration oldcollectionProtocolRegistration) throws DAOException
	{
		CollectionProtocol objCollectionProtocol = collectionProtocolRegistration.getCollectionProtocol();
		String sourceObjectName = collectionProtocolRegistration.getClass().getName();
		String[] selectColumns = null;
		String[] whereColumnName = null;
		String[] whereColumnCondition = new String[]{"=", "="};
		Object[] whereColumnValue = null;
		String arguments[] = null;
		String errMsg = "";
		// check for update opeartion and old values equals to new values
		int count = 0;
		if (oldcollectionProtocolRegistration != null)
		{
			if (collectionProtocolRegistration.getParticipant() != null && oldcollectionProtocolRegistration.getParticipant() != null)
			{
				if (collectionProtocolRegistration.getParticipant().getId().equals(oldcollectionProtocolRegistration.getParticipant().getId()))
				{
					count++;
				}
				if (collectionProtocolRegistration.getCollectionProtocol().getId().equals(
						oldcollectionProtocolRegistration.getCollectionProtocol().getId()))
				{
					count++;
				}
			}
			else if (collectionProtocolRegistration.getProtocolParticipantIdentifier() != null
					&& oldcollectionProtocolRegistration.getProtocolParticipantIdentifier() != null)
			{
				if (collectionProtocolRegistration.getProtocolParticipantIdentifier().equals(
						oldcollectionProtocolRegistration.getProtocolParticipantIdentifier()))
				{
					count++;
				}
				if (collectionProtocolRegistration.getCollectionProtocol().getId().equals(
						oldcollectionProtocolRegistration.getCollectionProtocol().getId()))
				{
					count++;
				}
			}
			// if count=0 return i.e. old values equals new values 
			if (count == 2)
				return;
		}
		if (collectionProtocolRegistration.getParticipant() != null)
		{
			// build query for collectionProtocol_id AND participant_id
			Participant objParticipant = collectionProtocolRegistration.getParticipant();
			selectColumns = new String[]{"collectionProtocol.id", "participant.id"};
			whereColumnName = new String[]{"collectionProtocol.id", "participant.id"};
			whereColumnValue = new Object[]{objCollectionProtocol.getId(), objParticipant.getId()};
			arguments = new String[]{"Collection Protocol Registration ", "COLLECTION_PROTOCOL_ID,PARTICIPANT_ID"};
		}
		else
		{
			//	    		 build query for collectionProtocol_id AND protocol_participant_id
			selectColumns = new String[]{"collectionProtocol.id", "protocolParticipantIdentifier"};
			whereColumnName = new String[]{"collectionProtocol.id", "protocolParticipantIdentifier"};
			whereColumnValue = new Object[]{objCollectionProtocol.getId(), collectionProtocolRegistration.getProtocolParticipantIdentifier()};
			arguments = new String[]{"Collection Protocol Registration ", "COLLECTION_PROTOCOL_ID,PROTOCOL_PARTICIPANT_ID"};
		}
		List l = dao.retrieve(sourceObjectName, selectColumns, whereColumnName, whereColumnCondition, whereColumnValue, Constants.AND_JOIN_CONDITION);
		if (l.size() > 0)
		{
			// if list is not empty the Constraint Violation occurs
			Logger.out.debug("Unique Constraint Violated: " + l.get(0));
			errMsg = new DefaultExceptionFormatter().getErrorMessage("Err.ConstraintViolation", arguments);
			Logger.out.debug("Unique Constraint Violated: " + errMsg);
			throw new DAOException(errMsg);
		}
		else
		{
			Logger.out.debug("Unique Constraint Passed");
		}
	}

	/**
	 * This function finds out all the registerd participants for a particular collection protocol.
	 * @return List of ParticipantRegInfo
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public List getAllParticipantRegistrationInfo() throws DAOException, ClassNotFoundException
	{

		List participantRegistrationInfoList = new Vector();

		String hql = "select cpr.collectionProtocol.id ,ccp.title,cpr.participant.id,cpr.protocolParticipantIdentifier,ccp.activityStatus,cpr.participant.activityStatus from "
				+ CollectionProtocolRegistration.class.getName()
				+ " as cpr right outer join cpr.collectionProtocol as ccp where ccp.id = cpr.collectionProtocol.id order by ccp.id";

		HibernateDAO dao = (HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		dao.openSession(null);

		List list = dao.executeQuery(hql, null, false, null);
		Logger.out.info("list size -----------:" + list.size());
		dao.closeSession();
		if (list != null)
		{
			for (int i = 0; i < list.size(); i++)
			{
				//Getitng participants for a particular CP.
				Object[] obj = (Object[]) list.get(i);
				Long cpId = (Long) obj[0];
				String cpTitle = (String) obj[1];
				Long participantID = (Long) obj[2];
				String protocolParticipantId = (String) obj[3];
				String cpStatus = (String) obj[4];
				String participantStatus = (String) obj[5];

				List participantInfoList = new ArrayList();

				if (participantID != null && !participantStatus.equals(Constants.ACTIVITY_STATUS_DISABLED))
				{
					String participantInfo = participantID.toString() + ":";
					if (protocolParticipantId != null && !protocolParticipantId.equals(""))
						participantInfo = participantInfo + protocolParticipantId;
					participantInfoList.add(participantInfo);

				}

				for (int j = i + 1; j < list.size(); j++, i++)
				{
					Object[] obj1 = (Object[]) list.get(j);
					Long cpId1 = (Long) obj1[0];
					Long participantID1 = (Long) obj1[2];
					String protocolParticipantId1 = (String) obj1[3];
					String participantStatus1 = (String) obj1[5];

					if (cpId1.longValue() == cpId.longValue())
					{
						if (participantID1 != null && !participantStatus1.equals(Constants.ACTIVITY_STATUS_DISABLED))
						{
							String participantInfo = participantID1.toString() + ":";
							if (protocolParticipantId1 != null && !protocolParticipantId1.equals(""))
								participantInfo = participantInfo + protocolParticipantId1;
							participantInfoList.add(participantInfo);
						}
					}
					else
					{
						break;
					}
				}

				//Creating ParticipanrRegistrationInfo object and storing in a vector participantRegistrationInfoList.
				if (!cpStatus.equalsIgnoreCase(Constants.ACTIVITY_STATUS_DISABLED))
				{
					ParticipantRegistrationInfo prInfo = new ParticipantRegistrationInfo();
					prInfo.setCpId(cpId);
					prInfo.setCpTitle(cpTitle);
					prInfo.setParticipantInfoCollection(participantInfoList);
					participantRegistrationInfoList.add(prInfo);
				}
			}
		}

		return participantRegistrationInfoList;
	}

	//Mandar : 11-Jan-07 For Consent Tracking Withdrawal -------- start
	/*
	 * verifyAndUpdateConsentWithdrawn(collectionProtocolRegistration)
	 * updateSCG(collectionProtocolRegistration, consentTierResponse)
	 * 
	 */
	
	/*
	 * This method verifies and updates SCG and child elements for withdrawn consents
	 */
	private void verifyAndUpdateConsentWithdrawn(CollectionProtocolRegistration collectionProtocolRegistration,  DAO dao, SessionDataBean sessionDataBean)
	{
		Collection newConsentTierResponseCollection = collectionProtocolRegistration.getConsentTierResponseCollection();
		Iterator itr = newConsentTierResponseCollection.iterator() ;
		while(itr.hasNext() )
		{
			ConsentTierResponse consentTierResponse = (ConsentTierResponse)itr.next();
			if(consentTierResponse.getResponse().equalsIgnoreCase(Constants.WITHDRAWN ) )	
			{
				long consentTierID = consentTierResponse.getConsentTier().getId().longValue();
				updateSCG(collectionProtocolRegistration, consentTierID, dao,  sessionDataBean);
			}
		}
	}
	
	/*
	 * This method updates all the scg's associated with the selected collectionprotocolregistration.
	 */
	private void updateSCG(CollectionProtocolRegistration collectionProtocolRegistration, long consentTierID,  DAO dao, SessionDataBean sessionDataBean)
	{
		Collection newScgCollection = new HashSet(); 
		Collection scgCollection = collectionProtocolRegistration.getSpecimenCollectionGroupCollection();
		Iterator scgItr = scgCollection.iterator();
		while(scgItr.hasNext() )
		{
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup)scgItr.next();
			String cprWithdrawOption = collectionProtocolRegistration.getConsentWithdrawalOption();
			
			WithdrawConsentUtil.updateSCG(scg, consentTierID, cprWithdrawOption, dao, sessionDataBean);
			
			newScgCollection.add(scg);	// set updated scg in cpr
		}
		collectionProtocolRegistration.setSpecimenCollectionGroupCollection(newScgCollection );
	}

	//Mandar : 11-Jan-07 For Consent Tracking Withdrawal -------- end

}