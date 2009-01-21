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
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.util.logger.Logger;

/**
 * A single unit of body fluid specimen that is collected or created from a Participant.
 * @hibernate.subclass name="FluidSpecimen" discriminator-value="Fluid"
 */
public class FluidSpecimen extends Specimen implements Serializable
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(FluidSpecimen.class);

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

    /**
     * Default Constructor.
     */
	public FluidSpecimen()
    {
    	super();
    }

	/**
	 * Parameterized Constructor.
	 * @param form AbstractActionForm.
	 */
    public FluidSpecimen(AbstractActionForm form)
    {
    	super();
    	setAllValues(form);
    }

    /**
     * This function Copies the data from an NewSpecimenForm object to a FluidSpecimen object.
     * @param abstractForm An SiteForm object containing the information about the site.
     * */
    public void setAllValues(IValueObject abstractForm)
    {
        try
        {
        	super.setAllValues(abstractForm);
        }
        catch (Exception excp)
        {
            logger.error(excp.getMessage());
        }
    }

    /**
     * Parameterized Constructor.
     * @param fluidReqSpecimen SpecimenRequirement.
     */
    public FluidSpecimen(SpecimenRequirement fluidReqSpecimen)
    {
    	super(fluidReqSpecimen);
    }
}