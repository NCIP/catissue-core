
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals; 
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.impl.UserFactoryImpl;
import com.krishagni.catissueplus.core.administrative.events.AllUsersEvent;
import com.krishagni.catissueplus.core.administrative.events.CloseUserEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableUserEvent;
import com.krishagni.catissueplus.core.administrative.events.ForgotPasswordEvent;
import com.krishagni.catissueplus.core.administrative.events.GetUserEvent;
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
import com.krishagni.catissueplus.core.administrative.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.administrative.repository.DepartmentDao;
import com.krishagni.catissueplus.core.administrative.repository.PermissibleValueDao;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.administrative.services.impl.PermissibleValueServiceImpl;
import com.krishagni.catissueplus.core.administrative.services.impl.UserServiceImpl;
import com.krishagni.catissueplus.core.auth.repository.DomainDao;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.PermissibleValuesManager;
import com.krishagni.catissueplus.core.common.PermissibleValuesManagerImpl;
import com.krishagni.catissueplus.core.common.email.EmailSender;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.privileges.repository.RoleDao;
import com.krishagni.catissueplus.core.services.testdata.PermissibleValueTestData;
import com.krishagni.catissueplus.core.services.testdata.UserTestData;

public class UserTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	UserDao userDao;

	@Mock
	DomainDao domainDao;

	@Mock
	DepartmentDao departmentDao;

	@Mock
	CollectionProtocolDao collectionProtocolDao;

	@Mock
	SiteDao siteDao;
	
	@Mock
	RoleDao roleDao;
	
	@Mock
	EmailSender emailSender;
	
	@Mock
	PermissibleValueDao pvDao;

	@Mock
	CommonValidator commonValidator;
	
	PermissibleValuesManager pvManager;
	
	private PermissibleValueService pvService;
	
	private UserFactory userFactory;

	private UserService userService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		when(daoFactory.getSiteDao()).thenReturn(siteDao);
		when(daoFactory.getUserDao()).thenReturn(userDao);
		
		when(daoFactory.getPermissibleValueDao()).thenReturn(pvDao);
		pvService = new PermissibleValueServiceImpl();
		
		((PermissibleValueServiceImpl) pvService).setDaoFactory(daoFactory);
		pvManager = new PermissibleValuesManagerImpl();
		((PermissibleValuesManagerImpl) pvManager).setPermissibleValueSvc(pvService);
		CommonValidator.setPvManager(pvManager);
		when(pvDao.getAllValuesByAttribute(anyString())).thenReturn(PermissibleValueTestData.getPvValues());

		
		when(daoFactory.getUserDao()).thenReturn(userDao);
		when(daoFactory.getDomainDao()).thenReturn(domainDao);
		when(daoFactory.getSiteDao()).thenReturn(siteDao);
		when(daoFactory.getRoleDao()).thenReturn(roleDao);
		when(daoFactory.getCollectionProtocolDao()).thenReturn(collectionProtocolDao);

		userService = new UserServiceImpl();
		((UserServiceImpl) userService).setDaoFactory(daoFactory);
		userFactory = new UserFactoryImpl();
		((UserFactoryImpl) userFactory).setDaoFactory(daoFactory);
		((UserServiceImpl) userService).setUserFactory(userFactory);
		((UserServiceImpl) userService).setEmailSender(emailSender);

		when(daoFactory.getDepartmentDao()).thenReturn(departmentDao);
		when(roleDao.getRoleByName(anyString())).thenReturn(UserTestData.getRole(1l));
		when(departmentDao.getDepartmentByName(anyString())).thenReturn(UserTestData.getDeparment("Chemical"));
		when(daoFactory.getUserDao()).thenReturn(userDao);
		when(siteDao.getSite(anyString())).thenReturn(UserTestData.getSite());
		when(collectionProtocolDao.getCPByTitle(anyString())).thenReturn(UserTestData.getCp());
		when(domainDao.isUniqueAuthDomainName(anyString())).thenReturn(Boolean.FALSE);
		when(userDao.isUniqueEmailAddress(anyString())).thenReturn(Boolean.TRUE);
		when(userDao.isUniqueLoginNameInDomain(anyString(), anyString())).thenReturn(Boolean.TRUE);
		when(domainDao.getAuthDomainByName(anyString())).thenReturn(UserTestData.getAuthDomain(1));
		when(domainDao.isUniqueAuthDomainName(anyString())).thenReturn(Boolean.TRUE);
		when(emailSender.sendUserCreatedEmail(UserTestData.getUser(1l))).thenReturn(Boolean.TRUE);
		when(emailSender.sendForgotPasswordEmail(UserTestData.getUser(1l))).thenReturn(Boolean.TRUE);
	}

	@Test
	public void testForSuccessfulUserCreation() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventForUserCreation();
		UserCreatedEvent response = userService.createUser(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		UserDetails createdUserDto = response.getUserDetails();
		assertEquals(reqEvent.getUserDetails().getFirstName(), createdUserDto.getFirstName());
	}
	
	@Test
	public void testForSuccessfulNonLdapUserCreation() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventForNonLdapUserCreation();
		UserCreatedEvent response = userService.createUser(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		UserDetails createdUserDto = response.getUserDetails();
		assertEquals(reqEvent.getUserDetails().getFirstName(), createdUserDto.getFirstName());
	}
	
	@Test
	public void testUserCreationWithNullAuthDomain() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEvent();
		when(domainDao.getAuthDomainByName(anyString())).thenReturn(null);
		UserCreatedEvent response = userService.createUser(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(UserTestData.AUTH_DOMAIN, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testForSuccessfulUserCreationWithNullSite() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventForUserCreation();
		when(siteDao.getSite(anyString())).thenReturn(null);
		UserCreatedEvent response = userService.createUser(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testForSuccessfulUserCreationWithNullCp() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventForUserCreation();
		when(collectionProtocolDao.getCPByTitle(anyString())).thenReturn(null);
		UserCreatedEvent response = userService.createUser(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testUserCreationWithDuplicateLoginName() {
		when(userDao.isUniqueLoginNameInDomain(anyString(), anyString())).thenReturn(Boolean.FALSE);
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventForUserCreation();
		UserCreatedEvent response = userService.createUser(reqEvent);
		
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(UserTestData.LOGIN_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.DUPLICATE_LOGIN_NAME.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testUserCreationWithEmptyLoginName() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventWithEmptyLoginName();
		when(userDao.isUniqueEmailAddress(anyString())).thenReturn(Boolean.TRUE);

		UserCreatedEvent response = userService.createUser(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(UserTestData.LOGIN_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testUserCreationWithEmptyDomainName() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventWithEmptyDomainName();

		UserCreatedEvent response = userService.createUser(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(UserTestData.AUTH_DOMAIN, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testUserCreationWithEmptyEmail() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventWithEmptyEmail();
		when(userDao.isUniqueEmailAddress("")).thenReturn(Boolean.TRUE);

		UserCreatedEvent response = userService.createUser(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(UserTestData.EMAIL_ADDRESS, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testUserCreationWithEmptyFirstName() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventWithEmptyFirstName();
		when(userDao.isUniqueEmailAddress(anyString())).thenReturn(Boolean.TRUE);

		UserCreatedEvent response = userService.createUser(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(UserTestData.FIRST_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testUserCreationWithEmptyLastName() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventWithEmptyLastName();
		UserCreatedEvent response = userService.createUser(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(UserTestData.LAST_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testUserCreationWithInvalidEmailAddress() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventWithInvalidEmail();
		when(userDao.isUniqueEmailAddress("admin")).thenReturn(Boolean.TRUE);
		UserCreatedEvent response = userService.createUser(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(UserTestData.EMAIL_ADDRESS, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testUserCreationWithNonUniqueEmailAddress() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEvent();
		when(userDao.isUniqueEmailAddress("sci@sci.com")).thenReturn(Boolean.FALSE);
		UserCreatedEvent response = userService.createUser(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(UserTestData.EMAIL_ADDRESS, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.DUPLICATE_EMAIL.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testUserCreationWithServerErr() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventForUserCreation();

		doThrow(new RuntimeException()).when(userDao).saveOrUpdate(any(User.class));
		UserCreatedEvent response = userService.createUser(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForSuccessfulUserUpdate() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		UpdateUserEvent reqEvent = UserTestData.getUpdateUserEvent();

		UserUpdatedEvent response = userService.updateUser(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		UserDetails createdUser = response.getUserDetails();
		assertEquals(reqEvent.getUserDetails().getFirstName(), createdUser.getFirstName());
		assertNotNull(createdUser.getDeptName());
		assertEquals("firstName", createdUser.getFirstName());
	}

	@Test
	public void testForSuccessfulUserUpdateWithNonUniqueEmail() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		UpdateUserEvent reqEvent = UserTestData.getCreateUserEventWithNonDupEmail();
		when(userDao.isUniqueEmailAddress(anyString())).thenReturn(Boolean.FALSE);
		UserUpdatedEvent response = userService.updateUser(reqEvent);

		assertEquals(UserTestData.EMAIL_ADDRESS, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.DUPLICATE_EMAIL.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testForUserUpdateWithChangedLoginName() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		UpdateUserEvent reqEvent = UserTestData.getUpadteUserEventWithLNUpdate();

		UserUpdatedEvent response = userService.updateUser(reqEvent);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
	
	@Test
	public void testForUserUpdateWithChangedDomain() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(2L));
		UpdateUserEvent reqEvent = UserTestData.getUpadteUserEventWithLNUpdate();

		UserUpdatedEvent response = userService.updateUser(reqEvent);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForInvalidUserUpdate() {
		when(userDao.getUserByIdAndDomainName(anyLong(), anyString())).thenReturn(null);
		UpdateUserEvent reqEvent = UserTestData.getUpdateUserEvent();

		UserUpdatedEvent response = userService.updateUser(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getUserId());
	}

	@Test
	public void testUserUpdateWithServerErr() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		UpdateUserEvent reqEvent = UserTestData.getUpdateUserEvent();

		doThrow(new RuntimeException()).when(userDao).saveOrUpdate(any(User.class));
		UserUpdatedEvent response = userService.updateUser(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForInvalidUserClose() {
		when(userDao.getUserByIdAndDomainName(anyLong(),anyString())).thenReturn(null);
		CloseUserEvent reqEvent = UserTestData.getCloseUserEvent();
		reqEvent.setSessionDataBean(UserTestData.getSessionDataBean());

		UserClosedEvent response = userService.closeUser(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}

	@Test
	public void testSuccessfulUserClose() {
		CloseUserEvent reqEvent = UserTestData.getCloseUserEvent();
		User userToDelete = UserTestData.getUser(1L);
		when(userDao.getUser(anyLong())).thenReturn(userToDelete);
		UserClosedEvent response = userService.closeUser(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(UserTestData.ACTIVITY_STATUS_CLOSED, userToDelete.getActivityStatus());
	}

	@Test
	public void testUserCloseWithServerErr() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1l));
		CloseUserEvent reqEvent = UserTestData.getCloseUserEvent();
		doThrow(new RuntimeException()).when(userDao).saveOrUpdate(any(User.class));
		UserClosedEvent response = userService.closeUser(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForInvalidUserDisable() {
		when(userDao.getUserByIdAndDomainName(anyLong(),anyString())).thenReturn(null);
		DisableUserEvent reqEvent = UserTestData.getDisableUserEvent();
		UserDisabledEvent response = userService.deleteUser(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}
	
	@Test
	public void testForInvalidUserDisableWithName() {
		when(userDao.getUserByLoginNameAndDomainName(anyString(),anyString())).thenReturn(null);
		DisableUserEvent reqEvent = UserTestData.getDisableUserEventForName();
		UserDisabledEvent response = userService.deleteUser(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getName());
	}

	@Test
	public void testSuccessfulUserDisable() {
		DisableUserEvent reqEvent = UserTestData.getDisableUserEvent();
		User userToDelete = UserTestData.getUser(1L);
		when(userDao.getUser(anyLong())).thenReturn(userToDelete);
		UserDisabledEvent response = userService.deleteUser(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(UserTestData.ACTIVITY_STATUS_DISABLED, userToDelete.getActivityStatus());
	}

	@Test
	public void testUserDisableWithServerErr() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1l));
		DisableUserEvent reqEvent = UserTestData.getDisableUserEvent();
		doThrow(new RuntimeException()).when(userDao).saveOrUpdate(any(User.class));
		UserDisabledEvent response = userService.deleteUser(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testUserCreationWithInvalidDepartment() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventForUserCreation();
		when(departmentDao.getDepartmentByName(anyString())).thenReturn(null);

		UserCreatedEvent response = userService.createUser(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(UserTestData.DEPARTMENT, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());

	}

	/*@Test
	public void testSuccessfullPasswordSet() {
		when(userDao.getUserByIdAndDomainName(anyLong(),anyString())).thenReturn(UserTestData.getUserWithCatissueDomain(1L));
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEvent();

		PasswordUpdatedEvent response = userService.setPassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}*/

	@Test
	public void testPasswordSetWithBlankNewPassword() {
		when(userDao.getUserByIdAndDomainName(anyLong(),anyString())).thenReturn(UserTestData.getUserWithCatissueDomain(1L));
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEventForBlankNewPass();

		PasswordUpdatedEvent response = userService.setPassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}

	@Test
	public void testPasswordChangeWithBlankOldPassword() {
		when(userDao.getUserByIdAndDomainName(anyLong(),anyString())).thenReturn(UserTestData.getUserWithCatissueDomain(1L));
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEventForBlankOldPass();

		PasswordUpdatedEvent response = userService.changePassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}
	
	@Test
	public void testPatchUser() {
		when(daoFactory.getUserDao().getUser(anyLong())).thenReturn(UserTestData.getUser(1l));
		when(daoFactory.getUserDao().isUniqueEmailAddress(anyString())).thenReturn(true);
		PatchUserEvent reqEvent = UserTestData.nonPatchData();
		UserUpdatedEvent response = userService.patchUser(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testForSuccessfulUserCreationWithNullRole() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventForUserCreation();
		when(roleDao.getRoleByName(anyString())).thenReturn(null);
		UserCreatedEvent response = userService.createUser(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testPasswordReSetWithInvalidOldPassword() {
		when(userDao.getUserByIdAndDomainName(anyLong(),anyString())).thenReturn(UserTestData.getUserWithCatissueDomain(1L));
		when(userDao.getOldPasswords(anyLong())).thenReturn(UserTestData.getOldPasswordList());
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEventInvalid();

		PasswordUpdatedEvent response = userService.changePassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}

	@Test
	public void testForPasswordSetWithInvalidUser() {
		when(userDao.getUserByIdAndDomainName(anyLong(),anyString())).thenReturn(null);
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEvent();
		PasswordUpdatedEvent response = userService.setPassword(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testForPasswordResetWithInvalidUser() {
		when(userDao.getUserByIdAndDomainName(anyLong(),anyString())).thenReturn(null);
		when(userDao.getOldPasswords(anyLong())).thenReturn(UserTestData.getOldPasswordList());
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEventForReSet();
		PasswordUpdatedEvent response = userService.changePassword(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testForPasswordSetWithBlankToken() {
		when(userDao.getUserByIdAndDomainName(anyLong(),anyString())).thenReturn(UserTestData.getUserWithCatissueDomain(1L));
		when(userDao.getOldPasswords(anyLong())).thenReturn(UserTestData.getOldPasswordList());
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEventWithBlankToken();
		PasswordUpdatedEvent response = userService.setPassword(reqEvent);
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}
	
	/*@Test
	public void testSuccessfullPasswordReset() {
		when(userDao.getUserByIdAndDomainName(anyLong(),anyString())).thenReturn(UserTestData.getUserWithCatissueDomain(1L));
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEventForReSet();
		when(userDao.getOldPasswords(anyLong())).thenReturn(UserTestData.getOldPasswordList());

		PasswordUpdatedEvent response = userService.changePassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}*/

	@Test
	public void testSuccessfullPasswordSetWithDiffTokens() {
		when(userDao.getUserByIdAndDomainName(anyLong(),anyString())).thenReturn(UserTestData.getUserWithCatissueDomain(1L));
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEventForDiffTokens();

		PasswordUpdatedEvent response = userService.setPassword(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}

	@Test
	public void testPasswordSetWithServerErr() {
		when(userDao.getUserByIdAndDomainName(anyLong(),anyString())).thenReturn(UserTestData.getUserWithCatissueDomain(1L));
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEvent();

		doThrow(new RuntimeException()).when(userDao).saveOrUpdate(any(User.class));
		PasswordUpdatedEvent response = userService.setPassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testPasswordReSetWithServerErr() {
		when(userDao.getUserByIdAndDomainName(anyLong(),anyString())).thenReturn(UserTestData.getUserWithCatissueDomain(1L));
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEventForReSet();
		when(userDao.getOldPasswords(anyLong())).thenReturn(UserTestData.getOldPasswordList());

		doThrow(new RuntimeException()).when(userDao).saveOrUpdate(any(User.class));
		PasswordUpdatedEvent response = userService.changePassword(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForInvalidUserForgotPassword() {
		when(userDao.isUniqueLoginNameInDomain(anyString(), anyString())).thenReturn(null);
		ForgotPasswordEvent reqEvent = UserTestData.getForgotPasswordEvent();
		reqEvent.setSessionDataBean(UserTestData.getSessionDataBean());

		PasswordForgottenEvent response = userService.forgotPassword(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testSuccessfulForgotPassword() {
		when(userDao.getUserByLoginNameAndDomainName(anyString(), anyString())).thenReturn(UserTestData.getUserWithCatissueDomain(1L));
		ForgotPasswordEvent reqEvent = UserTestData.getForgotPasswordEvent();
		PasswordForgottenEvent response = userService.forgotPassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testForgotPasswordWithLdapUser() {
		when(userDao.getUserByLoginNameAndDomainName(anyString(), anyString())).thenReturn(null);
		ForgotPasswordEvent reqEvent = UserTestData.getForgotPasswordEvent();
		PasswordForgottenEvent response = userService.forgotPassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testForgotPasswordWithServerErr() {
		when(userDao.getUserByLoginNameAndDomainName(anyString(), anyString())).thenReturn(UserTestData.getUserWithCatissueDomain(1L));
		ForgotPasswordEvent reqEvent = UserTestData.getForgotPasswordEvent();
		doThrow(new RuntimeException()).when(userDao).saveOrUpdate(any(User.class));
		PasswordForgottenEvent response = userService.forgotPassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testValidatePassword() {
		ValidatePasswordEvent reqEvent = new ValidatePasswordEvent("Darpan22");
		PasswordValidatedEvent response = userService.validatePassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testValidatePasswordWithLessChars() {
		ValidatePasswordEvent reqEvent = new ValidatePasswordEvent("Da22");
		PasswordValidatedEvent response = userService.validatePassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(response.isValid(), Boolean.FALSE);
	}

	@Test
	public void testValidatePasswordWithoutNumFiels() {
		ValidatePasswordEvent reqEvent = new ValidatePasswordEvent("Darepadss");
		PasswordValidatedEvent response = userService.validatePassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(response.isValid(), Boolean.FALSE);
	}

	@Test
	public void testValidatePasswordWithoutCapitalLetter() {
		ValidatePasswordEvent reqEvent = new ValidatePasswordEvent("darepad22");
		PasswordValidatedEvent response = userService.validatePassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(response.isValid(), Boolean.FALSE);
	}
	
	@Test
	public void testSuccessfullPatchUser() {
		when(daoFactory.getUserDao().getUser(anyLong())).thenReturn(UserTestData.getUser(1l));
		when(daoFactory.getUserDao().isUniqueEmailAddress(anyString())).thenReturn(true);
		PatchUserEvent reqEvent = UserTestData.getPatchData();
		UserUpdatedEvent response = userService.patchUser(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testPatchUserWithInvalidAttribute() {
		when(daoFactory.getUserDao().getUser(anyLong())).thenReturn(UserTestData.getUser(1l));
		when(daoFactory.getUserDao().isUniqueEmailAddress(anyString())).thenReturn(false);
		PatchUserEvent reqEvent = UserTestData.getPatchData();
		UserUpdatedEvent response = userService.patchUser(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals("Please resolve the highlighted errors.", response.getMessage());
	}
	
	@Test
	public void testPatchUserInvalidUser() {
		when(daoFactory.getUserDao().getUser(anyLong())).thenReturn(null);
		PatchUserEvent reqEvent = UserTestData.getPatchData();
		UserUpdatedEvent response = userService.patchUser(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getUserId());
	}
	
//	@Test
//	public void testGetAllUsers() {
//		when(userDao.getAllUsers(eq(1),eq(1),"")).thenReturn(UserTestData.getUsers());
//		ReqAllUsersEvent req = new ReqAllUsersEvent();
//		req.setMaxResults(1000);
//		AllUsersEvent resp = userService.getAllUsers(req);
//		assertNotNull("response cannot be null", resp.getUsers());
//	}
	
	@Test
	public void testPatchUserServerError() {
		when(daoFactory.getUserDao().getUser(anyLong())).thenReturn(UserTestData.getUser(1l));
		when(daoFactory.getUserDao().isUniqueEmailAddress(anyString())).thenReturn(true);
		PatchUserEvent reqEvent = UserTestData.getPatchData();
		doThrow(new RuntimeException()).when(userDao).saveOrUpdate(any(User.class));
		UserUpdatedEvent response = userService.patchUser(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
	
	@Test
	public void testForSuccessfulUserCreationWithoutPrivilege() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventForUserCreationWithoutPrev();
		UserCreatedEvent response = userService.createUser(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		UserDetails createdUserDto = response.getUserDetails();
		assertEquals(reqEvent.getUserDetails().getFirstName(), createdUserDto.getFirstName());
	}

	@Test
	public void testForSuccessfulgetUser() {
		when(daoFactory.getUserDao().getUser(anyLong())).thenReturn(UserTestData.getUser(1l));
		GetUserEvent event = userService.getUser(1l);
		assertNotNull(event.getUserDetails());
	}
}
