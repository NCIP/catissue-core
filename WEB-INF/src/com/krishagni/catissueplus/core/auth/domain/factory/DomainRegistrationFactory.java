package com.krishagni.catissueplus.core.auth.domain.factory;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.events.DomainDetail;


public interface DomainRegistrationFactory {

	AuthDomain getAuthDomain(DomainDetail domainDetails);

}
