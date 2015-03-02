
package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.administrative.repository.ContainerDao;
import com.krishagni.catissueplus.core.administrative.repository.DistributionOrderDao;
import com.krishagni.catissueplus.core.administrative.repository.DistributionProtocolDao;
import com.krishagni.catissueplus.core.administrative.repository.InstituteDao;
import com.krishagni.catissueplus.core.administrative.repository.PermissibleValueDao;
import com.krishagni.catissueplus.core.administrative.repository.ScheduledJobDao;
import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerDao;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.audit.repository.AuditDao;
import com.krishagni.catissueplus.core.auth.repository.DomainDao;
import com.krishagni.catissueplus.core.common.repository.KeyGeneratorDao;
import com.krishagni.catissueplus.core.notification.repository.CPStudyMappingDao;
import com.krishagni.catissueplus.core.notification.repository.ExternalAppNotificationDao;
import com.krishagni.catissueplus.core.notification.repository.ExternalApplicationDao;


public interface DaoFactory {

	public CollectionProtocolDao getCollectionProtocolDao();

	public ParticipantDao getParticipantDao();

	public CollectionProtocolRegistrationDao getCprDao();

	public SiteDao getSiteDao();

	public SpecimenDao getSpecimenDao();
	
	public SpecimenRequirementDao getSpecimenRequirementDao();

	public VisitsDao getVisitsDao();

	public UserDao getUserDao();
	
	public ContainerDao getContainerDao();

	public ExternalAppNotificationDao getExternalAppNotificationDao();

	public ExternalApplicationDao getExternalApplicationDao();

	public DomainDao getDomainDao();

	public CPStudyMappingDao getCPStudyMappingDao();

	public KeyGeneratorDao getKeyGeneratorDao();

	public InstituteDao getInstituteDao();

	public StorageContainerDao getStorageContainerDao();

	public DistributionProtocolDao getDistributionProtocolDao();

	public SpecimenListDao getSpecimenListDao();

	public PermissibleValueDao getPermissibleValueDao();
	
	public AuditDao getAuditDao();
	
	public DistributionOrderDao getDistributionOrderDao();
	
	public ScheduledJobDao getScheduledJobDao();
}
