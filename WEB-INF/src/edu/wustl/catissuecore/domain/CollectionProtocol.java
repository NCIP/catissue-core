/**
 * <p>Title: CollectionProtocol Class</p>
 * <p>Description:  A set of written procedures that describe how a biospecimen is prospectively collected.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

/**
 * A set of written procedures that describe how a biospecimen is prospectively collected.
 * @hibernate.joined-subclass table="CATISSUE_COLLECTION_PROTOCOL"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 * @author Mandar Deshmukh
 */
public class CollectionProtocol extends SpecimenProtocol implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * Collection of studies associated with the CollectionProtocol.
	 */
	protected Collection distributionProtocolCollection = new HashSet();
	
	/**
	 * Collection of users associated with the CollectionProtocol.
	 */
	protected Collection userCollection = new HashSet();
	
	/**
	 * Collection of CollectionProtocolEvents associated with the CollectionProtocol.
	 */
	protected Collection collectionProtocolEventCollection = new HashSet();
	
	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 * */
	public CollectionProtocol()
	{
		super();
	}
	
	public CollectionProtocol(AbstractActionForm form)
	{
		setAllValues(form);
	}
	
	/**
	 * Returns the collection of Studies for this Protocol.
	 * @hibernate.set name="distributionProtocolCollection" table="CATISSUE_COLLECTION_DISTRIBUTION_RELATION" 
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.DistributionProtocol" column="DISTRIBUTION_PROTOCOL_ID"
	 * @return Returns the collection of Studies for this Protocol.
	 */
	public Collection getDistributionProtocolCollection()
	{
		return distributionProtocolCollection;
	}

	/**
	 * @param studyCollection The studyCollection to set.
	 */
	public void setDistributionProtocolCollection(Collection distributionProtocolCollection)
	{
		this.distributionProtocolCollection = distributionProtocolCollection;
	}

	/**
	 * Returns the collection of Users(ProtocolCoordinators) for this Protocol.
	 * @hibernate.set name="userCollection" table="CATISSUE_COLLECTION_COORDINATORS" 
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.User" column="USER_ID"
	 * @return The collection of Users(ProtocolCoordinators) for this Protocol.
	 */
	public Collection getUserCollection()
	{
		return userCollection;
	}

	/**
	 * @param userCollection The userCollection to set.
	 */
	public void setUserCollection(Collection userCollection)
	{
		this.userCollection = userCollection;
	}

	/**
	 * Returns the collection of CollectionProtocolEvents for this Protocol.
	 * @hibernate.set name="collectionProtocolEventCollection"
	 * table="CATISSUE_COLLECTION_PROTOCOL_EVENT" cascade="save-update"
	 * inverse="true" lazy="false"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.CollectionProtocolEvent"
	 * @return The collection of CollectionProtocolEvents for this Protocol.
	 */
	public Collection getCollectionProtocolEventCollection()
	{
		return collectionProtocolEventCollection;
	}

	/**
	 * @param collectionProtocolEventCollection
	 * The collectionProtocolEventCollection to set.
	 */
	public void setCollectionProtocolEventCollection(Collection collectionProtocolEventCollection)
	{
		this.collectionProtocolEventCollection = collectionProtocolEventCollection;
	}
	
	   /**
     * This function Copies the data from an CollectionProtocolForm object to a CollectionProtocol object.
     * @param CollectionProtocol An CollectionProtocolForm object containing the information about the CollectionProtocol.  
     * */
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
        	super.setAllValues(abstractForm);
        	
        	CollectionProtocolForm cpForm = (CollectionProtocolForm) abstractForm;
        	
        	//userCollection.clear();
        	long [] coordinatorsArr = cpForm.getProtocolCoordinatorIds();
        	for (int i = 0; i < coordinatorsArr.length; i++)
			{
        		if(coordinatorsArr[i]!=-1)
        		{
	        		User coordinator = new User();
	        		coordinator.setSystemIdentifier(new Long(coordinatorsArr[i]));
	        		
//	        		if(!contians(userCollection, coordinator))
//	        		{
	        			//Temp Fix
	        			if(abstractForm.isAddOperation())
	        				userCollection.add(coordinator);
//	        		}
        		}
			}
        	
	        Map map = cpForm.getValues();
	        Logger.out.debug("PRE FIX MAP "+map);
	        map = fixMap(map);
	        System.out.println("POST FIX MAP "+map);
	        
	        MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
	        this.collectionProtocolEventCollection = parser.generateData(map);
	        Logger.out.debug("collectionProtocolEventCollection "+this.collectionProtocolEventCollection);
        }
        catch (Exception excp)
        {
	    	// use of logger as per bug 79
	    	Logger.out.error(excp.getMessage(),excp); 
        }
    }
    
    private boolean contiansCoordinator(User coordinator)
    {
    	return userCollection.contains(coordinator);
    }
    
    public static boolean contians(Collection collection, AbstractDomainObject obj)
    {
    	Iterator iterator = collection.iterator();
    	while(iterator.hasNext())
    	{
    		Object absObj = iterator.next();
    		if(absObj instanceof AbstractDomainObject)
    		{
    			AbstractDomainObject abstractDomainObject = (AbstractDomainObject)absObj;
    			if(abstractDomainObject.getSystemIdentifier() == obj.getSystemIdentifier())
    				return true;
    		}
    	}
    	return false;
    }
}