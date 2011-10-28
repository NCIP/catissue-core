
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SPPForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;

public class SPPAction extends SecureAction
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
		getSPPIdentifier(request, (SPPForm)form);
		if (setDefaultValue && request.getAttribute("org.apache.struts.action.ERROR")==null)
		{
			request.setAttribute(Constants.OPERATION, request.getAttribute(Constants.OPERATION));
			return mapping.findForward("pageOfDisplaySPPDefaultValue");
		}

		return mapping.findForward(Constants.PAGE_OF_SPP);
	}

	/**
	 * Gets the spp identifier.
	 *
	 * @param request the request
	 * @param form the form
	 *
	 * @return the SPP identifier
	 */
	private Long getSPPIdentifier(HttpServletRequest request, SPPForm form)
	{
		Long sppIdentifier = (Long) request.getAttribute(Constants.ID);
		if (sppIdentifier == null && request.getParameter(Constants.ID) != null)
		{
			sppIdentifier = Long.valueOf(request.getParameter(Constants.ID));
		}
		if (sppIdentifier == null)
		{
			sppIdentifier = form.getId();
		}
		request.setAttribute(Constants.ID, sppIdentifier);
		return sppIdentifier;
	}

}
