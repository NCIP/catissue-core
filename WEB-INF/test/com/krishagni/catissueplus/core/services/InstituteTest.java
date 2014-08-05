
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.impl.InstituteFactoryImpl;
import com.krishagni.catissueplus.core.administrative.events.CreateInstituteEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableInstituteEvent;
import com.krishagni.catissueplus.core.administrative.events.GetAllInstitutesEvent;
import com.krishagni.catissueplus.core.administrative.events.GetInstituteEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetails;
import com.krishagni.catissueplus.core.administrative.events.InstituteDisabledEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteGotEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllInstitutesEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateInstituteEvent;
import com.krishagni.catissueplus.core.administrative.repository.DepartmentDao;
import com.krishagni.catissueplus.core.administrative.repository.InstituteDao;
import com.krishagni.catissueplus.core.administrative.repository.PermissibleValueDao;
import com.krishagni.catissueplus.core.administrative.services.InstituteService;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.administrative.services.impl.InstituteServiceImpl;
import com.krishagni.catissueplus.core.administrative.services.impl.PermissibleValueServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.PermissibleValuesManager;
import com.krishagni.catissueplus.core.common.PermissibleValuesManagerImpl;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.privileges.domain.factory.PrivilegeErrorCode;
import com.krishagni.catissueplus.core.services.testdata.InstituteTestData;
import com.krishagni.catissueplus.core.services.testdata.PermissibleValueTestData;

