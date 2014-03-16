package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.administrative.repository.DepartmentDao;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;

public interface DaoFactory {
	
	public CollectionProtocolDao getCollectionProtocolDao();

	public ParticipantDao getParticipantDao();
	
	public CollectionProtocolRegistrationDao getCprDao();
	
	public SiteDao getSiteDao();
	
	public SpecimenDao getSpecimenDao();
	
	public SpecimenCollectionGroupDao getScgDao();
	
	public DepartmentDao getDepartmentDao();
	
	public UserDao getUserDao();	
}
