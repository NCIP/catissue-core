
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.opensaml.saml2.core.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;

import com.krishagni.catissueplus.core.administrative.domain.ForgotPasswordToken;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserFactory;
import com.krishagni.catissueplus.core.administrative.events.AnnouncementDetail;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.events.PasswordDetails;
import com.krishagni.catissueplus.core.administrative.events.UserDetail;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.repository.UserListCriteria;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.MessageUtil;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.rbac.events.SubjectRoleDetail;
import com.krishagni.rbac.service.RbacService;

public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private static final String DEFAULT_AUTH_DOMAIN = "openspecimen";
	
	private static final String FORGOT_PASSWORD_EMAIL_TMPL = "users_forgot_password_link"; 
	
	private static final String PASSWD_CHANGED_EMAIL_TMPL = "users_passwd_changed";
	
	private static final String SIGNED_UP_EMAIL_TMPL = "users_signed_up";
	
	private static final String NEW_USER_REQUEST_EMAIL_TMPL = "users_new_user_request";
	
	private static final String USER_REQUEST_REJECTED_TMPL = "users_request_rejected";
	
	private static final String USER_CREATED_EMAIL_TMPL = "users_created";

	private static final String ANNOUNCEMENT_EMAIL_TMPL = "announcement_email";

	private static final String ADMIN_MOD = "administrative";

	private static final String ACTIVE_USER_LOGIN_DAYS_CFG = "active_users_login_days";

	private DaoFactory daoFactory;

	private UserFactory userFactory;
	
	private EmailService emailService;
	
	private RbacService rbacSvc;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setUserFactory(UserFactory userFactory) {
		this.userFactory = userFactory;
	}
	
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public void setRbacSvc(RbacService rbacSvc) {
		this.rbacSvc = rbacSvc;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<UserSummary>> getUsers(RequestEvent<UserListCriteria> req) {
		List<UserSummary> users = daoFactory.getUserDao().getUsers(addUserListCriteria(req.getPayload()));
		return ResponseEvent.response(users);
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<Long> getUsersCount(RequestEvent<UserListCriteria> req) {
		return ResponseEvent.response(daoFactory.getUserDao().getUsersCount(addUserListCriteria(req.getPayload())));
	}

	@Override
	public UserDetails loadUserByUsername(String username)
	throws UsernameNotFoundException {
		return daoFactory.getUserDao().getUser(username, DEFAULT_AUTH_DOMAIN);
	}
	
	@Override
	@PlusTransactional
	public Object loadUserBySAML(SAMLCredential credential)
	throws UsernameNotFoundException {
		if (logger.isDebugEnabled()) {
			for (Attribute attr : credential.getAttributes()) {
				logger.debug(String.format(
					"Credential attr: %s (%s) = %s",
					attr.getName(), attr.getFriendlyName(), credential.getAttributeAsString(attr.getName())));
			}
		}

		//
		// The assumption is - there can be only one SAML auth provider
		// We should perhaps use SAML local entity ID
		//
		AuthDomain domain = daoFactory.getAuthDao().getAuthDomainByType("saml");

		Map<String, String> props = domain.getAuthProvider().getProps();
		String loginNameAttr = props.get("loginNameAttr");
		String emailAttr     = props.get("emailAddressAttr");
		
		User user = null;
		if (StringUtils.isNotBlank(loginNameAttr)) {
			String loginName = getCredentialAttrValue(credential, loginNameAttr);
			user = daoFactory.getUserDao().getUser(loginName, domain.getName());
		} else if (StringUtils.isNotBlank(emailAttr)) {
			String email = getCredentialAttrValue(credential, emailAttr);
			user = daoFactory.getUserDao().getUserByEmailAddress(email);
		}
		
		if (user == null) {
			throw new UsernameNotFoundException(MessageUtil.getInstance().getMessage("user_not_found"));
		}
		
		return user;
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
			if (isSignupReq) {
				detail.setActivityStatus(Status.ACTIVITY_STATUS_PENDING.getStatus());
			}
			
			User user = userFactory.createUser(detail);
			resetAttrs(user);

			if (!isSignupReq) {
				AccessCtrlMgr.getInstance().ensureCreateUserRights(user);
			}
		
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueLoginNameInDomain(user.getLoginName(), user.getAuthDomain().getName(), ose);
			ensureUniqueEmailAddress(user.getEmailAddress(), ose);
			ose.checkAndThrow();

			daoFactory.getUserDao().saveOrUpdate(user);
			
			if (user.isInstituteAdmin()) {
				addDefaultSiteAdminRole(user);
			}
			
			if (isSignupReq) {
				sendUserSignupEmail(user);
				sendNewUserRequestEmail(user);
			} else {
				ForgotPasswordToken token = generateForgotPwdToken(user);
				sendUserCreatedEmail(user, token);
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
		return updateUser(req, false);
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<UserDetail> patchUser(RequestEvent<UserDetail> req) {
		return updateUser(req, true);
	}

	@Override
	@PlusTransactional
	public ResponseEvent<UserDetail> updateStatus(RequestEvent<UserDetail> req) {
		try {
			boolean sendRequestApprovedMail = false;
			UserDetail detail = req.getPayload();
			User user =  daoFactory.getUserDao().getById(detail.getId());
			if (user == null) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}
			
			String currentStatus = user.getActivityStatus();
			String newStatus = detail.getActivityStatus();
			if (currentStatus.equals(newStatus)) {
				return ResponseEvent.response(UserDetail.from(user));
			}
			
			AccessCtrlMgr.getInstance().ensureUpdateUserRights(user);
			
			if (!isStatusChangeAllowed(newStatus)) {
				return ResponseEvent.userError(UserErrorCode.STATUS_CHANGE_NOT_ALLOWED);
			}
 			
			if (isActivated(currentStatus, newStatus)) {
				user.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
				sendRequestApprovedMail = currentStatus.equals(Status.ACTIVITY_STATUS_PENDING.getStatus());
			} else if (isLocked(currentStatus, newStatus)) {
				user.setActivityStatus(Status.ACTIVITY_STATUS_LOCKED.getStatus());
			}
			
			if (sendRequestApprovedMail) {
				ForgotPasswordToken token = generateForgotPwdToken(user);
				sendUserCreatedEmail(user, token);
			}
			
			return ResponseEvent.response(UserDetail.from(user));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch(Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req) {
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

			AccessCtrlMgr.getInstance().ensureDeleteUserRights(existing);

			/*
			 * Appending timestamp to email address, loginName of user while deleting user. 
			 * To send request reject mail, need original user object.
			 * So creating user object clone.
			 */
			User user = new User();
			user.update(existing);
			existing.delete(deleteEntityOp.isClose());

			boolean sendRequestRejectedMail = user.getActivityStatus().equals(Status.ACTIVITY_STATUS_PENDING.getStatus());
			if (sendRequestRejectedMail) {
				sendUserRequestRejectedEmail(user);
			}

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
			User currentUser = AuthUtil.getCurrentUser();

			if (user == null) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}
			
			if (!user.getAuthDomain().getName().equals(DEFAULT_AUTH_DOMAIN)) {
				return ResponseEvent.userError(UserErrorCode.PASSWD_CHANGE_NOT_ALLOWED, user.getAuthDomain().getName());
			}

			if (currentUser.equals(user)) {
				if (!user.isValidOldPassword(detail.getOldPassword())) {
					return ResponseEvent.userError(UserErrorCode.INVALID_OLD_PASSWD);
				}

			} else if (!currentUser.isAdmin()) {
				return ResponseEvent.userError(UserErrorCode.PERMISSION_DENIED);
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
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<SubjectRoleDetail>> getCurrentUserRoles() {
		return rbacSvc.getSubjectRoles(new RequestEvent<Long>(AuthUtil.getCurrentUser().getId()));
	}		
	
	@Override
	@PlusTransactional
	public ResponseEvent<InstituteDetail> getInstitute(RequestEvent<Long> req) {
		Institute institute = getInstitute(req.getPayload());
		return ResponseEvent.response(InstituteDetail.from(institute));
	}

	private UserListCriteria addUserListCriteria(UserListCriteria crit) {
		if (!AuthUtil.isAdmin() && !crit.listAll()) {
			crit.instituteName(getCurrUserInstitute().getName());
		}

		return crit;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> broadcastAnnouncement(RequestEvent<AnnouncementDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();

			AnnouncementDetail detail = req.getPayload();
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureValidAnnouncement(detail, ose);
			ose.checkAndThrow();

			//
			// For now announcements are broadcast using emails;
			// therefore fetching only email IDs
			// Later we can broadcast using SMS, WhatsApp, Facebook, Twitter, and anything
			//
			emailAnnouncements(detail, getActiveUsersEmailIds());
			return ResponseEvent.response(true);
		} catch(OpenSpecimenException ose){
			return ResponseEvent.error(ose);
		} catch(Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private ResponseEvent<UserDetail> updateUser(RequestEvent<UserDetail> req, boolean partial) {
		try {
			UserDetail detail = req.getPayload();
			Long userId = detail.getId();
			String emailAddress = detail.getEmailAddress();
			
			User existingUser = null; 
			if (userId != null) {
				existingUser = daoFactory.getUserDao().getById(userId); 
			} else if (StringUtils.isNotBlank(emailAddress)) {
				existingUser = daoFactory.getUserDao().getUserByEmailAddress(emailAddress);
			}
			
			if (existingUser == null) {
				return ResponseEvent.userError(UserErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureUpdateUserRights(existingUser);
			
			User user = null;
			if (partial) {
				user = userFactory.createUser(existingUser, detail);
			} else {
				user = userFactory.createUser(detail);
			}
			resetAttrs(existingUser, user);

			AccessCtrlMgr.getInstance().ensureUpdateUserRights(user);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

			ensureUniqueEmail(existingUser, user, ose);
			ensureUniqueLoginName(existingUser, user, ose);
			ose.checkAndThrow();

			boolean wasInstituteAdmin = existingUser.isInstituteAdmin();
			
			existingUser.update(user);
			
			if (!wasInstituteAdmin && existingUser.isInstituteAdmin()) {
				addDefaultSiteAdminRole(existingUser);
			} else if (wasInstituteAdmin && !existingUser.isInstituteAdmin()) {
				removeDefaultSiteAdminRole(existingUser);
			}

			return ResponseEvent.response(UserDetail.from(existingUser));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}		
	}
	
	private void addDefaultSiteAdminRole(User user) {
		rbacSvc.addSubjectRole(null, null, user, getDefaultSiteAdminRole());
	}
	
	private void removeDefaultSiteAdminRole(User user) {
		rbacSvc.removeSubjectRole(null, null, user, getDefaultSiteAdminRole());
	}
	
	private String[] getDefaultSiteAdminRole() {
		return new String[] {"Administrator"};
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
	
	private void sendUserCreatedEmail(User user, ForgotPasswordToken token) {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("user", user);
		props.put("token", token);
		
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
		props.put("$subject", subjParams);
		
		String[] to = {ConfigUtil.getInstance().getAdminEmailId()};
		emailService.sendEmail(NEW_USER_REQUEST_EMAIL_TMPL, to, props);
	}
	
	private void sendUserRequestRejectedEmail(User user) {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("user", user);
		
		emailService.sendEmail(USER_REQUEST_REJECTED_TMPL, new String[]{user.getEmailAddress()}, props);
	}
	
	private void resetAttrs(User newUser) {
		resetAttrs(null, newUser);
	}
	
	private void resetAttrs(User existingUser, User newUser) {
		if (AuthUtil.isAdmin()) {
			return;
		}

		//
		// Only super admin can update these attributes; therefore reset to
		// their earlier value or default value
		//
		newUser.setType(existingUser != null ? existingUser.getType() : User.Type.NONE);
		newUser.setManageForms(existingUser != null ? existingUser.canManageForms() : false);
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
	
	private void ensureUniqueLoginName(User existingUser, User newUser, OpenSpecimenException ose) {
		if (!existingUser.getLoginName().equals(newUser.getLoginName())) {
			ensureUniqueLoginNameInDomain(newUser.getLoginName(), newUser.getAuthDomain().getName(), ose);
		}
	}

	private void ensureUniqueLoginNameInDomain(String loginName, String domainName, OpenSpecimenException ose) {
		if (User.SYS_USER.equals(loginName.trim()) && User.DEFAULT_AUTH_DOMAIN.equals(domainName.trim())) {
			ose.addError(UserErrorCode.SYS_LOGIN_NAME, loginName);
			return;
		}
		
		if (!daoFactory.getUserDao().isUniqueLoginName(loginName, domainName)) {
			ose.addError(UserErrorCode.DUP_LOGIN_NAME);
		}
	}

	private boolean isStatusChangeAllowed(String newStatus) {
		return newStatus.equals(Status.ACTIVITY_STATUS_ACTIVE.getStatus()) || 
				newStatus.equals(Status.ACTIVITY_STATUS_LOCKED.getStatus());
	}
	
	private boolean isActivated(String currentStatus, String newStatus) {
		return !currentStatus.equals(Status.ACTIVITY_STATUS_ACTIVE.getStatus()) && 
				newStatus.equals(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
	}
	
	private boolean isLocked(String currentStatus, String newStatus) {
		return currentStatus.equals(Status.ACTIVITY_STATUS_ACTIVE.getStatus()) &&
				newStatus.equals(Status.ACTIVITY_STATUS_LOCKED.getStatus());
	}
		
	private Institute getCurrUserInstitute() {
		return getInstitute(AuthUtil.getCurrentUser().getId());
	}
	
	private Institute getInstitute(Long id) {
		User user = daoFactory.getUserDao().getById(id);
		return user.getInstitute();		
	}
	
	private ForgotPasswordToken generateForgotPwdToken(User user) {
		ForgotPasswordToken token = null;
		if (user.getAuthDomain().getName().equals(DEFAULT_AUTH_DOMAIN)) {
			token = new ForgotPasswordToken(user);
			daoFactory.getUserDao().saveFpToken(token);
		}
		return token;
	}

	private String getCredentialAttrValue(SAMLCredential credential, String attrName) {
		Attribute attr = credential.getAttributes().stream()
			.filter(a -> attrName.equals(a.getName()) || attrName.equals(a.getFriendlyName()))
			.findFirst().orElse(null);

		if (attr == null) {
			return null;
		}

		return credential.getAttributeAsString(attr.getName());
	}

	private void ensureValidAnnouncement(AnnouncementDetail detail, OpenSpecimenException ose) {
		if (StringUtils.isBlank(detail.getSubject())) {
			ose.addError(UserErrorCode.ANN_SUBJECT_REQ);
		}

		if (StringUtils.isBlank(detail.getMessage())) {
			ose.addError(UserErrorCode.ANN_MESSAGE_REQ);
		}
	}

	private List<String> getActiveUsersEmailIds() {
		Date today = Calendar.getInstance().getTime();
		return daoFactory.getUserDao().getActiveUsersEmailIds(getActiveUserLastLoginCutoff(today), today);
	}

	private Date getActiveUserLastLoginCutoff(Date present) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(present);
		cal.add(Calendar.DAY_OF_YEAR, -getActiveUserCfgDays());
		return Utility.chopTime(cal.getTime());
	}

	private int getActiveUserCfgDays() {
		return ConfigUtil.getInstance().getIntSetting(ADMIN_MOD, ACTIVE_USER_LOGIN_DAYS_CFG, 90);
	}

	private void emailAnnouncements(AnnouncementDetail detail, List<String> emailAddresses) {
		String[] adminEmailAddr = {ConfigUtil.getInstance().getAdminEmailId()};
		String[] rcpts = emailAddresses.toArray(new String[emailAddresses.size()]);

		Map<String, Object> props = new HashMap<>();
		props.put("user", AuthUtil.getCurrentUser());
		props.put("$subject", new String[] { detail.getSubject() });
		props.put("annDetail", detail);
		emailService.sendEmail(ANNOUNCEMENT_EMAIL_TMPL, adminEmailAddr, rcpts, null, props);
	}
}
