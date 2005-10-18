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

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
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
		
		registerParticipantAndProtocol(dao,collectionProtocolRegistration, sessionDataBean);
		
		dao.insert(collectionProtocolRegistration, sessionDataBean, true, true);
		
		try
        {
            SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,getProtectionObjects(collectionProtocolRegistration),getDynamicGroups(collectionProtocolRegistration));
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
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
			
			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			bizLogic.disableRelatedObjects(dao,collectionProtocolRegistrationIDArr);
		}
	}

	public Set getProtectionObjects(AbstractDomainObject obj)
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

    public String[] getDynamicGroups(AbstractDomainObject obj)
    {
        String[] dynamicGroups=null;
        CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;
        dynamicGroups = new String[1];
        dynamicGroups[0] = Constants.getCollectionProtocolPGName(collectionProtocolRegistration.getCollectionProtocol().getSystemIdentifier());
        return dynamicGroups;
        
    }
    
    private void registerParticipantAndProtocol(DAO dao, CollectionProtocolRegistration collectionProtocolRegistration, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
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
			participant = new Participant();
			
			participant.setLastName("");
			participant.setFirstName("");
			participant.setMiddleName("");
			participant.setSocialSecurityNumber(null);
			participant.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
			
			dao.insert(participant, sessionDataBean, true, true);
		}
		
		collectionProtocolRegistration.setParticipant(participant);

		Object collectionProtocolObj = dao.retrieve(CollectionProtocol.class.getName(),  collectionProtocolRegistration.getCollectionProtocol().getSystemIdentifier());
		if (collectionProtocolObj != null)
		{
			CollectionProtocol collectionProtocol = (CollectionProtocol)collectionProtocolObj;
			collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);
		}
	}
    
    /**
     * Disable all the related collection protocol regitration for a given array of participant ids. 
     **/
    public void disableRelatedObjectsForParticipant(DAO dao, Long participantIDArr[])throws DAOException 
    {
    	List listOfSubElement = super.disableObjects(dao, CollectionProtocolRegistration.class, "participant", 
    			"CATISSUE_COLLECTION_PROTOCOL_REGISTRATION", "PARTICIPANT_ID", participantIDArr);
    	if(!listOfSubElement.isEmpty())
    	{
    		SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
    		bizLogic.disableRelatedObjects(dao,Utility.toLongArray(listOfSubElement));
    	}
    }
    
    /**
     * Disable all the related collection protocol regitrations for a given array of collection protocol ids. 
     **/
    public void disableRelatedObjectsForCollectionProtocol(DAO dao, Long collectionProtocolIDArr[])throws DAOException 
    {
    	List listOfSubElement = super.disableObjects(dao, CollectionProtocolRegistration.class, "collectionProtocol", 
    			"CATISSUE_COLLECTION_PROTOCOL_REGISTRATION", "COLLECTION_PROTOCOL_ID", collectionProtocolIDArr);
    	if(!listOfSubElement.isEmpty())
    	{
			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
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
    public void assignPrivilegeToRelatedObjectsForParticipant(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId, boolean assignToUser) throws SMException, DAOException
    {
        List listOfSubElement = super.getRelatedObjects(dao, CollectionProtocolRegistration.class, "participant", 
    			 objectIds);
        
    	if(!listOfSubElement.isEmpty())
    	{
    	    super.setPrivilege(dao,privilegeName,CollectionProtocolRegistration.class,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser);
    		SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
    		bizLogic.assignPrivilegeToRelatedObjects(dao,privilegeName,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser);
    	}
    }

    /**
     * @see AbstractBizLogic#setPrivilege(DAO, String, Class, Long[], Long, String, boolean)
     * @param dao
     * @param privilegeName
     * @param objectIds
     * @param userId
     * @param roleId
     * @param assignToUser
     * @throws SMException
     * @throws DAOException
     */
    public void assignPrivilegeToRelatedObjectsForCP(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId, boolean assignToUser)throws SMException, DAOException
    {
        List listOfSubElement = super.getRelatedObjects(dao, CollectionProtocolRegistration.class, "collectionProtocol",objectIds);
    	if(!listOfSubElement.isEmpty())
    	{
    	    super.setPrivilege(dao,privilegeName,CollectionProtocolRegistration.class,Utility.toLongArray(listOfSubElement),userId,roleId, assignToUser);
			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			bizLogic.assignPrivilegeToRelatedObjects(dao,privilegeName,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser);
			
			ParticipantBizLogic participantBizLogic = (ParticipantBizLogic)BizLogicFactory.getBizLogic(Constants.PARTICIPANT_FORM_ID);
			participantBizLogic.assignPrivilegeToRelatedObjectsForCPR(dao,privilegeName,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser);
    	}
    }
    
    /**
     * @see AbstractBizLogic#setPrivilege(DAO, String, Class, Long[], Long, String, boolean)
     */
    public void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser) throws SMException, DAOException
    {
	    super.setPrivilege(dao,privilegeName,objectType,objectIds,userId, roleId, assignToUser);
	    
	    SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
		bizLogic.assignPrivilegeToRelatedObjects(dao,privilegeName,objectIds,userId, roleId, assignToUser);
		
		ParticipantBizLogic participantBizLogic = (ParticipantBizLogic)BizLogicFactory.getBizLogic(Constants.PARTICIPANT_FORM_ID);
		participantBizLogic.assignPrivilegeToRelatedObjectsForCPR(dao,privilegeName,objectIds,userId, roleId, assignToUser);
	
	}
}