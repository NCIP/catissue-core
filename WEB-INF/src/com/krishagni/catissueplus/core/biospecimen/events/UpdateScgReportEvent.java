
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateScgReportEvent extends RequestEvent {

	private Long id;

	private ScgReportDetail detail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ScgReportDetail getDetail() {
		return detail;
	}

	public void setDetail(ScgReportDetail detail) {
		this.detail = detail;
	}

}
