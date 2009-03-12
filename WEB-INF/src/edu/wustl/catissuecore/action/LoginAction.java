
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.LoginForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.Roles;
import edu.wustl.common.util.dbManager.DAOException;
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
     * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws IOException I/O exception
	 * @throws ServletException servlet exception
	 * @return value for ActionForward object
     */
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
//        String password = PasswordManager.encode(loginForm.getPassword());
        
        try
        { 
        	User validUser = getUser(loginName);
        	String password = loginForm.getPassword();
        	if (validUser != null)
        	{ 
	            boolean loginOK = SecurityManager.getInstance(LoginAction.class).login(loginName, password);
	            if (loginOK) 
	            {
	                DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
	            	defaultBizLogic.cachePrivileges(loginName);
	            	
	            	Logger.out.info(">>>>>>>>>>>>> SUCESSFUL LOGIN A <<<<<<<<< ");
	                HttpSession session = request.getSession(true);
	                
	                Long userId = validUser.getId();
	                String ipAddress = request.getRemoteAddr();
	                
	                SessionDataBean sessionData = new SessionDataBean();
	                
	                boolean adminUser = false;
	                
	                if(validUser.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
	                {
	                	adminUser = true;
	                }
	                
	                sessionData.setAdmin(adminUser);
	                sessionData.setUserName(loginName);
	                sessionData.setIpAddress(ipAddress);
	                sessionData.setUserId(userId);
	                sessionData.setFirstName(validUser.getFirstName());
	                sessionData.setLastName(validUser.getLastName());
	                Logger.out.debug("CSM USer ID ....................... : "+validUser.getCsmUserId());
	                sessionData.setCsmUserId(validUser.getCsmUserId().toString());
	                session.setAttribute(Constants.SESSION_DATA,sessionData);
	                session.setAttribute(Constants.USER_ROLE,validUser.getRoleId());
	                UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
	                	
	                String result = userBizLogic.checkFirstLoginAndExpiry(validUser);
													
					setSecurityParamsInSessionData(validUser, sessionData);
					
					if(!result.equals(Constants.SUCCESS)) 
					{
						//ActionError changed to ActionMessage
						ActionMessages messages = new ActionMessages();
						messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(result));
						saveMessages(request, messages);
	                
	                      session.setAttribute(Constants.SESSION_DATA,null);
	                      session.setAttribute(Constants.TEMP_SESSION_DATA,sessionData);
	                      request.setAttribute(Constants.PAGEOF,Constants.PAGEOF_CHANGE_PASSWORD);
	                      return mapping.findForward(Constants.ACCESS_DENIED);
	                }
					
					//Determining Role Name- Start
	                /**
	                 * Name : Virender Mehta
	                 * Reviewer: Sachin Lale
	                 * Bug ID: 3842
	                 * Patch ID: 3842_1
	                 * See also: 3842_2
	                 * Description: Default views based on user login
	                 * 				If user login as admin Default view is set Administrator Tab 
	                 * 				If user login as Technicion Default view is set as Collection Protocol Base View Under Biospecimen Tab 
	                 * 				If user login as Supervisor Default view is set as Collection Protocol Base View Under Biospecimen Tab
	                 * 				If user login as Scientist Default view is set as Admin Advance Search unser Search tab
	                 * Forwarding to default page depending on user role
	                 */
					String forwardToPage =Constants.SUCCESS; 
					//getForwardToPageOnLogin(validUser.getCsmUserId().longValue());
					return (mapping.findForward(forwardToPage));
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
    /**
     * To set the Security Parameters in the given SessionDataBean object depending upon the role of the user.
     * @param validUser reference to the User.
     * @param sessionData The reference to the SessionDataBean object.
     * @throws SMException
     */
	private void setSecurityParamsInSessionData(User validUser, SessionDataBean sessionData)
			throws SMException
	{
		String userRole = SecurityManager.getInstance(LoginAction.class).getUserGroup(
				validUser.getCsmUserId());
		if (userRole.equals(Roles.ADMINISTRATOR) || userRole.equals(Roles.SUPERVISOR))
		{
			sessionData.setSecurityRequired(false);
		}
		else
		{
			sessionData.setSecurityRequired(true);
		}
	}
    /**
     * Patch ID: 3842_2
     * This function will take LoginID for user and return the appropriate default page.
     * Get role from securitymanager and modify the role name where first 
     * character is in upper case and rest all are in lower case add prefix "pageOf" 
     * to modified role name and forward to that page.  
     * @param loginId
     * @return
     * @throws SMException
     */
    
    private String getForwardToPageOnLogin(Long loginId) throws SMException
    {
    	SecurityManager securityManager = SecurityManager.getInstance(LoginAction.class);
        String roleName = securityManager.getUserGroup(loginId);
        String modifiedRolename="";
        if(roleName==null || roleName.equals(""))
        	
        modifiedRolename ="pageOfScientist";
        else 
        	modifiedRolename =	"pageOfAdministrator";
     	//String modifiedRolename = roleName.substring(0,1).toUpperCase()+roleName.substring(1,roleName.length()).toLowerCase();
		return (modifiedRolename);
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