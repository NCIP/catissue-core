
package com.krishagni.catissueplus.core.auth.repository;

import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.AuthProvider;
import com.krishagni.catissueplus.core.auth.domain.AuthToken;
import com.krishagni.catissueplus.core.auth.domain.LoginAuditLog;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface AuthDao extends Dao<AuthDomain> {

	public List<AuthDomain> getAuthDomains(int maxResults);
	
	public AuthDomain getAuthDomainByName(String domainName);
	
	public AuthDomain getAuthDomainByType(String authType);

	public Boolean isUniqueAuthDomainName(String domainName);

	public AuthProvider getAuthProviderByType(String authType);
	
	public AuthToken getAuthTokenByKey(String key);
	
	public void saveAuthToken(AuthToken token);
	
	public void deleteInactiveAuthTokens(Date expiresOn);
	
	public void deleteAuthToken(AuthToken token);
	
	public List<LoginAuditLog> getLoginAuditLogsByUser(Long userId, int maxResults);
	
	public void saveLoginAuditLog(LoginAuditLog log);
	
	public int deleteAuthTokensByUser(List<Long> userIds);

}
