/**
 * <p>Title: SpecimenArrayOrderItem Class>
 * <p>Description:  Abstract Class for SpecimenArrayOrderItem.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on November 09,2006
 */
package edu.wustl.catissuecore.domain;

/**
 * This is abstract class indicating biospecimen array order items(both existing and new specimens).
 */
public abstract class SpecimenArrayOrderItem extends OrderItem
{
	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = -7464499029573351038L;

	/**
	 * Default Constructor.
	 */
	public SpecimenArrayOrderItem()
	{
		super();
	}
}