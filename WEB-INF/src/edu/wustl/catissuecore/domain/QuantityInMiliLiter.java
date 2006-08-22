/**
 * <p>Title: QuantityInMiliLiter Class>
 * <p>Description:  A class that represents quantity of a specimen in mili-liters.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

/*
 * @hibernate.subclass name="QuantityInMiliLiter"
 */
public class QuantityInMiliLiter extends Quantity implements Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * Quantity in mili-liters.
	 */
//	protected Double value;
	
	/**
	 * Default Constructor
	 */
	public QuantityInMiliLiter()
	{
	}
	
	/**
	 * Parameterized Constructor
	 */
	public QuantityInMiliLiter(double value)
	{
		this.value = new Double(value); 
	}
	
//	/**
//     * Returns the quantity in mili-liters.
//     * @hibernate.property name="value" type="double" 
//	 * column="QUANTITY" length="50"
//     * @return the quantity in mili-liters.
//     * @see #setQuantity(Double)
//     */
//    public Double getQuantity()
//    {
//        return quantity;
//    }
//
//    /**
//     * Sets the quantity in mili-liters.
//     * @param quantity the qauntity in mili-liters.
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