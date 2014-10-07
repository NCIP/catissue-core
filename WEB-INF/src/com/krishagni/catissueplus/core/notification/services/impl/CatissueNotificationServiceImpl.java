
package com.krishagni.catissueplus.core.notification.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationPatchDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantPatchDetail;
import com.krishagni.catissueplus.core.biospecimen.events.PatchRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.notification.domain.factory.CPStudyMappingErrorCode;
import com.krishagni.catissueplus.core.notification.events.RegisterParticipantEvent;
import com.krishagni.catissueplus.core.notification.repository.CPStudyMappingDao;
import com.krishagni.catissueplus.core.notification.services.CatissueNotificationService;

public class CatissueNotificationServiceImpl implements CatissueNotificationService {

	private static final String STUDY_ID = "Study Id";

	private static final String PPID = "Protocol Participant Identifier";

	private DaoFactory daoFactory;

	private CollectionProtocolRegistrationService cprSvc;

	/**
	 * @param cprSvc the cprSvc to set
	 */
	public void setCprSvc(CollectionProtocolRegistrationService cprSvc) {
		this.cprSvc = cprSvc;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public RegistrationCreatedEvent registerParticipant(RegisterParticipantEvent event) {
		try {
			CPStudyMappingDao cpStudyMappingdao = daoFactory.getCPStudyMappingDao();
			Long cpId = cpStudyMappingdao.getMappedCPId(event.getRegistrationDetails().getAppName(), event
					.getRegistrationDetails().getStudyId());
			if (cpId == null) {
				ObjectCreationException.raiseError(CPStudyMappingErrorCode.STUDY_NOT_MAPPED_WITH_CP, STUDY_ID);

			}
			CreateRegistrationEvent createRegEvent = new CreateRegistrationEvent();
			createRegEvent.setCpId(cpId);

			ParticipantDetail participantDetail = new ParticipantDetail();
			participantDetail.setBirthDate(event.getRegistrationDetails().getBirthDate());
			participantDetail.setGender(event.getRegistrationDetails().getGender());

			CollectionProtocolRegistrationDetail cprDetail = new CollectionProtocolRegistrationDetail();
			cprDetail.setParticipantDetail(participantDetail);
			cprDetail.setPpid(event.getRegistrationDetails().getPpId());
			cprDetail.setRegistrationDate(event.getRegistrationDetails().getEnrollmentDate());
			cprDetail.setCpId(cpId);
			createRegEvent.setCprDetail(cprDetail);
			createRegEvent.setSessionDataBean(event.getSessionDataBean());

			return cprSvc.createRegistration(createRegEvent);
		}
		catch (ObjectCreationException ce) {
			return RegistrationCreatedEvent.invalidRequest(CPStudyMappingErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return RegistrationCreatedEvent.serverError(e);
		}

	}

	@Override
	@PlusTransactional
	public RegistrationUpdatedEvent updateParticipantRegistartion(RegisterParticipantEvent event) {
		try {
			Long cpId = daoFactory.getCPStudyMappingDao().getMappedCPId(event.getRegistrationDetails().getAppName(),
					event.getRegistrationDetails().getStudyId());

			ObjectCreationException exception = new ObjectCreationException();
			if (cpId == null) {
				exception.addError(CPStudyMappingErrorCode.STUDY_NOT_MAPPED_WITH_CP, STUDY_ID);
			}
			if (event.getRegistrationDetails().getPpId() == null) {
				exception.addError(CPStudyMappingErrorCode.PPID_NULL, PPID);
			}
			exception.checkErrorAndThrow();
			PatchRegistrationEvent patchRegistrationEvent = createPatchRegistartionEvent(event, cpId);
			RegistrationUpdatedEvent regUpdatedEvent = cprSvc.patchRegistration(patchRegistrationEvent);
			return regUpdatedEvent;
		}
		catch (ObjectCreationException ce) {
			return RegistrationUpdatedEvent.invalidRequest(CPStudyMappingErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return RegistrationUpdatedEvent.serverError(e);
		}
	}

	private PatchRegistrationEvent createPatchRegistartionEvent(RegisterParticipantEvent registerPartEvent, Long cpId) {
		PatchRegistrationEvent patchRegistrationEvent = new PatchRegistrationEvent();
		CollectionProtocolRegistrationPatchDetail cprDetail = new CollectionProtocolRegistrationPatchDetail();
		cprDetail.setRegistrationDate(registerPartEvent.getRegistrationDetails().getEnrollmentDate());
		cprDetail.setPpid(registerPartEvent.getRegistrationDetails().getPpId());
		cprDetail.setCpId(cpId);
		ParticipantPatchDetail participantDetail = new ParticipantPatchDetail();
		participantDetail.setBirthDate(registerPartEvent.getRegistrationDetails().getBirthDate());
		participantDetail.setGender(registerPartEvent.getRegistrationDetails().getGender());
		cprDetail.setParticipantDetail(participantDetail);

		List<String> cprModifiedAttributes = new ArrayList<String>();
		cprModifiedAttributes.add("registrationDate");
		cprModifiedAttributes.add("ppid");
		cprModifiedAttributes.add("cpId");
		cprModifiedAttributes.add("participantDetail");
		cprDetail.setModifiedAttributes(cprModifiedAttributes);

		List<String> participantModifiedAttributes = new ArrayList<String>();
		participantModifiedAttributes.add("birthDate");
		participantModifiedAttributes.add("gender");
		participantDetail.setModifiedAttributes(participantModifiedAttributes);

		patchRegistrationEvent.setCollectionProtocolRegistrationDetail(cprDetail);
		return patchRegistrationEvent;
	}
}