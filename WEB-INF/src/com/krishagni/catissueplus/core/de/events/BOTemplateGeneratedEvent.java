package com.krishagni.catissueplus.core.de.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class BOTemplateGeneratedEvent extends ResponseEvent {
	
	List<Long> templateIds = new ArrayList<Long>();
	
	public static BOTemplateGeneratedEvent ok(List<Long> templateIds) {
		BOTemplateGeneratedEvent resp = new BOTemplateGeneratedEvent();
		resp.templateIds = templateIds;
		resp.setStatus(EventStatus.OK);
		return resp;
	}

	public static BOTemplateGeneratedEvent not_ok() {
		BOTemplateGeneratedEvent resp = new BOTemplateGeneratedEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

}
