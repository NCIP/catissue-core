package edu.wustl.catissuecore.factory;

import java.util.HashSet;

import edu.wustl.catissuecore.domain.pathology.Concept;
import edu.wustl.catissuecore.domain.pathology.ConceptReferent;


public class ConceptFactory implements InstanceFactory<Concept>
{
	private static ConceptFactory conceptFactory;

	private ConceptFactory()
	{
		super();
	}

	public static synchronized ConceptFactory getInstance()
	{
		if(conceptFactory==null){
			conceptFactory = new ConceptFactory();
		}
		return conceptFactory;
	}

	public Concept createClone(Concept t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Concept createObject()
	{
		Concept concept=new Concept();
		initDefaultValues(concept);
		return concept;
	}

	public void initDefaultValues(Concept obj)
	{
		obj.setConceptReferentCollection(new HashSet<ConceptReferent>());

	}

}
