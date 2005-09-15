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

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.beans.SessionDataBean;
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
		
		Iterator it = participantMedicalIdentifierCollection.iterator();
		while(it.hasNext())
		{
			ParticipantMedicalIdentifier pmIdentifier = (ParticipantMedicalIdentifier)it.next();
			Logger.out.debug(pmIdentifier);
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
	protected void update(DAO dao, Object obj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException 
    {
		Participant participant = (Participant)obj;
        
		dao.update(participant, sessionDataBean, true, true);
		
		Collection participantMedicalIdentifierCollection = participant.getParticipantMedicalIdentifierCollection();		
		Iterator it = participantMedicalIdentifierCollection.iterator();
		
		while(it.hasNext())
		{
			ParticipantMedicalIdentifier pmIdentifier = (ParticipantMedicalIdentifier)it.next();
			pmIdentifier.setParticipant(participant);
			dao.update(pmIdentifier, sessionDataBean, true, true);
		}
		
		Logger.out.debug("participant.getActivityStatus() "+participant.getActivityStatus());
		if(participant.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("participant.getActivityStatus() "+participant.getActivityStatus());
			Long participantIDArr[] = {participant.getSystemIdentifier()};
			
			CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
			bizLogic.disableRelatedObjectsForParticipant(dao,participantIDArr);
		}
    }
}