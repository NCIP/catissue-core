package com.krishagni.openspecimen.custom.le.services;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.openspecimen.custom.le.events.BulkParticipantRegDetail;

public interface CprService {
	public ResponseEvent<BulkParticipantRegDetail> registerParticipants(RequestEvent<BulkParticipantRegDetail> req);
}
