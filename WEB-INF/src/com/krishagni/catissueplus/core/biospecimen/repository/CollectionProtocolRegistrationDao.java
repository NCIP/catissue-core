
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCollectionGroupInfo;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface CollectionProtocolRegistrationDao extends Dao<CollectionProtocolRegistration> {

	public List<SpecimenCollectionGroupInfo> getSpecimenCollectiongroupsList(Long cprId);

	public void deleteByParticipant(Long participantId);

	public void deleteByRegistration(Long registrationId);

	public void delete(Long id);

	public boolean checkActiveChildren(long id);

	public boolean checkActiveChildrenForParticipant(long id);

}
