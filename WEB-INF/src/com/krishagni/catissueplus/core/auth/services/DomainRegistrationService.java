
package com.krishagni.catissueplus.core.auth.services;

import java.util.List;

import com.krishagni.catissueplus.core.auth.events.DomainDetail;
import com.krishagni.catissueplus.core.auth.events.ListAuthDomainCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface DomainRegistrationService {

	public ResponseEvent<DomainDetail> registerDomain(RequestEvent<DomainDetail> req);

	public ResponseEvent<List<DomainDetail>> getDomains(RequestEvent<ListAuthDomainCriteria> req);

}
