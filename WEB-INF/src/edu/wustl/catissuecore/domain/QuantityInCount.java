/**
 * <p>Title: QuantityInMicroGram Class>
 * <p>Description:  A class that represents quantity of a specimen in count.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

/*
 * @hibernate.subclass name="QuantityInCount"
 */
public class QuantityInCount extends Quantity implements Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * Quantity in count.
	 */
//	protected Integer value;
	
	/**
	 * Default Constructor
	 */
	public QuantityInCount()
	{
	}
	
	/**
	 * Parameterized Constructor
	 */
	public QuantityInCount(long value)
	{
//		this.quantity = new Integer(quantity);
		this.value = new Double(value);
	}
	
//	/**
//     * Returns the quantity in count.
//     * @hibernate.property name="quantity" type="int" 
//     * column="QUANTITY" length="50"
//     * @return the quantity in count.
//     * @see #setQuantity(Integer)
//     */
//    public Integer getQuantity()
//    {
//        return quantity;
//    }
//
//    /**
//     * Sets the quantity in count.
//     * @param quantity the quantity in count.
//     * @see #getQuantity()
//     */
//    public void setQuantity(Integer quantity)
//    {
//        this.quantity = quantity;
//    }
    
    /**
     * Overriding toString() method of object class.
     */
    public String toString()
    {
    	long qty = (long)value.doubleValue();
    	return String.valueOf(qty);
    }
}