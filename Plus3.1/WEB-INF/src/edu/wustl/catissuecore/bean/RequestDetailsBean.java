/**
 * <p>Title: RequestDetailsBean Class>
 * <p>Description:	This class contains attributes to display on RequestDetails.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Oct 10,2006
 */

package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import edu.wustl.common.beans.NameValueBean;
/**
 *
 * @author virender_mehta
 * @created-on Nov 10, 2009
 */
public class RequestDetailsBean implements Serializable
{
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The requested Item - Specimen, Array or Pathological case.
	 */
	private String requestedItem;
	/**
	 * The item requested for.
	 */
	private String requestFor;
	/**
	 * The requested quantity.
	 */
	private String requestedQty;
	/**
	 * The available quantity.
	 */
	private String availableQty;
	/**
	 * The assigned status.
	 */
	private String assignedStatus;
	/**
	 * The possible status List.
	 */
	private List<List<NameValueBean>> itemsStatusList = null;
	/**
	 * The requested item class.
	 */
	private String className;
	/**
	 * The requested item type.
	 */
	private String type;
	/**
	 * The description of the order item.
	 */
	private String description = "";
	/**
	 * Whether orderItem object is instanceof ExistingSpecimen or Derived Specimen or PathologicalCase.
	 */
	private String instanceOf;
	/**
	 * List of specimens(both parent and derivative specimens).
	 */
	private Collection<List<NameValueBean>> specimenList = null;
	/**
	 * The order item id.
	 */
	private String orderItemId;
	/**
	 * The assigned quantity.
	 */
	private String assignedQty;
	/**
	 * The specimen associated with the order item.
	 */
	private String specimenId = "";
	/**
	 * Specimen Coll group id associated with the Specimen Collection Group.
	 */
	private String specimenCollGroupId = "";
	/**
	 * To indicate whether the orderItem is distributed.
	 */
	private String distributedItemId = "";
	/**
	 * The class of specimen.
	 */
	private String actualSpecimenClass;
	/**
	 * The type of specimen.
	 */
	private String actualSpecimenType;

	/**
	 * This gives the type of selected specimen.
	 */
	private String selectedSpecimenType;

	/**
	 * Selected specimen Unit.
	 */
	private String specimenQuantityUnit;

	/**
	 * Selected specimen Quantity.
	 */
	private String selectedSpecimenQuantity;

	/**
	 * For consent View form.
	 *
	 */
	private String consentVerificationkey;

	/**
	 * rowStatuskey is the Status of row selected.
	 */
	private String rowStatuskey;

	/**
	 * flag to check.
	 */
	private String canDistribute;

