package com.krishagni.rbac.repository.impl;

import org.hibernate.SessionFactory;

import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.administrative.repository.impl.SiteDaoImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenDao;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsDao;
import com.krishagni.catissueplus.core.biospecimen.repository.impl.CollectionProtocolDaoImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.impl.CollectionProtocolRegistrationDaoImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.impl.SpecimenDaoImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.impl.VisitsDaoImpl;
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

	@Override
	public CollectionProtocolDao getCollectionProtocolDao() {
		CollectionProtocolDaoImpl dao = new CollectionProtocolDaoImpl();
		dao.setSessionFactory(sessionFactory);
		return dao;
	}

	@Override
	public SiteDao getSiteDao() {
		SiteDaoImpl dao = new SiteDaoImpl();
		dao.setSessionFactory(sessionFactory);
		return dao;
	}

	@Override
	public CollectionProtocolRegistrationDao getCprDao() {
		CollectionProtocolRegistrationDaoImpl dao = new CollectionProtocolRegistrationDaoImpl();
		dao.setSessionFactory(sessionFactory);
		return dao;
	}

	@Override
	public VisitsDao getVisitDao() {
		VisitsDaoImpl dao = new VisitsDaoImpl();
		dao.setSessionFactory(sessionFactory);
		return dao;
	}

	@Override
	public SpecimenDao getSpecimenDao() {
		SpecimenDaoImpl dao = new SpecimenDaoImpl();
		dao.setSessionFactory(sessionFactory);		
		return dao;
	}
}
