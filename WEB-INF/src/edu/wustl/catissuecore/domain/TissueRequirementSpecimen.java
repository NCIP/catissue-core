/**
 * <p>Title: TissueSpecimen Class>
 * <p>Description:  A single unit of tissue specimen 
 * that is collected or created from a Participant.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author virender_mehta
 * @version caTissueSuite V1.1
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.util.logger.Logger;

/**
 * A single unit of tissue specimen 
 * that is collected or created from a Participant.
 * @hibernate.subclass name="TissueRequirementSpecimen" discriminator-value="Tissue"
 */
public class TissueRequirementSpecimen extends RequirementSpecimen implements Serializable
{
	private static final long serialVersionUID = 12345673333230L;
	public TissueRequirementSpecimen()
	{
		
	}
	public TissueRequirementSpecimen(AbstractActionForm form)
    {
    	setAllValues(form);
    }
	/**
     * This function Copies the data from an NewSpecimenForm object to a TissueSpecimen object.
     * @param siteForm An SiteForm object containing the information about the site.  
     * */
    public void setAllValues(IValueObject abstractForm)
    {
        try
        {
        	super.setAllValues(abstractForm);
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
    public TissueRequirementSpecimen(TissueRequirementSpecimen tissueRequirementSpecimen)
    {
    	//super(tissueRequirementSpecimen);
    }
    
    public TissueRequirementSpecimen createClone()
    {
    	TissueRequirementSpecimen cloneTissueRequirementSpecimen = new TissueRequirementSpecimen(this);
    	return cloneTissueRequirementSpecimen;
    }
}
