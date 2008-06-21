/**
 * <p>Title: CellSpecimen Class>
 * <p>Description:  A biospecimen composed of purified single cells not in the 
 * context of a tissue or other biospecimen fluid.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author virender_mehta
 * @version catissueSuite V1.1
 */
package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.util.logger.Logger;

/**
 * A biospecimen composed of purified single cells not in the 
 * context of a tissue or other biospecimen fluid.
 * @hibernate.subclass name="CellRequirementSpecimen" discriminator-value = "Cell"
 */
public class CellRequirementSpecimen extends RequirementSpecimen implements Serializable
{
	private static final long serialVersionUID = 1232228923230L;
	public CellRequirementSpecimen()
	{
		
	}
	public CellRequirementSpecimen(AbstractActionForm form)
    {
    	setAllValues(form);
    }
	/**
     * This function Copies the data from an NewSpecimenForm object to a CellSpecimen object.
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
            Logger.out.error(excp.getMessage(),excp);
        }
    }
    
    
    public CellRequirementSpecimen(CellRequirementSpecimen cellRequirementSpecimen)
    {
    	//super(cellRequirementSpecimen);
    }
    
    public CellRequirementSpecimen createClone()
    {
    	CellRequirementSpecimen cloneCellRequirementSpecimen = new CellRequirementSpecimen(this);
    	return cloneCellRequirementSpecimen;
    }
}
