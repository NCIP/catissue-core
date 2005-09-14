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
	protected Site toSite = new Site();

	/**
	 * Original location(site) of the item. 
	 */
	protected Site fromSite = new Site();
	
	/**
	 * DistributionProtocol associated with this Distribution.
	 */
	protected DistributionProtocol distributionProtocol = new DistributionProtocol();
	
	/**
	 * Collection of Items distributed in this distribution.
	 */
	protected Collection distributedItemCollection = new HashSet();

	//Default Constructor
	public Distribution()
	{
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
	    	super.setAllValues(abstractForm);
	    	
	    	super.specimen = null;
	    	
	    	DistributionForm form = (DistributionForm) abstractForm;
	    	
	        toSite.setSystemIdentifier(new Long(form.getToSite()));
	        fromSite.setSystemIdentifier(new Long(form.getFromSite()));
	        
	        distributionProtocol.setSystemIdentifier(new Long(form.getDistributionProtocolId()));
	        
	        Map map = form.getValues();
	        Logger.out.debug("map "+map);
	        map = fixMap(map);
	        MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
	        distributedItemCollection = parser.generateData(map);
	        Logger.out.debug("distributedItemCollection "+distributedItemCollection);
	    }
	    catch(Exception excp)
	    {
	   
	    	// use of logger as per bug 79
	    	Logger.out.error(excp.getMessage(),excp); 
	   
	    }
	}
	
	protected Map fixMap(Map orgMap)
	{
		Map replaceMap = new HashMap();		
		Iterator it = orgMap.keySet().iterator();
		
		while(it.hasNext())
		{
			String key = (String)it.next();
			Logger.out.debug("Key************************"+key);
			if(key.indexOf("className")!=-1)
			{
				String value = (String)orgMap.get(key);
				Logger.out.debug("Value..........................."+value); 
				String replaceWith = "Specimen"+"#"+value+"Specimen";
				
				key = key.substring(0,key.lastIndexOf("_"));
				Logger.out.debug("Second Key***********************"+key);
				String newKey = key.replaceFirst("Specimen",replaceWith);
				Logger.out.debug("New Key................"+newKey);
				replaceMap.put(key,newKey);
			}
		}
		
		Map newMap = new HashMap();
		it = orgMap.keySet().iterator();
		while(it.hasNext())
		{
			String key = (String)it.next();
			String value = (String)orgMap.get(key);
			
			if(!key.endsWith("_unit"))
			{
				if(key.indexOf("Specimen")==-1)
				{
					newMap.put(key,value);
				}
				else
				{
					if(key.indexOf("className")==-1)
					{
						String keyPart, newKeyPart;
						
						//Replace # and class name and FIX for abstract class
						keyPart = key.substring(0,key.lastIndexOf("_"));
						newKeyPart = (String)replaceMap.get(keyPart);
						key = key.replaceFirst(keyPart,newKeyPart);
						newMap.put(key,value);
					}
				}
			}
		}
		
		return newMap;
	}
}