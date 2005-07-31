/**
 * <p>Title: UserHDAO Class>
 * <p>Description:	UserHDAO is used to add user information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 13, 2005
 */

package edu.wustl.catissuecore.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.action.DomainObjectListAction;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.SendEmail;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * UserHDAO is used to add user information into the database using Hibernate.
 * @author kapil_kaveeshwar
 */
public class UserBizLogic extends DefaultBizLogic
{
    
    /**
     * Saves the user object in the database.
     * @param session The session in which the object is saved.
     * @param obj The user object to be saved.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException
     */
    public void insert(Object obj) throws DAOException
    {
        try
        {
            
            User user = (User) obj;
            
//            boolean userExistsStatus = SecurityManager.
//            		getInstance(UserBizLogic.class).userExists(user.getUser().getLoginName());
            
//            if (userExistsStatus)
//            {
//                return false;
//            }
//            else
//            {
                AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
                dao.openSession();
                
                Department department = null;
                Institution institution = null;
                CancerResearchGroup cancerResearchGroup = null;
                
                List list = dao.retrieve(Department.class.getName(), "name", user
                        .getDepartment().getName());
                if (list.size() != 0)
                {
                    department = (Department) list.get(0);
                }
                
                list = dao.retrieve(Institution.class.getName(), "name", user
                        .getInstitution().getName());
                if (list.size() != 0)
                {
                    institution = (Institution) list.get(0);
                }
                
                list = dao.retrieve(CancerResearchGroup.class.getName(), "name",
                        user.getCancerResearchGroup().getName());
                if (list.size() != 0)
                {
                    cancerResearchGroup = (CancerResearchGroup) list.get(0);
                }
                
                user.setDepartment(department);
                user.setInstitution(institution);
                user.setCancerResearchGroup(cancerResearchGroup);
                
                SecurityManager.getInstance(UserBizLogic.class).createUser(
                        user.getUser());
                
                user.setSystemIdentifier(user.getUser().getUserId());
                
                dao.insert(user.getAddress());
                dao.insert(user);
                
                dao.closeSession();
                
                //Send email to administrator and cc it to the user registered.
                SendEmail email = new SendEmail();
                String separator = " : ";
                
                String subject = ApplicationProperties.getValue("userRegistration.request.subject");
                String body = "Dear "+user.getUser().getFirstName()+" "+user.getUser().getLastName()+"\n\n"+
                			  ApplicationProperties.getValue("userRegistration.request.body.start") +"\n"+
                			  "\n\n" + ApplicationProperties.getValue("userRegistration.request.body.userDetailsTitle") +
                			  "\n" + ApplicationProperties.getValue("user.loginName")+ separator + user.getUser().getLoginName() + 
                			  "\n" + ApplicationProperties.getValue("user.lastName")+ separator + user.getUser().getLastName() +
                			  "\n" + ApplicationProperties.getValue("user.firstName")+ separator + user.getUser().getFirstName() +
                			  "\n" + ApplicationProperties.getValue("user.street")+ separator + user.getAddress().getStreet() +
                			  "\n" + ApplicationProperties.getValue("user.city")+ separator + user.getAddress().getCity() +
                			  "\n" + ApplicationProperties.getValue("user.zipCode")+ separator + user.getAddress().getZipCode() +
                			  "\n" + ApplicationProperties.getValue("user.state")+ separator + user.getAddress().getState() +
                			  "\n" + ApplicationProperties.getValue("user.country")+ separator + user.getAddress().getCountry() +
                			  "\n" + ApplicationProperties.getValue("user.phoneNumber")+ separator + user.getAddress().getPhoneNumber() +
                			  "\n" + ApplicationProperties.getValue("user.faxNumber")+ separator + user.getAddress().getFaxNumber() +
                			  "\n" + ApplicationProperties.getValue("user.emailAddress")+ separator + user.getUser().getEmailId() +
                			  "\n" + ApplicationProperties.getValue("user.institution")+ separator + user.getInstitution().getName() +
                			  "\n" + ApplicationProperties.getValue("user.department")+ separator + user.getDepartment().getName() +
                			  "\n" + ApplicationProperties.getValue("user.cancerResearchGroup")+ separator + user.getCancerResearchGroup().getName() +
                			  "\n\n\t" + ApplicationProperties.getValue("userRegistration.request.body.end") + 
                			  "\n\n" + ApplicationProperties.getValue("email.catissuecore.team");
                
                String adminEmailAddress = ApplicationProperties.getValue("email.administrative.emailAddress");
                String technicalSupportEmailAddress = ApplicationProperties.getValue("email.technicalSupport.emailAddress");
                String mailServer = ApplicationProperties.getValue("email.mailServer");
                
                boolean emailStatus  = email.sendmail(adminEmailAddress,user.getUser().getEmailId(),
                        			   null,technicalSupportEmailAddress,mailServer,subject,body);
                
                if (emailStatus)
                {
                    Logger.out.info(ApplicationProperties.getValue("userRegistration.email.success") + 
                            user.getUser().getFirstName() + " " + user.getUser().getLastName());
                }
                else
                {
                    Logger.out.info(ApplicationProperties.getValue("userRegistration.email.failure") + 
                            		user.getUser().getFirstName() + " " + user.getUser().getLastName());
                }
                
//            }
        }
        catch (SMException smExp)
        {
            throw new DAOException(smExp);
//            smExp.printStackTrace();
        }
    }

