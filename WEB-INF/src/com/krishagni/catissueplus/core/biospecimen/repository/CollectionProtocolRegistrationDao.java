
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.CprSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantSummary;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCollectionGroupInfo;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface CollectionProtocolRegistrationDao extends Dao<CollectionProtocolRegistration> {	
	public List<CprSummary> getCprList(CprListCriteria listCrit);

	public List<SpecimenCollectionGroupInfo> getScgList(Long cprId);

	public CollectionProtocolRegistration getCpr(Long cprId);
	
	public CollectionProtocolRegistration getCprByPpId(Long cpId, String protocolParticipantIdentifier);

	public CollectionProtocolRegistration getCprByBarcode(String barcode);

	public List<ParticipantSummary> getPhiParticipants(Long cpId, String searchString);

	public ParticipantSummary getPhiParticipant(Long cpId, Long participantId);

	public ParticipantSummary getParticipant(Long cpId, Long participantId);

	public List<CollectionProtocolRegistration> getSubRegDetailForParticipantAndCp(Long participantId,Long cpId);
}
