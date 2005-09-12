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
import edu.wustl.catissuecore.domain.ClinicalReport;
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
public class SpecimenCollectionGroupBizLogic extends DefaultBizLogic 
{
	/**
	 * Saves the user object in the database.
	 * @param session The session in which the object is saved.
	 * @param obj The user object to be saved.
	 * @throws DAOException 
	 */
	protected void insert(DAO dao, Object obj) throws DAOException
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
		
		List list = dao.retrieve(Site.class.getName(), Constants.SYSTEM_IDENTIFIER, specimenCollectionGroup.getSite().getSystemIdentifier());
		if (!list.isEmpty())
		{
			specimenCollectionGroup.setSite((Site)list.get(0));
		}
		
		list  =  dao.retrieve(CollectionProtocolEvent.class.getName(), Constants.SYSTEM_IDENTIFIER, specimenCollectionGroup.getCollectionProtocolEvent().getSystemIdentifier());
		if(!list.isEmpty())
		{
			specimenCollectionGroup.setCollectionProtocolEvent((CollectionProtocolEvent)list.get(0));
		}
		
		setClinicalReport(dao, specimenCollectionGroup);
		setCollectionProtocolRegistration(dao, specimenCollectionGroup);
		
		dao.insert(specimenCollectionGroup,true);
		if(specimenCollectionGroup.getClinicalReport()!=null)
			dao.insert(specimenCollectionGroup.getClinicalReport(),true);
	}

	/**
	 * Updates the persistent object in the database.
	 * @param session The session in which the object is saved.
	 * @param obj The object to be updated.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj) throws DAOException 
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
		
		setCollectionProtocolRegistration(dao, specimenCollectionGroup);
		
		dao.update(specimenCollectionGroup);
		dao.update(specimenCollectionGroup.getClinicalReport());
	}
	
	private void setCollectionProtocolRegistration(DAO dao, SpecimenCollectionGroup specimenCollectionGroup) throws DAOException 
	{
		String sourceObjectName = CollectionProtocolRegistration.class.getName();
		String[] selectColumnName = null;
		String[] whereColumnName = new String[2];
		String[] whereColumnCondition = {"=","="};
		Object[] whereColumnValue = new Object[2];
		String joinCondition = Constants.AND_JOIN_CONDITION;
			
		whereColumnName[0]="collectionProtocol."+Constants.SYSTEM_IDENTIFIER;
		whereColumnValue[0]=specimenCollectionGroup.getCollectionProtocolRegistration().getCollectionProtocol().getSystemIdentifier();
		
		if(specimenCollectionGroup.getCollectionProtocolRegistration().getParticipant()!=null)
		{
			whereColumnName[1]="participant."+Constants.SYSTEM_IDENTIFIER;
			whereColumnValue[1]=specimenCollectionGroup.getCollectionProtocolRegistration().getParticipant().getSystemIdentifier();
		}
		else
		{
			whereColumnName[1] = "protocolParticipantIdentifier";
			whereColumnValue[1] = specimenCollectionGroup.getCollectionProtocolRegistration().getProtocolParticipantIdentifier();
			System.out.println("Value returned:"+whereColumnValue[1]);
		}
		
		List list = dao.retrieve( sourceObjectName, selectColumnName, whereColumnName, 
							 whereColumnCondition, whereColumnValue, joinCondition);
		if(!list.isEmpty())
		{
		   specimenCollectionGroup.setCollectionProtocolRegistration((CollectionProtocolRegistration)list.get(0));
		}
	}
	
	private void setClinicalReport(DAO dao, SpecimenCollectionGroup specimenCollectionGroup) throws DAOException
	{
		ClinicalReport clinicalReport = specimenCollectionGroup.getClinicalReport();
		ParticipantMedicalIdentifier participantMedicalIdentifier = clinicalReport.getParticipantMedicalIdentifier();
		if(participantMedicalIdentifier!=null)
		{
			List list  =  dao.retrieve(ParticipantMedicalIdentifier.class.getName(), Constants.SYSTEM_IDENTIFIER, participantMedicalIdentifier.getSystemIdentifier());
			if(!list.isEmpty())
			{
			   specimenCollectionGroup.getClinicalReport().setParticipantMedicalIdentifier((ParticipantMedicalIdentifier)list.get(0));
			}
		}
	}
}