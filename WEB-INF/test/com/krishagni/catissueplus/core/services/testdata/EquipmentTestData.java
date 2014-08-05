
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
import com.krishagni.catissueplus.core.administrative.domain.Equipment;
import com.krishagni.catissueplus.core.administrative.domain.Image;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CreateEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.EquipmentDetails;
import com.krishagni.catissueplus.core.administrative.events.EquipmentPatchDetails;
import com.krishagni.catissueplus.core.administrative.events.GetEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateEquipmentEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;

import edu.wustl.common.beans.SessionDataBean;

public class EquipmentTestData {

	private static final String PATCH_EQUIPMENT = "patch equipment";

	public static final Object EQUIPMENT_ID = "equipment id";

	public static final Object DEVICE_NAME = "device name";

	public static final Object DISPLAY_NAME = "display name";

	public static final Object SITE_NAME = "site name";

	public static Equipment getEquipment() {

		Equipment equipment = new Equipment();
		equipment.setDeviceName("DemoDevice");
		equipment.setDeviceSerialNumber("0003");
		equipment.setDisplayName("Device001");
		equipment.setEquipmentId("EID1003");
		equipment.setSoftwareVersion("2.0");
		equipment.setSite(getSite());
		equipment.setId(1L);
		equipment.setManufacturerName("Scanscope");
		return equipment;
	}

	public static Equipment getEquipmentWithImageCollection() {
		Equipment equipment = new Equipment();
		equipment.setDeviceName("DemoDevice");
		equipment.setDeviceSerialNumber("0003");
		equipment.setDisplayName("Device001");
		equipment.setEquipmentId("EID1003");
		equipment.setSoftwareVersion("2.0");
		equipment.setSite(getSite());
		equipment.setId(1L);
		equipment.setManufacturerName("Scanscope");
		equipment.setImages(getImages());
		return equipment;
	}

