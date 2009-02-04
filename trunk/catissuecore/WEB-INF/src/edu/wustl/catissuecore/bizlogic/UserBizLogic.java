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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.Password;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SecurityDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * UserBizLogic is used to add user information into the database using Hibernate.
 * @author kapil_kaveeshwar
 */
public class UserBizLogic extends DefaultBizLogic
{

	public static final int FAIL_SAME_AS_LAST_N = 8;
	public static final int FAIL_FIRST_LOGIN = 9;
	public static final int FAIL_EXPIRE = 10;
	public static final int FAIL_CHANGED_WITHIN_SOME_DAY = 11;
	public static final int FAIL_SAME_NAME_SURNAME_EMAIL = 12;
	public static final int FAIL_PASSWORD_EXPIRED = 13;
	
	public static final int SUCCESS = 0;

	/**
	 * Saves the user object in the database.
	 * @param obj The user object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{

		User user = (User) obj;
		gov.nih.nci.security.authorization.domainobjects.User csmUser = new gov.nih.nci.security.authorization.domainobjects.User();

		try
		{
			List list = dao.retrieve(Department.class.getName(), Constants.SYSTEM_IDENTIFIER, user.getDepartment().getId());
			Department department = null;
			if (list.size() != 0)
			{
				department = (Department) list.get(0);
			}

			list = dao.retrieve(Institution.class.getName(), Constants.SYSTEM_IDENTIFIER, user.getInstitution().getId());
			Institution institution = null;
			if (list.size() != 0)
			{
				institution = (Institution) list.get(0);
			}

			list = dao.retrieve(CancerResearchGroup.class.getName(), Constants.SYSTEM_IDENTIFIER, user.getCancerResearchGroup().getId());
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
				{
					SecurityManager.getInstance(UserBizLogic.class).assignRoleToUser(csmUser.getUserId().toString(), user.getRoleId());
				}

				user.setCsmUserId(csmUser.getUserId());
				//					                 user.setPassword(csmUser.getPassword());
				//Add password of user in password table.Updated by Supriya Dankh		 
				Password password = new Password();
				
				/**
				 * Start: Change for API Search   --- Jitendra 06/10/2006
				 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
				 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
				 * So we removed default class level initialization on domain object and are initializing in method
				 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
				 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
				 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
				 */
		        ApiSearchUtil.setPasswordDefault(password);
		        //End:-  Change for API Search 

				password.setUser(user);
				password.setPassword(csmUser.getPassword());
				password.setUpdateDate(new Date());

				user.getPasswordCollection().add(password);

				Logger.out.debug("password stored in passwore table");

