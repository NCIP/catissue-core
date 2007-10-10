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
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
//import java.util.Vector;
import java.util.Stack;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.ApiSearchUtil;
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
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.tree.QueryTreeNodeData;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * UserHDAO is used to add user information into the database using Hibernate.
 * @author kapil_kaveeshwar
 */
public class SpecimenCollectionGroupBizLogic extends DefaultBizLogic
{
	/**
	 * Saves the user object in the database.
	 * @param obj The user object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;

		if(specimenCollectionGroup.getSpecimenCollectionSite() != null)
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
		if (collectionProtocolEventObj != null)
		{
			CollectionProtocolEvent cpe = (CollectionProtocolEvent) collectionProtocolEventObj;

			//check for closed CollectionProtocol
			checkStatus(dao, cpe.getCollectionProtocol(), "Collection Protocol");

			specimenCollectionGroup.setCollectionProtocolEvent(cpe);
		}

		setCollectionProtocolRegistration(dao, specimenCollectionGroup, null);

		dao.insert(specimenCollectionGroup, sessionDataBean, true, true);

		try
		{
			SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, getProtectionObjects(specimenCollectionGroup),
					getDynamicGroups(specimenCollectionGroup));
		}
		catch (SMException e)
		{
			throw handleSMException(e);
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

	private String[] getDynamicGroups(AbstractDomainObject obj) throws SMException
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
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
		SpecimenCollectionGroup oldspecimenCollectionGroup = (SpecimenCollectionGroup) oldObj;

		//Adding default events if they are null from API
		Collection spEventColl = specimenCollectionGroup.getSpecimenEventParametersCollection();
		if (spEventColl == null || spEventColl.isEmpty())
		{
			setDefaultEvents(specimenCollectionGroup, sessionDataBean);
		}
		// Check for different closed site
		if(oldspecimenCollectionGroup.getSpecimenCollectionSite()==null&&specimenCollectionGroup.getSpecimenCollectionSite()!=null)
		{
			checkStatus(dao, specimenCollectionGroup.getSpecimenCollectionSite(), "Site");
		}
		else if (!specimenCollectionGroup.getSpecimenCollectionSite().getId().equals(oldspecimenCollectionGroup.getSpecimenCollectionSite().getId()))
		{
			checkStatus(dao, specimenCollectionGroup.getSpecimenCollectionSite(), "Site");
		}
		
		//site check complete
		Long oldEventId = oldspecimenCollectionGroup.getCollectionProtocolEvent().getId();
		Long eventId =	specimenCollectionGroup.getCollectionProtocolEvent().getId();
		if(oldEventId.longValue()!=eventId.longValue())
		{
			// -- check for closed CollectionProtocol
			List list = dao.retrieve(CollectionProtocolEvent.class.getName(), Constants.SYSTEM_IDENTIFIER, specimenCollectionGroup
					.getCollectionProtocolEvent().getId());
			if (!list.isEmpty())
			{
				// check for closed CollectionProtocol
				CollectionProtocolEvent cpe = (CollectionProtocolEvent) list.get(0);
				if (!cpe.getCollectionProtocol().getId().equals(oldspecimenCollectionGroup.getCollectionProtocolEvent().getCollectionProtocol().getId()))
					checkStatus(dao, cpe.getCollectionProtocol(), "Collection Protocol");
	
				specimenCollectionGroup.setCollectionProtocolEvent((CollectionProtocolEvent) list.get(0));
			}
		}
		//CollectionProtocol check complete.

		setCollectionProtocolRegistration(dao, specimenCollectionGroup, oldspecimenCollectionGroup);

		//Mandar 22-Jan-07 To disable consents accordingly in SCG and Specimen(s) start		
		if (!specimenCollectionGroup.getConsentWithdrawalOption().equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_NOACTION))
		{
			verifyAndUpdateConsentWithdrawn(specimenCollectionGroup, oldspecimenCollectionGroup, dao, sessionDataBean);
		}
		//Mandar 22-Jan-07 To disable consents accordingly in SCG and Specimen(s) end
		//Mandar 24-Jan-07 To update consents accordingly in SCG and Specimen(s) start
		else if (!specimenCollectionGroup.getApplyChangesTo().equalsIgnoreCase(Constants.APPLY_NONE))
		{
			WithdrawConsentUtil.updateSpecimenStatusInSCG(specimenCollectionGroup, oldspecimenCollectionGroup, dao);
		}
		//Mandar 24-Jan-07 To update consents accordingly in SCG and Specimen(s) end
		dao.update(specimenCollectionGroup, sessionDataBean, true, true, false);
		/**
		 * Name : Ashish Gupta
		 * Reviewer Name : Sachin Lale 
		 * Bug ID: 2741
		 * Patch ID: 2741_6	 
		 * Description: Method to update events in all specimens related to this scg
		 */
		//		Populating Events in all specimens
		if (specimenCollectionGroup.isApplyEventsToSpecimens())
		{
			updateEvents(specimenCollectionGroup, oldspecimenCollectionGroup, dao, sessionDataBean);
		}
		//Audit.
		dao.audit(obj, oldObj, sessionDataBean, true);
		SpecimenCollectionGroup oldSpecimenCollectionGroup = (SpecimenCollectionGroup) oldObj;

