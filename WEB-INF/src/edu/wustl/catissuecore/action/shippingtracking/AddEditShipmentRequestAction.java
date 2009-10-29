
package edu.wustl.catissuecore.action.shippingtracking;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.bizlogic.shippingtracking.ShipmentRequestBizLogic;
import edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;

/**
 * this class implements shipment add edit action.
 */
public class AddEditShipmentRequestAction extends SecureAction
{
	/**
	 * Logger object.
	 */
	private Logger logger = 
		Logger.getCommonLogger(AddEditShipmentRequestAction.class);
	/**
	 * action method for shipment add/edit.
	 * @param mapping object of ActionMapping class.
	 * @param form object of ActionForm class.
	 * @param request object of HttpServletRequest class.
	 * @param response object of HttpServletResponse class.
	 * @return forward mapping.
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		String target = edu.wustl.common.util.global.Constants.SUCCESS;
		String operation = request.getParameter(edu.wustl.common.util.global.Constants.OPERATION);
		if (operation == null || operation.equals(""))
		{
			operation = (String) request
					.getAttribute(edu.wustl.common.util.global.Constants.OPERATION);
		}
		if (operation == null || operation.equals(""))
		{
			operation = ((AbstractActionForm) form).getOperation();
		}
		request.setAttribute(edu.wustl.common.util.global.Constants.OPERATION, operation);
		final ActionErrors errors = new ActionErrors();
		final ActionMessages messages = new ActionMessages();
		final Collection<ShipmentRequest> shipmentReqCollection = (Collection<ShipmentRequest>) request
				.getSession().getAttribute("shipmentRequestCollection");
		try
		{
			//Call ShipmentRequestBizlogic's method to validate the contents of the shipment request
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final ShipmentRequestBizLogic bizLogic = (ShipmentRequestBizLogic) factory
					.getBizLogic(Constants.SHIPMENT_REQUEST_FORM_ID);
			final Iterator<ShipmentRequest> shipmentReqIterator = shipmentReqCollection.iterator();
			while (shipmentReqIterator.hasNext())
			{
				final ShipmentRequest shipmentRequest = shipmentReqIterator.next();
				if (request != null)
				{
					if (operation.equals(edu.wustl.common.util.global.Constants.ADD))
					{
						bizLogic.insert(shipmentRequest, this.getSessionData(request), 0);
						/* If the request is drafted,
						 * displays drafted request in 'Edit' mode.
						 */
						if (shipmentRequest.getActivityStatus() != null
								&& shipmentRequest.getActivityStatus().equals(
										Constants.ACTIVITY_STATUS_DRAFTED))
						{
							request.setAttribute(
									edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER,
									shipmentRequest.getId());
							target = Constants.ACTIVITY_STATUS_DRAFTED;
							messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									"object.drafted.successOnly", "Shipment Request"));
						}
					}
					else if (operation.equals(edu.wustl.catissuecore.util.global.Constants.EDIT))
					{
						DAO dao = null;
						ShipmentRequest shipmentReqOld = null;
						try
						{
							dao = AppUtility.openDAOSession(null);
							shipmentReqOld = (ShipmentRequest) dao.retrieveById(Class.forName(
									ShipmentRequest.class.getName()).getName(), Long
									.valueOf(Constants.SHIPMENT_REQUEST_FORM_ID));
							bizLogic.update(shipmentRequest, shipmentReqOld, 0, this
									.getSessionData(request));
							messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("object."
									+ operation + ".success", "Shipment Request(s)", "site(s)."));

						}
						catch (final Exception ex)
						{
							this.logger.error(ex.getMessage(),ex);
							ex.printStackTrace();
						}
						finally
						{
							try
							{
								AppUtility.closeDAOSession(dao);
							}
							catch (final ApplicationException e)
							{
								this.logger.error(e.getMessage(),e);
								e.printStackTrace();
							}
						}
					}
				}
			}
			// if draftedRequestId having value, indicates request generated from drafted request.
			// Mark the drafted request status as 'Closed'.
			final String draftedRequestId = request.getParameter("draftedRequestId");
			if (draftedRequestId != null && !draftedRequestId.trim().equals(""))
			{
				bizLogic.closeDraftedRequest(Long.parseLong(draftedRequestId), this
						.getSessionData(request), 0);
			}
		}
		//		catch (UserNotAuthorizedException authorizedException)
		//		{
		//			errors.add(ActionErrors.GLOBAL_ERROR,
		//new ActionError(authorizedException.getMessage()));
		//		}
		//to be verified...
		catch (final ApplicationException ex)
		{
			this.logger.error(ex.getMessage(),ex);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ex.getFormattedMessage()));
		}
		finally
		{
			request.getSession().removeAttribute("shipmentRequestCollection");
		}

		/*ShipmentRequestForm requestForm=(ShipmentRequestForm)request.getAttribute("shipmentRequestForm");
		request.setAttribute("shipmentRequestForm",requestForm);*/
		if (operation.equals(edu.wustl.catissuecore.util.global.Constants.ADD)
				&& !(target.equals(Constants.ACTIVITY_STATUS_DRAFTED)))
		{
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("object." + operation
					+ ".success", "Shipment Request(s)", "site(s)."));
		}
		this.saveMessages(request, messages);
		this.saveErrors(request, errors);
		if (errors.size() > 0)
		{
			return mapping.getInputForward();
		}
		return mapping.findForward(target);
	}

	/**
	 * gets the session data.
	 * @param request current request object.
	 * @return SessionDataBean object.
	 */
	@Override
	public SessionDataBean getSessionData(HttpServletRequest request)
	{
		SessionDataBean sessionData = null;
		Object obj = request.getSession().getAttribute(
				edu.wustl.catissuecore.util.global.Constants.SESSION_DATA);
		if (obj == null)
		{
			obj = request.getSession().getAttribute(
					edu.wustl.catissuecore.util.global.Constants.TEMP_SESSION_DATA);
		}
		if (obj != null)
		{
			sessionData = (SessionDataBean) obj;
		}
		return sessionData;
	}
}
