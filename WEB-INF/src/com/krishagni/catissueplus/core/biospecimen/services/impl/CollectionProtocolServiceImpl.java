
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.ChildCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqChildProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.AliquotSpecimensRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.ClinicalDiagnosis;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTier;
import com.krishagni.catissueplus.core.biospecimen.domain.DerivedSpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenRequirementFactory;
import com.krishagni.catissueplus.core.biospecimen.events.AddCpeEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AddSpecimenRequirementEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AliquotsRequirementCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ClinicalDiagnosesEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEventDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOpEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOpRespEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTiersEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CpeAddedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CpeListEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CpeUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateAliquotsRequirementEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateCollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateDerivedSpecimenReqEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DerivedSpecimenReqCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegisteredParticipantsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqClinicalDiagnosesEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqCollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqConsentTiersEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqCpeListEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqRegisteredParticipantsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenRequirementsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementAddedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateCpeEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.CprListCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.privileges.services.PrivilegeService;

public class CollectionProtocolServiceImpl implements CollectionProtocolService {

	private CollectionProtocolFactory cpFactory;
	
	private CpeFactory cpeFactory;
	
	private SpecimenRequirementFactory srFactory;

	private DaoFactory daoFactory;

	private PrivilegeService privilegeSvc;

	public CollectionProtocolFactory getCpFactory() {
		return cpFactory;
	}

	public void setCpFactory(CollectionProtocolFactory cpFactory) {
		this.cpFactory = cpFactory;
	}

	public CpeFactory getCpeFactory() {
		return cpeFactory;
	}

	public void setCpeFactory(CpeFactory cpeFactory) {
		this.cpeFactory = cpeFactory;
	}
	
	public SpecimenRequirementFactory getSrFactory() {
		return srFactory;
	}

	public void setSrFactory(SpecimenRequirementFactory srFactory) {
		this.srFactory = srFactory;
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
		
		//TODO: integrate privileges with rbac
//		List<Long> allowedCpIds = privilegeSvc.getCpList(
//				getUserId(req), 
//				PrivilegeType.REGISTRATION.name(),req.isChkPrivileges());
		
		List<CollectionProtocolSummary> result = new ArrayList<CollectionProtocolSummary>();
		for (CollectionProtocolSummary collectionProtocolSummary : cpList) {
			//TODO: uncomment below lines once rbac is integrted with module
//			if (allowedCpIds.contains(collectionProtocolSummary.getId())) {
				result.add(collectionProtocolSummary);
//			}
		}
		
		Collections.sort(result);
		return AllCollectionProtocolsEvent.ok(result);
	}
	
	@Override
	@PlusTransactional
	public CollectionProtocolDetailEvent getCollectionProtocol(ReqCollectionProtocolEvent req) {
		Long cpId = req.getCpId();
		
		CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(cpId);
		if (cp == null) {
			return CollectionProtocolDetailEvent.notFound(cpId);
		}
		
		return CollectionProtocolDetailEvent.ok(CollectionProtocolDetail.from(cp));
	}
	
