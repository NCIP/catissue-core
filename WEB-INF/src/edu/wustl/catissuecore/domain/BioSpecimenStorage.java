/**
 * <p>Title: BioSpecimenStorage Class>
 * <p>Description:  Models the biospecimen storage information. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import javax.swing.Box;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;

/**
 * Models the biospecimen storage information.
 * @hibernate.class table="CATISSUE_BIOSPECIMEN_STORAGE"
 * @author gautam_shetty
 */
public class BioSpecimenStorage extends AbstractDomainObject implements Serializable
{

    private static final long serialVersionUID = 1234567890L;

    /**
     * identifier is a unique id assigned to each biospecimen storage.
     * */
    protected Long identifier;
    
    /**
     * Biospecimen type.
     */
    protected String bioSpecimenType;

    /**
     * Biospecimen identifier.
     */
    protected Long bioSpecimenIdentifier;

    /**
     * Dimension one of the position of the Box. 
     */
    protected Integer positionDimensionOne;

    /**
     * Dimension two of the position of the Box. 
     */
    protected Integer positionDimensionTwo;

    /**
     * Dimension three of the position of the Box. 
     */
    protected Integer positionDimensionThree;

    /**
     * The Box in which the biospecimen is stored.
     */
    private Box box;

    /**
     * Returns the unique identifier assigned to the biospecimen storage.
     * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return the unique identifier assigned to the biospecimen storage.
     * @see #setIdentifier(int)
     * */
    public Long getIdentifier()
    {
        return identifier;
    }

    /**
     * Sets the unique identifier to the biospecimen storage.
     * @param identifier the unique identifier to the biospecimen storage.
     * @see #getIdentifier() 
     */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Returns the biospecimen type (e.g Specimen,Segment,Sample).
     * @hibernate.property name="bioSpecimenType" type="string"
     * column="TYPE" length="50"
     * @return the biospecimen type (e.g Specimen,Segment,Sample).
     * @see #setBioSpecimenType(String)
     */
    public String getBioSpecimenType()
    {
        return bioSpecimenType;
    }

    /**
     * Sets the biospecimen type (e.g Specimen,Segment,Sample).
     * @param bioSpecimenType the biospecimen type (e.g Specimen,Segment,Sample).
     * @see #getBioSpecimenType()
     */
    public void setBioSpecimenType(String bioSpecimenType)
    {
        this.bioSpecimenType = bioSpecimenType;
    }

    /**
     * Returns the biospecimen identifier.
     * @hibernate.property name="bioSpecimenIdentifier" type="long"
     * column="BIOSPECIMEN_IDENTIFIER" length="50"
     * @return the biospecimen identifier.
     * @see #setBioSpecimenIdentifier(Long)
     */
    public Long getBioSpecimenIdentifier()
    {
        return bioSpecimenIdentifier;
    }

    /**
     * Sets the biospecimen identifier.
     * @param bioSpecimenIdentifier the biospecimen identifier.
     * @see #getBioSpecimenIdentifier()
     */
    public void setBioSpecimenIdentifier(Long bioSpecimenIdentifier)
    {
        this.bioSpecimenIdentifier = bioSpecimenIdentifier;
    }

    /**
     * Returns the dimension one of the position of the Box.
     * @hibernate.property name="positionDimensionOne" type="long"
     * column="POSITION_DIMENSION_ONE" length="50" 
     * @return the dimension one of the position of the Box.
     * @see #setPositionDimensionOne(Integer)
     */
    public Integer getPositionDimensionOne()
    {
        return positionDimensionOne;
    }

    /**
     * Sets the dimension one of the position of the Box.
     * @param positionDimensionOne the dimension one of the position of the Box.
     * @see #getPositionDimensionOne()
     */
    public void setPositionDimensionOne(Integer positionDimensionOne)
    {
        this.positionDimensionOne = positionDimensionOne;
    }

    /**
     * Returns the dimension two of the position of the Box.
     * @hibernate.property name="positionDimensionTwo" type="long"
     * column="POSITION_DIMENSION_TWO" length="50" 
     * @return the dimension two of the position of the Box.
     * @see #setPositionDimensionTwo(Integer)
     */
    public Integer getPositionDimensionTwo()
    {
        return positionDimensionTwo;
    }

    /**
     * Sets the dimension two of the position of the Box.
     * @param positionDimensionTwo the dimension two of the position of the Box.
     * @see #getPositionDimensionTwo()
     */
    public void setPositionDimensionTwo(Integer positionDimensionTwo)
    {
        this.positionDimensionTwo = positionDimensionTwo;
    }

    /**
     * Returns the dimension three of the position of the Box.
     * @hibernate.property name="positionDimensionThree" type="long"
     * column="POSITION_DIMENSION_THREE" length="50" 
     * @return the dimension three of the position of the Box.
     * @see #setPositionDimensionThree(Integer)
     */
    public Integer getPositionDimensionThree()
    {
        return positionDimensionThree;
    }

    /**
     * Sets the dimension three of the position of the Box.
     * @param positionDimensionThree the dimension three of the position of the Box.
     * @see #getPositionDimensionThree()
     */
    public void setPositionDimensionThree(Integer positionDimensionThree)
    {
        this.positionDimensionThree = positionDimensionThree;
    }

    /**
     * Returns the box in which the biospecimen is stored.
     * @hibernate.many-to-one column="BOX_IDENTIFIER" 
	 * class="edu.wustl.catissuecore.domain.Box" constrained="true"
     * @return the box in which the biospecimen is stored.
     * @see #setBox(Box)
     */
    public Box getBox()
    {
        return box;
    }

    /**
     * Sets the box in which the biospecimen is stored.
     * @param box the box in which the biospecimen is stored.
     * @see #getBox()
     */
    public void setBox(Box box)
    {
        this.box = box;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {

    }
}