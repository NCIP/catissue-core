package edu.wustl.catissuecore.factory;

import java.util.LinkedHashSet;

import edu.wustl.catissuecore.domain.Aliquot;
import edu.wustl.catissuecore.domain.SpecimenPosition;

public class AliquotFactory implements InstanceFactory<Aliquot>
{
	private static AliquotFactory aliquotFactory;

	private AliquotFactory()
	{
		super();
	}

	public static synchronized AliquotFactory getInstance()
	{
		if(aliquotFactory==null){
			aliquotFactory = new AliquotFactory();
		}
		return aliquotFactory;
	}

	public Aliquot createClone(Aliquot obj)
	{
		Aliquot aliquot = createObject();

		aliquot.setCount(obj.getCount());
		aliquot.setQuantityPerAliquot(obj.getQuantityPerAliquot());
		aliquot.setSpecimen(obj.getSpecimen());
		aliquot.setSpecimenPositionCollection(obj.getSpecimenPositionCollection());
		aliquot.setAliquotsInSameContainer(obj.getAliquotsInSameContainer());

		return aliquot;
	}

	public Aliquot createObject()
	{
		Aliquot aliquot = new Aliquot();
		initDefaultValues(aliquot);
		return aliquot;
	}

	public void initDefaultValues(Aliquot obj)
	{
		obj.setSpecimenPositionCollection(new LinkedHashSet<SpecimenPosition>());
		obj.setAliquotsInSameContainer(Boolean.TRUE);
	}
}
