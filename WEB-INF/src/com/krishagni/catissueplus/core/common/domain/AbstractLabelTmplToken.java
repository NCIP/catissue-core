package com.krishagni.catissueplus.core.common.domain;

public abstract class AbstractLabelTmplToken implements LabelTmplToken {

	@Override
	public String getReplacement(Object object, String ... args) {
		return getReplacement(object);
	}

	@Override
	public int validate(Object object, String input, int startIdx, String ... args) {
		String label = getReplacement(object, args);
		
		int endIdx = startIdx + label.length();
		if (startIdx >= input.length() || endIdx > input.length()) {
			return startIdx;
		}
		
		if (input.substring(startIdx, endIdx).equals(label)) {
			return endIdx;
		}
		
		return startIdx;
	}
	
	protected int validateNumber(String input, int startIdx) {
		int endIdx = startIdx, len = input.length();		
		while (endIdx < len && Character.isDigit(input.charAt(endIdx))) {
			++endIdx;
		}
		
		return endIdx;
	}	
}
