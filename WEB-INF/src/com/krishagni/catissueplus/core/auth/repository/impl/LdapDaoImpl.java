package com.krishagni.catissueplus.core.auth.repository.impl;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.auth.domain.Ldap;
import com.krishagni.catissueplus.core.auth.repository.LdapDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;


public class LdapDaoImpl extends AbstractDao<Ldap> implements LdapDao {

	@Override
	public boolean isUniqueLdapName(String ldapName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_LDAP_BY_NAME);
		query.setString("ldapName", ldapName);
		return query.list().size() == 0 ? true : false;
	}
	
	private static final String FQN = Ldap.class.getName();

	private static final String GET_LDAP_BY_NAME = FQN + ".getLdapByName";

	
}
