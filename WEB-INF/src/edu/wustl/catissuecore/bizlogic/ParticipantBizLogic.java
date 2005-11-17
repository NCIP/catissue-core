/**
 * <p>Title: ParticipantHDAO Class>
 * <p>Description:	ParticipantHDAO is used to add Participant's information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 23, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;

/**
 * ParticipantHDAO is used to to add Participant's information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class ParticipantBizLogic extends DefaultBizLogic
{
	/**
     * Saves the Participant object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException 
	{
		Participant participant = (Participant)obj;
        
		dao.insert(participant,sessionDataBean, true, true);
		
		Collection participantMedicalIdentifierCollection = participant.getParticipantMedicalIdentifierCollection();
		
		if(participantMedicalIdentifierCollection.isEmpty())
		{
			//add a dummy participant MedicalIdentifier for Query.
			ParticipantMedicalIdentifier participantMedicalIdentifier = new ParticipantMedicalIdentifier();
			participantMedicalIdentifier.setMedicalRecordNumber(null);
			participantMedicalIdentifier.setSite(null);
			participantMedicalIdentifierCollection.add(participantMedicalIdentifier);
		}
		
		//Inserting medical identifiers in the database after setting the participant associated.
		Iterator it = participantMedicalIdentifierCollection.iterator();
		while(it.hasNext())
		{
			ParticipantMedicalIdentifier pmIdentifier = (ParticipantMedicalIdentifier)it.next();
			pmIdentifier.setParticipant(participant);
			dao.insert(pmIdentifier,sessionDataBean, true, true);
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
		Participant participant = (Participant)obj;
		Participant oldParticipant = (Participant) oldObj;
		
		dao.update(participant, sessionDataBean, true, true, false);
		
		//Audit of Participant.
		dao.audit(obj, oldObj, sessionDataBean, true);
		
		Collection oldParticipantMedicalIdentifierCollection = (Collection)oldParticipant.getParticipantMedicalIdentifierCollection();
		
		Collection participantMedicalIdentifierCollection = participant.getParticipantMedicalIdentifierCollection();
		Iterator it = participantMedicalIdentifierCollection.iterator();
		
		//Updating the medical identifiers of the participant
		while(it.hasNext())
		{
			ParticipantMedicalIdentifier pmIdentifier = (ParticipantMedicalIdentifier)it.next();
			pmIdentifier.setParticipant(participant);
			dao.update(pmIdentifier, sessionDataBean, true, true, false);
			
			//Audit of ParticipantMedicalIdentifier.
			ParticipantMedicalIdentifier oldPmIdentifier 
					= (ParticipantMedicalIdentifier)getCorrespondingOldObject(oldParticipantMedicalIdentifierCollection, 
					        pmIdentifier.getSystemIdentifier());
			
			dao.audit(pmIdentifier, oldPmIdentifier, sessionDataBean, true);
		}
		
		//Disable the associate collection protocol registration
		Logger.out.debug("participant.getActivityStatus() "+participant.getActivityStatus());
		if(participant.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("participant.getActivityStatus() "+participant.getActivityStatus());
			Long participantIDArr[] = {participant.getSystemIdentifier()};
			
			CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
			bizLogic.disableRelatedObjectsForParticipant(dao,participantIDArr);
		}
    }
	
	/**
	 * @see AbstractBizLogic#setPrivilege(DAO, String, Class, Long[], Long, String, boolean)
	 */
	public void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser) throws SMException, DAOException
    {
	    Logger.out.debug(" privilegeName:"+privilegeName+" objectType:"+objectType+" objectIds:"+edu.wustl.common.util.Utility.getArrayString(objectIds)+" userId:"+userId+" roleId:"+roleId+" assignToUser:"+assignToUser);
	    super.setPrivilege(dao,privilegeName,objectType,objectIds,userId, roleId, assignToUser);
	    
	    CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		bizLogic.assignPrivilegeToRelatedObjectsForParticipant(dao,privilegeName,objectIds,userId,roleId,assignToUser );
    }

	/**
	 * Assigns the privilege to related objects for Collection Protocol Registration.
	 * @param dao
	 * @param privilegeName
	 * @param longs
	 * @param userId
	 * @param roleId
	 * @param assignToUser
	 * @throws DAOException
	 * @throws SMException
	 */
	public void assignPrivilegeToRelatedObjectsForCPR(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId, boolean assignToUser) throws SMException, DAOException {
		 List listOfSubElement = super.getRelatedObjects(dao, CollectionProtocolRegistration.class, new String[] {Constants.PARTICIPANT_IDENTIFIER_IN_CPR+"."+Constants.SYSTEM_IDENTIFIER}, new String[] {Constants.SYSTEM_IDENTIFIER}, 
   			 objectIds);
    Logger.out.debug(" CPR Ids:"+edu.wustl.common.util.Utility.getArrayString(objectIds)+" Related Participant Ids:"+listOfSubElement);  
   	if(!listOfSubElement.isEmpty())
   	{
   	    super.setPrivilege(dao,privilegeName,Participant.class,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser);
   	}
	}
}