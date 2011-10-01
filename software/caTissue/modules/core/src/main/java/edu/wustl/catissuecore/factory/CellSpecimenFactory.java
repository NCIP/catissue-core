
package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.CellSpecimen;

public class CellSpecimenFactory extends AbstractSpecimenFactory<CellSpecimen>
{

	private static CellSpecimenFactory cellSpecimenFactory;

	public CellSpecimenFactory()
	{
		super();
	}

	public static synchronized CellSpecimenFactory getInstance()
	{
		if (cellSpecimenFactory == null)
		{
			cellSpecimenFactory = new CellSpecimenFactory();
		}
		return cellSpecimenFactory;
	}

	public CellSpecimen createClone(CellSpecimen t)
	{
		return null;
	}

	public CellSpecimen createObject()
	{
		CellSpecimen cellSpecimen = new CellSpecimen();
		initDefaultValues(cellSpecimen);
		return cellSpecimen;
	}

}
