
package com.krishagni.catissueplus.core.biospecimen.repository;

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
import com.krishagni.catissueplus.core.audit.repository.AuditDao;
import com.krishagni.catissueplus.core.audit.repository.AuditReportDao;
import com.krishagni.catissueplus.core.auth.repository.DomainDao;
import com.krishagni.catissueplus.core.common.repository.KeyGeneratorDao;
import com.krishagni.catissueplus.core.notification.repository.CPStudyMappingDao;
import com.krishagni.catissueplus.core.notification.repository.ExternalAppNotificationDao;
import com.krishagni.catissueplus.core.notification.repository.ExternalApplicationDao;
import com.krishagni.catissueplus.core.printer.printRule.repository.SpecimenPrintRuleDao;
import com.krishagni.catissueplus.core.privileges.repository.PrivilegeDao;
import com.krishagni.catissueplus.core.privileges.repository.RoleDao;
import com.krishagni.catissueplus.core.privileges.repository.UserCPRoleDao;

public interface DaoFactory {

	public CollectionProtocolDao getCollectionProtocolDao();

	public ParticipantDao getParticipantDao();

	public CollectionProtocolRegistrationDao getCprDao();

	public SiteDao getSiteDao();

	public SpecimenDao getSpecimenDao();

	public SpecimenCollectionGroupDao getScgDao();

	public DepartmentDao getDepartmentDao();

	public UserDao getUserDao();

	public ContainerDao getContainerDao();

	public AuditDao getAuditDao();

	public AuditReportDao getAuditReportDao();

	public ExternalAppNotificationDao getExternalAppNotificationDao();

	public ExternalApplicationDao getExternalApplicationDao();

	public DomainDao getDomainDao();

	public CPStudyMappingDao getCPStudyMappingDao();

	public RoleDao getRoleDao();

	public PrivilegeDao getPrivilegeDao();

	public UserCPRoleDao getCPUserRoleDao();

	public KeyGeneratorDao getKeyGeneratorDao();

	public InstituteDao getInstituteDao();

	public BiohazardDao getBiohazardDao();

	public StorageContainerDao getStorageContainerDao();

	public DistributionProtocolDao getDistributionProtocolDao();

	public SpecimenPrintRuleDao getSpecimenPrintRuleDao();

	public SpecimenListDao getSpecimenListDao();

	public EquipmentDao getEquipmentDao();

	public ImageDao getImageDao();
	
	public PermissibleValueDao getPermissibleValueDao();

}
