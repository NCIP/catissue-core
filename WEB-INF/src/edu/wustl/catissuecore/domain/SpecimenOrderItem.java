/**
 * <p>Title: SpecimenOrderItem Class>
 * <p>Description:  Abstract Class for SpecimenOrderItem.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on October 16,2006
 */
package edu.wustl.catissuecore.domain;




/**
 * This is abstract class indicating biospecimen order items(both existing and new specimens).
 * @author ashish_gupta
 */
public class SpecimenOrderItem extends OrderItem
{
	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = -5738302491419544365L;

	/**
	 * The specimens associated with the new Array order.
	 */
	protected NewSpecimenArrayOrderItem newSpecimenArrayOrderItem;

	/**
	 * @hibernate.many-to-one class="edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem"
	 * constrained="true" column="ARRAY_ORDER_ITEM_ID"
	 * @return the newSpecimenArrayOrderItem
	 */
	public NewSpecimenArrayOrderItem getNewSpecimenArrayOrderItem()
	{
		return newSpecimenArrayOrderItem;
	}

	/**
	 * @param newSpecimenArrayOrderItem the newSpecimenArrayOrderItem to set
	 */
	public void setNewSpecimenArrayOrderItem(NewSpecimenArrayOrderItem newSpecimenArrayOrderItem)
	{
		this.newSpecimenArrayOrderItem = newSpecimenArrayOrderItem;
	}
}