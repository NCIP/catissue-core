
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.internet.InternetAddress;

import com.krishagni.catissueplus.core.administrative.domain.Address;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteFactory;
import com.krishagni.catissueplus.core.administrative.events.SiteDetails;
import com.krishagni.catissueplus.core.administrative.events.UserInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class SiteFactoryImpl implements SiteFactory {

	private static final String SITE_NAME = "site name";

	private static final String SITE_TYPE = "site type";

	private static final String USER_NAME = "user name";

	private static final String ZIPCODE = "zipcode";

	private static final String STREET = "street";

	private static final String COUNTRY = "country";

	private static final String CITY = "city";

	private static final String ACTIVITY_STATUS = "activity status";

	private final String EMAIL_ADDRESS = "email address";

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Site createSite(SiteDetails details) {
		Site site = new Site();
		Address address = new Address();
		ObjectCreationException exceptionHandler = new ObjectCreationException();

		setCity(address, details.getCity(), exceptionHandler);
		setCountry(address, details.getCountry(), exceptionHandler);
		setFaxNumber(address, details.getFaxNumber());
		setPhoneNumber(address, details.getPhoneNumber());
		setState(address, details.getState());
		setStreet(address, details.getStreet(), exceptionHandler);
		setZipCode(address, details.getZipCode(), exceptionHandler);

		setName(site, details.getName(), exceptionHandler);
		setSiteAddress(site, address);
		setCoordinatorCollection(site, details.getCoordinatorCollection(), exceptionHandler);
		setType(site, details.getType(), exceptionHandler);
		setActivityStatus(site, details.getActivityStatus(), exceptionHandler);
		setEmailAddress(site, details.getEmailAddress(), exceptionHandler);
		exceptionHandler.checkErrorAndThrow();
		return site;
	}

	private void setEmailAddress(Site site, String email, ObjectCreationException exceptionHandler) {

		if (!isEmailValid(email)) {
			exceptionHandler.addError(SiteErrorCode.INVALID_ATTR_VALUE, EMAIL_ADDRESS);
			return;
		}
		site.setEmailAddress(email);
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

	private void setActivityStatus(Site site, String activityStatus, ObjectCreationException exceptionHandler) {
		if (!CommonValidator.isValidPv(activityStatus, ACTIVITY_STATUS)) {
			exceptionHandler.addError(SiteErrorCode.INVALID_ATTR_VALUE, ACTIVITY_STATUS);
		}
		site.setActivityStatus(activityStatus);
	}

	private void setType(Site site, String siteType, ObjectCreationException exceptionHandler) {

		if (isBlank(siteType)) {
			exceptionHandler.addError(SiteErrorCode.MISSING_ATTR_VALUE, SITE_TYPE);
			return;
		}
		if (!CommonValidator.isValidPv(siteType, SITE_TYPE)) {
			exceptionHandler.addError(SiteErrorCode.INVALID_ATTR_VALUE, SITE_TYPE);
		}
		site.setType(siteType);
	}

	private void setCoordinatorCollection(Site site, List<UserInfo> coordinatorCollection,
			ObjectCreationException exceptionHandler) {
		Set<User> userCollection = new HashSet<User>();
		for (UserInfo userInfo : coordinatorCollection) {

			User user = daoFactory.getUserDao().getUserByLoginNameAndDomainName(userInfo.getLoginName(),
					userInfo.getDomainName());
			if (user == null) {
				exceptionHandler.addError(SiteErrorCode.INVALID_ATTR_VALUE, USER_NAME);
				return;
			}
			userCollection.add(user);
		}

		site.setCoordinatorCollection(userCollection);
	}

	private void setSiteAddress(Site site, Address address) {
		site.setAddress(address);
	}

	private void setName(Site site, String siteName, ObjectCreationException exceptionHandler) {
		if (isBlank(siteName)) {
			exceptionHandler.addError(SiteErrorCode.MISSING_ATTR_VALUE, SITE_NAME);
			return;
		}
		site.setName(siteName);
	}

	@Override
	public Site patchSite(Site site, SiteDetails details) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		if (details.isSiteNameModified()) {
			setName(site, details.getName(), exceptionHandler);
		}

		if (details.isSiteTypeModified()) {
			setType(site, details.getType(), exceptionHandler);
		}

		if (details.isCoordinatorCollectionModified()) {
			setCoordinatorCollection(site, details.getCoordinatorCollection(), exceptionHandler);
		}

		if (details.isActivityStatusModified()) {
			setActivityStatus(site, details.getActivityStatus(), exceptionHandler);
		}

		if (details.isEmailAddressModified()) {
			setEmailAddress(site, details.getEmailAddress(), exceptionHandler);
		}

		if (details.isCountryModified()) {
			setCountry(site.getAddress(), details.getCountry(), exceptionHandler);
		}

		if (details.isStateModified()) {
			setState(site.getAddress(), details.getState());
		}

		if (details.isCityModified()) {
			setCity(site.getAddress(), details.getCity(), exceptionHandler);
		}

		if (details.isFaxNumberModified()) {
			setFaxNumber(site.getAddress(), details.getFaxNumber());
		}

		if (details.isPhoneNumberModified()) {
			setPhoneNumber(site.getAddress(), details.getPhoneNumber());
		}

		if (details.isStreetModified()) {
			setStreet(site.getAddress(), details.getStreet(), exceptionHandler);
		}

		if (details.isZipCodeModified()) {
			setZipCode(site.getAddress(), details.getZipCode(), exceptionHandler);
		}
		exceptionHandler.checkErrorAndThrow();
		return site;
	}

	private void setStreet(Address address, String street, ObjectCreationException exceptionHandler) {
		if (isBlank(street)) {
			exceptionHandler.addError(SiteErrorCode.MISSING_ATTR_VALUE, STREET);
			return;
		}
		address.setStreet(street);
	}

	private void setCity(Address address, String city, ObjectCreationException exceptionHandler) {
		if (isBlank(city)) {
			exceptionHandler.addError(SiteErrorCode.MISSING_ATTR_VALUE, CITY);
			return;
		}
		address.setCity(city);
	}

	private void setState(Address address, String state) {
		address.setState(state);
	}

	private void setZipCode(Address address, String zipCode, ObjectCreationException exceptionHandler) {
		if (isBlank(zipCode)) {
			exceptionHandler.addError(SiteErrorCode.MISSING_ATTR_VALUE, ZIPCODE);
			return;
		}
		address.setZipCode(zipCode);
	}

	private void setPhoneNumber(Address address, String phoneNumber) {
		address.setPhoneNumber(phoneNumber);
	}

	private void setFaxNumber(Address address, String faxNumber) {
		address.setFaxNumber(faxNumber);
	}

	private void setCountry(Address address, String country, ObjectCreationException exceptionHandler) {
		if (isBlank(country)) {
			exceptionHandler.addError(SiteErrorCode.MISSING_ATTR_VALUE, COUNTRY);
			return;
		}
		if (!CommonValidator.isValidPv(country, COUNTRY)) {
			exceptionHandler.addError(SiteErrorCode.INVALID_ATTR_VALUE, COUNTRY);
		}

		address.setCountry(country);
	}

}
