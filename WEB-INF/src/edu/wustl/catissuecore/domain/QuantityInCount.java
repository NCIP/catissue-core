/**
 * <p>Title: QuantityInMicroGram Class>
 * <p>Description:  A class that represents quantity of a specimen in count.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

///**
// * @author gautam_shetty
// * @hibernate.joined-subclass table="CATISSUE_QUANTITY_IN_COUNT"
// * @hibernate.joined-subclass-key column="IDENTIFIER"
// */
public class QuantityInCount extends Quantity 
{
	protected Integer value;
	
	public QuantityInCount(){

	}
	
//    /**
//     * @return Returns the value.
//     * @hibernate.property name="value" type="int" 
//     * column="VALUE" length="30"
//     */
//    public Integer getValue()
//    {
//        return value;
//    }
//    
//    /**
//     * @param value The value to set.
//     */
//    public void setValue(Integer value)
//    {
//        this.value = value;
//    }
}