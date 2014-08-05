
package com.krishagni.catissueplus.core.services.testdata;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.krishagni.catissueplus.core.administrative.domain.Address;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CreateSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.GetSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteDetails;
import com.krishagni.catissueplus.core.administrative.events.SitePatchDetails;
import com.krishagni.catissueplus.core.administrative.events.UpdateSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.UserInfo;
import com.krishagni.catissueplus.core.auth.domain.factory.AuthenticationType;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.common.util.Status;

import edu.wustl.common.beans.SessionDataBean;

public class SiteTestData {

	public static final String SITE_NAME = "site name";

	public static final String USER_NAME = "user name";

	public static final String ZIPCODE = "zipcode";

	public static final String STREET = "street";

	public static final String COUNTRY = "country";

	public static final String CITY = "city";

	public static final String PATCH_SITE = "patch site";

	public static final String EMAIL_ADDRESS = "email address";

	public static final String SITE_TYPE = "site type";

	public static Site getSite() {
		Site site = new Site();
		site.setId(46L);
		site.setName("Nagpur");
		site.setEmailAddress("admin@admin.com");
		Address address = new Address();
		address.setCity("pune");
		address.setCountry("india");
		address.setStreet("SB Road");
		address.setZipCode("412312");
		site.setAddress(address);
		site.setActivityStatus("Active");
		site.setCoordinatorCollection(getCoordinatorCollection());
		site.setScgCollection(new HashSet<SpecimenCollectionGroup>());
		site.setStorageContainerCollection(new HashSet<StorageContainer>());
		return site;
	}

	public static Site getSiteWithscgCollection() {
		Site site = getSite();
		site.setScgCollection(SiteTestData.getScgCollection());
		return site;
	}

	public static Site getSiteWithStorageContainerCollection() {
		Site site = getSite();
		site.setStorageContainerCollection(SiteTestData.getStorageContainerCollection());
		return site;
	}

	private static Set<StorageContainer> getStorageContainerCollection() {
		Set<StorageContainer> storageContainerCollection = new HashSet<StorageContainer>();
		StorageContainer storageContainer = new StorageContainer();
		storageContainer.setActivityStatus("Active");
		storageContainer.setId(1L);
		storageContainer.setName("ST");
		storageContainerCollection.add(storageContainer);
		return storageContainerCollection;
	}

	private static Set<User> getCoordinatorCollection() {
		Set<User> coordinatorCollection = new HashSet<User>();
		coordinatorCollection.add(getUser("admin@admin.com"));
		coordinatorCollection.add(getUser("ajay@ajay.com"));
		return coordinatorCollection;
	}

	public static CreateSiteEvent getCreateSiteEvent() {
		CreateSiteEvent reqEvent = new CreateSiteEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		SiteDetails siteDetails = getDetails();
		siteDetails.setCountry("India");
		reqEvent.setSiteDetails(siteDetails);
		return reqEvent;
	}

	public static SessionDataBean getSessionDataBean() {
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setAdmin(true);
		sessionDataBean.setCsmUserId("1");
		sessionDataBean.setFirstName("admin");
		sessionDataBean.setIpAddress("127.0.0.1");
		sessionDataBean.setLastName("admin");
		sessionDataBean.setUserId(1L);
		sessionDataBean.setUserName("admin@admin.com");
		return sessionDataBean;
	}

	public static HashSet<SpecimenCollectionGroup> getScgCollection() {
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setId(1L);
		scg.setName("SCGCollection");
		scg.setClinicalDiagnosis("");
		scg.setActivityStatus("Active");
		scg.setClinicalStatus("");
		scg.setCollectionComments("");
		scg.setCollectionSite(SiteTestData.getSite());
		scg.setCollectionStatus("");
		scg.setBarcode("dsdds3333");
		scg.setCollectionTimestamp(new Date());
		scg.setCollector(getUser("admin@admin.com"));
		scg.setReceiver(getUser("admin@admin.com"));

		HashSet<SpecimenCollectionGroup> scgCollection = new HashSet<SpecimenCollectionGroup>();
		scgCollection.add(scg);
		return scgCollection;
	}

