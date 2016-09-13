
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentResponses;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ConsentResponsesFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CprErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;
import com.krishagni.catissueplus.core.biospecimen.events.FileDetail;
import com.krishagni.catissueplus.core.biospecimen.events.MatchedParticipant;
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
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.LabelGenerator;
import com.krishagni.catissueplus.core.common.service.ObjectStateParamsResolver;
import com.krishagni.catissueplus.core.common.service.impl.ConfigurationServiceImpl;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.Status;


public class CollectionProtocolRegistrationServiceImpl implements CollectionProtocolRegistrationService, ObjectStateParamsResolver {
	private DaoFactory daoFactory;

	private CollectionProtocolRegistrationFactory cprFactory;
	
	private ConsentResponsesFactory consentResponsesFactory;
	
	private ParticipantService participantService;
	
	private ConfigurationServiceImpl cfgSvc;
	
	private LabelGenerator labelGenerator;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setCprFactory(CollectionProtocolRegistrationFactory cprFactory) {
		this.cprFactory = cprFactory;
	}
	
	public void setConsentResponsesFactory(ConsentResponsesFactory consentResponsesFactory) {
		this.consentResponsesFactory = consentResponsesFactory;
	}

	public void setParticipantService(ParticipantService participantService) {
		this.participantService = participantService;
	}
	
	public void setCfgSvc(ConfigurationServiceImpl cfgSvc) {
		this.cfgSvc = cfgSvc;
	}

