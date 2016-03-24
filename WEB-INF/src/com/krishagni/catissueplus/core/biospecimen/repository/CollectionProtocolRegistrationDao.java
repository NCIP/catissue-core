
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.CprSummary;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface CollectionProtocolRegistrationDao extends Dao<CollectionProtocolRegistration> {	
	public List<CprSummary> getCprList(CprListCriteria listCrit);

	public CollectionProtocolRegistration getCprByPpid(Long cpId, String ppid);
	
	public CollectionProtocolRegistration getCprByPpid(String cpTitle, String ppid);
	
	public CollectionProtocolRegistration getCprByCpShortTitleAndPpid(String cpShortTitle, String ppid);

	public CollectionProtocolRegistration getCprByCpShortTitleAndEmpi(String cpShortTitle, String empi);

	public CollectionProtocolRegistration getCprByBarcode(String barcode);

	public CollectionProtocolRegistration getCprByParticipantId(Long cpId, Long participantId);

	public Map<String, Object> getCprIds(String key, Object value);
}
