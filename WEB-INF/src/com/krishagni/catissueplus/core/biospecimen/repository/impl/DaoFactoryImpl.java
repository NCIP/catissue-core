
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import org.hibernate.SessionFactory;

import com.krishagni.catissueplus.core.administrative.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.administrative.repository.ContainerDao;
import com.krishagni.catissueplus.core.administrative.repository.DepartmentDao;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.repository.impl.CollectionProtocolDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.ContainerDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.DepartmentDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.UserDaoImpl;
import com.krishagni.catissueplus.core.audit.repository.AuditDao;
import com.krishagni.catissueplus.core.audit.repository.impl.AuditDaoImpl;
import com.krishagni.catissueplus.core.auth.repository.LdapDao;
import com.krishagni.catissueplus.core.auth.repository.impl.LdapDaoImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.ParticipantDao;
import com.krishagni.catissueplus.core.biospecimen.repository.SiteDao;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenCollectionGroupDao;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.extapp.repository.ExternalAppNotificationDao;
import com.krishagni.catissueplus.core.extapp.repository.impl.ExternalAppNotificationDaoImpl;

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
	public CollectionProtocolRegistrationDao getCprDao() {
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

	@Override
	public SpecimenDao getSpecimenDao() {
		SpecimenDaoImpl dao = new SpecimenDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public CollectionProtocolDao getCollectionProtocolDao() {
		CollectionProtocolDaoImpl dao = new CollectionProtocolDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public SpecimenCollectionGroupDao getScgDao() {
		SpecimenCollectionGroupDaoImpl dao = new SpecimenCollectionGroupDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public UserDao getUserDao() {
		UserDaoImpl dao = new UserDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public DepartmentDao getDepartmentDao() {
		DepartmentDaoImpl dao = new DepartmentDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public AuditDao getAuditDao() {
		AuditDaoImpl dao = new AuditDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public ContainerDao getContainerDao() {
		ContainerDaoImpl dao = new ContainerDaoImpl();
		setSessionFactory(dao);
		return dao;
	}
	

	@Override
	public ExternalAppNotificationDao getExternalAppNotificationDao() {
		ExternalAppNotificationDaoImpl dao = new ExternalAppNotificationDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

//	@Override
//	public ExternalApplicationDao getExternalApplicationDao() {
//		ExternalApplicationDaoImpl dao = new ExternalApplicationDaoImpl();
//		setSessionFactory(dao);
//		return dao;
//	}

	@Override
	public LdapDao getLdapDao() {
		LdapDaoImpl dao = new LdapDaoImpl();
		setSessionFactory(dao);
		return dao;
	}
}
