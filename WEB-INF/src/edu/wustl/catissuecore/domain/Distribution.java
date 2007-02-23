/**
 * <p>Title: Distribution Class</p>
 * <p>Description: An event that results in transfer of a specimen from a Repository to a Laboratory.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.DistributionForm;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

/**
 * An event that results in transfer of a specimen from a Repository to a Laboratory.
 * @hibernate.class table="CATISSUE_DISTRIBUTION"
 * @author Mandar Deshmukh
 */
public class Distribution extends SpecimenEventParameters implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
    // Change for API Search   --- Ashwin 04/10/2006
	/**
	 * New location(site) of the item.
	 */
	protected Site toSite;

	/**
	 * Original location(site) of the item. 
	 */
	//protected Site fromSite = new Site();
	
    // Change for API Search   --- Ashwin 04/10/2006
	/**
	 * DistributionProtocol associated with this Distribution.
	 */
	protected DistributionProtocol distributionProtocol;
	
	/**
	 * Collection of Items distributed in this distribution.
	 */
	protected Collection distributedItemCollection = new HashSet();
	
	/**
	 * Defines whether this SpecimenProtocol record can be queried (Active) or not queried (Inactive) by any actor.
	 */
	protected String activityStatus;
	
	/**
	 * Collection of specimen Array in this distribution.
	 * @see SpecimenArray
	 */
	protected Collection specimenArrayCollection = new HashSet(); 

	/**
	 * OrderDetails associated with the order_item.
	 */
	protected OrderDetails orderId;
	
	/**
	 * @return the orderId
	 * @hibernate.many-to-one column="ORDER_ID" class="edu.wustl.catissuecore.domain.OrderDetails" constrained="true"
	 */
	public OrderDetails getOrderId()
	{
		return orderId;
	}

	
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(OrderDetails orderId)
	{
		this.orderId = orderId;
	}

	//Default Constructor
	public Distribution()
	{
	}
	
	/**
     * Returns System generated unique id.
     * @return System generated unique id.
     * @see #setId(Integer)
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_DISTRIBUTION_SEQ" 
     */
	public Long getId()
	{
		return id;
	}
	public Distribution(AbstractActionForm form)
	{
		setAllValues(form);
	}
	
	// ---- Method Section

	/**
	 * Returns the destination/target Site of the Distribution.
	 * @hibernate.many-to-one column="TO_SITE_ID" 
	 * class="edu.wustl.catissuecore.domain.Site" constrained="true"
	 * @return the destination/target Site of the Distribution.
	 */
	public Site getToSite()
	{
		return toSite;
	}

	/**
	 * @param toSite
	 *  The toSite to set.
	 */
	public void setToSite(Site toSite)
	{
		this.toSite = toSite;
	}

