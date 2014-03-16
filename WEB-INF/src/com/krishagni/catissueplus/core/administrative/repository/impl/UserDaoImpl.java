package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class UserDaoImpl extends AbstractDao<User>implements UserDao {
	
	private String ACTIVITY_STATUS_DISABLED = "Disabled";
	
	
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
	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		String hql = "FROM "+ User.class.getName();
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List<User> result = query.list();
		return result;
	}

	@Override
	public Boolean isUniqueLoginName(String loginName) {
		boolean isUnique  = true;
		String hql = "SELECT user.loginName FROM "+ User.class.getName() +" user WHERE user.loginName = :loginName" ;
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString("loginName", loginName);
		isUnique =  query.list().size() == 0 ? true : false;
		return isUnique;
	}

}
