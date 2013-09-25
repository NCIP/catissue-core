/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

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
