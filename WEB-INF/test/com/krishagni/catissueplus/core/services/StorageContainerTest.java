
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

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.impl.StorageContainerFactoryImpl;
import com.krishagni.catissueplus.core.administrative.events.CreateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetails;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDisabledEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerDao;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;
import com.krishagni.catissueplus.core.administrative.services.impl.StorageContainerServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.StorageContainerTestData;

public class StorageContainerTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	UserDao userDao;

	@Mock
	StorageContainerDao storageContainerDao;

	@Mock
	CollectionProtocolDao collectionProtocolDao;

	@Mock
	SiteDao siteDao;
	
	private StorageContainerFactory storageContainerFactory;

	private StorageContainerService storageContainerService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		when(daoFactory.getUserDao()).thenReturn(userDao);
		when(daoFactory.getSiteDao()).thenReturn(siteDao);
		when(daoFactory.getCollectionProtocolDao()).thenReturn(collectionProtocolDao);
		when(daoFactory.getStorageContainerDao()).thenReturn(storageContainerDao);
		storageContainerService = new StorageContainerServiceImpl();
		((StorageContainerServiceImpl) storageContainerService).setDaoFactory(daoFactory);
		storageContainerFactory = new StorageContainerFactoryImpl();
		((StorageContainerFactoryImpl) storageContainerFactory).setDaoFactory(daoFactory);
		((StorageContainerServiceImpl) storageContainerService).setStorageContainerFactory(storageContainerFactory);

		when(storageContainerDao.getStorageContainer(anyLong())).thenReturn(StorageContainerTestData.getStorageContainer(1l));
		when(siteDao.getSite(anyString())).thenReturn(StorageContainerTestData.getSite());
		when(userDao.getUserByLoginNameAndDomainName(anyString(), anyString())).thenReturn(StorageContainerTestData.getUser(1l));
		when(collectionProtocolDao.getCPByTitle(anyString())).thenReturn(StorageContainerTestData.getCp());
		when(storageContainerDao.getStorageContainer(anyLong())).thenReturn(StorageContainerTestData.getStorageContainer(1l));
		when(storageContainerDao.getStorageContainerByName(anyString())).thenReturn(StorageContainerTestData.getStorageContainer(1l));
		when(storageContainerDao.isUniqueBarcode(anyString())).thenReturn(Boolean.TRUE);
		when(storageContainerDao.isUniqueContainerName(anyString())).thenReturn(Boolean.TRUE);
	}

	@Test
	public void testForSuccessfulStorageContainerCreation() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEvent();
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		StorageContainerDetails createdStorageContainerDto = response.getStorageContainerDetails();
		assertEquals(reqEvent.getDetails().getName(), createdStorageContainerDto.getName());
	}
	
	@Test
	public void testForSuccessfulStorageContainerCreationWithNullParent() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEvent();
		reqEvent.getDetails().setParentContainerName(null);
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		StorageContainerDetails createdStorageContainerDto = response.getStorageContainerDetails();
		assertEquals(reqEvent.getDetails().getName(), createdStorageContainerDto.getName());
	}

	@Test
	public void testForSuccessfulStorageContainerCreationWithNullSite() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEventForNullSite();
		when(siteDao.getSite(anyString())).thenReturn(null);
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(StorageContainerErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testForSuccessfulStorageContainerCreationWithNullCp() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEvent();
		when(collectionProtocolDao.getCPByTitle(anyString())).thenReturn(null);
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(StorageContainerErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testForSuccessfulStorageContainerCreationWithIsEmptyCp() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEventWithoutCpRestrict();
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		StorageContainerDetails createdStorageContainerDto = response.getStorageContainerDetails();
		assertEquals(reqEvent.getDetails().getName(), createdStorageContainerDto.getName());	
	}
	
	@Test
	public void testForSuccessfulStorageContainerCreationWithEmptyOneDimensionalLabel() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEventWithWrongOneDimensionLabel();
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		StorageContainerDetails createdStorageContainerDto = response.getStorageContainerDetails();
		assertEquals(reqEvent.getDetails().getName(), createdStorageContainerDto.getName());	
	}
	
	@Test
	public void testForSuccessfulStorageContainerCreationWithEmptyTwoDimensionalLabel() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEventWithWrongTwoDimensionLabel();
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		StorageContainerDetails createdStorageContainerDto = response.getStorageContainerDetails();
		assertEquals(reqEvent.getDetails().getName(), createdStorageContainerDto.getName());	
	}

	@Test
	public void testStorageContainerCreationWithDuplicateName() {
		when(storageContainerDao.isUniqueContainerName(anyString())).thenReturn(Boolean.FALSE);
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEvent();
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);
		
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(StorageContainerTestData.STORAGE_CONTAINER, response.getErroneousFields()[0].getFieldName());
		assertEquals(StorageContainerErrorCode.DUPLICATE_CONTAINER_NAME.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testStorageContainerCreationWithEmptyName() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEventWithEmptyName();
		when(storageContainerDao.isUniqueContainerName(anyString())).thenReturn(Boolean.TRUE);

		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(StorageContainerTestData.CONTAINER_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(StorageContainerErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testStorageContainerCreationWithNullSiteNameAndParent() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEventWithNullSiteNameAndParent();
		when(storageContainerDao.isUniqueContainerName(anyString())).thenReturn(Boolean.TRUE);

		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(StorageContainerTestData.SITE_CONTAINER, response.getErroneousFields()[0].getFieldName());
		assertEquals(StorageContainerErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testStorageContainerCreationWithInvalidUser() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEvent();
		when(userDao.getUserByLoginNameAndDomainName(anyString(), anyString())).thenReturn(null);
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(StorageContainerTestData.USER, response.getErroneousFields()[0].getFieldName());
		assertEquals(StorageContainerErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testStorageContainerCreationWithInvalidCP() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEvent();
		when(collectionProtocolDao.getCPByTitle(anyString())).thenReturn(null);
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		
		assertEquals(StorageContainerTestData.COLLECTION_PROTOCOL, response.getErroneousFields()[0].getFieldName());
		assertEquals(StorageContainerErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testStorageContainerCreationWithInvalidStorageContainer() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEvent();
		when(storageContainerDao.getStorageContainerByName(anyString())).thenReturn(null);
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(StorageContainerTestData.STORAGE_CONTAINER, response.getErroneousFields()[0].getFieldName());
		assertEquals(StorageContainerErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testStorageContainerCreationWithDuplicateBarcode() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEvent();
		when(storageContainerDao.isUniqueBarcode(anyString())).thenReturn(Boolean.FALSE);
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(StorageContainerTestData.BARCODE, response.getErroneousFields()[0].getFieldName());
		assertEquals(StorageContainerErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testStorageContainerCreationWithInvalidParent() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEvent();
		when(storageContainerDao.getStorageContainerByName(anyString())).thenReturn(null);
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(StorageContainerTestData.STORAGE_CONTAINER, response.getErroneousFields()[0].getFieldName());
		assertEquals(StorageContainerErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testStorageContainerCreationWithInvalidOneDimentionCapcity() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEventForOneDimentionCapacity();
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(StorageContainerTestData.ONE_DIMENSION_CAPACITY, response.getErroneousFields()[0].getFieldName());
		assertEquals(StorageContainerErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testStorageContainerCreationWithInvalidTwoDimentionCapcity() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEventForTwoDimentionCapacity();
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(StorageContainerTestData.TWO_DIMENSION_CAPACITY, response.getErroneousFields()[0].getFieldName());
		assertEquals(StorageContainerErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testStorageContainerCreationWithInvalidParentSiteCombination() {
		when(storageContainerDao.getStorageContainerByName(anyString())).thenReturn(StorageContainerTestData.getStorageContainerWithDiffSite(1l));
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEvent();
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(StorageContainerTestData.SITE, response.getErroneousFields()[0].getFieldName());
		assertEquals(StorageContainerErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testStorageContainerCreationWithNullbarcodeAndName() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEventForNullBarcodeAndName();
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(StorageContainerErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testStorageContainerCreationWithServerErr() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEvent();
		
		doThrow(new RuntimeException()).when(storageContainerDao).saveOrUpdate(any(StorageContainer.class));
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForSuccessfulStorageContainerUpdate() {
		UpdateStorageContainerEvent reqEvent = StorageContainerTestData.getUpdateStorageContainerEvent();

		StorageContainerUpdatedEvent response = storageContainerService.updateStorageContainer(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		StorageContainerDetails createdStorageContainer = response.getStorageContainerDetails();
		assertEquals(reqEvent.getDetails().getName(), createdStorageContainer.getName());
		assertNotNull(createdStorageContainer.getSiteName());
	}
	
	@Test
	public void testForStorageContainerUpdateWithInvalidContainer() {
		UpdateStorageContainerEvent reqEvent = StorageContainerTestData.getUpdateStorageContainerEvent();
		when(storageContainerDao.getStorageContainer(anyLong())).thenReturn(null);
		StorageContainerUpdatedEvent response = storageContainerService.updateStorageContainer(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}
	
	@Test
	public void testForSuccessfulStorageContainerUpdateWithChangeInName() {
		UpdateStorageContainerEvent reqEvent = StorageContainerTestData.getUpdateStorageContainerEventWithChangeInName();

		StorageContainerUpdatedEvent response = storageContainerService.updateStorageContainer(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		StorageContainerDetails createdStorageContainer = response.getStorageContainerDetails();
		assertEquals(reqEvent.getDetails().getName(), createdStorageContainer.getName());
		assertNotNull(createdStorageContainer.getSiteName());
	}

	@Test
	public void testStorageContainerUpdationWithInvalidTwoDimentionCapcity() {
		UpdateStorageContainerEvent reqEvent = StorageContainerTestData.getUpdateStorageContainerEventForTwoDimentionCapacity();
		StorageContainerUpdatedEvent response = storageContainerService.updateStorageContainer(reqEvent);

		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(StorageContainerTestData.TWO_DIMENSION_CAPACITY, response.getErroneousFields()[0].getFieldName());
		assertEquals(StorageContainerErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testStorageContainerCreationWithNullTwoDimentionCapcity() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getUpdateStorageContainerEventForNullTwoDimentionCapacity();
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testStorageContainerCreationWithNullOneDimentionCapcity() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getUpdateStorageContainerEventForNullOneDimentionCapacity();
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testStorageContainerCreationWithNullBarcode() {
		CreateStorageContainerEvent reqEvent = StorageContainerTestData.getCreateStorageContainerEventForNullBarcode();
		StorageContainerCreatedEvent response = storageContainerService.createStorageContainer(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testSuccessfullPatchStorageContainer() {
		PatchStorageContainerEvent reqEvent = StorageContainerTestData.getPatchData();
		StorageContainerUpdatedEvent response = storageContainerService.patchStorageContainer(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testSuccessfullPatchStorageContainerForParentContainer() {
		PatchStorageContainerEvent reqEvent = StorageContainerTestData.getPatchDataToSetParentContainer();
		StorageContainerUpdatedEvent response = storageContainerService.patchStorageContainer(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testPatchStorageContainer() {
		PatchStorageContainerEvent reqEvent = StorageContainerTestData.nonPatchData();
		StorageContainerUpdatedEvent response = storageContainerService.patchStorageContainer(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testPatchStorageContainerWithInvalidAttribute() {
		when(collectionProtocolDao.getCPByTitle(anyString())).thenReturn(null);
		PatchStorageContainerEvent reqEvent = StorageContainerTestData.getPatchData();
		StorageContainerUpdatedEvent response = storageContainerService.patchStorageContainer(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals("Please resolve the highlighted errors.", response.getMessage());
	}
	
	@Test
	public void testPatchStorageContainerInvalidStorageContainer() {
		when(storageContainerDao.getStorageContainer(anyLong())).thenReturn(null);
		PatchStorageContainerEvent reqEvent = StorageContainerTestData.getPatchData();
		StorageContainerUpdatedEvent response = storageContainerService.patchStorageContainer(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}
	
	@Test
	public void testPatchStorageContainerServerError() {
		PatchStorageContainerEvent reqEvent = StorageContainerTestData.getPatchData();
		doThrow(new RuntimeException()).when(storageContainerDao).saveOrUpdate(any(StorageContainer.class));
		StorageContainerUpdatedEvent response = storageContainerService.patchStorageContainer(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
	
	@Test
	public void testUpdatetorageContainerServerError() {
		UpdateStorageContainerEvent reqEvent = StorageContainerTestData.getUpdateStorageContainerEvent();
		doThrow(new RuntimeException()).when(storageContainerDao).saveOrUpdate(any(StorageContainer.class));
		StorageContainerUpdatedEvent response = storageContainerService.updateStorageContainer(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
	
	@Test
	public void testForInvalidStorageContainerDisable() {
		when(daoFactory.getStorageContainerDao().getStorageContainer(anyLong())).thenReturn(null);
		DisableStorageContainerEvent reqEvent = StorageContainerTestData.getDisableStorageContainerEvent();
		reqEvent.setSessionDataBean(StorageContainerTestData.getSessionDataBean());

		StorageContainerDisabledEvent response = storageContainerService.disableStorageContainer(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testSuccessfulStorageContainerDisable() {
		DisableStorageContainerEvent reqEvent = StorageContainerTestData.getDisableStorageContainerEvent();
		StorageContainer scToDelete = StorageContainerTestData.getStorageContainer(1L);
		when(storageContainerDao.getStorageContainer(anyLong())).thenReturn(scToDelete);
		StorageContainerDisabledEvent response = storageContainerService.disableStorageContainer(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(StorageContainerTestData.ACTIVITY_STATUS_CLOSED, scToDelete.getActivityStatus());
	}

	@Test
	public void testStorageContainerDisableWithServerErr() {
		DisableStorageContainerEvent reqEvent = StorageContainerTestData.getDisableStorageContainerEvent();
		doThrow(new RuntimeException()).when(storageContainerDao).saveOrUpdate(any(StorageContainer.class));
		StorageContainerDisabledEvent response = storageContainerService.disableStorageContainer(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

}
	