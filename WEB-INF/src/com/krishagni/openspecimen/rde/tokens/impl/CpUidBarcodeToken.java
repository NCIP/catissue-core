package com.krishagni.openspecimen.rde.tokens.impl;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.openspecimen.rde.tokens.BarcodePart;
import com.krishagni.openspecimen.rde.tokens.BarcodeToken;

public class CpUidBarcodeToken extends EmptyBarcodeToken {
	@Override
	public String getName() {
		return "CP_UID";
	}

	@Override
	public BarcodePart parse(Map<String, Object> contextMap, String input, int startIdx, String... args) {
		String[] parts = input.substring(startIdx).split("-", 2);
		String uid = parts[0];

		BarcodePart result = new BarcodePart();
		result.setToken(getName());
		result.setStartIdx(startIdx);
		result.setEndIdx(startIdx + uid.length());
		result.setCode(uid);

		String regex = "\\d+";
		if (args != null && args.length > 0) {
			int digit = Integer.parseInt(args[0]);
			regex = digit == 0 ? regex : "\\d{" + digit + "}";
		}

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(uid);
		if (matcher.find() && matcher.start() == 0 && matcher.end() == uid.length()) {
			result.setValue(uid);
			result.setDisplayValue(uid);
		}

		return result;
	}
}