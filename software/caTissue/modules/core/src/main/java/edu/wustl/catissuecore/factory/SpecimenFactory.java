
package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.Specimen;

public class SpecimenFactory extends AbstractSpecimenFactory<Specimen>
{

	private static SpecimenFactory specimenFactory;

	protected SpecimenFactory()
	{
		super();
	}

	public static synchronized SpecimenFactory getInstance()
	{
		if (specimenFactory == null)
		{
			specimenFactory = new SpecimenFactory();
		}
		return specimenFactory;
	}

	public Specimen createClone(Specimen t)
	{
		return null;
	}

	public Specimen createObject()
	{
		Specimen specimen = new Specimen();
		initDefaultValues(specimen);
		return specimen;
	}

}
