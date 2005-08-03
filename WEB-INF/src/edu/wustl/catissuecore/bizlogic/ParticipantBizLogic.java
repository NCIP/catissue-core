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

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.dao.AbstractDAO;
import edu.wustl.catissuecore.dao.DAOFactory;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.util.global.Constants;
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
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
	public void insert(Object obj) throws DAOException 
	{
		Participant participant = (Participant)obj;
        
		AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
		dao.openSession();

		dao.insert(participant);
		
		Collection participantMedicalIdentifierCollection = participant.getParticipantMedicalIdentifierCollection();		
		Iterator it = participantMedicalIdentifierCollection.iterator();
		System.out.println("&&&&&&&&&&&&&&&&&&&&&& : "+ participantMedicalIdentifierCollection.size());
		while(it.hasNext())
		{
			ParticipantMedicalIdentifier pmIdentifier = (ParticipantMedicalIdentifier)it.next();
			pmIdentifier.setParticipant(participant);
			System.out.println("****************" + pmIdentifier.getMedicalRecordNumber());
			System.out.println("****************" + pmIdentifier.getParticipant().getSystemIdentifier());
			System.out.println("****************" + pmIdentifier.getSite().getSystemIdentifier());
			dao.insert(pmIdentifier);
		}
	    
		dao.closeSession();
	}
	
	/**
     * Updates the persistent object in the database.
     * @param session The session in which the object is saved.
     * @param obj The object to be updated.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
    public void update(Object obj) throws DAOException
    {
    }
    
//    public String getNextStorageContainerNo(Site site, StorageType type )
//    {
//    	AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
//    	
//    	String whereColNames = {}
//    	dao.retrieve(StorageContainer.class.getName(),)
//    	
//    	return null;
//    }
    
}