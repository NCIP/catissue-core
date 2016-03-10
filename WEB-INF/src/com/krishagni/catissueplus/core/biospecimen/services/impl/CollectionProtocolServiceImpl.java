
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.AliquotSpecimensRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTier;
import com.krishagni.catissueplus.core.biospecimen.domain.CpWorkflowConfig;
import com.krishagni.catissueplus.core.biospecimen.domain.CpWorkflowConfig.Workflow;
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
import com.krishagni.catissueplus.core.biospecimen.events.CopyCpOpDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOp.OP;
import com.krishagni.catissueplus.core.biospecimen.events.CopyCpeOpDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CpQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.CpWorkflowCfgDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CpWorkflowCfgDetail.WorkflowDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CprSummary;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenPoolRequirements;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CpListCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.CprListCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr.ParticipantReadAccess;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ObjectStateParamsResolver;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.rbac.common.errors.RbacErrorCode;
import com.krishagni.rbac.service.RbacService;


public class CollectionProtocolServiceImpl implements CollectionProtocolService, ObjectStateParamsResolver {

	private CollectionProtocolFactory cpFactory;
	
	private CpeFactory cpeFactory;
	
	private SpecimenRequirementFactory srFactory;

	private DaoFactory daoFactory;
	
	private RbacService rbacSvc;
	
	public void setCpFactory(CollectionProtocolFactory cpFactory) {
		this.cpFactory = cpFactory;
	}

	public void setCpeFactory(CpeFactory cpeFactory) {
		this.cpeFactory = cpeFactory;
	}
	
