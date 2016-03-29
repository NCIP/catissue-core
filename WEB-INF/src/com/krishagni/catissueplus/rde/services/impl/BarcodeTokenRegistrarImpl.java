package com.krishagni.catissueplus.rde.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.rde.services.BarcodeTokenRegistrar;
import com.krishagni.catissueplus.rde.tokens.BarcodeToken;

public class BarcodeTokenRegistrarImpl implements BarcodeTokenRegistrar {
	
	private Map<String, BarcodeToken> tokensMap = new HashMap<String, BarcodeToken>();

	public void setTokens(List<BarcodeToken> tokens) {
		tokensMap.clear();
		
		for (BarcodeToken token : tokens) {
			tokensMap.put(token.getName(), token);
		}
	}
	
	@Override
	public void register(BarcodeToken token) {
		tokensMap.put(token.getName(), token);
	}

	@Override
	public BarcodeToken getToken(String name) {
		return tokensMap.get(name);
	}
}
