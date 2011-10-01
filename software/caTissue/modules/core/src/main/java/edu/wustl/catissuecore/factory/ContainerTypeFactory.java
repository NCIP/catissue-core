package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.ContainerType;

public class ContainerTypeFactory implements InstanceFactory<ContainerType>
{
	private static ContainerTypeFactory containerTypeFactory;

	private ContainerTypeFactory()
	{
		super();
	}

	public static synchronized ContainerTypeFactory getInstance()
	{
		if(containerTypeFactory==null){
			containerTypeFactory = new ContainerTypeFactory();
		}
		return containerTypeFactory;
	}


	public ContainerType createClone(ContainerType obj)
	{
		ContainerType containerType = createObject();
		return containerType;
	}

	public ContainerType createObject()
	{
		ContainerType containerType = new ContainerType();
		initDefaultValues(containerType);
		return containerType;
	}

	public void initDefaultValues(ContainerType obj)
	{

	}
}
