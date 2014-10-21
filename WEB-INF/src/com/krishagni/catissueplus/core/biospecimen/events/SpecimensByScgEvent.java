package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimensByScgEvent extends ResponseEvent {
	private List<SpecimenDetail> specimens = new ArrayList<SpecimenDetail>();

	public List<SpecimenDetail> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(List<SpecimenDetail> specimens) {
		this.specimens = specimens;
	}
	
	public static SpecimensByScgEvent ok(List<SpecimenDetail> specimens) {
		SpecimensByScgEvent event = new SpecimensByScgEvent();
		event.setSpecimens(specimens);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static SpecimensByScgEvent invalidRequest(String message, Long... id) {
		SpecimensByScgEvent resp = new SpecimensByScgEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static SpecimensByScgEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		SpecimensByScgEvent resp = new SpecimensByScgEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}
