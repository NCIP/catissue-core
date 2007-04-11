/**
 * <p>Title: DefinedArrayDetailsBean Class>
 * <p>Description:	This class contains attributes of defined array details to display on ArrayRequests.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ramya Nagraj
 * @version 1.00
 * Created on Nov 28,2006
 */

package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.List;

public class DefinedArrayDetailsBean implements Serializable
{

	/**
	 * Default Serial Verison Id
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * String containing the specimen id
	 */
	private String specimenId="";
	
	/**
	 * Specimen Coll group id associated with the Specimen Collection Group. 
	 */
	private String specimenCollGroupId = "";
	
	/**
	 * String containing the item requested for
	 */
	private String requestFor;
	
	/**
	 * Specimen or Pathological case.
	 */
	private String requestedItem;
	
	/**
	 * String containing the requested quantity of specimens 
	 */
	private String requestedQuantity;
	
	/**
	 * String containing the available quantity of specimens
	 */
	private String availableQuantity;
	
	/**
	 * String containing the assigned quantity for each specimen
	 */
	private String assignedQuantity;
	/**
	 * String containing the assigned status for each specimen
	 */
	private String assignedStatus;
	
	/**
	 * List containing the list of derived specimens for a given specimen id
	 */
	private List specimenList;
	
	/**
	 * List containing the next possible status of individual specimen items.
	 */
	private List itemStatusList;
	
	/**
	 * The requested item class.
	 */
	private String className="";
	/**
	 * The requested item type.
	 */
	private String type="";
	/**
	 * The description of the order item.
	 */
	private String description = "";
	/**
	 * The order item id.
	 */
	private String orderItemId;
	/**
	 * Whether orderItem object is instanceof ExistingSpecimen or Derived Specimen.
	 */
	private String instanceOf;
	/**
	 * The assigned quantity.
	 */
	private String assignedQty = "";
	/**
	 * The specimen associated with the order item.
	 */
	//private String specimenId = "";
	/**
	 * The class of specimen.
	 */
	private String actualSpecimenClass;
	/**
	 * The type of specimen.
	 */
	private String actualSpecimenType;
	
	/**
	 * @return the actualSpecimenClass
	 */
	public String getActualSpecimenClass()
	{
		return actualSpecimenClass;
	}

	
	/**
	 * @param actualSpecimenClass the actualSpecimenClass to set
	 */
	public void setActualSpecimenClass(String actualSpecimenClass)
	{
		this.actualSpecimenClass = actualSpecimenClass;
	}

	
	/**
	 * @return the actualSpecimenType
	 */
	public String getActualSpecimenType()
	{
		return actualSpecimenType;
	}

	
	/**
	 * @param actualSpecimenType the actualSpecimenType to set
	 */
	public void setActualSpecimenType(String actualSpecimenType)
	{
		this.actualSpecimenType = actualSpecimenType;
	}

	/**
	 * @return description
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * @param description String containing the description of each order item in defined array
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the assignedQty
	 */
	public String getAssignedQty()
	{
		return assignedQty;
	}

	/**
	 * @return the specimenId
	 *//*
	public String getSpecimenId()
	{
		return specimenId;
	}

	
	*//**
	 * @param specimenId the specimenId to set
	 *//*
	public void setSpecimenId(String specimenId)
	{
		this.specimenId = specimenId;
	}*/
	
	
	/**
	 * @param assignedQty the assignedQty to set
	 */
	public void setAssignedQty(String assignedQty)
	{
		this.assignedQty = assignedQty;
	}
	
	/**
	 * Returns the orderItem id
	 * @return orderItemId
	 */
	public String getOrderItemId()
	{
		return orderItemId;
	}

	/**
	 * Sets the orderItemId
	 * @param orderItemId String containing the id of the orderitem
	 */
	public void setOrderItemId(String orderItemId)
	{
		this.orderItemId = orderItemId;
	}

