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
