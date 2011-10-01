package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.uiobject.OrderUIObject;


public class OrderDetailsFactory implements InstanceFactory<OrderDetails>
{
	// To display the number of Order Items updated.
	public static int numberItemsUpdated = 0;

	private static OrderDetailsFactory orderDetailsFactory;

	protected OrderDetailsFactory()
	{
		super();
	}

	public static synchronized OrderDetailsFactory getInstance()
	{
		if(orderDetailsFactory==null){
			orderDetailsFactory = new OrderDetailsFactory();
		}
		return orderDetailsFactory;
	}

	public OrderDetails createClone(OrderDetails t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public OrderDetails createObject()
	{
		OrderDetails details=new OrderDetails();
		initDefaultValues(details);
		return details;
	}

	public void initDefaultValues(OrderDetails t)
	{
	//	t.setOperationAdd(Boolean.FALSE);
	}

	/**
	* Returns message label to display on success add or edit.
	* @return String object
	*/
	public static String getMessageLabel(OrderDetails orderDetails, OrderUIObject orderUIObject)
	{
		String messageLabel;
		if(orderUIObject.getOperationAdd()==null)
		{
			orderUIObject.setOperationAdd(Boolean.FALSE);
		}
		if (orderUIObject.getOperationAdd().booleanValue())
		{
			messageLabel = orderDetails.getName();
		}
		else
		{
			final int numberItem = numberItemsUpdated;
			messageLabel = " " + numberItem + " OrderItems.";
		}
		return messageLabel;
	}

}
