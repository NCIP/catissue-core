/**
 * <p>Title: NewSpecimenHDAO Class>
 * <p>Description:	NewSpecimenBizLogicHDAO is used to add new specimen information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 21, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * NewSpecimenHDAO is used to add new specimen information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class NewSpecimenBizLogic extends DefaultBizLogic
{
	/**
     * Saves the storageType object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
	    Set protectionObjects = new HashSet();
		Specimen specimen = (Specimen)obj;
		
		setSpecimenAttributes(dao,specimen);
				
		dao.insert(specimen.getSpecimenCharacteristics(),sessionDataBean, true, true);
		dao.insert(specimen,sessionDataBean, true, true);
		protectionObjects.add(specimen);
//		if(specimen.getSpecimenCharacteristics()!=null)
//		{
//		    protectionObjects.add(specimen.getSpecimenCharacteristics());
//		}
		
		Collection externalIdentifierCollection = specimen.getExternalIdentifierCollection();
		if(externalIdentifierCollection != null)
		{
			Iterator it = externalIdentifierCollection.iterator();
			while(it.hasNext())
			{
				ExternalIdentifier exId = (ExternalIdentifier)it.next();
				exId.setSpecimen(specimen);
				dao.insert(exId,sessionDataBean, true, true);
//				protectionObjects.add(exId);
			}
		}
		//Inserting data for Authorization
		try
        {
            SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,protectionObjects,getDynamicGroups(specimen));
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
	}
	
	
    public String[] getDynamicGroups(AbstractDomainObject obj)
    {
        String[] dynamicGroups=null;
        Specimen specimen = (Specimen)obj;
        dynamicGroups = new String[1];
        
        try
        {
            dynamicGroups[0] = SecurityManager.getInstance(this.getClass()).getProtectionGroupByName(specimen.getSpecimenCollectionGroup(),Constants.getCollectionProtocolPGName(null));
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
        Logger.out.debug("Dynamic Group name: "+dynamicGroups[0]);
        return dynamicGroups;
        
    }

    private SpecimenCollectionGroup loadSpecimenCollectionGroup(Long specimenID, DAO dao) throws DAOException
	{
		//get list of Participant's names
		String sourceObjectName = Specimen.class.getName();
	  	String [] selectedColumn = {"specimenCollectionGroup."+Constants.SYSTEM_IDENTIFIER};
	  	String whereColumnName[] = {Constants.SYSTEM_IDENTIFIER};
	  	String whereColumnCondition[] = {"="};
	  	Object whereColumnValue[] = {specimenID};
	  	String joinCondition = Constants.AND_JOIN_CONDITION;
	  	
	  	List list = dao.retrieve(sourceObjectName, selectedColumn, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
	  	if(!list.isEmpty())
	  	{
	  		Long specimenCollectionGroupId  = (Long)list.get(0);
	  		SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
	  		specimenCollectionGroup.setSystemIdentifier(specimenCollectionGroupId);
	  		return specimenCollectionGroup;
	  	}
	  	return null;
	}
    
    private SpecimenCharacteristics loadSpecimenCharacteristics(Long specimenID, DAO dao) throws DAOException
	{
		//get list of Participant's names
		String sourceObjectName = Specimen.class.getName();
	  	String [] selectedColumn = {"specimenCharacteristics."+Constants.SYSTEM_IDENTIFIER};
	  	String whereColumnName[] = {Constants.SYSTEM_IDENTIFIER};
	  	String whereColumnCondition[] = {"="};
	  	Object whereColumnValue[] = {specimenID};
	  	String joinCondition = Constants.AND_JOIN_CONDITION;
	  	
	  	List list = dao.retrieve(sourceObjectName, selectedColumn, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
	  	if(!list.isEmpty())
	  	{
	  		Long specimenCharacteristicsId  = (Long)list.get(0);
	  		SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
	  		specimenCharacteristics.setSystemIdentifier(specimenCharacteristicsId);
	  		return specimenCharacteristics;

	  		//return (SpecimenCharacteristics)list.get(0);
	  	}
	  	return null;
	}
	
	/**
     * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 * @throws HibernateException Exception thrown during hibernate operations.
     */
    public void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
    {
    	Specimen specimen = (Specimen)obj;
    	Specimen specimenOld = (Specimen)oldObj;
    	
    	if(specimen.isParentChanged())
        {
        	//Check whether continer is moved to one of its sub container.
        	if(isUnderSubSpecimen(specimen,specimen.getParentSpecimen().getSystemIdentifier()))
        	{
        		throw new DAOException(ApplicationProperties.getValue("errors.specimen.under.subspecimen"));  
        	}
        	Logger.out.debug("Loading ParentSpecimen: "+specimen.getParentSpecimen().getSystemIdentifier());

			// check for closed ParentSpecimen
			checkStatus(dao, specimen.getParentSpecimen(), "Parent Specimen" );
			
        	
//        	Specimen parentSpecimen = (Specimen) dao.retrieve(Specimen.class.getName(), specimen.getParentSpecimen().getSystemIdentifier());
//        	specimen.setParentSpecimen(parentSpecimen);
//        	specimen.setSpecimenCollectionGroup(parentSpecimen.getSpecimenCollectionGroup());
//        	specimen.setSpecimenCharacteristics(parentSpecimen.getSpecimenCharacteristics());
        	SpecimenCollectionGroup scg = loadSpecimenCollectionGroup(specimen.getParentSpecimen().getSystemIdentifier(), dao);
        	
        	specimen.setSpecimenCollectionGroup(scg);
        	SpecimenCharacteristics sc= loadSpecimenCharacteristics(specimen.getParentSpecimen().getSystemIdentifier(), dao);
        	specimen.setSpecimenCharacteristics(sc);
        }
    	
    	//check for closed Specimen Collection Group
    	if(!specimen.getSpecimenCollectionGroup().getSystemIdentifier().equals(specimenOld.getSpecimenCollectionGroup().getSystemIdentifier()))
    		checkStatus(dao,specimen.getSpecimenCollectionGroup(), "Specimen Collection Group" );
		
    	setSpecimenGroupForSubSpecimen(specimen,specimen.getSpecimenCollectionGroup(),specimen.getSpecimenCharacteristics());
    	
		//dao.update(specimen.getSpecimenCharacteristics(), sessionDataBean, true, true, false);
		dao.update(specimen, sessionDataBean, true, true, false);
		
		//Audit of Specimen.
		dao.audit(obj, oldObj, sessionDataBean, true);
		
		Collection oldExternalIdentifierCollection = specimenOld.getExternalIdentifierCollection();
		
		Collection externalIdentifierCollection = specimen.getExternalIdentifierCollection();
		if(externalIdentifierCollection != null)
		{
			Iterator it = externalIdentifierCollection.iterator();
			while(it.hasNext())
			{
				ExternalIdentifier exId = (ExternalIdentifier)it.next();
				exId.setSpecimen(specimen);
				dao.update(exId, sessionDataBean, true, true, false);
				
				ExternalIdentifier oldExId = (ExternalIdentifier)
					getCorrespondingOldObject(oldExternalIdentifierCollection, exId.getSystemIdentifier());
				dao.audit(exId, oldExId, sessionDataBean, true);
			}
		}
		
		//Disable functionality
		Logger.out.debug("specimen.getActivityStatus() "+specimen.getActivityStatus());
		if(specimen.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			setDisableToSubSpecimen(specimen);
			Logger.out.debug("specimen.getActivityStatus() "+specimen.getActivityStatus());
			Long specimenIDArr[] = new Long[1];
			specimenIDArr[0] = specimen.getSystemIdentifier();
			
			disableSubSpecimens(dao,specimenIDArr);
		}
    }
    
    private boolean isUnderSubSpecimen(Specimen specimen, Long parentSpecimenID)
    {
    	if (specimen != null)
        {
        	Iterator iterator = specimen.getChildrenSpecimen().iterator();
            while (iterator.hasNext())
            {
            	Specimen childSpecimen = (Specimen) iterator.next();
                //Logger.out.debug("SUB CONTINER container "+parentContainerID.longValue()+" "+container.getSystemIdentifier().longValue()+"  "+(parentContainerID.longValue()==container.getSystemIdentifier().longValue()));
                if(parentSpecimenID.longValue()==childSpecimen.getSystemIdentifier().longValue())
                	return true;
                if(isUnderSubSpecimen(childSpecimen,parentSpecimenID))
                	return true;
            }
        }
    	return false;
    }
    
    private void setSpecimenGroupForSubSpecimen(Specimen specimen, SpecimenCollectionGroup specimenCollectionGroup, SpecimenCharacteristics specimenCharacteristics)
    {
        if (specimen != null)
        {
        	Logger.out.debug("specimen() "+specimen.getSystemIdentifier());
        	Logger.out.debug("specimen.getChildrenContainerCollection() "+specimen.getChildrenSpecimen().size());
            
        	Iterator iterator = specimen.getChildrenSpecimen().iterator();
            while (iterator.hasNext())
            {
            	Specimen childSpecimen = (Specimen) iterator.next();
            	childSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
            	childSpecimen.setSpecimenCharacteristics(specimenCharacteristics);
            	setSpecimenGroupForSubSpecimen(childSpecimen, specimenCollectionGroup, specimenCharacteristics);
            }
        }
    }
    
