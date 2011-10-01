package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.DistributedItem;

public class DistributedItemFactory implements InstanceFactory<DistributedItem>
{
	private static DistributedItemFactory distributedItemFactory;

	private DistributedItemFactory()
	{
		super();
	}

	public static synchronized DistributedItemFactory getInstance()
	{
		if(distributedItemFactory==null){
			distributedItemFactory = new DistributedItemFactory();
		}
		return distributedItemFactory;
	}
	public DistributedItem createClone(DistributedItem obj)
	{
		DistributedItem distributedItem = createObject();
		return distributedItem;
	}
	public DistributedItem createObject()
	{
		DistributedItem distributedItem = new DistributedItem();
		initDefaultValues(distributedItem);
		return distributedItem;
	}
	public void initDefaultValues(DistributedItem obj)
	{
	}
}