	public static CreateSiteEvent getCreateSiteEventWithEmptySiteName() {
		CreateSiteEvent reqEvent = getCreateSiteEvent();
		reqEvent.getSiteDetails().setName("");
		return reqEvent;
	}

	public static CreateSiteEvent getCreateSiteEventWithEmptySiteType() {
		CreateSiteEvent reqEvent = getCreateSiteEvent();
		reqEvent.getSiteDetails().setType("");
		return reqEvent;
	}

	public static User getUser(String loginName) {
		User user = new User();
		user.setLoginName(loginName);
		return user;
	}

	public static UpdateSiteEvent getUpdateSiteEvent() {
		SiteDetails details = getDetails();
		details.setCountry("India");
		UpdateSiteEvent reqEvent = new UpdateSiteEvent(details, details.getId());
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setSiteDetails(details);
		return reqEvent;
	}

	public static UpdateSiteEvent getUpdateSiteEventWithSiteName() {
		SiteDetails details = getDetails();
		details.setCountry("India");
		details.setId(null);
		UpdateSiteEvent reqEvent = new UpdateSiteEvent(details, details.getName());
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setSiteDetails(details);
		return reqEvent;
	}

	private static List<UserInfo> getCoordinatorNameCollection() {
		List<UserInfo> userInfos = new ArrayList<UserInfo>();
		UserInfo userInfo = new UserInfo();
		userInfo.setDomainName("catissue");
		userInfo.setLoginName("admin@admin.com");
		userInfos.add(userInfo);
		return userInfos;
	}

	private static SiteDetails getDetails() {
		SiteDetails details = new SiteDetails();
		details.setId(1L);
		details.setCity("Pune");
		details.setCountry("india");
		details.setFaxNumber("333111");
		details.setEmailAddress("admin@admin.com");
		details.setPhoneNumber("9103201122");
		details.setState("Maharashtra");
		details.setStreet("MyStreet");
		details.setZipCode("412312");
		details.setName("DSD");
		details.setType("Not Specified");
		details.setCoordinatorCollection(getCoordinatorNameCollection());
		details.setActivityStatus("Active");
		return details;
	}

	private static SitePatchDetails getPatchSiteDetails() {
		SitePatchDetails details = new SitePatchDetails();
		details.setId(1L);
		details.setCity("Pune");
		details.setCountry("india");
		details.setFaxNumber("333111");
		details.setEmailAddress("admin@admin.com");
		details.setPhoneNumber("9103201122");
		details.setState("Maharashtra");
		details.setStreet("MyStreet");
		details.setZipCode("412312");
		details.setName("DSD");
		details.setType("Not Specified");
		details.setCoordinatorCollection(getCoordinatorNameCollection());
		details.setActivityStatus("Active");
		return details;
	}

	private static List<UserInfo> getCoordinatorCollectionList() {
		List<UserInfo> users = new ArrayList<UserInfo>();
		UserInfo userInfo = new UserInfo();
		userInfo.setDomainName(AuthenticationType.CATISSUE.value());
		userInfo.setLoginName("admin@admin.com");
		users.add(userInfo);
		return users;
	}

	public static UpdateSiteEvent getUpdateSiteEventWithEmptySiteName() {
		UpdateSiteEvent reqEvent = getUpdateSiteEvent();
		reqEvent.getSiteDetails().setName("");
		return reqEvent;
	}

	public static UpdateSiteEvent getUpdateSiteEventWithEmptySiteType() {
		UpdateSiteEvent reqEvent = getUpdateSiteEvent();
		reqEvent.getSiteDetails().setType("");
		return reqEvent;
	}

	public static PatchSiteEvent getPatchData() {
		PatchSiteEvent event = new PatchSiteEvent();
		event.setSiteId(48L);
		SitePatchDetails details = new SitePatchDetails();
		try {
			BeanUtils.populate(details, getSitePatchAttributes());
		}
		catch (Exception e) {
			reportError(SiteErrorCode.BAD_REQUEST, PATCH_SITE);
		}
		details.setModifiedAttributes(new ArrayList<String>(getSitePatchAttributes().keySet()));
		event.setDetails(details);
		return event;
	}

