package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;

/**
 * This Action is for forwarding to CPResultsView Page 
 * @author vaishali_khandelwal
 *
 */
public class CPBasedSearchAction extends SecureAction
{
	/**
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    { 

		SessionDataBean sessionDataBean = super.getSessionData(request);
		
		 /** Name : Aarti Sharma
		 * Reviewer: Sachin Lale
		 * Bug ID: 4111
		 * Patch ID: 4111_1
		 * See also: 4111_2
		 * Desciption: If user does not have privilege to view identified data attribute "Access" is set to "Denied" 
		 * so that he/she is not able to view the Participant list in CP based view
		 */
		long csmUserId = new Long(sessionDataBean.getCsmUserId()).longValue();		
		boolean hasIdentifiedDataAccess = true;
//		//SecurityManager.getInstance(this.getClass()).hasIdentifiedDataAccess(csmUserId);
//		if(!hasIdentifiedDataAccess)
//		{
//			request.getSession().setAttribute("Access", "Denied");
//		}
//		else
//		{
//			request.removeAttribute("Access");
//		}
		return mapping.findForward("success");
		
    }
	        
}
