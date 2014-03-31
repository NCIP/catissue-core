
package com.krishagni.catissueplus.core.notification.services;

import com.krishagni.catissueplus.core.biospecimen.events.NotifiedParticipantDetails;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationUpdatedEvent;

public interface CatissueNotificationService {

	public RegistrationCreatedEvent registerParticipant(NotifiedParticipantDetails participantDetails);

	public RegistrationUpdatedEvent updateParticipantRegistartion(NotifiedParticipantDetails participantDetails);

}
