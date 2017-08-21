package com.krishagni.catissueplus.core.support.services;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.support.events.FeedbackDetail;

public interface SupportService {
	public ResponseEvent<Boolean> sendFeedback(RequestEvent<FeedbackDetail> req);
}
