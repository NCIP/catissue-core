package com.krishagni.rbac.repository;

import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenDao;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsDao;

public interface DaoFactory {
	public ResourceDao getResourceDao();
	
	public OperationDao getOperationDao();
	
	public PermissionDao getPermissionDao();
	
	public RoleDao getRoleDao();
	
	public GroupDao getGroupDao();
	
	public SubjectDao getSubjectDao();
		
	public CollectionProtocolDao getCollectionProtocolDao();
	
	public SiteDao getSiteDao();
	
	public CollectionProtocolRegistrationDao getCprDao();
	
	public VisitsDao getVisitDao();
	
	public SpecimenDao getSpecimenDao();	

}
