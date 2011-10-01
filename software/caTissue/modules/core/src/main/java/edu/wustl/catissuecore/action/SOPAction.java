
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SOPForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;

public class SOPAction extends SecureAction
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.action.SecureAction#executeSecureAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		boolean setDefaultValue = Boolean.parseBoolean(request
				.getParameter(Constants.SET_DEFAULT_VALUE));
		getSOPIdentifier(request, (SOPForm)form);
		if (setDefaultValue && request.getAttribute("org.apache.struts.action.ERROR")==null)
		{
			request.setAttribute(Constants.OPERATION, request.getAttribute(Constants.OPERATION));
			return mapping.findForward("pageOfDisplaySPPDefaultValue");
		}

		return mapping.findForward(Constants.PAGE_OF_SOP);
	}

	/**
	 * Gets the sop identifier.
	 *
	 * @param request the request
	 * @param form the form
	 *
	 * @return the SOP identifier
	 */
	private Long getSOPIdentifier(HttpServletRequest request, SOPForm form)
	{
		Long sopIdentifier = (Long) request.getAttribute(Constants.ID);
		if (sopIdentifier == null && request.getParameter(Constants.ID) != null)
		{
			sopIdentifier = Long.valueOf(request.getParameter(Constants.ID));
		}
		if (sopIdentifier == null)
		{
			sopIdentifier = form.getId();
		}
		request.setAttribute(Constants.ID, sopIdentifier);
		return sopIdentifier;
	}

}
