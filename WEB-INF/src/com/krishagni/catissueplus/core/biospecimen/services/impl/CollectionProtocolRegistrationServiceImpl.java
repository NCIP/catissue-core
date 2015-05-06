
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantRegistrationsList;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimensQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class CollectionProtocolRegistrationServiceImpl implements CollectionProtocolRegistrationService {
	private DaoFactory daoFactory;

	private CollectionProtocolRegistrationFactory cprFactory;
	
	private ParticipantService participantService;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setCprFactory(CollectionProtocolRegistrationFactory cprFactory) {
		this.cprFactory = cprFactory;
	}
	
	public void setParticipantService(ParticipantService participantService) {
		this.participantService = participantService;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolRegistrationDetail> getRegistration(RequestEvent<RegistrationQueryCriteria> req) {				
		try {			
			RegistrationQueryCriteria crit = req.getPayload();
			CollectionProtocolRegistration cpr = null;
			
			if (crit.getCprId() != null) {
				cpr = daoFactory.getCprDao().getById(crit.getCprId());
			} else if (crit.getCpId() != null && crit.getPpid() != null) {
				cpr = daoFactory.getCprDao().getCprByPpid(crit.getCpId(), crit.getPpid());
			} 
			
			if (cpr == null) {
				return ResponseEvent.userError(CprErrorCode.NOT_FOUND);
			}
			
			boolean allowPhiAccess = AccessCtrlMgr.getInstance().ensureReadCprRights(cpr);
			return ResponseEvent.response(CollectionProtocolRegistrationDetail.from(cpr, !allowPhiAccess));
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
			return ResponseEvent.response(createRegistration(req.getPayload(), true));
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
				existing = daoFactory.getCprDao().getCprByPpid(detail.getCpId(), detail.getPpid());
			}
			
			if (existing == null) {
				return ResponseEvent.userError(CprErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureUpdateCprRights(existing);
			
			CollectionProtocolRegistration cpr = cprFactory.createCpr(detail);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniquePpid(existing, cpr, ose);
			ensureUniqueBarcode(existing, cpr, ose);
			ose.checkAndThrow();
			
			saveParticipant(existing, cpr);
			existing.update(cpr);
			cpr.setPpidIfEmpty();
			
			daoFactory.getCprDao().saveOrUpdate(existing);
			return ResponseEvent.response(CollectionProtocolRegistrationDetail.from(existing, false));
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
			Long cprId = req.getPayload().cprId();
			CollectionProtocolRegistration cpr = daoFactory.getCprDao().getById(cprId);
			if (cpr == null) {
				return ResponseEvent.userError(CprErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureReadVisitRights(cpr);
			return ResponseEvent.response(daoFactory.getVisitsDao().getVisits(req.getPayload()));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenDetail>> getSpecimens(RequestEvent<VisitSpecimensQueryCriteria> req) {
		VisitSpecimensQueryCriteria crit = req.getPayload();
		
		try {
			Long cprId = req.getPayload().getCprId();
			CollectionProtocolRegistration cpr = daoFactory.getCprDao().getById(cprId);
			if (cpr == null) {
				return ResponseEvent.userError(CprErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureReadSpecimenRights(cpr);

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
	public ResponseEvent<ParticipantRegistrationsList> createRegistrations(RequestEvent<ParticipantRegistrationsList> req) {
		try {
			ParticipantRegistrationsList input = req.getPayload();
						
			//
			// Step 1: Save or update participant
			//
			ParticipantDetail participantDetail = participantService.saveOrUpdateParticipant(input.getParticipant());
			ParticipantDetail p = new ParticipantDetail();
			p.setId(participantDetail.getId());
			
			//
			// Step 2: Run through each registration
			//
			List<CollectionProtocolRegistrationDetail> registrations = new ArrayList<CollectionProtocolRegistrationDetail>();
			for (CollectionProtocolRegistrationDetail cprDetail : input.getRegistrations()) {
				cprDetail.setParticipant(p);
				cprDetail = createRegistration(cprDetail, false);
				cprDetail.setParticipant(null);
				registrations.add(cprDetail);
			}
			
			ParticipantRegistrationsList result = new ParticipantRegistrationsList();
			result.setParticipant(participantDetail);
			result.setRegistrations(registrations);			
			return ResponseEvent.response(result);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private CollectionProtocolRegistrationDetail createRegistration(CollectionProtocolRegistrationDetail input, boolean saveParticipant) {
		CollectionProtocolRegistration cpr = cprFactory.createCpr(input);
		AccessCtrlMgr.getInstance().ensureCreateCprRights(cpr);
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		ensureUniqueParticipantReg(cpr, ose);
		ensureUniquePpid(null, cpr, ose);
		ensureUniqueBarcode(null, cpr, ose);
		ose.checkAndThrow();
		
		if (saveParticipant) {
			saveParticipant(null, cpr);
		}		
		cpr.setPpidIfEmpty();
		daoFactory.getCprDao().saveOrUpdate(cpr);
		return CollectionProtocolRegistrationDetail.from(cpr, false);		
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
		
		if (daoFactory.getCprDao().getCprByParticipantId(cpId, participantId) != null) {
			ose.addError(CprErrorCode.DUP_REGISTRATION);
		}
	}

	private void ensureUniquePpid(CollectionProtocolRegistration existing, CollectionProtocolRegistration cpr, OpenSpecimenException ose) {
		if (existing != null && existing.getPpid().equals(cpr.getPpid())) { // ppid has not changed
			return;
		}
		
		Long cpId = cpr.getCollectionProtocol().getId();
		String ppid = cpr.getPpid();		
		if (daoFactory.getCprDao().getCprByPpid(cpId, ppid) != null) {
			ose.addError(CprErrorCode.DUP_PPID, ppid);
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
