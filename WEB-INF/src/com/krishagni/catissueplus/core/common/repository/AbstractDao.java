
package com.krishagni.catissueplus.core.common.repository;

import org.hibernate.SessionFactory;

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
		saveOrUpdate(obj, false);
	}
	
	@Override
	public void saveOrUpdate(T obj, boolean flush) {
		sessionFactory.getCurrentSession().saveOrUpdate(obj);
		if (flush) {
			sessionFactory.getCurrentSession().flush();
		}
	}
	

	@Override
	public void delete(T obj) {
		sessionFactory.getCurrentSession().delete(obj);
	}
}
