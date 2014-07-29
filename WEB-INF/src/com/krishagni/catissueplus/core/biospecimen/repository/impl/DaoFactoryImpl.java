
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import org.hibernate.SessionFactory;

import com.krishagni.catissueplus.core.administrative.repository.BiohazardDao;
import com.krishagni.catissueplus.core.administrative.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.administrative.repository.ContainerDao;
import com.krishagni.catissueplus.core.administrative.repository.DepartmentDao;
import com.krishagni.catissueplus.core.administrative.repository.DistributionProtocolDao;
import com.krishagni.catissueplus.core.administrative.repository.EquipmentDao;
import com.krishagni.catissueplus.core.administrative.repository.ImageDao;
import com.krishagni.catissueplus.core.administrative.repository.InstituteDao;
import com.krishagni.catissueplus.core.administrative.repository.PermissibleValueDao;
import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerDao;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.repository.impl.BiohazardDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.CollectionProtocolDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.ContainerDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.DepartmentDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.DistributionProtocolDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.EquipmentDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.ImageDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.InstituteDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.PermissibleValueDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.SiteDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.StorageContainerDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.UserDaoImpl;
import com.krishagni.catissueplus.core.audit.repository.AuditDao;
import com.krishagni.catissueplus.core.audit.repository.AuditReportDao;
import com.krishagni.catissueplus.core.audit.repository.impl.AuditDaoImpl;
import com.krishagni.catissueplus.core.audit.repository.impl.AuditReportDaoImpl;
import com.krishagni.catissueplus.core.auth.repository.DomainDao;
import com.krishagni.catissueplus.core.auth.repository.impl.DomainDaoImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.ParticipantDao;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenCollectionGroupDao;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenDao;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.repository.KeyGeneratorDao;
import com.krishagni.catissueplus.core.common.repository.impl.KeyGeneratorDaoImpl;
import com.krishagni.catissueplus.core.notification.repository.CPStudyMappingDao;
import com.krishagni.catissueplus.core.notification.repository.ExternalAppNotificationDao;
import com.krishagni.catissueplus.core.notification.repository.ExternalApplicationDao;
import com.krishagni.catissueplus.core.notification.repository.impl.CPStudyMappingDaoImpl;
import com.krishagni.catissueplus.core.notification.repository.impl.ExternalAppNotificationDaoImpl;
import com.krishagni.catissueplus.core.notification.repository.impl.ExternalApplicationDaoImpl;
import com.krishagni.catissueplus.core.printer.printRule.repository.SpecimenPrintRuleDao;
import com.krishagni.catissueplus.core.printer.printRule.repository.impl.SpecimenPrintRuleDaoImpl;
import com.krishagni.catissueplus.core.privileges.repository.PrivilegeDao;
import com.krishagni.catissueplus.core.privileges.repository.RoleDao;
import com.krishagni.catissueplus.core.privileges.repository.UserCPRoleDao;
import com.krishagni.catissueplus.core.privileges.repository.impl.PrivilegeDaoImpl;
import com.krishagni.catissueplus.core.privileges.repository.impl.RoleDaoImpl;
import com.krishagni.catissueplus.core.privileges.repository.impl.UserCPRoleDaoImpl;

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
	public AuditReportDao getAuditReportDao() {
		AuditReportDaoImpl dao = new AuditReportDaoImpl();
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

	@Override
	public ExternalApplicationDao getExternalApplicationDao() {
		ExternalApplicationDaoImpl dao = new ExternalApplicationDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public DomainDao getDomainDao() {
		DomainDaoImpl dao = new DomainDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public CPStudyMappingDao getCPStudyMappingDao() {
		CPStudyMappingDaoImpl dao = new CPStudyMappingDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public RoleDao getRoleDao() {
		RoleDaoImpl dao = new RoleDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public PrivilegeDao getPrivilegeDao() {
		PrivilegeDaoImpl dao = new PrivilegeDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public UserCPRoleDao getCPUserRoleDao() {
		UserCPRoleDaoImpl dao = new UserCPRoleDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public KeyGeneratorDao getKeyGeneratorDao() {
		KeyGeneratorDaoImpl dao = new KeyGeneratorDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public InstituteDao getInstituteDao() {
		InstituteDaoImpl dao = new InstituteDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public BiohazardDao getBiohazardDao() {
		BiohazardDaoImpl dao = new BiohazardDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public StorageContainerDao getStorageContainerDao() {
		StorageContainerDaoImpl dao = new StorageContainerDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public DistributionProtocolDao getDistributionProtocolDao() {
		DistributionProtocolDaoImpl dao = new DistributionProtocolDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public SpecimenPrintRuleDao getSpecimenPrintRuleDao() {
		SpecimenPrintRuleDaoImpl dao = new SpecimenPrintRuleDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public SpecimenListDao getSpecimenListDao() {
		SpecimenListDaoImpl dao = new SpecimenListDaoImpl();
		setSessionFactory(dao);
		return dao;
	}
	
	@Override
	public PermissibleValueDao getPermissibleValueDao() {
		PermissibleValueDaoImpl dao = new PermissibleValueDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public EquipmentDao getEquipmentDao() {
		EquipmentDaoImpl dao = new EquipmentDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public ImageDao getImageDao() {
		ImageDaoImpl dao = new ImageDaoImpl();
		setSessionFactory(dao);
		return dao;
	}
}
