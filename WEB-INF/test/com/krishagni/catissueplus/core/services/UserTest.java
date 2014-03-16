package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.administrative.domain.User;
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
import com.krishagni.catissueplus.core.common.events.EventStatus;

import edu.wustl.common.beans.SessionDataBean;

public class UserTest {
	
	private final String ACTIVITY_STATUS_DISABLED = "Disabled";
	
	@Mock
	private DaoFactory daoFactory;
	
	@Mock
	UserDao userDao;

	private UserFactory userFactory;

	private UserService userService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		userService = new UserServiceImpl();
		
		when(daoFactory.getUserDao()).thenReturn(userDao);
		userService = new UserServiceImpl();
		((UserServiceImpl) userService).setDaoFactory(daoFactory);
		
		userFactory = new UserFactoryImpl();
		((UserServiceImpl) userService).setUserFactory(userFactory);
	}

	@Test
	public void testForSuccessfulUserCreation() {
		CreateUserEvent reqEvent = new CreateUserEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		
		UserDetails details = new UserDetails();
		details.setActivityStatus("Active");
		details.setFirstName("firstName");
		details.setLastName("lastName");
		details.setDepartmentId(1L);
		details.setEmailId("sci@sci.com");
		details.setLoginName("sci@sci.com");
		details.setLdapId(1L);
		details.setAddressId(1L);
		details.setStartDate(new java.util.Date());		
		
		reqEvent.setUserDetails(details);
		UserCreatedEvent response = userService.createUser(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		UserDetails createdUserDto = response.getUserDetails();
		assertEquals(details.getFirstName(), createdUserDto.getFirstName());
	}

	@Test
	public void testForSuccessfulUserUpdate() {
		User user = getUser(1L);
		when(userDao.getUser(anyLong())).thenReturn(user);
		UpdateUserEvent reqEvent = new UpdateUserEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
	
		UserDetails details = getUserDetails();
		details.setId(1L);
		details.setActivityStatus("Active");
		details.setFirstName("firstName");
		details.setLastName("lastName");
		reqEvent.setUserDetails(details);
		
		UserUpdatedEvent response = userService.updateUser(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		UserDetails createdUser = response.getUserDetails();
		assertEquals(details.getFirstName(), createdUser.getFirstName());
		assertNotNull(createdUser.getDepartmentId());
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
		return user;
	}
	
	private UserDetails getUserDetails() {
		UserDetails details = new UserDetails();
		details.setActivityStatus("Active");
		details.setFirstName("firstName");
		details.setLastName("lastName");
		details.setDepartmentId(1L);
		details.setEmailId("sci@sci.com");
		details.setLoginName("sci@sci.com");
		details.setLdapId(1L);
		details.setAddressId(1L);
		details.setStartDate(new java.util.Date());		
		return details;
	}
}
