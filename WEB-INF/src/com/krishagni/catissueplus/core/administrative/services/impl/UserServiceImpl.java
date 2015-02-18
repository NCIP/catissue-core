
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.List;
import java.util.UUID;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserFactory;
import com.krishagni.catissueplus.core.administrative.events.ListUserCriteria;
import com.krishagni.catissueplus.core.administrative.events.PasswordDetails;
import com.krishagni.catissueplus.core.administrative.events.UserDetail;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.email.EmailSender;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.Status;

public class UserServiceImpl implements UserService {

	private DaoFactory daoFactory;

	private UserFactory userFactory;

	private final String CATISSUE = "catissue";

	private EmailSender emailSender;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setUserFactory(UserFactory userFactory) {
		this.userFactory = userFactory;
	}

	public void setEmailSender(EmailSender emailSender) {
		this.emailSender = emailSender;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<UserSummary>> getAllUsers(RequestEvent<ListUserCriteria> req) {
		ListUserCriteria crit = req.getPayload();		
		List<UserSummary> users = daoFactory.getUserDao().getUsers(crit.startAt(), crit.maxResults(), crit.query());		
		return ResponseEvent.response(users);
	}

	@Override
	@PlusTransactional
	public ResponseEvent<UserDetail> createUser(RequestEvent<UserDetail> req) {
		try {
			UserDetail detail = req.getPayload();
			User user = createUser(detail, false);
			emailSender.sendUserCreatedEmail(user);
			return ResponseEvent.response(UserDetail.fromDomain(user));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<UserDetail> signupUser(RequestEvent<UserDetail> req) {
		try {
			UserDetail detail = req.getPayload();
			detail.setActivityStatus(Status.ACTIVITY_STATUS_PENDING.getStatus());
			
			User user = createUser(detail, true);
			emailSender.sendUserSignupEmail(user);
			
			return ResponseEvent.response(UserDetail.fromDomain(user));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<UserDetail> updateUser(RequestEvent<UserDetail> req) {
		try {
			UserDetail detail = req.getPayload();
			Long userId = detail.getId();
			
			User oldUser = daoFactory.getUserDao().getUser(userId);
			if (oldUser == null) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}
			
			User user = userFactory.createUser(detail);

			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			validateChangeInUniqueEmail(oldUser, user, ose);
			ose.checkAndThrow();

			oldUser.update(user);
			daoFactory.getUserDao().saveOrUpdate(oldUser);
			return ResponseEvent.response(UserDetail.fromDomain(oldUser));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<UserDetail> closeUser(RequestEvent<Long> req) {
		try {
			Long userId = req.getPayload();
			User oldUser = daoFactory.getUserDao().getUser(userId);
			if (oldUser == null) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}
			
			oldUser.close();
			daoFactory.getUserDao().saveOrUpdate(oldUser);
			return ResponseEvent.response(UserDetail.fromDomain(oldUser));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<UserDetail> deleteUser(RequestEvent<Long> req) {
		try {
			Long userId = req.getPayload();
			User user =  daoFactory.getUserDao().getUser(userId);
			
			if (user == null) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}
			
			user.delete();
			daoFactory.getUserDao().saveOrUpdate(user);
			return ResponseEvent.response(UserDetail.fromDomain(user));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> changePassword(RequestEvent<PasswordDetails> req) {
		try {
			Long userId = req.getPayload().getUserId();
			User user = daoFactory.getUserDao().getUserByIdAndDomainName(userId, CATISSUE);
			if (user == null) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}

			user.changePassword(req.getPayload());
			daoFactory.getUserDao().saveOrUpdate(user);
			return ResponseEvent.response(true);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> setPassword(RequestEvent<PasswordDetails> req) {
		try {
			Long userId = req.getPayload().getUserId();
			User user = daoFactory.getUserDao().getUserByIdAndDomainName(userId, CATISSUE);
			if (user == null) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}
			
			user.setPassword(req.getPayload(), req.getPayload().getPasswordToken());
			daoFactory.getUserDao().saveOrUpdate(user);
			return ResponseEvent.response(true);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> forgotPassword(RequestEvent<String> req) {
		try {
			String loginName = req.getPayload();
			User user = daoFactory.getUserDao().getUserByLoginNameAndDomainName(loginName, CATISSUE);			
			if (user == null) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}
			
			user.setPasswordToken(UUID.randomUUID().toString());
			daoFactory.getUserDao().saveOrUpdate(user);
			emailSender.sendForgotPasswordEmail(user);			
			return ResponseEvent.response(true);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<UserDetail> getUser(RequestEvent<Long> req) {
		User user = daoFactory.getUserDao().getUser(req.getPayload());
		if (user == null) {
			ResponseEvent.userError(UserErrorCode.NOT_FOUND);
		}
		
		return ResponseEvent.response(UserDetail.fromDomain(user));
	}

	@Override
	public ResponseEvent<Boolean> validatePassword(RequestEvent<String> req) {
		return ResponseEvent.response(User.isValidPasswordPattern(req.getPayload()));
	}
	
	private User createUser(UserDetail detail, Boolean isSignupReq) {
		User user = userFactory.createUser(detail);
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		ensureUniqueLoginNameInDomain(user.getLoginName(), user.getAuthDomain().getName(), ose);
		ensureUniqueEmailAddress(user.getEmailAddress(), ose);
		ose.checkAndThrow();
		
		if (!isSignupReq) {
		  user.generatePasswordToken();
		}
		
		daoFactory.getUserDao().saveOrUpdate(user);
		return user;
	}

	private void ensureUniqueEmailAddress(String emailAddress, OpenSpecimenException ose) {
		if (!daoFactory.getUserDao().isUniqueEmailAddress(emailAddress)) {
			ose.addError(UserErrorCode.DUP_EMAIL);
		}
	}

	private void ensureUniqueLoginNameInDomain(String loginName, String domainName,
			OpenSpecimenException ose) {
		if (!daoFactory.getUserDao().isUniqueLoginNameInDomain(loginName, domainName)) {
			ose.addError(UserErrorCode.DUP_LOGIN_NAME);
		}
	}

	private void validateChangeInUniqueEmail(User oldUser, User newUser, OpenSpecimenException ose) {
		if (!oldUser.getEmailAddress().equals(newUser.getEmailAddress())) {
			ensureUniqueEmailAddress(newUser.getEmailAddress(), ose);
		}
	}
	
	
}