
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.Password;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.auth.domain.Ldap;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class UserDaoImpl extends AbstractDao<User> implements UserDao {

	@Override
	public User getUser(Long id) {
		return (User) sessionFactory.getCurrentSession().get(User.class, id);
	}

	@Override
	public User getUser(String witnessName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isUniqueLoginName(String loginName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USER_BY_LOGIN_NAME);
		query.setString("loginName", loginName);
		return query.list().size() == 0 ? true : false;
	}

	@Override
	public Boolean isUniqueEmailAddress(String emailAddress) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USER_BY_EMAIL_ADDRESS);
		query.setString("emailAddress", emailAddress);
		return query.list().size() == 0 ? true : false;
	}

	@Override
	public List<String> getOldPasswords(Long id) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_OLD_PASSWORD_BY_USER_ID);
		query.setLong("userId", id);
		return query.list();
	}

	@Override
	public List<String> getOldPasswordsByLoginName(String loginName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_OLD_PASSWORD_BY_LOGIN_NAME);
		query.setString("loginName", loginName);
		return query.list();
	}

	@Override
	public User getUserByLoginName(String loginName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USER_BY_LOGIN_NAME);
		query.setString("loginName", loginName);
		List<User> result = query.list();
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public Boolean isValidLdapId(Long ldapId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_LDAP_BY_LDAP_ID);
		query.setLong("ldapId", ldapId);
		return query.list().size() == 0 ? false : true;
	}

	private static final String FQN = User.class.getName();

	private static final String GET_USER_BY_LOGIN_NAME = FQN + ".getUserByLoginName";

	private static final String GET_USER_BY_EMAIL_ADDRESS = FQN + ".getUserByEmailAddress";

	private static final String GET_LDAP_BY_LDAP_ID = Ldap.class.getName() + ".getLdapByLdapId";

	private static final String GET_OLD_PASSWORD_BY_USER_ID = Password.class.getName() + ".getOldPasswordByUserId";

	private static final String GET_OLD_PASSWORD_BY_LOGIN_NAME = Password.class.getName() + ".getOldPasswordByLoginId";

}
