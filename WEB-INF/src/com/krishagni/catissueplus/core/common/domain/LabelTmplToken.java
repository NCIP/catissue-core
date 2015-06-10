package com.krishagni.catissueplus.core.common.domain;

public interface LabelTmplToken {
	public String getName();
	
	public String getReplacement(Object object);
	
	public int validate(Object object, String input, int startIdx);
}
