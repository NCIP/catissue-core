
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

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.factory.DepartmentFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.impl.DepartmentFactoryImpl;
import com.krishagni.catissueplus.core.administrative.events.CreateDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDetails;
import com.krishagni.catissueplus.core.administrative.events.DepartmentUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.repository.DepartmentDao;
import com.krishagni.catissueplus.core.administrative.repository.InstituteDao;
import com.krishagni.catissueplus.core.administrative.services.DepartmentService;
import com.krishagni.catissueplus.core.administrative.services.impl.DepartmentServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.privileges.domain.factory.PrivilegeErrorCode;
import com.krishagni.catissueplus.core.services.testdata.DepartmentTestData;
import com.krishagni.catissueplus.core.services.testdata.InstituteTestData;

public class DepartmentTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	DepartmentDao departmentDao;

	@Mock
	InstituteDao instituteDao;

	private DepartmentFactory departmentFactory;

	private DepartmentService departmentService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		when(daoFactory.getDepartmentDao()).thenReturn(departmentDao);
		when(daoFactory.getInstituteDao()).thenReturn(instituteDao);

		departmentService = new DepartmentServiceImpl();
		((DepartmentServiceImpl) departmentService).setDaoFactory(daoFactory);
		departmentFactory = new DepartmentFactoryImpl();
		((DepartmentFactoryImpl) departmentFactory).setDaoFactory(daoFactory);
		((DepartmentServiceImpl) departmentService).setDepartmentFactory(departmentFactory);
		
		when(instituteDao.getInstituteByName(anyString())).thenReturn(InstituteTestData.getInstitute(1l));
		when(departmentDao.getDepartmentByName(anyString())).thenReturn(null);
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
		when(departmentDao.getDepartmentByName(anyString())).thenReturn(DepartmentTestData.getDepartment(1l));
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
	public void testForInvalidDepartmentUpdate() {
		when(departmentDao.getDepartment(anyLong())).thenReturn(null);
		UpdateDepartmentEvent reqEvent = DepartmentTestData.getUpdateDepartmentEvent();

		DepartmentUpdatedEvent response = departmentService.updateDepartment(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testDepartmentUpdateWithServerErr() {
		when(departmentDao.getDepartment(anyLong())).thenReturn(DepartmentTestData.getDepartment(1L));
		UpdateDepartmentEvent reqEvent = DepartmentTestData.getUpdateDepartmentEvent();

		doThrow(new RuntimeException()).when(departmentDao).saveOrUpdate(any(Department.class));
		DepartmentUpdatedEvent response = departmentService.updateDepartment(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

}
