/**
 * <p>Title: ApproveUserBizLogic Class>
 * <p>Description:	ApproveUserBizLogic is the bizLogic class for approve users.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SecurityDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.PasswordManager;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * ApproveUserBizLogic is the bizLogic class for approve users.
 * @author gautam_shetty
 */
public class ApproveUserBizLogic extends DefaultBizLogic
{

    /**
     * Overrides the insert method of DefaultBizLogic. 
     */
    protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) 
    											throws DAOException, UserNotAuthorizedException
    {
        User user = (User) obj;

        gov.nih.nci.security.authorization.domainobjects.User csmUser = 
            					new gov.nih.nci.security.authorization.domainobjects.User();
        
        try
        {
            //If the activity status is Active, create a csm user.
            if (Constants.ACTIVITY_STATUS_ACTIVE.equals(user.getActivityStatus()))
            {
                csmUser.setLoginName(user.getLoginName());
                csmUser.setLastName(user.getLastName());
                csmUser.setFirstName(user.getFirstName());
                csmUser.setEmailId(user.getEmailAddress());
                if (user.getActivityStatus().equals(
                        Constants.ACTIVITY_STATUS_ACTIVE))
                    csmUser.setPassword(PasswordManager.encode(PasswordManager.generatePassword()));
                csmUser.setStartDate(Calendar.getInstance().getTime());
                
                SecurityManager.getInstance(ApproveUserBizLogic.class).createUser(csmUser);
                
                if (user.getRoleId() != null)
                {
                    SecurityManager.getInstance(ApproveUserBizLogic.class)
                            .assignRoleToUser(csmUser.getLoginName(),
                                    user.getRoleId());
                }
                
                user.setCsmUserId(csmUser.getUserId());
                user.setPassword(csmUser.getPassword());
            }
            
            //Update the user record in catissue table.
            dao.update(user.getAddress(), sessionDataBean, true, false, false);
	        dao.update(user, sessionDataBean, true, true, true);
	        
            //Audit of User Update during approving user.
            User oldUser = (User) oldObj;
            dao.audit(user.getAddress(), oldUser.getAddress(),sessionDataBean,true);
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
            
            //If user is approved send approval and login details emails to the user and administrator.
            if (Constants.ACTIVITY_STATUS_ACTIVE.equals(user.getActivityStatus()))
	        {
                EmailHandler emailHandler = new EmailHandler(); 
                
                //Send approval email to the user and administrator.
                emailHandler.sendApprovalEmail(user);
	        }
            else if (Constants.ACTIVITY_STATUS_REJECT.equals(user.getActivityStatus()))
	        {//If user is rejected send rejection email to the user and administrator.
                EmailHandler emailHandler = new EmailHandler();
                
                //Send rejection email to the user and administrator.
                emailHandler.sendRejectionEmail(user);
	        }
        }
        catch (Exception exp)
        {
            Logger.out.debug(exp.getMessage(), exp);
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
                Logger.out.debug(ApplicationProperties.getValue("errors.user.delete")
                        				+smExp.getMessage(), smExp);
                throw new DAOException(smExp.getMessage(), smExp);
            }
            throw new DAOException(ApplicationProperties.getValue("errors.user.approve")
                    					+exp.getMessage(), exp);
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
     * Returns the list of users according to the column name and value passed.
     * @return the list of users according to the column name and value passed.
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
                    Role role = SecurityManager.getInstance(ApproveUserBizLogic.class)
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