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
