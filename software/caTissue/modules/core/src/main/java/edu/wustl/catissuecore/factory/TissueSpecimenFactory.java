
package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.TissueSpecimen;

public class TissueSpecimenFactory extends AbstractSpecimenFactory<TissueSpecimen>
{

	private static TissueSpecimenFactory tissueSpecimenFactory;

	public TissueSpecimenFactory()
	{
		super();
	}

	public static synchronized TissueSpecimenFactory getInstance()
	{
		if (tissueSpecimenFactory == null)
		{
			tissueSpecimenFactory = new TissueSpecimenFactory();
		}
		return tissueSpecimenFactory;
	}

	public TissueSpecimen createClone(TissueSpecimen t)
	{
		return null;
	}

	public TissueSpecimen createObject()
	{
		TissueSpecimen tissueSpecimen = new TissueSpecimen();
		initDefaultValues(tissueSpecimen);
		return tissueSpecimen;
	}

}