//	/**
//	 * Returns the original/source Site of the Distribution.
//	 * @hibernate.many-to-one column="FROM_SITE_ID"
//	 * class="edu.wustl.catissuecore.domain.Site" constrained="true"
//	 * @return the original/source Site of the Distribution.
//	 */
//	public Site getFromSite()
//	{
//		return fromSite;
//	}
//
//	/**
//	 * @param fromSite
//	 *  The fromSite to set.
//	 */
//	public void setFromSite(Site fromSite)
//	{
//		this.fromSite = fromSite;
//	}

	/**
	 * Returns the distributionProtocol of the distribution.
	 * @hibernate.many-to-one column="DISTRIBUTION_PROTOCOL_ID"
	 * class="edu.wustl.catissuecore.domain.DistributionProtocol"
	 * constrained="true"
	 * @return the distributionProtocol of the distribution.
	 */
	public DistributionProtocol getDistributionProtocol()
	{
		return distributionProtocol;
	}

	/**
	 * @param distributionProtocol
	 *  The distributionProtocol to set.
	 */
	public void setDistributionProtocol(DistributionProtocol distributionProtocol)
	{
		this.distributionProtocol = distributionProtocol;
	}

	/**
	 * Returns the collection of DistributedItems for this Distribution.
	 * @hibernate.set name="distributedItemCollection"
	 *                table="CATISSUE_DISTRIBUTED_ITEM" cascade="save-update"
	 *                inverse="true" lazy="false"
	 * @hibernate.collection-key column="DISTRIBUTION_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.DistributedItem"
	 * @return Returns the collection of DistributedItems for this Distribution.
	 */
	public Collection getDistributedItemCollection()
	{
		return distributedItemCollection;
	}

	/**
	 * Returns the activityStatus.
	 * @hibernate.property name="activityStatus" type="string" column="ACTIVITY_STATUS" length="50"
	 * @return Returns the activityStatus.
	 */
	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * @param activityStatus The activityStatus to set.
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}
	
	/**
	 * @param distributedItemCollection
	 *  The distributedItemCollection to set.
	 */
	public void setDistributedItemCollection(Collection distributedItemCollection)
	{
		this.distributedItemCollection = distributedItemCollection;
	}
	public void setAllValues(AbstractActionForm abstractForm)
	{
	    try
	    {
	    	super.setAllValues(abstractForm);
	    	super.specimen = null;
	        // Change for API Search   --- Ashwin 04/10/2006
	    	if (SearchUtil.isNullobject(toSite))
	    	{
	    		toSite = new Site();
	    	}
	    	
	        // Change for API Search   --- Ashwin 04/10/2006
	    	if (SearchUtil.isNullobject(distributionProtocol))
	    	{
	    		distributionProtocol = new DistributionProtocol();
	    	}

	    	DistributionForm form = (DistributionForm) abstractForm;
	        toSite.setId(new Long(form.getToSite()));
	        //fromSite.setId(new Long(form.getFromSite()));
	        distributionProtocol.setId(new Long(form.getDistributionProtocolId()));
	        this.activityStatus = form.getActivityStatus();
	        
	        Map map = form.getValues();
	        Logger.out.debug("map "+map);
	        map = fixMap(map);
	        Logger.out.debug("fixedMap "+map);
	        MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
	        //Change for Consent tracking (Virender Mehta)
	        Collection itemCollectionMap = parser.generateData(map);
	        Collection finalItemCollecitonMap = new HashSet();
//	        if(form.getDistributionType().intValue() == Constants.SPECIMEN_DISTRIBUTION_TYPE)
//	        {  
		        Iterator itr = itemCollectionMap.iterator();
		        while(itr.hasNext())
		        {
		        	DistributedItem distributedItem = (DistributedItem) itr.next();
		        	if(distributedItem.getSpecimen() != null)
		        		finalItemCollecitonMap.add(distributedItem);
		        	//Added by Ashish
		        	if(distributedItem.getSpecimenArray() != null)
		        		finalItemCollecitonMap.add(distributedItem);
		        }
//	        }
	        
//	        if (form.getDistributionType().intValue() == Constants.SPECIMEN_DISTRIBUTION_TYPE ) 
//	        {
		        distributedItemCollection = finalItemCollecitonMap;
//	        }
//	        else
//	        {
//	        	specimenArrayCollection = finalItemCollecitonMap;
//	        }
	        Logger.out.debug("distributedItemCollection "+distributedItemCollection);
	    }
	    //Change for Consent tracking (Virender Mehta)	    
	    catch(Exception excp)
	    {
	    	// use of logger as per bug 79
	    	Logger.out.error(excp.getMessage(),excp); 
	    }
	}
	
	protected Map fixMap(Map orgMap)
	{
		Iterator it = orgMap.keySet().iterator();
		Map newMap = new HashMap();
		while(it.hasNext())
		{
			String key = (String)it.next();
			String value = (String)orgMap.get(key);
			if(key.endsWith("_id") || key.endsWith("uantity"))
			{
				newMap.put(key,value);
			}
		}	
		return newMap;
		
	}

	/**
	 * Returns a collection of specimen array.
	 * @hibernate.set name="specimenArrayCollection"
	 *                table="CATISSUE_SPECIMEN_ARRAY" cascade="none"
	 *                inverse="false" lazy="false"
	 * @hibernate.collection-key column="DISTRIBUTION_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.SpecimenArray"
	 * @return specimenArrayCollection a collection of specimen array.
	 */
	public Collection getSpecimenArrayCollection() {
		return specimenArrayCollection;
	}

	/**
	 * Sets the collection of specimen Array.
	 * @param specimenArrayCollection set of specimen array
	 */
	public void setSpecimenArrayCollection(Collection specimenArrayCollection) {
		this.specimenArrayCollection = specimenArrayCollection;
	}
	
	/**
     * Returns message label to display on success add or edit
     * @return String
     */
	public String getMessageLabel() {
        // Change for API Search   --- Ashwin 04/10/2006
    	if (SearchUtil.isNullobject(distributionProtocol))
    	{
    		distributionProtocol = new DistributionProtocol();
    	}
		String message = this.distributionProtocol.title + " ";
		if (this.user != null) {
			if (this.user.lastName!= null && !this.user.lastName.equals("") && this.user.firstName != null && !this.user.firstName.equals("")) 
			{
				message = message + this.user.lastName + "," + this.user.firstName;
			} 
			else if(this.user.lastName!= null && !this.user.lastName.equals(""))
			{
				message = message + this.user.lastName;
			}
			else if(this.user.firstName!= null && !this.user.firstName.equals(""))
			{
				message = message + this.user.firstName;
			}		
		} 		
		else if (this.user != null)
		{
			message = message + this.user;
		}			
		return message;
	}
}