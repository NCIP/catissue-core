
package com.krishagni.catissueplus.core.printer.printRule.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdatePrintRuleEvent extends RequestEvent {

	private Object printRuleDetails;

	private String printRuleName;

	private Long printRuleId;

	public UpdatePrintRuleEvent(Object printRuleDetails, String printRuleName) {
		this.printRuleDetails = printRuleDetails;
		setPrintRuleName(printRuleName);
	}

	public UpdatePrintRuleEvent(Object printRuleDetails, Long ruleId) {
		this.printRuleDetails = printRuleDetails;
		setPrintRuleId(ruleId);
	}

	public Object getPrintRuleDetails() {
		return printRuleDetails;
	}

	public void setPrintRuleDetails(Object printRuleDetails) {
		this.printRuleDetails = printRuleDetails;
	}

	public Long getPrintRuleId() {
		return printRuleId;
	}

	public void setPrintRuleId(Long printRuleId) {
		this.printRuleId = printRuleId;
	}

	public String getPrintRuleName() {
		return printRuleName;
	}

	public void setPrintRuleName(String printRuleName) {
		this.printRuleName = printRuleName;
	}

}
