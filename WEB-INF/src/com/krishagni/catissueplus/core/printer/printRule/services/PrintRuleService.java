package com.krishagni.catissueplus.core.printer.printRule.services;

import com.krishagni.catissueplus.core.printer.printRule.events.CreatePrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.DeletePrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.GetAllPrintRulesEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.GetPrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PatchPrintRuleEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PrintRuleCreatedEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PrintRuleDeletedEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PrintRuleGotEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.PrintRuleUpdatedEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.ReqAllPrintRulesEvent;
import com.krishagni.catissueplus.core.printer.printRule.events.UpdatePrintRuleEvent;

public interface PrintRuleService {
	
	public PrintRuleCreatedEvent createPrintRule(CreatePrintRuleEvent event);

	public PrintRuleUpdatedEvent updatePrintRule(UpdatePrintRuleEvent event);

	public PrintRuleDeletedEvent deletePrintRule(DeletePrintRuleEvent event);
	
	public PrintRuleUpdatedEvent patchPrintRule(PatchPrintRuleEvent event);

	public GetAllPrintRulesEvent getPrintAllRules(ReqAllPrintRulesEvent event);

	public PrintRuleGotEvent getPrintRule(GetPrintRuleEvent event);
}
