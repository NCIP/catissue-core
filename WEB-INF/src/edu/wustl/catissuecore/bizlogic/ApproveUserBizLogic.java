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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.GeneratePassword;
import edu.wustl.catissuecore.util.global.SendEmail;
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
 * @author gautam_shetty
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ApproveUserBizLogic extends DefaultBizLogic
{

    /**
     * Overrides the insert method of DefaultBizLogic 
     */
    protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
    {
        Logger.out.debug("In Approve user BizLogic ..................");
        User user = (User) obj;
        List list = null;

        try
        {
            //If the activity status is Active, create a csm user.
            if (Constants.ACTIVITY_STATUS_ACTIVE.equals(user.getActivityStatus()))
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

            dao.update(user.getAddress(), sessionDataBean, true, false, false);
	        dao.update(user, sessionDataBean, true, true, true);

	        if (Constants.ACTIVITY_STATUS_ACTIVE.equals(user.getActivityStatus()) 
	                || Constants.ACTIVITY_STATUS_REJECT.equals(user.getActivityStatus()))
	        {
	            //Send email to administrator and cc it to the user registered.
	            SendEmail email = new SendEmail();
	            
	            String subject = ApplicationProperties
	                    .getValue("userRegistration.approve.subject");
	            
	            String body = "Dear " + user.getFirstName()
	                    + " " + user.getLastName()
	                    + "\n\n"+ ApplicationProperties.getValue("userRegistration.approved.body.start")
	                    + ApplicationProperties.getValue("userRegistration.loginDetails")
	                    + "\n\tLogin Name : " + user.getLoginName()
	                    + "\n\tPassword : " + PasswordEncoderDecoder.decode(user.getPassword())
	                    + "\n\n" + ApplicationProperties.getValue("email.catissuecore.team");
	            
	            if (Constants.ACTIVITY_STATUS_REJECT.equals(user.getActivityStatus()))
	            {
	                subject = ApplicationProperties.getValue("userRegistration.reject.subject");
	                
	                body = "Dear " + user.getFirstName()
                    + " " + user.getLastName()
                    + "\n\n"+ ApplicationProperties.getValue("userRegistration.reject.body.start");
	                
	                Logger.out.debug("user.getComments()..................."+user.getComments());
	                
	                if ((user.getComments() != null) 
	                        && ("".equals(user.getComments()) == false))
	                {
	                    body = body + "\n\n" + ApplicationProperties.getValue("userRegistration.reject.comments")
	                    					 + user.getComments();
	                }
                    
                    body = body + "\n\n"+ ApplicationProperties.getValue("userRegistration.reject.body.end")
                    			+ "\n\n" + ApplicationProperties.getValue("email.catissuecore.team");
	            }
	            
	            String adminEmailAddress = ApplicationProperties
	                    .getValue("email.administrative.emailAddress");
	            String technicalSupportEmailAddress = ApplicationProperties
	                    .getValue("email.technicalSupport.emailAddress");
	            String mailServer = ApplicationProperties
	                    .getValue("email.mailServer");
	            
	            boolean emailStatus = email.sendmail(adminEmailAddress, user
	                    .getEmailAddress(), null, technicalSupportEmailAddress,
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
        }
        catch (SMException smex)
        {
            Logger.out.debug("Exception in CSM user creation:"
                    + smex.getMessage(), smex);
            throw new DAOException(smex.getCause().getMessage());
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