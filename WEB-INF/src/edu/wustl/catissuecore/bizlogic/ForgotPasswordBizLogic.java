/**
 * <p>Title: ForgotPasswordBizLogic Class>
 * <p>Description:  ForgotPasswordBizLogic is the business logic class for forgot password.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.action.ForgotPasswordSearchAction;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.SendEmail;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * ForgotPasswordBizLogic is the business logic class for forgot password.
 * @author gautam_shetty
 */
public class ForgotPasswordBizLogic extends DefaultBizLogic
{

    public String getForgotPassword(String emailAddress) throws DAOException
    {
        
        String statusMessageKey = null;
        try
        {
            List list = null;
            gov.nih.nci.security.authorization.domainobjects.User csmUser = null;
            
            //retrieve password using email address.
            List csmList = SecurityManager.getInstance(
                    ForgotPasswordSearchAction.class).getUsersByEmail(
                    emailAddress);
            
            if (!csmList.isEmpty())
            {
                csmUser = (gov.nih.nci.security.authorization.domainobjects.User) csmList.get(0);
                list = retrieve(User.class.getName(), "systemIdentifier",
                        String.valueOf(csmUser.getUserId()));
            }
            
            if ((csmUser != null) && (!list.isEmpty()))
            {
                User user = (User) list.get(0);

                Logger.out.debug("Password successfully retrieved for user: "
                        + csmUser.getLoginName());

                if (user.getActivityStatus().equals(
                        Constants.ACTIVITY_STATUS_ACTIVE))
                {
                    SendEmail email = new SendEmail();
                    
                    String mailServer = ApplicationProperties
                            .getValue("email.mailServer");
                    String technicalSupportEmailAddress = ApplicationProperties
                            .getValue("email.technicalSupport.emailAddress");
                    String subject = ApplicationProperties
                            .getValue("forgotPassword.email.subject");

                    String body = "Dear " + csmUser.getFirstName()
                            + " " + csmUser.getLastName()
                            + "\n\n" + ApplicationProperties.getValue("forgotPassword.email.body.start")
                            + "\n\t User Name : " + csmUser.getLoginName()
                            + "\n\t Password : " + csmUser.getPassword()
                            + "\n\n" + ApplicationProperties.getValue("email.catissuecore.team");
                    
                    boolean emailStatus = email.sendmail(csmUser
                            .getEmailId(), technicalSupportEmailAddress,
                            mailServer, subject, body);
                    
                    if (emailStatus == true)
                    {
                        Logger.out.debug("Password successfully sent to "
                                + csmUser.getLoginName() + " at "
                                + csmUser.getEmailId());
                        statusMessageKey = "password.send.success";
                    }
                    else
                    {
                        Logger.out.error("Sending Password Failed to "
                                + csmUser.getLoginName() + " at "
                                + csmUser.getEmailId());
                        statusMessageKey = "password.send.failure";
                    }
                }
                else
                {
                    statusMessageKey = "errors.forgotpassword.user.notApproved";
                }
            }
            else
            {
                statusMessageKey = "errors.forgotpassword.user.unknown";
            }

        }
        catch (SMException smExp)
        {
            throw new DAOException(smExp.getMessage(),smExp);
        }
        
        return statusMessageKey;
    }
}