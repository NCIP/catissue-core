package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimensInfoEvent extends ResponseEvent	 {

	private List<SpecimenInfo> info;

	public List<SpecimenInfo> getInfo() {
		return info;
	}

	public void setInfo(List<SpecimenInfo> info) {
		this.info = info;
	}
	
	public static SpecimensInfoEvent ok(List<SpecimenInfo> specimensList) {
		SpecimensInfoEvent event = new SpecimensInfoEvent();
		event.setInfo(specimensList);
		event.setStatus(EventStatus.OK);
		return event;
	}
	
	public static SpecimensInfoEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		SpecimensInfoEvent resp = new SpecimensInfoEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}
