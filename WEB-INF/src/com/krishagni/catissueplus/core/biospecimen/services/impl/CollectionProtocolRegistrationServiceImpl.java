
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitFactory;
import com.krishagni.catissueplus.core.biospecimen.events.AddVisitEvent;
import com.krishagni.catissueplus.core.biospecimen.events.BulkRegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CprRegistrationDetails;
import com.krishagni.catissueplus.core.biospecimen.events.CreateBulkRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantRegistrationDetails;
import com.krishagni.catissueplus.core.biospecimen.events.PatchRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqVisitSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqVisitsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenSummary;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitAddedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitsEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.privileges.services.PrivilegeService;

import edu.wustl.security.global.Permissions;

@Service(value = "CollectionProtocolRegistrationServiceImpl")
public class CollectionProtocolRegistrationServiceImpl implements CollectionProtocolRegistrationService {

	private final String PPID = "participant protocol identifier";

	private final String BARCODE = "barcode";

	private DaoFactory daoFactory;

	private CollectionProtocolRegistrationFactory registrationFactory;
	
	private VisitFactory visitFactory;
	
	private PrivilegeService privilegeSvc;
	
	private ParticipantService participantService;

	public void setPrivilegeSvc(PrivilegeService privilegeSvc) {
		this.privilegeSvc = privilegeSvc;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setRegistrationFactory(CollectionProtocolRegistrationFactory registrationFactory) {
		this.registrationFactory = registrationFactory;
	}
	
	public void setVisitFactory(VisitFactory visitFactory) {
		this.visitFactory = visitFactory;
	}

	public void setParticipantService(ParticipantService participantService) {
		this.participantService = participantService;
	}
	
	@Override
	@PlusTransactional
	public RegistrationEvent getRegistration(ReqRegistrationEvent req) {
		RegistrationEvent resp = null;
		
		try {			
			if (req.getCprId() != null) {
				resp = getByCprId(req.getCprId());
			} else if (req.getCpId() != null && req.getPpid() != null) {
				resp = getByCpIdAndPpid(req.getCpId(), req.getPpid());
			} else {
				resp = RegistrationEvent.invalidRequest();
			}
		} catch (Exception e) {
			resp = RegistrationEvent.serverError(e);
		}
		
		return resp;
	}

	@Override
	@PlusTransactional
	public RegistrationCreatedEvent createRegistration(CreateRegistrationEvent req) {
		try {
			//TODO: change below code to use Rbac
//			Long cpId = getCpId(req);
//			if (!privilegeSvc.hasPrivilege(req.getSessionDataBean().getUserId(), cpId, Permissions.REGISTRATION)){
//				return RegistrationCreatedEvent.accessDenied(Permissions.REGISTRATION, cpId);
//			}

			CollectionProtocolRegistration cpr = registrationFactory.createCpr(req.getCprDetail());
			
			ObjectCreationException oce = new ObjectCreationException();
			ensureUniqueParticipantReg(cpr, oce);
			ensureUniquePpid(cpr, oce);
			ensureUniqueBarcode(cpr.getBarcode(), oce);
			oce.checkErrorAndThrow();
			
			saveParticipant(cpr, req.getCprDetail().getParticipant());
			daoFactory.getCprDao().saveOrUpdate(cpr);
			return RegistrationCreatedEvent.ok(CollectionProtocolRegistrationDetail.fromDomain(cpr));
		} catch (ObjectCreationException ce) {
			return RegistrationCreatedEvent.invalidRequest(ParticipantErrorCode.ERRORS.message(), ce.getErroneousFields());
		} catch (Exception e) {
			return RegistrationCreatedEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public VisitsEvent getVisits(ReqVisitsEvent req) {
		try {
			VisitsListCriteria crit = new VisitsListCriteria()
				.includeStat(req.isIncludeStats())
				.cprId(req.getCprId());
			
			return VisitsEvent.ok(daoFactory.getVisitsDao().getVisits(crit));
		} catch (Exception e) {
			return VisitsEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public VisitSpecimensEvent getSpecimens(ReqVisitSpecimensEvent req) {
		Long cprId   = req.getCprId(); 
		Long eventId = req.getEventId();
		Long visitId = req.getVisitId();
		
		try {
			if (visitId != null) {
				return getSpecimensByVisit(cprId, visitId);
			} else if (eventId != null) {
				return getAnticipatedSpecimens(cprId, eventId);
			}
			
			return VisitSpecimensEvent.ok(cprId, eventId, visitId, Collections.<SpecimenSummary>emptyList());
		} catch (Exception e) {
			return VisitSpecimensEvent.serverError(cprId, eventId, visitId, e);
		}
	}
	
	@Override
	@PlusTransactional
	public VisitAddedEvent addVisit(AddVisitEvent req) {
		try { // TODO: visit name
			Visit visit = visitFactory.createVisit(req.getVisit()); 
			visit.setName(UUID.randomUUID().toString()); 

			daoFactory.getVisitsDao().saveOrUpdate(visit); 
			return VisitAddedEvent.ok(VisitDetail.from(visit));			
		} catch (ObjectCreationException oce) {
			return VisitAddedEvent.invalidRequest(oce.getMessage(), oce.getErroneousFields());
		} catch (Exception e) {
			return VisitAddedEvent.serverError(e);
		}
	}
		
	
	@Override
	@PlusTransactional
	public BulkRegistrationCreatedEvent createBulkRegistration(CreateBulkRegistrationEvent req) {
		try {
			ParticipantRegistrationDetails participantRegDetails = req.getParticipantDetails();
			for (int i =0; i < participantRegDetails.getRegistrationDetails().size(); i++) {
				CreateRegistrationEvent request = new CreateRegistrationEvent();
				CollectionProtocolRegistrationDetail cprDetails = buildCprForBulkParticipantDetails(participantRegDetails, i );
				request.setCpId(cprDetails.getCpId());
				request.setCprDetail(cprDetails);
				request.setSessionDataBean(req.getSessionDataBean());
				
				RegistrationCreatedEvent response = createRegistration(request);
				
				if (response.getStatus() == EventStatus.OK) {
					CollectionProtocolRegistrationDetail savedCpr = response.getCprDetail(); 
					participantRegDetails.setId(savedCpr.getParticipant().getId());
					participantRegDetails.getRegistrationDetails().get(i).setCprId(savedCpr.getId());
				} else {
					return BulkRegistrationCreatedEvent.invalidRequest(response.getMessage(), response.getErroneousFields());
				}
			}
			
			return BulkRegistrationCreatedEvent.ok(participantRegDetails);
		} catch (Exception e) {
			return BulkRegistrationCreatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public RegistrationUpdatedEvent updateRegistration(UpdateRegistrationEvent event) {
		try {
			CollectionProtocolRegistration oldCpr = null;
			if (event.getId() != null) {
				oldCpr = daoFactory.getCprDao().getById(event.getId());
			}
			else if (event.getCprDetail().getPpid() != null && event.getCprDetail().getCpId() != null) {
				oldCpr = daoFactory.getCprDao().getCprByPpId(event.getCprDetail().getCpId(), event.getCprDetail().getPpid());
			}

			if (oldCpr == null) {
				RegistrationUpdatedEvent.notFound(event.getId());
			}
			ObjectCreationException errorHandler = new ObjectCreationException();
			event.getCprDetail().setId(event.getId());
			CollectionProtocolRegistration cpr = registrationFactory.createCpr(event.getCprDetail());

			validatePpid(oldCpr, cpr, errorHandler);
			validateBarcode(oldCpr.getBarcode(), cpr.getBarcode(), errorHandler);
			errorHandler.checkErrorAndThrow();
			oldCpr.update(cpr);
			daoFactory.getCprDao().saveOrUpdate(cpr);
			return RegistrationUpdatedEvent.ok(CollectionProtocolRegistrationDetail.fromDomain(cpr));
		}
		catch (ObjectCreationException ce) {
			return RegistrationUpdatedEvent.invalidRequest(ParticipantErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return RegistrationUpdatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public RegistrationUpdatedEvent patchRegistration(PatchRegistrationEvent event) {
//		try {
//			CollectionProtocolRegistration oldCpr = null;
//			CollectionProtocolRegistrationPatchDetail detail = event.getCollectionProtocolRegistrationDetail();
//			if (event.getId() != null) {
//				oldCpr = daoFactory.getCprDao().getCpr(event.getId());
//			}
//			else if (detail.getPpid() != null && detail.getCpId() != null) {
//				oldCpr = daoFactory.getCprDao().getCprByPpId(detail.getCpId(), detail.getPpid());
//			}
//
//			if (oldCpr == null) {
//				RegistrationUpdatedEvent.notFound(event.getId());
//			}
//			ObjectCreationException errorHandler = new ObjectCreationException();
//			CollectionProtocolRegistration cpr = registrationFactory.patchCpr(oldCpr, detail);
//
//			validatePpid(oldCpr, cpr, errorHandler);
//			validateBarcode(oldCpr.getBarcode(), cpr.getBarcode(), errorHandler);
//			errorHandler.checkErrorAndThrow();
//			oldCpr.update(cpr);
//			daoFactory.getCprDao().saveOrUpdate(oldCpr);
//			return RegistrationUpdatedEvent.ok(CollectionProtocolRegistrationDetail.fromDomain(oldCpr));
//		}
//		catch (ObjectCreationException ce) {
//			return RegistrationUpdatedEvent.invalidRequest(ParticipantErrorCode.ERRORS.message(), ce.getErroneousFields());
//		}
//		catch (Exception e) {
//			return RegistrationUpdatedEvent.serverError(e);
//		}
		return null;
	}

	@Override
	@PlusTransactional
	public RegistrationDeletedEvent delete(DeleteRegistrationEvent event) {
		try {
			CollectionProtocolRegistration registration = daoFactory.getCprDao().getById(event.getId());
			if (registration == null) {
				return RegistrationDeletedEvent.notFound(event.getId());
			}
			registration.delete(event.isIncludeChildren());

			daoFactory.getCprDao().saveOrUpdate(registration);
			return RegistrationDeletedEvent.ok();
		}
		catch (CatissueException ce) {
			return RegistrationDeletedEvent.invalidRequest(ce.getMessage() + " : " + ce.getErroneousFields());
		}
		catch (Exception e) {
			return RegistrationDeletedEvent.serverError(e);
		}
	}
	
	private void saveParticipant(CollectionProtocolRegistration cpr, ParticipantDetail detail) {
		if (detail.getId() != null) {
			return; 
		}
		
		Participant participant = cpr.getParticipant();
		participantService.createParticipant(participant);
		cpr.setParticipant(participant);
	}
	
	private Long getCpId(CreateRegistrationEvent req) {
		Long cpId = req.getCpId();
		if (cpId == null && req.getCprDetail() != null) {
			cpId = req.getCprDetail().getCpId();
			
			String title = req.getCprDetail().getCpTitle();			
			if (cpId == null && StringUtils.isNotBlank(title)) {
				CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(title);				
				if (cp == null) {
					ObjectCreationException oce = new ObjectCreationException();
					oce.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, "collection protocol");
					throw oce;
				}
				
				cpId = cp.getId();
			}
		}
		
		if (cpId != null) {
			return cpId;
		}
		
		ObjectCreationException exception = new ObjectCreationException();
		exception.addError(ParticipantErrorCode.MISSING_ATTR_VALUE, "collection protocol");
		throw exception;
	}
	
	private void ensureUniqueParticipantReg(CollectionProtocolRegistration cpr, ObjectCreationException oce) {
		if (cpr.getParticipant() == null || cpr.getParticipant().getId() == null || 
				cpr.getCollectionProtocol() == null) {
			return ;
		}
		
		Long participantId = cpr.getParticipant().getId();
		Long cpId = cpr.getCollectionProtocol().getId();
		
		if (daoFactory.getCprDao().getRegistrationId(cpId, participantId) != null) {
			oce.addError(ParticipantErrorCode.ALREADY_REGISTERED, "participant");
		}
	}

	private void ensureUniquePpid(CollectionProtocolRegistration cpr, ObjectCreationException oce) {
		Long cpId = cpr.getCollectionProtocol().getId();
		String ppid = cpr.getProtocolParticipantIdentifier();
		
		if (daoFactory.getCprDao().getCprByPpId(cpId, ppid) != null) {
			oce.addError(ParticipantErrorCode.DUPLICATE_PPID, PPID);
		}
	}

	private void ensureUniqueBarcode(String barcode, ObjectCreationException oce) {
		if (!StringUtils.isBlank(barcode) && daoFactory.getCprDao().getCprByBarcode(barcode) != null) {
			oce.addError(ParticipantErrorCode.DUPLICATE_BARCODE, BARCODE);
		}
	}

	private void validatePpid(CollectionProtocolRegistration oldCpr, CollectionProtocolRegistration cpr,
			ObjectCreationException errorHandler) {
		String oldPpid = oldCpr.getProtocolParticipantIdentifier();
		String newPpid = cpr.getProtocolParticipantIdentifier();
		if (!oldPpid.equals(newPpid)) {
			ensureUniquePpid(cpr, errorHandler);
		}
	}

	private void validateBarcode(String oldBarcode, String newBarcode, ObjectCreationException errorHandler) {
		if (!isBlank(newBarcode) && !newBarcode.equals(oldBarcode)) {
			ensureUniqueBarcode(newBarcode, errorHandler);
		}
	}

	private CollectionProtocolRegistrationDetail buildCprForBulkParticipantDetails(ParticipantRegistrationDetails participantDetails
			,int i ) {
		CollectionProtocolRegistrationDetail cprDetails = new CollectionProtocolRegistrationDetail();
		ParticipantDetail participant = (ParticipantDetail)participantDetails;
		
		cprDetails.setParticipant(participant);
		CprRegistrationDetails cpr = participantDetails.getRegistrationDetails().get(i);
		cprDetails.setCpId(cpr.getCpId());
		cprDetails.setCpTitle(cpr.getCpTitle());
		cprDetails.setId(cpr.getCprId());
		cprDetails.setPpid(cpr.getPpId());
		cprDetails.setRegistrationDate(cpr.getRegistrationDate());
		cprDetails.setConsentDetails(cpr.getConsentResponseDetail());
		return cprDetails;
	}
	
	private RegistrationEvent getByCprId(Long cprId) {
		CollectionProtocolRegistration cpr = daoFactory.getCprDao().getById(cprId);
		if (cpr == null) {
			return RegistrationEvent.notFound(cprId);
		}
		
		return RegistrationEvent.ok(CollectionProtocolRegistrationDetail.fromDomain(cpr));
	}
	
	private RegistrationEvent getByCpIdAndPpid(Long cpId, String ppid) {
		CollectionProtocolRegistration cpr = daoFactory.getCprDao().getCprByPpId(cpId, ppid);
		if (cpr == null) {
			return RegistrationEvent.notFound(cpId, ppid);
		}
		
		return RegistrationEvent.ok(CollectionProtocolRegistrationDetail.fromDomain(cpr));
	}
	
	private VisitSpecimensEvent getSpecimensByVisit(Long cprId, Long visitId) {
		Visit visit = daoFactory.getVisitsDao().getVisit(visitId);
		if (visit == null) {
			return VisitSpecimensEvent.notFound(cprId, null, visitId);
		}
		
		Set<SpecimenRequirement> anticipatedSpecimens = visit.getCpEvent().getTopLevelAnticipatedSpecimens();
		Set<Specimen> specimens = visit.getTopLevelSpecimens();

		return VisitSpecimensEvent.ok(
				cprId, null, visitId, 
				getSpecimens(anticipatedSpecimens, specimens));
	}
	
	private VisitSpecimensEvent getAnticipatedSpecimens(Long cprId, Long eventId) {
		CollectionProtocolEvent cpe = daoFactory.getCollectionProtocolDao().getCpe(eventId);
		if (cpe == null) {
			return VisitSpecimensEvent.notFound(cprId, eventId, null);
		}
		
		Set<SpecimenRequirement> anticipatedSpecimens = cpe.getTopLevelAnticipatedSpecimens();
		return VisitSpecimensEvent.ok(
				cprId, eventId, null, 
				getSpecimens(anticipatedSpecimens, Collections.<Specimen>emptySet()));		
	}
	
	private List<SpecimenSummary> getSpecimens(Collection<SpecimenRequirement> anticipated, Collection<Specimen> specimens) {		
		List<SpecimenSummary> result = SpecimenSummary.from(specimens);
		merge(anticipated, result, null, getReqSpecimenMap(result));

		SpecimenSummary.sort(result);
		return result;
	}
	
	private Map<Long, SpecimenSummary> getReqSpecimenMap(List<SpecimenSummary> specimens) {
		Map<Long, SpecimenSummary> reqSpecimenMap = new HashMap<Long, SpecimenSummary>();
						
		List<SpecimenSummary> remaining = new ArrayList<SpecimenSummary>();
		remaining.addAll(specimens);
		
		while (!remaining.isEmpty()) {
			SpecimenSummary specimen = remaining.remove(0);
			Long srId = (specimen.getReqId() == null) ? -1 : specimen.getReqId();
			reqSpecimenMap.put(srId, specimen);
			
			remaining.addAll(specimen.getChildren());
		}
		
		return reqSpecimenMap;
	}
	
	private void merge(
			Collection<SpecimenRequirement> anticipatedSpecimens, 
			List<SpecimenSummary> result, 
			SpecimenSummary currentParent,
			Map<Long, SpecimenSummary> reqSpecimenMap) {
		
		for (SpecimenRequirement anticipated : anticipatedSpecimens) {
			SpecimenSummary specimen = reqSpecimenMap.get(anticipated.getId());
			if (specimen != null) {
				merge(anticipated.getChildSpecimenRequirements(), result, specimen, reqSpecimenMap);
			} else {
				specimen = SpecimenSummary.from(anticipated);
				
				if (currentParent == null) {
					result.add(specimen);
				} else if (specimen == null) {
					currentParent.getChildren().add(specimen);
				}				
			}						
		}
	}
}
