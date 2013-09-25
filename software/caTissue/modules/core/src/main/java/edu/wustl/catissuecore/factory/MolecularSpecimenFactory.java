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

import edu.wustl.catissuecore.domain.MolecularSpecimen;

public class MolecularSpecimenFactory extends AbstractSpecimenFactory<MolecularSpecimen>
{

	private static MolecularSpecimenFactory molecularSpecimenFactory;

	public MolecularSpecimenFactory()
	{
		super();
	}

	public static synchronized MolecularSpecimenFactory getInstance()
	{
		if (molecularSpecimenFactory == null)
		{
			molecularSpecimenFactory = new MolecularSpecimenFactory();
		}
		return molecularSpecimenFactory;
	}

	public MolecularSpecimen createClone(MolecularSpecimen t)
	{
		return null;
	}

	public MolecularSpecimen createObject()
	{
		MolecularSpecimen molecularSpecimen = new MolecularSpecimen();
		initDefaultValues(molecularSpecimen);
		return molecularSpecimen;
	}

}
