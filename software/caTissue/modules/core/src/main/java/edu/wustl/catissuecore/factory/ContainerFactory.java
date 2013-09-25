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

import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.ContainerPosition;

public class ContainerFactory implements InstanceFactory<Container>
{
	private static ContainerFactory containerFactory;

	protected ContainerFactory()
	{
		super();
	}

	public static synchronized ContainerFactory getInstance()
	{
		if(containerFactory==null){
			containerFactory = new ContainerFactory();
		}
		return containerFactory;
	}


	public Container createClone(Container obj)
	{
		Container container = createObject();
		container.setLocatedAtPosition(obj.getLocatedAtPosition());
		container.setOccupiedPositions(obj.getOccupiedPositions());
		container.setFull(obj.getFull());
		container.setName(obj.getName());
		//set barcode if requiered.
		container.setActivityStatus(obj.getActivityStatus());
		container.setComment(obj.getComment());
		container.setCapacity(obj.getCapacity());

		return container;
	}

	public Container createObject()
	{
		Container container = new Container();
		initDefaultValues(container);
		return container;
	}

	public void initDefaultValues(Container obj)
	{
		Collection<ContainerPosition> occupiedPositions= new HashSet<ContainerPosition>();
		obj.setOccupiedPositions(occupiedPositions);
	}
}
