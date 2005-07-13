/**
 * <p>Title: AccessionHDAO Class>
 * <p>Description:	AccessionHDAO is used to add accession information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 14, 2005
 */

package edu.wustl.catissuecore.dao;

import java.util.List;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.domain.Accession;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * AccessionHDAO is used to add accession information into the database using Hibernate.
 * @author gautam_shetty
 */
public class AccessionBizLogic extends AbstractBizLogic
{

    /**
     * Saves the accession object in the database.
     * @param obj The accession object to be saved.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
    public void insert(Object obj) throws HibernateException,
            DAOException
    {
        Accession accession = (Accession) obj;
        AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
        dao.openSession();
        
        List list = dao.retrieve(Participant.class.getName(), Constants.IDENTIFIER,
                accession.getParticipant().getIdentifier().toString());
        Participant participant = (Participant) list.get(0);
        if (list.size() != 0)
        {
            accession.setParticipant(participant);
            dao.insert(accession);
        }
        dao.closeSession();
    }
}