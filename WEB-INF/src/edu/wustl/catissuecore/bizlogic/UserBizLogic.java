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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.Password;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.UserDTO;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.exception.PasswordEncryptionException;
import edu.wustl.common.exceptionformatter.DefaultExceptionFormatter;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.beans.SecurityDataBean;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.locator.CSMGroupLocator;
import edu.wustl.security.locator.SecurityManagerPropertiesLocator;
import edu.wustl.security.manager.SecurityManagerFactory;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;
import edu.wustl.security.privilege.PrivilegeUtility;
import edu.wustl.wustlkey.util.global.WUSTLKeyUtility;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;

/**
 * UserBizLogic is used to add user information into the database using Hibernate.
 * @author kapil_kaveeshwar
 */
public class UserBizLogic extends CatissueDefaultBizLogic
{
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	private static Logger logger = Logger.getCommonLogger(UserBizLogic.class);
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
	 * @throws BizLogicException
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		User user = null;
		Map<String, SiteUserRolePrivilegeBean> userRowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>();

		if (obj instanceof UserDTO)
		{
			user = ((UserDTO) obj).getUser();
			userRowIdMap = ((UserDTO) obj).getUserRowIdBeanMap();
		}
		else
		{
			user = (User) obj;
		}
		// Method to populate rowIdMap in case, Add Privilege button is not clicked
		userRowIdMap = this.getUserRowIdMap(user, userRowIdMap);
		final gov.nih.nci.security.authorization.domainobjects.User csmUser = new gov.nih.nci.security.authorization.domainobjects.User();

		try
		{
			Object object = dao.retrieveById(Department.class.getName(), user.getDepartment()
					.getId());
			Department department = null;
			if (object != null)
			{
				department = (Department) object;
			}

			object = dao.retrieveById(Institution.class.getName(), user.getInstitution().getId());
			Institution institution = null;
			if (object != null)
			{
				institution = (Institution) object;
			}

			object = dao.retrieveById(CancerResearchGroup.class.getName(), user
					.getCancerResearchGroup().getId());
			CancerResearchGroup cancerResearchGroup = null;
			if (object != null)
			{
				cancerResearchGroup = (CancerResearchGroup) object;
			}

			user.setDepartment(department);
			user.setInstitution(institution);
			user.setCancerResearchGroup(cancerResearchGroup);
			final String generatedPassword = PasswordManager.generatePassword();

			// If the page is of signup user don't create the csm user.
			if (user.getPageOf().equals(Constants.PAGE_OF_SIGNUP) == false
					|| user.getPageOf() == null)
			{
				csmUser.setLoginName(user.getLoginName());
				csmUser.setLastName(user.getLastName());
				csmUser.setFirstName(user.getFirstName());
				csmUser.setEmailId(user.getEmailAddress());
				csmUser.setStartDate(user.getStartDate());
				csmUser.setPassword(generatedPassword);

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

					SecurityManagerFactory.getSecurityManager().assignRoleToUser(
							csmUser.getUserId().toString(), role);
				}

				user.setCsmUserId(csmUser.getUserId());
				//					                 user.setPassword(csmUser.getPassword());
				//Add password of user in password table.Updated by Supriya Dankh		 
				final Password password = new Password();

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
				password.setPassword(PasswordManager.encrypt(generatedPassword));
				password.setUpdateDate(new Date());

				user.getPasswordCollection().add(password);

				logger.debug("password stored in passwore table");
			}

			/**
			 *  First time login is always set to true when a new user is created
			 */
			user.setFirstTimeLogin(new Boolean(true));

			// Create address and the user in catissue tables.
			dao.insert(user.getAddress());

			if (userRowIdMap != null && !userRowIdMap.isEmpty())
			{
				this.updateUserDetails(user, userRowIdMap);
			}
			dao.insert(user);

			final Set protectionObjects = new HashSet();
			protectionObjects.add(user);

