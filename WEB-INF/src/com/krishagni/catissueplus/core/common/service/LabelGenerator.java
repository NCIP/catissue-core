package com.krishagni.catissueplus.core.common.service;

public interface LabelGenerator {
	public boolean isValidLabelTmpl(String labelTmpl);
	
	public String generateLabel(String labelTmpl, Object object);
}
