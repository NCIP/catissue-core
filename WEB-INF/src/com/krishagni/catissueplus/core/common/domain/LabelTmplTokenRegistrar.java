package com.krishagni.catissueplus.core.common.domain;

public interface LabelTmplTokenRegistrar {
	public void register(LabelTmplToken token);
	
	public LabelTmplToken getToken(String tokenName);
}
