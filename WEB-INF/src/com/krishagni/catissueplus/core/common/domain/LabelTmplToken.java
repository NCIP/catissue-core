package com.krishagni.catissueplus.core.common.domain;

public interface LabelTmplToken {
	public static final String EMPTY_VALUE = "##!EMPTY_VALUE!##";

	public String getName();

	public String getReplacement(Object object);

	public String getReplacement(Object object, String ... args);

	public int validate(Object object, String input, int startIdx, String ... args);
}
