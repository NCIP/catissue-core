
package edu.wustl.catissuecore.action.shippingtracking;

import edu.wustl.catissuecore.util.shippingtracking.Constants;

/**
 * this class implements incoming shipment request action.
 */
public class IncomingShipmentRequestAction extends ProcessShipmentRequestsAction
{

	/**
	 * this method gives the mapping to which request is to be forwarded.
	 * @param activityStatus status to be checked.
	 * @param operation to be performed.
	 * @return mapping to which request is to be forwarded.
	 */
	@Override
	protected String getForwardTo(String activityStatus, String operation)
	{
		String forwardTo = edu.wustl.catissuecore.util.global.Constants.FAILURE;
		if (activityStatus != null && !activityStatus.trim().equals("")
				&& activityStatus.equals(Constants.ACTIVITY_STATUS_IN_PROGRESS))
		{
			forwardTo = Constants.VIEW_SHIPMENT_REQUEST;
		}
		else
		{
			forwardTo = Constants.CREATE_SHIPMENT;
		}
		return forwardTo;
	}
}
