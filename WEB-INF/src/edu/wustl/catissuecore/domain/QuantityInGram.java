/**
 * <p>Title: QuantityInGram Class>
 * <p>Description:  A class that represents quantity of a specimen in grams.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;


public class QuantityInGram extends Quantity implements Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * Quantity in grams.
	 */
//	protected Double value;
	
	/**
	 * Default Constructor
	 */
	public QuantityInGram()
	{
	}
	
	/**
	 * Parameterized Constructor
	 */
	public QuantityInGram(double value)
	{
		this.value = new Double(value);
	}
	
//	/**
//     * Returns the quantity in grams.
//     * @hibernate.property name="quantity" type="double" 
//	 * column="QUANTITY" length="50"
//     * @return the quantity in grams.
//     * @see #setQuantity(Double)
//     */
//    public Double getQuantity()
//    {
//        return quantity;
//    }
//
//    /**
//     * Sets the quantity in grams.
//     * @param quantity the quantity in grams.
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