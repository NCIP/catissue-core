
package com.krishagni.catissueplus.core.repository.impl;

import org.hibernate.SessionFactory;

import com.krishagni.catissueplus.core.repository.Dao;


public class AbstractDao<T> implements Dao<T> {

	protected SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void saveOrUpdate(T obj) {
		sessionFactory.getCurrentSession().saveOrUpdate(obj);
	}

}
