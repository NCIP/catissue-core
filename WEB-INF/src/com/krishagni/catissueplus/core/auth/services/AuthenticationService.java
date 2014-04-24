
package com.krishagni.catissueplus.core.auth.services;

import com.krishagni.catissueplus.core.auth.events.LoginDetails;

public interface AuthenticationService {

	void authenticate(LoginDetails loginDetails);

}
