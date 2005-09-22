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
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;

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
		if(specimen.getSpecimenCharacteristics()!=null)
		{
		    protectionObjects.add(specimen.getSpecimenCharacteristics());
		}
		
		Collection externalIdentifierCollection = specimen.getExternalIdentifierCollection();
		if(externalIdentifierCollection != null && externalIdentifierCollection.size() > 0)
		{
			Iterator it = externalIdentifierCollection.iterator();
			
			while(it.hasNext())
			{
				ExternalIdentifier exId = (ExternalIdentifier)it.next();
				exId.setSpecimen(specimen);
				dao.insert(exId,sessionDataBean, true, true);
				protectionObjects.add(exId);
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

	
	/**
     * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 * @throws HibernateException Exception thrown during hibernate operations.
     */
    public void update(DAO dao, Object obj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
    {
    	Specimen specimen = (Specimen)obj;
    	
//		setSpecimenAttributes(dao,specimen);
		dao.update(specimen.getSpecimenCharacteristics(), sessionDataBean, true, true, false);
		dao.update(specimen, sessionDataBean, true, true, false);
		
		Collection externalIdentifierCollection = specimen.getExternalIdentifierCollection();
		if(externalIdentifierCollection != null && externalIdentifierCollection.size() > 0)
		{
			Iterator it = externalIdentifierCollection.iterator();
			
			while(it.hasNext())
			{
				ExternalIdentifier exId = (ExternalIdentifier)it.next();
				exId.setSpecimen(specimen);
				dao.update(exId, sessionDataBean, true, true, false);
			}
		}
		
		Logger.out.debug("specimen.getActivityStatus() "+specimen.getActivityStatus());
		if(specimen.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("specimen.getActivityStatus() "+specimen.getActivityStatus());
			Long specimenIDArr[] = new Long[1];
			specimenIDArr[0] = specimen.getSystemIdentifier();
			
			disableSubSpecimens(dao,specimenIDArr);
		}
    }
    
    private void setSpecimenAttributes(DAO dao, Specimen specimen) throws DAOException
	{
    	//Load & set Specimen Collection Group if present
		if(specimen.getSpecimenCollectionGroup() != null)
		{
	    	List list = dao.retrieve(SpecimenCollectionGroup.class.getName(), "systemIdentifier", specimen.getSpecimenCollectionGroup().getSystemIdentifier());
			
			if(list!=null && list.size()!=0)
			{
				SpecimenCollectionGroup spg = (SpecimenCollectionGroup)list.get(0);
				specimen.setSpecimenCollectionGroup(spg);
			}
		}
		
		//Load & set Parent Specimen if present
		if(specimen.getParentSpecimen() != null)
		{
			List parentSpecimenList = dao.retrieve(Specimen.class.getName(),"systemIdentifier",specimen.getParentSpecimen().getSystemIdentifier());
			
			if(parentSpecimenList!=null && parentSpecimenList.size()!=0)
			{
				Specimen parentSpecimen = (Specimen)parentSpecimenList.get(0);
				specimen.setParentSpecimen(parentSpecimen);
			}
		}
		
		//Load & set Storage Container
		List scList = dao.retrieve(StorageContainer.class.getName(), "systemIdentifier", specimen.getStorageContainer().getSystemIdentifier());
		
		if(scList!=null && scList.size()!=0)
		{
			StorageContainer container = (StorageContainer)scList.get(0);
			specimen.setStorageContainer(container);
		}
		
		//Setting the Biohazard Collection
		Set set = new HashSet();
		
		Collection biohazardCollection = specimen.getBiohazardCollection();
		if(biohazardCollection != null && biohazardCollection.size() > 0)
		{
			Iterator it = biohazardCollection.iterator();

			while(it.hasNext())
			{
				Biohazard hazard = (Biohazard)it.next();
				System.out.println("hazard.getSystemIdentifier() "+hazard.getSystemIdentifier());
				Object bioObj = dao.retrieve(Biohazard.class.getName(), hazard.getSystemIdentifier());
				if(bioObj!=null)
				{
					Biohazard hazardObj = (Biohazard)bioObj;
					System.out.println("hazard.getSystemIdentifier() "+hazardObj.getSystemIdentifier());
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
    
    public void disableRelatedObjectsForStorageContainer(DAO dao, Long storageContainerIdArr[])throws DAOException 
    {
    	Logger.out.debug("disableRelatedObjectsForStorageContainer NewSpecimenBizLogic");
    	List listOfSpecimenId = super.disableObjects(dao, Specimen.class, "storageContainer", 
    			"CATISSUE_SPECIMEN", "STORAGE_CONTAINER_IDENTIFIER", storageContainerIdArr);
    	if(!listOfSpecimenId.isEmpty())
    	{
    		disableSubSpecimens(dao,Utility.toLongArray(listOfSpecimenId));
    	}
    }
    
    private void disableSubSpecimens(DAO dao, Long speIDArr[])throws DAOException
	{
    	List listOfSubElement = super.disableObjects(dao, Specimen.class, "parentSpecimen", 
    			"CATISSUE_SPECIMEN", "PARENT_SPECIMEN_ID", speIDArr);
    	
    	if(listOfSubElement.isEmpty())
    		return;
    	disableSubSpecimens(dao, Utility.toLongArray(listOfSubElement));
	}
}