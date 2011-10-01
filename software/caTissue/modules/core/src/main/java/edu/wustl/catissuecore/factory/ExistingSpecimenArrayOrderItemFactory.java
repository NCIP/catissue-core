package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem;


public class ExistingSpecimenArrayOrderItemFactory
		implements
			InstanceFactory<ExistingSpecimenArrayOrderItem>
{

	private static ExistingSpecimenArrayOrderItemFactory existingSpecimenArrayOrderItemFactory;

	protected ExistingSpecimenArrayOrderItemFactory()
	{
		super();
	}

	public static synchronized ExistingSpecimenArrayOrderItemFactory getInstance()
	{
		if(existingSpecimenArrayOrderItemFactory==null){
			existingSpecimenArrayOrderItemFactory = new ExistingSpecimenArrayOrderItemFactory();
		}
		return existingSpecimenArrayOrderItemFactory;
	}

	public ExistingSpecimenArrayOrderItem createClone(ExistingSpecimenArrayOrderItem t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public ExistingSpecimenArrayOrderItem createObject()
	{
		ExistingSpecimenArrayOrderItem item=new ExistingSpecimenArrayOrderItem();
		initDefaultValues(item);
		return item;
	}

	public void initDefaultValues(ExistingSpecimenArrayOrderItem t)
	{
		// TODO Auto-generated method stub

	}

}
