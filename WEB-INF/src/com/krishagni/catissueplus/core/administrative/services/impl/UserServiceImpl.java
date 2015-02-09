
package com.krishagni.catissueplus.core.administrative.services.impl;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.krishagni.catissueplus.core.administrative.domain.ForgotPasswordToken;
import com.krishagni.catissueplus.core.administrative.domain.Password;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserFactory;
import com.krishagni.catissueplus.core.administrative.events.AllUsersEvent;
import com.krishagni.catissueplus.core.administrative.events.CloseUserEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableUserEvent;
import com.krishagni.catissueplus.core.administrative.events.ForgotPasswordEvent;
import com.krishagni.catissueplus.core.administrative.events.GetUserEvent;
import com.krishagni.catissueplus.core.administrative.events.PasswordDetails;
import com.krishagni.catissueplus.core.administrative.events.PasswordForgottenEvent;
import com.krishagni.catissueplus.core.administrative.events.PasswordUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.PasswordValidatedEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchUserEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllUsersEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdatePasswordEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UserClosedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.administrative.events.UserDisabledEvent;
import com.krishagni.catissueplus.core.administrative.events.UserUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ValidatePasswordEvent;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.email.EmailSender;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.services.SavedQueryErrorCode;

public class UserServiceImpl implements UserService {

	private DaoFactory daoFactory;

	private UserFactory userFactory;
	
	private BCryptPasswordEncoder passwordEncoder;
	
	private EmailSender emailSender;

	private final String LOGIN_NAME = "login name";

	private final String EMAIL_ADDRESS = "email address";

	private final String CATISSUE = "catissue";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setUserFactory(UserFactory userFactory) {
		this.userFactory = userFactory;
	}

	public void setEmailSender(EmailSender emailSender) {
		this.emailSender = emailSender;
	}

	public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@PlusTransactional
	public AllUsersEvent getAllUsers(ReqAllUsersEvent req) {
		
		if (req.getStartAt() < 0 || req.getMaxRecords() <= 0) {
			String msg = SavedQueryErrorCode.INVALID_PAGINATION_FILTER.message();
			return AllUsersEvent.badRequest(msg, null);
		}

		List<UserSummary> users = daoFactory.getUserDao().getAllUsers(
						req.getStartAt(), req.getMaxRecords(),
						req.getSortBy(), req.getSearchString());
		
		Long count = null;
		if (req.isCountReq()) {
			count = daoFactory.getUserDao().getUsersCount(req.getSearchString());
		}

		return AllUsersEvent.ok(users,count);
	}

