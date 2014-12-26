
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.ChildCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqChildProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.ClinicalDiagnosis;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.AllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ClinicalDiagnosesEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.CreateCollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegisteredParticipantsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqClinicalDiagnosesEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqCollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqRegisteredParticipantsEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.CprListCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.privileges.PrivilegeType;
import com.krishagni.catissueplus.core.privileges.services.PrivilegeService;

import edu.wustl.security.global.Permissions;

public class CollectionProtocolServiceImpl implements CollectionProtocolService {

	private CollectionProtocolFactory cpFactory;

	private DaoFactory daoFactory;

	private PrivilegeService privilegeSvc;

	public CollectionProtocolFactory getCpFactory() {
		return cpFactory;
	}

	public void setCpFactory(CollectionProtocolFactory cpFactory) {
		this.cpFactory = cpFactory;
	}

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setPrivilegeSvc(PrivilegeService privilegeSvc) {
		this.privilegeSvc = privilegeSvc;
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
	public CollectionProtocolEvent getCollectionProtocol(ReqCollectionProtocolEvent req) {
		Long cpId = req.getCpId();
		
		CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(cpId);
		if (cp == null) {
			return CollectionProtocolEvent.notFound(cpId);
		}
		
		return CollectionProtocolEvent.ok(CollectionProtocolDetail.from(cp));
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
	public ClinicalDiagnosesEvent getDiagnoses(ReqClinicalDiagnosesEvent req) {
		String searchTerm = req.getSearchTerm();
		if (searchTerm != null) {
			searchTerm = searchTerm.toLowerCase();
		}

		Long cpId = req.getCpId();
		if (cpId == null || cpId == -1) {
			return ClinicalDiagnosesEvent.ok(getClinicalDiagnoses(searchTerm));
		}

		CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(cpId);
		if (cp == null) {
			return ClinicalDiagnosesEvent.notFound();
		}

		List<String> diagnoses = new ArrayList<String>();
		for (ClinicalDiagnosis cd : cp.getClinicalDiagnosis()) {
			if (searchTerm == null || searchTerm.isEmpty()) {
				diagnoses.add(cd.getName());
			} else if (cd.getName().toLowerCase().contains(searchTerm)) {
				diagnoses.add(cd.getName());
			}
		}

		return ClinicalDiagnosesEvent.ok(diagnoses);
	}
	
	@Override
	@PlusTransactional
	public CollectionProtocolCreatedEvent createCollectionProtocol(CreateCollectionProtocolEvent req) {
		try {
			CollectionProtocol cp = cpFactory.createCollectionProtocol(req.getCp());

			ObjectCreationException oce = new ObjectCreationException();
			ensureUniqueTitle(cp.getTitle(), oce);
			oce.checkErrorAndThrow();

			daoFactory.getCollectionProtocolDao().saveOrUpdate(cp);
			return CollectionProtocolCreatedEvent.ok(CollectionProtocolDetail.from(cp));
		} catch (ObjectCreationException oce) {
			return CollectionProtocolCreatedEvent.badRequest(oce);
		} catch (Exception e) {
			return CollectionProtocolCreatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ChildCollectionProtocolsEvent getChildProtocols(ReqChildProtocolEvent req) {
		return null;
	}
	
	private Long getUserId(RequestEvent req) {
		return req.getSessionDataBean().getUserId();
	}

	private List<String> getClinicalDiagnoses(String searchTerm) {
		return daoFactory.getPermissibleValueDao().getAllValuesByAttribute("Clinical_Diagnosis_PID", searchTerm, 100);
	}

	private void ensureUniqueTitle(String title, ObjectCreationException oce) {
		CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(title);
		if (cp != null) {
			oce.addError(CpErrorCode.TITLE_NOT_UNIQUE, "title");
		}
	}

}
