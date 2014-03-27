
package com.krishagni.catissueplus.core.administrative.services.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.UUID;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserFactory;
import com.krishagni.catissueplus.core.administrative.events.CloseUserEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.ForgotPasswordEvent;
import com.krishagni.catissueplus.core.administrative.events.PasswordDetails;
import com.krishagni.catissueplus.core.administrative.events.PasswordForgottenEvent;
import com.krishagni.catissueplus.core.administrative.events.PasswordUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdatePasswordEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UserClosedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.administrative.events.UserUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.errors.ObjectUpdationException;

import edu.wustl.catissuecore.actionForm.UserForm;

public class UserServiceImpl implements UserService {

	private DaoFactory daoFactory;

	private UserFactory userFactory;

	private ObjectCreationException exceptionHandler = new ObjectUpdationException();

	private final String LOGIN_NAME = "login name";

	private final String EMAIL_ADDRESS = "email address";

	private final String PASSWORD_TOKEN = "password token";

	private final String OLD_PASSWORD = "old password";

	private final String CONFIRM_PASSWORD = "confirm password";

	private final String NEW_PASSWORD = "new password";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setUserFactory(UserFactory userFactory) {
		this.userFactory = userFactory;
	}

	@Override
	@PlusTransactional
	public UserCreatedEvent createUser(CreateUserEvent event) {
		try {
			User user = userFactory.createUser(event.getUserDetails());
			ensureUniqueLoginName(user.getLoginName());
			ensureUniqueEmailAddress(user.getEmailAddress());
			exceptionHandler.checkErrorAndThrow();
			user.setPasswordToken(UUID.randomUUID().toString());
			daoFactory.getUserDao().saveOrUpdate(user);
			return UserCreatedEvent.ok(UserDetails.fromDomain(user));
		}
		catch (ObjectCreationException ce) {
			return UserCreatedEvent
					.invalidRequest(UserErrorCode.ERROR_WHILE_USER_CREATION.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return UserCreatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public UserUpdatedEvent updateUser(UpdateUserEvent event) {
		try {
			Long userId = event.getUserDetails().getId();
			User oldUser = daoFactory.getUserDao().getUser(userId);
			if (oldUser == null) {
				return UserUpdatedEvent.notFound(userId);
			}
			User user = userFactory.createUser(event.getUserDetails());
			validateFields(oldUser, user);
			exceptionHandler.checkErrorAndThrow();

			oldUser.update(user);
			daoFactory.getUserDao().saveOrUpdate(oldUser);
			return UserUpdatedEvent.ok(UserDetails.fromDomain(oldUser));

		}
		catch (ObjectUpdationException ce) {
			return UserUpdatedEvent
					.invalidRequest(UserErrorCode.ERROR_WHILE_USER_UPDATION.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return UserUpdatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public UserClosedEvent closeUser(CloseUserEvent event) {
		try {
			User oldUser = daoFactory.getUserDao().getUser(event.getId());
			if (oldUser == null) {
				return UserClosedEvent.notFound(event.getId());
			}
			oldUser.close();
			daoFactory.getUserDao().saveOrUpdate(oldUser);
			return UserClosedEvent.ok();
		}
		catch (Exception e) {
			return UserClosedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public PasswordUpdatedEvent setPassword(UpdatePasswordEvent event) {
		try {
			Long userId = event.getPasswordDetails().getId();
			User user = daoFactory.getUserDao().getUser(userId);
			if (user == null) {
				return PasswordUpdatedEvent.notFound(userId);
			}

			validateTerms(event, user);
			user.updatePassword(event.getPasswordDetails());
			daoFactory.getUserDao().saveOrUpdate(user);
			return PasswordUpdatedEvent.ok(event.getPasswordDetails());
		}
		catch (CatissueException ce) {
			return PasswordUpdatedEvent.invalidRequest(ce.getMessage());
		}
		catch (Exception e) {
			return PasswordUpdatedEvent.serverError(e);
		}
	}

	@Override
	public PasswordUpdatedEvent resetPassword(UpdatePasswordEvent event) {
		try {
			Long userId = event.getPasswordDetails().getId();
			User user = daoFactory.getUserDao().getUser(userId);
			if (user == null) {
				return PasswordUpdatedEvent.notFound(userId);
			}

			validateTerms(event, user);
			validateOldPassword(event.getPasswordDetails());

			user.updatePassword(event.getPasswordDetails());
			daoFactory.getUserDao().saveOrUpdate(user);
			return PasswordUpdatedEvent.ok(event.getPasswordDetails());
		}
		catch (CatissueException ce) {
			return PasswordUpdatedEvent.invalidRequest(ce.getMessage());
		}
		catch (Exception e) {
			return PasswordUpdatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public PasswordForgottenEvent forgotPassword(ForgotPasswordEvent event) {
		try {
			Long userId = event.getId();
			User user = daoFactory.getUserDao().getUser(userId);
			if (user == null) {
				return PasswordForgottenEvent.notFound(userId);
			}
			user.setPasswordToken(UUID.randomUUID().toString());
			daoFactory.getUserDao().saveOrUpdate(user);
			return PasswordForgottenEvent.ok(UserDetails.fromDomain(user));
		}
		catch (Exception e) {
			return PasswordForgottenEvent.serverError(e);
		}
	}

	private void validateOldPassword(PasswordDetails passwordDetails) {
		if (isBlank(passwordDetails.getOldPassword())) {
			reportError(UserErrorCode.INVALID_ATTR_VALUE, OLD_PASSWORD);
		}

		if (!daoFactory.getUserDao().isValidOldPassword(passwordDetails.getOldPassword(), passwordDetails.getId())) {
			reportError(UserErrorCode.INVALID_ATTR_VALUE, OLD_PASSWORD);
		}
	}

	private void validateTerms(UpdatePasswordEvent event, User user) {
		if (isBlank(event.getPasswordDetails().getNewPassword())) {
			reportError(UserErrorCode.INVALID_ATTR_VALUE, NEW_PASSWORD);
		}

		if (isBlank(event.getPasswordDetails().getConfirmPassword())) {
			reportError(UserErrorCode.INVALID_ATTR_VALUE, CONFIRM_PASSWORD);
		}

		if (!event.getPasswordDetails().getNewPassword().equals(event.getPasswordDetails().getConfirmPassword())) {
			reportError(UserErrorCode.INVALID_ATTR_VALUE, NEW_PASSWORD);
		}

		if (!user.getPasswordToken().equals(event.getPasswordToken())) {
			reportError(UserErrorCode.INVALID_ATTR_VALUE, PASSWORD_TOKEN);
		}
	}

	private void ensureUniqueLoginName(String loginName) {
		if (!daoFactory.getUserDao().isUniqueLoginName(loginName)) {
			exceptionHandler.addError(UserErrorCode.DUPLICATE_LOGIN_NAME, LOGIN_NAME);
		}
	}

	private void validateFields(User oldUser, User newUser) {
		if (!oldUser.getLoginName().equals(newUser.getLoginName())) {
			exceptionHandler.addError(UserErrorCode.CHANGE_IN_LOGIN_NAME, LOGIN_NAME);
		}
		else if (!oldUser.getEmailAddress().equals(newUser.getEmailAddress())) {
			ensureUniqueEmailAddress(newUser.getEmailAddress());
		}
	}

	private void ensureUniqueEmailAddress(String emailAddress) {
		if (!daoFactory.getUserDao().isUniqueEmailAddress(emailAddress)) {
			exceptionHandler.addError(UserErrorCode.DUPLICATE_EMAIL_ADDRESS, EMAIL_ADDRESS);
		}
	}

}
