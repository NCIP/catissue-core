
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import org.hibernate.SessionFactory;

import com.krishagni.catissueplus.core.administrative.repository.ContainerTypeDao;
import com.krishagni.catissueplus.core.administrative.repository.DistributionOrderDao;
import com.krishagni.catissueplus.core.administrative.repository.DistributionProtocolDao;
import com.krishagni.catissueplus.core.administrative.repository.DpRequirementDao;
import com.krishagni.catissueplus.core.administrative.repository.InstituteDao;
import com.krishagni.catissueplus.core.administrative.repository.PermissibleValueDao;
import com.krishagni.catissueplus.core.administrative.repository.ScheduledJobDao;
import com.krishagni.catissueplus.core.administrative.repository.ShipmentDao;
import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.administrative.repository.SpecimenRequestDao;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerDao;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.repository.impl.ContainerTypeDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.DistributionOrderDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.DistributionProtocolDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.DpRequirementDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.InstituteDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.PermissibleValueDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.ScheduledJobDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.ShipmentDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.SiteDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.SpecimenRequestDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.StorageContainerDaoImpl;
import com.krishagni.catissueplus.core.administrative.repository.impl.UserDaoImpl;
import com.krishagni.catissueplus.core.audit.repository.AuditDao;
import com.krishagni.catissueplus.core.audit.repository.impl.AuditDaoImpl;
import com.krishagni.catissueplus.core.auth.repository.AuthDao;
import com.krishagni.catissueplus.core.auth.repository.impl.AuthDaoImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.AnonymizeEventDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CpReportSettingsDao;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.LabelPrintJobDao;
import com.krishagni.catissueplus.core.biospecimen.repository.ParticipantDao;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenDao;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListDao;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenRequirementDao;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.repository.ConfigSettingDao;
import com.krishagni.catissueplus.core.common.repository.UnhandledExceptionDao;
import com.krishagni.catissueplus.core.common.repository.UniqueIdGenerator;
import com.krishagni.catissueplus.core.common.repository.UpgradeLogDao;
import com.krishagni.catissueplus.core.common.repository.impl.ConfigSettingDaoImpl;
import com.krishagni.catissueplus.core.common.repository.impl.UnhandledExceptionDaoImpl;
import com.krishagni.catissueplus.core.common.repository.impl.UniqueIdGeneratorImpl;
import com.krishagni.catissueplus.core.common.repository.impl.UpgradeLogDaoImpl;

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
		sessionFactory.getCurrentSession().enableFilter("activeEntity");
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
	public AnonymizeEventDao getAnonymizeEventDao() {
		AnonymizeEventDaoImpl dao = new AnonymizeEventDaoImpl();
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
	public SpecimenRequirementDao getSpecimenRequirementDao() {
		SpecimenRequirementDaoImpl dao = new SpecimenRequirementDaoImpl();
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
	public VisitsDao getVisitsDao() {
		VisitsDaoImpl dao = new VisitsDaoImpl();
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
	public AuthDao getAuthDao() {
		AuthDaoImpl dao = new AuthDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public UniqueIdGenerator getUniqueIdGenerator() {
		UniqueIdGeneratorImpl impl = new UniqueIdGeneratorImpl();
		setSessionFactory(impl);
		return impl;
	}

	@Override
	public InstituteDao getInstituteDao() {
		InstituteDaoImpl dao = new InstituteDaoImpl();
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
	public ContainerTypeDao getContainerTypeDao() {
		ContainerTypeDaoImpl dao = new ContainerTypeDaoImpl();
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
	public DistributionOrderDao getDistributionOrderDao() {
		DistributionOrderDaoImpl dao = new DistributionOrderDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public ScheduledJobDao getScheduledJobDao() {
		ScheduledJobDaoImpl dao = new ScheduledJobDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public ConfigSettingDao getConfigSettingDao() {
		ConfigSettingDaoImpl dao = new ConfigSettingDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public LabelPrintJobDao getLabelPrintJobDao() {
		LabelPrintJobDaoImpl dao = new LabelPrintJobDaoImpl();
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
	public DpRequirementDao getDistributionProtocolRequirementDao() {
		DpRequirementDaoImpl dao = new DpRequirementDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public ShipmentDao getShipmentDao() {
		ShipmentDaoImpl dao = new ShipmentDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public SpecimenRequestDao getSpecimenRequestDao() {
		SpecimenRequestDaoImpl dao = new SpecimenRequestDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public UpgradeLogDao getUpgradeLogDao() {
		UpgradeLogDaoImpl dao = new UpgradeLogDaoImpl();
		setSessionFactory(dao);
		return dao;
	}

	@Override
	public CpReportSettingsDao getCpReportSettingsDao() {
		CpReportSettingsDaoImpl dao = new CpReportSettingsDaoImpl();
		setSessionFactory(dao);
		return dao;
	}
	
	@Override
	public UnhandledExceptionDao getUnhandledExceptionDao() {
		UnhandledExceptionDaoImpl dao = new UnhandledExceptionDaoImpl();
		setSessionFactory(dao);
		return dao;
	}
}
