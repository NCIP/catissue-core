
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.ParticipantInfo;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCollectionGroupInfo;
import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;

public interface CollectionProtocolRegistrationDao extends Dao<CollectionProtocolRegistration> {

	public List<ParticipantInfo> getParticipants(Long cpId, String searchString);
	
	public List<SpecimenCollectionGroupInfo> getScgList(Long cprId);

	public void deleteByParticipant(Long participantId);

	public void deleteByRegistration(Long registrationId);

	public void delete(CollectionProtocolRegistration registration);

	public boolean checkActiveChildren(long id);

	public boolean checkActiveChildrenForParticipant(long id);

	public CollectionProtocolRegistration getCpr(Long cprId);

}
