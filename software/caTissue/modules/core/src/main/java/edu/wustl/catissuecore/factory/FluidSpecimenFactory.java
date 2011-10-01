
package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.FluidSpecimen;

public class FluidSpecimenFactory extends AbstractSpecimenFactory<FluidSpecimen>
{

	private static FluidSpecimenFactory fluidSpecimenFactory;

	public FluidSpecimenFactory()
	{
		super();
	}

	public static synchronized FluidSpecimenFactory getInstance()
	{
		if (fluidSpecimenFactory == null)
		{
			fluidSpecimenFactory = new FluidSpecimenFactory();
		}
		return fluidSpecimenFactory;
	}

	public FluidSpecimen createClone(FluidSpecimen t)
	{
		return null;
	}

	public FluidSpecimen createObject()
	{
		FluidSpecimen fluidSpecimen = new FluidSpecimen();
		initDefaultValues(fluidSpecimen);
		return fluidSpecimen;
	}

}
