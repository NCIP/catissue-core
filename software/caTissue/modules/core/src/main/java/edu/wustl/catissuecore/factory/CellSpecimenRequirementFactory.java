package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.CellSpecimenRequirement;


public class CellSpecimenRequirementFactory extends SpecimenRequirementFactory
{
	private static CellSpecimenRequirementFactory cellSpecimenRequirement;

	private CellSpecimenRequirementFactory()
	{
		super();
	}

	public static synchronized CellSpecimenRequirementFactory getInstance()
	{
		if(cellSpecimenRequirement==null)
		{
			cellSpecimenRequirement = new CellSpecimenRequirementFactory();
		}
		return cellSpecimenRequirement;
	}

	public CellSpecimenRequirement createClone(CellSpecimenRequirement t)
	{
		return null;
	}

	public CellSpecimenRequirement createObject()
	{
		CellSpecimenRequirement req=new CellSpecimenRequirement();
		initDefaultValues(req);
		return req;
	}

	public void initDefaultValues(CellSpecimenRequirement t)
	{}

}
