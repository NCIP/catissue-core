package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.pathology.ConceptReferent;


public class ConceptReferentFactory implements InstanceFactory<ConceptReferent>
{
	private static ConceptReferentFactory conceptReferentFactory;

	private ConceptReferentFactory()
	{
		super();
	}

	public static synchronized ConceptReferentFactory getInstance()
	{
		if(conceptReferentFactory==null){
			conceptReferentFactory = new ConceptReferentFactory();
		}
		return conceptReferentFactory;
	}

	public ConceptReferent createClone(ConceptReferent t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public ConceptReferent createObject()
	{
		ConceptReferent cr=new ConceptReferent();
		initDefaultValues(cr);
		return cr;
	}

	public void initDefaultValues(ConceptReferent t)
	{}

}
