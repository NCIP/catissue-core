
package edu.wustl.catissuecore.action;

import java.io.IOException;

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
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.User;
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
        password = loginForm.getPassword();
        
        try {
            
//            boolean loginOK = login(loginName,password);
             
            boolean loginOK = SecurityManager.getInstance(LoginAction.class).login(loginName,password);
        if (loginOK)
        {
            Logger.out.info(">>>>>>>>>>>>> SUCESSFUL LOGIN <<<<<<<<< ");
            session = request.getSession(true);
            return mapping.findForward(Constants.SUCCESS);
        }
        else
        {
            Logger.out.info("User " + loginName + " Invalid user. Sending back to the login Page");
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
        catch (Exception e){
            Logger.out.info("Exception: "+e.getMessage());
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                    "errors.exceptionErrorMessage"));
            //Report any errors we have discovered
            if (!errors.isEmpty())
            {
                saveErrors(request, errors);
            }
            return (mapping.findForward(Constants.FAILURE));
        }
        
       
    }
    
    
   
}