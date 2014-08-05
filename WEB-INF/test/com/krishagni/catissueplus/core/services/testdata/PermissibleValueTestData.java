
package com.krishagni.catissueplus.core.services.testdata;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.events.AddPvEvent;
import com.krishagni.catissueplus.core.administrative.events.DeletePvEvent;
import com.krishagni.catissueplus.core.administrative.events.EditPvEvent;
import com.krishagni.catissueplus.core.administrative.events.GetAllPVsEvent;
import com.krishagni.catissueplus.core.administrative.events.PermissibleValueDetails;
import com.krishagni.catissueplus.core.administrative.events.ValidatePvEvent;
import com.krishagni.catissueplus.core.common.util.Status;

import java.util.Arrays;
import edu.wustl.common.beans.SessionDataBean;

public class PermissibleValueTestData {

	public static final String VALUE = "pv value";

	public static final String SUCCESS = "success";

	public static final Object CONCEPT_CODE = "concept code";
	
	public static final String ACTIVITY_STATUS = "activity status";

	public static final String SPECIMEN_CLASS = "specimen class";

	public static final String SPECIMEN_TYPE = "specimen type";
	

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

	public static PermissibleValue getPermissibleValue(long id) {
		PermissibleValue pv = new PermissibleValue();
		pv.setConceptCode("32344");
		pv.setAttribute("abc");
		pv.setValue("Tissue");
		return pv;
	}

	public static AddPvEvent getAddPvEvent() {
		AddPvEvent event = new AddPvEvent(getPvDetails());
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	private static PermissibleValueDetails getPvDetails() {
		PermissibleValueDetails details = new PermissibleValueDetails();
		details.setConceptCode("32344");
		details.setAttribute("dsad");
		details.setValue("Tissue");
		details.setParentId(1l);
		return details;
	}

	public static EditPvEvent getEditPvEvent() {
		EditPvEvent event = new EditPvEvent(getPvDetailsForChangedValue(), 1l);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	private static PermissibleValueDetails getPvDetailsForChangedValue() {
		PermissibleValueDetails details = getPvDetails();
		details.setValue("Cell");
		return details;
	}

	public static DeletePvEvent getDeletePvEvent() {
		DeletePvEvent event = new DeletePvEvent();
		event.setId(1l);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}

	public static AddPvEvent getAddPvEventForNullValue() {
		AddPvEvent event = getAddPvEvent();
		event.getDetails().setValue(null);
		return event;
	}

	public static AddPvEvent getAddPvEventForNullAttribute() {
		AddPvEvent event = getAddPvEvent();
		event.getDetails().setAttribute(null);
		return event;
	}

	public static GetAllPVsEvent getGetAllPVsEvent() {
		GetAllPVsEvent event = new GetAllPVsEvent();
		event.setAttribute("abc");
		event.setMaxResult(1000);
		event.setSearchString("f");
		return event;
	}

	public static List<PermissibleValue> getPermissibleValues() {
		List<PermissibleValue> pvs = new ArrayList<PermissibleValue>();
		pvs.add(getPermissibleValue(1l));
		pvs.add(getPermissibleValue(2l));
		pvs.add(getPermissibleValue(3l));
		pvs.add(getPermissibleValue(4l));
		return pvs;
	}

	public static AddPvEvent getAddPvEventWithNullParent() {
		AddPvEvent event = getAddPvEvent();
		event.getDetails().setParentId(null);
		return event;
	}

	public static List<String> getPvValues() {
			List<String> values = new ArrayList<String>();
			values.add(Status.ACTIVITY_STATUS_DISABLED.getStatus());
			values.add(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
			values.add("Not Specified");
			values.add("Collection Site");
			values.add("Repository");
			values.add("Laboratory");
			values.add("Infectious");
			values.add("Radioactive");
			values.add("Toxic");
			values.add("Carcinogen");
			values.add("Mutagen");
			values.add("India");
			values.add("Tissue");
			values.add("Any");
			values.add("Cell");
			values.add("Blue");
			return values;
	}

	public static ValidatePvEvent getValidatePvEvent() {
		ValidatePvEvent event = new ValidatePvEvent();
		event.setParentValue("Tissue");
		String value = "Eeds";
		event.setValues(Arrays.asList(value));
		event.setAttribute("XYZ");
		return event;
	}

	public static ValidatePvEvent getValidatePvEventWithoutParent() {
		ValidatePvEvent event = getValidatePvEvent();
		event.setParentValue(null);
		return event;
	}
	
	public static ValidatePvEvent getValidatePvEventForSuccess() {
		ValidatePvEvent event = getValidatePvEvent();
		event.setParentValue(null);
		event.setValues(Arrays.asList("Cell"));
		return event;
	}

}
