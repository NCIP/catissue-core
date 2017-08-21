package com.krishagni.catissueplus.core.common.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultLabelTmplTokenRegistrar implements LabelTmplTokenRegistrar {
	private Map<String, LabelTmplToken> tokenMap = new HashMap<String, LabelTmplToken>();

	@Override
	public void register(LabelTmplToken token) {
		tokenMap.put(token.getName(), token);
	}

	@Override
	public LabelTmplToken getToken(String tokenName) {
		return tokenMap.get(tokenName);
	}
	
	public void setTokens(List<LabelTmplToken> tokens) {
		tokenMap.clear();
		
		for (LabelTmplToken token : tokens) {
			register(token);
		}
	}
	
	public List<LabelTmplToken> getTokens() {
		return new ArrayList<LabelTmplToken>(tokenMap.values());
	}
}