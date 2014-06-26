
package com.krishagni.catissueplus.core.printer.printRule.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class DeletePrintRuleEvent extends RequestEvent {

	private String printRuleName;

	private Long printRuleId;

	private Object printRuleDetails;

	public String getPrintRuleName() {
		return printRuleName;
	}

	public void setPrintRuleName(String printRuleName) {
		this.printRuleName = printRuleName;
	}

	public Long getPrintRuleId() {
		return printRuleId;
	}

	public void setPrintRuleId(Long printRuleId) {
		this.printRuleId = printRuleId;
	}

	public Object getPrintRuleDetails() {
		return printRuleDetails;
	}

	public void setPrintRuleDetails(Object printRuleDetails) {
		this.printRuleDetails = printRuleDetails;
	}

}
