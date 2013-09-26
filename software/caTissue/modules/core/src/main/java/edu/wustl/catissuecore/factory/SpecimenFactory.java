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
