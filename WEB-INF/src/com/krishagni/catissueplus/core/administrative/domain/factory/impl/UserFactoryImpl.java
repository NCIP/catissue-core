
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Address;
import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserFactory;
import com.krishagni.catissueplus.core.administrative.events.UserCPRoleDetails;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.administrative.events.UserPatchDetails;
import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.SetUpdater;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.privileges.domain.Role;
import com.krishagni.catissueplus.core.privileges.domain.UserCPRole;

public class UserFactoryImpl implements UserFactory {

	private DaoFactory daoFactory;

	private final String FIRST_NAME = "first name";

	private final String LAST_NAME = "last name";

	private final String LOGIN_NAME = "login name";

	private final String AUTH_DOMAIN = "auth domain";

	private final String EMAIL_ADDRESS = "email address";

	private final String COUNTRY = "country";

	private final String SITE = "site";

	private static final String ROLE = "role";

	private static final String DEPARTMENT = "department";

	private final String COLLECTION_PROTOCOL = "collection protocol";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public User createUser(UserDetails details) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();

		User user = new User();
		setComments(user, details.getComments());
		setUserSites(user, details.getUserSiteNames(), exceptionHandler);
		setUserCPRoles(user, details.getUserCPRoles(), exceptionHandler);
		setLoginName(user, details.getLoginName(), exceptionHandler);
		setFirstName(user, details.getFirstName(), exceptionHandler);
		setLastName(user, details.getLastName(), exceptionHandler);
		setActivityStatus(user, details.getActivityStatus(), exceptionHandler);
		setAddress(user, details, exceptionHandler);
		setEmailAddress(user, details.getEmailAddress(), exceptionHandler);
		setDepartment(user, details.getDeptName(), exceptionHandler);
		setAuthDomain(user, details.getDomainName(), exceptionHandler);

