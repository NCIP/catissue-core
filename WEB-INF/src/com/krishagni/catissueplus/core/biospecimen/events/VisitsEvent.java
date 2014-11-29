
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class VisitsEvent extends ResponseEvent {
	private List<VisitSummary> visits;

	public List<VisitSummary> getVisits() {
		return visits;
	}

	public void setVisits(List<VisitSummary> visits) {
		this.visits = visits;
	}

	public static VisitsEvent ok(List<VisitSummary> visits) {
		VisitsEvent resp = new VisitsEvent();
		resp.setStatus(EventStatus.OK);
		resp.setVisits(visits);		
		return resp;
	}

	public static VisitsEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		VisitsEvent resp = new VisitsEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}
