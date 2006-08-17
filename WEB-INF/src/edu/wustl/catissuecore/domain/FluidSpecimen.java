/**
 * <p>Title: FluidSpecimen Class>
 * <p>Description:  A single unit of body fluid specimen that is 
 * collected or created from a Participant.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.catissuecore.actionForm.SpecimenForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.util.logger.Logger;

/**
 * A single unit of body fluid specimen that is collected or created from a Participant.
 * @hibernate.subclass name="FluidSpecimen" discriminator-value="Fluid" 
 */
public class FluidSpecimen extends Specimen implements Serializable
{
    private static final long serialVersionUID = 1234567890L;

    /**
     * Initial amount of specimen either 
     * directly collected from participant or created from another specimen.
     */
//    protected Double quantityInMilliliter;

    /**
     * Current available quantity of the specimen.
     */
//    protected Double availableQuantityInMilliliter;

    public FluidSpecimen()
    {
    	
    }
//  Constructor
    public FluidSpecimen(AbstractActionForm form)
    {
//    	super(form);
    	setAllValues(form);
    }
    
    /*
     * Returns the initial amount of specimen either 
     * directly collected from participant or created from another specimen.
     * @hibernate.property name="quantityInMilliliter" type="double" 
	 * column="QUANTITY" length="50"
     * @return the initial amount of specimen either 
     * directly collected from participant or created from another specimen.
     * @see #setQuantityInMiliLiter(Double)
     */
//    public Double getQuantityInMilliliter()
//    {
//        return quantityInMilliliter;
//    }

    /**
     * Sets the initial amount of specimen either 
     * directly collected from participant or created from another specimen. 
     * @param quantityInMiliLiter the initial amount of specimen either 
     * directly collected from participant or created from another specimen.
     * @see #getQuantityInMiliLiter()
     */
//    public void setQuantityInMilliliter(Double quantityInMilliliter)
//    {
//        this.quantityInMilliliter = quantityInMilliliter;
//    }

    /*
     * Returns the current available quantity of the specimen.
     * @hibernate.property name="availableQuantityInMilliliter" type="double" 
	 * column="AVAILABLE_QUANTITY" length="50"
     * @return the current available quantity of the specimen.
     * @see #setAvailableQuantityInMiliLiter(Double)
     */
//    public Double getAvailableQuantityInMilliliter()
//    {
//        return availableQuantityInMilliliter;
//    }

    /**
     * Sets the current available quantity of the specimen. 
     * @param availableQuantityInMiliLiter the current available quantity of the specimen.
     * @see #getAvailableQuantityInMiliLiter()
     */
//    public void setAvailableQuantityInMilliliter(Double availableQuantityInMilliliter)
//    {
//        this.availableQuantityInMilliliter = availableQuantityInMilliliter;
//    }
    
    /**
     * This function Copies the data from an NewSpecimenForm object to a TissueSpecimen object.
     * @param siteForm An SiteForm object containing the information about the site.  
     * */
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
        	super.setAllValues(abstractForm);
        	SpecimenForm form = (SpecimenForm) abstractForm;
        	
//        	this.quantityInMilliliter = new Double(form.getQuantity());
//        	
//        	if(form.isAddOperation())
//        	{
//        		this.availableQuantityInMilliliter = quantityInMilliliter;
//        	}
//        	else
//        	{
//        		this.availableQuantityInMilliliter = new Double(form.getAvailableQuantity());
//        	}
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
}