
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimenCollGroupsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetails;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenCollGroupSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;

@Service(value = "CollectionProtocolRegistrationServiceImpl")
public class CollectionProtocolRegistrationServiceImpl implements CollectionProtocolRegistrationService {

	private final String PPID = "participant protocol identifier";

	private DaoFactory daoFactory;

	private CollectionProtocolRegistrationFactory registrationFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setRegistrationFactory(CollectionProtocolRegistrationFactory registrationFactory) {
		this.registrationFactory = registrationFactory;
	}

	@Override
	@PlusTransactional
	public AllSpecimenCollGroupsSummaryEvent getSpecimenCollGroupsList(
			ReqSpecimenCollGroupSummaryEvent reqSpecimenCollGroupSummaryEvent) {

		try {
			return AllSpecimenCollGroupsSummaryEvent.ok(daoFactory.getRegistrationDao().getSpecimenCollectiongroupsList(
					reqSpecimenCollGroupSummaryEvent.getCollectionProtocolRegistrationId()));
		}
		catch (Exception e) {
			return AllSpecimenCollGroupsSummaryEvent.serverError(e);
		}

	}

	@Override
	public RegistrationCreatedEvent createRegistration(CreateRegistrationEvent event) {
		try {
			CollectionProtocolRegistration registration = registrationFactory.createCpr(event.getRegistrationDetails());
			if (!daoFactory.getCollectionProtocolDao().isPpidUniqueForProtocol(registration.getCollectionProtocol().getId(),
					registration.getProtocolParticipantIdentifier())) {
				reportError(ParticipantErrorCode.DUPLICATE_PPID, PPID);
			}
			daoFactory.getRegistrationDao().saveOrUpdate(registration);
			return RegistrationCreatedEvent.ok(CollectionProtocolRegistrationDetails.fromDomain(registration));
		}
		catch (CatissueException ce) {
			return RegistrationCreatedEvent.invalidRequest(ce.getMessage() + " : " + ce.getErroneousFields());
		}
		catch (Exception e) {
			return RegistrationCreatedEvent.serverError(e);
		}
	}

	@Override
	public RegistrationDeletedEvent delete(DeleteRegistrationEvent event) {
		try {
			CollectionProtocolRegistration registration = daoFactory.getRegistrationDao().getCpr(event.getId());
			if (registration == null) {
				return RegistrationDeletedEvent.notFound(event.getId());
			}
			registration.delete(event.isIncludeChildren());
			daoFactory.getRegistrationDao().delete(registration);
			return RegistrationDeletedEvent.ok();
		}
		catch (CatissueException ce) {
			return RegistrationDeletedEvent.invalidRequest(ce.getMessage() + " : " + ce.getErroneousFields());
		}
		catch (Exception e) {
			return RegistrationDeletedEvent.serverError(e);
		}
	}

}
