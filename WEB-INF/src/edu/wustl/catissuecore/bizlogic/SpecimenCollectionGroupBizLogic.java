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

import java.util.List;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * UserHDAO is used to add user information into the database using Hibernate.
 * @author kapil_kaveeshwar
 */
public class SpecimenCollectionGroupBizLogic extends DefaultBizLogic {

	/**
	 * Saves the user object in the database.
	 * @param session The session in which the object is saved.
	 * @param obj The user object to be saved.
	 * @throws DAOException 
	 */
	protected void insert(DAO dao, Object obj) throws DAOException
	{
		SpecimenCollectionGroup specimenCollectionGroup =
			(SpecimenCollectionGroup) obj;

		List list = dao.retrieve(Site.class.getName(), "systemIdentifier", specimenCollectionGroup.getSite().getSystemIdentifier());
		if (!list.isEmpty())
		{
			specimenCollectionGroup.setSite((Site)list.get(0));
		}
		
		list  =  dao.retrieve(CollectionProtocolEvent.class.getName(), "systemIdentifier", specimenCollectionGroup.getCollectionProtocolEvent().getSystemIdentifier());
		if(!list.isEmpty())
		{
  			   specimenCollectionGroup.setCollectionProtocolEvent((CollectionProtocolEvent)list.get(0));
		}
		
		list  =  dao.retrieve(ParticipantMedicalIdentifier.class.getName(), "systemIdentifier", specimenCollectionGroup.getClinicalReport().getParticipantMedicalIdentifier().getSystemIdentifier());
		if(!list.isEmpty())
		{
		   specimenCollectionGroup.getClinicalReport().setParticipantMedicalIdentifier((ParticipantMedicalIdentifier)list.get(0));
		}
		
		
		String sourceObjectName = CollectionProtocolRegistration.class.getName();
		String[] selectColumnName = null;
		String[] whereColumnName = new String[2];
		String[] whereColumnCondition = {"=","="};
		Object[] whereColumnValue = new String[2];
		String joinCondition = Constants.AND_JOIN_CONDITION;
		
		whereColumnName[0]="collectionProtocol.systemIdentifier";
		whereColumnValue[0]=specimenCollectionGroup.getCollectionProtocolRegistration().getCollectionProtocol().getSystemIdentifier();
		
		if(specimenCollectionGroup.getCollectionProtocolRegistration().getParticipant()!=null)
		{
			whereColumnName[1]="participant.systemIdentifier";
			whereColumnValue[1]=specimenCollectionGroup.getCollectionProtocolRegistration().getParticipant().getSystemIdentifier();
		}
		else
		{
			whereColumnName[1] = "protocolParticipantIdentifier";
			whereColumnValue[1] = specimenCollectionGroup.getCollectionProtocolRegistration().getProtocolParticipantIdentifier();
		}
		
		
		list = dao.retrieve( sourceObjectName, selectColumnName,
						whereColumnName, whereColumnCondition, 
						whereColumnValue, joinCondition);
		
		if(!list.isEmpty())
		{
		   specimenCollectionGroup.setCollectionProtocolRegistration((CollectionProtocolRegistration)list.get(0));
		}
		
		dao.insert(specimenCollectionGroup);
		dao.insert(specimenCollectionGroup.getClinicalReport());
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