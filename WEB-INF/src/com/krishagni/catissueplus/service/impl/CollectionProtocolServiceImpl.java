
package com.krishagni.catissueplus.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.annotation.PlusTransactional;
import com.krishagni.catissueplus.dao.CollectionProtocolDao;
import com.krishagni.catissueplus.errors.CaTissueException;
import com.krishagni.catissueplus.events.collectionprotocols.AllCollProtocolsSummaryEvent;
import com.krishagni.catissueplus.events.collectionprotocols.CollectionProtocolDetail;
import com.krishagni.catissueplus.events.collectionprotocols.CollectionProtocolDetailEvent;
import com.krishagni.catissueplus.events.collectionprotocols.CollectionProtocolInfo;
import com.krishagni.catissueplus.events.collectionprotocols.ReqCollProtocolDetailEvent;
import com.krishagni.catissueplus.events.collectionprotocols.ReqCollProtocolsSummaryEvent;
import com.krishagni.catissueplus.events.participants.ParticipantInfo;
import com.krishagni.catissueplus.events.participants.ParticipantsSummaryEvent;
import com.krishagni.catissueplus.events.participants.ReqParticipantsSummaryEvent;
import com.krishagni.catissueplus.service.CollectionProtocolService;

import edu.wustl.catissuecore.domain.CollectionProtocol;

@Service(value = "CollectionProtocolServiceImpl")
public class CollectionProtocolServiceImpl implements CollectionProtocolService {

	private CollectionProtocolDao collectionProtocolDao;

	public CollectionProtocolDao getCollectionProtocolDao() {
		return collectionProtocolDao;
	}

	public void setCollectionProtocolDao(CollectionProtocolDao collectionProtocolDao) {
		this.collectionProtocolDao = collectionProtocolDao;
	}

	@Override
	@PlusTransactional
	public AllCollProtocolsSummaryEvent getCollectionProtocolList(
			ReqCollProtocolsSummaryEvent reqCollectionProtocolsSummary) {
		try {
			List<CollectionProtocolInfo> collectionProtocolsInfo = collectionProtocolDao.getCollectionProtocolsList();
			return AllCollProtocolsSummaryEvent.ok(collectionProtocolsInfo);
		}
		catch (CaTissueException e) {
			return AllCollProtocolsSummaryEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public CollectionProtocolDetailEvent getCollectionProtocol(ReqCollProtocolDetailEvent reqProtocolDetailEvent) {
		try {
			CollectionProtocol collectionProtocol = collectionProtocolDao.getCollectionProtocol(reqProtocolDetailEvent
					.getId());
			return CollectionProtocolDetailEvent.ok(CollectionProtocolDetail.fromCollectionProtocol(collectionProtocol));
		}
		catch (CaTissueException e) {
			return CollectionProtocolDetailEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ParticipantsSummaryEvent getRegisteredParticipantList(ReqParticipantsSummaryEvent reqParticipantsSummaryEvent) {
		try {
			List<ParticipantInfo> participantsInfo = collectionProtocolDao.getRegisteredParticipants(
					reqParticipantsSummaryEvent.getCpId(), reqParticipantsSummaryEvent.getSearchString());
			return ParticipantsSummaryEvent.ok(participantsInfo);
		}
		catch (CaTissueException e) {
			return ParticipantsSummaryEvent.serverError(e);
		}
	}

}
