
package com.krishagni.catissueplus.core.auth.services;

import com.krishagni.catissueplus.core.auth.events.LoginDetail;

public interface AuthenticationService {
	void authenticate(LoginDetail loginDetail);
}
