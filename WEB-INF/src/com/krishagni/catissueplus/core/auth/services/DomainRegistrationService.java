
package com.krishagni.catissueplus.core.auth.services;

import java.util.List;

import com.krishagni.catissueplus.core.auth.events.AuthDomainDetail;
import com.krishagni.catissueplus.core.auth.events.ListAuthDomainCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface DomainRegistrationService {

	public ResponseEvent<AuthDomainDetail> registerDomain(RequestEvent<AuthDomainDetail> req);

	public ResponseEvent<List<AuthDomainDetail>> getDomains(RequestEvent<ListAuthDomainCriteria> req);

}
