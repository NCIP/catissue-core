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
import edu.wustl.common.util.dbManager.DAOException;

/**
 * ParticipantHDAO is used to to add Participant's information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class ParticipantBizLogic extends DefaultBizLogic
{
	/**
     * Saves the Participant object in the database.
     * @param session The session in which the object is saved.
     * @param obj The storageType object to be saved.
     * @throws DAOException 
     */
	protected void insert(DAO dao, Object obj) throws DAOException 
	{
		Participant participant = (Participant)obj;
        
		dao.insert(participant,true);
		
		Collection participantMedicalIdentifierCollection = participant.getParticipantMedicalIdentifierCollection();		
		Iterator it = participantMedicalIdentifierCollection.iterator();
		
		while(it.hasNext())
		{
			ParticipantMedicalIdentifier pmIdentifier = (ParticipantMedicalIdentifier)it.next();
			pmIdentifier.setParticipant(participant);
			dao.insert(pmIdentifier,true);
		}
	}
	
	/**
     * Updates the persistent object in the database.
     * @param session The session in which the object is saved.
     * @param obj The object to be updated.
     * @throws DAOException 
     */
	protected void update(DAO dao, Object obj) throws DAOException 
    {
		Participant participant = (Participant)obj;
        
		dao.update(participant);
		
		Collection participantMedicalIdentifierCollection = participant.getParticipantMedicalIdentifierCollection();		
		Iterator it = participantMedicalIdentifierCollection.iterator();
		
		while(it.hasNext())
		{
			ParticipantMedicalIdentifier pmIdentifier = (ParticipantMedicalIdentifier)it.next();
			pmIdentifier.setParticipant(participant);
			dao.update(pmIdentifier);
		}
    }
}