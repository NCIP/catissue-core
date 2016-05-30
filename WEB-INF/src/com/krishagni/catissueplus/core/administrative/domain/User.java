
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

import static com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode.DOMAIN_CHANGE_NOT_ALLOWED;
import static com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode.DOMAIN_NAME_REQUIRED;

@Configurable
@Audited
public class User extends BaseEntity implements UserDetails {
	public static final String SYS_USER = "$system";
	
	public static final String DEFAULT_AUTH_DOMAIN = "openspecimen";
	
	private static final long serialVersionUID = 1L;
	
	private static final String ENTITY_NAME = "user";

	private static final Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})");
	
	private static final int PASSWDS_TO_EXAMINE = 5;
		
	private String lastName;

	private String firstName;

	private AuthDomain authDomain;

	private Set<Site> sites = new HashSet<Site>();

	private String emailAddress;

	private String phoneNumber;

	private String loginName;

	private Date creationDate;

	private String activityStatus;

	private Department department;

	private String address;

	private String comments;
	
	private String password;
	
	private Boolean admin;

	private Boolean manageForms;
	
	private Set<Password> passwords = new HashSet<Password>();
	
	@Autowired 
	private DaoFactory daoFactory;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public static String getEntityName() {
		return ENTITY_NAME;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@NotAudited
	public AuthDomain getAuthDomain() {
		return authDomain;
	}

	@NotAudited
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
	
	public Institute getInstitute() {
		return getDepartment().getInstitute();
	}

	@NotAudited
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@NotAudited
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return admin != null ? admin : false;
	}
	
	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
	
	public boolean canManageForms() {
		return manageForms != null ? manageForms : false;
	}

	public Boolean getManageForms() {
		return manageForms;
	}

	public void setManageForms(Boolean manageForms) {
		this.manageForms = manageForms;
	}

	@NotAudited
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
		return this.activityStatus.equals(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
	}

	public void update(User user) {
		this.setFirstName(user.getFirstName());
		this.setLastName(user.getLastName());
		this.setActivityStatus(user.getActivityStatus());
		this.setAddress(user.getAddress());
		this.setDepartment(user.getDepartment());
		this.setEmailAddress(user.getEmailAddress());
		this.setLoginName(user.getLoginName());
		this.setPhoneNumber(user.getPhoneNumber());
		this.setComments(user.getComments());
		this.setAdmin(user.isAdmin());
		this.setManageForms(user.canManageForms());
	}
	
	public void changePassword(String newPassword) {
		if (StringUtils.isBlank(newPassword) || !isValidPasswordPattern(newPassword)) {
			throw OpenSpecimenException.userError(UserErrorCode.PASSWD_VIOLATES_RULES);
		}
		
		if (isSameAsLastNPassword(newPassword)) {
			throw OpenSpecimenException.userError(UserErrorCode.PASSWD_SAME_AS_LAST_N);
		}
		
		this.password = passwordEncoder.encode(newPassword);
		
		Password password = new Password();
		password.setUpdationDate(new Date());
		password.setUser(this);
		password.setPassword(this.password);
		this.passwords.add(password);
	}
	
	//TODO: need to check few more entities like AQ, Custom form, Distribution order etc.
	public List<DependentEntityDetail> getDependentEntities() {
		return daoFactory.getUserDao().getDependentEntities(getId());
	}
	
	public void delete(boolean close) {
		String activityStatus = Status.ACTIVITY_STATUS_CLOSED.getStatus();
		if (!close) {
			activityStatus = Status.ACTIVITY_STATUS_DISABLED.getStatus();
			List<DependentEntityDetail> dependentEntities = getDependentEntities();
			if (!dependentEntities.isEmpty()) {
				throw OpenSpecimenException.userError(UserErrorCode.REF_ENTITY_FOUND);
			}
			
			setLoginName(Utility.getDisabledValue(getLoginName(), 255));
			setEmailAddress(Utility.getDisabledValue(getEmailAddress(), 255));
		}
		
		setActivityStatus(activityStatus);
	}
	
	public boolean isValidOldPassword(String oldPassword) {
		if (StringUtils.isBlank(oldPassword)) {
			throw OpenSpecimenException.userError(UserErrorCode.OLD_PASSWD_NOT_SPECIFIED);
		}
		
		return passwordEncoder.matches(oldPassword, this.getPassword());
	}
	
	public String formattedName() {
		StringBuilder name = new StringBuilder();
		if (StringUtils.isNotBlank(firstName)) {
			name.append(firstName);
		}

		if (StringUtils.isNotBlank(lastName)) {
			if (name.length() > 0) {
				name.append(" ");
			}

			name.append(lastName);
		}

		return name.toString();
	}

	private boolean isValidPasswordPattern(String password) {
		return pattern.matcher(password).matches();
	}
	
	private boolean isSameAsLastNPassword(String newPassword) {
		boolean isSameAsLastN = false;
		List<Password> passwords = new ArrayList<Password>(this.getPasswords());
		Collections.sort(passwords);
		
		int examined = 0;
		for (Password passwd: passwords) {
			if (examined == PASSWDS_TO_EXAMINE) {
				break;
			}
			
			if(passwordEncoder.matches(newPassword, passwd.getPassword())) {
				isSameAsLastN = true;
				break;
			}
			++examined;
		}
		return isSameAsLastN;
	}
	
}
