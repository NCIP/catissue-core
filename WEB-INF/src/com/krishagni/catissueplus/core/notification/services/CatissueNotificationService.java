
package com.krishagni.catissueplus.core.notification.services;

import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationUpdatedEvent;
import com.krishagni.catissueplus.core.notification.events.RegisterParticipantEvent;

public interface CatissueNotificationService {

	public RegistrationCreatedEvent registerParticipant(RegisterParticipantEvent event);

	public RegistrationUpdatedEvent updateParticipantRegistartion(RegisterParticipantEvent event);

}
