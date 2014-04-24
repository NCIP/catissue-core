
package com.krishagni.catissueplus.core.auth.repository;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.AuthProvider;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface DomainDao extends Dao<AuthDomain> {

	Boolean isUniqueAuthDomainName(String domainName);

	AuthDomain getAuthDomainByName(String domainName);

	AuthProvider getAuthProviderByType(String authType);

}
