package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.HashMap;
import java.util.Map;

public class SpecimenDisposalEvent extends SpecimenEvent {
	private String reason;

	
	public SpecimenDisposalEvent(Specimen specimen) {
		super(specimen);
	}

	public String getReason() {
		loadRecordIfNotLoaded();
		return reason;
	}

	public void setReason(String reason) {
		loadRecordIfNotLoaded();
		this.reason = reason;;
	}

	@Override
	protected Map<String, Object> getEventAttrs() {
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("reason", reason);
		return attrs;
	}

	@Override
	protected void setEventAttrs(Map<String, Object> attrValues) {
		this.reason = (String)attrValues.get("reason");
	}

	@Override
	public String getFormName() {
		return "SpecimenDisposalEvent";
	}
}