package edu.wustl.catissuecore.domain;

import edu.wustl.common.domain.AbstractDomainObject;

import java.io.Serializable;
/**
	* 
	**/

public class ContainerType extends AbstractDomainObject implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
	/**
	* activityStatus.
	**/
	
	protected String activityStatus;
	/**
	* Retrieves the value of the activityStatus attribute
	* @return activityStatus
	**/

	public String getActivityStatus(){
		return activityStatus;
	}

	/**
	* Sets the value of activityStatus attribute
	**/

	public void setActivityStatus(String activityStatus){
		this.activityStatus = activityStatus;
	}
	
	/**
	* comment.
	**/
	
	protected String comment;
	/**
	* Retrieves the value of the comment attribute
	* @return comment
	**/

	public String getComment(){
		return comment;
	}

	/**
	* Sets the value of comment attribute
	**/

	public void setComment(String comment){
		this.comment = comment;
	}
	
	/**
	* id.
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
	* name.
	**/
	
	protected String name;
	/**
	* Retrieves the value of the name attribute
	* @return name
	**/

	public String getName(){
		return name;
	}

	/**
	* Sets the value of name attribute
	**/

	public void setName(String name){
		this.name = name;
	}
	
	/**
	* oneDimensionLabel.
	**/
	
	protected String oneDimensionLabel;
	/**
	* Retrieves the value of the oneDimensionLabel attribute
	* @return oneDimensionLabel
	**/

	public String getOneDimensionLabel(){
		return oneDimensionLabel;
	}

	/**
	* Sets the value of oneDimensionLabel attribute
	**/

	public void setOneDimensionLabel(String oneDimensionLabel){
		this.oneDimensionLabel = oneDimensionLabel;
	}
	
	/**
	* twoDimensionLabel.
	**/
	
	protected String twoDimensionLabel;
	/**
	* Retrieves the value of the twoDimensionLabel attribute
	* @return twoDimensionLabel
	**/

	public String getTwoDimensionLabel(){
		return twoDimensionLabel;
	}

	/**
	* Sets the value of twoDimensionLabel attribute
	**/

	public void setTwoDimensionLabel(String twoDimensionLabel){
		this.twoDimensionLabel = twoDimensionLabel;
	}
	
	/**
	* An associated edu.wustl.catissuecore.domain.Capacity object
	**/
			
	private Capacity capacity;
	/**
	* Retrieves the value of the capacity attribute
	* @return capacity
	**/
	
	public Capacity getCapacity(){
		return capacity;
	}
	/**
	* Sets the value of capacity attribute
	**/

	public void setCapacity(Capacity capacity){
		this.capacity = capacity;
	}
	
	private String oneDimensionLabellingScheme;
	private String twoDimensionLabellingScheme;
		
	public String getOneDimensionLabellingScheme()
	{
		return oneDimensionLabellingScheme;
	}

	
	public void setOneDimensionLabellingScheme(String oneDimensionLabellingScheme)
	{
		this.oneDimensionLabellingScheme = oneDimensionLabellingScheme;
	}

	
	public String getTwoDimensionLabellingScheme()
	{
		return twoDimensionLabellingScheme;
	}

	
	public void setTwoDimensionLabellingScheme(String twoDimensionLabellingScheme)
	{
		this.twoDimensionLabellingScheme = twoDimensionLabellingScheme;
	}


			
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof ContainerType) 
		{
			ContainerType c =(ContainerType)obj; 			 
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
	
}