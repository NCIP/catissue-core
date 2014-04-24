package com.krishagni.catissueplus.core.auth.domain.factory;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.events.DomainDetails;


public interface DomainRegistrationFactory {

	AuthDomain getAuthDomain(DomainDetails domainDetails);

}
