
package com.krishagni.catissueplus.core.auth.services.impl;

import java.util.List;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.factory.AuthProviderErrorCode;
import com.krishagni.catissueplus.core.auth.domain.factory.DomainRegistrationFactory;
import com.krishagni.catissueplus.core.auth.events.AuthDomainDetail;
import com.krishagni.catissueplus.core.auth.events.AuthDomainSummary;
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
	public ResponseEvent<List<AuthDomainSummary>> getDomains(RequestEvent<ListAuthDomainCriteria> req) {
		List<AuthDomain> authDomains = daoFactory.getAuthDao().getAuthDomains(req.getPayload().maxResults());
		return ResponseEvent.response(AuthDomainSummary.from(authDomains));
	}

	@Override
	@PlusTransactional
	public ResponseEvent<AuthDomainDetail> registerDomain(RequestEvent<AuthDomainDetail> req) {
		try {
			AuthDomainDetail detail = req.getPayload();			
			AuthDomain authDomain = domainRegFactory.createDomain(detail);
			
			ensureUniqueDomainName(authDomain.getName());
			daoFactory.getAuthDao().saveOrUpdate(authDomain);
			return ResponseEvent.response(AuthDomainDetail.from(authDomain));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@PlusTransactional
	public ResponseEvent<AuthDomainDetail> updateDomain(RequestEvent<AuthDomainDetail> req) {
		try {
			AuthDomainDetail detail = req.getPayload();
			AuthDomain existingDomain = daoFactory.getAuthDao().getAuthDomainByName(detail.getName());
			if (existingDomain == null) {
				throw OpenSpecimenException.userError(AuthProviderErrorCode.DOMAIN_NOT_FOUND); 
			}
			
			AuthDomain authDomain = domainRegFactory.createDomain(detail);
			existingDomain.update(authDomain);
			
			return  ResponseEvent.response(AuthDomainDetail.from(existingDomain));
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
