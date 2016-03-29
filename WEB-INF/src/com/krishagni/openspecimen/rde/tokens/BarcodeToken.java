package com.krishagni.openspecimen.rde.tokens;

import java.util.Map;

import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public interface BarcodeToken extends LabelTmplToken {
	public BarcodePart parse(Map<String, Object> contextMap, String input, int startIdx, String... args);
}
