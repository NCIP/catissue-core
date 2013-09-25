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

import edu.wustl.catissuecore.domain.Capacity;

public class CapacityFactory implements InstanceFactory<Capacity>
{
	private static CapacityFactory capacityFactory;

	private CapacityFactory()
	{
		super();
	}

	public static synchronized CapacityFactory getInstance()
	{
		if(capacityFactory==null)
		{
			capacityFactory = new CapacityFactory();
		}
		return capacityFactory;
	}

	public Capacity createClone(Capacity obj)
	{
		Capacity  capacity = createObject();
		capacity.setOneDimensionCapacity(obj.getOneDimensionCapacity());
		capacity.setTwoDimensionCapacity(obj.getTwoDimensionCapacity());
		return capacity;

	}

	public Capacity createObject()
	{
		Capacity capacity = new Capacity();
		initDefaultValues(capacity);
		return capacity;
	}

	public void initDefaultValues(Capacity obj)
	{

	}

}
