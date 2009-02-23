/**
 * This class is used to handle any exception condition and redirect to report problem page.
 */
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;


/**
 * @author mandar_deshmukh
 *
 */
public class RedirectToProblemAction  extends Action {

	/**
	 * 
	 */
	public RedirectToProblemAction() {
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		request.setAttribute(Constants.EXCEPTION_OCCURED, Boolean.TRUE);
		request.setAttribute("operation", "add");
		return mapping.findForward(Constants.SUCCESS);
	}

}
