package com.krishagni.openspecimen.rde.tokens.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.openspecimen.rde.services.BarcodeTokenRegistrar;
import com.krishagni.openspecimen.rde.tokens.BarcodePart;
import com.krishagni.openspecimen.rde.tokens.BarcodeToken;

public class PpidBarcodeToken extends AbstractLabelTmplToken implements BarcodeToken, BarcodeTokenRegistrar {
	private Map<String, BarcodeToken> ppidTokens = new HashMap<>();

	private Pattern fnTokenPattern = Pattern.compile("(.+?)\\((.+?)\\)");

	public void setTokens(List<BarcodeToken> tokens) {
		ppidTokens.clear();

		for (BarcodeToken token : tokens) {
			ppidTokens.put(token.getName(), token);
		}
	}

	@Override
	public void register(BarcodeToken token) {
		ppidTokens.put(token.getName(), token);
	}

	@Override
	public BarcodeToken getToken(String name) {
		return ppidTokens.get(name);
	}

	@Override
	public String getName() {		
		return "PPI";
	}

	@Override
	public String getReplacement(Object object) {
		if (object instanceof Visit) {
			return ((Visit)object).getRegistration().getPpid();
		} else if (object instanceof Specimen) {
			return ((Specimen)object).getRegistration().getPpid();
		}
		
		throw new RuntimeException("Unknown object type: " + object != null ? object.getClass().getName() : "null");
	}

	@Override
	public BarcodePart parse(Map<String, Object> contextMap, String input, int startIdx, String ... args) {
		int endIdx = startIdx;
		
		BarcodePart result = new BarcodePart();
		result.setToken(getName());
		result.setStartIdx(startIdx);
		result.setEndIdx(endIdx);
				
		CollectionProtocol cp = (CollectionProtocol)contextMap.get("cp");
		String ppidFmt = cp.getPpidFormat();		
		if (StringUtils.isBlank(ppidFmt)) {
			return result;
		}

		String[] tokenParts = ppidFmt.split("-");
		String[] valueParts = input.substring(startIdx).split("-", tokenParts.length);
		valueParts[valueParts.length - 1] = valueParts[valueParts.length - 1].split("-", 2)[0];

		String ppid = StringUtils.join(valueParts, "-");
		result.setCode(ppid);
		result.setEndIdx(startIdx + ppid.length());

		if (tokenParts.length != valueParts.length) {
			return result;
		}

		boolean error = false;
		for (int i = 0; i < tokenParts.length; ++i) {
			String tokenName = null;
			String[] tokenArgs = null;

			if (tokenParts[i].length() > 2 && tokenParts[i].startsWith("%") && tokenParts[i].endsWith("%")) {
				tokenName = tokenParts[i].substring(1, tokenParts[i].length() - 1);
				Matcher fnMatcher = fnTokenPattern.matcher(tokenName);
				if (fnMatcher.matches()) {
					tokenName = fnMatcher.group(1);
					tokenArgs = fnMatcher.group(2).split(",");
				}
			} else {
				tokenName = tokenParts[i];
			}

			BarcodeToken token = getToken(tokenName);
			if (token == null) {
				if (!tokenParts[i].equals(valueParts[i])) {
					error = true;
					break;
				}
			} else {
				BarcodePart part = token.parse(contextMap, valueParts[i], 0, tokenArgs);
				if (part.getValue() == null) {
					error = true;
					break;
				}
			}
		}

		if (!error) {
			result.setValue(ppid);
			result.setDisplayValue(ppid);
		}

		return result;
	}
}