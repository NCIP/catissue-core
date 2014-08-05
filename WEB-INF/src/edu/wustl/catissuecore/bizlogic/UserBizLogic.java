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

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.auth.exception.MigrationRuleException;
import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.factory.AuthManagerFactory;
import edu.wustl.catissuecore.bean.CpAndParticipentsBean;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.Password;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.UserDTO;
import edu.wustl.catissuecore.dto.UserNameIdDTO;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.exception.PasswordEncryptionException;
import edu.wustl.common.exceptionformatter.DefaultExceptionFormatter;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.idp.IdPManager;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.UniqueIDGenerator;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.UserDetails;
import edu.wustl.migrator.exception.MigratorException;
import edu.wustl.migrator.util.Utility;
import edu.wustl.security.beans.SecurityDataBean;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.locator.CSMGroupLocator;
import edu.wustl.security.locator.SecurityManagerPropertiesLocator;
import edu.wustl.security.manager.SecurityManagerFactory;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;
import edu.wustl.security.privilege.PrivilegeUtility;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;

// TODO: Auto-generated Javadoc
/**
 * UserBizLogic is used to add user information into the database using
 * Hibernate.
 *
 * @author kapil_kaveeshwar
 */
public class UserBizLogic extends CatissueDefaultBizLogic
{
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(UserBizLogic.class);

	/** The Constant FAIL_SAME_AS_LAST_N. */
	public static final int FAIL_SAME_AS_LAST_N = 8;

	/** The Constant FAIL_FIRST_LOGIN. */
	public static final int FAIL_FIRST_LOGIN = 9;

	/** The Constant FAIL_EXPIRE. */
	public static final int FAIL_EXPIRE = 10;

	/** The Constant FAIL_CHANGED_WITHIN_SOME_DAY. */
	public static final int FAIL_CHANGED_WITHIN_SOME_DAY = 11;

	/** The Constant FAIL_SAME_NAME_SURNAME_EMAIL. */
	public static final int FAIL_SAME_NAME_SURNAME_EMAIL = 12;

	/** The Constant FAIL_PASSWORD_EXPIRED. */
	public static final int FAIL_PASSWORD_EXPIRED = 13;

	/** The Constant USER_EMAIL_ADDRESS. */
	private static final String USER_EMAIL_ADDRESS = "user.emailAddress";

	/** The Constant ERRORS_ITEM_REQUIRED. */
	private static final String ERRORS_ITEM_REQUIRED = "errors.item.required";

	/** The Constant SUCCESS. */
	public static final int SUCCESS = 0;

	/**
	 * Saves the user object in the database.
	 *
	 * @param obj
	 *            The user object to be saved.
	 * @param dao
	 *            the dao
	 * @param sessionDataBean
	 *            the session data bean
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */
	@Override
	protected void insert(final Object obj, final DAO dao, final SessionDataBean sessionDataBean)
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
		// Method to populate rowIdMap in case, Add Privilege button is not
		// clicked
		userRowIdMap = getUserRowIdMap(user, userRowIdMap);
		final gov.nih.nci.security.authorization.domainobjects.User csmUser = new gov.nih.nci.security.authorization.domainobjects.User();

		try
		{
			Object object = dao.retrieveById(Department.class.getName(), user.getDepartment().getId());
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

			object = dao.retrieveById(CancerResearchGroup.class.getName(), user.getCancerResearchGroup().getId());
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
			if (user.getPageOf() == null || !user.getPageOf().equals(Constants.PAGE_OF_SIGNUP))
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

					SecurityManagerFactory.getSecurityManager().assignRoleToUser(csmUser.getUserId().toString(),
							role);
				}

				user.setCsmUserId(csmUser.getUserId());
				// user.setPassword(csmUser.getPassword());
				// Add password of user in password table.Updated by Supriya
				// Dankh
				final Password password = new Password();

				/**
				 * Start: Change for API Search --- Jitendra 06/10/2006 In Case
				 * of Api Search, previoulsy it was failing since there was
				 * default class level initialization on domain object. For
				 * example in User object, it was initialized as protected
				 * String lastName=""; So we removed default class level
				 * initialization on domain object and are initializing in
				 * method setAllValues() of domain object. But in case of Api
				 * Search, default values will not get set since setAllValues()
				 * method of domainObject will not get called. To avoid null
				 * pointer exception, we are setting the default values same as
				 * we were setting in setAllValues() method of domainObject.
				 */
				ApiSearchUtil.setPasswordDefault(password);
				// End:- Change for API Search

				password.setUser(user);
				password.setPassword(PasswordManager.encrypt(generatedPassword));
				password.setUpdateDate(new Date());

				user.getPasswordCollection().add(password);

