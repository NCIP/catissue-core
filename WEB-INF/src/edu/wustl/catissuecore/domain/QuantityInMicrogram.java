/**
 * <p>Title: QuantityInMicrogram Class>
 * <p>Description:  A class that represents quantity of a specimen in micro-grams.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

/*
 * @hibernate.subclass name="QuantityInMicrogram"
 */
public class QuantityInMicrogram extends Quantity implements Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * Quantity in micro-grams.
	 */
//	protected Double value;
	
	/**
	 * Default Constructor
	 */
	public QuantityInMicrogram()
	{
	}
	
	/**
	 * Parameterized Constructor
	 */
	public QuantityInMicrogram(double value)
	{
		this.value = new Double(value);
	}
	
//	/**
//     * Returns the qauntity in micro-grams.
//     * @hibernate.property name="value" type="double" 
//	 * column="QUANTITY" length="50"
//     * @return the quantity in micro-grams.
//     * @see #setQuantity(Double)
//     */
//    public Double getQuantity()
//    {
//        return quantity;
//    }
//
//    /**
//     * Sets the qauntity in micro-grams.
//     * @param quantity the qauntity in micro-grams.
//     * @see #getQuantity()
//     */
//    public void setQuantity(Double quantity)
//    {
//        this.quantity = quantity;
//    }
    
    /**
     * Overriding toString() method of object class.
     */
    public String toString()
    {
    	return String.valueOf(value);
    }
}