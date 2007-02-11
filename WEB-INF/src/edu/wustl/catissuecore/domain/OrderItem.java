/**
 * <p>Title: OrderItem Class>
 * <p>Description:  Parent Class for Ordering System.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on October 10,2006
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * It identifies individual order items ordered by the user. 
 * @author ashish_gupta
 * @hibernate.class table="CATISSUE_ORDER_ITEM"
 */
public class OrderItem extends AbstractDomainObject implements Serializable
{

	/**
	 * System generated unique id.
	 */
	protected Long id;

	/**
	 * String containing the description of ordered specimens
	 */
	protected String description;

	/**
	 * Integer containing the amount of requested quantity of ordered specimens
	 */
	protected Quantity requestedQuantity;
	/**
	 * String containing the status of order.
	 */
	protected String status;
	/**
	 * OrderDetails associated with the order_item.
	 */
	protected OrderDetails order;
	/**
	 * The distributed Item associated with this OrderItem.
	 */
	protected DistributedItem distributedItem;
	
	/**
	 * The form id.
	 */
	private static final long serialVersionUID = 1L;
	
	/** 
	 * @param abstractActionForm object.
	 * @throws  AssignDataException object.
	 */
	public void setAllValues(AbstractActionForm abstractActionForm) throws AssignDataException
	{
		
	}

	/**
	 * Returns the system generated unique id.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_ORDER_ITEM_SEQ"
	 * @return the system generated unique id.
	 * @see #setId(Long)
	 * */
	public Long getId()
	{
		return id;
	}

	/**
	 * Sets the system generated unique id.
	 * @param id the system generated unique id.
	 * @see #getId()
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * Returns the description entered by user for each requested orders
	 * @hibernate.property name="description" type="string" column="DESCRIPTION" length="1000"
	 * @return description String containing the description of each order item.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Sets the description entered by User. 
	 * @param description object
	 * @see #getDescription()
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * Returns the amount/quantity of requested specimens
	 * @hibernate.component class="edu.wustl.catissuecore.domain.Quantity"
	 * @return quantity of the requested specimens
	 */
	public Quantity getRequestedQuantity()
	{
		return requestedQuantity;
	}

	/**
	 * Sets the amount/quantity of requested specimens
	 * @param requestedQuantity Quantity
	 */
	public void setRequestedQuantity(Quantity requestedQuantity)
	{
		this.requestedQuantity = requestedQuantity;
	}	

	/**
	 * Returns the status of the ordered specimens
	 * @hibernate.property name="status" type="string" column="STATUS" length="50" 
	 * @return Status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * Sets the status of the ordered derived specimens
	 * @param status String
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	
	/**
	 * The order id associated with the order item.
	 * @hibernate.many-to-one column="ORDER_ID" class="edu.wustl.catissuecore.domain.OrderDetails" constrained="true"
	 * @return the order
	 */
	public OrderDetails getOrder()
	{
		return order;
	}

	
	/**
	 * @param order the orderId to set
	 */
	public void setOrder(OrderDetails order)
	{
		this.order = order;
	}

	
	/**
	 * @return the distributedItem
	 * @hibernate.many-to-one column="DISTRIBUTED_ITEM_ID"
	 * class="edu.wustl.catissuecore.domain.DistributedItem" constrained="true"
	 */
	public DistributedItem getDistributedItem()
	{
		return distributedItem;
	}

	
	/**
	 * @param distributedItem the distributedItem to set
	 */
	public void setDistributedItem(DistributedItem distributedItem)
	{
		this.distributedItem = distributedItem;
	}

	
	

	
	

	
}
