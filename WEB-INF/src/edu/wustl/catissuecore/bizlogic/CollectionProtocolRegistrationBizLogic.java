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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.domain.AbstractDomainObject;
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
		checkStatus(dao, collectionProtocolRegistration.getCollectionProtocol(), "Collection Protocol" );

		// Check for closed Participant
		checkStatus(dao, collectionProtocolRegistration.getParticipant(), "Participant" );
		checkUniqueConstraint(dao,collectionProtocolRegistration, null);
		registerParticipantAndProtocol(dao,collectionProtocolRegistration, sessionDataBean);
		
		dao.insert(collectionProtocolRegistration, sessionDataBean, true, true);
		
		try
        {
            SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,
            		getProtectionObjects(collectionProtocolRegistration),
					getDynamicGroups(collectionProtocolRegistration));
        }
		catch (SMException e)
        {
			throw handleSMException(e);
        }
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
		if(!collectionProtocolRegistration.getCollectionProtocol().getSystemIdentifier().equals( oldCollectionProtocolRegistration.getCollectionProtocol().getSystemIdentifier()))
		{
			checkStatus(dao,collectionProtocolRegistration.getCollectionProtocol(), "Collection Protocol" );
		}

		// -- Check for different Participants and closed participant
		// old and new values are not null
		if(collectionProtocolRegistration.getParticipant() != null  && 
				oldCollectionProtocolRegistration.getParticipant() != null &&
				collectionProtocolRegistration.getParticipant().getSystemIdentifier() != null  && 
				oldCollectionProtocolRegistration.getParticipant().getSystemIdentifier()  != null)
		{
			if(!collectionProtocolRegistration.getParticipant().getSystemIdentifier().equals( oldCollectionProtocolRegistration.getParticipant().getSystemIdentifier()))
			{					
				checkStatus(dao,collectionProtocolRegistration.getParticipant(), "Participant" );
			}		
		}
		
		//when old participant is null and new is not null
		if(collectionProtocolRegistration.getParticipant() != null  && oldCollectionProtocolRegistration.getParticipant() == null)
		{
			if(collectionProtocolRegistration.getParticipant().getSystemIdentifier()  != null )
			{
				checkStatus(dao, collectionProtocolRegistration.getParticipant(), "Participant" );
			}
		}
		
		/**
		 * Case: While updating the registration if the participant is deselected then 
		 * we need to maintain the link between registration and participant by adding a dummy participant
		 * for query module. 
		 */
		if(collectionProtocolRegistration.getParticipant() == null)
		{
			Participant oldParticipant = oldCollectionProtocolRegistration.getParticipant();
			
			//Check for if the older participant was also a dummy, if true use the same participant, 
			//otherwise create an another dummay participant
			if(oldParticipant != null)
			{
				String firstName = Utility.toString(oldParticipant.getFirstName());
				String lastName = Utility.toString(oldParticipant.getLastName());
				String birthDate = Utility.toString(oldParticipant.getBirthDate());
				String ssn = Utility.toString(oldParticipant.getSocialSecurityNumber());
				if(firstName.trim().length() == 0 && lastName.trim().length() == 0 && birthDate.trim().length() == 0 && ssn.trim().length() == 0)
				{
					collectionProtocolRegistration.setParticipant(oldParticipant);
				}
			} // oldpart != null
			else  
			{
				//create dummy participant.
				Participant participant = addDummyParticipant(dao, sessionDataBean);
				collectionProtocolRegistration.setParticipant(participant);
			}
		}
		checkUniqueConstraint(dao,collectionProtocolRegistration, oldCollectionProtocolRegistration);
		//Update registration
		dao.update(collectionProtocolRegistration, sessionDataBean, true, true, false);
		
		//Audit.
		dao.audit(obj, oldObj, sessionDataBean, true);
		
		//Disable all specimen Collection group under this registration. 
		Logger.out.debug("collectionProtocolRegistration.getActivityStatus() "+collectionProtocolRegistration.getActivityStatus());
		if(collectionProtocolRegistration.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("collectionProtocolRegistration.getActivityStatus() "+collectionProtocolRegistration.getActivityStatus());
			Long collectionProtocolRegistrationIDArr[] = {collectionProtocolRegistration.getSystemIdentifier()};
			
			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			bizLogic.disableRelatedObjects(dao,collectionProtocolRegistrationIDArr);
		}
	}
	
	private Set getProtectionObjects(AbstractDomainObject obj)
    {
        Set protectionObjects = new HashSet();
        
        CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;
        protectionObjects.add(collectionProtocolRegistration);
        
		Participant participant = null;
		//Case of registering Participant on its participant ID
		if(collectionProtocolRegistration.getParticipant()!=null)
		{
		    protectionObjects.add(collectionProtocolRegistration.getParticipant());
		}
		
        Logger.out.debug(protectionObjects.toString());
        return protectionObjects;
    }
	
    private String[] getDynamicGroups(AbstractDomainObject obj)
    {
        String[] dynamicGroups=null;
        CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;
        dynamicGroups = new String[1];
        dynamicGroups[0] = Constants.getCollectionProtocolPGName(collectionProtocolRegistration.getCollectionProtocol().getSystemIdentifier());
        return dynamicGroups;
    }
    
    private void registerParticipantAndProtocol(DAO dao, CollectionProtocolRegistration collectionProtocolRegistration,
    		    		SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
    	//Case of registering Participant on its participant ID
    	Participant participant = null;
    	
		if(collectionProtocolRegistration.getParticipant()!=null)
		{
			Object participantObj = dao.retrieve(Participant.class.getName(), collectionProtocolRegistration.getParticipant().getSystemIdentifier());
			
			if (participantObj != null )
			{
				participant = (Participant)participantObj;
			}
		}
		else
		{
			participant = addDummyParticipant(dao, sessionDataBean);
		}
		
		collectionProtocolRegistration.setParticipant(participant);

		Object collectionProtocolObj = dao.retrieve(CollectionProtocol.class.getName(),  collectionProtocolRegistration.getCollectionProtocol().getSystemIdentifier());
		if (collectionProtocolObj != null)
		{
			CollectionProtocol collectionProtocol = (CollectionProtocol)collectionProtocolObj;
			collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);
		}
	}
    
    /** Add a dummy participant when participant is registed to a protocol using 
	 * participant protocol id */
    private Participant addDummyParticipant(DAO dao,SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
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
		
		return participant;
    }
    
    /**
     * Disable all the related collection protocol regitration for a given array of participant ids. 
     **/
    public void disableRelatedObjectsForParticipant(DAO dao, Long participantIDArr[])throws DAOException 
    {
    	List listOfSubElement = super.disableObjects(dao, CollectionProtocolRegistration.class, "participant", 
    			"CATISSUE_COLL_PROT_REG", "PARTICIPANT_ID", participantIDArr);
    	if(!listOfSubElement.isEmpty())
    	{
    		SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
    		bizLogic.disableRelatedObjects(dao,Utility.toLongArray(listOfSubElement));
    	}
    }
    
    /**
     * Disable all the related collection protocol regitrations for a given array of collection protocol ids. 
     **/
    public void disableRelatedObjectsForCollectionProtocol(DAO dao, Long collectionProtocolIDArr[])throws DAOException 
    {
    	List listOfSubElement = super.disableObjects(dao, CollectionProtocolRegistration.class, "collectionProtocol", 
    			"CATISSUE_COLL_PROT_REG", "COLLECTION_PROTOCOL_ID", collectionProtocolIDArr);
    	if(!listOfSubElement.isEmpty())
    	{
			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			bizLogic.disableRelatedObjects(dao,Utility.toLongArray(listOfSubElement));
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
    public void assignPrivilegeToRelatedObjectsForParticipant(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId, boolean assignToUser, boolean assignOperation) throws SMException, DAOException
    {
        List listOfSubElement = super.getRelatedObjects(dao, CollectionProtocolRegistration.class, "participant", 
    			 objectIds);
        
    	if(!listOfSubElement.isEmpty())
    	{
    	    super.setPrivilege(dao,privilegeName,CollectionProtocolRegistration.class,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser, assignOperation);
    		SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
    		bizLogic.assignPrivilegeToRelatedObjects(dao,privilegeName,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser, assignOperation);
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
    public void assignPrivilegeToRelatedObjectsForCP(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId, boolean assignToUser, boolean assignOperation)throws SMException, DAOException
    {
        List listOfSubElement = super.getRelatedObjects(dao, CollectionProtocolRegistration.class, "collectionProtocol",objectIds);
    	if(!listOfSubElement.isEmpty())
    	{
    	    super.setPrivilege(dao,privilegeName,CollectionProtocolRegistration.class,Utility.toLongArray(listOfSubElement),userId,roleId, assignToUser, assignOperation);
			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			bizLogic.assignPrivilegeToRelatedObjects(dao,privilegeName,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser, assignOperation);
			
			ParticipantBizLogic participantBizLogic = (ParticipantBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.PARTICIPANT_FORM_ID);
			participantBizLogic.assignPrivilegeToRelatedObjectsForCPR(dao,privilegeName,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser, assignOperation);
    	}
    }
    
    /**
     * @see edu.wustl.common.bizlogic.IBizLogic#setPrivilege(DAO, String, Class, Long[], Long, String, boolean)
     */
    public void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser, boolean assignOperation) throws SMException, DAOException
    {
	    super.setPrivilege(dao,privilegeName,objectType,objectIds,userId, roleId, assignToUser, assignOperation);
	    
	    SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
		bizLogic.assignPrivilegeToRelatedObjects(dao,privilegeName,objectIds,userId, roleId, assignToUser,assignOperation);
		
		ParticipantBizLogic participantBizLogic = (ParticipantBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.PARTICIPANT_FORM_ID);
		participantBizLogic.assignPrivilegeToRelatedObjectsForCPR(dao,privilegeName,objectIds,userId, roleId, assignToUser,assignOperation);
	
	}
    
    /**
     * Overriding the parent class's method to validate the enumerated attribute values
     */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
    {
		CollectionProtocolRegistration registration = (CollectionProtocolRegistration)obj;

		if(operation.equals(Constants.ADD))
		{
			if(!Constants.ACTIVITY_STATUS_ACTIVE.equals(registration.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.active.errMsg"));
			}
		}
		else
		{
			if(!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES,registration.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
			}
		}
		
		return true;
    }
	 public void  checkUniqueConstraint(DAO dao, CollectionProtocolRegistration collectionProtocolRegistration,
	    		CollectionProtocolRegistration oldcollectionProtocolRegistration) throws DAOException
	    {
	    	CollectionProtocol objCollectionProtocol = collectionProtocolRegistration.getCollectionProtocol();
	    	String sourceObjectName = collectionProtocolRegistration.getClass().getName();
	    	String[] selectColumns=null;
	    	String[] whereColumnName=null;
	    	String[] whereColumnCondition =new String[]{"=","="};
	    	Object[] whereColumnValue=null;
	    	String arguments[]=null;
	    	String errMsg="";
	    	// check for update opeartion and old values equals to new values
	    	int count =0;
	    	if(oldcollectionProtocolRegistration!=null)
	    	{
	    		if(collectionProtocolRegistration.getParticipant()!=null&&oldcollectionProtocolRegistration.getParticipant()!=null)
	    		{
		    		if(collectionProtocolRegistration.getParticipant().getSystemIdentifier().equals(oldcollectionProtocolRegistration.getParticipant().getSystemIdentifier()))
		    		{
		    			count++;
		    		}
		    		if(collectionProtocolRegistration.getCollectionProtocol().getSystemIdentifier().equals(oldcollectionProtocolRegistration.getCollectionProtocol().getSystemIdentifier()))
		    		{
		    			count++;
		    		}
	    		}	
	    		else if(collectionProtocolRegistration.getProtocolParticipantIdentifier()!=null&&oldcollectionProtocolRegistration.getProtocolParticipantIdentifier()!=null)
	    		{
	    			if(collectionProtocolRegistration.getProtocolParticipantIdentifier().equals(oldcollectionProtocolRegistration.getProtocolParticipantIdentifier()))
	    			{
	    				count++;
	    			}
	    			if(collectionProtocolRegistration.getCollectionProtocol().getSystemIdentifier().equals(oldcollectionProtocolRegistration.getCollectionProtocol().getSystemIdentifier()))
		    		{
		    			count++;
		    		}
	    		}
	    		// if count=0 return i.e. old values equals new values 
	    		if(count==2)
	    			return;
	    	}
	    	if(collectionProtocolRegistration.getParticipant()!=null)
	    	{
	    		// build query for collectionProtocol_id AND participant_id
	    		Participant objParticipant = collectionProtocolRegistration.getParticipant();
	    		selectColumns = new String[]{"collectionProtocol.systemIdentifier","participant.systemIdentifier"};
	    		whereColumnName = new String[]{"collectionProtocol.systemIdentifier","participant.systemIdentifier"};
	    		whereColumnValue = new Object[]{objCollectionProtocol.getSystemIdentifier(),objParticipant.getSystemIdentifier()};
	    		arguments = new String[]{"Collection Protocol Registration ","COLLECTION_PROTOCOL_ID,PARTICIPANT_ID"};
	    	}
	    	else
	    	{
//	    		 build query for collectionProtocol_id AND protocol_participant_id
	    		selectColumns = new String[]{"collectionProtocol.systemIdentifier","protocolParticipantIdentifier"};
	    		whereColumnName = new String[]{"collectionProtocol.systemIdentifier","protocolParticipantIdentifier"};
	    		whereColumnValue = new Object[]{objCollectionProtocol.getSystemIdentifier(),collectionProtocolRegistration.getProtocolParticipantIdentifier()};
	    		arguments = new String[]{"Collection Protocol Registration ","COLLECTION_PROTOCOL_ID,PROTOCOL_PARTICIPANT_ID"};
	    	}
	    	List l = dao.retrieve(sourceObjectName,selectColumns,whereColumnName,whereColumnCondition,whereColumnValue,Constants.AND_JOIN_CONDITION);
	    	if(l.size()>0)
	    	{
	    		// if list is not empty the Constraint Violation occurs
	    		Logger.out.debug("Unique Constraint Violated: " + l.get(0));
	    		errMsg = new DefaultExceptionFormatter().getErrorMessage("Err.ConstraintViolation",arguments);
	    		Logger.out.debug("Unique Constraint Violated: " + errMsg);
	    		throw new DAOException(errMsg);
	    	}
	    	else
	    	{
	    		Logger.out.debug("Unique Constraint Passed");
	    	}
	    }
}