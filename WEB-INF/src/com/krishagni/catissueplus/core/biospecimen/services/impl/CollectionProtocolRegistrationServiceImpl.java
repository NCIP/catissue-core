
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimenCollGroupsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenCollGroupSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

@Service(value = "CollectionProtocolRegistrationServiceImpl")
public class CollectionProtocolRegistrationServiceImpl implements CollectionProtocolRegistrationService {

	private final String PPID = "participant protocol identifier";

	private DaoFactory daoFactory;

	private CollectionProtocolRegistrationFactory registrationFactory;
	private ParticipantFactory participantFactory;

	
	public void setParticipantFactory(ParticipantFactory participantFactory) {
		this.participantFactory = participantFactory;
	}

	private ObjectCreationException exception;
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
			exception = new ObjectCreationException();
		try {
			CollectionProtocolRegistration registration = registrationFactory
					.createCpr(event.getCprDetail(), exception);
			Participant participant;
			if(event.getCprDetail().getParticipantDetail().getId() == null)
			{
				participant = participantFactory.createParticipant(event.getCprDetail().getParticipantDetail(), exception);
			}
			else
			{
				participant = daoFactory.getParticipantDao().getParticipant(event.getCprDetail().getParticipantDetail().getId());
			}
			if(participant == null)
			{
				exception.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, "participant");
			}
			registration.setParticipant(participant);
			ensureUniquePpid(registration);
			exception.checkErrorAndThrow();
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
	
	private void ensureUniquePpid(CollectionProtocolRegistration registration) {
		if (!daoFactory.getCollectionProtocolDao().isPpidUniqueForProtocol(registration.getCollectionProtocol().getId(),
				registration.getProtocolParticipantIdentifier())) {
			exception.addError(ParticipantErrorCode.DUPLICATE_PPID, PPID);
		}
	}


}
