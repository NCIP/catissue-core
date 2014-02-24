
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.AllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AllConsentsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AllRegistrationsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantInfo;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqConsentsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqRegistrationSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
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
	public ParticipantsSummaryEvent getRegisteredParticipantList(ReqParticipantsSummaryEvent reqParticipantsSummaryEvent) {
		try {
			List<ParticipantInfo> participantsInfo = daoFactory.getCollectionProtocolDao().getRegisteredParticipants(
					reqParticipantsSummaryEvent.getCpId(), reqParticipantsSummaryEvent.getSearchString());
			return ParticipantsSummaryEvent.ok(participantsInfo);
		}
		catch (CatissueException e) {
			return ParticipantsSummaryEvent.serverError(e);
		}
	}
	
	@Override
	public AllRegistrationsSummaryEvent getAllRegistrations(ReqRegistrationSummaryEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AllConsentsSummaryEvent getConsents(ReqConsentsSummaryEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegistrationCreatedEvent createRegistration(ReqRegistrationEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegistrationUpdatedEvent updateResgistration(UpdateRegistrationEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRegistrationDao(CollectionProtocolRegistrationDao dao) {
		// TODO Auto-generated method stub
		
	}
}
