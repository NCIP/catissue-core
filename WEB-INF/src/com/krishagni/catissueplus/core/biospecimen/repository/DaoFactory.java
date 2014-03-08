package com.krishagni.catissueplus.core.biospecimen.repository;

public interface DaoFactory {
	
	public CollectionProtocolDao getCollectionProtocolDao();

	public ParticipantDao getParticipantDao();
	
	public CollectionProtocolRegistrationDao getCprDao();
	
	public SiteDao getSiteDao();
	
	public SpecimenDao getSpecimenDao();
	
	public SpecimenCollectionGroupDao getScgDao();
	
	public UserDao getUserDao();	
}
