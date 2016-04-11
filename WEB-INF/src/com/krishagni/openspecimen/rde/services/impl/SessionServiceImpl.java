package com.krishagni.openspecimen.rde.services.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.openspecimen.rde.domain.Session;
import com.krishagni.openspecimen.rde.domain.SessionError;
import com.krishagni.openspecimen.rde.events.SessionDetail;
import com.krishagni.openspecimen.rde.repository.SessionDao;
import com.krishagni.openspecimen.rde.services.SessionService;

public class SessionServiceImpl implements SessionService {
	private SessionDao sessionDao;
	
	public void setSessionDao(SessionDao sessionDao) {
		this.sessionDao = sessionDao;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<SessionDetail>> getSessions() {
		User user = AuthUtil.getCurrentUser().isAdmin() ? null : AuthUtil.getCurrentUser(); 
		List<Session> sessions = sessionDao.getSessions(user);
		return ResponseEvent.response(SessionDetail.from(sessions));
	}

	@Override
	@PlusTransactional
	public ResponseEvent<String> generateUid() {
		return ResponseEvent.response(UUID.randomUUID().toString());
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SessionDetail> getSession(RequestEvent<Long> req) {
		try {
			Session session = sessionDao.getById(req.getPayload());
			if (session == null) {
				return ResponseEvent.userError(SessionError.NOT_FOUND, req.getPayload());
			}

			ensureAccessAllowed(session);
			return ResponseEvent.response(SessionDetail.from(session));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SessionDetail> getSessionByUid(RequestEvent<String> req) {
		try {
			Session session = sessionDao.getByUid(req.getPayload());
			if (session == null) {
				return ResponseEvent.userError(SessionError.NOT_FOUND, req.getPayload());
			}

			ensureAccessAllowed(session);
			return ResponseEvent.response(SessionDetail.from(session));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SessionDetail> createSession(RequestEvent<SessionDetail> req) {
		try {
			SessionDetail detail = req.getPayload();
			Session session = new Session();
			session.setId(detail.getId());
			session.setUid(detail.getUid());
			session.setUser(AuthUtil.getCurrentUser());
			session.setCreatedOn(new Date());
			session.setData(detail.getData());
			sessionDao.saveOrUpdate(session);
			return ResponseEvent.response(SessionDetail.from(session));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SessionDetail> updateSession(RequestEvent<SessionDetail> req) {
		try {
			SessionDetail detail = req.getPayload();

			Session session = sessionDao.getById(detail.getId());
			if (session == null) {
				return createSession(req);
			}

			ensureAccessAllowed(session);
			session.setData(detail.getData());
			sessionDao.saveOrUpdate(session);
			return ResponseEvent.response(SessionDetail.from(session));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> deleteSession(RequestEvent<Long> req) {
		try {
			Long sessionId = req.getPayload();
			Session session = sessionDao.getById(sessionId);
			if (session == null) {
				return ResponseEvent.userError(SessionError.NOT_FOUND, sessionId);
			}

			ensureAccessAllowed(session);
			sessionDao.delete(session);
			return ResponseEvent.response(true);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private void ensureAccessAllowed(Session session) {
		if (!AuthUtil.isAdmin() && !session.getUser().equals(AuthUtil.getCurrentUser())) {
			throw OpenSpecimenException.userError(SessionError.ACCESS_NOT_ALLOWED, session.getId());
		}
	}
}
