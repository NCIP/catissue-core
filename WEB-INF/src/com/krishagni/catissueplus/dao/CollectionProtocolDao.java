
package com.krishagni.catissueplus.dao;

import java.util.List;

import com.krishagni.catissueplus.events.collectionprotocols.CollectionProtocolInfo;
import com.krishagni.catissueplus.events.participants.ParticipantInfo;

import edu.wustl.catissuecore.domain.CollectionProtocol;

public interface CollectionProtocolDao extends Dao<CollectionProtocol> {

	//TODO need to modify the hbm's to set the lazy false, so that DAO will return the domain objects instead of DTO's.

	public List<CollectionProtocolInfo> getCollectionProtocolsList();

	public CollectionProtocol getCollectionProtocol(Long id);

	public List<ParticipantInfo> getRegisteredParticipants(Long cpId, String searchString);

}
