
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.AliquotSpecimensRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTier;
import com.krishagni.catissueplus.core.biospecimen.domain.DerivedSpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenRequirementFactory;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEventDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOp;
import com.krishagni.catissueplus.core.biospecimen.events.CprSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ListCpCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.CprListCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class CollectionProtocolServiceImpl implements CollectionProtocolService {

	private CollectionProtocolFactory cpFactory;
	
	private CpeFactory cpeFactory;
	
	private SpecimenRequirementFactory srFactory;

	private DaoFactory daoFactory;

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

	@Override
	@PlusTransactional
	public ResponseEvent<List<CollectionProtocolSummary>> getProtocols(RequestEvent<ListCpCriteria> req) {
		ListCpCriteria crit = req.getPayload();
		List<CollectionProtocolSummary> cpList = daoFactory.getCollectionProtocolDao()
				.getCollectionProtocols(crit.includePi(), crit.includeStat());
		
		List<CollectionProtocolSummary> result = new ArrayList<CollectionProtocolSummary>();
		for (CollectionProtocolSummary collectionProtocolSummary : cpList) {
			result.add(collectionProtocolSummary);
		}
		
		Collections.sort(result);
		return ResponseEvent.response(result);
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolDetail> getCollectionProtocol(RequestEvent<Long> req) {
		Long cpId = req.getPayload();
		
		CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(cpId);
		if (cp == null) {
			return ResponseEvent.userError(CpErrorCode.NOT_FOUND);
		}
		
		return ResponseEvent.response(CollectionProtocolDetail.from(cp));
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<CprSummary>> getRegisteredParticipants(RequestEvent<CprListCriteria> req) {
		try {			
			CprListCriteria listCrit = req.getPayload();
			return ResponseEvent.response(daoFactory.getCprDao().getCprList(listCrit));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolDetail> createCollectionProtocol(RequestEvent<CollectionProtocolDetail> req) {
		try {
			CollectionProtocol cp = cpFactory.createCollectionProtocol(req.getPayload());

			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueTitle(cp.getTitle(), ose);
			ose.checkAndThrow();

			daoFactory.getCollectionProtocolDao().saveOrUpdate(cp);
			return ResponseEvent.response(CollectionProtocolDetail.from(cp));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<ConsentTierDetail>> getConsentTiers(RequestEvent<Long> req) {
		Long cpId = req.getPayload();

		try {
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(cpId);
			if (cp == null) {
				return ResponseEvent.userError(CpErrorCode.NOT_FOUND);
			}

			return ResponseEvent.response(ConsentTierDetail.from(cp.getConsentTier()));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ConsentTierDetail> updateConsentTier(RequestEvent<ConsentTierOp> req) {
		ConsentTierOp opDetail = req.getPayload();
		
		Long cpId = opDetail.getCpId();
		try {
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(cpId);
			if (cp == null) {
				return ResponseEvent.userError(CpErrorCode.NOT_FOUND);
			}
			
			ConsentTierDetail input = opDetail.getConsentTier();
			ConsentTier resp = null;
			
			switch (opDetail.getOp()) {
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
						
			return ResponseEvent.response(ConsentTierDetail.from(resp));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}		
	}	

	@Override
	@PlusTransactional
	public ResponseEvent<List<CollectionProtocolEventDetail>> getProtocolEvents(RequestEvent<Long> req) {
		Long cpId = req.getPayload();
		
		try {
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(cpId);
			if (cp == null) {
				return ResponseEvent.userError(CpErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(CollectionProtocolEventDetail.from(cp.getCollectionProtocolEvents()));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}		
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolEventDetail> getProtocolEvent(RequestEvent<Long> req) {
		Long cpeId = req.getPayload();
		
		try {
			CollectionProtocolEvent cpe = daoFactory.getCollectionProtocolDao().getCpe(cpeId);
			if (cpe == null) {
				return ResponseEvent.userError(CpeErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(CollectionProtocolEventDetail.from(cpe));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
		
	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolEventDetail> addEvent(RequestEvent<CollectionProtocolEventDetail> req) {
		try {
			CollectionProtocolEvent cpe = cpeFactory.createCpe(req.getPayload());			
			CollectionProtocol cp = cpe.getCollectionProtocol();
			cp.addCpe(cpe);
			
			daoFactory.getCollectionProtocolDao().saveOrUpdate(cp, true);			
			return ResponseEvent.response(CollectionProtocolEventDetail.from(cpe));			
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolEventDetail> updateEvent(RequestEvent<CollectionProtocolEventDetail> req) {
		try {
			CollectionProtocolEvent cpe = cpeFactory.createCpe(req.getPayload());			
			CollectionProtocol cp = cpe.getCollectionProtocol();			
			cp.updateCpe(cpe);
						
			return ResponseEvent.response(CollectionProtocolEventDetail.from(cpe));			
		} catch (OpenSpecimenException ose) {		
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenRequirementDetail>> getSpecimenRequirments(RequestEvent<Long> req) {
		Long cpeId = req.getPayload();
		try {
			CollectionProtocolEvent cpe = daoFactory.getCollectionProtocolDao().getCpe(cpeId);
			if (cpe == null) {
				return ResponseEvent.userError(CpeErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(SpecimenRequirementDetail.from(cpe.getTopLevelAnticipatedSpecimens()));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenRequirementDetail> addSpecimenRequirement(RequestEvent<SpecimenRequirementDetail> req) {
		try {
			SpecimenRequirement requirement = srFactory.createSpecimenRequirement(req.getPayload());
			CollectionProtocolEvent cpe = requirement.getCollectionProtocolEvent();

			cpe.addSpecimenRequirement(requirement);
			daoFactory.getCollectionProtocolDao().saveCpe(cpe, true);
			return ResponseEvent.response(SpecimenRequirementDetail.from(requirement));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenRequirementDetail>> createAliquots(RequestEvent<AliquotSpecimensRequirement> req) {
		try {
			AliquotSpecimensRequirement requirement = req.getPayload();
			List<SpecimenRequirement> aliquots = srFactory.createAliquots(requirement);
			
			SpecimenRequirement parent = daoFactory.getSpecimenRequirementDao().getById(requirement.getParentSrId());
			parent.addChildRequirements(aliquots);
			
			daoFactory.getSpecimenRequirementDao().saveOrUpdate(parent, true);
			return ResponseEvent.response(SpecimenRequirementDetail.from(aliquots));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenRequirementDetail> createDerived(RequestEvent<DerivedSpecimenRequirement> req) {
		try {
			DerivedSpecimenRequirement requirement = req.getPayload();
			SpecimenRequirement derived = srFactory.createDerived(requirement);			
			daoFactory.getSpecimenRequirementDao().saveOrUpdate(derived, true);
			return ResponseEvent.response(SpecimenRequirementDetail.from(derived));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private void ensureUniqueTitle(String title, OpenSpecimenException ose) {
		CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(title);
		if (cp != null) {
			ose.addError(CpErrorCode.DUP_TITLE);
		}
	}
}
