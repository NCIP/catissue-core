package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;

public class CollectionProtocolDetail extends CollectionProtocolSummary {
	
	public static CollectionProtocolDetail from(CollectionProtocol cp) {
		CollectionProtocolDetail result = new CollectionProtocolDetail();
		CollectionProtocolSummary.copy(cp, result);
		return result;
	}
}
