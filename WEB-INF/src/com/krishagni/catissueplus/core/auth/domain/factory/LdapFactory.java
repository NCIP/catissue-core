package com.krishagni.catissueplus.core.auth.domain.factory;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.Ldap;
import com.krishagni.catissueplus.core.auth.events.DomainDetail;


public interface LdapFactory {

	Ldap getLdap(DomainDetail details, AuthDomain authDomain);
}
