
package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.HashSet;

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

    protected User createdBy;

    protected StorageContainer storageContainer;

    protected Collection specimenArrayContentCollection = new HashSet();

    public SpecimenArray()
    {
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
     * @hibernate.set name="specimenArrayContentCollection" table="CATISSUE_SPECIMEN_ARRAY_CONTENT"
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
}