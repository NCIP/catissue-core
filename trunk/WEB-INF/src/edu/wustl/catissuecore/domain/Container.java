/**
 * <p>Title: Container Class>
 * <p>Description:  Container class represents the base class of container class.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.ContainerForm;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.global.Validator;

/**
 * Container class represents the base class of container class.
 * @author gautam_shetty
 * @author Ashwin Gupta 
 * @hibernate.class table="CATISSUE_CONTAINER"
 */
public class Container extends AbstractDomainObject implements Serializable 
{
    
    protected static final long serialVersionUID = 1234567890L;
    
	protected Long id;
	
	protected Boolean full;
	
	protected String name;
	
	protected String barcode;
	
	protected String activityStatus;
	
	protected Integer positionDimensionOne;
	
	protected Integer positionDimensionTwo;
	
	protected String comment;
	
    // Change for API Search   --- Ashwin 04/10/2006
	protected Capacity capacity;
	 
	protected Collection children = new HashSet();
	
	protected Container parent;
	
	public Container()
	{
	}
	
	/**
     * @return Returns the id.
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native" 
     * @hibernate.generator-param name="sequence" value="CATISSUE_CONTAINER_SEQ"
     */
    public Long getId()
    {
        return id;
    }
    
    /**
     * @param id The id to set.
     */
    public void setId(Long id)
    {
        this.id = id;
    }
	
    /**
     * Returns the activity status of the container.
	 * @hibernate.property name="activityStatus" type="string" column="ACTIVITY_STATUS" length="50"
	 * @return Returns the activity status of the container.
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
     * @return Returns the barcode.
     * @hibernate.property name="barcode" type="string" column="BARCODE" length="255"
     */
    public String getBarcode()
    {
        return barcode;
    }
    
    /**
     * @param barcode The barcode to set.
     */
    public void setBarcode(String barcode)
    {
        this.barcode = barcode;
    }
    
    /**
     * @return Returns the capacity.
     * @hibernate.many-to-one column="CAPACITY_ID" class="edu.wustl.catissuecore.domain.Capacity" constrained="true"
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
     * @return Returns the children.
     * @hibernate.set name="children" table="CATISSUE_CONTAINER" cascade="save-update" 
	 * @hibernate.collection-key column="PARENT_CONTAINER_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Container"
     */
    public Collection getChildren()
    {
        return children;
    }
    
    /**
     * @param children The children to set.
     */
    public void setChildren(Collection children)
    {
        this.children = children;
    }
    
    /**
     * @return Returns the parent.
     * @hibernate.many-to-one column="PARENT_CONTAINER_ID" 
     * class="edu.wustl.catissuecore.domain.Container" constrained="true"
     */
    public Container getParent()
    {
        return parent;
    }
    
    /**
     * @param parent The parent to set.
     */
    public void setParent(Container parent)
    {
        this.parent = parent;
    }
    
    /**
     * @return Returns the comment.
     * @hibernate.property name="comment" type="string" column="COMMENTS" length="2000"
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
    
    /**
     * @return Returns the full.
     * @hibernate.property name="isFull" type="boolean" column="FULL"
     */
    public Boolean isFull()
    {
        return full;
    }
    
    /**
     * @param full The full to set.
     */
    public void setFull(Boolean full)
    {
        this.full = full;
    }
    
    /**
     * @return Returns the name.
     * @hibernate.property name="name" type="string" column="NAME" length="255"
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
     * @return Returns the positionDimensionOne.
     * @hibernate.property name="positionDimensionOne" type="int" column="POSITION_DIMENSION_ONE" length="30"
     */
    public Integer getPositionDimensionOne()
    {
        return positionDimensionOne;
    }
    
    /**
     * @param positionDimensionOne The positionDimensionOne to set.
     */
    public void setPositionDimensionOne(Integer positionDimensionOne)
    {
        this.positionDimensionOne = positionDimensionOne;
    }
    
    /**
     * @return Returns the positionDimensionTwo.
     * @hibernate.property name="positionDimensionTwo" type="int" column="POSITION_DIMENSION_TWO" length="30"
     */
    public Integer getPositionDimensionTwo()
    {
        return positionDimensionTwo;
    }
    
    /**
     * @param positionDimensionTwo The positionDimensionTwo to set.
     */
    public void setPositionDimensionTwo(Integer positionDimensionTwo)
    {
        this.positionDimensionTwo = positionDimensionTwo;
    }
    
    /**
     * (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm actionForm)
            throws AssignDataException
    {
    	if (actionForm instanceof ContainerForm)
    	{
	    	ContainerForm containerForm = (ContainerForm) actionForm;
	        // Change for API Search   --- Ashwin 04/10/2006
	    	if (SearchUtil.isNullobject(this.capacity))
	    	{
	    		capacity = new Capacity();
	    	}
	    	
	    	Validator validator = new Validator();
	        this.id = new Long(containerForm.getId());
	        
	        if(!validator.isEmpty(containerForm.getBarcode()))
	        	this.barcode = containerForm.getBarcode();
	        else
	        	this.barcode = null; 
	
	        this.full = new Boolean(containerForm.getIsFull());
	        this.name = containerForm.getName();
	        this.activityStatus = containerForm.getActivityStatus();
//	        this.positionDimensionOne = new Integer(containerForm.getPositionDimensionOne());
//	        this.positionDimensionTwo = new Integer(containerForm.getPositionDimensionTwo());
	        this.comment = containerForm.getComment();
	        this.capacity.setOneDimensionCapacity(new Integer(containerForm.getOneDimensionCapacity()));
	        this.capacity.setTwoDimensionCapacity(new Integer(containerForm.getTwoDimensionCapacity()));
	        ////////////   Remaining:  put code about parent children container relationship //////
    	}    
    }
    
    /**
     * Returns message label to display on success add or edit
     * @return String
     */
    public String getMessageLabel() {
    	return this.name; 
    }
}