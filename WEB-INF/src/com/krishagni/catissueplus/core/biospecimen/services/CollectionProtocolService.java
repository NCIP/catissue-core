
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.administrative.events.ChildCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqChildProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ClinicalDiagnosesEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateCollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegisteredParticipantsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqClinicalDiagnosesEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqCollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqRegisteredParticipantsEvent;

public interface CollectionProtocolService {

	public AllCollectionProtocolsEvent getAllProtocols(ReqAllCollectionProtocolsEvent req);
	
	public CollectionProtocolEvent getCollectionProtocol(ReqCollectionProtocolEvent req);

	public RegisteredParticipantsEvent getRegisteredParticipants(ReqRegisteredParticipantsEvent req);
	
	public ClinicalDiagnosesEvent getDiagnoses(ReqClinicalDiagnosesEvent req);

	public CollectionProtocolCreatedEvent createCollectionProtocol(CreateCollectionProtocolEvent req);

	//public ParticipantSummaryEvent getParticipant(ReqParticipantSummaryEvent event);

	public ChildCollectionProtocolsEvent getChildProtocols(ReqChildProtocolEvent req);

}
