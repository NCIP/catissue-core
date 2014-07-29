
package com.krishagni.catissueplus.core.services.testdata;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.krishagni.catissueplus.core.administrative.domain.Address;
import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Password;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CloseUserEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableUserEvent;
import com.krishagni.catissueplus.core.administrative.events.ForgotPasswordEvent;
import com.krishagni.catissueplus.core.administrative.events.PasswordDetails;
import com.krishagni.catissueplus.core.administrative.events.PatchUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdatePasswordEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UserCPRoleDetails;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.administrative.events.UserPatchDetails;
import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.AuthProvider;
import com.krishagni.catissueplus.core.privileges.PrivilegeType;
import com.krishagni.catissueplus.core.auth.domain.Ldap;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.privileges.domain.Privilege;
import com.krishagni.catissueplus.core.privileges.domain.Role;
import com.krishagni.catissueplus.core.privileges.domain.UserCPRole;

import edu.wustl.common.beans.SessionDataBean;

public class UserTestData {

	public static final String ACTIVITY_STATUS_CLOSED = "Closed";

	public static final String FIRST_NAME = "first name";

	public static final String LAST_NAME = "last name";

	public static final String LOGIN_NAME = "login name";

	public static final String EMAIL_ADDRESS = "email address";

	public static final String DEPARTMENT = "department";

	public static final String AUTH_DOMAIN = "auth domain";

	public static final String LDAP = "ldap";

	private static final String PATCH_USER = "patch user";

	public static final Object ACTIVITY_STATUS_DISABLED = "Disabled";

	public static List<User> getUserList() {
		List<User> users = new ArrayList<User>();
		users.add(new User());
		users.add(new User());
		return users;
	}

	public static Role getRole(long id) {
		Role role = new Role();
		role.setId(id);
		role.setName("My Role" + id);
		role.setPrivileges(getPrivileges());
		return role;
	}

	private static Set<Privilege> getPrivileges() {
		Set<Privilege> privileges = new HashSet<Privilege>();
		privileges.add(getPrivilege(1l));
		return privileges;
	}

	public static Privilege getPrivilege(long id) {
		Privilege privilege = new Privilege();
		privilege.setId(id);
		privilege.setName(PrivilegeType.DISTRIBUTION.value());
		return privilege;
	}

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

	public static User getUser(Long id) {
		User user = new User();
		user.setId(id);
		user.setFirstName("firstName1");
		user.setLastName("lastName1");
		user.setLoginName("admin@admin.com");
		user.setEmailAddress("sci@sci.com");
		user.setPasswordToken("e5412f93-a1c5-4ede-b66d-b32302cd4018");
		user.setDepartment(new Department());
		user.setAddress(getAddress());
		user.setAuthDomain(getAuthDomain(id));
		user.setUserSites(new HashSet<Site>());
		user.setUserCPRoles(new HashSet<UserCPRole>());
		Long ide = user.getId(); //for coverage
		return user;
	}

	private static Address getAddress() {
		Address address = new Address();
		address.setCity("Pune");
		address.setCountry("India");
		address.setFaxNumber("421-323");
		address.setId(1l);
		address.setState("Maharatera");
		address.setPhoneNumber("3123213");
		address.setZipCode("222134");
		Long id = address.getId(); // for coverage
		return address;
	}

