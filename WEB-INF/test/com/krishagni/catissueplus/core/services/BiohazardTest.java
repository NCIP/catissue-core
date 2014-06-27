
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

import com.krishagni.catissueplus.core.administrative.domain.Biohazard;
import com.krishagni.catissueplus.core.administrative.domain.factory.BiohazardErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.BiohazardFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.impl.BiohazardFactoryImpl;
import com.krishagni.catissueplus.core.administrative.events.BiohazardCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.BiohazardDetails;
import com.krishagni.catissueplus.core.administrative.events.BiohazardUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateBiohazardEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchBiohazardEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateBiohazardEvent;
import com.krishagni.catissueplus.core.administrative.repository.BiohazardDao;
import com.krishagni.catissueplus.core.administrative.services.BiohazardService;
import com.krishagni.catissueplus.core.administrative.services.impl.BiohazardServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.BiohazardTestData;

public class BiohazardTest {

	@Mock
	DaoFactory daoFactory;

	@Mock
	BiohazardDao biohazardDao;

	private BiohazardFactory biohazardFactory;

	private BiohazardService biohazardService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(daoFactory.getBiohazardDao()).thenReturn(biohazardDao);

		biohazardFactory = new BiohazardFactoryImpl();
		biohazardService = new BiohazardServiceImpl();

