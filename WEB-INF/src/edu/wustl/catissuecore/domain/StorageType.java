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

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author gautam_shetty
 * @hibernate.joined-subclass table="CATISSUE_STORAGE_TYPE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class StorageType extends ContainerType
{

    protected Double defaultTempratureInCentigrade;

    protected Collection holdsStorageTypeCollection = new HashSet();

    protected Collection holdsSpecimenClassCollection = new HashSet();
    
    protected String activityStatus;

    public StorageType()
    {
    }
    
    public StorageType(AbstractActionForm abstractActionForm) throws AssignDataException
    {
        setAllValues(abstractActionForm);
    }

    /**
     * @return Returns the defaultTempratureInCentigrade.
     * @hibernate.property name="defaultTempratureInCentigrade" type="double" 
     * column="DEFAULT_TEMPERATURE" length="30"
     */
    public Double getDefaultTempratureInCentigrade()
    {
        return defaultTempratureInCentigrade;
    }

    /**
     * @param defaultTempratureInCentigrade The defaultTempratureInCentigrade to set.
     */
    public void setDefaultTempratureInCentigrade(
            Double defaultTempratureInCentigrade)
    {
        this.defaultTempratureInCentigrade = defaultTempratureInCentigrade;
    }

    /**
     * @return Returns the holdsSpecimenClassCollection.
     * @hibernate.set name="holdsSpecimenClassCollection" table="CATISSUE_STOR_TYPE_SPEC_CLASS"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="STORAGE_TYPE_ID"
	 * @hibernate.element type="string" column="SPECIMEN_CLASS" length="30"
     */
    public Collection getHoldsSpecimenClassCollection()
    {
        return holdsSpecimenClassCollection;
    }

    /**
     * @param holdsSpecimenClassCollection The holdsSpecimenClassCollection to set.
     */
    public void setHoldsSpecimenClassCollection(Collection holdsSpecimenClassCollection)
    {
        this.holdsSpecimenClassCollection = holdsSpecimenClassCollection;
    }
    
    /**
     * @return Returns the activityStatus.
     * @hibernate.property name="activityStatus" type="string" column="ACTIVITY_STATUS" length="30"
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
     * @return Returns the holdsStorageTypeCollection.
     * Returns the collection of storage types associated with type 
     * @hibernate.set name="holdsStorageTypeCollection" table="CATISSUE_STOR_TYPE_HOLDS_TYPE"
     * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="STORAGE_TYPE_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.StorageType" column="HOLDS_STORAGE_TYPE_ID"
     */
    public Collection getHoldsStorageTypeCollection()
    {
        return holdsStorageTypeCollection;
    }

    /**
     * @param holdsStorageTypeCollection The holdsStorageTypeCollection to set.
     */
    public void setHoldsStorageTypeCollection(Collection holdsStorageTypeCollection)
    {
        this.holdsStorageTypeCollection = holdsStorageTypeCollection;
    }
    
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.ContainerType#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
            throws AssignDataException
    {
        try
	    {
	        StorageTypeForm storageTypeForm = (StorageTypeForm) abstractForm;
	        
	        this.id = new Long(storageTypeForm.getId());
	        this.name = storageTypeForm.getType().trim() ;
	        
	        if(storageTypeForm.getDefaultTemperature()!=null && storageTypeForm.getDefaultTemperature().trim().length() >0 )
	        	this.defaultTempratureInCentigrade = new Double(storageTypeForm.getDefaultTemperature());
	        
	        this.oneDimensionLabel = storageTypeForm.getOneDimensionLabel();
	        this.twoDimensionLabel = storageTypeForm.getTwoDimensionLabel();

	        capacity.setOneDimensionCapacity(new Integer(storageTypeForm.getOneDimensionCapacity()));
	        capacity.setTwoDimensionCapacity(new Integer(storageTypeForm.getTwoDimensionCapacity()));
	        
	        
	        holdsStorageTypeCollection.clear();
        	long [] storageTypeArr = storageTypeForm.getHoldsStorageTypeIds();
        	if(storageTypeArr!=null)
        	{
	        	for (int i = 0; i < storageTypeArr.length; i++)
				{
	        		Logger.out.debug("type Id :"+storageTypeArr[i]);
	        		if(storageTypeArr[i]!=-1)
	        		{
		        		StorageType storageType = new StorageType();
		        		storageType.setId(new Long(storageTypeArr[i]));
		        		holdsStorageTypeCollection.add(storageType);
	        		}
				}
        	}

        	
        	holdsSpecimenClassCollection.clear();
        	String [] specimenClassTypeArr = storageTypeForm.getHoldsSpecimenClassTypes();
        	if(specimenClassTypeArr!=null)
        	{
        		
	        	for (int i = 0; i < specimenClassTypeArr.length; i++)
				{
	        		Logger.out.debug("type Id :"+specimenClassTypeArr[i]);
	        		if(specimenClassTypeArr[i].equals("-1"))
	        		{
	        			holdsSpecimenClassCollection.addAll(Utility.getSpecimenClassTypes());
	        			break;
	        		}
	        		else
	        		{
	        			holdsSpecimenClassCollection.add(specimenClassTypeArr[i]);
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