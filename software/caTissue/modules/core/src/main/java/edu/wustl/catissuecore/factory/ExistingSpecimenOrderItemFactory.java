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

import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;


public class ExistingSpecimenOrderItemFactory implements InstanceFactory<ExistingSpecimenOrderItem>
{
	private static ExistingSpecimenOrderItemFactory existingSpecimenOrderItemFactory;

	protected ExistingSpecimenOrderItemFactory()
	{
		super();
	}

	public static synchronized ExistingSpecimenOrderItemFactory getInstance()
	{
		if(existingSpecimenOrderItemFactory==null){
			existingSpecimenOrderItemFactory = new ExistingSpecimenOrderItemFactory();
		}
		return existingSpecimenOrderItemFactory;
	}
	public ExistingSpecimenOrderItem createClone(ExistingSpecimenOrderItem t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public ExistingSpecimenOrderItem createObject()
	{
		ExistingSpecimenOrderItem existingSpecimenOrderItem=new ExistingSpecimenOrderItem();
		initDefaultValues(existingSpecimenOrderItem);
		return existingSpecimenOrderItem;
	}

	public void initDefaultValues(ExistingSpecimenOrderItem t)
	{
		// TODO Auto-generated method stub

	}

}
