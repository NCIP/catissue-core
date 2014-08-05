
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

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.domain.factory.PVErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.PermissibleValueFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.impl.PermissibleValueFactoryImpl;
import com.krishagni.catissueplus.core.administrative.events.AddPvEvent;
import com.krishagni.catissueplus.core.administrative.events.AllPvsEvent;
import com.krishagni.catissueplus.core.administrative.events.DeletePvEvent;
import com.krishagni.catissueplus.core.administrative.events.EditPvEvent;
import com.krishagni.catissueplus.core.administrative.events.GetAllPVsEvent;
import com.krishagni.catissueplus.core.administrative.events.PermissibleValueDetails;
import com.krishagni.catissueplus.core.administrative.events.PvAddedEvent;
import com.krishagni.catissueplus.core.administrative.events.PvDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.PvEditedEvent;
import com.krishagni.catissueplus.core.administrative.events.ValidatePvEvent;
import com.krishagni.catissueplus.core.administrative.repository.PermissibleValueDao;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.administrative.services.impl.PermissibleValueServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.DepartmentTestData;
import com.krishagni.catissueplus.core.services.testdata.PermissibleValueTestData;

public class PermissibleValueTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	PermissibleValueDao pvDao;

	private PermissibleValueFactory pvFactory;

	private PermissibleValueService pvService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		when(daoFactory.getPermissibleValueDao()).thenReturn(pvDao);

		pvService = new PermissibleValueServiceImpl();
		((PermissibleValueServiceImpl) pvService).setDaoFactory(daoFactory);
		pvFactory = new PermissibleValueFactoryImpl();
		((PermissibleValueServiceImpl) pvService).setPvFactory(pvFactory);
		((PermissibleValueFactoryImpl) pvFactory).setDaoFactory(daoFactory);
		when(daoFactory.getPermissibleValueDao().getPermissibleValue(anyLong())).thenReturn(
				PermissibleValueTestData.getPermissibleValue(1l));
		when(pvDao.isUniqueValueInAttribute(anyString(), anyString())).thenReturn(Boolean.TRUE);
		when(pvDao.isUniqueConceptCode(anyString())).thenReturn(Boolean.TRUE);
	}

	@Test
	public void testForSuccessfulPermissibleValueAddition() {
		AddPvEvent reqEvent = PermissibleValueTestData.getAddPvEvent();
		PvAddedEvent response = pvService.createPermissibleValue(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		PermissibleValueDetails dto = response.getDetails();
		assertEquals(reqEvent.getDetails().getValue(), dto.getValue());
	}

	@Test
	public void testForSuccessfulPermissibleValueAdditionWithNullParent() {
		AddPvEvent reqEvent = PermissibleValueTestData.getAddPvEventWithNullParent();
		PvAddedEvent response = pvService.createPermissibleValue(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		PermissibleValueDetails dto = response.getDetails();
		assertEquals(reqEvent.getDetails().getValue(), dto.getValue());
	}

	@Test
	public void testForPermissibleValueAdditionWithInvalidParentPV() {
		when(daoFactory.getPermissibleValueDao().getPermissibleValue(anyLong())).thenReturn(null);
		AddPvEvent reqEvent = PermissibleValueTestData.getAddPvEvent();
		PvAddedEvent response = pvService.createPermissibleValue(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(DepartmentTestData.PERMISSIBLE_VALUE, response.getErroneousFields()[0].getFieldName());
		assertEquals(UserErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testPermissibleValueAditionWithNullValue() {
		AddPvEvent reqEvent = PermissibleValueTestData.getAddPvEventForNullValue();
		PvAddedEvent response = pvService.createPermissibleValue(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(PermissibleValueTestData.VALUE, response.getErroneousFields()[0].getFieldName());
		assertEquals(PVErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testPermissibleValueAditionWithNullName() {
		AddPvEvent reqEvent = PermissibleValueTestData.getAddPvEventForNullAttribute();
		PvAddedEvent response = pvService.createPermissibleValue(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(PVErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testPermissibleValueCreationWithDuplicateValue() {
		when(pvDao.isUniqueValueInAttribute(anyString(), anyString())).thenReturn(Boolean.FALSE);
		AddPvEvent reqEvent = PermissibleValueTestData.getAddPvEvent();
		PvAddedEvent response = pvService.createPermissibleValue(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(PermissibleValueTestData.VALUE, response.getErroneousFields()[0].getFieldName());
		assertEquals(PVErrorCode.DUPLICATE_PV_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testPermissibleValueCreationWithDuplicateConceptCode() {
		when(pvDao.isUniqueConceptCode(anyString())).thenReturn(Boolean.FALSE);
		AddPvEvent reqEvent = PermissibleValueTestData.getAddPvEvent();
		PvAddedEvent response = pvService.createPermissibleValue(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(PermissibleValueTestData.CONCEPT_CODE, response.getErroneousFields()[0].getFieldName());
		assertEquals(PVErrorCode.DUPLICATE_CONCEPT_CODE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testForSuccessfulPermissibleValueUpdate() {
		when(pvDao.getPermissibleValue(anyLong())).thenReturn(PermissibleValueTestData.getPermissibleValue(1L));
		EditPvEvent reqEvent = PermissibleValueTestData.getEditPvEvent();

		PvEditedEvent response = pvService.updatePermissibleValue(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		PermissibleValueDetails addedPermissibleValue = response.getDetails();
		assertEquals(reqEvent.getDetails().getAttribute(), addedPermissibleValue.getAttribute());
		assertEquals(addedPermissibleValue.getValue(), addedPermissibleValue.getValue());
	}

	@Test
	public void testForPVUpdateWithNonUniqueValueInName() {
		when(pvDao.getPermissibleValue(anyLong())).thenReturn(PermissibleValueTestData.getPermissibleValue(1L));
		EditPvEvent reqEvent = PermissibleValueTestData.getEditPvEvent();
		when(pvDao.isUniqueValueInAttribute(anyString(), anyString())).thenReturn(Boolean.FALSE);
		PvEditedEvent response = pvService.updatePermissibleValue(reqEvent);

		assertEquals(PermissibleValueTestData.VALUE, response.getErroneousFields()[0].getFieldName());
		assertEquals(PVErrorCode.DUPLICATE_PV_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testForPermissibleValueUpdateWithChangedValue() {
		when(pvDao.getPermissibleValue(anyLong())).thenReturn(PermissibleValueTestData.getPermissibleValue(1L));
		EditPvEvent reqEvent = PermissibleValueTestData.getEditPvEvent();

		PvEditedEvent response = pvService.updatePermissibleValue(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testForInvalidPermissibleValueUpdate() {
		when(pvDao.getPermissibleValue(anyLong())).thenReturn(null);
		EditPvEvent reqEvent = PermissibleValueTestData.getEditPvEvent();

		PvEditedEvent response = pvService.updatePermissibleValue(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}

	@Test
	public void testPermissibleValueUpdateWithServerErr() {
		when(pvDao.getPermissibleValue(anyLong())).thenReturn(PermissibleValueTestData.getPermissibleValue(1L));
		EditPvEvent reqEvent = PermissibleValueTestData.getEditPvEvent();

		doThrow(new RuntimeException()).when(pvDao).saveOrUpdate(any(PermissibleValue.class));
		PvEditedEvent response = pvService.updatePermissibleValue(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForInvalidPermissibleValueDelete() {
		when(pvDao.getPermissibleValue(anyLong())).thenReturn(null);
		DeletePvEvent reqEvent = PermissibleValueTestData.getDeletePvEvent();
		PvDeletedEvent response = pvService.deletePermissibleValue(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}

	@Test
	public void testSuccessfulPermissibleValueDelete() {
		DeletePvEvent reqEvent = PermissibleValueTestData.getDeletePvEvent();
		PermissibleValue pvToDelete = PermissibleValueTestData.getPermissibleValue(1L);
		when(pvDao.getPermissibleValue(anyLong())).thenReturn(pvToDelete);
		PvDeletedEvent response = pvService.deletePermissibleValue(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(PermissibleValueTestData.SUCCESS, response.getMessage());
	}

	@Test
	public void testPermissibleValueDeleteWithServerErr() {
		when(pvDao.getPermissibleValue(anyLong())).thenReturn(PermissibleValueTestData.getPermissibleValue(1l));
		DeletePvEvent reqEvent = PermissibleValueTestData.getDeletePvEvent();
		doThrow(new RuntimeException()).when(pvDao).delete(any(PermissibleValue.class));
		PvDeletedEvent response = pvService.deletePermissibleValue(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForlPermissibleValueAdditionWithServerErr() {
		AddPvEvent reqEvent = PermissibleValueTestData.getAddPvEvent();
		doThrow(new RuntimeException()).when(pvDao).saveOrUpdate(any(PermissibleValue.class));
		PvAddedEvent response = pvService.createPermissibleValue(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForSuccessfulForGetAPermissibleValueUpdate() {
		when(pvDao.getAllPVsByAttribute(anyString(), anyString(), eq(1000))).thenReturn(PermissibleValueTestData.getPermissibleValues());
		GetAllPVsEvent event = PermissibleValueTestData.getGetAllPVsEvent();
		AllPvsEvent response = pvService.getPermissibleValues(event);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(4, response.getPvs().size());
		assertEquals(event.getAttribute(), response.getPvs().get(0).getAttribute());
		assertEquals("Tissue", response.getPvs().get(0).getValue());
	}

	/*@Test
	public void testValidatePv() {
		ValidatePvEvent event = PermissibleValueTestData.getValidatePvEvent();
		Boolean response = pvService.validate(event);
		assertEquals(response, Boolean.FALSE);
	}
	
	@Test
	public void testValidatePvWithoutParent() {
		when(pvDao.getAllValuesByAttribute(anyString())).thenReturn(PermissibleValueTestData.getPvValues());
		ValidatePvEvent event = PermissibleValueTestData.getValidatePvEventWithoutParent();
		Boolean response = pvService.validate(event);
		assertEquals(response, Boolean.FALSE);
	}*/
	
	@Test
	public void testValidatePvWithoutParentForSuccess() {
		when(pvDao.getAllValuesByAttribute(anyString())).thenReturn(PermissibleValueTestData.getPvValues());
		ValidatePvEvent event = PermissibleValueTestData.getValidatePvEventForSuccess();
		Boolean response = pvService.validate(event);
		assertEquals(response, Boolean.TRUE);
	}

}