				LOGGER.debug("password stored in passwore table");
			}

			/**
			 * First time login is always set to true when a new user is created
			 */
			user.setFirstTimeLogin(Boolean.TRUE);
			String userToken=UniqueIDGenerator.getUniqueID();
		    user.setForgotPasswordToken(userToken);

			// Create address and the user in catissue tables.
			dao.insert(user.getAddress());

			if (userRowIdMap != null && !userRowIdMap.isEmpty())
			{
				updateUserDetails(user, userRowIdMap);
			}
			dao.insert(user);
			if (Constants.PAGE_OF_SIGNUP.equals(user.getPageOf()))
			{
				insertUserIDPInformation(user);
			}
			if (isIdpEnabled())
			{
				IdPManager idp = IdPManager.getInstance();
				idp.addUserToQueue(SecurityManagerPropertiesLocator.getInstance()
						.getApplicationCtxName(), csmUser);
			}
			final Set protectionObjects = new HashSet();
			protectionObjects.add(user);

			final EmailHandler emailHandler = new EmailHandler();
			// Send the user registration email to user and the administrator.
			if (Constants.PAGE_OF_SIGNUP.equals(user.getPageOf()))
			{
				// SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,
				// protectionObjects, null);

				emailHandler.sendUserSignUpEmail(user);
			}
			else
				// Send the user creation email to user and the administrator.
			{
				// SecurityManager.getInstance(this.getClass()).insertAuthorizationData(getAuthorizationData(user),
				// protectionObjects, null);

				final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();

				privilegeManager.insertAuthorizationData(getAuthorizationData(user, userRowIdMap),
						protectionObjects, null, user.getObjectId());

				emailHandler.sendApprovalEmail(user);
			}
		}
		catch (final SMException e)
		{
			UserBizLogic.LOGGER.error(e.getMessage(), e);
			// added to format constrainviolation message
			deleteCSMUser(csmUser);
			throw getBizLogicException(e, "sm.check.priv", "");
		}
		catch (final PasswordEncryptionException exception)
		{
			UserBizLogic.LOGGER.error(exception.getMessage(), exception);
			deleteCSMUser(csmUser);
			throw getBizLogicException(exception, "pwd.encrytion.error", "");
		}
		catch (final ApplicationException e)
		{
			UserBizLogic.LOGGER.error(e.getMessage(), e);
			deleteCSMUser(csmUser);
			// ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
			throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());

		}
	}

	private void insertUserIDPInformation(final User user) throws BizLogicException
	{
		try
		{
			if (!Validator.isEmpty(user.getTargetIdp()))
			{
				final UserDetails userDetails = new UserDetails();
				userDetails.setLoginName(user.getLoginName());
				userDetails.setMigratedLoginName(user.getTargetIdpLoginName());
				userDetails.setTargetIDP(user.getTargetIdp());
				Utility.migrateUser(userDetails);
			}
		}
		catch (final MigratorException e)
		{
			UserBizLogic.LOGGER.error(e.getMessage(), e);
			throw getBizLogicException(e, "migration.info.updation.error", "");
		}
	}

	/**
	 * Gets the user row id map.
	 *
	 * @param user
	 *            the user
	 * @param userRowIdMap
	 *            the user row id map
	 *
	 * @return the user row id map
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */
	public Map<String, SiteUserRolePrivilegeBean> getUserRowIdMap(final User user,
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
					final List<Role> roleList = SecurityManagerFactory.getSecurityManager().getRoles();
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
					UserBizLogic.LOGGER.error(e.getMessage(), e);
					throw getBizLogicException(e, "user.roleNotFound", "");
				}
				int identifier = 0;
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
					userRowIdMap.put(String.valueOf(identifier), siteUserRolePrivilegeBean);
					identifier++;
				}
			}
		}
		return userRowIdMap;
			}

	/**
	 * Deletes the csm user from the csm user table.
	 *
	 * @param csmUser
	 *            The csm user to be deleted.
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */
	public void deleteCSMUser(final gov.nih.nci.security.authorization.domainobjects.User csmUser)
	throws BizLogicException
	{
		try
		{
			if (csmUser.getUserId() != null)
			{
				SecurityManagerFactory.getSecurityManager().removeUser(csmUser.getUserId().toString());
			}
		}
		catch (final SMException smExp)
		{
			UserBizLogic.LOGGER.error(smExp.getMessage(), smExp);
			throw getBizLogicException(smExp, "sm.operation.error", "Error in checking has privilege");
		}
	}

	/**
	 * This method returns collection of UserGroupRoleProtectionGroup objects
	 * that speciefies the user group protection group linkage through a role.
	 * It also specifies the groups the protection elements returned by this
	 * class should be added to.
	 *
	 * @param obj
	 *            the obj
	 * @param userRowIdMap
	 *            the user row id map
	 *
	 * @return the authorization data
	 *
	 * @throws SMException
	 *             the SM exception
	 */
	private Vector getAuthorizationData(final AbstractDomainObject obj,
			final Map<String, SiteUserRolePrivilegeBean> userRowIdMap) throws SMException
			{
		final Vector authorizationData = new Vector();
		final Set group = new HashSet();
		final User aUser = (User) obj;

		final String userId = String.valueOf(aUser.getCsmUserId());
		final gov.nih.nci.security.authorization.domainobjects.User user = SecurityManagerFactory
		.getSecurityManager().getUserById(userId);
		LOGGER.debug(" User: " + user.getLoginName());
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

		LOGGER.debug(authorizationData.toString());

		if (userRowIdMap != null)
		{
			insertCPSitePrivileges(aUser, authorizationData, userRowIdMap);
		}

		return authorizationData;
			}

	/**
	 * Insert cp site privileges.
	 *
	 * @param user1
	 *            the user1
	 * @param authorizationData
	 *            the authorization data
	 * @param userRowIdMap
	 *            the user row id map
	 */
	public void insertCPSitePrivileges(final User user1, final Vector authorizationData,
			final Map<String, SiteUserRolePrivilegeBean> userRowIdMap)
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

		distributeMapData(userRowIdMap, cpPrivilegeMap, sitePrivilegeMap);

		/* For SITE Privileges Purpose */
		insertSitePrivileges(user1, authorizationData, sitePrivilegeMap);

		/* For CP Privileges Purpose */
		insertCPPrivileges(user1, authorizationData, cpPrivilegeMap);

		/* Common for both */
		// updateUserDetails(user1, userRowIdMap);
	}

	/**
	 * Update user details.
	 *
	 * @param user1
	 *            the user1
	 * @param userRowIdMap
	 *            the user row id map
	 */
	public void updateUserDetails(final User user1, final Map<String, SiteUserRolePrivilegeBean> userRowIdMap)
	{
		final Set<Site> siteCollection = new HashSet<Site>();
		final Set<CollectionProtocol> cpCollection = new HashSet<CollectionProtocol>();
		final Set<CollectionProtocol> removedCpCollection = new HashSet<CollectionProtocol>();
		for (final String key : userRowIdMap.keySet())
		{
			final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = userRowIdMap.get(key);

			final CollectionProtocol protocol = siteUserRolePrivilegeBean.getCollectionProtocol();
			if (protocol != null && !siteUserRolePrivilegeBean.isRowDeleted())
			{
				cpCollection.add(protocol);
			}
			else if (protocol != null && siteUserRolePrivilegeBean.isRowDeleted())
			{
				removedCpCollection.add(protocol);
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
		updateCollectionProtocolCollection(user1, cpCollection, removedCpCollection);
	}

	/**
	 * Update collection protocol collection.
	 *
	 * @param user1
	 *            the user1
	 * @param cpCollection
	 *            the cp collection
	 * @param removedCpCollection
	 *            the removed cp collection
	 */
	private void updateCollectionProtocolCollection(final User user1, final Set<CollectionProtocol> cpCollection,
			final Set<CollectionProtocol> removedCpCollection)
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

	/**
	 * Insert site privileges.
	 *
	 * @param user1
	 *            the user1
	 * @param authorizationData
	 *            the authorization data
	 * @param sitePrivilegeMap
	 *            the site privilege map
	 */
	private void insertSitePrivileges(final User user1, final Vector authorizationData,
			final Map<String, SiteUserRolePrivilegeBean> sitePrivilegeMap)
	{
		String roleName = "";

		for (final String key : sitePrivilegeMap.keySet())
		{
			try
			{
				final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = sitePrivilegeMap.get(key);

				if (siteUserRolePrivilegeBean.isRowDeleted())
				{
					AppUtility.processDeletedPrivileges(siteUserRolePrivilegeBean);
				}
				else if (siteUserRolePrivilegeBean.isRowEdited())
				{
					final Site site = siteUserRolePrivilegeBean.getSiteList().get(0);
					final String defaultRole = siteUserRolePrivilegeBean.getRole().getValue();
					if (defaultRole != null
							&& (defaultRole.equalsIgnoreCase("0") || defaultRole.equalsIgnoreCase("-1")))
					{
						roleName = Constants.getSiteRoleName(site.getId(), user1.getCsmUserId(), defaultRole);
					}
					else
					{
						roleName = siteUserRolePrivilegeBean.getRole().getName();
					}
					final Set<String> privileges = new HashSet<String>();
					final List<NameValueBean> privilegeList = siteUserRolePrivilegeBean.getPrivileges();

					for (final NameValueBean privilege : privilegeList)
					{
						privileges.add(privilege.getValue());
					}

					AppUtility.processRole(roleName);

					PrivilegeManager.getInstance().createRole(roleName, privileges);

					final String userId = String.valueOf(user1.getCsmUserId());

					gov.nih.nci.security.authorization.domainobjects.User csmUser = null;
					csmUser = getUserByID(userId);
					final HashSet<gov.nih.nci.security.authorization.domainobjects.User> group = new HashSet<gov.nih.nci.security.authorization.domainobjects.User>();
					group.add(csmUser);

					final String protectionGroupName = CSMGroupLocator.getInstance().getPGName(site.getId(),
							Site.class);

					createProtectionGroup(protectionGroupName, site, false);

					final SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
					userGroupRoleProtectionGroupBean.setUser("");
					userGroupRoleProtectionGroupBean.setRoleName(roleName);

					userGroupRoleProtectionGroupBean.setGroupName(Constants.getSiteUserGroupName(site.getId(),
							user1.getCsmUserId()));
					userGroupRoleProtectionGroupBean.setProtGrpName(protectionGroupName);
					userGroupRoleProtectionGroupBean.setGroup(group);
					authorizationData.add(userGroupRoleProtectionGroupBean);
				}
			}

			catch (final SMException e)
			{
				UserBizLogic.LOGGER.error(e.getMessage(), e);
			}
			catch (final ApplicationException e)
			{
				UserBizLogic.LOGGER.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * Insert cp privileges.
	 *
	 * @param user1
	 *            the user1
	 * @param authorizationData
	 *            the authorization data
	 * @param cpPrivilegeMap
	 *            the cp privilege map
	 */
	private void insertCPPrivileges(final User user1, final Vector authorizationData,
			final Map<String, SiteUserRolePrivilegeBean> cpPrivilegeMap)
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
					if (siteUserRolePrivilegeBean.isAllCPChecked())
					{
						final String defaultRole = siteUserRolePrivilegeBean.getRole().getValue();
						final Site site = siteUserRolePrivilegeBean.getSiteList().get(0);

						if (defaultRole != null
								&& (defaultRole.equalsIgnoreCase("-1") || defaultRole.equalsIgnoreCase("0") || defaultRole
										.equalsIgnoreCase("7")))
						{
							roleName = Constants.getCurrentAndFutureRoleName(site.getId(), user1.getCsmUserId(),
									defaultRole);
						}
						else
						{
							roleName = siteUserRolePrivilegeBean.getRole().getName();
						}

						protectionGroupName = Constants.getCurrentAndFuturePGAndPEName(site.getId());
						createProtectionGroup(protectionGroupName, site, true);

						userGroupRoleProtectionGroupBean.setGroupName(Constants.getSiteUserGroupName(site.getId(),
								user1.getCsmUserId()));
					}
					else
					{
						final CollectionProtocol protocol = siteUserRolePrivilegeBean.getCollectionProtocol();
						final String defaultRole = siteUserRolePrivilegeBean.getRole().getValue();

						// if (defaultRole != null &&
						// (defaultRole.equalsIgnoreCase("0") ||
						// defaultRole.equalsIgnoreCase("-1") ||
						// defaultRole.equalsIgnoreCase("7")))
						// {
						roleName = Constants.getCPRoleName(protocol.getId(), user1.getCsmUserId(), defaultRole);
						// }
						/*
						 * else { roleName =
						 * siteUserRolePrivilegeBean.getRole().getName(); }
						 */

						protectionGroupName = CSMGroupLocator.getInstance().getPGName(protocol.getId(),
								CollectionProtocol.class);
						createProtectionGroup(protectionGroupName, protocol, false);

						userGroupRoleProtectionGroupBean.setGroupName(Constants.getCPUserGroupName(protocol
								.getId(), user1.getCsmUserId()));
					}

					final Set<String> privileges = new HashSet<String>();
					final List<NameValueBean> privilegeList = siteUserRolePrivilegeBean.getPrivileges();

					for (final NameValueBean privilege : privilegeList)
					{
						privileges.add(privilege.getValue());
					}

					AppUtility.processRole(roleName);

					PrivilegeManager.getInstance().createRole(roleName, privileges);

					final String userId = String.valueOf(user1.getCsmUserId());

					final gov.nih.nci.security.authorization.domainobjects.User csmUser = getUserByID(userId);
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
				UserBizLogic.LOGGER.error(e.getMessage(), e);
			}
			catch (final ApplicationException e)
			{
				UserBizLogic.LOGGER.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * Creates the protection group.
	 *
	 * @param protectionGroupName
	 *            the protection group name
	 * @param obj
	 *            the obj
	 * @param isAllCPChecked
	 *            the is all cp checked
	 */
	private void createProtectionGroup(final String protectionGroupName, final AbstractDomainObject obj,
			final boolean isAllCPChecked)
	{
		ProtectionElement protElement = new ProtectionElement();
		final Set<ProtectionElement> peSet = new HashSet<ProtectionElement>();
		List<ProtectionElement> peList = new ArrayList<ProtectionElement>();
		final PrivilegeUtility privilegeUtility = new PrivilegeUtility();

		try
		{
			if (isAllCPChecked)
			{
				protElement.setObjectId(Constants.getCurrentAndFuturePGAndPEName(obj.getId()));
				protElement.setProtectionElementName(Constants.getCurrentAndFuturePGAndPEName(obj.getId()));
				protElement.setProtectionElementDescription("For All Current & Future CP's for Site with Id "
						+ obj.getId().toString());
				protElement.setApplication(privilegeUtility.getApplication(SecurityManagerPropertiesLocator
						.getInstance().getApplicationCtxName()));
				final ProtectionElementSearchCriteria searchCriteria = new ProtectionElementSearchCriteria(
						protElement);
				peList = privilegeUtility.getUserProvisioningManager().getObjects(searchCriteria);
				if (peList != null && !peList.isEmpty())
				{
					protElement = peList.get(0);
				}
				else
				{
					privilegeUtility.getUserProvisioningManager().createProtectionElement(protElement);
				}
				peList.add(protElement);
			}
			else
			{
				protElement.setObjectId(obj.getObjectId());
				final ProtectionElementSearchCriteria searchCriteria = new ProtectionElementSearchCriteria(
						protElement);

				peList = privilegeUtility.getUserProvisioningManager().getObjects(searchCriteria);
			}
			final ProtectionGroup protGroup = new ProtectionGroup();
			protGroup.setProtectionGroupName(protectionGroupName);
			peSet.addAll(peList);
			protGroup.setProtectionElements(peSet);
			new PrivilegeUtility().getUserProvisioningManager().createProtectionGroup(protGroup);
		}
		catch (final Exception e)
		{
			UserBizLogic.LOGGER.error(e.getMessage(), e);
		}

	}

	/**
	 * Distribute map data.
	 *
	 * @param userRowIdMap
	 *            the user row id map
	 * @param cpPrivilegeMap
	 *            the cp privilege map
	 * @param sitePrivilegeMap
	 *            the site privilege map
	 */
	private void distributeMapData(final Map<String, SiteUserRolePrivilegeBean> userRowIdMap,
			final Map<String, SiteUserRolePrivilegeBean> cpPrivilegeMap,
			final Map<String, SiteUserRolePrivilegeBean> sitePrivilegeMap)
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
	 *
	 * @param userId
	 *            user Id
	 *
	 * @return user
	 *
	 * @throws SMException
	 *             Generic SM Exception
	 */
	private gov.nih.nci.security.authorization.domainobjects.User getUserByID(final String userId)
	throws SMException
	{
		final gov.nih.nci.security.authorization.domainobjects.User user = SecurityManagerFactory
		.getSecurityManager().getUserById(userId);

		return user;
	}

	/**
	 * Update user.
	 *
	 * @param dao
	 *            DAO object.
	 * @param obj
	 *            The object to be updated
	 * @param oldObj
	 *            The old object
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 *
	 * @throws BizLogicException
	 *             BizLogic Exception
	 */
	public void updateUser(final DAO dao, final Object obj, final Object oldObj,
			final SessionDataBean sessionDataBean) throws BizLogicException
			{
		this.update(dao, obj, oldObj, sessionDataBean);
			}

	/**
	 * Updates the persistent object in the database.
	 *
	 * @param obj
	 *            The object to be updated.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @param dao
	 *            the dao
	 * @param oldObj
	 *            the old obj
	 *
	 * @throws BizLogicException
	 *             BizLogic Exception
	 */
	@Override
	protected void update(final DAO dao, final Object obj, final Object oldObj,
			final SessionDataBean sessionDataBean) throws BizLogicException
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
		// Method to populate rowIdMap in case, Add Privilege button is not
		// clicked
		userRowIdMap = getUserRowIdMap(user, userRowIdMap);

		final User oldUser = (User) oldObj;

		boolean isLoginUserUpdate = false;
		if (sessionDataBean!=null && oldUser.getLoginName().equals(sessionDataBean.getUserName()))
		{
			isLoginUserUpdate = true;
		}

		// If the user is rejected, its record cannot be updated.
		if (Status.ACTIVITY_STATUS_REJECT.toString().equals(oldUser.getActivityStatus()))
		{

			throw getBizLogicException(null, "errors.editRejectedUser", "");
		}
		else if (Status.ACTIVITY_STATUS_NEW.toString().equals(oldUser.getActivityStatus())
				|| Status.ACTIVITY_STATUS_PENDING.toString().equals(oldUser.getActivityStatus()))
		{
			// If the user is not approved yet, its record cannot be updated.
			throw getBizLogicException(null, "errors.editNewPendingUser", "");
		}

		try
		{
			if (Constants.ACTIVITY_STATUS_LOCKED.equals(oldUser.getActivityStatus()))
			{
//				String userStatus = migra.checkForMigratedUser(user.getLoginName(),
//						edu.wustl.wustlkey.util.global.Constants.APPLICATION_NAME);
//				if (edu.wustl.wustlkey.util.global.Constants.CSM.equals(userStatus))
//				{
//					user.setFirstTimeLogin(Boolean.TRUE);
//				}
			}
			// Get the csm userId if present.
			String csmUserId = null;

			/**
			 * Santosh: Changes done for Api User should not edit the first time
			 * login field.
			 */
			if (user.getFirstTimeLogin() == null)
			{
				throw getBizLogicException(null, "domain.object.null.err.msg", "First Time Login");
			}

			if (oldUser.getFirstTimeLogin() != null
					&& user.getFirstTimeLogin().booleanValue() != oldUser.getFirstTimeLogin().booleanValue())
			{
				throw getBizLogicException(null, "errors.cannotedit.firsttimelogin", "");
			}

			if (user.getCsmUserId() != null)
			{
				csmUserId = user.getCsmUserId().toString();
			}

			final gov.nih.nci.security.authorization.domainobjects.User csmUser = SecurityManagerFactory
			.getSecurityManager().getUserById(csmUserId);

			// Bug:7979
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
					throw getBizLogicException(null, "errors.oldPassword.wrong", "");
				}

				// Added for Password validation by Supriya Dankh.
				final Validator validator = new Validator();
				if (!validator.isEmpty(user.getNewPassword()) && !validator.isEmpty(oldPassword))
				{
					passwordValidation(user, oldUser, oldPassword);
				}
				csmUser.setPassword(user.getNewPassword());

				// Set values in password domain object and adds changed
				// password in Password Collection
				final Password password = new Password(PasswordManager.encrypt(user.getNewPassword()), user);
				user.getPasswordCollection().add(password);

			}
			else if ("pageOfResetPassword".equals(user.getPageOf()))
			{
				// Added for Password validation by Supriya Dankh.

				final Validator validator = new Validator();
				if (!validator.isEmpty(user.getNewPassword()))
				{
					passwordValidation(user, oldUser, null);
				}
				csmUser.setPassword(user.getNewPassword());

				// Set values in password domain object and adds changed
				// password in Password Collection
				final Password password = new Password(PasswordManager.encrypt(user.getNewPassword()), user);
				user.getPasswordCollection().add(password);

			}


			// Bug-1516: Jitendra Administartor should be able to edit the
			// password
			else if (Constants.PAGE_OF_USER_ADMIN.equals(user.getPageOf())
					&& !user.getNewPassword().equals(csmUser.getPassword()))
			{
				final Validator validator = new Validator();
				if (!validator.isEmpty(user.getNewPassword()))
				{
					passwordValidation(user, oldUser, oldPassword);
				}
				csmUser.setPassword(user.getNewPassword());
				// Set values in password domain object and
				// adds changed password in Password Collection
				final Password password = new Password(PasswordManager.encrypt(user.getNewPassword()), user);
				user.getPasswordCollection().add(password);
				user.setFirstTimeLogin(Boolean.TRUE);
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
						edu.wustl.catissuecore.util.global.Variables.sessionDataMap.remove(user.getLoginName());
					}
				}

				if (userRowIdMap != null && !userRowIdMap.isEmpty())
				{
					updateUserDetails(user, userRowIdMap);
				}

				final Vector authorizationData = new Vector();
				final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();

				insertCPSitePrivileges(user, authorizationData, userRowIdMap);

				boolean flag = true;

				final Set protectionObjects = new HashSet();
				protectionObjects.add(user);
				final PrivilegeUtility privilegeUtility = new PrivilegeUtility();
				try
				{
					final ProtectionElement protectionElement = privilegeUtility.getUserProvisioningManager()
					.getProtectionElement(User.class.getName() + "_" + user.getId().toString());
				}
				catch (final CSObjectNotFoundException e)
				{
					UserBizLogic.LOGGER.error(e.getMessage(), e);
					flag = false;
					privilegeManager.insertAuthorizationData(authorizationData, protectionObjects, null, user
							.getObjectId());
				}
				if (flag)
				{
					privilegeManager.insertAuthorizationData(authorizationData, null, null, user.getObjectId());
				}

				dao.update(user.getAddress(), oldUser.getAddress());

			}

			if (Constants.PAGE_OF_CHANGE_PASSWORD.equals(user.getPageOf()))
			{
				user.setFirstTimeLogin(Boolean.FALSE);
			}
			dao.update(user, oldUser);

			// Modify the csm user.
			SecurityManagerFactory.getSecurityManager().modifyUser(csmUser);
			if (isIdpEnabled())
			{
				IdPManager idp = IdPManager.getInstance();
				idp.addUserToQueue(SecurityManagerPropertiesLocator.getInstance()
						.getApplicationCtxName(), csmUser);
			}
			if (isLoginUserUpdate)
			{
				sessionDataBean.setUserName(csmUser.getLoginName());
			}

			if (!oldUser.getEmailAddress().equals(user.getEmailAddress()))
			{
				updateWustlKey(user);
			}
		}
		catch (final SMException smExp)
		{
			UserBizLogic.LOGGER.error(smExp.getMessage(), smExp);
			throw getBizLogicException(smExp, "sm.operation.error", "Error in checking has privilege");
		}
		catch (final PasswordEncryptionException e)
		{
			UserBizLogic.LOGGER.error(e.getMessage(), e);
			throw getBizLogicException(e, "pwd.encrytion.error", "");
		}
		catch (final ApplicationException e)
		{
			UserBizLogic.LOGGER.error(e.getMessage(), e);
			// ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
			throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
			}

	/**
	 * This method validate password.
	 *
	 * @param user
	 *            User object.
	 * @param oldUser
	 *            old User object
	 * @param oldPassword
	 *            old Password
	 *
	 * @throws PasswordEncryptionException
	 *             Generic Password Encryption Exception
	 * @throws BizLogicException
	 *             BizLogic Exception
	 */
	private void passwordValidation(final User user, final User oldUser, final String oldPassword)
	throws PasswordEncryptionException, BizLogicException
	{
		final int result = validatePassword(oldUser, user.getNewPassword(), oldPassword);

		LOGGER.debug("return from Password validate " + result);

		// if validatePassword method returns value greater than zero then
		// validation fails
		if (result != SUCCESS)
		{
			// get error message of validation failure
			final List<String> parameters = new ArrayList<String>();
			final String errorKey = getPasswordErrorMsg(result, parameters);
			final StringBuffer strBuf = new StringBuffer("");
			for (final String msgValue : parameters)
			{
				strBuf.append(msgValue).append(":");
			}

			LOGGER.debug("errorKey" + errorKey);
			throw getBizLogicException(null, errorKey, strBuf.toString());
		}
	}

	/**
	 * Returns the list of NameValueBeans with name as "LastName,Firstname" and
	 * value as systemtIdentifier, of all users who are not disabled.
	 *
	 * @param operation
	 *            the operation
	 *
	 * @return the list of NameValueBeans with name as "LastName,Firstname" and
	 *         value as systemtIdentifier, of all users who are not disabled.
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */
	public Vector getUsers(final String operation) throws BizLogicException
	{
		final List users = getActiveUserList(operation);

		final Vector nameValuePairs = new Vector();
		nameValuePairs.add(new NameValueBean(Constants.SELECT_OPTION, String
				.valueOf(Constants.SELECT_OPTION_VALUE)));

		// If the list of users retrieved is not empty.
		if (!users.isEmpty())
		{
			// Creating name value beans.
			for (int i = 0; i < users.size(); i++)
			{
				// Changes made to optimize the query to get only required
				// fields data
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
	 * This function returns Name-Value list of all active Clinportal Application Users.
	 * @param operation type of operation add/edit
	 * @return list of Users as NameValueBean List
	 * @throws BizLogicException
	 */
	public List<NameValueBean> getUsersNameValueList(final String operation) throws BizLogicException
	{
		final List users = getActiveUserList(operation);
		// If the list of users retrieved is not empty.
		List<NameValueBean> nameValuePairs = new ArrayList<NameValueBean>();
		if (!users.isEmpty())
		{
			// Creating name value beans.
			for (int i = 0; i < users.size(); i++)
			{
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
	 * This returns list of all the active users of Clinportal Application.
	 * @param operation type of operation
	 * @return list of Users
	 * @throws BizLogicException
	 */
	private List getActiveUserList(final String operation)
			throws BizLogicException {
		final String sourceObjectName = User.class.getName();
		// Get only the fields required
		final String[] selectColumnName = { Constants.SYSTEM_IDENTIFIER, Constants.LASTNAME, Constants.FIRSTNAME };
		String[] whereColumnName;
		String[] whereColumnCondition;
		Object[] whereColumnValue;
		String joinCondition;
		if (operation != null && operation.equalsIgnoreCase(Constants.ADD))
		{
			final String tmpArray1[] = { Status.ACTIVITY_STATUS.toString() };
			final String tmpArray2[] = { Constants.EQUALS };
			final String tmpArray3[] = { Status.ACTIVITY_STATUS_ACTIVE.toString() };
			whereColumnName = tmpArray1;
			whereColumnCondition = tmpArray2;
			whereColumnValue = tmpArray3;
			joinCondition = null;
		}
		else
		{
			final String tmpArray1[] = { Status.ACTIVITY_STATUS.toString(), Status.ACTIVITY_STATUS.toString() };
			final String tmpArray2[] = { Constants.EQUALS, Constants.EQUALS };
			final String tmpArray3[] = { Status.ACTIVITY_STATUS_ACTIVE.toString(),
					Status.ACTIVITY_STATUS_CLOSED.toString() };
			whereColumnName = tmpArray1;
			whereColumnCondition = tmpArray2;
			whereColumnValue = tmpArray3;
			joinCondition = Constants.OR_JOIN_CONDITION;
		}
		// Retrieve the users whose activity status is not disabled.
		final List users = this.retrieve(sourceObjectName, selectColumnName, whereColumnName,
				whereColumnCondition, whereColumnValue, joinCondition);
		return users;
	}
	


	/**
	 * Returns the list of NameValueBeans with name as "LastName,Firstname" and
	 * value as systemtIdentifier, of all users who are not disabled.
	 *
	 * @return the list of NameValueBeans with name as "LastName,Firstname" and
	 *         value as systemtIdentifier, of all users who are not disabled.
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 * @throws SMException
	 *             the SM exception
	 */
	public Vector getCSMUsers() throws BizLogicException, SMException
	{
		// Retrieve the users whose activity status is not disabled.
		final List users = SecurityManagerFactory.getSecurityManager().getUsers();

		final Vector nameValuePairs = new Vector();
		nameValuePairs.add(new NameValueBean(Constants.SELECT_OPTION, String
				.valueOf(Constants.SELECT_OPTION_VALUE)));

		// If the list of users retrieved is not empty.
		if (!users.isEmpty())
		{
			// Creating name value beans.
			for (int i = 0; i < users.size(); i++)
			{
				final gov.nih.nci.security.authorization.domainobjects.User user = (gov.nih.nci.security.authorization.domainobjects.User) users
				.get(i);
				final NameValueBean nameValueBean = new NameValueBean();
				nameValueBean.setName(user.getLastName() + ", " + user.getFirstName());
				nameValueBean.setValue(String.valueOf(user.getUserId()));
				LOGGER.debug(nameValueBean.toString());
				nameValuePairs.add(nameValueBean);
			}
		}

		Collections.sort(nameValuePairs);
		return nameValuePairs;
	}

	/**
	 * Returns a list of users according to the column name and value.
	 *
	 * @param colName
	 *            column name on the basis of which the user list is to be
	 *            retrieved.
	 * @param colValue
	 *            Value for the column name.
	 * @param className
	 *            the class name
	 *
	 * @return the list
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */
	@Override
	public List retrieve(final String className, final String colName, final Object colValue)
	throws BizLogicException
	{
		List userList = null;
		LOGGER.debug("In user biz logic retrieve........................");
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
					// Get the role of the user.
					final Role role = SecurityManagerFactory.getSecurityManager().getUserRole(
							appUser.getCsmUserId().longValue());
					// logger.debug("In USer biz logic.............role........id......."
					// + role.getId().toString());

					if (role != null)
					{
						appUser.setRoleId(role.getId().toString());
					}
				}
			}
		}
		catch (final SMException smExp)
		{
			UserBizLogic.LOGGER.error(smExp.getMessage(), smExp);
			throw getBizLogicException(smExp, "sm.check.priv", "");
		}

		return userList;
	}

	/**
	 * Retrieves and sends the login details email to the user whose email
	 * address is passed else returns the error key in case of an error.
	 *
	 * @param emailAddress
	 *            the email address of the user whose password is to be sent.
	 * @param sessionData
	 *            the session data
	 *
	 * @return the error key in case of an error.
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */
	public String sendForgotPassword(final String emailAddress, final SessionDataBean sessionData)
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

					// Send the login details email to the user.
					boolean emailStatus = false;
					String userToken=UniqueIDGenerator.getUniqueID();
					emailStatus = emailHandler.sendForgotPasswordEmail(user, userToken);

					if (emailStatus)
					{
						// if success commit
						/**
						 * Update the field FirstTimeLogin which will ensure
						 * user changes his password on login Note --> We can
						 * not use CommonAddEditAction to update as the user has
						 * not still logged in and user authorisation will fail.
						 * So writing separate code for update.
						 */

						user.setForgotPasswordToken(userToken);
						dao = openDAOSession(sessionData);
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
					// Error key if the user is not active.
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
			UserBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final ApplicationException e)
		{
			UserBizLogic.LOGGER.error(e.getMessage(), e);
			throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally
		{
			closeDAOSession(dao);
		}
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute
	 * values.
	 *
	 * @param obj
	 *            the obj
	 * @param dao
	 *            the dao
	 * @param operation
	 *            the operation
	 *
	 * @return true, if validate
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */
	@Override
	protected boolean validate(final Object obj, final DAO dao, final String operation) throws BizLogicException
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

		if (Constants.PAGE_OF_SIGNUP.equals(user.getPageOf()))
		{
			if (!Validator.isEmpty(user.getTargetIdp()))
			{
				validateIDPInformation(user);
			}
		}
		/**
		 * Start: Change for API Search --- Jitendra 06/10/2006 In Case of Api
		 * Search, previoulsy it was failing since there was default class level
		 * initialization on domain object. For example in User object, it was
		 * initialized as protected String lastName=""; So we removed default
		 * class level initialization on domain object and are initializing in
		 * method setAllValues() of domain object. But in case of Api Search,
		 * default values will not get set since setAllValues() method of
		 * domainObject will not get called. To avoid null pointer exception, we
		 * are setting the default values same as we were setting in
		 * setAllValues() method of domainObject.
		 */
		ApiSearchUtil.setUserDefault(user);
		if (Constants.PAGE_OF_CHANGE_PASSWORD.equals(user.getPageOf()) == false)
		{
			// if condition added by Geeta for ECMC
			/*if ((user.getAddress().getState() != "null" && user.getAddress().getState() != "")
					&& edu.wustl.catissuecore.util.global.Variables.isStateRequired)
			{
				if (!Validator.isEnumeratedValue(AppUtility.getStateList(), user.getAddress().getState()))
				{

					throw getBizLogicException(null, "state.errMsg", "");
				}
			}*/
			if (!Validator.isEnumeratedValue(CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_COUNTRY_LIST, null), user.getAddress().getCountry()))
			{
				throw getBizLogicException(null, "country.errMsg", "");
			}

			if (Constants.PAGE_OF_USER_ADMIN.equals(user.getPageOf()))
			{
				if (operation.equals(Constants.ADD))
				{
					if (!Status.ACTIVITY_STATUS_ACTIVE.toString().equals(user.getActivityStatus()))
					{
						throw getBizLogicException(null, "activityStatus.active.errMsg", "");
					}
				}
				else
				{
					if (!Validator.isEnumeratedValue(Constants.USER_ACTIVITY_STATUS_VALUES, user
							.getActivityStatus()))
					{
						throw getBizLogicException(null, "activityStatus.errMsg", "");
					}
				}
			}

			// Added by Ashish
			/**
			 * Two more parameter 'dao' and 'operation' is added by Vijay Pande
			 * to use it in isUniqueEmailAddress method
			 */
			apiValidate(user, dao, operation);
			// END
		}
		return true;
	}

	private void validateIDPInformation(final User user) throws BizLogicException
	{
		validateForEmptyMigrationFields(user);
		validateMigrationRule(user);
		authenticateMigrationCredentials(user);
	}

	private void validateMigrationRule(final User user) throws BizLogicException
	{
		final UserDetails userDetails = new UserDetails();
		userDetails.setLoginName(user.getLoginName());
		userDetails.setMigratedLoginName(user.getTargetIdpLoginName());
		userDetails.setTargetIDP(user.getTargetIdp());
		try
		{
			final boolean statisfiesRule=Utility.isStatisfyMigrationRule(userDetails);
			if(!statisfiesRule)
			{
				throw getBizLogicException(null, "app.migration.rule.check.error", "");
			}
		}
		catch (final MigrationRuleException e)
		{
			LOGGER.debug(e);
			throw getBizLogicException(null, "app.migration.rule.check.error", "");
		}
	}

	private void authenticateMigrationCredentials(final User user) throws BizLogicException
	{
		try
		{
			final IDPAuthManager authManager = AuthManagerFactory.getInstance().getAuthManagerInstance(
					user.getTargetIdp());

			final LoginCredentials loginCredentials = new LoginCredentials();
			loginCredentials.setLoginName(user.getTargetIdpLoginName());
			loginCredentials.setPassword(user.getTargetPassword());
			final boolean authenticationSuccesful = authManager.authenticate(loginCredentials);
			if (!authenticationSuccesful)
			{
				LOGGER.debug("Authentication of user against " + user.getTargetIdp() + " failed.");
				throw getBizLogicException(null, "errors.incorrectLoginIDPassword", "");
			}
		}
		catch (final AuthenticationException e)
		{
			LOGGER.debug(e);
			throw getBizLogicException(null, "app.target.idp.auth.error", "");
		}
	}

	private void validateForEmptyMigrationFields(final User user) throws BizLogicException
	{
		if (Validator.isEmpty(user.getTargetIdpLoginName()))
		{
			if (Validator.isEmpty(user.getTargetPassword()))
			{
				throw getBizLogicException(null, "app.target.loginname.password.required", "");
			}
			else
			{
				throw getBizLogicException(null, "errors.item.required", "user.loginName");
			}
		}
		else if (Validator.isEmpty(user.getTargetPassword()))
		{
			throw getBizLogicException(null, "errors.item.required", "user.password");
		}
	}

	// Added by Ashish
	/**
	 * Api validate.
	 *
	 * @param user
	 *            user
	 * @param dao
	 *            the dao
	 * @param operation
	 *            the operation
	 *
	 * @return true, if api validate
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */

	private boolean apiValidate(final User user, final DAO dao, final String operation) throws BizLogicException
	{
		final Validator validator = new Validator();
		String message = "";
		final boolean validate = true;

		if (validator.isEmpty(user.getEmailAddress()))
		{
			message = ApplicationProperties.getValue(USER_EMAIL_ADDRESS);
			throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
		}
		else
		{
			if (!validator.isValidEmailAddress(user.getEmailAddress()))
			{
				message = ApplicationProperties.getValue(USER_EMAIL_ADDRESS);
				throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
			}
			/**
			 * Name : Vijay_Pande Reviewer : Sntosh_Chandak Bug ID: 4185_2 Patch
			 * ID: 1-2 See also: 1 Description: Wrong error meassage was
			 * dispayed while adding user with existing email address in use.
			 * Following method is provided to verify whether the email address
			 * is already present in the system or not.
			 */
			if (!(isUniqueEmailAddress(user.getEmailAddress(), user.getId(), dao, operation)))
			{
				String arguments[] = null;
				arguments = new String[] { "User", ApplicationProperties.getValue(USER_EMAIL_ADDRESS) };
				final String errMsg = new DefaultExceptionFormatter().getErrorMessage("Err.ConstraintViolation",
						arguments);
				LOGGER.debug("Unique Constraint Violated: " + errMsg);
				throw getBizLogicException(null, "Err.ConstraintViolation", "User :"
						+ ApplicationProperties.getValue(USER_EMAIL_ADDRESS));
			}
			//            if (null != user.getTargetIdpLoginName() && !"".equals(user.getTargetIdpLoginName()))
			//            {
			//                if (!user.getEmailAddress().endsWith(edu.wustl.wustlkey.util.global.Constants.WUSTL_EDU)
			//                        && !user.getEmailAddress().endsWith(
			//                                edu.wustl.wustlkey.util.global.Constants.WUSTL_EDU_CAPS))
			//                {
			//                    message = ApplicationProperties.getValue("email.washu.user");
			//                    throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
			//                }
			//            }
		}
		if (validator.isEmpty(user.getLastName()))
		{
			message = ApplicationProperties.getValue("user.lastName");
			throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
		}
		else if (validator.isXssVulnerable(user.getLastName()))
		{
			message = ApplicationProperties.getValue("user.lastName");
			throw getBizLogicException(null, "errors.xss.invalid", message);
		}

		if (validator.isEmpty(user.getFirstName()))
		{
			message = ApplicationProperties.getValue("user.firstName");
			throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
		}
		else if (validator.isXssVulnerable(user.getFirstName()))
		{
			message = ApplicationProperties.getValue("user.firstName");
			throw getBizLogicException(null, "errors.xss.invalid", message);
		}

		if (validator.isEmpty(user.getAddress().getCity()))
		{
			message = ApplicationProperties.getValue("user.city");
			throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
		}
	/*	if (edu.wustl.catissuecore.util.global.Variables.isStateRequired
				&& (!validator.isValidOption(user.getAddress().getState()) || validator.isEmpty(user.getAddress()
						.getState())))
		{
			message = ApplicationProperties.getValue("user.state");
			throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
		}*/
		if (!validator.isValidOption(user.getAddress().getCountry())
				|| validator.isEmpty(user.getAddress().getCountry()))
		{
			message = ApplicationProperties.getValue("user.country");
			throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
		}
		if (user.getInstitution().getId() == null || user.getInstitution().getId().longValue() <= 0)
		{
			message = ApplicationProperties.getValue("user.institution");
			throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
		}

		if (user.getDepartment().getId() == null || user.getDepartment().getId().longValue() <= 0)
		{
			message = ApplicationProperties.getValue("user.department");
			throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
		}

		if (user.getCancerResearchGroup().getId() == null
				|| user.getCancerResearchGroup().getId().longValue() <= 0)
		{
			message = ApplicationProperties.getValue("user.cancerResearchGroup");
			throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
		}
		return validate;
	}

	/**
	 * Validate password.
	 *
	 * @param oldUser
	 *            User object
	 * @param newPassword
	 *            New Password value
	 * @param oldPassword
	 *            Old Password value
	 *
	 * @return SUCCESS (constant int 0) if all condition passed else return
	 *         respective error code (constant int) value
	 *
	 * @throws PasswordEncryptionException
	 *             the password encryption exception
	 */
	private int validatePassword(final User oldUser, final String newPassword, final String oldPassword)
	throws PasswordEncryptionException
	{
		final List oldPwdList = new ArrayList(oldUser.getPasswordCollection());
		Collections.sort(oldPwdList);
		if (oldPwdList != null && !oldPwdList.isEmpty())
		{
			// Check new password is equal to last n password if value
			final String encryptedPassword = PasswordManager.encrypt(newPassword);
			if (checkPwdNotSameAsLastN(encryptedPassword, oldPwdList))
			{
				LOGGER.debug("Password is not valid returning FAIL_SAME_AS_LAST_N");
				return FAIL_SAME_AS_LAST_N;
			}

			// Get the last updated date of the password
			final Date lastestUpdateDate = ((Password) oldPwdList.get(0)).getUpdateDate();
			final boolean firstTimeLogin = getFirstLogin(oldUser);
			if (!firstTimeLogin && checkPwdUpdatedOnSameDay(lastestUpdateDate))
			{

				LOGGER.debug("Password is not valid returning FAIL_CHANGED_WITHIN_SOME_DAY");
				return FAIL_CHANGED_WITHIN_SOME_DAY;
			}

			/**
			 * to check password does not contain user name,surname,email
			 * address. if same return FAIL_SAME_NAME_SURNAME_EMAIL eg.
			 * username=XabcY@abc.com newpassword=abc is not valid
			 */

			String emailAddress = oldUser.getEmailAddress();
			final int usernameBeforeMailaddress = emailAddress.indexOf('@');
			// get substring of emailAddress before '@' character
			emailAddress = emailAddress.substring(0, usernameBeforeMailaddress);
			final String userFirstName = oldUser.getFirstName();
			final String userLastName = oldUser.getLastName();

			final StringBuffer sb = new StringBuffer(newPassword);
			if (emailAddress != null && newPassword.toLowerCase().indexOf(emailAddress.toLowerCase()) != -1)
			{
				LOGGER.debug("Password is not valid returning FAIL_SAME_NAME_SURNAME_EMAIL");
				return FAIL_SAME_NAME_SURNAME_EMAIL;
			}

			if (userFirstName != null && newPassword.toLowerCase().indexOf(userFirstName.toLowerCase()) != -1)
			{
				LOGGER.debug("Password is not valid returning FAIL_SAME_NAME_SURNAME_EMAIL");
				return FAIL_SAME_NAME_SURNAME_EMAIL;
			}

			if (userLastName != null && newPassword.toLowerCase().indexOf(userLastName.toLowerCase()) != -1)
			{
				LOGGER.debug("Password is not valid returning FAIL_SAME_NAME_SURNAME_EMAIL");
				return FAIL_SAME_NAME_SURNAME_EMAIL;
			}

		}
		return SUCCESS;
	}

	/**
	 * This function checks whether user has logged in for first time or whether
	 * user's password is expired. In both these case user needs to change his
	 * password so Error key is returned
	 *
	 * @param user
	 *            - user object
	 *
	 * @return String
	 *
	 * @throws BizLogicException
	 *             - throws BizLogicException
	 */
	public String checkFirstLoginAndExpiry(final User user)
	{
		final List passwordList = new ArrayList(user.getPasswordCollection());
/*Commented below code as we are sending set password link in approval mail */
//		final boolean firstTimeLogin = getFirstLogin(user);
//		// If user has logged in for the first time, return key of Change
//		// password on first login
//		if (firstTimeLogin)
//		{
//			return "errors.changePassword.changeFirstLogin";
//		}

		Collections.sort(passwordList);
		final Password lastPassword = (Password) passwordList.get(0);
		final Date lastUpdateDate = lastPassword.getUpdateDate();

		final Validator validator = new Validator();
		// Get difference in days between last password update date and current
		// date.
		final long dayDiff = validator.getDateDiff(lastUpdateDate, new Date());
		final int expireDaysCount = Integer.parseInt(XMLPropertyHandler.getValue("password.expire_after_n_days"));
		LOGGER.info("expireDaysCount" + expireDaysCount);
		LOGGER.info("dayDiff" + dayDiff);
		if (dayDiff > expireDaysCount)
		{
			LOGGER.info("returning error change password expire");
			return "errors.changePassword.expire";
		}
		return Constants.SUCCESS;

	}

	/**
	 * This function will check if the user is First time logging.
	 *
	 * @param user
	 *            user object
	 *
	 * @return firstTimeLogin
	 */
	public boolean getFirstLogin(final User user)
	{
		boolean firstTimeLogin = false;
		if (user.getFirstTimeLogin() != null)
		{
			firstTimeLogin = user.getFirstTimeLogin().booleanValue();
		}
		return firstTimeLogin;
	}

	/**
	 * Check pwd not same as last n.
	 *
	 * @param newPassword
	 *            newPassword
	 * @param oldPwdList
	 *            oldPwdList
	 *
	 * @return boolean
	 */
	private boolean checkPwdNotSameAsLastN(final String newPassword, final List oldPwdList)
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
	 * Check pwd updated on same day.
	 *
	 * @param lastUpdateDate
	 *            Date last updated
	 *
	 * @return boolean
	 */
	private boolean checkPwdUpdatedOnSameDay(final Date lastUpdateDate)
	{
		final Validator validator = new Validator();
		// Get difference in days between last password update date and current
		// date.
		final long dayDiff = validator.getDateDiff(lastUpdateDate, new Date());
		final int dayDiffConstant = Integer.parseInt(XMLPropertyHandler.getValue("daysCount"));
		if (dayDiff < dayDiffConstant)
		{
			LOGGER.debug("Password is not valid returning FAIL_CHANGED_WITHIN_SOME_DAY");
			return true;
		}
		return false;
	}

	/**
	 * Gets the password error msg.
	 *
	 * @param errorCode
	 *            int value return by validatePassword() method
	 * @param parameters
	 *            List of strings
	 *
	 * @return String error message with respect to error code
	 */
	private String getPasswordErrorMsg(final int errorCode, final List<String> parameters)
	{
		String errMsg = "";
		switch (errorCode)
		{
			case FAIL_SAME_AS_LAST_N:
				final String dayCount = String.valueOf(Integer.parseInt(XMLPropertyHandler
						.getValue("password.not_same_as_last_n")));
				parameters.add(dayCount);
				errMsg = "errors.newPassword.sameAsLastn";
				break;
			case FAIL_FIRST_LOGIN:
				errMsg = "errors.changePassword.changeFirstLogin";
				break;
			case FAIL_EXPIRE:
				errMsg = "errors.changePassword.expire";
				break;
			case FAIL_CHANGED_WITHIN_SOME_DAY:
				final Integer daysCount = Integer.parseInt(XMLPropertyHandler.getValue("daysCount"));
				parameters.add(daysCount.toString());
				if (daysCount.intValue() == 1)
				{
					// errMsg =
					// ApplicationProperties.getValue("errors.changePassword.sameDay");
					errMsg = "errors.changePassword.sameDay";
				}
				else
				{
					errMsg = "errors.changePassword.afterSomeDays";
				}
				break;
			case FAIL_SAME_NAME_SURNAME_EMAIL:
				errMsg = "errors.changePassword.sameAsNameSurnameEmail";
				break;
			case FAIL_PASSWORD_EXPIRED:
				errMsg = "errors.changePassword.expire";
			default:
				errMsg = "errors.newPassword.genericmessage";
				break;
		}
		return errMsg;
	}

	/**
	 * Name : Vijay_Pande Reviewer : Sntosh_Chandak Bug ID: 4185_2 Patch ID: 1-2
	 * See also: 1 Description: Wrong error meassage was dispayed while adding
	 * user with existing email address in use. Following method is provided to
	 * verify whether the email address is already present in the system or not.
	 *
	 * @param emailAddress
	 *            the email address
	 * @param userId
	 *            the user id
	 * @param dao
	 *            the dao
	 * @param operation
	 *            the operation
	 *
	 * @return true, if checks if is unique email address
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */
	/**
	 * Method to check whether email address already exist or not
	 *
	 * @param emailAddress
	 *            email address to be check
	 * @param dao
	 *            an object of DAO
	 * @param userId
	 *            User identifier
	 * @return isUnique boolean value to indicate presence of similar email
	 *         address
	 * @throws BizLogicException
	 *             database exception
	 */
	private boolean isUniqueEmailAddress(final String emailAddress, final Long userId, final DAO dao,
			final String operation) throws BizLogicException
			{
		boolean isUnique = true;
		try
		{
			final String sourceObjectName = User.class.getName();
			final String[] selectColumnName = new String[] { "id", "emailAddress" };

			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("emailAddress", emailAddress));

			final List userList = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);

			if (!userList.isEmpty())
			{
				final Object[] objects = (Object[]) userList.get(0);

				if (operation.equalsIgnoreCase(Constants.ADD))
				{
					isUnique = false;
				}
				else
				{
					if (emailAddress.equals(objects[1]) && !userId.equals(objects[0]))
					{
						isUnique = false;
					}
				}

			}

		}
		catch (final DAOException daoExp)
		{
			UserBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return isUnique;
			}

	/**
	 * Set Role to user object before populating actionForm out of it.
	 *
	 * @param domainObj
	 *            object of AbstractDomainObject
	 * @param uiForm
	 *            object of the class which implements IValueObject
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */
	@Override
	protected void prePopulateUIBean(final AbstractDomainObject domainObj, final IValueObject uiForm)
	throws BizLogicException
	{
		UserBizLogic.LOGGER.info("Inside prePopulateUIBean method of UserBizLogic...");

		final User user = (User) domainObj;
		Role role = null;
		if (user.getCsmUserId() != null)
		{
			try
			{
				// Get the role of the user.
				role = SecurityManagerFactory.getSecurityManager().getUserRole(user.getCsmUserId().longValue());
				if (role != null)
				{
					user.setRoleId(role.getId().toString());
				}
			}
			catch (final SMException e)
			{
				UserBizLogic.LOGGER.error("SMException in " + "prePopulateUIBean method of UserBizLogic..."
						+ e.getMessage(), e);
			}
		}
	}

	

	/** This method gets all the user Related cp's and creates the CpAndParticipentsBean of CPS for which user has either
	 *  Registration or Specimen_Processing privilege.
	 * @param userId
	 * @return
	 * @throws BizLogicException
	 */
	public List<CpAndParticipentsBean> getRelatedCPAndParticipantBean(final Long userId)
	throws BizLogicException
	{
		DAO dao = null;
		Collection<CollectionProtocol> userCpCollection = new HashSet<CollectionProtocol>();
		Collection<CollectionProtocol> userColl = null;
		List<CpAndParticipentsBean> cpIds = new ArrayList<CpAndParticipentsBean>();

		try
		{
			dao = getHibernateDao(getAppName(), null);
			final User user = (User) dao.retrieveById(User.class.getName(), userId);

			if (user.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
			{
			  //If User is Admin get all the Collection Protocol.	
				IFactory factory = null;
				factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
				final CollectionProtocolRegistrationBizLogic cBizLogic = (CollectionProtocolRegistrationBizLogic) factory
						.getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
				cpIds = cBizLogic.getCollectionProtocolBeanList();
				
			}
			else
			{
				userColl = user.getCollectionProtocolCollection();
				userCpCollection = user.getAssignedProtocolCollection();
					final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
					final PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(user.getLoginName());
									
					for (final CollectionProtocol collectionProtocol : userCpCollection)
					{
						final String privilegeName = edu.wustl.common.util.global.Variables.privilegeDetailsMap
						.get(Constants.EDIT_PROFILE_PRIVILEGE);
						
						boolean isPhiView = privilegeCache.hasPrivilege(collectionProtocol.getObjectId(),
								privilegeName);
											
						if (privilegeCache.hasPrivilege(collectionProtocol.getObjectId(),Permissions.SPECIMEN_PROCESSING) ||
								isPhiView)
						{
							cpIds.add(new CpAndParticipentsBean(collectionProtocol.getShortTitle(), 
									collectionProtocol.getId().toString(), isPhiView));
						}
					}
				}
			if(userColl != null)
			{
				for (final CollectionProtocol cp : userColl)
				{
					cpIds.add(new CpAndParticipentsBean(cp.getShortTitle(), cp.getId().toString(), true));
				}
			}
		}
		catch (final DAOException e)
		{
			UserBizLogic.LOGGER.error(e.getMessage(), e);
		}
		catch (final SMException e)
		{
			UserBizLogic.LOGGER.error(e.getMessage(), e);
		}
		finally
		{
			closeDAOSession(dao);
		}

		return cpIds;
	}

	/**
	 * Gets the related site ids.
	 *
	 * @param userId
	 *            user Id
	 *
	 * @return Set
	 *
	 * @throws BizLogicException
	 *             BizLogicException
	 */
	public Set<Long> getRelatedSiteIds(final Long userId) throws BizLogicException
	{
		DAO dao = null;

		HashSet<Long> idSet = null;

		try
		{
			dao = openDAOSession(null);

			String query = "select user.csmUserId " + " from edu.wustl.catissuecore.domain.User user "
			+ " where user.id = ?";
			ColumnValueBean colValueBean = new ColumnValueBean(userId);
			List<ColumnValueBean> colvaluebeanlist = new ArrayList<ColumnValueBean>();
			colvaluebeanlist.add(colValueBean);

			final List<Long> userList = executeQuery(query, colvaluebeanlist);
			boolean isAdminUser = false;
			if (!userList.isEmpty())
			{
				final Long csmUserId = userList.get(0);
				final Role role = SecurityManagerFactory.getSecurityManager().getUserRole(csmUserId);
				if (role != null && role.getId() != null
						&& Constants.ADMIN_USER.equalsIgnoreCase(role.getId().toString()))
				{
					isAdminUser = true;
				}
			}
			if (!isAdminUser)
			{
				query = "select user.siteCollection.id " + " from edu.wustl.catissuecore.domain.User user "
				+ " where user.id = " + userId;
				final List<Long> list = executeQuery(query);

				idSet = new HashSet<Long>();

				for (final Long siteId : list)
				{
					idSet.add(siteId);
				}

			}
		}
		catch (final ApplicationException e1)
		{
			UserBizLogic.LOGGER.error(e1.getMessage(), e1);
			throw getBizLogicException(e1, e1.getErrorKeyName(), e1.getMsgValues());
		}
		finally
		{
			closeDAOSession(dao);
		}
		return idSet;
	}

	/**
	 * Custom method for Add User Case.
	 *
	 * @param dao
	 *            DAO object
	 * @param domainObject
	 *            Domain object
	 *
	 * @return the object id
	 */
	@Override
	public String getObjectId(final DAO dao, final Object domainObject)
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
			if (user.getRoleId() == null || user.getRoleId().equals("") || user.getSiteCollection() == null
					|| user.getSiteCollection().isEmpty())
			{
				return Constants.siteIsRequired;
			}
			try
			{
				userRowIdMap = getUserRowIdMap(user, userRowIdMap);
			}
			catch (final BizLogicException e)
			{
				UserBizLogic.LOGGER.error(e.getMessage(), e);
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
	 * To get PrivilegeName for authorization check from
	 * 'PermissionMapDetails.xml' (non-Javadoc)
	 *
	 * @param domainObject
	 *            the domain object
	 *
	 * @return the privilege key
	 *
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	@Override
	protected String getPrivilegeKey(final Object domainObject)
	{
		return Constants.ADD_EDIT_USER;
	}

	/**
	 * Check user.
	 *
	 * @param domainObject
	 *            the domain object
	 * @param sessionDataBean
	 *            the session data bean
	 *
	 * @return true, if successful
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */
	public boolean checkUser(final Object domainObject, final SessionDataBean sessionDataBean)
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
				throw getBizLogicException(null, "user.cannotCreateSuperAdmin", "");
			}
			userRowIdMap = userDTO.getUserRowIdBeanMap();
			if (userRowIdMap == null)
			{

				if (user.getRoleId().equals(Constants.NON_ADMIN_USER))
				{

					throw getBizLogicException(null, "user.cannotCreateScientist", "");
				}
				if (user.getRoleId() == null || user.getRoleId().equals("") || user.getSiteCollection() == null
						|| user.getSiteCollection().isEmpty())
				{

					throw getBizLogicException(null, "user.siteIsRequired", "");
				}
			}
			else
			{
				final Object[] mapKeys = userRowIdMap.keySet().toArray();
				for (final Object mapKey : mapKeys)
				{
					final String key = mapKey.toString();
					final SiteUserRolePrivilegeBean bean = userRowIdMap.get(key);

					if (bean.getSiteList() == null || bean.getSiteList().isEmpty())
					{
						throw getBizLogicException(null, "user.cannotCreateScientist", "");
					}
				}

			}
		}
		if ("pageOfChangePassword".equalsIgnoreCase(user.getPageOf()) 
				|| "pageOfResetPassword".equalsIgnoreCase(user.getPageOf()))
		{
			return true;
		}
		/*
		 * if(user.getId().equals(sessionDataBean.getUserId())) { throw new
		 * DAOException
		 * (ApplicationProperties.getValue("user.cannotEditOwnPrivileges")); }
		 */
		if ("pageOfSignUp".equalsIgnoreCase(user.getPageOf()))
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
	 * his/her details e.g. Password (non-Javadoc)
	 *
	 * @param dao
	 *            the dao
	 * @param domainObject
	 *            the domain object
	 * @param sessionDataBean
	 *            the session data bean
	 *
	 * @return true, if checks if is authorized
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 *
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.common.dao.DAO,
	 *      java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
	@Override
	public boolean isAuthorized(final DAO dao, final Object domainObject, final SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		try
		{
			boolean isAuthorized = false;
			isAuthorized = checkUser(domainObject, sessionDataBean);
			if (isAuthorized)
			{
				return true;
			}

			final String privilegeName = getPrivilegeName(domainObject);
			final String protectionElementName = getObjectId(dao, domainObject);

			if (Constants.cannotCreateSuperAdmin.equals(protectionElementName))
			{
				throw getBizLogicException(null, "user.cannotCreateSuperAdmin", "");
			}
			if (Constants.siteIsRequired.equals(protectionElementName))
			{

				throw getBizLogicException(null, "user.siteIsRequired", "");
			}

			final PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
					sessionDataBean.getUserName());

			if (protectionElementName == null)
			{
				// return false;
				// bug 11611 and 11659
				throw AppUtility.getUserNotAuthorizedException(privilegeName, protectionElementName, domainObject
						.getClass().getSimpleName());

			}
			else
			{
				final String[] prArray = protectionElementName.split(Constants.UNDERSCORE);
				final String baseObjectId = prArray[0];
				String objId = null;

				for (int i = 1; i < prArray.length; i++)
				{
					objId = baseObjectId + Constants.UNDERSCORE + prArray[i];
					isAuthorized = privilegeCache.hasPrivilege(objId, privilegeName);
					if (!isAuthorized)
					{
						break;
					}
				}

				if (!isAuthorized)
				{
					// bug 11611 and 11659
					throw AppUtility.getUserNotAuthorizedException(privilegeName, protectionElementName,
							domainObject.getClass().getSimpleName());
				}
				return isAuthorized;
			}
		}
		/*
		 * catch(ApplicationException exp) { logger.debug(exp.getMessage(),
		 * exp); throw getBizLogicException(exp, "dao.error", ""); }
		 */
		catch (final SMException e)
		{
			UserBizLogic.LOGGER.error(e.getMessage(), e);
			throw AppUtility.handleSMException(e);
		}
	}

	/**
	 * This method will migrate user to WUSTLKey.
	 *
	 * @param user
	 *            Object of USER
	 *
	 * @throws BizLogicException
	 *             Object of BizLogicException
	 */
	private void updateWustlKey(final User user) throws BizLogicException
	{
		try
		{
			if (user.getTargetIdpLoginName() != null)
			{
				final String queryStr = "UPDATE CSM_MIGRATE_USER SET LOGIN_NAME=? WHERE WUSTLKEY =?";
				final List<ColumnValueBean> parameters = new ArrayList<ColumnValueBean>();
				final ColumnValueBean loginNameBean = new ColumnValueBean(user.getLoginName());
				final ColumnValueBean wustlKeyNameBean = new ColumnValueBean(user.getTargetIdpLoginName());

				parameters.add(loginNameBean);
				parameters.add(wustlKeyNameBean);

				Utility.executeQueryUsingDataSource(queryStr, parameters, true,
						edu.wustl.wustlkey.util.global.Constants.APPLICATION_NAME);
			}
		}
		catch (final ApplicationException e)
		{
			UserBizLogic.LOGGER.error(e.getMessage(), e);
			throw new BizLogicException(ErrorKey.getErrorKey("db.update.data.error"), e,
			"Error in database operation");
		}
	}

	/**
	 *
	 * @param loginName
	 *            : loginName
	 * @return User : User
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public User getUser(final String loginName) throws BizLogicException
	{
		User validUser = null;
		final String getActiveUser = "from " + User.class.getName() + " user where " + "user.activityStatus= "
		+ "'" + Status.ACTIVITY_STATUS_ACTIVE.toString() + "' and user.loginName =" + "'" + loginName
		+ "'";
		final DefaultBizLogic bizlogic = new DefaultBizLogic();
		final List<User> users = bizlogic.executeQuery(getActiveUser);
		if (users != null && !users.isEmpty())
		{
			validUser = users.get(0);
		}
		return validUser;
	}

	/**
	 * @return
	 */
	private boolean isIdpEnabled()
	{
		boolean result = false;
		String idpEnabled = XMLPropertyHandler.getValue(Constants.IDP_ENABLED);
		if (Constants.TRUE.equalsIgnoreCase(idpEnabled))
		{
			result = true;
		}
		return result;
	}
	
	
	public void updatePasswordUsingToken(String resetPasswordToken,String newPassword,String pageOf) throws BizLogicException
	{
		User oldUser = getUserBasedonUserToken(resetPasswordToken);
		User user = oldUser;
		user.setNewPassword(newPassword);
		user.setForgotPasswordToken(null);
		user.setPageOf(pageOf);
		update(user, oldUser, null);

	}
	
	public User getUserBasedonUserToken(final String userToken) throws BizLogicException
	{
		User validUser = null;
		final String getActiveUser = "from " + User.class.getName() + " user where " + "user.activityStatus= "
		+ "'" + Status.ACTIVITY_STATUS_ACTIVE.toString() + "' and user.forgotPasswordToken =" + "'" + userToken
		+ "'";
		final DefaultBizLogic bizlogic = new DefaultBizLogic();
		final List<User> users = bizlogic.executeQuery(getActiveUser);
		if (users != null && !users.isEmpty())
		{
			validUser = users.get(0);
		}
		return validUser;
		
	}

	public boolean isPasswordTokenValid(String resetPasswordToken) throws ApplicationException
	{
		boolean isValid=false;
		String query="select count(*) from catissue_user where forgot_password_token='"+resetPasswordToken+"'";
		
		List resultList=AppUtility.executeSQLQuery(query);
		if(resultList!=null && resultList.size()>0)
		{
			ArrayList arr= (ArrayList) resultList.get(0);
			if(Integer.valueOf((String) arr.get(0))>0)
			{
				isValid=true;
			}
		}
		return isValid;
	}

	public Set<Long> getRelatedCPIds(Long userId, boolean isCheckForCPBasedView) throws BizLogicException {
		DAO dao = null;
		Collection<CollectionProtocol> userCpCollection = new HashSet<CollectionProtocol>();
		Collection<CollectionProtocol> userColl;
		Set<Long> cpIds = new HashSet<Long>();

		try
		{
			dao = getHibernateDao(getAppName(), null);
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
					final PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(user.getLoginName());
									
					for (final CollectionProtocol collectionProtocol : userCpCollection)
					{
						final String privilegeName = edu.wustl.common.util.global.Variables.privilegeDetailsMap
						.get(Constants.EDIT_PROFILE_PRIVILEGE);
						
						boolean isPhiView = false;
						boolean hasViewPrivilege = false;
						if (privilegeCache.hasPrivilege(collectionProtocol.getObjectId(),
								edu.wustl.common.util.global.Variables.privilegeDetailsMap
										.get(Constants.EDIT_PROFILE_PRIVILEGE)))
						{
							isPhiView = true;
							hasViewPrivilege = true;
						}
						else if(privilegeCache.hasPrivilege(collectionProtocol.getObjectId(),Permissions.SPECIMEN_PROCESSING))
						{
							isPhiView = false;
							hasViewPrivilege = true;
						}
						
					 // My changes---- Added chek for SPECIMEN_PROCESSING privilege for cp 			
						if (hasViewPrivilege)
						{
							cpIds.add( collectionProtocol.getId());
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
			UserBizLogic.LOGGER.error(e.getMessage(), e);
		}
		catch (final SMException e)
		{
			UserBizLogic.LOGGER.error(e.getMessage(), e);
		}
		finally
		{
			closeDAOSession(dao);
		}

		return cpIds;
	}
	
	public List<UserNameIdDTO> getUserList(DAO dao) throws DAOException
	{
		List<UserNameIdDTO> userNvblist = new ArrayList<UserNameIdDTO>();
		
		String hql = "select user.id,user.firstName,user.lastName from edu.wustl.catissuecore.domain.User as user where" +
				" user.activityStatus='Active'"; 
		List list = dao.executeQuery(hql);
		
		for(Object user:list)
		{
			UserNameIdDTO dto = new UserNameIdDTO();
			Object[] userNV = (Object[])user;
			dto.setUserId((Long)userNV[0]);
			dto.setFirstName((String)userNV[1]);
			dto.setLastName((String)userNV[2]);
			userNvblist.add(dto);
		}
		return userNvblist;
		
	}
	public User getUserNameById(Long id,HibernateDAO dao) throws DAOException
	{
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.LONG, id));
		final List userNames = dao.executeNamedQuery("getUserName", substParams);
		Object[] userNameValues = (Object[]) userNames.get(0);
		User user = new User();
		user.setLastName(userNameValues[0].toString());
		user.setFirstName(userNameValues[1].toString());
		return user;
	}
}