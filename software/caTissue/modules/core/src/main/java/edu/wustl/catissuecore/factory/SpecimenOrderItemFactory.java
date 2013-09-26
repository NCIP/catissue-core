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

import edu.wustl.catissuecore.domain.SpecimenOrderItem;


public class SpecimenOrderItemFactory implements InstanceFactory<SpecimenOrderItem>
{
	private static SpecimenOrderItemFactory spOrderItemFactory;

	private SpecimenOrderItemFactory() {
		super();
	}

	public static synchronized SpecimenOrderItemFactory getInstance() {
		if(spOrderItemFactory == null) {
			spOrderItemFactory = new SpecimenOrderItemFactory();
		}
		return spOrderItemFactory;
	}

	public SpecimenOrderItem createClone(SpecimenOrderItem t)
	{
		return null;
	}

	public SpecimenOrderItem createObject()
	{
		SpecimenOrderItem spOrderItem=new SpecimenOrderItem();
		initDefaultValues(spOrderItem);
		return spOrderItem;
	}

	public void initDefaultValues(SpecimenOrderItem t)
	{}

}
