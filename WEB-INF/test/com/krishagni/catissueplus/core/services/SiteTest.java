
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

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.impl.SiteFactoryImpl;
import com.krishagni.catissueplus.core.administrative.events.AllSitesEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.GetSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteDetails;
import com.krishagni.catissueplus.core.administrative.events.SiteGotEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateSiteEvent;
import com.krishagni.catissueplus.core.administrative.repository.PermissibleValueDao;
import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.administrative.services.SiteService;
import com.krishagni.catissueplus.core.administrative.services.impl.PermissibleValueServiceImpl;
import com.krishagni.catissueplus.core.administrative.services.impl.SiteServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.PermissibleValuesManager;
import com.krishagni.catissueplus.core.common.PermissibleValuesManagerImpl;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.PermissibleValueTestData;
import com.krishagni.catissueplus.core.services.testdata.SiteTestData;

public class SiteTest {

	@Mock
	DaoFactory daoFactory;

	@Mock
	SiteDao siteDao;

	@Mock
	UserDao userDao;

	@Mock
	PermissibleValueDao pvDao;

	@Mock
	CommonValidator commonValidator;

	PermissibleValuesManager pvManager;

	private PermissibleValueService pvService;

	SiteFactory siteFactory;

	SiteService siteService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		when(daoFactory.getSiteDao()).thenReturn(siteDao);
		when(daoFactory.getUserDao()).thenReturn(userDao);

		when(daoFactory.getPermissibleValueDao()).thenReturn(pvDao);
		pvService = new PermissibleValueServiceImpl();

		((PermissibleValueServiceImpl) pvService).setDaoFactory(daoFactory);
		pvManager = new PermissibleValuesManagerImpl();
		((PermissibleValuesManagerImpl) pvManager).setPermissibleValueSvc(pvService);
		CommonValidator.setPvManager(pvManager);
		when(pvDao.getAllValuesByAttribute(anyString())).thenReturn(PermissibleValueTestData.getPvValues());

		siteService = new SiteServiceImpl();
		siteFactory = new SiteFactoryImpl();
		((SiteServiceImpl) siteService).setDaoFactory(daoFactory);
		((SiteServiceImpl) siteService).setSiteFactory(siteFactory);
		((SiteFactoryImpl) siteFactory).setDaoFactory(daoFactory);

		when(siteDao.getSite(anyString())).thenReturn(SiteTestData.getSite());
		when(siteDao.isUniqueSiteName(anyString())).thenReturn(Boolean.TRUE);
		when(userDao.getUserByLoginNameAndDomainName(anyString(), anyString())).thenReturn(
				SiteTestData.getUser("admin@admin.com"));

