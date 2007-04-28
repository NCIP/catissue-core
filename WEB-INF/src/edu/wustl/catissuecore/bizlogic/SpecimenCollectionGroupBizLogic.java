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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.domain.ClinicalReport;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.integration.IntegrationManager;
import edu.wustl.catissuecore.integration.IntegrationManagerFactory;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.tree.QueryTreeNodeData;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.User;

/**
 * UserHDAO is used to add user information into the database using Hibernate.
 * @author kapil_kaveeshwar
 */
public class SpecimenCollectionGroupBizLogic extends IntegrationBizLogic
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

		Object siteObj = dao.retrieve(Site.class.getName(), specimenCollectionGroup.getSite().getId());
		if (siteObj != null)
		{
			// check for closed Site
			checkStatus(dao, specimenCollectionGroup.getSite(), "Site");

			specimenCollectionGroup.setSite((Site) siteObj);
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

		setClinicalReport(dao, specimenCollectionGroup);
		setCollectionProtocolRegistration(dao, specimenCollectionGroup, null);

		dao.insert(specimenCollectionGroup, sessionDataBean, true, true);
		if (specimenCollectionGroup.getClinicalReport() != null)
			dao.insert(specimenCollectionGroup.getClinicalReport(), sessionDataBean, true, true);

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
		if(spEventColl == null || spEventColl.isEmpty())
		{
			setDefaultEvents(specimenCollectionGroup,sessionDataBean);
		}
		// Check for different closed site
		if (!specimenCollectionGroup.getSite().getId().equals(oldspecimenCollectionGroup.getSite().getId()))
		{
			checkStatus(dao, specimenCollectionGroup.getSite(), "Site");
		}
		//site check complete

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
		//CollectionProtocol check complete.

		setCollectionProtocolRegistration(dao, specimenCollectionGroup, oldspecimenCollectionGroup);

		dao.update(specimenCollectionGroup, sessionDataBean, true, true, false);
		dao.update(specimenCollectionGroup.getClinicalReport(), sessionDataBean, true, true, false);
		/**
		 * Name : Ashish Gupta
		 * Reviewer Name : Sachin Lale 
		 * Bug ID: 2741
		 * Patch ID: 2741_6	 
		 * Description: Method to update events in all specimens related to this scg
		 */
//		Populating Events in all specimens
		if(specimenCollectionGroup.isApplyEventsToSpecimens())
		{
			updateEvents(specimenCollectionGroup,oldspecimenCollectionGroup,dao,sessionDataBean);
		}
		//Audit.
		dao.audit(obj, oldObj, sessionDataBean, true);
		SpecimenCollectionGroup oldSpecimenCollectionGroup = (SpecimenCollectionGroup) oldObj;
		dao.audit(specimenCollectionGroup.getClinicalReport(), oldspecimenCollectionGroup.getClinicalReport(), sessionDataBean, true);

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
	private void setDefaultEvents(SpecimenCollectionGroup specimenCollectionGroup,SessionDataBean sessionDataBean)
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
	private void updateEvents(SpecimenCollectionGroup specimenCollectionGroup,SpecimenCollectionGroup oldspecimenCollectionGroup,DAO dao,SessionDataBean sessionDataBean) throws UserNotAuthorizedException,DAOException
	{			
		CollectionEventParameters scgCollectionEventParameters = null;
		ReceivedEventParameters scgReceivedEventParameters = null;
		Collection newEventColl = specimenCollectionGroup.getSpecimenEventParametersCollection();
		if(newEventColl != null && !newEventColl.isEmpty())
		{
			Iterator newEventCollIter = newEventColl.iterator();
			while(newEventCollIter.hasNext())
			{
				Object newEventCollObj = newEventCollIter.next();
				if(newEventCollObj instanceof CollectionEventParameters)
				{
					scgCollectionEventParameters = (CollectionEventParameters)newEventCollObj;
					continue;
				}
				else if(newEventCollObj instanceof ReceivedEventParameters)
				{
					scgReceivedEventParameters = (ReceivedEventParameters)newEventCollObj;						
				}
			}
		}			
		//populateEventsInSpecimens(oldspecimenCollectionGroup,)
		Collection specimenColl = oldspecimenCollectionGroup.getSpecimenCollection();
		if(specimenColl != null && !specimenColl.isEmpty())
		{
			SpecimenEventParametersBizLogic specimenEventParametersBizLogic = (SpecimenEventParametersBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID);
			Iterator iter = specimenColl.iterator();
			while(iter.hasNext())
			{
				Specimen specimen = (Specimen)iter.next();
				Collection eventColl = specimen.getSpecimenEventCollection();
				if(eventColl != null && !eventColl.isEmpty())
				{
					Iterator eventIter = eventColl.iterator();
					while(eventIter.hasNext())
					{
						Object eventObj = eventIter.next();
						if(eventObj instanceof CollectionEventParameters)
						{			
							CollectionEventParameters collectionEventParameters = (CollectionEventParameters)eventObj;
							CollectionEventParameters newcollectionEventParameters =populateCollectionEventParameters(eventObj,scgCollectionEventParameters,collectionEventParameters);
							specimenEventParametersBizLogic.update(dao, newcollectionEventParameters, collectionEventParameters, sessionDataBean);
							continue;
						}
						else if(eventObj instanceof ReceivedEventParameters)
						{
							ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters)eventObj;
							ReceivedEventParameters newReceivedEventParameters = populateReceivedEventParameters(receivedEventParameters,scgReceivedEventParameters);						
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
	private CollectionEventParameters populateCollectionEventParameters(Object eventObj,CollectionEventParameters scgCollectionEventParameters,CollectionEventParameters collectionEventParameters)
	{		
		//CollectionEventParameters newcollectionEventParameters = collectionEventParameters;
		CollectionEventParameters newcollectionEventParameters = new CollectionEventParameters(); 
		newcollectionEventParameters.setCollectionProcedure(scgCollectionEventParameters.getCollectionProcedure());
		newcollectionEventParameters.setContainer(scgCollectionEventParameters.getContainer());
		newcollectionEventParameters.setTimestamp(scgCollectionEventParameters.getTimestamp());
		newcollectionEventParameters.setUser(scgCollectionEventParameters.getUser());		

		newcollectionEventParameters.setComments(scgCollectionEventParameters.getComments());
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
	private ReceivedEventParameters populateReceivedEventParameters(ReceivedEventParameters receivedEventParameters,ReceivedEventParameters scgReceivedEventParameters)
	{
		//ReceivedEventParameters newReceivedEventParameters = receivedEventParameters;
		ReceivedEventParameters newReceivedEventParameters = new ReceivedEventParameters();
		newReceivedEventParameters.setReceivedQuality(scgReceivedEventParameters.getReceivedQuality());
		newReceivedEventParameters.setTimestamp(scgReceivedEventParameters.getTimestamp());
		newReceivedEventParameters.setUser(scgReceivedEventParameters.getUser());	

		newReceivedEventParameters.setId(receivedEventParameters.getId());								
		newReceivedEventParameters.setComments(scgReceivedEventParameters.getComments());
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

	private void setClinicalReport(DAO dao, SpecimenCollectionGroup specimenCollectionGroup) throws DAOException
	{
		ClinicalReport clinicalReport = specimenCollectionGroup.getClinicalReport();
		ParticipantMedicalIdentifier participantMedicalIdentifier = clinicalReport.getParticipantMedicalIdentifier();
		if (participantMedicalIdentifier != null)
		{
			List list = dao.retrieve(ParticipantMedicalIdentifier.class.getName(), Constants.SYSTEM_IDENTIFIER, participantMedicalIdentifier.getId());
			if (!list.isEmpty())
			{
				specimenCollectionGroup.getClinicalReport().setParticipantMedicalIdentifier((ParticipantMedicalIdentifier) list.get(0));
			}
		}
	}

	public void disableRelatedObjects(DAO dao, Long collProtRegIDArr[]) throws DAOException
	{
		List listOfSubElement = super.disableObjects(dao, SpecimenCollectionGroup.class, "collectionProtocolRegistration",
				"CATISSUE_SPECIMEN_COLL_GROUP", "COLLECTION_PROTOCOL_REG_ID", collProtRegIDArr);
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

		if (group.getClinicalReport() == null)
		{
			group.setClinicalReport(new ClinicalReport());
		}

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

		if (group.getSite() == null || group.getSite().getId() == null || group.getSite().getId() == 0)
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
		/* Bug ID: 4165
	 	 * Patch ID: 4165_11	 
	 	 * Description: Validation added to check incorrect events added through API
		 */
//		Events Validation
		if (group.getSpecimenEventParametersCollection() != null)
		{
			Iterator specimenEventCollectionIterator = group.getSpecimenEventParametersCollection().iterator();
			while (specimenEventCollectionIterator.hasNext())
			{				
				Object eventObject = specimenEventCollectionIterator.next();
				EventsUtil.validateEventsObject(eventObject,validator);
			}
		}	

		return true;
	}

	/**
	 * This method fetches linked data from integrated application i.e. CAE/caTies.
	 */
	public List getLinkedAppData(Long id, String applicationID)
	{
		Logger.out.debug("In getIntegrationData() of SCGBizLogic ");

		Logger.out.debug("ApplicationName in getIntegrationData() of SCGBizLogic==>" + applicationID);

		long identifiedPathologyReportId = 0;

		try
		{
			//JDBC call to get matching identifier from database
			Class.forName("org.gjt.mm.mysql.Driver");

			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/catissuecore", "catissue_core", "catissue_core");

			Statement stmt = connection.createStatement();

			String clinicalReportQuery = "select CLINICAL_REPORT_ID from CATISSUE_SPECIMEN_COLL_GROUP where IDENTIFIER=" + id;

			ResultSet clinicalReportResultSet = stmt.executeQuery(clinicalReportQuery);

			long clinicalReportId = 0;
			while (clinicalReportResultSet.next())
			{
				clinicalReportId = clinicalReportResultSet.getLong(1);
				break;
			}
			Logger.out.debug("ClinicalReportId==>" + clinicalReportId);
			clinicalReportResultSet.close();
			if (clinicalReportId == 0)
			{
				List exception = new ArrayList();
				exception.add("ClinicalReportId is not available for SpecimenCollectionGroup");
				return exception;
			}

			String identifiedPathologyReportIdQuery = "select IDENTIFIER from CATISSUE_IDENTIFIED_PATHOLOGY_REPORT where CLINICAL_REPORT_ID="
				+ clinicalReportId;

			ResultSet identifiedPathologyReportResultSet = stmt.executeQuery(identifiedPathologyReportIdQuery);

			while (identifiedPathologyReportResultSet.next())
			{
				identifiedPathologyReportId = identifiedPathologyReportResultSet.getLong(1);
				break;
			}
			Logger.out.debug("IdentifiedPathologyReportId==>" + identifiedPathologyReportId);
			identifiedPathologyReportResultSet.close();
			if (identifiedPathologyReportId == 0)
			{
				List exception = new ArrayList();
				exception.add("IdentifiedPathologyReportId is not available for linked ClinicalReportId");
				return exception;
			}

			stmt.close();

			connection.close();

		}
		catch (Exception e)
		{
			Logger.out.debug("JDBC Exception==>" + e.getMessage());
		}

		IntegrationManager integrationManager = IntegrationManagerFactory.getIntegrationManager(applicationID);

		return integrationManager.getLinkedAppData(new SpecimenCollectionGroup(), new Long(identifiedPathologyReportId));
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
	public Vector getSCGTreeForCPBasedView(Long cpId, Long participantId) throws DAOException, ClassNotFoundException {
		Vector treeData = new Vector();
		String hql = "select  cpe.id, cpe.studyCalendarEventPoint,cpe.collectionPointLabel from " + CollectionProtocolEvent.class.getName()
		+ " as cpe where cpe.collectionProtocol.id= "+ cpId.toString() +" order by cpe.studyCalendarEventPoint";
		List cpeList = executeQuery(hql);
		for(int count = 0; count < cpeList.size() ; count++)
		{
			Object[] obj = (Object[]) cpeList.get(count);
			Long eventId = (Long)obj[0];
			Double eventPoint = (Double) obj[1];
			String collectionPointLabel = (String)obj[2];
			List scgList = getSCGsForCPRAndEventId(eventId, cpId,participantId);
			if (scgList != null && !scgList.isEmpty())
			{
				createTreeNodeForExistingSCG(treeData, eventPoint, collectionPointLabel, scgList);
			}
			else
			{
				createTreeNodeForFutureSCG(treeData, eventId, eventPoint, collectionPointLabel);
			}
		}
		return treeData;
	}
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
	private void createTreeNodeForFutureSCG(Vector treeData, Long eventId, Double eventPoint, String collectionPointLabel) {
		Long futureSCGId = new Long(0);
		String futureSCGName = eventPoint + "_" +collectionPointLabel;
		String scgActivityStatus = Constants.ACTIVITY_STATUS_ACTIVE;
		String toolTipText = getToolTipText(eventPoint.toString(),collectionPointLabel,null);
		setQueryTreeNode(futureSCGId.toString()+":"+eventId.toString()+":"+Constants.FUTURE_SCG, Constants.SPECIMEN_COLLECTION_GROUP, futureSCGName, "0", null, null, null, scgActivityStatus, toolTipText,treeData);
	}
	/**
	 * Patch Id : FutureSCG_10
	 * Description : method to create TreeNode For ExistingSCG
	 */
	/**
	 * Creates a tree node for existing SCgs
	 * @param treeData data structure for storing tree data
	 * @param eventPoint event point in no of days
	 * @param collectionPointLabel String label of collection event point
	 * @param scgList list of scgs
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	private void createTreeNodeForExistingSCG(Vector treeData, Double eventPoint, String collectionPointLabel, List scgList) throws DAOException, ClassNotFoundException {
		for (int i = 0; i < scgList.size(); i++)
		{
			Object[] obj1 = (Object[]) scgList.get(i);
			Long scgId = (Long) obj1[0];
			String scgNodeLabel = (String) obj1[1];
			String scgActivityStatus = (String) obj1[2];
			String colName = "id";	
			List scgListFromDB = retrieve(SpecimenCollectionGroup.class.getName(), colName, scgId);
			if(scgListFromDB != null && !scgListFromDB.isEmpty())
			{
				SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup)scgListFromDB.get(0);
				Collection eventsColl = specimenCollectionGroup.getSpecimenEventParametersCollection();
				if(eventsColl != null && !eventsColl.isEmpty())
				{
					String receivedDate = "";
					Iterator iter = eventsColl.iterator();
					while(iter.hasNext())
					{
						Object temp = iter.next();
						if(temp instanceof ReceivedEventParameters)
						{
							ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters)temp;
							receivedDate = Utility.parseDateToString(receivedEventParameters.getTimestamp(),"yyyy-MM-dd");
							scgNodeLabel = eventPoint + "_" +collectionPointLabel + "_" + receivedDate; 
						}    					
					}
					String toolTipText = getToolTipText(eventPoint.toString(),collectionPointLabel,receivedDate);
					setQueryTreeNode(scgId.toString(), Constants.SPECIMEN_COLLECTION_GROUP, scgNodeLabel, "0", null, null, null, scgActivityStatus, toolTipText,treeData);
					addSpecimenNodesToSCGTree(treeData,scgId);
				}
			}
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
		String hql = "select scg.id,scg.name,scg.activityStatus from "
			+ SpecimenCollectionGroup.class.getName()
			+ " as scg where scg.collectionProtocolRegistration.id = (select cpr.id from "
			+ CollectionProtocolRegistration.class.getName() +" as cpr where cpr.collectionProtocol.id = "
			+ cpId + " and cpr.participant.id = "+participantId 
			+") and scg.collectionProtocolEvent.id = "+eventId ;
		List list = executeQuery(hql);
		return list;
	}
	/**
	 * Patch Id : FutureSCG_12
	 * Description : method to add SpecimenNodes To SCGTree
	 */
	/**
	 * Adds specimen nodes to SCG node
	 * @param treeData vector to store tree data
	 * @param scgId id of specimen collection group
	 */
	private void addSpecimenNodesToSCGTree( Vector treeData, Long scgId) throws DAOException, ClassNotFoundException
	{
		String hql = "select sp.id,sp.label,sp.parentSpecimen.id,sp.activityStatus from "
			+ Specimen.class.getName()
			+ " as sp where sp.specimenCollectionGroup.id = "+scgId;
		List specimenList = executeQuery(hql);
		for (int j = 0; j < specimenList.size(); j++)
		{
			Object[] obj = (Object[])specimenList.get(j);
			Long spId1 = (Long) obj[0];
			String spLabel1 = (String) obj[1];
			Long parentSpecimenId = (Long) obj[2];
			String spActivityStatus = (String) obj[3];
			if (spId1 != null)
			{
				if (parentSpecimenId != null)
				{
					setQueryTreeNode(spId1.toString(), Constants.SPECIMEN, spLabel1, parentSpecimenId.toString(), Constants.SPECIMEN,
							null, null, spActivityStatus,spLabel1, treeData);
				}
				else
				{
					setQueryTreeNode(spId1.toString(), Constants.SPECIMEN, spLabel1, scgId.toString(),
							Constants.SPECIMEN_COLLECTION_GROUP, null, null, spActivityStatus,spLabel1, treeData);
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
	private List executeQuery(String hql) throws DAOException, ClassNotFoundException {
		HibernateDAO dao = (HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		dao.openSession(null);
		List list = dao.executeQuery(hql, null, false, null);
		dao.closeSession();
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
	private void setQueryTreeNode(String identifier, String objectName, String displayName, String parentIdentifier, String parentObjectName,
			String combinedParentIdentifier, String combinedParentObjectName, String activityStatus, String toolTipText ,Vector vector)
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
	}
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
		toolTipText.append("\\n  Collection point label : ");
		toolTipText.append(collectionPointLabel);
		if(receivedDate != null)
		{
			toolTipText.append("\\n  Received date : ");
			toolTipText.append(receivedDate);
		}
		return toolTipText.toString();
	}

}