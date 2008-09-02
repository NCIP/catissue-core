/**
 * 
 */

package edu.wustl.catissuecore.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.TaskTimeCalculater;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SecurityDataBean;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.Roles;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.exceptions.CSException;

/**
 * @author supriya_dankh
 *
 */
public class CollectionProtocolAuthorization implements edu.wustl.catissuecore.util.Roles
{

	public void authenticate(CollectionProtocol collectionProtocol, HashSet protectionObjects,
			Map<String,SiteUserRolePrivilegeBean> rowIdMap) throws DAOException
	{

		TaskTimeCalculater cpAuth = TaskTimeCalculater.startTask("CP insert Authenticatge",
				CollectionProtocolBizLogic.class);
		try
		{

			PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
			privilegeManager.insertAuthorizationData(
					getAuthorizationData(collectionProtocol, rowIdMap), protectionObjects,
					getDynamicGroups(collectionProtocol), collectionProtocol.getObjectId());
		}
		catch (SMException e)
		{
			throw new DAOException(e);
		}
		catch (CSException e)
		{
			throw new DAOException(e);
		}
		finally
		{
			TaskTimeCalculater.endTask(cpAuth);
		}

	}

	/**
	 * This method returns collection of UserGroupRoleProtectionGroup objects that speciefies the 
	 * user group protection group linkage through a role. It also specifies the groups the protection  
	 * elements returned by this class should be added to.
	 * @return
	 * @throws CSException 
	 */
	protected Vector<SecurityDataBean> getAuthorizationData(AbstractDomainObject obj,
			Map<String,SiteUserRolePrivilegeBean> rowIdMap) throws SMException, CSException
	{

		Vector<SecurityDataBean> authorizationData = new Vector<SecurityDataBean>();
		CollectionProtocol collectionProtocol = (CollectionProtocol) obj;
		inserPIPrivileges(collectionProtocol, authorizationData);
		insertCoordinatorPrivileges(collectionProtocol, authorizationData);
		if(rowIdMap !=null)
		{
		  insertCpUserPrivilegs(collectionProtocol, authorizationData, rowIdMap);
		}
		return authorizationData;
	}

