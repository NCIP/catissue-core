
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimensSummaryEvent extends ResponseEvent {

	private List<SpecimenSummary> specimensSummary;
	
	private Long count;

	public List<SpecimenSummary> getSpecimensSummary() {
		return specimensSummary;
	}

	public void setSpecimensSummary(List<SpecimenSummary> specimensSummary) {
		this.specimensSummary = specimensSummary;
	}

	public Long getCount() {
		return count;
	}
	
	public void setCount(Long conut) {
		this.count = conut;
	}
	
	public static SpecimensSummaryEvent ok(List<SpecimenSummary> specimensSummary, Long count) {
		SpecimensSummaryEvent event = new SpecimensSummaryEvent();
		event.setSpecimensSummary(specimensSummary);
		event.setCount(count);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static SpecimensSummaryEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		SpecimensSummaryEvent resp = new SpecimensSummaryEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static SpecimensSummaryEvent badRequest(String msg, Throwable t) {
		SpecimensSummaryEvent resp = new SpecimensSummaryEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(msg);
		resp.setException(t);
		return resp;
	}

	

}
