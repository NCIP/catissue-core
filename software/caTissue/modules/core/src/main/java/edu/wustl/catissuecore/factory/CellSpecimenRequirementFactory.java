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