				// user.setPassword(csmUser.getPassword());            
			}
			
			/**
			 *  First time login is always set to true when a new user is created
			 */
			user.setFirstTimeLogin(new Boolean(true));

			// Create address and the user in catissue tables.
			dao.insert(user.getAddress(), sessionDataBean, true, false);
			dao.insert(user, sessionDataBean, true, false);

			Set protectionObjects = new HashSet();
			protectionObjects.add(user);

			EmailHandler emailHandler = new EmailHandler();
			// Send the user registration email to user and the administrator.
			if (Constants.PAGEOF_SIGNUP.equals(user.getPageOf()))
			{
				SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, protectionObjects, null);

				emailHandler.sendUserSignUpEmail(user);
			}
			else
			// Send the user creation email to user and the administrator.
			{
				SecurityManager.getInstance(this.getClass()).insertAuthorizationData(getAuthorizationData(user), protectionObjects, null);

				emailHandler.sendApprovalEmail(user);
			}
		}
		catch (DAOException daoExp)
		{
			Logger.out.debug(daoExp.getMessage(), daoExp);
			deleteCSMUser(csmUser);
			throw daoExp;
		}
		catch (SMException e)
		{
			// added to format constrainviolation message
			deleteCSMUser(csmUser);
			throw handleSMException(e);
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
				SecurityManager.getInstance(ApproveUserBizLogic.class).removeUser(csmUser.getUserId().toString());
			}
		}
		catch (SMException smExp)
		{
			throw handleSMException(smExp);
		}
	}

	/**
	 * This method returns collection of UserGroupRoleProtectionGroup objects that speciefies the 
	 * user group protection group linkage through a role. It also specifies the groups the protection  
	 * elements returned by this class should be added to.
	 * @return
	 */
	private Vector getAuthorizationData(AbstractDomainObject obj) throws SMException
	{
		Logger.out.debug("--------------- In here ---------------");

		Vector authorizationData = new Vector();
		Set group = new HashSet();
		User aUser = (User) obj;

		String userId = String.valueOf(aUser.getCsmUserId());
		gov.nih.nci.security.authorization.domainobjects.User user = SecurityManager.getInstance(this.getClass()).getUserById(userId);
		Logger.out.debug(" User: " + user.getLoginName());
		group.add(user);

		// Protection group of User
		String protectionGroupName = Constants.getUserPGName(aUser.getId());
		SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
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
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		User user = (User) obj;
		User oldUser = (User) oldObj;
		
		boolean isLoginUserUpdate = false;
		if(sessionDataBean.getUserName().equals(oldUser.getLoginName())) 
		{
			isLoginUserUpdate = true;
		}
		
		//If the user is rejected, its record cannot be updated.
		if (Constants.ACTIVITY_STATUS_REJECT.equals(oldUser.getActivityStatus()))
		{
			throw new DAOException(ApplicationProperties.getValue("errors.editRejectedUser"));
		}
		else if (Constants.ACTIVITY_STATUS_NEW.equals(oldUser.getActivityStatus())
				|| Constants.ACTIVITY_STATUS_PENDING.equals(oldUser.getActivityStatus()))
		{
			//If the user is not approved yet, its record cannot be updated.
			throw new DAOException(ApplicationProperties.getValue("errors.editNewPendingUser"));
		}

		try
		{
			// Get the csm userId if present. 
			String csmUserId = null;
			
			/**
			 * Santosh: Changes done for Api
			 * User should not edit the first time login field.
			 */
			if (user.getFirstTimeLogin() == null)
			{
				throw new DAOException(ApplicationProperties.getValue("domain.object.null.err.msg","First Time Login"));
			}
			
			if(oldUser.getFirstTimeLogin() != null && user.getFirstTimeLogin().booleanValue() != oldUser.getFirstTimeLogin().booleanValue())
			{
				throw new DAOException(ApplicationProperties.getValue("errors.cannotedit.firsttimelogin"));
			}
			
			if (user.getCsmUserId() != null)
			{
				csmUserId = user.getCsmUserId().toString();
			}

			gov.nih.nci.security.authorization.domainobjects.User csmUser = SecurityManager.getInstance(UserBizLogic.class).getUserById(csmUserId);

			// If the page is of change password, 
			// update the password of the user in csm and catissue tables. 
			if (user.getPageOf().equals(Constants.PAGEOF_CHANGE_PASSWORD))
			{
				if (!user.getOldPassword().equals(PasswordManager.decode(csmUser.getPassword())))
				{
					throw new DAOException(ApplicationProperties.getValue("errors.oldPassword.wrong"));
				}

				//Added for Password validation by Supriya Dankh.
				Validator validator = new Validator();
				if (!validator.isEmpty(user.getNewPassword()) && !validator.isEmpty(user.getOldPassword()))
				{
					int result = validatePassword(oldUser, user.getNewPassword(), user.getOldPassword());

					Logger.out.debug("return from Password validate " + result);

					//if validatePassword method returns value greater than zero then validation fails
					if (result != SUCCESS)
					{
						// get error message of validation failure 
						String errorMessage = getPasswordErrorMsg(result);

						Logger.out.debug("Error Message from method" + errorMessage);
						throw new DAOException(errorMessage);
					}
				}
				csmUser.setPassword(PasswordManager.encode(user.getNewPassword()));

				// Set values in password domain object and adds changed password in Password Collection
				Password password = new Password(csmUser.getPassword(), user);
				user.getPasswordCollection().add(password);
							
			}
			
			//Bug-1516: Jitendra Administartor should be able to edit the password 
			else if(user.getPageOf().equals(Constants.PAGEOF_USER_ADMIN) && !user.getNewPassword().equals(PasswordManager.decode(csmUser.getPassword())))
			{				
				Validator validator = new Validator();
				if (!validator.isEmpty(user.getNewPassword()))
				{
					int result = validatePassword(oldUser, user.getNewPassword(), user.getOldPassword());

					Logger.out.debug("return from Password validate " + result);

					//if validatePassword method returns value greater than zero then validation fails
					if (result != SUCCESS)
					{
						// get error message of validation failure 
						String errorMessage = getPasswordErrorMsg(result);

						Logger.out.debug("Error Message from method" + errorMessage);
						throw new DAOException(errorMessage);
					}
				}
				csmUser.setPassword(PasswordManager.encode(user.getNewPassword()));
				// Set values in password domain object and adds changed password in Password Collection
				Password password = new Password(csmUser.getPassword(), user);
				user.getPasswordCollection().add(password);
				user.setFirstTimeLogin(new Boolean(true));
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
					SecurityManager.getInstance(UserBizLogic.class).assignRoleToUser(csmUser.getUserId().toString(), user.getRoleId());
				}
			
				dao.update(user.getAddress(), sessionDataBean, true, false, false);

				// Audit of user address.
				dao.audit(user.getAddress(), oldUser.getAddress(), sessionDataBean, true);
			}
			
			if (user.getPageOf().equals(Constants.PAGEOF_CHANGE_PASSWORD)) 
			{
			    user.setFirstTimeLogin(new Boolean(false));
			}
			dao.update(user, sessionDataBean, true, true, true);  
			
			//Modify the csm user.
			SecurityManager.getInstance(UserBizLogic.class).modifyUser(csmUser);
			
			if(isLoginUserUpdate)
			{
				sessionDataBean.setUserName(csmUser.getLoginName());
			}

			//Audit of user.
			dao.audit(obj, oldObj, sessionDataBean, true);

			if (Constants.ACTIVITY_STATUS_ACTIVE.equals(user.getActivityStatus()))
			{
				Set protectionObjects = new HashSet();
				protectionObjects.add(user);
				SecurityManager.getInstance(this.getClass()).insertAuthorizationData(getAuthorizationData(user), protectionObjects, null);
			}
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}
	}

	/**
	 * Returns the list of NameValueBeans with name as "LastName,Firstname" 
	 * and value as systemtIdentifier, of all users who are not disabled. 
	 * @return the list of NameValueBeans with name as "LastName,Firstname" 
	 * and value as systemtIdentifier, of all users who are not disabled.
	 * @throws DAOException
	 */
	public Vector getUsers(String operation) throws DAOException
	{
		String sourceObjectName = User.class.getName();
		String[] selectColumnName = null;
		String[] whereColumnName;
		String[] whereColumnCondition;
		Object[] whereColumnValue;
		String joinCondition;
		if (operation != null && operation.equalsIgnoreCase(Constants.ADD))
		{
			String tmpArray1[] = {Constants.ACTIVITY_STATUS};
			String tmpArray2[] = {"="};
			String tmpArray3[] = {Constants.ACTIVITY_STATUS_ACTIVE};
			whereColumnName = tmpArray1;
			whereColumnCondition = tmpArray2;
			whereColumnValue = tmpArray3;
			joinCondition = null;
		}
		else
		{
			String tmpArray1[] = {Constants.ACTIVITY_STATUS, Constants.ACTIVITY_STATUS};
			String tmpArray2[] = {"=", "="};
			String tmpArray3[] = {Constants.ACTIVITY_STATUS_ACTIVE, Constants.ACTIVITY_STATUS_CLOSED};
			whereColumnName = tmpArray1;
			whereColumnCondition = tmpArray2;
			whereColumnValue = tmpArray3;
			joinCondition = Constants.OR_JOIN_CONDITION;

			//					     		whereColumnName = {Constants.ACTIVITY_STATUS,Constants.ACTIVITY_STATUS};
			//					     		whereColumnCondition = {"=","="};
			//					     		whereColumnValue = {Constants.ACTIVITY_STATUS_ACTIVE,Constants.ACTIVITY_STATUS_CLOSED };
			//					     		joinCondition = Constants.OR_JOIN_CONDITION ;

		}

		//Retrieve the users whose activity status is not disabled.
		List users = retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);

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
				nameValueBean.setName(user.getLastName() + ", " + user.getFirstName());
				nameValueBean.setValue(String.valueOf(user.getId()));
				Logger.out.debug(nameValueBean.toString() + " : " + user.getActivityStatus());
				nameValuePairs.add(nameValueBean);
			}
		}
		Collections.sort(nameValuePairs);
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
				gov.nih.nci.security.authorization.domainobjects.User user = (gov.nih.nci.security.authorization.domainobjects.User) users.get(i);
				NameValueBean nameValueBean = new NameValueBean();
				nameValueBean.setName(user.getLastName() + ", " + user.getFirstName());
				nameValueBean.setValue(String.valueOf(user.getUserId()));
				Logger.out.debug(nameValueBean.toString());
				nameValuePairs.add(nameValueBean);
			}
		}

		Collections.sort(nameValuePairs);
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
		Logger.out.debug("In user biz logic retrieve........................");
		try
		{
			// Get the caTISSUE user.
			userList = super.retrieve(className, colName, colValue);

			User appUser = null;
			if (userList!=null && !userList.isEmpty())
			{
				appUser = (User) userList.get(0);

				if (appUser.getCsmUserId() != null)
				{
					//Get the role of the user.
					Role role = SecurityManager.getInstance(UserBizLogic.class).getUserRole(appUser.getCsmUserId().longValue());
					//Logger.out.debug("In USer biz logic.............role........id......." + role.getId().toString());

					if (role != null)
					{
						appUser.setRoleId(role.getId().toString());
					}
				}
			}
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}

		return userList;
	}

	/**
	 * Retrieves and sends the login details email to the user whose email address is passed 
	 * else returns the error key in case of an error.  
	 * @param emailAddress the email address of the user whose password is to be sent.
	 * @return the error key in case of an error.
	 * @throws DAOException
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 * @throws UserNotAuthorizedException
	 */
	public String sendForgotPassword(String emailAddress,SessionDataBean sessionData) throws DAOException, UserNotAuthorizedException 
	{
		String statusMessageKey = null;
		List list = retrieve(User.class.getName(), "emailAddress", emailAddress);
		if (list!=null && !list.isEmpty())
		{
			User user = (User) list.get(0);
			if (user.getActivityStatus().equals(Constants.ACTIVITY_STATUS_ACTIVE))
			{
				EmailHandler emailHandler = new EmailHandler();
				

				//Send the login details email to the user.
				boolean emailStatus = false;
				try
				{
					emailStatus = emailHandler.sendLoginDetailsEmail(user, null);
				}
				catch (DAOException e)
				{
						e.printStackTrace();
				}
				if (emailStatus)
				{
					// if success commit 
					/**
					 *  Update the field FirstTimeLogin which will ensure user changes his password on login
					 *  Note --> We can not use CommonAddEditAction to update as the user has not still logged in
					 *  and user authorisation will fail. So writing saperate code for update. 
					 */
					
					user.setFirstTimeLogin(new Boolean(true));
					AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
					dao.openSession(sessionData);
			    	dao.update(user, sessionData, true, true, true);
					dao.commit();
			        dao.closeSession();
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
    	
    	
		//Added by Ashish Gupta
		/*
		if (user == null)
			throw new DAOException("domain.object.null.err.msg", new String[]{"User"});
			*/
		//END	
		if (Constants.PAGEOF_CHANGE_PASSWORD.equals(user.getPageOf()) == false)
		{
			if (!Validator.isEnumeratedValue(CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_STATE_LIST, null), user
					.getAddress().getState()))
			{
				throw new DAOException(ApplicationProperties.getValue("state.errMsg"));
			}

			if (!Validator.isEnumeratedValue(CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COUNTRY_LIST, null), user
					.getAddress().getCountry()))
			{
				throw new DAOException(ApplicationProperties.getValue("country.errMsg"));
			}

			if (Constants.PAGEOF_USER_ADMIN.equals(user.getPageOf()))
			{
				try
				{
					if (!Validator.isEnumeratedValue(getRoles(), user.getRoleId()))
					{
						throw new DAOException(ApplicationProperties.getValue("user.role.errMsg"));
					}
				}
				catch (SMException e)
				{
					throw handleSMException(e);
				}

				if (operation.equals(Constants.ADD))
				{
					if (!Constants.ACTIVITY_STATUS_ACTIVE.equals(user.getActivityStatus()))
					{
						throw new DAOException(ApplicationProperties.getValue("activityStatus.active.errMsg"));
					}
				}
				else
				{
					if (!Validator.isEnumeratedValue(Constants.USER_ACTIVITY_STATUS_VALUES, user.getActivityStatus()))
					{
						throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
					}
				}
			}
			
			//Added by Ashish
			apiValidate(user);
			//END
		}
		return true;
	}
	
	//Added by Ashish
	/**
	 * @param user user
	 * @return 
	 * @throws DAOException
	 */
	
	private boolean apiValidate(User user)
					throws DAOException
	{
		Validator validator = new Validator();
		String message = "";
		boolean validate = true;
		
		if (validator.isEmpty(user.getEmailAddress()))
		{
			message = ApplicationProperties.getValue("user.emailAddress");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));		
			
		}
		else
		{
			if (!validator.isValidEmailAddress(user.getEmailAddress()))
			{
				message = ApplicationProperties.getValue("user.emailAddress");
				throw new DAOException(ApplicationProperties.getValue("errors.item.format",message));	
			}
		}
		if (validator.isEmpty(user.getLastName()))
		{
			message = ApplicationProperties.getValue("user.lastName");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}

		if (validator.isEmpty(user.getFirstName()))
		{
			message = ApplicationProperties.getValue("user.firstName");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}

		if (validator.isEmpty(user.getAddress().getCity()))
		{
			message = ApplicationProperties.getValue("user.city");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}

		if (!validator.isValidOption(user.getAddress().getState()) || validator.isEmpty(user.getAddress().getState()))
		{
			message = ApplicationProperties.getValue("user.state");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}

		if (validator.isEmpty(user.getAddress().getZipCode()))
		{
			message = ApplicationProperties.getValue("user.zipCode");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}
		else
		{
			if (!validator.isValidZipCode(user.getAddress().getZipCode()))
			{
				message = ApplicationProperties.getValue("user.zipCode");
				throw new DAOException(ApplicationProperties.getValue("errors.item.format",message));	
			}
		}
		
		if (!validator.isValidOption(user.getAddress().getCountry()) || validator.isEmpty(user.getAddress().getCountry()))
		{
			message = ApplicationProperties.getValue("user.country");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}

		if (validator.isEmpty(user.getAddress().getPhoneNumber()))
		{
			message = ApplicationProperties.getValue("user.phoneNumber");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}
		
		if (user.getInstitution().getId()==null || user.getInstitution().getId().longValue()<=0)
		{
			message = ApplicationProperties.getValue("user.institution");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}

		if (user.getDepartment().getId()==null || user.getDepartment().getId().longValue()<=0)
		{
			message = ApplicationProperties.getValue("user.department");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}

		if (user.getCancerResearchGroup().getId()==null || user.getCancerResearchGroup().getId().longValue()<=0)
		{
			message = ApplicationProperties.getValue("user.cancerResearchGroup");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}
		
		if (user.getRoleId() != null)
		{
			if (!validator.isValidOption(user.getRoleId()) || validator.isEmpty(String.valueOf(user.getRoleId())))
			{
				message = ApplicationProperties.getValue("user.role");
				throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
			}
		}
		return validate;
	}
	
	//END
	/**
	 * Returns a list of all roles that can be assigned to a user.
	 * @return a list of all roles that can be assigned to a user.
	 * @throws SMException
	 */
	private List getRoles() throws SMException
	{
		//Sets the roleList attribute to be used in the Add/Edit User Page.
		Vector roleList = SecurityManager.getInstance(UserBizLogic.class).getRoles();

		List roleNameValueBeanList = new ArrayList();
		NameValueBean nameValueBean = new NameValueBean();
		nameValueBean.setName(Constants.SELECT_OPTION);
		nameValueBean.setValue("-1");
		roleNameValueBeanList.add(nameValueBean);

		ListIterator iterator = roleList.listIterator();
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

	/**
	 * @param oldUser User object
	 * @param newPassword New Password value
	 * @param validator VAlidator object
	 * @param oldPassword Old Password value
	 * @return SUCCESS (constant int 0) if all condition passed 
	 *   else return respective error code (constant int) value  
	 */

	private int validatePassword(User oldUser, String newPassword, String oldPassword)
	{
		List oldPwdList = new ArrayList(oldUser.getPasswordCollection());
		Collections.sort(oldPwdList);
		if (oldPwdList != null && !oldPwdList.isEmpty())
		{
			//Check new password is equal to last n password if value
			if (checkPwdNotSameAsLastN(newPassword, oldPwdList))
			{
				Logger.out.debug("Password is not valid returning FAIL_SAME_AS_LAST_N");
				return FAIL_SAME_AS_LAST_N;
			}

			//Get the last updated date of the password
			Date lastestUpdateDate = ((Password) oldPwdList.get(0)).getUpdateDate();
			boolean firstTimeLogin = false;
			if(oldUser.getFirstTimeLogin() != null)
			{
				firstTimeLogin = oldUser.getFirstTimeLogin().booleanValue();
			}
			if (!firstTimeLogin) 
			{
			if (checkPwdUpdatedOnSameDay(lastestUpdateDate))
			{
				Logger.out.debug("Password is not valid returning FAIL_CHANGED_WITHIN_SOME_DAY");
				return FAIL_CHANGED_WITHIN_SOME_DAY;
			}
			}

			/**	
			 * to check password does not contain user name,surname,email address.  if same return FAIL_SAME_NAME_SURNAME_EMAIL
			 *  eg. username=XabcY@abc.com newpassword=abc is not valid
			 */

			String emailAddress = oldUser.getEmailAddress();
			int usernameBeforeMailaddress = emailAddress.indexOf('@');
			// get substring of emailAddress before '@' character    
			emailAddress = emailAddress.substring(0, usernameBeforeMailaddress);
			String userFirstName = oldUser.getFirstName();
			String userLastName = oldUser.getLastName();
	      			
			StringBuffer sb = new StringBuffer(newPassword);
			if (emailAddress != null && newPassword.toLowerCase().indexOf(emailAddress.toLowerCase())!=-1)
			{
				Logger.out.debug("Password is not valid returning FAIL_SAME_NAME_SURNAME_EMAIL");
				return FAIL_SAME_NAME_SURNAME_EMAIL; 
			}
			
			if (userFirstName != null && newPassword.toLowerCase().indexOf(userFirstName.toLowerCase())!=-1)
			{
				Logger.out.debug("Password is not valid returning FAIL_SAME_NAME_SURNAME_EMAIL");
				return FAIL_SAME_NAME_SURNAME_EMAIL; 
			}
			
			if (userLastName != null && newPassword.toLowerCase().indexOf(userLastName.toLowerCase())!=-1)
			{
				Logger.out.debug("Password is not valid returning FAIL_SAME_NAME_SURNAME_EMAIL");
				return FAIL_SAME_NAME_SURNAME_EMAIL; 
			}
			
		}
		return SUCCESS;
	}
	
	
	/**
	 * This function checks whether user has logged in for first time or whether user's password is expired. 
	 * In both these case user needs to change his password so Error key is returned
	 * @param user - user object
	 * @throws DAOException - throws DAOException
	 */
	public String checkFirstLoginAndExpiry(User user) 
	{
		List passwordList = new ArrayList(user.getPasswordCollection());
		
		boolean firstTimeLogin = false;
		if(user.getFirstTimeLogin() != null)
		{
			firstTimeLogin = user.getFirstTimeLogin().booleanValue();
		}
		// If user has logged in for the first time, return key of Change password on first login
		if (firstTimeLogin) 
		{
		  	return "errors.changePassword.changeFirstLogin";
		}
		
		Collections.sort(passwordList);
		Password lastPassword = (Password)passwordList.get(0);
		Date lastUpdateDate = lastPassword.getUpdateDate();
		
		Validator validator = new Validator();
		//Get difference in days between last password update date and current date.
		long dayDiff = validator.getDateDiff(lastUpdateDate, new Date());
		int expireDaysCount = Integer.parseInt(XMLPropertyHandler.getValue("password.expire_after_n_days"));
		if (dayDiff > expireDaysCount)
		{
			return "errors.changePassword.expire";
    	}
		return Constants.SUCCESS;
		
	}

	private boolean checkPwdNotSameAsLastN(String newPassword, List oldPwdList)
	{
		int noOfPwdNotSameAsLastN = 0;
		String pwdNotSameAsLastN = XMLPropertyHandler.getValue("password.not_same_as_last_n");
		if (pwdNotSameAsLastN != null && !pwdNotSameAsLastN.equals(""))
		{
			noOfPwdNotSameAsLastN = Integer.parseInt(pwdNotSameAsLastN);
			noOfPwdNotSameAsLastN = Math.max(0, noOfPwdNotSameAsLastN);
		}

		boolean isSameFound = false;
		int loopCount = Math.min(oldPwdList.size(), noOfPwdNotSameAsLastN);
		for (int i = 0; i < loopCount; i++)
		{
			Password pasword = (Password) oldPwdList.get(i);
			if (newPassword.equals(PasswordManager.decode(pasword.getPassword())))
			{
				isSameFound = true;
				break;
			}
		}
		return isSameFound;
	}

	private boolean checkPwdUpdatedOnSameDay(Date lastUpdateDate)
	{
		Validator validator = new Validator();
		//Get difference in days between last password update date and current date.
		long dayDiff = validator.getDateDiff(lastUpdateDate, new Date());
		int dayDiffConstant = Integer.parseInt(XMLPropertyHandler.getValue("daysCount"));
		if (dayDiff <= dayDiffConstant)
		{
			Logger.out.debug("Password is not valid returning FAIL_CHANGED_WITHIN_SOME_DAY");
			return true;
		}
		return false;
	}

	/**
	 * @param errorCode int value return by validatePassword() method
	 * @return String error message with respect to error code 
	 */
	private String getPasswordErrorMsg(int errorCode)
	{
		String errMsg = "";
		switch (errorCode)
		{
			case FAIL_SAME_AS_LAST_N :
				List parameters = new ArrayList();
				String dayCount = "" + Integer.parseInt(XMLPropertyHandler.getValue("password.not_same_as_last_n"));
				parameters.add(dayCount);				
				errMsg = ApplicationProperties.getValue("errors.newPassword.sameAsLastn",parameters);
				break;
			case FAIL_FIRST_LOGIN :
				errMsg = ApplicationProperties.getValue("errors.changePassword.changeFirstLogin");
				break;
			case FAIL_EXPIRE :
				errMsg = ApplicationProperties.getValue("errors.changePassword.expire");
				break;
			case FAIL_CHANGED_WITHIN_SOME_DAY :
				errMsg = ApplicationProperties.getValue("errors.changePassword.afterSomeDays");
				break;
			case FAIL_SAME_NAME_SURNAME_EMAIL :
				errMsg = ApplicationProperties.getValue("errors.changePassword.sameAsNameSurnameEmail");
				break;	
			case FAIL_PASSWORD_EXPIRED :
				errMsg = ApplicationProperties.getValue("errors.changePassword.expire");
			default :
				errMsg = PasswordManager.getErrorMessage(errorCode);
				break;
		}
		return errMsg;
	}

	//					     //method to return a comma seperated list of emails of administrators of a particular institute
	//					     
	//					     private String getInstitutionAdmins(Long instID) throws DAOException,SMException 
	//					     {
	//					     	String retStr="";
	//					     	String[] userEmail;
	//					     	
	//					     	Long[] csmAdminIDs = SecurityManager.getInstance(UserBizLogic.class).getAllAdministrators() ; 
	//					     	  if (csmAdminIDs != null )
	//					     	  {
	//					         	for(int cnt=0;cnt<csmAdminIDs.length ;cnt++  )
	//					         	{
	//					             	String sourceObjectName = User.class.getName();
	//					             	String[] selectColumnName = null;
	//					             	String[] whereColumnName = {"institution","csmUserId"};
	//					                 String[] whereColumnCondition = {"=","="};
	//					                 Object[] whereColumnValue = {instID, csmAdminIDs[cnt] };
	//					                 String joinCondition = Constants.AND_JOIN_CONDITION;
	//					         		
	//					                 //Retrieve the users for given institution and who are administrators.
	//					                 List users = retrieve(sourceObjectName, selectColumnName, whereColumnName,
	//					                         whereColumnCondition, whereColumnValue, joinCondition);
	//					                 
	//					                 if(!users.isEmpty() )
	//					                 {
	//					                 	User adminUser = (User)users.get(0);
	//					                 	retStr = retStr + "," + adminUser.getEmailAddress();
	//					                 	Logger.out.debug(retStr);
	//					                 }
	//					         	}
	//					         	retStr = retStr.substring(retStr.indexOf(",")+1 );
	//					         	Logger.out.debug(retStr);
	//					     	  }
	//					     	return retStr;
	//					     }
	//					     
}