		when(daoFactory.getSiteDao().getSite(anyLong())).thenReturn(SiteTestData.getSite());
		when(daoFactory.getSiteDao().getSite(anyString())).thenReturn(SiteTestData.getSite());
	}

	@Test
	public void testForSuccessfulSiteCreation() {
		CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEvent();
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

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.SITE_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.DUPLICATE_SITE_NAME.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSiteCreationWithEmptySiteName() {
		CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEventWithEmptySiteName();
		SiteCreatedEvent response = siteService.createSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.SITE_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSiteCreationWithEmptySiteType() {
		CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEventWithEmptySiteType();
		SiteCreatedEvent response = siteService.createSite(reqEvent);

		assertNotNull("response cannot be null", response);
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

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.USER_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());

	}

	@Test
	public void testSiteCreationWithServerErr() {

		doThrow(new RuntimeException()).when(siteDao).saveOrUpdate(any(Site.class));

		CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEvent();
		SiteCreatedEvent response = siteService.createSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testSiteCreationWithEmptyStreet() {
		CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEventWithEmptyStreet();
		SiteCreatedEvent response = siteService.createSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.STREET, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSiteCreationWithEmptyZipCode() {
		CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEventWithEmptyZipCode();
		SiteCreatedEvent response = siteService.createSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.ZIPCODE, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSiteCreationWithEmptyCityName() {
		CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEventWithEmptyCityName();
		SiteCreatedEvent response = siteService.createSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.CITY, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSiteCreationWithEmptyCountryName() {
		CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEventWithEmptyCountryName();
		SiteCreatedEvent response = siteService.createSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.COUNTRY, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSiteCreationWithInvalidEmailAddress() {
		CreateSiteEvent reqEvent = SiteTestData.getCreateSiteEventWithInvalidEmail();
		SiteCreatedEvent response = siteService.createSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.EMAIL_ADDRESS, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testForSuccessfulSiteUpdate() {

		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEvent();
		SiteUpdatedEvent response = siteService.updateSite(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		SiteDetails createSite = response.getSiteDetails();

		assertEquals(reqEvent.getSiteDetails().getName(), createSite.getName());
	}

	@Test
	public void testForSuccessfulSiteUpdateUsingSiteName() {

		when(siteDao.getSite(anyString())).thenReturn(SiteTestData.getSite());

		UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEventWithSiteName();
		SiteUpdatedEvent response = siteService.updateSite(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		SiteDetails createSite = response.getSiteDetails();

		assertEquals(reqEvent.getSiteDetails().getName(), createSite.getName());
	}

	@Test
	public void testSiteUpdationWithDuplicateSiteName() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());
		when(siteDao.isUniqueSiteName(anyString())).thenReturn(Boolean.FALSE);

		UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEvent();
		SiteUpdatedEvent response = siteService.updateSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.SITE_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.DUPLICATE_SITE_NAME.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSiteUpdationWithEmptySiteName() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEventWithEmptySiteName();
		SiteUpdatedEvent response = siteService.updateSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.SITE_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSiteUpdationWithEmptySiteType() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEventWithEmptySiteType();
		SiteUpdatedEvent response = siteService.updateSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.SITE_TYPE, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSiteUpdationWithServerErr() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		doThrow(new RuntimeException()).when(siteDao).saveOrUpdate(any(Site.class));

		UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEvent();
		SiteUpdatedEvent response = siteService.updateSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testSiteUpdationNullSite() {
		when(siteDao.getSite(anyLong())).thenReturn(null);

		UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEvent();
		SiteUpdatedEvent response = siteService.updateSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}

	@Test
	public void testSiteUpdationNullSiteUsingSiteName() {
		when(siteDao.getSite(anyString())).thenReturn(null);

		UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEventWithSiteName();
		SiteUpdatedEvent response = siteService.updateSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getSiteName());
	}

	@Test
	public void testUserUpdationWithInvalidUser() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());
		when(userDao.getUserByLoginNameAndDomainName(anyString(), anyString())).thenReturn(null);

		UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEvent();
		SiteUpdatedEvent response = siteService.updateSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.USER_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());

	}

	@Test
	public void testSiteUpdationWithEmptyStreet() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());
		UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEventWithEmptyStreet();
		SiteUpdatedEvent response = siteService.updateSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.STREET, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSiteUpdationWithEmptyZipCode() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEventWithEmptyZipCode();
		SiteUpdatedEvent response = siteService.updateSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.ZIPCODE, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSiteUpdationWithEmptyCityName() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEventWithEmptyCityName();
		SiteUpdatedEvent response = siteService.updateSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.CITY, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSiteUpdationWithEmptyCountryName() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEventWithEmptyCountryName();
		SiteUpdatedEvent response = siteService.updateSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.COUNTRY, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSuccessfullPatchSite() {

		when(daoFactory.getSiteDao().isUniqueSiteName(anyString())).thenReturn(true);
		PatchSiteEvent reqEvent = SiteTestData.getPatchData();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testPatchSiteWithInvalidAttribute() {

		when(daoFactory.getSiteDao().isUniqueSiteName(anyString())).thenReturn(false);

		PatchSiteEvent reqEvent = SiteTestData.getPatchDataWithInavalidAttribute();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(response.getStatus(), EventStatus.BAD_REQUEST);

	}

	@Test
	public void testSitePatchWithNullSite() {
		when(siteDao.getSite(anyLong())).thenReturn(null);

		PatchSiteEvent reqEvent = SiteTestData.getPatchData();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testSitePatchWithNullSiteUsingSiteName() {
		when(siteDao.getSite(anyString())).thenReturn(null);

		PatchSiteEvent reqEvent = SiteTestData.getPatchDataWithSiteName();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testPatchSiteServerError() {

		when(daoFactory.getSiteDao().isUniqueSiteName(anyString())).thenReturn(true);
		PatchSiteEvent reqEvent = SiteTestData.getPatchData();
		doThrow(new RuntimeException()).when(siteDao).saveOrUpdate(any(Site.class));
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testSitePatchWithEmptyStreet() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithEmptyStreet();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.STREET, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSitePatchWithEmptyZipCode() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithEmptyZipCode();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.ZIPCODE, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSitePatchWithEmptyCityName() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithEmptyCityName();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.CITY, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSitePatchWithEmptyCountryName() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithEmptyCountryName();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SiteTestData.COUNTRY, response.getErroneousFields()[0].getFieldName());
		assertEquals(SiteErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSitePatchWithModifiedSiteName() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedName();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSitePatchWithModifiedSiteType() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedType();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSitePatchWithModifiedActivityStatus() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedActivityStatus();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSiteUpdateWithDisabledActivityStatus() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		UpdateSiteEvent reqEvent = SiteTestData.getUpdateSiteEventWithDisabledActvityStatus();
		SiteUpdatedEvent response = siteService.updateSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSitePatchWithModifiedSiteCity() {

		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedCity();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSitePatchWithModifiedSiteCountry() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedCountry();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSitePatchWithModifiedSiteState() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedState();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSitePatchWithModifiedSiteStreet() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedStreet();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSitePatchWithModifiedSiteZipCode() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedZipCode();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSitePatchWithModifiedSiteFaxNumber() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedFaxNumber();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSitePatchWithModifiedEmailAddress() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedEmailAddress();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSitePatchWithModifiedSitePhoneNumber() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedPhoneNumber();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSitePatchWithModifiedSiteCoordinators() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());

		PatchSiteEvent reqEvent = SiteTestData.getPatchSiteEventWithModifiedCoordinatorCollection();
		SiteUpdatedEvent response = siteService.patchSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testForSuccessfulSiteDeletion() {

		DeleteSiteEvent reqEvent = SiteTestData.getDeleteSiteEvent();
		SiteDeletedEvent response = siteService.deleteSite(reqEvent);

		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSiteDeleteWithNullOldSiteObject() {

		when(daoFactory.getSiteDao().getSite(anyLong())).thenReturn(null);
		DeleteSiteEvent reqEvent = SiteTestData.getDeleteSiteEvent();
		SiteDeletedEvent response = siteService.deleteSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testSiteDeleteWithActiveScgChildren() {

		when(daoFactory.getSiteDao().getSite(anyLong())).thenReturn(SiteTestData.getSiteWithscgCollection());
		DeleteSiteEvent reqEvent = SiteTestData.getDeleteSiteEvent();
		SiteDeletedEvent response = siteService.deleteSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testSiteDeleteWithActiveStorageContainerChildren() {

		when(daoFactory.getSiteDao().getSite(anyLong())).thenReturn(SiteTestData.getSiteWithStorageContainerCollection());
		DeleteSiteEvent reqEvent = SiteTestData.getDeleteSiteEvent();
		SiteDeletedEvent response = siteService.deleteSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testSiteDeleteWithoutActiveChildren() {

		when(daoFactory.getSiteDao().getSite(anyLong())).thenReturn(SiteTestData.getSite());
		DeleteSiteEvent reqEvent = SiteTestData.getDeleteSiteEvent();
		SiteDeletedEvent response = siteService.deleteSite(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testForSuccessfulSiteDeleteWithName() {
		when(siteDao.getSite(anyString())).thenReturn(SiteTestData.getSite());
		DeleteSiteEvent reqEvent = SiteTestData.getDeleteSiteEventForName();
		SiteDeletedEvent response = siteService.deleteSite(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testForInvalidSiteDeleteWithName() {
		when(siteDao.getSite(anyString())).thenReturn(null);
		DeleteSiteEvent reqEvent = SiteTestData.getDeleteSiteEventForName();
		SiteDeletedEvent response = siteService.deleteSite(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getName());
	}

	@Test
	public void testGetAllSites() {
		when(siteDao.getAllSites(eq(1000))).thenReturn(SiteTestData.getSites());
		ReqAllSiteEvent reqEvent = SiteTestData.getAllSitesEvent();
		AllSitesEvent response = siteService.getAllSites(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(2, response.getSites().size());
	}

	@Test
	public void testGetSiteById() {
		when(siteDao.getSite(anyLong())).thenReturn(SiteTestData.getSite());
		GetSiteEvent reqEvent = SiteTestData.getSiteEvent();
		SiteGotEvent response = siteService.getSite(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testGetSiteWithWrongId() {
		when(siteDao.getSite(anyLong())).thenReturn(null);
		GetSiteEvent reqEvent = SiteTestData.getSiteEvent();
		SiteGotEvent response = siteService.getSite(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}

	@Test
	public void testGetSiteByName() {
		when(siteDao.getSite(anyString())).thenReturn(SiteTestData.getSite());
		GetSiteEvent reqEvent = SiteTestData.getSiteEventForName();
		SiteGotEvent response = siteService.getSite(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(response.getDetails());
	}

	@Test
	public void testGetSiteWithWrongName() {
		when(siteDao.getSite(anyString())).thenReturn(null);
		GetSiteEvent reqEvent = SiteTestData.getSiteEventForName();
		SiteGotEvent response = siteService.getSite(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getName());
	}
}
