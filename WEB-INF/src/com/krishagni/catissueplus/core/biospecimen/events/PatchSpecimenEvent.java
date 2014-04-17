
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchSpecimenEvent extends RequestEvent {

	private Long id;

	private SpecimenDetail detail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SpecimenDetail getDetail() {
		return detail;
	}

	public void setDetail(SpecimenDetail detail) {
		this.detail = detail;
	}

}
