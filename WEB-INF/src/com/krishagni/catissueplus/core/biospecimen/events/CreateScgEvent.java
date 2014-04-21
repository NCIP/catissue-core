
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateScgEvent extends RequestEvent {

	private ScgDetail scgDetail;

	private Long cprId;

	public ScgDetail getScgDetail() {
		return scgDetail;
	}

	public void setScgDetail(ScgDetail scgDetail) {
		this.scgDetail = scgDetail;
	}

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

}
