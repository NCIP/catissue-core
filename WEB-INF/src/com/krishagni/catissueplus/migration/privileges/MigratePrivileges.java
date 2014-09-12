
package com.krishagni.catissueplus.migration.privileges;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.krishagni.catissueplus.core.administrative.events.AllUsersEvent;
import com.krishagni.catissueplus.core.administrative.events.GetUserEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchUserEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllUsersEvent;
import com.krishagni.catissueplus.core.administrative.events.UserCPRoleDetails;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.administrative.events.UserPatchDetails;
import com.krishagni.catissueplus.core.administrative.events.UserUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.privileges.events.CreateRoleEvent;
import com.krishagni.catissueplus.core.privileges.events.RoleCreatedEvent;
import com.krishagni.catissueplus.core.privileges.events.RoleDetails;
import com.krishagni.catissueplus.core.privileges.services.RoleService;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.CaTissuePrivilegeUtility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

public class MigratePrivileges {

	private static final Logger logger = Logger.getLogger(MigratePrivileges.class);

	private Set<Long> updatedUsers = new HashSet<Long>();

	private Set<Long> failedUsers = new HashSet<Long>();

	private Map<PrivilegeDto, RoleDetails> privilegeRoles;

	private int count = 1;

	public void migratePrivileges() {
		try {
			initializeRolesMap();
			List<UserSummary> allUsers = getAllUsers();
			for (UserSummary userSummary : allUsers) {
				try {
					PrivilegeCache pc = PrivilegeManager.getInstance().getPrivilegeCache(userSummary.getLoginName());
					Map<String, SiteUserRolePrivilegeBean> privilegeMap = CaTissuePrivilegeUtility.getAllPrivileges(pc);
					Set<Long> cpLevelPriviCPIds = new HashSet<Long>();
					//CP level privileges
					List<CPRoleDetails> cpUserRoles = getCPLevelPrivileges(privilegeMap, cpLevelPriviCPIds);
					//Site Level Privileges
					List<CPRoleDetails> siteCPRoles = getSiteLevelPrivileges(userSummary, privilegeMap, cpLevelPriviCPIds);

					cpUserRoles.addAll(siteCPRoles);
					for (CPRoleDetails cpRoleDetails : cpUserRoles) {
						PrivilegeDto privDto = new PrivilegeDto();
						privDto.setPrivileges(cpRoleDetails.getRoleDetails().getPrivilegeNames());
						RoleDetails roleDetails = privilegeRoles.get(privDto);
						if (roleDetails.getId() == null) {
							RoleDetails newRoleDetails = createRole(cpRoleDetails.getRoleDetails());
							privilegeRoles.put(privDto, newRoleDetails);
						}
					}

					boolean isUserUpdated = updateUser(userSummary, cpUserRoles);
					if (isUserUpdated) {
						updatedUsers.add(userSummary.getId());
					}
					else {
						failedUsers.add(userSummary.getId());
					}
				}
				catch (Exception e) {
					failedUsers.add(userSummary.getId());
				}
			}

			System.out.println("Migration Failed For Users : " + failedUsers);
			System.out.println("Migration Successfully done for Users" + updatedUsers);
		}
		catch (Exception e) {
			logger.error("Privilege Migration failed ", e);
		}
	}

