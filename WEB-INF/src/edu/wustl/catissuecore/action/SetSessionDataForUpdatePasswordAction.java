/*
 * Created on Sep 23, 2005
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
import edu.wustl.common.action.BaseAction;


/**
 * @author santosh_chandak
 *
 * This class is used to set request/session data after successful password change. 
 */
public class SetSessionDataForUpdatePasswordAction extends BaseAction
{
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.action.SecureAction#executeSecureAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
      
        request.getSession().setAttribute(Constants.PASSWORD_CHANGE_IN_SESSION,new Boolean(true));
        return mapping.findForward(Constants.SUCCESS);
    }

}
