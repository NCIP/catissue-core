/**
 * <p>Title: DistributionHDAO Class>
 * <p>Description:	DistributionHDAO is used to add distribution information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Aug 23, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * DistributionHDAO is used to add distribution information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class DistributionBizLogic extends DefaultBizLogic
{
	/**
     * Saves the Distribution object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException 
	{
		Distribution dist = (Distribution)obj;
		
		//Load & set the User
		Object userObj = dao.retrieve(User.class.getName(), dist.getUser().getSystemIdentifier());
		if(userObj!=null)
		{
			User user = (User)userObj;
			dist.setUser(user);
		}
		
		//Load & set From Site
//		Object siteObj = dao.retrieve(Site.class.getName(), dist.getFromSite().getSystemIdentifier());
//		if(siteObj!=null)
//		{
//			Site site = (Site)siteObj;
//			dist.setFromSite(site);
//		}

		//Load & set the To Site
		Object siteObj = dao.retrieve(Site.class.getName(), dist.getToSite().getSystemIdentifier());
		if(siteObj!=null )
		{
			Site site = (Site)siteObj;
			dist.setToSite(site);
		}
		
		dao.insert(dist,sessionDataBean, Constants.IS_AUDITABLE_TRUE,Constants.IS_SECURE_UPDATE_TRUE);
		Collection distributedItemCollection = dist.getDistributedItemCollection();		
		Iterator it = distributedItemCollection.iterator();
		while(it.hasNext())
		{
			DistributedItem item = (DistributedItem)it.next();
			//update the available quantity
			Object specimenObj = dao.retrieve(Specimen.class.getName(), item.getSpecimen().getSystemIdentifier());
			double quantity = item.getQuantity().doubleValue();
			boolean availability = checkAvailableQty((Specimen)specimenObj,quantity);
			if (!availability)
            {
                throw new DAOException(ApplicationProperties.getValue("errors.distribution.quantity"));
            }
			else
			{
				dao.update(specimenObj,sessionDataBean,Constants.IS_AUDITABLE_TRUE,Constants.IS_SECURE_UPDATE_TRUE,Constants.HAS_OBJECT_LEVEL_PRIVILEGE_FALSE);
			}
			item.setDistribution(dist);
			dao.insert(item,sessionDataBean, Constants.IS_AUDITABLE_TRUE,Constants.IS_SECURE_UPDATE_TRUE);
		}
		
		try
        {
            SecurityManager.getInstance(this.getClass()).insertAuthorizationData(
            			null, getProtectionObjects(dist), getDynamicGroups(dist));
        }
		catch (SMException e)
        {
			throw handleSMException(e);
        }
	}
	
	private Set getProtectionObjects(AbstractDomainObject obj)
    {
        Set protectionObjects = new HashSet();
        
        Distribution distribution = (Distribution) obj;
        protectionObjects.add(distribution);
        
        Iterator distributedItemIterator = distribution.getDistributedItemCollection().iterator();
        while (distributedItemIterator.hasNext())
        {
            DistributedItem distributedItem = (DistributedItem) distributedItemIterator.next();  
            protectionObjects.add(distributedItem.getSpecimen());
        }
        
        return protectionObjects;
    }
	
    private String[] getDynamicGroups(AbstractDomainObject obj)
    {
        Distribution distribution = (Distribution) obj;
        String[] dynamicGroups = new String[1];
        dynamicGroups[0] = Constants.getDistributionProtocolPGName(
        		distribution.getDistributionProtocol().getSystemIdentifier());
        
        return dynamicGroups;
    }
	
	/**
     * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 * @throws HibernateException Exception thrown during hibernate operations.
     */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException 
    {
		Distribution distribution = (Distribution)obj;
		dao.update(obj, sessionDataBean, Constants.IS_AUDITABLE_TRUE,Constants.IS_SECURE_UPDATE_TRUE, Constants.HAS_OBJECT_LEVEL_PRIVILEGE_FALSE);
		
		//Audit of Distribution.
		dao.audit(obj, oldObj, sessionDataBean, Constants.IS_AUDITABLE_TRUE);
		
		Distribution oldDistribution = (Distribution)oldObj;
		Collection oldDistributedItemCollection = oldDistribution.getDistributedItemCollection();
		
		Collection distributedItemCollection = distribution.getDistributedItemCollection();		
		Iterator it = distributedItemCollection.iterator();
		while(it.hasNext())
		{
			DistributedItem item = (DistributedItem)it.next();
			
			DistributedItem oldItem 
				= (DistributedItem)getCorrespondingOldObject(oldDistributedItemCollection, 
				        item.getSystemIdentifier());
			//update the available quantity
			Object specimenObj = dao.retrieve(Specimen.class.getName(), item.getSpecimen().getSystemIdentifier());
	        Double previousQuantity = (Double)item.getPreviousQuantity();
	        double quantity = item.getQuantity().doubleValue();
	        if(previousQuantity!=null)
	        {
	        	quantity = quantity-previousQuantity.doubleValue();
	        }
	        boolean availability = checkAvailableQty((Specimen)specimenObj,quantity);
	        
			if (!availability)
            {
                throw new DAOException(ApplicationProperties.getValue("errors.distribution.quantity"));
            }
			else
			{
				dao.update(specimenObj,sessionDataBean,Constants.IS_AUDITABLE_TRUE,Constants.IS_SECURE_UPDATE_TRUE, Constants.HAS_OBJECT_LEVEL_PRIVILEGE_FALSE);
				//Audit of Specimen.
				//If a new specimen is distributed.
				if(oldItem==null)
				{
					Object specimenObjPrev = dao.retrieve(Specimen.class.getName(), item.getSpecimen().getSystemIdentifier());
					dao.audit(specimenObj, specimenObjPrev, sessionDataBean, Constants.IS_AUDITABLE_TRUE);
				}
				//if a distributed specimen is updated  
				else
					dao.audit(specimenObj, oldItem.getSpecimen(), sessionDataBean, Constants.IS_AUDITABLE_TRUE);
			}
			item.setDistribution(distribution);
			
			dao.update(item, sessionDataBean,Constants.IS_AUDITABLE_TRUE,Constants.IS_SECURE_UPDATE_TRUE,  Constants.HAS_OBJECT_LEVEL_PRIVILEGE_FALSE);
			
			//Audit of Distributed Item.
			dao.audit(item, oldItem, sessionDataBean, Constants.IS_AUDITABLE_TRUE);
		}
    }
	private boolean checkAvailableQty(Specimen specimen, double quantity)
	{
		if(specimen instanceof TissueSpecimen)
		{
			TissueSpecimen tissueSpecimen = (TissueSpecimen) specimen;
			double availabeQty = tissueSpecimen.getAvailableQuantityInGram().doubleValue();
			Logger.out.debug("TissueAvailabeQty"+availabeQty);
			if(quantity > availabeQty)
				return false;
			else
			{
				availabeQty = availabeQty - quantity;
				Logger.out.debug("TissueAvailabeQty after deduction"+availabeQty);
				tissueSpecimen.setAvailableQuantityInGram(new Double(availabeQty));
			}
		}
		else if(specimen instanceof CellSpecimen)
		{
			CellSpecimen cellSpecimen = (CellSpecimen) specimen;
			int availabeQty = cellSpecimen.getAvailableQuantityInCellCount().intValue();
			if(quantity > availabeQty)
				return false;
			else
			{
				availabeQty = availabeQty - (int)quantity;
				cellSpecimen.setAvailableQuantityInCellCount(new Integer(availabeQty));
			}
		}
		else if(specimen instanceof MolecularSpecimen)
		{
			MolecularSpecimen molecularSpecimen = (MolecularSpecimen) specimen;
			double availabeQty = molecularSpecimen.getAvailableQuantityInMicrogram().doubleValue();
			if(quantity > availabeQty)
				return false;
			else
			{
				availabeQty = availabeQty - quantity;
				molecularSpecimen.setAvailableQuantityInMicrogram(new Double(availabeQty));
			}
		}
		else if(specimen instanceof FluidSpecimen)
		{
			FluidSpecimen fluidSpecimen = (FluidSpecimen) specimen;
			double availabeQty = fluidSpecimen.getAvailableQuantityInMilliliter().doubleValue();
			if(quantity > availabeQty)
				return false;
			else
			{
				availabeQty = availabeQty - quantity;
				fluidSpecimen.setAvailableQuantityInMilliliter(new Double(availabeQty));
			}
		}
		return true;
		
	}
	
	public void disableRelatedObjects(DAO dao, Long distributionProtocolIDArr[])throws DAOException 
    {
    	List listOfSubElement = super.disableObjects(dao, Distribution.class, "distributionProtocol", 
    			"CATISSUE_DISTRIBUTION", "DISTRIBUTION_PROTOCOL_ID", distributionProtocolIDArr);
    }
	
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
    public void assignPrivilegeToRelatedObjectsForDP(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId, boolean assignToUser, boolean assignOperation)throws SMException, DAOException
    {
        List listOfSubElement = super.getRelatedObjects(dao, Distribution.class, "distributionProtocol",objectIds);
        Logger.out.debug("Distribution................"+listOfSubElement.size());
    	if(!listOfSubElement.isEmpty())
    	{
    	    Logger.out.debug("Distribution Id : ................"+listOfSubElement.get(0));
    	    super.setPrivilege(dao,privilegeName,Distribution.class,Utility.toLongArray(listOfSubElement),userId,roleId, assignToUser, assignOperation);
    	    
			assignPrivilegeToRelatedObjectsForDistribution(dao,privilegeName,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser, assignOperation);
    	}
    }
    
    public void assignPrivilegeToRelatedObjectsForDistribution(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId, boolean assignToUser, boolean assignOperation)throws SMException, DAOException
    {
        List listOfSubElement = super.getRelatedObjects(dao, DistributedItem.class, "distribution",objectIds);
        Logger.out.debug("Distributed Item................"+listOfSubElement.size());
        if(!listOfSubElement.isEmpty())
    	{
            Logger.out.debug("Distribution Item Id : ................"+listOfSubElement.get(0));
    	    super.setPrivilege(dao,privilegeName,DistributedItem.class,Utility.toLongArray(listOfSubElement),userId,roleId, assignToUser, assignOperation);
    	    
    	    NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic)BizLogicFactory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
    	    bizLogic.assignPrivilegeToRelatedObjectsForDistributedItem(dao,privilegeName,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser, assignOperation);
    	}
        
    }
    
    /**
     * Overriding the parent class's method to validate the enumerated attribute values
     */
    protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
    {
    	Distribution distribution = (Distribution)obj;

    	if(operation.equals(Constants.ADD))
		{
			if(!Constants.ACTIVITY_STATUS_ACTIVE.equals(distribution.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.active.errMsg"));
			}
		}
		else
		{
			if(!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES,distribution.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
			}
		}
    	
    	return true;
    }
}