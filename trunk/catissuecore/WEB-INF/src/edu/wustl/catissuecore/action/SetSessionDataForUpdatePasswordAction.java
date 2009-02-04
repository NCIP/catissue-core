/*
 * Created on Sep 21, 2006
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
    
   /**
    *  This method sets the session data required for change password feature.
    */
    public ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        // PASSWORD_CHANGE_IN_SESSION is set to indicate that password has been changed in this session.
    	request.getSession().setAttribute(Constants.PASSWORD_CHANGE_IN_SESSION,new Boolean(true));
    	
    	/**
    	 *  TEMP_SESSION_DATA is set when user is forced to change the password, at that time  
    	 *  SESSION_DATA is set to Null. Here we set the SESSION_DATA with TEMP_SESSION_DATA, if  
    	 *  TEMP_SESSION_DATA is not null.
    	 */ 
        if(request.getSession().getAttribute(Constants.TEMP_SESSION_DATA) != null) 
        {
        request.getSession().setAttribute(Constants.SESSION_DATA, request.getSession().getAttribute(Constants.TEMP_SESSION_DATA));
        request.getSession().setAttribute(Constants.TEMP_SESSION_DATA,null);
        }
        return mapping.findForward(Constants.SUCCESS);
    }

}
