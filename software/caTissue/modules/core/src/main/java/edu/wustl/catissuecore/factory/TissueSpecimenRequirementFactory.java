package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.TissueSpecimenRequirement;


public class TissueSpecimenRequirementFactory extends SpecimenRequirementFactory
{
	private static TissueSpecimenRequirementFactory tissueSpecimenRequirement;

	private TissueSpecimenRequirementFactory()
	{
		super();
	}

	public static synchronized TissueSpecimenRequirementFactory getInstance()
	{
		if(tissueSpecimenRequirement==null)
		{
			tissueSpecimenRequirement = new TissueSpecimenRequirementFactory();
		}
		return tissueSpecimenRequirement;
	}

	public TissueSpecimenRequirement createClone(TissueSpecimenRequirement t)
	{
		return null;
	}

	public TissueSpecimenRequirement createObject()
	{
		TissueSpecimenRequirement req=new TissueSpecimenRequirement();
		initDefaultValues(req);
		return req;
	}

	public void initDefaultValues(TissueSpecimenRequirement t)
	{}

}
