
package com.krishagni.catissueplus.core.printer.printRule.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchPrintRuleEvent extends RequestEvent {

	private String printRuleName;

	private Object printRuleDetails;

	private Long printRuleId;

	public String getPrintRuleName() {
		return printRuleName;
	}

	public void setPrintRuleName(String printRuleName) {
		this.printRuleName = printRuleName;
	}

	public Object getPrintRuleDetails() {
		return printRuleDetails;
	}

	public Long getPrintRuleId() {
		return printRuleId;
	}

	public void setPrintRuleId(Long printRuleId) {
		this.printRuleId = printRuleId;
	}

	public void setPrintRuleDetails(Object printRuleDetails) {
		this.printRuleDetails = printRuleDetails;
	}

}
