
package edu.wustl.catissuecore.domain;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author gautam_shetty
 * @author Ashwin Gupta 
 * @hibernate.class table="CATISSUE_CONTAINER_TYPE"
 */
public class ContainerType extends AbstractDomainObject
{
    
    protected Long id;

    protected String name;

    protected String oneDimensionLabel;

    protected String twoDimensionLabel;

    protected Capacity capacity = new Capacity();
    
    protected String comment;

    public ContainerType()
    {
    }
    
    public ContainerType(AbstractActionForm abstractActionForm)
    {
    }
    
    /**
     * @see edu.wustl.common.domain.AbstractDomainObject#getId()
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_CONTAINER_TYPE_SEQ"
     */
    public Long getId()
    {
        return this.id;
    }
    
    /**
     * (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#setId(java.lang.Long)
     */
    public void setId(Long id)
    {
        this.id = id;
    }
    
    /**
     * @return Returns the capacity.
     * @hibernate.many-to-one column="CAPACITY_ID" class="edu.wustl.catissuecore.domain.Capacity"
     * constrained="true"
     */
    public Capacity getCapacity()
    {
        return capacity;
    }
    
    /**
     * @param capacity The capacity to set.
     */
    public void setCapacity(Capacity capacity)
    {
        this.capacity = capacity;
    }
    
    /**
     * @return Returns the name.
     * @hibernate.property name="name" type="string" column="NAME" length="100"
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * @return Returns the oneDimensionLabel.
     * @hibernate.property name="oneDimensionLabel" type="string" column="ONE_DIMENSION_LABEL" length="100"
     */
    public String getOneDimensionLabel()
    {
        return oneDimensionLabel;
    }

    /**
     * @param oneDimensionLabel The oneDimensionLabel to set.
     */
    public void setOneDimensionLabel(String oneDimensionLabel)
    {
        this.oneDimensionLabel = oneDimensionLabel;
    }

    /**
     * @return Returns the twoDimensionLabel.
     * @hibernate.property name="twoDimensionLabel" type="string" column="TWO_DIMENSION_LABEL" length="100"
     */
    public String getTwoDimensionLabel()
    {
        return twoDimensionLabel;
    }

    /**
     * @param twoDimensionLabel The twoDimensionLabel to set.
     */
    public void setTwoDimensionLabel(String twoDimensionLabel)
    {
        this.twoDimensionLabel = twoDimensionLabel;
    }
    
    /**
     * @return Returns the comment.
     * @hibernate.property name="comment" type="string" column="COMMENT" length="2000"
     */
    public String getComment()
    {
        return comment;
    }

    /**
     * @param comment The comment to set.
     */
    public void setComment(String comment)
    {
        this.comment = comment;
    }
    
    /* (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm arg0)
            throws AssignDataException
    {
    }
}