	private static Set<Image> getImages() {
		Set<Image> images = new HashSet<Image>();
		images.add(new Image());
		return images;
	}

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
		site.setCoordinatorCollection(new HashSet<User>());
		site.setScgCollection(new HashSet<SpecimenCollectionGroup>());
		site.setStorageContainerCollection(new HashSet<StorageContainer>());
		return site;
	}

	public static CreateEquipmentEvent getCreateEquipmentEvent() {
		CreateEquipmentEvent event = new CreateEquipmentEvent();
		EquipmentDetails details = getEquipmentDetails();
		event.setDetails(details);
		event.setSessionDataBean(getSessionDataBean());
		return event;
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

	private static EquipmentDetails getEquipmentDetails() {
		EquipmentDetails details = new EquipmentDetails();
		details.setDeviceName("DemoDevice1");
		details.setDeviceSerialNumber("0003");
		details.setDisplayName("Device002");
		details.setEquipmentId("EID100122");
		details.setSoftwareVersion("2.0");
		details.setSiteName("Site1234");
		details.setManufacturerName("Scanscope");
		details.setActivityStatus("Active");
		return details;
	}

	private static EquipmentPatchDetails getEquipmentPatchDetails() {
		EquipmentPatchDetails details = new EquipmentPatchDetails();
		details.setDeviceName("DemoDevice111");
		details.setDeviceSerialNumber("0003");
		details.setDisplayName("Device0112");
		details.setEquipmentId("EID1002");
		details.setSoftwareVersion("2.0");
		details.setSiteName("Site1234");
		details.setManufacturerName("Scanscope");
		return details;
	}

	//	private static EquipmentPatchDetails getEquipmentPatchDetails() {
	//		EquipmentPatchDetails details = new EquipmentPatchDetails();
	//		details.setDeviceName("DemoDevice");
	//		details.setDeviceSerialNumber("0003");
	//		details.setDisplayName("Device001");
	//		details.setEquipmentId("disply_name");
	//		details.setSoftwareVersion("2.0");
	//		details.setSiteName("Site123");
	//		details.setManufacturerName("Scanscope");
	//		return details;
	//	}

	public static UpdateEquipmentEvent getUpdateEquipmentEvent() {
		UpdateEquipmentEvent event = new UpdateEquipmentEvent();
		event.setDetails(getEquipmentDetails());
		event.setId(1L);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static CreateEquipmentEvent getCreateEquipmentEventWithEmptyDisplayName() {
		CreateEquipmentEvent event = new CreateEquipmentEvent();
		EquipmentDetails details = getEquipmentDetails();
		details.setDisplayName("");
		event.setDetails(details);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static CreateEquipmentEvent getCreateEquipmentEventWithEmptyDeviceName() {
		CreateEquipmentEvent event = new CreateEquipmentEvent();
		EquipmentDetails details = getEquipmentDetails();
		details.setDeviceName("");
		event.setDetails(details);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static CreateEquipmentEvent getCreateEquipmentEventWithEmptySiteName() {
		CreateEquipmentEvent event = new CreateEquipmentEvent();
		EquipmentDetails details = getEquipmentDetails();
		details.setSiteName("");
		event.setDetails(details);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static UpdateEquipmentEvent getUpdateEquipmentEventWithEmptyDisplayName() {
		UpdateEquipmentEvent event = new UpdateEquipmentEvent();
		event.setId(1L);
		EquipmentDetails details = getEquipmentDetails();
		details.setDisplayName("");
		event.setDetails(details);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static UpdateEquipmentEvent getUpdateEquipmentEventWithEmptyDeviceName() {
		UpdateEquipmentEvent event = new UpdateEquipmentEvent();
		event.setId(1L);
		EquipmentDetails details = getEquipmentDetails();
		details.setDeviceName("");
		event.setDetails(details);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static UpdateEquipmentEvent getUpdateEquipmentEventWithEmptySiteName() {
		UpdateEquipmentEvent event = new UpdateEquipmentEvent();
		event.setId(1L);
		EquipmentDetails details = getEquipmentDetails();
		details.setSiteName("");
		event.setDetails(details);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static PatchEquipmentEvent getPatchEquipmentEvent() {
		PatchEquipmentEvent reqEvent = new PatchEquipmentEvent();
		reqEvent.setId(1L);
		reqEvent.setSessionDataBean(getSessionDataBean());
		EquipmentPatchDetails details = new EquipmentPatchDetails();

		try {
			BeanUtils.populate(details, getEquipmentPatchAttributes());
		}
		catch (Exception e) {
			reportError(SiteErrorCode.BAD_REQUEST, PATCH_EQUIPMENT);
		}
		details.setModifiedAttributes(new ArrayList<String>(getEquipmentPatchAttributes().keySet()));
		reqEvent.setDetails(details);
		return reqEvent;
	}

	private static Map<String, Object> getEquipmentPatchAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("deviceName", "Device12");
		attributes.put("deviceSerialNumber", "0003");
		attributes.put("manufacturerName", "Scanscope");
		attributes.put("siteName", "Site1");
		attributes.put("displayName", "DeviceDemo1");
		attributes.put("softwareVersion", "4.0");
		attributes.put("equipmentId", "EID331");
		attributes.put("activityStatus", "Active");
		return attributes;
	}

	public static PatchEquipmentEvent getPatchEquipmentEventWithEmptyDisplayName() {
		PatchEquipmentEvent event = getPatchEquipmentEvent();
		event.setId(1L);
		event.getDetails().setDisplayName("");
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static PatchEquipmentEvent getPatchEquipmentEventWithEmptyDeviceName() {
		PatchEquipmentEvent event = getPatchEquipmentEvent();
		event.setId(1L);
		event.getDetails().setDeviceName("");
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static PatchEquipmentEvent getPatchEquipmentEventWithEmptySiteName() {
		PatchEquipmentEvent event = getPatchEquipmentEvent();
		event.setId(1L);
		event.getDetails().setSiteName("");
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static PatchEquipmentEvent getPatchEquipmentEventDisplayName() {
		PatchEquipmentEvent event = getEmptyPatchData();
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static PatchEquipmentEvent getEmptyPatchData() {
		PatchEquipmentEvent event = new PatchEquipmentEvent();

		EquipmentPatchDetails details = getEquipmentPatchDetails();
		event.setDetails(details);
		event.setId(1L);
		return event;
	}

	public static CreateEquipmentEvent getCreateEquipmentEventWithEmptyEquipmentId() {
		CreateEquipmentEvent event = new CreateEquipmentEvent();
		EquipmentDetails details = getEquipmentDetails();
		details.setEquipmentId("");
		event.setDetails(details);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static PatchEquipmentEvent getPatchEquipmentEventWithEmptyEquipmentId() {
		PatchEquipmentEvent event = getPatchEquipmentEvent();
		event.setId(1L);
		event.getDetails().setEquipmentId("");
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static UpdateEquipmentEvent getUpdateEquipmentEventWithEmptyEquipmentId() {
		UpdateEquipmentEvent event = new UpdateEquipmentEvent();
		event.setId(1L);
		EquipmentDetails details = getEquipmentDetails();
		details.setEquipmentId("");
		event.setDetails(details);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static PatchEquipmentEvent getPatchEquipmentEventWithDiffEqpId() {
		PatchEquipmentEvent event = getPatchEquipmentEvent();
		event.setId(1L);
		event.getDetails().setEquipmentId("EQPID0202");
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static DeleteEquipmentEvent getDeleteEquipmentEvent() {
		DeleteEquipmentEvent event = new DeleteEquipmentEvent();
		event.setId(1L);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static CreateEquipmentEvent getCreateEventWithInvalidActivityStatus() {
		CreateEquipmentEvent event = getCreateEquipmentEvent();
		event.getDetails().setActivityStatus("ANY");
		return event;
	}

	public static List<Equipment> getEquipments() {
		List<Equipment> equipments = new ArrayList<Equipment>();
		equipments.add(getEquipment());
		equipments.add(getEquipment());
		return equipments;
	}

	public static ReqAllEquipmentEvent getAllEquipmentsEvent() {
		ReqAllEquipmentEvent event = new ReqAllEquipmentEvent();
		event.setMaxResults(1000);
		return event;
	}

	public static GetEquipmentEvent getEquipmentEvent() {
		GetEquipmentEvent event = new GetEquipmentEvent();
		event.setId(1L);
		return event;
	}

	public static GetEquipmentEvent getEquipmentEventForName() {
		GetEquipmentEvent event = new GetEquipmentEvent();
		event.setDisplayName("disp");
		return event;
	}

	public static DeleteEquipmentEvent getDeleteEquipmentEventForName() {
		DeleteEquipmentEvent event = new DeleteEquipmentEvent();
		event.setDisplayName("displayName");
		return event;
	}

}
