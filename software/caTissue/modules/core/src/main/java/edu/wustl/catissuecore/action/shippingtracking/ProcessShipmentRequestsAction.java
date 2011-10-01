
package edu.wustl.catissuecore.action.shippingtracking;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentRequestForm;
import edu.wustl.catissuecore.bizlogic.shippingtracking.ShipmentRequestBizLogic;
import edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;

/**
 * this class implements the action for processing the shipment requests.
 */
public abstract class ProcessShipmentRequestsAction extends SecureAction
{

	/**
	 * this method populates the UI bean.
	 * @param request HttpServletRequest object.
	 * @param shipmentRequest containing the shipment request.
	 */
	protected void populateUIBean(HttpServletRequest request, ShipmentRequest shipmentRequest)
	{
		final ShipmentRequestForm shipmentRequestForm = new ShipmentRequestForm();
		shipmentRequestForm.setAllValues(shipmentRequest);
		shipmentRequestForm.setOperation(edu.wustl.catissuecore.util.global.Constants.EDIT);
		request.setAttribute("shipmentRequestForm", shipmentRequestForm);
	}

	/**
	 * implements the action.
	 * @param mapping object of ActionMapping class.
	 * @param form object of ActionForm class.
	 * @param request object of HttpServletRequest class.
	 * @param response object of HttpServletResponse class.
	 * @return forward mapping.
	 * @throws Exception if some problem occurs.
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String forwardTo = "";
		//Getting the ShipmentId parameter from the form
		String requestIdString = request
				.getParameter(edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER);
		if (requestIdString == null || requestIdString.trim().equals("")
				|| requestIdString.equals("0"))
		{
			requestIdString = ((Long) request
					.getAttribute(edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER))
					.toString();
		}
		final String operation = request
				.getParameter(edu.wustl.catissuecore.util.global.Constants.OPERATION);
		if (requestIdString != null)
		{
			final Long requestId = Long.parseLong(requestIdString);
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic bizLogic = factory.getBizLogic(Constants.SHIPMENT_REQUEST_FORM_ID);
			final ShipmentRequest shipmentRequest = ((ShipmentRequestBizLogic) bizLogic)
					.getShipmentRequestObject(requestId);
			if (shipmentRequest != null)
			{
				this.populateUIBean(request, shipmentRequest);
				forwardTo = this.getForwardTo(shipmentRequest.getActivityStatus(), operation);
			}
		}
		return mapping.findForward(forwardTo);
	}

	/**
	 * creates the shipment form.
	 * @param shipmentForm the ShipmentRequestForm.
	 * @return Shipment form.
	 */
	/*private ShipmentForm createShipmentForm(ShipmentRequestForm shipmentForm)
	{
		ShipmentForm form = new ShipmentForm();
		form.setReceiverSiteId(shipmentForm.getSenderSiteId());
		form.setSenderSiteId(shipmentForm.getReceiverSiteId());
		form.setReceiverContactId(shipmentForm.getSenderContactId());
		form.setSenderContactId(shipmentForm.getReceiverContactId());
		form.setSpecimenLabelChoice("SpecimenLabel");
		form.setContainerLabelChoice("ContainerLabel");
		form.setSpecimenDetailsMap(shipmentForm.getSpecimenDetailsMap());
		form.setSpecimenCounter(shipmentForm.getSpecimenCounter());
		form.setContainerDetailsMap(shipmentForm.getContainerDetailsMap());
		form.setContainerCounter(shipmentForm.getContainerCounter());
		return form;
	}*/

	/**
	 * abstract method to find the forward mapping.
	 * @param activityStatus status to be checked for.
	 * @param operation to be performed.
	 * @return mapping to which request is to be forwarded.
	 */
	protected abstract String getForwardTo(String activityStatus, String operation);
}