	public void setLabelGenerator(LabelGenerator labelGenerator) {
		this.labelGenerator = labelGenerator;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolRegistrationDetail> getRegistration(RequestEvent<RegistrationQueryCriteria> req) {				
		try {			
			RegistrationQueryCriteria crit = req.getPayload();
			CollectionProtocolRegistration cpr = getCpr(crit.getCprId(), crit.getCpId(), crit.getPpid());
			boolean allowPhiAccess = AccessCtrlMgr.getInstance().ensureReadCprRights(cpr);

			List<CollectionProtocolRegistration> otherCprs = getOtherCprs(cpr);
			return ResponseEvent.response(CollectionProtocolRegistrationDetail.from(cpr, !allowPhiAccess, otherCprs));
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
			return ResponseEvent.response(saveOrUpdateRegistration(req.getPayload(), null, true));
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
			CollectionProtocolRegistration existing = getCpr(detail.getId(), detail.getCpId(), detail.getCpShortTitle(), detail.getPpid());
			return ResponseEvent.response(saveOrUpdateRegistration(detail, existing, true));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<RegistrationQueryCriteria> req) {
		try {
			RegistrationQueryCriteria crit = req.getPayload();
			CollectionProtocolRegistration cpr = getCpr(crit.getCprId(), crit.getCpId(), crit.getPpid());
			AccessCtrlMgr.getInstance().ensureReadCprRights(cpr);
			return ResponseEvent.response(cpr.getDependentEntities());
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolRegistrationDetail> deleteRegistration(RequestEvent<RegistrationQueryCriteria> req) {
		try {
			RegistrationQueryCriteria crit = req.getPayload();
			CollectionProtocolRegistration cpr = getCpr(crit.getCprId(), crit.getCpId(), crit.getPpid());
			AccessCtrlMgr.getInstance().ensureDeleteCprRights(cpr);
			cpr.delete();
			return ResponseEvent.response(CollectionProtocolRegistrationDetail.from(cpr, false));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}	
	
	@Override
	@PlusTransactional
	public ResponseEvent<File> getConsentForm(RequestEvent<RegistrationQueryCriteria> req) {
		try {
			Long cprId = req.getPayload().getCprId();
			CollectionProtocolRegistration existing = daoFactory.getCprDao().getById(cprId);
			if (existing == null) {
				return ResponseEvent.userError(CprErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureReadCprRights(existing);
			
			String fileName = existing.getSignedConsentDocumentName();
			if (StringUtils.isBlank(fileName)) {
				return ResponseEvent.userError(CprErrorCode.CONSENT_FORM_NOT_FOUND);
			}
			
			File file = new File(getConsentDirPath() + fileName);
			if (!file.exists()) {
				return ResponseEvent.userError(CprErrorCode.CONSENT_FORM_NOT_FOUND);
			}
			
			return ResponseEvent.response(file);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<String> uploadConsentForm(RequestEvent<FileDetail> req) {
		OutputStream outputStream = null;
		try {
			FileDetail detail = req.getPayload();
			CollectionProtocolRegistration existing = daoFactory.getCprDao().getById(detail.getId());
			if (existing == null) {
				return ResponseEvent.userError(CprErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureUpdateCprRights(existing);
			
			if (existing.getCollectionProtocol().isConsentsWaived()) {
				return ResponseEvent.userError(CpErrorCode.CONSENTS_WAIVED, existing.getCollectionProtocol().getShortTitle());
			}
			
			String newFileName = UUID.randomUUID() + "_" + detail.getFilename();
			File newFile = new File(getConsentDirPath() + newFileName);
			
			outputStream = new FileOutputStream(newFile);
			IOUtils.copy(detail.getFileIn(), outputStream);
			
			String oldFileName = existing.getSignedConsentDocumentName();
			if (StringUtils.isNotBlank(oldFileName)) {
				File oldFile = new File(getConsentDirPath() + oldFileName);
				if (oldFile.exists()) {
					oldFile.delete();
				}
 			}
			existing.setSignedConsentDocumentName(newFileName);
			return ResponseEvent.response(detail.getFilename());
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> deleteConsentForm(RequestEvent<RegistrationQueryCriteria> req) {
		try {
			Long cprId = req.getPayload().getCprId();
			CollectionProtocolRegistration cpr = daoFactory.getCprDao().getById(cprId);
			if (cpr == null) {
				return ResponseEvent.userError(CprErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureUpdateCprRights(cpr);

			String fileName = cpr.getSignedConsentDocumentName();
			if (StringUtils.isBlank(fileName)) {
				return ResponseEvent.userError(CprErrorCode.CONSENT_FORM_NOT_FOUND);
			}
			
			File file = new File(getConsentDirPath() + fileName);
			if (!file.exists()) {
				return ResponseEvent.userError(CprErrorCode.CONSENT_FORM_NOT_FOUND);
			}
			
			boolean isFileDeleted = file.delete();
			if (isFileDeleted) {
				cpr.setSignedConsentDocumentName(null);
			} 

			return ResponseEvent.response(isFileDeleted);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ConsentDetail> getConsents(RequestEvent<RegistrationQueryCriteria> req) {
		try {
			RegistrationQueryCriteria crit = req.getPayload();
			CollectionProtocolRegistration cpr = getCpr(crit.getCprId(), crit.getCpId(), crit.getPpid());
			AccessCtrlMgr.getInstance().ensureReadCprRights(cpr);
			return ResponseEvent.response(ConsentDetail.fromCpr(cpr));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ConsentDetail> saveConsents(RequestEvent<ConsentDetail> req) {
		try {
			ConsentDetail consentDetail = req.getPayload();

			CollectionProtocolRegistration existing = getCpr(consentDetail.getCprId(),
				consentDetail.getCpId(), consentDetail.getCpShortTitle(), consentDetail.getPpid());
			AccessCtrlMgr.getInstance().ensureUpdateCprRights(existing);
			
			ConsentResponses consentResponses = consentResponsesFactory.createConsentResponses(consentDetail);
			existing.updateConsents(consentResponses);
			return ResponseEvent.response(ConsentDetail.fromCpr(existing));
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
			CollectionProtocolRegistration cpr = getCpr(req.getPayload().cprId(), null, null);
			AccessCtrlMgr.getInstance().ensureReadVisitRights(cpr, false);
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
			CollectionProtocolRegistration cpr = getCpr(req.getPayload().getCprId(), null, null);
			AccessCtrlMgr.getInstance().ensureReadSpecimenRights(cpr, false);

			List<SpecimenDetail> specimens = Collections.emptyList();			
			if (crit.getVisitId() != null) {
				specimens = getSpecimensByVisit(crit.getCprId(), crit.getVisitId());
				checkDistributedSpecimens(specimens);
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
			return ResponseEvent.response(saveOrUpdateRegistrations(req.getPayload(), false));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ParticipantRegistrationsList> updateRegistrations(RequestEvent<ParticipantRegistrationsList> req) {
		try {
			return ResponseEvent.response(saveOrUpdateRegistrations(req.getPayload(), true));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	public String getObjectName() {
		return "cpr";
	}

	@Override
	@PlusTransactional
	public Map<String, Object> resolve(String key, Object value) {
		if (key.equals("id")) {
			value = Long.valueOf(value.toString());
		}

		return daoFactory.getCprDao().getCprIds(key, value);
	}

	private CollectionProtocolRegistrationDetail saveOrUpdateRegistration(
			CollectionProtocolRegistrationDetail input,
			CollectionProtocolRegistration existing ,
			boolean saveParticipant) {

		CollectionProtocolRegistration cpr = cprFactory.createCpr(existing, input);
		if (existing == null) {
			AccessCtrlMgr.getInstance().ensureCreateCprRights(cpr);
		} else {
			AccessCtrlMgr.getInstance().ensureUpdateCprRights(cpr);
		}
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		ensureValidAndUniquePpid(existing, cpr, ose);
		ensureUniqueBarcode(existing, cpr, ose);
		if (existing == null) {
			ensureUniqueParticipantReg(cpr, ose);
		}
		
		ose.checkAndThrow();
		
		if (saveParticipant) {
			saveParticipant(existing, cpr);
		}

		if (existing != null) {
			existing.update(cpr);
			cpr = existing;
		}
		
		cpr.setPpidIfEmpty();
		daoFactory.getCprDao().saveOrUpdate(cpr);
		return CollectionProtocolRegistrationDetail.from(cpr, false);		
	}
	
	private ParticipantRegistrationsList saveOrUpdateRegistrations(ParticipantRegistrationsList input, boolean update) {
		ParticipantDetail inputParticipant = input.getParticipant();
		if (inputParticipant == null) {
			inputParticipant = new ParticipantDetail();
		}

		if (update) {
			Participant existing = getParticipant(input);
			if (Status.isDisabledStatus(existing.getActivityStatus())) {
				return deleteParticipant(existing);
			}

			AccessCtrlMgr.getInstance().ensureUpdateParticipantRights(existing);
			inputParticipant.setId(existing.getId());
		}

		//
		// Step 1: Save/update participant
		//
		ParticipantDetail participantDetail = participantService.saveOrUpdateParticipant(inputParticipant);
		Participant participant = daoFactory.getParticipantDao().getById(participantDetail.getId());

		ParticipantDetail p = new ParticipantDetail();
		p.setId(participantDetail.getId());

		//
		// Step 2: Build a map of participant registrations
		//
		Map<String, CollectionProtocolRegistration> cprMap = participant.getCprs().stream()
			.collect(Collectors.toMap(reg -> reg.getCpShortTitle() + "_" + reg.getPpid(), reg -> reg));

		//
		// Step 3: Run through each registration
		//
		List<CollectionProtocolRegistrationDetail> registrations = new ArrayList<>();
		for (CollectionProtocolRegistrationDetail cprDetail : input.getRegistrations()) {
			cprDetail.setParticipant(p);

			CollectionProtocolRegistration existing = null;
			if (StringUtils.isNotBlank(cprDetail.getCpShortTitle()) && StringUtils.isNotBlank(cprDetail.getPpid())) {
				existing = cprMap.get(cprDetail.getCpShortTitle() + "_" + cprDetail.getPpid());
			}

			cprDetail = saveOrUpdateRegistration(cprDetail, existing, false);

			cprDetail.setParticipant(null);
			registrations.add(cprDetail);
		}

		ParticipantRegistrationsList result = new ParticipantRegistrationsList();
		result.setParticipant(participantDetail);
		result.setRegistrations(registrations);
		return result;
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
			existingParticipant = mergeParticipant(existing, cpr);
		}
		
		if (existingParticipant != null) {
			participantService.updateParticipant(existingParticipant, participant);
			cpr.setParticipant(existingParticipant);
		} else {
			participantService.createParticipant(participant);
			cpr.setParticipant(participant);
		}
	}
	
	private Participant mergeParticipant(CollectionProtocolRegistration existingCpr, CollectionProtocolRegistration inputCpr) {
		if (existingCpr.getParticipant().equals(inputCpr.getParticipant())) {
			//
			//No change in participant between registration updates. Do nothing
			//
			return existingCpr.getParticipant();
		}
		
		//
		// we’ve been asked to use participant in inputCpr for updating existingCpr
		//
		// Two broad cases:
		// Case #1: Participants are registered to non-overlapping protocols. 
		// e.g. {cp1, cp4} and {cp2, cp3}
		// In this case, we need to only point registration to appropriate participant
		//
		// Case #2: Participants are registered to overlapping protocols. 
		// e.g. {cp1, cp2, cp4} and {cp2, cp3, cp4}
		// In this case, we need to transfer visits from source participant’s overlapping CPRs 
		// to target participant’s corresponding CPRs
		//
		//
		
		Participant tgtParticipant = daoFactory.getParticipantDao().getById(inputCpr.getParticipant().getId());
		Participant srcParticipant = existingCpr.getParticipant();
		
		Set<CollectionProtocolRegistration> tgtCprs = tgtParticipant.getCprs();
		for (CollectionProtocolRegistration srcCpr : srcParticipant.getCprs()) {
			CollectionProtocolRegistration mergedCpr = mergeCpr(srcCpr, tgtCprs);
			if (mergedCpr == null) {
				// case 1
				srcCpr.setParticipant(tgtParticipant);
			} else if (mergedCpr.getCollectionProtocol().equals(existingCpr.getCollectionProtocol())){
				// case 2
				// marking input CPR should not be used
				inputCpr.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
			}
		}
		
		srcParticipant.getCprs().clear();
		if (srcParticipant.isActive()) {
			srcParticipant.delete();
		}
		
		return tgtParticipant;
	}
	
	private CollectionProtocolRegistration mergeCpr(CollectionProtocolRegistration srcCpr, Set<CollectionProtocolRegistration> tgtCprs) {
		try {
			AccessCtrlMgr.getInstance().ensureUpdateCprRights(srcCpr);
		} catch (OpenSpecimenException ose) {
			throw OpenSpecimenException.userError(CprErrorCode.CANNOT_MERGE_PARTICIPANT, srcCpr.getCollectionProtocol().getShortTitle());
		}
		
		for (CollectionProtocolRegistration tgtCpr : tgtCprs) {
			if (srcCpr.getCollectionProtocol().equals(tgtCpr.getCollectionProtocol())) {
				tgtCpr.addVisits(srcCpr.getVisits());
				srcCpr.getVisits().clear();
				srcCpr.delete();
				return tgtCpr;
			}
		}
		
		return null;
	}

	private Participant getParticipant(ParticipantRegistrationsList input) {
		for (CollectionProtocolRegistrationDetail cprDetail : input.getRegistrations()) {
			String cpShortTitle = cprDetail.getCpShortTitle();
			String ppid         = cprDetail.getPpid();

			if (StringUtils.isNotBlank(cpShortTitle) && StringUtils.isNotBlank(ppid)) {
				CollectionProtocolRegistration cpr = daoFactory.getCprDao().getCprByCpShortTitleAndPpid(cpShortTitle, ppid);
				if (cpr != null) {
					return cpr.getParticipant();
				}
			}
		}

		return daoFactory.getParticipantDao().getById(lookupParticipant(input.getParticipant()).getId());
	}

	private ParticipantDetail lookupParticipant(ParticipantDetail detail) {
		ResponseEvent<List<MatchedParticipant>> resp = participantService.getMatchingParticipants(new RequestEvent<>(detail));
		resp.throwErrorIfUnsuccessful();

		List<MatchedParticipant> result = resp.getPayload();
		if (result.isEmpty()) {
			throw OpenSpecimenException.userError(ParticipantErrorCode.NOT_FOUND);
		}

		return result.iterator().next().getParticipant();
	}

	//
	// Deletes all registrations of participant
	//
	private ParticipantRegistrationsList deleteParticipant(Participant participant) {
		List<CollectionProtocolRegistrationDetail> registrations = new ArrayList<>();
		for (CollectionProtocolRegistration cpr : participant.getCprs()) {
			AccessCtrlMgr.getInstance().ensureDeleteCprRights(cpr);
			cpr.delete();
			registrations.add(CollectionProtocolRegistrationDetail.from(cpr, true));
		}

		ParticipantRegistrationsList result = new ParticipantRegistrationsList();
		result.setParticipant(ParticipantDetail.from(participant, true));
		result.setRegistrations(registrations);
		return result;
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

	private void ensureValidAndUniquePpid(CollectionProtocolRegistration existing, CollectionProtocolRegistration cpr, OpenSpecimenException ose) {
		if (existing != null && existing.getPpid().equals(cpr.getPpid())) {
			return;
		}
		
		CollectionProtocol cp = cpr.getCollectionProtocol();

		String ppid = cpr.getPpid();
		if (StringUtils.isBlank(ppid)) {
			boolean ppidReq = cp.isManualPpidEnabled() || StringUtils.isBlank(cp.getPpidFormat());
			if (ppidReq) {
				ose.addError(CprErrorCode.PPID_REQUIRED);
			}
			
			return;
		}
		
		
		if (StringUtils.isNotBlank(cp.getPpidFormat())) {
			//
			// PPID format is specified
			//
			
			if (!cp.isManualPpidEnabled()) {
				ose.addError(CprErrorCode.MANUAL_PPID_NOT_ALLOWED);
				return;
			}
			
			if (!labelGenerator.validate(cp.getPpidFormat(), cpr, ppid)) {
				ose.addError(CprErrorCode.INVALID_PPID, ppid);
				return;
			}
		}
		
		if (daoFactory.getCprDao().getCprByPpid(cp.getId(), ppid) != null) {
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
		
		Set<SpecimenRequirement> anticipatedSpecimens = visit.isUnplanned() ? Collections.EMPTY_SET : visit.getCpEvent().getTopLevelAnticipatedSpecimens();
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
	
	private CollectionProtocolRegistration getCpr(Long cprId, Long cpId, String ppid) {
		return getCpr(cprId, cpId, null, ppid);
	}

	private CollectionProtocolRegistration getCpr(Long cprId, Long cpId, String cpShortTitle, String ppid) {
		CollectionProtocolRegistration cpr = null;
		if (cprId != null) {
			cpr = daoFactory.getCprDao().getById(cprId);
		} else if (cpId != null && StringUtils.isNotBlank(ppid)) {
			cpr = daoFactory.getCprDao().getCprByPpid(cpId, ppid);
		} else if (StringUtils.isNotBlank(cpShortTitle) && StringUtils.isNotBlank(ppid)) {
			cpr = daoFactory.getCprDao().getCprByCpShortTitleAndPpid(cpShortTitle, ppid);
		}
		
		if (cpr == null) {
			throw OpenSpecimenException.userError(CprErrorCode.NOT_FOUND);
		}
		
		return cpr;
	}

	private void checkDistributedSpecimens(List<SpecimenDetail> specimens) {
		List<Long> specimenIds = getSpecimenIds(specimens);
		if (CollectionUtils.isEmpty(specimenIds)) {
			return;
		}
		
		Map<Long, String> distStatus = daoFactory.getSpecimenDao().getDistributionStatus(specimenIds);
		setDistributedStatus(specimens, distStatus);
	}

	private void setDistributedStatus(List<SpecimenDetail> specimens, Map<Long, String> distStatus) {
		for (SpecimenDetail detail : specimens) {
			detail.setDistributionStatus(distStatus.get(detail.getId()));
			if (CollectionUtils.isNotEmpty(detail.getChildren())) {
				setDistributedStatus(detail.getChildren(), distStatus);
			}
		}
	}

	private List<Long> getSpecimenIds(List<SpecimenDetail> specimens) {
		List<Long> ids = new ArrayList<Long>();
		for (SpecimenDetail detail : specimens) {
			if (detail.getId() != null) {
				ids.add(detail.getId());
			}

			if (CollectionUtils.isNotEmpty(detail.getChildren())) {
				ids.addAll(getSpecimenIds(detail.getChildren()));
			}
		}

		return ids;
	}

	private String getConsentDirPath() {
		String path = cfgSvc.getStrSetting(ConfigParams.MODULE, "participant_consent_dir");
		if (path == null) {
			path = ConfigUtil.getInstance().getDataDir() + File.separator + "participant-consents";
		}
		
		return path + File.separator;
	}
	
	private List<CollectionProtocolRegistration> getOtherCprs(CollectionProtocolRegistration cpr) {
		Participant participant = cpr.getParticipant();
		if (participant.getCprs().size() < 2) {
			return Collections.emptyList();
		}

		return participant.getCprs().stream()
			.filter(otherCpr -> {
				if (otherCpr.equals(cpr)) {
					return true;
				}

				try {
					AccessCtrlMgr.getInstance().ensureReadCprRights(otherCpr);
					return true;
				} catch (OpenSpecimenException e) {
					return false;
				}
			})
			.collect(Collectors.toList());
	}
}
