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
import java.util.Set;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
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
	 * @param session The session in which the object is saved.
	 * @param obj The user object to be saved.
	 * @throws DAOException 
	 */
	protected void insert(DAO dao, Object obj) throws DAOException
	{
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;

		Participant participant = null;
		//Case of registering Participant on its participant ID
		if(collectionProtocolRegistration.getParticipant()!=null)
		{
			Object participantObj = dao.retrieve(Participant.class.getName(), collectionProtocolRegistration.getParticipant().getSystemIdentifier());
			if (obj != null)
			{
				participant = (Participant) participantObj;
			}
		}
		else//Case of registering Participant on Participant Protocol ID
		{
			//Add a dummy participant in the system
			participant = null;
		}
		collectionProtocolRegistration.setParticipant(participant);

		Object collectionProtocolObj = dao.retrieve(CollectionProtocol.class.getName(), 
				collectionProtocolRegistration.getCollectionProtocol().getSystemIdentifier());
		if (collectionProtocolObj!=null)
		{
			CollectionProtocol collectionProtocol = (CollectionProtocol) collectionProtocolObj;
			collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);
		}

		dao.insert(collectionProtocolRegistration, true);
		
//		try
//        {
//		    SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,getProtectionObjects(participant),getDynamicGroups(participant));
//        }
//        catch (SMException e)
//        {
//            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
//        }
	}

	/**
	 * Updates the persistent object in the database.
	 * @param session The session in which the object is saved.
	 * @param obj The object to be updated.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj) throws DAOException
	{
	}

	public Set getProtectionObjects(AbstractDomainObject obj)
    {
        Set protectionObjects = new HashSet();
        
        CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;

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
        dynamicGroups[0] = "COLLECTION_PROTOCOL_"+collectionProtocolRegistration.getCollectionProtocol().getSystemIdentifier();
        return dynamicGroups;
        
    }
	
}