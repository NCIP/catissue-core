package com.krishagni.catissueplus.core.de.repository.impl;

import org.hibernate.SessionFactory;

import com.krishagni.catissueplus.core.de.repository.CpCatalogSettingDao;
import com.krishagni.catissueplus.core.de.repository.DaoFactory;
import com.krishagni.catissueplus.core.de.repository.FormDao;
import com.krishagni.catissueplus.core.de.repository.QueryAuditLogDao;
import com.krishagni.catissueplus.core.de.repository.QueryFolderDao;
import com.krishagni.catissueplus.core.de.repository.SavedQueryDao;

public class DaoFactoryImpl implements DaoFactory {
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public FormDao getFormDao() {
		FormDaoImpl formDao = new FormDaoImpl();
		formDao.setSessionFactory(sessionFactory);
		return formDao;
	}

	@Override
	public SavedQueryDao getSavedQueryDao() {
		SavedQueryDaoImpl savedQueryDao = new SavedQueryDaoImpl();
		savedQueryDao.setSessionFactory(sessionFactory);
		return savedQueryDao;
	}
	
	@Override
	public QueryFolderDao getQueryFolderDao() {
		QueryFolderDaoImpl queryFolderDao = new QueryFolderDaoImpl();
		queryFolderDao.setSessionFactory(sessionFactory);
		return queryFolderDao;
	}

	@Override
	public QueryAuditLogDao getQueryAuditLogDao() {
		QueryAuditLogDaoImpl auditLogDao = new QueryAuditLogDaoImpl();
		auditLogDao.setSessionFactory(sessionFactory);
		return auditLogDao;
	}

	@Override
	public CpCatalogSettingDao getCpCatalogSettingDao() {
		CpCatalogSettingDaoImpl dao = new CpCatalogSettingDaoImpl();
		dao.setSessionFactory(sessionFactory);
		return dao;
	}
}