	private List<CPRoleDetails> getSiteLevelPrivileges(UserSummary userSummary,
			Map<String, SiteUserRolePrivilegeBean> privilegeMap, Set<Long> cpLevelPrivCpIds) throws Exception {
		List<CPRoleDetails> cpUserRoles = new ArrayList<CPRoleDetails>();
		Set<CPRoleDetails> conflictedCpUserRoles = new HashSet<CPRoleDetails>();

		for (Map.Entry<String, SiteUserRolePrivilegeBean> entry : privilegeMap.entrySet()) {
			String key = entry.getKey();
			SiteUserRolePrivilegeBean siteUserRolePriBean = entry.getValue();

			if (!key.startsWith("CP_")) {
				// populate privileges
				List<String> privileges = populatePrivileges(siteUserRolePriBean.getPrivileges());
				Site site = siteUserRolePriBean.getSiteList().get(0);
				Collection<CollectionProtocol> cps = site.getCollectionProtocolCollection();
				for (CollectionProtocol collectionProtocol : cps) {
					if (!cpLevelPrivCpIds.contains(collectionProtocol.getId())
							&& !isPrivilegeConflict(collectionProtocol, cpUserRoles, conflictedCpUserRoles)) {
						PrivilegeDto privDto = new PrivilegeDto();
						privDto.setPrivileges(privileges);
						// Get role name
						RoleDetails roleDetails = privilegeRoles.get(privDto);
						if (roleDetails == null) {
							// If for given privileges there is no role is exist then create new Role
							roleDetails = createTransientRole(privileges);
							privilegeRoles.put(privDto, roleDetails);
						}

						CPRoleDetails cpRoleDetails = new CPRoleDetails();
						cpRoleDetails.setCpTitle(collectionProtocol.getTitle());
						cpRoleDetails.setRoleDetails(roleDetails);
						cpUserRoles.add(cpRoleDetails);

					}
				}
			}
		}
		System.out
				.println("########################### Conflicted User CP Roles #######################################"
						+ conflictedCpUserRoles);
		cpUserRoles.removeAll(conflictedCpUserRoles);
		for (CPRoleDetails userCPRoleDetails : conflictedCpUserRoles) {
						System.out.println("Conflicted User Collection Protocol:  " + userCPRoleDetails.getCpTitle() + " USERID : " + userSummary.getId() );

		}

		return cpUserRoles;
	}

	private boolean isPrivilegeConflict(CollectionProtocol collectionProtocol, List<CPRoleDetails> cpUserRoles,
			Set<CPRoleDetails> conflictedCpUserRoles) {
		boolean flag = false;
		for (CPRoleDetails userCPRoleDetails : cpUserRoles) {
			if (userCPRoleDetails.getCpTitle().equals(collectionProtocol.getTitle())) {
				conflictedCpUserRoles.add(userCPRoleDetails);
				flag = true;

			}
		}
		return flag;
	}

	private List<CPRoleDetails> getCPLevelPrivileges(Map<String, SiteUserRolePrivilegeBean> privilegeMap,
			Set<Long> cpLevelPrivCpIds) throws Exception {
		List<CPRoleDetails> cpUserRoles = new ArrayList<CPRoleDetails>();
		for (Map.Entry<String, SiteUserRolePrivilegeBean> entry : privilegeMap.entrySet()) {
			String key = entry.getKey();
			SiteUserRolePrivilegeBean siteUserRolePriBean = entry.getValue();

			if (key.startsWith("CP_")) {
				// populate privileges
				List<String> privileges = populatePrivileges(siteUserRolePriBean.getPrivileges());
				CollectionProtocol cp = siteUserRolePriBean.getCollectionProtocol();
				PrivilegeDto privDto = new PrivilegeDto();
				privDto.setPrivileges(privileges);
				// GET role name
				RoleDetails roleDetails = privilegeRoles.get(privDto);
				if (roleDetails == null) {
					roleDetails = createTransientRole(privileges);
					privilegeRoles.put(privDto, roleDetails);
				}

				CPRoleDetails cpRoleDetails = new CPRoleDetails();
				cpRoleDetails.setCpTitle(cp.getTitle());
				cpRoleDetails.setRoleDetails(roleDetails);

				cpUserRoles.add(cpRoleDetails);
				cpLevelPrivCpIds.add(cp.getId());
			}
		}
		return cpUserRoles;
	}

	private List<UserSummary> getAllUsers() {
		ApplicationContext caTissueContext = OpenSpecimenAppCtxProvider.getAppCtx();
		ReqAllUsersEvent reqAllUsersEvent = new ReqAllUsersEvent();
		reqAllUsersEvent.setSessionDataBean(getSessionDataBean());
		UserService userSvc = (UserService) caTissueContext.getBean("userSvc");
		AllUsersEvent allUsersEvent = userSvc.getAllUsers(reqAllUsersEvent);
		return allUsersEvent.getUsers();
	}

	private void initializeRolesMap() {
		privilegeRoles = new HashMap<PrivilegeDto, RoleDetails>();
		ApplicationContext caTissueContext = OpenSpecimenAppCtxProvider.getAppCtx();
		RoleService roleSvc = (RoleService) caTissueContext.getBean("roleSvc");
		List<RoleDetails> roles = roleSvc.getAllRoles();
		for (RoleDetails role : roles) {
			List<String> privileges = role.getPrivilegeNames();
			Collections.sort(privileges);
			PrivilegeDto privilegeDto = new PrivilegeDto();
			privilegeDto.setPrivileges(privileges);
			privilegeRoles.put(privilegeDto, role);
		}
	}

