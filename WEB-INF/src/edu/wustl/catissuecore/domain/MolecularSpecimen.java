/**
 * <p>Title: MolecularSpecimen Class>
 * <p>Description:  A molecular derivative (I.e. RNA / DNA / Protein Lysate) 
 * obtained from a specimen.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.SpecimenForm;
import edu.wustl.common.util.logger.Logger;

/**
 * A molecular derivative (I.e. RNA / DNA / Protein Lysate) 
 * obtained from a specimen.
 * @hibernate.joined-subclass table="CATISSUE_MOLECULAR_SPECIMEN"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 * @author gautam_shetty
 */
public class MolecularSpecimen extends Specimen implements Serializable
{
    private static final long serialVersionUID = 1234567890L;

    /**
     * Concentration of liquid molecular specimen measured in microgram per microlitter.
     */
    protected Double concentrationInMicrogramPerMicroliter;

    /**
     * Initial amount of specimen created from another specimen.
     */
    protected Double quantityInMicrogram;

    /**
     * Current available quantity of the specimen. 
     * Available mass of the molecular specimen in tissue bank.
     */
    protected Double availableQuantityInMicrogram;

    public MolecularSpecimen()
	{
    	
	}
//  Constructor
    public MolecularSpecimen(AbstractActionForm form)
    {
    	super(form);
    	setAllValues(form);
    }
    
    /**
     * Returns the concentration of liquid molecular specimen measured 
     * in microgram per microlitter.
     * @hibernate.property name="concentrationInMicrogramPerMicroliter" type="double" 
     * column="CONCENTRATION_IN_MICROGRAM_PER_MICROLITER" length="50"
     * @return the concentration of liquid molecular specimen measured 
     * in microgram per microlitter. 
     * directly collected from participant or created from another specimen.
     * @see #setConcentrationInMicrogramPerMicroLiter(Double)
     */
    public Double getConcentrationInMicrogramPerMicroliter()
    {
        return concentrationInMicrogramPerMicroliter;
    }

    /**
     * Sets the concentration of liquid molecular specimen measured 
     * in microgram per microlitter.
     * @param concentrationInMicrogramPerMicroLiter the concentration of 
     * liquid molecular specimen measuredin microgram per microlitter.
     * @see #getConcentrationInMicrogramPerMicroLiter()
     */
    public void setConcentrationInMicrogramPerMicroliter(Double concentrationInMicrogramPerMicroliter)
    {
        this.concentrationInMicrogramPerMicroliter = concentrationInMicrogramPerMicroliter;
    }

    /**
     * Returns the initial amount of specimen created from another specimen.
     * @hibernate.property name="quantityInMicrogram" type="double" 
     * column="QUANTITY_IN_MICROGRAM" length="50"
     * @return the initial amount of specimen created from another specimen.
     * @see #setQuantityInMicroGram(Double)
     */
    public Double getQuantityInMicrogram()
    {
        return quantityInMicrogram;
    }

    /**
     * Sets the initial amount of specimen created from another specimen.
     * @param quantityInMicroGram the initial amount of specimen created from another specimen.
     * @see #getQuantityInMicroGram()
     */
    public void setQuantityInMicrogram(Double quantityInMicrogram)
    {
        this.quantityInMicrogram = quantityInMicrogram;
    }

    /**
     * Returns the current available quantity of the specimen. 
     * Available mass of the molecular specimen in tissue bank.
     * @hibernate.property name="availableQuantityInMicrogram" type="double" 
     * column="AVAILABLE_QUANTITY_IN_MICROGRAM" length="50"
     * @return the current available quantity of the specimen.
     * @see #setAvailableQuantityInMicroGram(Double)
     */
    public Double getAvailableQuantityInMicrogram()
    {
        return availableQuantityInMicrogram;
    }

    /**
     * Sets the current available quantity of the specimen. 
     * Available mass of the molecular specimen in tissue bank.
     * @param availableQuantityInMicroGram the current available 
     * mass of the molecular specimen in tissue bank. 
     * @see #getAvailableQuantityInMicroGram()
     */
    public void setAvailableQuantityInMicrogram(Double availableQuantityInMicrogram)
    {
        this.availableQuantityInMicrogram = availableQuantityInMicrogram;
    }
    
    /**
     * This function Copies the data from an NewSpecimenForm object to a MolecularSpecimen object.
     * @param siteForm An SiteForm object containing the information about the site.  
     * */
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
        	super.setAllValues(abstractForm);
        	SpecimenForm form = (SpecimenForm) abstractForm;
        	
        	this.quantityInMicrogram = new Double(form.getQuantity());
        	this.concentrationInMicrogramPerMicroliter = new Double(form.getConcentration());
        	
        	if(form.isAddOperation())
        	{
        		this.availableQuantityInMicrogram = quantityInMicrogram;
        	}
        	else
        	{
            	this.availableQuantityInMicrogram = new Double(form.getAvailableQuantity());
        	}
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
}