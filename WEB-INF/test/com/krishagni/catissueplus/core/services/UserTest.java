package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.impl.UserFactoryImpl;
import com.krishagni.catissueplus.core.administrative.events.CreateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UserCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.administrative.events.UserUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.administrative.services.impl.UserServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.administrative.repository.DepartmentDao;
import com.krishagni.catissueplus.core.common.events.EventStatus;

import edu.wustl.common.beans.SessionDataBean;

public class UserTest {
	
	private final String ACTIVITY_STATUS_DISABLED = "Disabled";
	
	private final String FIRST_NAME = "first name";

	private final String LAST_NAME = "last name";

	private final String LOGIN_NAME = "login name";

	private final String EMAIL_ADDRESS = "email address";
	
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
		when(departmentDao.getDepartment(anyString())).thenReturn(getDeparment("Chemical"));
	}

	@Test
	public void testForSuccessfulUserCreation() {
		CreateUserEvent reqEvent = getUserDetailsForUserCreation();
		when(userDao.isUniqueLoginName("admin@admin.com")).thenReturn(Boolean.TRUE);		
		
		UserCreatedEvent response = userService.createUser(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		UserDetails createdUserDto = response.getUserDetails();
		assertEquals(reqEvent.getUserDetails().getFirstName(), createdUserDto.getFirstName());
	}
	
	@Test
	public void testUserCreationWithDuplicateLoginName() {
		
		CreateUserEvent reqEvent = getUserDetailsForUserCreation();
		when(userDao.isUniqueLoginName("admin@admin.com")).thenReturn(Boolean.FALSE);		
		
		UserCreatedEvent response = userService.createUser(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
        assertEquals(LOGIN_NAME,response.getErroneousFields()[0].getFieldName());
        assertEquals(UserErrorCode.DUPLICATE_LOGIN_NAME.message(),response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testUserCreationWithEmptyLoginName() {
		CreateUserEvent reqEvent = getUserDetailsWithEmptyLoginName();
		when(userDao.isUniqueLoginName("")).thenReturn(Boolean.TRUE);
		
		UserCreatedEvent response = userService.createUser(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
        assertEquals(LOGIN_NAME,response.getErroneousFields()[0].getFieldName());
        assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(),response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testUserCreationWithEmptyFirstName() {
		CreateUserEvent reqEvent = getUserDetailsWithEmptyFirstName();
		when(userDao.isUniqueLoginName("admin@admin.com")).thenReturn(Boolean.TRUE);
		
		UserCreatedEvent response = userService.createUser(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
        assertEquals(FIRST_NAME,response.getErroneousFields()[0].getFieldName());
        assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(),response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testUserCreationWithEmptyLastName() {
		CreateUserEvent reqEvent = getUserDetailsWithEmptyLastName();
		when(userDao.isUniqueLoginName("admin@admin.com")).thenReturn(Boolean.TRUE);
		
		UserCreatedEvent response = userService.createUser(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
        assertEquals(LAST_NAME,response.getErroneousFields()[0].getFieldName());
        assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(),response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testUserCreationWithInvalidEmailAddress() {
		CreateUserEvent reqEvent = getUserDetailsWithInvalidEmail();
		when(userDao.isUniqueLoginName("admin@admin.com")).thenReturn(Boolean.TRUE);
		
		UserCreatedEvent response = userService.createUser(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
        assertEquals(EMAIL_ADDRESS,response.getErroneousFields()[0].getFieldName());
        assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(),response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testForSuccessfulUserUpdate() {
		User user = getUser(1L);
		when(userDao.getUser(anyLong())).thenReturn(user);
		
		UserDetails details = getUserDetailsForUpdate();
		UpdateUserEvent reqEvent = new UpdateUserEvent(details, anyLong());
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setUserDetails(details);	
		when(userDao.isUniqueLoginName("admin@admin.com")).thenReturn(Boolean.TRUE);
		
		UserUpdatedEvent response = userService.updateUser(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		
		UserDetails createdUser = response.getUserDetails();
		assertEquals(details.getFirstName(), createdUser.getFirstName());
		assertNotNull(createdUser.getDeptName());
		assertEquals("firstName", createdUser.getFirstName());
	}
	
	@Test
	public void testSuccessfulUserDelete(){
		Long userId = 1l;
		DeleteUserEvent event = new DeleteUserEvent();
		event.setId(userId);
		event.setIncludeChildren(true);
		event.setSessionDataBean(getSessionDataBean());
		
		User userToDelete = getUser(userId);
		when(userDao.getUser(anyLong())).thenReturn(userToDelete);
		UserDeletedEvent response = userService.delete(event);
		assertNotNull("response cannot be null",response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(ACTIVITY_STATUS_DISABLED, userToDelete.getActivityStatus());
	}
	
	private SessionDataBean getSessionDataBean() {
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setAdmin(true);
		sessionDataBean.setCsmUserId("1");
		sessionDataBean.setFirstName("admin");
		sessionDataBean.setIpAddress("127.0.0.1");
		sessionDataBean.setLastName("admin");
		sessionDataBean.setUserId(1L);
		sessionDataBean.setUserName("admin@admin.com");
		return sessionDataBean;
	}
	
	private User getUser(Long id) {
		User user = new User();
		user.setId(id);
		user.setFirstName("firstName1");
		user.setLastName("lastName1");
		user.setLoginName("admin@admin.com");
		return user;
	}
	
	private UserDetails getUserDetailsForUpdate() {
		CreateUserEvent reqEvent = new CreateUserEvent(null);
		reqEvent.setSessionDataBean(getSessionDataBean());

		UserDetails details = new UserDetails();
		details.setActivityStatus("Active");
		details.setFirstName("firstName");
		details.setLastName("lastName");
		details.setDeptName("Chemical");
		details.setEmailAddress("sci@sci.com");
		details.setLoginName("admin@admin.com");
		details.setLdapId(1L);
		details.setStartDate(new java.util.Date());		
		
		return details;
	}
	
	private  CreateUserEvent getUserDetails() {
		CreateUserEvent reqEvent = new CreateUserEvent(null);
		reqEvent.setSessionDataBean(getSessionDataBean());

		UserDetails details = new UserDetails();
		details.setActivityStatus("Active");
		details.setFirstName("firstName");
		details.setLastName("lastName");
		details.setDeptName("Chemical");
		details.setEmailAddress("sci@sci.com");
		details.setLoginName("admin@admin.com");
		details.setLdapId(1L);
		details.setStartDate(new java.util.Date());		
		
		reqEvent.setUserDetails(details);
		return reqEvent;
	}
	
	private CreateUserEvent getUserDetailsWithInvalidEmail() {
		CreateUserEvent reqEvent = getUserDetails();
		UserDetails details = reqEvent.getUserDetails();
		details.setEmailAddress("admin");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}

	private CreateUserEvent getUserDetailsForUserCreation() {
		CreateUserEvent reqEvent = getUserDetails();
		UserDetails details = reqEvent.getUserDetails();
		reqEvent.setUserDetails(details);
		return reqEvent;
	}
	
	private CreateUserEvent getUserDetailsWithEmptyLoginName() {
		CreateUserEvent reqEvent = getUserDetails();
		UserDetails details = reqEvent.getUserDetails();
		details.setLoginName("");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}
	
	private CreateUserEvent getUserDetailsWithEmptyFirstName() {
		CreateUserEvent reqEvent = getUserDetails();
		UserDetails details = reqEvent.getUserDetails();
		details.setFirstName("");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}
	
	private CreateUserEvent getUserDetailsWithEmptyLastName() {
		CreateUserEvent reqEvent = getUserDetails();
		UserDetails details = reqEvent.getUserDetails();
		details.setLastName("");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}
	
	private Department getDeparment(String name) {
		Department department = new Department();
		department.setId(10l);
		department.setName(name);
		return department;
	}

}
