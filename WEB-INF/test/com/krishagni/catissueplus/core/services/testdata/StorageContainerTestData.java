
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
import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CreateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.GetAllStorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.GetStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllStorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetails;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerPatchDetails;
import com.krishagni.catissueplus.core.administrative.events.UpdateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.UserInfo;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.privileges.domain.UserCPRole;

import edu.wustl.common.beans.SessionDataBean;

public class StorageContainerTestData {

	public static final String ACTIVITY_STATUS_CLOSED = "Disabled";

	public static final String CONTAINER_NAME = "container name";

	public static final String SITE = "site";

	public static final String COLLECTION_PROTOCOL = "collection protocol";

	public static final Object USER = "user";

	public static final Object BARCODE = "barcode";

	public static final String STORAGE_CONTAINER = "storage container";

	public static final String PATCH_CONTAINER = "patch container";

	public static final String ONE_DIMENSION_CAPACITY = "one dimension capacity";

	public static final String TWO_DIMENSION_CAPACITY = "two dimension capacity";

	public static final String SITE_CONTAINER = "site container";

	public static List<User> getUserList() {
		List<User> users = new ArrayList<User>();
		users.add(new User());
		users.add(new User());
		return users;
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

	private static Set<String> getCpNames() {
		Set<String> cpNames = new HashSet<String>();
		cpNames.add("My CP");
		cpNames.add("Cp1");
		return cpNames;
	}

	public static User getUser(Long id) {
		User user = new User();
		user.setId(id);
		user.setFirstName("firstName1");
		user.setLastName("lastName1");
		user.setLoginName("admin@admin.com");
		user.setEmailAddress("sci@sci.com");
		user.setPasswordToken("e5412f93-a1c5-4ede-b66d-b32302cd4018");
		user.setDepartment(new Department());
		user.setAddress(new Address());
		user.setUserSites(new HashSet<Site>());
		user.setUserCPRoles(new HashSet<UserCPRole>());
		return user;
	}

	public static Site getSite() {
		Site site = new Site();
		site.setName("My Site");
		site.setId(1l);
		return site;
	}

	public static CollectionProtocol getCp() {
		CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setTitle("Query CP");
		collectionProtocol.setId(1l);
		collectionProtocol.setShortTitle("qcp");
		return collectionProtocol;
	}

	public static CreateStorageContainerEvent getCreateStorageContainerEvent() {
		CreateStorageContainerEvent event = new CreateStorageContainerEvent(getStorageContainerDetails());
		return event;
	}

	public static CreateStorageContainerEvent getCreateStorageContainerEventWithName() {
		CreateStorageContainerEvent event = new CreateStorageContainerEvent(getStorageContainerDetails());
		event.getDetails().setName("Container1");
		return event;
	}

	public static CreateStorageContainerEvent getCreateStorageContainerEventForNullSite() {
		CreateStorageContainerEvent event = new CreateStorageContainerEvent(getStorageContainerDetails());
		event.getDetails().setParentContainerName(null);
		return event;
	}

	public static CreateStorageContainerEvent getCreateStorageContainerEventWithoutCpRestrict() {
		CreateStorageContainerEvent event = new CreateStorageContainerEvent(getStorageContainerDetails());
		event.getDetails().setHoldsCPTitles(new HashSet<String>());
		return event;
	}

	public static CreateStorageContainerEvent getCreateStorageContainerEventWithWrongOneDimensionLabel() {
		CreateStorageContainerEvent event = new CreateStorageContainerEvent(getStorageContainerDetails());
		event.getDetails().setOneDimentionLabelingScheme("Roman");
		return event;
	}

	public static CreateStorageContainerEvent getCreateStorageContainerEventWithWrongTwoDimensionLabel() {
		CreateStorageContainerEvent event = new CreateStorageContainerEvent(getStorageContainerDetails());
		event.getDetails().setTwoDimentionLabelingScheme("Alphbets");
		return event;
	}

	public static CreateStorageContainerEvent getCreateStorageContainerEventWithNullSiteNameAndParent() {
		CreateStorageContainerEvent event = getCreateStorageContainerEvent();
		event.getDetails().setSiteName(null);
		event.getDetails().setParentContainerName(null);
		return event;
	}

	public static StorageContainerDetails getStorageContainerDetails() {
		StorageContainerDetails details = new StorageContainerDetails();
		details.setActivityStatus("Active");
		details.setHoldsCPTitles(getCpNames());
		details.setBarcode("2-edpwesdadas-343");
		details.setOneDimensionCapacity(10);
		details.setTwoDimensionCapacity(10);
		details.setComments("Blah blah blah");
		details.setParentContainerName("Freezer");
		details.setSiteName("My Site");
		details.setTempratureInCentigrade(22.22);
		details.setCreatedBy(getUserInfo());
		details.setOneDimensionCapacity(20);
		details.setTwoDimensionCapacity(20);
		details.setOneDimentionLabelingScheme("Numbers");
		details.setTwoDimentionLabelingScheme("Numbers");
		return details;
	}

	private static UserInfo getUserInfo() {
		UserInfo userInfo = new UserInfo();
		userInfo.setLoginName("admin@admin.com");
		userInfo.setDomainName("catissue");
		return userInfo;
	}

	public static StorageContainer getStorageContainer(Long l) {
		StorageContainer container = new StorageContainer();
		container.setHoldsCPs(getCps());
		container.setBarcode("2-edpwesdadas-343");
		container.setOneDimensionCapacity(10);
		container.setTwoDimensionCapacity(10);
		container.setId(l);
		container.setName("Container1");
		container.setComments("Blah blah blah");
		container.setSite(getSite());
		container.setOneDimentionLabelingScheme("Numbers");
		container.setTwoDimentionLabelingScheme("Numbers");
		container.setTempratureInCentigrade(22.22);
		container.setOneDimensionCapacity(20);
		container.setTwoDimensionCapacity(20);
		container.setCreatedBy(getUser(1l));
		Long id = container.getId(); // for coverage
		return container;
	}

	public static StorageContainer getStorageContainerWithDiffSite(Long l) {
		StorageContainer container = getStorageContainer(1l);
		container.getSite().setName("abc");
		return container;
	}

	private static Set<CollectionProtocol> getCps() {
		Set<CollectionProtocol> cps = new HashSet<CollectionProtocol>();
		cps.add(getCp());
		return cps;
	}

	public static UpdateStorageContainerEvent getUpdateStorageContainerEvent() {
		UpdateStorageContainerEvent event = new UpdateStorageContainerEvent(getStorageContainerDetails(), 1l);
		event.getDetails().setName("Container1");
		return event;
	}

	public static UpdateStorageContainerEvent getUpdateStorageContainerEventWithNullBarcode() {
		UpdateStorageContainerEvent event = new UpdateStorageContainerEvent(getStorageContainerDetails(), 1l);
		event.getDetails().setName("Container1");
		event.getDetails().setBarcode(null);
		return event;
	}

	public static PatchStorageContainerEvent getPatchData() {
		PatchStorageContainerEvent event = new PatchStorageContainerEvent();
		event.setStorageContainerId(1l);
		StorageContainerPatchDetails details = new StorageContainerPatchDetails();
		try {
			BeanUtils.populate(details, getStorageContainerPatchAttributes());
		}
		catch (Exception e) {
			reportError(UserErrorCode.BAD_REQUEST, PATCH_CONTAINER);
		}
		details.setModifiedAttributes(new ArrayList<String>(getStorageContainerPatchAttributes().keySet()));
		event.setStorageContainerDetails(details);
		return event;
	}

	public static PatchStorageContainerEvent getPatchDataToSetParentContainer() {
		PatchStorageContainerEvent event = new PatchStorageContainerEvent();
		event.setStorageContainerId(1l);
		StorageContainerPatchDetails details = new StorageContainerPatchDetails();
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("parentContainerName", "Freezer");
		try {
			BeanUtils.populate(details, attributes);
		}
		catch (Exception e) {
			reportError(UserErrorCode.BAD_REQUEST, PATCH_CONTAINER);
		}
		details.setModifiedAttributes(new ArrayList<String>(attributes.keySet()));
		event.setStorageContainerDetails(details);
		return event;
	}

	public static PatchStorageContainerEvent nonPatchData() {
		PatchStorageContainerEvent event = new PatchStorageContainerEvent();
		event.setStorageContainerId(1l);
		StorageContainerPatchDetails details = new StorageContainerPatchDetails();
		event.setStorageContainerDetails(details);
		return event;
	}

	private static Map<String, Object> getStorageContainerPatchAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		//	attributes.put("name", "Container");
		attributes.put("barcode", "a-essdsds-222");
		attributes.put("siteName", "My Site");
		attributes.put("holdsCPTitles", getCpNames());
		attributes.put("activityStatus", Status.ACTIVITY_STATUS_DISABLED.getStatus());
		attributes.put("comments", "blah blah");
		attributes.put("tempratureInCentigrade", 22.22);
		attributes.put("parentContainerName", "Freezer");
		attributes.put("holdsSpecimenTypes", getSpecimenTypes());
		attributes.put("createdBy", getUserInfo());
		attributes.put("oneDimensionCapacity", 20);
		attributes.put("twoDimensionCapacity", 20);
		attributes.put("oneDimentionLabelingScheme", "Numbers");
		attributes.put("twoDimentionLabelingScheme", "Numbers");
		return attributes;
	}

