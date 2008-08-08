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

import edu.wustl.catissuecore.domain.Password;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.UserDTO;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SecurityDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.PasswordEncryptionException;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.SMTransactionException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

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
		/**
		 * Start: Change for API Search   --- Jitendra 06/10/2006
		 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
		 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
		 * So we removed default class level initialization on domain object and are initializing in method
		 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
		 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
		 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
		 */
		ApiSearchUtil.setUserDefault(user);
		//End:- Change for API Search    

		gov.nih.nci.security.authorization.domainobjects.User csmUser = 
			new gov.nih.nci.security.authorization.domainobjects.User();

		try
		{
			dao.update(user.getAddress(), sessionDataBean, true, true, false);
			//If the activity status is Active, create a csm user.
			if (Constants.ACTIVITY_STATUS_ACTIVE.equals(user.getActivityStatus()))
			{
				approveUser(user, csmUser, dao, sessionDataBean );
			}
			else
			{
				dao.update(user, sessionDataBean, true, true, false);
			}

			//Audit of User Update during approving user.
			User oldUser = (User) oldObj;
			dao.audit(user.getAddress(), oldUser.getAddress(),sessionDataBean,true);
			dao.audit(obj, oldObj,sessionDataBean,true);


			EmailHandler emailHandler = new EmailHandler(); 

			//If user is approved send approval and login details emails to the user and administrator.
			if (Constants.ACTIVITY_STATUS_ACTIVE.equals(user.getActivityStatus()))
			{
				//Send approval email to the user and administrator.
				emailHandler.sendApprovalEmail(user);
			}
			else if (Constants.ACTIVITY_STATUS_REJECT.equals(user.getActivityStatus()))
			{
				//If user is rejected send rejection email to the user and administrator.
				//Send rejection email to the user and administrator.
				emailHandler.sendRejectionEmail(user);
			}
		}
		catch(DAOException daoExp)
		{
			Logger.out.debug(daoExp.getMessage(), daoExp);
			new UserBizLogic().deleteCSMUser(csmUser);
			throw new DAOException(daoExp.getMessage(), daoExp);
		}
		catch (SMException exp)
		{
			Logger.out.debug(exp.getMessage(), exp);
			new UserBizLogic().deleteCSMUser(csmUser);
			throw new DAOException(exp.getMessage(), exp);
		}
		catch (PasswordEncryptionException e)
		{
			new UserBizLogic().deleteCSMUser(csmUser);
			throw new DAOException(e.getMessage(), e);
		}
	}


	/**
	 * Populates CsmUser data & creates Protection elements for User
	 * 
	 * @param user
	 * @param csmUser
	 * @throws SMTransactionException
	 * @throws SMException
	 * @throws PasswordEncryptionException
	 * @throws DAOException 
	 */
	private void approveUser(User user, gov.nih.nci.security.authorization.domainobjects.User csmUser, DAO dao, SessionDataBean sessionDataBean) throws SMTransactionException, SMException, PasswordEncryptionException, DAOException {
		csmUser.setLoginName(user.getLoginName());
		csmUser.setLastName(user.getLastName());
		csmUser.setFirstName(user.getFirstName());
		csmUser.setEmailId(user.getEmailAddress());
		csmUser.setStartDate(Calendar.getInstance().getTime());

		String generatedPassword = PasswordManager.generatePassword();

		if (user.getActivityStatus().equals(Constants.ACTIVITY_STATUS_ACTIVE))
		{
			csmUser.setPassword(generatedPassword);
		}


		SecurityManager.getInstance(ApproveUserBizLogic.class).createUser(csmUser);

		if (user.getRoleId() != null)
		{
			SecurityManager.getInstance(ApproveUserBizLogic.class)
			.assignRoleToUser(csmUser.getUserId().toString(), user.getRoleId());
		}

		user.setCsmUserId(csmUser.getUserId());

		Password password = new Password(PasswordManager.encrypt(generatedPassword),user);
		user.getPasswordCollection().add(password);

		Logger.out.debug("password stored in passwore table");

		PrivilegeManager privilegeManager = PrivilegeManager.getInstance();

		Set protectionObjects=new HashSet();
		protectionObjects.add(user);

		privilegeManager.insertAuthorizationData(
				getAuthorizationData(user), protectionObjects, null, user.getObjectId());

//		SecurityManager.getInstance(this.getClass()).insertAuthorizationData(
//		getAuthorizationData(user), protectionObjects, null);

		dao.update(user, sessionDataBean, true, true, true);
	}

	/**
	 * This method returns collection of UserGroupRoleProtectionGroup objects that speciefies the 
	 * user group protection group linkage through a role. It also specifies the groups the protection  
	 * elements returned by this class should be added to.
	 * @return
	 */
	private Vector getAuthorizationData(AbstractDomainObject obj) throws SMException
	{
		Vector authorizationData = new Vector();
		Set group = new HashSet();
		SecurityDataBean userGroupRoleProtectionGroupBean;
		String protectionGroupName;
		Collection coordinators;
		User aUser = (User)obj;
		String userId = String.valueOf(aUser.getCsmUserId());
		gov.nih.nci.security.authorization.domainobjects.User user = 
			SecurityManager.getInstance(this.getClass()).getUserById(userId);
		Logger.out.debug(" User: "+user.getLoginName());
		group.add(user);

		// Protection group of PI
		protectionGroupName = Constants.getUserPGName(aUser.getId());
		userGroupRoleProtectionGroupBean = new SecurityDataBean();
		userGroupRoleProtectionGroupBean.setUser(userId);
		userGroupRoleProtectionGroupBean.setRoleName(Roles.UPDATE_ONLY);
		userGroupRoleProtectionGroupBean.setGroupName(Constants.getUserGroupName(aUser.getId()));
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
//		try
//		{
			// Get the caTISSUE user.
			userList = super.retrieve(className, colName, colValue);

			edu.wustl.catissuecore.domain.User appUser = null;
			if (!userList.isEmpty())
			{
				appUser = (edu.wustl.catissuecore.domain.User) userList.get(0);

//				if (appUser.getCsmUserId() != null)
//				{
//					//Get the role of the user.
//					Role role = SecurityManager.getInstance(ApproveUserBizLogic.class)
//					.getUserRole(appUser.getCsmUserId().longValue());
//					if (role != null)
//					{
//						appUser.setRoleId(role.getId().toString());
//					}
//				}
			}
//		}
//		catch (SMException smExp)
//		{
//			throw handleSMException(smExp);
//		}

		return userList; 
	}
	
	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.AbstractDAO, java.lang.Object)
	 */
	public String getObjectId(AbstractDAO dao, Object domainObject, SessionDataBean sessionDataBean) 
	{
		User user = null;
		
		try 
		{
			user = (User) dao.retrieve(User.class.getName(), sessionDataBean.getUserId());
		} 
		catch (DAOException e) 
		{
			Logger.out.debug(e.getMessage(), e);
		}
		
			Collection<Site> siteCollection = user.getSiteCollection();
		
			StringBuffer sb = new StringBuffer();
			boolean hasUserProvisioningPrivilege = false;
			
			if (siteCollection != null && !siteCollection.isEmpty())
			{
				sb.append(Constants.SITE_CLASS_NAME);
				for (Site site : siteCollection)
				{
					if (site.getId()!=null)
					{
						sb.append(Constants.UNDERSCORE).append(site.getId());
						hasUserProvisioningPrivilege = true;
					}
				}
			}
			if(hasUserProvisioningPrivilege)
			{
				return sb.toString();
			}
			
		return null;
	}
	
	/**
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	protected String getPrivilegeKey(Object domainObject)
    {
	   	return Constants.ADD_EDIT_USER;
    }
    
	/**
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.common.dao.AbstractDAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
	public boolean isAuthorized(AbstractDAO dao, Object domainObject, SessionDataBean sessionDataBean)  
	{
		if(sessionDataBean != null && sessionDataBean.isAdmin())
		{
			return true;
		}
		
		boolean isAuthorized = false;
		
		String privilegeName = getPrivilegeName(domainObject);
		String protectionElementName = getObjectId(dao, domainObject, sessionDataBean);
		PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(sessionDataBean.getUserName());
		 
		if (protectionElementName != null)
		{
			String [] prArray = protectionElementName.split(Constants.UNDERSCORE);
			String baseObjectId = prArray[0];
			StringBuffer objId = new StringBuffer();
			
    		for (int i = 1 ; i < prArray.length;i++)
    		{
    			objId.append(baseObjectId).append(Constants.UNDERSCORE).append(prArray[i]);
    			isAuthorized = privilegeCache.hasPrivilege(objId.toString(),privilegeName);
    			if (!isAuthorized)
    			{
    				break;
    			}
    		}
    		
    		return isAuthorized;		
		}
		else
		{
			return false;
		}		
	}		
}