	/**
	 * @return className
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * @param className String containing the specimen class name of individual order items
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	/**
	 * @return type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type String containing the type of individual order items
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * Returns the assigned quantity
	 * @return assignedQuantity
	 */
	public String getAssignedQuantity()
	{
		return assignedQuantity;
	}
	
	/**
	 * Sets the assigned quantity
	 * @param assignedQuantity String containing the value of qty assigned by admin
	 */
	public void setAssignedQuantity(String assignedQuantity)
	{
		this.assignedQuantity = assignedQuantity;
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
	 * @param assignedStatus String containing the status of individual order item within defined array
	 */
	public void setAssignedStatus(String assignedStatus)
	{
		this.assignedStatus = assignedStatus;
	}
	
	/**
	 * Returns the availableQuantity
	 * @return availableQuantity
	 */
	public String getAvailableQuantity()
	{
		return availableQuantity;
	}
	
	/**
	 * Sets the availableQuantity
	 * @param availableQuantity String containing the available qty of orderitem in the system
	 */
	public void setAvailableQuantity(String availableQuantity)
	{
		this.availableQuantity = availableQuantity;
	}

	/**
	 * Returns the requestedQuantity
	 * @return requestedQuantity
	 */
	public String getRequestedQuantity()
	{
		return requestedQuantity;
	}
	
	/**
	 * Sets the requestedQuantity
	 * @param requestedQuantity String containing the requested qty for individual orderitem by the scientist
	 */
	public void setRequestedQuantity(String requestedQuantity)
	{
		this.requestedQuantity = requestedQuantity;
	}
	
	/**
	 * Returns the item requested for
	 * @return requestFor
	 */
	public String getRequestFor()
	{
		return requestFor;
	}
	
	/**
	 * Sets the item requested for
	 * @param requestFor String indicating whether specimen derivative is requested or it is requested as is by the user
	 */
	public void setRequestFor(String requestFor)
	{
		this.requestFor = requestFor;
	}

	/**
	 * Returns the specimen list
	 * @return specimenList
	 */
	public List getSpecimenList()
	{
		return specimenList;
	}

	/**
	 * Sets the specimenList
	 * @param specimenList List containing the list of specimens
	 */
	public void setSpecimenList(List specimenList)
	{
		this.specimenList = specimenList;
	}

	/**
	 * Returns the itemStatusList
	 * @return itemStatusList
	 */
	public List getItemStatusList()
	{
		return itemStatusList;
	}
	
	/**
	 * Sets the itemStatusList
	 * @param itemStatusList List coontaining the next possible statuses of individual order item
	 */
	public void setItemStatusList(List itemStatusList)
	{
		this.itemStatusList = itemStatusList;
	}

	/**
	 * @return specimenId
	 */
	public String getSpecimenId()
	{
		return specimenId;
	}
	
	/**
	 * @param specimenId String containing the id of specimen whose derivatives are to be retrieved
	 */
	public void setSpecimenId(String specimenId)
	{
		this.specimenId = specimenId;
	}

	/**
	 * @return instanceOf
	 */
	public String getInstanceOf()
	{
		return instanceOf;
	}
	
	/**
	 * @param instanceOf String to indicate whether individual orderitem is instanceof ExistingSpeicmenOrderItem or DerivedSpecimenOrderItem
	 */
	public void setInstanceOf(String instanceOf)
	{
		this.instanceOf = instanceOf;
	}

	/**
	 * @return requestedItem
	 */
	public String getRequestedItem()
	{
		return requestedItem;
	}

	/**
	 * @param requestedItem String containing the the requestedItem to set
	 */
	public void setRequestedItem(String requestedItem)
	{
		this.requestedItem = requestedItem;
	}

	/**
	 * @return specimenCollGroupId
	 */
	public String getSpecimenCollGroupId()
	{
		return specimenCollGroupId;
	}

	/**
	 * @param specimenCollGroupId String containing the specimen collection group id to be set
	 */
	public void setSpecimenCollGroupId(String specimenCollGroupId)
	{
		this.specimenCollGroupId = specimenCollGroupId;
	}

}
