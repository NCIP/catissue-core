
package com.krishagni.catissueplus.core.auth.services;

import java.util.Map;


public interface AuthenticationService {
	public AuthenticationService init(Map<String, String> props);
	
	public void authenticate(String username, String password);
}
