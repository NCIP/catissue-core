
package com.krishagni.catissueplus.core.auth.services.impl;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.factory.AuthErrorCode;
import com.krishagni.catissueplus.core.auth.domain.factory.DomainRegistrationFactory;
import com.krishagni.catissueplus.core.auth.events.AllDomainsEvent;
import com.krishagni.catissueplus.core.auth.events.DomainDetails;
import com.krishagni.catissueplus.core.auth.events.DomainRegisteredEvent;
import com.krishagni.catissueplus.core.auth.events.RegisterDomainEvent;
import com.krishagni.catissueplus.core.auth.events.ReqAllAuthDomainEvent;
import com.krishagni.catissueplus.core.auth.services.DomainRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class DomainRegistrationServiceImpl implements DomainRegistrationService {

	private DaoFactory daoFactory;

	private DomainRegistrationFactory domainRegFactory;

	private final String DOMAIN_NAME = "domain name";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setDomainRegFactory(DomainRegistrationFactory domainRegFactory) {
		this.domainRegFactory = domainRegFactory;
	}

	@Override
	@PlusTransactional
	public AllDomainsEvent getAllDomains(ReqAllAuthDomainEvent req) {
		List<AuthDomain> authDomains = daoFactory.getDomainDao().getAllAuthDomains(req.getMaxResults());

		List<DomainDetails> result = new ArrayList<DomainDetails>();

		for (AuthDomain domain : authDomains) {
			result.add(DomainDetails.fromDomain(domain));
		}
		return AllDomainsEvent.ok(result);
	}

	@Override
	@PlusTransactional
	public DomainRegisteredEvent registerDomain(RegisterDomainEvent event) {
		try {
			DomainDetails details = event.getDomainDetails();
			AuthDomain authDomain = domainRegFactory.getAuthDomain(details);
			ensureUniqueDomainName(authDomain.getName());
			daoFactory.getDomainDao().saveOrUpdate(authDomain);
			return DomainRegisteredEvent.ok(DomainDetails.fromDomain(authDomain));
		}
		catch (ObjectCreationException ce) {
			return DomainRegisteredEvent.invalidRequest(AuthErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return DomainRegisteredEvent.serverError(e);
		}
	}

	private void ensureUniqueDomainName(String domainName) {
		if (!daoFactory.getDomainDao().isUniqueAuthDomainName(domainName)) {
			reportError(AuthErrorCode.INVALID_ATTR_VALUE, DOMAIN_NAME);
		}
	}

}
