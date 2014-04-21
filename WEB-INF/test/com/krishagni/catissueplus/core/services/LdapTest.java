
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.auth.domain.Ldap;
import com.krishagni.catissueplus.core.auth.domain.factory.LdapRegistrationErrorCode;
import com.krishagni.catissueplus.core.auth.domain.factory.LdapRegistrationFactory;
import com.krishagni.catissueplus.core.auth.domain.factory.impl.LdapRegistrationFactoryImpl;
import com.krishagni.catissueplus.core.auth.events.AddLdapEvent;
import com.krishagni.catissueplus.core.auth.events.LdapAddedEvent;
import com.krishagni.catissueplus.core.auth.events.LdapDetails;
import com.krishagni.catissueplus.core.auth.repository.LdapDao;
import com.krishagni.catissueplus.core.auth.services.LdapRegistrationService;
import com.krishagni.catissueplus.core.auth.services.impl.LdapRegistrationServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.LdapTestData;

public class LdapTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	LdapDao ldapDao;

	private LdapRegistrationFactory ldapRegFactory;

	private LdapRegistrationService ldapRegService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(daoFactory.getLdapDao()).thenReturn(ldapDao);
		ldapRegService = new LdapRegistrationServiceImpl();
		((LdapRegistrationServiceImpl) ldapRegService).setDaoFactory(daoFactory);

		ldapRegFactory = new LdapRegistrationFactoryImpl();
		((LdapRegistrationFactoryImpl) ldapRegFactory).setDaoFactory(daoFactory);
		((LdapRegistrationServiceImpl) ldapRegService).setLdapRegFactory(ldapRegFactory);
		when(ldapDao.isUniqueLdapName("Myldap")).thenReturn(Boolean.TRUE);
	}

	@Test
	public void testForSuccessfulldapAddition() {
		AddLdapEvent reqEvent = LdapTestData.getAddLdapEvent();
		LdapAddedEvent response = ldapRegService.addLdap(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		LdapDetails createdLdapDto = response.getLdapDetails();
		assertEquals(reqEvent.getLdapDetails().getLdapName(), createdLdapDto.getLdapName());
	}

	@Test
	public void testLdapAdditionWithDuplicateLdapName() {
		AddLdapEvent reqEvent = LdapTestData.getAddLdapEvent();
		when(ldapDao.isUniqueLdapName("Myldap")).thenReturn(Boolean.FALSE);
		LdapAddedEvent response = ldapRegService.addLdap(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(LdapTestData.LDAP_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(LdapRegistrationErrorCode.DUPLICATE_LDAP_NAME.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapAdditionWithEmptyLdapName() {
		AddLdapEvent reqEvent = LdapTestData.getAddLdapEventWithEmptyLdapName();
		when(ldapDao.isUniqueLdapName("")).thenReturn(Boolean.TRUE);
		LdapAddedEvent response = ldapRegService.addLdap(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(LdapTestData.LDAP_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(LdapRegistrationErrorCode.MISSING_ATTR_VALUE.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapAdditionWithEmptyPassword() {
		AddLdapEvent reqEvent = LdapTestData.getAddLdapEventWithEmptyPassword();
		LdapAddedEvent response = ldapRegService.addLdap(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(LdapTestData.PASSWORD, response.getErroneousFields()[0].getFieldName());
		assertEquals(LdapRegistrationErrorCode.MISSING_ATTR_VALUE.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapAdditionWithEmptyIdField() {
		AddLdapEvent reqEvent = LdapTestData.getAddLdapEventWithEmptyIdField();
		LdapAddedEvent response = ldapRegService.addLdap(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(LdapTestData.ID_FIELD, response.getErroneousFields()[0].getFieldName());
		assertEquals(LdapRegistrationErrorCode.MISSING_ATTR_VALUE.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapAdditionWithEmptyLdapPort() {
		AddLdapEvent reqEvent = LdapTestData.getAddLdapEventWithNullPort();
		LdapAddedEvent response = ldapRegService.addLdap(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(LdapTestData.PORT, response.getErroneousFields()[0].getFieldName());
		assertEquals(LdapRegistrationErrorCode.MISSING_ATTR_VALUE.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapAdditionWithEmptyHost() {
		AddLdapEvent reqEvent = LdapTestData.getAddLdapEventWithEmptyHost();
		LdapAddedEvent response = ldapRegService.addLdap(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(LdapTestData.HOST, response.getErroneousFields()[0].getFieldName());
		assertEquals(LdapRegistrationErrorCode.MISSING_ATTR_VALUE.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapAdditionWithEmptyDirectory() {
		AddLdapEvent reqEvent = LdapTestData.getAddLdapEventWithEmptyDirectoryContext();
		LdapAddedEvent response = ldapRegService.addLdap(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(LdapTestData.DIRECTORY_CONTEXT, response.getErroneousFields()[0].getFieldName());
		assertEquals(LdapRegistrationErrorCode.MISSING_ATTR_VALUE.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapAdditionWithEmptyLoginName() {
		AddLdapEvent reqEvent = LdapTestData.getAddLdapEventWithEmptyLoginName();
		LdapAddedEvent response = ldapRegService.addLdap(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(LdapTestData.LOGIN_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(LdapRegistrationErrorCode.MISSING_ATTR_VALUE.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapAdditionWithWrongLdapInfo() {
		AddLdapEvent reqEvent = LdapTestData.getAddLdapEventWithwrongLdapInfo();
		LdapAddedEvent response = ldapRegService.addLdap(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(LdapTestData.LDAP, response.getErroneousFields()[0].getFieldName());
		assertEquals(LdapRegistrationErrorCode.AUTHENTICATION_FAILURE.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testLdapAdditionWithServerErr() {
		AddLdapEvent reqEvent = LdapTestData.getAddLdapEvent();
		doThrow(new RuntimeException()).when(ldapDao).saveOrUpdate(any(Ldap.class));
		LdapAddedEvent response = ldapRegService.addLdap(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testLdapAdditionWithEmptySearchBaseDir() {
		AddLdapEvent reqEvent = LdapTestData.getAddLdapEventWithEmptySearchBaseDir();
		LdapAddedEvent response = ldapRegService.addLdap(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(LdapTestData.SEARCH_BASE_DIR, response.getErroneousFields()[0].getFieldName());
		assertEquals(LdapRegistrationErrorCode.MISSING_ATTR_VALUE.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testLdapAdditionWithEmptyFilterString() {
		AddLdapEvent reqEvent = LdapTestData.getAddLdapEventWithEmptyFilterString();
		LdapAddedEvent response = ldapRegService.addLdap(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(LdapTestData.FILTER_STRING, response.getErroneousFields()[0].getFieldName());
		assertEquals(LdapRegistrationErrorCode.MISSING_ATTR_VALUE.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

}
