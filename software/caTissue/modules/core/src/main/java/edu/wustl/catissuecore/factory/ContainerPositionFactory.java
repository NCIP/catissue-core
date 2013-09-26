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

import edu.wustl.catissuecore.domain.ContainerPosition;

public class ContainerPositionFactory implements InstanceFactory<ContainerPosition>
{
	private static ContainerPositionFactory containerPositionFactory;

	private ContainerPositionFactory()
	{
		super();
	}

	public static synchronized ContainerPositionFactory getInstance()
	{
		if(containerPositionFactory==null){
			containerPositionFactory = new ContainerPositionFactory();
		}
		return containerPositionFactory;
	}

	public ContainerPosition createClone(ContainerPosition obj)
	{
		ContainerPosition containerPosition = createObject();
		return containerPosition;
	}

	public ContainerPosition createObject()
	{
		ContainerPosition containerPosition =  new ContainerPosition();
		initDefaultValues(containerPosition);
		return containerPosition;
	}

	public void initDefaultValues(ContainerPosition obj)
	{
	}


}
