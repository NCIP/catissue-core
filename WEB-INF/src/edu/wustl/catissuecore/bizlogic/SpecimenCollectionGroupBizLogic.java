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
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.ClinicalReport;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;

/**
 * UserHDAO is used to add user information into the database using Hibernate.
 * @author kapil_kaveeshwar
 */
public class SpecimenCollectionGroupBizLogic extends DefaultBizLogic 
{
	/**
	 * Saves the user object in the database.
	 * @param obj The user object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
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
		
		dao.insert(specimenCollectionGroup,sessionDataBean, true, true);
		if(specimenCollectionGroup.getClinicalReport()!=null)
			dao.insert(specimenCollectionGroup.getClinicalReport(),sessionDataBean, true, true);
		try
        {
            SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,getProtectionObjects(specimenCollectionGroup),getDynamicGroups(specimenCollectionGroup));
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
		
	}
	
	public Set getProtectionObjects(AbstractDomainObject obj)
    {
        Set protectionObjects = new HashSet();
        
        SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
        protectionObjects.add(specimenCollectionGroup);
        
		Participant participant = null;
//		//Case of registering Participant on its participant ID
//		if(specimenCollectionGroup.getClinicalReport()!=null)
//		{
//		    protectionObjects.add(specimenCollectionGroup.getClinicalReport());
//		}
		
        Logger.out.debug(protectionObjects.toString());
        return protectionObjects;
    }

    public String[] getDynamicGroups(AbstractDomainObject obj)
    {
        String[] dynamicGroups=null;
        SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
        dynamicGroups = new String[1];
        
        try
        {
            dynamicGroups[0] = SecurityManager.getInstance(this.getClass()).getProtectionGroupByName(specimenCollectionGroup.getCollectionProtocolRegistration(),Constants.getCollectionProtocolPGName(null));
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
        Logger.out.debug("Dynamic Group name: "+dynamicGroups[0]);
        return dynamicGroups;
        
    }

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException 
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
		
		setCollectionProtocolRegistration(dao, specimenCollectionGroup);
		
		dao.update(specimenCollectionGroup, sessionDataBean, true, true, false);
		dao.update(specimenCollectionGroup.getClinicalReport(), sessionDataBean, true, true, false);
		
		Logger.out.debug("specimenCollectionGroup.getActivityStatus() "+specimenCollectionGroup.getActivityStatus());
		if(specimenCollectionGroup.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("specimenCollectionGroup.getActivityStatus() "+specimenCollectionGroup.getActivityStatus());
			Long specimenCollectionGroupIDArr[] = {specimenCollectionGroup.getSystemIdentifier()};
			
			NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic)BizLogicFactory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			bizLogic.disableRelatedObjectsForSpecimenCollectionGroup(dao,specimenCollectionGroupIDArr);
		}
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
	
	public void disableRelatedObjects(DAO dao, Long collProtRegIDArr[])throws DAOException 
    {
    	List listOfSubElement = super.disableObjects(dao, SpecimenCollectionGroup.class, "collectionProtocolRegistration", 
    			"CATISSUE_SPECIMEN_COLLECTION_GROUP", "COLLECTION_PROTOCOL_REGISTRATION_ID", collProtRegIDArr);
    	if(!listOfSubElement.isEmpty())
    	{
	    	NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic)BizLogicFactory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			bizLogic.disableRelatedObjectsForSpecimenCollectionGroup(dao,Utility.toLongArray(listOfSubElement));
    	}
    }

    /**
     * @param dao
     * @param privilegeName
     * @param objectIds
     * @param assignToUser
     * @param roleId
     * @param longs
     * @throws DAOException
     * @throws SMException
     */
    public void assignPrivilegeToRelatedObjects(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId, boolean assignToUser) throws SMException, DAOException
    {
        List listOfSubElement = super.getRelatedObjects(dao, SpecimenCollectionGroup.class,"collectionProtocolRegistration", objectIds);
		if(!listOfSubElement.isEmpty())
		{
		    super.setPrivilege(dao,privilegeName,SpecimenCollectionGroup.class,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser);
	    	NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic)BizLogicFactory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			bizLogic.assignPrivilegeToRelatedObjectsForSCG(dao,privilegeName,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser);
		}
        
    }  
    
    public void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser) throws SMException, DAOException
    {
	    super.setPrivilege(dao,privilegeName,objectType,objectIds,userId, roleId, assignToUser);
	    
	    NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic)BizLogicFactory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
	    bizLogic.assignPrivilegeToRelatedObjectsForSCG(dao,privilegeName,objectIds,userId, roleId, assignToUser);
    }
}