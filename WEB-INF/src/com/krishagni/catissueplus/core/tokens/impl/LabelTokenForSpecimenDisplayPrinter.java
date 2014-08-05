
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.printer.printRule.domain.SpecimenPrintRule;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForSpecimenDisplayPrinter implements LabelToken<SpecimenPrintRule> {

	private static String PRINTER_NAME = "";

	@Override
	public String getTokenValue(SpecimenPrintRule printRule) {
		if (printRule.getPrinterName() != null) {
			return printRule.getPrinterName();
		}
		return PRINTER_NAME;
	}
}
