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
