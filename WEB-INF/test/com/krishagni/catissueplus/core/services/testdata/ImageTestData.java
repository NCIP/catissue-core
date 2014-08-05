
package com.krishagni.catissueplus.core.services.testdata;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.krishagni.catissueplus.core.administrative.domain.Equipment;
import com.krishagni.catissueplus.core.administrative.domain.Image;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CreateImageEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteImageEvent;
import com.krishagni.catissueplus.core.administrative.events.GetImageEvent;
import com.krishagni.catissueplus.core.administrative.events.ImageDetails;
import com.krishagni.catissueplus.core.administrative.events.ImagePatchDetails;
import com.krishagni.catissueplus.core.administrative.events.PatchImageEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateImageEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.ExternalIdentifier;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

import edu.wustl.common.beans.SessionDataBean;

public class ImageTestData {

	public static final String PATCH_IMAGE = "patch image";

	public static final String SPECIMEN_ID = "specimen id";

	public static final String URL = "full loc url";

	public static final String EQUIPMENT_ID = "equipment id";

	public static final String EQP_IMG_ID = "equipment image id";

	public static final String ACTIVITY_STATUS = "activity status";

	public static final String STAIN_NAME = "stain name";

	public static Image getImage() {
		Image image = new Image();
		image.setId(1L);
		image.setActivityStatus("Active");
		image.setDescription("");
		image.setEqpImageId("IMG0010");
		image.setEquipment(getEquipment());
		image.setFullLocUrl("");
		image.setImageType("");
		image.setLastUpdateDate(new Date());
		image.setQuality(11L);
		image.setResolution("");
		image.setScanDate(new Date());
		image.setSpecimen(getSpecimen());
		image.setStainName("Blue");
		image.setHeight(120L);
		return image;
	}

	public static Image getImageWithNullDates() {
		Image image = getImage();
		ImageDetails details = getNullDateDetails();
		details.setLastUpdateDate(null);
		details.setScanDate(null);
		return image;
	}

	private static ImageDetails getNullDateDetails() {
		ImageDetails details = new ImageDetails();
		details.setActivityStatus("Active");
		details.setDescription("Somthing");
		details.setEqpImageId("IMG001");
		details.setEquipmentId(1L);
		details.setFullLocUrl("www.url.com");
		details.setImageType("");
		details.setLastUpdateDate(null);
		details.setQuality(11L);
		details.setResolution("");
		details.setScanDate(null);
		details.setSpecimenId(1L);
		details.setStainName("Blue");
		details.setHeight(100L);
		return details;
	}

	public static Specimen getSpecimen() {
		Specimen specimen = new Specimen();
		specimen.setId(1L);
		specimen.setLabel("new label");
		specimen.setBarcode("new barcode");
		specimen.setAvailableQuantity((double) 10);
		specimen.setInitialQuantity((double) 10);

		ExternalIdentifier externalIdentifier = new ExternalIdentifier();
		externalIdentifier.setId(1L);
		externalIdentifier.setName("new External Id");
		externalIdentifier.setValue("1");
		externalIdentifier.setSpecimen(specimen);
		HashSet<ExternalIdentifier> externalIdentifierCollection = new HashSet<ExternalIdentifier>();
		externalIdentifierCollection.add(externalIdentifier);
		specimen.setExternalIdentifierCollection(externalIdentifierCollection);
		return specimen;
	}