	@Override
	@PlusTransactional
	public UserCreatedEvent createUser(CreateUserEvent event) {
		try {
			User user = userFactory.createUser(event.getUserDetails());
			ObjectCreationException exceptionHandler = new ObjectCreationException();
			ensureUniqueLoginNameInDomain(user.getLoginName(), user.getAuthDomain().getName(), exceptionHandler);
			ensureUniqueEmailAddress(user.getEmailAddress(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();

			daoFactory.getUserDao().saveOrUpdate(user);
			emailSender.sendUserCreatedEmail(user);
			return UserCreatedEvent.ok(UserDetails.fromDomain(user));
		}
		catch (ObjectCreationException ce) {
			return UserCreatedEvent.invalidRequest(UserErrorCode.ERRORS.message(), ce.getErroneousFields());
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

			ObjectCreationException exceptionHandler = new ObjectCreationException();
			validateChangeInUniqueEmail(oldUser, user, exceptionHandler);
			exceptionHandler.checkErrorAndThrow();

			oldUser.update(user);
			daoFactory.getUserDao().saveOrUpdate(oldUser);
			return UserUpdatedEvent.ok(UserDetails.fromDomain(oldUser));
		}
		catch (ObjectCreationException ce) {
			return UserUpdatedEvent.invalidRequest(UserErrorCode.ERRORS.message(), ce.getErroneousFields());
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
	public UserDisabledEvent deleteUser(DisableUserEvent event) {
		try {
			User user =  null;
			if(event.getName() != null) {	
				user =	daoFactory.getUserDao().getUserByLoginNameAndDomainName(event.getName(), CATISSUE);
				if (user == null) {
					return UserDisabledEvent.notFound(event.getName());
				}
			}else {
				user =	daoFactory.getUserDao().getUser(event.getId());
			if (user == null) {
				return UserDisabledEvent.notFound(event.getId());
			}}
			user.delete();
			daoFactory.getUserDao().saveOrUpdate(user);
			return UserDisabledEvent.ok();
		}
		catch (Exception e) {
			return UserDisabledEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public PasswordUpdatedEvent changePassword(UpdatePasswordEvent event) {
		try {
			PasswordDetails detail = event.getPasswordDetails();
			User user = daoFactory.getUserDao().getUserByLoginNameAndDomainName(detail.getLoginName(), CATISSUE);
			if (user == null) {
				return PasswordUpdatedEvent.notFound();
			}
			
			if (!validateOldPassword(user, detail.getOldPassword())) {
				reportError(UserErrorCode.INVALID_ATTR_VALUE, "old password");
			}
			
			setUserPassword(user, detail.getNewPassword());
			return PasswordUpdatedEvent.ok();
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
	public PasswordUpdatedEvent resetPassword(UpdatePasswordEvent event) {
		try {
			UserDao dao = daoFactory.getUserDao();
			PasswordDetails detail = event.getPasswordDetails();
			if (StringUtils.isEmpty(detail.getResetPasswordToken())) {
				reportError(UserErrorCode.INVALID_ATTR_VALUE, "Reset password token");
			}
			
			ForgotPasswordToken token = dao.getTokenByToken(detail.getResetPasswordToken());
			if (token == null) {
				return PasswordUpdatedEvent.notFound();
			}
			
			User user = token.getUser();
			if (!user.getLoginName().equals(detail.getLoginName())) {
				reportError(UserErrorCode.INVALID_ATTR_VALUE, "Login Name");
			}
			
			if (token.hasExpired()) {
				dao.deleteForgotPasswordToken(token);
				return PasswordUpdatedEvent.notFound();
			}
			
			setUserPassword(user, detail.getNewPassword());
			dao.deleteForgotPasswordToken(token);
			return PasswordUpdatedEvent.ok();
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
			UserDao dao = daoFactory.getUserDao();
			User user = dao.getUserByLoginNameAndDomainName(event.getLoginName(), CATISSUE);
			if (user == null) {
				return PasswordForgottenEvent.notFound();
			}
			
			ForgotPasswordToken oldToken = dao.getTokenByUser(user.getId());
			if (oldToken != null) {
				dao.deleteForgotPasswordToken(oldToken);
			}
			
			ForgotPasswordToken token = new ForgotPasswordToken(user);
			dao.saveForgotPasswordToken(token);
			emailSender.sendForgotPasswordEmail(user);
			return PasswordForgottenEvent.ok();
		}
		catch (Exception e) {
			return PasswordForgottenEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public GetUserEvent getUser(Long userId) {
		User user = daoFactory.getUserDao().getUser(userId);
		UserDetails userDetails = UserDetails.fromDomain(user);
		return GetUserEvent.ok(userDetails);
	}

	@Override
	public PasswordValidatedEvent validatePassword(ValidatePasswordEvent event) {
		Boolean isValid = User.isValidPasswordPattern(event.getPassword());
		return PasswordValidatedEvent.ok(isValid);
	}

	@Override
	@PlusTransactional
	public UserUpdatedEvent patchUser(PatchUserEvent event) {
		try {
			Long userId = event.getUserId();
			User oldUser = daoFactory.getUserDao().getUser(userId);
			if (oldUser == null) {
				return UserUpdatedEvent.notFound(userId);
			}
			User user = userFactory.patchUser(oldUser, event.getUserDetails());

			if (event.getUserDetails().isEmailAddressModified()) {
				ObjectCreationException exceptionHandler = new ObjectCreationException();
				ensureUniqueEmailAddress(user.getEmailAddress(), exceptionHandler);
				exceptionHandler.checkErrorAndThrow();
			}

			daoFactory.getUserDao().saveOrUpdate(user);
			return UserUpdatedEvent.ok(UserDetails.fromDomain(user));
		}
		catch (ObjectCreationException ce) {
			return UserUpdatedEvent.invalidRequest(UserErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return UserUpdatedEvent.serverError(e);
		}
	}

	private void ensureUniqueEmailAddress(String emailAddress, ObjectCreationException exceptionHandler) {
		if (!daoFactory.getUserDao().isUniqueEmailAddress(emailAddress)) {
			exceptionHandler.addError(UserErrorCode.DUPLICATE_EMAIL, EMAIL_ADDRESS);
		}
	}

	private void ensureUniqueLoginNameInDomain(String loginName, String domainName,
			ObjectCreationException exceptionHandler) {
		if (!daoFactory.getUserDao().isUniqueLoginNameInDomain(loginName, domainName)) {
			exceptionHandler.addError(UserErrorCode.DUPLICATE_LOGIN_NAME, LOGIN_NAME);
		}
	}

	private void validateChangeInUniqueEmail(User oldUser, User newUser, ObjectCreationException exceptionHandler) {
		if (!oldUser.getEmailAddress().equals(newUser.getEmailAddress())) {
			ensureUniqueEmailAddress(newUser.getEmailAddress(), exceptionHandler);
		}
	}
	
	private void setUserPassword(User user, String newPassword) {
		if (!User.isValidPasswordPattern(newPassword)) {
			reportError(UserErrorCode.INVALID_ATTR_VALUE, "New Password");
		}
		
		user.addPassword(passwordEncoder.encode(newPassword));
	}
	
	public boolean validateOldPassword(User user, String oldPassword) {
		List<Password> passwords = new ArrayList<Password>(user.getPasswordCollection());
		Collections.sort(passwords);
		
		if (!passwords.isEmpty()) {
			return passwordEncoder.matches(oldPassword, passwords.get(passwords.size() - 1).getPassword());
		}
		
		return false;
	}

}