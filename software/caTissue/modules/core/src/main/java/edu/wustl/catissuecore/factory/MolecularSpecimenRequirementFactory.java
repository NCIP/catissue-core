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
