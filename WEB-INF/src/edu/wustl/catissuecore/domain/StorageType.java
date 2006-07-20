/**
 * <p>Title: StorageType Class
 * <p>Description:  Type of the storage container e.g. Freezer, Box etc. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;

/**
 * Type of the storage container e.g. Freezer, Box etc.
 * @hibernate.class table="CATISSUE_STORAGE_TYPE"
 */
public class StorageType extends AbstractDomainObject implements Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
     * System generated unique systemIdentifier.
     */
	protected Long systemIdentifier;
	
	/**
     * Text name assigned to the container type
     */
	protected String type;
	
	/**
     * Default temperature of current type of storage container.
     */
	protected Double defaultTempratureInCentigrade;
	
	/**
     * Human understandable name assigned to dimension one.
     */
	protected String oneDimensionLabel;
	
	/**
     * Human understandable name assigned to dimension two.
     */
	protected String twoDimensionLabel;

	/**
	 * Defines whether this Storage type record can be queried (ACTIVE) or not queried (INACTIVE) by any actor.
	 */
	protected String activityStatus;
	
	/**
     * Default capacity of a storage container.
     */
	private StorageContainerCapacity defaultStorageCapacity = new StorageContainerCapacity();
	
	/**
	 * Collection of storage types that Storage Type Can Hold
	 */
	private Collection storageTypeCollection=new HashSet();
	
	/**
	 * Collection of Specimen class types that Storage Type can hold
	 */
	private Collection specimenClassCollection=new HashSet();
	
	/**
	 * Collection of Storage containers that storage type can hold
	 */
	private Collection storageContainerCollection=new HashSet();
	//DO Not delete required by hibernate
	public StorageType()
	{
		
	}
	
	public StorageType(AbstractActionForm abstractForm)
	{
		setAllValues(abstractForm);
	}
	
	/**
     * Returns System generated unique systemIdentifier.
     * @return System generated unique systemIdentifier.
     * @see #setSystemIdentifier(Integer)
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_STORAGE_TYPE_SEQ" 
     * */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
     * Sets system generated unique systemIdentifier.
     * @param systemIdentifier systemIdentifier of the Storagetype to be set.
     * @see #getSystemIdentifier()
     * */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	/**
     * Returns the type of storage container.
     * @hibernate.property name="type" type="string" 
     * column="TYPE" length="50" not-null="true" unique="true"
     * @return The type of storage container.
     * @see #setType(String)
     */
	public String getType()
	{
		return type;
	}

	/**
     * Sets the type of storage container.
     * @param type The type of storage container to be set.
     * @see #getType()
     */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
     * Returns the default temperature of a storage container in centigrade.
     * @hibernate.property name="defaultTempratureInCentigrade" type="double" 
     * column="DEFAULT_TEMP_IN_CENTIGRADE" length="30"
     * @return The default temperature of a storage container in centigrade.
     * @see #setDefaultTempratureInCentigrade(Double)
     */
	public Double getDefaultTempratureInCentigrade()
	{
		return defaultTempratureInCentigrade;
	}

	/**
     * Sets the default temperature of a storage container in centigrade.
     * @param defaultTempratureInCentigrade temperature of a storage container in centigrade to be set.
     * @see #getDefaultTempratureInCentigrade()
     */
	public void setDefaultTempratureInCentigrade(Double defaultTempratureInCentigrade)
	{
		this.defaultTempratureInCentigrade = defaultTempratureInCentigrade;
	}
	
	/**
     * Returns human understandable name assigned to dimension one.
     * @return Human understandable name assigned to dimension one.
     * @see #setOneDimensionLabel(String)
     * @hibernate.property name="oneDimensionLabel" type="string" 
     * column="ONE_DIMENSION_LABEL" length="50"
     */
	public String getOneDimensionLabel()
	{
		return oneDimensionLabel;
	}

	/**
     * Sets human understandable name assigned to dimension one.
     * @param oneDimensionLabel human understandable name assigned to dimension one.
     * @see #getOneDimensionLabel()
     */
	public void setOneDimensionLabel(String oneDimensionLabel)
	{
		this.oneDimensionLabel = oneDimensionLabel;
	}

	/**
     * Returns human understandable name assigned to dimension two.
     * @return Human understandable name assigned to dimension two.
     * @see #setTwoDimensionLabel(String)
     * @hibernate.property name="twoDimensionLabel" type="string" 
     * column="TWO_DIMENSION_LABEL" length="50"
     */
	public String getTwoDimensionLabel()
	{
		return twoDimensionLabel;
	}

	/**
     * Sets human understandable name assigned to dimension two.
     * @param oneDimensionLabel human understandable name assigned to dimension two.
     * @see #getTwoDimensionLabel()
     */
	public void setTwoDimensionLabel(String twoDimensionLabel)
	{
		this.twoDimensionLabel = twoDimensionLabel;
	}
	
	/**
     * Returns the activity status of the storage type. 
     * @return The activity status of storage type.
     * @see #setActivityStatus(String)
     * @hibernate.property name="activityStatus" type="string"  
     * column="ACTIVITY_STATUS" length="30" default="Active"
     */
	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
     * Sets the activity status.
     * @param activityStatus the activity status of the storagetype to be set.
     * @see #getActivityStatus()
     */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}


	/**
     * Returns the collection of storage types associated with type 
     * @hibernate.set name="storageTypeCollection" table="CATISSUE_STORAGETYPE_HOLDS_REL"
     * cascade="none" inverse="false" lazy="false"
     * @hibernate.collection-key column="STORAGE_TYPE_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.StorageType" column="STORAGE_TYPE_HOLD_ID"
     * @return the collection of storage types this type can hold 
     * @see #setStorageTypeCollection(Set)
     */
    public Collection getStorageTypeCollection()
    {
        return storageTypeCollection;
    }

    /**
     * Sets the collection of storage types of a Storage Type 
     * @param storageTypeCollection the collection of storage types of a Storage Type 
     * @see #getStorageTypeCollection
     */
    public void setStorageTypeCollection(Collection storageTypeCollection)
    {
        this.storageTypeCollection = storageTypeCollection;
    }
    


    /**
     * Returns the collection of Specimen Class Types associated with Storage Type 
     * @hibernate.set name="specimenClassTypeCollection" table="CATISSUE_TYPE_SPECIMENCL_REL"
     * cascade="none" lazy="false"
     * @hibernate.collection-key column="STORAGE_TYPE_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.SpecimenClass" column="SPECIMEN_CLASS_ID"
     * @return the collection of Specimen Class Types of a Storage Type 
     * @see #setSpecimenClassCollection(Set)
     */
    public Collection getSpecimenClassCollection()
    {
        return specimenClassCollection;
    }

    /**
     * Sets the collection of specimen class types of a Storage Type 
     * @param specimenClasseCollection the collection of specimenClassTypes of a Storage Type 
     * @see #getSpecimenClassCollection
     */
    public void setSpecimenClassCollection(Collection specimenClassCollection)
    {
        this.specimenClassCollection = specimenClassCollection;
    }

    /**
     * Returns the collection of Storage Containers associated with Storage Type 
     * @hibernate.set name="storageContainerCollection" table="CATISSUE_CONTAINER_TYPE_REL"
     * cascade="none" lazy="false"
     * @hibernate.collection-key column="STORAGE_TYPE_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.StorageContainer" column="STORAGE_CONTAINER_ID"
     * @return the collection of StorageContainer of a Storage Type 
     * @see #setStorageContainer(Set)
     */
    public Collection getStorageContainerCollection()
    {
        return storageContainerCollection;
    }

    /**
     * Sets the collection of storage container of a Storage Type 
     * @param storageContainerCollection the collection of storage container of a Storage Type 
     * @see #getStorageContainerCollection
     */
    public void setStorageContainerCollection(Collection storageContainerCollection)
    {
        this.storageContainerCollection = storageContainerCollection;
    }

    

	/**
	 * Returns the default capacity of a storage container.
	 * @return The default capacity of a storage container.
	 * @hibernate.many-to-one column="STORAGE_CONTAINER_CAPACITY_ID"
	 * class="edu.wustl.catissuecore.domain.StorageContainerCapacity" constrained="true"
	 * @see #setDefaultStorageCapacity(StorageContainerCapacity)
	 */
	public StorageContainerCapacity getDefaultStorageCapacity()
	{
		return defaultStorageCapacity;
	}

	/**
     * Sets the default storage capacity.
     * @param defaultStorageCapacity capacity  of a storage container to be set.
     * @see #getDefaultStorageCapacity
     * ()
     */
	public void setDefaultStorageCapacity(StorageContainerCapacity defaultStorageCapacity)
	{
		this.defaultStorageCapacity = defaultStorageCapacity;
	}
	
	/**
	 * This function Copies the data from a StorageTypeForm object to a StorageType object.
	 * @param storageTypeForm A StorageTypeForm object containing the information about the StorageType.  
	 * */
	public void setAllValues(AbstractActionForm abstractForm)
	{
	    try
	    {
	        StorageTypeForm storageTypeForm = (StorageTypeForm) abstractForm;
	        
	        this.systemIdentifier = new Long(storageTypeForm.getSystemIdentifier());
	        this.type = storageTypeForm.getType().trim() ;
	        
	        if(storageTypeForm.getDefaultTemperature()!=null && storageTypeForm.getDefaultTemperature().trim().length() >0 )
	        	this.defaultTempratureInCentigrade = new Double(storageTypeForm.getDefaultTemperature());
	        
	        this.oneDimensionLabel = storageTypeForm.getOneDimensionLabel();
	        this.twoDimensionLabel = storageTypeForm.getTwoDimensionLabel();

	        defaultStorageCapacity.setOneDimensionCapacity(new Integer(storageTypeForm.getOneDimensionCapacity()));
	        defaultStorageCapacity.setTwoDimensionCapacity(new Integer(storageTypeForm.getTwoDimensionCapacity()));
	        
	        
	        storageTypeCollection.clear();
        	long [] storageTypeArr = storageTypeForm.getHoldsStorageTypeIds();
        	if(storageTypeArr!=null)
        	{
	        	for (int i = 0; i < storageTypeArr.length; i++)
				{
	        		Logger.out.debug("type Id :"+storageTypeArr[i]);
	        		if(storageTypeArr[i]!=-1)
	        		{
		        		StorageType storageType = new StorageType();
		        		storageType.setSystemIdentifier(new Long(storageTypeArr[i]));
		        		storageTypeCollection.add(storageType);
	        		}
				}
        	}

        	
        	specimenClassCollection.clear();
        	long [] specimenClassTypeArr = storageTypeForm.getHoldsSpecimenClassTypeIds();
        	if(specimenClassTypeArr!=null)
        	{
	        	for (int i = 0; i < specimenClassTypeArr.length; i++)
				{
	        		Logger.out.debug("type Id :"+specimenClassTypeArr[i]);
	        		if(specimenClassTypeArr[i]!=-1)
	        		{
		        		SpecimenClass specimenClass = new SpecimenClass();
		        		specimenClass.setSystemIdentifier(new Long(specimenClassTypeArr[i]));
		        		specimenClassCollection.add(specimenClass);
	        		}
				}
        	}

        	this.activityStatus="Active";
	    }
	    catch(Exception excp)
	    {
	    	Logger.out.error(excp.getMessage());
	    }
	}
}