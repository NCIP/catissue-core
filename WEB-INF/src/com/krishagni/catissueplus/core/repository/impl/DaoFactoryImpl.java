
package com.krishagni.catissueplus.core.repository.impl;

import org.hibernate.SessionFactory;

import com.krishagni.catissueplus.core.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.repository.DaoFactory;
import com.krishagni.catissueplus.core.repository.ParticipantDao;
import com.krishagni.catissueplus.core.repository.SiteDao;

public class DaoFactoryImpl implements DaoFactory {

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void setSessionFactory(AbstractDao<?> dao) {
		dao.setSessionFactory(sessionFactory);
	}

	@Override
	public ParticipantDao getParticipantDao() {
		ParticipantDaoImpl dao = new ParticipantDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public CollectionProtocolRegistrationDao getregistrationDao() {
		CollectionProtocolRegistrationDaoImpl dao = new CollectionProtocolRegistrationDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public SiteDao getSiteDao() {
		SiteDaoImpl dao = new SiteDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

}
