package com.krishagni.catissueplus.core.auth.repository.impl;

import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.AuthProvider;
import com.krishagni.catissueplus.core.auth.domain.AuthToken;
import com.krishagni.catissueplus.core.auth.domain.LoginAuditLog;
import com.krishagni.catissueplus.core.auth.repository.AuthDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class AuthDaoImpl extends AbstractDao<AuthDomain> implements AuthDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<AuthDomain> getAuthDomains(int maxResults) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_AUTH_DOMAINS)
				.setMaxResults(maxResults)
				.list();
	}
	
	@SuppressWarnings(value = {"unchecked"})
	@Override
	public AuthDomain getAuthDomainByName(String domainName) {
		List<AuthDomain> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_DOMAIN_BY_NAME)
				.setString("domainName", domainName)
				.list();
		
		return result.isEmpty() ? null : result.get(0);
	}
	
	@SuppressWarnings(value = {"unchecked"})
	@Override
	public AuthDomain getAuthDomainByType(String authType) {
		List<AuthDomain> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_DOMAIN_BY_TYPE)
				.setString("authType", authType)
				.list();

		return result.isEmpty() ? null : result.get(0);
	}

	@SuppressWarnings(value = {"unchecked"})
	@Override
	public Boolean isUniqueAuthDomainName(String domainName) {
		List<AuthDomain> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_DOMAIN_BY_NAME)
				.setString("domainName", domainName)
				.list();
		
		return result.isEmpty();
	}
	
	@SuppressWarnings(value = {"unchecked"})
	@Override
	public AuthProvider getAuthProviderByType(String authType) {
		List<AuthProvider> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_PROVIDER_BY_TYPE)
				.setString("authType", authType)
				.list();
		
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
	public void deleteAuthToken(AuthToken token) {
		sessionFactory.getCurrentSession().delete(token);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LoginAuditLog> getLoginAuditLogsByUser(Long userId, int maxResults) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_LOGIN_AUDIT_LOGS_BY_USER_ID)
				.setLong("userId", userId)
				.setMaxResults(maxResults)
				.list();
	}
	
	@Override
	public void deleteInactiveAuthTokens(Date latestAccessTime) {
		sessionFactory.getCurrentSession()
			.getNamedQuery(DELETE_INACTIVE_AUTH_TOKENS)
			.setTimestamp("latestCallTime", latestAccessTime)
			.executeUpdate();
	}
	
	@Override
	public int deleteAuthTokensByUser(List<Long> userIds) {
		return getCurrentSession().getNamedQuery(DELETE_AUTH_TOKENS_BY_USER_ID)
			.setParameterList("ids", userIds)
			.executeUpdate();
	}
	
	@Override
	public void saveLoginAuditLog(LoginAuditLog log) {
		sessionFactory.getCurrentSession().saveOrUpdate(log);
	}
	
	private static final String FQN = AuthDomain.class.getName();
	
	private static final String GET_AUTH_DOMAINS = FQN + ".getAuthDomains";

	private static final String GET_DOMAIN_BY_NAME = FQN + ".getDomainByName";

	private static final String GET_DOMAIN_BY_TYPE = FQN + ".getDomainByType";

	private static final String GET_PROVIDER_BY_TYPE = AuthProvider.class.getName() + ".getProviderByType";
	
	private static final String GET_AUTH_TOKEN_BY_KEY = AuthToken.class.getName() + ".getByKey";
	
	private static final String DELETE_INACTIVE_AUTH_TOKENS = AuthToken.class.getName() + ".deleteInactiveAuthTokens";

	private static final String GET_LOGIN_AUDIT_LOGS_BY_USER_ID = LoginAuditLog.class.getName() + ".getLogsByUserId";
	
	private static final String DELETE_AUTH_TOKENS_BY_USER_ID = AuthToken.class.getName() + ".deleteAuthTokensByUserId";
	
}
