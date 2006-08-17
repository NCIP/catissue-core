/**
 * <p>Title: QuantityInGram Class>
 * <p>Description:  A class that represents quantity of a specimen in grams.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;


///**
// * @author gautam_shetty
// * @hibernate.joined-subclass table="CATISSUE_QUANTITY_IN_GRAM"
// * @hibernate.joined-subclass-key column="IDENTIFIER"
// */
public class QuantityInGram extends Quantity 
{
    
	protected Double value;
	
	public QuantityInGram()
	{
	}
	
//    /**
//     * @return Returns the value.
//     * @hibernate.property name="value" type="double" column="VALUE" length="50"
//     */
//    public Double getValue()
//    {
//        return value;
//    }
//    
//    /**
//     * @param value The value to set.
//     */
//    public void setValue(Double value)
//    {
//        this.value = value;
//    }
    
    /**
     * Overriding toString() method of object class.
     */
    public String toString()
    {
    	return String.valueOf(value);
    }
}