		exceptionHandler.checkErrorAndThrow();
		return user;
	}

	@Override
	public User patchUser(User user, UserPatchDetails details) {
		ObjectCreationException exception = new ObjectCreationException();
		if (details.isFirstNameModified()) {
			setFirstName(user, details.getFirstName(), exception);
		}

		if (details.isLastNameModified()) {
			setLastName(user, details.getLastName(), exception);
		}

		if (details.isUserSitesModified()) {
			setUserSites(user, details.getUserSiteNames(), exception);
		}

		if (details.isUserCPRolesModified()) {
			setUserCPRoles(user, details.getUserCPRoles(), exception);
		}

		if (details.isActivityStatusModified()) {
			setActivityStatus(user, details.getActivityStatus(), exception);
		}

		if (details.isCommentsModified()) {
			setComments(user, details.getComments());
		}

		if (details.isEmailAddressModified()) {
			setEmailAddress(user, details.getEmailAddress(), exception);
		}

		if (details.isDeptNameModified()) {
			setDepartment(user, details.getDeptName(), exception);
		}

		if (details.isCountryModified()) {
			setCountry(user.getAddress(), details.getCountry(), exception);
		}

		if (details.isStateModified()) {
			setState(user.getAddress(), details.getState());
		}

		if (details.isCityModified()) {
			setCity(user.getAddress(), details.getCity());
		}

		if (details.isFaxNumberModified()) {
			setFaxNumber(user.getAddress(), details.getFaxNumber());
		}

		if (details.isPhoneNumberModified()) {
			setPhoneNumber(user.getAddress(), details.getPhoneNumber());
		}

		if (details.isStreetModified()) {
			setStreet(user.getAddress(), details.getStreet());
		}

		if (details.isZipCodeModified()) {
			setZipCode(user.getAddress(), details.getZipCode());
		}
		exception.checkErrorAndThrow();
		return user;
	}

	private void setUserCPRoles(User user, List<UserCPRoleDetails> userCPRoleDetails,
			ObjectCreationException exceptionHandler) {
		if (userCPRoleDetails.isEmpty()) {
			return;
		}

		Set<UserCPRole> userCpRoles = new HashSet<UserCPRole>();
		for (UserCPRoleDetails ucrDetails : userCPRoleDetails) {
			UserCPRole userCpRole = new UserCPRole();
			CollectionProtocol collectionProtocol = daoFactory.getCollectionProtocolDao().getCPByTitle(
					ucrDetails.getCpTitle());
			if (collectionProtocol == null) {
				exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, COLLECTION_PROTOCOL);
				return;
			}
			userCpRole.setCollectionProtocol(collectionProtocol);
			userCpRole.setUser(user);

			Role role = daoFactory.getRoleDao().getRoleByName(ucrDetails.getRoleName());
			if (role == null) {
				exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, ROLE);
				return;
			}
			userCpRole.setId(ucrDetails.getId());
			userCpRole.setRole(role);
			userCpRoles.add(userCpRole);
		}
		SetUpdater.<UserCPRole> newInstance().update(user.getUserCPRoles(), userCpRoles);
	}

	private void setUserSites(User user, List<String> userSiteNames, ObjectCreationException exceptionHandler) {
		if (userSiteNames.isEmpty()) {
			return;
		}
		Set<Site> userSites = new HashSet<Site>();
		for (String siteName : userSiteNames) {

			Site site = daoFactory.getSiteDao().getSite(siteName);
			if (site == null) {
				exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, SITE);
				return;
			}

			userSites.add(site);
		}
		SetUpdater.<Site> newInstance().update(user.getUserSites(), userSites);
	}

	private void setComments(User user, String comments) {
		user.setComments(comments);
	}

	private void setFirstName(User user, String firstName, ObjectCreationException exceptionHandler) {
		if (isBlank(firstName)) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, FIRST_NAME);
			return; 
		}
		user.setFirstName(firstName);
	}

	private void setLastName(User user, String lastName, ObjectCreationException exceptionHandler) {
		if (isBlank(lastName)) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, LAST_NAME);
			return;
		}
		user.setLastName(lastName);
	}

	private void setLoginName(User user, String loginName, ObjectCreationException exceptionHandler) {
		if (isBlank(loginName)) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, LOGIN_NAME);
			return;
		}
		user.setLoginName(loginName);
	}

	private void setDepartment(User user, String departmentName, ObjectCreationException exceptionHandler) {
		Department department = daoFactory.getDepartmentDao().getDepartmentByName(departmentName);

		if (department == null) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, DEPARTMENT);
			return;
		}
		user.setDepartment(department);
	}

	private void setAddress(User user, UserDetails details, ObjectCreationException exceptionHandler) {
		Address address = new Address();
		setStreet(address, details.getStreet());
		setCountry(address, details.getCountry(), exceptionHandler);
		setFaxNumber(address, details.getFaxNumber());
		setPhoneNumber(address, details.getPhoneNumber());
		setZipCode(address, details.getZipCode());
		setState(address, details.getState());
		setCity(address, details.getCity());
		user.setAddress(address);
	}

	private void setCity(Address address, String city) {
		address.setCity(address.getCity());
	}

	private void setState(Address address, String state) {
		address.setState(state);
	}

	private void setZipCode(Address address, String zipCode) {
		address.setZipCode(zipCode);
	}

	private void setPhoneNumber(Address address, String phoneNumber) {
		address.setPhoneNumber(phoneNumber);
	}

	private void setFaxNumber(Address address, String faxNumber) {
		address.setFaxNumber(faxNumber);
	}

	private void setCountry(Address address, String country, ObjectCreationException exceptionHandler) {
		if (!CommonValidator.isValidPv(country, COUNTRY)) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, COUNTRY);
		}
		address.setCountry(country);
	}

	private void setStreet(Address address, String street) {
		address.setStreet(street);
	}

	private void setActivityStatus(User user, String activityStatus, ObjectCreationException exceptionHandler) {
		user.setActivityStatus(activityStatus);
	}

	private void setEmailAddress(User user, String email, ObjectCreationException exceptionHandler) {
		if (isBlank(email)) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, EMAIL_ADDRESS);
			return;
		}

		if (!CommonValidator.isEmailValid(email)) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, EMAIL_ADDRESS);
			return;
		}
		user.setEmailAddress(email);
	}

	private void setAuthDomain(User user, String domainName, ObjectCreationException exceptionHandler) {
		if (isBlank(domainName)) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, AUTH_DOMAIN);
			return;
		}

		AuthDomain authDomain = daoFactory.getDomainDao().getAuthDomainByName(domainName);
		if (authDomain == null) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, AUTH_DOMAIN);
			return;
		}
		user.setAuthDomain(authDomain);
	}
}
