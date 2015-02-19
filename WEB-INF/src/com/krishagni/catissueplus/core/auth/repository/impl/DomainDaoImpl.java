
package com.krishagni.catissueplus.core.auth.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.AuthProvider;
import com.krishagni.catissueplus.core.auth.domain.AuthToken;
import com.krishagni.catissueplus.core.auth.repository.DomainDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class DomainDaoImpl extends AbstractDao<AuthDomain> implements DomainDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<AuthDomain> getAllAuthDomains(int maxResults) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_ALL_DOMAINS);
		query.setMaxResults(maxResults);
		return query.list();
	}
	
	@SuppressWarnings(value = {"unchecked"})
	@Override
	public AuthDomain getAuthDomainByName(String domainName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_DOMAIN_BY_NAME);
		query.setString("domainName", domainName);
		List<AuthDomain> result = query.list();
		return result.isEmpty() ? null : result.get(0);
	}
	
	@SuppressWarnings(value = {"unchecked"})
	@Override
	public Boolean isUniqueAuthDomainName(String domainName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_DOMAIN_BY_NAME);
		query.setString("domainName", domainName);
		List<AuthDomain> result = query.list();
		return result.isEmpty();
	}
	
	@SuppressWarnings(value = {"unchecked"})
	@Override
	public AuthProvider getAuthProviderByType(String authType) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PROVIDER_BY_TYPE);
		query.setString("authType", authType);
		List<AuthProvider> result = query.list();
		return result.isEmpty() ? null : result.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public AuthToken getAuthTokenByKey(String key) {
		List<AuthToken> tokens = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_AUTH_TOKEN_BY_KEY)
				.setString("token", key)
				.list();

		return tokens.isEmpty() ? null : tokens.get(0);
	}
	
	@Override
	public void saveAuthToken(AuthToken token) {
		 sessionFactory.getCurrentSession().saveOrUpdate(token);
	}
	
	@Override
	public void deleteAuthToken(String key) {
		String deleteHql = "delete from " + AuthToken.class.getSimpleName() + " where token = :key";
		Query query = sessionFactory.getCurrentSession().createQuery(deleteHql);
		query.setString("key", key);
		query.executeUpdate();
	}
	
	private static final String FQN = AuthDomain.class.getName();
	
	private static final String GET_ALL_DOMAINS = FQN + ".getAllDomains";

	private static final String GET_DOMAIN_BY_NAME = FQN + ".getDomainByName";

	private static final String GET_PROVIDER_BY_TYPE = AuthProvider.class.getName() + ".getProviderByType";
	
	private static final String GET_AUTH_TOKEN_BY_KEY = AuthToken.class.getName() + ".getByKey";

}