//  TODO TO BE REMOVED 
    private void setDisableToSubSpecimen(Specimen specimen)
    {
    	if (specimen != null)
        {
        	Iterator iterator = specimen.getChildrenSpecimen().iterator();
            while (iterator.hasNext())
            {
            	Specimen childSpecimen = (Specimen) iterator.next();
            	childSpecimen.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED);
            	setDisableToSubSpecimen(childSpecimen);
            }
        }
    }
    
    private void setSpecimenAttributes(DAO dao, Specimen specimen) throws DAOException
	{
    	//Load & set Specimen Collection Group if present
		if(specimen.getSpecimenCollectionGroup() != null)
		{
	    	Object specimenCollectionGroupObj = dao.retrieve(SpecimenCollectionGroup.class.getName(), specimen.getSpecimenCollectionGroup().getSystemIdentifier());
			if(specimenCollectionGroupObj!=null )
			{
				SpecimenCollectionGroup spg = (SpecimenCollectionGroup)specimenCollectionGroupObj;
				
				checkStatus(dao,spg, "Specimen Collection Group" );
				
				specimen.setSpecimenCollectionGroup(spg);
			}
		}
		
		//Load & set Parent Specimen if present
		if(specimen.getParentSpecimen() != null)
		{
			Object parentSpecimenObj = dao.retrieve(Specimen.class.getName(), specimen.getParentSpecimen().getSystemIdentifier());
			if(parentSpecimenObj!=null)
			{
				Specimen parentSpecimen = (Specimen)parentSpecimenObj;
				
				// check for closed Parent Specimen
				checkStatus(dao,parentSpecimen, "Parent Specimen");
				
				specimen.setParentSpecimen(parentSpecimen);
			}
		}
		
		//Load & set Storage Container
		Object containerObj = dao.retrieve(StorageContainer.class.getName(), specimen.getStorageContainer().getSystemIdentifier());
		if(containerObj != null)
		{
			StorageContainer container = (StorageContainer)containerObj;
			// check for closed Storage Container
			checkStatus(dao,container, "Storage Container");
			specimen.setStorageContainer(container);
		}
		
		//Setting the Biohazard Collection
		Set set = new HashSet();
		Collection biohazardCollection = specimen.getBiohazardCollection();
		if(biohazardCollection != null)
		{
			Iterator it = biohazardCollection.iterator();
			while(it.hasNext())
			{
				Biohazard hazard = (Biohazard)it.next();
				Logger.out.debug("hazard.getSystemIdentifier() "+hazard.getSystemIdentifier());
				Object bioObj = dao.retrieve(Biohazard.class.getName(), hazard.getSystemIdentifier());
				if(bioObj!=null)
				{
					Biohazard hazardObj = (Biohazard)bioObj;
					set.add(hazardObj);
				}
			}
		}
		specimen.setBiohazardCollection(set);
	}
    
    public void disableRelatedObjectsForSpecimenCollectionGroup(DAO dao, Long specimenCollectionGroupArr[])throws DAOException 
    {
    	Logger.out.debug("disableRelatedObjects NewSpecimenBizLogic");
    	List listOfSpecimenId = super.disableObjects(dao, Specimen.class, "specimenCollectionGroup", 
    			"CATISSUE_SPECIMEN", "SPECIMEN_COLLECTION_GROUP_ID", specimenCollectionGroupArr);
    	if(!listOfSpecimenId.isEmpty())
    	{
    		disableSubSpecimens(dao,Utility.toLongArray(listOfSpecimenId));
    	}
    }
    