	/**
	 * @return the className
	 */
	public String getClassName()
	{
		return this.className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return the itemsStatusList
	 */
	public List<List<NameValueBean>> getItemsStatusList()
	{
		return this.itemsStatusList;
	}

	/**
	 * @param itemsStatusList the itemsStatusList to set
	 */
	public void setItemsStatusList(List<List<NameValueBean>> itemsStatusList)
	{
		this.itemsStatusList = itemsStatusList;
	}

	/**
	 * @return the assignedStatus
	 */
	public String getAssignedStatus()
	{
		return this.assignedStatus;
	}

	/**
	 * @param assignedStatus the assignedStatus to set
	 */
	public void setAssignedStatus(String assignedStatus)
	{
		this.assignedStatus = assignedStatus;
	}

	/**
	 * @return the availableQty
	 */
	public String getAvailableQty()
	{
		return this.availableQty;
	}

	/**
	 * @param availableQty the availableQty to set
	 */
	public void setAvailableQty(String availableQty)
	{
		this.availableQty = availableQty;
	}

	/**
	 * @return the requestedItem
	 */
	public String getRequestedItem()
	{
		return this.requestedItem;
	}

	/**
	 * @param requestedItem the requestedItem to set
	 */
	public void setRequestedItem(String requestedItem)
	{
		this.requestedItem = requestedItem;
	}

	/**
	 * @return the requestedQty
	 */
	public String getRequestedQty()
	{
		return this.requestedQty;
	}

	/**
	 * @param requestedQty the requestedQty to set
	 */
	public void setRequestedQty(String requestedQty)
	{
		this.requestedQty = requestedQty;
	}

	/**
	 * @return the requestFor
	 */
	public String getRequestFor()
	{
		return this.requestFor;
	}

	/**
	 * @param requestFor the requestFor to set
	 */
	public void setRequestFor(String requestFor)
	{
		this.requestFor = requestFor;
	}

	/**
	 * Return the Specimen list.
	 * @return List
	 */
	public Collection<List<NameValueBean>> getSpecimenList()
	{
		return this.specimenList;
	}

	/**
	 * Sets the specimen List.
	 * @param specimenList List containing existing and derived specimens is set in the bean
	 */
	public void setSpecimenList(Collection<List<NameValueBean>> specimenList)
	{
		this.specimenList = specimenList;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the instanceOf
	 */
	public String getInstanceOf()
	{
		return this.instanceOf;
	}

	/**
	 * @param instanceOf the instanceOf to set
	 */
	public void setInstanceOf(String instanceOf)
	{
		this.instanceOf = instanceOf;
	}

	/**
	 * @return the orderItemId
	 */
	public String getOrderItemId()
	{
		return this.orderItemId;
	}

	/**
	 * @param orderItemId the orderItemId to set
	 */
	public void setOrderItemId(String orderItemId)
	{
		this.orderItemId = orderItemId;
	}

	/**
	 * @return the assignedQty
	 */
	public String getAssignedQty()
	{
		return this.assignedQty;
	}

	/**
	 * @param assignedQty the assignedQty to set
	 */
	public void setAssignedQty(String assignedQty)
	{
		this.assignedQty = assignedQty;
	}

	/**
	 * @return the specimenId
	 */
	public String getSpecimenId()
	{
		return this.specimenId;
	}

	/**
	 * @param specimenId the specimenId to set
	 */
	public void setSpecimenId(String specimenId)
	{
		this.specimenId = specimenId;
	}

	/**
	 * @return SpecimenCollGroupId
	 */
	public String getSpecimenCollGroupId()
	{
		return this.specimenCollGroupId;
	}

	/**
	 * @param specimenCollGroupId the specimen collection group.
	 */
	public void setSpecimenCollGroupId(String specimenCollGroupId)
	{
		this.specimenCollGroupId = specimenCollGroupId;
	}

	/**
	 * @return the distributedItemId
	 */
	public String getDistributedItemId()
	{
		return this.distributedItemId;
	}

	/**
	 * @param distributedItemId the distributedItemId to set
	 */
	public void setDistributedItemId(String distributedItemId)
	{
		this.distributedItemId = distributedItemId;
	}

	/**
	 * @return the actualSpecimenClass
	 */
	public String getActualSpecimenClass()
	{
		return this.actualSpecimenClass;
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
		return this.actualSpecimenType;
	}

	/**
	 * @param actualSpecimenType the actualSpecimenType to set
	 */
	public void setActualSpecimenType(String actualSpecimenType)
	{
		this.actualSpecimenType = actualSpecimenType;
	}
	/**
	 *
	 * @return consentVerificationkey
	 */
	public String getConsentVerificationkey()
	{
		return this.consentVerificationkey;
	}
	/**
	 *
	 * @param consentVerificationkey consentVerificationkey
	 */
	public void setConsentVerificationkey(String consentVerificationkey)
	{
		this.consentVerificationkey = consentVerificationkey;
	}
	/**
	 *
	 * @return rowStatuskey
	 */
	public String getRowStatuskey()
	{
		return this.rowStatuskey;
	}
	/**
	 *
	 * @param rowStatuskey rowStatuskey
	 */
	public void setRowStatuskey(String rowStatuskey)
	{
		this.rowStatuskey = rowStatuskey;
	}
	/**
	 *
	 * @return canDistribute
	 */
	public String getCanDistribute()
	{
		return this.canDistribute;
	}
	/**
	 *
	 * @param canDistribute canDistribute
	 */
	public void setCanDistribute(String canDistribute)
	{
		this.canDistribute = canDistribute;
	}
	/**
	 *
	 * @return selectedSpecimenType
	 */
	public String getSelectedSpecimenType()
	{
		return this.selectedSpecimenType;
	}
	/**
	 *
	 * @param selectedSpecimenType selectedSpecimenType
	 */
	public void setSelectedSpecimenType(String selectedSpecimenType)
	{
		this.selectedSpecimenType = selectedSpecimenType;
	}
	/**
	 *
	 * @return specimenQuantityUnit
	 */
	public String getSpecimenQuantityUnit()
	{
		return this.specimenQuantityUnit;
	}
	/**
	 *
	 * @param specimenQuantityUnit specimenQuantityUnit
	 */
	public void setSpecimenQuantityUnit(String specimenQuantityUnit)
	{
		this.specimenQuantityUnit = specimenQuantityUnit;
	}
	/**
	 *
	 * @return selectedSpecimenQuantity
	 */
	public String getSelectedSpecimenQuantity()
	{
		return this.selectedSpecimenQuantity;
	}
	/**
	 *
	 * @param selectedSpecimenQuantity selectedSpecimenQuantity
	 */
	public void setSelectedSpecimenQuantity(String selectedSpecimenQuantity)
	{
		this.selectedSpecimenQuantity = selectedSpecimenQuantity;
	}

}
