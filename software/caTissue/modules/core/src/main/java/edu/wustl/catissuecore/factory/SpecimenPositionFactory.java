package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.SpecimenPosition;


public class SpecimenPositionFactory implements InstanceFactory<SpecimenPosition>
{
	private static SpecimenPositionFactory specimenPositionFactory;

	private SpecimenPositionFactory()
	{
		super();
	}

	public static synchronized SpecimenPositionFactory getInstance()
	{
		if(specimenPositionFactory==null)
		{
			specimenPositionFactory = new SpecimenPositionFactory();
		}
		return specimenPositionFactory;
	}

	public SpecimenPosition createClone(SpecimenPosition t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public SpecimenPosition createObject()
	{
		SpecimenPosition specimenPosition=new SpecimenPosition();
		initDefaultValues(specimenPosition);
		return specimenPosition;
	}

	public void initDefaultValues(SpecimenPosition t)
	{
		// TODO Auto-generated method stub

	}

}
