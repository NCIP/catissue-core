package com.krishagni.catissueplus.core.common.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplTokenRegistrar;
import com.krishagni.catissueplus.core.common.service.LabelGenerator;

public class LabelGeneratorImpl implements LabelGenerator {
	private LabelTmplTokenRegistrar tokenRegistrar;
	
	private Pattern labelPattern = Pattern.compile("%(.+?)%"); 

	public LabelTmplTokenRegistrar getTokenRegistrar() {
		return tokenRegistrar;
	}

	public void setTokenRegistrar(LabelTmplTokenRegistrar tokenRegistrar) {
		this.tokenRegistrar = tokenRegistrar;
	}

	@Override
	public boolean isValidLabelTmpl(String labelTmpl) {
		boolean valid = true;
		
		Matcher matcher = labelPattern.matcher(labelTmpl);		
		while (matcher.find()) {
			LabelTmplToken token = tokenRegistrar.getToken(matcher.group(1));
			if (token == null) {
				valid = false;
				break;
			}			
		}
		
		return valid;
	}	
	
	@Override
	public String generateLabel(String labelTmpl, Object object) { 
		Matcher matcher = labelPattern.matcher(labelTmpl);
		
		StringBuilder result = new StringBuilder();
		int lastIdx = 0;
		
		while (matcher.find()) {			
			LabelTmplToken token = tokenRegistrar.getToken(matcher.group(1));
			String replacement = null;
			if (token != null) {
				replacement = token.getReplacement(object);
			}
			
			result.append(labelTmpl.substring(lastIdx, matcher.start()));
			if (replacement == null) {
				result.append(matcher.group(0));
			} else {
				result.append(replacement);
			}
			
			lastIdx = matcher.end();
		}
		
		result.append(labelTmpl.substring(lastIdx, labelTmpl.length()));
		return result.toString();
	}
}