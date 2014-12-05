
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class AddVisitEvent extends RequestEvent {

	private VisitDetail visit;

	private Long cprId;

	public VisitDetail getVisit() {
		return visit;
	}

	public void setVisit(VisitDetail visit) {
		this.visit = visit;
	}

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}
}
