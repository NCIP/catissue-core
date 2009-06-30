
package edu.wustl.catissuecore.action.shippingtracking;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentForm;
import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentRequestForm;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;

/**
 * this class implements the shipment form request action.
 */
public class ShipmentFromRequestAction extends SecureAction
{

	/**
	 * action method for shipment form request.
	 * @param mapping object of ActionMapping class.
	 * @param form object of ActionForm class.
	 * @param request object of HttpServletRequest class.
	 * @param response object of HttpServletResponse class.
	 * @return forward mapping.
	 * @throws Exception if some operation fails.
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Object obj = request.getAttribute("shipmentRequestForm");
		if (obj != null && obj instanceof ShipmentRequestForm)
		{
			ShipmentRequestForm requestForm = (ShipmentRequestForm) obj;
			ShipmentForm shipmentForm = createShipmentForm(requestForm);
			Long loggedInUserId = 0l;
			if (request.getSession().getAttribute(
					edu.wustl.catissuecore.util.global.Constants.SESSION_DATA) != null)
			{
				loggedInUserId = ((SessionDataBean) request.getSession().getAttribute(
						edu.wustl.catissuecore.util.global.Constants.SESSION_DATA)).getUserId();
			}
			shipmentForm.setSenderContactId(loggedInUserId);
			shipmentForm.setShipmentRequestId(requestForm.getId());
			request.setAttribute("shipmentForm", shipmentForm);
			requestForm.setActivityStatus(Constants.ACTIVITY_STATUS_PROCESSED);
			requestForm.setOperation(edu.wustl.catissuecore.util.global.Constants.EDIT);
			request.setAttribute("shipmentRequestForm", requestForm);
		}
		return mapping.findForward(edu.wustl.catissuecore.util.global.Constants.SUCCESS);
	}

	/**
	 * this method creates the shipment for by processing the requestForm.
	 * @param requestForm the imput form to be processed.
	 * @return ShipmentForm.
	 */
	private ShipmentForm createShipmentForm(ShipmentRequestForm requestForm)
	{
		ShipmentForm shipmentForm = new ShipmentForm();
		shipmentForm.setSpecimenCounter(requestForm.getSpecimenCounter());
		shipmentForm.setSpecimenDetailsMap(requestForm.getSpecimenDetailsMap());
		shipmentForm.setSpecimenLabelChoice("SpecimenLabel");
		shipmentForm.setContainerCounter(requestForm.getContainerCounter());
		shipmentForm.setContainerDetailsMap(requestForm.getContainerDetailsMap());
		shipmentForm.setContainerLabelChoice("ContainerLabel");
		shipmentForm.setSenderSiteId(requestForm.getReceiverSiteId());
		shipmentForm.setReceiverSiteId(requestForm.getSenderSiteId());
		shipmentForm.setReceiverContactId(requestForm.getSenderContactId());
		return shipmentForm;
	}
}