	private static Set<String> getSpecimenTypes() {
		Set<String> spectTypes = new HashSet<String>();
		spectTypes.add("Tissue");
		spectTypes.add("Cell");
		return spectTypes;
	}

	public static DisableStorageContainerEvent getDisableStorageContainerEvent() {
		DisableStorageContainerEvent event = new DisableStorageContainerEvent();
		event.setId(1l);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static CreateStorageContainerEvent getCreateStorageContainerEventForOneDimentionCapacity() {
		CreateStorageContainerEvent event = getCreateStorageContainerEvent();
		event.getDetails().setOneDimensionCapacity(null);
		return event;
	}

	public static CreateStorageContainerEvent getCreateStorageContainerEventForTwoDimentionCapacity() {
		CreateStorageContainerEvent event = getCreateStorageContainerEvent();
		event.getDetails().setTwoDimensionCapacity(-1);
		return event;
	}

	public static UpdateStorageContainerEvent getUpdateStorageContainerEventWithChangeInName() {
		UpdateStorageContainerEvent event = getUpdateStorageContainerEvent();
		event.getDetails().setName("Dsda");
		event.getDetails().setBarcode("sda434-434");
		return event;
	}

	public static UpdateStorageContainerEvent getUpdateStorageContainerEventForTwoDimentionCapacity() {
		UpdateStorageContainerEvent event = getUpdateStorageContainerEvent();
		event.getDetails().setTwoDimensionCapacity(-1);
		return event;
	}

	public static CreateStorageContainerEvent getCreateStorageContainerEventWithEmptyName() {
		CreateStorageContainerEvent event = getCreateStorageContainerEvent();
		event.getDetails().setName("");
		return event;
	}

	public static CreateStorageContainerEvent getUpdateStorageContainerEventForNullTwoDimentionCapacity() {
		CreateStorageContainerEvent event = new CreateStorageContainerEvent(getStorageContainerDetails());
		event.getDetails().setTwoDimentionLabelingScheme(null);
		return event;
	}

	public static CreateStorageContainerEvent getUpdateStorageContainerEventForNullOneDimentionCapacity() {
		CreateStorageContainerEvent event = new CreateStorageContainerEvent(getStorageContainerDetails());
		event.getDetails().setOneDimentionLabelingScheme(null);
		return event;
	}

	public static CreateStorageContainerEvent getCreateStorageContainerEventForNullBarcode() {
		CreateStorageContainerEvent event = new CreateStorageContainerEvent(getStorageContainerDetails());
		event.getDetails().setBarcode(null);
		return event;
	}

	public static CreateStorageContainerEvent getCreateStorageContainerEventForNullBarcodeAndName() {
		CreateStorageContainerEvent event = new CreateStorageContainerEvent(getStorageContainerDetails());
		event.getDetails().setBarcode(null);
		event.getDetails().setName(null);
		return event;
	}

	public static StorageContainer getStorageContainerForDisable(long l) {
		StorageContainer storageContainer = getStorageContainer(l);
		storageContainer.setChildContainers(getChildContainers());
		return storageContainer;
	}

	private static Set<StorageContainer> getChildContainers() {
		Set<StorageContainer> childs = new HashSet<StorageContainer>();
		childs.add(new StorageContainer());
		return childs;
	}

	public static DisableStorageContainerEvent getDisableStorageContainerEventForName() {
		DisableStorageContainerEvent event = new DisableStorageContainerEvent();
		event.setName("Abc");
		return event;
	}
	
	public static ReqAllStorageContainersEvent getAllStorageContainerEvent() {
		ReqAllStorageContainersEvent event = new ReqAllStorageContainersEvent();
		event.setMaxResults(1000);
		return event;
	}

	public static List<StorageContainer> getStorageContainers() {
		List<StorageContainer> StorageContainers = new ArrayList<StorageContainer>();
		StorageContainers.add(getStorageContainer(1l));
		StorageContainers.add(getStorageContainer(2l));
		return StorageContainers;
	}

	public static GetStorageContainerEvent getStorageContainerEvent() {
		GetStorageContainerEvent event = new GetStorageContainerEvent();
		event.setId(1l);
		return event;
	}

	public static GetStorageContainerEvent getStorageContainerEventForName() {
		GetStorageContainerEvent event = new GetStorageContainerEvent();
		event.setName("Abc");
		return event;
	}
}
