
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimenCollGroupsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetails;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenCollGroupSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenCollGroupService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;

@Service(value = "CollectionProtocolRegistrationServiceImpl")
public class CollectionProtocolRegistrationServiceImpl implements CollectionProtocolRegistrationService {

	private final String PPID = "participant protocol identifier";

	private DaoFactory daoFactory;

	private CollectionProtocolRegistrationFactory registrationFactory;

	private SpecimenCollGroupService specimenCollGroupSvc;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public CollectionProtocolRegistrationFactory getRegistrationFactory() {
		return registrationFactory;
	}

	public void setRegistrationFactory(CollectionProtocolRegistrationFactory registrationFactory) {
		this.registrationFactory = registrationFactory;
	}

	public SpecimenCollGroupService getSpecimenCollGroupSvc() {
		return specimenCollGroupSvc;
	}

	public void setSpecimenCollGroupSvc(SpecimenCollGroupService specimenCollGroupSvc) {
		this.specimenCollGroupSvc = specimenCollGroupSvc;
	}

	@Override
	@PlusTransactional
	public AllSpecimenCollGroupsSummaryEvent getSpecimenCollGroupsList(
			ReqSpecimenCollGroupSummaryEvent reqSpecimenCollGroupSummaryEvent) {

		try {
			return AllSpecimenCollGroupsSummaryEvent.ok(daoFactory.getRegistrationDao().getSpecimenCollectiongroupsList(
					reqSpecimenCollGroupSummaryEvent.getCollectionProtocolRegistrationId()));
		}
		catch (CatissueException e) {
			return AllSpecimenCollGroupsSummaryEvent.serverError(e);
		}

	}

	@Override
	public RegistrationCreatedEvent createRegistration(CreateRegistrationEvent event) {
		try {
			CollectionProtocolRegistration registration = registrationFactory.createCpr(event.getRegistrationDetails());
			if (daoFactory.getCollectionProtocolDao().isPpidUniqueForProtocol(registration.getCollectionProtocol().getId(),
					registration.getProtocolParticipantIdentifier())) {
				reportError(ParticipantErrorCode.DUPLICATE_PPID, PPID);
			}
			daoFactory.getRegistrationDao().saveOrUpdate(registration);
			return RegistrationCreatedEvent.ok(CollectionProtocolRegistrationDetails.fromDomain(registration));
		}
		catch (CatissueException ce) {
			return RegistrationCreatedEvent.invalidRequest(ce.getMessage()+ " : " + ce.getErroneousFields());
		}
		catch (Exception e) {
			return RegistrationCreatedEvent.serverError(e);
		}
	}

	@Override
	public RegistrationUpdatedEvent updateResgistration(UpdateRegistrationEvent event) {
//		try {
//			Long registrationId = event.getRegistrationDetails().getId();
//			CollectionProtocolRegistration oldCpr = daoFactory.getRegistrationDao().getCpr(registrationId);
//			if (oldCpr == null) {
//				RegistrationUpdatedEvent.notFound(registrationId);
//			}
//			//TODO: Handle populating the object from factory.
//			CollectionProtocolRegistration cpr = null;
//			oldCpr.update(cpr);
//			RegistrationUpdatedEvent.ok(CollectionProtocolRegistrationDetails.fromDomain(oldCpr));
//		}
//		catch (CatissueException ce) {
//			return RegistrationUpdatedEvent.invalidRequest(ce.getMessage());
//		}
//		catch (Exception e) {
//			return RegistrationUpdatedEvent.serverError(e);
//		}
		return null;
	}

	@Override
	public void delete(DeleteParticipantEvent event) {
//		if (event.isIncludeChildren()) {
//			specimenCollGroupSvc.delete(event);
//		}
//		else if (daoFactory.getRegistrationDao().checkActiveChildrenForParticipant(event.getId())) {
//			throw new CatissueException(ParticipantErrorCode.ACTIVE_CHILDREN_FOUND);
//		}
//		daoFactory.getRegistrationDao().deleteByParticipant(event.getId());
	}

	@Override
	public void delete(DeleteRegistrationEvent event) {
//		if (event.isIncludeChildren()) {
//			specimenCollGroupSvc.delete(event);
//		}
//		else if (daoFactory.getRegistrationDao().checkActiveChildren(event.getId())) {
//			throw new CatissueException(ParticipantErrorCode.ACTIVE_CHILDREN_FOUND);
//		}
//		daoFactory.getRegistrationDao().delete(event.getId());
	}

}
