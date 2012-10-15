/*
 * Created on Sep 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.XSSSupportedAction;
import edu.wustl.common.util.logger.Logger;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SearchObjectAction extends XSSSupportedAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final Long identifier = Long.valueOf(request.getParameter(Constants.SYSTEM_IDENTIFIER));
		StringBuffer urlParam = new StringBuffer();
		request.setAttribute(Constants.SYSTEM_IDENTIFIER, identifier);

		final String pageOf = request.getParameter(Constants.PAGE_OF);
		ActionForward actionforward = mapping.findForward(pageOf);
		if(!"pageOfNewSpecimen".equalsIgnoreCase(pageOf) && !"pageOfSpecimenCollectionGroup".equalsIgnoreCase(pageOf) && !"pageOfCollectionProtocolRegistration".equalsIgnoreCase(pageOf))
		{	
		 request.setAttribute(Constants.PAGE_OF, pageOf);
		}
		else
		{
			urlParam.append("?").append(Constants.SYSTEM_IDENTIFIER).append(Constants.EQUALS).append(identifier).append("&").append(Constants.PAGE_OF).append(Constants.EQUALS).
			append(pageOf);
			 final ActionForward newActionForward = new ActionForward();
		     newActionForward.setName(actionforward.getName());
		     newActionForward.setRedirect(false);
		     newActionForward.setContextRelative(false);
		     newActionForward.setPath(actionforward.getPath()+urlParam.toString());
		     actionforward = newActionForward;
		}
				
		Logger.out.debug("identifier:" + identifier + " PAGEOF:" + pageOf);
		return actionforward; 
	}

}
