package edu.wustl.catissuecore.factory;

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;

public class DistributionFactory implements InstanceFactory<Distribution>
{
	private static DistributionFactory distributionFactory;

	private DistributionFactory()
	{
		super();
	}

	public static synchronized DistributionFactory getInstance()
	{
		if(distributionFactory==null){
			distributionFactory = new DistributionFactory();
		}
		return distributionFactory;
	}

	public Distribution createClone(Distribution obj)
	{
		Distribution distribution = createObject();
		return distribution;
	}


	public Distribution createObject()
	{
		Distribution distribution = new Distribution();
		initDefaultValues(distribution);
		return distribution;
	}

	public void initDefaultValues(Distribution obj)
	{
		Collection<DistributedItem> distributedItemCollection = new HashSet();
	}

}
