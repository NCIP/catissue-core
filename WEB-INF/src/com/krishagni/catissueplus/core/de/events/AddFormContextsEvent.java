package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class AddFormContextsEvent extends RequestEvent {
	private List<FormContextDetail> formContexts;  
	
	public List<FormContextDetail> getFormContexts() {
		return formContexts;
	}

	public void setFormContexts(List<FormContextDetail> formContexts) {
		this.formContexts = formContexts;
	}
}