	private static Map<String, Object> getSitePatchAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("name", "pune");
		attributes.put("type", "Collection Site");
		attributes.put("coordinatorCollection", getCoordinatorCollectionList());
		attributes.put("emailAddress", "admin@admin.com");
		attributes.put("activityStatus", Status.ACTIVITY_STATUS_DISABLED.getStatus());
		attributes.put("country", "India");
		attributes.put("state", "maharashtra");
		attributes.put("city", "mumbai");
		attributes.put("faxNumber", "43249-434");
		attributes.put("phoneNumber", "76543");
		attributes.put("street", "kasaba road");
		attributes.put("zipCode", "76543");
		return attributes;
	}

	public static CreateSiteEvent getCreateSiteEventWithEmptyCityName() {
		CreateSiteEvent reqEvent = getCreateSiteEvent();
		reqEvent.getSiteDetails().setCity("");
		return reqEvent;
	}

	public static CreateSiteEvent getCreateSiteEventWithEmptyCountryName() {
		CreateSiteEvent reqEvent = getCreateSiteEvent();
		reqEvent.getSiteDetails().setCountry("");
		return reqEvent;
	}

	public static CreateSiteEvent getCreateSiteEventWithEmptyStreet() {
		CreateSiteEvent reqEvent = getCreateSiteEvent();
		reqEvent.getSiteDetails().setStreet("");
		return reqEvent;
	}

	public static CreateSiteEvent getCreateSiteEventWithEmptyZipCode() {
		CreateSiteEvent reqEvent = getCreateSiteEvent();
		reqEvent.getSiteDetails().setZipCode("");
		return reqEvent;
	}

	public static UpdateSiteEvent getUpdateSiteEventWithEmptyCityName() {
		UpdateSiteEvent reqEvent = getUpdateSiteEvent();
		reqEvent.getSiteDetails().setCity("");
		return reqEvent;
	}

	public static UpdateSiteEvent getUpdateSiteEventWithEmptyCountryName() {
		UpdateSiteEvent reqEvent = getUpdateSiteEvent();
		reqEvent.getSiteDetails().setCountry("");
		return reqEvent;
	}

	public static UpdateSiteEvent getUpdateSiteEventWithEmptyStreet() {
		UpdateSiteEvent reqEvent = getUpdateSiteEvent();
		reqEvent.getSiteDetails().setStreet("");
		return reqEvent;
	}

	public static UpdateSiteEvent getUpdateSiteEventWithEmptyZipCode() {
		UpdateSiteEvent reqEvent = getUpdateSiteEvent();
		reqEvent.getSiteDetails().setZipCode("");
		return reqEvent;
	}

	public static PatchSiteEvent getPatchSiteEventWithEmptyCityName() {
		PatchSiteEvent reqEvent = getPatchData();
		reqEvent.getDetails().setCity("");
		return reqEvent;
	}

	private static PatchSiteEvent getEmptyPatchData() {
		PatchSiteEvent event = new PatchSiteEvent();
		SitePatchDetails details = new SitePatchDetails();
		event.setDetails(details);
		return event;
	}

	public static PatchSiteEvent getPatchSiteEventWithEmptyCountryName() {
		PatchSiteEvent reqEvent = getPatchData();
		reqEvent.getDetails().setCountry("");
		return reqEvent;
	}

	public static PatchSiteEvent getPatchSiteEventWithEmptyStreet() {
		PatchSiteEvent reqEvent = getPatchData();
		reqEvent.getDetails().setStreet("");
		return reqEvent;
	}

	public static PatchSiteEvent getPatchSiteEventWithEmptyZipCode() {
		PatchSiteEvent reqEvent = getPatchData();
		reqEvent.getDetails().setZipCode("");
		return reqEvent;
	}

	public static PatchSiteEvent getPatchSiteEventWithModifiedName() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getDetails().setName("Patana");
		return event;
	}

	public static PatchSiteEvent getPatchSiteEventWithModifiedType() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getDetails().setType("Collection Site");
		return event;
	}

	public static PatchSiteEvent getPatchSiteEventWithModifiedActivityStatus() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getDetails().setActivityStatus("Active");
		return event;
	}

	public static PatchSiteEvent getPatchSiteEventWithModifiedCountry() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getDetails().setCountry("India");
		return event;
	}

	public static PatchSiteEvent getPatchSiteEventWithModifiedCity() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getDetails().setCity("India");
		return event;
	}

	public static PatchSiteEvent getPatchSiteEventWithModifiedStreet() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getDetails().setStreet("SB Road");
		return event;
	}

	public static PatchSiteEvent getPatchSiteEventWithModifiedState() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getDetails().setState("Maharashtra");
		return event;
	}

	public static PatchSiteEvent getPatchSiteEventWithModifiedFaxNumber() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getDetails().setFaxNumber("2344324");
		return event;
	}

	public static PatchSiteEvent getPatchSiteEventWithModifiedZipCode() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getDetails().setZipCode("412312");
		return event;
	}

	public static PatchSiteEvent getPatchSiteEventWithModifiedPhoneNumber() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getDetails().setPhoneNumber("913433434232");
		return event;
	}

	public static PatchSiteEvent getPatchSiteEventWithModifiedEmailAddress() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getDetails().setEmailAddress("admin@admin.com");
		return event;
	}

	public static PatchSiteEvent getPatchSiteEventWithModifiedCoordinatorCollection() {
		PatchSiteEvent event = getEmptyPatchData();
		List<UserInfo> coordinatorCollection = new ArrayList<UserInfo>();
		UserInfo userInfo = new UserInfo();
		userInfo.setDomainName("catissue");
		userInfo.setLoginName("admin@admin.com");
		coordinatorCollection.add(userInfo);
		event.getDetails().setCoordinatorCollection(coordinatorCollection);
		return event;
	}

	public static CreateSiteEvent getCreateSiteEventWithInvalidEmail() {
		CreateSiteEvent reqEvent = getCreateSiteEvent();
		SiteDetails details = reqEvent.getSiteDetails();
		details.setEmailAddress("admin");
		reqEvent.setSiteDetails(details);
		return reqEvent;
	}

	public static PatchSiteEvent getPatchDataWithInavalidAttribute() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getDetails().setType("");
		event.getDetails().getModifiedAttributes().add("type");
		return event;
	}

	public static PatchSiteEvent getPatchDataWithSiteName() {
		SitePatchDetails details = getPatchSiteDetails();
		details.setId(null);
		PatchSiteEvent reqEvent = new PatchSiteEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setDetails(details);
		reqEvent.setSiteName(details.getName());
		return reqEvent;
	}

	public static DeleteSiteEvent getDeleteSiteEvent() {
		DeleteSiteEvent event = new DeleteSiteEvent();
		event.setId(1L);
		return event;
	}

	public static UpdateSiteEvent getUpdateSiteEventWithDisabledActvityStatus() {
		UpdateSiteEvent reqEvent = getUpdateSiteEvent();
		reqEvent.getSiteDetails().setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
		return reqEvent;
	}

	public static UpdateSiteEvent getDeleteSiteEventWithDisabledActvityStatus() {
		UpdateSiteEvent reqEvent = getUpdateSiteEvent();
		reqEvent.getSiteDetails().setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
		return reqEvent;
	}

	public static List<Site> getSites() {
		List<Site> sites = new ArrayList<Site>();
		sites.add(getSite());
		sites.add(getSite());
		return sites;
	}

	public static ReqAllSiteEvent getAllSitesEvent() {
		ReqAllSiteEvent event = new ReqAllSiteEvent();
		event.setMaxResults(1000);
		return event;
	}

	public static GetSiteEvent getSiteEvent() {
		GetSiteEvent event = new GetSiteEvent();
		event.setId(1L);
		return event;
	}

	public static GetSiteEvent getSiteEventForName() {
		GetSiteEvent event = new GetSiteEvent();
		event.setName("In Transit");
		return event;
	}

	public static DeleteSiteEvent getDeleteSiteEventForName() {
		DeleteSiteEvent event = new DeleteSiteEvent();
		event.setName("In Transit");
		return event;
	}

}
