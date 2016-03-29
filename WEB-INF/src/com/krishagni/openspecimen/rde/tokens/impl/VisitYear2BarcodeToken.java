package com.krishagni.openspecimen.rde.tokens.impl;

import java.util.Map;

import com.krishagni.openspecimen.rde.tokens.BarcodePart;

public class VisitYear2BarcodeToken extends VisitYearBarcodeToken {

	@Override
	public String getName() {
		return "YR_OF_VISIT2";
	}

	@Override
	public String getReplacement(Object object) {
		return String.valueOf(getYearOfVisit(object) % 100);
	}

	@Override
	public BarcodePart parse(Map<String, Object> contextMap, String input, int startIdx, String... args) {
		return parseYear(input, startIdx, 2);
	}
}
