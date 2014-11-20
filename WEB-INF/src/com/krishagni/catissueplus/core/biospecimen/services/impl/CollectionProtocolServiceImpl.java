
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.ChildCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqChildProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.events.AllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRespEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegisteredParticipantsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqCollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqRegisteredParticipantsEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.CprListCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
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
		List<CollectionProtocolSummary> cpList = daoFactory.getCollectionProtocolDao()
				.getAllCollectionProtocols(req.isIncludePi(), req.isIncludeStats());
		
		
		List<Long> allowedCpIds = privilegeSvc.getCpList(
				getUserId(req), 
				PrivilegeType.REGISTRATION.name(),req.isChkPrivileges());
		
		List<CollectionProtocolSummary> result = new ArrayList<CollectionProtocolSummary>();
		for (CollectionProtocolSummary collectionProtocolSummary : cpList) {
			if (allowedCpIds.contains(collectionProtocolSummary.getId())) {
				result.add(collectionProtocolSummary);
			}
		}
		
		Collections.sort(result);
		return AllCollectionProtocolsEvent.ok(result);
	}
	
	@Override
	@PlusTransactional
	public CollectionProtocolRespEvent getCollectionProtocol(ReqCollectionProtocolEvent req) {
		Long cpId = req.getCpId();
		
		CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(cpId);
		if (cp == null) {
			return CollectionProtocolRespEvent.notFound(cpId);
		}
		
		return CollectionProtocolRespEvent.ok(CollectionProtocolDetail.from(cp));
	}
	
	@Override
	@PlusTransactional
	public RegisteredParticipantsEvent getRegisteredParticipants(ReqRegisteredParticipantsEvent req) {
		try {
			Long cpId = req.getCpId();
			String searchStr = req.getSearchString();
			boolean includePhi = privilegeSvc.hasPrivilege(getUserId(req), cpId, Permissions.REGISTRATION);
			
			CprListCriteria listCrit = new CprListCriteria()
				.cpId(cpId)
				.query(searchStr)
				.includePhi(includePhi)
				.startAt(req.getStartAt())
				.maxResults(req.getMaxResults())
				.includeStat(req.isIncludeStats());
			
			return RegisteredParticipantsEvent.ok(daoFactory.getCprDao().getCprList(listCrit));
		} catch (CatissueException e) {
			return RegisteredParticipantsEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ParticipantSummaryEvent getParticipant(ReqParticipantSummaryEvent event) {
		try {
			Long cpId = event.getCpId();
			Long participantId = event.getParticipantId();
			ParticipantSummary participant;
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
		// TODO: Fix this
		return null;
		
//		Long cpId = req.getCpId();
//		List<CollectionProtocolSummary> list = daoFactory.getCollectionProtocolDao().getChildProtocols(cpId);
//		return ChildCollectionProtocolsEvent.ok(list);
	}
	
	private Long getUserId(RequestEvent req) {
		return req.getSessionDataBean().getUserId();
	}

}
