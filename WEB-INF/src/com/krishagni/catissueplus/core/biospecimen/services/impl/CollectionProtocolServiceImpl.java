
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.AllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantInfo;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;

public class CollectionProtocolServiceImpl implements CollectionProtocolService {

	private DaoFactory daoFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public AllCollectionProtocolsEvent getAllProtocols(ReqAllCollectionProtocolsEvent req) {
		return AllCollectionProtocolsEvent.ok(daoFactory.getCollectionProtocolDao().getAllCollectionProtocols());
	}

	@Override
	@PlusTransactional
	public ParticipantsSummaryEvent getParticipants(ReqParticipantsSummaryEvent req) {
		try {
			Long cpId = req.getCpId();
			String searchStr = req.getSearchString();

			List<ParticipantInfo> participants = daoFactory.getCprDao().getParticipants(cpId, searchStr);
			return ParticipantsSummaryEvent.ok(participants);
		}
		catch (CatissueException e) {
			return ParticipantsSummaryEvent.serverError(e);
		}
	}

}
