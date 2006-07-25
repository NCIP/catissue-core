/**
 * <p>Title: StorageContainer Class>
 * <p>Description:  A class that models storage container's information. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */
package edu.wustl.catissuecore.domain;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.actionForm.SimilarContainersForm;
import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;

/**
 * A class that models storage container's information.
 * @hibernate.class table="CATISSUE_STORAGE_CONTAINER"
 * @author Aniruddha Phadnis
 */
public class StorageContainer extends AbstractDomainObject implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
     * System generated unique systemIdentifier.
     */
	protected Long systemIdentifier;

	
	/** 
	 * Name assigned to storage container
	 * 
	 */
	protected String name;
	/**
     * Intended temperature of the storage container.
     */
	protected Double tempratureInCentigrade;
	
	/**
	 * Is no space available in the container to store more specimens?
	 */
	protected Boolean isFull;
	
	/**
	 * Defines whether this Storage Container record can be queried (ACTIVE) or not queried (INACTIVE) by any actor.
	 */
	protected String activityStatus;
	
	/**
	 * Barcode assigned to container.
	 */
	protected String barcode;
	
	/**
	 * Type of the storage container e.g. Freezer, Box etc.
	 */
	protected StorageType storageType;
	
	/**
	 * A physical location of storage container
	 */
	protected Site site;
	
	/**
	 * Parent container of this container.
	 */
	protected StorageContainer parentContainer = null;
	
	
	/**
	 * A capacity of storage container
	 */
	protected StorageContainerCapacity storageContainerCapacity = new StorageContainerCapacity();
	
	/**
	 * A collection of sub containers under this container
	 */
	protected Collection childrenContainerCollection = new HashSet();
	
	protected Integer positionDimensionOne;

	protected Integer positionDimensionTwo;
	
	/**
     * Collection of Collection Protocols that associated with Storage Container.
     */
	protected Collection collectionProtocolCollection=new HashSet();
	
	/** Collection of Storage Types that associated with Storage Container
	 * 
	 */
	protected Collection storageTypeCollection=new HashSet();
	/** Collection of specimen class that associated with Storage Container
	 * 
	 */
	protected Collection specimenClassCollection=new HashSet();
	
	/** Collection of holds Group that associated with Storage Container
	 * 
	 */
	//protected Collection holdsGroupCollection=new HashSet();
	
	/**
	 * Number of containers
	 */
	protected transient Integer startNo;
	/**
	 * Number of containers
	 */
	protected transient Integer noOfContainers;
	
	protected transient Map similarContainerMap;

	protected transient boolean isParentChanged = false;

	//Default Constructor
	public StorageContainer()
	{
	}
	
	public StorageContainer(AbstractActionForm form)
	{
		setAllValues(form);
	}
	
	public StorageContainer(StorageContainer oldContainer)
	{
		this.setSystemIdentifier(oldContainer.getSystemIdentifier());
		this.setActivityStatus(oldContainer.getActivityStatus());
		this.setParentContainer(oldContainer.getParentContainer());
		//this.setNumber(oldContainer.getNumber());
		this.setName(oldContainer.getName());
		this.setIsFull(oldContainer.getIsFull()   );
		if(parentContainer!=null)
		{
			parentContainer.getChildrenContainerCollection().add(this);
		}
		this.setSite(oldContainer.getSite());		
		storageContainerCapacity = new StorageContainerCapacity(oldContainer.getStorageContainerCapacity());
		
		this.setStorageType(oldContainer.getStorageType());
		this.setTempratureInCentigrade(oldContainer.getTempratureInCentigrade());
		this.setCollectionProtocolCollection(oldContainer.getCollectionProtocolCollection());
		this.setStorageTypeCollection(oldContainer.getStorageTypeCollection());
		this.setSpecimenClassCollection(oldContainer.getSpecimenClassCollection());
		
	}
	
	/**
     * Returns System generated unique systemIdentifier.
     * @return System generated unique systemIdentifier.
     * @see #setSystemIdentifier(Long)
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" unique="true" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_STORAGE_CONTAINER_SEQ"
     */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
     * Sets unique systemIdentifier.
     * @param systemIdentifier Identifier to be set.
     * @see #getSystemIdentifier()
     */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	/**
     * Returns the Name of the torage container. 
     * @return name of the storage container.
     * @see #setName(String)
     * @hibernate.property name="name" type="string" unique="true"
     * column="CONTAINER_NAME" length="50"
     */

	public String getName()
	{
		return name;
	}
	/**
     * Sets the name of the storage container.
     * @param name of the storage container to be set.
     * @see #getName()
     */
	public void setName(String name)
	{
		this.name=name;
	}
	/**
     * Returns the temperature of the storage container. 
     * @return Temperature of the storage container.
     * @see #setTempratureInCentigrade(Double)
     * @hibernate.property name="tempratureInCentigrade" type="double" 
     * column="TEMPERATURE" length="30"
     */
	public Double getTempratureInCentigrade()
	{
		return tempratureInCentigrade;
	}

	/**
     * Sets the temperature of the storage container.
     * @param tempratureInCentigrade temperature of the storage container to be set.
     * @see #getTempratureInCentigrade()
     */
	public void setTempratureInCentigrade(Double tempratureInCentigrade)
	{
		this.tempratureInCentigrade = tempratureInCentigrade;
	}

	/**
     * Tells whether the space is available in the storage container or not. 
     * @return Availabilty of space in the storage container.
     * @see #setIsFull(Boolean)
     * @hibernate.property name="isFull" type="boolean" 
     * column="IS_CONTAINER_FULL"
     */
	public Boolean getIsFull()
	{
		return isFull;
	}

	/**
     * Sets the availability of the storage container.
     * @param isFull availability of the storage container.
     * @see #getIsFull()
     */
	public void setIsFull(Boolean isFull)
	{
		this.isFull = isFull;
	}

	/**
     * Returns the barcode of the storage container. 
     * @return The barcode of storage container.
     * @see #setBarcode(Boolean)
     * @hibernate.property name="barcode" type="string" 
     * column="BARCODE" length="50" unique="true"
     */
	public String getBarcode()
	{
		return barcode;
	}

	/**
     * Sets the barcode of the storage container.
     * @param barcode the barcode of the storage container.
     * @see #getBarcode()
     */
	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
	}

	/**
     * Returns the activity status of the storage container. 
     * @return The activity status of storage container.
     * @see #setActivityStatus(String)
     * @hibernate.property name="activityStatus" type="string" 
     * column="ACTIVITY_STATUS" length="30"
     */
	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
     * Sets the activity status.
     * @param activityStatus the activity status of the storagecontainer to be set.
     * @see #getActivityStatus()
     */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
     * Returns the storage type of the container.
     * @hibernate.many-to-one column="STORAGE_TYPE_ID" 
     * class="edu.wustl.catissuecore.domain.StorageType" constrained="true"
     * @return The storage type of the container.
     * @see #setStorageType(StorageType)
     */
	public StorageType getStorageType()
	{
		return storageType;
	}

	/**
     * Sets the storage type of the container.
     * @param storageType the storage type of the container to be set.
     * @see #getStorageType()
     */
	public void setStorageType(StorageType storageType)
	{
		this.storageType = storageType;
	}

	/**
     * Returns the physical location of the storage container.
     * @hibernate.many-to-one column="SITE_ID" 
     * class="edu.wustl.catissuecore.domain.Site" constrained="true"
     * @return Site the physical location of the storage container.
     * @see #setSite(Site)
     */
	public Site getSite()
	{
		return site;
	}

	/**
     * Sets the physical location of the container.
     * @param site the physical location of the container.
     * @see #getSite()
     */
	public void setSite(Site site)
	{
		this.site = site;
	}

	/**
     * Returns the parent container of this storage container.
     * @hibernate.many-to-one column="PARENT_CONTAINER_ID" 
     * class="edu.wustl.catissuecore.domain.StorageContainer" constrained="true"
     * @return The parent container of this storage container.
     * @see #setParentContainer(StorageContainer)
     */
	public StorageContainer getParentContainer()
	{
		return parentContainer;
	}

	/**
     * Sets the parent container of this storage container.
     * @param parentContainer the parent container of this storage container.
     * @see #getSite()
     */
	public void setParentContainer(StorageContainer parentContainer)
	{
		this.parentContainer = parentContainer;
	}

	/**
     * Returns the collection of collectionProtocols associated with container 
     * @hibernate.set name="collectionProtocolCollection" table="CATISSUE_CONTAINER_CP_REL"
     * cascade="none" inverse="false" lazy="false"
     * @hibernate.collection-key column="STORAGE_CONTAINER_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.CollectionProtocol" column="COLLECTION_PROTOCOL_ID"
     * @return the collection of collectionProtocols of a Storage Container 
     * @see #setCollectionProtocolCollection(Set)
     */
    public Collection getCollectionProtocolCollection()
    {
        return collectionProtocolCollection;
    }

    /**
     * Sets the collection of collectionProtocol of a Storage Container 
     * @param collectionProtocolCollection the collection of collectionProtocols of a Storage Container 
     * @see #getCollectionProtocolCollection
     */
    public void setCollectionProtocolCollection(Collection collectionProtocolCollection)
    {
        this.collectionProtocolCollection = collectionProtocolCollection;
    }
    
    /**
     * Returns the collection of Storage Types associated with container 
     * @hibernate.set name="storageTypeCollection" table="CATISSUE_CONTAINER_TYPE_REL"
     * cascade="none" lazy="false"
     * @hibernate.collection-key column="STORAGE_CONTAINER_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.StorageType" column="STORAGE_TYPE_ID"
     * @return the collection of storage types of a Storage Container 
     * @see #setStorageTypeCollection(Set)
     */
    public Collection getStorageTypeCollection()
    {
        return storageTypeCollection;
    }

    /**
     * Sets the collection of storage type of a Storage Container 
     * @param storageTypeCollection the collection of storage types of a Storage Container 
     * @see #getStorageTypeCollection
     */
    public void setStorageTypeCollection(Collection storageTypeCollection)
    {
        this.storageTypeCollection = storageTypeCollection;
    }

    
    /**
     * Returns the collection of Specimen Classes associated with container 
     * @hibernate.set name="specimenClassCollection" table="CATISSUE_CONT_SPECIMENCL_REL"
     * cascade="none" inverse="false" lazy="false"
     * @hibernate.collection-key column="STORAGE_CONTAINER_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.SpecimenClass" column="SPECIMEN_CLASS_ID"
     * @return the collection of specimen classes of a Storage Container 
     * @see #setSpecimenClassCollection(Set)
     */
    public Collection getSpecimenClassCollection()
    {
        return specimenClassCollection;
    }

    /**
     * Sets the collection of specimen class of a Storage Container 
     * @param specimenClassCollection the collection of specimen classes of a Storage Container 
     * @see #getSpecimenClassCollection
     */
    public void setSpecimenClassCollection(Collection specimenClassCollection)
    {
        this.specimenClassCollection = specimenClassCollection;
    }

	/**
     * Returns the capacity of this storage container.
     * @hibernate.many-to-one column="STORAGE_CONTAINER_CAPACITY_ID" 
     * class="edu.wustl.catissuecore.domain.StorageContainerCapacity" constrained="true" cascade="save-update" 
     * @return The capacity of this storage container.
     * @see #setStorageContainerCapacity(StorageContainerCapacity)
     */
	public StorageContainerCapacity getStorageContainerCapacity()
	{
		return storageContainerCapacity;
	}

	/**
     * Sets the capacity of this storage container.
     * @param storageContainerCapacity the capacity of storage container to be set.
     * @see #getStorageContainerCapacity()
     */
	public void setStorageContainerCapacity(StorageContainerCapacity storageContainerCapacity)
	{
		this.storageContainerCapacity = storageContainerCapacity;
	}
	
	
	/**
	 * Returns collection of sub containers under this.
	 * @return collection of sub containers under this.
	 * @hibernate.set name="childrenContainerCollection" table="CATISSUE_STORAGE_CONTAINER"
	 * cascade="save-update" 
	 * @hibernate.collection-key column="PARENT_CONTAINER_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.StorageContainer"
	 * @see setChildrenContainerCollection(Collection)
	 */
	public Collection getChildrenContainerCollection()
	{
		return childrenContainerCollection;
	}

	/**
	 * Sets the collection of sub containers under this.
	 * @param storageContainerDetailsCollection collection of sub containers under this.
	 * @see #getChildrenContainerCollection()
	 */
	public void setChildrenContainerCollection(Collection childrenContainerCollection)
	{
		this.childrenContainerCollection = childrenContainerCollection;
	}
	
	/**
     * Returns the reference to dimensional position one of the specimen in Storage Container.
     * @hibernate.property name="positionDimensionOne" type="int" column="POSITION_DIMENSION_ONE" length="30"  
     * @return the reference to dimensional position one of the specimen in Storage Container.
     * @see #setPositionDimensionOne(Integer)
     */
    public Integer getPositionDimensionOne()
    {
        return positionDimensionOne;
    }

    /**
     * Sets the reference to dimensional position one of the specimen in Storage Container.
     * @param positionDimensionOne the reference to dimensional position one of the specimen 
     * in Storage Container.
     * @see #getPositionDimensionOne()
     */
    public void setPositionDimensionOne(Integer positionDimensionOne)
    {
        this.positionDimensionOne = positionDimensionOne;
    }

    /**
     * Returns the reference to dimensional position two of the specimen in Storage Container.
     * @hibernate.property name="positionDimensionTwo" type="int" column="POSITION_DIMENSION_TWO" length="50"  
     * @return the reference to dimensional position two of the specimen in Storage Container.
     * @see #setPositionDimensionOne(Integer)
     */
    public Integer getPositionDimensionTwo()
    {
        return positionDimensionTwo;
    }

    /**
     * Sets the reference to dimensional position two of the specimen in Storage Container.
     * @param positionDimensionTwo the reference to dimensional position two of the specimen 
     * in Storage Container.
     * @see #getPositionDimensionTwo()
     */
    public void setPositionDimensionTwo(Integer positionDimensionTwo)
    {
        this.positionDimensionTwo = positionDimensionTwo;
    }
	
	/**
	 * @return Returns the noOfContainers.
	 */
	public Integer getNoOfContainers()
	{
		return noOfContainers;
	}
	/**
	 * @param noOfContainers The noOfContainers to set.
	 */
	public void setNoOfContainers(Integer noOfContainers)
	{
		this.noOfContainers = noOfContainers;
	}
	/**
	 * @return Returns the startNo.
	 */
	public Integer getStartNo()
	{
		return startNo;
	}
	/**
	 * @param startNo The startNo to set.
	 */
	public void setStartNo(Integer startNo)
	{
		this.startNo = startNo;
	}
	
	public boolean isParentChanged()
	{
		return isParentChanged;
	}
	public void setParentChanged(boolean isParentChanged)
	{
		this.isParentChanged = isParentChanged;
	}
	
	/**
	 * This function Copies the data from a StorageTypeForm object to a StorageType object.
	 * @param storageTypeForm A StorageTypeForm object containing the information about the StorageType.  
	 * */
	public void setAllValues(AbstractActionForm abstractForm)
	{
	    try
	    {
	      if(abstractForm instanceof StorageContainerForm)
	      {
	        StorageContainerForm form = (StorageContainerForm) abstractForm;
	        this.name					=form.getContainerName();
	        if (Utility.toString(form.getDefaultTemperature()).trim().length() > 0  )
        	{
	        	this.tempratureInCentigrade	= new Double(form.getDefaultTemperature());
        	}
	        if(!form.getBarcode().trim().equals(""))
	        	this.barcode			= form.getBarcode();
	        else
	        	this.barcode			= null;
	        
	        this.isFull					= new Boolean(form.getIsFull());
	        Logger.out.debug("SC Domain : "+ this.isFull + " :-: form.getIsFull() : " + form.getIsFull()  );
	        this.activityStatus			= form.getActivityStatus();	        
	        this.noOfContainers			= new Integer(form.getNoOfContainers());
	        
	        storageType = new StorageType();
	        storageType.setSystemIdentifier(new Long(form.getTypeId()));
	        storageType.setOneDimensionLabel(form.getOneDimensionLabel());
	        storageType.setTwoDimensionLabel(form.getTwoDimensionLabel());
	        
	        storageContainerCapacity.setOneDimensionCapacity(new Integer(form.getOneDimensionCapacity()));
	        storageContainerCapacity.setTwoDimensionCapacity(new Integer(form.getTwoDimensionCapacity()));
	        
	        //in case of edit
        	if(!form.isAddOperation())
        	{
        		//Previously Container was in a site
        		if(parentContainer==null)
        		{
        			//Put this in another container
        			if(form.getParentContainerId()>0)
        			{
        				isParentChanged = true;
        			}
        		}
        		else//in another container
        		{
        			if(parentContainer.getSystemIdentifier().longValue()!=form.getParentContainerId())
        			{
        				isParentChanged = true;
        			}
        			// code to check if the position of the container is changed
        			else
        			{
        				if(positionDimensionOne.intValue() != form.getPositionDimensionOne() ||
        						positionDimensionTwo.intValue() != form.getPositionDimensionOne()	)
        				{
        					positionChanged = true; 
        				}
        			}
        		}
        	}
	        
        	Logger.out.debug("isParentChanged "+isParentChanged);
	        if(form.getCheckedButton() == Constants.CONTAINER_IN_ANOTHER_CONTAINER)
			{
        		parentContainer = new StorageContainer();
				parentContainer.setSystemIdentifier(new Long(form.getParentContainerId()));
        		this.setPositionDimensionOne(new Integer(form.getPositionDimensionOne()));
        		this.setPositionDimensionTwo(new Integer(form.getPositionDimensionTwo()));
			}
	        else
	        {
	        	parentContainer = null;
	        	this.setPositionDimensionOne(null);
	        	this.setPositionDimensionTwo(null);
		        site = new Site();
		        site.setSystemIdentifier(new Long(form.getSiteId()));
	        }
	        
	        
	        collectionProtocolCollection.clear();
        	long [] collectionProtocolArr = form.getCollectionIds();
        	if(collectionProtocolArr!=null)
        	{
	        	for (int i = 0; i < collectionProtocolArr.length; i++)
				{
	        		Logger.out.debug("Collection prtocoo Id :"+collectionProtocolArr[i]);
	        		if(collectionProtocolArr[i]!=-1)
	        		{
		        		CollectionProtocol collectionProtocol = new CollectionProtocol();
		        		collectionProtocol.setSystemIdentifier(new Long(collectionProtocolArr[i]));
		        		collectionProtocolCollection.add(collectionProtocol);
	        		}
				}
        	}
        	storageTypeCollection.clear();
        	long [] storageTypeArr = form.getHoldsStorageTypeIds();
        	if(storageTypeArr!=null)
        	{
	        	for (int i = 0; i < storageTypeArr.length; i++)
				{
	        		Logger.out.debug("Storage Type Id :"+storageTypeArr[i]);
	        		if(storageTypeArr[i]!=-1)
	        		{
		        		StorageType storageType = new StorageType();
		        		storageType.setSystemIdentifier(new Long(storageTypeArr[i]));
		        		storageTypeCollection.add(storageType);
	        		}
				}
        	}
	                
        	specimenClassCollection.clear();
        	long [] specimenClassArr = form.getHoldsSpecimenClassTypeIds();
        	if(specimenClassArr!=null)
        	{
	        	for (int i = 0; i < specimenClassArr.length; i++)
				{
	        		Logger.out.debug("Specimen class Id :"+specimenClassArr[i]);
	        		if(specimenClassArr[i]!=-1)
	        		{
		        		SpecimenClass specimenClass = new SpecimenClass();
		        		specimenClass.setSystemIdentifier(new Long(specimenClassArr[i]));
		        		specimenClassCollection.add(specimenClass);
	        		}
				}
        	}
	      }else if(abstractForm instanceof SimilarContainersForm)
	      {
	    	SimilarContainersForm form = (SimilarContainersForm) abstractForm;
	    	this.similarContainerMap = 		form.getSimilarContainersMap();
	    	this.isFull					= new Boolean(form.getIsFull());
	    	this.activityStatus			= form.getActivityStatus();
	    	this.noOfContainers			= new Integer(form.getNoOfContainers());
	    	
	    	if (Utility.toString(form.getDefaultTemperature()).trim().length() > 0  )
        	{
	        	this.tempratureInCentigrade	= new Double(form.getDefaultTemperature());
        	}
	    	
	    	//System.out.println("form.getTypeId() -->>&& "+form.getTypeId());
	    		    	
	    	storageType = new StorageType();
	        storageType.setSystemIdentifier(new Long(form.getTypeId()));
	        storageType.setOneDimensionLabel(form.getOneDimensionLabel());
	        storageType.setTwoDimensionLabel(form.getTwoDimensionLabel());
	        
	        storageContainerCapacity.setOneDimensionCapacity(new Integer(form.getOneDimensionCapacity()));
	        storageContainerCapacity.setTwoDimensionCapacity(new Integer(form.getTwoDimensionCapacity()));
	    	
	        collectionProtocolCollection.clear();
        	long [] collectionProtocolArr = form.getCollectionIds();
        	if(collectionProtocolArr!=null)
        	{
	        	for (int i = 0; i < collectionProtocolArr.length; i++)
				{
	        		Logger.out.debug("Collection prtocoo Id :"+collectionProtocolArr[i]);
	        		if(collectionProtocolArr[i]!=-1)
	        		{
		        		CollectionProtocol collectionProtocol = new CollectionProtocol();
		        		collectionProtocol.setSystemIdentifier(new Long(collectionProtocolArr[i]));
		        		collectionProtocolCollection.add(collectionProtocol);
	        		}
				}
        	}
        	storageTypeCollection.clear();
        	long [] storageTypeArr = form.getHoldsStorageTypeIds();
        	if(storageTypeArr!=null)
        	{
	        	for (int i = 0; i < storageTypeArr.length; i++)
				{
	        		Logger.out.debug("Storage Type Id :"+storageTypeArr[i]);
	        		if(storageTypeArr[i]!=-1)
	        		{
		        		StorageType storageType = new StorageType();
		        		storageType.setSystemIdentifier(new Long(storageTypeArr[i]));
		        		storageTypeCollection.add(storageType);
	        		}
				}
        	}
	                
        	specimenClassCollection.clear();
        	long [] specimenClassArr = form.getHoldsSpecimenClassTypeIds();
        	if(specimenClassArr!=null)
        	{
	        	for (int i = 0; i < specimenClassArr.length; i++)
				{
	        		Logger.out.debug("Specimen class Id :"+specimenClassArr[i]);
		        		SpecimenClass specimenClass = new SpecimenClass();
		        		specimenClass.setSystemIdentifier(new Long(specimenClassArr[i]));
		        		specimenClassCollection.add(specimenClass);
				}
        	}	        
	        
	      }	        
	    }
	    catch(Exception excp)
	    {
	        Logger.out.error(excp.getMessage(),excp);
	    }
	}
	
	
	// -------- To check for changed position in the same container.
	private boolean positionChanged = false;
	
	/**
	 * @return Returns the positionChanged.
	 */
	public boolean isPositionChanged() {
		return positionChanged;
	}
	/**
	 * @param positionChanged The positionChanged to set.
	 */
	public void setPositionChanged(boolean positionChanged) {
		this.positionChanged = positionChanged;
	}

	/**
	 * @return Returns the similarContainerMap.
	 */
	public Map getSimilarContainerMap() {
		return similarContainerMap;
	}

	/**
	 * @param similarContainerMap The similarContainerMap to set.
	 */
	public void setSimilarContainerMap(Map similarContainerMap) {
		this.similarContainerMap = similarContainerMap;
	}
}