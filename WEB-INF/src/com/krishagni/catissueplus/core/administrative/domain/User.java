
package com.krishagni.catissueplus.core.administrative.domain;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.PasswordDetails;
import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.common.SetUpdater;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.privileges.domain.UserCPRole;

import edu.wustl.common.util.XMLPropertyHandler;

public class User {

	private Long id;

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

	private String passwordToken;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getPasswordToken() {
		return passwordToken;
	}

	public void setPasswordToken(String passwordToken) {
		this.passwordToken = passwordToken;
	}

	public Set<UserCPRole> getUserCPRoles() {
		return userCPRoles;
	}

	public void setUserCPRoles(Set<UserCPRole> userCPRoles) {
		this.userCPRoles = userCPRoles;
	}

	private final String LOGIN_NAME = "login name";

	private final String LDAP = "ldap";

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
		updateAddressDetails(this.getAddress(), user.getAddress());
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

	@Override
	public int hashCode() {
		return 31 * 1 + ((id == null) ? 0 : id.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		User other = (User) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		}
		else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	private final static String CATISSUE = "catissue";

	private final static String OLD_PASSWORD = "old password";

	private final static String PASSWORD_TOKEN = "password token";

	private static final String PASSWORD = "password";

	public void changePassword(PasswordDetails passwordDetails) {
		validateOldPassword(passwordDetails.getOldPassword());
		updatePassword(passwordDetails.getNewPassword());
	}

	public void setPassword(PasswordDetails passwordDetails, String token) {
		validatePasswordToken(token);
		updatePassword(passwordDetails.getNewPassword());
	}

	private void updatePassword(String newPassword) {
		Password password = new Password();
		if (isBlank(newPassword) || !isValidPasswordPattern(newPassword)) {
			reportError(UserErrorCode.INVALID_ATTR_VALUE, PASSWORD);
		}
		password.setUpdateDate(new Date());
		password.setUser(this);
		this.setPasswordToken(null);
		password.setPassword(BCrypt.hashpw(newPassword,
				BCrypt.gensalt(Integer.parseInt(XMLPropertyHandler.getValue("bcrypt.salt.windings")))));

		this.passwordCollection.add(password);
	}

	public void setPasswordToken(User user, String domainName) {
		if (CATISSUE.equalsIgnoreCase(domainName)) {
			user.setPasswordToken(UUID.randomUUID().toString());
		}
	}

	private final static String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})";

	public static boolean isValidPasswordPattern(String password) {
		boolean result = false;
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher mat = pattern.matcher(password);
		result = mat.matches();
		return result;
	}

	private void validateOldPassword(String oldPassword) {
		if (isBlank(oldPassword)) {
			reportError(UserErrorCode.INVALID_ATTR_VALUE, OLD_PASSWORD);
		}

		Set<Password> passwords = this.passwordCollection;
		if (!passwords.isEmpty()) {
			List<Password> passList = new ArrayList<Password>(passwords);
			Password lastPassword = passList.get(0);

			if (!BCrypt.checkpw(oldPassword, lastPassword.getPassword())) {
				reportError(UserErrorCode.INVALID_ATTR_VALUE, OLD_PASSWORD);
			}
		}
	}

	private void validatePasswordToken(String token) {
		if (isBlank(token)) {
			reportError(UserErrorCode.INVALID_ATTR_VALUE, PASSWORD_TOKEN);
		}

		if (!this.getPasswordToken().equals(token)) {
			reportError(UserErrorCode.INVALID_ATTR_VALUE, PASSWORD_TOKEN);
		}
	}

	public void delete() {
		this.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}

}
