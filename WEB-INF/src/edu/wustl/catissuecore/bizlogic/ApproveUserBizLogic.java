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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.domain.Password;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.UserDTO;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.exception.PasswordEncryptionException;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.beans.SecurityDataBean;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.exception.UserNotAuthorizedException;
import edu.wustl.security.manager.SecurityManagerFactory;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 * ApproveUserBizLogic is the bizLogic class for approve users.
 * @author gautam_shetty
 */
public class ApproveUserBizLogic  extends CatissueDefaultBizLogic
{

	private transient Logger logger = Logger.getCommonLogger(ApproveUserBizLogic.class);
	/**
	 * Overrides the insert method of DefaultBizLogic. 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) 
	throws BizLogicException
	{
		User user = null;        
		UserDTO userDTO = null;

		if(obj instanceof UserDTO)
		{
			userDTO = (UserDTO) obj;
			user = userDTO.getUser(); 
		}
		else
		{
			user = (User) obj;
		}
		UserBizLogic objUserBizlogic = new UserBizLogic();
		objUserBizlogic.validate(user, dao, Constants.EDIT);
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
			dao.update(user.getAddress());
			//If the activity status is Active, create a csm user.
			if (Status.ACTIVITY_STATUS_ACTIVE.equals(user.getActivityStatus()))
			{
				approveUser(obj, csmUser, dao, sessionDataBean );
			}
			else
			{
				dao.update(user);
			}

			//Audit of User Update during approving user.
			User oldUser = (User) oldObj;
			((HibernateDAO)dao).audit(user.getAddress(), oldUser.getAddress());
			((HibernateDAO)dao).audit(obj, oldObj);


			EmailHandler emailHandler = new EmailHandler(); 

			//If user is approved send approval and login details emails to the user and administrator.
			if (Status.ACTIVITY_STATUS_ACTIVE.equals(user.getActivityStatus()))
			{
				//Send approval email to the user and administrator.
				emailHandler.sendApprovalEmail(user);
			}
			else if (Status.ACTIVITY_STATUS_REJECT.equals(user.getActivityStatus()))
			{
				//If user is rejected send rejection email to the user and administrator.
				//Send rejection email to the user and administrator.
				emailHandler.sendRejectionEmail(user);
			}
		}
		catch(Exception exp)
		{
			
			logger.debug(exp.getMessage(), exp);
			new UserBizLogic().deleteCSMUser(csmUser);
			ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
			throw new BizLogicException(errorKey,exp ,"ApproveUserBizLogic.java :"); 
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
	 * @throws BizLogicException 
	 */
	private void approveUser(Object user1, gov.nih.nci.security.authorization.domainobjects.User csmUser,
			DAO dao, SessionDataBean sessionDataBean) throws BizLogicException, DAOException, SMException, PasswordEncryptionException {
		
		User user = null;
		UserDTO userDTO = null;
		Map<String,SiteUserRolePrivilegeBean> userRowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>();
		
		if(user1 instanceof UserDTO)
		{
			userDTO = (UserDTO) user1;
			user = userDTO.getUser();
			userRowIdMap = userDTO.getUserRowIdBeanMap();
		}
		else
		{
			user = (User) user1;
		}
		// Method to populate rowIdMap in case, Add Privilege button is not clicked
        userRowIdMap = new UserBizLogic().getUserRowIdMap(user, userRowIdMap);
		
		csmUser.setLoginName(user.getLoginName());
		csmUser.setLastName(user.getLastName());
		csmUser.setFirstName(user.getFirstName());
		csmUser.setEmailId(user.getEmailAddress());
		csmUser.setStartDate(Calendar.getInstance().getTime());

		String generatedPassword = PasswordManager.generatePassword();

		if (user.getActivityStatus().equals(Status.ACTIVITY_STATUS_ACTIVE.toString()))
		{
			csmUser.setPassword(generatedPassword);
		}

		SecurityManagerFactory.getSecurityManager().createUser(csmUser);

		String role = "";
		if (user.getRoleId() != null)
		{
            if (user.getRoleId().equalsIgnoreCase(Constants.SUPER_ADMIN_USER))
            {
            	role = Constants.ADMIN_USER;
            }
            else
            {
            	role = Constants.NON_ADMIN_USER;
            }
            
            SecurityManagerFactory.getSecurityManager().assignRoleToUser(csmUser.getUserId().toString(), role);
		}

		user.setCsmUserId(csmUser.getUserId());

		Password password = new Password(PasswordManager.encrypt(generatedPassword),user);
		user.getPasswordCollection().add(password);

		Logger.out.debug("password stored in passwore table");

		PrivilegeManager privilegeManager = PrivilegeManager.getInstance();

		Set protectionObjects=new HashSet();
		protectionObjects.add(user);

		if(userRowIdMap != null && !userRowIdMap.isEmpty())
		{
			new UserBizLogic().updateUserDetails(user,userRowIdMap);
		}
		
		privilegeManager.insertAuthorizationData(
				getAuthorizationData(user, userRowIdMap), protectionObjects, null, user.getObjectId());

//		SecurityManager.getInstance(this.getClass()).insertAuthorizationData(
//		getAuthorizationData(user), protectionObjects, null);

		dao.update(user);
	}