		//Disable the related specimens to this specimen group
		Logger.out.debug("specimenCollectionGroup.getActivityStatus() " + specimenCollectionGroup.getActivityStatus());
		if (specimenCollectionGroup.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("specimenCollectionGroup.getActivityStatus() " + specimenCollectionGroup.getActivityStatus());
			Long specimenCollectionGroupIDArr[] = {specimenCollectionGroup.getId()};

			NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			bizLogic.disableRelatedObjectsForSpecimenCollectionGroup(dao, specimenCollectionGroupIDArr);
		}

	}

	/**
	 * @param specimenCollectionGroup
	 * @param sessionDataBean
	 * Sets the default events if they are not specified
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
		//populateEventsInSpecimens(oldspecimenCollectionGroup,)
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
		//CollectionEventParameters newcollectionEventParameters = collectionEventParameters;
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
		//ReceivedEventParameters newReceivedEventParameters = receivedEventParameters;
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

	private void setCollectionProtocolRegistration(DAO dao, SpecimenCollectionGroup specimenCollectionGroup,
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
			//check for closed CollectionProtocolRegistration
			CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

			/**
			 * Start: Change for API Search   --- Jitendra 06/10/2006
			 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
			 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
			 * So we removed default class level initialization on domain object and are initializing in method
			 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
			 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
			 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setCollectionProtocolRegistrationDefault(collectionProtocolRegistration);
			//End:-  Change for API Search 

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

//	public void disableRelatedObjects(DAO dao, Long collProtRegIDArr[]) throws DAOException
//	{
//		List listOfSubElement = super.disableObjects(dao, SpecimenCollectionGroup.class, "collectionProtocolRegistration",
//				"CATISSUE_SPECIMEN_COLL_GROUP", "COLLECTION_PROTOCOL_REG_ID", collProtRegIDArr);
//		if (!listOfSubElement.isEmpty())
//		{
//			NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
//			bizLogic.disableRelatedObjectsForSpecimenCollectionGroup(dao, Utility.toLongArray(listOfSubElement));
//		}
//	}

	public void disableRelatedObjects(DAO dao, Long collProtRegIDArr[]) throws DAOException
	{
		List listOfSubElement = getRelatedObjects(dao, SpecimenCollectionGroup.class, "collectionProtocolRegistration", collProtRegIDArr);
		dao.disableRelatedObjects("CATISSUE_ABSTRACT_SPECIMEN_COLL_GROUP", Constants.SYSTEM_IDENTIFIER_COLUMN_NAME, Utility.toLongArray(listOfSubElement));
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
	 * @see edu.wustl.common.bizlogic.IBizLogic#setPrivilege(DAO, String, Class, Long[], Long, String, boolean)
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
	 * @param obj
	 * @param dao
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	protected boolean isSpecimenExists(Object obj, DAO dao,Long scgId) throws DAOException, ClassNotFoundException
	{
		
		String hql = " select" +
        " elements(scg.specimenCollection) " +
        " from " +
        " edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg , " +
        " edu.wustl.catissuecore.domain.Specimen as s" +
        " where scg.id = "+scgId+" and"+
        " scg.id = s.specimenCollectionGroup.id and " +
        " s.activityStatus = '"+Constants.ACTIVITY_STATUS_ACTIVE+"'";
		
		List specimenList=(List)executeHqlQuery( dao,hql);
		if((specimenList!=null) && (specimenList).size()>0)
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
		SpecimenCollectionGroup group = (SpecimenCollectionGroup) obj;

		//Added by Ashish	

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

		//if participant name field is checked.
		//			if(group.getCollectionProtocolRegistration().getParticipant().getId() == -1)
		//			{
		//				message = ApplicationProperties.getValue("specimenCollectionGroup.protocoltitle");
		//				throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		//				
		//				String message = ApplicationProperties.getValue("specimenCollectionGroup.collectedByParticipant");
		//				throw new DAOException("errors.item.selected", new String[]{message});
		//				
		//			}

		//			if(!validator.isValidOption(group.getCollectionProtocolRegistration().getProtocolParticipantIdentifier()))
		//			{
		//				String message = ApplicationProperties.getValue("specimenCollectionGroup.collectedByProtocolParticipantNumber");
		//				throw new DAOException("errors.item.selected", new String[]{message});
		//				
		//			}

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

		//Condition for medical Record Number.

		//END

		List clinicalDiagnosisList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CLINICAL_DIAGNOSIS, null);
		if (!Validator.isEnumeratedValue(clinicalDiagnosisList, group.getClinicalDiagnosis()))
		{
			throw new DAOException(ApplicationProperties.getValue("spg.clinicalDiagnosis.errMsg"));
		}

		//NameValueBean undefinedVal = new NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
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
		
		//check the activity status of all the specimens associated to the Specimen Collection Group
		if(group.getActivityStatus().equalsIgnoreCase(Constants.DISABLED))
		{
			try {
			
				boolean isSpecimenExist=(boolean)isSpecimenExists(obj, dao,(Long)group.getId());
				if(isSpecimenExist)
				{
					throw new DAOException(ApplicationProperties.getValue("specimencollectiongroup.specimen.exists"));
				}
		
			
			} catch (ClassNotFoundException e) {
					e.printStackTrace();
			}
		}
		/* Bug ID: 4165
		 * Patch ID: 4165_11	 
		 * Description: Validation added to check incorrect events added through API
		 */
		//		Events Validation
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
	 * @param cpId 
	 * @param participantId
	 * @return
	 * @throws Exception
	 */
	/*
	 public Vector getSCGandSpecimens(Long cpId, Long participantId) throws Exception
	 {
	 String hql = null;
	 if (participantId.longValue() == -1)
	 {
	 hql = "select scg.id,scg.name,sp.id,sp.label,sp.parentSpecimen.id ,scg.activityStatus,sp.activityStatus from " + Specimen.class.getName()
	 + " as sp right outer join sp.specimenCollectionGroup as scg where scg.collectionProtocolRegistration.collectionProtocol.id= "
	 + cpId.toString() + " and scg.id = sp.specimenCollectionGroup.id order by scg.id,sp.id";

	 }
	 else
	 {
	 hql = "select scg.id,scg.name,sp.id,sp.label,sp.parentSpecimen.id,scg.activityStatus,sp.activityStatus from " + Specimen.class.getName()
	 + " as sp right outer join sp.specimenCollectionGroup as scg where scg.collectionProtocolRegistration.collectionProtocol.id= "
	 + cpId.toString() + " and scg.collectionProtocolRegistration.participant.id= " + participantId.toString()
	 + " and scg.id = sp.specimenCollectionGroup.id order by scg.id,sp.id";

	 }
	 HibernateDAO dao = (HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
	 dao.openSession(null);

	 List list = dao.executeQuery(hql, null, false, null);
	 Logger.out.info("list size -----------:" + list.size());
	 dao.closeSession();
	 //Map map1 = new TreeMap();
	 Vector treeData = new Vector();
	 if (list != null)
	 {
	 for (int i = 0; i < list.size(); i++)
	 {
	 //Getitng participants for a particular CP.
	 Object[] obj = (Object[]) list.get(i);
	 Long scgId = (Long) obj[0];
	 String scgName = (String) obj[1];
	 String scgActivityStatus = (String) obj[5];

	 setQueryTreeNode(scgId.toString(), Constants.SPECIMEN_COLLECTION_GROUP, scgName, "0", null, null, null, scgActivityStatus, treeData);

	 for (int j = i; j < list.size(); j++, i++)
	 {
	 Object[] obj1 = (Object[]) list.get(j);
	 Long scgId1 = (Long) obj1[0];

	 if (scgId.longValue() == scgId1.longValue())
	 {
	 Long spId1 = (Long) obj1[2];
	 String spLabel1 = (String) obj1[3];
	 Long parentSpecimenId = (Long) obj1[4];
	 String spActivityStatus = (String) obj1[6];

	 if (spId1 != null)
	 {
	 if (parentSpecimenId != null)
	 {
	 setQueryTreeNode(spId1.toString(), Constants.SPECIMEN, spLabel1, parentSpecimenId.toString(), Constants.SPECIMEN,
	 null, null, spActivityStatus, treeData);

	 }
	 else
	 {
	 setQueryTreeNode(spId1.toString(), Constants.SPECIMEN, spLabel1, scgId1.toString(),
	 Constants.SPECIMEN_COLLECTION_GROUP, null, null, spActivityStatus, treeData);

	 }
	 }
	 }
	 else
	 {
	 i--;
	 break;
	 }
	 }

	 }
	 }

	 return treeData;
	 }*/
	/**
	 * Patch Id : FutureSCG_8
	 * Description : method to get SCGTree ForCPBasedView
	 */
	/**
	 * Creates tree which consists of SCG nodes and specimen nodes under each SCG if available.
	 * For a CPR if there is any SCG created those are shown with its details like  '<# event day>_<Event point label>_<SCG_recv_date>'. 
	 * When user clicks on this node ,Edit SCG page will be shown on right side panel of the screen, where now user can edit this SCG.
	 * But if there are no SCGs present for a CPR , a future(dummy) SCG is shown in tree as  '<# event day>_<Event point label>'. 
	 * When user clicks on this node , Add SCG page will be shown on right side panel of the screen, where now user can actually add new SCG 
	 * and specimens for this CPR.
	 * @param cpId id of collection protocol
	 * @param participantId id of participant
	 * @return Vector tree data structure
	 * @throws DAOException daoException
	 * @throws ClassNotFoundException classNotFoundException
	 */
	public String getSCGTreeForCPBasedView(Long cpId, Long participantId) throws DAOException, ClassNotFoundException {
		Logger.out.debug("Start of getSCGTreeForCPBasedView");
		
	    //creating XML String rep of SCGs,specimens & child specimens ::Addded by baljeet 
		StringBuffer xmlString = new StringBuffer();
		
		xmlString.append("<node>");

		String hql = "select  cpe.id, cpe.studyCalendarEventPoint,cpe.collectionPointLabel from " + CollectionProtocolEvent.class.getName()
		+ " as cpe where cpe.collectionProtocol.id= "+ cpId.toString() +" order by cpe.studyCalendarEventPoint";
		List cpeList = executeQuery(hql);
		Logger.out.debug("After executeQuery");
		for(int count = 0; count < cpeList.size() ; count++)
		{
			Object[] obj = (Object[]) cpeList.get(count);
			Long eventId = (Long)obj[0];
			Double eventPoint = (Double) obj[1];
			String collectionPointLabel = (String)obj[2];
			List scgList = getSCGsForCPRAndEventId(eventId, cpId,participantId);
			if (scgList != null && !scgList.isEmpty())
			{
			     createTreeNodeForExistingSCG(xmlString, eventPoint, collectionPointLabel, scgList); 
			}
//			else
//			{
//				 createTreeNodeForFutureSCG(xmlString, eventId, eventPoint, collectionPointLabel);
//			}
			
		}
		xmlString.append("</node>");
		return xmlString.toString();
	}
	/**
	 * Name : Deepti Shelar
	 * Bug id : 4268
	 * Patch id : 4268_1
	 *//*
	*//**
	 * Gets all scgs under given cp for all participants.
	 * @param eventId studycalendareventpoint
	 * @param cpId collection protocol id
	 * @return List of scgs
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	/*
	 private List getSCGsForTechnicians(Long eventId, Long cpId) throws DAOException, ClassNotFoundException 
	 {
	 String hql = "select scg.id,scg.name,scg.activityStatus from "
	 + SpecimenCollectionGroup.class.getName()
	 + " as scg where scg.collectionProtocolRegistration.id = (select cpr.id from "
	 + CollectionProtocolRegistration.class.getName() +" as cpr where cpr.collectionProtocol.id = "
	 + cpId + ") and scg.collectionProtocolEvent.id = "+eventId ;
	 
	 String hql = "select scg.id,scg.name,scg.activityStatus from "
	 + SpecimenCollectionGroup.class.getName()
	 + " as scg where scg.collectionProtocolEvent.id = "+eventId ;
	 List list = executeQuery(hql);
	 return list;
	 }*/

	/**
	 * Patch Id : FutureSCG_9
	 * Description : method to create TreeNode For FutureSCG
	 */
	/**
	 * Creates tree node for a SCG which does not exist in database, but user can create it by clicking on this node.
	 * @param treeData data structure for tree data
	 * @param eventId id of studyCalendarEvent
	 * @param eventPoint event point in no of days
	 * @param collectionPointLabel String label of collection event point
	 */
	private void createTreeNodeForFutureSCG(StringBuffer xmlString, Long eventId, Double eventPoint, String collectionPointLabel) 
	{
		Long futureSCGId = new Long(0);
		String futureSCGName = "T"+eventPoint + ": " +collectionPointLabel;
		String toolTipText = futureSCGName;//getToolTipText(eventPoint.toString(),collectionPointLabel,null);
		String nodeId = futureSCGId.toString()+":"+eventId.toString()+":"+Constants.FUTURE_SCG;
		
		xmlString.append("<node id=\""+Constants.SPECIMEN_COLLECTION_GROUP+"_"+nodeId+"\" "+"name=\""+futureSCGName+"\" "+"toolTip=\""+toolTipText+"\" "+"type=\""+Constants.SPECIMEN_COLLECTION_GROUP+"\"></node>");
	}
	/**
	 * Patch Id : FutureSCG_10
	 * Description : method to create TreeNode For ExistingSCG
	 */
	/**
	 * Creates a tree node for existing SCgs
	 * @param xmlString data structure for storing tree data
	 * @param eventPoint event point in no of days
	 * @param collectionPointLabel String label of collection event point
	 * @param scgList list of scgs
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	private void createTreeNodeForExistingSCG(StringBuffer xmlString, Double eventPoint, String collectionPointLabel, List scgList) throws DAOException, ClassNotFoundException 
	{
		for (int i = 0; i < scgList.size(); i++)
		{
			Object[] obj1 = (Object[]) scgList.get(i);
			Long scgId = (Long) obj1[0];
			String scgCollectionStatus=(String)obj1[3]; 
			String scgNodeLabel = "";
			//String scgActivityStatus = (String) obj1[2];
			
			/**
			 * Name: Vijay Pande
			 * Reviewer Name: Aarti Sharma
			 * recievedEvent related to scg is trieved from db and, proper receivedDate and scgNodeLabel are set to set toolTip of the node
			 */
			String receivedDate = "";
			if(scgId != null  && scgId>0)
			{
				String sourceObjName=ReceivedEventParameters.class.getName();
				String columnName=Constants.COLUMN_NAME_SCG_ID;
				Long ColumnValue=scgId;
				Collection eventsColl=retrieve(sourceObjName, columnName, ColumnValue);
                
				//Collection eventsColl = specimenCollectionGroup.getSpecimenEventParametersCollection();
				if(eventsColl != null && !eventsColl.isEmpty())
				{
					receivedDate = "";
					Iterator iter = eventsColl.iterator();
					while(iter.hasNext())
					{
						ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters)iter.next();
						receivedDate = Utility.parseDateToString(receivedEventParameters.getTimestamp(),"yyyy-MM-dd");
						scgNodeLabel = "T"+eventPoint + ": " +collectionPointLabel + ": " + receivedDate; 
						break;
					}
				}
				if(scgNodeLabel.equalsIgnoreCase("")&&receivedDate.equalsIgnoreCase(""))
				{
					receivedDate = Utility.parseDateToString(new Date(System.currentTimeMillis()),"yyyy-MM-dd");
					//String toolTipText = scgNodeLabel+ ": "+scgName;//
					scgNodeLabel = "T"+eventPoint + ": " +collectionPointLabel + ": " + receivedDate;
				}
			}
			String toolTipText=getToolTipText(eventPoint.toString(),collectionPointLabel,receivedDate);

			//creating SCG node
			xmlString.append("<node id= \""+Constants.SPECIMEN_COLLECTION_GROUP+"_"+scgId.toString()+ "\" "+"name=\""+ scgNodeLabel+"\" "+ "toolTip=\""+toolTipText+"\" " +"type=\""+ Constants.SPECIMEN_COLLECTION_GROUP+"\" "+"scgCollectionStatus=\""+ scgCollectionStatus+"\">");
			
			//Adding specimen Nodes to SCG tree
			addSpecimenNodesToSCGTree(xmlString,scgId);
			xmlString.append("</node>");
		}
	}
	/**
	 * Patch Id : FutureSCG_11
	 * Description : method to get SCGs For CPR And EventId
	 */
	/**
	 * Returns the list of SCGs present for given CPR and eventId.
	 * @param eventId id of collection protocol event
	 * @param cpId id of collection protocol
	 * @param participantId id of participant
	 * @return list of SCGs.
	 * @throws DAOException daoException
	 * @throws ClassNotFoundException classNotFoundException
	 */
	private List getSCGsForCPRAndEventId(Long eventId, Long cpId, Long participantId) throws DAOException, ClassNotFoundException
	{
		String hql = "select scg.id,scg.name,scg.activityStatus,scg.collectionStatus from "
			+ SpecimenCollectionGroup.class.getName()
			+ " as scg where scg.collectionProtocolRegistration.id = (select cpr.id from "
			+ CollectionProtocolRegistration.class.getName() +" as cpr where cpr.collectionProtocol.id = "
			+ cpId + " and cpr.participant.id = "+participantId 
			+") and scg.collectionProtocolEvent.id = "+eventId
			+" and scg.activityStatus <> '"+Constants.ACTIVITY_STATUS_DISABLED+"'";
		List list = executeQuery(hql);
		return list;
	}
	/**
	 * Patch Id : FutureSCG_12
	 * Description : method to add SpecimenNodes To SCGTree
	 */
	/**
	 * Adds specimen nodes to SCG node
	 * @param xmlString vector to store XML rep of tree data
	 * @param scgId id of specimen collection group
	 */
	private void addSpecimenNodesToSCGTree(StringBuffer xmlString, Long scgId) throws DAOException, ClassNotFoundException
	{
		String hql = "select sp.id,sp.label,sp.parentSpecimen.id,sp.activityStatus,sp.type,sp.collectionStatus	from "
			+ Specimen.class.getName()
			+ " as sp where sp.specimenCollectionGroup.id = "+scgId +" and sp.activityStatus <> '"+Constants.ACTIVITY_STATUS_DISABLED+"' order by sp.id";
		List specimenList = executeQuery(hql);
		
	    //here we have two Lists to separate out Specimens and child Specimens
		List <Object[]>finalList = new ArrayList<Object[]>();
		List <Object[]>childrenList = new ArrayList<Object[]>();
		
		//Stack
		Stack <Object[]>spStack = new Stack<Object[]>();
		
		//Here iterate over specimenList to separate out Specimens and child Specimens
		for(int i = 0; i < specimenList.size(); i++)
		{
			Object[] obj = (Object[])specimenList.get(i);
			Long spId = (Long) obj[0];
			Long parentSpecimenId = (Long) obj[2];
			
			
			//Long peekSpecimenId = null;
			if(spId != null)	
			{
				if(parentSpecimenId == null)
				{
					//if parentSpecimenId is null then it's going to be parent specimen
					finalList.add((Object[])specimenList.get(i));
				}
				else
				{
					childrenList.add((Object[])specimenList.get(i));
				}
			}
		}
		
		//Now iterate over childrenList to place children specimens just after their parent specimen  
		for(int i=0;i<childrenList.size();i++)
		{
			Object[] obj = (Object[])childrenList.get(i);
			Long parentSpecimenId = (Long)obj[2];
			
			
			for(int j=0; j< finalList.size(); j++)
			{
				Object[] obj1 = (Object[])finalList.get(j);
				Long spId = (Long)obj1[0];
				
				//This if statement is not working.......convert Long objects to long values 
				if(parentSpecimenId.longValue() == spId.longValue())
				{
					finalList.add(j+1, childrenList.get(i));
					break;
					//Here break is important ....once parent is found
				}
			}
		}
		
		//Here create XML String  rep. of parent/child specimen tree from finalList 
		for(int i=0; i<finalList.size();i++)
		{
		     	Object[] specimens = (Object[])finalList.get(i);
		     	Long spId = (Long)specimens[0];
		     	Long parentId = (Long)specimens[2];
		     	String spLabel1 = (String) specimens[1];
		     	//String spActivityStatus = (String) specimens[3];
		     	String type = (String)specimens[4];
		     	String spCollectionStatus = (String)specimens[5];
		     	
		     	//Added later for toolTip text for specimens
		        String toolTipText = "Label : " + spLabel1 + " ; Type : " + type;
				
				String hqlCon = "select colEveParam.container from " + CollectionEventParameters.class.getName()
					+" as colEveParam where colEveParam.specimen.id = "+spId;
				
				List container = executeQuery(hqlCon);
				for (int k = 0; k < container.size(); k++)
				{
					String con = (String)container.get(k);
					toolTipText += " ; Container : "+con;
				}
		     	
		     	//If parent id is null, then it's going to be a new specimens
		     	if(parentId == null)
		     	{
		     		while(!spStack.isEmpty()) //closes all specimens node when a new specimens starts 
		     		{
		     			spStack.pop();
		     			xmlString.append("</node>");
		     		}
		     	    
		     		if(i == finalList.size()-1) //If last element & parent specimen
		     		{
		     			xmlString.append("<node id=\""+Constants.SPECIMEN+"_"+spId.toString()+"\" "+"name=\""+spLabel1+"\" "+"toolTip=\""+toolTipText+"\" "+"type=\""+Constants.SPECIMEN+"\" "+"collectionStatus=\""+spCollectionStatus+"\">");
		     		    xmlString.append("</node>");
		     		}
		     		else //If not the last element and parent specimen
		     		{
		     		    xmlString.append("<node id=\""+Constants.SPECIMEN+"_"+spId.toString()+"\" "+"name=\""+spLabel1+"\" "+"toolTip=\""+toolTipText+"\" "+"type=\""+Constants.SPECIMEN+"\" "+"collectionStatus=\""+spCollectionStatus+"\">");
		     		    spStack.push((Object[])finalList.get(i));
		     		}
		     	}
		         
		     	else
		     	{
		     		Object[] previousSp = (Object[])finalList.get(i-1);
		     		Long previousSpId = (Long)previousSp[0];
		     		
		     		//If immediate prevoius node is parent of current node 
		     		if(parentId.longValue() == previousSpId.longValue())
		     		{
		     			if(i != finalList.size()-1) //if not the last element in specimen list
		     			{
		     				Object[]nextSp = (Object[])finalList.get(i+1); //Getting next sp
		     			    Long nextSpPid = (Long)nextSp[2];
		     			    
		     			    if(nextSpPid != null)
		     			    {
		     			    	//Check if current specimen have children specimens
			     			    if(spId.longValue() == nextSpPid.longValue())
			     			    {
			     			    	xmlString.append("<node id=\""+Constants.SPECIMEN+"_"+spId.toString()+"\" "+"name=\""+spLabel1+"\" "+"toolTip=\""+toolTipText+"\" "+"type=\""+ Constants.SPECIMEN +"\" "+"collectionStatus=\""+spCollectionStatus+"\">");
			     			        spStack.push((Object[])finalList.get(i)); 
			     			    }
			     			    else //if current specimen doesn't have children specimens
			     			    {
			     			    	xmlString.append("<node id=\""+Constants.SPECIMEN+"_"+spId.toString()+"\" "+"name=\""+spLabel1+"\" "+"toolTip=\""+toolTipText+"\" "+"type=\""+ Constants.SPECIMEN +"\" "+"collectionStatus=\""+spCollectionStatus+"\"></node> ");
			     			    }
		     			    }
		     			    else
		     			    {
		     			    	xmlString.append("<node id=\""+Constants.SPECIMEN+"_"+spId.toString()+"\" "+"name=\""+spLabel1+"\" "+"toolTip=\""+toolTipText+"\" "+"type=\""+ Constants.SPECIMEN +"\" "+"collectionStatus=\""+spCollectionStatus+"\"></node> ");
		     			    }
		     			} 
		     			
		     			else //last element in specimen List
		     			{
		     			   xmlString.append("<node id=\""+Constants.SPECIMEN+"_"+spId.toString()+"\" "+"name=\""+spLabel1+"\" "+"toolTip=\""+toolTipText+"\" "+"type=\""+ Constants.SPECIMEN +"\" "+"collectionStatus=\""+spCollectionStatus+"\"></node> ");
		     			    while(!spStack.isEmpty())
		     			    {
		     			    	spStack.pop();
		     			    	xmlString.append("</node>");
		     			    }
		     			}
		     		}
		     		else //current node has parent but not the immediate prevoius node. Parent may be on the stack
		     		{
		     			Long peekSpecimenId2 = null;
		     			do
		     			{
		     				Object[] peekSp = (Object[])spStack.peek(); //Retrieving the peek element at stack 
		     				Long peekSpId = (Long)peekSp[0]; 
		     				if(parentId.longValue() == peekSpId.longValue())//If current node has parent as peek (last) element at stack 
		     				{
		     					if(i!=finalList.size()-1) //if not the last element
		     					{
		     						Object[] nextSp = (Object[])finalList.get(i+1);
		     					    Long nextSpPid = (Long)nextSp[2];
		     					    
		     					    //Here nextSpPid may be null
		     					    if(nextSpPid != null)
		     					    {
		     					    	//If it has children
			     					    if(spId.longValue() == nextSpPid.longValue())
			     					    {
			     					    	xmlString.append("<node id=\""+Constants.SPECIMEN+"_"+spId.toString()+"\" "+"name=\""+spLabel1+"\" "+"toolTip=\""+toolTipText+"\" "+"type=\""+ Constants.SPECIMEN +"\" "+"collectionStatus=\""+spCollectionStatus+"\">");
					     			        spStack.push((Object[])finalList.get(i)); 
			     					    }
			     					    else //if it doesn't has children
			     					    {
			     					    	xmlString.append("<node id=\""+Constants.SPECIMEN+"_"+spId.toString()+"\" "+"name=\""+spLabel1+"\" "+"toolTip=\""+toolTipText+"\" "+"type=\""+ Constants.SPECIMEN +"\" "+"collectionStatus=\""+spCollectionStatus+"\"></node> ");
			     					        break; //Note this break is imp
			     					    }
		     					    	
		     					    }
		     					    else // next node parent id is null then , then it's parent specimen
		     					    {
		     					    	xmlString.append("<node id=\""+Constants.SPECIMEN+"_"+spId.toString()+"\" "+"name=\""+spLabel1+"\" "+"toolTip=\""+toolTipText+"\" "+"type=\""+ Constants.SPECIMEN +"\" "+"collectionStatus=\""+spCollectionStatus+"\"></node> ");
		     					        break;
		     					    }
		     					    
		     					}
		     					else //If last element
		     					{
		     						xmlString.append("<node id=\""+Constants.SPECIMEN+"_"+spId.toString()+"\" "+"name=\""+spLabel1+"\" "+"toolTip=\""+toolTipText+"\" "+"type=\""+ Constants.SPECIMEN +"\" "+"collectionStatus=\""+spCollectionStatus+"\"></node> ");
				     			    while(!spStack.isEmpty())
				     			    {
				     			    	spStack.pop();
				     			    	xmlString.append("</node>");
				     			    }
				     			    
				     			    break; //If last element,& stack is empty, simply break
		     					}
		     					
		     				}
		     				else //If node has parent but at peek element of stack
		     				{
		     					//So iterate till, node has parent element at stack 
		     					Long peekSpecimenId1 = null;
		     					do 
		     					{
		     						spStack.pop();
		     						xmlString.append("</node>");
		     						
		     						Object[] peekSp1 = (Object[])spStack.peek(); //Retrieving the peek element at stack 
				     				peekSpecimenId1 = (Long)peekSp1[0];
		     					
		     					}while(parentId.longValue() != peekSpecimenId1.longValue() && !spStack.isEmpty());
		     				}
		     				
		     				if(!spStack.empty())
		     				{
		     					Object[] peekSp2 = (Object[])spStack.peek(); //Retrieving the peek element at stack 
			     				peekSpecimenId2 = (Long)peekSp2[0]; 
		     				}
		     		  }while(parentId.longValue() == peekSpecimenId2.longValue() && !spStack.empty());
		     				
		     		}
		     		
		     	}
		    }
	}
	/**
	 * Patch Id : FutureSCG_13
	 * Description : method to executeQuery
	 */
	/**
	 * Executes hql Query and returns the results.
	 * @param hql String hql
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	private List executeQuery(String hql) throws DAOException, ClassNotFoundException
	{
		HibernateDAO dao = (HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		dao.openSession(null);
		List list = dao.executeQuery(hql, null, false, null);
		dao.closeSession();
		return list;
	}
	
	private List executeHqlQuery(DAO dao,String hql) throws DAOException, ClassNotFoundException
	{
		List list = dao.executeQuery(hql, null, false, null);
		return list;
	}
	
	/**
	 * This function sets the data in QuertTreeNodeData object adds in a list of these nodes.
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
	
	//Commented out as it is not required after  Flex related chages
	/*private void setQueryTreeNode(String identifier, String objectName, String displayName, String parentIdentifier, String parentObjectName,
			String combinedParentIdentifier, String combinedParentObjectName, String activityStatus, String toolTipText, Vector vector)
	  {
		if (!activityStatus.equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			QueryTreeNodeData treeNode = new QueryTreeNodeData();
			treeNode.setIdentifier(identifier);
			treeNode.setObjectName(objectName);
			treeNode.setDisplayName(displayName);
			treeNode.setParentIdentifier(parentIdentifier);
			treeNode.setParentObjectName(parentObjectName);
			treeNode.setCombinedParentIdentifier(combinedParentIdentifier);
			treeNode.setCombinedParentObjectName(combinedParentObjectName);
			treeNode.setToolTipText(toolTipText);
			vector.add(treeNode);
		}
	}*/
	/**
	 * Patch Id : FutureSCG_14
	 * Description : method to getToolTipText
	 */
	/**
	 * Generates tooltip for SCGs in c based views
	 * @param eventDays no of days 
	 * @param collectionPointLabel label of CPE
	 * @param receivedDate received date for specimens
	 * @return String Tooltip text
	 */
	private static String getToolTipText(String eventDays, String collectionPointLabel, String receivedDate)
	{
		StringBuffer toolTipText= new StringBuffer();
		toolTipText.append("Event point : ");
		toolTipText.append(eventDays);
		toolTipText.append(";  Collection point label : ");
		toolTipText.append(collectionPointLabel);
		if(receivedDate != null)
		{
			toolTipText.append(";  Received date : ");
			toolTipText.append(receivedDate);
		}
		return toolTipText.toString();
	}

	//Mandar : 15-Jan-07 For Consent Tracking Withdrawal -------- start
	/*
	 * This method verifies and updates SCG and child elements for withdrawn consents
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
				//break;
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
			
			scgName=(String) scgList.get(0);
		}
		
		dao.closeSession();
		return scgName;
	}

	public Map getSpecimenList(Long id) throws DAOException{
	
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			dao.openSession(null);
			List scgList = dao.retrieve(SpecimenCollectionGroup.class.getName(), "id", id);

			if (scgList != null && !scgList.isEmpty())
			{
				SpecimenCollectionGroup specimenCollectionGroup=
					(SpecimenCollectionGroup)scgList.get(0);
				Collection specimenCollection = 
					specimenCollectionGroup.getSpecimenCollection();
				return CollectionProtocolUtil.getSpecimensMap(specimenCollection, "E1");
			}
			else
			{
				return null;
			}
		}
		catch(DAOException exception)
		{
			throw exception;
		}
		finally
		{
			dao.commit();
			dao.closeSession();
		}
	}
}