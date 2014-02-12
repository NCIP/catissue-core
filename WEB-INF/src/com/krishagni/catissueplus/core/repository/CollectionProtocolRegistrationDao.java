package com.krishagni.catissueplus.core.repository;

import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;


public interface CollectionProtocolRegistrationDao extends Dao<CollectionProtocolRegistration>{

	public CollectionProtocolRegistration update(CollectionProtocolRegistration protocolRegistration);
	public List<CollectionProtocolRegistration> getAllRegistrations(Long cpId);
}
