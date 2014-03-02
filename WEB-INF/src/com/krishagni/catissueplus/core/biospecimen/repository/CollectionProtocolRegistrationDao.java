
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCollectionGroupInfo;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface CollectionProtocolRegistrationDao extends Dao<CollectionProtocolRegistration> {

	public List<SpecimenCollectionGroupInfo> getSpecimenCollectiongroupsList(Long cprId);

	public void delete(Long participantId);
}
