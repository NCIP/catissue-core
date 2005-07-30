/**
 * <p>Title: UserHDAO Class>
 * <p>Description:	UserHDAO is used to add user information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ajay Sharma
 * @version 1.00
 * Created on Apr 13, 2005
 */

package edu.wustl.catissuecore.dao;

import java.util.List;

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * UserHDAO is used to add user information into the database using Hibernate.
 * @author kapil_kaveeshwar
 */
public class SpecimenCollectionGroupBizLogic extends DefaultBizLogic
{

    /**
     * Saves the user object in the database.
     * @param session The session in which the object is saved.
     * @param obj The user object to be saved.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
    public void insert(Object obj) throws DAOException
    {
		SpecimenCollectionGroupForm specimenCollectionGroupForm = (SpecimenCollectionGroupForm) obj; 
        AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
        dao.openSession();

        Participant participant = null;
        
        List list = dao.retrieve(Participant.class.getName(), "systemIdentifier", 
		specimenCollectionGroupForm.getParticipantName().getSystemIdentifier());
        if (list.size() != 0)
        {
			participant = (Participant) list.get(0);
        }
        System.out.println("participant "+participant.getFirstName());

		/*CollectionProtocol collectionProtocol = null;
		list = dao.retrieve(CollectionProtocol.class.getName(), "systemIdentifier", 
			collectionProtocolRegistration.getCollectionProtocol().getSystemIdentifier());
		if (list.size() != 0)
		{
			collectionProtocol = (CollectionProtocol)list.get(0);
		}
		System.out.println("participant "+collectionProtocol.getTitle());

		collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);
		collectionProtocolRegistration.setParticipant(participant);

        dao.insert(collectionProtocolRegistration);*/

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
}