
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.krishagni.catissueplus.core.administrative.domain.ForgotPasswordToken;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserFactory;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.repository.UserListCriteria;
import com.krishagni.catissueplus.core.administrative.events.PasswordDetails;
import com.krishagni.catissueplus.core.administrative.events.UserDetail;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.Status;

import edu.wustl.common.util.XMLPropertyHandler;

public class UserServiceImpl implements UserService {
	private static final String DEFAULT_AUTH_DOMAIN = "openspecimen";
	
	private static final String FORGOT_PASSWORD_EMAIL_TMPL = "users_forgot_password_link"; 
	
	private static final String PASSWD_CHANGED_EMAIL_TMPL = "users_passwd_changed";
	
	private static final String SIGNED_UP_EMAIL_TMPL = "users_signed_up";
	
	private static final String REQUEST_APPROVED_EMAIL_TMPL = "users_request_approved";
	
	private static final String NEW_USER_REQUEST_EMAIL_TMPL = "users_new_user_request";
	
	private static final String USER_CREATED_EMAIL_TMPL = "users_created";
	
	private static final String adminEmailAddress = XMLPropertyHandler.getValue("email.administrative.emailAddress");
	
	private DaoFactory daoFactory;

	private UserFactory userFactory;
	
	private EmailService emailService;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setUserFactory(UserFactory userFactory) {
		this.userFactory = userFactory;
	}
	
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<UserSummary>> getUsers(RequestEvent<UserListCriteria> req) {
		UserListCriteria crit = req.getPayload();		
		List<UserSummary> users = daoFactory.getUserDao().getUsers(crit);		
		return ResponseEvent.response(users);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return daoFactory.getUserDao().getUser(username, DEFAULT_AUTH_DOMAIN);
	}

