
package com.krishagni.catissueplus.core.administrative.domain;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.SetUpdater;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.privileges.domain.UserCPRole;

@Configurable
public class User extends BaseEntity {

	private String lastName;

	private String firstName;

	private AuthDomain authDomain;

	private Set<Site> userSites = new HashSet<Site>();

	private Set<UserCPRole> userCPRoles = new HashSet<UserCPRole>();

	private String emailAddress;

	private String loginName;

	private Date createDate;

	private String activityStatus;

	private Department department;

	private Address address;

	private String comments;

	private Set<Password> passwordCollection = new HashSet<Password>();
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public AuthDomain getAuthDomain() {
		return authDomain;
	}

	public Set<Site> getUserSites() {
		return userSites;
	}

	public void setUserSites(Set<Site> userSites) {
		this.userSites = userSites;
	}

	public void setAuthDomain(AuthDomain authDomain) {
		if (this.getAuthDomain() != null && !this.getAuthDomain().getId().equals(authDomain.getId())) {
			reportError(UserErrorCode.CHANGE_IN_DOMAIN, LDAP);
		}
		this.authDomain = authDomain;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		if (!isBlank(this.getLoginName()) && !this.getLoginName().equals(loginName)) {
			reportError(UserErrorCode.CHANGE_IN_LOGIN_NAME, LOGIN_NAME);
		}
		this.loginName = loginName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Set<Password> getPasswordCollection() {
		return passwordCollection;
	}

	public void setPasswordCollection(Set<Password> passwordCollection) {
		this.passwordCollection = passwordCollection;
	}

	public Set<UserCPRole> getUserCPRoles() {
		return userCPRoles;
	}

	public void setUserCPRoles(Set<UserCPRole> userCPRoles) {
		this.userCPRoles = userCPRoles;
	}

	public BCryptPasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	private final String LOGIN_NAME = "login name";

	private final String LDAP = "ldap";
	
	private final static String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})";

	public void close() {
		this.setActivityStatus(Status.ACTIVITY_STATUS_CLOSED.getStatus());
	}

	public void update(User user) {
		this.setFirstName(user.getFirstName());
		this.setLastName(user.getLastName());
		this.setActivityStatus(user.getActivityStatus());
		this.setAuthDomain(user.getAuthDomain());
		this.setAddress(user.getAddress());
		this.setLoginName(user.getLoginName());
		this.setCreateDate(user.getCreateDate());
		this.setDepartment(user.getDepartment());
		this.setEmailAddress(user.getEmailAddress());
		this.setComments(user.getComments());
		SetUpdater.<Site> newInstance().update(this.getUserSites(), user.getUserSites());

		for (UserCPRole userCP : user.getUserCPRoles()) {
			userCP.setUser(this);
		}
		SetUpdater.<UserCPRole> newInstance().update(this.getUserCPRoles(), user.getUserCPRoles());
		//updateAddressDetails(this.getAddress(), user.getAddress());
	}

	private void updateAddressDetails(Address oldAddress, Address address) {
		oldAddress.setStreet(address.getStreet());
		oldAddress.setCountry(address.getCountry());
		oldAddress.setFaxNumber(address.getFaxNumber());
		oldAddress.setPhoneNumber(address.getPhoneNumber());
		oldAddress.setState(address.getState());
		oldAddress.setCity(address.getCity());
		oldAddress.setZipCode(address.getZipCode());
	}

	public void addPassword(String newPassword) {
		Password password = new Password();
		password.setUpdateDate(new Date());
		password.setUser(this);
		password.setPassword(passwordEncoder.encode(newPassword));

		this.passwordCollection.add(password);
	}

	public static boolean isValidPasswordPattern(String password) {
		boolean result = false;
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher mat = pattern.matcher(password);
		result = mat.matches();
		return result;
	}

	public boolean validateOldPassword(String oldPassword) {
		Set<Password> passwords = this.passwordCollection;
		if (!passwords.isEmpty()) {
			List<Password> passList = new ArrayList<Password>(passwords);
			Password lastPassword = passList.get(0);
			
			if (passwordEncoder.matches(oldPassword, lastPassword.getPassword())){
				return true;
			}
		}
		
		return false;
	}

	public void delete() {
		this.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}

}