	/**
	 * This method returns collection of UserGroupRoleProtectionGroup objects that speciefies the 
	 * user group protection group linkage through a role. It also specifies the groups the protection  
	 * elements returned by this class should be added to.
	 * @return
	 */
	private Vector getAuthorizationData(AbstractDomainObject obj, Map<String,SiteUserRolePrivilegeBean> userRowIdMap) throws SMException
	{
		Vector authorizationData = new Vector();
		Set group = new HashSet();
		SecurityDataBean userGroupRoleProtectionGroupBean;
		String protectionGroupName;
		Collection coordinators;
		User aUser = (User)obj;
		String userId = String.valueOf(aUser.getCsmUserId());
		gov.nih.nci.security.authorization.domainobjects.User user = 
			SecurityManagerFactory.getSecurityManager().getUserById(userId);
		Logger.out.debug(" User: "+user.getLoginName());
		group.add(user);

		// Protection group of PI
		protectionGroupName = Constants.getUserPGName(aUser.getId());
		userGroupRoleProtectionGroupBean = new SecurityDataBean();
		userGroupRoleProtectionGroupBean.setUser(userId);
		userGroupRoleProtectionGroupBean.setRoleName(Roles.UPDATE_ONLY);
		userGroupRoleProtectionGroupBean.setGroupName(Constants.getUserGroupName(aUser.getId()));
		userGroupRoleProtectionGroupBean.setProtGrpName(protectionGroupName);
		userGroupRoleProtectionGroupBean.setGroup(group);
		authorizationData.add(userGroupRoleProtectionGroupBean);

		Logger.out.debug(authorizationData.toString());
		
		if(userRowIdMap !=null)
		{
			new UserBizLogic().insertCPSitePrivileges(aUser, authorizationData, userRowIdMap);
		}
		
		return authorizationData;
	}

	/**
	 * Returns the list of users according to the column name and value passed.
	 * @return the list of users according to the column name and value passed.
	 */
	public List retrieve(String className, String colName, Object colValue) throws BizLogicException
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
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
	 */
	public String getObjectId(DAO dao, Object domainObject) 
	{
		return new UserBizLogic().getObjectId(dao, domainObject);
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
	 * @throws BizLogicException 
	 * @throws UserNotAuthorizedException 
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.dao.DAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
		boolean isAuthorized = false;
		isAuthorized = checkUser(domainObject, sessionDataBean);
		if(isAuthorized)
		{
			return true;
		}
		
		String privilegeName = getPrivilegeName(domainObject);
		String protectionElementName = getObjectId(dao, domainObject);
		
		
			return AppUtility.returnIsAuthorized(sessionDataBean, privilegeName,
					protectionElementName);
		}
		catch (UserNotAuthorizedException e)
		{
			logger.debug(e.getMessage(), e);
			throw getBizLogicException(e, "sm.operation.error", "User not authorized");
		} catch (BizLogicException e)
		{
			logger.debug(e.getMessage(), e);
			ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
			throw new BizLogicException(errorKey,e ,"ApproveUserBizLogic.java :");	
		}
	}


	private boolean checkUser(Object domainObject, SessionDataBean sessionDataBean) throws BizLogicException 
	{
		return new UserBizLogic().checkUser(domainObject, sessionDataBean);
	}		
}