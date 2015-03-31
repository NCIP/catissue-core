
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.CprSummary;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface CollectionProtocolRegistrationDao extends Dao<CollectionProtocolRegistration> {	
	public List<CprSummary> getCprList(CprListCriteria listCrit);

	public CollectionProtocolRegistration getCprByPpId(Long cpId, String protocolParticipantIdentifier);

	public CollectionProtocolRegistration getCprByBarcode(String barcode);

	public CollectionProtocolRegistration getCprByParticipantId(Long cpId, Long participantId);
}
