
package com.krishagni.catissueplus.core.administrative.domain;

import static com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode.*;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

public class User extends BaseEntity implements UserDetails {
	
	private final static Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})");
	
	private String lastName;

	private String firstName;

	private AuthDomain authDomain;

	private Set<Site> sites = new HashSet<Site>();

	private String emailAddress;

	private String loginName;

	private Date creationDate;

	private String activityStatus;

	private Department department;

	private Address address;

	private String comments;
	
	private String password;

	private Set<Password> passwords = new HashSet<Password>();
	
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

	public Set<Site> getSites() {
		return sites;
	}

	public void setSites(Set<Site> sites) {
		this.sites = sites;
	}

	public void setAuthDomain(AuthDomain authDomain) {
		if (authDomain == null || StringUtils.isBlank(authDomain.getName())) {
			throw OpenSpecimenException.userError(DOMAIN_NAME_REQUIRED);
		}
		
		if (this.getAuthDomain() != null && !this.getAuthDomain().getId().equals(authDomain.getId())) {
			throw OpenSpecimenException.userError(DOMAIN_CHANGE_NOT_ALLOWED);
		}
		
		this.authDomain = authDomain;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		if (StringUtils.isBlank(loginName)) {
			throw OpenSpecimenException.userError(LOGIN_NAME_REQUIRED);
		}
		
		if (StringUtils.isNotBlank(this.getLoginName()) && !this.getLoginName().equals(loginName)) {
			throw OpenSpecimenException.userError(LOGIN_NAME_CHANGE_NOT_ALLOWED);
		}

		this.loginName = loginName;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date createDate) {
		this.creationDate = createDate;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Password> getPasswords() {
		return passwords;
	}

	public void setPasswords(Set<Password> passwords) {
		this.passwords = passwords;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("user"));
		return authorities;
	}

	@Override
	public String getUsername() {
		return loginName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public void update(User user) {
		this.setFirstName(user.getFirstName());
		this.setLastName(user.getLastName());
		this.setActivityStatus(user.getActivityStatus());
		this.setAddress(user.getAddress());
		this.setDepartment(user.getDepartment());
		this.setEmailAddress(user.getEmailAddress());
		this.setComments(user.getComments());
	}

	// TODO: Add password pattern validation here 
	public void addPassword(String newPassword) {
		this.password = newPassword;
		
		Password password = new Password();
		password.setUpdationDate(new Date());
		password.setUser(this);
		password.setPassword(newPassword);
		this.passwords.add(password);
	}

	public static boolean isValidPasswordPattern(String password) {
		return pattern.matcher(password).matches();
	}

	public void delete(boolean isClosed) { 
		if (isClosed) {
			this.setActivityStatus(Status.ACTIVITY_STATUS_CLOSED.getStatus());
		} else {
			this.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
		}
	}
	
}
