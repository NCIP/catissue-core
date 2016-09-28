
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantUtil;
import com.krishagni.catissueplus.core.biospecimen.events.MatchedParticipant;
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
import com.krishagni.catissueplus.core.common.service.MpiGenerator;

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
			ParticipantDetail detail = req.getPayload();
			Participant existing = getParticipant(detail, true);
			if (existing == null) {
				return ResponseEvent.userError(ParticipantErrorCode.NOT_FOUND);
			}

			
			Participant participant = participantFactory.createParticipant(detail);
			updateParticipant(existing, participant);			
			return ResponseEvent.response(ParticipantDetail.from(existing, false));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	public ResponseEvent<ParticipantDetail> patchParticipant(RequestEvent<ParticipantDetail> req) {
		try {
			ParticipantDetail detail = req.getPayload();
			Participant existing = getParticipant(detail, true);
			if (existing == null) {
				return ResponseEvent.userError(ParticipantErrorCode.NOT_FOUND);
			}
			
			Participant participant = participantFactory.createParticipant(existing, detail);
			updateParticipant(existing, participant);			
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
	public ResponseEvent<List<MatchedParticipant>> getMatchingParticipants(RequestEvent<ParticipantDetail> req) {
		return ResponseEvent.response(participantLookupLogic.getMatchingParticipants(req.getPayload())); 
	}
	
	public void createParticipant(Participant participant) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		ParticipantUtil.ensureUniqueUid(daoFactory, participant.getUid(), ose);
		ParticipantUtil.ensureUniquePmis(daoFactory, PmiDetail.from(participant.getPmis(), false), participant, ose);
		ParticipantUtil.ensureUniqueEmpi(daoFactory, participant.getEmpi(), ose);

		ose.checkAndThrow();

		participant.setEmpiIfEmpty();
		daoFactory.getParticipantDao().saveOrUpdate(participant, true);
		participant.addOrUpdateExtension();
	}

	public void updateParticipant(Participant existing, Participant newParticipant) {
		ParticipantUtil.ensureLockedFieldsAreUntouched(existing, newParticipant);

		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		String existingUid = existing.getUid();
		String newUid = newParticipant.getUid();
		if (StringUtils.isNotBlank(newUid) && !newUid.equals(existingUid)) {
			ParticipantUtil.ensureUniqueUid(daoFactory, newUid, ose);
		}
		
		String existingEmpi = existing.getEmpi();
		String newEmpi = newParticipant.getEmpi();
		MpiGenerator generator = ParticipantUtil.getMpiGenerator();
		if (generator != null && !existingEmpi.equals(newEmpi)){
			ose.addError(ParticipantErrorCode.MANUAL_MPI_NOT_ALLOWED);
		} else if (generator == null && StringUtils.isNotBlank(newEmpi) && !newEmpi.equals(existingEmpi)){
			ParticipantUtil.ensureUniqueEmpi(daoFactory, newEmpi, ose);
		}
		
		List<PmiDetail> pmis = PmiDetail.from(newParticipant.getPmis(), false);
		ParticipantUtil.ensureUniquePmis(daoFactory, pmis, existing, ose);
		ose.checkAndThrow();
		
		existing.update(newParticipant);
		daoFactory.getParticipantDao().saveOrUpdate(existing);
		existing.addOrUpdateExtension();
	}

	@Override
	public ParticipantDetail saveOrUpdateParticipant(ParticipantDetail detail) {
		Participant existing = getParticipant(detail, false);

		if (existing == null) {
			Participant participant = participantFactory.createParticipant(detail);
			createParticipant(participant);
			return ParticipantDetail.from(participant, false);
		} else {
			Participant participant = participantFactory.createParticipant(existing, detail);
			updateParticipant(existing, participant);
			return ParticipantDetail.from(existing, false);
		}
	}
	
	private Participant getParticipant(ParticipantDetail detail, boolean forUpdate) {
		Participant result = null;
		if (detail.getId() != null) {
			result = daoFactory.getParticipantDao().getById(detail.getId());
		} else {
			boolean blankEmpi = StringUtils.isBlank(detail.getEmpi());
			if (!blankEmpi) {
				result = daoFactory.getParticipantDao().getByEmpi(detail.getEmpi());
			}
			
			if (blankEmpi || (result == null && !forUpdate)) {
				result = getByPmis(detail);
			}
		}
		
		return result;
	}
	
	private Participant getByPmis(ParticipantDetail detail) {
		Participant result = null;
		
		if (CollectionUtils.isEmpty(detail.getPmis())) {
			return result;
		}
		
		List<Participant> participants = daoFactory.getParticipantDao().getByPmis(detail.getPmis());
		if (participants.size() > 1) {
			throw OpenSpecimenException.userError(ParticipantErrorCode.DUP_MRN);
		} else if (participants.size() == 1) {
			result = participants.iterator().next();
		}
		
		return result;
	}
}
