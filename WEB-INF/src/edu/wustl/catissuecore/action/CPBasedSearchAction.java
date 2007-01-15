package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * This Action is for forwarding to CPResultsView Page 
 * @author vaishali_khandelwal
 *
 */
public class CPBasedSearchAction extends SecureAction
{

	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
		 
		SessionDataBean sessionDataBean = super.getSessionData(request);
		long csmUserId = new Long(sessionDataBean.getCsmUserId()).longValue();
		Role role = SecurityManager.getInstance(UserBizLogic.class).getUserRole(csmUserId);
		//Checking for the role of user , if role is technician then access is denied for viewing
		//participant information in CP Based view 
		if(role.getName() != null && role.getName().equals(Constants.TECHNICIAN))
			request.getSession().setAttribute("Access", "Denied");
		
		return mapping.findForward("success");
		
    }
	        
}
