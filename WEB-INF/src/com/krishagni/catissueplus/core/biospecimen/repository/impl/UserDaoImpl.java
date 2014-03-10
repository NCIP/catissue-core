package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.repository.UserDao;
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

}
