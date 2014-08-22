
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.ChildCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqChildProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantInfo;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.privileges.PrivilegeType;
import com.krishagni.catissueplus.core.privileges.services.PrivilegeService;

import edu.wustl.security.global.Permissions;

public class CollectionProtocolServiceImpl implements CollectionProtocolService {

	private DaoFactory daoFactory;

	private PrivilegeService privilegeSvc;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setPrivilegeSvc(PrivilegeService privilegeSvc) {
		this.privilegeSvc = privilegeSvc;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public AllCollectionProtocolsEvent getAllProtocols(ReqAllCollectionProtocolsEvent req) {
		List<CollectionProtocolSummary> list = daoFactory.getCollectionProtocolDao().getAllCollectionProtocols();
		List<CollectionProtocolSummary> listToReturn = new ArrayList<CollectionProtocolSummary>();
		List<Long> cpList = privilegeSvc.getCpList(req.getSessionDataBean().getUserId(), PrivilegeType.REGISTRATION.name(),req.isChkPrivileges());
		for (CollectionProtocolSummary collectionProtocolSummary : list) {
			if(cpList.contains(collectionProtocolSummary.getId())){
				listToReturn.add(collectionProtocolSummary);
			}
		}
		Collections.sort(listToReturn);
		return AllCollectionProtocolsEvent.ok(listToReturn);
	}

	@Override
	@PlusTransactional
	public ParticipantsSummaryEvent getParticipants(ReqParticipantsSummaryEvent req) {
		try {
			Long cpId = req.getCpId();
			String searchStr = req.getSearchString();
			List<ParticipantInfo> participants = new ArrayList<ParticipantInfo>();
			if(privilegeSvc.hasPrivilege(req.getSessionDataBean().getUserId(), cpId,Permissions.REGISTRATION)){
					participants = daoFactory.getCprDao().getPhiParticipants(cpId, searchStr);
			}
			else{
				participants = daoFactory.getCprDao().getParticipants(cpId, searchStr);
			}
			
			return ParticipantsSummaryEvent.ok(participants);
		}
		catch (CatissueException e) {
			return ParticipantsSummaryEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ParticipantSummaryEvent getParticipant(ReqParticipantSummaryEvent event) {
		try {
			Long cpId = event.getCpId();
			Long participantId = event.getParticipantId();
			ParticipantInfo participant;
			if(privilegeSvc.hasPrivilege(event.getSessionDataBean().getUserId(), cpId,Permissions.REGISTRATION)){
				participant = daoFactory.getCprDao().getPhiParticipant(cpId, participantId);
			}
			else{
				participant = daoFactory.getCprDao().getParticipant(cpId, participantId);
			}
			
			return ParticipantSummaryEvent.ok(participant);
		}
		catch (CatissueException e) {
			return ParticipantSummaryEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ChildCollectionProtocolsEvent getChildProtocols(ReqChildProtocolEvent req) {
		Long cpId = req.getCpId();
		List<CollectionProtocolSummary> list = daoFactory.getCollectionProtocolDao().getChildProtocols(cpId);
		return ChildCollectionProtocolsEvent.ok(list);
	}

}
