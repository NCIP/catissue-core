package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.MolecularSpecimenRequirement;

public class MolecularSpecimenRequirementFactory extends SpecimenRequirementFactory
{
	private static MolecularSpecimenRequirementFactory molecularSpecimenRequirement;

	private MolecularSpecimenRequirementFactory()
	{
		super();
	}

	public static synchronized MolecularSpecimenRequirementFactory getInstance()
	{
		if(molecularSpecimenRequirement==null)
		{
			molecularSpecimenRequirement = new MolecularSpecimenRequirementFactory();
		}
		return molecularSpecimenRequirement;
	}

	public MolecularSpecimenRequirement createClone(MolecularSpecimenRequirement t)
	{
		return null;
	}

	public MolecularSpecimenRequirement createObject()
	{
		MolecularSpecimenRequirement req=new MolecularSpecimenRequirement();
		initDefaultValues(req);
		return req;
	}

	public void initDefaultValues(MolecularSpecimenRequirement t)
	{}

}
