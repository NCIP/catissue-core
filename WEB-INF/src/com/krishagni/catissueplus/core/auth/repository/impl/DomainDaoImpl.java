
package com.krishagni.catissueplus.core.auth.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.AuthProvider;
import com.krishagni.catissueplus.core.auth.repository.DomainDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class DomainDaoImpl extends AbstractDao<AuthDomain> implements DomainDao {

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public Boolean isUniqueAuthDomainName(String domainName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_DOMAIN_BY_NAME);
		query.setString("domainName", domainName);
		List<AuthDomain> result = query.list();
		return result.isEmpty() ? true : false;
	}

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public AuthDomain getAuthDomainByName(String domainName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_DOMAIN_BY_NAME);
		query.setString("domainName", domainName);
		List<AuthDomain> result = query.list();
		return result.isEmpty() ? null : result.get(0);
	}

	private static final String FQN = AuthDomain.class.getName();

	private static final String GET_DOMAIN_BY_NAME = FQN + ".getDomainByName";

	private static final String GET_ALL_DOMAINS = FQN + ".getAllDomains";

	private static final String GET_PROVIDER_BY_TYPE = AuthProvider.class.getName() + ".getProviderByType";

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public AuthProvider getAuthProviderByType(String authType) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PROVIDER_BY_TYPE);
		query.setString("authType", authType);
		List<AuthProvider> result = query.list();
		return result.isEmpty() ? null : result.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuthDomain> getAllAuthDomains(int maxResults) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_ALL_DOMAINS);
		query.setMaxResults(maxResults);
		return query.list();
	}

}
