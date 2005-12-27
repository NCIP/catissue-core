/**
 * <p>Title: Specimen Class>
 * <p>Description:  A single unit of tissue, body fluid, or derivative 
 * biological macromolecule that is collected or created from a Participant </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * A single unit of tissue, body fluid, or derivative biological macromolecule 
 * that is collected or created from a Participant
 * @hibernate.class table="CATISSUE_SPECIMEN"
 * @hibernate.discriminator column="SPECIMEN_CLASS" 
 */
public class Specimen extends AbstractDomainObject implements Serializable
{
    private static final long serialVersionUID = 1234567890L;

    /**
     * System generated unique systemIdentifier.
     */
    protected Long systemIdentifier;

    /**
     * Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
     */
    protected String type;

    /**
     * Is this specimen still physically available in the tissue bank?
     */
    protected Boolean available;

    /**
     * Reference to dimensional position one of the specimen in Storage Container.
     */
    protected Integer positionDimensionOne;

    /**
     * Reference to dimensional position two of the specimen in Storage Container.
     */
    protected Integer positionDimensionTwo;

    /**
     * Barcode assigned to the specimen.
     */
    protected String barcode;

    /**
     * Comments on specimen.
     */
    protected String comments;

    /**
     * Defines whether this Specimen record can be queried (Active) 
     * or not queried (Inactive) by any actor.
     */
    protected String activityStatus;

    /**
     * Parent specimen from which this specimen is derived. 
     */
    protected Specimen parentSpecimen;

    /**
     * Collection of attributes of a Specimen that renders it potentially harmful to a User.
     */
    protected Collection biohazardCollection = new HashSet();

    /**
     * A physically discreet container that is used to store a specimen  e.g. Box, Freezer etc.
     */
    protected StorageContainer storageContainer = new StorageContainer();

    /**
     * Collection of Specimen Event Parameters associated with this specimen. 
     */
    protected Collection specimenEventCollection = new HashSet();

    /**
     * Collection of children specimens derived from this specimen. 
     */
    protected Collection childrenSpecimen = new HashSet();

    /**
     * Collection of a pre-existing, externally defined systemIdentifier associated with a specimen.
     */
    protected Collection externalIdentifierCollection = new HashSet();

    /**
     * An event that results in the collection of one or more specimen from a participant.
     */
    protected SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();

    /**
     * The combined anatomic state and pathological disease classification of a specimen.
     */
    protected SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();

    protected transient boolean isParentChanged = false;
    public Specimen()
    {    	
    }
    
    //Constructor
    public Specimen(AbstractActionForm form)
    {
    	setAllValues(form);
    }
    
    /**
     * Returns the system generated unique systemIdentifier.
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30" 
     * unsaved-value="null" generator-class="native"
     * @return the system generated unique systemIdentifier.
     * @see #setSystemIdentifier(Long)
     * */
    public Long getSystemIdentifier()
    {
        return systemIdentifier;
    }

    /**
     * Sets the system generated unique systemIdentifier.
     * @param systemIdentifier the system generated unique systemIdentifier.
     * @see #getSystemIdentifier()
     * */
    public void setSystemIdentifier(Long systemIdentifier)
    {
        this.systemIdentifier = systemIdentifier;
    }

    /**
     * Returns the type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
     * @hibernate.property name="type" type="string" column="TYPE" length="50"
     * @return The type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
     * @see #setType(String)
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
     * @param type The type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
     * @see #getType()
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Returns true if this specimen still physically available 
     * in the tissue bank else returns false.
     * @hibernate.property name="available" type="boolean" column="AVAILABLE"
     * @return true if this specimen still physically available 
     * in the tissue bank else returns false.
     * @see #setAvailable(Boolean)
     */
    public Boolean getAvailable()
    {
        return available;
    }

