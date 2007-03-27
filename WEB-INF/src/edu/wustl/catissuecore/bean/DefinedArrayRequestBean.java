/**
 * <p>Title: ArrayRequestBean Class>
 * <p>Description:	This class contains attributes to display Array Information on ArrayRequests.jsp</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ramya Nagraj
 * @version 1.00
 * Created on Nov 28,2006
 */

package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.List;


public class DefinedArrayRequestBean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String noOfItems;
	/**
	 * To determine when to enable or disable create array button.
	 */
	private String createArrayButtonDisabled;
	/**
	 * String containing the name of the user who requested for the array
	 */
	private String arrayName;
	/**
	 * String indicating the first dimension of defined array
	 */
	private String oneDimensionCapacity;
	/**
	 * String indicating the second dimension of defined array
	 */
	private String twoDimensionCapacity;
	/**
	 * String indicating class of defined array
	 */
	private String arrayClass;
	/**
	 * String containing the name of the array type selected
	 */
	private String arrayType;
	/**
	 * String containing the id of the array type selected
	 */
	private String arrayTypeId;
	/**
	 * String containing status of each of the defined arrays
	 */
	private String assignedStatus;
	/**
	 * String containing the orderItemId of each of the defined arrays
	 */
	private String orderItemId;
	/**
	 * List containing the next possible statuses of each of the defined arrays
	 */
	private List arrayStatusList;
	/**
	 * String containing the id of the each of the defined arrays
	 */
	private String arrayId;
	/**
	 * String containing the name of the user who had requested for the array
	 */
	private String userName="";
	/**
	 * String containing the userId who had requested for the array
	 */
	private Long userId;
	/**
	 * If the array is distributed, it will have a distributed item id.
	 */
	private String distributedItemId;
	 /**
     * String returning the id of the user who created the array
     * @return userId
     */
	public Long getUserId()
	{
		return userId;
	}
	
	/**
	 * @param userId String containing the userId who had requested for the array
	 */
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	/**
	 *  @return userName 
	 */
	public String getUserName()
	{
		return userName;
	}
	
	/**
	 * @param userName String containing the name of the user who requested for the array
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	/**
	 * @return the arrayId
	 */
	public String getArrayId()
	{
		return arrayId;
	}
	
	/**
	 * @param arrayId the arrayId to set
	 */
	public void setArrayId(String arrayId)
	{
		this.arrayId = arrayId;
	}

	/**
	 * @return the orderItemId
	 */
	public String getOrderItemId()
	{
		return orderItemId;
	}


	
	/**
	 * @param orderItemId String containing the orderItemId of each of the defined arrays
	 */
	public void setOrderItemId(String orderItemId)
	{
		this.orderItemId = orderItemId;
	}


	/**
	 * @return the assignedStatus
	 */
	public String getAssignedStatus()
	{
		return assignedStatus;
	}

	
	/**
	 * @param assignedStatus the assignedStatus to set
	 */
	public void setAssignedStatus(String assignedStatus)
	{
		this.assignedStatus = assignedStatus;
	}

	/**
	 * Returns the class of the defined array
	 * @return arrayClass
	 */
	public String getArrayClass()
	{
		return arrayClass;
	}
	
	/**
	 * Sets the class of the defined array
	 * @param arrayClass String indicating class of defined array
	 */
	public void setArrayClass(String arrayClass)
	{
		this.arrayClass = arrayClass;
	}
	
	/**
	 * Returns the name of defined array 
	 * @return arrayName
	 */
	public String getArrayName()
	{
		return arrayName;
	}
	
	/**
	 * Sets the name of defined array
	 * @param arrayName String containing the name of defined array
	 */
	public void setArrayName(String arrayName)
	{
		this.arrayName = arrayName;
	}
	
	/**
	 * Returns the type of Defined array
	 * @return arrayType
	 */
	public String getArrayType()
	{
		return arrayType;
	}
	
	/**
	 * Sets the type of defined array
	 * @param arrayType String indicating the type of defined array
	 */
	public void setArrayType(String arrayType)
	{
		this.arrayType = arrayType;
	}

	/**
	 * Returns the first dimension of defined array
	 * @return oneDimensionCapacity
	 */
	public String getOneDimensionCapacity()
	{
		return oneDimensionCapacity;
	}

	/**
	 * Sets the first dimension of defined array
	 * @param oneDimensionCapacity String indicating the first dimension of defined array
	 */
	public void setOneDimensionCapacity(String oneDimensionCapacity)
	{
		this.oneDimensionCapacity = oneDimensionCapacity;
	}

	/**
	 * Returns the second dimension of defined array
	 * @return twoDimensionCapacity
	 */
	public String getTwoDimensionCapacity()
	{
		return twoDimensionCapacity;
	}

	/**
	 * Sets the second dimension of defined array
	 * @param twoDimensionCapacity String indicating the second dimension of defined array
	 */
	public void setTwoDimensionCapacity(String twoDimensionCapacity)
	{
		this.twoDimensionCapacity = twoDimensionCapacity;
	}
	
	/**
	 * Returns the status list for individual defined arrays
	 * @return arrayStatusList List
	 */
	public List getArrayStatusList()
	{
		return arrayStatusList;
	}
	
	/**
	 * Sets the status list for individual defined arrays
	 * @param arrayStatusList List
	 */
	public void setArrayStatusList(List arrayStatusList)
	{
		this.arrayStatusList = arrayStatusList;
	}

	/**
	 * arrayTypeId
	 * @return arrayTypeId
	 */
	public String getArrayTypeId()
	{
		return arrayTypeId;
	}

	/**
	 * Sets the arrayTypeId in the form
	 * @param arrayTypeId String indicating the id of array type parameter
	 */
	public void setArrayTypeId(String arrayTypeId)
	{
		this.arrayTypeId = arrayTypeId;
	}

	
	/**
	 * @return the createArrayButtonDisabled
	 */
	public String isCreateArrayButtonDisabled()
	{
		return createArrayButtonDisabled;
	}

	
	/**
	 * @param createArrayButtonDisabled the createArrayButtonDisabled to set
	 */
	public void setCreateArrayButtonDisabled(String createArrayButtonDisabled)
	{
		this.createArrayButtonDisabled = createArrayButtonDisabled;
	}

	
	/**
	 * @return the distributedItemId
	 */
	public String getDistributedItemId()
	{
		return distributedItemId;
	}

	
	/**
	 * @param distributedItemId the distributedItemId to set
	 */
	public void setDistributedItemId(String distributedItemId)
	{
		this.distributedItemId = distributedItemId;
	}

	
	/**
	 * @return the noOfItems
	 */
	public String getNoOfItems()
	{
		return noOfItems;
	}

	
	/**
	 * @param noOfItems the noOfItems to set
	 */
	public void setNoOfItems(String noOfItems)
	{
		this.noOfItems = noOfItems;
	}
	
}
