
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

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CreateSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateSiteEvent;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SiteFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.impl.SiteFactoryImpl;
import com.krishagni.catissueplus.core.biospecimen.events.SiteDetails;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.SiteDao;
import com.krishagni.catissueplus.core.biospecimen.services.SiteService;
import com.krishagni.catissueplus.core.biospecimen.services.impl.SiteServiceImpl;
import com.krishagni.catissueplus.core.common.errors.ErrorCodeEnum;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.SiteTestData;

public class SiteTest {

	@Mock
	DaoFactory daoFactory;

	@Mock
	SiteDao siteDao;

	@Mock
	UserDao userDao;

	SiteFactory siteFactory;

	SiteService siteService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		when(daoFactory.getSiteDao()).thenReturn(siteDao);
		when(daoFactory.getUserDao()).thenReturn(userDao);

		siteService = new SiteServiceImpl();
		siteFactory = new SiteFactoryImpl();
		((SiteServiceImpl) siteService).setDaoFactory(daoFactory);
		((SiteServiceImpl) siteService).setSiteFactory(siteFactory);
		((SiteFactoryImpl) siteFactory).setDaoFactory(daoFactory);

		when(siteDao.getSiteByName(anyString())).thenReturn(SiteTestData.getSite());
		when(userDao.getUserByLoginNameAndDomainName(anyString(), anyString())).thenReturn(
				SiteTestData.getUser("admin@admin.com"));

	}

		@Test
		public void testForSuccessfulSiteCreation() {
			CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEvent();
			when(siteDao.getSiteByName(anyString())).thenReturn(null);
			when(siteDao.isUniqueSiteName(anyString())).thenReturn(Boolean.TRUE);
			SiteCreatedEvent response = siteService.createSite(reqEvent);
	
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.OK, response.getStatus());
			SiteDetails createdSiteDto = response.getSiteDetails();
			assertEquals(reqEvent.getSiteDetails().getName(), createdSiteDto.getName());
		}
	
		@Test
		public void testSiteCreationWithDuplicateSiteName() {
			when(siteDao.isUniqueSiteName(anyString())).thenReturn(Boolean.FALSE);
			CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEvent();
			SiteCreatedEvent response = siteService.createSite(reqEvent);
	
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.SITE_NAME, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.DUPLICATE_SITE_NAME.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSiteCreationWithEmptySiteName() {
			CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEventWithEmptySiteName();
			SiteCreatedEvent response = siteService.createSite(reqEvent);
	
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.SITE_NAME, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSiteCreationWithEmptySiteType() {
			CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEventWithEmptySiteType();
			SiteCreatedEvent response = siteService.createSite(reqEvent);
	
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.SITE_TYPE, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSiteCreationWithInvalidUser() {
			when(userDao.getUserByLoginNameAndDomainName(anyString(), anyString())).thenReturn(null);
			CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEvent();
			SiteCreatedEvent response = siteService.createSite(reqEvent);
	
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.USER_NAME, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	
		}
	
		@Test
		public void testSiteCreationWithServerErr() {
	
			doThrow(new RuntimeException()).when(siteDao).saveOrUpdate(any(Site.class));
			when(siteDao.isUniqueSiteName(anyString())).thenReturn(Boolean.TRUE);
	
			CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEvent();
			SiteCreatedEvent response = siteService.createSite(reqEvent);
	
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
		}
	
		@Test
		public void testForSuccessfulSiteUpdate() {
			when(siteDao.isUniqueSiteName(anyString())).thenReturn(Boolean.TRUE);
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
	
			UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEvent();
			SiteUpdatedEvent response = siteService.updateSite(reqEvent);
			assertEquals(EventStatus.OK, response.getStatus());
			SiteDetails createSite = response.getSiteDetails();
	
			assertEquals(reqEvent.getSiteDetails().getName(), createSite.getName());
		}
	
		@Test
		public void testSiteUpdationWithDuplicateSiteName() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			when(siteDao.isUniqueSiteName(anyString())).thenReturn(Boolean.FALSE);
	
			UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEvent();
			SiteUpdatedEvent response = siteService.updateSite(reqEvent);
	
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.SITE_NAME, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.DUPLICATE_SITE_NAME.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSiteUpdationWithEmptySiteName() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
	
			UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEventWithEmptySiteName();
			SiteUpdatedEvent response = siteService.updateSite(reqEvent);
	
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.SITE_NAME, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSiteUpdationWithEmptySiteType() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
	
			UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEventWithEmptySiteType();
			SiteUpdatedEvent response = siteService.updateSite(reqEvent);
	
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.SITE_TYPE, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSiteUpdationWithServerErr() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			when(siteDao.isUniqueSiteName(anyString())).thenReturn(Boolean.TRUE);
			doThrow(new RuntimeException()).when(siteDao).saveOrUpdate(any(Site.class));
	
			UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEvent();
			SiteUpdatedEvent response = siteService.updateSite(reqEvent);
	
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
		}
	
		@Test
		public void testSiteUpdationNullSite() {
			when(siteDao.getSiteById(anyLong())).thenReturn(null);
	
			UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEvent();
			SiteUpdatedEvent response = siteService.updateSite(reqEvent);
	
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		}
	
		@Test
		public void testUserUpdationWithInvalidUser() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEvent();
			when(userDao.getUserByLoginNameAndDomainName(anyString(), anyString())).thenReturn(null);
	
			SiteUpdatedEvent response = siteService.updateSite(reqEvent);
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.USER_NAME, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	
		}
	
		@Test
		public void testSuccessfullPatchSite() {
			when(daoFactory.getSiteDao().getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			when(daoFactory.getSiteDao().isUniqueSiteName(anyString())).thenReturn(true);
			PatchSiteEvent reqEvent = SiteTestData.getPatchData();
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.OK, response.getStatus());
		}

	@Test
	public void testPatchSiteWithInvalidAttribute() {
		when(daoFactory.getSiteDao().getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
		when(daoFactory.getSiteDao().isUniqueSiteName(anyString())).thenReturn(false);
		
		PatchSiteEvent reqEvent = SiteTestData.getPatchDataWithInavalidAttribute();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(response.getStatus(),EventStatus.BAD_REQUEST);
		
	}

		@Test
		public void testSitePatchNullSite() {
			when(siteDao.getSiteById(anyLong())).thenReturn(null);
	
			PatchSiteEvent reqEvent = SiteTestData.getPatchData();
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
	
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		}
	
		@Test
		public void testPatchSiteServerError() {
			when(daoFactory.getSiteDao().getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			when(daoFactory.getSiteDao().isUniqueSiteName(anyString())).thenReturn(true);
			PatchSiteEvent reqEvent = SiteTestData.getPatchData();
			doThrow(new RuntimeException()).when(siteDao).saveOrUpdate(any(Site.class));
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
		}
	
		@Test
		public void testSiteCreationWithEmptyStreet() {
			CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEventWithEmptyStreet();
	
			SiteCreatedEvent response = siteService.createSite(reqEvent);
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.STREET, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSiteCreationWithEmptyZipCode() {
			CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEventWithEmptyZipCode();
	
			SiteCreatedEvent response = siteService.createSite(reqEvent);
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.ZIPCODE, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSiteCreationWithEmptyCityName() {
			CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEventWithEmptyCityName();
	
			SiteCreatedEvent response = siteService.createSite(reqEvent);
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.CITY, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSiteCreationWithEmptyCountryName() {
			CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEventWithEmptyCountryName();
	
			SiteCreatedEvent response = siteService.createSite(reqEvent);
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.COUNTRY, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSiteUpdationWithEmptyStreet() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEventWithEmptyStreet();
	
			SiteUpdatedEvent response = siteService.updateSite(reqEvent);
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.STREET, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSiteUpdationWithEmptyZipCode() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEventWithEmptyZipCode();
	
			SiteUpdatedEvent response = siteService.updateSite(reqEvent);
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.ZIPCODE, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSiteUpdationWithEmptyCityName() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEventWithEmptyCityName();
	
			SiteUpdatedEvent response = siteService.updateSite(reqEvent);
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.CITY, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSiteUpdationWithEmptyCountryName() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEventWithEmptyCountryName();
	
			SiteUpdatedEvent response = siteService.updateSite(reqEvent);
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.COUNTRY, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSitePatchWithEmptyStreet() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithEmptyStreet();
	
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.STREET, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSitePatchWithEmptyZipCode() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithEmptyZipCode();
	
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.ZIPCODE, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSitePatchWithEmptyCityName() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithEmptyCityName();
	
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.CITY, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSitePatchWithEmptyCountryName() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithEmptyCountryName();
	
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.COUNTRY, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}
	
		@Test
		public void testSitePatchWithModifiedSiteName() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedName();
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.OK, response.getStatus());
		}
	
		@Test
		public void testSitePatchWithModifiedSiteType() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedType();
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.OK, response.getStatus());
		}
	
		@Test
		public void testSitePatchWithModifiedActivityStatus() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedActivityStatus();
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.OK, response.getStatus());
		}
	
		@Test
		public void testSitePatchWithModifiedSiteCity() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedCity();
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.OK, response.getStatus());
		}
	
		@Test
		public void testSitePatchWithModifiedSiteCountry() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedCountry();
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.OK, response.getStatus());
		}
	
		@Test
		public void testSitePatchWithModifiedSiteState() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedState();
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.OK, response.getStatus());
		}
	
		@Test
		public void testSitePatchWithModifiedSiteStreet() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedStreet();
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.OK, response.getStatus());
		}
	
		@Test
		public void testSitePatchWithModifiedSiteZipCode() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedZipCode();
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.OK, response.getStatus());
		}
	
		@Test
		public void testSitePatchWithModifiedSiteFaxNumber() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedFaxNumber();
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.OK, response.getStatus());
		}
	
		@Test
		public void testSitePatchWithModifiedSitePhoneNumber() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedPhoneNumber();
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.OK, response.getStatus());
		}
		
		@Test
		public void testSitePatchWithModifiedSiteCoordinators() {
			when(siteDao.getSiteById(anyLong())).thenReturn(SiteTestData.getSite());
			PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedCoordinatorCollection();
			SiteUpdatedEvent response = siteService.patchSite(reqEvent);
			assertNotNull("response cannot be null", response);
			assertEquals(EventStatus.OK, response.getStatus());
		}
		
		@Test
		public void testSiteCreationWithInvalidEmailAddress() {
			CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEventWithInvalidEmail();
			SiteCreatedEvent response = siteService.createSite(reqEvent);
	
			assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
			assertEquals(1, response.getErroneousFields().length);
			assertEquals(SiteTestData.EMAIL_ADDRESS, response.getErroneousFields()[0].getFieldName());
			assertEquals(SiteErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		}

}
