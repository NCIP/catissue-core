package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Map;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimensLabelValidatedEvent extends ResponseEvent {
	
	private Map<String, Boolean> labelValidationMap;

	public Map<String, Boolean> getLabelValidationMap() {
		return labelValidationMap;
	}

	public void setLabelValidationMap(Map<String, Boolean> labelValidationMap) {
		this.labelValidationMap = labelValidationMap;
	}

	public static SpecimensLabelValidatedEvent ok(Map<String, Boolean> labelValidationMap){
		SpecimensLabelValidatedEvent resp = new SpecimensLabelValidatedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setLabelValidationMap(labelValidationMap);
		return resp;
	}

	public static SpecimensLabelValidatedEvent serverError(Exception ex) {
		SpecimensLabelValidatedEvent resp = new SpecimensLabelValidatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(ex);
		resp.setMessage(ex.getMessage());
		return resp;
	}
}
