package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCollectionGroupInfo;
import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;


public interface CollectionProtocolRegistrationDao extends Dao<CollectionProtocolRegistration>{

	public List<SpecimenCollectionGroupInfo> getSpecimenCollectiongroupsList(Long cprId);
	public List<CollectionProtocolRegistration> getAllRegistrations(Long cpId);	
}