		((BiohazardServiceImpl) biohazardService).setDaoFactory(daoFactory);
		((BiohazardServiceImpl) biohazardService).setBiohazardFactory(biohazardFactory);

	}

	@Test
	public void testForSuccessfulBiohazardCreation() {
		when(biohazardDao.isUniqueBiohazardName(anyString())).thenReturn(Boolean.TRUE);

		CreateBiohazardEvent reqEvent = BiohazardTestData.setCreateBiohazardEvent();
		BiohazardCreatedEvent response = biohazardService.createBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		BiohazardDetails createdBiohazardDetails = response.getBiohazardDetails();
		assertEquals(reqEvent.getBiohazardDetails().getName(), createdBiohazardDetails.getName());
	}

	@Test
	public void testBiohazardCreationWithDuplicateBiohazardName() {
		when(biohazardDao.isUniqueBiohazardName(anyString())).thenReturn(Boolean.FALSE);

		CreateBiohazardEvent reqEvent = BiohazardTestData.setCreateBiohazardEvent();
		BiohazardCreatedEvent response = biohazardService.createBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(BiohazardTestData.BIOHAZARD_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(BiohazardErrorCode.DUPLICATE_BIOHAZARD_NAME.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testBiohazardCreationWithEmptyBiohazardName() {
		CreateBiohazardEvent reqEvent = BiohazardTestData.getCreateBiohazardEventWithEmptyBiohazardName();
		BiohazardCreatedEvent response = biohazardService.createBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(BiohazardTestData.BIOHAZARD_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(BiohazardErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testBiohazardCreationWithEmptyBiohazardType() {
		CreateBiohazardEvent reqEvent = BiohazardTestData.getCreateEventWithEmptyBiohazardType();
		BiohazardCreatedEvent response = biohazardService.createBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(BiohazardTestData.BIOHAZARD_TYPE, response.getErroneousFields()[0].getFieldName());
		assertEquals(BiohazardErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	//	@Test
	//	public void testBiohazardCreationWithInvalidBiohazardType() {
	//		when(biohazardDao.isUniqueBiohazardName(anyString())).thenReturn(Boolean.TRUE);
	//		
	//		CreateBiohazardEvent reqEvent = BiohazardTestData.getCreateEventWithInvalidBiohazardType();
	//		BiohazardCreatedEvent response = biohazardService.createBiohazard(reqEvent);
	//
	//		assertNotNull("response cannot be null", response);
	//		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
	//		assertEquals(1, response.getErroneousFields().length);
	//		assertEquals(BiohazardErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	//	}
	
//@Test
//	public void testBiohazardCreationWithInvalidActivityStatus() {
//		when(biohazardDao.isUniqueBiohazardName(anyString())).thenReturn(Boolean.TRUE);
//		
//		CreateBiohazardEvent reqEvent = BiohazardTestData.getCreateEventWithInvalidActivityStatus();
//		BiohazardCreatedEvent response = biohazardService.createBiohazard(reqEvent);
//
//		assertNotNull("response cannot be null", response);
//		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
//		assertEquals(1, response.getErroneousFields().length);
//		assertEquals(BiohazardErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
//	}
	
	

	@Test
	public void testBiohazardCreationWithServerErr() {

		doThrow(new RuntimeException()).when(biohazardDao).saveOrUpdate(any(Biohazard.class));
		when(biohazardDao.isUniqueBiohazardName(anyString())).thenReturn(Boolean.TRUE);

		CreateBiohazardEvent reqEvent = BiohazardTestData.setCreateBiohazardEvent();
		BiohazardCreatedEvent response = biohazardService.createBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForSuccessfulBiohazardUpdation() {
		when(biohazardDao.isUniqueBiohazardName(anyString())).thenReturn(Boolean.TRUE);
		when(biohazardDao.getBiohazard(anyLong())).thenReturn(BiohazardTestData.getBiohazard());

		UpdateBiohazardEvent reqEvent = BiohazardTestData.getUpdateBiohazardEvent();
		BiohazardUpdatedEvent response = biohazardService.updateBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());

		BiohazardDetails createBiohazard = response.getBiohazardDetails();

		assertEquals(reqEvent.getBiohazardDetails().getName(), createBiohazard.getName());
	}
	
	@Test
	public void testForSuccessfulBiohazardUpdationWithName() {
		when(biohazardDao.isUniqueBiohazardName(anyString())).thenReturn(Boolean.TRUE);
		when(biohazardDao.getBiohazard(anyString())).thenReturn(BiohazardTestData.getBiohazard());

		UpdateBiohazardEvent reqEvent = BiohazardTestData.getUpdateBiohazardEventWithName();
		BiohazardUpdatedEvent response = biohazardService.updateBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());

		BiohazardDetails createBiohazard = response.getBiohazardDetails();

		assertEquals(reqEvent.getBiohazardDetails().getName(), createBiohazard.getName());
	}

	@Test
	public void testBiohazardUpdationWithDuplicateBiohazardName() {
		when(biohazardDao.isUniqueBiohazardName(anyString())).thenReturn(Boolean.FALSE);
		when(biohazardDao.getBiohazard(anyLong())).thenReturn(BiohazardTestData.getBiohazard());

		UpdateBiohazardEvent reqEvent = BiohazardTestData.getUpdateBiohazardEventWithDuplicateName();
		BiohazardUpdatedEvent response = biohazardService.updateBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(BiohazardTestData.BIOHAZARD_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(BiohazardErrorCode.DUPLICATE_BIOHAZARD_NAME.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testBiohazardUpdationWithEmptyBiohazardName() {
		when(daoFactory.getBiohazardDao().getBiohazard(anyLong())).thenReturn(BiohazardTestData.getBiohazard());

		UpdateBiohazardEvent reqEvent = BiohazardTestData.getUpdateBiohazardEventWithEmptyName();
		BiohazardUpdatedEvent response = biohazardService.updateBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(BiohazardTestData.BIOHAZARD_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(BiohazardErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testBiohazardUpdationWithEmptyBiohazardType() {
		when(biohazardDao.getBiohazard(anyLong())).thenReturn(BiohazardTestData.getBiohazard());

		UpdateBiohazardEvent reqEvent = BiohazardTestData.getUpdateBiohazardEventWithEmptyType();
		BiohazardUpdatedEvent response = biohazardService.updateBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(BiohazardTestData.BIOHAZARD_TYPE, response.getErroneousFields()[0].getFieldName());
		assertEquals(BiohazardErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	//	@Test
	//	public void testBiohazardUpdationWithInvalidBiohazardType() {
	//		when(daoFactory.getBiohazardDao().getBiohazard(anyLong())).thenReturn(BiohazardTestData.getBiohazard());
	//		when(biohazardDao.isUniqueBiohazardName(anyString())).thenReturn(Boolean.TRUE);
	//		
	//		UpdateBiohazardEvent reqEvent = BiohazardTestData.getUpdateEventWithInvalidBiohazardType();
	//		BiohazardUpdatedEvent response = biohazardService.updateBiohazard(reqEvent);
	//		
	//		assertNotNull("response cannot be null", response);
	//		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
	//		assertEquals(1, response.getErroneousFields().length);
	//		assertEquals(BiohazardErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	//		
	//	}

	@Test
	public void testBiohazardUpdationWithServerErr() {
		when(biohazardDao.getBiohazard(anyLong())).thenReturn(BiohazardTestData.getBiohazard());
		when(biohazardDao.isUniqueBiohazardName(anyString())).thenReturn(Boolean.TRUE);
		doThrow(new RuntimeException()).when(biohazardDao).saveOrUpdate(any(Biohazard.class));

		UpdateBiohazardEvent reqEvent = BiohazardTestData.getUpdateBiohazardEvent();
		BiohazardUpdatedEvent response = biohazardService.updateBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testUpdationWithNullBiohazard() {
		when(biohazardDao.getBiohazard(anyLong())).thenReturn(null);

		UpdateBiohazardEvent reqEvent = BiohazardTestData.getUpdateBiohazardEvent();
		BiohazardUpdatedEvent response = biohazardService.updateBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}
	
	@Test
	public void testUpdationWithNullBiohazardWithName() {
		when(biohazardDao.getBiohazard(anyLong())).thenReturn(null);

		UpdateBiohazardEvent reqEvent = BiohazardTestData.getUpdateBiohazardEventWithName();
		BiohazardUpdatedEvent response = biohazardService.updateBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testSuccessfullPatchBiohazard() {
		when(daoFactory.getBiohazardDao().getBiohazard(anyLong())).thenReturn(BiohazardTestData.getBiohazard());
		when(daoFactory.getBiohazardDao().isUniqueBiohazardName(anyString())).thenReturn(true);

		PatchBiohazardEvent reqEvent = BiohazardTestData.getPatchData();
		BiohazardUpdatedEvent response = biohazardService.patchBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testSuccessfullPatchBiohazardWithName() {
		when(daoFactory.getBiohazardDao().getBiohazard(anyString())).thenReturn(BiohazardTestData.getBiohazard());
		when(daoFactory.getBiohazardDao().isUniqueBiohazardName(anyString())).thenReturn(true);

		PatchBiohazardEvent reqEvent = BiohazardTestData.getPatchDataWithName();
		BiohazardUpdatedEvent response = biohazardService.patchBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testPatchBiohazardWithInvalidAttribute() {
		when(daoFactory.getBiohazardDao().getBiohazard(anyLong())).thenReturn(BiohazardTestData.getBiohazard());
		when(daoFactory.getBiohazardDao().isUniqueBiohazardName(anyString())).thenReturn(false);

		PatchBiohazardEvent reqEvent = BiohazardTestData.getPatchDataWithInavalidAttribute();
		BiohazardUpdatedEvent response = biohazardService.patchBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(response.getStatus(), EventStatus.BAD_REQUEST);

	}

	@Test
	public void testBiohazardPatchNullBiohazard() {
		when(biohazardDao.getBiohazard(anyLong())).thenReturn(null);

		PatchBiohazardEvent reqEvent = BiohazardTestData.getPatchData();
		BiohazardUpdatedEvent response = biohazardService.patchBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}
	
	@Test
	public void testBiohazardPatchNullBiohazardWithName() {
		when(biohazardDao.getBiohazard(anyString())).thenReturn(null);

		PatchBiohazardEvent reqEvent = BiohazardTestData.getPatchDataWithName();
		BiohazardUpdatedEvent response = biohazardService.patchBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testPatchBiohazardServerError() {
		when(daoFactory.getBiohazardDao().getBiohazard(anyLong())).thenReturn(BiohazardTestData.getBiohazard());
		when(daoFactory.getBiohazardDao().isUniqueBiohazardName(anyString())).thenReturn(true);
		doThrow(new RuntimeException()).when(biohazardDao).saveOrUpdate(any(Biohazard.class));

		PatchBiohazardEvent reqEvent = BiohazardTestData.getPatchData();
		BiohazardUpdatedEvent response = biohazardService.patchBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testBiohazardPatchWithEmptyName() {
		when(daoFactory.getBiohazardDao().getBiohazard(anyLong())).thenReturn(BiohazardTestData.getBiohazard());

		PatchBiohazardEvent reqEvent = BiohazardTestData.getPatchDataWithEmptyName();
		BiohazardUpdatedEvent response = biohazardService.patchBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(BiohazardTestData.BIOHAZARD_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(BiohazardErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testBiohazardPatchWithEmptyType() {
		when(daoFactory.getBiohazardDao().getBiohazard(anyLong())).thenReturn(BiohazardTestData.getBiohazard());

		PatchBiohazardEvent reqEvent = BiohazardTestData.getPatchDataWithEmptyType();
		BiohazardUpdatedEvent response = biohazardService.patchBiohazard(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(BiohazardTestData.BIOHAZARD_TYPE, response.getErroneousFields()[0].getFieldName());
		assertEquals(BiohazardErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	//	@Test
	//	public void testBiohazardPatchWithInvalidBiohazardType() {
	//		when(daoFactory.getBiohazardDao().getBiohazard(anyLong())).thenReturn(BiohazardTestData.getBiohazard());
	//		when(biohazardDao.isUniqueBiohazardName(anyString())).thenReturn(Boolean.TRUE);
	//		
	//		PatchBiohazardEvent reqEvent = BiohazardTestData.getPatchEventWithInvalidBiohazardType();
	//		BiohazardUpdatedEvent response = biohazardService.patchBiohazard(reqEvent);
	//		
	//		assertNotNull("response cannot be null", response);
	//		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
	//		assertEquals(1, response.getErroneousFields().length);
	//		assertEquals(BiohazardErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	//		
	//	}
}
