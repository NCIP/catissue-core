
package com.krishagni.catissueplus.events.specimens;

import java.util.List;

import com.krishagni.catissueplus.events.EventStatus;
import com.krishagni.catissueplus.events.ResponseEvent;

public class AllSpecimensSummaryEvent extends ResponseEvent {

	private List<SpecimenInfo> specimensInfo;

	public List<SpecimenInfo> getSpecimensInfo() {
		return specimensInfo;
	}

	public void setSpecimensInfo(List<SpecimenInfo> specimensInfo) {
		this.specimensInfo = specimensInfo;
	}

	public static AllSpecimensSummaryEvent ok(List<SpecimenInfo> specimensInfo) {
		AllSpecimensSummaryEvent event = new AllSpecimensSummaryEvent();
		event.setSpecimensInfo(specimensInfo);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static AllSpecimensSummaryEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		AllSpecimensSummaryEvent resp = new AllSpecimensSummaryEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}
