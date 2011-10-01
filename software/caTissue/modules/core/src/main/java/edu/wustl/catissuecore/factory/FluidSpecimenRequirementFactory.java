package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.FluidSpecimenRequirement;


public class FluidSpecimenRequirementFactory extends SpecimenRequirementFactory
{
	private static FluidSpecimenRequirementFactory fluidSpecimenRequirement;

	private FluidSpecimenRequirementFactory()
	{
		super();
	}

	public static synchronized FluidSpecimenRequirementFactory getInstance()
	{
		if(fluidSpecimenRequirement==null)
		{
			fluidSpecimenRequirement = new FluidSpecimenRequirementFactory();
		}
		return fluidSpecimenRequirement;
	}

	public FluidSpecimenRequirement createClone(FluidSpecimenRequirement t)
	{
		return null;
	}

	public FluidSpecimenRequirement createObject()
	{
		FluidSpecimenRequirement req=new FluidSpecimenRequirement();
		initDefaultValues(req);
		return req;
	}

	public void initDefaultValues(FluidSpecimenRequirement t)
	{}

}
