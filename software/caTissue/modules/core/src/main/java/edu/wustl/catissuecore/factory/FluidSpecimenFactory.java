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
