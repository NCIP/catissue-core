/**
 * <p>Title: UserBizLogic Class>
 * <p>Description:	UserBizLogic is used to add user information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 13, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.action.DomainObjectListAction;
import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.catissuecore.util.PasswordManager;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SecurityDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * UserBizLogic is used to add user information into the database using Hibernate.
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
        gov.nih.nci.security.authorization.domainobjects.User csmUser = 
            				new gov.nih.nci.security.authorization.domainobjects.User();
        
        try
        {
            List list = dao.retrieve(Department.class.getName(),
                    Constants.SYSTEM_IDENTIFIER, user.getDepartment()
                            .getSystemIdentifier());
            
            Department department = null;
            if (list.size() != 0)
            {
                department = (Department) list.get(0);
            }
            list = dao.retrieve(Institution.class.getName(),
                    Constants.SYSTEM_IDENTIFIER, user.getInstitution()
                            .getSystemIdentifier());
            
            Institution institution = null;
            if (list.size() != 0)
            {
                institution = (Institution) list.get(0);
            }
            
            list = dao.retrieve(CancerResearchGroup.class.getName(),
                    Constants.SYSTEM_IDENTIFIER, user.getCancerResearchGroup()
                            .getSystemIdentifier());
            
            CancerResearchGroup cancerResearchGroup = null;
            if (list.size() != 0)
            {
                cancerResearchGroup = (CancerResearchGroup) list.get(0);
            }
            
            user.setDepartment(department);
            user.setInstitution(institution);
            user.setCancerResearchGroup(cancerResearchGroup);
            
            // If the page is of signup user don't create the csm user.
            if (user.getPageOf().equals(Constants.PAGEOF_SIGNUP) == false)
            {
                csmUser.setLoginName(user.getLoginName());
                csmUser.setLastName(user.getLastName());
                csmUser.setFirstName(user.getFirstName());
                csmUser.setEmailId(user.getEmailAddress());
                csmUser.setStartDate(user.getStartDate());
                csmUser.setPassword(PasswordManager.encode(PasswordManager.generatePassword()));
                
                SecurityManager.getInstance(UserBizLogic.class).createUser(csmUser);
                
                if (user.getRoleId() != null)
                    SecurityManager.getInstance(UserBizLogic.class)
                            .assignRoleToUser(csmUser.getUserId().toString(),
                                    user.getRoleId());
                
                user.setCsmUserId(csmUser.getUserId());
                user.setPassword(csmUser.getPassword());
            }
            
            // Create address and the user in catissue tables.
            dao.insert(user.getAddress(), sessionDataBean, true, false);
            dao.insert(user, sessionDataBean, true, false);
            
            Set protectionObjects=new HashSet();
            protectionObjects.add(user);
            
            // Send the user registration email to user and the administrator.
            if (Constants.PAGEOF_SIGNUP.equals(user.getPageOf()))
            {
                EmailHandler emailHandler = new EmailHandler();
//                String cc = getInstitutionAdmins(user.getInstitution().getSystemIdentifier()  );
                emailHandler.sendUserSignUpEmail(user);
                try
                {
                    SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,protectionObjects,null);
                }
                catch (SMException smExp)
                {
                    Logger.out.error(smExp.getMessage(),smExp);
                }
                
            }
            else// Send the user creation email to user and the administrator.
            {
                EmailHandler emailHandler = new EmailHandler();
                
                emailHandler.sendApprovalEmail(user);
                
               
        	    try
                {
                    SecurityManager.getInstance(this.getClass()).insertAuthorizationData(getAuthorizationData(user),protectionObjects,null);
                }
                catch (SMException smExp)
                {
                    Logger.out.error(smExp.getMessage(),smExp);
                }
            }
        }
        catch(DAOException daoExp)
        {
            Logger.out.debug(daoExp.getMessage(), daoExp);
            deleteCSMUser(csmUser);
            //throw new DAOException(daoExp.getMessage(), daoExp.getWrapException());
            throw daoExp;
        }
        catch (SMException exp)
        {
            Logger.out.debug(exp.getMessage(), exp);
            // added to format constrainviolation message
            deleteCSMUser(csmUser);
            //throw new DAOException(errMsg,(Exception)exp.getCause());
            throw new DAOException(exp.getCause().getMessage(),(Exception)exp.getCause());
            //throw new DAOException(exp.getMessage(), exp);
        }
    }
    
    /**
     * Deletes the csm user from the csm user table.
     * @param csmUser The csm user to be deleted.
     * @throws DAOException
     */
    private void deleteCSMUser(gov.nih.nci.security.authorization.domainobjects.User csmUser) throws DAOException
    {
        try
        {
            if (csmUser.getUserId() != null)
            {
                SecurityManager.getInstance(ApproveUserBizLogic.class)
                					.removeUser(csmUser.getUserId().toString());
            }
        }
        catch(SMException smExp)
        {
            Logger.out.debug(ApplicationProperties.getValue("errors.user.delete")+
                    				smExp.getMessage(), smExp);
            throw new DAOException(smExp.getMessage(), smExp);
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
        User aUser = (User)obj;
        String userId = String.valueOf(aUser.getCsmUserId());
        try
        {
            gov.nih.nci.security.authorization.domainobjects.User user = 
            		SecurityManager.getInstance(this.getClass()).getUserById(userId);
            Logger.out.debug(" User: "+user.getLoginName());
            group.add(user);
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
        
        // Protection group of PI
        String protectionGroupName = Constants.getUserPGName(aUser.getSystemIdentifier());
        SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
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
            // Get the csm userId if present. 
            String csmUserId = null;
            if (user.getCsmUserId() != null)
            {
                csmUserId = user.getCsmUserId().toString(); 
            }
            
            gov.nih.nci.security.authorization.domainobjects.User csmUser = SecurityManager
                				.getInstance(DomainObjectListAction.class).getUserById(csmUserId);
            
            // If the page is of change password, 
            // update the password of the user in csm and catissue tables. 
            if (user.getPageOf().equals(Constants.PAGEOF_CHANGE_PASSWORD))
            {
                if (!user.getOldPassword().equals(PasswordManager.decode(csmUser.getPassword())))
                {
                    throw new DAOException(ApplicationProperties.getValue("errors.oldPassword.wrong"));
                }
                
                csmUser.setPassword(PasswordManager.encode(user.getPassword()));
                user.setPassword(csmUser.getPassword());
            }
            else
            {
                csmUser.setLoginName(user.getLoginName());
                csmUser.setLastName(user.getLastName());
                csmUser.setFirstName(user.getFirstName());
                csmUser.setEmailId(user.getEmailAddress());
                
                // Assign Role only if the page is of Administrative user edit.
                if ((Constants.PAGEOF_USER_PROFILE.equals(user.getPageOf()) == false)
                        && (Constants.PAGEOF_CHANGE_PASSWORD.equals(user.getPageOf()) == false))
                {
                    SecurityManager.getInstance(UserBizLogic.class).assignRoleToUser(
                    		csmUser.getUserId().toString(), user.getRoleId());
                }
                
                dao.update(user.getAddress(), sessionDataBean, true, false, false);
                
                //Audit of user address.
                User oldUser = (User) oldObj;
                dao.audit(user.getAddress(), oldUser.getAddress(),sessionDataBean,true);
            }
            
            // Modify the csm user.
            SecurityManager.getInstance(UserBizLogic.class).modifyUser(csmUser);
            
	        dao.update(user, sessionDataBean, true, true, true);
	        
	        //Audit of user.
            dao.audit(obj, oldObj,sessionDataBean,true);
            
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
//        	 added to format constrainviolation message
           // throw new DAOException(errMsg, (Exception)smExp.getCause());
        	 throw new DAOException(smExp.getCause().getMessage(), (Exception)smExp.getCause());
        	// throw new DAOException(smExp.getMessage(), smExp);
        }
    }
    
    /**
     * Returns the list of NameValueBeans with name as "LastName,Firstname" 
     * and value as systemtIdentifier, of all users who are not disabled. 
     * @return the list of NameValueBeans with name as "LastName,Firstname" 
     * and value as systemtIdentifier, of all users who are not disabled.
     * @throws DAOException
     */
    public Vector getUsers() throws DAOException
    {
    	String sourceObjectName = User.class.getName();
    	String[] selectColumnName = null;
    	String[] whereColumnName = {Constants.ACTIVITY_STATUS,Constants.ACTIVITY_STATUS};
        String[] whereColumnCondition = {"=","="};
        Object[] whereColumnValue = {Constants.ACTIVITY_STATUS_ACTIVE,Constants.ACTIVITY_STATUS_CLOSED };
        String joinCondition = Constants.OR_JOIN_CONDITION ;
		
        //Retrieve the users whose activity status is not disabled.
        List users = retrieve(sourceObjectName, selectColumnName, whereColumnName,
                whereColumnCondition, whereColumnValue, joinCondition);
        
        Vector nameValuePairs = new Vector();
        nameValuePairs.add(new NameValueBean(Constants.SELECT_OPTION, String.valueOf(Constants.SELECT_OPTION_VALUE)));
        
        // If the list of users retrieved is not empty. 
        if (users.isEmpty() == false)
        {
            // Creating name value beans.
            for (int i = 0; i < users.size(); i++)
            {
                User user = (User) users.get(i);
                NameValueBean nameValueBean = new NameValueBean();
                nameValueBean.setName(user.getLastName() + ", "
                        + user.getFirstName());
                nameValueBean.setValue(String.valueOf(user
                        .getSystemIdentifier()));
                Logger.out.debug(nameValueBean.toString() + " : " + user.getActivityStatus() );
                nameValuePairs.add(nameValueBean);
            }
        }
        Collections.sort(nameValuePairs) ;
        return nameValuePairs;
    }
    
    /**
     * Returns the list of NameValueBeans with name as "LastName,Firstname" 
     * and value as systemtIdentifier, of all users who are not disabled. 
     * @return the list of NameValueBeans with name as "LastName,Firstname" 
     * and value as systemtIdentifier, of all users who are not disabled.
     * @throws DAOException
     */
    public Vector getCSMUsers() throws DAOException, SMException
    {
        //Retrieve the users whose activity status is not disabled.
        List users = SecurityManager.getInstance(UserBizLogic.class).getUsers();
        
        Vector nameValuePairs = new Vector();
        nameValuePairs.add(new NameValueBean(Constants.SELECT_OPTION, String.valueOf(Constants.SELECT_OPTION_VALUE)));
        
        // If the list of users retrieved is not empty. 
        if (users.isEmpty() == false)
        {
            // Creating name value beans.
            for (int i = 0; i < users.size(); i++)
            {
                gov.nih.nci.security.authorization.domainobjects.User user 
                			= (gov.nih.nci.security.authorization.domainobjects.User) users.get(i);
                NameValueBean nameValueBean = new NameValueBean();
                nameValueBean.setName(user.getLastName() + ", "
                        + user.getFirstName());
                nameValueBean.setValue(String.valueOf(user
                        .getUserId()));
                Logger.out.debug(nameValueBean.toString());
                nameValuePairs.add(nameValueBean);
            }
        }
        
        Collections.sort(nameValuePairs) ;
        return nameValuePairs;
    }

    /**
     * Returns a list of users according to the column name and value.
     * @param colName column name on the basis of which the user list is to be retrieved.
     * @param colValue Value for the column name.
     * @throws DAOException
     */
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
    
    /**
     * Retrieves and sends the login details email to the user whose email address is passed 
     * else returns the error key in case of an error.  
     * @param emailAddress the email address of the user whose password is to be sent.
     * @return the error key in case of an error.
     * @throws DAOException
     */
    public String sendForgotPassword(String emailAddress) throws DAOException
    {
        String statusMessageKey = null;
        List list = retrieve(User.class.getName(), "emailAddress", emailAddress);
        if (!list.isEmpty())
        {
        	User user = (User) list.get(0);
        	if (user.getActivityStatus().equals(Constants.ACTIVITY_STATUS_ACTIVE))
            {
                EmailHandler emailHandler = new EmailHandler();
                
                //Send the login details email to the user.
                boolean emailStatus = emailHandler.sendLoginDetailsEmail(user, null);
                
                if (emailStatus)
                {
                    statusMessageKey = "password.send.success";
                }
                else
                {
                    statusMessageKey = "password.send.failure";
                }
            }
            else
            {
                //Error key if the user is not active.
                statusMessageKey = "errors.forgotpassword.user.notApproved";
            }            	
        }
        else
        {
        	// Error key if the user is not present.
            statusMessageKey = "errors.forgotpassword.user.unknown";
        }
        return statusMessageKey;
    }
    
    /**
     * Overriding the parent class's method to validate the enumerated attribute values
     */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
    {
		User user = (User)obj;
		
		if(!Validator.isEnumeratedValue(Constants.STATEARRAY,user.getAddress().getState()))
		{
			throw new DAOException(ApplicationProperties.getValue("state.errMsg"));
		}
		
		if(!Validator.isEnumeratedValue(Constants.COUNTRYARRAY,user.getAddress().getCountry()))
		{
			throw new DAOException(ApplicationProperties.getValue("country.errMsg"));
		}
		
		if(Constants.PAGEOF_USER_ADMIN.equals(user.getPageOf()))
		{
			try
			{
				if(!Validator.isEnumeratedValue(getRoles(),user.getRoleId()))
				{
					throw new DAOException(ApplicationProperties.getValue("user.role.errMsg"));
				}
			}
			catch(SMException e)
			{
				Logger.out.debug(e.getMessage(),e);
				throw new DAOException(ApplicationProperties.getValue("user.role.dbErrMsg"));
			}
	
			if(operation.equals(Constants.ADD))
			{
				if(!Constants.ACTIVITY_STATUS_ACTIVE.equals(user.getActivityStatus()))
				{
					throw new DAOException(ApplicationProperties.getValue("activityStatus.active.errMsg"));
				}
			}
			else
			{
				if(!Validator.isEnumeratedValue(Constants.USER_ACTIVITY_STATUS_VALUES,user.getActivityStatus()))
				{
					throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
				}
			}
		}
		
		return true;
    }
	
	/**
     * Returns a list of all roles that can be assigned to a user.
     * @return a list of all roles that can be assigned to a user.
     * @throws SMException
     */
    private List getRoles() throws SMException
    {
        //Sets the roleList attribute to be used in the Add/Edit User Page.
        Vector roleList = SecurityManager.getInstance(UserBizLogic.class).getRoles();
        
        ListIterator iterator = roleList.listIterator();
        
        List roleNameValueBeanList = new ArrayList();
        NameValueBean nameValueBean = new NameValueBean();
        nameValueBean.setName(Constants.SELECT_OPTION);
        nameValueBean.setValue("-1");
        roleNameValueBeanList.add(nameValueBean);
        
        while (iterator.hasNext())
        {
            Role role = (Role) iterator.next();
            nameValueBean = new NameValueBean();
            nameValueBean.setName(role.getName());
            nameValueBean.setValue(String.valueOf(role.getId()));
            roleNameValueBeanList.add(nameValueBean);
        }
        return roleNameValueBeanList;
    }
    
//    //method to return a comma seperated list of emails of administrators of a particular institute
//    
//    private String getInstitutionAdmins(Long instID) throws DAOException,SMException 
//    {
//    	String retStr="";
//    	String[] userEmail;
//    	
//    	Long[] csmAdminIDs = SecurityManager.getInstance(UserBizLogic.class).getAllAdministrators() ; 
//    	  if (csmAdminIDs != null )
//    	  {
//        	for(int cnt=0;cnt<csmAdminIDs.length ;cnt++  )
//        	{
//            	String sourceObjectName = User.class.getName();
//            	String[] selectColumnName = null;
//            	String[] whereColumnName = {"institution","csmUserId"};
//                String[] whereColumnCondition = {"=","="};
//                Object[] whereColumnValue = {instID, csmAdminIDs[cnt] };
//                String joinCondition = Constants.AND_JOIN_CONDITION;
//        		
//                //Retrieve the users for given institution and who are administrators.
//                List users = retrieve(sourceObjectName, selectColumnName, whereColumnName,
//                        whereColumnCondition, whereColumnValue, joinCondition);
//                
//                if(!users.isEmpty() )
//                {
//                	User adminUser = (User)users.get(0);
//                	retStr = retStr + "," + adminUser.getEmailAddress();
//                	Logger.out.debug(retStr);
//                }
//        	}
//        	retStr = retStr.substring(retStr.indexOf(",")+1 );
//        	Logger.out.debug(retStr);
//    	  }
//    	return retStr;
//    }
//    
}