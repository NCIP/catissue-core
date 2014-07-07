
package com.krishagni.catissueplus.core.services.testdata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.privileges.PrivilegeType;
import com.krishagni.catissueplus.core.privileges.domain.Privilege;
import com.krishagni.catissueplus.core.privileges.domain.Role;
import com.krishagni.catissueplus.core.privileges.events.CreateRoleEvent;
import com.krishagni.catissueplus.core.privileges.events.RoleDetails;
import com.krishagni.catissueplus.core.privileges.events.UpdateRoleEvent;

import edu.wustl.common.beans.SessionDataBean;

public class RoleTestData {

	public static final String ACTIVITY_STATUS_CLOSED = "Closed";

	public static final String FIRST_NAME = "first name";

	public static final String LAST_NAME = "last name";

	public static final String ROLE_NAME = "role name";

	public static final String EMAIL_ADDRESS = "email address";

	public static final String DEPARTMENT = "department";

	public static final String AUTH_DOMAIN = "auth domain";

	public static final String LDAP = "ldap";

	public static SessionDataBean getSessionDataBean() {
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setAdmin(true);
		sessionDataBean.setCsmUserId("1");
		sessionDataBean.setFirstName("admin");
		sessionDataBean.setIpAddress("127.0.0.1");
		sessionDataBean.setLastName("admin");
		sessionDataBean.setUserId(1L);
		sessionDataBean.setUserName("admin@admin.com");
		return sessionDataBean;
	}

	public static CreateRoleEvent getCreateRoleEventWithEmptyRoleName() {
		CreateRoleEvent event = getCreateRoleEvent();
		RoleDetails details = event.getRoleDetails();
		details.setName(null);
		event.setRoleDetails(details);
		return event;
	}

	public static CreateRoleEvent getCreateRoleEvent() {
		CreateRoleEvent event = new CreateRoleEvent();
		event.setSessionDataBean(getSessionDataBean());
		RoleDetails details = new RoleDetails();
		details.setName("My Role");
		details.setPrivilegeNames(getPrivilegeNames());
		event.setRoleDetails(details);
		return event;
	}

	private static List<String> getPrivilegeNames() {
		List<String> privlegeNames = new ArrayList<String>();
		privlegeNames.add(PrivilegeType.DISTRIBUTION.value());
		return privlegeNames;
	}

	public static Role getRole(long id) {
		Role role = new Role();
		role.setId(id);
		role.setName("My Role");
		role.setPrivileges(getPrivileges());
		return role;
	}

	private static Set<Privilege> getPrivileges() {
		Set<Privilege> privileges = new HashSet<Privilege>();
		privileges.add(getPrivilege(1l));
		return privileges;
	}

	public static UpdateRoleEvent getUpdateRoleEvent() {
		UpdateRoleEvent event = new UpdateRoleEvent();
		event.setSessionDataBean(getSessionDataBean());
		RoleDetails details = new RoleDetails();
		details.setName("My Role");
		details.setPrivilegeNames(getPrivilegeNames());
		event.setRoleDetails(details);
		return event;
	}

	public static Privilege getPrivilege(long id) {
		Privilege privilege = new Privilege();
		privilege.setId(id);
		privilege.setName(PrivilegeType.DISTRIBUTION.value());
		return privilege;
	}

	public static List<Role> getRoles() {
		List<Role> roles = new ArrayList<Role>();
		roles.add(getRole(1l));
		return roles;
	}

}
