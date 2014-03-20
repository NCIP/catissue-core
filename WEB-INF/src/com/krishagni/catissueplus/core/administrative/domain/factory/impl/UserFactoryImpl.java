
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;

import com.krishagni.catissueplus.core.administrative.domain.Address;
import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserFactory;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class UserFactoryImpl implements UserFactory {

	private DaoFactory daoFactory;

	private final String FIRST_NAME = "first name";

	private final String LAST_NAME = "last name";

	private final String LOGIN_NAME = "login name";

	private final String DEPARTMENT = "department";

	private final String EMAIL_ADDRESS = "email address";

	private final String COUNTRY = "country";

	private List<ErroneousField> erroneousFields = new ArrayList<ErroneousField>();

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public User createUser(UserDetails details, ObjectCreationException exceptionHandler) {
		User user = new User();
		user.setLdapId(details.getLdapId());
		user.setComments(details.getComments());

		setLoginName(user, details);
		setName(user, details);
		setActivityStatus(user, details);
		setAddress(user, details);
		setEmailAddress(user, details.getEmailAddress());
		setDepartment(user, details);
		exceptionHandler.addError(erroneousFields);
		return user;
	}

	private void setName(User user, UserDetails details) {
		if (isBlank(details.getFirstName())) {
			addError(UserErrorCode.INVALID_ATTR_VALUE, FIRST_NAME);
		}
		user.setFirstName(details.getFirstName());

		if (isBlank(details.getLastName())) {
			addError(UserErrorCode.INVALID_ATTR_VALUE, LAST_NAME);
		}
		user.setLastName(details.getLastName());
	}

	private void setLoginName(User user, UserDetails details) {
		if (isBlank(details.getLoginName())) {
			addError(UserErrorCode.INVALID_ATTR_VALUE, LOGIN_NAME);
		}
		user.setLoginName(details.getLoginName());
	}

	private void setDepartment(User user, UserDetails details) {
		Department department = daoFactory.getDepartmentDao().getDepartment(details.getDeptName());

		if (department == null) {
			addError(UserErrorCode.NOT_FOUND, DEPARTMENT);
		}
		user.setDepartment(department);
	}

	private void setAddress(User user, UserDetails details) {
		Address address = new Address();
		address.setStreet(details.getStreet());
		address.setCountry(details.getCountry());
		address.setFaxNumber(details.getFaxNumber());
		address.setPhoneNumber(details.getPhoneNumber());
		address.setZipCode(details.getZipCode());
		address.setState(details.getState());
		address.setCity(details.getCity());
		user.setAddress(address);
		checkForPVs(details);
	}

	private void setActivityStatus(User user, UserDetails details) {
		user.setActivityStatus(details.getActivityStatus());
	}

	private void checkForPVs(UserDetails details) {
		if (!CommonValidator.isValidPv(details.getCountry(), COUNTRY)) {
			addError(UserErrorCode.INVALID_ATTR_VALUE, COUNTRY);
		}
	}

	private void setEmailAddress(User user, String email) {
		if (isBlank(email)) {
			addError(UserErrorCode.INVALID_ATTR_VALUE, EMAIL_ADDRESS);
		}

		if (!isEmailValid(email)) {
			addError(ParticipantErrorCode.INVALID_ATTR_VALUE, EMAIL_ADDRESS);
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

	private void addError(CatissueErrorCode event, String field) {
		erroneousFields.add(new ErroneousField(event, field));
	}
}
