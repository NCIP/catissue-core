
package com.krishagni.catissueplus.core.services.testdata;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.notification.events.NotifiedRegistrationDetail;
import com.krishagni.catissueplus.core.notification.events.RegisterParticipantEvent;

public class CaTissueNotificationTestData {

	public static RegisterParticipantEvent getRegisterParticipantEvent() {

		NotifiedRegistrationDetail registrationDetail = new NotifiedRegistrationDetail();
		registrationDetail.setAppName("Mirth");
		registrationDetail.setGender("Male Gender");
		registrationDetail.setPpId("11_11");
		registrationDetail.setStudyId("test");
		
		RegisterParticipantEvent event = new RegisterParticipantEvent();
		event.setRegistrationDetails(registrationDetail);
		return event;
	}

	public static CollectionProtocolRegistration getCprToReturn() {
		CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
		cpr.setProtocolParticipantIdentifier("11_11");
		cpr.setActive();
		cpr.setCollectionProtocol(CprTestData.getCollectionProtocol(1l));
		return cpr;
	}

	public static RegisterParticipantEvent getRegisterParticipantEventWithNullPpId() {

		NotifiedRegistrationDetail registrationDetail = new NotifiedRegistrationDetail();
		registrationDetail.setAppName("Mirth");
		registrationDetail.setGender("Male Gender");
		registrationDetail.setPpId(null);
		registrationDetail.setStudyId("test");
		
		RegisterParticipantEvent event = new RegisterParticipantEvent();
		event.setRegistrationDetails(registrationDetail);
		return event;
	}

}
