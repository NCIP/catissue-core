package com.krishagni.catissueplus.core.common.domain;

public abstract class AbstractLabelTmplToken implements LabelTmplToken {
	public int validate(Object object, String input, int startIdx) {
		String label = getReplacement(object);
		
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
