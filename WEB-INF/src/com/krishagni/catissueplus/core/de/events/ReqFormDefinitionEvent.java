package com.krishagni.catissueplus.core.de.events;

import java.io.OutputStream;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqFormDefinitionEvent extends RequestEvent {

	private Long formId;
	
	private OutputStream out;

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}
	
	public OutputStream getOut() {
		return out;
	}

	public void setOut(OutputStream out) {
		this.out = out;
	}	
}
