package com.krishagni.openspecimen.rde.services;

import com.krishagni.openspecimen.rde.tokens.BarcodeToken;


public interface BarcodeTokenRegistrar {
	public void register(BarcodeToken token);
	
	public BarcodeToken getToken(String name);

}
