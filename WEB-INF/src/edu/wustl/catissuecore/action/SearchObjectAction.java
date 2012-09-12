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

import edu.wustl.catissuecore.util.global.CDMSIntegrationConstants;
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
		request.setAttribute(Constants.SYSTEM_IDENTIFIER, identifier);

		final String pageOf = request.getParameter(Constants.PAGE_OF);
		if(!"pageOfNewSpecimen".equalsIgnoreCase(pageOf) && !"pageOfSpecimenCollectionGroup".equalsIgnoreCase(pageOf))
		{	
		 request.setAttribute(Constants.PAGE_OF, pageOf);
		}
		ActionForward actionforward = this.getActionForward(pageOf,mapping,identifier);
		
		
		Logger.out.debug("identifier:" + identifier + " PAGEOF:" + pageOf);
		return actionforward; 
	}

	private ActionForward getActionForward(String pageOf,ActionMapping mapping,Long id) {
		
		ActionForward actionForward = new ActionForward();
		String path;
		
		if("pageOfNewSpecimen".equalsIgnoreCase(pageOf))
		{
			path = "/urlSpecimenView.do?"
					+"identifier="
					+id;
			actionForward.setName(pageOf);
			actionForward.setPath(path);
		}
		else if("pageOfSpecimenCollectionGroup".equalsIgnoreCase(pageOf))
		{
			path = "/GotoSCG.do?"
					+CDMSIntegrationConstants.SCGID+"="
					+id;	
			actionForward.setName(pageOf);
			actionForward.setPath(path);
		}
		else
		{
			actionForward = mapping.findForward(pageOf);
		}
		
		
		return actionForward;
	}
		
}
