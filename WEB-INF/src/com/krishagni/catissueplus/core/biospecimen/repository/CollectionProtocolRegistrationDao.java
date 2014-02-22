package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;


public interface CollectionProtocolRegistrationDao extends Dao<CollectionProtocolRegistration>{

	public CollectionProtocolRegistration update(CollectionProtocolRegistration protocolRegistration);
	
	public List<CollectionProtocolRegistration> getAllRegistrations(Long cpId);	
}