	public void insertCpUserPrivilegs(CollectionProtocol collectionProtocol,
			Vector<SecurityDataBean> authorizationData, Map<String,SiteUserRolePrivilegeBean> rowIdMap) throws SMException,
			CSException
	{
		int noOfUsers = rowIdMap.size();
		Set<Site> siteCollection = new HashSet<Site>();
		Set<User> userCollection = new HashSet<User>();
		String roleName = "";
		
		for (Iterator<String> mapItr = rowIdMap.keySet().iterator(); mapItr.hasNext(); )
		{
			String key = mapItr.next();
			SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = rowIdMap.get(key);
			User user = siteUserRolePrivilegeBean.getUser();
			
			if(!siteUserRolePrivilegeBean.isCustChecked())
			{
				List<Site> siteList = siteUserRolePrivilegeBean.getSiteList();
				if(siteList != null && !siteList.isEmpty())
				{
					siteCollection.add(siteList.get(0));
				}
				continue;
			}
			
			if(siteUserRolePrivilegeBean.isRowDeleted())
			{
				Utility.processDeletedPrivilegesOnCPPage(siteUserRolePrivilegeBean, collectionProtocol.getId());
			}
			else if(siteUserRolePrivilegeBean.isRowEdited())
			{
				// siteCollection.addAll(siteList);
				// user = siteUserRolePrivilegeBean.getUser();
				// userCollection.add(user);
				String defaultRole = siteUserRolePrivilegeBean.getRole().getValue();
				if (defaultRole != null && (defaultRole.equalsIgnoreCase("-1") || defaultRole.equalsIgnoreCase("0") || defaultRole.equalsIgnoreCase(Constants.NON_ADMIN_USER)) )
				{
					roleName = Constants.getCPRoleName(collectionProtocol.getId(), user.getCsmUserId(), defaultRole);
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
				
				PrivilegeManager.getInstance().createRole(roleName,
						privileges);
				String userId = String.valueOf(user.getCsmUserId());
				gov.nih.nci.security.authorization.domainobjects.User csmUser = getUserByID(userId);
				HashSet<gov.nih.nci.security.authorization.domainobjects.User> group = new HashSet<gov.nih.nci.security.authorization.domainobjects.User>();
				group.add(csmUser); 
				String protectionGroupName = new String(Constants
						.getCollectionProtocolPGName(collectionProtocol.getId()));
				SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
				userGroupRoleProtectionGroupBean.setUser("");
				userGroupRoleProtectionGroupBean.setRoleName(roleName);
				userGroupRoleProtectionGroupBean.setGroupName(Constants.getCPUserGroupName(collectionProtocol.getId(), user.getCsmUserId()));
				userGroupRoleProtectionGroupBean.setProtectionGroupName(protectionGroupName);
				userGroupRoleProtectionGroupBean.setGroup(group);
				authorizationData.add(userGroupRoleProtectionGroupBean);
			}
			
			if(!siteUserRolePrivilegeBean.isRowDeleted())
			{
				userCollection.add(user);
			
				List<Site> siteList = siteUserRolePrivilegeBean.getSiteList();
				for (Site site : siteList)
				{ 
					boolean isPresent = false;
					for (Site setSite : siteCollection)
					{
						if (setSite.getId().equals(site.getId()))
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
		collectionProtocol.getSiteCollection().clear();
		collectionProtocol.getSiteCollection().addAll(siteCollection);
		for (User user : userCollection)
		{
			boolean isPresent = false;
			for (User setUser : collectionProtocol.getAssignedProtocolUserCollection())
			{
				if (user.getId().equals(setUser.getId()))
				{
					isPresent = true;
				}
			}
			if (!isPresent)
			{
				collectionProtocol.getAssignedProtocolUserCollection().add(user);
			}
		} 
	}

	/**
	 * @param siteCollection
	 * @param siteList
	 */
	private Set<Site> getSiteCollection(List<Long> siteList)
	{
		Set<Site> siteCollection = new HashSet<Site>();
		for (Long siteId : siteList)
		{
			Site site = new Site();
			site.setId(siteId);
			siteCollection.add(site);
		}
		return siteCollection;
	}

	public void insertCoordinatorPrivileges(CollectionProtocol collectionProtocol,
			Vector<SecurityDataBean> authorizationData) throws SMException
	{
		Collection<User> coordinators = collectionProtocol.getCoordinatorCollection();
		HashSet<gov.nih.nci.security.authorization.domainobjects.User> group = new HashSet<gov.nih.nci.security.authorization.domainobjects.User>();
		String userId = "";
		for (Iterator<User> it = coordinators.iterator(); it.hasNext();)
		{
			User aUser = it.next();
			userId = String.valueOf(aUser.getCsmUserId());
			gov.nih.nci.security.authorization.domainobjects.User user = getUserByID(userId);
			group.add(user);
		}

		String protectionGroupName = new String(Constants
				.getCollectionProtocolPGName(collectionProtocol.getId()));
		SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
		userGroupRoleProtectionGroupBean.setUser(userId);
		userGroupRoleProtectionGroupBean.setRoleName(COORDINATOR);
		userGroupRoleProtectionGroupBean.setGroupName(Constants
				.getCollectionProtocolCoordinatorGroupName(collectionProtocol.getId()));
		userGroupRoleProtectionGroupBean.setProtectionGroupName(protectionGroupName);
		userGroupRoleProtectionGroupBean.setGroup(group);
		authorizationData.add(userGroupRoleProtectionGroupBean);
	}

	public void inserPIPrivileges(CollectionProtocol collectionProtocol,
			Vector<SecurityDataBean> authorizationData) throws SMException
	{
		HashSet<gov.nih.nci.security.authorization.domainobjects.User> group = new HashSet<gov.nih.nci.security.authorization.domainobjects.User>();
		String userId = String
				.valueOf(collectionProtocol.getPrincipalInvestigator().getCsmUserId());
		gov.nih.nci.security.authorization.domainobjects.User user = getUserByID(userId);
		group.add(user);

		String protectionGroupName = new String(Constants
				.getCollectionProtocolPGName(collectionProtocol.getId()));
		SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
		userGroupRoleProtectionGroupBean.setUser(userId);
		userGroupRoleProtectionGroupBean.setRoleName(PI);
		userGroupRoleProtectionGroupBean.setGroupName(Constants
				.getCollectionProtocolPIGroupName(collectionProtocol.getId()));
		userGroupRoleProtectionGroupBean.setProtectionGroupName(protectionGroupName);
		userGroupRoleProtectionGroupBean.setGroup(group);
		authorizationData.add(userGroupRoleProtectionGroupBean);

	}

	private String[] getDynamicGroups(AbstractDomainObject obj)
	{
		String[] dynamicGroups = null;
		return dynamicGroups;
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
//not required
	/**
	 * @param collectionProtocol
	 * @return
	 * @throws DAOException
	 */
	public Long getCSMUserId(DAO dao, User user) throws DAOException
	{
		String[] selectColumnNames = {Constants.CSM_USER_ID};
		String[] whereColumnNames = {Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnCondition = {"="};
		Long[] whereColumnValues = {user.getId()};
		List csmUserIdList = dao.retrieve(User.class.getName(), selectColumnNames,
				whereColumnNames, whereColumnCondition, whereColumnValues,
				Constants.AND_JOIN_CONDITION);
		

		if (csmUserIdList.isEmpty() == false)
		{
			Long csmUserId = (Long) csmUserIdList.get(0);

			return csmUserId;
		}

		return null;
	}

	

	public boolean hasCoordinator(User coordinator, CollectionProtocol collectionProtocol)
	{
		Iterator<User> it = collectionProtocol.getCoordinatorCollection().iterator();
		while (it.hasNext())
		{
			User coordinatorOld = it.next();
			if (coordinator.getId().equals(coordinatorOld.getId()))
			{
				return true;
			}
		}
		return false;
	}

	public void updatePIAndCoordinatorGroup(DAO dao, CollectionProtocol collectionProtocol,
			boolean operation) throws SMException, DAOException
	{
		Long principalInvestigatorId = collectionProtocol.getPrincipalInvestigator().getCsmUserId();
		
		String userGroupName = Constants.getCollectionProtocolPIGroupName(collectionProtocol
				.getId());
		if (operation)
		{
			SecurityManager.getInstance(CollectionProtocolBizLogic.class).removeUserFromGroup(
					userGroupName, principalInvestigatorId.toString());
		}
		else
		{
			SecurityManager.getInstance(CollectionProtocolBizLogic.class).assignUserToGroup(
					userGroupName, principalInvestigatorId.toString());
		}

		userGroupName = Constants.getCollectionProtocolCoordinatorGroupName(collectionProtocol
				.getId());

		Iterator<User> iterator = collectionProtocol.getCoordinatorCollection().iterator();
		while (iterator.hasNext())
		{
			User user = iterator.next();
			if (operation)
			{
				SecurityManager.getInstance(CollectionProtocolBizLogic.class).removeUserFromGroup(
						userGroupName, user.getCsmUserId().toString());
			}
			else
			{
				Long csmUserId = getCSMUserId(dao, user);
				if (csmUserId != null)
				{
					SecurityManager.getInstance(CollectionProtocolBizLogic.class)
							.assignUserToGroup(userGroupName, csmUserId.toString());
				}
			}
		}
	}

}
