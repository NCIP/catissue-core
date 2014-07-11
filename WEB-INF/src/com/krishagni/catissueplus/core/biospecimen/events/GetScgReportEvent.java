
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class GetScgReportEvent extends RequestEvent {

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
