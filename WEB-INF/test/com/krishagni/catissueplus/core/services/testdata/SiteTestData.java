
package com.krishagni.catissueplus.core.services.testdata;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.krishagni.catissueplus.core.administrative.domain.Address;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CreateSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.biospecimen.events.SiteDetails;
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
	
	public static Site getSite() {
		Site site = new Site();
		site.setId(46L);
		site.setName("Nagpur");
		Address address = new Address();
		address.setCity("pune");
		address.setCountry("india");
		address.setStreet("SB Road");
		address.setZipCode("412312");
		site.setAddress(address);
		site.setCoordinatorCollection(getCoordinatorCollection());
		return site;
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
		
		UpdateSiteEvent reqEvent = new UpdateSiteEvent(details, details.getId());
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setSiteDetails(details);
		return reqEvent;
	}

	private static List<String> getCoordinatorNameCollection() {
		List<String> loginNames = new ArrayList<String>();
		loginNames.add("admin@admin.com");
		loginNames.add("ajay@ajay.com");
		return loginNames;
	}

	private static SiteDetails getDetails() {
		SiteDetails details = new SiteDetails();
		details.setCity("Pune");
		details.setCountry("india");
		details.setFaxNumber("333111");
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

	private static List<String> getCoordinatorCollectionList() {
		List<String> users=new ArrayList<String>();
		users.add("admin@admin.com");
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
		SiteDetails details = new SiteDetails();
		try {
			BeanUtils.populate(details, getSitePatchAttributes());
		}
		catch (Exception e) {
			reportError(SiteErrorCode.BAD_REQUEST, PATCH_SITE);
		}
		details.setModifiedAttributes(new ArrayList<String>(getSitePatchAttributes().keySet()));
		event.setSiteDetails(details);
		return event;
	}

	private static Map<String, Object> getSitePatchAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("name", "pune");
	  attributes.put("type", "Collection Site");
	  attributes.put("coordinatorCollection", getCoordinatorCollectionList());
		attributes.put("activityStatus", Status.ACTIVITY_STATUS_DISABLED.getStatus());
		attributes.put("country", "india");
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
		reqEvent.getSiteDetails().setCity("");
		return reqEvent;
	}

	private static PatchSiteEvent getEmptyPatchData() {
		PatchSiteEvent event = new PatchSiteEvent();
		SiteDetails details = new SiteDetails();
		event.setSiteDetails(details);
		return event;
	}

	public static PatchSiteEvent getPatchSiteEventWithEmptyCountryName() {
		PatchSiteEvent reqEvent = getPatchData();
		reqEvent.getSiteDetails().setCountry("");
		return reqEvent;
	}

	public static PatchSiteEvent getPatchSiteEventWithEmptyStreet() {
		PatchSiteEvent reqEvent = getPatchData();
		reqEvent.getSiteDetails().setStreet("");
		return reqEvent;
	}

	public static PatchSiteEvent getPatchSiteEventWithEmptyZipCode() {
		PatchSiteEvent reqEvent = getPatchData();
		reqEvent.getSiteDetails().setZipCode("");
		return reqEvent;
	}

	public static PatchSiteEvent getPatchSiteEventWithModifiedName() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getSiteDetails().setName("Patana");
		return event;
	}	
	
	public static PatchSiteEvent getPatchSiteEventWithModifiedType() {
		PatchSiteEvent event =  getEmptyPatchData();
		event.getSiteDetails().setType("Collection Site");
		return event;
	}	
	
	public static PatchSiteEvent getPatchSiteEventWithModifiedActivityStatus() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getSiteDetails().setActivityStatus("Active");
		return event;
	}	
	
	public static PatchSiteEvent getPatchSiteEventWithModifiedCountry() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getSiteDetails().setCountry("india");
		return event;
	}	
	
	public static PatchSiteEvent getPatchSiteEventWithModifiedCity() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getSiteDetails().setCity("india");
		return event;
	}	

	public static PatchSiteEvent getPatchSiteEventWithModifiedStreet() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getSiteDetails().setStreet("SB Road");
		return event;
	}	
	
	public static PatchSiteEvent getPatchSiteEventWithModifiedState() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getSiteDetails().setState("Maharashtra");
		return event;
	}	

	public static PatchSiteEvent getPatchSiteEventWithModifiedFaxNumber() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getSiteDetails().setFaxNumber("2344324");
		return event;
	}	

	public static PatchSiteEvent getPatchSiteEventWithModifiedZipCode() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getSiteDetails().setZipCode("412312");
		return event;
	}	

	public static PatchSiteEvent getPatchSiteEventWithModifiedPhoneNumber() {
		PatchSiteEvent event = getEmptyPatchData();
		event.getSiteDetails().setPhoneNumber("913433434232");
		return event;
	}	
	
	public static PatchSiteEvent getPatchSiteEventWithModifiedCoordinatorCollection() {
		PatchSiteEvent event = getEmptyPatchData();
		List<String> coordinatorCollection = new ArrayList<String>();
		coordinatorCollection.add("site_admin@washu.com");
		event.getSiteDetails().setCoordinatorCollection(coordinatorCollection);
		return event;
	}

	public static CreateSiteEvent getCreateSiteEventWithInvalidEmail() {
		CreateSiteEvent reqEvent = getCreateSiteEvent();
		SiteDetails details = reqEvent.getSiteDetails();
		details.setEmailAddress("admin");
		reqEvent.setSiteDetails(details);
		return reqEvent;
	}	

}
