
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
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
import com.krishagni.catissueplus.core.administrative.repository.DepartmentDao;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.administrative.services.impl.UserServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.UserTestData;

public class UserTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	UserDao userDao;

	@Mock
	DepartmentDao departmentDao;

	private UserFactory userFactory;

	private UserService userService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		when(daoFactory.getUserDao()).thenReturn(userDao);
		userService = new UserServiceImpl();
		((UserServiceImpl) userService).setDaoFactory(daoFactory);

		userFactory = new UserFactoryImpl();
		((UserFactoryImpl) userFactory).setDaoFactory(daoFactory);
		((UserServiceImpl) userService).setUserFactory(userFactory);

		when(daoFactory.getDepartmentDao()).thenReturn(departmentDao);
		when(departmentDao.getDepartment(anyString())).thenReturn(UserTestData.getDeparment("Chemical"));
		when(daoFactory.getUserDao().isValidLdapId(anyLong())).thenReturn(true);

		when(daoFactory.getUserDao()).thenReturn(userDao);
		when(userDao.isUniqueLoginName("admin@admin.com")).thenReturn(Boolean.TRUE);
		when(userDao.isUniqueEmailAddress("sci@sci.com")).thenReturn(Boolean.TRUE);
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
	public void testUserCreationWithDuplicateLoginName() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventForUserCreation();
		when(userDao.isUniqueLoginName("admin@admin.com")).thenReturn(Boolean.FALSE); 
		UserCreatedEvent response = userService.createUser(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(UserTestData.LOGIN_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.DUPLICATE_LOGIN_NAME.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testUserCreationWithEmptyLoginName() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventWithEmptyLoginName();
		when(userDao.isUniqueLoginName("")).thenReturn(Boolean.TRUE);
		when(userDao.isUniqueEmailAddress("sci@sci.com")).thenReturn(Boolean.TRUE);

		UserCreatedEvent response = userService.createUser(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(UserTestData.LOGIN_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testUserCreationWithEmptyEmail() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventWithEmptyEmail();
		when(userDao.isUniqueLoginName("admin@admin.com")).thenReturn(Boolean.TRUE);
		when(userDao.isUniqueEmailAddress("")).thenReturn(Boolean.TRUE);

		UserCreatedEvent response = userService.createUser(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(2, response.getErroneousFields().length);
		assertEquals(UserTestData.EMAIL_ADDRESS, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testUserCreationWithEmptyFirstName() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventWithEmptyFirstName();
		when(userDao.isUniqueLoginName("admin@admin.com")).thenReturn(Boolean.TRUE);
		when(userDao.isUniqueEmailAddress("sci@sci.com")).thenReturn(Boolean.TRUE);

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
	public void testUserCreationWithInvalidLdapId() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEvent();
		when(daoFactory.getUserDao().isValidLdapId(anyLong())).thenReturn(false);
		UserCreatedEvent response = userService.createUser(reqEvent);
		
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(UserTestData.LDAP_ID, response.getErroneousFields()[0].getFieldName());
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
		assertEquals(UserErrorCode.DUPLICATE_EMAIL_ADDRESS.message(), response.getErroneousFields()[0].getErrorMessage());
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
		when(userDao.isUniqueEmailAddress("sci@sci.com")).thenReturn(Boolean.FALSE);
		UserUpdatedEvent response = userService.updateUser(reqEvent);
		
		assertEquals(UserTestData.EMAIL_ADDRESS, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.DUPLICATE_EMAIL_ADDRESS.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testForUserUpdateWithChangedLoginName() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		UpdateUserEvent reqEvent = UserTestData.getUpadteUserEventWithLNUpdate();

		UserUpdatedEvent response = userService.updateUser(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(UserTestData.LOGIN_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.CHANGE_IN_LOGIN_NAME.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testForInvalidUserUpdate() {
		when(userDao.getUser(anyLong())).thenReturn(null);
		UpdateUserEvent reqEvent = UserTestData.getUpdateUserEvent();

		UserUpdatedEvent response = userService.updateUser(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
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
		when(userDao.getUser(anyLong())).thenReturn(null);
		CloseUserEvent reqEvent = UserTestData.getCloseUserEvent();
		reqEvent.setSessionDataBean(UserTestData.getSessionDataBean());

		UserClosedEvent response = userService.closeUser(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
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
	public void testUserCreationWithInvalidDepartment() {
		CreateUserEvent reqEvent = UserTestData.getCreateUserEventForUserCreation();
		when(departmentDao.getDepartment(anyString())).thenReturn(null);

		UserCreatedEvent response = userService.createUser(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(UserTestData.DEPARTMENT, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.NOT_FOUND.message(), response.getErroneousFields()[0].getErrorMessage());

	}
	
	@Test
	public void testSuccessfullPasswordSet() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEvent();
		
		PasswordUpdatedEvent response = userService.setPassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		PasswordDetails passDto = response.getPasswordDetails();
		assertEquals("krishagni", passDto.getNewPassword());
	}
	
	@Test
	public void testPasswordSetWithBlankNewPassword() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEventForBlankNewPass();
		
		PasswordUpdatedEvent response = userService.setPassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}
	
	@Test
	public void testPasswordSetWithBlankConfirmPassword() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEventForBlankConfirmPass();
		
		PasswordUpdatedEvent response = userService.setPassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}
	
	@Test
	public void testPasswordReSetWithBlankOldPassword() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEventForBlankOldPass();
		
		PasswordUpdatedEvent response = userService.resetPassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}
	
	@Test
	public void testPasswordReSetWithInvalidOldPassword() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		when(userDao.getOldPasswords(anyLong())).thenReturn(UserTestData.getOldPasswordList());
		
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEvent();
		
		PasswordUpdatedEvent response = userService.resetPassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}
	
	@Test
	public void testForPasswordSetWithInvalidUser() {
		when(userDao.getUser(anyLong())).thenReturn(null);
		UpdatePasswordEvent reqEvent =  UserTestData.getUpdatePasswordEvent();
		PasswordUpdatedEvent response = userService.setPassword(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}
	
	@Test
	public void testForPasswordResetWithInvalidUser() {
		when(userDao.getUser(anyLong())).thenReturn(null);
		when(userDao.getOldPasswords(anyLong())).thenReturn(UserTestData.getOldPasswordList());
		UpdatePasswordEvent reqEvent =  UserTestData.getUpdatePasswordEventForReSet();
		PasswordUpdatedEvent response = userService.resetPassword(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}
	
	@Test
	public void testSuccessfullPasswordReset() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEventForReSet();
		when(userDao.getOldPasswords(anyLong())).thenReturn(UserTestData.getOldPasswordList());

		PasswordUpdatedEvent response = userService.resetPassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		PasswordDetails passDto = response.getPasswordDetails();
		assertEquals("krishagni", passDto.getNewPassword());
		assertEquals("catissue", passDto.getOldPassword());
	}
	
	@Test
	public void testSuccessfullPasswordSetWithDiffPasswords () {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEventForDiffPass();

		PasswordUpdatedEvent response = userService.setPassword(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}
	
	@Test
	public void testSuccessfullPasswordSetWithDiffTokens () {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEventForDiffTokens();

		PasswordUpdatedEvent response = userService.setPassword(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}
	
	@Test
	public void testPasswordSetWithServerErr() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		UpdatePasswordEvent reqEvent =  UserTestData.getUpdatePasswordEvent();

		doThrow(new RuntimeException()).when(userDao).saveOrUpdate(any(User.class));
		PasswordUpdatedEvent response = userService.setPassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
	
	@Test
	public void testPasswordReSetWithServerErr() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		UpdatePasswordEvent reqEvent = UserTestData.getUpdatePasswordEventForReSet();
		when(userDao.getOldPasswords(anyLong())).thenReturn(UserTestData.getOldPasswordList());

		doThrow(new RuntimeException()).when(userDao).saveOrUpdate(any(User.class));
		PasswordUpdatedEvent response = userService.resetPassword(reqEvent);
		
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
	
	@Test
	public void testForInvalidUserForgotPassword() {
		when(userDao.getUser(anyLong())).thenReturn(null);
		ForgotPasswordEvent reqEvent = UserTestData.getForgotPasswordEvent();
		reqEvent.setSessionDataBean(UserTestData.getSessionDataBean());

		PasswordForgottenEvent response = userService.forgotPassword(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testSuccessfulForgotPassword() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		ForgotPasswordEvent reqEvent = UserTestData.getForgotPasswordEvent();
		PasswordForgottenEvent response = userService.forgotPassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(response.getUserDetails().getPasswordToken());
	}

	@Test
	public void testForgotPasswordWithServerErr() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		ForgotPasswordEvent reqEvent = UserTestData.getForgotPasswordEvent();
		doThrow(new RuntimeException()).when(userDao).saveOrUpdate(any(User.class));
		PasswordForgottenEvent response = userService.forgotPassword(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
}