			final EmailHandler emailHandler = new EmailHandler();
			// Send the user registration email to user and the administrator.
			if (Constants.PAGE_OF_SIGNUP.equals(user.getPageOf()))
			{
				//SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, protectionObjects, null);

				emailHandler.sendUserSignUpEmail(user);
			}
			else
			// Send the user creation email to user and the administrator.
			{
				//				SecurityManager.getInstance(this.getClass()).insertAuthorizationData(getAuthorizationData(user), protectionObjects, null);

				final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();

				privilegeManager.insertAuthorizationData(this.getAuthorizationData(user,
						userRowIdMap), protectionObjects, null, user.getObjectId());

				emailHandler.sendApprovalEmail(user);
			}
		}
		catch (final SMException e)
		{
			UserBizLogic.logger.error(e.getMessage(), e);
			e.printStackTrace() ;
			// added to format constrainviolation message
			this.deleteCSMUser(csmUser);
			throw this.getBizLogicException(e, "sm.check.priv", "");
		}
		catch (final PasswordEncryptionException exception)
		{
			UserBizLogic.logger.error(exception.getMessage(), exception);
			this.deleteCSMUser(csmUser);
			throw this.getBizLogicException(exception, "pwd.encrytion.error", "");
		}
		catch (final ApplicationException e)
		{
			UserBizLogic.logger.error(e.getMessage(), e);
			e.printStackTrace() ;
			this.deleteCSMUser(csmUser);
			//ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());

		}
	}

	public Map<String, SiteUserRolePrivilegeBean> getUserRowIdMap(User user,
			Map<String, SiteUserRolePrivilegeBean> userRowIdMap) throws BizLogicException
	{
		if (user.getRoleId() != null && !user.getRoleId().equalsIgnoreCase("-1")
				&& !user.getRoleId().equalsIgnoreCase("0"))
		{
			if (userRowIdMap == null || userRowIdMap.isEmpty() && user.getSiteCollection() != null
					&& !user.getSiteCollection().isEmpty())
			{
				final List<NameValueBean> list = new AssignPrivilegePageBizLogic()
						.getActionsList(user.getRoleId());
				final NameValueBean roleBean = new NameValueBean();
				try
				{
					final List<Role> roleList = SecurityManagerFactory.getSecurityManager()
							.getRoles();
					roleBean.setValue(user.getRoleId());
					for (final Role role : roleList)
					{
						if (role.getId().toString().equalsIgnoreCase(user.getRoleId()))
						{
							roleBean.setName(role.getName());
							break;
						}
					}
				}
				catch (final SMException e)
				{
					UserBizLogic.logger.error(e.getMessage(), e);
					e.printStackTrace() ;
					throw this.getBizLogicException(e, "user.roleNotFound", "");
				}
				int i = 0;
				userRowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>();
				for (final Site site : user.getSiteCollection())
				{
					final List<Site> siteList = new ArrayList<Site>();
					siteList.add(site);
					final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = new SiteUserRolePrivilegeBean();
					siteUserRolePrivilegeBean.setAllCPChecked(true);
					siteUserRolePrivilegeBean.setPrivileges(list);
					siteUserRolePrivilegeBean.setRole(roleBean);
					siteUserRolePrivilegeBean.setSiteList(siteList);
					userRowIdMap.put(new Integer(i).toString(), siteUserRolePrivilegeBean);
					i++;
				}
			}
		}
		return userRowIdMap;
	}

	/**
	 * Deletes the csm user from the csm user table.
	 * @param csmUser The csm user to be deleted.
	 * @throws BizLogicException
	 */
	public void deleteCSMUser(gov.nih.nci.security.authorization.domainobjects.User csmUser)
			throws BizLogicException
	{
		try
		{
			if (csmUser.getUserId() != null)
			{
				SecurityManagerFactory.getSecurityManager().removeUser(
						csmUser.getUserId().toString());
			}
		}
		catch (final SMException smExp)
		{
			UserBizLogic.logger.error(smExp.getMessage(), smExp);
			smExp.printStackTrace();
			throw this.getBizLogicException(smExp, "sm.operation.error",
					"Error in checking has privilege");
		}
	}

	/**
	 * This method returns collection of UserGroupRoleProtectionGroup objects that speciefies the 
	 * user group protection group linkage through a role. It also specifies the groups the protection  
	 * elements returned by this class should be added to.
	 * @return
	 */
	private Vector getAuthorizationData(AbstractDomainObject obj,
			Map<String, SiteUserRolePrivilegeBean> userRowIdMap) throws SMException
	{
		final Vector authorizationData = new Vector();
		final Set group = new HashSet();
		final User aUser = (User) obj;

		final String userId = String.valueOf(aUser.getCsmUserId());
		final gov.nih.nci.security.authorization.domainobjects.User user = SecurityManagerFactory
				.getSecurityManager().getUserById(userId);
		this.logger.debug(" User: " + user.getLoginName());
		group.add(user);

		// Protection group of User
		final String protectionGroupName = Constants.getUserPGName(aUser.getId());
		final SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
		userGroupRoleProtectionGroupBean.setUser(userId);
		userGroupRoleProtectionGroupBean.setRoleName(Roles.UPDATE_ONLY);
		userGroupRoleProtectionGroupBean.setGroupName(Constants.getUserGroupName(aUser.getId()));
		userGroupRoleProtectionGroupBean.setProtGrpName(protectionGroupName);
		userGroupRoleProtectionGroupBean.setGroup(group);
		authorizationData.add(userGroupRoleProtectionGroupBean);

		this.logger.debug(authorizationData.toString());

		if (userRowIdMap != null)
		{
			this.insertCPSitePrivileges(aUser, authorizationData, userRowIdMap);
		}

		return authorizationData;
	}

	public void insertCPSitePrivileges(User user1, Vector authorizationData,
			Map<String, SiteUserRolePrivilegeBean> userRowIdMap)
	{
		if (userRowIdMap == null || userRowIdMap.isEmpty())
		{
			return;
		}

		final Map<String, SiteUserRolePrivilegeBean> cpPrivilegeMap = new HashMap<String, SiteUserRolePrivilegeBean>();
		final Map<String, SiteUserRolePrivilegeBean> sitePrivilegeMap = new HashMap<String, SiteUserRolePrivilegeBean>();

		final Object[] mapKeys = userRowIdMap.keySet().toArray();
		for (final Object mapKey : mapKeys)
		{
			final String key = mapKey.toString();
			final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = userRowIdMap.get(key);
			siteUserRolePrivilegeBean.setUser(user1);
			if (siteUserRolePrivilegeBean.isAllCPChecked())
			{
				final Map<String, SiteUserRolePrivilegeBean> map = AppUtility
						.splitBeanData(siteUserRolePrivilegeBean);
				final SiteUserRolePrivilegeBean bean1 = map.get("SITE");
				final SiteUserRolePrivilegeBean bean2 = map.get("CP");
				userRowIdMap.remove(key);
				if (bean1.getPrivileges().isEmpty())
				{
					bean1.setPrivileges(bean2.getPrivileges());
				}
				userRowIdMap.put(key, bean1);
				if (bean2.getPrivileges().isEmpty())
				{
					bean2.setPrivileges(bean1.getPrivileges());
				}
				userRowIdMap.put(key + "All_CurrentnFuture_CPs", bean2);
			}
		}

		this.distributeMapData(userRowIdMap, cpPrivilegeMap, sitePrivilegeMap);

		/* For SITE Privileges Purpose */
		this.insertSitePrivileges(user1, authorizationData, sitePrivilegeMap);

		/* For CP Privileges Purpose */
		this.insertCPPrivileges(user1, authorizationData, cpPrivilegeMap);

		/* Common for both */
		//updateUserDetails(user1, userRowIdMap);
	}

	public void updateUserDetails(User user1, Map<String, SiteUserRolePrivilegeBean> userRowIdMap)
	{
		final Set<Site> siteCollection = new HashSet<Site>();
		final Set<CollectionProtocol> cpCollection = new HashSet<CollectionProtocol>();
		final Set<CollectionProtocol> removedCpCollection = new HashSet<CollectionProtocol>();
		for (final String key : userRowIdMap.keySet())
		{
			final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = userRowIdMap.get(key);

			final CollectionProtocol cp = siteUserRolePrivilegeBean.getCollectionProtocol();
			if (cp != null && !siteUserRolePrivilegeBean.isRowDeleted())
			{
				cpCollection.add(cp);
			}
			else if (cp != null && siteUserRolePrivilegeBean.isRowDeleted())
			{
				removedCpCollection.add(cp);
			}

			List<Site> siteList = null;

			if (!siteUserRolePrivilegeBean.isRowDeleted())
			{
				siteList = siteUserRolePrivilegeBean.getSiteList();

				if (siteList != null && !siteList.isEmpty())
				{
					for (final Site site : siteList)
					{
						boolean isPresent = false;
						for (final Site site1 : siteCollection)
						{
							if (site1.getId().equals(site.getId()))
							{
								isPresent = true;
							}
						}
						if (!isPresent)
						{
							siteCollection.add(site);
						}
					}
				}
			}
		}

		user1.getSiteCollection().clear();
		user1.getSiteCollection().addAll(siteCollection);
		this.updateCollectionProtocolCollection(user1, cpCollection, removedCpCollection);
	}

	private void updateCollectionProtocolCollection(User user1,
			Set<CollectionProtocol> cpCollection, Set<CollectionProtocol> removedCpCollection)
	{
		final Collection<CollectionProtocol> tempCollection = new HashSet<CollectionProtocol>();
		tempCollection.addAll(user1.getAssignedProtocolCollection());
		for (final CollectionProtocol newCp : cpCollection)
		{
			boolean isPresent = false;
			for (final CollectionProtocol cp : user1.getAssignedProtocolCollection())
			{
				if (newCp.getId().equals(cp.getId()))
				{
					isPresent = true;
				}
			}
			if (!isPresent)
			{
				user1.getAssignedProtocolCollection().add(newCp);
			}
		}
		for (final CollectionProtocol removedCp : removedCpCollection)
		{
			boolean isPresent = false;
			for (final CollectionProtocol cp : cpCollection)
			{
				if (removedCp.getId().equals(cp.getId()))
				{
					isPresent = true;
				}
			}
			if (!isPresent)
			{
				for (final CollectionProtocol existingCp : tempCollection)
				{
					if (existingCp.getId().equals(removedCp.getId()))
					{
						user1.getAssignedProtocolCollection().remove(existingCp);
					}
				}
			}
		}
	}

	private void insertSitePrivileges(User user1, Vector authorizationData,
			Map<String, SiteUserRolePrivilegeBean> sitePrivilegeMap)
	{
		String roleName = "";

		for (final String key : sitePrivilegeMap.keySet())
		{
			try
			{
				final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = sitePrivilegeMap
						.get(key);

				if (siteUserRolePrivilegeBean.isRowDeleted())
				{
					AppUtility.processDeletedPrivileges(siteUserRolePrivilegeBean);
				}
				else if (siteUserRolePrivilegeBean.isRowEdited())
				{
					final Site site = siteUserRolePrivilegeBean.getSiteList().get(0);
					final String defaultRole = siteUserRolePrivilegeBean.getRole().getValue();
					if (defaultRole != null
							&& (defaultRole.equalsIgnoreCase("0") || defaultRole
									.equalsIgnoreCase("-1")))
					{
						roleName = Constants.getSiteRoleName(site.getId(), user1.getCsmUserId(),
								defaultRole);
					}
					else
					{
						roleName = siteUserRolePrivilegeBean.getRole().getName();
					}
					final Set<String> privileges = new HashSet<String>();
					final List<NameValueBean> privilegeList = siteUserRolePrivilegeBean
							.getPrivileges();

					for (final NameValueBean privilege : privilegeList)
					{
						privileges.add(privilege.getValue());
					}

					AppUtility.processRole(roleName);

					PrivilegeManager.getInstance().createRole(roleName, privileges);

					final String userId = String.valueOf(user1.getCsmUserId());

					gov.nih.nci.security.authorization.domainobjects.User csmUser = null;
					csmUser = this.getUserByID(userId);
					final HashSet<gov.nih.nci.security.authorization.domainobjects.User> group = new HashSet<gov.nih.nci.security.authorization.domainobjects.User>();
					group.add(csmUser);

					final String protectionGroupName = CSMGroupLocator.getInstance().getPGName(
							site.getId(), Site.class);

					this.createProtectionGroup(protectionGroupName, site, false);

					final SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
					userGroupRoleProtectionGroupBean.setUser("");
					userGroupRoleProtectionGroupBean.setRoleName(roleName);

					userGroupRoleProtectionGroupBean.setGroupName(Constants.getSiteUserGroupName(
							site.getId(), user1.getCsmUserId()));
					userGroupRoleProtectionGroupBean.setProtGrpName(protectionGroupName);
					userGroupRoleProtectionGroupBean.setGroup(group);
					authorizationData.add(userGroupRoleProtectionGroupBean);
				}
			}

			catch (final SMException e)
			{
				UserBizLogic.logger.error(e.getMessage(), e);
				e.printStackTrace() ;
			}
			catch (final ApplicationException e)
			{
				UserBizLogic.logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}
	}

	private void insertCPPrivileges(User user1, Vector authorizationData,
			Map<String, SiteUserRolePrivilegeBean> cpPrivilegeMap)
	{
		String roleName = "";
		String protectionGroupName = "";

		for (final String key : cpPrivilegeMap.keySet())
		{
			try
			{
				final SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
				final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = cpPrivilegeMap.get(key);

				if (siteUserRolePrivilegeBean.isRowDeleted())
				{
					AppUtility.processDeletedPrivileges(siteUserRolePrivilegeBean);
				}
				else if (siteUserRolePrivilegeBean.isRowEdited())
				{
					// Case for 'All Current & Future CP's selected
					if (siteUserRolePrivilegeBean.isAllCPChecked() == true)
					{
						final String defaultRole = siteUserRolePrivilegeBean.getRole().getValue();
						final Site site = siteUserRolePrivilegeBean.getSiteList().get(0);

						if (defaultRole != null
								&& (defaultRole.equalsIgnoreCase("-1")
										|| defaultRole.equalsIgnoreCase("0") || defaultRole
										.equalsIgnoreCase("7")))
						{
							roleName = Constants.getCurrentAndFutureRoleName(site.getId(), user1
									.getCsmUserId(), defaultRole);
						}
						else
						{
							roleName = siteUserRolePrivilegeBean.getRole().getName();
						}

						protectionGroupName = Constants
								.getCurrentAndFuturePGAndPEName(site.getId());
						this.createProtectionGroup(protectionGroupName, site, true);

						userGroupRoleProtectionGroupBean.setGroupName(Constants
								.getSiteUserGroupName(site.getId(), user1.getCsmUserId()));
					}
					else
					{
						final CollectionProtocol cp = siteUserRolePrivilegeBean
								.getCollectionProtocol();
						final String defaultRole = siteUserRolePrivilegeBean.getRole().getValue();

						//						if (defaultRole != null && (defaultRole.equalsIgnoreCase("0") || defaultRole.equalsIgnoreCase("-1") || defaultRole.equalsIgnoreCase("7")))
						//						{
						roleName = Constants.getCPRoleName(cp.getId(), user1.getCsmUserId(),
								defaultRole);
						//						}
						/*else
						{
							roleName = siteUserRolePrivilegeBean.getRole().getName();
						}*/

						protectionGroupName = CSMGroupLocator.getInstance().getPGName(cp.getId(),
								CollectionProtocol.class);
						this.createProtectionGroup(protectionGroupName, cp, false);

						userGroupRoleProtectionGroupBean.setGroupName(Constants.getCPUserGroupName(
								cp.getId(), user1.getCsmUserId()));
					}

					final Set<String> privileges = new HashSet<String>();
					final List<NameValueBean> privilegeList = siteUserRolePrivilegeBean
							.getPrivileges();

					for (final NameValueBean privilege : privilegeList)
					{
						privileges.add(privilege.getValue());
					}

					AppUtility.processRole(roleName);

					PrivilegeManager.getInstance().createRole(roleName, privileges);

					final String userId = String.valueOf(user1.getCsmUserId());

					final gov.nih.nci.security.authorization.domainobjects.User csmUser = this
							.getUserByID(userId);
					final HashSet<gov.nih.nci.security.authorization.domainobjects.User> group = new HashSet<gov.nih.nci.security.authorization.domainobjects.User>();
					group.add(csmUser);

					userGroupRoleProtectionGroupBean.setUser("");
					userGroupRoleProtectionGroupBean.setRoleName(roleName);
					userGroupRoleProtectionGroupBean.setProtGrpName(protectionGroupName);
					userGroupRoleProtectionGroupBean.setGroup(group);
					authorizationData.add(userGroupRoleProtectionGroupBean);
				}
			}
			catch (final SMException e)
			{
				UserBizLogic.logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
			catch (final ApplicationException e)
			{
				UserBizLogic.logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}
	}

	private void createProtectionGroup(String protectionGroupName, AbstractDomainObject obj,
			boolean isAllCPChecked)
	{
		ProtectionElement pe = new ProtectionElement();
		final Set<ProtectionElement> peSet = new HashSet<ProtectionElement>();
		List<ProtectionElement> peList = new ArrayList<ProtectionElement>();
		final PrivilegeUtility privilegeUtility = new PrivilegeUtility();

		try
		{
			if (isAllCPChecked)
			{
				pe.setObjectId(Constants.getCurrentAndFuturePGAndPEName(obj.getId()));
				pe.setProtectionElementName(Constants.getCurrentAndFuturePGAndPEName(obj.getId()));
				pe
						.setProtectionElementDescription("For All Current & Future CP's for Site with Id "
								+ obj.getId().toString());
				pe.setApplication(privilegeUtility.getApplication(SecurityManagerPropertiesLocator
						.getInstance().getApplicationCtxName()));
				final ProtectionElementSearchCriteria searchCriteria = new ProtectionElementSearchCriteria(
						pe);
				peList = privilegeUtility.getUserProvisioningManager().getObjects(searchCriteria);
				if (peList != null && !peList.isEmpty())
				{
					pe = peList.get(0);
				}
				else
				{
					privilegeUtility.getUserProvisioningManager().createProtectionElement(pe);
				}
				peList.add(pe);
			}
			else
			{
				pe.setObjectId(obj.getObjectId());
				final ProtectionElementSearchCriteria searchCriteria = new ProtectionElementSearchCriteria(
						pe);

				peList = privilegeUtility.getUserProvisioningManager().getObjects(searchCriteria);
			}
			final ProtectionGroup pg = new ProtectionGroup();
			pg.setProtectionGroupName(protectionGroupName);
			peSet.addAll(peList);
			pg.setProtectionElements(peSet);
			new PrivilegeUtility().getUserProvisioningManager().createProtectionGroup(pg);
		}
		catch (final Exception e)
		{
			UserBizLogic.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	private void distributeMapData(Map<String, SiteUserRolePrivilegeBean> userRowIdMap,
			Map<String, SiteUserRolePrivilegeBean> cpPrivilegeMap,
			Map<String, SiteUserRolePrivilegeBean> sitePrivilegeMap)
	{
		for (final String key : userRowIdMap.keySet())
		{
			final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = userRowIdMap.get(key);

			if (siteUserRolePrivilegeBean.getCollectionProtocol() == null
					&& siteUserRolePrivilegeBean.isAllCPChecked() == false)
			{
				sitePrivilegeMap.put(key, siteUserRolePrivilegeBean);
			}
			else
			{
				cpPrivilegeMap.put(key, siteUserRolePrivilegeBean);
			}
		}
	}

	/**
	 * This method gets user by id.
	 * @param userId user Id
	 * @return user
	 * @throws SMException Generic SM Exception
	 */
	private gov.nih.nci.security.authorization.domainobjects.User getUserByID(String userId)
			throws SMException
	{
		final gov.nih.nci.security.authorization.domainobjects.User user = SecurityManagerFactory
				.getSecurityManager().getUserById(userId);

		return user;
	}

	/**
	 * @param dao DAO object.
	 * @param obj The object to be updated
	 * @param oldObj The old object
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws BizLogicException BizLogic Exception
	*/
	public void updateUser(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		this.update(dao, obj, oldObj, sessionDataBean);
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws BizLogicException BizLogic Exception
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		User user = null;
		Map<String, SiteUserRolePrivilegeBean> userRowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>();

		if (obj instanceof UserDTO)
		{
			user = ((UserDTO) obj).getUser();
			userRowIdMap = ((UserDTO) obj).getUserRowIdBeanMap();
		}
		else
		{
			user = (User) obj;
		}
		// Method to populate rowIdMap in case, Add Privilege button is not clicked
		userRowIdMap = this.getUserRowIdMap(user, userRowIdMap);

		final User oldUser = (User) oldObj;

		boolean isLoginUserUpdate = false;
		if (oldUser.getLoginName().equals(sessionDataBean.getUserName()))
		{
			isLoginUserUpdate = true;
		}

		//If the user is rejected, its record cannot be updated.
		if (Status.ACTIVITY_STATUS_REJECT.toString().equals(oldUser.getActivityStatus()))
		{

			throw this.getBizLogicException(null, "errors.editRejectedUser", "");
		}
		else if (Status.ACTIVITY_STATUS_NEW.toString().equals(oldUser.getActivityStatus())
				|| Status.ACTIVITY_STATUS_PENDING.toString().equals(oldUser.getActivityStatus()))
		{
			//If the user is not approved yet, its record cannot be updated.
			throw this.getBizLogicException(null, "errors.editNewPendingUser", "");
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
				throw this.getBizLogicException(null, "domain.object.null.err.msg",
						"First Time Login");
			}

			if (oldUser.getFirstTimeLogin() != null
					&& user.getFirstTimeLogin().booleanValue() != oldUser.getFirstTimeLogin()
							.booleanValue())
			{
				throw this.getBizLogicException(null, "errors.cannotedit.firsttimelogin", "");
			}

			if (user.getCsmUserId() != null)
			{
				csmUserId = user.getCsmUserId().toString();
			}

			final gov.nih.nci.security.authorization.domainobjects.User csmUser = SecurityManagerFactory
					.getSecurityManager().getUserById(csmUserId);

			//Bug:7979
			if (Constants.DUMMY_PASSWORD.equals(user.getNewPassword()))
			{
				user.setNewPassword(csmUser.getPassword());
			}

			final String oldPassword = user.getOldPassword();
			// If the page is of change password,
			// update the password of the user in csm and catissue tables.
			if (Constants.PAGE_OF_CHANGE_PASSWORD.equals(user.getPageOf()))
			{
				if (!oldPassword.equals(csmUser.getPassword()))
				{
					throw this.getBizLogicException(null, "errors.oldPassword.wrong", "");
				}

				//Added for Password validation by Supriya Dankh.
				final Validator validator = new Validator();
				if (!validator.isEmpty(user.getNewPassword()) && !validator.isEmpty(oldPassword))
				{
					this.passwordValidation(user, oldUser, oldPassword);
				}
				csmUser.setPassword(user.getNewPassword());

				// Set values in password domain object and adds changed password in Password Collection
				final Password password = new Password(PasswordManager.encrypt(user
						.getNewPassword()), user);
				user.getPasswordCollection().add(password);

			}

			//Bug-1516: Jitendra Administartor should be able to edit the password 
			else if (Constants.PAGE_OF_USER_ADMIN.equals(user.getPageOf())
					&& !user.getNewPassword().equals(csmUser.getPassword()))
			{
				final Validator validator = new Validator();
				if (!validator.isEmpty(user.getNewPassword()))
				{
					this.passwordValidation(user, oldUser, oldPassword);
				}
				csmUser.setPassword(user.getNewPassword());
				// Set values in password domain object and
				//adds changed password in Password Collection
				final Password password = new Password(PasswordManager.encrypt(user
						.getNewPassword()), user);
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
				if ((Constants.PAGE_OF_USER_PROFILE.equals(user.getPageOf()) == false)
						&& (Constants.PAGE_OF_CHANGE_PASSWORD.equals(user.getPageOf()) == false))
				{
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

						SecurityManagerFactory.getSecurityManager().assignRoleToUser(
								csmUser.getUserId().toString(), role);
					}
				}

				if (userRowIdMap != null && !userRowIdMap.isEmpty())
				{
					this.updateUserDetails(user, userRowIdMap);
				}

				final Vector authorizationData = new Vector();
				final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();

				this.insertCPSitePrivileges(user, authorizationData, userRowIdMap);

				boolean flag = true;

				final Set protectionObjects = new HashSet();
				protectionObjects.add(user);
				final PrivilegeUtility privilegeUtility = new PrivilegeUtility();
				try
				{
					final ProtectionElement protectionElement = privilegeUtility
							.getUserProvisioningManager().getProtectionElement(
									User.class.getName() + "_" + user.getId().toString());
				}
				catch (final CSObjectNotFoundException e)
				{
					UserBizLogic.logger.error(e.getMessage(), e);
					e.printStackTrace();
					flag = false;
					privilegeManager.insertAuthorizationData(authorizationData, protectionObjects,
							null, user.getObjectId());
				}
				if (flag)
				{
					privilegeManager.insertAuthorizationData(authorizationData, null, null, user
							.getObjectId());
				}

				dao.update(user.getAddress(),oldUser.getAddress());

			}

			if (Constants.PAGE_OF_CHANGE_PASSWORD.equals(user.getPageOf()))
			{
				user.setFirstTimeLogin(new Boolean(false));
			}
			dao.update(user,oldUser);

			//Modify the csm user.
			SecurityManagerFactory.getSecurityManager().modifyUser(csmUser);

			if (isLoginUserUpdate)
			{
				sessionDataBean.setUserName(csmUser.getLoginName());
			}

			if(!oldUser.getEmailAddress().equals(user.getEmailAddress()))
			{
				updateWustlKey(user);
			}
		}
		catch (final SMException smExp)
		{
			UserBizLogic.logger.error(smExp.getMessage(), smExp);
			smExp.printStackTrace();
			throw this.getBizLogicException(smExp, "sm.operation.error",
					"Error in checking has privilege");
		}
		catch (final PasswordEncryptionException e)
		{
			UserBizLogic.logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw this.getBizLogicException(e, "pwd.encrytion.error", "");
		}
		catch (final ApplicationException e)
		{
			UserBizLogic.logger.error(e.getMessage(), e);
			e.printStackTrace();
			//ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 * This method validate password.
	 * @param user User object.
	 * @param oldUser old User object
	 * @param oldPassword old Password
	 * @throws PasswordEncryptionException Generic Password Encryption Exception
	 * @throws BizLogicException BizLogic Exception
	 */
	private void passwordValidation(User user, User oldUser, String oldPassword)
			throws PasswordEncryptionException, BizLogicException
	{
		final int result = this.validatePassword(oldUser, user.getNewPassword(), oldPassword);

		this.logger.debug("return from Password validate " + result);

		//if validatePassword method returns value greater than zero then validation fails
		if (result != SUCCESS)
		{
			// get error message of validation failure 
			final List<String> parameters = new ArrayList<String>();
			final String errorKey = this.getPasswordErrorMsg(result, parameters);
			final StringBuffer strBuf = new StringBuffer("");
			for (final String msgValue : parameters)
			{
				strBuf.append(msgValue).append(":");
			}

			this.logger.debug("errorKey" + errorKey);
			throw this.getBizLogicException(null, errorKey, strBuf.toString());
		}
	}

	/**
	 * Returns the list of NameValueBeans with name as "LastName,Firstname" 
	 * and value as systemtIdentifier, of all users who are not disabled. 
	 * @return the list of NameValueBeans with name as "LastName,Firstname" 
	 * and value as systemtIdentifier, of all users who are not disabled.
	 * @throws BizLogicException
	 */
	public Vector getUsers(String operation) throws BizLogicException
	{
		final String sourceObjectName = User.class.getName();
		//Get only the fields required 
		final String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER, Constants.LASTNAME,
				Constants.FIRSTNAME};
		String[] whereColumnName;
		String[] whereColumnCondition;
		Object[] whereColumnValue;
		String joinCondition;
		if (operation != null && operation.equalsIgnoreCase(Constants.ADD))
		{
			final String tmpArray1[] = {Status.ACTIVITY_STATUS.toString()};
			final String tmpArray2[] = {Constants.EQUALS};
			final String tmpArray3[] = {Status.ACTIVITY_STATUS_ACTIVE.toString()};
			whereColumnName = tmpArray1;
			whereColumnCondition = tmpArray2;
			whereColumnValue = tmpArray3;
			joinCondition = null;
		}
		else
		{
			final String tmpArray1[] = {Status.ACTIVITY_STATUS.toString(),
					Status.ACTIVITY_STATUS.toString()};
			final String tmpArray2[] = {Constants.EQUALS, Constants.EQUALS};
			final String tmpArray3[] = {Status.ACTIVITY_STATUS_ACTIVE.toString(),
					Status.ACTIVITY_STATUS_CLOSED.toString()};
			whereColumnName = tmpArray1;
			whereColumnCondition = tmpArray2;
			whereColumnValue = tmpArray3;
			joinCondition = Constants.OR_JOIN_CONDITION;
		}
		//Retrieve the users whose activity status is not disabled.
		final List users = this.retrieve(sourceObjectName, selectColumnName, whereColumnName,
				whereColumnCondition, whereColumnValue, joinCondition);

		final Vector nameValuePairs = new Vector();
		nameValuePairs.add(new NameValueBean(Constants.SELECT_OPTION, String
				.valueOf(Constants.SELECT_OPTION_VALUE)));

		// If the list of users retrieved is not empty. 
		if (users.isEmpty() == false)
		{
			// Creating name value beans.
			for (int i = 0; i < users.size(); i++)
			{
				//Changes made to optimize the query to get only required fields data
				final Object[] userData = (Object[]) users.get(i);
				final NameValueBean nameValueBean = new NameValueBean();
				nameValueBean.setName(userData[1] + ", " + userData[2]);
				nameValueBean.setValue(userData[0]);
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
	 * @throws BizLogicException
	 */
	public Vector getCSMUsers() throws BizLogicException, SMException
	{
		//Retrieve the users whose activity status is not disabled.
		final List users = SecurityManagerFactory.getSecurityManager().getUsers();

		final Vector nameValuePairs = new Vector();
		nameValuePairs.add(new NameValueBean(Constants.SELECT_OPTION, String
				.valueOf(Constants.SELECT_OPTION_VALUE)));

		// If the list of users retrieved is not empty. 
		if (users.isEmpty() == false)
		{
			// Creating name value beans.
			for (int i = 0; i < users.size(); i++)
			{
				final gov.nih.nci.security.authorization.domainobjects.User user = (gov.nih.nci.security.authorization.domainobjects.User) users
						.get(i);
				final NameValueBean nameValueBean = new NameValueBean();
				nameValueBean.setName(user.getLastName() + ", " + user.getFirstName());
				nameValueBean.setValue(String.valueOf(user.getUserId()));
				this.logger.debug(nameValueBean.toString());
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
	 * @throws BizLogicException
	 */
	public List retrieve(String className, String colName, Object colValue)
			throws BizLogicException
	{
		List userList = null;
		this.logger.debug("In user biz logic retrieve........................");
		try
		{
			// Get the caTISSUE user.
			userList = super.retrieve(className, colName, colValue);

			User appUser = null;
			if (userList != null && !userList.isEmpty())
			{
				appUser = (User) userList.get(0);

				if (appUser.getCsmUserId() != null)
				{
					//Get the role of the user.
					final Role role = SecurityManagerFactory.getSecurityManager().getUserRole(
							appUser.getCsmUserId().longValue());
					//logger.debug("In USer biz logic.............role........id......." + role.getId().toString());

					if (role != null)
					{
						appUser.setRoleId(role.getId().toString());
					}
				}
			}
		}
		catch (final SMException smExp)
		{
			UserBizLogic.logger.error(smExp.getMessage(), smExp);
			smExp.printStackTrace();
			throw this.getBizLogicException(smExp, "sm.check.priv", "");
		}

		return userList;
	}

	/**
	 * Retrieves and sends the login details email to the user whose email address is passed 
	 * else returns the error key in case of an error.  
	 * @param emailAddress the email address of the user whose password is to be sent.
	 * @return the error key in case of an error.
	 * @throws BizLogicException
	 * @throws BizLogicException

	 */
	public String sendForgotPassword(String emailAddress, SessionDataBean sessionData)
			throws BizLogicException
	{
		DAO dao = null;
		try
		{
			String statusMessageKey = null;
			final List list = this.retrieve(User.class.getName(), "emailAddress", emailAddress);
			if (list != null && !list.isEmpty())
			{
				final User user = (User) list.get(0);
				if (user.getActivityStatus().equals(Status.ACTIVITY_STATUS_ACTIVE.toString()))
				{
					final EmailHandler emailHandler = new EmailHandler();

					//Send the login details email to the user.
					boolean emailStatus = false;

					emailStatus = emailHandler.sendLoginDetailsEmail(user, null);

					if (emailStatus)
					{
						// if success commit 
						/**
						 *  Update the field FirstTimeLogin which will ensure user changes his password on login
						 *  Note --> We can not use CommonAddEditAction to update as the user has not still logged in
						 *  and user authorisation will fail. So writing saperate code for update. 
						 */

						user.setFirstTimeLogin(new Boolean(true));
						dao = this.openDAOSession(sessionData);
						dao.update(user);
						dao.commit();

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
		catch (final DAOException daoExp)
		{
			UserBizLogic.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final ApplicationException e)
		{
			UserBizLogic.logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		User user = null;
		if (obj instanceof UserDTO)
		{
			user = ((UserDTO) obj).getUser();
		}
		else
		{
			user = (User) obj;
		}

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
		if (Constants.PAGE_OF_CHANGE_PASSWORD.equals(user.getPageOf()) == false)
		{
			// if condition added by Geeta for ECMC 
			if ((user.getAddress().getState() != "null" && user.getAddress().getState() != "")
					&& edu.wustl.catissuecore.util.global.Variables.isStateRequired)
			{
				if (!Validator.isEnumeratedValue(CDEManager.getCDEManager()
						.getPermissibleValueList(Constants.CDE_NAME_STATE_LIST, null), user
						.getAddress().getState()))
				{

					throw this.getBizLogicException(null, "state.errMsg", "");
				}
			}
			if (!Validator.isEnumeratedValue(CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_COUNTRY_LIST, null), user.getAddress().getCountry()))
			{
				throw this.getBizLogicException(null, "country.errMsg", "");
			}

			if (Constants.PAGE_OF_USER_ADMIN.equals(user.getPageOf()))
			{
				if (operation.equals(Constants.ADD))
				{
					if (!Status.ACTIVITY_STATUS_ACTIVE.toString().equals(user.getActivityStatus()))
					{
						throw this.getBizLogicException(null, "activityStatus.active.errMsg", "");
					}
				}
				else
				{
					if (!Validator.isEnumeratedValue(Constants.USER_ACTIVITY_STATUS_VALUES, user
							.getActivityStatus()))
					{
						throw this.getBizLogicException(null, "activityStatus.errMsg", "");
					}
				}
			}

			//Added by Ashish
			/**
			 * Two more parameter 'dao' and 'operation' is added by Vijay Pande to use it in isUniqueEmailAddress method
			 */
			this.apiValidate(user, dao, operation);
			//END
		}
		return true;
	}

	//Added by Ashish
	/**
	 * @param user user
	 * @param dao
	 * @param operation
	 * @return 
	 * @throws BizLogicException
	 */

	private boolean apiValidate(User user, DAO dao, String operation) throws BizLogicException
	{
		final Validator validator = new Validator();
		String message = "";
		final boolean validate = true;

		if (validator.isEmpty(user.getEmailAddress()))
		{
			message = ApplicationProperties.getValue("user.emailAddress");
			throw this.getBizLogicException(null, "errors.item.required", message);

		}
		else
		{
			if (!validator.isValidEmailAddress(user.getEmailAddress()))
			{
				message = ApplicationProperties.getValue("user.emailAddress");
				throw this.getBizLogicException(null, "errors.item.format", message);
			}
			/**
			 * Name : Vijay_Pande
			 * Reviewer : Sntosh_Chandak
			 * Bug ID: 4185_2
			 * Patch ID: 1-2
			 * See also: 1
			 * Description: Wrong error meassage was dispayed while adding user with existing email address in use.
			 * Following method is provided to verify whether the email address is already present in the system or not.
			 */
			if (!(this.isUniqueEmailAddress(user.getEmailAddress(), user.getId(), dao, operation)))
			{
				String arguments[] = null;
				arguments = new String[]{"User",
						ApplicationProperties.getValue("user.emailAddress")};
				final String errMsg = new DefaultExceptionFormatter().getErrorMessage(
						"Err.ConstraintViolation", arguments);
				this.logger.debug("Unique Constraint Violated: " + errMsg);
				throw this.getBizLogicException(null, "Err.ConstraintViolation", "User :"
						+ ApplicationProperties.getValue("user.emailAddress"));
			}
			if( null != user.getWustlKey() && !"".equals(user.getWustlKey()))
			{
			  if(!user.getEmailAddress().endsWith(edu.wustl.wustlkey.util.global.Constants.WUSTL_EDU)
			  && !user.getEmailAddress().
			  endsWith(edu.wustl.wustlkey.util.global.Constants.WUSTL_EDU_CAPS))
			  {
				  message = ApplicationProperties.getValue("email.washu.user");
				  throw this.getBizLogicException(null, "errors.item.required", message);
			  }
			}
		}
		if (validator.isEmpty(user.getLastName()))
		{
			message = ApplicationProperties.getValue("user.lastName");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}
		else if (validator.isXssVulnerable(user.getLastName()))
		{
			message = ApplicationProperties.getValue("user.lastName");
			throw this.getBizLogicException(null, "errors.xss.invalid", message);
		}

		if (validator.isEmpty(user.getFirstName()))
		{
			message = ApplicationProperties.getValue("user.firstName");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}
		else if (validator.isXssVulnerable(user.getFirstName()))
		{
			message = ApplicationProperties.getValue("user.firstName");
			throw this.getBizLogicException(null, "errors.xss.invalid", message);
		}

		if (validator.isEmpty(user.getAddress().getCity()))
		{
			message = ApplicationProperties.getValue("user.city");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}
		if (edu.wustl.catissuecore.util.global.Variables.isStateRequired)
		{
			if (!validator.isValidOption(user.getAddress().getState())
					|| validator.isEmpty(user.getAddress().getState()))
			{
				message = ApplicationProperties.getValue("user.state");
				throw this.getBizLogicException(null, "errors.item.required", message);
			}
		}
		if (!validator.isValidOption(user.getAddress().getCountry())
				|| validator.isEmpty(user.getAddress().getCountry()))
		{
			message = ApplicationProperties.getValue("user.country");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}
		if (user.getInstitution().getId() == null || user.getInstitution().getId().longValue() <= 0)
		{
			message = ApplicationProperties.getValue("user.institution");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}

		if (user.getDepartment().getId() == null || user.getDepartment().getId().longValue() <= 0)
		{
			message = ApplicationProperties.getValue("user.department");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}

		if (user.getCancerResearchGroup().getId() == null
				|| user.getCancerResearchGroup().getId().longValue() <= 0)
		{
			message = ApplicationProperties.getValue("user.cancerResearchGroup");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}
		return validate;
	}
	/**
	 * @param oldUser User object
	 * @param newPassword New Password value
	 * @param validator VAlidator object
	 * @param oldPassword Old Password value
	 * @return SUCCESS (constant int 0) if all condition passed
	 *   else return respective error code (constant int) value
	 * @throws PasswordEncryptionException
	 */
	private int validatePassword(User oldUser, String newPassword, String oldPassword)
			throws PasswordEncryptionException
	{
		final List oldPwdList = new ArrayList(oldUser.getPasswordCollection());
		Collections.sort(oldPwdList);
		if (oldPwdList != null && !oldPwdList.isEmpty())
		{
			//Check new password is equal to last n password if value
			final String encryptedPassword = PasswordManager.encrypt(newPassword);
			if (this.checkPwdNotSameAsLastN(encryptedPassword, oldPwdList))
			{
				this.logger.debug("Password is not valid returning FAIL_SAME_AS_LAST_N");
				return FAIL_SAME_AS_LAST_N;
			}

			//Get the last updated date of the password
			final Date lastestUpdateDate = ((Password) oldPwdList.get(0)).getUpdateDate();
			boolean firstTimeLogin = getFirstLogin(oldUser);
			if (!firstTimeLogin)
			{
				if (this.checkPwdUpdatedOnSameDay(lastestUpdateDate))
				{

					this.logger
					.debug("Password is not valid returning FAIL_CHANGED_WITHIN_SOME_DAY");
					return FAIL_CHANGED_WITHIN_SOME_DAY;
				}
			}

			/**	
			 * to check password does not contain user name,surname,email address.
			 * if same return FAIL_SAME_NAME_SURNAME_EMAIL
			 *  eg. username=XabcY@abc.com newpassword=abc is not valid
			 */

			String emailAddress = oldUser.getEmailAddress();
			final int usernameBeforeMailaddress = emailAddress.indexOf('@');
			// get substring of emailAddress before '@' character
			emailAddress = emailAddress.substring(0, usernameBeforeMailaddress);
			final String userFirstName = oldUser.getFirstName();
			final String userLastName = oldUser.getLastName();

			final StringBuffer sb = new StringBuffer(newPassword);
			if (emailAddress != null
					&& newPassword.toLowerCase().indexOf(emailAddress.toLowerCase()) != -1)
			{
				this.logger.debug("Password is not valid returning FAIL_SAME_NAME_SURNAME_EMAIL");
				return FAIL_SAME_NAME_SURNAME_EMAIL;
			}

			if (userFirstName != null
					&& newPassword.toLowerCase().indexOf(userFirstName.toLowerCase()) != -1)
			{
				this.logger.debug("Password is not valid returning FAIL_SAME_NAME_SURNAME_EMAIL");
				return FAIL_SAME_NAME_SURNAME_EMAIL;
			}

			if (userLastName != null
					&& newPassword.toLowerCase().indexOf(userLastName.toLowerCase()) != -1)
			{
				this.logger.debug("Password is not valid returning FAIL_SAME_NAME_SURNAME_EMAIL");
				return FAIL_SAME_NAME_SURNAME_EMAIL;
			}

		}
		return SUCCESS;
	}

	/**
	 * This function checks whether user has logged in for
	 * first time or whether user's password is expired.
	 * In both these case user needs to change his password so Error key is returned
	 * @param user - user object
	 * @return String
	 * @throws BizLogicException - throws BizLogicException
	 */
	public String checkFirstLoginAndExpiry(User user)
	{
		final List passwordList = new ArrayList(user.getPasswordCollection());

		boolean firstTimeLogin = getFirstLogin(user);
		// If user has logged in for the first time, return key of Change password on first login
		if (firstTimeLogin)
		{
			return "errors.changePassword.changeFirstLogin";
		}

		Collections.sort(passwordList);
		final Password lastPassword = (Password) passwordList.get(0);
		final Date lastUpdateDate = lastPassword.getUpdateDate();

		final Validator validator = new Validator();
		//Get difference in days between last password update date and current date.
		final long dayDiff = validator.getDateDiff(lastUpdateDate, new Date());
		final int expireDaysCount = Integer.parseInt(XMLPropertyHandler
				.getValue("password.expire_after_n_days"));
		this.logger.info("expireDaysCount" +expireDaysCount);
		this.logger.info("dayDiff" +dayDiff);
		if (dayDiff > expireDaysCount)
		{
			this.logger.info("returning error change password expire");
			return "errors.changePassword.expire";
		}
		return Constants.SUCCESS;

	}
	/**
	 * This function will check if the user is First time logging.
	 * @param user user object
	 * @return firstTimeLogin
	 */
	public boolean getFirstLogin(User user)
	{
		boolean firstTimeLogin = false;
		if (user.getFirstTimeLogin() != null)
		{
			firstTimeLogin = user.getFirstTimeLogin().booleanValue();
		}
		return firstTimeLogin;
	}
	/**
	 * @param newPassword newPassword
	 * @param oldPwdList oldPwdList
	 * @return boolean
	 */
	private boolean checkPwdNotSameAsLastN(String newPassword, List oldPwdList)
	{
		int noOfPwdNotSameAsLastN = 0;
		final String pwdNotSameAsLastN = XMLPropertyHandler.getValue("password.not_same_as_last_n");
		if (pwdNotSameAsLastN != null && !pwdNotSameAsLastN.equals(""))
		{
			noOfPwdNotSameAsLastN = Integer.parseInt(pwdNotSameAsLastN);
			noOfPwdNotSameAsLastN = Math.max(0, noOfPwdNotSameAsLastN);
		}

		boolean isSameFound = false;
		final int loopCount = Math.min(oldPwdList.size(), noOfPwdNotSameAsLastN);
		for (int i = 0; i < loopCount; i++)
		{
			final Password pasword = (Password) oldPwdList.get(i);
			if (newPassword.equals(pasword.getPassword()))
			{
				isSameFound = true;
				break;
			}
		}
		return isSameFound;
	}
	/**
	 * @param lastUpdateDate Date last updated
	 * @return boolean
	 */
	private boolean checkPwdUpdatedOnSameDay(Date lastUpdateDate)
	{
		final Validator validator = new Validator();
		//Get difference in days between last password update date and current date.
		final long dayDiff = validator.getDateDiff(lastUpdateDate, new Date());
		final int dayDiffConstant = Integer.parseInt(XMLPropertyHandler.getValue("daysCount"));
		if (dayDiff < dayDiffConstant)
		{
			this.logger.debug("Password is not valid returning FAIL_CHANGED_WITHIN_SOME_DAY");
			return true;
		}
		return false;
	}

	/**
	 * @param errorCode int value return by validatePassword() method
	 * @param parameters List of strings
	 * @return String error message with respect to error code
	 */
	private String getPasswordErrorMsg(int errorCode, List<String> parameters)
	{
		String errMsg = "";
		switch (errorCode)
		{
			case FAIL_SAME_AS_LAST_N :
				final String dayCount = ""
						+ Integer.parseInt(XMLPropertyHandler
								.getValue("password.not_same_as_last_n"));
				parameters.add(dayCount);
				errMsg = "errors.newPassword.sameAsLastn";
				break;
			case FAIL_FIRST_LOGIN :
				errMsg = "errors.changePassword.changeFirstLogin";
				break;
			case FAIL_EXPIRE :
				errMsg = "errors.changePassword.expire";
				break;
			case FAIL_CHANGED_WITHIN_SOME_DAY :
				final Integer daysCount = Integer
						.parseInt(XMLPropertyHandler.getValue("daysCount"));
				parameters.add(daysCount.toString());
				if (daysCount.intValue() == 1)
				{
					//errMsg = ApplicationProperties.getValue("errors.changePassword.sameDay");
					errMsg = "errors.changePassword.sameDay";
				}
				else
				{
					errMsg = "errors.changePassword.afterSomeDays";
				}
				break;
			case FAIL_SAME_NAME_SURNAME_EMAIL :
				errMsg = "errors.changePassword.sameAsNameSurnameEmail";
				break;
			case FAIL_PASSWORD_EXPIRED :
				errMsg = "errors.changePassword.expire";
			default :
				errMsg = "errors.newPassword.genericmessage";
				break;
		}
		return errMsg;
	}

	/**
	 * Name : Vijay_Pande
	 * Reviewer : Sntosh_Chandak
	 * Bug ID: 4185_2
	 * Patch ID: 1-2
	 * See also: 1
	 * Description: Wrong error meassage was dispayed while adding user with existing email address in use.
	 * Following method is provided to verify whether the email address is already present in the system or not.
	 */
	/**
	 * Method to check whether email address already exist or not
	 * @param emailAddress email address to be check
	 * @param dao an object of DAO
	 * @param userId User identifier
	 * @return isUnique boolean value to indicate presence of similar email address
	 * @throws BizLogicException database exception
	 */
	private boolean isUniqueEmailAddress(String emailAddress, Long userId, DAO dao, String operation)
			throws BizLogicException
	{
		boolean isUnique = true;
		try
		{
			final String sourceObjectName = User.class.getName();
			final String[] selectColumnName = new String[]{"id", "emailAddress"};

			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("emailAddress", emailAddress));

			final List userList = dao
					.retrieve(sourceObjectName, selectColumnName, queryWhereClause);

			if (userList.size() > 0)
			{
				final Object[] objects = (Object[]) userList.get(0);

				if (operation.equalsIgnoreCase(Constants.ADD))
				{
					isUnique = false;
				}
				else
				{
					if (emailAddress.equals((String) objects[1])
							&& !userId.equals((Long) objects[0]))
					{
						isUnique = false;
					}
				}

			}

		}
		catch (final DAOException daoExp)
		{
			UserBizLogic.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
			.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return isUnique;
	}

	/**
	 * Set Role to user object before populating actionForm out of it
	 * @param domainObj object of AbstractDomainObject
	 * @param uiForm object of the class which implements IValueObject
	 * @throws BizLogicException 
	 */
	protected void prePopulateUIBean(AbstractDomainObject domainObj, IValueObject uiForm)
			throws BizLogicException
	{
		UserBizLogic.logger.info("Inside prePopulateUIBean method of UserBizLogic...");
	
		final User user = (User) domainObj;
		Role role = null;
		if (user.getCsmUserId() != null)
		{
			try
			{
				//Get the role of the user.
				role = SecurityManagerFactory.getSecurityManager().getUserRole(
						user.getCsmUserId().longValue());
				if (role != null)
				{
					user.setRoleId(role.getId().toString());
				}
			}
			catch (final SMException e)
			{
				UserBizLogic.logger.error("SMException in " +
						"prePopulateUIBean method of UserBizLogic..." +e.getMessage(),e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * To Sort CP's in CP based view according to the
	 * Privilges of User on CP.
	 * Done for MSR functionality change
	 * @author ravindra_jain 
	 */

	public Set<Long> getRelatedCPIds(Long userId, boolean isCheckForCPBasedView)
			throws BizLogicException
	{
		DAO dao = null;
		Collection<CollectionProtocol> userCpCollection = new HashSet<CollectionProtocol>();
		Collection<CollectionProtocol> userColl;
		Set<Long> cpIds = new HashSet<Long>();

		try
		{
			dao = getHibernateDao(this.getAppName(), null);
			final User user = (User) dao.retrieveById(User.class.getName(), userId);
			userColl = user.getCollectionProtocolCollection();
			userCpCollection = user.getAssignedProtocolCollection();

			if (user.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
			{
				cpIds = null;
			}
			else
			{
				if (isCheckForCPBasedView)
				{
					final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
					final PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(user
							.getLoginName());

					for (final CollectionProtocol collectionProtocol : userCpCollection)
					{
						final String privilegeName = edu.wustl.common.util.global.Variables.privilegeDetailsMap
								.get(Constants.EDIT_PROFILE_PRIVILEGE);
						if (privilegeCache.hasPrivilege(collectionProtocol.getObjectId(),
								privilegeName)
								|| collectionProtocol.getPrincipalInvestigator().getLoginName()
										.equals(user.getLoginName()))
						{
							cpIds.add(collectionProtocol.getId());
						}
					}
				}
				else
				{
					for (final CollectionProtocol collectionProtocol : userCpCollection)
					{
						cpIds.add(collectionProtocol.getId());
					}
				}

				for (final CollectionProtocol cp : userColl)
				{
					cpIds.add(cp.getId());
				}
			}
		}
		catch (final DAOException e)
		{
			UserBizLogic.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		catch (final SMException e)
		{
			UserBizLogic.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		finally
		{
			this.closeDAOSession(dao);
		}

		return cpIds;
	}
	/**
	 * @param userId user Id
	 * @return Set
	 * @throws BizLogicException BizLogicException
	 */
	public Set<Long> getRelatedSiteIds(Long userId) throws BizLogicException
	{
		DAO dao = null;

		HashSet<Long> idSet = null;

		try
		{
			dao = this.openDAOSession(null);

			final User user = (User) dao.retrieveById(User.class.getName(), userId);
			if (!user.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
			{
				final Collection<Site> siteCollection = user.getSiteCollection();
				idSet = new HashSet<Long>();

				for (final Site site : siteCollection)
				{
					idSet.add(site.getId());
				}
			}
		}
		catch (final ApplicationException e1)
		{
			UserBizLogic.logger.error(e1.getMessage(), e1);
			e1.printStackTrace();
			throw this.getBizLogicException(e1, e1.getErrorKeyName(), e1.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}

		return idSet;
	}

	/**
	 * Custom method for Add User Case
	 * @param dao DAO object
	 * @param domainObject Domain object
	 * @param sessionDataBean SessionDataBean object
	 * @return
	 */
	public String getObjectId(DAO dao, Object domainObject)
	{
		User user = null;
		UserDTO userDTO = null;
		Map<String, SiteUserRolePrivilegeBean> userRowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>();
		final Collection<Site> siteCollection = new ArrayList<Site>();

		if (domainObject instanceof UserDTO)
		{
			userDTO = (UserDTO) domainObject;
			user = userDTO.getUser();
			userRowIdMap = userDTO.getUserRowIdBeanMap();
		}
		else
		{
			user = (User) domainObject;
		}

		if (user.getRoleId().equals(Constants.SUPER_ADMIN_USER))
		{
			return Constants.cannotCreateSuperAdmin;
		}
		if (userRowIdMap == null)
		{
			if (user.getRoleId() == null || user.getRoleId().equals("")
					|| user.getSiteCollection() == null || user.getSiteCollection().isEmpty())
			{
				return Constants.siteIsRequired;
			}
			try
			{
				userRowIdMap = this.getUserRowIdMap(user, userRowIdMap);
			}
			catch (final BizLogicException e)
			{
				UserBizLogic.logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}

		if (userRowIdMap != null)
		{
			final Object[] mapKeys = userRowIdMap.keySet().toArray();
			for (final Object mapKey : mapKeys)
			{
				final String key = mapKey.toString();
				final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = userRowIdMap.get(key);

				siteCollection.add(siteUserRolePrivilegeBean.getSiteList().get(0));
			}
		}
		final StringBuffer sb = new StringBuffer();
		boolean hasUserProvisioningPrivilege = false;

		if (siteCollection != null && !siteCollection.isEmpty())
		{
			sb.append(Constants.SITE_CLASS_NAME);
			for (final Site site : siteCollection)
			{
				if (site.getId() != null)
				{
					sb.append(Constants.UNDERSCORE).append(site.getId());
					hasUserProvisioningPrivilege = true;
				}
			}
		}
		if (hasUserProvisioningPrivilege)
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

	public boolean checkUser(Object domainObject, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		User user = null;
		UserDTO userDTO = null;
		Map<String, SiteUserRolePrivilegeBean> userRowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>();

		if (sessionDataBean != null && sessionDataBean.isAdmin())
		{
			return true;
		}
		if (domainObject instanceof User)
		{
			user = (User) domainObject;
		}
		if (domainObject instanceof UserDTO)
		{
			userDTO = (UserDTO) domainObject;
			user = userDTO.getUser();
			if (user.getRoleId().equals(Constants.SUPER_ADMIN_USER))
			{
				throw this.getBizLogicException(null, "user.cannotCreateSuperAdmin", "");
			}
			userRowIdMap = userDTO.getUserRowIdBeanMap();
			if (userRowIdMap != null)
			{
				final Object[] mapKeys = userRowIdMap.keySet().toArray();
				for (final Object mapKey : mapKeys)
				{
					final String key = mapKey.toString();
					final SiteUserRolePrivilegeBean bean = userRowIdMap.get(key);

					if (bean.getSiteList() == null || bean.getSiteList().isEmpty())
					{
						throw this.getBizLogicException(null, "user.cannotCreateScientist", "");
					}
				}
			}
			else
			{
				if (user.getRoleId().equals(Constants.NON_ADMIN_USER))
				{

					throw this.getBizLogicException(null, "user.cannotCreateScientist", "");
				}
				if (user.getRoleId() == null || user.getRoleId().equals("")
						|| user.getSiteCollection() == null || user.getSiteCollection().isEmpty())
				{

					throw this.getBizLogicException(null, "user.siteIsRequired", "");
				}
			}
		}
		if (user.getPageOf().equalsIgnoreCase("pageOfChangePassword"))
		{
			return true;
		}
		/*if(user.getId().equals(sessionDataBean.getUserId()))
		{
			throw new DAOException(ApplicationProperties.getValue("user.cannotEditOwnPrivileges"));
		}*/
		if (user.getPageOf().equalsIgnoreCase("pageOfSignUp"))
		{
			return true;
		}
		if (sessionDataBean != null && user.getLoginName().equals(sessionDataBean.getUserName()))
		{
			return true;
		}
		return false;
	}

	/**
	 * Over-ridden for the case of Non - Admin user should be able to edit
	 * his/her details e.g. Password 
	 * (non-Javadoc)
	 * @throws BizLogicException 
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.common.dao.DAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			boolean isAuthorized = false;
			isAuthorized = this.checkUser(domainObject, sessionDataBean);
			if (isAuthorized)
			{
				return true;
			}

			final String privilegeName = this.getPrivilegeName(domainObject);
			final String protectionElementName = this.getObjectId(dao, domainObject);

			if (Constants.cannotCreateSuperAdmin.equals(protectionElementName))
			{
				throw this.getBizLogicException(null, "user.cannotCreateSuperAdmin", "");
			}
			if (Constants.siteIsRequired.equals(protectionElementName))
			{

				throw this.getBizLogicException(null, "user.siteIsRequired", "");
			}

			final PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
					sessionDataBean.getUserName());

			if (protectionElementName != null)
			{
				final String[] prArray = protectionElementName.split(Constants.UNDERSCORE);
				final String baseObjectId = prArray[0];
				String objId = null;

				for (int i = 1; i < prArray.length; i++)
				{
					objId = baseObjectId + Constants.UNDERSCORE + prArray[i];
					isAuthorized = privilegeCache.hasPrivilege(objId.toString(), privilegeName);
					if (!isAuthorized)
					{
						break;
					}
				}

				if (!isAuthorized)
				{
					//bug 11611 and 11659
					throw AppUtility.getUserNotAuthorizedException(privilegeName,
							protectionElementName, domainObject.getClass().getSimpleName());
				}
				return isAuthorized;
			}
			else
			{
				// return false;
				//bug 11611 and 11659
				throw AppUtility.getUserNotAuthorizedException(privilegeName,
						protectionElementName, domainObject.getClass().getSimpleName());
			}
		}
		/*catch(ApplicationException exp)
		{
			logger.debug(exp.getMessage(), exp);
			throw getBizLogicException(exp, "dao.error", "");
		}*/
		catch (final SMException e)
		{
			UserBizLogic.logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw AppUtility.handleSMException(e);
		}
	}
	/**
	 * This method will migrate user to WUSTLKey.
	 * @param user
	 * 			Object of USER
	 * @throws BizLogicException Object of BizLogicException
	 */
	private void updateWustlKey(User user) throws BizLogicException
	{
		try
		{
			if(user.getWustlKey()!=null)
			{
				String queryStr ="UPDATE CSM_MIGRATE_USER SET LOGIN_NAME='"+user.getLoginName()+"' "+
				"WHERE WUSTLKEY ='"+user.getWustlKey()+"'";
				WUSTLKeyUtility.executeQueryUsingDataSource
				(queryStr,true,edu.wustl.wustlkey.util.global.Constants.APPLICATION_NAME);
			}
		}
		catch (ApplicationException e)
		{
			UserBizLogic.logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new BizLogicException(ErrorKey.getErrorKey("db.update.data.error"), e,
			"Error in database operation");
		}
	}

}