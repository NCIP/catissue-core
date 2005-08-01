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
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
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
            gov.nih.nci.security.authorization.domainobjects.User csmUser = null;

            if (!validator.isEmpty(uForm.getLoginName()))
            {
                //if loginName is entered retrieve password using loginName.
                csmUser = SecurityManager.getInstance(ForgotPasswordSearchAction.class)
    			.getUser(uForm.getLoginName());
                
            }
            else
            {
                //if loginName is not entered retrieve password using email address.
                List csmList = SecurityManager.getInstance(ForgotPasswordSearchAction.class)
                								.getUsersByEmail(uForm.getEmailAddress());
                
                if (csmList.size() != 0)
                {
                    csmUser = (gov.nih.nci.security.authorization.domainobjects.User )
                    				csmList.get(0);
                }
                 
            }
            
            if (csmUser != null)
            {
                list = bizLogic.retrieve(User.class.getName(), "systemIdentifier",
                        String.valueOf(csmUser.getUserId()));
            }
                

            if ((csmUser != null) && (list.size() != 0))
            {
                User user = (User) list.get(0);
                
                user.setUser(csmUser);
                
                Logger.out.debug("Password successfully retrieved for user: "+user.getUser().getLoginName());
                
                if (user.getActivityStatus().equals(Constants.ACTIVITY_STATUS_ACTIVE))
                {
                    SendEmail email = new SendEmail();
                    
                    String mailServer = ApplicationProperties.getValue("email.mailServer");
                    String technicalSupportEmailAddress = ApplicationProperties.getValue("email.technicalSupport.emailAddress");
                    String subject = ApplicationProperties.getValue("forgotPassword.email.subject");
                    
                    String body = "Dear " + user.getUser().getFirstName()+ " " + user.getUser().getLastName() +
                    			  "\n\n" + ApplicationProperties.getValue("forgotPassword.email.body.start") +
                    			  "\n\t User Name : " + user.getUser().getLoginName() + "\n\t Password : " + 
                    			  user.getUser().getPassword() +
                    			  "\n\n" + ApplicationProperties.getValue("email.catissuecore.team");
                    boolean emailStatus = email.sendmail(user.getUser().getEmailId(),
                            technicalSupportEmailAddress, mailServer,
                            subject, body);
                    
                    if (emailStatus == true)
                    {
                        
                        Logger.out.debug("Password successfully sent to "+user.getUser().getLoginName()+" at "+user.getUser().getEmailId());
                        String statusMessageKey = "password.send.success";

                        request.setAttribute(Constants.STATUS_MESSAGE_KEY,statusMessageKey);
                        target = new String(Constants.SUCCESS);
                    }
                    else
                    {
                        Logger.out.error("Sending Password Failed to "+user.getUser().getLoginName()+" at "+user.getUser().getEmailId());
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
        catch(SMException exp)
        {
            Logger.out.error(exp.getMessage(),exp);
        }
        return (mapping.findForward(target));
    }
}