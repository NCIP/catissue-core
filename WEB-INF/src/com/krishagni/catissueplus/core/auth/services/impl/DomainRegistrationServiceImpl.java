
package com.krishagni.catissueplus.core.auth.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.factory.AuthProviderErrorCode;
import com.krishagni.catissueplus.core.auth.domain.factory.DomainRegistrationFactory;
import com.krishagni.catissueplus.core.auth.events.DomainDetail;
import com.krishagni.catissueplus.core.auth.events.ListAuthDomainCriteria;
import com.krishagni.catissueplus.core.auth.services.DomainRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DomainRegistrationServiceImpl implements DomainRegistrationService {

	private DaoFactory daoFactory;

	private DomainRegistrationFactory domainRegFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setDomainRegFactory(DomainRegistrationFactory domainRegFactory) {
		this.domainRegFactory = domainRegFactory;
	}

	@Override
	@PlusTransactional	
	public ResponseEvent<List<DomainDetail>> getDomains(RequestEvent<ListAuthDomainCriteria> req) {
		List<AuthDomain> authDomains = daoFactory.getAuthDao().getAuthDomains(req.getPayload().maxResults());

		List<DomainDetail> result = new ArrayList<DomainDetail>();
		for (AuthDomain domain : authDomains) {
			result.add(DomainDetail.fromDomain(domain));
		}
		
		return ResponseEvent.response(result);
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DomainDetail> registerDomain(RequestEvent<DomainDetail> req) {
		try {
			DomainDetail detail = req.getPayload();			
			AuthDomain authDomain = domainRegFactory.getAuthDomain(detail);
			
			ensureUniqueDomainName(authDomain.getName());
			daoFactory.getAuthDao().saveOrUpdate(authDomain);
			return ResponseEvent.response(DomainDetail.fromDomain(authDomain));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private void ensureUniqueDomainName(String domainName) {
		if (!daoFactory.getAuthDao().isUniqueAuthDomainName(domainName)) {
			throw OpenSpecimenException.userError(AuthProviderErrorCode.DUP_DOMAIN_NAME);
		}
	}
}
