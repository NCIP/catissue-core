package com.krishagni.openspecimen.rde.services.impl;

import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.openspecimen.rde.domain.SpecimenCollectionSession;
import com.krishagni.openspecimen.rde.domain.factory.SpecimenCollSessionErrorCode;
import com.krishagni.openspecimen.rde.events.SpecimenCollSessionDetail;
import com.krishagni.openspecimen.rde.repository.SpecimenCollSessionDao;
import com.krishagni.openspecimen.rde.services.SpecimenCollSessionService;

public class SpecimenCollSessionServiceImpl implements SpecimenCollSessionService {
	private SpecimenCollSessionDao specimenCollSessionDao;
	
	public void setSpecimenCollSessionDao(SpecimenCollSessionDao specimenCollSessionDao) {
		this.specimenCollSessionDao = specimenCollSessionDao;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenCollSessionDetail>> getSessions() {
		User user = AuthUtil.getCurrentUser().isAdmin() ? null : AuthUtil.getCurrentUser(); 
		List<SpecimenCollectionSession> sessions = specimenCollSessionDao.getSessions(user);
		return ResponseEvent.response(SpecimenCollSessionDetail.from(sessions));
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenCollSessionDetail> createSession(RequestEvent<SpecimenCollSessionDetail> req) {
		SpecimenCollSessionDetail detail = req.getPayload();
		SpecimenCollectionSession session = new SpecimenCollectionSession();
		session.setId(detail.getId());
		session.setUser(AuthUtil.getCurrentUser());
		session.setCreatedOn(new Date());
		session.setData(detail.getData());

		specimenCollSessionDao.saveOrUpdate(session);
		return ResponseEvent.response(SpecimenCollSessionDetail.from(session));
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenCollSessionDetail> updateSession(RequestEvent<SpecimenCollSessionDetail> req) {
		SpecimenCollSessionDetail detail = req.getPayload();
		SpecimenCollectionSession session = specimenCollSessionDao.getById(detail.getId());
		if (session == null) {
			return ResponseEvent.userError(SpecimenCollSessionErrorCode.NOT_FOUND);
		}
		
		ensureAccessAllowed(session);
		session.setData(detail.getData());
		specimenCollSessionDao.saveOrUpdate(session);
		return ResponseEvent.response(SpecimenCollSessionDetail.from(session));
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> deleteSession(RequestEvent<Long> req) {
		Long sessionId = req.getPayload();
		SpecimenCollectionSession session = specimenCollSessionDao.getById(sessionId);
		if (session == null) {
			return ResponseEvent.userError(SpecimenCollSessionErrorCode.NOT_FOUND);
		}
		
		ensureAccessAllowed(session);
		specimenCollSessionDao.delete(session);
		return ResponseEvent.response(true);
	}

	private void ensureAccessAllowed(SpecimenCollectionSession session) {
		if (!AuthUtil.getCurrentUser().isAdmin() && !session.getUser().equals(AuthUtil.getCurrentUser())) {
			throw OpenSpecimenException.userError(SpecimenCollSessionErrorCode.ACCESS_NOT_ALLOWED);
		}
	}

}
