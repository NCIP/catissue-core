
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.internet.InternetAddress;

import com.krishagni.catissueplus.core.administrative.domain.Address;
import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserFactory;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class UserFactoryImpl implements UserFactory {

	private DaoFactory daoFactory;

	private final String FIRST_NAME = "first name";

	private final String LAST_NAME = "last name";

	private final String LOGIN_NAME = "login name";

	private final String DEPARTMENT = "department";

	private final String AUTH_DOMAIN = "auth domain";

	private final String EMAIL_ADDRESS = "email address";

	private final String COUNTRY = "country";

	private final String SITE = "site";

	private final String COLLECTION_PROTOCOL = "collection protocol";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public User createUser(UserDetails details) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		
		User user = new User();
		setComments(user, details.getComments());
		setSites(user, details.getSiteNames(), exceptionHandler);
		setCPs(user, details.getCpTitles(), exceptionHandler);
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
	public User patchUser(User user, UserDetails details) {
		ObjectCreationException exception = new ObjectCreationException();
		if (details.isFirstNameModified()) {
			setFirstName(user, details.getFirstName(), exception);
		}

		if (details.isLastNameModified()) {
			setLastName(user, details.getLastName(), exception);
		}

		if (details.isSitesModified()) {
			setSites(user, details.getSiteNames(), exception);
		}

		if (details.isCpsModified()) {
			setCPs(user, details.getCpTitles(), exception);
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

	private void setCPs(User user, List<String> cpTitles, ObjectCreationException exceptionHandler) {
		Set<CollectionProtocol> cpCollection = new HashSet<CollectionProtocol>();
		for (String cpTitle : cpTitles) {
			CollectionProtocol collectionProtocol = daoFactory.getCollectionProtocolDao().getCPByTitle(cpTitle);
			if (collectionProtocol == null) {
				exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, COLLECTION_PROTOCOL);
				return;
			}
			cpCollection.add(collectionProtocol);
		}
		user.setCpCollection(cpCollection);
	}

	private void setSites(User user, List<String> siteNames, ObjectCreationException exceptionHandler) {
		Set<Site> siteCollection = new HashSet<Site>();
		for (String siteName : siteNames) {
			Site site = daoFactory.getSiteDao().getSiteByName(siteName);
			if (site == null) {
				exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, SITE);
				return;
			}
			siteCollection.add(site);
		}
		user.setSiteCollection(siteCollection);
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
		Department department = daoFactory.getDepartmentDao().getDepartment(departmentName);

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

		if (!isEmailValid(email)) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, EMAIL_ADDRESS);
			return;
		}
		user.setEmailAddress(email);
	}

	private boolean isEmailValid(String emailAddress) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(emailAddress);
			emailAddr.validate();
		}
		catch (Exception exp) {
			result = false;
		}
		return result;
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
