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

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.common.util.dbManager.DAOException;

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
			participant = new Participant();
			dao.insert(participant, true);
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
}