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

import edu.wustl.catissuecore.domain.FluidSpecimenRequirement;


public class FluidSpecimenRequirementFactory extends SpecimenRequirementFactory
{
	private static FluidSpecimenRequirementFactory fluidSpecimenRequirement;

	private FluidSpecimenRequirementFactory()
	{
		super();
	}

	public static synchronized FluidSpecimenRequirementFactory getInstance()
	{
		if(fluidSpecimenRequirement==null)
		{
			fluidSpecimenRequirement = new FluidSpecimenRequirementFactory();
		}
		return fluidSpecimenRequirement;
	}

	public FluidSpecimenRequirement createClone(FluidSpecimenRequirement t)
	{
		return null;
	}

	public FluidSpecimenRequirement createObject()
	{
		FluidSpecimenRequirement req=new FluidSpecimenRequirement();
		initDefaultValues(req);
		return req;
	}

	public void initDefaultValues(FluidSpecimenRequirement t)
	{}

}
