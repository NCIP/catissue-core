/**
 * <p>Title: UserBizLogic Class>
 * <p>Description:	UserHDAO is used to add user information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 13, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.action.DomainObjectListAction;
import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.GeneratePassword;
import edu.wustl.catissuecore.util.global.SendEmail;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SecurityDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.PasswordEncoderDecoder;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * UserHDAO is used to add user information into the database using Hibernate.
 * @author kapil_kaveeshwar
 */
public class UserBizLogic extends DefaultBizLogic
{

    /**
     * Saves the user object in the database.
     * @param obj The user object to be saved.
     * @param session The session in which the object is saved.
     * @throws DAOException
     */
    protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
            throws DAOException, UserNotAuthorizedException
    {
        
        User user = (User) obj;
        
        Department department = null;
        Institution institution = null;
        CancerResearchGroup cancerResearchGroup = null;
        
        try
        {
            
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
            
            if (user.getPageOf().equals(Constants.PAGEOF_SIGNUP) == false)
            {
                gov.nih.nci.security.authorization.domainobjects.User csmUser = new gov.nih.nci.security.authorization.domainobjects.User();

                csmUser.setLoginName(user.getLoginName());
                csmUser.setLastName(user.getLastName());
                csmUser.setFirstName(user.getFirstName());
                csmUser.setEmailId(user.getEmailAddress());
                csmUser.setStartDate(user.getStartDate());
                csmUser.setPassword(PasswordEncoderDecoder.encode(GeneratePassword
                        .getPassword()));
                Logger.out.debug("Password generated:" + csmUser.getPassword());
                
                SecurityManager.getInstance(UserBizLogic.class).createUser(csmUser);
                
                if (user.getRoleId() != null)
                    SecurityManager.getInstance(UserBizLogic.class)
                            .assignRoleToUser(csmUser.getLoginName(),
                                    user.getRoleId());
                
                user.setCsmUserId(csmUser.getUserId());
                user.setPassword(csmUser.getPassword());
            }
            
            dao.insert(user.getAddress(), sessionDataBean, true, false);
            dao.insert(user, sessionDataBean, true, false);
            
            Set protectionObjects=new HashSet();
            protectionObjects.add(user);
    	    try
            {
                SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,protectionObjects,null);
            }
            catch (SMException e)
            {
                Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
            }
            
            //Send email to administrator and cc it to the user registered.
            SendEmail email = new SendEmail();
            
            String adminEmailAddress = ApplicationProperties
                    .getValue("email.administrative.emailAddress");
            String technicalSupportEmailAddress = ApplicationProperties
                    .getValue("email.technicalSupport.emailAddress");
            String mailServer = ApplicationProperties
                    .getValue("email.mailServer");
            
            String subjectKey = "userRegistration.request.subject";
            String emailBodyStartKey = "userRegistration.request.body.start";
            String emailBodyEndKey = "userRegistration.request.body.end";
            
            if (Constants.PAGEOF_USER_ADMIN.equals(user.getPageOf()))
            {
                subjectKey = "createUser.subject";
                emailBodyStartKey = "createUser.body.start";
                emailBodyEndKey = null;
            }
            
            String subject = ApplicationProperties.getValue(subjectKey);
            
            String body = "Dear "+ user.getFirstName()+" "+ user.getLastName()+"\n\n"+
						  ApplicationProperties.getValue(emailBodyStartKey) +"\n"+
						  "\n\n" + ApplicationProperties.getValue("user.loginName")+ Constants.SEPARATOR + user.getLoginName() + 
						  "\n\n" + ApplicationProperties.getValue("user.lastName")+ Constants.SEPARATOR + user.getLastName() +
						  "\n\n" + ApplicationProperties.getValue("user.firstName")+ Constants.SEPARATOR + user.getFirstName() +
						  "\n\n" + ApplicationProperties.getValue("user.street")+ Constants.SEPARATOR + user.getAddress().getStreet() +
						  "\n\n" + ApplicationProperties.getValue("user.city")+ Constants.SEPARATOR + user.getAddress().getCity() +
						  "\n\n" + ApplicationProperties.getValue("user.zipCode")+ Constants.SEPARATOR + user.getAddress().getZipCode() +
						  "\n\n" + ApplicationProperties.getValue("user.state")+ Constants.SEPARATOR + user.getAddress().getState() +
						  "\n\n" + ApplicationProperties.getValue("user.country")+ Constants.SEPARATOR + user.getAddress().getCountry() +
						  "\n\n" + ApplicationProperties.getValue("user.phoneNumber")+ Constants.SEPARATOR + user.getAddress().getPhoneNumber() +
						  "\n\n" + ApplicationProperties.getValue("user.faxNumber")+ Constants.SEPARATOR + user.getAddress().getFaxNumber() +
						  "\n\n" + ApplicationProperties.getValue("user.emailAddress")+ Constants.SEPARATOR + user.getEmailAddress() +
						  "\n\n" + ApplicationProperties.getValue("user.institution")+ Constants.SEPARATOR + institution.getName() +
						  "\n\n" + ApplicationProperties.getValue("user.department")+ Constants.SEPARATOR + department.getName() +
						  "\n\n" + ApplicationProperties.getValue("user.cancerResearchGroup")+ Constants.SEPARATOR + cancerResearchGroup.getName();
            
            if (Constants.PAGEOF_USER_ADMIN.equals(user.getPageOf()))
            {
                body = body + "\n\n" + ApplicationProperties.getValue("userRegistration.loginDetails")
				  + "\n\tLogin Name : " + user.getLoginName()
				  + "\n\tPassword : " + PasswordEncoderDecoder.decode(user.getPassword());
            }
            
            if (emailBodyEndKey != null)
            {
                body = body + "\n\n\t" + ApplicationProperties.getValue(emailBodyEndKey);
            }

            body = body + "\n\n" + "\n\n" + ApplicationProperties.getValue("email.catissuecore.team");
            
            boolean emailStatus = email.sendmail(user
                    .getEmailAddress(), adminEmailAddress, null, technicalSupportEmailAddress,
                    mailServer, subject, body);

            if (emailStatus)
            {
                Logger.out.info(ApplicationProperties
                        .getValue("userRegistration.email.success")
                        + user.getFirstName() + " " + user.getLastName());
            }
            else
            {
                Logger.out.info(ApplicationProperties
                        .getValue("userRegistration.email.failure")
                        + user.getFirstName() + " " + user.getLastName());
            }
        }
        catch (SMException smex)
        {
            Logger.out.debug("Exception in CSM user creation:"
                    + smex.getMessage(), smex);
            throw new DAOException(smex.getCause().getMessage(),smex);
        }  
    }
    
    /**
     * This method returns collection of UserGroupRoleProtectionGroup objects that speciefies the 
     * user group protection group linkage through a role. It also specifies the groups the protection  
     * elements returned by this class should be added to.
     * @return
     */
    public Vector getAuthorizationData(AbstractDomainObject obj)
    {
        Logger.out.debug("--------------- In here ---------------");
        Vector authorizationData = new Vector();
        Set group = new HashSet();
        SecurityDataBean userGroupRoleProtectionGroupBean;
        String protectionGroupName;
        gov.nih.nci.security.authorization.domainobjects.User user ;
        Collection coordinators;
        User aUser = (User)obj;
        String userId = String.valueOf(aUser.getCsmUserId());
        try
        {
            user = new gov.nih.nci.security.authorization.domainobjects.User();
            user = SecurityManager.getInstance(this.getClass()).getUserById(userId);
            Logger.out.debug(" User: "+user.getLoginName());
            group.add(user);
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
        
        // Protection group of PI
        protectionGroupName = Constants.getUserPGName(aUser.getSystemIdentifier());
        userGroupRoleProtectionGroupBean = new SecurityDataBean();
        userGroupRoleProtectionGroupBean.setUser(userId);
        userGroupRoleProtectionGroupBean.setRoleName(Roles.UPDATE_ONLY);
        userGroupRoleProtectionGroupBean.setGroupName(Constants.getUserGroupName(aUser.getSystemIdentifier()));
        userGroupRoleProtectionGroupBean.setProtectionGroupName(protectionGroupName);
        userGroupRoleProtectionGroupBean.setGroup(group);
        authorizationData.add(userGroupRoleProtectionGroupBean);
        
        Logger.out.debug(authorizationData.toString());
        return authorizationData;
    }

    /**
     * Updates the persistent object in the database.
     * @param obj The object to be updated.
     * @param session The session in which the object is saved.
     * @throws DAOException 
     */
    protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
            throws DAOException, UserNotAuthorizedException
    {
        User user = (User) obj;
        
        try
        {
            String csmUserId = null;
            if (user.getCsmUserId() != null)
            {
                csmUserId = user.getCsmUserId().toString(); 
            }
            
            Logger.out.debug("In UserBizLogic.......................csmUserId........."+csmUserId);
            gov.nih.nci.security.authorization.domainobjects.User csmUser = SecurityManager
                    .getInstance(DomainObjectListAction.class).getUserById(csmUserId);

            if (user.getPageOf().equals(Constants.PAGEOF_CHANGE_PASSWORD))
            {
                if (!user.getOldPassword().equals(PasswordEncoderDecoder.decode(csmUser.getPassword())))
                {
                    throw new DAOException(ApplicationProperties.getValue("errors.oldPassword.wrong"));
                }
                
                csmUser.setPassword(PasswordEncoderDecoder.encode(user.getPassword()));
                user.setPassword(csmUser.getPassword());
            }
            else
            {
                csmUser.setLoginName(user.getLoginName());
                csmUser.setLastName(user.getLastName());
                csmUser.setFirstName(user.getFirstName());
                csmUser.setEmailId(user.getEmailAddress());
                
                
                if ((Constants.PAGEOF_USER_PROFILE.equals(user.getPageOf()) == false)
                        && (Constants.PAGEOF_CHANGE_PASSWORD.equals(user.getPageOf()) == false))
                {
                    SecurityManager.getInstance(UserBizLogic.class).assignRoleToUser(
                            csmUser.getLoginName(), user.getRoleId());
                }
                
                dao.update(user.getAddress(), sessionDataBean, true, false, false);
                
                //Audit of user address.
                User oldUser = (User) oldObj;
                dao.audit(user.getAddress(), oldUser.getAddress(),sessionDataBean,true);
            }
            
	        dao.update(user, sessionDataBean, true, true, true);
	        
	        //Audit of user.
            dao.audit(obj, oldObj,sessionDataBean,true);
            
            SecurityManager.getInstance(UserBizLogic.class).modifyUser(csmUser);
            
            if (Constants.ACTIVITY_STATUS_ACTIVE.equals(user.getActivityStatus()))
            {
                Set protectionObjects=new HashSet();
                protectionObjects.add(user);
        	    try
                {
                    SecurityManager.getInstance(this.getClass()).insertAuthorizationData(getAuthorizationData(user),protectionObjects,null);
                }
                catch (SMException e)
                {
                    Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
                }
            }
        }
        catch (SMException smExp)
        {
            throw new DAOException(smExp.getMessage(), smExp);
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

    public Vector getUsers() throws DAOException
    {
    	String sourceObjectName = User.class.getName();
    	String[] selectColumnName = null;
    	String[] whereColumnName = {"activityStatus"};
        String[] whereColumnCondition = {"!="};
        Object[] whereColumnValue = {Constants.ACTIVITY_STATUS_DISABLED};
        String joinCondition = Constants.AND_JOIN_CONDITION;
		
        List users = retrieve(sourceObjectName, selectColumnName, whereColumnName,
                whereColumnCondition, whereColumnValue, joinCondition);
				
        Vector nameValuePairs = new Vector();
        nameValuePairs.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
        
        // Set CSM users for the user objects retrieved
        User user = null;
        if (users != null)
        {

            // Creating name value beans based on Activity Status passed 
            NameValueBean nameValueBean;

            for (int i = 0; i < users.size(); i++)
            {
                user = (User) users.get(i);
                nameValueBean = new NameValueBean();
                nameValueBean.setName(user.getLastName() + ", "
                        + user.getFirstName());
                nameValueBean.setValue(String.valueOf(user
                        .getSystemIdentifier()));
                Logger.out.debug(nameValueBean.toString());
                nameValuePairs.add(nameValueBean);
            }
        }
        return nameValuePairs;
    }

    //    public static void main(String args[])
    //    {
    //        String [] whereColumnName = {"country"};
    //        String [] whereColumnCondition = {"LIKE"};
    //        String [] whereColumnValue = {"United States"};
    //        
    //        try
    //        {
    //            List list = retrieve(Address.class.getName(),whereColumnName,
    //                    whereColumnCondition,whereColumnValue,null);
    //            
    //            System.out.println("LIST SIZE..............."+list.size());
    //        }
    //        catch(DAOException daoExp)
    //        {
    //            daoExp.printStackTrace();
    //        }
    //        
    //    }
    
    public List retrieve(String className, String colName, Object colValue) throws DAOException
    {
        List userList = null;
        try
        {
            // Get the caTISSUE user.
            userList = super.retrieve(className, colName, colValue);

            edu.wustl.catissuecore.domain.User appUser = null;
            if (!userList.isEmpty())
            {
                appUser = (edu.wustl.catissuecore.domain.User) userList.get(0);
                
                if (appUser.getCsmUserId() != null)
                {
                    //Get the role of the user.
                    Role role = SecurityManager.getInstance(UserBizLogic.class)
                    					.getUserRole(appUser.getCsmUserId().longValue());
                    if (role != null)
                    {
                        appUser.setRoleId(role.getId().toString());
                    }
                }
            }
        }
        catch (SMException smExp)
        {
            Logger.out.debug(smExp.getMessage(), smExp);
            throw new DAOException(smExp.getMessage(), smExp);
        }
        
        return userList; 
    }
}