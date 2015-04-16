
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantUtil;
import com.krishagni.catissueplus.core.biospecimen.events.MatchedParticipants;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.PmiDetail;
import com.krishagni.catissueplus.core.biospecimen.matching.ParticipantLookupLogic;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ParticipantServiceImpl implements ParticipantService {
	private DaoFactory daoFactory;

	private ParticipantFactory participantFactory;
	
	private ParticipantLookupLogic participantLookupLogic;

	
	public void setParticipantLookupLogic(ParticipantLookupLogic participantLookupLogic) {
		this.participantLookupLogic = participantLookupLogic;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setParticipantFactory(ParticipantFactory participantFactory) {
		this.participantFactory = participantFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ParticipantDetail> getParticipant(RequestEvent<Long> req) {
		Participant participant = daoFactory.getParticipantDao().getById(req.getPayload());
		if (participant == null) {
			return ResponseEvent.userError(ParticipantErrorCode.NOT_FOUND);
		}
		
		return ResponseEvent.response(ParticipantDetail.from(participant, false));
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ParticipantDetail> createParticipant(RequestEvent<ParticipantDetail> req) {
		try {
			Participant participant = participantFactory.createParticipant(req.getPayload());
			createParticipant(participant);
			return ResponseEvent.response(ParticipantDetail.from(participant, false));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ParticipantDetail> updateParticipant(RequestEvent<ParticipantDetail> req) {
		try {
			Long participantId = req.getPayload().getId();
			Participant existing = daoFactory.getParticipantDao().getById(participantId);
			if (existing == null) {
				return ResponseEvent.userError(ParticipantErrorCode.NOT_FOUND);
			}
			
			Participant participant = participantFactory.createParticipant(req.getPayload());
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			
			String newSsn = participant.getSocialSecurityNumber();
			if (StringUtils.isNotBlank(newSsn) && !newSsn.equals(existing.getSocialSecurityNumber())) {
				ParticipantUtil.ensureUniqueSsn(daoFactory, newSsn, ose);
			}
			
			ParticipantUtil.ensureUniquePmis(
					daoFactory, 
					req.getPayload().getPmis(), 
					participant, 
					ose);
			
			ose.checkAndThrow();

			existing.update(participant);
			daoFactory.getParticipantDao().saveOrUpdate(existing);
			return ResponseEvent.response(ParticipantDetail.from(existing, false));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ParticipantDetail> delete(RequestEvent<Long> req) {
		try {
			Long participantId = req.getPayload();
			Participant participant = daoFactory.getParticipantDao().getById(participantId);
			if (participant == null) {
				return ResponseEvent.userError(ParticipantErrorCode.NOT_FOUND);
			}
			
			participant.delete();
			daoFactory.getParticipantDao().saveOrUpdate(participant);
			return ResponseEvent.response(ParticipantDetail.from(participant, false));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<MatchedParticipants> getMatchingParticipants(RequestEvent<ParticipantDetail> req) {
		return ResponseEvent.response(participantLookupLogic.getMatchingParticipants(req.getPayload())); 
	}
	
	public void createParticipant(Participant participant) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		ParticipantUtil.ensureUniqueSsn(daoFactory, participant.getSocialSecurityNumber(), ose);
		ParticipantUtil.ensureUniquePmis(daoFactory, PmiDetail.from(participant.getPmis(), false), participant, ose);		
		ose.checkAndThrow();
		
		daoFactory.getParticipantDao().saveOrUpdate(participant);
	}
	
	public void updateParticipant(Participant existing, Participant newParticipant) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		String existingSsn = existing.getSocialSecurityNumber();
		String newSsn = newParticipant.getSocialSecurityNumber();
		if (StringUtils.isNotBlank(newSsn) && !newSsn.equals(existingSsn)) {
			ParticipantUtil.ensureUniqueSsn(daoFactory, newSsn, ose);
		}
		
		
		List<PmiDetail> pmis = PmiDetail.from(newParticipant.getPmis(), false);
		ParticipantUtil.ensureUniquePmis(daoFactory, pmis, existing, ose);
		ose.checkAndThrow();
		
		existing.update(newParticipant);
		daoFactory.getParticipantDao().saveOrUpdate(existing, true);			
	}	
}
