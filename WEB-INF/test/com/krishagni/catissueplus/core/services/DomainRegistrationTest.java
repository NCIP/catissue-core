
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.factory.AuthErrorCode;
import com.krishagni.catissueplus.core.auth.domain.factory.DomainRegistrationFactory;
import com.krishagni.catissueplus.core.auth.domain.factory.LdapFactory;
import com.krishagni.catissueplus.core.auth.domain.factory.impl.DomainRegistrationFactoryImpl;
import com.krishagni.catissueplus.core.auth.domain.factory.impl.LdapFactoryImpl;
import com.krishagni.catissueplus.core.auth.events.DomainRegisteredEvent;
import com.krishagni.catissueplus.core.auth.events.LdapDetails;
import com.krishagni.catissueplus.core.auth.events.RegisterDomainEvent;
import com.krishagni.catissueplus.core.auth.repository.DomainDao;
import com.krishagni.catissueplus.core.auth.services.DomainRegistrationService;
import com.krishagni.catissueplus.core.auth.services.impl.DomainRegistrationServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.DomainRegistrationTestData;

public class DomainRegistrationTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	DomainDao domainDao;

	private DomainRegistrationService domainRegService;

	private DomainRegistrationFactory domainRegFactory;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(daoFactory.getDomainDao()).thenReturn(domainDao);
		domainRegService = new DomainRegistrationServiceImpl();
		((DomainRegistrationServiceImpl) domainRegService).setDaoFactory(daoFactory);

		domainRegFactory = new DomainRegistrationFactoryImpl();
		LdapFactory ldapFactory = new LdapFactoryImpl();
		((DomainRegistrationServiceImpl) domainRegService).setDomainRegFactory(domainRegFactory);
		((DomainRegistrationFactoryImpl) domainRegFactory).setDaoFactory(daoFactory);
		((DomainRegistrationFactoryImpl) domainRegFactory).setLdapFactory(ldapFactory);
		when(domainDao.isUniqueAuthDomainName(anyString())).thenReturn(Boolean.TRUE);
		when(domainDao.getAuthProviderByType(anyString())).thenReturn(DomainRegistrationTestData.getAuthProviderForLdap());
	}

	@Test
	public void testForSuccessfullDomainRegistration() {
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventForLdap();
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		LdapDetails createdLdapDto = response.getDomainDetails().getLdapDetails();
		assertNotNull(createdLdapDto);
	}
	
	@Test
	public void testForSuccessfullDomainRegistrationForInvalidIMPLClass() {
		when(domainDao.getAuthProviderByType("custom")).thenReturn(null);
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventForCustom();
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testLdapDomainAdditionWithDuplicateDomainName() {
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventForLdap();
		when(domainDao.isUniqueAuthDomainName(anyString())).thenReturn(Boolean.FALSE);
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(AuthErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}
	
	@Test
	public void testLdapDomainAdditionWithNullAuthPrivder() {
		when(domainDao.getAuthProviderByType("ldap")).thenReturn(null);
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventForLdap();
		when(domainDao.isUniqueAuthDomainName(anyString())).thenReturn(Boolean.FALSE);
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(AuthErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}

	@Test
	public void testLdapDomainAdditionWithEmptyPassword() {
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventWithEmptyPassword();
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(DomainRegistrationTestData.PASSWORD, response.getErroneousFields()[0].getFieldName());
		assertEquals(AuthErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapDomainAdditionWithEmptyIdField() {
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventWithEmptyIdField();
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(DomainRegistrationTestData.ID_FIELD, response.getErroneousFields()[0].getFieldName());
		assertEquals(AuthErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapDomainAdditionWithEmptyLdapPort() {
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventWithNullPort();
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(DomainRegistrationTestData.PORT, response.getErroneousFields()[0].getFieldName());
		assertEquals(AuthErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapDomainAdditionWithEmptyHost() {
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventWithEmptyHost();
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(DomainRegistrationTestData.HOST, response.getErroneousFields()[0].getFieldName());
		assertEquals(AuthErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapDomainAdditionWithEmptyDirectory() {
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventWithEmptyDirectoryContext();
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(DomainRegistrationTestData.DIRECTORY_CONTEXT, response.getErroneousFields()[0].getFieldName());
		assertEquals(AuthErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapDomainAdditionWithLoginName() {
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventWithEmptyLoginName();
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(DomainRegistrationTestData.LOGIN_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(AuthErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapDomainAdditionWithWrongLdapInfo() {
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventWithwrongLdapInfo();
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertEquals(AuthErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}

	@Test
	public void testLdapDomainAdditionWithServerErr() {
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventForLdap();
		doThrow(new RuntimeException()).when(domainDao).saveOrUpdate(any(AuthDomain.class));
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testLdapDomainAdditionWithEmptySearchBaseDir() {
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventWithEmptySearchBaseDir();
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(DomainRegistrationTestData.SEARCH_BASE_DIR, response.getErroneousFields()[0].getFieldName());
		assertEquals(AuthErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapDomainAdditionWithEmptyFilterString() {
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventWithEmptyFilterString();
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(DomainRegistrationTestData.FILTER_STRING, response.getErroneousFields()[0].getFieldName());
		assertEquals(AuthErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapDomainAdditionWithEmptyDomainName() {
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventWithEmptyDomainName();
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(AuthErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}

	@Test
	public void testLdapDomainAdditionWithEmptyImplClass() {
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventWithEmptyImplClass();
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(AuthErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}

	@Test
	public void testLdapDomainAdditionWithNullLdapDetails() {
		RegisterDomainEvent reqEvent = DomainRegistrationTestData.getRegisterDomainEventWithNullLdapDetails();
		DomainRegisteredEvent response = domainRegService.registerDomain(reqEvent);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(AuthErrorCode.INVALID_ATTR_VALUE.message(), response.getMessage());
	}

}
