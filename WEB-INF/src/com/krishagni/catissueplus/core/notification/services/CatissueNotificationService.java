
package com.krishagni.catissueplus.core.notification.services;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.notification.events.NotifiedRegistrationDetail;

public interface CatissueNotificationService {

	public ResponseEvent<CollectionProtocolRegistrationDetail> registerParticipant(RequestEvent<NotifiedRegistrationDetail> req);

//	public ResponseEvent<CollectionProtocolRegistrationDetail> updateParticipantRegistartion(RequestEvent<NotifiedRegistrationDetail> event);
}
