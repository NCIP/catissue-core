
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.AliquotSpecimensRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTier;
import com.krishagni.catissueplus.core.biospecimen.domain.DerivedSpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenRequirementFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SrErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEventDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOp;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOp.OP;
import com.krishagni.catissueplus.core.biospecimen.events.CopyCpeOpDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CpQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.CprSummary;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CpListCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.CprListCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.wustl.common.beans.SessionDataBean;

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
	public ResponseEvent<List<CollectionProtocolSummary>> getProtocols(RequestEvent<CpListCriteria> req) {
		CpListCriteria crit = req.getPayload();
		List<CollectionProtocolSummary> cpList = daoFactory.getCollectionProtocolDao()
				.getCollectionProtocols(crit);
		
		List<CollectionProtocolSummary> result = new ArrayList<CollectionProtocolSummary>();
		for (CollectionProtocolSummary collectionProtocolSummary : cpList) {
			result.add(collectionProtocolSummary);
		}
		
		Collections.sort(result);
		return ResponseEvent.response(result);
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolDetail> getCollectionProtocol(RequestEvent<CpQueryCriteria> req) {
		CpQueryCriteria crit = req.getPayload();
		CollectionProtocol cp = null;
		
		if (crit.getId() != null) {
			cp = daoFactory.getCollectionProtocolDao().getById(crit.getId()); 
		} else if (crit.getTitle() != null) {
			cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(crit.getTitle());
		} else if (crit.getShortTitle() != null) {
			cp = daoFactory.getCollectionProtocolDao().getCpByShortTitle(crit.getShortTitle());
		}
		
		if (cp == null) {
			return ResponseEvent.userError(CpErrorCode.NOT_FOUND);
		}
		
		return ResponseEvent.response(CollectionProtocolDetail.from(cp, crit.isFullObject()));
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
			ensureUniqueShortTitle(cp.getShortTitle(), ose);
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
	public ResponseEvent<CollectionProtocolDetail> updateCollectionProtocol(RequestEvent<CollectionProtocolDetail> req) {
		try {
			CollectionProtocolDetail detail = req.getPayload();
			CollectionProtocol existingCp = daoFactory.getCollectionProtocolDao().getById(detail.getId());
			if (existingCp == null) {
				return ResponseEvent.userError(CpErrorCode.NOT_FOUND);
			}

			CollectionProtocol cp = cpFactory.createCollectionProtocol(detail);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueTitle(existingCp, cp, ose);
			ose.checkAndThrow();
			
			existingCp.update(cp);
			return ResponseEvent.response(CollectionProtocolDetail.from(existingCp));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolDetail> importCollectionProtocol(RequestEvent<CollectionProtocolDetail> req) {
		try {
			SessionDataBean sdb = req.getSessionDataBean();
			CollectionProtocolDetail cpDetail = req.getPayload();
			
			ResponseEvent<CollectionProtocolDetail> resp = createCollectionProtocol(req);
			resp.throwErrorIfUnsuccessful();
			
			
			importConsents(resp.getPayload().getId(), sdb, cpDetail.getConsents());
			importEvents(cpDetail.getTitle(), sdb, cpDetail.getEvents());		
			
			return resp;
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
	public ResponseEvent<CollectionProtocolEventDetail> copyEvent(RequestEvent<CopyCpeOpDetail> req) {
		try {
			CollectionProtocolDao cpDao = daoFactory.getCollectionProtocolDao();
			
			CopyCpeOpDetail opDetail = req.getPayload();
			String cpTitle = opDetail.getCollectionProtocol();
			String eventLabel = opDetail.getEventLabel();
			
			CollectionProtocolEvent existing = null;
			if (opDetail.getEventId() != null) {
				existing = cpDao.getCpe(opDetail.getEventId());
			} else if (!StringUtils.isBlank(eventLabel) && !StringUtils.isBlank(cpTitle)) {
				existing = cpDao.getCpeByEventLabel(cpTitle, eventLabel);
			}
			
			if (existing == null) {
				throw OpenSpecimenException.userError(CpeErrorCode.NOT_FOUND);
			}
			
			CollectionProtocol cp = existing.getCollectionProtocol();
			CollectionProtocolEvent cpe = cpeFactory.createCpeCopy(opDetail.getCpe(), existing);
			existing.copySpecimenRequirementsTo(cpe);			
			
			cp.addCpe(cpe);			
			cpDao.saveOrUpdate(cp, true);			
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
	public ResponseEvent<SpecimenRequirementDetail> getSpecimenRequirement(RequestEvent<Long> req) {
		Long reqId = req.getPayload();
		try {
			SpecimenRequirement sr = daoFactory.getSpecimenRequirementDao().getById(reqId);
			if (sr == null) {
				return ResponseEvent.userError(SrErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(SpecimenRequirementDetail.from(sr));				
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
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenRequirementDetail> copySpecimenRequirement(RequestEvent<Long> req) {
		try {
			Long srId = req.getPayload();
			
			SpecimenRequirement sr = daoFactory.getSpecimenRequirementDao().getById(srId);
			if (sr == null) {
				throw OpenSpecimenException.userError(SrErrorCode.NOT_FOUND);
			}
			
			SpecimenRequirement copy = sr.deepCopy(null);
			daoFactory.getSpecimenRequirementDao().saveOrUpdate(copy, true);
			return ResponseEvent.response(SpecimenRequirementDetail.from(copy));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void ensureUniqueTitle(CollectionProtocol existingCp, CollectionProtocol cp, OpenSpecimenException ose) {
		if (!existingCp.getTitle().equals(cp.getTitle())) {
			ensureUniqueTitle(cp.getTitle(), ose);
		}
	}
	
	private void ensureUniqueTitle(String title, OpenSpecimenException ose) {
		CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(title);
		if (cp != null) {
			ose.addError(CpErrorCode.DUP_TITLE);
		}		
	}
	
	private void ensureUniqueShortTitle(String shortTitle, OpenSpecimenException ose) {
		CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getCpByShortTitle(shortTitle);
		if (cp != null) {
			ose.addError(CpErrorCode.DUP_SHORT_TITLE);
		}
	}
	
	private void importConsents(Long cpId, SessionDataBean sdb, List<ConsentTierDetail> consents) {
		if (CollectionUtils.isEmpty(consents)) {
			return;			
		}
		
		for (ConsentTierDetail consent : consents) {
			ConsentTierOp addOp = new ConsentTierOp();
			addOp.setConsentTier(consent);
			addOp.setCpId(cpId);
			addOp.setOp(OP.ADD);
			
			RequestEvent<ConsentTierOp> req = new RequestEvent<ConsentTierOp>(sdb, addOp);					
			ResponseEvent<ConsentTierDetail> resp = updateConsentTier(req);
			resp.throwErrorIfUnsuccessful();
		}
	}
	
	private void importEvents(String cpTitle, SessionDataBean sdb, List<CollectionProtocolEventDetail> events) {
		if (CollectionUtils.isEmpty(events)) {
			return;
		}
		
		for (CollectionProtocolEventDetail event : events) {
			event.setCollectionProtocol(cpTitle);
			RequestEvent<CollectionProtocolEventDetail> req = new RequestEvent<CollectionProtocolEventDetail>(sdb, event);
			ResponseEvent<CollectionProtocolEventDetail> resp = addEvent(req);
			resp.throwErrorIfUnsuccessful();
			
			Long eventId = resp.getPayload().getId();
			importSpecimenReqs(eventId, null, sdb, event.getSpecimenRequirements());
		}
	}
	
	private void importSpecimenReqs(Long eventId, Long parentSrId, SessionDataBean sdb, List<SpecimenRequirementDetail> srs) {
		if (CollectionUtils.isEmpty(srs)) {
			return;
		}
		
		for (SpecimenRequirementDetail sr : srs) {
			sr.setEventId(eventId);
			
			if (sr.getLineage().equals(Specimen.NEW)) {
				RequestEvent<SpecimenRequirementDetail> req = new RequestEvent<SpecimenRequirementDetail>(sdb, sr);
				ResponseEvent<SpecimenRequirementDetail> resp = addSpecimenRequirement(req);
				resp.throwErrorIfUnsuccessful();
				
				importSpecimenReqs(eventId, resp.getPayload().getId(), sdb, sr.getChildren());
			} else if (parentSrId != null && sr.getLineage().equals(Specimen.ALIQUOT)) {				
				AliquotSpecimensRequirement aliquotReq = sr.toAliquotRequirement(parentSrId, 1);
				ResponseEvent<List<SpecimenRequirementDetail>> resp = createAliquots(new RequestEvent<AliquotSpecimensRequirement>(sdb, aliquotReq));
				resp.throwErrorIfUnsuccessful();
				
				importSpecimenReqs(eventId, resp.getPayload().get(0).getId(), sdb, sr.getChildren());
			} else if (parentSrId != null && sr.getLineage().equals(Specimen.DERIVED)) {
				DerivedSpecimenRequirement derivedReq = sr.toDerivedRequirement(parentSrId);
				ResponseEvent<SpecimenRequirementDetail> resp = createDerived(new RequestEvent<DerivedSpecimenRequirement>(sdb, derivedReq));
				resp.throwErrorIfUnsuccessful();
				
				importSpecimenReqs(eventId, resp.getPayload().getId(), sdb, sr.getChildren());
			}			
		}
	}
}
