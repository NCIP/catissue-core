package com.krishagni.catissueplus.core.printer.printRule.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class GetAllPrintRulesEvent extends ResponseEvent {

	private List<SpecimenPrintRuleDetails> details;

	public List<SpecimenPrintRuleDetails> getDetails() {
		return details;
	}

	public void setDetails(List<SpecimenPrintRuleDetails> details) {
		this.details = details;
	}

	public static GetAllPrintRulesEvent ok(List<SpecimenPrintRuleDetails> details) {
		GetAllPrintRulesEvent event = new GetAllPrintRulesEvent();
		event.setStatus(EventStatus.OK);
		event.setDetails(details);
		return event;
	}

}
