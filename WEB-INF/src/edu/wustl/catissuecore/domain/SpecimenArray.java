
package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.SpecimenArrayForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author gautam_shetty
 * @author Ashwin Gupta 
 * @hibernate.joined-subclass table="CATISSUE_SPECIMEN_ARRAY"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class SpecimenArray extends Container
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    protected SpecimenArrayType specimenArrayType = new SpecimenArrayType();

    protected User createdBy = new User();

    protected StorageContainer storageContainer = new StorageContainer();

    protected Collection specimenArrayContentCollection = new HashSet();
    
    protected Boolean available = new Boolean(true);

    /**
     * Default Constructor 
     */
    public SpecimenArray()
    {
    }

    /**
     * Constructor with action form.
     * @param actionForm abstract action form
     * @throws AssignDataException 
     */
    public SpecimenArray(AbstractActionForm actionForm) throws AssignDataException {
    	setAllValues(actionForm);
    }
    
    /**
     * @return Returns the createdBy.
     * @hibernate.many-to-one column="CREATED_BY_ID" class="edu.wustl.catissuecore.domain.User" 
     * constrained="true"
     */
    public User getCreatedBy()
    {
        return createdBy;
    }

    /**
     * @param createdBy The createdBy to set.
     */
    public void setCreatedBy(User createdBy)
    {
        this.createdBy = createdBy;
    }

    /**
     * @return Returns the specimenArrayContentCollection.
     * @hibernate.set name="specimenArrayContentCollection" table="CATISSUE_SPECI_ARRAY_CONTENT"
     * cascade="none" inverse="false" lazy="false"
     * @hibernate.collection-key column="SPECIMEN_ARRAY_ID"
     * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.SpecimenArrayContent"
     */
    public Collection getSpecimenArrayContentCollection()
    {
        return specimenArrayContentCollection;
    }

    /**
     * @param specimenArrayContentCollection The specimenArrayContentCollection to set.
     */
    public void setSpecimenArrayContentCollection(Collection specimenArrayContentCollection)
    {
        this.specimenArrayContentCollection = specimenArrayContentCollection;
    }

    /**
     * @return Returns the specimenArrayType.
     * @hibernate.many-to-one column="SPECIMEN_ARRAY_TYPE_ID" class="edu.wustl.catissuecore.domain.SpecimenArrayType" 
     * constrained="true"
     */
    public SpecimenArrayType getSpecimenArrayType()
    {
        return specimenArrayType;
    }

    /**
     * @param specimenArrayType The specimenArrayType to set.
     */
    public void setSpecimenArrayType(SpecimenArrayType specimenArrayType)
    {
        this.specimenArrayType = specimenArrayType;
    }

    /**
     * @return Returns the storageContainer.
     * @hibernate.many-to-one column="STORAGE_CONTAINER_ID" class="edu.wustl.catissuecore.domain.StorageContainer" 
     * constrained="true"
     */
    public StorageContainer getStorageContainer()
    {
        return storageContainer;
    }

    /**
     * @param storageContainer The storageContainer to set.
     */
    public void setStorageContainer(StorageContainer storageContainer)
    {
        this.storageContainer = storageContainer;
    }

	/**
	 * @return Returns the available.
	 * @hibernate.property name="available" type="boolean" column="AVAILABLE"
	 */
	public Boolean getAvailable() {
		return available;
	}

	/**
	 * @param available The available to set.
	 */
	public void setAvailable(Boolean available) {
		this.available = available;
	}
	
    /**
     * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm actionForm) throws AssignDataException 
    {
    	super.setAllValues(actionForm);
    	SpecimenArrayForm specimenArrayForm = (SpecimenArrayForm) actionForm;
    	specimenArrayType.setId(new Long(specimenArrayForm.getSpecimenArrayTypeId()));
    	
    	storageContainer.setId(new Long(specimenArrayForm.getStorageContainer()));
    	if (createdBy == null) {
    		createdBy = new User();
    	}
    	createdBy.setId(new Long(specimenArrayForm.getCreatedBy()));
    	capacity.setOneDimensionCapacity(new Integer(specimenArrayForm.getOneDimensionCapacity()));
    	capacity.setTwoDimensionCapacity(new Integer(specimenArrayForm.getTwoDimensionCapacity()));
    	specimenArrayContentCollection = specimenArrayForm.getSpecArrayContentCollection();
    	//SpecimenArrayUtil.getSpecimenContentCollection(specimenArrayForm.getSpecimenArrayGridContentList());
    }
}