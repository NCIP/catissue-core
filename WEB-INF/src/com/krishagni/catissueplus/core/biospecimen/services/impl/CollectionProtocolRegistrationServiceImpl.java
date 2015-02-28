
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CprErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitFactory;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantRegistrationsList;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimensQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class CollectionProtocolRegistrationServiceImpl implements CollectionProtocolRegistrationService {
	private DaoFactory daoFactory;

	private CollectionProtocolRegistrationFactory cprFactory;
	
	private VisitFactory visitFactory;
	
	private ParticipantService participantService;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setCprFactory(CollectionProtocolRegistrationFactory cprFactory) {
		this.cprFactory = cprFactory;
	}
	
	public void setVisitFactory(VisitFactory visitFactory) {
		this.visitFactory = visitFactory;
	}

	public void setParticipantService(ParticipantService participantService) {
		this.participantService = participantService;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolRegistrationDetail> getRegistration(RequestEvent<RegistrationQueryCriteria> req) {				
		try {			
			RegistrationQueryCriteria crit = req.getPayload();
			CollectionProtocolRegistrationDetail cpr = null;
			
			if (crit.getCprId() != null) {
				cpr = getByCprId(crit.getCprId());
			} else if (crit.getCpId() != null && crit.getPpid() != null) {
				cpr = getByCpIdAndPpid(crit.getCpId(), crit.getPpid());
			} 
			
			return ResponseEvent.response(cpr);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolRegistrationDetail> createRegistration(RequestEvent<CollectionProtocolRegistrationDetail> req) {
		try {
			CollectionProtocolRegistrationDetail cprDetail = req.getPayload();
			CollectionProtocolRegistration cpr = cprFactory.createCpr(cprDetail);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			
			ensureUniqueParticipantReg(cpr, ose);
			ensureUniquePpid(null, cpr, ose);
			ensureUniqueBarcode(null, cpr, ose);
			ose.checkAndThrow();
			
			saveParticipant(null, cpr);
			daoFactory.getCprDao().saveOrUpdate(cpr);
			return ResponseEvent.response(CollectionProtocolRegistrationDetail.from(cpr));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolRegistrationDetail> updateRegistration(RequestEvent<CollectionProtocolRegistrationDetail> req) {
		try {
			CollectionProtocolRegistrationDetail detail = req.getPayload();
			
			CollectionProtocolRegistration existing = null;
			if (detail.getId() != null) {
				existing = daoFactory.getCprDao().getById(detail.getId());
			} else if (detail.getCpId() != null && StringUtils.isNotBlank(detail.getPpid())) {
				existing = daoFactory.getCprDao().getCprByPpId(detail.getCpId(), detail.getPpid());
			}
			
			if (existing == null) {
				return ResponseEvent.userError(CprErrorCode.NOT_FOUND);
			}
			
			CollectionProtocolRegistration cpr = cprFactory.createCpr(detail);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniquePpid(existing, cpr, ose);
			ensureUniqueBarcode(existing, cpr, ose);
			ose.checkAndThrow();
			
			saveParticipant(existing, cpr);
			existing.update(cpr);
			
			daoFactory.getCprDao().saveOrUpdate(existing);
			return ResponseEvent.response(CollectionProtocolRegistrationDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<VisitSummary>> getVisits(RequestEvent<VisitsListCriteria> req) {
		try {
			return ResponseEvent.response(daoFactory.getVisitsDao().getVisits(req.getPayload()));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenDetail>> getSpecimens(RequestEvent<VisitSpecimensQueryCriteria> req) {
		VisitSpecimensQueryCriteria crit = req.getPayload();
		
		try {
			List<SpecimenDetail> specimens = Collections.emptyList();			
			if (crit.getVisitId() != null) {
				specimens = getSpecimensByVisit(crit.getCprId(), crit.getVisitId());
			} else if (crit.getEventId() != null) {
				specimens = getAnticipatedSpecimens(crit.getCprId(), crit.getEventId());
			}
			
			return ResponseEvent.response(specimens);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<VisitDetail> addVisit(RequestEvent<VisitDetail> req) {
		try { // TODO: visit name
			Visit visit = visitFactory.createVisit(req.getPayload()); 
			visit.setName(UUID.randomUUID().toString()); 

			daoFactory.getVisitsDao().saveOrUpdate(visit); 
			return ResponseEvent.response(VisitDetail.from(visit));			
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);			
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
		
	
	@Override
	@PlusTransactional
	public ResponseEvent<ParticipantRegistrationsList> createBulkRegistration(RequestEvent<ParticipantRegistrationsList> req) {
		try {
			ParticipantRegistrationsList participantRegDetails = req.getPayload();
			for (int i =0; i < participantRegDetails.getRegistrations().size(); i++) {
				CollectionProtocolRegistrationDetail cprDetail = buildCprForBulkParticipantDetails(participantRegDetails, i);
				
				ResponseEvent<CollectionProtocolRegistrationDetail> response = 
						createRegistration(new RequestEvent<CollectionProtocolRegistrationDetail>(req.getSessionDataBean(), cprDetail));
				
				if (response.isSuccessful()) {
					CollectionProtocolRegistrationDetail savedCpr = response.getPayload(); 
					participantRegDetails.setId(savedCpr.getParticipant().getId());
					participantRegDetails.getRegistrations().get(i).setId(savedCpr.getId());
				} else {
					return ResponseEvent.error(response.getError());
				}
			}
			
			return ResponseEvent.response(participantRegDetails);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void saveParticipant(CollectionProtocolRegistration existing, CollectionProtocolRegistration cpr) {		
		Participant existingParticipant = null;
		Participant participant = cpr.getParticipant();
		
		if (existing == null) { 
			// new registration
			if (participant.getId() != null) { 
				// existing participant
				existingParticipant = daoFactory.getParticipantDao().getById(participant.getId());
			} else {
				// new participant
			}
		} else { 
			// existing reg, therefore it has to be existing participant
			existingParticipant = existing.getParticipant();
		}
		
		if (existingParticipant != null) {
			participantService.updateParticipant(existingParticipant, participant);
			cpr.setParticipant(existingParticipant);
		} else {
			participantService.createParticipant(participant);
			cpr.setParticipant(participant);
		}
	}
	
	//
	// Checks whether same participant is registered for same protocol already
	//
	private void ensureUniqueParticipantReg(CollectionProtocolRegistration cpr, OpenSpecimenException ose) {
		if (cpr.getParticipant() == null || cpr.getParticipant().getId() == null) {
			return ;
		}
		
		Long participantId = cpr.getParticipant().getId();
		Long cpId = cpr.getCollectionProtocol().getId();
		
		if (daoFactory.getCprDao().getRegistrationId(cpId, participantId) != null) {
			ose.addError(CprErrorCode.DUP_REGISTRATION);
		}
	}

	private void ensureUniquePpid(CollectionProtocolRegistration existing, CollectionProtocolRegistration cpr, OpenSpecimenException ose) {
		if (existing != null && existing.getPpid().equals(cpr.getPpid())) { // ppid has not changed
			return;
		}
		
		Long cpId = cpr.getCollectionProtocol().getId();
		String ppid = cpr.getPpid();		
		if (daoFactory.getCprDao().getCprByPpId(cpId, ppid) != null) {
			ose.addError(CprErrorCode.DUP_PPID);
		}
	}

	private void ensureUniqueBarcode(CollectionProtocolRegistration existing, CollectionProtocolRegistration cpr, OpenSpecimenException ose) {
		if (existing != null && 
			StringUtils.isNotBlank(existing.getBarcode()) && 
			existing.getBarcode().equals(cpr.getBarcode())) { // barcode has not changed
			return;
		}
		
		if (StringUtils.isBlank(cpr.getBarcode())) {
			return;
		}
		
		if (daoFactory.getCprDao().getCprByBarcode(cpr.getBarcode()) != null) {
			ose.addError(CprErrorCode.DUP_BARCODE);
		}
	}

	private CollectionProtocolRegistrationDetail buildCprForBulkParticipantDetails(ParticipantRegistrationsList participantRegList, int idx) {
		CollectionProtocolRegistrationDetail result = new CollectionProtocolRegistrationDetail();
		
		ParticipantDetail participant = (ParticipantDetail)participantRegList;		
		result.setParticipant(participant);
		
		CollectionProtocolRegistrationDetail cpr = participantRegList.getRegistrations().get(idx);
		result.setCpId(cpr.getCpId());
		result.setCpTitle(cpr.getCpTitle());
		result.setId(cpr.getId());
		result.setPpid(cpr.getPpid());
		result.setRegistrationDate(cpr.getRegistrationDate());
		result.setConsentDetails(cpr.getConsentDetails());
		
		return result;
	}
	
	private CollectionProtocolRegistrationDetail getByCprId(Long cprId) {
		CollectionProtocolRegistration cpr = daoFactory.getCprDao().getById(cprId);
		if (cpr == null) {
			throw OpenSpecimenException.userError(CprErrorCode.NOT_FOUND);
		}
		
		return CollectionProtocolRegistrationDetail.from(cpr);
	}
	
	private CollectionProtocolRegistrationDetail getByCpIdAndPpid(Long cpId, String ppid) {
		CollectionProtocolRegistration cpr = daoFactory.getCprDao().getCprByPpId(cpId, ppid);
		if (cpr == null) {
			throw OpenSpecimenException.userError(CprErrorCode.INVALID_CP_AND_PPID);
		}
		
		return CollectionProtocolRegistrationDetail.from(cpr);
	}
	
	private List<SpecimenDetail> getSpecimensByVisit(Long cprId, Long visitId) {
		Visit visit = daoFactory.getVisitsDao().getById(visitId);
		if (visit == null) {
			throw OpenSpecimenException.userError(VisitErrorCode.NOT_FOUND);
		}
		
		Set<SpecimenRequirement> anticipatedSpecimens = visit.getCpEvent().getTopLevelAnticipatedSpecimens();
		Set<Specimen> specimens = visit.getTopLevelSpecimens();

		return SpecimenDetail.getSpecimens(anticipatedSpecimens, specimens);
	}
	
	private List<SpecimenDetail> getAnticipatedSpecimens(Long cprId, Long eventId) {
		CollectionProtocolEvent cpe = daoFactory.getCollectionProtocolDao().getCpe(eventId);
		if (cpe == null) {
			throw OpenSpecimenException.userError(CpeErrorCode.NOT_FOUND);
		}
		
		Set<SpecimenRequirement> anticipatedSpecimens = cpe.getTopLevelAnticipatedSpecimens();
		return SpecimenDetail.getSpecimens(anticipatedSpecimens, Collections.<Specimen>emptySet());		
	}	
}
