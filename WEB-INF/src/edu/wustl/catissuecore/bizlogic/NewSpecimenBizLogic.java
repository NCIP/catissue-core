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
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
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
		try
		{
			Set protectionObjects = new HashSet();
			Specimen specimen = (Specimen)obj;
			
			setSpecimenAttributes(dao,specimen,sessionDataBean);
			specimen.getStorageContainer();		
			dao.insert(specimen.getSpecimenCharacteristics(),sessionDataBean, true, true);
			dao.insert(specimen,sessionDataBean, true, true);
			protectionObjects.add(specimen);
			
			if(specimen.getSpecimenCharacteristics()!=null)
			{
				protectionObjects.add(specimen.getSpecimenCharacteristics());
			}
			
			Collection externalIdentifierCollection = specimen.getExternalIdentifierCollection();
			
			if(externalIdentifierCollection != null)
			{
				if(externalIdentifierCollection.isEmpty()) //Dummy entry added for query
				{
					ExternalIdentifier exId = new ExternalIdentifier();
					
					exId.setName(null);
					exId.setValue(null);
					
					externalIdentifierCollection.add(exId);
				}
				
				Iterator it = externalIdentifierCollection.iterator();
				while(it.hasNext())
				{
					ExternalIdentifier exId = (ExternalIdentifier)it.next();
					exId.setSpecimen(specimen);
					dao.insert(exId,sessionDataBean, true, true);
				}
			}
			
			//Inserting data for Authorization
			
			SecurityManager.getInstance(this.getClass()).insertAuthorizationData(
					null, protectionObjects, getDynamicGroups(specimen));
		}
		catch (SMException e)
		{
			throw handleSMException(e);
        }
	}
	
    private String[] getDynamicGroups(AbstractDomainObject obj) throws SMException
    {
        Specimen specimen = (Specimen)obj;
        String[] dynamicGroups = new String[1];
        
        dynamicGroups[0] = SecurityManager.getInstance(
        		this.getClass()).getProtectionGroupByName(
        				specimen.getSpecimenCollectionGroup(),Constants.getCollectionProtocolPGName(null));
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
	private void setAvailableQuantity(Specimen obj, Specimen oldObj) throws DAOException
	{
		if(obj instanceof TissueSpecimen)
		{
			Logger.out.debug("In TissueSpecimen");
			TissueSpecimen tissueSpecimenObj = (TissueSpecimen)obj;
			TissueSpecimen tissueSpecimenOldObj = (TissueSpecimen)oldObj;
			// get new qunatity modifed by user
			double newQty = tissueSpecimenObj.getQuantityInGram().doubleValue();
			// get old qunatity from database
			double oldQty = tissueSpecimenOldObj.getQuantityInGram().doubleValue();
			Logger.out.debug("New Qty: " +newQty+" Old Qty: " +oldQty);
			// get Available qty
			double oldAvailableQty = tissueSpecimenOldObj.getAvailableQuantityInGram().doubleValue();
			
			double distQty = 0;
			double newAvailableQty=0;
			// Distributed Qty = Old_Qty - Old_Available_Qty
			distQty = oldQty - oldAvailableQty;
			
			// New_Available_Qty = New_Qty - Distributed_Qty
			newAvailableQty = newQty - distQty;
			Logger.out.debug("Dist Qty: " +distQty+" New Available Qty: " +newAvailableQty);
			if(newAvailableQty<0)
			{
				throw new DAOException("Newly modified Quantity '" + newQty + "' should not be less than current Distributed Quantity '" + distQty + "'");
			}
			else
			{
				// set new available quantity
				tissueSpecimenObj.setAvailableQuantityInGram(new Double(newAvailableQty));
			}
			
		}
		else if(obj instanceof MolecularSpecimen)
		{
			Logger.out.debug("In MolecularSpecimen");
			MolecularSpecimen molecularSpecimenObj = (MolecularSpecimen)obj;
			MolecularSpecimen molecularSpecimenOldObj = (MolecularSpecimen)oldObj;
			// get new qunatity modifed by user
			double newQty = molecularSpecimenObj.getQuantityInMicrogram().doubleValue();
			// get old qunatity from database
			double oldQty = molecularSpecimenOldObj.getQuantityInMicrogram().doubleValue();
			Logger.out.debug("New Qty: " +newQty+" Old Qty: " +oldQty);
			// get Available qty
			double oldAvailableQty = molecularSpecimenOldObj.getAvailableQuantityInMicrogram().doubleValue();
			
			double distQty = 0;
			double newAvailableQty=0;
			// Distributed Qty = Old_Qty - Old_Available_Qty
			distQty = oldQty - oldAvailableQty;
			
			// New_Available_Qty = New_Qty - Distributed_Qty
			newAvailableQty = newQty - distQty;
			Logger.out.debug("Dist Qty: " +distQty+" New Available Qty: " +newAvailableQty);
			if(newAvailableQty<0)
			{
				throw new DAOException("Newly modified Quantity '" + newQty + "' should not be less than current Distributed Quantity '" + distQty + "'");
			}
			else
			{
				// set new available quantity
				molecularSpecimenObj.setAvailableQuantityInMicrogram(new Double(newAvailableQty));
			}
		}
		else if(obj instanceof CellSpecimen)
		{
			Logger.out.debug("In CellSpecimen");
			CellSpecimen cellSpecimenObj = (CellSpecimen)obj;
			CellSpecimen cellSpecimenOldObj = (CellSpecimen)oldObj;
			// get new qunatity modifed by user
			int newQty = cellSpecimenObj.getQuantityInCellCount().intValue();
			// get old qunatity from database
			int  oldQty = cellSpecimenOldObj.getQuantityInCellCount().intValue();
			Logger.out.debug("New Qty: " +newQty+" Old Qty: " +oldQty);
			// get Available qty
			int oldAvailableQty = cellSpecimenOldObj.getAvailableQuantityInCellCount().intValue();
			
			int  distQty = 0;
			int  newAvailableQty=0;
			// Distributed Qty = Old_Qty - Old_Available_Qty
			distQty = oldQty - oldAvailableQty;
			
			// New_Available_Qty = New_Qty - Distributed_Qty
			newAvailableQty = newQty - distQty;
			Logger.out.debug("Dist Qty: " +distQty+" New Available Qty: " +newAvailableQty);
			if(newAvailableQty<0)
			{
				throw new DAOException("Newly modified Quantity '" + newQty + "' should not be less than current Distributed Quantity '" + distQty + "'");
			}
			else
			{
				// set new available quantity
				cellSpecimenObj.setAvailableQuantityInCellCount(new Integer(newAvailableQty));
			}
		}
		else if(obj instanceof FluidSpecimen)
		{
			Logger.out.debug("In FluidSpecimen");
			FluidSpecimen fluidSpecimenObj = (FluidSpecimen)obj;
			FluidSpecimen fluidSpecimenOldObj = (FluidSpecimen)oldObj;
			// get new qunatity modifed by user
			double newQty = fluidSpecimenObj.getQuantityInMilliliter().doubleValue();
			// get old qunatity from database
			double  oldQty = fluidSpecimenOldObj.getQuantityInMilliliter().doubleValue();
			Logger.out.debug("New Qty: " +newQty+" Old Qty: " +oldQty);
			// get Available qty
			double oldAvailableQty = fluidSpecimenOldObj.getAvailableQuantityInMilliliter().doubleValue();
			
			double  distQty = 0;
			double  newAvailableQty=0;
			// Distributed Qty = Old_Qty - Old_Available_Qty
			distQty = oldQty - oldAvailableQty;
			
			// New_Available_Qty = New_Qty - Distributed_Qty
			newAvailableQty = newQty - distQty;
			Logger.out.debug("Dist Qty: " +distQty+" New Available Qty: " +newAvailableQty);
			if(newAvailableQty<0)
			{
				throw new DAOException("Newly modified Quantity '" + newQty + "' should not be less than current Distributed Quantity '" + distQty + "'");
			}
			else
			{
				fluidSpecimenObj.setAvailableQuantityInMilliliter(new Double(newAvailableQty));
			}
		}
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
    	Logger.out.debug("Specimen Type: " +obj+" ----- "+oldObj);
//    	try
//    	{
    		setAvailableQuantity(specimen,specimenOld);
//    	}
//    	catch(BizLogicException e)
//    	{
//    		Logger.out.error(e.getMessage(),e);
//    		throw new DAOException(e.getMessage(),e); 
//    	}
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
			
        	SpecimenCollectionGroup scg = loadSpecimenCollectionGroup(specimen.getParentSpecimen().getSystemIdentifier(), dao);
        	
        	specimen.setSpecimenCollectionGroup(scg);
        	SpecimenCharacteristics sc= loadSpecimenCharacteristics(specimen.getParentSpecimen().getSystemIdentifier(), dao);
        	specimen.setSpecimenCharacteristics(sc);
        	 
        }
    	//check for closed Specimen Collection Group
    	if(!specimen.getSpecimenCollectionGroup().getSystemIdentifier().equals(specimenOld.getSpecimenCollectionGroup().getSystemIdentifier()))
    		checkStatus(dao,specimen.getSpecimenCollectionGroup(), "Specimen Collection Group" );
		
    	setSpecimenGroupForSubSpecimen(specimen,specimen.getSpecimenCollectionGroup(),specimen.getSpecimenCharacteristics());
    	
		dao.update(specimen.getSpecimenCharacteristics(), sessionDataBean, true, true, false);
		
    	dao.update(specimen, sessionDataBean, true, true, false);
		
		//Audit of Specimen.
		dao.audit(obj, oldObj, sessionDataBean, true);
		
		//Audit of Specimen Characteristics.
		dao.audit(specimen.getSpecimenCharacteristics(), specimenOld.getSpecimenCharacteristics(), sessionDataBean, true);
		
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
    
    private void setSpecimenAttributes(DAO dao, Specimen specimen,SessionDataBean sessionDataBean) throws DAOException,SMException
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
			
			StorageContainerBizLogic storageContainerBizLogic 
									= (StorageContainerBizLogic)BizLogicFactory
											.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
			
			// --- check for all validations on the storage container.
			storageContainerBizLogic.checkContainer(dao,container.getSystemIdentifier().toString(),
					specimen.getPositionDimensionOne().toString(),specimen.getPositionDimensionTwo().toString(),sessionDataBean);
			
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
    public void assignPrivilegeToRelatedObjectsForSCG(DAO dao, String privilegeName, Long[] specimenCollectionGroupArr, Long userId, String roleId, boolean assignToUser, boolean assignOperation) throws SMException, DAOException
    {
        Logger.out.debug("assignPrivilegeToRelatedObjectsForSCG NewSpecimenBizLogic");
    	List listOfSpecimenId = super.getRelatedObjects(dao, Specimen.class, "specimenCollectionGroup", 
    			 specimenCollectionGroupArr);
    	if(!listOfSpecimenId.isEmpty())
    	{
    	    super.setPrivilege(dao,privilegeName,Specimen.class,Utility.toLongArray(listOfSpecimenId),userId, roleId, assignToUser, assignOperation);
    	    List specimenCharacteristicsIds = super.getRelatedObjects(dao,Specimen.class,new String[] {"specimenCharacteristics."+Constants.SYSTEM_IDENTIFIER},new String[]{Constants.SYSTEM_IDENTIFIER},Utility.toLongArray(listOfSpecimenId));
    	    super.setPrivilege(dao,privilegeName,Address.class,Utility.toLongArray(specimenCharacteristicsIds),userId, roleId, assignToUser, assignOperation);
    	    
    	    assignPrivilegeToSubSpecimens(dao,privilegeName,Specimen.class,Utility.toLongArray(listOfSpecimenId),userId, roleId, assignToUser, assignOperation);
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
    private void assignPrivilegeToSubSpecimens(DAO dao, String privilegeName, Class class1, Long[] speIDArr, Long userId, String roleId, boolean assignToUser, boolean assignOperation) throws SMException, DAOException
    {
        List listOfSubElement = super.getRelatedObjects(dao, Specimen.class, "parentSpecimen",  speIDArr);
    	
    	if(listOfSubElement.isEmpty())
    		return;
    	super.setPrivilege(dao,privilegeName,Specimen.class,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser, assignOperation);
    	List specimenCharacteristicsIds = super.getRelatedObjects(dao,Specimen.class,new String[] {"specimenCharacteristics."+Constants.SYSTEM_IDENTIFIER},new String[]{Constants.SYSTEM_IDENTIFIER},Utility.toLongArray(listOfSubElement));
	    super.setPrivilege(dao,privilegeName,Address.class,Utility.toLongArray(specimenCharacteristicsIds),userId, roleId, assignToUser, assignOperation);
    	
    	assignPrivilegeToSubSpecimens(dao,privilegeName,Specimen.class, Utility.toLongArray(listOfSubElement),userId,roleId,assignToUser, assignOperation);
    }
    
    public void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser, boolean assignOperation) throws SMException, DAOException
    {
	    super.setPrivilege(dao,privilegeName,objectType,objectIds,userId, roleId, assignToUser, assignOperation);
	    List specimenCharacteristicsIds = super.getRelatedObjects(dao,Specimen.class,new String[] {"specimenCharacteristics."+Constants.SYSTEM_IDENTIFIER},new String[]{Constants.SYSTEM_IDENTIFIER},objectIds);
	    super.setPrivilege(dao,privilegeName,Address.class,Utility.toLongArray(specimenCharacteristicsIds),userId, roleId, assignToUser, assignOperation);
	    
	    assignPrivilegeToSubSpecimens(dao,privilegeName,Specimen.class,objectIds,userId, roleId, assignToUser, assignOperation);
    }
    
// validation code here
    /**
     * @see AbstractBizLogic#setPrivilege(DAO, String, Class, Long[], Long, String, boolean)
     * @param dao
     * @param privilegeName
     * @param objectIds
     * @param userId
     * @param roleId
     * @param assignToUser
     * @throws SMException
     * @throws DAOException
     */
    public void assignPrivilegeToRelatedObjectsForDistributedItem(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId, boolean assignToUser, boolean assignOperation)throws SMException, DAOException
    {
        String [] selectColumnNames = {"specimen.systemIdentifier"};
        String [] whereColumnNames = {"systemIdentifier"};
        List listOfSubElement = super.getRelatedObjects(dao, DistributedItem.class, selectColumnNames, whereColumnNames, objectIds);
    	if(!listOfSubElement.isEmpty())
    	{
    	    super.setPrivilege(dao,privilegeName,Specimen.class,Utility.toLongArray(listOfSubElement),userId,roleId, assignToUser, assignOperation);
    	}
    }
    
    /**
     * Overriding the parent class's method to validate the enumerated attribute values
     */
    protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
    {
		Specimen specimen = (Specimen)obj;
		
		List specimenClassList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_SPECIMEN_CLASS,null);
		String specimenClass = Utility.getSpecimenClassName(specimen);
    	
    	if(!Validator.isEnumeratedValue(specimenClassList,specimenClass))
		{
			throw new DAOException(ApplicationProperties.getValue("protocol.class.errMsg"));
		}
		
    	if(specimenClass.equals(Constants.CELL))
    	{
    		if(specimen.getType() != null)
    		{
    			throw new DAOException(ApplicationProperties.getValue("protocol.type.errMsg"));
    		}
    	}
		else if(!Validator.isEnumeratedValue(Utility.getSpecimenTypes(specimenClass),specimen.getType()))
		{
			throw new DAOException(ApplicationProperties.getValue("protocol.type.errMsg"));
		}
		
		SpecimenCharacteristics characters = specimen.getSpecimenCharacteristics();

		if(characters == null)
		{
			throw new DAOException(ApplicationProperties.getValue("specimen.characteristics.errMsg"));
		}
		else
		{
			NameValueBean undefinedVal = new NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
	    	List tissueSiteList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_TISSUE_SITE,undefinedVal);
			
	    	if(!Validator.isEnumeratedValue(tissueSiteList,characters.getTissueSite()))
			{
				throw new DAOException(ApplicationProperties.getValue("protocol.tissueSite.errMsg"));
			}
			
	    	NameValueBean unknownVal = new NameValueBean(Constants.UNKNOWN,Constants.UNKNOWN);
	    	List tissueSideList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_TISSUE_SIDE,unknownVal);

			if(!Validator.isEnumeratedValue(tissueSideList,characters.getTissueSide()))
			{
				throw new DAOException(ApplicationProperties.getValue("specimen.tissueSide.errMsg"));
			}
			
			List pathologicalStatusList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_PATHOLOGICAL_STATUS,null);
			
			if(!Validator.isEnumeratedValue(pathologicalStatusList,characters.getPathologicalStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("protocol.pathologyStatus.errMsg"));
			}
		}
		
		if(operation.equals(Constants.ADD))
		{
			if(!specimen.getAvailable().booleanValue())
			{
				throw new DAOException(ApplicationProperties.getValue("specimen.available.errMsg"));
			}
			
			if(!Constants.ACTIVITY_STATUS_ACTIVE.equals(specimen.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.active.errMsg"));
			}
		}
		else
		{
			if(!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES,specimen.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
			}
		}
    	
    	return true;
    }
}