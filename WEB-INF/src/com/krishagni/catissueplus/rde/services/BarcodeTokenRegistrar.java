package com.krishagni.catissueplus.rde.services;

import com.krishagni.catissueplus.rde.tokens.BarcodeToken;


public interface BarcodeTokenRegistrar {
	public void register(BarcodeToken token);
	
	public BarcodeToken getToken(String name);

}
