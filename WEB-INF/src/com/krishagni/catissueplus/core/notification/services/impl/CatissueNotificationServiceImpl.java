
package com.krishagni.catissueplus.core.notification.services.impl;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
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
import com.krishagni.catissueplus.rest.controller.PatchRegistrationEvent;

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

			PatchRegistrationEvent patchRegistrationEvent = new PatchRegistrationEvent();
			Map<String, Object> registrationProps = new HashMap<String, Object>();
			registrationProps.put("registrationDate", event.getRegistrationDetails().getEnrollmentDate());
			registrationProps.put("protocolParticipantIdentifier", event.getRegistrationDetails().getEnrollmentDate());
			registrationProps.put("cpId", cpId);
			Map<String, Object> participantDetails = new HashMap<String, Object>();
			participantDetails.put("birthDate", event.getRegistrationDetails().getBirthDate());
			participantDetails.put("gender", event.getRegistrationDetails().getGender());
			registrationProps.put("participantDetail", participantDetails);
			patchRegistrationEvent.setRegistrationProps(registrationProps);

			return cprSvc.patchRegistration(patchRegistrationEvent);
		}
		catch (ObjectCreationException ce) {
			return RegistrationUpdatedEvent.invalidRequest(CPStudyMappingErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return RegistrationUpdatedEvent.serverError(e);
		}
	}
}
