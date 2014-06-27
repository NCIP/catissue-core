
package com.krishagni.catissueplus.core.services.testdata;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.krishagni.catissueplus.core.administrative.domain.Biohazard;
import com.krishagni.catissueplus.core.administrative.domain.factory.BiohazardErrorCode;
import com.krishagni.catissueplus.core.administrative.events.BiohazardDetails;
import com.krishagni.catissueplus.core.administrative.events.CreateBiohazardEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchBiohazardEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateBiohazardEvent;

import edu.wustl.common.beans.SessionDataBean;

public class BiohazardTestData {

	public static final String BIOHAZARD_NAME = "biohazard name";

	public static final String BIOHAZARD_TYPE = "biohazard type";

	private static final String PATCH_BIOHAZARD = null;

	public static Biohazard getBiohazard() {
		Biohazard biohazard = new Biohazard();
		biohazard.setId(4l);
		biohazard.setName("Cr-555");
		biohazard.setComment("zsasasa");
		biohazard.setType("Not Specified");
		biohazard.setActivityStatus("Active");
		return biohazard;
	}

	public static CreateBiohazardEvent setCreateBiohazardEvent() {
		CreateBiohazardEvent biohazardEvent = new CreateBiohazardEvent();
		biohazardEvent.setSessionDataBean(getSessionDataBean());
		BiohazardDetails biohazardDetails = getDetails();
		biohazardEvent.setBiohazardDetails(biohazardDetails);
		return biohazardEvent;
	}

	private static BiohazardDetails getDetails() {
		BiohazardDetails details = new BiohazardDetails();
		details.setName("Cr-555");
		details.setType("Not Specified");
		details.setComment("Something");
		details.setActivityStatus("Active");
		return details;
	}

	private static BiohazardDetails getDetailsForUpdate() {
		BiohazardDetails details = new BiohazardDetails();
		details.setName("Cr-555");
		details.setType("Not Specified");
		details.setComment("Something");
		details.setActivityStatus("Active");
		return details;
	}

	public static CreateBiohazardEvent getCreateBiohazardEventWithEmptyBiohazardName() {
		CreateBiohazardEvent biohazardEvent = new CreateBiohazardEvent();
		biohazardEvent.setSessionDataBean(getSessionDataBean());
		BiohazardDetails biohazardDetails = getDetails();
		biohazardDetails.setName("");
		biohazardEvent.setBiohazardDetails(biohazardDetails);
		return biohazardEvent;
	}

	public static CreateBiohazardEvent getCreateEventWithEmptyBiohazardType() {
		CreateBiohazardEvent biohazardEvent = new CreateBiohazardEvent();
		biohazardEvent.setSessionDataBean(getSessionDataBean());
		BiohazardDetails biohazardDetails = getDetails();
		biohazardDetails.setType("");
		biohazardEvent.setBiohazardDetails(biohazardDetails);
		return biohazardEvent;
	}

	public static CreateBiohazardEvent getCreateEventWithInvalidBiohazardType() {
		CreateBiohazardEvent biohazardEvent = new CreateBiohazardEvent();
		biohazardEvent.setSessionDataBean(getSessionDataBean());
		BiohazardDetails biohazardDetails = getDetails();
		biohazardDetails.setType("abc");
		biohazardEvent.setBiohazardDetails(biohazardDetails);
		return biohazardEvent;
	}

