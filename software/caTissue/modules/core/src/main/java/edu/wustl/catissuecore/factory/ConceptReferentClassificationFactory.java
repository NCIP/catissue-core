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
