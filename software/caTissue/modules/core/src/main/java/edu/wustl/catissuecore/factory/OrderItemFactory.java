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
