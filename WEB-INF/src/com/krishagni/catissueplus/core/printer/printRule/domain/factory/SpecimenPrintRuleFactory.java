package com.krishagni.catissueplus.core.printer.printRule.domain.factory;

import com.krishagni.catissueplus.core.printer.printRule.domain.SpecimenPrintRule;
import com.krishagni.catissueplus.core.printer.printRule.events.SpecimenPrintRuleDetails;


public interface SpecimenPrintRuleFactory {

	public SpecimenPrintRule createSpecimenPrintRule(SpecimenPrintRuleDetails details);

	public SpecimenPrintRule patchSpecimenPrintRule(SpecimenPrintRule oldSpecimenPrintRule, SpecimenPrintRuleDetails details);

}
