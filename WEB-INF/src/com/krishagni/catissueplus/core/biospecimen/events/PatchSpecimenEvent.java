
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchSpecimenEvent extends RequestEvent {

	private Long id;

	private SpecimenPatchDetail detail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SpecimenPatchDetail getDetail() {
		return detail;
	}

	public void setDetail(SpecimenPatchDetail detail) {
		this.detail = detail;
	}

}