public class InstituteTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	InstituteDao instituteDao;
	
	@Mock
	DepartmentDao departmentDao;
	
	@Mock
	PermissibleValueDao pvDao;

	@Mock
	CommonValidator commonValidator;
	
	PermissibleValuesManager pvManager;
	
	private PermissibleValueService pvService;

	private InstituteFactory instituteFactory;
	private InstituteService instituteService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(daoFactory.getPermissibleValueDao()).thenReturn(pvDao);
		pvService = new PermissibleValueServiceImpl();
		
		((PermissibleValueServiceImpl) pvService).setDaoFactory(daoFactory);
		pvManager = new PermissibleValuesManagerImpl();
		((PermissibleValuesManagerImpl) pvManager).setPermissibleValueSvc(pvService);
		CommonValidator.setPvManager(pvManager);
		when(pvDao.getAllValuesByAttribute(anyString())).thenReturn(PermissibleValueTestData.getPvValues());
	

		when(daoFactory.getDepartmentDao()).thenReturn(departmentDao);	
		when(daoFactory.getInstituteDao()).thenReturn(instituteDao);
		instituteService = new InstituteServiceImpl();
		((InstituteServiceImpl) instituteService).setDaoFactory(daoFactory);
		instituteFactory = new InstituteFactoryImpl();
		((InstituteServiceImpl) instituteService).setInstituteFactory(instituteFactory);
		when(instituteDao.getInstituteByName(anyString())).thenReturn(null);
	}

	@Test
	public void testForSuccessfulInstituteCreation() {
		CreateInstituteEvent reqEvent = InstituteTestData.getCreateInstituteEvent();
		InstituteCreatedEvent response = instituteService.createInstitute(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(reqEvent.getInstituteDetails().getName(), response.getInstituteDetails().getName());
	}

	@Test
	public void testInstituteCreationWithDuplicateInstituteName() {
		when(instituteDao.getInstituteByName(anyString())).thenReturn(InstituteTestData.getInstitute(1l));
		CreateInstituteEvent reqEvent = InstituteTestData.getCreateInstituteEvent();
		InstituteCreatedEvent response = instituteService.createInstitute(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(InstituteTestData.INSTITUTE_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.DUPLICATE_INSTITUTE_NAME.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testInstituteCreationWithEmptyInstituteName() {
		CreateInstituteEvent reqEvent = InstituteTestData.getCreateInstituteEventForEmptyName();
		InstituteCreatedEvent response = instituteService.createInstitute(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(InstituteTestData.INSTITUTE_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(PrivilegeErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testInstituteCreationWithServerErr() {
		CreateInstituteEvent reqEvent = InstituteTestData.getCreateInstituteEvent();

		doThrow(new RuntimeException()).when(instituteDao).saveOrUpdate(any(Institute.class));
		InstituteCreatedEvent response = instituteService.createInstitute(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForSuccessfulInstituteUpdate() {
		when(instituteDao.getInstitute(anyLong())).thenReturn(InstituteTestData.getInstitute(1L));
		UpdateInstituteEvent reqEvent = InstituteTestData.getUpdateInstituteEvent();

		InstituteUpdatedEvent response = instituteService.updateInstitute(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		InstituteDetails createdInstitute = response.getInstituteDetails();
		assertEquals(reqEvent.getInstituteDetails().getName(), createdInstitute.getName());
	}
	
	@Test
	public void testForSuccessfulInstituteUpdateForName() {
		when(instituteDao.getInstituteByName(anyString())).thenReturn(InstituteTestData.getInstitute(1L));
		UpdateInstituteEvent reqEvent = InstituteTestData.getUpdateInstituteEventForName();

		InstituteUpdatedEvent response = instituteService.updateInstitute(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		InstituteDetails createdInstitute = response.getInstituteDetails();
		assertEquals(reqEvent.getInstituteDetails().getName(), createdInstitute.getName());
	}

	@Test
	public void testForInvalidInstituteUpdate() {
		when(instituteDao.getInstitute(anyLong())).thenReturn(null);
		UpdateInstituteEvent reqEvent = InstituteTestData.getUpdateInstituteEvent();

		InstituteUpdatedEvent response = instituteService.updateInstitute(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getInstituteId());
	}
	
	@Test
	public void testForInvalidInstituteUpdateInForInvalidName() {
		when(instituteDao.getInstituteByName(anyString())).thenReturn(null);
		UpdateInstituteEvent reqEvent = InstituteTestData.getUpdateInstituteEventForName();

		InstituteUpdatedEvent response = instituteService.updateInstitute(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getName());
	}

	@Test
	public void testInstituteUpdateWithServerErr() {
		when(instituteDao.getInstitute(anyLong())).thenReturn(InstituteTestData.getInstitute(1L));
		UpdateInstituteEvent reqEvent = InstituteTestData.getUpdateInstituteEvent();

		doThrow(new RuntimeException()).when(instituteDao).saveOrUpdate(any(Institute.class));
		InstituteUpdatedEvent response = instituteService.updateInstitute(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
	
	@Test
	public void testInstituteUpdationWithEmptyInstituteName() {
		when(instituteDao.getInstitute(anyLong())).thenReturn(InstituteTestData.getInstitute(1L));
		UpdateInstituteEvent reqEvent = InstituteTestData.getUpdateInstituteEventForEmptyName();
		InstituteUpdatedEvent response = instituteService.updateInstitute(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(InstituteTestData.INSTITUTE_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(PrivilegeErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testGetAllInstitutes() {
		when(instituteDao.getAllInstitutes(eq(1000))).thenReturn(InstituteTestData.getInstitutes());
		ReqAllInstitutesEvent reqEvent = InstituteTestData.getAllInstitutesEvent();
		GetAllInstitutesEvent response = instituteService.getInstitutes(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(2, response.getDetails().size());
	}
	
	@Test
	public void testGetInstituteById() {
		when(instituteDao.getInstitute(anyLong())).thenReturn(InstituteTestData.getInstitute(1l));
		GetInstituteEvent reqEvent = InstituteTestData.getInstituteEvent();
		InstituteGotEvent response = instituteService.getInstitute(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testGetInstituteWithWrongInstWithId() {
		when(instituteDao.getInstitute(anyLong())).thenReturn(null);
		GetInstituteEvent reqEvent = InstituteTestData.getInstituteEvent();
		InstituteGotEvent response = instituteService.getInstitute(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}
	
	@Test
	public void testGetInstituteByName() {
		when(instituteDao.getInstituteByName(anyString())).thenReturn(InstituteTestData.getInstitute(1l));
		GetInstituteEvent reqEvent = InstituteTestData.getInstituteEventForName();
		InstituteGotEvent response = instituteService.getInstitute(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(response.getDetails());
	}
	
	@Test
	public void testGetInstituteWithWrongInst() {
		when(instituteDao.getInstituteByName(anyString())).thenReturn(null);
		GetInstituteEvent reqEvent = InstituteTestData.getInstituteEventForName();
		InstituteGotEvent response = instituteService.getInstitute(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getName());
	}
	
	@Test
	public void testInstituteGetWithEmptyInstituteName() {
		when(instituteDao.getInstitute(anyLong())).thenReturn(InstituteTestData.getInstitute(1L));
		UpdateInstituteEvent reqEvent = InstituteTestData.getUpdateInstituteEventForEmptyName();
		InstituteUpdatedEvent response = instituteService.updateInstitute(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(InstituteTestData.INSTITUTE_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(PrivilegeErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testForInvalidInstituteDisable() {
		when(instituteDao.getInstitute(anyLong())).thenReturn(null);
		DisableInstituteEvent reqEvent = InstituteTestData.getDisableInstituteEvent();
		InstituteDisabledEvent response = instituteService.deleteInstitute(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}
	
	@Test
	public void testForInvalidInstituteDisableWithName() {
		when(instituteDao.getInstituteByName(anyString())).thenReturn(null);
		DisableInstituteEvent reqEvent = InstituteTestData.getDisableInstituteEventForName();
		InstituteDisabledEvent response = instituteService.deleteInstitute(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getName());
	}

	@Test
	public void testSuccessfulInstituteDisable() {
		DisableInstituteEvent reqEvent = InstituteTestData.getDisableInstituteEvent();
		Institute instituteToDelete = InstituteTestData.getInstitute(1L);
		when(instituteDao.getInstitute(anyLong())).thenReturn(instituteToDelete);
		InstituteDisabledEvent response = instituteService.deleteInstitute(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(InstituteTestData.ACTIVITY_STATUS_DISABLED, instituteToDelete.getActivityStatus());
	}
	
	@Test
	public void testInstituteDisableWithReference() {
		when(instituteDao.getInstitute(anyLong())).thenReturn(InstituteTestData.getInstituteForDisable(1l));
		DisableInstituteEvent reqEvent = InstituteTestData.getDisableInstituteEvent();
		InstituteDisabledEvent response = instituteService.deleteInstitute(reqEvent);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(UserErrorCode.REFERENCED_ATTRIBUTE.message(), response.getMessage());
	}

	@Test
	public void testInstituteDisableWithServerErr() {
		when(instituteDao.getInstitute(anyLong())).thenReturn(InstituteTestData.getInstitute(1l));
		DisableInstituteEvent reqEvent = InstituteTestData.getDisableInstituteEvent();
		doThrow(new RuntimeException()).when(instituteDao).saveOrUpdate(any(Institute.class));
		InstituteDisabledEvent response = instituteService.deleteInstitute(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
	
	@Test
	public void testGetInstituteByIdWithServerError() {
		GetInstituteEvent reqEvent = InstituteTestData.getInstituteEvent();
		doThrow(new RuntimeException()).when(instituteDao).getInstitute(anyLong());
		InstituteGotEvent response = instituteService.getInstitute(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}	
}
