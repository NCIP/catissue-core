package edu.wustl.catissuecore.factory;

import java.util.HashSet;

import edu.wustl.catissuecore.domain.SpecimenArrayType;


public class SpecimenArrayTypeFactory implements InstanceFactory<SpecimenArrayType>
{
	private static SpecimenArrayTypeFactory specimenArrayTypeFactoryFactory;

	private SpecimenArrayTypeFactory() {
		super();
	}

	public static synchronized SpecimenArrayTypeFactory getInstance() {
		if(specimenArrayTypeFactoryFactory == null) {
			specimenArrayTypeFactoryFactory = new SpecimenArrayTypeFactory();
		}
		return specimenArrayTypeFactoryFactory;
	}


	public SpecimenArrayType createClone(SpecimenArrayType t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public SpecimenArrayType createObject()
	{
		SpecimenArrayType spArrayType= new SpecimenArrayType();
		initDefaultValues(spArrayType);
		return spArrayType;
	}

	public void initDefaultValues(SpecimenArrayType obj)
	{
		obj.setSpecimenTypeCollection(new HashSet<String>());

	}

}
