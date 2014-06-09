package com.krishagni.catissueplus.core.services.testdata;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.events.CreateInstituteEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetails;
import com.krishagni.catissueplus.core.administrative.events.UpdateInstituteEvent;


public class InstituteTestData {

	public static final Object INSTITUTE_NAME = "institute name";

	public static CreateInstituteEvent getCreateInstituteEvent() {
		CreateInstituteEvent event = new CreateInstituteEvent();
		InstituteDetails details = new InstituteDetails();
		details.setName("My Inst");
		event.setInstituteDetails(details);
		return event;
	}

	public static Institute getInstitute(long id) {
		Institute institute = new Institute();
		institute.setId(id);
		institute.setName("My Inst");
		return institute;
	}

	public static UpdateInstituteEvent getUpdateInstituteEvent() {
		UpdateInstituteEvent event = new UpdateInstituteEvent();
		InstituteDetails details = new InstituteDetails();
		details.setName("My Inst");
		event.setInstituteDetails(details);
		return event;
	}

	public static CreateInstituteEvent getCreateInstituteEventForEmptyName() {
		CreateInstituteEvent event = getCreateInstituteEvent();
		event.getInstituteDetails().setName(null);
		return event;
	}

	public static UpdateInstituteEvent getUpdateInstituteEventForEmptyName() {
		UpdateInstituteEvent event = getUpdateInstituteEvent();
		event.getInstituteDetails().setName(null);
		return event;
	}

}
