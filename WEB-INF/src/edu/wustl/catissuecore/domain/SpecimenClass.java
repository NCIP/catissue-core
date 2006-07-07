
package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * A single unit of tissue, body fluid, or derivative biological macromolecule 
 * that is collected or created from a Participant
 * @hibernate.class table="CATISSUE_SPECIMEN_CLASS"
 *  
 */
public class SpecimenClass extends AbstractDomainObject implements Serializable
{

	private static final long serialVersionUID = 1234567890L;
	/**
	 * System generated unique systemIdentifier.
	 */
	protected Long systemIdentifier;

	/** name of a specimen class
	 * 
	 */
	protected String name;
	
	private Collection storageContainerCollection=new HashSet(); 

	/**
	 * Returns the class of specimen
	 * @hibernate.property name="name" type="string" column="NAME" length="50"
	 * @see #setName(String)
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the class of specimen.
	 * 
	 * @see #getName()
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Returns the system generated unique systemIdentifier.
	 * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30" 
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_SPECIMEN_CLASS_SEQ"
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
     * Returns the collection of Storage Containers associated with SpecimenClassType 
     * @hibernate.set name="storageContainerCollection" table="CATISSUE_CONT_SPECIMENCL_REL"
     * cascade="none" inverse="false" lazy="false"
     * @hibernate.collection-key column="SPECIMEN_CLASS_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.StorageContainer" column="STORAGE_CONTAINER_ID"
     * @return the collection of StorageContainer of a Storage Type 
     * @see #setStorageContainer(Set)
     */
    public Collection getStorageContainerCollection()
    {
        return storageContainerCollection;
    }

    /**
     * Sets the collection of storage container of a Specimen Class 
     * @param storageContainerCollection the collection of storage container of a Specimen Clas Type 
     * @see #getStorageContainerCollection
     */
    public void setStorageContainerCollection(Collection storageContainerCollection)
    {
        this.storageContainerCollection = storageContainerCollection;
    }

	public void setAllValues(AbstractActionForm abstractForm)
	{
	}

}
