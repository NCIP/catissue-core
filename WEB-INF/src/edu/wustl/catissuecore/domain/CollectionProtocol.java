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
import java.util.Map;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

/**
 * A set of written procedures that describe how a biospecimen is prospectively collected.
 * @hibernate.joined-subclass table="CATISSUE_COLLECTION_PROTOCOL"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 * @author Mandar Deshmukh
 */
public class CollectionProtocol extends SpecimenProtocol implements java.io.Serializable,Comparable
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
	 * whether Aliquote in same container
	 */
	protected Boolean aliqoutInSameContainer;
	
	//protected Collection storageContainerCollection=new HashSet();
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
	 * @hibernate.set name="distributionProtocolCollection" table="CATISSUE_COLL_DISTRIBUTION_REL" 
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
	 * @hibernate.set name="userCollection" table="CATISSUE_COLL_COORDINATORS" 
	 * cascade="none" inverse="false" lazy="false"
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
	 * table="CATISSUE_COLL_PROT_EVENT" cascade="save-update"
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
	
	/*
	 * Returns the collection of Containers for this Protocol.
	 * @hibernate.set name="storageContainerCollection" table="CATISSUE_CONTAINER_CP_REL" 
	 * cascade="none" inverse="false" lazy="false"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.StorageContainer" column="STORAGE_CONTAINER_ID"
	 * @return The collection of Storage Containers for this Protocol.*/
	 /*
	public Collection getStorageContainerCollection()
	{
		return storageContainerCollection;
	}
*/
	/*
	 * @param storageContainerCollection The storageContainerCollection to set.
	 */
	/*public void setStorageContainerCollection(Collection storageContainerCollection)
	{
		this.storageContainerCollection = storageContainerCollection;
	}*/

	   /**
     * This function Copies the data from an CollectionProtocolForm object to a CollectionProtocol object.
     * @param CollectionProtocol An CollectionProtocolForm object containing the information about the CollectionProtocol.  
     * */
    public void setAllValues(IValueObject abstractForm)
    {
        try
        {
        	super.setAllValues(abstractForm);
        	
        	CollectionProtocolForm cpForm = (CollectionProtocolForm) abstractForm;
        	
        	userCollection.clear();
        	long [] coordinatorsArr = cpForm.getProtocolCoordinatorIds();
        	if(coordinatorsArr!=null)
        	{
	        	for (int i = 0; i < coordinatorsArr.length; i++)
				{
	        		if(coordinatorsArr[i]!=-1)
	        		{
		        		User coordinator = new User();
		        		coordinator.setId(new Long(coordinatorsArr[i]));
		        		userCollection.add(coordinator);
	        		}
				}
        	}
        	aliqoutInSameContainer = new Boolean(cpForm.isAliqoutInSameContainer());
	        Map map = cpForm.getValues();
	        Logger.out.debug("PRE FIX MAP "+map);
	        //map = fixMap(map);
	        Logger.out.debug("POST FIX MAP "+map);
	        
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
    
    /**
     * Returns message label to display on success add or edit
     * @return String
     */
	public String getMessageLabel() {		
		return this.title;
	}
	
	public boolean equals(Object object)
    {
		
    	
    	if(this.getClass().getName().equals(object.getClass().getName()))
    	{
    		CollectionProtocol collectionProtocol = (CollectionProtocol)object;
    		if(this.getId().longValue() == collectionProtocol.getId().longValue())
    			return true;
    	}
    	return false;
    }
	public int compareTo(Object object)
	{
		
    	if(this.getClass().getName().equals(object.getClass().getName()))
    	{
    		CollectionProtocol collectionProtocol = (CollectionProtocol)object;
    		return this.getId().compareTo(collectionProtocol.getId());
    	}
    	return 0;
	}
	public int hashCode()
	{
		if(this.getId() != null)
			return this.getId().hashCode();
		return super.hashCode();
	}

	/**
	 * @return Returns the aliqoutInSameContainer.
	 * @hibernate.property name="aliqoutInSameContainer" type="boolean" column="ALIQUOT_IN_SAME_CONTAINER"
	 */
	public Boolean getAliqoutInSameContainer() {
		return aliqoutInSameContainer;
	}

	/**
	 * @param aliqoutInSameContainer The aliqoutInSameContainer to set.
	 */
	public void setAliqoutInSameContainer(Boolean aliqoutInSameContainer) {
		this.aliqoutInSameContainer = aliqoutInSameContainer;
	}
}