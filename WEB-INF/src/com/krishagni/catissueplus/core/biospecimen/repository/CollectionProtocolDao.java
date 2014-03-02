package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.Collection;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantInfo;
import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.SpecimenRequirement;

public interface CollectionProtocolDao extends Dao<CollectionProtocol> {
	
	public List<CollectionProtocolSummary> getAllCollectionProtocols();
	public CollectionProtocol getCollectionProtocol(Long cpId);
	public Collection<ConsentTier> getConsentTierCollection(Long cpId);
	public List<ParticipantInfo> getRegisteredParticipants(Long cpId, String searchString);
	public CollectionProtocolRegistration getCpr(Long cpId, String ppid);
	public Collection<CollectionProtocolEvent> getEventCollection(Long cpId);
	public Collection<SpecimenRequirement> getSpecimenRequirements(Long cpeId);

}