    /**
     * Updates the persistent object in the database.
     * @param session The session in which the object is saved.
     * @param obj The object to be updated.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
    public void update(Object obj) throws DAOException
    {
        User user = (User) obj;
        List list = null;
        
        try
        {
            
	        HibernateDAO dao = new HibernateDAO();
	        dao.openSession();
	        
	        gov.nih.nci.security.authorization.domainobjects.User csmUser = 
               	SecurityManager.getInstance(DomainObjectListAction.class).
            		getUserById(String.valueOf(user.getSystemIdentifier()));
	        
	        csmUser.setLoginName(user.getUser().getLoginName());
	        csmUser.setLastName(user.getUser().getLastName());
	        csmUser.setFirstName(user.getUser().getFirstName());
	        csmUser.setEmailId(user.getUser().getEmailId());
	        csmUser.setPassword(user.getUser().getPassword());
            
	        SecurityManager.getInstance(UserBizLogic.class)
	        	.modifyUser(csmUser);
	        
	        SecurityManager.getInstance(UserBizLogic.class)
	        	.assignRoleToUser(csmUser.getLoginName(),user.getRoleId());
	        
	        dao.update(user);
	        dao.closeSession();
	        
	        //Send email to administrator and cc it to the user registered.
            SendEmail email = new SendEmail();
            
            String subject = ApplicationProperties.getValue("userRegistration.approve.subject");
            
            String body = "Dear "+user.getUser().getFirstName()+" "+user.getUser().getLastName()+"\n\n"+
            			  ApplicationProperties.getValue("userRegistration.approved.body.start")+
            			  ApplicationProperties.getValue("userRegistration.loginDetails") + "\n\tLogin Name : "
                          + user.getUser().getLoginName() + "\n\tPassword : " + csmUser.getPassword() +
                          "\n\n" + ApplicationProperties.getValue("email.catissuecore.team");
            
            String adminEmailAddress = ApplicationProperties.getValue("email.administrative.emailAddress");
            String technicalSupportEmailAddress = ApplicationProperties.getValue("email.technicalSupport.emailAddress");
            String mailServer = ApplicationProperties.getValue("email.mailServer");
            
            boolean emailStatus  = email.sendmail(adminEmailAddress,user.getUser().getEmailId(),
                    			   null,technicalSupportEmailAddress,mailServer,subject,body);
            
            if (emailStatus)
            {
                Logger.out.info(ApplicationProperties.getValue("userRegistration.email.success") + 
                        user.getUser().getFirstName() + " " + user.getUser().getLastName());
            }
            else
            {
                Logger.out.info(ApplicationProperties.getValue("userRegistration.email.failure") + 
                        		user.getUser().getFirstName() + " " + user.getUser().getLastName());
            }
        }
        catch(SMException smExp)
        {
            Logger.out.error(smExp.getMessage(),smExp);
        }
    }
   

    
//    public Vector getList(String sourceObjectName, String[] displayNameFields, String valueField, String[] whereColumnName,
//            String[] whereColumnCondition, Object[] whereColumnValue,
//            String joinCondition, String separatorBetweenFields) throws DAOException
//            {
//        		AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
//        		List users = dao.retrieve(User.class.getName());
//        		Vector nameValuePairs = new Vector();
//        		List csmUsers;
//        		try
//                {
//                     csmUsers=SecurityManager.getInstance(UserBizLogic.class).getUsers();
//                }
//                catch (SMException e)
//                {
//                    Logger.out.debug("Unable to get all users: Exception: "+e.getMessage());
//        	        throw new DAOException (e.getMessage(), e);
//                }
//                
//                // Set CSM users for the user objects retrieved
//                User user;
//                gov.nih.nci.security.authorization.domainobjects.User csmUser;
//                for(int i=0;i<users.size();i++)
//                {
//                    user = (User) users.get(i);
//                    for(int j=0; j<csmUsers.size();j++)
//                    {
//                        csmUser =  (gov.nih.nci.security.authorization.domainobjects.User) csmUsers.get(j);
//                        if(csmUser.getUserId() == user.getSystemIdentifier())
//                        {
//                            user.setUser(csmUser);
//                            break;
//                        }
//                    }
//                }
//                
//                
//                return nameValuePairs;
//            }
    
    		public Vector getUsers(String ActivityStatus) throws DAOException
            {
        		List users = retrieve(User.class.getName(),"activityStatus",ActivityStatus);
        		Vector nameValuePairs = new Vector();
                
                // Set CSM users for the user objects retrieved
                User user=null;
                gov.nih.nci.security.authorization.domainobjects.User csmUser = null;
                if(users != null)
                {
	                                
	                // Creating name value beans based on Activity Status passed 
	                NameValueBean nameValueBean; 
                
	                for(int i=0;i<users.size();i++)
	                {
	                    user = (User) users.get(i);
	                    try
	                    {
	                        csmUser = SecurityManager.getInstance(UserBizLogic.class).getUserById(String.valueOf(user.getSystemIdentifier()));
	                    }
	                    catch (SMException e)
	                    {
	                        Logger.out.debug("Unable to get user : "+e.getMessage());
	                        throw new DAOException(e);
	                    }
	                    nameValueBean = new NameValueBean();
	                    nameValueBean.setName(csmUser.getLastName()+", "+csmUser.getFirstName());
	                    nameValueBean.setValue(String.valueOf(user.getSystemIdentifier()));
	                    Logger.out.debug(nameValueBean.toString());
	                    nameValuePairs.add(nameValueBean);
	                    
	                }
                
                
                }
                return nameValuePairs;
            }
    
}