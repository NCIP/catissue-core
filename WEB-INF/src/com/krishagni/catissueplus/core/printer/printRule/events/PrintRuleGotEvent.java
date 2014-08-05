package com.krishagni.catissueplus.core.printer.printRule.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class PrintRuleGotEvent extends ResponseEvent {

	private Long id;

	private String name;

	private SpecimenPrintRuleDetails details;

	public SpecimenPrintRuleDetails getDetails() {
		return details;
	}

	public void setDetails(SpecimenPrintRuleDetails details) {
		this.details = details;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static PrintRuleGotEvent ok(SpecimenPrintRuleDetails details) {
		PrintRuleGotEvent event = new PrintRuleGotEvent();
		event.setDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static PrintRuleGotEvent notFound(Long id) {
		PrintRuleGotEvent resp = new PrintRuleGotEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setId(id);
		return resp;
	}

	public static PrintRuleGotEvent notFound(String name) {
		PrintRuleGotEvent resp = new PrintRuleGotEvent();
		resp.setName(name);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
	
	public static PrintRuleGotEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		PrintRuleGotEvent resp = new PrintRuleGotEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}
