
package com.krishagni.catissueplus.core.auth.services;

import com.krishagni.catissueplus.core.auth.events.AllDomainsEvent;
import com.krishagni.catissueplus.core.auth.events.DomainRegisteredEvent;
import com.krishagni.catissueplus.core.auth.events.RegisterDomainEvent;
import com.krishagni.catissueplus.core.auth.events.ReqAllAuthDomainEvent;

public interface DomainRegistrationService {

	public DomainRegisteredEvent registerDomain(RegisterDomainEvent event);

	public AllDomainsEvent getAllDomains(ReqAllAuthDomainEvent req);

}
