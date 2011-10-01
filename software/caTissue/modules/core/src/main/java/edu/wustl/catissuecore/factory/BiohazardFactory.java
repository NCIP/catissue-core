
package edu.wustl.catissuecore.factory;

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.Specimen;

public class BiohazardFactory implements InstanceFactory<Biohazard>
{

	private static BiohazardFactory biohazardFactory;

	private BiohazardFactory()
	{
		super();
	}

	public static synchronized BiohazardFactory getInstance()
	{
		if(biohazardFactory==null)
		{
			biohazardFactory = new BiohazardFactory();
		}
		return biohazardFactory;
	}

	public Biohazard createClone(Biohazard obj)
	{

		Biohazard biohazard = createObject();//new Biohazard();
		biohazard.setComment(obj.getComment());
		biohazard.setName(obj.getName());
//<<<<<<< .mine
//		//biohazard.setPersisted(obj.getPersisted());
//=======
//>>>>>>> .r25181
		biohazard.setType(obj.getType());
		return biohazard;
	}


	public void initDefaultValues(Biohazard obj)
	{
		Collection<Specimen> specimenCollection = new HashSet<Specimen>();
		obj.setSpecimenCollection(specimenCollection);
	}

	public Biohazard createObject()
	{
		Biohazard biohazard = new Biohazard();
		initDefaultValues(biohazard);
		return biohazard;
	}
}
