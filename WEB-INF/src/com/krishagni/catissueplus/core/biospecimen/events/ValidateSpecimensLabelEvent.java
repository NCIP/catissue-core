package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ValidateSpecimensLabelEvent extends RequestEvent{
	
	private List<String> labels;

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}


}
