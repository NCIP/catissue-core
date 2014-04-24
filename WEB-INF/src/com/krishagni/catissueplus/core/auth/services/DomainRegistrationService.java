package com.krishagni.catissueplus.core.auth.services;

import com.krishagni.catissueplus.core.auth.events.DomainRegisteredEvent;
import com.krishagni.catissueplus.core.auth.events.RegisterDomainEvent;


public interface DomainRegistrationService {

	DomainRegisteredEvent registerDomain(RegisterDomainEvent event);

}
