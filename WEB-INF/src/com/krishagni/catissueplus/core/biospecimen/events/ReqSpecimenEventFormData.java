
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqSpecimenEventFormData extends RequestEvent {

	private Long formId;

	private List<String> specimenLabels;

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public List<String> getSpecimenLabels() {
		return specimenLabels;
	}

	public void setSpecimenLabels(List<String> specimenLabels) {
		this.specimenLabels = specimenLabels;
	}
}
