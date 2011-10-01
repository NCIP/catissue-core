package edu.wustl.catissuecore.factory;

import java.util.HashSet;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.DistributionSpecimenRequirement;


public class DistributionProtocolFactory implements InstanceFactory<DistributionProtocol>
{
	private static DistributionProtocolFactory distributionProtocolFactory;

	protected DistributionProtocolFactory()
	{
		super();
	}

	public static synchronized DistributionProtocolFactory getInstance()
	{
		if(distributionProtocolFactory==null){
			distributionProtocolFactory = new DistributionProtocolFactory();
		}
		return distributionProtocolFactory;
	}
	public DistributionProtocol createClone(DistributionProtocol t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public DistributionProtocol createObject()
	{
		DistributionProtocol dp=new DistributionProtocol();
		initDefaultValues(dp);
		return dp;
	}

	public void initDefaultValues(DistributionProtocol obj)
	{
		obj.setDistributionSpecimenRequirementCollection(new HashSet<DistributionSpecimenRequirement>());
		obj.setCollectionProtocolCollection(new HashSet<CollectionProtocol>());
	}

}
