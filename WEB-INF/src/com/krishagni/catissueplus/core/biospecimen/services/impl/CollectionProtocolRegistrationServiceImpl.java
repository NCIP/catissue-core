
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimenCollGroupsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.BulkRegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationPatchDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CprRegistrationDetails;
import com.krishagni.catissueplus.core.biospecimen.events.CreateBulkRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantRegistrationDetails;
import com.krishagni.catissueplus.core.biospecimen.events.PatchRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenCollGroupSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.privileges.services.PrivilegeService;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.security.global.Permissions;

@Service(value = "CollectionProtocolRegistrationServiceImpl")
public class CollectionProtocolRegistrationServiceImpl implements CollectionProtocolRegistrationService {

	private final String PPID = "participant protocol identifier";

	private final String BARCODE = "barcode";

	private DaoFactory daoFactory;

	private CollectionProtocolRegistrationFactory registrationFactory;
	
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
	
	public void setParticipantService(ParticipantService participantService) {
		this.participantService = participantService;
	}

	@Override
	@PlusTransactional
	public AllSpecimenCollGroupsSummaryEvent getSpecimenCollGroupsList(ReqSpecimenCollGroupSummaryEvent req) {
		try {
			return AllSpecimenCollGroupsSummaryEvent.ok(daoFactory.getCprDao().getScgList(req.getCprId()));
		}
		catch (Exception e) {
			return AllSpecimenCollGroupsSummaryEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public RegistrationCreatedEvent createRegistration(CreateRegistrationEvent req) {
		try {
			Long cpId = getCpId(req);
			if (!privilegeSvc.hasPrivilege(req.getSessionDataBean().getUserId(), cpId, Permissions.REGISTRATION)){
				return RegistrationCreatedEvent.accessDenied(Permissions.REGISTRATION, cpId);
			}
			
			CollectionProtocolRegistration cpr = registrationFactory.createCpr(req.getCprDetail());
			
			ObjectCreationException oce = new ObjectCreationException();
			ensureUniquePpid(cpr, oce);
			ensureUniqueBarcode(cpr.getBarcode(), oce);
			oce.checkErrorAndThrow();
			
			saveParticipant(cpr, req);
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
				oldCpr = daoFactory.getCprDao().getCpr(event.getId());
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
			CollectionProtocolRegistration registration = daoFactory.getCprDao().getCpr(event.getId());
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
	
	private void saveParticipant(CollectionProtocolRegistration cpr, CreateRegistrationEvent req) {
		if (req.getCprDetail().getParticipant().getId() != null) {
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

	private void ensureUniquePpid(CollectionProtocolRegistration cpr, ObjectCreationException oce) {
		Long cpId = cpr.getCollectionProtocol().getId();
		String ppid = cpr.getProtocolParticipantIdentifier();
		
		if (!daoFactory.getCprDao().isPpidUniqueForProtocol(cpId, ppid)) {
			oce.addError(ParticipantErrorCode.DUPLICATE_PPID, PPID);
		}
	}

	private void ensureUniqueBarcode(String barcode, ObjectCreationException oce) {
		if (!StringUtils.isBlank(barcode) && !daoFactory.getCprDao().isBarcodeUnique(barcode)) {
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
}
