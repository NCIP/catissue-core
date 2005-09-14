
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
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.PasswordEncoderDecoder;
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
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {

        String loginName = null;
        String password = null;
        HttpSession session = null;

        if (form == null)
        {
            Logger.out.debug("Form is Null");
            return (mapping.findForward(Constants.FAILURE));
        }

        LoginForm loginForm = (LoginForm) form;
        Logger.out.info("Inside Login Action, Just before validation");

        loginName = loginForm.getLoginName();
        password = PasswordEncoderDecoder.encode(loginForm.getPassword());
        String ipAddress = null;
        SessionDataBean sessionData = new SessionDataBean();
        Long userId = null;
        try
        {
        	boolean validUser = isValidUser(loginName );
        	
        	if (validUser)
        	{
	            boolean loginOK = SecurityManager.getInstance(LoginAction.class)
	                    .login(loginName, password);
	            if (loginOK)
	            {
	                Logger.out.info(">>>>>>>>>>>>> SUCESSFUL LOGIN A <<<<<<<<< ");
	                session = request.getSession(true);
	                gov.nih.nci.security.authorization.domainobjects.User userData = SecurityManager.getInstance(
	                																LoginAction.class).getUser(loginName);
	                userId = userData.getUserId();
	                ipAddress = request.getRemoteAddr();
	                sessionData.setUserName(loginName);
	                sessionData.setIpAddress(ipAddress);
	                sessionData.setUserId(userId);
	                session.setAttribute(Constants.SESSION_DATA,sessionData);
	                //session.setAttribute(Constants.SESSION_DATA,loginName);
	                Logger.out.info(">>>>>>>>>>>>> SUCESSFUL LOGIN B <<<<<<<<< ");
	                return mapping.findForward(Constants.SUCCESS);
	            }
	            else
	            {
	                Logger.out.info("User " + loginName
	                        + " Invalid user. Sending back to the login Page");
	                ActionErrors errors = new ActionErrors();
	                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
	                        "errors.incorrectLoginNamePassword"));
	
	                //Report any errors we have discovered
	                if (!errors.isEmpty())
	                {
	                    saveErrors(request, errors);
	                }
	                return (mapping.findForward(Constants.FAILURE));
	            }
        	} // if valid user
        	else
        	{                Logger.out.info("User " + loginName
                    + " Invalid user. Sending back to the login Page");
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                    "errors.incorrectLoginNamePassword"));

            //Report any errors we have discovered
            if (!errors.isEmpty())
            {
                saveErrors(request, errors);
            }
           	System.out.println("\n\n\n\n****** Invalid User : " + loginName + "\n\n\n\n\n****");
            return (mapping.findForward(Constants.FAILURE));


        	} // invalid user
       	}
        catch (Exception e)
        {
            Logger.out.info("Exception: " + e.getMessage());
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                    "errors.incorrectLoginNamePassword"));
            //Report any errors we have discovered
            if (!errors.isEmpty())
            {
                saveErrors(request, errors);
            }
            return (mapping.findForward(Constants.FAILURE));
        }

    }
    public boolean isValidUser(String loginName) throws DAOException
    {
    	System.out.println("\n\n\n\n****** User : " + loginName + "\n\n\n\n\n****");
        gov.nih.nci.security.authorization.domainobjects.User csmUser = null;
        try
        {
            csmUser = SecurityManager.getInstance(UserBizLogic.class).getUser(loginName);
            
            if (csmUser == null)
            	return false;
            else
            {
            	Long uidCSM = csmUser.getUserId();
//            	System.out.println("\n\n\n\n****** CSMUser : " + csmUser + "\n\n\n\n\n****");
//            	System.out.println("\n\n\n\n****** CSMUserId : " + csmUser.getUserId() + "\n\n\n\n\n****");
//            	//---------------------------
            	
            	UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getBizLogic(Constants.USER_FORM_ID);
            	List users = userBizLogic.retrieve(User.class.getName(), "activityStatus",Constants.ACTIVITY_STATUS_ACTIVE);
            	

				User user = null;
               
                if (users != null)
                {
                	boolean found=false;
                    for (int i = 0; i < users.size(); i++)
                    {
                        user = (User) users.get(i);
                        try
                        {
                        	Long uidLocal = user.getSystemIdentifier();
//                        	System.out.println("\n****** LocalUserId : " + uidLocal.toString() + "\n****");
                        	if (uidLocal.longValue() == (uidCSM.longValue() ))
                        	{
                        		found = true;
                        		break;
                        	}
                        		
                        }
                        catch (Exception e)
                        {
                            Logger.out.debug("Unable to get user : " + e.getMessage());
                            throw new DAOException(e);
                        }
                   } // for
                   if (found)
                   	return true;
                   else
                   	return false;
                }
                else
                	return false;
            }
        }
        catch (SMException e)
        {
//        	System.out.println("\n\n\n\n****** error : " + e + "\n\n\n\n\n****");
            Logger.out.debug("Unable to get user : " + e.getMessage());
            return false;
        }
        catch (Exception e1)
        {
//        	System.out.println("\n\n\n\n****** error : " + e1 + "\n\n\n\n\n****");
            Logger.out.debug("Unable to get user : " + e1.getMessage());
            return false;
        }
    }
}