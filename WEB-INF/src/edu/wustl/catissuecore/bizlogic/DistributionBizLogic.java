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
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
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
		Object siteObj = dao.retrieve(Site.class.getName(), dist.getFromSite().getSystemIdentifier());
		if(siteObj!=null)
		{
			Site site = (Site)siteObj;
			dist.setFromSite(site);
		}

		//Load & set the To Site
		siteObj = dao.retrieve(Site.class.getName(), dist.getToSite().getSystemIdentifier());
		if(siteObj!=null )
		{
			Site site = (Site)siteObj;
			dist.setToSite(site);
		}

		dao.insert(dist,sessionDataBean, true, true);
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
				dao.update(specimenObj,sessionDataBean,true,true,false);
			}
			item.setDistribution(dist);
			dao.insert(item,sessionDataBean, true, true);
		}
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
		dao.update(obj, sessionDataBean, true, true, false);
		
		//Audit of Distribution.
		dao.audit(obj, oldObj, sessionDataBean, true);
		
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
	        Logger.out.debug("previousQuantity "+previousQuantity);
			double quantity = item.getQuantity().doubleValue()-previousQuantity.doubleValue();
			
			boolean availability = checkAvailableQty((Specimen)specimenObj,quantity);
			if (!availability)
            {
                throw new DAOException(ApplicationProperties.getValue("errors.distribution.quantity"));
            }
			else
			{
				dao.update(specimenObj,sessionDataBean,true,true,false);
				
				//Audit of Specimen.
				dao.audit(specimenObj, oldItem.getSpecimen(), sessionDataBean, true);
			}
			item.setDistribution(distribution);
			
			dao.update(item, sessionDataBean, true, true, false);
			
			//Audit of Distributed Item.
			dao.audit(item, oldItem, sessionDataBean, true);
		}
    }
	public boolean checkAvailableQty(Specimen specimen, double quantity)
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
}