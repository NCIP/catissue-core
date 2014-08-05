
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

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.factory.DepartmentFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.impl.DepartmentFactoryImpl;
import com.krishagni.catissueplus.core.administrative.events.AllDepartmentsEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDetails;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDisabledEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentGotEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.GetDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.repository.DepartmentDao;
import com.krishagni.catissueplus.core.administrative.repository.InstituteDao;
import com.krishagni.catissueplus.core.administrative.repository.PermissibleValueDao;
import com.krishagni.catissueplus.core.administrative.services.DepartmentService;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.administrative.services.impl.DepartmentServiceImpl;
import com.krishagni.catissueplus.core.administrative.services.impl.PermissibleValueServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.PermissibleValuesManager;
import com.krishagni.catissueplus.core.common.PermissibleValuesManagerImpl;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.privileges.domain.factory.PrivilegeErrorCode;
import com.krishagni.catissueplus.core.services.testdata.DepartmentTestData;
import com.krishagni.catissueplus.core.services.testdata.InstituteTestData;
import com.krishagni.catissueplus.core.services.testdata.PermissibleValueTestData;

public class DepartmentTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	DepartmentDao departmentDao;

	@Mock
	InstituteDao instituteDao;

	@Mock
	PermissibleValueDao pvDao;

	@Mock
	CommonValidator commonValidator;

	PermissibleValuesManager pvManager;

	private PermissibleValueService pvService;

	private DepartmentFactory departmentFactory;

	private DepartmentService departmentService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		when(daoFactory.getDepartmentDao()).thenReturn(departmentDao);
		when(daoFactory.getInstituteDao()).thenReturn(instituteDao);
		when(daoFactory.getPermissibleValueDao()).thenReturn(pvDao);
		pvService = new PermissibleValueServiceImpl();

		((PermissibleValueServiceImpl) pvService).setDaoFactory(daoFactory);
		pvManager = new PermissibleValuesManagerImpl();
		((PermissibleValuesManagerImpl) pvManager).setPermissibleValueSvc(pvService);
		CommonValidator.setPvManager(pvManager);
		when(pvDao.getAllValuesByAttribute(anyString())).thenReturn(PermissibleValueTestData.getPvValues());

		departmentService = new DepartmentServiceImpl();
		((DepartmentServiceImpl) departmentService).setDaoFactory(daoFactory);
		departmentFactory = new DepartmentFactoryImpl();
		((DepartmentFactoryImpl) departmentFactory).setDaoFactory(daoFactory);
		((DepartmentServiceImpl) departmentService).setDepartmentFactory(departmentFactory);

		when(instituteDao.getInstituteByName(anyString())).thenReturn(InstituteTestData.getInstitute(1l));

		when(departmentDao.getDepartmentByName(anyString())).thenReturn(null);
		when(departmentDao.isUniqueDepartmentInInstitute(anyString(), anyString())).thenReturn(Boolean.TRUE);
	}

	@Test
	public void testForSuccessfulDepartmentCreation() {
		CreateDepartmentEvent reqEvent = DepartmentTestData.getCreateDepartmentEvent();
		DepartmentCreatedEvent response = departmentService.createDepartment(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(reqEvent.getDepartmentDetails().getName(), response.getDepartmentDetails().getName());
	}

	@Test
	public void testDepartmentCreationWithDuplicateDepartmentName() {
		when(departmentDao.isUniqueDepartmentInInstitute(anyString(), anyString())).thenReturn(Boolean.FALSE);
		CreateDepartmentEvent reqEvent = DepartmentTestData.getCreateDepartmentEvent();
		DepartmentCreatedEvent response = departmentService.createDepartment(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(DepartmentTestData.DEPARTMENT_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.DUPLICATE_DEPARTMENT_NAME.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testDepartmentCreationWithEmptyDepartmentName() {
		CreateDepartmentEvent reqEvent = DepartmentTestData.getCreateDepartmentEventWithEmptyDepartmentName();
		DepartmentCreatedEvent response = departmentService.createDepartment(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(DepartmentTestData.DEPARTMENT_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(PrivilegeErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testDepartmentCreationWithInvalidInstitute() {
		CreateDepartmentEvent reqEvent = DepartmentTestData.getCreateDepartmentEvent();
		when(instituteDao.getInstituteByName(anyString())).thenReturn(null);

		DepartmentCreatedEvent response = departmentService.createDepartment(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(DepartmentTestData.INSTITUTE, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());

	}

	@Test
	public void testDepartmentCreationWithServerErr() {
		CreateDepartmentEvent reqEvent = DepartmentTestData.getCreateDepartmentEvent();
		doThrow(new RuntimeException()).when(departmentDao).saveOrUpdate(any(Department.class));
		DepartmentCreatedEvent response = departmentService.createDepartment(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForSuccessfulDepartmentUpdate() {
		when(departmentDao.getDepartment(anyLong())).thenReturn(DepartmentTestData.getDepartment(1L));
		UpdateDepartmentEvent reqEvent = DepartmentTestData.getUpdateDepartmentEvent();

		DepartmentUpdatedEvent response = departmentService.updateDepartment(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		DepartmentDetails createdDepartment = response.getDepartmentDetails();
		assertEquals(reqEvent.getDepartmentDetails().getName(), createdDepartment.getName());
	}

	@Test
	public void testForSuccessfulDepartmentUpdateWithChangedName() {
		when(departmentDao.getDepartment(anyLong())).thenReturn(DepartmentTestData.getDepartment(1L));
		UpdateDepartmentEvent reqEvent = DepartmentTestData.getUpdateDepartmentEventForChangedName();

		DepartmentUpdatedEvent response = departmentService.updateDepartment(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		DepartmentDetails createdDepartment = response.getDepartmentDetails();
		assertEquals(reqEvent.getDepartmentDetails().getName(), createdDepartment.getName());
	}

	@Test
	public void testDepartmentUpdationWithEmptyDepartmentName() {
		when(departmentDao.getDepartment(anyLong())).thenReturn(DepartmentTestData.getDepartment(1L));
		UpdateDepartmentEvent reqEvent = DepartmentTestData.getUpdateDepartmentEventWithEmptyDepartmentName();
		DepartmentUpdatedEvent response = departmentService.updateDepartment(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(DepartmentTestData.DEPARTMENT_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(PrivilegeErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
	}

	@Test
	public void testForInvalidDepartmentUpdate() {
		when(departmentDao.getDepartment(anyLong())).thenReturn(null);
		UpdateDepartmentEvent reqEvent = DepartmentTestData.getUpdateDepartmentEvent();

		DepartmentUpdatedEvent response = departmentService.updateDepartment(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getDepartmentId());
	}

	@Test
	public void testDepartmentUpdateWithServerErr() {
		when(departmentDao.getDepartment(anyLong())).thenReturn(DepartmentTestData.getDepartment(1L));
		UpdateDepartmentEvent reqEvent = DepartmentTestData.getUpdateDepartmentEvent();

		doThrow(new RuntimeException()).when(departmentDao).saveOrUpdate(any(Department.class));
		DepartmentUpdatedEvent response = departmentService.updateDepartment(reqEvent);
		assertNotNull("response cannot be null", response);
	}

	@Test
	public void testForInvalidDepartmentDisable() {
		when(departmentDao.getDepartmentByName(anyString())).thenReturn(null);
		DisableDepartmentEvent reqEvent = DepartmentTestData.getDisableDepartmentEvent();
		DepartmentDisabledEvent response = departmentService.deleteDepartment(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}

	@Test
	public void testForInvalidDepartmentDisableWithName() {
		when(departmentDao.getDepartmentByName(anyString())).thenReturn(null);
		DisableDepartmentEvent reqEvent = DepartmentTestData.getDisableDepartmentEventForName();
		DepartmentDisabledEvent response = departmentService.deleteDepartment(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getName());
	}

	@Test
	public void testSuccessfulDepartmentDisable() {
		DisableDepartmentEvent reqEvent = DepartmentTestData.getDisableDepartmentEvent();
		Department departmentToDelete = DepartmentTestData.getDepartment(1L);
		when(departmentDao.getDepartment(anyLong())).thenReturn(departmentToDelete);
		DepartmentDisabledEvent response = departmentService.deleteDepartment(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(DepartmentTestData.ACTIVITY_STATUS_DISABLED, departmentToDelete.getActivityStatus());
	}

	@Test
	public void testDepartmentDisableWithReference() {
		when(departmentDao.getDepartment(anyLong())).thenReturn(DepartmentTestData.getDepartmentForDisable(1l));
		DisableDepartmentEvent reqEvent = DepartmentTestData.getDisableDepartmentEvent();
		DepartmentDisabledEvent response = departmentService.deleteDepartment(reqEvent);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(UserErrorCode.REFERENCED_ATTRIBUTE.message(), response.getMessage());
	}

	@Test
	public void testDepartmentDisableWithServerErr() {
		when(departmentDao.getDepartment(anyLong())).thenReturn(DepartmentTestData.getDepartment(1l));
		DisableDepartmentEvent reqEvent = DepartmentTestData.getDisableDepartmentEvent();
		doThrow(new RuntimeException()).when(departmentDao).saveOrUpdate(any(Department.class));
		DepartmentDisabledEvent response = departmentService.deleteDepartment(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testGetDepartmentById() {
		when(departmentDao.getDepartment(anyLong())).thenReturn(DepartmentTestData.getDepartment(1l));
		GetDepartmentEvent reqEvent = DepartmentTestData.getDepartmentEvent();
		DepartmentGotEvent response = departmentService.getDepartment(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testGetDepartmentWithWrongInstWithId() {
		when(departmentDao.getDepartment(anyLong())).thenReturn(null);
		GetDepartmentEvent reqEvent = DepartmentTestData.getDepartmentEvent();
		DepartmentGotEvent response = departmentService.getDepartment(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}

	@Test
	public void testGetDepartmentByName() {
		when(departmentDao.getDepartmentByName(anyString())).thenReturn(DepartmentTestData.getDepartment(1l));
		GetDepartmentEvent reqEvent = DepartmentTestData.getDepartmentEventForName();
		DepartmentGotEvent response = departmentService.getDepartment(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(response.getDetails());
	}

	@Test
	public void testGetDepartmentWithWrongInst() {
		when(departmentDao.getDepartmentByName(anyString())).thenReturn(null);
		GetDepartmentEvent reqEvent = DepartmentTestData.getDepartmentEventForName();
		DepartmentGotEvent response = departmentService.getDepartment(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testGetDepartmentByIdWithServerError() {
		GetDepartmentEvent reqEvent = DepartmentTestData.getDepartmentEvent();
		doThrow(new RuntimeException()).when(departmentDao).getDepartment(anyLong());
		DepartmentGotEvent response = departmentService.getDepartment(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testGetAllDepartments() {
		when(departmentDao.getAllDepartments(eq(1000))).thenReturn(DepartmentTestData.getDepartments());
		ReqAllDepartmentEvent reqEvent = DepartmentTestData.getAllDepartmentEvent();
		AllDepartmentsEvent response = departmentService.getAllDepartments(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(2, response.getDepartments().size());
	}

}