	private List<String> populatePrivileges(List<NameValueBean> nvb) {
		List<String> privileges = new ArrayList<String>();
		for (NameValueBean privilege : nvb) {
			privileges.add(privilege.getName());
		}
		Collections.sort(privileges);
		return privileges;
	}

	private boolean updateUser(UserSummary userSummary, List<CPRoleDetails> cpUserRoles) throws Exception {
		try {
			List<UserCPRoleDetails> userCPRoleDetailsList = populateUserCPRoles(cpUserRoles);
			ApplicationContext caTissueContext = OpenSpecimenAppCtxProvider.getAppCtx();
			UserService userSvc = (UserService) caTissueContext.getBean("userSvc");
			GetUserEvent getUserEvent = userSvc.getUser(userSummary.getId());
			UserDetails oldUserDetails = getUserEvent.getUserDetails();
			List<UserCPRoleDetails> userCPRoles = oldUserDetails.getUserCPRoles();
			userCPRoles.addAll(userCPRoleDetailsList);

			UserPatchDetails userDetails = new UserPatchDetails();
			userDetails.setUserCPRoles(userCPRoles);

			PatchUserEvent patchUserEvent = new PatchUserEvent();
			patchUserEvent.setUserDetails(userDetails);
			patchUserEvent.setUserId(userSummary.getId());

			List<String> modifiedAttributes = new ArrayList<String>();
			modifiedAttributes.add("userCPRoles");
			userDetails.setModifiedAttributes(modifiedAttributes);
			UserUpdatedEvent userUpdatedEvent = userSvc.patchUser(patchUserEvent);
			if (userUpdatedEvent.getStatus().equals(EventStatus.OK)) {
				return true;
			}
		}
		catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
		return false;

	}

	private List<UserCPRoleDetails> populateUserCPRoles(List<CPRoleDetails> cpUserRoles) {
		List<UserCPRoleDetails> userCPRoleDetailsList = new ArrayList<UserCPRoleDetails>();
		for (CPRoleDetails cpRoleDetails : cpUserRoles) {
			UserCPRoleDetails userCpRoleDetails = new UserCPRoleDetails();
			userCpRoleDetails.setCpTitle(cpRoleDetails.getCpTitle());
			userCpRoleDetails.setRoleName(cpRoleDetails.getRoleDetails().getName());
			//set the id
			userCPRoleDetailsList.add(userCpRoleDetails);
		}
		return userCPRoleDetailsList;
	}

	private RoleDetails createTransientRole(List<String> privileges) {
		String roleName = "Role" + count;
		count++;
		RoleDetails details = new RoleDetails();
		details.setName(roleName);
		details.setDescription("Role created while migration.");
		details.setPrivilegeNames(privileges);
		return details;

	}

	private RoleDetails createRole(RoleDetails roleDetails) throws Exception {
		ApplicationContext caTissueContext = OpenSpecimenAppCtxProvider.getAppCtx();
		RoleService roleSvc = (RoleService) caTissueContext.getBean("roleSvc");

		RoleDetails details = new RoleDetails();
		details.setName(roleDetails.getName());
		details.setDescription(roleDetails.getDescription());
		details.setPrivilegeNames(roleDetails.getPrivilegeNames());

		CreateRoleEvent createRoleEvent = new CreateRoleEvent();
		createRoleEvent.setRoleDetails(details);

		SessionDataBean sessionDataBean = getSessionDataBean();
		createRoleEvent.setSessionDataBean(sessionDataBean);

		RoleCreatedEvent event = roleSvc.createRole(createRoleEvent);
		if (event.getStatus().equals(EventStatus.OK)) {
			RoleDetails newRoleDetails = event.getRoleDetails();
			List<String> privilegeNames = newRoleDetails.getPrivilegeNames();
			Collections.sort(privilegeNames);
			newRoleDetails.setPrivilegeNames(privilegeNames);
			return newRoleDetails;
		}
		else {
			throw new Exception("Role with name " + roleDetails.getName() + "can not created");
		}

	}

	private SessionDataBean getSessionDataBean() {
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setAdmin(true);
		sessionDataBean.setUserName("admin@admin.com");
		sessionDataBean.setCsmUserId("1");
		sessionDataBean.setUserId(1L);
		sessionDataBean.setFirstName("admin");
		sessionDataBean.setLastName("admin");
		return sessionDataBean;
	}

}
