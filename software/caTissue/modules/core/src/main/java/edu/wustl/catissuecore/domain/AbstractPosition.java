package edu.wustl.catissuecore.domain;

import edu.wustl.common.domain.AbstractDomainObject;

import java.io.Serializable;
/**
	* 
	**/

public abstract class AbstractPosition extends AbstractDomainObject implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
	/**
	* It is the identifier of the storage container.
	**/
	
	protected Long id;
	/**
	* Retrieves the value of the id attribute
	* @return id
	**/

	public Long getId(){
		return id;
	}

	/**
	* Sets the value of id attribute
	**/

	public void setId(Long id){
		this.id = id;
	}
	
	/**
	* It is the dimension one of the storage container.
	**/
	
	protected Integer positionDimensionOne;
	/**
	* Retrieves the value of the positionDimensionOne attribute
	* @return positionDimensionOne
	**/

	public Integer getPositionDimensionOne(){
		return positionDimensionOne;
	}

	/**
	* Sets the value of positionDimensionOne attribute
	**/

	public void setPositionDimensionOne(Integer positionDimensionOne){
		this.positionDimensionOne = positionDimensionOne;
	}
	
	/**
	* It is the dimension two of the storage container.
	**/
	
	protected Integer positionDimensionTwo;
	/**
	* Retrieves the value of the positionDimensionTwo attribute
	* @return positionDimensionTwo
	**/

	public Integer getPositionDimensionTwo(){
		return positionDimensionTwo;
	}

	/**
	* Sets the value of positionDimensionTwo attribute
	**/

	public void setPositionDimensionTwo(Integer positionDimensionTwo){
		this.positionDimensionTwo = positionDimensionTwo;
	}
	
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof AbstractPosition) 
		{
			AbstractPosition c =(AbstractPosition)obj; 			 
			if(getId() != null && getId().equals(c.getId()))
				return true;
		}
		return false;
	}
		
	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null)
			return getId().hashCode();
		return 0;
	}
	
	/*
	 * It is the dimension one of the storage container to store values in string.
	 */
	protected String positionDimensionOneString;
	/**
	 * It is the dimension two of the storage container to store values in string.
	 */
	protected String positionDimensionTwoString;
	
	public String getPositionDimensionOneString()
	{
		return positionDimensionOneString;
	}

	
	public void setPositionDimensionOneString(String positionDimensionOneString)
	{
		this.positionDimensionOneString = positionDimensionOneString;
	}

	
	public String getPositionDimensionTwoString()
	{
		return positionDimensionTwoString;
	}

	
	public void setPositionDimensionTwoString(String positionDimensionTwoString)
	{
		this.positionDimensionTwoString = positionDimensionTwoString;
	}
	
}