package com.krishagni.openspecimen.rde.services;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.openspecimen.rde.events.SessionDetail;

public interface SessionService {
	public ResponseEvent<List<SessionDetail>> getSessions();

	public ResponseEvent<String> generateUid();

	public ResponseEvent<SessionDetail> getSession(RequestEvent<Long> req);

	public ResponseEvent<SessionDetail> getSessionByUid(RequestEvent<String> req);
	
	public ResponseEvent<SessionDetail> createSession(RequestEvent<SessionDetail> req);
	
	public ResponseEvent<SessionDetail> updateSession(RequestEvent<SessionDetail> req);
	
	public ResponseEvent<Boolean> deleteSession(RequestEvent<Long> req);
}
