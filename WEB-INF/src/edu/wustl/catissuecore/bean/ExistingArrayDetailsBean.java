
/**
 * <p>Title: ExistingArrayDetailsBean Class>
 * <p>Description:	This class contains attributes of Existing BioSpecimenArray to display them on ArrayRequests.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ramya Nagraj
 * @version 1.00
 * Created on Dec 01,2006
 */

package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.List;


public class ExistingArrayDetailsBean implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * String containing the name of existing biospecimen array
	 */
	private String bioSpecimenArrayName;
	/**
	 * String containing description
	 */
	private String description="";
	
	/**
	 * String containing assigned status
	 */
	private String assignedStatus;
	/**
	 * String containing the description entered by admin
	 */
	private String addDescription="";
	
	/**
	 * List to store the statuses of individual existingarray order items
	 */
	private List itemStatusList;
	/**
	 * String to store the id of corresponding orderitems
	 */
	private String orderItemId;
	/**
	 * String to store the arrayId of corresponding existing Array order item
	 */
	private String arrayId;
	
	/**
	 * String containing the requested quantity of number of slides for the derived array
	 */
	private String requestedQuantity;
	
	/**
	 * String containing the assigned quantity of number of slides for the derived array
	 */
	private String assignedQuantity;
	/**
	 * If array is distributed, it will have a distributedItemId.
	 */
	private String distributedItemId;
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
	 * @param orderItemId the orderItemId to set
	 */
	public void setOrderItemId(String orderItemId)
	{
		this.orderItemId = orderItemId;
	}

	/**
	 * Returns the assignedStatus
	 * @return assignedStatus 
	 */
	public String getAssignedStatus()
	{
		return assignedStatus;
	}
	
	/**
	 * Sets the assignedStatus
	 * @param assignedStatus String containing the status assigned to each of the existing arrays
	 */
	public void setAssignedStatus(String assignedStatus)
	{
		this.assignedStatus = assignedStatus;
	}
	
	/**
	 * Returns the bioSpecimenArrayName
	 * @return bioSpecimenArrayName 
	 */
	public String getBioSpecimenArrayName()
	{
		return bioSpecimenArrayName;
	}
	
	/**
	 * Sets the bioSpecimenArrayName
	 * @param bioSpecimenArrayName String containing the name of the array
	 */
	public void setBioSpecimenArrayName(String bioSpecimenArrayName)
	{
		this.bioSpecimenArrayName = bioSpecimenArrayName;
	}
	
	/**
	 * Returns the description
	 * @return description
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Sets the description
	 * @param description String containing the description of array entered by the scientist 
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return itemStatusList
	 */
	public List getItemStatusList()
	{
		return itemStatusList;
	}

	/**
	 * @param itemStatusList List containing the next possible statuses of each of the existing arrays
	 */
	public void setItemStatusList(List itemStatusList)
	{
		this.itemStatusList = itemStatusList;
	}
	
	
	/**
	 * Returns the added Description
	 * @return addDescription
	 */
	public String getAddDescription()
	{
		return addDescription;
	}
	
	/**
	 * Sets the added Description
	 * @param addDescription String containing the description entered by the admin
	 */
	public void setAddDescription(String addDescription)
	{
		this.addDescription = addDescription;
	}

	/**
	 * @return requestedQuantity
	 */
	public String getRequestedQuantity()
	{
		return requestedQuantity;
	}
	
	/**
	 * @param requestedQuantity String containing the qty(i.e,no of slides) requested by the scientist
	 */
	public void setRequestedQuantity(String requestedQuantity)
	{
		this.requestedQuantity = requestedQuantity;
	}

	/**
	 * @return assignedQuantity
	 */
	public String getAssignedQuantity()
	{
		return assignedQuantity;
	}

	/**
	 * @param assignedQuantity String containing the qty assigned by the admin
	 */
	public void setAssignedQuantity(String assignedQuantity)
	{
		this.assignedQuantity = assignedQuantity;
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
}
