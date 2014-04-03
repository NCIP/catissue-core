package com.krishagni.catissueplus.core.auth.services;

import com.krishagni.catissueplus.core.auth.events.AddLdapEvent;
import com.krishagni.catissueplus.core.auth.events.LdapAddedEvent;


public interface LdapRegistrationService {

	LdapAddedEvent addLdap(AddLdapEvent configureLdapEvent);

}
