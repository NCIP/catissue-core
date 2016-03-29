package com.krishagni.catissueplus.rde.tokens.impl;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.rde.tokens.BarcodePart;
import com.krishagni.catissueplus.rde.tokens.BarcodeToken;

public class PpidBarcodeToken extends AbstractLabelTmplToken implements BarcodeToken {
	private static final Pattern digitsPtrn = Pattern.compile("%(\\d+)d");

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
				
		int numHyphens = StringUtils.countMatches(ppidFmt, "-");
		String[] parts = input.substring(startIdx).split("-", numHyphens + 1);
		parts[parts.length - 1] = parts[parts.length - 1].split("-", 2)[0];

		String ppid = StringUtils.join(parts, "-");
		result.setCode(ppid);
		result.setEndIdx(startIdx + ppid.length());
		
		Matcher matcher = digitsPtrn.matcher(ppidFmt);
		if (!matcher.find()) {
			if (ppidFmt.equals(ppid)) {
				result.setValue(ppid);
			}
			
			return result;
		}
		
		int matchStartIdx = ppidFmt.indexOf(matcher.group(0));
		String beforeDigits = ppidFmt.substring(0, matchStartIdx);
		String afterDigits = ppidFmt.substring(matchStartIdx + matcher.group(0).length());
		
		String regex = beforeDigits + "\\d{" + matcher.group(1) + "}" + afterDigits;
		if (Pattern.matches(regex, ppid)) {
			result.setValue(ppid);
			result.setDisplayValue(ppid);
		}
		
		return result;
	}
}
