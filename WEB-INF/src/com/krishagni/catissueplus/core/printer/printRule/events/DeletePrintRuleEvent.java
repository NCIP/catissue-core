
package com.krishagni.catissueplus.core.printer.printRule.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class DeletePrintRuleEvent extends RequestEvent {

	private String printRuleName;

	private Long printRuleId;

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

}