	public void setSrFactory(SpecimenRequirementFactory srFactory) {
		this.srFactory = srFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setRbacSvc(RbacService rbacSvc) {
		this.rbacSvc = rbacSvc;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<CollectionProtocolSummary>> getProtocols(RequestEvent<CpListCriteria> req) {
		try {
			Set<Long> cpIds = AccessCtrlMgr.getInstance().getReadableCpIds();
			
			CpListCriteria crit = req.getPayload();
			if (cpIds != null && cpIds.isEmpty()) {
				return ResponseEvent.response(Collections.<CollectionProtocolSummary>emptyList());
			} else if (cpIds != null) {
				crit.ids(new ArrayList<Long>(cpIds));
			}
			
			List<CollectionProtocolSummary> cpList = daoFactory.getCollectionProtocolDao().getCollectionProtocols(crit);			
			return ResponseEvent.response(cpList);
		} catch (OpenSpecimenException oce) {
			return ResponseEvent.error(oce);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolDetail> getCollectionProtocol(RequestEvent<CpQueryCriteria> req) {
		try {
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

			AccessCtrlMgr.getInstance().ensureReadCpRights(cp);
			return ResponseEvent.response(CollectionProtocolDetail.from(cp, crit.isFullObject()));
		} catch (OpenSpecimenException oce) {
			return ResponseEvent.error(oce);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<CprSummary>> getRegisteredParticipants(RequestEvent<CprListCriteria> req) {
		try { 
			CprListCriteria listCrit = req.getPayload();
			ParticipantReadAccess access = AccessCtrlMgr.getInstance().getParticipantReadAccess(listCrit.cpId());
			//When siteIds is null, access restriction is not enforced based on MRN sites
			if (!access.admin && access.siteIds != null && access.siteIds.isEmpty()) {
				return ResponseEvent.response(Collections.<CprSummary>emptyList());
			} 
			
			listCrit.includePhi(access.phiAccess);
			if (!access.admin) {
				listCrit.siteIds(access.siteIds);
			}
			
			return ResponseEvent.response(daoFactory.getCprDao().getCprList(listCrit));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolDetail> createCollectionProtocol(RequestEvent<CollectionProtocolDetail> req) {
		try {
			CollectionProtocol cp = createCollectionProtocol(req.getPayload(), null, false);
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

			AccessCtrlMgr.getInstance().ensureUpdateCpRights(existingCp);
			CollectionProtocol cp = cpFactory.createCollectionProtocol(detail);
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(cp);
			ensureUsersBelongtoCpSites(cp);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueTitle(existingCp, cp, ose);
			ensureUniqueShortTitle(existingCp, cp, ose);
			ensureUniqueCode(existingCp, cp, ose);
			ensureUniqueCpSiteCode(cp, ose);
			if (!existingCp.isConsentsWaived().equals(cp.isConsentsWaived())) {
			  ensureConsentTierIsEmpty(existingCp, ose);
			}
		
			ose.checkAndThrow();
			
			User oldPI = existingCp.getPrincipalInvestigator();
			boolean piChanged = !oldPI.equals(cp.getPrincipalInvestigator());
			
			Collection<User> addedCoord = CollectionUtils.subtract(cp.getCoordinators(), existingCp.getCoordinators());
			Collection<User> removedCoord = CollectionUtils.subtract(existingCp.getCoordinators(), cp.getCoordinators());
			
			existingCp.update(cp);
			existingCp.addOrUpdateExtension();
			
			// PI role handling
			if (piChanged) {
				removeDefaultPiRoles(cp, oldPI);
				addDefaultPiRoles(cp, cp.getPrincipalInvestigator());
			} 

			// Coordinator Role Handling
			removeDefaultCoordinatorRoles(cp, removedCoord);
			addDefaultCoordinatorRoles(cp, addedCoord);
			
			return ResponseEvent.response(CollectionProtocolDetail.from(existingCp));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolDetail> copyCollectionProtocol(RequestEvent<CopyCpOpDetail> req) {
		try {
			CopyCpOpDetail opDetail = req.getPayload();
			Long cpId = opDetail.getCpId();
			CollectionProtocol existing = daoFactory.getCollectionProtocolDao().getById(cpId);
			if (existing == null) {
				throw OpenSpecimenException.userError(CpeErrorCode.NOT_FOUND, cpId);
			}
			
			AccessCtrlMgr.getInstance().ensureReadCpRights(existing);
			
			CollectionProtocol cp = createCollectionProtocol(opDetail.getCp(), existing, true);
			return ResponseEvent.response(CollectionProtocolDetail.from(cp));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@PlusTransactional
	public ResponseEvent<CollectionProtocolDetail> updateConsentsWaived(RequestEvent<CollectionProtocolDetail> req) {
		try {
			CollectionProtocolDetail detail = req.getPayload();
			CollectionProtocol existingCp = daoFactory.getCollectionProtocolDao().getById(detail.getId());
			if (existingCp == null) {
				return ResponseEvent.userError(CpErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(existingCp);
			
			if (CollectionUtils.isNotEmpty(existingCp.getConsentTier())) {
				return ResponseEvent.userError(CpErrorCode.CONSENT_TIER_FOUND, existingCp.getShortTitle());
			}

			existingCp.setConsentsWaived(detail.getConsentsWaived());
			return ResponseEvent.response(CollectionProtocolDetail.from(existingCp));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@PlusTransactional
	public ResponseEvent<List<DependentEntityDetail>> getCpDependentEntities(RequestEvent<Long> req) {
		try {
			CollectionProtocol existingCp = daoFactory.getCollectionProtocolDao().getById(req.getPayload());
			if (existingCp == null) {
				return ResponseEvent.userError(CpErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(existingCp.getDependentEntities());
 		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolDetail> deleteCollectionProtocol(RequestEvent<Long> req) {
		try {
			CollectionProtocol existingCp = daoFactory.getCollectionProtocolDao().getById(req.getPayload());
			if (existingCp == null) {
				return ResponseEvent.userError(StorageContainerErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureDeleteCpRights(existingCp);
			removeDefaultPiRoles(existingCp, existingCp.getPrincipalInvestigator());
			removeDefaultCoordinatorRoles(existingCp, existingCp.getCoordinators());
			existingCp.delete();
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
			CollectionProtocolDetail cpDetail = req.getPayload();
			
			ResponseEvent<CollectionProtocolDetail> resp = createCollectionProtocol(req);
			resp.throwErrorIfUnsuccessful();
						
			importConsents(resp.getPayload().getId(), cpDetail.getConsents());
			importEvents(cpDetail.getTitle(), cpDetail.getEvents());
			
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
			
			AccessCtrlMgr.getInstance().ensureReadCpRights(cp);
			return ResponseEvent.response(ConsentTierDetail.from(cp.getConsentTier()));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
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
			
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(cp);
			
			if (cp.isConsentsWaived()) {
				return ResponseEvent.userError(CpErrorCode.CONSENTS_WAIVED, cp.getShortTitle());
			}
			
			ConsentTierDetail input = opDetail.getConsentTier();
			ConsentTier resp = null;			
			switch (opDetail.getOp()) {
				case ADD:
					ensureUniqueConsentStatement(input, cp);
					resp = cp.addConsentTier(input.toConsentTier());
					break;
					
				case UPDATE:
					ensureUniqueConsentStatement(input, cp);
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
	public ResponseEvent<List<DependentEntityDetail>> getConsentDependentEntities(RequestEvent<ConsentTierDetail> req) {
		try {
			ConsentTierDetail consentTierDetail = req.getPayload();
			ConsentTier consentTier = getConsentTier(consentTierDetail);
			return ResponseEvent.response(consentTier.getDependentEntities());
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch(Exception e) {
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

			AccessCtrlMgr.getInstance().ensureReadCpRights(cp);
			return ResponseEvent.response(CollectionProtocolEventDetail.from(cp.getOrderedCpeList()));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
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

			CollectionProtocol cp = cpe.getCollectionProtocol();
			AccessCtrlMgr.getInstance().ensureReadCpRights(cp);

			int minEventPoint = daoFactory.getCollectionProtocolDao().getMinEventPoint(cp.getId());
			cpe.setOffset(minEventPoint > 0 ? 0 : minEventPoint);

			return ResponseEvent.response(CollectionProtocolEventDetail.from(cpe));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		}  catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
		
	@Override
	@PlusTransactional
	public ResponseEvent<CollectionProtocolEventDetail> addEvent(RequestEvent<CollectionProtocolEventDetail> req) {
		try {
			CollectionProtocolEvent cpe = cpeFactory.createCpe(req.getPayload());			
			CollectionProtocol cp = cpe.getCollectionProtocol();
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(cp);
			
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
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(cp);
			
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
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(cp);
			
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
	public ResponseEvent<CollectionProtocolEventDetail> deleteEvent(RequestEvent<Long> req) {
		try {
			Long cpeId = req.getPayload();
			CollectionProtocolEvent cpe = daoFactory.getCollectionProtocolDao().getCpe(cpeId);
			if (cpe == null) {
				throw OpenSpecimenException.userError(CpeErrorCode.NOT_FOUND);
			}
			
			CollectionProtocol cp = cpe.getCollectionProtocol();
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(cp);
			
			cpe.delete();
			daoFactory.getCollectionProtocolDao().saveCpe(cpe);
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
			
			AccessCtrlMgr.getInstance().ensureReadCpRights(cpe.getCollectionProtocol());
			return ResponseEvent.response(SpecimenRequirementDetail.from(cpe.getTopLevelAnticipatedSpecimens()));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		}  catch (Exception e) {
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
			
			AccessCtrlMgr.getInstance().ensureReadCpRights(sr.getCollectionProtocol());
			return ResponseEvent.response(SpecimenRequirementDetail.from(sr));				
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
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
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(cpe.getCollectionProtocol());
			
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
	public ResponseEvent<List<SpecimenRequirementDetail>> addSpecimenPoolReqs(RequestEvent<SpecimenPoolRequirements> req) {
		try {
			List<SpecimenRequirement> spmnPoolReqs = srFactory.createSpecimenPoolReqs(req.getPayload());

			SpecimenRequirement pooledReq = spmnPoolReqs.iterator().next().getPooledSpecimenRequirement();
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(pooledReq.getCollectionProtocol());

			pooledReq.getCollectionProtocolEvent().ensureUniqueSrCodes(spmnPoolReqs);
			pooledReq.addSpecimenPoolReqs(spmnPoolReqs);
			daoFactory.getSpecimenRequirementDao().saveOrUpdate(pooledReq, true);
			return ResponseEvent.response(SpecimenRequirementDetail.from(spmnPoolReqs));
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
			return ResponseEvent.response(SpecimenRequirementDetail.from(createAliquots(req.getPayload())));
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
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(derived.getCollectionProtocol());
			
			if (StringUtils.isNotBlank(derived.getCode())) {
				if (derived.getCollectionProtocolEvent().getSrByCode(derived.getCode()) != null) {
					return ResponseEvent.userError(SrErrorCode.DUP_CODE, derived.getCode());
				}
			}
			
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
	public ResponseEvent<SpecimenRequirementDetail> updateSpecimenRequirement(RequestEvent<SpecimenRequirementDetail> req) {
		try {
			SpecimenRequirementDetail detail = req.getPayload();
			Long srId = detail.getId();
			SpecimenRequirement sr = daoFactory.getSpecimenRequirementDao().getById(srId);
			if (sr == null) {
				throw OpenSpecimenException.userError(SrErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(sr.getCollectionProtocol());
			SpecimenRequirement partial = srFactory.createForUpdate(sr.getLineage(), detail);
			if (isSpecimenClassOrTypeChanged(sr, partial)) {
				ensureSpecimensNotCollected(sr);
			}
			
			sr.update(partial);
			return ResponseEvent.response(SpecimenRequirementDetail.from(sr));
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
			
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(sr.getCollectionProtocol());
			SpecimenRequirement copy = sr.deepCopy(null);
			daoFactory.getSpecimenRequirementDao().saveOrUpdate(copy, true);
			return ResponseEvent.response(SpecimenRequirementDetail.from(copy));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenRequirementDetail> deleteSpecimenRequirement(RequestEvent<Long> req) {
		try {
			Long srId = req.getPayload();
			SpecimenRequirement sr = daoFactory.getSpecimenRequirementDao().getById(srId);
			if (sr == null) {
				throw OpenSpecimenException.userError(SrErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(sr.getCollectionProtocol());
			sr.delete();
			daoFactory.getSpecimenRequirementDao().saveOrUpdate(sr);
			return ResponseEvent.response(SpecimenRequirementDetail.from(sr));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<Integer> getSrSpecimensCount(RequestEvent<Long> req) {
		try {
			Long srId = req.getPayload();
			SpecimenRequirement sr = daoFactory.getSpecimenRequirementDao().getById(srId);
			if (sr == null) {
				throw OpenSpecimenException.userError(SrErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(
					daoFactory.getSpecimenRequirementDao().getSpecimensCount(srId));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<CpWorkflowCfgDetail> getWorkflows(RequestEvent<Long> req) {
		Long cpId = req.getPayload();
		CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(cpId);
		if (cp == null) {
			return ResponseEvent.userError(CpErrorCode.NOT_FOUND);
		}
		
		CpWorkflowConfig cfg = daoFactory.getCollectionProtocolDao().getCpWorkflows(cpId);
		if (cfg == null) {
			cfg = new CpWorkflowConfig();
			cfg.setCp(cp);
		}
		
		return ResponseEvent.response(CpWorkflowCfgDetail.from(cfg));
	}

	@Override
	@PlusTransactional
	public ResponseEvent<CpWorkflowCfgDetail> saveWorkflows(RequestEvent<CpWorkflowCfgDetail> req) {
		try {
			CpWorkflowCfgDetail input = req.getPayload();
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(input.getCpId());
			if (cp == null) {
				return ResponseEvent.userError(CpErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(cp);
			CpWorkflowConfig cfg = daoFactory.getCollectionProtocolDao().getCpWorkflows(input.getCpId());
			if (cfg == null) {
				cfg = new CpWorkflowConfig();
				cfg.setCp(cp);
			}
			
			cfg.getWorkflows().clear();
			for (WorkflowDetail detail : input.getWorkflows().values()) {
				Workflow wf = new Workflow();
				BeanUtils.copyProperties(detail, wf);
				cfg.getWorkflows().put(wf.getName(), wf);
			}
			
			daoFactory.getCollectionProtocolDao().saveCpWorkflows(cfg);
			return ResponseEvent.response(CpWorkflowCfgDetail.from(cfg));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<CollectionProtocolSummary>> getRegisterEnabledCps(List<String> siteNames, String searchTitle) {
		try {
			Set<Long> cpIds = AccessCtrlMgr.getInstance().getRegisterEnabledCpIds(siteNames);
			
			CpListCriteria crit = new CpListCriteria()
				.title(searchTitle);
			if (cpIds != null && cpIds.isEmpty()) {
				return ResponseEvent.response(Collections.<CollectionProtocolSummary>emptyList());
			} else if (cpIds != null) {
				crit.ids(new ArrayList<Long>(cpIds));
			}
			
			List<CollectionProtocolSummary> cpList = daoFactory.getCollectionProtocolDao().getCollectionProtocols(crit);			
			return ResponseEvent.response(cpList);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}


	@Override
	public String getObjectName() {
		return "cp";
	}

	@Override
	@PlusTransactional
	public Map<String, Object> resolve(String key, Object value) {
		if (key.equals("id")) {
			value = Long.valueOf(value.toString());
		}

		return daoFactory.getCollectionProtocolDao().getCpIds(key, value);
	}
	
	private CollectionProtocol createCollectionProtocol(CollectionProtocolDetail detail, CollectionProtocol existing, boolean createCopy) {
		CollectionProtocol cp = null;
		if (!createCopy) {
			cp = cpFactory.createCollectionProtocol(detail);
		} else {
			cp = cpFactory.createCpCopy(detail, existing);
		}
		
		AccessCtrlMgr.getInstance().ensureCreateCpRights(cp);
		ensureUsersBelongtoCpSites(cp);
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		ensureUniqueTitle(null, cp, ose);
		ensureUniqueShortTitle(null, cp, ose);
		ensureUniqueCode(null, cp, ose);
		ensureUniqueCpSiteCode(cp, ose);
		ose.checkAndThrow();

		daoFactory.getCollectionProtocolDao().saveOrUpdate(cp, true);
		cp.addOrUpdateExtension();
		
		//Assign default roles to PI and Coordinators
		addDefaultPiRoles(cp, cp.getPrincipalInvestigator());
		addDefaultCoordinatorRoles(cp, cp.getCoordinators());
		
		return cp;
	}

	private void ensureUsersBelongtoCpSites(CollectionProtocol cp) {
		ensureCreatorBelongToCpSites(cp);
	}
	
	private void ensureCreatorBelongToCpSites(CollectionProtocol cp) {
		User user = AuthUtil.getCurrentUser();
		if (user.isAdmin()) {
			return;
		}
		
		user = loadUser(user);
		
		Set<Site> userSites = user.getInstitute().getSites();
		Set<Site> cpSites = cp.getRepositories();		
		if (!userSites.containsAll(cpSites)) {
			throw OpenSpecimenException.userError(CpErrorCode.CREATOR_DOES_NOT_BELONG_CP_REPOS);
		}
	}
	
	private User loadUser(User user) {
		return daoFactory.getUserDao().getById(user.getId());
	}

	private void ensureUniqueTitle(CollectionProtocol existingCp, CollectionProtocol cp, OpenSpecimenException ose) {
		String title = cp.getTitle();
		if (existingCp != null && existingCp.getTitle().equals(title)) {
			return;
		}
		
		CollectionProtocol dbCp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(title);
		if (dbCp != null) {
			ose.addError(CpErrorCode.DUP_TITLE, title);
		}		
	}
	
	private void ensureUniqueShortTitle(CollectionProtocol existingCp, CollectionProtocol cp, OpenSpecimenException ose) {
		String shortTitle = cp.getShortTitle();
		if (existingCp != null && existingCp.getShortTitle().equals(shortTitle)) {
			return;
		}
		
		CollectionProtocol dbCp = daoFactory.getCollectionProtocolDao().getCpByShortTitle(shortTitle);
		if (dbCp != null) {
			ose.addError(CpErrorCode.DUP_SHORT_TITLE, shortTitle);
		}
	}
	
	private void ensureUniqueCode(CollectionProtocol existingCp, CollectionProtocol cp, OpenSpecimenException ose) {
		String code = cp.getCode();
		if (StringUtils.isBlank(code)) {
			return;
		}
		
		if (existingCp != null && code.equals(existingCp.getCode())) {
			return;
		}
		
		CollectionProtocol dbCp = daoFactory.getCollectionProtocolDao().getCpByCode(code);
		if (dbCp != null) {
			ose.addError(CpErrorCode.DUP_CODE, code);
		}
	}
	
	private void ensureUniqueCpSiteCode(CollectionProtocol cp, OpenSpecimenException ose) {
		List<String> codes = Utility.<List<String>>collect(cp.getSites(), "code");
		codes.removeAll(Arrays.asList(new String[] {null, ""}));
		
		Set<String> uniqueCodes = new HashSet<String>(codes);
		if (codes.size() != uniqueCodes.size()) {
			ose.addError(CpErrorCode.DUP_CP_SITE_CODES, codes);
		}
	}
	
	private void ensureConsentTierIsEmpty(CollectionProtocol existingCp, OpenSpecimenException ose) {
		if (CollectionUtils.isNotEmpty(existingCp.getConsentTier())) {
			ose.addError(CpErrorCode.CONSENT_TIER_FOUND, existingCp.getShortTitle());
		}
	}
	
	private void importConsents(Long cpId, List<ConsentTierDetail> consents) {
		if (CollectionUtils.isEmpty(consents)) {
			return;			
		}
		
		for (ConsentTierDetail consent : consents) {
			ConsentTierOp addOp = new ConsentTierOp();
			addOp.setConsentTier(consent);
			addOp.setCpId(cpId);
			addOp.setOp(OP.ADD);
			
			RequestEvent<ConsentTierOp> req = new RequestEvent<ConsentTierOp>(addOp);					
			ResponseEvent<ConsentTierDetail> resp = updateConsentTier(req);
			resp.throwErrorIfUnsuccessful();
		}
	}
	
	private void importEvents(String cpTitle, List<CollectionProtocolEventDetail> events) {
		if (CollectionUtils.isEmpty(events)) {
			return;
		}
		
		for (CollectionProtocolEventDetail event : events) {
			event.setCollectionProtocol(cpTitle);
			RequestEvent<CollectionProtocolEventDetail> req = new RequestEvent<CollectionProtocolEventDetail>(event);
			ResponseEvent<CollectionProtocolEventDetail> resp = addEvent(req);
			resp.throwErrorIfUnsuccessful();
			
			Long eventId = resp.getPayload().getId();
			importSpecimenReqs(eventId, null, event.getSpecimenRequirements());
		}
	}
	
	private void importSpecimenReqs(Long eventId, Long parentSrId, List<SpecimenRequirementDetail> srs) {
		if (CollectionUtils.isEmpty(srs)) {
			return;
		}
		
		for (SpecimenRequirementDetail sr : srs) {
			sr.setEventId(eventId);
			
			if (sr.getLineage().equals(Specimen.NEW)) {
				RequestEvent<SpecimenRequirementDetail> req = new RequestEvent<SpecimenRequirementDetail>(sr);
				ResponseEvent<SpecimenRequirementDetail> resp = addSpecimenRequirement(req);
				resp.throwErrorIfUnsuccessful();
				
				importSpecimenReqs(eventId, resp.getPayload().getId(), sr.getChildren());
			} else if (parentSrId != null && sr.getLineage().equals(Specimen.ALIQUOT)) {				
				AliquotSpecimensRequirement aliquotReq = sr.toAliquotRequirement(parentSrId, 1);
				List<SpecimenRequirement> aliquots = createAliquots(aliquotReq);

				if (StringUtils.isNotBlank(sr.getCode())) {
					aliquots.get(0).setCode(sr.getCode());
				}
				
				importSpecimenReqs(eventId, aliquots.get(0).getId(), sr.getChildren());
			} else if (parentSrId != null && sr.getLineage().equals(Specimen.DERIVED)) {
				DerivedSpecimenRequirement derivedReq = sr.toDerivedRequirement(parentSrId);
				ResponseEvent<SpecimenRequirementDetail> resp = createDerived(new RequestEvent<DerivedSpecimenRequirement>(derivedReq));
				resp.throwErrorIfUnsuccessful();
				
				importSpecimenReqs(eventId, resp.getPayload().getId(), sr.getChildren());
			}			
		}
	}

	private List<SpecimenRequirement> createAliquots(AliquotSpecimensRequirement requirement) {
		List<SpecimenRequirement> aliquots = srFactory.createAliquots(requirement);
		SpecimenRequirement aliquot = aliquots.iterator().next();
		AccessCtrlMgr.getInstance().ensureUpdateCpRights(aliquot.getCollectionProtocol());

		SpecimenRequirement parent = aliquot.getParentSpecimenRequirement();
		if (StringUtils.isNotBlank(requirement.getCode())) {
			setAliquotCode(parent, aliquots, requirement.getCode());
		}

		parent.addChildRequirements(aliquots);
		daoFactory.getSpecimenRequirementDao().saveOrUpdate(parent, true);
		return aliquots;
	}

	private void addDefaultPiRoles(CollectionProtocol cp, User user) {
		try {
			rbacSvc.addSubjectRole(null, cp, user, getDefaultPiRoles());
		} catch (OpenSpecimenException ose) {
			ose.rethrow(RbacErrorCode.ACCESS_DENIED, CpErrorCode.USER_UPDATE_RIGHTS_REQUIRED);
			throw ose;
		}
	}

	private void removeDefaultPiRoles(CollectionProtocol cp, User user) {
		try {
			rbacSvc.removeSubjectRole(null, cp, user, getDefaultPiRoles());
		} catch (OpenSpecimenException ose) {
			ose.rethrow(RbacErrorCode.ACCESS_DENIED, CpErrorCode.USER_UPDATE_RIGHTS_REQUIRED);
		}
	}
	
	private void addDefaultCoordinatorRoles(CollectionProtocol cp, Collection<User> coordinators) {
		try {
			for (User user : coordinators) {
				rbacSvc.addSubjectRole(null, cp, user, getDefaultCoordinatorRoles());
			}
		} catch (OpenSpecimenException ose) {
			ose.rethrow(RbacErrorCode.ACCESS_DENIED, CpErrorCode.USER_UPDATE_RIGHTS_REQUIRED);
		}
	}
	
	private void removeDefaultCoordinatorRoles(CollectionProtocol cp, Collection<User> coordinators) {
		try {
			for (User user : coordinators) {
				rbacSvc.removeSubjectRole(null, cp, user, getDefaultCoordinatorRoles());
			}
		} catch (OpenSpecimenException ose) {
			ose.rethrow(RbacErrorCode.ACCESS_DENIED, CpErrorCode.USER_UPDATE_RIGHTS_REQUIRED);
		}
	}
	
	private String[] getDefaultPiRoles() {
		return new String[] {"Principal Investigator"};
	}
	
	private String[] getDefaultCoordinatorRoles() {
		return new String[] {"Coordinator"};
	}
	
	private ConsentTier getConsentTier(ConsentTierDetail consentTierDetail) {
		CollectionProtocolDao cpDao = daoFactory.getCollectionProtocolDao();
		
		ConsentTier consentTier = null;
		if (consentTierDetail.getId() != null) {
			consentTier = cpDao.getConsentTier(consentTierDetail.getId());
		} else if (StringUtils.isNotBlank(consentTierDetail.getStatement()) && consentTierDetail.getCpId() != null ) {
			consentTier = cpDao.getConsentTierByStatement(consentTierDetail.getCpId(), consentTierDetail.getStatement());
		}
		
		if (consentTier == null) {
			throw OpenSpecimenException.userError(CpErrorCode.CONSENT_TIER_NOT_FOUND);
		}
		
		return consentTier;
	}

	private void ensureUniqueConsentStatement(ConsentTierDetail consentTierDetail, CollectionProtocol cp) {
		for (ConsentTier consentTier : cp.getConsentTier()) {
			if (consentTierDetail.getStatement().equals(consentTier.getStatement()) && 
					consentTier.getId() != consentTierDetail.getId()) {
				throw OpenSpecimenException.userError(CpErrorCode.DUP_CONSENT, consentTier.getStatement());
			}
		}
	}
	
	private void ensureSpecimensNotCollected(SpecimenRequirement sr) {
		int count = daoFactory.getSpecimenRequirementDao().getSpecimensCount(sr.getId());
		if (count > 0) {
			throw OpenSpecimenException.userError(SrErrorCode.CANNOT_CHANGE_CLASS_OR_TYPE);
		}
	}
	
	private boolean isSpecimenClassOrTypeChanged(SpecimenRequirement existingSr, SpecimenRequirement sr) {
		return !existingSr.getSpecimenClass().equals(sr.getSpecimenClass()) || 
				!existingSr.getSpecimenType().equals(sr.getSpecimenType());
	}
	
	private void setAliquotCode(SpecimenRequirement parent, List<SpecimenRequirement> aliquots, String code) {
		Set<String> codes = new HashSet<String>();
		CollectionProtocolEvent cpe = parent.getCollectionProtocolEvent();
		for (SpecimenRequirement sr : cpe.getSpecimenRequirements()) {
			if (StringUtils.isNotBlank(sr.getCode())) {
				codes.add(sr.getCode());
			}
		}

		int count = 1;
		for (SpecimenRequirement sr : aliquots) {
			while (!codes.add(code + count)) {
				count++;
			}

			sr.setCode(code + count++);
		}
	}
}