	public static CreateBiohazardEvent getCreateEventWithInvalidActivityStatus() {
		CreateBiohazardEvent biohazardEvent = new CreateBiohazardEvent();
		biohazardEvent.setSessionDataBean(getSessionDataBean());
		BiohazardDetails biohazardDetails = getDetails();
		biohazardDetails.setActivityStatus("in-Active");
		biohazardEvent.setBiohazardDetails(biohazardDetails);
		return biohazardEvent;
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

	public static UpdateBiohazardEvent getUpdateBiohazardEvent() {
		BiohazardDetails details = getDetailsForUpdate();
		UpdateBiohazardEvent reqEvent = new UpdateBiohazardEvent();
		reqEvent.setId(4L);
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setBiohazardDetails(details);
		return reqEvent;
	}

	public static UpdateBiohazardEvent getUpdateBiohazardEventWithEmptyName() {
		UpdateBiohazardEvent reqEvent = getUpdateBiohazardEvent();
		reqEvent.setId(4L);
		reqEvent.getBiohazardDetails().setName("");
		return reqEvent;
	}

	public static UpdateBiohazardEvent getUpdateBiohazardEventWithEmptyType() {
		UpdateBiohazardEvent reqEvent = getUpdateBiohazardEvent();
		reqEvent.setId(4L);
		reqEvent.getBiohazardDetails().setType("");
		return reqEvent;
	}

	public static UpdateBiohazardEvent getUpdateEventWithInvalidBiohazardType() {
		UpdateBiohazardEvent biohazardEvent = getUpdateBiohazardEvent();
		biohazardEvent.getBiohazardDetails().setType("abc");
		return biohazardEvent;
	}

	public static UpdateBiohazardEvent getUpdateBiohazardEventWithDuplicateName() {
		UpdateBiohazardEvent biohazardEvent = getUpdateBiohazardEvent();
		biohazardEvent.setId(4L);
		biohazardEvent.getBiohazardDetails().setName("Cr-51");
		return biohazardEvent;
	}

	public static PatchBiohazardEvent getPatchData() {
		PatchBiohazardEvent event = new PatchBiohazardEvent();
		event.setId(5L);
		BiohazardDetails details = new BiohazardDetails();
		try {
			BeanUtils.populate(details, getBiohazardPatchAttributes());
		}
		catch (Exception e) {
			reportError(BiohazardErrorCode.BAD_REQUEST, PATCH_BIOHAZARD);
		}
		details.setModifiedAttributes(new ArrayList<String>(getBiohazardPatchAttributes().keySet()));
		event.setDetails(details);
		return event;
	}

	private static Map<String, Object> getBiohazardPatchAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("name", "pune");
		attributes.put("type", "Collection Site");
		attributes.put("comment", "Something");
		attributes.put("activityStatus", "Active");
		return attributes;
	}

	public static PatchBiohazardEvent getPatchDataWithInavalidAttribute() {
		PatchBiohazardEvent event = getEmptyPatchData();
		event.setId(6L);
		event.getDetails().setType("");
		event.getDetails().getModifiedAttributes().add("type");
		return event;
	}

	private static PatchBiohazardEvent getEmptyPatchData() {
		PatchBiohazardEvent event = new PatchBiohazardEvent();
		BiohazardDetails details = new BiohazardDetails();
		event.setDetails(details);
		return event;
	}

	public static PatchBiohazardEvent getPatchDataWithEmptyName() {
		PatchBiohazardEvent event = getEmptyPatchData();
		event.setId(6L);
		event.getDetails().setName("");
		event.getDetails().getModifiedAttributes().add("name");
		return event;
	}

	public static PatchBiohazardEvent getPatchDataWithEmptyType() {
		PatchBiohazardEvent event = getEmptyPatchData();
		event.setId(6L);
		event.getDetails().setType("");
		event.getDetails().getModifiedAttributes().add("type");
		return event;
	}

	public static PatchBiohazardEvent getPatchEventWithInvalidBiohazardType() {
		PatchBiohazardEvent event = getEmptyPatchData();
		event.setId(6L);
		event.getDetails().setType("abc");
		event.getDetails().getModifiedAttributes().add("type");
		return event;
	}

	public static UpdateBiohazardEvent getUpdateBiohazardEventWithName() {
		BiohazardDetails details = getDetailsForUpdate();
		UpdateBiohazardEvent reqEvent = new UpdateBiohazardEvent();
		reqEvent.setName(details.getName());
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setBiohazardDetails(details);
		return reqEvent;
	}

	public static PatchBiohazardEvent getPatchDataWithName() {
		PatchBiohazardEvent event = new PatchBiohazardEvent();
		event.setName("Cr-61");
		BiohazardDetails details = new BiohazardDetails();
		try {
			BeanUtils.populate(details, getBiohazardPatchAttributes());
		}
		catch (Exception e) {
			reportError(BiohazardErrorCode.BAD_REQUEST, PATCH_BIOHAZARD);
		}
		details.setModifiedAttributes(new ArrayList<String>(getBiohazardPatchAttributes().keySet()));
		event.setDetails(details);
		return event;
	}

}
