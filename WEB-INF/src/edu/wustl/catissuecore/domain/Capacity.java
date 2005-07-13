/**
 * <p>Title: Capacity Class>
 * <p>Description:  Models the Capacity information for different storage units.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.domain;

/**
 * Models the Capacity information for different storage units.
 * @hibernate.class table="CATISSUE_CAPACITY"
 * @author gautam_shetty
 */
public class Capacity implements java.io.Serializable
{

    private static final long serialVersionUID = 1234567890L;

    /**
	 * identifier is a unique id assigned to each capacity of a storage unit.
	 */
    protected Long identifier;

    /**
     * Number of objects that can be stored in dimension one.
     */
    protected Integer dimensionOne;

    /**
     * Number of objects that can be stored in dimension two.
     */
    protected Integer dimensionTwo;

    /**
     * Number of objects that can be stored in dimension three.
     */
    protected Integer dimensionThree;

    /**
	 * Returns the identifier assigned to this capacity.
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @return the identifier assigned to this capacity.
	 * @see #setIdentifier(Long)
	 */
    public Long getIdentifier()
    {
        return identifier;
    }

    /**
     * Sets the identifier assigned to this capacity.
     * @param identifier the identifier assigned to this capacity.
     * @see #getIdentifier()
     */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Returns the number of objects that can be stored in dimension one.
	 * @hibernate.property name="dimensionOne" type="int" column="DIMENSION_ONE" length="50"
     * @return the number of objects that can be stored in dimension one.
     * @see #setDimensionOne(Integer)
     */
    public Integer getDimensionOne()
    {
        return dimensionOne;
    }

    /**
     * Sets the number of objects that can be stored in dimension one.
     * @param dimensionOne the number of objects that can be stored in dimension one.
     * @see #getDimensionOne()
     */
    public void setDimensionOne(Integer dimensionOne)
    {
        this.dimensionOne = dimensionOne;
    }

    /**
     * Returns the number of objects that can be stored in dimension two.
	 * @hibernate.property name="dimensionTwo" type="int" column="DIMENSION_TWO" length="50"
     * @return the number of objects that can be stored in dimension two.
     * @see #setDimensionTwo(Integer)
     */
    public Integer getDimensionTwo()
    {
        return dimensionTwo;
    }

    /**
     * Sets the number of objects that can be stored in dimension two.
     * @param dimensionTwo the number of objects that can be stored in dimension two.
     * @see #getDimensionTwo()
     */
    public void setDimensionTwo(Integer dimensionTwo)
    {
        this.dimensionTwo = dimensionTwo;
    }

    /**
     * Returns the number of objects that can be stored in dimension three.
	 * @hibernate.property name="dimensionThree" type="int" column="DIMENSION_THREE" length="50"
     * @return the number of objects that can be stored in dimension three.
     * @see #setDimensionThree(Integer)
     */
    public Integer getDimensionThree()
    {
        return dimensionThree;
    }

    /**
     * Sets the number of objects that can be stored in dimension three.
     * @param dimensionThree the number of objects that can be stored in dimension three.
     * @see #getdimensionThree()
     */
    public void setDimensionThree(Integer dimensionThree)
    {
        this.dimensionThree = dimensionThree;
    }
}