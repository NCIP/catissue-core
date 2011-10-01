/**
 * <p>Title: OutGoingShipmentAction Class.</p>
 * <p>Description:OutGoingShipmentAction contains all the SendingShipment Id.</p>
 * Copyright: Copyright (c) year 2008.
 * @author vijay_chittem .
 * @version 1.00.
 * Created on July 29th 2008.
 */

package edu.wustl.catissuecore.action.shippingtracking;

import edu.wustl.catissuecore.util.shippingtracking.Constants;

/**
 * this class implements outgoing shipment action.
 */
public class OutgoingShipmentAction extends ProcessShipmentAction
{

	/**
	 * this method gives the mapping to which request is to be forwarded.
	 * @param activityStatus status to be checked.
	 * @return mapping to which request is to be forwarded.
	 */
	@Override
	protected String getForwardTo(String activityStatus)
	{
		String forwardTo = edu.wustl.catissuecore.util.global.Constants.FAILURE;
		if (activityStatus != null && !(activityStatus.trim().equals("")))
		{
			forwardTo = this.getForwardToPage(activityStatus);
		}
		//		if(activityStatus!=null
		//				&& !activityStatus.trim().equals("")
		//				&& activityStatus.equals(Constants.ACTIVITY_STATUS_IN_TRANSIT))
		//		{
		//			forwardTo=Constants.EDIT_SHIPMENT;
		//		}
		//		else if(activityStatus!=null
		//				&& !activityStatus.trim().equals("")
		//				&& activityStatus.equals(Constants.ACTIVITY_STATUS_RECEIVED) )
		//		{
		//			forwardTo=Constants.VIEW_SHIPMENT;
		//		}
		return forwardTo;
	}

	/**
	 * gets the page to which control is to be forwarded.
	 * @param activityStatus sctivityStatus of shipment.
	 * @return string containing info of forwarding page.
	 */
	private String getForwardToPage(String activityStatus)
	{
		String forwardPage = edu.wustl.catissuecore.util.global.Constants.FAILURE;
		if (activityStatus.equals(Constants.ACTIVITY_STATUS_IN_TRANSIT))
		{
			forwardPage = Constants.EDIT_SHIPMENT;
		}
		else if (activityStatus.equals(Constants.ACTIVITY_STATUS_RECEIVED))
		{
			forwardPage = Constants.VIEW_SHIPMENT;
		}
		return forwardPage;
	}
}
