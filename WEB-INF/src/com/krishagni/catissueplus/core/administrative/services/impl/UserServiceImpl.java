
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.krishagni.catissueplus.core.administrative.domain.ForgotPasswordToken;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserFactory;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.events.DeleteUserOp;
import com.krishagni.catissueplus.core.administrative.events.ListUserCriteria;
import com.krishagni.catissueplus.core.administrative.events.PasswordDetails;
import com.krishagni.catissueplus.core.administrative.events.SiteDetail;
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
	private static final String DEFAULT_AUTH_DOMAIN = "openspecimen";
	
	private DaoFactory daoFactory;

	private UserFactory userFactory;
	
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
	public ResponseEvent<List<UserSummary>> getUsers(RequestEvent<ListUserCriteria> req) {
		ListUserCriteria crit = req.getPayload();		
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
			ResponseEvent.userError(UserErrorCode.NOT_FOUND);
		}
		
		return ResponseEvent.response(UserDetail.from(user));
	}

	
	@Override
	@PlusTransactional
	public ResponseEvent<UserDetail> createUser(RequestEvent<UserDetail> req) {
		try {
			UserDetail detail = req.getPayload();
			User user = userFactory.createUser(detail);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueLoginNameInDomain(user.getLoginName(), user.getAuthDomain().getName(), ose);
			ensureUniqueEmailAddress(user.getEmailAddress(), ose);
			ose.checkAndThrow();

			daoFactory.getUserDao().saveOrUpdate(user);
			emailSender.sendUserCreatedEmail(user);
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
	public ResponseEvent<Map<String, List>> deleteUser(RequestEvent<DeleteUserOp> req) {
		try {
			DeleteUserOp deleteUserOp = req.getPayload();
			boolean close = deleteUserOp.isClose();
			User user =  daoFactory.getUserDao().getById(deleteUserOp.getId());
			
			if (user == null) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}
			
			//TODO: Revisit and check other depedencies like cp, dp, AQ
			if (!close && !user.getSites().isEmpty()) {
				return ResponseEvent.response(getDependencies(user));
			}
			
			user.delete(close);
			return ResponseEvent.response(Collections.<String, List>emptyMap());
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
				return ResponseEvent.userError(UserErrorCode.INVALID_PASSWD_TOKEN);
			}
			
			user.changePassword(detail.getNewPassword());
			dao.deleteFpToken(token);
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
			emailSender.sendForgotPasswordEmail(user, token.getToken());
			return ResponseEvent.response(true);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private Map<String, List> getDependencies(User user) {
		List<SiteDetail> sites = new ArrayList<SiteDetail>();
		for (Site site: user.getSites()) {
			sites.add(SiteDetail.from(site));
		}
		
		Map<String, List> dependencies = new HashMap<String, List>();
		dependencies.put("sites", sites);
		
		return dependencies;
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
