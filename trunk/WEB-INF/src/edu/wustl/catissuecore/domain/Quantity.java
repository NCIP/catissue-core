/**
 * <p>Title: Quantity Class>
 * <p>Description:  An abstract class that represents quantity of a specimen.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/*
 * @hibernate.class table="CATISSUE_QUANTITY"
 */
public class Quantity extends AbstractDomainObject implements Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
     * System generated unique id.
     */
    protected Long id;
    
    /**
	 * Quantity in mili-liters.
	 */
	protected Double value;
    
	public Quantity()
	{
	}
	
	public Quantity(String quantity)
	{
		this.value = new Double(quantity);
	}
	
	public Quantity(Quantity quantity)
	{
		this.value = new Double(quantity.getValue().doubleValue());
	}
	
    /*
     * Returns the system generated unique id.
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30" 
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_QUANTITY_SEQ"
     * @return the system generated unique id.
     * @see #setId(Long)
     */
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
     * Returns the quantity in grams.
     * @hibernate.property name="value" type="double" 
	 * column="QUANTITY" length="50"
     * @return the quantity in grams.
     * @see #setValue(Double)
     */
    public Double getValue()
    {
        return value;
    }

    /**
     * Sets the quantity in grams.
     * @param quantity the quantity in grams.
     * @see #getValue()
     */
    public void setValue(Double quantity)
    {
        this.value = quantity;
    }
    
    /**
     * Overrides the method of parent class.  
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {
    	
    }
    
    /**
     * Overriding toString() method of object class.
     */
    public String toString()
    {
    	return String.valueOf(value);
    }
    
}