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
