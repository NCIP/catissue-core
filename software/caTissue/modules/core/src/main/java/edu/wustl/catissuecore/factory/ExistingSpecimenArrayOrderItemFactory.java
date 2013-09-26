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

import edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem;


public class ExistingSpecimenArrayOrderItemFactory
		implements
			InstanceFactory<ExistingSpecimenArrayOrderItem>
{

	private static ExistingSpecimenArrayOrderItemFactory existingSpecimenArrayOrderItemFactory;

	protected ExistingSpecimenArrayOrderItemFactory()
	{
		super();
	}

	public static synchronized ExistingSpecimenArrayOrderItemFactory getInstance()
	{
		if(existingSpecimenArrayOrderItemFactory==null){
			existingSpecimenArrayOrderItemFactory = new ExistingSpecimenArrayOrderItemFactory();
		}
		return existingSpecimenArrayOrderItemFactory;
	}

	public ExistingSpecimenArrayOrderItem createClone(ExistingSpecimenArrayOrderItem t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public ExistingSpecimenArrayOrderItem createObject()
	{
		ExistingSpecimenArrayOrderItem item=new ExistingSpecimenArrayOrderItem();
		initDefaultValues(item);
		return item;
	}

	public void initDefaultValues(ExistingSpecimenArrayOrderItem t)
	{
		// TODO Auto-generated method stub

	}

}
