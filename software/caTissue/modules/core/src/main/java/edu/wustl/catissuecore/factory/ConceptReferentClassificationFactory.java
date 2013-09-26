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

import edu.wustl.catissuecore.domain.pathology.ConceptReferentClassification;


public class ConceptReferentClassificationFactory
		implements
			InstanceFactory<ConceptReferentClassification>
{
	private static ConceptReferentClassificationFactory crClassificationFactory;

	private ConceptReferentClassificationFactory()
	{
		super();
	}

	public static synchronized ConceptReferentClassificationFactory getInstance()
	{
		if(crClassificationFactory==null)
		{
			crClassificationFactory = new ConceptReferentClassificationFactory();
		}
		return crClassificationFactory;
	}


	public ConceptReferentClassification createClone(ConceptReferentClassification t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public ConceptReferentClassification createObject()
	{
		ConceptReferentClassification crClassification=new ConceptReferentClassification();
		initDefaultValues(crClassification);
		return crClassification;
	}

	public void initDefaultValues(ConceptReferentClassification t)
	{
		// TODO Auto-generated method stub

	}

}
