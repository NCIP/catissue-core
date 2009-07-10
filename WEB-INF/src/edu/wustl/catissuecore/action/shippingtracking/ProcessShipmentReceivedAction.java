
package edu.wustl.catissuecore.action.shippingtracking;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentForm;
import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentReceivingForm;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.CommonAddEditAction;
import edu.wustl.common.action.CommonEdtAction;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;

/**
 * this class implements the action to process the shipment receiving.
 */
public class ProcessShipmentReceivedAction extends CommonAddEditAction
{

	/**
	 * common catissue logger.
	 */
	Logger logger = Logger.getCommonLogger(ProcessShipmentReceivedAction.class);

	/**
	 * action method to process shipment receiving.
	 * @param mapping object of ActionMapping class.
	 * @param form object of ActionForm class.
	 * @param request object of HttpServletRequest class.
	 * @param response object of HttpServletResponse class.
	 * @return forward mapping.
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		final CommonEdtAction commonEditAction = new CommonEdtAction();
		ActionForward forward = mapping.findForward(Constants.FAILURE);
		if (form instanceof ShipmentReceivingForm)
		{
			//			try
			//			{
			//Populate
			final boolean isRejectAndResend = this.processRejectAndReturnContents(request,
					(ShipmentReceivingForm) form);
			//bug 11543
			try
			{
				forward = commonEditAction.execute(mapping, form, request, response);
			}
			catch (final ApplicationException e)
			{
				this.logger.debug(e.getMessage(), e);
				final ActionErrors actionErrors = new ActionErrors();
				actionErrors.add(ActionErrors.GLOBAL_ERROR,
						new ActionError(e.getFormattedMessage()));
				this.saveErrors(request, actionErrors);
				forward = mapping.findForward(Constants.FAILURE);
				e.printStackTrace();
			}
			if (isRejectAndResend)
			{
				forward = mapping.findForward("rejectAndResend");
			}
			//			}
			//			catch (UserNotAuthorizedException excp)
			//			{
			//	            ActionErrors actionErrors = new ActionErrors();
			//	            SessionDataBean sessionDataBean = getSessionData(request);
			//	            String userName = "";
			//	            if(sessionDataBean != null)
			//	        	{
			//	        	    userName = sessionDataBean.getUserName();
			//	        	}
			//	            String className = getActualClassName(new Shipment().getClass().getName());
			//	            String decoratedPrivilegeName = Utility.getDisplayLabelForUnderscore(excp.getPrivilegeName());
			//	            String baseObject = "";
			//	            if (excp.getBaseObject() != null && excp.getBaseObject().trim().length() != 0)
			//	            {
			//	                baseObject = excp.getBaseObject();
			//	            }
			//	            else
			//	            {
			//	                baseObject = className;
			//	            }
			//	            ActionError error = new ActionError("access.addedit.object.denied", userName,
			//	            		className,decoratedPrivilegeName,baseObject);
			//	            actionErrors.add(ActionErrors.GLOBAL_ERROR, error);
			//	        	saveErrors(request, actionErrors);
			//	        	Logger.out.error(excp.getMessage(), excp);
			//	        	forward=mapping.findForward(Constants.FAILURE);
			//	        }
			//			catch (AssignDataException assignDataException)
			//			{
			//				ActionErrors actionErrors = new ActionErrors();
			//				actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item",
			//						assignDataException.getMessage()));
			//				saveErrors(request, actionErrors);
			//				forward=mapping.findForward(Constants.FAILURE);
			//			}
			//			catch (BizLogicException bizLogicException)
			//			{
			//				ActionErrors actionErrors = new ActionErrors();
			//				actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item",
			//						bizLogicException.getMessage()));
			//				saveErrors(request, actionErrors);
			//				forward=mapping.findForward(Constants.FAILURE);
			//			}
		}
		return forward;
	}

	/**
	 * processes the contents for RejectandReturn request.
	 * @param request HttpServletRequest object.
	 * @param shipmentReceivingForm form containing all values.
	 * @return boolean result of the operation.
	 */
	protected boolean processRejectAndReturnContents(HttpServletRequest request,
			ShipmentReceivingForm shipmentReceivingForm)
	{
		boolean isRejectAndResendSpecimens = false;
		boolean isRejectAndResendContainers = false;
		final ShipmentForm shipmentForm = new ShipmentForm();
		if (shipmentReceivingForm != null)
		{
			isRejectAndResendSpecimens = this.populateSpecimenDetails(shipmentForm,
					shipmentReceivingForm.getSpecimenCollection());
			isRejectAndResendContainers = this.populateContainerDetails(shipmentForm,
					shipmentReceivingForm.getContainerCollection());
		}
		if (shipmentForm.getSpecimenCounter() > 0 || shipmentForm.getContainerCounter() > 0)
		{
			request.setAttribute("shipmentForm", shipmentForm);
		}
		return isRejectAndResendContainers || isRejectAndResendSpecimens;
	}

	/**
	 * populates container details.
	 * @param shipmentForm form containing all values.
	 * @param containerCollection list of StorageContainers.
	 * @return boolean result of operation.
	 */
	private boolean populateContainerDetails(ShipmentForm shipmentForm,
			List<StorageContainer> containerCollection)
	{
		boolean isRejectAndResend = false;
		//Process containers
		if (containerCollection != null)
		{
			shipmentForm.setContainerLabelChoice("ContainerLabel");
			for (final StorageContainer container : containerCollection)
			{
				if (container != null
						&& container.getActivityStatus() != null
						&& container
								.getActivityStatus()
								.trim()
								.equals(
										edu.wustl.catissuecore.util.shippingtracking.Constants.REJECT_AND_RESEND))
				{
					isRejectAndResend = true;
					int containerCounter = shipmentForm.getContainerCounter();
					containerCounter++;
					shipmentForm.setContainerCounter(containerCounter);
					shipmentForm.getContainerDetailsMap().put("containerLabel_" + containerCounter,
							container.getName());
				}
			}
		}
		return isRejectAndResend;
	}

	/**
	 * populates specimen details.
	 * @param shipmentForm form containing all values.
	 * @param specimenCollection collection of Specimen objects.
	 * @return boolean result of the operation.
	 */
	protected boolean populateSpecimenDetails(ShipmentForm shipmentForm,
			Collection<Specimen> specimenCollection)
	{
		boolean isRejectAndResend = false;
		//		Process specimens
		if (specimenCollection != null && shipmentForm != null)
		{
			shipmentForm.setSpecimenLabelChoice("SpecimenLabel");
			for (final Specimen specimen : specimenCollection)
			{
				if (specimen != null
						&& specimen.getActivityStatus() != null
						&& specimen
								.getActivityStatus()
								.trim()
								.equals(
										edu.wustl.catissuecore.util.shippingtracking.Constants.REJECT_AND_RESEND))
				{
					isRejectAndResend = true;
					int specimenCount = shipmentForm.getSpecimenCounter();
					specimenCount++;
					shipmentForm.setSpecimenCounter(specimenCount);
					shipmentForm.getSpecimenDetailsMap().put("specimenLabel_" + specimenCount,
							specimen.getLabel());
				}
			}
		}
		return isRejectAndResend;
	}
	/**
	 * gets the actual class name from a string.
	 * @param name name containing classname.
	 * @return string containing actual class name.
	 */
	/*private String getActualClassName(String name)
	{
		if(name != null && name.trim().length() != 0)
	    {
			String splitter = "\\.";
	        String[] arr = name.split(splitter);
	        if(arr != null && arr.length != 0)
	        {
	        	return arr[arr.length - 1];
	        }
	    }
	    return name;
	}*/
}
