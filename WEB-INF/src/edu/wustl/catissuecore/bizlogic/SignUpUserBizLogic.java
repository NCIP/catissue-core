/*
 * Created on Aug 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.SignUpUser;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.SendEmail;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SignUpUserBizLogic extends DefaultBizLogic
{
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.bizlogic.DefaultBizLogic#insert(edu.wustl.catissuecore.dao.DAO, java.lang.Object)
     */
    protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
    {
        Logger.out.debug("IN SignUpUserBizLogic insert***************************");
        SignUpUser user = (SignUpUser) obj;
        
        Department department = null;
        Institution institution = null;
        CancerResearchGroup cancerResearchGroup = null;

        List list = dao.retrieve(Department.class.getName(),
                "systemIdentifier", user.getDepartment()
                        .getSystemIdentifier());
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
        
        dao.insert(user,sessionDataBean, true, false);
        
        //Send email to administrator and cc it to the user registered.
        SendEmail email = new SendEmail();

        String adminEmailAddress = ApplicationProperties
                .getValue("email.administrative.emailAddress");
        String technicalSupportEmailAddress = ApplicationProperties
                .getValue("email.technicalSupport.emailAddress");
        String mailServer = ApplicationProperties
                .getValue("email.mailServer");

        String subject = ApplicationProperties.getValue("userRegistration.request.subject");
        String body = "Dear "+ user.getFirstName()+" "+ user.getLastName()+"\n\n"+
        			  ApplicationProperties.getValue("userRegistration.request.body.start") +"\n"+
        			  "\n\n" + ApplicationProperties.getValue("userRegistration.request.body.userDetailsTitle") +
        			  "\n" + ApplicationProperties.getValue("user.loginName")+ Constants.SEPARATOR + user.getLoginName() + 
        			  "\n" + ApplicationProperties.getValue("user.lastName")+ Constants.SEPARATOR + user.getLastName() +
        			  "\n" + ApplicationProperties.getValue("user.firstName")+ Constants.SEPARATOR + user.getFirstName() +
        			  "\n" + ApplicationProperties.getValue("user.street")+ Constants.SEPARATOR + user.getStreet() +
        			  "\n" + ApplicationProperties.getValue("user.city")+ Constants.SEPARATOR + user.getCity() +
        			  "\n" + ApplicationProperties.getValue("user.zipCode")+ Constants.SEPARATOR + user.getZipCode() +
        			  "\n" + ApplicationProperties.getValue("user.state")+ Constants.SEPARATOR + user.getState() +
        			  "\n" + ApplicationProperties.getValue("user.country")+ Constants.SEPARATOR + user.getCountry() +
        			  "\n" + ApplicationProperties.getValue("user.phoneNumber")+ Constants.SEPARATOR + user.getPhoneNumber() +
        			  "\n" + ApplicationProperties.getValue("user.faxNumber")+ Constants.SEPARATOR + user.getFaxNumber() +
        			  "\n" + ApplicationProperties.getValue("user.emailAddress")+ Constants.SEPARATOR + user.getEmailAddress() +
        			  "\n" + ApplicationProperties.getValue("user.institution")+ Constants.SEPARATOR + institution.getName() +
        			  "\n" + ApplicationProperties.getValue("user.department")+ Constants.SEPARATOR + department.getName() +
        			  "\n" + ApplicationProperties.getValue("user.cancerResearchGroup")+ Constants.SEPARATOR + cancerResearchGroup.getName() +
        			  "\n\n\t" + ApplicationProperties.getValue("userRegistration.request.body.end") + 
        			  "\n\n" + ApplicationProperties.getValue("email.catissuecore.team");
        
        boolean emailStatus = email.sendmail(adminEmailAddress, user.getEmailAddress(), null,
                technicalSupportEmailAddress, mailServer, subject, body);

        if (emailStatus)
        {
            Logger.out.info(ApplicationProperties
                    .getValue("userRegistration.email.success")
                    + user.getFirstName()
                    + " " + user.getLastName());
        }
        else
        {
            Logger.out.info(ApplicationProperties
                    .getValue("userRegistration.email.failure")
                    + user.getFirstName()
                    + " " + user.getLastName());
        }
    }

    /**
     * Updates the persistent object in the database.
     * @param obj The object to be updated.
     * @param session The session in which the object is saved.
     * @throws DAOException 
     */
    protected void update(DAO dao, Object obj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
    {
        Logger.out.debug("IN SignUpUserBizLogic update***************************");
        SignUpUser user = (SignUpUser) obj;
        dao.update(user, sessionDataBean, true,true);
        
    }
}
