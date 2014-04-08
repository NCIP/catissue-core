
package com.krishagni.catissueplus.core.auth.repository;

import com.krishagni.catissueplus.core.auth.domain.Ldap;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface LdapDao extends Dao<Ldap> {

	boolean isUniqueLdapName(String ldapName);
	
	Ldap getLdapByLdapId(Long ldapId);

}
