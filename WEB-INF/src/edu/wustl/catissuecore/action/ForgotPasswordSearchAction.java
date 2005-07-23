/**
 * <p>Title: ForgotPasswordSearchAction Class>
 * <p>Description:	This Class is used to retrieve the password of the user and mail it to his email address.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 19, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.dao.AbstractBizLogic;
import edu.wustl.catissuecore.dao.BizLogicFactory;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.SendEmail;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to retrieve the password of the user and mail it to his email address.
 * @author gautam_shetty
 */
public class ForgotPasswordSearchAction extends Action
{

    /**
     * Overrides the execute method of Action class.
     * Adds the user information in the database.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        String target = null;
        try
        {
            UserForm uForm = (UserForm) form;
            AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(uForm.getFormId());
            List list = null;
            Validator validator = new Validator();

            if (!validator.isEmpty(uForm.getLoginName()))
            {
                //if loginName is entered retrieve password using loginName.
                list = bizLogic.retrieve(User.class.getName(), Constants.LOGINNAME,
                        uForm.getLoginName());
            }
            else
            {
                //if loginName is not entered retrieve password using email address.
                list = bizLogic.retrieve(User.class.getName(), Constants.EMAIL,
                        uForm.getEmailAddress());
            }
            
            if (list.size() != 0)
            {
                User user = (User) list.get(0);
                
                Logger.out.debug("Password successfully retrieved for user: "+user.getUser().getLoginName());
                
                if (user.getActivityStatus().equals(Constants.ACTIVITY_STATUS_APPROVE))
                {
                    SendEmail email = new SendEmail();
                    
                    String body = "\n User Name : " + user.getUser().getLoginName() + "\n Password : " + user.getUser().getPassword();
                    boolean emailStatus = email.sendmail(user.getAddress().getEmailAddress(),
                            Variables.toAddress, Variables.mailServer,
                            Constants.YOUR_PASSWORD, body);
                    
                    /**
                     *TODO Body of Email.
                     */

                    if (emailStatus == true)
                    {
                        
                        Logger.out.debug("Password successfully sent to "+user.getUser().getLoginName()+" at "+user.getAddress().getEmailAddress());
                        target = new String(Constants.SUCCESS);
                    }
                    else
                    {
                        Logger.out.error("Sending Password Failed to "+user.getUser().getLoginName()+" at "+user.getAddress().getEmailAddress());
                        target = new String(Constants.FAILURE);
                    }
                }
                else
                {
                    ActionErrors errors = new ActionErrors();
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                    "errors.forgotpassword.user.notApproved"));
                    saveErrors(request, errors);
                    target = new String(Constants.FAILURE);
                }
            }
            else
            {
                ActionErrors errors = new ActionErrors();

                if (uForm.getLoginName() == null)
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.forgotpassword.unknown.email"));
                }
                else
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.item.unknown", ApplicationProperties
                                    .getValue("user.name")));
                }
                target = new String(Constants.FAILURE);
                saveErrors(request, errors);
            }
        }
        catch (DAOException excp)
        {
            target = new String(Constants.FAILURE);
            Logger.out.error(excp.getMessage());
        }
        return (mapping.findForward(target));
    }
}