	@Override
	@PlusTransactional
	public RegisteredParticipantsEvent getRegisteredParticipants(ReqRegisteredParticipantsEvent req) {
		try {
			Long cpId = req.getCpId();
			String searchStr = req.getSearchString();

			//TODO: Add rbac module below to use phi details
			//boolean includePhi = privilegeSvc.hasPrivilege(getUserId(req), cpId, Permissions.REGISTRATION);
			boolean includePhi = true;
			
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

		CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(cpId);
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
	public ConsentTiersEvent getConsentTiers(ReqConsentTiersEvent req) {
		Long cpId = req.getCpId();

		try {
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(cpId);
			if (cp == null) {
				return ConsentTiersEvent.notFound(cpId);
			}

			return ConsentTiersEvent.ok(cpId, ConsentTierDetail.from(cp.getConsentTier()));
		} catch (Exception e) {
			return ConsentTiersEvent.serverError(cpId, e);
		}
	}
	
	@Override
	@PlusTransactional
	public ConsentTierOpRespEvent updateConsentTier(ConsentTierOpEvent req) {
		Long cpId = req.getCpId();
		try {
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(cpId);
			if (cp == null) {
				return ConsentTierOpRespEvent.notFound(cpId);
			}
			
			ConsentTierDetail input = req.getConsentTier();
			ConsentTier resp = null;
			
			switch (req.getOp()) {
				case ADD:
					resp = cp.addConsentTier(input.toConsentTier());
					break;
					
				case UPDATE:
					resp = cp.updateConsentTier(input.toConsentTier());
					break;
					
				case REMOVE:
					resp = cp.removeConsentTier(input.getId());
					break;			    
			}
			
			if (resp != null) {
				daoFactory.getCollectionProtocolDao().saveOrUpdate(cp, true);
			}
						
			return ConsentTierOpRespEvent.ok(cpId, ConsentTierDetail.from(resp));
		} catch (IllegalArgumentException iae) {
			return ConsentTierOpRespEvent.badRequest(cpId, iae);
		} catch (Exception e) {
			return ConsentTierOpRespEvent.serverError(cpId, e);
		}		
	}	

	@Override
	@PlusTransactional
	public CpeListEvent getProtocolEvents(ReqCpeListEvent req) {
		Long cpId = req.getCpId();
		
		try {
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(cpId);
			if (cp == null) {
				return CpeListEvent.notFound(cpId);
			}
			
			return CpeListEvent.ok(
					cpId, 
					CollectionProtocolEventDetail.from(cp.getCollectionProtocolEvents()));
		} catch (Exception e) {
			return CpeListEvent.serverError(cpId, e);
		}		
	}
	
	@Override
	@PlusTransactional
	public CpeAddedEvent addEvent(AddCpeEvent req) {
		try {
			CollectionProtocolEvent cpe = cpeFactory.createCpe(req.getCpe());			
			CollectionProtocol cp = cpe.getCollectionProtocol();
			cp.addCpe(cpe);
			
			daoFactory.getCollectionProtocolDao().saveOrUpdate(cp, true);			
			return CpeAddedEvent.ok(CollectionProtocolEventDetail.from(cpe));			
		} catch (IllegalArgumentException iae) {
			return CpeAddedEvent.badRequest(iae);
		} catch (ObjectCreationException oce) {		
			return CpeAddedEvent.badRequest(oce);
		} catch (Exception e) {
			return CpeAddedEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public CpeUpdatedEvent updateEvent(UpdateCpeEvent req) {
		try {
			CollectionProtocolEvent cpe = cpeFactory.createCpe(req.getCpe());			
			CollectionProtocol cp = cpe.getCollectionProtocol();			
			cp.updateCpe(cpe);
						
			return CpeUpdatedEvent.ok(CollectionProtocolEventDetail.from(cpe));			
		} catch (IllegalArgumentException iae) {
			return CpeUpdatedEvent.badRequest(iae);
		} catch (ObjectCreationException oce) {		
			return CpeUpdatedEvent.badRequest(oce);
		} catch (Exception e) {
			return CpeUpdatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public SpecimenRequirementsEvent getSpecimenRequirments(ReqSpecimenRequirementsEvent req) {
		Long cpeId = req.getCpeId();
		try {
			CollectionProtocolEvent cpe = daoFactory.getCollectionProtocolDao().getCpe(cpeId);
			if (cpe == null) {
				return SpecimenRequirementsEvent.notFound(cpeId);
			}
			
			return SpecimenRequirementsEvent.ok(
					cpeId, 
					SpecimenRequirementDetail.from(cpe.getTopLevelAnticipatedSpecimens()));
		} catch (Exception e) {
			return SpecimenRequirementsEvent.serverError(cpeId, e);
		}
	}
	
	@Override
	@PlusTransactional
	public SpecimenRequirementAddedEvent addSpecimenRequirement(AddSpecimenRequirementEvent req) {
		try {
			SpecimenRequirement requirement = srFactory.createSpecimenRequirement(req.getRequirement());
			CollectionProtocolEvent cpe = requirement.getCollectionProtocolEvent();

			cpe.addSpecimenRequirement(requirement);
			daoFactory.getCollectionProtocolDao().saveCpe(cpe, true);
			return SpecimenRequirementAddedEvent.ok(SpecimenRequirementDetail.from(requirement));
		} catch (ObjectCreationException oce) {
			return SpecimenRequirementAddedEvent.badRequest(oce);
		} catch (Exception e) {
			return SpecimenRequirementAddedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public AliquotsRequirementCreatedEvent createAliquots(CreateAliquotsRequirementEvent req) {
		try {
			AliquotSpecimensRequirement requirement = req.getRequirement();
			List<SpecimenRequirement> aliquots = srFactory.createAliquots(requirement);
			
			SpecimenRequirement parent = daoFactory.getSpecimenRequirementDao().getById(requirement.getParentSrId());
			parent.addChildRequirements(aliquots);
			
			daoFactory.getSpecimenRequirementDao().saveOrUpdate(parent, true);
			return AliquotsRequirementCreatedEvent.ok(SpecimenRequirementDetail.from(aliquots));
		} catch (IllegalArgumentException iae) {
			return AliquotsRequirementCreatedEvent.badRequest(iae);
		} catch (Exception e) {
			return AliquotsRequirementCreatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public DerivedSpecimenReqCreatedEvent createDerived(CreateDerivedSpecimenReqEvent req) {
		try {
			DerivedSpecimenRequirement requirement = req.getRequirement();
			SpecimenRequirement derived = srFactory.createDerived(requirement);			
			daoFactory.getSpecimenRequirementDao().saveOrUpdate(derived, true);
			return DerivedSpecimenReqCreatedEvent.ok(SpecimenRequirementDetail.from(derived));
		} catch (ObjectCreationException oce) {
			return DerivedSpecimenReqCreatedEvent.badRequest(oce);
		} catch (Exception e) {
			return DerivedSpecimenReqCreatedEvent.serverError(e);
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
