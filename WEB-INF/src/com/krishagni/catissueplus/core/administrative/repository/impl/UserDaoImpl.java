package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.events.PasswordDetails;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class UserDaoImpl extends AbstractDao<User>implements UserDao {
	
	@Override
	public User getUser(Long id) {
		return (User)sessionFactory.getCurrentSession().get(User.class, id);
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
	public Boolean isValidOldPassword(String oldPassword, Long id) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_OLD_PASSWORD_BY_USER_ID);
		query.setLong("user_id", id);
		
		List<String> results = query.list();
		if(results.size() > 0) {
			return results.get(results.size()-1).equals(oldPassword) ? true : false;
		}
		return false;
	}

	private static final String FQN = User.class.getName();

	private static final String GET_USER_BY_LOGIN_NAME = FQN + ".getUserByLoginName";

	private static final String GET_USER_BY_EMAIL_ADDRESS = FQN + ".getUserByEmailAddress";
	
	private static final String GET_OLD_PASSWORD_BY_USER_ID = PasswordDetails.class.getName() + ".getOldPasswordByUserId";
	
}
