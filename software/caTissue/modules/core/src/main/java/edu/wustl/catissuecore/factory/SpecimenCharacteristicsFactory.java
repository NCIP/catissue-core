package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.SpecimenCharacteristics;


public class SpecimenCharacteristicsFactory implements InstanceFactory<SpecimenCharacteristics>
{
	private static SpecimenCharacteristicsFactory specimenCharacteristicsFactory;

	private SpecimenCharacteristicsFactory() {
		super();
	}

	public static synchronized SpecimenCharacteristicsFactory getInstance() {
		if(specimenCharacteristicsFactory == null) {
			specimenCharacteristicsFactory = new SpecimenCharacteristicsFactory();
		}
		return specimenCharacteristicsFactory;
	}

	public SpecimenCharacteristics createClone(SpecimenCharacteristics obj)
	{
		SpecimenCharacteristics specimenCharacteristics=createObject();
		specimenCharacteristics.setTissueSide(obj.getTissueSide());
		specimenCharacteristics.setTissueSite(obj.getTissueSite());
		return specimenCharacteristics;
	}

	public SpecimenCharacteristics createObject()
	{
		SpecimenCharacteristics specimenCharacteristics=new SpecimenCharacteristics();
		initDefaultValues(specimenCharacteristics);
		return specimenCharacteristics;
	}

	public void initDefaultValues(SpecimenCharacteristics t)
	{}

}
