
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateScgEvent extends RequestEvent {

	private Long id;

	private VisitDetail scgDetail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VisitDetail getScgDetail() {
		return scgDetail;
	}

	public void setScgDetail(VisitDetail scgDetail) {
		this.scgDetail = scgDetail;
	}

}
