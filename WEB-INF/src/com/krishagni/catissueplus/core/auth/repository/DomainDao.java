
package com.krishagni.catissueplus.core.auth.repository;

import java.util.List;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.AuthProvider;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface DomainDao extends Dao<AuthDomain> {

	public Boolean isUniqueAuthDomainName(String domainName);

	public AuthDomain getAuthDomainByName(String domainName);

	public AuthProvider getAuthProviderByType(String authType);

	public List<AuthDomain> getAllAuthDomains(int maxResults);

}
