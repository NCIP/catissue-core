/**
 * <p>Title: CellSpecimen Class>
 * <p>Description:  A biospecimen composed of purified single cells not in the 
 * context of a tissue or other biospecimen fluid.</p>
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
 * A biospecimen composed of purified single cells not in the 
 * context of a tissue or other biospecimen fluid.
 * @hibernate.subclass name="CellSpecimen" discriminator-value = "Cell"
 */
public class CellSpecimen extends Specimen implements Serializable
{
    private static final long serialVersionUID = 1234567890L;

    /**
     * Absolute number of cells contained in the biospecimen
     * at the time of its generation.
     */
//    protected Integer quantityInCellCount;

    /**
     * Absolute number of cells remaining in the biospecimen.
     */
//    protected Integer availableQuantityInCellCount;

    public CellSpecimen()
    {
    	
    }
    
    //Constructor
    public CellSpecimen(AbstractActionForm form)
    {
//    	super(form);
    	setAllValues(form);
    }
    
    /*
     * Returns the absolute number of cells contained in the biospecimen
     * at the time of its generation.
     * @hibernate.property name="quantityInCellCount" type="int" 
     * column="QUANTITY" length="50"
     * @return the absolute number of cells contained in the biospecimen
     * at the time of its generation.
     * @see #setQuantityInCellCount(Integer)
     */
//    public Integer getQuantityInCellCount()
//    {
//        return quantityInCellCount;
//    }

    /**
     * Sets the absolute number of cells contained in the biospecimen.
     * @param quantityInCellCount the absolute number of cells contained in the biospecimen.
     * @see #getQuantityInCellCount()
     */
//    public void setQuantityInCellCount(Integer quantityInCellCount)
//    {
//        this.quantityInCellCount = quantityInCellCount;
//    }

    /*
     * Returns the absolute number of cells remaining in the biospecimen.
     * @hibernate.property name="availableQuantityInCellCount" type="int" 
     * column="AVAILABLE_QUANTITY" length="50"
     * @return the absolute number of cells remaining in the biospecimen.
     * @see #setAvailableQuantityInCellCount(Integer)
     */
//    public Integer getAvailableQuantityInCellCount()
//    {
//        return availableQuantityInCellCount;
//    }

    /**
     * Sets the absolute number of cells remaining in the biospecimen.
     * @param availableQuantityInCellCount the absolute number of cells remaining in the biospecimen.
     * @see #getAvailableQuantityInCellCount()
     */
//    public void setAvailableQuantityInCellCount(Integer availableQuantityInCellCount)
//    {
//        this.availableQuantityInCellCount = availableQuantityInCellCount;
//    }
    
    /**
     * This function Copies the data from an NewSpecimenForm object to a CellSpecimen object.
     * @param siteForm An SiteForm object containing the information about the site.  
     * */
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
        	super.setAllValues(abstractForm);
        	SpecimenForm form = (SpecimenForm) abstractForm;
        	
//        	this.quantityInCellCount = new Integer(form.getQuantity());
        	
        	if(form.isAddOperation())
        	{
//        		this.availableQuantityInCellCount = quantityInCellCount;
        	}
        	else
        	{
//        		this.availableQuantityInCellCount = new Integer(form.getAvailableQuantity());
        	}
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage(),excp);
        }
    }
    
}