    /**
     * Sets true if this specimen still physically available 
     * in the tissue bank else returns false.
     * @param available true if this specimen still physically available else false.
     * @see #getAvailable()
     */
    public void setAvailable(Boolean available)
    {
        this.available = available;
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
     * Returns the barcode assigned to the specimen.
     * @hibernate.property name="barcode" type="string" column="BARCODE" length="50" unique="true"
     * @return the barcode assigned to the specimen.
     * @see #setBarcode(String)
     */
    public String getBarcode()
    {
        return barcode;
    }

    /**
     * Sets the barcode assigned to the specimen.
     * @param barCode the barcode assigned to the specimen.
     * @see #getBarcode()
     */
    public void setBarcode(String barcode)
    {
        this.barcode = barcode;
    }

    /**
     * Returns the comments on the specimen.
     * @hibernate.property name="comments" type="string" column="COMMENTS" length="200"
     * @return the comments on the specimen.
     * @see #setComments(String)
     */
    public String getComments()
    {
        return comments;
    }

    /**
     * Sets the comments on the specimen.
     * @param comments The comments to set.
     * @see #getComments()
     */
    public void setComments(String comments)
    {
        this.comments = comments;
    }

    /**
     * Returns whether this Specimen record can be queried (Active) or not queried (Inactive) by any actor.
     * @hibernate.property name="activityStatus" type="string" column="ACTIVITY_STATUS" length="50"
     * @return "Active" if this Specimen record can be queried or "Inactive" if cannot be queried.
     * @see #setActivityStatus(String)
     */
    public String getActivityStatus()
    {
        return activityStatus;
    }

    /**
     * Sets whether this Specimen record can be queried (Active) or not queried (Inactive) by any actor.
     * @param activityStatus "Active" if this Specimen record can be queried else "Inactive".
     * @see #getActivityStatus()
     */
    public void setActivityStatus(String activityStatus)
    {
        this.activityStatus = activityStatus;
    }

    /**
     * Returns the parent specimen from which this specimen is derived.
     * @hibernate.many-to-one column="PARENT_SPECIMEN_ID"
     * class="edu.wustl.catissuecore.domain.Specimen" constrained="true"
     * @return the parent specimen from which this specimen is derived.
     * @see #setParentSpecimen(SpecimenNew)
     */
    public Specimen getParentSpecimen()
    {
        return parentSpecimen;
    }

    /**
     * Sets the parent specimen from which this specimen is derived.
     * @param parentSpecimen the parent specimen from which this specimen is derived.
     * @see #getParentSpecimen()
     */
    public void setParentSpecimen(Specimen parentSpecimen)
    {
        this.parentSpecimen = parentSpecimen;
    }

    /**
     * Returns the collection of attributes of a Specimen 
     * that renders it potentially harmful to a User.
     * @hibernate.set name="biohazardCollection" table="CATISSUE_SPECIMEN_BIOHAZARD_RELATIONSHIP"
     * cascade="none" inverse="false" lazy="false"
     * @hibernate.collection-key column="SPECIMEN_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.Biohazard" column="BIOHAZARD_ID"
     * @return the collection of attributes of a Specimen 
     * that renders it potentially harmful to a User.
     * @see #setBiohazardCollection(Set)
     */
    public Collection getBiohazardCollection()
    {
        return biohazardCollection;
    }

    /**
     * Sets the collection of attributes of a Specimen 
     * that renders it potentially harmful to a User.
     * @param biohazardCollection the collection of attributes of a Specimen 
     * that renders it potentially harmful to a User.
     * @see #getBiohazardCollection()
     */
    public void setBiohazardCollection(Collection biohazardCollection)
    {
        this.biohazardCollection = biohazardCollection;
    }

    /**
     * Returns the collection of Specimen Event Parameters associated with this specimen.  
     * @hibernate.set name="specimenEventCollection" table="CATISSUE_SPECIMEN_EVENT"
     * cascade="save-update" inverse="true" lazy="false"
     * @hibernate.collection-key column="SPECIMEN_ID"
     * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.SpecimenEventParameters"
     * @return the collection of Specimen Event Parameters associated with this specimen.
     * @see #setSpecimenEventCollection(Set)
     */
    public Collection getSpecimenEventCollection()
    {
        return specimenEventCollection;
    }

    /**
     * Sets the collection of Specimen Event Parameters associated with this specimen.
     * @param specimenEventCollection the collection of Specimen Event Parameters 
     * associated with this specimen.
     * @see #getSpecimenEventCollection()
     */
    public void setSpecimenEventCollection(Collection specimenEventCollection)
    {
        this.specimenEventCollection = specimenEventCollection;
    }

    /**
     * Returns the collection of children specimens derived from this specimen.
     * @hibernate.set name="childrenSpecimen" table="CATISSUE_SPECIMEN"
     * cascade="save-update" inverse="true" lazy="false"
     * @hibernate.collection-key column="PARENT_SPECIMEN_ID"
     * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Specimen"
     * @return the collection of children specimens derived from this specimen.
     * @see #setChildrenSpecimen(Set)
     */
    public Collection getChildrenSpecimen()
    {
        return childrenSpecimen;
    }

    /**
     * Sets the collection of children specimens derived from this specimen.
     * @param childrenSpecimen the collection of children specimens 
     * derived from this specimen.
     * @see #getChildrenSpecimen()
     */
    public void setChildrenSpecimen(Collection childrenSpecimen)
    {
        this.childrenSpecimen = childrenSpecimen;
    }

    /**
     * Returns the physically discreet container that is used to store a specimen  e.g. Box, Freezer etc.
     * @hibernate.many-to-one column="STORAGE_CONTAINER_IDENTIFIER" 
     * class="edu.wustl.catissuecore.domain.StorageContainer" constrained="true"
     * @return the physically discreet container that is used to store a specimen  e.g. Box, Freezer etc.
     * @see #setStorageContainer(StorageContainer)
     */
    public StorageContainer getStorageContainer()
    {
        return storageContainer;
    }

    /**
     * Sets the physically discreet container that is used to store a specimen  e.g. Box, Freezer etc.
     * @param storageContainer the physically discreet container that is used to store a specimen  
     * e.g. Box, Freezer etc.
     * @see #getStorageContainer()
     */
    public void setStorageContainer(StorageContainer storageContainer)
    {
        this.storageContainer = storageContainer;
    }

    /**
     * Returns the collection of a pre-existing, externally defined systemIdentifier associated with a specimen.
     * @hibernate.set name="externalIdentifierCollection" table="CATISSUE_EXTERNAL_IDENTIFIER"
     * cascade="save-update" inverse="true" lazy="false"
     * @hibernate.collection-key column="SPECIMEN_ID"
     * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.ExternalIdentifier"
     * @return the collection of a pre-existing, externally defined systemIdentifier associated with a specimen.
     * @see #setExternalIdentifierCollection(Set)
     */
    public Collection getExternalIdentifierCollection()
    {
        return externalIdentifierCollection;
    }

    /**
     * Sets the collection of a pre-existing, externally defined systemIdentifier 
     * associated with a specimen.
     * @param externalIdentifierCollection the collection of a pre-existing, 
     * externally defined systemIdentifier associated with a specimen.
     * @see #getExternalIdentifierCollection()
     */
    public void setExternalIdentifierCollection(Collection externalIdentifierCollection)
    {
        this.externalIdentifierCollection = externalIdentifierCollection;
    }

    /**
     * Returns the event that results in the collection of one or more specimen from a participant.
     * @hibernate.many-to-one column="SPECIMEN_COLLECTION_GROUP_ID"
	 * class="edu.wustl.catissuecore.domain.SpecimenCollectionGroup" constrained="true"
     * @return the event that results in the collection of one or more specimen from a participant.
     * @see #setSpecimenCollectionGroup(SpecimenCollectionGroup)
     */
    public SpecimenCollectionGroup getSpecimenCollectionGroup()
    {
        return specimenCollectionGroup;
    }

    /**
     * Sets the event that results in the collection of one or more specimen from a participant.
     * @param specimenCollectionGroup the event that results in the collection of one or more 
     * specimen from a participant.
     * @see #getSpecimenCollectionGroup()
     */
    public void setSpecimenCollectionGroup(
            SpecimenCollectionGroup specimenCollectionGroup)
    {
        this.specimenCollectionGroup = specimenCollectionGroup;
    }

    /**
     * Returns the combined anatomic state and pathological disease classification of a specimen.
     * @hibernate.many-to-one column="SPECIMEN_CHARACTERISTICS_ID" 
     * class="edu.wustl.catissuecore.domain.SpecimenCharacteristics" constrained="true"
     * @return the combined anatomic state and pathological disease classification of a specimen.
     * @see #setSpecimenCharacteristics(SpecimenCharacteristics)
     */
    public SpecimenCharacteristics getSpecimenCharacteristics()
    {
        return specimenCharacteristics;
    }

    /**
     * Sets the combined anatomic state and pathological disease classification of a specimen.
     * @param specimenCharacteristics the combined anatomic state and pathological disease 
     * classification of a specimen.
     * @see #getSpecimenCharacteristics()
     */
    public void setSpecimenCharacteristics(SpecimenCharacteristics specimenCharacteristics)
    {
        this.specimenCharacteristics = specimenCharacteristics;
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
     * This function Copies the data from an NewSpecimenForm object to a Specimen object.
     * @param siteForm An SiteForm object containing the information about the site.  
     * */
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
        	Validator validator = new Validator();
        	if(abstractForm instanceof NewSpecimenForm)
            {
	        	NewSpecimenForm form = (NewSpecimenForm) abstractForm;
	            
	            this.activityStatus = form.getActivityStatus();
	            
	            if(!validator.isEmpty(form.getBarcode()))
	            	this.barcode = form.getBarcode();
	            else
	            	this.barcode = null; 
	            
	            this.comments = form.getComments();
	            this.positionDimensionOne = new Integer(form.getPositionDimensionOne());
	            this.positionDimensionTwo = new Integer(form.getPositionDimensionTwo());
	            this.type = form.getType();
	            
	            if(form.isAddOperation())
	            {
	            	this.available = new Boolean(true);
	            }
	            else
	            {
	            	this.available = new Boolean(form.isAvailable());
	            }
	            
	            //in case of edit
	        	if(!form.isAddOperation())
	        	{
	        		//specimen is a new specimen  
	        		if(parentSpecimen==null)
	        		{
	        			String parentSpecimenId = form.getParentSpecimenId();
	        			// specimen created from another specimen
	        			if(parentSpecimenId!=null && !parentSpecimenId.trim().equals("") && Long.parseLong(parentSpecimenId)>0)
	        			{
	        				isParentChanged = true;
	        			}
	        		}
	        		else//specimen created from another specimen
	        		{
	        			if(parentSpecimen.getSystemIdentifier().longValue()!=Long.parseLong(form.getParentSpecimenId()))
	        			{
	        				isParentChanged = true;
	        			}
	        		}
	        	}
		        
	            Logger.out.debug("isParentChanged "+isParentChanged);
		        if(form.isParentPresent())
				{
		            parentSpecimen = new CellSpecimen();
					parentSpecimen.setSystemIdentifier(new Long(form.getParentSpecimenId()));
					
	        		this.setPositionDimensionOne(new Integer(form.getPositionDimensionOne()));
	        		this.setPositionDimensionTwo(new Integer(form.getPositionDimensionTwo()));
				}
		        else
		        {
		        	parentSpecimen = null;
		        	specimenCollectionGroup = new SpecimenCollectionGroup();
		        	this.specimenCollectionGroup.setSystemIdentifier(new Long(form.getSpecimenCollectionGroupId()));
		        }
	            
	            this.storageContainer.setSystemIdentifier(new Long(form.getStorageContainer()));
	            
	            //Setting the SpecimenCharacteristics
	            specimenCharacteristics.pathologicalStatus = form.getPathologicalStatus();
	            specimenCharacteristics.tissueSide = form.getTissueSide();
	            specimenCharacteristics.tissueSite = form.getTissueSite();
	            
	            //Getting the Map of External Identifiers
	            Map extMap = form.getExternalIdentifier();
		        
		        MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
		        
		        Collection extCollection = parser.generateData(extMap);
		        this.externalIdentifierCollection = extCollection;
		        
		        Map bioMap = form.getBiohazard();
		        Logger.out.debug("PRE FIX MAP "+bioMap);
		        bioMap = fixMap(bioMap);
		        Logger.out.debug("POST FIX MAP "+bioMap);
		        
		        //Getting the Map of Biohazards
		        parser = new MapDataParser("edu.wustl.catissuecore.domain");
		        Collection bioCollection = parser.generateData(bioMap);
		        Logger.out.debug("BIO-COL : " + bioCollection );

		        this.biohazardCollection = bioCollection;
            }
            else if(abstractForm instanceof CreateSpecimenForm)
            {
            	CreateSpecimenForm form = (CreateSpecimenForm)abstractForm;
            	
            	this.activityStatus = form.getActivityStatus();
            	
            	if(!validator.isEmpty(form.getBarcode()))
	            	this.barcode = form.getBarcode();
	            else
	            	this.barcode = null; 
            	
            	this.comments = form.getComments();
            	this.positionDimensionOne = new Integer(form.getPositionDimensionOne());
	            this.positionDimensionTwo = new Integer(form.getPositionDimensionTwo());
            	this.type = form.getType();
            	
            	if(form.isAddOperation())
	            {
	            	this.available = new Boolean(true);
	            }
	            else
	            {
	            	this.available = new Boolean(form.isAvailable());
	            }
            	
            	this.storageContainer.setSystemIdentifier(new Long(form.getStorageContainer()));
            	this.parentSpecimen = new CellSpecimen();
            	
            	this.parentSpecimen.setSystemIdentifier(new Long(form.getParentSpecimenId()));
            	//Getting the Map of External Identifiers
	            Map extMap = form.getExternalIdentifier();
		        
		        MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
		        
		        Collection extCollection = parser.generateData(extMap);
		        this.externalIdentifierCollection = extCollection;
            }
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage(),excp);
        }
    }
    
    protected Map fixMap(Map orgMap)
	{
		Map newMap = new HashMap();
		Iterator it = orgMap.keySet().iterator();
		while(it.hasNext())
		{
			String key = (String)it.next();
			String value = (String)orgMap.get(key);
			
			//Logger.out.debug("key "+key);
			
			if(key.indexOf("persisted")==-1)
			{
				newMap.put(key,value);
			}
		}		
		return newMap;
	}
    
    
    /**
     * This function returns the actual type of the specimen i.e Cell / Fluid / Molecular / Tissue.
     */
    
    public final String getClassName()
    {
    	String className = null;
    	
    	if(this instanceof CellSpecimen)
    	{
    		className = "Cell";
    	}
    	else if(this instanceof MolecularSpecimen)
    	{
    		className = "Molecular";
    	}
    	else if(this instanceof FluidSpecimen)
    	{
    		className = "Fluid";
    	}
    	else if(this instanceof TissueSpecimen)
    	{
    		className = "Tissue";
    	}
    	
    	return className;
    }
    
    public String getObjectId() 
    {
		Logger.out.debug(this.getClass().getName()+" is an instance of Specimen class");
		return Specimen.class.getName() + "_" + this.getSystemIdentifier();
	}
}