	@Override
	@PlusTransactional
	public ResponseEvent<UserDetail> getUser(RequestEvent<Long> req) {
		User user = daoFactory.getUserDao().getById(req.getPayload());
		if (user == null) {
			return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
		}
		
		return ResponseEvent.response(UserDetail.from(user));
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<UserDetail> createUser(RequestEvent<UserDetail> req) {
		try {
			boolean isSignupReq = (AuthUtil.getCurrentUser() == null);
			
			UserDetail detail = req.getPayload();
			if(isSignupReq) {
				detail.setActivityStatus(Status.ACTIVITY_STATUS_PENDING.getStatus());
			}
			User user = userFactory.createUser(detail);
		
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueLoginNameInDomain(user.getLoginName(), user.getAuthDomain().getName(), ose);
			ensureUniqueEmailAddress(user.getEmailAddress(), ose);
			ose.checkAndThrow();
		
			daoFactory.getUserDao().saveOrUpdate(user);
			if (isSignupReq) {
				sendUserSignupEmail(user);
				sendNewUserRequestEmail(user);
			} else {
				sendUserCreatedEmail(user);
			}
			return ResponseEvent.response(UserDetail.from(user));
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
			
			User existingUser = daoFactory.getUserDao().getById(userId);
			if (existingUser == null) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}
			
			User user = userFactory.createUser(detail);

			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueEmail(existingUser, user, ose);
			ose.checkAndThrow();

			existingUser.update(user);
			return ResponseEvent.response(UserDetail.from(existingUser));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<UserDetail> activateUser(RequestEvent<Long> req) {
		try {
			Long id = req.getPayload();
			User user =  daoFactory.getUserDao().getById(id);
			if (user == null) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}
			
			user.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
			ForgotPasswordToken token = null;
			if (user.getAuthDomain().getName().equals(DEFAULT_AUTH_DOMAIN)) {
				token = new ForgotPasswordToken(user);
				daoFactory.getUserDao().saveFpToken(token);
			}
			
			sendUserRequestApprovedEmail(user, token);
			return ResponseEvent.response(UserDetail.from(user));
		} catch(Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<Map<String, Object>>> getUserDependentEntities(RequestEvent<Long> req) {
		try {
			User existing = daoFactory.getUserDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(existing.getDependentEntities());
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<UserDetail> deleteUser(RequestEvent<DeleteEntityOp> req) {
		try {
			DeleteEntityOp deleteEntityOp = req.getPayload();
			User existing =  daoFactory.getUserDao().getById(deleteEntityOp.getId());
			if (existing == null) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}
			
			existing.delete(deleteEntityOp.isClose());
			return ResponseEvent.response(UserDetail.from(existing)); 
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> changePassword(RequestEvent<PasswordDetails> req) {
		try {
			PasswordDetails detail = req.getPayload();
			User user = daoFactory.getUserDao().getById(detail.getUserId());
			if (user == null) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}
			
			if (!user.isValidOldPassword(detail.getOldPassword())) {
				return ResponseEvent.userError(UserErrorCode.INVALID_OLD_PASSWD);
			}
			
			user.changePassword(detail.getNewPassword());
			daoFactory.getUserDao().saveOrUpdate(user);
			sendPasswdChangedEmail(user);
			return ResponseEvent.response(true);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> resetPassword(RequestEvent<PasswordDetails> req) {
		try {
			UserDao dao = daoFactory.getUserDao();
			PasswordDetails detail = req.getPayload();
			if (StringUtils.isEmpty(detail.getResetPasswordToken())) {
				return ResponseEvent.userError(UserErrorCode.INVALID_PASSWD_TOKEN);
			}
			
			ForgotPasswordToken token = dao.getFpToken(detail.getResetPasswordToken());
			if (token == null) {
				return ResponseEvent.userError(UserErrorCode.INVALID_PASSWD_TOKEN);
			}
			
			User user = token.getUser();
			if (!user.getLoginName().equals(detail.getLoginName())) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}
			
			if (token.hasExpired()) {
				dao.deleteFpToken(token);
				return ResponseEvent.userError(UserErrorCode.INVALID_PASSWD_TOKEN, true);
			}
			
			user.changePassword(detail.getNewPassword());
			dao.deleteFpToken(token);
			sendPasswdChangedEmail(user);
			return ResponseEvent.response(true);
		}catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> forgotPassword(RequestEvent<String> req) {
		try {
			UserDao dao = daoFactory.getUserDao();
			String loginName = req.getPayload();
			User user = dao.getUser(loginName, DEFAULT_AUTH_DOMAIN);
			if (user == null || !user.getActivityStatus().equals(Status.ACTIVITY_STATUS_ACTIVE.getStatus())) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}
			
			ForgotPasswordToken oldToken = dao.getFpTokenByUser(user.getId());
			if (oldToken != null) {
				dao.deleteFpToken(oldToken);
			}
			
			ForgotPasswordToken token = new ForgotPasswordToken(user);
			dao.saveFpToken(token);
			sendForgotPasswordLinkEmail(user, token.getToken());
			return ResponseEvent.response(true);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void sendForgotPasswordLinkEmail(User user, String token) {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("user", user);
		props.put("token", token);
		
		emailService.sendEmail(FORGOT_PASSWORD_EMAIL_TMPL, new String[]{user.getEmailAddress()}, props);
	}
	
	private void sendPasswdChangedEmail(User user) {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("user", user);
		
		emailService.sendEmail(PASSWD_CHANGED_EMAIL_TMPL, new String[]{user.getEmailAddress()}, props);
	} 
	
	private void sendUserCreatedEmail(User user) {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("user", user);
		
		emailService.sendEmail(USER_CREATED_EMAIL_TMPL, new String[]{user.getEmailAddress()}, props);
	}
	
	private void sendUserSignupEmail(User user) {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("user", user);
		
		emailService.sendEmail(SIGNED_UP_EMAIL_TMPL, new String[]{user.getEmailAddress()}, props);
	}
	
	private void sendNewUserRequestEmail(User user) {
		String [] subjParams = new String[] {user.getFirstName(), user.getLastName()};
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("newUser", user);
		props.put("admin", user); //TODO:replace with admin 
		props.put("$subject", subjParams);
		
		emailService.sendEmail(NEW_USER_REQUEST_EMAIL_TMPL, new String[]{adminEmailAddress}, props);
	}
	
	private void sendUserRequestApprovedEmail(User user, ForgotPasswordToken token) {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("user", user);
		props.put("token", token);
		
		emailService.sendEmail(REQUEST_APPROVED_EMAIL_TMPL, new String[]{user.getEmailAddress()}, props);
	}
	
	private void ensureUniqueEmail(User existingUser, User newUser, OpenSpecimenException ose) {
		if (!existingUser.getEmailAddress().equals(newUser.getEmailAddress())) {
			ensureUniqueEmailAddress(newUser.getEmailAddress(), ose);
		}
	}

	private void ensureUniqueEmailAddress(String emailAddress, OpenSpecimenException ose) {
		if (!daoFactory.getUserDao().isUniqueEmailAddress(emailAddress)) {
			ose.addError(UserErrorCode.DUP_EMAIL);
		}
	}

	private void ensureUniqueLoginNameInDomain(String loginName, String domainName,
			OpenSpecimenException ose) {
		if (!daoFactory.getUserDao().isUniqueLoginName(loginName, domainName)) {
			ose.addError(UserErrorCode.DUP_LOGIN_NAME);
		}
	}
	
}
