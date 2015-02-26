
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
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

	//TODO: Handle privileges
	private DaoFactory daoFactory;

	/**
	 * Participant factory to create/update and perform all validations on participant details 
	 */
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
		Long participantId = req.getPayload();

		Participant participant = daoFactory.getParticipantDao().getById(participantId);
		if (participant == null) {
			return ResponseEvent.userError(ParticipantErrorCode.NOT_FOUND);
		}
		
		return ResponseEvent.response(ParticipantDetail.from(participant));
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ParticipantDetail> createParticipant(RequestEvent<ParticipantDetail> req) {
		try {
			Participant participant = participantFactory.createParticipant(req.getPayload());
			createParticipant(participant);
			return ResponseEvent.response(ParticipantDetail.from(participant));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	public void createParticipant(Participant participant) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		ensureUniqueSsn(participant.getSocialSecurityNumber(), ose);
		ensureUniquePMI(participant.getPmiCollection(), ose);
		
		ose.checkAndThrow();
		daoFactory.getParticipantDao().saveOrUpdate(participant);
	}
	
	public void updateParticipant(Participant existing, Participant newParticipant) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		String existingSsn = existing.getSocialSecurityNumber();
		String newSsn = newParticipant.getSocialSecurityNumber();
		if (StringUtils.isNotBlank(newSsn) && !newSsn.equals(existingSsn)) {
			ensureUniqueSsn(newSsn, ose);
		}
		
		List<PmiDetail> pmis = PmiDetail.from(newParticipant.getPmiCollection().values());
		List<Long> participantIds = daoFactory.getParticipantDao().getParticipantIdsByPmis(pmis);
		for (Long participantId : participantIds) {
			if (!participantId.equals(existing.getId())) {
				ose.addError(ParticipantErrorCode.DUP_MRN);
				break;
			}
		}
		
		ose.checkAndThrow();
		
		existing.update(newParticipant);
		daoFactory.getParticipantDao().saveOrUpdate(existing, true);			
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
			validateSsn(existing.getSocialSecurityNumber(), participant.getSocialSecurityNumber(), ose);
			checkForUniquePMI(existing.getPmiCollection(), participant.getPmiCollection(), ose);
			ose.checkAndThrow();

			existing.update(participant);
			daoFactory.getParticipantDao().saveOrUpdate(existing);
			return ResponseEvent.response(ParticipantDetail.from(existing));
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
			
			participant.delete(true);
			daoFactory.getParticipantDao().saveOrUpdate(participant);
			return ResponseEvent.response(ParticipantDetail.from(participant));
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
	
	private void validateSsn(String oldSsn, String newSsn, OpenSpecimenException ose) {
		if (StringUtils.isBlank(oldSsn) && !StringUtils.isBlank(newSsn)) {
			ensureUniqueSsn(newSsn, ose);
		} else if (!StringUtils.isBlank(oldSsn) && !StringUtils.isBlank(newSsn) && !oldSsn.equals(newSsn)) {
			ensureUniqueSsn(newSsn, ose);
		}
	}

	private void ensureUniqueSsn(String ssn, OpenSpecimenException ose) {
		if (!daoFactory.getParticipantDao().isSsnUnique(ssn)) {
			ose.addError(ParticipantErrorCode.DUP_SSN);
		}
	}

	private void ensureUniquePMI(Map<String, ParticipantMedicalIdentifier> pmiCollection, OpenSpecimenException ose) {
		//TODO: need to handle for update
		for (Entry<String, ParticipantMedicalIdentifier> entry : pmiCollection.entrySet()) {
			String siteName = entry.getKey();
			String mrn = entry.getValue().getMedicalRecordNumber();
			if (!daoFactory.getParticipantDao().isPmiUnique(siteName, mrn)) {
				ose.addError(ParticipantErrorCode.DUP_MRN);
			}

		}
	}
	
	private void checkForUniquePMI(Map<String, ParticipantMedicalIdentifier> oldPmiCollection,
					Map<String, ParticipantMedicalIdentifier> newPmiCollection, OpenSpecimenException ose) {
		Map<String,ParticipantMedicalIdentifier> map = new HashMap<String, ParticipantMedicalIdentifier>();
					for (Entry<String, ParticipantMedicalIdentifier> entry : newPmiCollection.entrySet()) {
						{
							if(!oldPmiCollection.containsKey(entry.getKey())){
										map.put(entry.getKey(), entry.getValue());
							}
							else{
								ParticipantMedicalIdentifier pmi = oldPmiCollection.get(entry.getKey());
								pmi.setMedicalRecordNumber(entry.getValue().getMedicalRecordNumber());
								entry.setValue(pmi);
//								entry.getValue().setId(oldPmiCollection.get(entry.getKey()).getId());
							}
						}
					}
					if (!map.isEmpty()) {
						ensureUniquePMI(map, ose);
				}
			}

}
