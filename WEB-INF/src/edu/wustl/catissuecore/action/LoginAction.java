
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.LoginForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.logger.Logger;

/**
 * 
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class LoginAction extends Action
{

    /**
     * Overrides the execute method of Action class.
     * Initializes the various drop down fields in Institute.jsp Page.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        if (form == null)
        {
            Logger.out.debug("Form is Null");
            return (mapping.findForward(Constants.FAILURE));
        }
        
        HttpSession prevSession = request.getSession();
        if(prevSession!=null)
        	prevSession.invalidate();
		
        LoginForm loginForm = (LoginForm) form;
        Logger.out.info("Inside Login Action, Just before validation");

        String loginName = loginForm.getLoginName();
        String password = PasswordManager.encode(loginForm.getPassword());
        
        try
        {
        	User validUser = getUser(loginName);
        	
        	if (validUser != null)
        	{
	            boolean loginOK = SecurityManager.getInstance(LoginAction.class).login(loginName, password);
	            if (loginOK)
	            {
	                Logger.out.info(">>>>>>>>>>>>> SUCESSFUL LOGIN A <<<<<<<<< ");
	                HttpSession session = request.getSession(true);
	                
	                Long userId = validUser.getId();
	                String ipAddress = request.getRemoteAddr();
	                
	                SessionDataBean sessionData = new SessionDataBean();
	                sessionData.setUserName(loginName);
	                sessionData.setIpAddress(ipAddress);
	                sessionData.setUserId(userId);
	                sessionData.setFirstName(validUser.getFirstName());
	                sessionData.setLastName(validUser.getLastName());
	                Logger.out.debug("CSM USer ID ....................... : "+validUser.getCsmUserId());
	                sessionData.setCsmUserId(validUser.getCsmUserId().toString());
	                session.setAttribute(Constants.SESSION_DATA,sessionData);
	                UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
	             
	                String result = userBizLogic.checkFirstLoginAndExpiry(validUser);
													
					if(!result.equals(Constants.SUCCESS)) 
					{
	                	  ActionErrors errors = new ActionErrors();
	                      errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(result));
	                      saveErrors(request, errors);
	                      session.setAttribute(Constants.SESSION_DATA,null);
	                      session.setAttribute(Constants.TEMP_SESSION_DATA,sessionData);
	                      request.setAttribute(Constants.PAGEOF,Constants.PAGEOF_CHANGE_PASSWORD);
	                      return mapping.findForward(Constants.ACCESS_DENIED);
	                }
	                         
	                return mapping.findForward(Constants.SUCCESS);
	            }
	            else
	            {
	                Logger.out.info("User " + loginName + " Invalid user. Sending back to the login Page");
	                handleError(request, "errors.incorrectLoginNamePassword");
	                return (mapping.findForward(Constants.FAILURE));
	            }
        	} // if valid user
        	else
        	{                
        		Logger.out.info("User " + loginName + " Invalid user. Sending back to the login Page");
        		handleError(request, "errors.incorrectLoginNamePassword");
	            return (mapping.findForward(Constants.FAILURE));
        	} // invalid user
       	}
        catch (Exception e)
        {
            Logger.out.info("Exception: " + e.getMessage(), e);
            handleError(request, "errors.incorrectLoginNamePassword");
            return (mapping.findForward(Constants.FAILURE));
        }
    }
    
    private void handleError(HttpServletRequest request, String errorKey)
    {
        ActionErrors errors = new ActionErrors();
        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey));
        //Report any errors we have discovered
        if (!errors.isEmpty())
        {
            saveErrors(request, errors);
        }
    }
    
    private User getUser(String loginName) throws DAOException
    {
    	UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
    	String[] whereColumnName = {"activityStatus","loginName"};
    	String[] whereColumnCondition = {"=","="};
    	String[] whereColumnValue = {Constants.ACTIVITY_STATUS_ACTIVE, loginName};
    	
    	List users = userBizLogic.retrieve(User.class.getName(), whereColumnName, 
    			whereColumnCondition, whereColumnValue,Constants.AND_JOIN_CONDITION);
    	
    	if (users!=null && !users.isEmpty())
    	{
    	    User validUser = (User)users.get(0);
    	    return validUser;
    	}
        return null;
    }
}