
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimenCollGroupsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationPatchDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.PatchRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenCollGroupSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.privileges.services.PrivilegeService;

import edu.wustl.security.global.Permissions;

@Service(value = "CollectionProtocolRegistrationServiceImpl")
public class CollectionProtocolRegistrationServiceImpl implements CollectionProtocolRegistrationService {

	private final String PPID = "participant protocol identifier";

	private final String BARCODE = "barcode";

	private DaoFactory daoFactory;

	private CollectionProtocolRegistrationFactory registrationFactory;

	private PrivilegeService privilegeSvc;

	public void setPrivilegeSvc(PrivilegeService privilegeSvc) {
		this.privilegeSvc = privilegeSvc;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setRegistrationFactory(CollectionProtocolRegistrationFactory registrationFactory) {
		this.registrationFactory = registrationFactory;
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
	public RegistrationCreatedEvent createRegistration(CreateRegistrationEvent event) {
		try {
			if(!privilegeSvc.hasPrivilege(event.getSessionDataBean().getUserId(), event.getCpId(), Permissions.REGISTRATION)){
				return RegistrationCreatedEvent.accessDenied(Permissions.REGISTRATION,event.getCpId());
			}
				
			CollectionProtocolRegistration registration = registrationFactory.createCpr(event.getCprDetail());
			ObjectCreationException errorHandler = new ObjectCreationException();
			ensureUniquePpid(registration, errorHandler);
			ensureUniqueBarcode(registration.getBarcode(), errorHandler);
			errorHandler.checkErrorAndThrow();
			daoFactory.getCprDao().saveOrUpdate(registration);
			return RegistrationCreatedEvent.ok(CollectionProtocolRegistrationDetail.fromDomain(registration));
		}
		catch (ObjectCreationException ce) {
			return RegistrationCreatedEvent.invalidRequest(ParticipantErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return RegistrationCreatedEvent.serverError(e);
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
		try {
			CollectionProtocolRegistration oldCpr = null;
			CollectionProtocolRegistrationPatchDetail detail = event.getCollectionProtocolRegistrationDetail();
			if (event.getId() != null) {
				oldCpr = daoFactory.getCprDao().getCpr(event.getId());
			}
			else if (detail.getPpid() != null && detail.getCpId() != null) {
				oldCpr = daoFactory.getCprDao().getCprByPpId(detail.getCpId(), detail.getPpid());
			}

			if (oldCpr == null) {
				RegistrationUpdatedEvent.notFound(event.getId());
			}
			ObjectCreationException errorHandler = new ObjectCreationException();
			CollectionProtocolRegistration cpr = registrationFactory.patchCpr(oldCpr, detail);

			validatePpid(oldCpr, cpr, errorHandler);
			validateBarcode(oldCpr.getBarcode(), cpr.getBarcode(), errorHandler);
			errorHandler.checkErrorAndThrow();
			oldCpr.update(cpr);
			daoFactory.getCprDao().saveOrUpdate(oldCpr);
			return RegistrationUpdatedEvent.ok(CollectionProtocolRegistrationDetail.fromDomain(oldCpr));
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

	private void ensureUniquePpid(CollectionProtocolRegistration registration, ObjectCreationException errorHandler) {
		if (!daoFactory.getCprDao().isPpidUniqueForProtocol(registration.getCollectionProtocol().getId(),
				registration.getProtocolParticipantIdentifier())) {
			errorHandler.addError(ParticipantErrorCode.DUPLICATE_PPID, PPID);
		}
	}

	private void ensureUniqueBarcode(String barcode, ObjectCreationException errorHandler) {
		if (!CommonValidator.isBlank(barcode) && !daoFactory.getCprDao().isBacodeUnique(barcode)) {
			errorHandler.addError(ParticipantErrorCode.DUPLICATE_BARCODE, BARCODE);
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

}
