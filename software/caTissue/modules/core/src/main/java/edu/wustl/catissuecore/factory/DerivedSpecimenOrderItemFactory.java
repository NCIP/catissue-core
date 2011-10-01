package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem;


public class DerivedSpecimenOrderItemFactory implements InstanceFactory<DerivedSpecimenOrderItem>
{
	private static DerivedSpecimenOrderItemFactory derivedSpecimenOrderItemFactory;

	protected DerivedSpecimenOrderItemFactory()
	{
		super();
	}

	public static synchronized DerivedSpecimenOrderItemFactory getInstance()
	{
		if(derivedSpecimenOrderItemFactory==null){
			derivedSpecimenOrderItemFactory = new DerivedSpecimenOrderItemFactory();
		}
		return derivedSpecimenOrderItemFactory;
	}
	public DerivedSpecimenOrderItem createClone(DerivedSpecimenOrderItem t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public DerivedSpecimenOrderItem createObject()
	{
		DerivedSpecimenOrderItem derivedSpecimenOrderItem=new DerivedSpecimenOrderItem();
		initDefaultValues(derivedSpecimenOrderItem);
		return derivedSpecimenOrderItem;
	}

	public void initDefaultValues(DerivedSpecimenOrderItem t)
	{
		// TODO Auto-generated method stub

	}

}
