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
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
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
    protected Double concentrationInMicrogramPerMicroLiter;

    /**
     * Initial amount of specimen created from another specimen.
     */
    protected Double quantityInMicroGram;

    /**
     * Current available quantity of the specimen. 
     * Available mass of the molecular specimen in tissue bank.
     */
    protected Double availableQuantityInMicroGram;

//  Constructor
    public MolecularSpecimen(AbstractActionForm form)
    {
    	super(form);
    	setAllValues(form);
    }
    
    /**
     * Returns the concentration of liquid molecular specimen measured 
     * in microgram per microlitter.
     * @hibernate.property name="concentrationInMicrogramPerMicroLiter" type="double" 
     * column="CONCENTRATION_IN_MICROGRAM_PER_MICROLITER" length="50"
     * @return the concentration of liquid molecular specimen measured 
     * in microgram per microlitter. 
     * directly collected from participant or created from another specimen.
     * @see #setConcentrationInMicrogramPerMicroLiter(Double)
     */
    public Double getConcentrationInMicrogramPerMicroLiter()
    {
        return concentrationInMicrogramPerMicroLiter;
    }

    /**
     * Sets the concentration of liquid molecular specimen measured 
     * in microgram per microlitter.
     * @param concentrationInMicrogramPerMicroLiter the concentration of 
     * liquid molecular specimen measuredin microgram per microlitter.
     * @see #getConcentrationInMicrogramPerMicroLiter()
     */
    public void setConcentrationInMicrogramPerMicroLiter(Double concentrationInMicrogramPerMicroLiter)
    {
        this.concentrationInMicrogramPerMicroLiter = concentrationInMicrogramPerMicroLiter;
    }

    /**
     * Returns the initial amount of specimen created from another specimen.
     * @hibernate.property name="quantityInMicroGram" type="double" 
     * column="QUANTITY_IN_MICROGRAM" length="50"
     * @return the initial amount of specimen created from another specimen.
     * @see #setQuantityInMicroGram(Double)
     */
    public Double getQuantityInMicroGram()
    {
        return quantityInMicroGram;
    }

    /**
     * Sets the initial amount of specimen created from another specimen.
     * @param quantityInMicroGram the initial amount of specimen created from another specimen.
     * @see #getQuantityInMicroGram()
     */
    public void setQuantityInMicroGram(Double quantityInMicroGram)
    {
        this.quantityInMicroGram = quantityInMicroGram;
    }

    /**
     * Returns the current available quantity of the specimen. 
     * Available mass of the molecular specimen in tissue bank.
     * @hibernate.property name="availableQuantityInMicroGram" type="double" 
     * column="AVAILABLE_QUANTITY_IN_MICROGRAM" length="50"
     * @return the current available quantity of the specimen.
     * @see #setAvailableQuantityInMicroGram(Double)
     */
    public Double getAvailableQuantityInMicroGram()
    {
        return availableQuantityInMicroGram;
    }

    /**
     * Sets the current available quantity of the specimen. 
     * Available mass of the molecular specimen in tissue bank.
     * @param availableQuantityInMicroGram the current available 
     * mass of the molecular specimen in tissue bank. 
     * @see #getAvailableQuantityInMicroGram()
     */
    public void setAvailableQuantityInMicroGram(Double availableQuantityInMicroGram)
    {
        this.availableQuantityInMicroGram = availableQuantityInMicroGram;
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
        	NewSpecimenForm form = (NewSpecimenForm) abstractForm;
        	
        	this.quantityInMicroGram = new Double(form.getQuantity());
        	//this.availableQuantityInMicroGram=
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
}