package com.krishagni.catissueplus.core.administrative.repository.impl;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class UserDaoImpl extends AbstractDao<User>implements UserDao {
	
	@Override
	public User getUser(Long id) {
		return (User)sessionFactory.getCurrentSession().get(User.class, id);
	}

	@Override 
	public edu.wustl.catissuecore.domain.User getUser(String witnessName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Boolean isUniqueLoginName(String loginName) {
		boolean isUnique  = true;
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USER_BY_LOGIN_NAME);
		query.setString("loginName", loginName);
		isUnique =  query.list().size() == 0 ? true : false;
		return isUnique;
	}

	@Override
	public Boolean isUniqueEmailAddress(String emailAddress) {
		boolean isUnique  = true;
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USER_BY_EMAIL_ADDRESS);
		query.setString("emailAddress", emailAddress);
		isUnique =  query.list().size() == 0 ? true : false;
		return isUnique;
	}

	private static final String FQN = User.class.getName();

	private static final String GET_USER_BY_LOGIN_NAME = FQN + ".getUserByLoginName";

	private static final String GET_USER_BY_EMAIL_ADDRESS = FQN + ".getUserByEmailAddress";
	
}
