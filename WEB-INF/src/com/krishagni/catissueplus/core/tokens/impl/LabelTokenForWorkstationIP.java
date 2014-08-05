
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.printer.printRule.domain.SpecimenPrintRule;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForWorkstationIP implements LabelToken<SpecimenPrintRule> {

	private static final String EMPTY_WORKSHOP_ID = "";

	@Override
	public String getTokenValue(SpecimenPrintRule printRule) {
		if (printRule.getWorkstationIP() != null) {
			return printRule.getWorkstationIP();
		}
		return EMPTY_WORKSHOP_ID;
	}

}
