
package edu.wustl.catissuecore.action.shippingtracking;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentForm;
import edu.wustl.catissuecore.bizlogic.shippingtracking.ShipmentBizLogic;
import edu.wustl.catissuecore.domain.shippingtracking.Shipment;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;

/**
 * this class implements the action for shipment processing.
 */
public abstract class ProcessShipmentAction extends SecureAction
{

	/**
	 * this method opens the shipment object for edit.
	 * @param request object of HttpServletRequest class.
	 * @param shipment object of Shipment class.
	 */
	protected void openInEdit(HttpServletRequest request, Shipment shipment)
	{
		final ShipmentForm shipmentForm = new ShipmentForm();
		shipmentForm.setAllValues(shipment);
		shipmentForm.setOperation(edu.wustl.catissuecore.util.global.Constants.EDIT);
		request.setAttribute("shipmentForm", shipmentForm);
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
		final String outgoingShipmentId = request
				.getParameter(edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER);
		if (outgoingShipmentId != null)
		{
			final Long shipmentId = Long.parseLong(outgoingShipmentId);
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic bizLogic = factory.getBizLogic(Constants.SHIPMENT_FORM_ID);
			final Shipment shipment = ((ShipmentBizLogic) bizLogic).getShipmentObject(shipmentId);
			if (shipment != null)
			{
				this.openInEdit(request, shipment);
				forwardTo = this.getForwardTo(shipment.getActivityStatus());
			}
		}
		return mapping.findForward(forwardTo);
	}

	/**
	 * abstract method to implement request forward finding.
	 * @param activityStatus to be checked for.
	 * @return mapping to which request is to be forwarded.
	 */
	protected abstract String getForwardTo(String activityStatus);
}
