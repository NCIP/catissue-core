
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantInfo;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCollectionGroupInfo;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface CollectionProtocolRegistrationDao extends Dao<CollectionProtocolRegistration> {

	public List<ParticipantInfo> getParticipants(Long cpId, String searchString);

	public List<SpecimenCollectionGroupInfo> getScgList(Long cprId);

	public CollectionProtocolRegistration getCpr(Long cprId);
	
	public CollectionProtocolRegistration getCprByPpId(Long cpId, String protocolParticipantIdentifier);

	public boolean isBacodeUnique(String barcode);

	boolean isPpidUniqueForProtocol(Long cpId, String protocolParticipantIdentifier);
	
	public List<ParticipantInfo> getPhiParticipants(Long cpId, String searchString);

	public ParticipantInfo getPhiParticipant(Long cpId, Long participantId);

	public ParticipantInfo getParticipant(Long cpId, Long participantId);

	public List<CollectionProtocolRegistration> getRegDetailsForParticipant(Long participantId);

	public List<CollectionProtocolRegistration> getSubRegDetailForParticipantAndCp(Long participantId,Long cpId);

}
