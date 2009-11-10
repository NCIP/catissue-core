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

import edu.wustl.common.beans.NameValueBean;

/**
 * @author janhavi_hasabnis
 */
public class DefinedArrayRequestBean implements Serializable
{
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * noOfItems.
	 */
	private String noOfItems;
	/**
	 * To determine when to enable or disable create array button.
	 */
	private String createArrayButtonDisabled;
	/**
	 * String containing the name of the user who requested for the array.
	 */
	private String arrayName;
	/**
	 * String indicating the first dimension of defined array.
	 */
	private String oneDimensionCapacity;
	/**
	 * String indicating the second dimension of defined array.
	 */
	private String twoDimensionCapacity;
	/**
	 * String indicating class of defined array.
	 */
	private String arrayClass;
	/**
	 * String containing the name of the array type selected.
	 */
	private String arrayType;
	/**
	 * String containing the id of the array type selected.
	 */
	private String arrayTypeId;
	/**
	 * String containing status of each of the defined arrays.
	 */
	private String assignedStatus;
	/**
	 * String containing the orderItemId of each of the defined arrays.
	 */
	private String orderItemId;
	/**
	 * List containing the next possible statuses of each of the defined arrays.
	 */
	private List<List<NameValueBean>> arrayStatusList;
	/**
	 * String containing the id of the each of the defined arrays.
	 */
	private String arrayId;
	/**
	 * String containing the name of the user who had requested for the array.
	 */
	private String userName = "";
	/**
	 * String containing the userId who had requested for the array.
	 */
	private Long userId;
	/**
	 * If the array is distributed, it will have a distributed item id.
	 */
	private String distributedItemId;

	/**
	* String returning the id of the user who created the array.
	* @return userId
	*/
	public Long getUserId()
	{
		return this.userId;
	}

	/**
	 * @param userIdParam String containing the userId who had requested for the array
	 */
	public void setUserId(Long userIdParam)
	{
		this.userId = userIdParam;
	}

	/**
	 *  @return userName
	 */
	public String getUserName()
	{
		return this.userName;
	}

	/**
	 * @param userNameParam String containing the name of the user who requested for the array
	 */
	public void setUserName(String userNameParam)
	{
		this.userName = userNameParam;
	}

	/**
	 * @return the arrayId
	 */
	public String getArrayId()
	{
		return this.arrayId;
	}

	/**
	 * @param arrayIdParam the arrayId to set
	 */
	public void setArrayId(String arrayIdParam)
	{
		this.arrayId = arrayIdParam;
	}

	/**
	 * @return the orderItemId
	 */
	public String getOrderItemId()
	{
		return this.orderItemId;
	}

	/**
	 * @param orderItemIdParam String containing the orderItemId of each of the defined arrays
	 */
	public void setOrderItemId(String orderItemIdParam)
	{
		this.orderItemId = orderItemIdParam;
	}

	/**
	 * @return the assignedStatus
	 */
	public String getAssignedStatus()
	{
		return this.assignedStatus;
	}

	/**
	 * @param assignedStatusParam the assignedStatus to set
	 */
	public void setAssignedStatus(String assignedStatusParam)
	{
		this.assignedStatus = assignedStatusParam;
	}

	/**
	 * Returns the class of the defined array.
	 * @return arrayClass
	 */
	public String getArrayClass()
	{
		return this.arrayClass;
	}

	/**
	 * Sets the class of the defined array.
	 * @param arrayClassParam String indicating class of defined array
	 */
	public void setArrayClass(String arrayClassParam)
	{
		this.arrayClass = arrayClassParam;
	}

	/**
	 * Returns the name of defined array.
	 * @return arrayName
	 */
	public String getArrayName()
	{
		return this.arrayName;
	}

	/**
	 * Sets the name of defined array.
	 * @param arrayNameParam String containing the name of defined array
	 */
	public void setArrayName(String arrayNameParam)
	{
		this.arrayName = arrayNameParam;
	}

	/**
	 * Returns the type of Defined array.
	 * @return arrayType
	 */
	public String getArrayType()
	{
		return this.arrayType;
	}

	/**
	 * Sets the type of defined array.
	 * @param arrayTypeParam String indicating the type of defined array
	 */
	public void setArrayType(String arrayTypeParam)
	{
		this.arrayType = arrayTypeParam;
	}

	/**
	 * Returns the first dimension of defined array.
	 * @return oneDimensionCapacity
	 */
	public String getOneDimensionCapacity()
	{
		return this.oneDimensionCapacity;
	}

	/**
	 * Sets the first dimension of defined array.
	 * @param oneDimensionCapacityParam String indicating the first dimension of defined array
	 */
	public void setOneDimensionCapacity(String oneDimensionCapacityParam)
	{
		this.oneDimensionCapacity = oneDimensionCapacityParam;
	}

	/**
	 * Returns the second dimension of defined array.
	 * @return twoDimensionCapacity
	 */
	public String getTwoDimensionCapacity()
	{
		return this.twoDimensionCapacity;
	}

	/**
	 * Sets the second dimension of defined array.
	 * @param twoDimensionCapacityParam String indicating the second dimension of defined array
	 */
	public void setTwoDimensionCapacity(String twoDimensionCapacityParam)
	{
		this.twoDimensionCapacity = twoDimensionCapacityParam;
	}

	/**
	 * Returns the status list for individual defined arrays.
	 * @return arrayStatusList List
	 */
	public List<List<NameValueBean>> getArrayStatusList()
	{
		return this.arrayStatusList;
	}

	/**
	 * Sets the status list for individual defined arrays.
	 * @param arrayStatusListParam List
	 */
	public void setArrayStatusList(List<List<NameValueBean>> arrayStatusListParam)
	{
		this.arrayStatusList = arrayStatusListParam;
	}

	/**
	 * arrayTypeId.
	 * @return arrayTypeId
	 */
	public String getArrayTypeId()
	{
		return this.arrayTypeId;
	}

	/**
	 * Sets the arrayTypeId in the form.
	 * @param arrayTypeIdParam String indicating the id of array type parameter
	 */
	public void setArrayTypeId(String arrayTypeIdParam)
	{
		this.arrayTypeId = arrayTypeIdParam;
	}

	/**
	 * @return the createArrayButtonDisabled
	 */
	public String isCreateArrayButtonDisabled()
	{
		return this.createArrayButtonDisabled;
	}

	/**
	 * @param createArrayButtonDisabledParam the createArrayButtonDisabled to set
	 */
	public void setCreateArrayButtonDisabled(String createArrayButtonDisabledParam)
	{
		this.createArrayButtonDisabled = createArrayButtonDisabledParam;
	}

	/**
	 * @return the distributedItemId
	 */
	public String getDistributedItemId()
	{
		return this.distributedItemId;
	}

	/**
	 * @param distributedItemIdParam the distributedItemId to set
	 */
	public void setDistributedItemId(String distributedItemIdParam)
	{
		this.distributedItemId = distributedItemIdParam;
	}

	/**
	 * @return the noOfItems
	 */
	public String getNoOfItems()
	{
		return this.noOfItems;
	}

	/**
	 * @param noOfItemsParam the noOfItems to set
	 */
	public void setNoOfItems(String noOfItemsParam)
	{
		this.noOfItems = noOfItemsParam;
	}
}