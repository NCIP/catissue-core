package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem;


public class NewSpecimenArrayOrderItemFactory implements InstanceFactory<NewSpecimenArrayOrderItem>
{
	private static NewSpecimenArrayOrderItemFactory newSpecimenArrayOrderItemFactory;

	protected NewSpecimenArrayOrderItemFactory()
	{
		super();
	}

	public static synchronized NewSpecimenArrayOrderItemFactory getInstance()
	{
		if(newSpecimenArrayOrderItemFactory==null){
			newSpecimenArrayOrderItemFactory = new NewSpecimenArrayOrderItemFactory();
		}
		return newSpecimenArrayOrderItemFactory;
	}
	public NewSpecimenArrayOrderItem createClone(NewSpecimenArrayOrderItem t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public NewSpecimenArrayOrderItem createObject()
	{
		NewSpecimenArrayOrderItem item =new NewSpecimenArrayOrderItem();
		initDefaultValues(item);
		return item;
	}

	public void initDefaultValues(NewSpecimenArrayOrderItem t)
	{
		// TODO Auto-generated method stub

	}

}
