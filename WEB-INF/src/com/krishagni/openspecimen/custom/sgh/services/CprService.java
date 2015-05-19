package com.krishagni.openspecimen.custom.sgh.services;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.openspecimen.custom.sgh.events.BulkParticipantRegDetail;
import com.krishagni.openspecimen.custom.sgh.events.BulkParticipantRegSummary;


public interface CprService {
	public ResponseEvent<BulkParticipantRegDetail> registerParticipants(RequestEvent<BulkParticipantRegSummary> req);
}

