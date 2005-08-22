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
import java.util.HashSet;
import java.util.Map;
import java.util.Date;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.DistributionForm;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

/**
 * An event that results in transfer of a specimen from a Repository to a Laboratory.
 * @hibernate.joined-subclass table="CATISSUE_DISTRIBUTION"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 * @author Mandar Deshmukh
 */
public class Distribution extends SpecimenEventParameters implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * New location(site) of the item.
	 */
	protected Site toSite;

	/**
	 * Original location(site) of the item. 
	 */
	protected Site fromSite;
	
	/**
	 * DistributionProtocol associated with this Distribution.
	 */
	protected DistributionProtocol distributionProtocol;
	
	/**
	 * Collection of Items distributed in this distribution.
	 */
	protected Collection distributedItemCollection = new HashSet();

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

	/**
	 * Returns the original/source Site of the Distribution.
	 * @hibernate.many-to-one column="FROM_SITE_ID"
	 * class="edu.wustl.catissuecore.domain.Site" constrained="true"
	 * @return the original/source Site of the Distribution.
	 */
	public Site getFromSite()
	{
		return fromSite;
	}

	/**
	 * @param fromSite
	 *  The fromSite to set.
	 */
	public void setFromSite(Site fromSite)
	{
		this.fromSite = fromSite;
	}

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
	    	DistributionForm form = (DistributionForm) abstractForm;
	        super.setAllValues(form);
	        this.toSite = new Site();
	        toSite.setSystemIdentifier(new Long(form.getToSite()));
	        this.fromSite =new Site();
	        fromSite.setSystemIdentifier(new Long(form.getFromSite()));
	        
	        Map map = form.getValues();
	        System.out.println("Map "+map);
	        MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
	        distributedItemCollection = parser.generateData(map);
	        System.out.println("distributedItemCollection "+distributedItemCollection);
	    }
	    catch(Exception excp)
	    {
	    	excp.printStackTrace();
	        Logger.out.error(excp.getMessage());
	    }
	}
}