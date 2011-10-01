package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.SpecimenRequirement;


public class SpecimenRequirementFactory implements InstanceFactory<SpecimenRequirement>
{
	private static SpecimenRequirementFactory specimenRequirementFactory;

	protected SpecimenRequirementFactory()
	{
		super();
	}

	public static synchronized SpecimenRequirementFactory getInstance()
	{
		if(specimenRequirementFactory==null)
		{
			specimenRequirementFactory = new SpecimenRequirementFactory();
		}
		return specimenRequirementFactory;
	}

	public SpecimenRequirement createClone(SpecimenRequirement t)
	{
		return null;
	}

	public SpecimenRequirement createObject()
	{
		SpecimenRequirement req=new SpecimenRequirement();
		initDefaultValues(req);
		return req;
	}

	public void initDefaultValues(SpecimenRequirement t)
	{}

}