//    public void disableRelatedObjectsForStorageContainer(DAO dao, Long storageContainerIdArr[])throws DAOException 
//    {
//    	Logger.out.debug("disableRelatedObjectsForStorageContainer NewSpecimenBizLogic");
//    	List listOfSpecimenId = super.disableObjects(dao, Specimen.class, "storageContainer", 
//    			"CATISSUE_SPECIMEN", "STORAGE_CONTAINER_IDENTIFIER", storageContainerIdArr);
//    	if(!listOfSpecimenId.isEmpty())
//    	{
//    		disableSubSpecimens(dao,Utility.toLongArray(listOfSpecimenId));
//    	}
//    }
    
    private void disableSubSpecimens(DAO dao, Long speIDArr[])throws DAOException
	{
    	List listOfSubElement = super.disableObjects(dao, Specimen.class, "parentSpecimen", 
    			"CATISSUE_SPECIMEN", "PARENT_SPECIMEN_ID", speIDArr);
    	
    	if(listOfSubElement.isEmpty())
    		return;
    	disableSubSpecimens(dao, Utility.toLongArray(listOfSubElement));
	}


    /**
     * @param dao
     * @param privilegeName
     * @param longs
     * @param userId
     * @throws DAOException
     * @throws SMException
     */
    public void assignPrivilegeToRelatedObjectsForSCG(DAO dao, String privilegeName, Long[] specimenCollectionGroupArr, Long userId, String roleId, boolean assignToUser) throws SMException, DAOException
    {
        Logger.out.debug("assignPrivilegeToRelatedObjectsForSCG NewSpecimenBizLogic");
    	List listOfSpecimenId = super.getRelatedObjects(dao, Specimen.class, "specimenCollectionGroup", 
    			 specimenCollectionGroupArr);
    	if(!listOfSpecimenId.isEmpty())
    	{
    	    super.setPrivilege(dao,privilegeName,Specimen.class,Utility.toLongArray(listOfSpecimenId),userId, roleId, assignToUser);
    		assignPrivilegeToSubSpecimens(dao,privilegeName,Specimen.class,Utility.toLongArray(listOfSpecimenId),userId, roleId, assignToUser);
    	}
    }


    /**
     * @param dao
     * @param privilegeName
     * @param class1
     * @param longs
     * @param userId
     * @throws DAOException
     * @throws SMException
     */
    private void assignPrivilegeToSubSpecimens(DAO dao, String privilegeName, Class class1, Long[] speIDArr, Long userId, String roleId, boolean assignToUser) throws SMException, DAOException
    {
        List listOfSubElement = super.getRelatedObjects(dao, Specimen.class, "parentSpecimen",  speIDArr);
    	
    	if(listOfSubElement.isEmpty())
    		return;
    	super.setPrivilege(dao,privilegeName,Specimen.class,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser);
    	assignPrivilegeToSubSpecimens(dao,privilegeName,Specimen.class, Utility.toLongArray(listOfSubElement),userId,roleId,assignToUser);
    }
    
    public void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser) throws SMException, DAOException
    {
	    super.setPrivilege(dao,privilegeName,objectType,objectIds,userId, roleId, assignToUser);
	    assignPrivilegeToSubSpecimens(dao,privilegeName,Specimen.class,objectIds,userId, roleId, assignToUser);
    }
}