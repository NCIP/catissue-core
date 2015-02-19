package com.krishagni.rbac.repository.impl;

import org.hibernate.SessionFactory;

import com.krishagni.rbac.repository.DaoFactory;
import com.krishagni.rbac.repository.GroupDao;
import com.krishagni.rbac.repository.OperationDao;
import com.krishagni.rbac.repository.PermissionDao;
import com.krishagni.rbac.repository.ResourceDao;
import com.krishagni.rbac.repository.RoleDao;
import com.krishagni.rbac.repository.SubjectDao;

public class DaoFactoryImpl implements DaoFactory {
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public ResourceDao getResourceDao() {
		ResourceDaoImpl dao = new ResourceDaoImpl();
		dao.setSessionFactory(sessionFactory);
		return dao;
	}

	@Override
	public OperationDao getOperationDao() {
		OperationDaoImpl dao = new OperationDaoImpl();
		dao.setSessionFactory(sessionFactory);
		return dao;
	}

	@Override
	public PermissionDao getPermissionDao() {
		PermissionDaoImpl dao = new PermissionDaoImpl();
		dao.setSessionFactory(sessionFactory);
		return dao;
	}

	@Override
	public RoleDao getRoleDao() {
		RoleDaoImpl dao = new RoleDaoImpl();
		dao.setSessionFactory(sessionFactory);
		return dao;
	}

	@Override
	public GroupDao getGroupDao() {
		GroupDaoImpl dao = new GroupDaoImpl();
		dao.setSessionFactory(sessionFactory);
		return dao;
	}

	@Override
	public SubjectDao getSubjectDao() {
		SubjectDaoImpl dao = new SubjectDaoImpl();
		dao.setSessionFactory(sessionFactory);
		return dao;
	}

}
