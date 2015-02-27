package com.krishagni.catissueplus.core.auth.domain.factory;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.events.AuthDomainDetail;


public interface DomainRegistrationFactory {

	AuthDomain createDomain(AuthDomainDetail domainDetails);

}
