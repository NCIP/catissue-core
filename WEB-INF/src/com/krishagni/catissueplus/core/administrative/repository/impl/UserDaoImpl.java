
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.Password;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class UserDaoImpl extends AbstractDao<User> implements UserDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUsers() {
		return sessionFactory.getCurrentSession().getNamedQuery(GET_ALL_USERS).list();
	}

	
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
	public Boolean isUniqueEmailAddress(String emailAddress) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USER_BY_EMAIL_ADDRESS);
		query.setString("emailAddress", emailAddress);
		return query.list().isEmpty() ? true : false;
	}

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public List<String> getOldPasswords(Long id) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_OLD_PASSWORD_BY_USER_ID);
		query.setLong("userId", id);
		return query.list();
	}

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public List<String> getOldPasswordsByLoginName(String loginName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_OLD_PASSWORD_BY_LOGIN_NAME);
		query.setString("loginName", loginName);
		return query.list();
	}

	@Override
	public Boolean isUniqueLoginNameInDomain(String loginName, String domainName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USER_BY_LOGIN_NAME_AND_DOMAIN);
		query.setString("loginName", loginName);
		query.setString("domainName", domainName);
		return query.list().isEmpty() ? true : false;
	}
	
	@Override
	@SuppressWarnings(value = {"unchecked"})
	public User getUserByLoginNameAndDomainName(String loginName, String domainName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USER_BY_LOGIN_NAME_AND_DOMAIN);
		query.setString("loginName", loginName);
		query.setString("domainName", domainName);
		List<User> result = query.list();
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	@SuppressWarnings(value = {"unchecked"})
	public User getUserByIdAndDomainName(Long userId, String domainName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USER_BY_ID_AND_DOMAIN);
		query.setLong("userId", userId);
		query.setString("domainName", domainName);
		List<User> result = query.list();
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public List<User> getUsersById(List<Long> userIds) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USERS_BY_ID);
		return query.setParameterList("userIds", userIds).list();
	}


	private static final String FQN = User.class.getName();

	private static final String GET_USER_BY_ID_AND_DOMAIN = FQN + ".getUserByIdAndDomain";
	
	private static final String GET_USER_BY_EMAIL_ADDRESS = FQN + ".getUserByEmailAddress";
	
	private static final String GET_USER_BY_LOGIN_NAME_AND_DOMAIN = FQN + ".getUserByLoginNameAndDomainName";

	private static final String GET_OLD_PASSWORD_BY_USER_ID = Password.class.getName() + ".getOldPasswordByUserId";

	private static final String GET_OLD_PASSWORD_BY_LOGIN_NAME = Password.class.getName() + ".getOldPasswordByLoginId";

	private static final String GET_USERS_BY_ID = FQN + ".getUsersById";
	
	private static final String GET_ALL_USERS = FQN + ".getAllUsers";
}
