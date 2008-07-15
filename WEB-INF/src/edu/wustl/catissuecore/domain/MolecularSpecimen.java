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

import edu.wustl.catissuecore.actionForm.SpecimenForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.util.logger.Logger;

/**
 * A molecular derivative (I.e. RNA / DNA / Protein Lysate) obtained from a specimen.
 * @hibernate.subclass name="MolecularSpecimen" discriminator-value="Molecular"
 */
public class MolecularSpecimen extends Specimen implements Serializable
{
    private static final long serialVersionUID = 1234567890L;

    /**
     * Concentration of liquid molecular specimen measured in microgram per microlitter.
     */
    protected Double concentrationInMicrogramPerMicroliter;

    public MolecularSpecimen()
	{
    	
	}
    public MolecularSpecimen(AbstractActionForm form)
    {
    	setAllValues(form);
    }
    
    /**
     * Returns the concentration of liquid molecular specimen measured 
     * in microgram per microlitter.
     * @hibernate.property name="concentrationInMicrogramPerMicroliter" type="double" 
     * column="CONCENTRATION" length="50"
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
     * This function Copies the data from an NewSpecimenForm object to a MolecularSpecimen object.
     * @param siteForm An SiteForm object containing the information about the site.  
     * */
    public void setAllValues(IValueObject abstractForm)
    {
        try
        {
        	super.setAllValues(abstractForm);
        	SpecimenForm form = (SpecimenForm) abstractForm;
        	if(!form.getConcentration().equals(""))
        	{
        		this.concentrationInMicrogramPerMicroliter = new Double(form.getConcentration());
        	}
        	else
        	{
        		Logger.out.debug("Concentration is "+form.getConcentration());
        	}
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
    public MolecularSpecimen(SpecimenRequirement molecularReqSpecimen)
    {
    	super(molecularReqSpecimen);
    	this.concentrationInMicrogramPerMicroliter = ((MolecularSpecimenRequirement)molecularReqSpecimen).concentrationInMicrogramPerMicroliter;
    }
}