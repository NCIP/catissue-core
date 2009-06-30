
package edu.wustl.catissuecore.action.shippingtracking;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentRequestForm;
import edu.wustl.common.util.global.Status;

/**
 * This class implements the action for shipment reject request action.
 */
public class RejectShipmentRequestAction extends IncomingShipmentRequestAction
{

	/**
	 * action method for shipment reject request.
	 * @param mapping object of ActionMapping class.
	 * @param form object of ActionForm class.
	 * @param request object of HttpServletRequest class.
	 * @param response object of HttpServletResponse class.
	 * @return forward mapping.
	 * @throws Exception if some operation fails
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//ActionForward actionForward = super.executeSecureAction(mapping, form, request, response);
		if (request.getAttribute("shipmentRequestForm") != null)
		{
			ShipmentRequestForm shipmentRequestForm = (ShipmentRequestForm) request
					.getAttribute("shipmentRequestForm");
			shipmentRequestForm.setActivityStatus(Status.ACTIVITY_STATUS_REJECT.toString());
			if (form instanceof ShipmentRequestForm)
			{
				form = shipmentRequestForm;
			}
			else
			{
				request.setAttribute("shipmentRequestForm", shipmentRequestForm);
			}
		}
		return mapping.findForward(edu.wustl.catissuecore.util.global.Constants.SUCCESS);
	}
}
