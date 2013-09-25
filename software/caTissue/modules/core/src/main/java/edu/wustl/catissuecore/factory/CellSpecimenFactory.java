/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */


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
