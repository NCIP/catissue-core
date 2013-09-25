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

import edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem;


public class DerivedSpecimenOrderItemFactory implements InstanceFactory<DerivedSpecimenOrderItem>
{
	private static DerivedSpecimenOrderItemFactory derivedSpecimenOrderItemFactory;

	protected DerivedSpecimenOrderItemFactory()
	{
		super();
	}

	public static synchronized DerivedSpecimenOrderItemFactory getInstance()
	{
		if(derivedSpecimenOrderItemFactory==null){
			derivedSpecimenOrderItemFactory = new DerivedSpecimenOrderItemFactory();
		}
		return derivedSpecimenOrderItemFactory;
	}
	public DerivedSpecimenOrderItem createClone(DerivedSpecimenOrderItem t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public DerivedSpecimenOrderItem createObject()
	{
		DerivedSpecimenOrderItem derivedSpecimenOrderItem=new DerivedSpecimenOrderItem();
		initDefaultValues(derivedSpecimenOrderItem);
		return derivedSpecimenOrderItem;
	}

	public void initDefaultValues(DerivedSpecimenOrderItem t)
	{
		// TODO Auto-generated method stub

	}

}
