/**
 * <p>Title: FluidRequirementSpecimen Class>
 * <p>Description:  A single unit of body fluid specimen that is
 * collected or created from a Participant.</p>
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
 * A single unit of body fluid specimen that is collected or created from a Participant.
 * @hibernate.subclass name="FluidRequirementSpecimen" discriminator-value="Fluid"
 */
public class FluidSpecimenRequirement extends SpecimenRequirement implements Serializable
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(FluidSpecimenRequirement.class);

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 12345678923230L;

	/**
	 * Default Constructor.
	 */
	public FluidSpecimenRequirement()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param form AbstractActionForm.
	 */
	public FluidSpecimenRequirement(AbstractActionForm form)
	{
		super();
		setAllValues(form);
	}

	/**
	 * This function Copies the data from an NewSpecimenForm object to a TissueSpecimen object.
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
	 * @param fluidRequirementSpecimen FluidSpecimenRequirement.
	 */
	public FluidSpecimenRequirement(FluidSpecimenRequirement fluidRequirementSpecimen)
	{
		super();
		//super(fluidRequirementSpecimen);
	}

	/**
	 * Create Clone of FluidSpecimenRequirement.
	 * @return FluidSpecimenRequirement object.
	 */
	public FluidSpecimenRequirement createClone()
	{
		FluidSpecimenRequirement cloneFluidRequirementSpecimen = new FluidSpecimenRequirement(this);
		return cloneFluidRequirementSpecimen;
	}
}