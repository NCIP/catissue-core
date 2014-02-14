package com.krishagni.catissueplus.core.repository;


public interface DaoFactory {

	public ParticipantDao getParticipantDao();
	public CollectionProtocolRegistrationDao getregistrationDao();
	public SiteDao getSiteDao();
}
