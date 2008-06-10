/**
 * 
 */
package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.common.domain.AbstractDomainObject;


/**
 * @author ashish_gupta
 *@hibernate.class table="CATISSUE_ABSTRACT_POSITION"
 */
public abstract class AbstractPosition extends AbstractDomainObject implements Serializable
{
	protected Integer positionDimensionOne;
	protected Integer positionDimensionTwo;
	protected Long id;
	
	
	
	
	/**
	 * @return the id
	 *  @hibernate.id name="id" column="IDENTIFIER" type="long" length="30" 
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_ABS_POSITION_SEQ"
	 */
	public Long getId()
	{
		return id;
	}

	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @return the positionDimensionOne
	 * @hibernate.property name="positionDimensionOne" type="int" column="POSITION_DIMENSION_ONE" length="30"
	 */
	public Integer getPositionDimensionOne()
	{
		return positionDimensionOne;
	}
	
	/**
	 * @param positionDimensionOne the positionDimensionOne to set
	 */
	public void setPositionDimensionOne(Integer positionDimensionOne)
	{
		this.positionDimensionOne = positionDimensionOne;
	}
	
	/**
	 * @return the positionDimensionTwo
	 * @hibernate.property name="positionDimensionTwo" type="int" column="POSITION_DIMENSION_TWO" length="30
	 */
	public Integer getPositionDimensionTwo()
	{
		return positionDimensionTwo;
	}
	
	/**
	 * @param positionDimensionTwo the positionDimensionTwo to set
	 */
	public void setPositionDimensionTwo(Integer positionDimensionTwo)
	{
		this.positionDimensionTwo = positionDimensionTwo;
	}
	
}
