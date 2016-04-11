package com.krishagni.openspecimen.rde.tokens.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.openspecimen.rde.tokens.BarcodePart;
import com.krishagni.openspecimen.rde.tokens.BarcodeToken;

public class EmptyBarcodeToken extends AbstractLabelTmplToken implements BarcodeToken {
	@Override
	public String getName() {
		return StringUtils.EMPTY;
	}

	@Override
	public String getReplacement(Object object) {
		return null;
	}

	@Override
	public BarcodePart parse(Map<String, Object> contextMap, String input, int startIdx, String... args) {
		return null;
	}
}
