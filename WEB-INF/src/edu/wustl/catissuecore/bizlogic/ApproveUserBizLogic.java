/**
 * <p>Title: ApproveUserBizLogic Class>
 * <p>Description:	ApproveUserBizLogic is the bizLogic class for Approve Users.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Calendar;
import java.util.List;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.SignUpUser;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.GeneratePassword;
import edu.wustl.catissuecore.util.global.SendEmail;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.PasswordEncoderDecoder;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author gautam_shetty
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ApproveUserBizLogic extends DefaultBizLogic
{

    /**
     * Overrides the insert method of DefaultBizLogic 
     */
    protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
            throws DAOException, UserNotAuthorizedException
    {
        User user = (User) obj;
        List list = null;

        try
        {
            gov.nih.nci.security.authorization.domainobjects.User csmUser = new gov.nih.nci.security.authorization.domainobjects.User();
            csmUser.setLoginName(user.getLoginName());
            csmUser.setLastName(user.getLastName());
            csmUser.setFirstName(user.getFirstName());
            csmUser.setEmailId(user.getEmailAddress());
            if (user.getActivityStatus().equals(
                    Constants.ACTIVITY_STATUS_ACTIVE))
                csmUser.setPassword(PasswordEncoderDecoder
                        .encode(GeneratePassword.getPassword()));
            csmUser.setStartDate(Calendar.getInstance().getTime());

            SecurityManager.getInstance(ApproveUserBizLogic.class).createUser(
                    csmUser);

            if (user.getRoleId() != null)
            {
                SecurityManager.getInstance(ApproveUserBizLogic.class)
                        .assignRoleToUser(csmUser.getLoginName(),
                                user.getRoleId());
            }

            user.setSystemIdentifier(csmUser.getUserId());

            Department department = null;
            Institution institution = null;
            CancerResearchGroup cancerResearchGroup = null;
            Address address = null;

            list = dao.retrieve(Department.class.getName(), "systemIdentifier",
                    user.getDepartment().getSystemIdentifier());
            if (list.size() != 0)
            {
                department = (Department) list.get(0);
            }

            list = dao.retrieve(Institution.class.getName(),
                    "systemIdentifier", user.getInstitution()
                            .getSystemIdentifier());
            if (list.size() != 0)
            {
                institution = (Institution) list.get(0);
            }

            list = dao.retrieve(CancerResearchGroup.class.getName(),
                    "systemIdentifier", user.getCancerResearchGroup()
                            .getSystemIdentifier());
            if (list.size() != 0)
            {
                cancerResearchGroup = (CancerResearchGroup) list.get(0);
            }

            user.setDepartment(department);
            user.setInstitution(institution);
            user.setCancerResearchGroup(cancerResearchGroup);

            dao.insert(user.getAddress(), sessionDataBean, true, false);
            dao.insert(user, sessionDataBean, true, true);

            list = dao.retrieve(SignUpUser.class.getName(), "emailAddress",
                    user.getEmailAddress());
            if (list != null)
            {
                SignUpUser signUpUser = (SignUpUser) list.get(0);
                dao.delete(signUpUser);
            }

            //Send email to administrator and cc it to the user registered.
            SendEmail email = new SendEmail();

            String subject = ApplicationProperties
                    .getValue("userRegistration.approve.subject");

            String body = "Dear "
                    + csmUser.getFirstName()
                    + " "
                    + csmUser.getLastName()
                    + "\n\n"
                    + ApplicationProperties
                            .getValue("userRegistration.approved.body.start")
                    + ApplicationProperties
                            .getValue("userRegistration.loginDetails")
                    + "\n\tLogin Name : " + csmUser.getLoginName()
                    + "\n\tPassword : "
                    + PasswordEncoderDecoder.decode(csmUser.getPassword())
                    + "\n\n"
                    + ApplicationProperties.getValue("email.catissuecore.team");

            String adminEmailAddress = ApplicationProperties
                    .getValue("email.administrative.emailAddress");
            String technicalSupportEmailAddress = ApplicationProperties
                    .getValue("email.technicalSupport.emailAddress");
            String mailServer = ApplicationProperties
                    .getValue("email.mailServer");

            boolean emailStatus = email.sendmail(adminEmailAddress, csmUser
                    .getEmailId(), null, technicalSupportEmailAddress,
                    mailServer, subject, body);

            if (emailStatus)
            {
                Logger.out.info(ApplicationProperties
                        .getValue("userRegistration.email.success")
                        + csmUser.getFirstName() + " " + csmUser.getLastName());
            }
            else
            {
                Logger.out.info(ApplicationProperties
                        .getValue("userRegistration.email.failure")
                        + csmUser.getFirstName() + " " + csmUser.getLastName());
            }

        }
        catch (SMException smex)
        {
            Logger.out.debug("Exception in CSM user creation:"
                    + smex.getMessage(), smex);
            throw new DAOException(smex.getCause().getMessage());
        }
    }
}