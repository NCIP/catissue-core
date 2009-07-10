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
/**
 * @author janhavi_hasabnis
 *
 */
public class DefinedArrayDetailsBean implements Serializable
{

	/**
	 * Default Serial Verison Id
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * String containing the specimen id
	 */
	private String specimenId = "";

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
	private String className = "";
	/**
	 * The requested item type.
	 */
	private String type = "";
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
		return this.actualSpecimenClass;
	}

	/**
	 * @param actualSpecimenClassParam the actualSpecimenClass to set
	 */
	public void setActualSpecimenClass(String actualSpecimenClassParam)
	{
		this.actualSpecimenClass = actualSpecimenClassParam;
	}

	/**
	 * @return the actualSpecimenType
	 */
	public String getActualSpecimenType()
	{
		return this.actualSpecimenType;
	}

	/**
	 * @param actualSpecimenTypeParam the actualSpecimenType to set
	 */
	public void setActualSpecimenType(String actualSpecimenTypeParam)
	{
		this.actualSpecimenType = actualSpecimenTypeParam;
	}

	/**
	 * @return description
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * @param descriptionParam String containing the description of each order item in defined array
	 */
	public void setDescription(String descriptionParam)
	{
		this.description = descriptionParam;
	}

	/**
	 * @return the assignedQty
	 */
	public String getAssignedQty()
	{
		return this.assignedQty;
	}

	/**
	 * @return the specimenId
	 */
	/*
		public String getSpecimenId()
		{
			return specimenId;
		}
		*//**
		 * @param specimenId the specimenId to set
		 */
	/*
		public void setSpecimenId(String specimenId)
		{
			this.specimenId = specimenId;
		}*/

	/**
	 * @param assignedQtyParam the assignedQty to set
	 */
	public void setAssignedQty(String assignedQtyParam)
	{
		this.assignedQty = assignedQtyParam;
	}

	/**
	 * Returns the orderItem id
	 * @return orderItemId
	 */
	public String getOrderItemId()
	{
		return this.orderItemId;
	}

	/**
	 * Sets the orderItemId
	 * @param orderItemIdParam String containing the id of the orderitem
	 */
	public void setOrderItemId(String orderItemIdParam)
	{
		this.orderItemId = orderItemIdParam;
	}

	/**
	 * @return className
	 */
	public String getClassName()
	{
		return this.className;
	}

	/**
	 * @param classNameParam String containing the specimen class name of individual order items
	 */
	public void setClassName(String classNameParam)
	{
		this.className = classNameParam;
	}

	/**
	 * @return type
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * @param typeParam String containing the type of individual order items
	 */
	public void setType(String typeParam)
	{
		this.type = typeParam;
	}

	/**
	 * Returns the assigned quantity
	 * @return assignedQuantity
	 */
	public String getAssignedQuantity()
	{
		return this.assignedQuantity;
	}

	/**
	 * Sets the assigned quantity
	 * @param assignedQuantityParam String containing the value of qty assigned by admin
	 */
	public void setAssignedQuantity(String assignedQuantityParam)
	{
		this.assignedQuantity = assignedQuantityParam;
	}

	/**
	 * Returns the assignedStatus
	 * @return assignedStatus
	 */
	public String getAssignedStatus()
	{
		return this.assignedStatus;
	}

	/**
	 * Sets the assignedStatus
	 * @param assignedStatusParam String containing the status of individual order item within defined array
	 */
	public void setAssignedStatus(String assignedStatusParam)
	{
		this.assignedStatus = assignedStatusParam;
	}

	/**
	 * Returns the availableQuantity
	 * @return availableQuantity
	 */
	public String getAvailableQuantity()
	{
		return this.availableQuantity;
	}

	/**
	 * Sets the availableQuantity
	 * @param availableQuantityParam String containing the available qty of orderitem in the system
	 */
	public void setAvailableQuantity(String availableQuantityParam)
	{
		this.availableQuantity = availableQuantityParam;
	}

	/**
	 * Returns the requestedQuantity
	 * @return requestedQuantity
	 */
	public String getRequestedQuantity()
	{
		return this.requestedQuantity;
	}

	/**
	 * Sets the requestedQuantity
	 * @param requestedQuantityParam String containing the requested qty for individual orderitem by the scientist
	 */
	public void setRequestedQuantity(String requestedQuantityParam)
	{
		this.requestedQuantity = requestedQuantityParam;
	}

	/**
	 * Returns the item requested for
	 * @return requestFor
	 */
	public String getRequestFor()
	{
		return this.requestFor;
	}

	/**
	 * Sets the item requested for
	 * @param requestForParam String indicating whether specimen derivative is requested or it is requested as is by the user
	 */
	public void setRequestFor(String requestForParam)
	{
		this.requestFor = requestForParam;
	}

	/**
	 * Returns the specimen list
	 * @return specimenList
	 */
	public List getSpecimenList()
	{
		return this.specimenList;
	}

	/**
	 * Sets the specimenList
	 * @param specimenListParam List containing the list of specimens
	 */
	public void setSpecimenList(List specimenListParam)
	{
		this.specimenList = specimenListParam;
	}

	/**
	 * Returns the itemStatusList
	 * @return itemStatusList
	 */
	public List getItemStatusList()
	{
		return this.itemStatusList;
	}

	/**
	 * Sets the itemStatusList
	 * @param itemStatusListParam List coontaining the next possible statuses of individual order item
	 */
	public void setItemStatusList(List itemStatusListParam)
	{
		this.itemStatusList = itemStatusListParam;
	}

	/**
	 * @return specimenId
	 */
	public String getSpecimenId()
	{
		return this.specimenId;
	}

	/**
	 * @param specimenIdParam String containing the id of specimen whose derivatives are to be retrieved
	 */
	public void setSpecimenId(String specimenIdParam)
	{
		this.specimenId = specimenIdParam;
	}

	/**
	 * @return instanceOf
	 */
	public String getInstanceOf()
	{
		return this.instanceOf;
	}

	/**
	 * @param instanceOfParam String to indicate whether individual orderitem is instanceof ExistingSpeicmenOrderItem or DerivedSpecimenOrderItem
	 */
	public void setInstanceOf(String instanceOfParam)
	{
		this.instanceOf = instanceOfParam;
	}

	/**
	 * @return requestedItem
	 */
	public String getRequestedItem()
	{
		return this.requestedItem;
	}

	/**
	 * @param requestedItemParam String containing the the requestedItem to set
	 */
	public void setRequestedItem(String requestedItemParam)
	{
		this.requestedItem = requestedItemParam;
	}

	/**
	 * @return specimenCollGroupId
	 */
	public String getSpecimenCollGroupId()
	{
		return this.specimenCollGroupId;
	}

	/**
	 * @param specimenCollGroupIdParam String containing the specimen collection group id to be set
	 */
	public void setSpecimenCollGroupId(String specimenCollGroupIdParam)
	{
		this.specimenCollGroupId = specimenCollGroupIdParam;
	}

}
