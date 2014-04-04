package com.krishagni.catissueplus.core.auth.domain.factory;

import com.krishagni.catissueplus.core.auth.domain.Ldap;
import com.krishagni.catissueplus.core.auth.events.LdapDetails;


public interface LdapRegistrationFactory {

	Ldap getLdap(LdapDetails ldapDetails);

}
