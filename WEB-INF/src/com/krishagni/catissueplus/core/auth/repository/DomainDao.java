
package com.krishagni.catissueplus.core.auth.repository;

import java.util.List;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.AuthProvider;
import com.krishagni.catissueplus.core.auth.domain.AuthToken;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface DomainDao extends Dao<AuthDomain> {

	public List<AuthDomain> getAllAuthDomains(int maxResults);
	
	public AuthDomain getAuthDomainByName(String domainName);
	
	public Boolean isUniqueAuthDomainName(String domainName);

	public AuthProvider getAuthProviderByType(String authType);
	
	public AuthToken getAuthTokenByKey(String key);
	
	public void saveAuthToken(AuthToken token);
	
	public void deleteAuthToken(String key);

}