	public static Equipment getEquipment() {
		Equipment equipment = new Equipment();
		equipment.setId(1L);
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

	private static Site getSite() {
		Site site = new Site();
		return site;
	}

	public static CreateImageEvent getCreateImageEvent() {
		CreateImageEvent event = new CreateImageEvent();
		event.setDetails(getDetails());
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static CreateImageEvent getCreateImageEventWithEmptyEqpImageId() {
		CreateImageEvent event = new CreateImageEvent();
		ImageDetails details = getDetails();
		details.setEqpImageId("");
		event.setDetails(details);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static CreateImageEvent getCreateImageEventWithEmptyURL() {
		CreateImageEvent event = new CreateImageEvent();
		ImageDetails details = getDetails();
		details.setFullLocUrl("");
		event.setDetails(details);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static CreateImageEvent getCreateImageEventWithNullDates() {
		CreateImageEvent event = new CreateImageEvent();
		ImageDetails details = getNullDateDetails();
		event.setDetails(details);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static UpdateImageEvent getUpdateImageEventWithEmptyURL() {
		UpdateImageEvent event = new UpdateImageEvent();
		ImageDetails details = getDetails();
		details.setFullLocUrl("");
		event.setDetails(details);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static PatchImageEvent getPatchImageEventWithEmptyURL() {
		PatchImageEvent event = new PatchImageEvent();
		ImagePatchDetails details = getPatchDetails();
		details.setFullLocUrl("");
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

	private static ImageDetails getDetails() {
		ImageDetails details = new ImageDetails();
		details.setActivityStatus("Active");
		details.setDescription("Somthing");
		details.setEqpImageId("IMG001");
		details.setEquipmentId(1L);
		details.setFullLocUrl("www.url.com");
		details.setImageType("");
		details.setLastUpdateDate(new Date());
		details.setQuality(11L);
		details.setResolution("");
		details.setScanDate(new Date());
		details.setSpecimenId(1L);
		details.setStainName("Blue");
		details.setHeight(100L);
		return details;
	}

	private static ImagePatchDetails getPatchDetails() {
		ImagePatchDetails details = new ImagePatchDetails();
		details.setActivityStatus("Active");
		details.setDescription("Somthing");
		details.setEqpImageId("IMG001");
		details.setEquipmentId(1L);
		details.setFullLocUrl("www.url.com");
		details.setImageType("");
		details.setLastUpdateDate(new Date());
		details.setQuality(11L);
		details.setResolution("");
		details.setScanDate(new Date());
		details.setSpecimenId(1L);
		details.setStainName("Blue");
		details.setHeight(100L);
		return details;
	}

	public static UpdateImageEvent getUpdateImageEvent() {
		UpdateImageEvent event = new UpdateImageEvent();
		event.setDetails(getDetails());
		event.setId(1L);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static PatchImageEvent getPatchData() {
		PatchImageEvent event = new PatchImageEvent();
		event.setSessionDataBean(getSessionDataBean());
		event.setId(1L);
		ImagePatchDetails details = new ImagePatchDetails();
		try {
			BeanUtils.populate(details, getImagePatchAttributes());
		}
		catch (Exception e) {
			reportError(SiteErrorCode.BAD_REQUEST, PATCH_IMAGE);
		}
		details.setModifiedAttributes(new ArrayList<String>(getImagePatchAttributes().keySet()));
		event.setDetails(details);
		return event;
	}

	private static Map<String, Object> getImagePatchAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("description", "description");
		attributes.put("eqpImageId", "eqpImageId");
		attributes.put("lastUpdateDate", new Date());
		attributes.put("quality", "admin@admin.com");
		attributes.put("resolution", "resolution");
		attributes.put("scanDate", new Date());
		attributes.put("stainName", "Blue");
		attributes.put("status", "status");
		attributes.put("width", "400");
		attributes.put("height", "400");
		attributes.put("imageType", "imageType");
		attributes.put("equipmentId", "equipmentId");
		attributes.put("specimenId", "specimenId");
		attributes.put("fullLocUrl", "fullLocUrl");
		attributes.put("activityStatus", "Active");
		return attributes;
	}

	public static DeleteImageEvent getDeleteImageEvent() {
		DeleteImageEvent event = new DeleteImageEvent();
		event.setId(1L);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static PatchImageEvent getEmptyPatchData() {
		PatchImageEvent event = new PatchImageEvent();
		ImagePatchDetails details = new ImagePatchDetails();
		event.setDetails(details);
		return event;
	}

	public static GetImageEvent getImageEvent() {
		GetImageEvent event = new GetImageEvent();
		event.setId(1L);
		return event;
	}

	public static GetImageEvent getImageEventForName() {
		GetImageEvent event = new GetImageEvent();
		event.setEqpImageId("eqpImageId");
		return event;
	}

	public static DeleteImageEvent getDeleteImageEventForName() {
		DeleteImageEvent event = new DeleteImageEvent();
		event.setEqpImageId("In Transit");
		return event;
	}

}
