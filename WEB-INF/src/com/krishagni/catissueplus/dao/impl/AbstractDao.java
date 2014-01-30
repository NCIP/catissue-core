
package com.krishagni.catissueplus.dao.impl;

import org.hibernate.SessionFactory;

import com.krishagni.catissueplus.dao.Dao;

public class AbstractDao<T> implements Dao<T> {

	protected SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void save(T object) {
		sessionFactory.getCurrentSession().saveOrUpdate(object);
	}

}
