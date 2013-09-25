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

import edu.wustl.catissuecore.domain.OrderItem;


public class OrderItemFactory implements InstanceFactory<OrderItem>
{
	private static OrderItemFactory orderItemFactory;

	protected OrderItemFactory() {
		super();
	}

	public static synchronized OrderItemFactory getInstance() {
		if(orderItemFactory == null) {
			orderItemFactory = new OrderItemFactory();
		}
		return orderItemFactory;
	}

	public OrderItem createClone(OrderItem t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public OrderItem createObject()
	{
		OrderItem orderItem=new OrderItem();
		initDefaultValues(orderItem);
		return orderItem;
	}

	public void initDefaultValues(OrderItem t)
	{}
}