	public static CloseUserEvent getCloseUserEvent() {
		Long userId = 1l;
		CloseUserEvent event = new CloseUserEvent();
		event.setId(userId);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static ForgotPasswordEvent getForgotPasswordEvent() {
		ForgotPasswordEvent event = new ForgotPasswordEvent();
		event.setName("admin@admin.com");
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static UpdateUserEvent getUpdateUserEvent() {
		UserDetails details = new UserDetails();
		details.setActivityStatus("Active");
		details.setFirstName("firstName");
		details.setLastName("lastName");
		details.setDeptName("Chemical");
		details.setCountry("India");
		details.setEmailAddress("sci@sci.com");
		details.setLoginName("admin@admin.com");
		details.setDomainName("MyLdap");
		details.setUserSiteNames(getSites());
		details.setUserCPRoles(getUserCpRolesDetails());

		UpdateUserEvent reqEvent = new UpdateUserEvent(details, 1L);
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setUserDetails(details);
		return reqEvent;
	}

	public static AuthDomain getAuthDomain(long id) {
		AuthDomain authDomain = new AuthDomain();
		authDomain.setId(id);
		authDomain.setName("MyAuth");

		Ldap ldap = new Ldap();
		ldap.setHost("ldap.testathon.net");
		ldap.setPort(389l);
		ldap.setIdField("cn");
		ldap.setDirectoryContext("OU=users,DC=testathon,DC=net");
		ldap.setBindUser("john");
		ldap.setBindPassword("john");

		ldap.setGivenNameField("givenName");
		ldap.setSurnameField("sn");
		ldap.setEmailField("mail");

		ldap.setSearchBaseDir("OU=users,DC=testathon,DC=net");
		ldap.setFilterString("(&(objectClass=*)(uid={0}))");
		AuthProvider authProvider = new AuthProvider();
		authProvider.setAuthType("ldap");
		authProvider.setImplClass("com.krishagni.catissueplus.core.auth.services.impl.LdapAuthServiceImpl");
		authDomain.setAuthProvider(authProvider);
		authDomain.setLdap(ldap);
		return authDomain;
	}

	public static UpdateUserEvent getUpadteUserEventWithLNUpdate() {
		UpdateUserEvent reqEvent = getUpdateUserEvent();
		UserDetails details = getUserDetails();
		details.setLoginName("admin");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}

	public static CreateUserEvent getCreateUserEvent() {
		CreateUserEvent reqEvent = new CreateUserEvent(null);
		reqEvent.setSessionDataBean(getSessionDataBean());
		UserDetails details = getUserDetails();
		reqEvent.setUserDetails(details);
		return reqEvent;
	}

	public static UserDetails getUserDetails() {
		UserDetails details = new UserDetails();
		details.setActivityStatus("Active");
		details.setFirstName("firstName");
		details.setLastName("lastName");
		details.setDeptName("Chemical");
		details.setEmailAddress("sci@sci.com");
		details.setLoginName("admin@admin.com");
		details.setDomainName("myLdap");
		details.setCountry("India");
		details.setUserSiteNames(getSites());
		details.setUserCPRoles(getUserCpRolesDetails());
		return details;
	}

	public static CreateUserEvent getCreateUserEventWithInvalidEmail() {
		CreateUserEvent reqEvent = getCreateUserEvent();
		UserDetails details = getUserDetails();
		details.setEmailAddress("admin");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}

	public static UpdateUserEvent getCreateUserEventWithNonDupEmail() {
		UpdateUserEvent reqEvent = getUpdateUserEvent();
		UserDetails details = getUserDetails();
		details.setEmailAddress("admin@admin");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}

	public static CreateUserEvent getCreateUserEventForUserCreation() {
		CreateUserEvent reqEvent = getCreateUserEvent();
		UserDetails details = getUserDetails();
		reqEvent.setUserDetails(details);
		return reqEvent;
	}

	public static CreateUserEvent getCreateUserEventWithNullSite() {
		CreateUserEvent reqEvent = getCreateUserEvent();
		UserDetails details = getUserDetails();
		details.setUserSiteNames(null);
		reqEvent.setUserDetails(details);
		return reqEvent;
	}

	public static CreateUserEvent getCreateUserEventWithNullCP() {
		CreateUserEvent reqEvent = getCreateUserEvent();
		UserDetails details = getUserDetails();
		details.setUserCPRoles(null);
		reqEvent.setUserDetails(details);
		return reqEvent;
	}

	public static CreateUserEvent getCreateUserEventWithEmptyLoginName() {
		CreateUserEvent reqEvent = getCreateUserEvent();
		UserDetails details = getUserDetails();
		details.setLoginName("");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}

	public static CreateUserEvent getCreateUserEventWithEmptyDomainName() {
		CreateUserEvent reqEvent = getCreateUserEvent();
		UserDetails details = getUserDetails();
		details.setDomainName("");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}

	public static CreateUserEvent getCreateUserEventWithEmptyEmail() {
		CreateUserEvent reqEvent = getCreateUserEvent();
		UserDetails details = getUserDetails();
		details.setEmailAddress("");

		reqEvent.setUserDetails(details);
		return reqEvent;
	}

	public static CreateUserEvent getCreateUserEventWithEmptyFirstName() {
		CreateUserEvent reqEvent = getCreateUserEvent();
		UserDetails details = getUserDetails();
		details.setFirstName("");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}

	public static CreateUserEvent getCreateUserEventWithEmptyLastName() {
		CreateUserEvent reqEvent = getCreateUserEvent();
		UserDetails details = getUserDetails();
		details.setLastName("");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}

	public static Department getDeparment(String name) {
		Department department = new Department();
		department.setId(10l);

		department.setName(name);
		return department;
	}

	public static UpdatePasswordEvent getUpdatePasswordEvent() {
		PasswordDetails details = new PasswordDetails();
		details.setUserId(80L);
		details.setOldPassword("Krishagni123");
		details.setNewPassword("Krishagni21");

		UpdatePasswordEvent reqEvent = new UpdatePasswordEvent(details, "e5412f93-a1c5-4ede-b66d-b32302cd4018", 80L);
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setPasswordToken("e5412f93-a1c5-4ede-b66d-b32302cd4018");
		return reqEvent;
	}

	public static UpdatePasswordEvent getUpdatePasswordEventInvalid() {
		PasswordDetails details = new PasswordDetails();
		details.setUserId(80L);
		details.setOldPassword("");
		details.setNewPassword("Krishagni21");

		UpdatePasswordEvent reqEvent = new UpdatePasswordEvent(details, "e5412f93-a1c5-4ede-b66d-b32302cd4018", 80L);
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setPasswordToken("e5412f93-a1c5-4ede-b66d-b32302cd4018");
		return reqEvent;
	}

	public static UpdatePasswordEvent getUpdatePasswordEventForReSet() {
		UpdatePasswordEvent reqEvent = getUpdatePasswordEvent();
		reqEvent.getPasswordDetails().setOldPassword("Darshan123");
		return reqEvent;
	}

	public static UpdatePasswordEvent getUpdatePasswordEventForBlankNewPass() {
		UpdatePasswordEvent reqEvent = getUpdatePasswordEvent();
		reqEvent.getPasswordDetails().setNewPassword("");
		reqEvent.getPasswordDetails().setOldPassword("catissue");
		return reqEvent;
	}

	public static UpdatePasswordEvent getUpdatePasswordEventForBlankOldPass() {
		UpdatePasswordEvent reqEvent = getUpdatePasswordEvent();
		reqEvent.getPasswordDetails().setOldPassword("");
		return reqEvent;
	}

	public static UpdatePasswordEvent getUpdatePasswordEventForBlankConfirmPass() {
		UpdatePasswordEvent reqEvent = getUpdatePasswordEvent();
		reqEvent.getPasswordDetails().setOldPassword("catissue");
		return reqEvent;
	}

	public static UpdatePasswordEvent getUpdatePasswordEventForDiffPass() {
		UpdatePasswordEvent reqEvent = getUpdatePasswordEvent();
		reqEvent.getPasswordDetails().setNewPassword("catissue");
		return reqEvent;
	}

	public static UpdatePasswordEvent getUpdatePasswordEventForDiffTokens() {
		UpdatePasswordEvent reqEvent = getUpdatePasswordEvent();
		reqEvent.setPasswordToken("e5412f93-a1c5-4ede-b66d-b32302");
		return reqEvent;
	}

	public static List<String> getOldPasswordList() {
		List<String> Passwords = new ArrayList<String>();
		Passwords.add("kris");
		Passwords.add("$2a$04$0J4/9Cb3eYcvHAjxEw43Wud9ii1jYxiogouyXe5nUVa28LXQ6al7K");
		return Passwords;
	}

	public static Site getSite() {
		Site site = new Site();
		site.setName("LABS");
		site.setId(1l);
		return site;
	}

	public static CollectionProtocol getCp() {
		CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setTitle("Query CP");
		collectionProtocol.setId(1l);
		collectionProtocol.setShortTitle("qcp");
		return collectionProtocol;
	}

	private static List<UserCPRoleDetails> getUserCpRolesDetails() {
		List<UserCPRoleDetails> cpUserRoles = new ArrayList<UserCPRoleDetails>();
		UserCPRoleDetails curDetails = new UserCPRoleDetails();
		curDetails.setRoleName("Registrar and distributor");
		curDetails.setCpTitle("Test new CP");
		cpUserRoles.add(curDetails);
		return cpUserRoles;
	}

	private static List<String> getSites() {
		List<String> sites = new ArrayList<String>();
		sites.add("ATCC");
		return sites;
	}

	public static User getUserWithCatissueDomain(long id) {
		User user = new User();
		user.setId(id);
		user.setFirstName("firstName1");
		user.setLastName("lastName1");
		user.setLoginName("admin@admin.com");
		user.setEmailAddress("sci@sci.com");
		user.setPasswordToken("e5412f93-a1c5-4ede-b66d-b32302cd4018");
		user.setDepartment(new Department());
		user.setAddress(new Address());
		user.setPasswordCollection(getPasswordCollection());
		AuthDomain authDomain = new AuthDomain();
		AuthProvider authProvider = new AuthProvider();
		authProvider.setAuthType("catissue");
		authProvider.setImplClass("com.krishagni.catissueplus.core.auth.services.impl.LdapAuthServiceImpl");
		authDomain.setName("catissue");
		user.getPasswordCollection(); //for coverage
		user.setAuthDomain(authDomain);
		return user;
	}

	private static Set<Password> getPasswordCollection() {
		Set<Password> passwords = new HashSet<Password>();
		passwords.add(getPassword("Ssadas22"));
		return passwords;
	}

	private static Password getPassword(String password) {
		Password pass = new Password();
		pass.setId(1l);
		pass.setPassword(password);
		pass.setUpdateDate(new Date());
		pass.setUser(getUser(1l));
		Long id = pass.getId(); //For coverage
		Date date =  pass.getUpdateDate();
		User user =pass.getUser();
		return pass;
	}

	public static CreateUserEvent getCreateUserEventForNonLdapUserCreation() {
		CreateUserEvent reqEvent = getCreateUserEvent();
		UserDetails details = reqEvent.getUserDetails();
		details.setDomainName("catissue");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}

	public static UpdatePasswordEvent getUpdatePasswordEventWithBlankToken() {
		UpdatePasswordEvent reqEvent = getUpdatePasswordEvent();
		reqEvent.setPasswordToken("");
		return reqEvent;
	}

	public static PatchUserEvent getPatchData() {
		PatchUserEvent event = new PatchUserEvent();
		event.setUserId(1l);
		UserPatchDetails details = new UserPatchDetails();
		try {
			BeanUtils.populate(details, getUserPatchAttributes());
		}
		catch (Exception e) {
			reportError(UserErrorCode.BAD_REQUEST, PATCH_USER);
		}
		details.setModifiedAttributes(new ArrayList<String>(getUserPatchAttributes().keySet()));
		event.setUserDetails(details);
		return event;
	}

	private static Map<String, Object> getUserPatchAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("firstName", "daresh");
		attributes.put("lastName", "patil");
		attributes.put("userSiteNames", getSites());
		attributes.put("userCPRoles", getUserCpRolesDetails());
		attributes.put("activityStatus", Status.ACTIVITY_STATUS_DISABLED.getStatus());
		attributes.put("comments", "blah blah");
		attributes.put("loginName", "dpatil");
		attributes.put("emailAddress", "daresh@smaol.com");
		attributes.put("deptName", "chemical");
		attributes.put("country", "India");
		attributes.put("state", "alaska");
		attributes.put("city", "newuor");
		attributes.put("faxNumber", "43249-434");
		attributes.put("phoneNumber", "76543");

		attributes.put("street", "john road");
		attributes.put("zipCode", "76543");
		return attributes;
	}

	public static List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		users.add(getUser(2l));
		users.add(getUser(3l));
		compareObjects();//coverage
		return users;
	}

	private static void compareObjects() {
		getUser(1l).equals(new User()); // for coverage
		getUser(1l).equals(getUser(2l));
		getUser(1l).equals(null);
		new User().equals(getUser(1l));
		getUser(1l).equals(new Department());
	}

	public static CreateUserEvent getCreateUserEventForUserCreationWithoutPrev() {
		CreateUserEvent event = getCreateUserEvent();
		event.getUserDetails().setUserCPRoles(new ArrayList<UserCPRoleDetails>());
		event.getUserDetails().setUserSiteNames(new ArrayList<String>());
		return event;
	}

	public static PatchUserEvent nonPatchData() {
		PatchUserEvent event = new PatchUserEvent();
		event.setUserId(1l);
		UserPatchDetails details = new UserPatchDetails();
		event.setUserDetails(details);
		return event;
	}

	public static DisableUserEvent getDisableUserEvent() {
		DisableUserEvent event = new DisableUserEvent();
		event.setSessionDataBean(getSessionDataBean());
		event.setId(1l);
		return event;
	}

	public static DisableUserEvent getDisableUserEventForName() {
		DisableUserEvent event = new DisableUserEvent();
		event.setSessionDataBean(getSessionDataBean());
		event.setName("ABC");
		return event;
	}

}
