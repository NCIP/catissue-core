

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Validator;

/**
 * 
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class LoginForm extends ActionForm
{
    	/**
    	 * login ID entered by user
    	 */
        private String loginName = new String();
        
        /**
         * password entered by user
         */
        private String password = new String();
 
        /**
         * @return Returns the loginName.
         */
        public String getLoginName()
        {
            return loginName;
        }
        /**
         * @param loginName The loginName to set.
         */
        public void setLoginName(String loginName)
        {
            this.loginName = loginName;
        }
        /**
         * @return Returns the password.
         */
        public String getPassword()
        {
            return password;
        }
        /**
         * @param password The password to set.
         */
        public void setPassword(String password)
        {
            this.password = password;
        }
        
        public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
        {
        	HttpSession prevSession = request.getSession();
            if(prevSession!=null)
            	prevSession.invalidate();
            
            ActionErrors errors = new ActionErrors();
            Validator validator = new Validator();
            if (validator.isEmpty(loginName))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("user.loginName")));
            }
            else
            {
                if (!Character.isLetter(loginName.charAt(0)))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("user.loginName")));
                }
            }
            if (validator.isEmpty(password))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("user.password")));
            }
            return errors;
        }
        
        
        /**
         * Resets the values of all the fields.
         * This method defined in ActionForm is overridden in this class.
         */
        public void reset(ActionMapping mapping, HttpServletRequest request)
        {
            this.loginName = null;
            this.password = null;
        }
}