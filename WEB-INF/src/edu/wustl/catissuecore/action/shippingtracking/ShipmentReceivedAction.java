/**
 * <p>Title: ShipmentReceivedAction Class.</p>
 * <p>Description:ShipmentReceivedAction contains all the ShipmentReceivedId.</p>
 * Copyright: Copyright (c) year 2008.
 * Company: 
 * @author vijay_chittem .
 * @version 1.00.
 * Created on July 29th 2008.
 */

package edu.wustl.catissuecore.action.shippingtracking;

import edu.wustl.catissuecore.util.shippingtracking.Constants;

/**
 * this class implements the shipment received action by extending ProcessShipmentAction class.
 */
public class ShipmentReceivedAction extends ProcessShipmentAction
{

	/**
	 * abstract method to find the forward mapping.
	 * @param activityStatus status to be checked for.
	 * @return mapping to which request is to be forwarded.
	 */
	protected String getForwardTo(String activityStatus)
	{
		String forwardTo = edu.wustl.catissuecore.util.global.Constants.FAILURE;
		if (activityStatus != null && !activityStatus.trim().equals("")
				&& activityStatus.equals(Constants.ACTIVITY_STATUS_IN_TRANSIT))
		{
			forwardTo = Constants.VIEW_SHIPMENT;
		}
		else if (activityStatus != null && !activityStatus.trim().equals("")
				&& activityStatus.equals(Constants.ACTIVITY_STATUS_RECEIVED))
		{
			forwardTo = Constants.ACCEPT_OR_REJECT_SHIPMENT;
		}
		return null;
	}

}
