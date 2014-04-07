
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.auth.events.AuthenticateUserEvent;
import com.krishagni.catissueplus.core.auth.events.UserAuthenticatedEvent;
import com.krishagni.catissueplus.core.auth.services.CatissueAuthService;
import com.krishagni.catissueplus.core.auth.services.UserAuthenticationService;
import com.krishagni.catissueplus.core.auth.services.impl.CatissueAuthServiceImpl;
import com.krishagni.catissueplus.core.auth.services.impl.UserAuthenticationServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.AuthenticationTestData;

public class AuthenticationTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	UserDao userDao;

	private CatissueAuthService caAuthService;

	private UserAuthenticationService userAuthService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(daoFactory.getUserDao()).thenReturn(userDao);
		
		caAuthService = new CatissueAuthServiceImpl();
		((CatissueAuthServiceImpl) caAuthService).setDaoFactory(daoFactory);

		userAuthService = new UserAuthenticationServiceImpl();
		((UserAuthenticationServiceImpl) userAuthService).setDaoFactory(daoFactory);
		((UserAuthenticationServiceImpl) userAuthService).setCatissueAuthService(caAuthService);

		when(userDao.getOldPasswordsByLoginName(anyString())).thenReturn(AuthenticationTestData.getOldPasswordList());
		when(userDao.getUserByLoginName(anyString())).thenReturn(AuthenticationTestData.getUser(1l));
	}

	@Test
	public void testForSuccessfulUserAuthentication() {
		AuthenticateUserEvent reqEvent = AuthenticationTestData.getAuthenticateUserEvent();
		UserAuthenticatedEvent response = userAuthService.authenticateUser(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(response.getUserDetails());

	}

	@Test
	public void testPasswordReSetWithBlankLoginId() {
		AuthenticateUserEvent reqEvent = AuthenticationTestData.getAuthenticateUserEventWithEmptyLoginID();
		UserAuthenticatedEvent response = userAuthService.authenticateUser(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(UserErrorCode.MISSING_ATTR_VALUE.message(), response.getMessage());
	}

	@Test
	public void testPasswordReSetWithBlankPassword() {
		AuthenticateUserEvent reqEvent = AuthenticationTestData.getAuthenticateUserEventWithEmptyPass();
		UserAuthenticatedEvent response = userAuthService.authenticateUser(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(UserErrorCode.MISSING_ATTR_VALUE.message(), response.getMessage());
	}
	
	@Test
	public void testUserAuthenticationWithServerErr() {
		AuthenticateUserEvent reqEvent = AuthenticationTestData.getAuthenticateUserEvent();
		doThrow(new RuntimeException()).when(userDao).getOldPasswordsByLoginName(anyString());
		UserAuthenticatedEvent response = userAuthService.authenticateUser(reqEvent);
		
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
	
	@Test
	public void testUserAuthenticationWithWrongPass() {
		AuthenticateUserEvent reqEvent = AuthenticationTestData.getAuthenticateUserEventWrongPass();
		UserAuthenticatedEvent response = userAuthService.authenticateUser(reqEvent);
		
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_AUTHENTICATED, response.getStatus());
	}
	
	@Test
	public void testUserAuthenticationWithNullUser() {
		when(userDao.getUserByLoginName(anyString())).thenReturn(null);
		AuthenticateUserEvent reqEvent = AuthenticationTestData.getAuthenticateUserEvent();
		UserAuthenticatedEvent response = userAuthService.authenticateUser(reqEvent);
		
		assertNotNull("response cannot be null", response);
		assertEquals(UserErrorCode.NOT_FOUND.message(), response.getMessage());
	}
	
	@Test
	public void testUserAuthenticationWithInActiveUser() {
		when(userDao.getUserByLoginName(anyString())).thenReturn(AuthenticationTestData.getNonActiveUser(1l));
		AuthenticateUserEvent reqEvent = AuthenticationTestData.getAuthenticateUserEvent();
		UserAuthenticatedEvent response = userAuthService.authenticateUser(reqEvent);
		
		assertNotNull("response cannot be null", response);
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}

}