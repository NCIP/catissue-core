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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.hibernate.Session;

import edu.common.dynamicextensions.util.global.Variables;
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
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SecurityDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exceptionformatter.DefaultExceptionFormatter;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.PrivilegeUtility;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.PasswordEncryptionException;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;

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
		User user = null;
		Map<String,SiteUserRolePrivilegeBean> userRowIdMap =new HashMap<String, SiteUserRolePrivilegeBean>();

		if(obj instanceof UserDTO)
		{
			user = ((UserDTO)obj).getUser();
			userRowIdMap = ((UserDTO)obj).getUserRowIdBeanMap();
		}
		else
		{
			user = (User) obj;            
		}
      	// Method to populate rowIdMap in case, Add Privilege button is not clicked
        userRowIdMap = getUserRowIdMap(user, userRowIdMap);
		gov.nih.nci.security.authorization.domainobjects.User csmUser = new gov.nih.nci.security.authorization.domainobjects.User();

		try
		{
			Object object = dao.retrieve(Department.class.getName(), user.getDepartment().getId());
			Department department = null;
			if (object != null)
			{
				department = (Department) object;
			}

			object = dao.retrieve(Institution.class.getName(), user.getInstitution().getId());
			Institution institution = null;
			if (object != null)
			{
				institution = (Institution) object;
			}

			object = dao.retrieve(CancerResearchGroup.class.getName(), user.getCancerResearchGroup().getId());
			CancerResearchGroup cancerResearchGroup = null;
			if (object != null)
			{
				cancerResearchGroup = (CancerResearchGroup) object;
			}

			user.setDepartment(department);
			user.setInstitution(institution);
			user.setCancerResearchGroup(cancerResearchGroup);
			String generatedPassword = PasswordManager.generatePassword();

			// If the page is of signup user don't create the csm user.
			if (user.getPageOf().equals(Constants.PAGEOF_SIGNUP) == false)
			{
				csmUser.setLoginName(user.getLoginName());
				csmUser.setLastName(user.getLastName());
				csmUser.setFirstName(user.getFirstName());
				csmUser.setEmailId(user.getEmailAddress());
				csmUser.setStartDate(user.getStartDate());
				csmUser.setPassword(generatedPassword);

				SecurityManager.getInstance(UserBizLogic.class).createUser(csmUser);

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
                  
					SecurityManager.getInstance(UserBizLogic.class).assignRoleToUser(csmUser.getUserId().toString(), role);
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
				password.setPassword(PasswordManager.encrypt(generatedPassword));
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
			if(userRowIdMap != null && !userRowIdMap.isEmpty())
			{
				updateUserDetails(user,userRowIdMap);
			}
			dao.insert(user, sessionDataBean, true, false);

			Set protectionObjects = new HashSet();
			protectionObjects.add(user);

			EmailHandler emailHandler = new EmailHandler();
			// Send the user registration email to user and the administrator.
			if (Constants.PAGEOF_SIGNUP.equals(user.getPageOf()))
			{
				//SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, protectionObjects, null);

				emailHandler.sendUserSignUpEmail(user);
			}
			else
			// Send the user creation email to user and the administrator.
			{
//				SecurityManager.getInstance(this.getClass()).insertAuthorizationData(getAuthorizationData(user), protectionObjects, null);
				
				PrivilegeManager privilegeManager = PrivilegeManager.getInstance();

				privilegeManager.insertAuthorizationData(getAuthorizationData(user, userRowIdMap), 
						protectionObjects, null, user.getObjectId());

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
		catch (PasswordEncryptionException e)
		{
			Logger.out.debug(e.getMessage(), e);
			deleteCSMUser(csmUser);
			throw new DAOException(e.getMessage(), e);
		}
	}

	public Map<String, SiteUserRolePrivilegeBean> getUserRowIdMap(User user, Map<String, SiteUserRolePrivilegeBean> userRowIdMap) throws DAOException 
	{
		if (user.getRoleId() != null && !user.getRoleId().equalsIgnoreCase("-1") && !user.getRoleId().equalsIgnoreCase("0"))
        {
            if (userRowIdMap == null || userRowIdMap.isEmpty() && user.getSiteCollection() != null && !user.getSiteCollection().isEmpty())
            {
                List<NameValueBean> list = new AssignPrivilegePageBizLogic().getActionsList(user.getRoleId());
                NameValueBean roleBean = new NameValueBean();
                try
                {
                    Vector<Role> roleList = SecurityManager.getInstance(this.getClass()).getRoles();
                    roleBean.setValue(user.getRoleId());
                    for (Role role : roleList)
                    {
                        if (role.getId().toString().equalsIgnoreCase(user.getRoleId()))
                        {
                            roleBean.setName(role.getName());
                            break;
                        }
                    }
                }
                catch (SMException e)
                {
                	throw new DAOException(ApplicationProperties.getValue("user.roleNotFound"));
                }
                int i = 0;
                userRowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>(); 
                for (Site site : user.getSiteCollection())
                {
                    List <Site> siteList = new ArrayList<Site>();
                    siteList.add(site);
                    SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = new SiteUserRolePrivilegeBean();
                    siteUserRolePrivilegeBean.setAllCPChecked(true);
                    siteUserRolePrivilegeBean.setPrivileges(list);
                    siteUserRolePrivilegeBean.setRole(roleBean);
                    siteUserRolePrivilegeBean.setSiteList(siteList);
                    userRowIdMap.put(new Integer(i).toString(),siteUserRolePrivilegeBean);
                    i++;
                }
            }
        }
		return userRowIdMap;
	}

	/**
	 * Deletes the csm user from the csm user table.
	 * @param csmUser The csm user to be deleted.
	 * @throws DAOException
	 */
	public void deleteCSMUser(gov.nih.nci.security.authorization.domainobjects.User csmUser) throws DAOException
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
	private Vector getAuthorizationData(AbstractDomainObject obj, Map<String,SiteUserRolePrivilegeBean> userRowIdMap) throws SMException
	{
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
		
		if(userRowIdMap !=null)
		{
			insertCPSitePrivileges(aUser, authorizationData, userRowIdMap);
		}
		
		return authorizationData;
	}

	public void insertCPSitePrivileges(User user1, Vector authorizationData, Map<String, SiteUserRolePrivilegeBean> userRowIdMap)
	{
		if(userRowIdMap == null || userRowIdMap.isEmpty())
		{
		 return;
		}
		
        
		Map<String, SiteUserRolePrivilegeBean> cpPrivilegeMap = new HashMap<String, SiteUserRolePrivilegeBean>();
		Map<String, SiteUserRolePrivilegeBean> sitePrivilegeMap = new HashMap<String, SiteUserRolePrivilegeBean>();
		
		Object[] mapKeys = userRowIdMap.keySet().toArray();
		for(Object mapKey : mapKeys)
		{
			String key = mapKey.toString();
			SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = userRowIdMap.get(key);
            siteUserRolePrivilegeBean.setUser(user1);
			if(siteUserRolePrivilegeBean.isAllCPChecked())
			{
				Map<String, SiteUserRolePrivilegeBean> map = Utility.splitBeanData(siteUserRolePrivilegeBean);
				SiteUserRolePrivilegeBean bean1 = map.get("SITE");
				SiteUserRolePrivilegeBean bean2 = map.get("CP");
				userRowIdMap.remove(key);
				if(bean1.getPrivileges().isEmpty())
				{
					bean1.setPrivileges(bean2.getPrivileges());
				}
				userRowIdMap.put(key, bean1);
				if(bean2.getPrivileges().isEmpty())
				{
					bean2.setPrivileges(bean1.getPrivileges());
				}
				userRowIdMap.put(key+"All_CurrentnFuture_CPs", bean2);
			}
		}
		
		distributeMapData(userRowIdMap, cpPrivilegeMap, sitePrivilegeMap);
		
		/* For SITE Privileges Purpose */
		insertSitePrivileges(user1, authorizationData, sitePrivilegeMap);
		
		/* For CP Privileges Purpose */
		insertCPPrivileges(user1, authorizationData, cpPrivilegeMap);
				
		/* Common for both */	
		//updateUserDetails(user1, userRowIdMap);
	}
	
	
	public void updateUserDetails(User user1, Map<String, SiteUserRolePrivilegeBean> userRowIdMap) 
	{
		Set<Site> siteCollection = new HashSet<Site>();
		Set<CollectionProtocol> cpCollection = new HashSet<CollectionProtocol>();
        Set<CollectionProtocol> removedCpCollection = new HashSet<CollectionProtocol>();
		for (Iterator<String> mapItr = userRowIdMap.keySet().iterator(); mapItr.hasNext(); )
		{
			String key = mapItr.next();
			SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = userRowIdMap.get(key);
			
			CollectionProtocol cp = siteUserRolePrivilegeBean.getCollectionProtocol();
			if(cp != null && !siteUserRolePrivilegeBean.isRowDeleted())
			{
				cpCollection.add(cp);
			}
            else if (cp!= null && siteUserRolePrivilegeBean.isRowDeleted())
            {
                removedCpCollection.add(cp);
            }
			
			List<Site> siteList = null;
			
			if(!siteUserRolePrivilegeBean.isRowDeleted())
			{
				siteList = siteUserRolePrivilegeBean.getSiteList();
				
				if(siteList != null && !siteList.isEmpty())
				{
	                for(Site site : siteList) 
	                {
	                    boolean isPresent = false;
	                    for (Site site1 : siteCollection)
	                    {
	                        if (site1.getId().equals(site.getId()))
	                        {
	                            isPresent = true;
	                        }
	                    }
	                    if(!isPresent)
	                    {
	                        siteCollection.add(site);
	                    }
	                }
				}
			} 
		}
		
		user1.getSiteCollection().clear();
		user1.getSiteCollection().addAll(siteCollection);
		updateCollectionProtocolCollection(user1,cpCollection,removedCpCollection);
	}

	
	private void updateCollectionProtocolCollection(User user1, Set<CollectionProtocol> cpCollection, Set<CollectionProtocol> removedCpCollection)
    {
        Collection<CollectionProtocol> tempCollection = new HashSet<CollectionProtocol>();
        tempCollection.addAll(user1.getAssignedProtocolCollection());
        for (CollectionProtocol newCp : cpCollection)
        {
            boolean isPresent = false;
            for (CollectionProtocol cp: user1.getAssignedProtocolCollection())
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
        for (CollectionProtocol removedCp : removedCpCollection)
        {
            boolean isPresent = false;
            for (CollectionProtocol cp: cpCollection)
            {
                if (removedCp.getId().equals(cp.getId()))
                {
                    isPresent = true;
                }
            }
            if (!isPresent)
            {
                for (CollectionProtocol existingCp: tempCollection)
                {
                    if (existingCp.getId().equals(removedCp.getId()))
                    {
                        user1.getAssignedProtocolCollection().remove(existingCp);
                    }
                }
            }
        }
    }

    private void insertSitePrivileges(User user1, Vector authorizationData, Map<String, SiteUserRolePrivilegeBean> sitePrivilegeMap) 
	{
		String roleName = "";
		
		for (Iterator<String> mapItr = sitePrivilegeMap.keySet().iterator(); mapItr.hasNext(); )
		{
			try 
			{
				String key = mapItr.next();
				SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = sitePrivilegeMap.get(key);
				
				if(siteUserRolePrivilegeBean.isRowDeleted())
				{
					Utility.processDeletedPrivileges(siteUserRolePrivilegeBean);
				}
				else if(siteUserRolePrivilegeBean.isRowEdited())
				{
					Site site = siteUserRolePrivilegeBean.getSiteList().get(0);
					String defaultRole = siteUserRolePrivilegeBean.getRole().getValue();
					if (defaultRole != null && (defaultRole.equalsIgnoreCase("0") || defaultRole.equalsIgnoreCase("-1")))
					{
					    roleName = Constants.getSiteRoleName(site.getId(), user1.getCsmUserId(), defaultRole);
	                } else
	                {
	                    roleName = siteUserRolePrivilegeBean.getRole().getName();
	                }
					Set<String> privileges = new HashSet<String>();
					List<NameValueBean> privilegeList = siteUserRolePrivilegeBean.getPrivileges();
					
					for(NameValueBean privilege : privilegeList)
					{
						privileges.add(privilege.getValue());
					}
					
					Utility.processRole(roleName);
					
					PrivilegeManager.getInstance().createRole(roleName,
								privileges);
					
					String userId = String.valueOf(user1.getCsmUserId());
					
					gov.nih.nci.security.authorization.domainobjects.User csmUser = null;
					csmUser = getUserByID(userId);
					HashSet<gov.nih.nci.security.authorization.domainobjects.User> group = new HashSet<gov.nih.nci.security.authorization.domainobjects.User>();
					group.add(csmUser);
					
					String protectionGroupName = new String(Constants
							.getSitePGName(site.getId()));
					
					createProtectionGroup(protectionGroupName, site, false);
					
					SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
					userGroupRoleProtectionGroupBean.setUser("");
					userGroupRoleProtectionGroupBean.setRoleName(roleName);

					userGroupRoleProtectionGroupBean.setGroupName(Constants.getSiteUserGroupName(site.getId(), user1.getCsmUserId()));
					userGroupRoleProtectionGroupBean.setProtectionGroupName(protectionGroupName);
					userGroupRoleProtectionGroupBean.setGroup(group);
					authorizationData.add(userGroupRoleProtectionGroupBean);
				}
			} 
			
			catch (CSException e) 
			{
				Logger.out.error(e.getMessage(), e);
			} 
			catch (SMException e) 
			{
				Logger.out.error(e.getMessage(), e);
			}
		}
	}
	
	
	private void insertCPPrivileges(User user1, Vector authorizationData, Map<String, SiteUserRolePrivilegeBean> cpPrivilegeMap) 
	{
		String roleName = "";
		String protectionGroupName = "";
		
		for (Iterator<String> mapItr = cpPrivilegeMap.keySet().iterator(); mapItr.hasNext(); )
		{
			try 
			{
				SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
				String key = mapItr.next();
				SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = cpPrivilegeMap.get(key);
				
				if(siteUserRolePrivilegeBean.isRowDeleted())
				{
					Utility.processDeletedPrivileges(siteUserRolePrivilegeBean);
				}
				else if(siteUserRolePrivilegeBean.isRowEdited())
				{
					// Case for 'All Current & Future CP's selected
					if(siteUserRolePrivilegeBean.isAllCPChecked() == true)
					{
						String defaultRole = siteUserRolePrivilegeBean.getRole().getValue();
						Site site = siteUserRolePrivilegeBean.getSiteList().get(0);
						
						if (defaultRole != null && (defaultRole.equalsIgnoreCase("-1") || defaultRole.equalsIgnoreCase("0") || defaultRole.equalsIgnoreCase("7")))
						{
							roleName = Constants.getCurrentAndFutureRoleName(site.getId(), user1.getCsmUserId(), defaultRole);
						} else
						{
							roleName = siteUserRolePrivilegeBean.getRole().getName();
						}
						
						protectionGroupName = Constants.getCurrentAndFuturePGAndPEName(site.getId());	
						createProtectionGroup(protectionGroupName, site, true);
			
						userGroupRoleProtectionGroupBean.setGroupName(Constants.getSiteUserGroupName(site.getId(), user1.getCsmUserId()));			
					}
					else
					{
						CollectionProtocol cp = siteUserRolePrivilegeBean.getCollectionProtocol();
						String defaultRole = siteUserRolePrivilegeBean.getRole().getValue();
						
//						if (defaultRole != null && (defaultRole.equalsIgnoreCase("0") || defaultRole.equalsIgnoreCase("-1") || defaultRole.equalsIgnoreCase("7")))
//						{
							roleName = Constants.getCPRoleName(cp.getId(), user1.getCsmUserId(), defaultRole);
//						}
						/*else
						{
							roleName = siteUserRolePrivilegeBean.getRole().getName();
						}*/

						protectionGroupName = Constants.getCollectionProtocolPGName(cp.getId());
						createProtectionGroup(protectionGroupName, cp, false);
					
						userGroupRoleProtectionGroupBean.setGroupName(Constants.getCPUserGroupName(cp.getId(), user1.getCsmUserId()));
					}
					
					Set<String> privileges = new HashSet<String>();
					List<NameValueBean> privilegeList = siteUserRolePrivilegeBean.getPrivileges();
					
					for(NameValueBean privilege : privilegeList)
					{
						privileges.add(privilege.getValue());
					}
					
					Utility.processRole(roleName);
					
					PrivilegeManager.getInstance().createRole(roleName,
								privileges);
					
					String userId = String.valueOf(user1.getCsmUserId());
					
					gov.nih.nci.security.authorization.domainobjects.User csmUser = getUserByID(userId);
					HashSet<gov.nih.nci.security.authorization.domainobjects.User> group = new HashSet<gov.nih.nci.security.authorization.domainobjects.User>();
					group.add(csmUser);
					
					userGroupRoleProtectionGroupBean.setUser("");
					userGroupRoleProtectionGroupBean.setRoleName(roleName);
					userGroupRoleProtectionGroupBean.setProtectionGroupName(protectionGroupName);
					userGroupRoleProtectionGroupBean.setGroup(group);
					authorizationData.add(userGroupRoleProtectionGroupBean);
				}
			} 
			catch (CSException e) 
			{
				Logger.out.error(e.getMessage(), e);
			}
			catch (SMException e) 
			{
				Logger.out.error(e.getMessage(), e);
			}
		}	
	}

	
	private void createProtectionGroup(String protectionGroupName, AbstractDomainObject obj, boolean isAllCPChecked) 
	{
		ProtectionElement pe = new ProtectionElement();
		Set<ProtectionElement> peSet = new HashSet<ProtectionElement>();
		List<ProtectionElement> peList = new ArrayList<ProtectionElement>();
		PrivilegeUtility privilegeUtility = new PrivilegeUtility();
		
		try 
		{
			if(isAllCPChecked)
			{
				pe.setObjectId(Constants.getCurrentAndFuturePGAndPEName(obj.getId()));
				pe.setProtectionElementName(Constants.getCurrentAndFuturePGAndPEName(obj.getId()));
				pe.setProtectionElementDescription("For All Current & Future CP's for Site with Id "+obj.getId().toString());
				pe.setApplication(privilegeUtility.getApplication(SecurityManager.APPLICATION_CONTEXT_NAME));
				ProtectionElementSearchCriteria searchCriteria = new ProtectionElementSearchCriteria(pe);
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
				ProtectionElementSearchCriteria searchCriteria = new ProtectionElementSearchCriteria(pe);
				
				peList = privilegeUtility.getUserProvisioningManager().getObjects(searchCriteria);
			}
			ProtectionGroup pg = new ProtectionGroup();
			pg.setProtectionGroupName(protectionGroupName);
			peSet.addAll(peList);
			pg.setProtectionElements(peSet);
			new PrivilegeUtility().getUserProvisioningManager().createProtectionGroup(pg);	  
		} 
		catch (Exception e) 
		{
			Logger.out.debug(e.getMessage(), e);
		}
			
	}
	
	private void distributeMapData(Map<String, SiteUserRolePrivilegeBean> userRowIdMap, Map<String, SiteUserRolePrivilegeBean> cpPrivilegeMap, 
																		Map<String, SiteUserRolePrivilegeBean> sitePrivilegeMap)
	{
		for (Iterator<String> mapItr = userRowIdMap.keySet().iterator(); mapItr.hasNext(); )
		{
			String key = mapItr.next();
			SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = userRowIdMap.get(key);
			
			if(siteUserRolePrivilegeBean.getCollectionProtocol() == null && siteUserRolePrivilegeBean.isAllCPChecked() == false)
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
	 * @param userId
	 * @return
	 * @throws SMException
	 */
	private gov.nih.nci.security.authorization.domainobjects.User getUserByID(String userId)
			throws SMException
	{
		gov.nih.nci.security.authorization.domainobjects.User user = SecurityManager.getInstance(
				this.getClass()).getUserById(userId);
		return user;
	}
	
	
	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		User user = null;
		Map<String,SiteUserRolePrivilegeBean> userRowIdMap =new HashMap<String, SiteUserRolePrivilegeBean>();

		if(obj instanceof UserDTO)
		{
			user = ((UserDTO)obj).getUser();
			userRowIdMap = ((UserDTO)obj).getUserRowIdBeanMap();
		}
		else
		{
			user = (User) obj;
		}
		// Method to populate rowIdMap in case, Add Privilege button is not clicked
        userRowIdMap = getUserRowIdMap(user, userRowIdMap);
		
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

			//Bug:7979
			if(Constants.DUMMY_PASSWORD.equals(user.getNewPassword()))
			{
				user.setNewPassword(csmUser.getPassword());
			}
			
			String oldPassword = user.getOldPassword();
			// If the page is of change password, 
			// update the password of the user in csm and catissue tables. 
			if (Constants.PAGEOF_CHANGE_PASSWORD.equals(user.getPageOf()))
			{
				if (!oldPassword.equals(csmUser.getPassword()))
				{
					throw new DAOException(ApplicationProperties.getValue("errors.oldPassword.wrong"));
				}

				//Added for Password validation by Supriya Dankh.
				Validator validator = new Validator();
				if (!validator.isEmpty(user.getNewPassword()) && !validator.isEmpty(oldPassword))
				{
					int result = validatePassword(oldUser, user.getNewPassword(), oldPassword);

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
				csmUser.setPassword(user.getNewPassword());

				// Set values in password domain object and adds changed password in Password Collection
				Password password = new Password(PasswordManager.encrypt(user.getNewPassword()), user);
				user.getPasswordCollection().add(password);
							
			}
			
			//Bug-1516: Jitendra Administartor should be able to edit the password 
			else if(Constants.PAGEOF_USER_ADMIN.equals(user.getPageOf()) && !user.getNewPassword().equals(csmUser.getPassword()))
			{				
				Validator validator = new Validator();
				if (!validator.isEmpty(user.getNewPassword()))
				{
					int result = validatePassword(oldUser, user.getNewPassword(), oldPassword);

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
				csmUser.setPassword(user.getNewPassword());
				// Set values in password domain object and adds changed password in Password Collection
				Password password = new Password(PasswordManager.encrypt(user.getNewPassword()), user);
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
	                  
						SecurityManager.getInstance(UserBizLogic.class).assignRoleToUser(csmUser.getUserId().toString(), role);
					}
				}
				
				// Set protectionObjects = new HashSet();
				// protectionObjects.add(user);
			
		  		if(userRowIdMap != null && !userRowIdMap.isEmpty())
				{
					updateUserDetails(user,userRowIdMap);
				}
				Vector authorizationData = new Vector();
				PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
				
				insertCPSitePrivileges(user, authorizationData, userRowIdMap);
				privilegeManager.insertAuthorizationData(authorizationData, null, null, user.getObjectId());
				
				// privilegeManager.insertAuthorizationData(getAuthorizationData(user, userRowIdMap), 
				//		protectionObjects, null, user.getObjectId());
				
				dao.update(user.getAddress(), sessionDataBean, true, false, false);

				// Audit of user address.
				dao.audit(user.getAddress(), oldUser.getAddress(), sessionDataBean, true);
			}
			
			if (Constants.PAGEOF_CHANGE_PASSWORD.equals(user.getPageOf())) 
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

			/* pratha commented for bug# 7304 
			if (Constants.ACTIVITY_STATUS_ACTIVE.equals(user.getActivityStatus()))
			{
				Set protectionObjects = new HashSet();
				protectionObjects.add(user);
				try{
					SecurityManager.getInstance(this.getClass()).insertAuthorizationData(getAuthorizationData(user), protectionObjects, null);
				}
				catch (SMException e)
				{
						//digest exception
				}
				
			}  */
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}
		catch (PasswordEncryptionException e)
		{
			throw new DAOException(e.getMessage(), e);
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
		//Get only the fields required 
		String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER,Constants.LASTNAME,Constants.FIRSTNAME};
		String[] whereColumnName;
		String[] whereColumnCondition;
		Object[] whereColumnValue;
		String joinCondition;
		if (operation != null && operation.equalsIgnoreCase(Constants.ADD))
		{
			String tmpArray1[] = {Constants.ACTIVITY_STATUS};
			String tmpArray2[] = {Constants.EQUALS};
			String tmpArray3[] = {Constants.ACTIVITY_STATUS_ACTIVE};
			whereColumnName = tmpArray1;
			whereColumnCondition = tmpArray2;
			whereColumnValue = tmpArray3;
			joinCondition = null;
		}
		else
		{
			String tmpArray1[] = {Constants.ACTIVITY_STATUS, Constants.ACTIVITY_STATUS};
			String tmpArray2[] = {Constants.EQUALS,Constants.EQUALS};
			String tmpArray3[] = {Constants.ACTIVITY_STATUS_ACTIVE, Constants.ACTIVITY_STATUS_CLOSED};
			whereColumnName = tmpArray1;
			whereColumnCondition = tmpArray2;
			whereColumnValue = tmpArray3;
			joinCondition = Constants.OR_JOIN_CONDITION;
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
				//Changes made to optimize the query to get only required fields data
				Object[] userData = (Object[])users.get(i);
				NameValueBean nameValueBean = new NameValueBean();
				nameValueBean.setName(userData[1]+", "+userData[2]);
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
		User user = null;
		if (obj instanceof UserDTO)
		{
			user = ((UserDTO)obj).getUser();
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
//				try
//				{
//					if (!Validator.isEnumeratedValue(getRoles(), user.getRoleId()))
//					{
//						throw new DAOException(ApplicationProperties.getValue("user.role.errMsg"));
//					}
//				}
//				catch (SMException e)
//				{
//					throw handleSMException(e);
//				}

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
			/**
			 * Two more parameter 'dao' and 'operation' is added by Vijay Pande to use it in isUniqueEmailAddress method
			 */
			apiValidate(user, dao,operation);
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
	 * @throws DAOException
	 */
	
	private boolean apiValidate(User user, DAO dao, String operation)
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
			/**
			 * Name : Vijay_Pande
			 * Reviewer : Sntosh_Chandak
			 * Bug ID: 4185_2 
			 * Patch ID: 1-2
			 * See also: 1
			 * Description: Wrong error meassage was dispayed while adding user with existing email address in use.
			 * Following method is provided to verify whether the email address is already present in the system or not. 
			 */
			if(operation.equals(Constants.ADD) && !(isUniqueEmailAddress(user.getEmailAddress(),dao)))
			{
				String arguments[] = null;
				arguments = new String[]{"User", ApplicationProperties.getValue("user.emailAddress")};
				String errMsg = new DefaultExceptionFormatter().getErrorMessage("Err.ConstraintViolation", arguments);
				Logger.out.debug("Unique Constraint Violated: " + errMsg);
				throw new DAOException(errMsg);
			}
			/** -- patch ends here -- */
		}
		if (validator.isEmpty(user.getLastName()))
		{
			message = ApplicationProperties.getValue("user.lastName");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}
		else if(validator.isXssVulnerable(user.getLastName()))
		{
			message = ApplicationProperties.getValue("user.lastName");
			throw new DAOException(ApplicationProperties.getValue("errors.xss.invalid",message));	
		}

		if (validator.isEmpty(user.getFirstName()))
		{
			message = ApplicationProperties.getValue("user.firstName");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
		}
		else if(validator.isXssVulnerable(user.getFirstName()))
		{
			message = ApplicationProperties.getValue("user.firstName");
			throw new DAOException(ApplicationProperties.getValue("errors.xss.invalid",message));	
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
		
//		if (user.getRoleId() != null)
//		{
//			if (!validator.isValidOption(user.getRoleId()) || validator.isEmpty(String.valueOf(user.getRoleId())))
//			{
//				message = ApplicationProperties.getValue("user.role");
//				throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));	
//			}
//		}
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
	 * @throws PasswordEncryptionException 
	 */

	private int validatePassword(User oldUser, String newPassword, String oldPassword) throws PasswordEncryptionException
	{
		List oldPwdList = new ArrayList(oldUser.getPasswordCollection());
		Collections.sort(oldPwdList);
		if (oldPwdList != null && !oldPwdList.isEmpty())
		{
			//Check new password is equal to last n password if value
			String encryptedPassword = PasswordManager.encrypt(newPassword);
			if (checkPwdNotSameAsLastN(encryptedPassword, oldPwdList))
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
			if (newPassword.equals(pasword.getPassword()))
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
		if (dayDiff < dayDiffConstant)
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
				List<String> parameters = new ArrayList<String>();
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
				parameters = new ArrayList<String>();
				Integer daysCount = Integer.parseInt(XMLPropertyHandler.getValue("daysCount"));
				parameters.add(daysCount.toString());
				if(daysCount.intValue() == 1)
					errMsg = ApplicationProperties.getValue("errors.changePassword.sameDay");
				else
					errMsg = ApplicationProperties.getValue("errors.changePassword.afterSomeDays",parameters);
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
	 * @return isUnique boolean value to indicate presence of similar email address
	 * @throws DAOException database exception
	 */
	private boolean isUniqueEmailAddress(String emailAddress, DAO dao) throws DAOException
	{
		boolean isUnique=true;
		
		String sourceObjectName=User.class.getName();
		String[] selectColumnName=new String[] {"id"};
		String[] whereColumnName = new String[]{"emailAddress"};
		String[] whereColumnCondition = new String[]{"="};
		Object[] whereColumnValue = new String[]{emailAddress};
		String joinCondition = null;
		
		List userList = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
		
		if (userList.size() > 0)
		{
			isUnique=false;
		}
		return isUnique;
	}
	
	/**
	 * Set Role to user object before populating actionForm out of it
	 * @param domainObj object of AbstractDomainObject
	 * @param uiForm object of the class which implements IValueObject
	 * @throws BizLogicException 
	 */
	protected void prePopulateUIBean(AbstractDomainObject domainObj, IValueObject uiForm) throws BizLogicException
	{	
		Logger.out.info("Inside prePopulateUIBean method of UserBizLogic...");
		 
    	User user = (User)domainObj;
    	Role role=null;
    	if (user.getCsmUserId() != null)
		{
			try 
			{
				//Get the role of the user.
				role = SecurityManager.getInstance(UserBizLogic.class).getUserRole(user.getCsmUserId().longValue());
				if (role != null)
				{
					user.setRoleId(role.getId().toString());
				}
				//	Logger.out.debug("In USer biz logic.............role........id......." + role.getId().toString());
			} 
			catch (SMException e) 
			{
				Logger.out.error("SMException in prePopulateUIBean method of UserBizLogic..."+e);
				//throw new BizLogicException(e.getMessage());
			}
		}  	     
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
	
	
	/**
	 * To Sort CP's in CP based view according to the 
	 * Privilges of User on CP
	 * Done for MSR functionality change
	 * @author ravindra_jain 
	 */

	public Set<Long> getRelatedCPIds(Long userId, boolean isCheckForCPBasedView)
	{
		Session session = null;
		Collection<CollectionProtocol> userCpCollection = new HashSet<CollectionProtocol>();
		Collection<CollectionProtocol> userColl;
		Set<Long> cpIds = new HashSet<Long>();

		try 
		{
			session = DBUtil.getCleanSession();
			
			User user = (User) session.load(User.class.getName(), userId);
			userColl = user.getCollectionProtocolCollection();
			userCpCollection = user.getAssignedProtocolCollection();

			if (user.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
			{
				cpIds = null;
			}
			else
			{
				if(isCheckForCPBasedView)
				{
					PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
					PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(user
							.getLoginName());
	
					for (CollectionProtocol collectionProtocol : userCpCollection)
					{
						if (privilegeCache.hasPrivilege(collectionProtocol.getObjectId(),
								Variables.privilegeDetailsMap.get(Constants.EDIT_PROFILE_PRIVILEGE))
								|| collectionProtocol.getPrincipalInvestigator().getLoginName().equals(
										user.getLoginName()))
						{
							cpIds.add(collectionProtocol.getId());
						}
					}
				}
				else
				{
					for (CollectionProtocol collectionProtocol : userCpCollection)
					{
						cpIds.add(collectionProtocol.getId());
					}
				}

				for (CollectionProtocol cp : userColl)
				{
					cpIds.add(cp.getId());
				}
			}
		} 
		catch (BizLogicException e) 
		{
			Logger.out.debug(e.getMessage(), e);
		}
		finally
		{
			session.close();
		}

		return cpIds;
	}

	public Set<Long> getRelatedSiteIds(Long userId) 
	{
		Session session = null;
	
		HashSet<Long> idSet = null;
		
		try 
		{
			session = DBUtil.getCleanSession();

			User user = (User) session.load(User.class.getName(), userId);
			if (!user.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
			{
				Collection<Site> siteCollection = user.getSiteCollection();
				idSet = new HashSet<Long>();

				for (Site site : siteCollection)
				{
					idSet.add(site.getId());
				}
			}
		}
		catch (BizLogicException e1) 
		{
			Logger.out.debug(e1.getMessage(), e1);
		}
		finally
		{
			session.close();
		}
		
		return idSet;
	} 
		
		
		/**
		 * Custom method for Add User Case
		 * @param dao
		 * @param domainObject
		 * @param sessionDataBean
		 * @return
		 */
		public String getObjectId(AbstractDAO dao, Object domainObject) 
		{	
			User user = null;
			UserDTO userDTO = null;
			Map<String,SiteUserRolePrivilegeBean> userRowIdMap =new HashMap<String, SiteUserRolePrivilegeBean>();
			Collection<Site> siteCollection = new ArrayList<Site>();
			
			if(domainObject instanceof UserDTO)
			{
				userDTO = (UserDTO) domainObject;
				user = userDTO.getUser();
				userRowIdMap = userDTO.getUserRowIdBeanMap();
			}
			else
			{
				user = (User) domainObject;
			}
			
			if(user.getRoleId().equals(Constants.SUPER_ADMIN_USER))
			{
				return Constants.cannotCreateSuperAdmin;
			}
			if(userRowIdMap==null)
			{
				if(user.getRoleId()==null || user.getRoleId().equals("") || user.getSiteCollection()==null || user.getSiteCollection().isEmpty())
				{
					return Constants.siteIsRequired;
				}
				try 
				{
					userRowIdMap = getUserRowIdMap(user, userRowIdMap);
				} 
				catch (DAOException e) 
				{
					e.printStackTrace();
				}
			}
			
			if(userRowIdMap != null)
			{
				Object[] mapKeys = userRowIdMap.keySet().toArray();
				for(Object mapKey : mapKeys)
				{
					String key = mapKey.toString();
					SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = userRowIdMap.get(key);
					
					siteCollection.add(siteUserRolePrivilegeBean.getSiteList().get(0));
				}
			}
			 
			// Collection<Site> siteCollection = user.getSiteCollection();
			
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
		
		public boolean checkUser(Object domainObject, SessionDataBean sessionDataBean) throws DAOException 
		{
			User user = null;
			UserDTO userDTO = null;
			Map<String, SiteUserRolePrivilegeBean> userRowIdMap= new HashMap<String, SiteUserRolePrivilegeBean>();
			
			if(sessionDataBean != null && sessionDataBean.isAdmin())
			{
				return true;
			}
			if(domainObject instanceof User)
			{
				user = (User) domainObject;
			}
			if(domainObject instanceof UserDTO)
			{
				userDTO = (UserDTO) domainObject;
				user = userDTO.getUser();
				if(user.getRoleId().equals(Constants.SUPER_ADMIN_USER))
				{
					throw new DAOException(ApplicationProperties.getValue("user.cannotCreateSuperAdmin"));
				}
				userRowIdMap = userDTO.getUserRowIdBeanMap();
				if(userRowIdMap != null)
				{
					Object[] mapKeys = userRowIdMap.keySet().toArray();
					for(Object mapKey : mapKeys)
					{
						String key = mapKey.toString();
						SiteUserRolePrivilegeBean bean = userRowIdMap.get(key);
						
						if(bean.getSiteList()==null || bean.getSiteList().isEmpty())
						{
							throw new DAOException(ApplicationProperties.getValue("user.cannotCreateScientist"));
						}
					}
				}
				else
				{
					if(user.getRoleId().equals(Constants.NON_ADMIN_USER))
					{
						throw new DAOException(ApplicationProperties.getValue("user.cannotCreateScientist"));
					}
					if(user.getRoleId()==null || user.getRoleId().equals("") || user.getSiteCollection()==null || user.getSiteCollection().isEmpty())
					{	
						throw new DAOException(ApplicationProperties.getValue("user.siteIsRequired"));
					}
				}
			}
			if(user.getPageOf().equalsIgnoreCase("pageOfChangePassword"))
			{
				return true;
			}
			/*if(user.getId().equals(sessionDataBean.getUserId()))
			{
				throw new DAOException(ApplicationProperties.getValue("user.cannotEditOwnPrivileges"));
			}*/
			if(user.getPageOf().equalsIgnoreCase("pageOfSignUp"))
			{
				return true;
			}
			if(sessionDataBean!=null && user.getLoginName().equals(sessionDataBean.getUserName()))
			{
				return true;
			}
			return false;
		}	
		
		/**
		 * Over-ridden for the case of Non - Admin user should be able to edit
		 * his/her details e.g. Password 
		 * (non-Javadoc)
		 * @throws UserNotAuthorizedException 
		 * @throws DAOException 
		 * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.common.dao.AbstractDAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
		 */
		public boolean isAuthorized(AbstractDAO dao, Object domainObject, SessionDataBean sessionDataBean) throws UserNotAuthorizedException, DAOException  
		{
			boolean isAuthorized = false;
			isAuthorized = checkUser(domainObject, sessionDataBean);
			if(isAuthorized)
			{
				return true;
			}
			
			String privilegeName = getPrivilegeName(domainObject);
			String protectionElementName = getObjectId(dao, domainObject);
			
			if(Constants.cannotCreateSuperAdmin.equals(protectionElementName))
			{
				throw new DAOException(ApplicationProperties.getValue("user.cannotCreateSuperAdmin"));
			}
			if(Constants.siteIsRequired.equals(protectionElementName))
			{
				throw new DAOException(ApplicationProperties.getValue("user.siteIsRequired"));
			}
			
			PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(sessionDataBean.getUserName());
			 
			if (protectionElementName != null)
			{
				String [] prArray = protectionElementName.split(Constants.UNDERSCORE);
				String baseObjectId = prArray[0];
				String objId = null;
				
	    		for (int i = 1 ; i < prArray.length;i++)
	    		{
	    			objId = baseObjectId+Constants.UNDERSCORE+prArray[i];
	    			isAuthorized = privilegeCache.hasPrivilege(objId.toString(),privilegeName);
	    			if (!isAuthorized)
	    			{
	    				break;
	    			}
	    		}
	    		
	    		if (!isAuthorized)
	            {
	    			throw Utility.getUserNotAuthorizedException(privilegeName, protectionElementName);    
	            }
	    		return isAuthorized;		
			}
			else
			{
				// return false;
				throw Utility.getUserNotAuthorizedException(privilegeName, protectionElementName);  
			}		
		}			
}