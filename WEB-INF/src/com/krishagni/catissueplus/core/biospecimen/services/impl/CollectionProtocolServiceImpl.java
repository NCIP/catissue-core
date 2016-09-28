
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.WorkflowUtil;
import com.krishagni.catissueplus.core.biospecimen.domain.AliquotSpecimensRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTier;
import com.krishagni.catissueplus.core.biospecimen.domain.CpReportSettings;
import com.krishagni.catissueplus.core.biospecimen.domain.CpWorkflowConfig;
import com.krishagni.catissueplus.core.biospecimen.domain.CpWorkflowConfig.Workflow;
import com.krishagni.catissueplus.core.biospecimen.domain.DerivedSpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpReportSettingsFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CprErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenRequirementFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SrErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEventDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOp;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOp.OP;
import com.krishagni.catissueplus.core.biospecimen.events.CopyCpOpDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CopyCpeOpDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CpQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.CpReportSettingsDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CpWorkflowCfgDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CpWorkflowCfgDetail.WorkflowDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CprSummary;
import com.krishagni.catissueplus.core.biospecimen.events.FileDetail;
import com.krishagni.catissueplus.core.biospecimen.events.MergeCpDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenPoolRequirements;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CpListCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.CprListCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr.ParticipantReadAccess;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.EntityDeleteResp;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.service.ObjectStateParamsResolver;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.MessageUtil;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.query.Column;
import com.krishagni.catissueplus.core.query.ListConfig;
import com.krishagni.catissueplus.core.query.ListDetail;
import com.krishagni.catissueplus.core.query.ListGenerator;
import com.krishagni.rbac.common.errors.RbacErrorCode;
import com.krishagni.rbac.service.RbacService;


public class CollectionProtocolServiceImpl implements CollectionProtocolService, ObjectStateParamsResolver {

	private ThreadPoolTaskExecutor taskExecutor;

	private CollectionProtocolFactory cpFactory;
	
	private CpeFactory cpeFactory;
	
	private SpecimenRequirementFactory srFactory;

	private DaoFactory daoFactory;
	
	private RbacService rbacSvc;
	
	private EmailService emailService;

	private ListGenerator listGenerator;

	private CpReportSettingsFactory rptSettingsFactory;

	private Map<String, Function<Map<String, Object>, ListConfig>> listConfigFns = new HashMap<>();
	
	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

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

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public void setListGenerator(ListGenerator listGenerator) {
		this.listGenerator = listGenerator;
	}

	public void setRptSettingsFactory(CpReportSettingsFactory rptSettingsFactory) {
		this.rptSettingsFactory = rptSettingsFactory;
	}

	public CollectionProtocolServiceImpl() {
		listConfigFns.put("participant-list-view", this::getParticipantsListConfig);
		listConfigFns.put("specimen-list-view", this::getSpecimenListConfig);
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<CollectionProtocolSummary>> getProtocols(RequestEvent<CpListCriteria> req) {
		try {
			CpListCriteria crit = addCpListCriteria(req.getPayload());
			if (crit == null) {
				return ResponseEvent.response(Collections.emptyList());
			}

			return ResponseEvent.response(daoFactory.getCollectionProtocolDao().getCollectionProtocols(crit));
		} catch (OpenSpecimenException oce) {
			return ResponseEvent.error(oce);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<Long> getProtocolsCount(RequestEvent<CpListCriteria> req) {
		try {
			CpListCriteria crit = addCpListCriteria(req.getPayload());
			if (crit == null) {
				return ResponseEvent.response(0L);
			}
			
			return ResponseEvent.response(daoFactory.getCollectionProtocolDao().getCpCount(crit));
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
			CollectionProtocol cp = getCollectionProtocol(crit.getId(), crit.getTitle(), crit.getShortTitle());
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
			CprListCriteria listCrit = addCprListCriteria(req.getPayload());
			if (listCrit == null) {
				return ResponseEvent.response(Collections.emptyList());
			}

			return ResponseEvent.response(daoFactory.getCprDao().getCprList(listCrit));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Long> getRegisteredParticipantsCount(RequestEvent<CprListCriteria> req) {
		try {
			CprListCriteria listCrit = addCprListCriteria(req.getPayload());
			if (listCrit == null) {
				return ResponseEvent.response(0L);
			}

			return ResponseEvent.response(daoFactory.getCprDao().getCprCount(listCrit));
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
			
			User oldPi = existingCp.getPrincipalInvestigator();
			boolean piChanged = !oldPi.equals(cp.getPrincipalInvestigator());
			
			Collection<User> addedCoord = CollectionUtils.subtract(cp.getCoordinators(), existingCp.getCoordinators());
			Collection<User> removedCoord = CollectionUtils.subtract(existingCp.getCoordinators(), cp.getCoordinators());
			
			existingCp.update(cp);
			existingCp.addOrUpdateExtension();
			
			// PI role handling
			if (piChanged) {
				removeDefaultPiRoles(cp, oldPi);
				addDefaultPiRoles(cp, cp.getPrincipalInvestigator());
			} 

			// Coordinator Role Handling
			removeDefaultCoordinatorRoles(cp, removedCoord);
			addDefaultCoordinatorRoles(cp, addedCoord);

			fixSopDocumentName(existingCp);
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

	@Override
	@PlusTransactional
	public ResponseEvent<MergeCpDetail> mergeCollectionProtocols(RequestEvent<MergeCpDetail> req) {
		AccessCtrlMgr.getInstance().ensureUserIsAdmin();

		CollectionProtocol srcCp = getCollectionProtocol(req.getPayload().getSrcCpShortTitle());
		CollectionProtocol tgtCp = getCollectionProtocol(req.getPayload().getTgtCpShortTitle());

		ensureMergeableCps(srcCp, tgtCp);

		int maxRecords = 30;
		boolean moreRecords = true;
		while (moreRecords) {
			List<CollectionProtocolRegistration> cprs = daoFactory.getCprDao().getCprsByCpId(srcCp.getId(), 0, maxRecords);
			for (CollectionProtocolRegistration srcCpr: cprs) {
				mergeCprIntoCp(srcCpr, tgtCp);
			}

			if (cprs.size() < maxRecords) {
				moreRecords = false;
			}
		}

		return ResponseEvent.response(req.getPayload());
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
	public ResponseEvent<EntityDeleteResp<CollectionProtocolDetail>> deleteCollectionProtocol(RequestEvent<DeleteEntityOp> req) {
		try {
			DeleteEntityOp crit = req.getPayload();

			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(crit.getId());
			if (cp == null) {
				return ResponseEvent.userError(CpErrorCode.NOT_FOUND, crit.getId());
			}

			AccessCtrlMgr.getInstance().ensureDeleteCpRights(cp);
			boolean completed = crit.isForceDelete() ? forceDeleteCp(cp) : deleteCp(cp);

			EntityDeleteResp<CollectionProtocolDetail> resp = new EntityDeleteResp<>();
			resp.setCompleted(completed);
			resp.setEntity(CollectionProtocolDetail.from(cp));
			return ResponseEvent.response(resp);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<File> getSopDocument(RequestEvent<Long> req) {
		try {
			Long cpId = req.getPayload();
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(cpId);
			if (cp == null) {
				return ResponseEvent.userError(CprErrorCode.NOT_FOUND);
			}

			AccessCtrlMgr.getInstance().ensureReadCpRights(cp);

			String filename = cp.getSopDocumentName();
			File file = null;
			if (StringUtils.isBlank(filename)) {
				file = ConfigUtil.getInstance().getFileSetting(ConfigParams.MODULE, ConfigParams.CP_SOP_DOC, null);
			} else {
				file = new File(getSopDocDir() + filename);
				if (!file.exists()) {
					filename = filename.split("_", 2)[1];
					return ResponseEvent.userError(CpErrorCode.SOP_DOC_MOVED_OR_DELETED, cp.getShortTitle(), filename);
				}
			}

			if (file == null) {
				return ResponseEvent.userError(CpErrorCode.SOP_DOC_NOT_FOUND, cp.getShortTitle());
			}

			return ResponseEvent.response(file);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	public ResponseEvent<String> uploadSopDocument(RequestEvent<FileDetail> req) {
		OutputStream out = null;

		try {
			FileDetail detail = req.getPayload();
			String filename = UUID.randomUUID() + "_" + detail.getFilename();

			File file = new File(getSopDocDir() + filename);
			out = new FileOutputStream(file);
			IOUtils.copy(detail.getFileIn(), out);

			return ResponseEvent.response(filename);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		} finally {
			IOUtils.closeQuietly(out);
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
	public ResponseEvent<CpReportSettingsDetail> getReportSettings(RequestEvent<CpQueryCriteria> req) {
		try {
			CpReportSettings settings = getReportSetting(req.getPayload());
			if (settings == null) {
				return ResponseEvent.response(null);
			}

			AccessCtrlMgr.getInstance().ensureReadCpRights(settings.getCp());
			return ResponseEvent.response(CpReportSettingsDetail.from(settings));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<CpReportSettingsDetail> saveReportSettings(RequestEvent<CpReportSettingsDetail> req) {
		try {
			CpReportSettings settings = rptSettingsFactory.createSettings(req.getPayload());
			CollectionProtocol cp = settings.getCp();
			AccessCtrlMgr.getInstance().ensureUpdateCpRights(cp);

			CpReportSettings existing = daoFactory.getCpReportSettingsDao().getByCp(cp.getId());
			if (existing == null) {
				existing = settings;
			} else {
				existing.update(settings);
			}

			daoFactory.getCpReportSettingsDao().saveOrUpdate(existing);
			return ResponseEvent.response(CpReportSettingsDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<CpReportSettingsDetail> deleteReportSettings(RequestEvent<CpQueryCriteria> req) {
		try {
			CpReportSettings settings = getReportSetting(req.getPayload());
			if (settings == null) {
				return ResponseEvent.response(null);
			}

			AccessCtrlMgr.getInstance().ensureUpdateCpRights(settings.getCp());
			settings.delete();
			return ResponseEvent.response(CpReportSettingsDetail.from(settings));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> generateReport(RequestEvent<CpQueryCriteria> req) {
		try {
			CpQueryCriteria crit = req.getPayload();
			CollectionProtocol cp = getCollectionProtocol(crit.getId(), crit.getTitle(), crit.getShortTitle());
			AccessCtrlMgr.getInstance().ensureReadCpRights(cp);

			CpReportSettings cpSettings = daoFactory.getCpReportSettingsDao().getByCp(cp.getId());
			if (cpSettings != null && !cpSettings.isEnabled()) {
				return ResponseEvent.userError(CpErrorCode.RPT_DISABLED, cp.getShortTitle());
			}

			taskExecutor.execute(new CpReportTask(cp.getId()));
			return ResponseEvent.response(true);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<File> getReportFile(Long cpId, String fileId) {
		try {
			CollectionProtocol cp = getCollectionProtocol(cpId, null, null);
			AccessCtrlMgr.getInstance().ensureReadCpRights(cp);

			File file = new CpReportGenerator().getDataFile(cpId, fileId);
			if (file == null) {
				return ResponseEvent.userError(CpErrorCode.RPT_FILE_NOT_FOUND);
			}

			return ResponseEvent.response(file);
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
		CollectionProtocol cp = null;

		CpWorkflowConfig cfg;
		if (cpId == null || cpId == -1L) {
			cfg = WorkflowUtil.getInstance().getSysWorkflows();
		} else {
			cp = daoFactory.getCollectionProtocolDao().getById(cpId);
			if (cp == null) {
				return ResponseEvent.userError(CpErrorCode.NOT_FOUND);
			}

			cfg = daoFactory.getCollectionProtocolDao().getCpWorkflows(cpId);
		}

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
	@PlusTransactional
	public ResponseEvent<ListConfig> getCpListCfg(RequestEvent<Map<String, Object>> req) {
		try {
			Map<String, Object> input = req.getPayload();
			String listName = (String)input.get("listName");

			ListConfig cfg = getListConfig(input, listName, null);
			if (cfg == null) {
				//
				// TODO: return appropriate error code
				//
				return ResponseEvent.response(null);
			}

			cfg.setFilters(listGenerator.getFilters(cfg));
			return ResponseEvent.response(cfg);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ListDetail> getList(RequestEvent<Map<String, Object>> req) {
		try {
			Map<String, Object> listReq = req.getPayload();
			String listName = (String)listReq.get("listName");
			Function<Map<String, Object>, ListConfig> configFn = listConfigFns.get(listName);
			if (configFn == null) {
				return ResponseEvent.response(null);
			}

			ListConfig cfg = configFn.apply(listReq);
			if (cfg == null) {
				return ResponseEvent.response(null);
			}

			return ResponseEvent.response(listGenerator.getList(cfg, (List<Column>)listReq.get("filters")));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Integer> getListSize(RequestEvent<Map<String, Object>> req) {
		try {
			Map<String, Object> listReq = req.getPayload();
			String listName = (String)listReq.get("listName");
			Function<Map<String, Object>, ListConfig> configFn = listConfigFns.get(listName);
			if (configFn == null) {
				return ResponseEvent.response(null);
			}

			ListConfig cfg = configFn.apply(listReq);
			if (cfg == null) {
				return ResponseEvent.response(null);
			}

			return ResponseEvent.response(listGenerator.getListSize(cfg, (List<Column>)listReq.get("filters")));
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

	private CpListCriteria addCpListCriteria(CpListCriteria crit) {
		Set<Long> cpIds = AccessCtrlMgr.getInstance().getReadableCpIds();
		if (cpIds != null && cpIds.isEmpty()) {
			return null;
		} else if (cpIds != null) {
			crit.ids(new ArrayList<>(cpIds));
		}

		return crit;
	}

	private CprListCriteria addCprListCriteria(CprListCriteria listCrit) {
		ParticipantReadAccess access = AccessCtrlMgr.getInstance().getParticipantReadAccess(listCrit.cpId());
		if (!access.admin) {
			if (access.noAccessibleSites() || (!access.phiAccess && listCrit.hasPhiFields())) {
				return null;
			}
		}

		return listCrit.includePhi(access.phiAccess)
			.phiSiteCps(access.phiSiteCps)
			.siteCps(access.siteCps)
			.useMrnSites(AccessCtrlMgr.getInstance().isAccessRestrictedBasedOnMrn());
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
		fixSopDocumentName(cp);
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

	private void fixSopDocumentName(CollectionProtocol cp) {
		if (StringUtils.isBlank(cp.getSopDocumentName())) {
			return;
		}

		String[] nameParts = cp.getSopDocumentName().split("_", 2);
		if (nameParts[0].equals(cp.getId().toString())) {
			return;
		}

		try {
			UUID uuid = UUID.fromString(nameParts[0]);
		} catch (Exception e) {
			throw OpenSpecimenException.userError(CpErrorCode.INVALID_SOP_DOC, cp.getSopDocumentName());
		}

		if (StringUtils.isBlank(nameParts[1])) {
			throw OpenSpecimenException.userError(CpErrorCode.INVALID_SOP_DOC, cp.getSopDocumentName());
		}

		File src = new File(getSopDocDir() + File.separator + cp.getSopDocumentName());
		if (!src.exists()) {
			throw OpenSpecimenException.userError(CpErrorCode.SOP_DOC_MOVED_OR_DELETED, cp.getSopDocumentName(), cp.getShortTitle());
		}

		cp.setSopDocumentName(cp.getId() + "_" + nameParts[1]);

		File dest = new File(getSopDocDir() + File.separator + cp.getSopDocumentName());
		src.renameTo(dest);
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

	private CollectionProtocol getCollectionProtocol(String shortTitle) {
		return getCollectionProtocol(null, null, shortTitle);
	}

	private CollectionProtocol getCollectionProtocol(Long id, String title, String shortTitle) {
		CollectionProtocol cp = null;
		if (id != null) {
			cp = daoFactory.getCollectionProtocolDao().getById(id);
		} else if (StringUtils.isNotBlank(title)) {
			cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(title);
		} else if (StringUtils.isNoneBlank(shortTitle)) {
			cp = daoFactory.getCollectionProtocolDao().getCpByShortTitle(shortTitle);
		}

		if (cp == null) {
			throw OpenSpecimenException.userError(CpErrorCode.NOT_FOUND);
		}

		return cp;
	}

	private void mergeCprIntoCp(CollectionProtocolRegistration srcCpr, CollectionProtocol tgtCp) {
		//
		// Step 1: Get a matching CPR either by PPID or participant ID
		//
		CollectionProtocolRegistration tgtCpr = daoFactory.getCprDao().getCprByPpid(tgtCp.getId(), srcCpr.getPpid());
		if (tgtCpr == null) {
			tgtCpr = srcCpr.getParticipant().getCpr(tgtCp);
		}

		//
		// Step 2: Map all visits of source CP registrations to first event of target CP
		// Further mark all created specimens as unplanned
		//
		CollectionProtocolEvent firstCpe = tgtCp.firstEvent();
		for (Visit visit : srcCpr.getVisits()) {
			visit.setCpEvent(firstCpe);
			visit.getSpecimens().stream().forEach(s -> s.setSpecimenRequirement(null));
		}

		//
		// Step 3: Attach registrations to target CP
		//
		if (tgtCpr == null) {
			//
			// case 1: No matching registration was found in target CP; therefore make source
			// registration as part of target CP
			//
			srcCpr.setCollectionProtocol(tgtCp);
		} else {
			//
			// case 2: Matching registration was found in target CP; therefore do following
			// 2.1 Move all visits of source CP registration to target CP registration
			// 2.2 Finally delete source CP registration
			//
			tgtCpr.addVisits(srcCpr.getVisits());
			srcCpr.getVisits().clear();
			srcCpr.delete();
		}
	}

	private void ensureMergeableCps(CollectionProtocol srcCp, CollectionProtocol tgtCp) {
		ArrayList<String> notSameLabels = new ArrayList<>();

		ensureBlankOrSame(srcCp.getPpidFormat(), tgtCp.getPpidFormat(), PPID_MSG, notSameLabels);
		ensureBlankOrSame(srcCp.getVisitNameFormat(), tgtCp.getVisitNameFormat(), VISIT_NAME_MSG, notSameLabels);
		ensureBlankOrSame(srcCp.getSpecimenLabelFormat(), tgtCp.getSpecimenLabelFormat(), SPECIMEN_LABEL_MSG, notSameLabels);
		ensureBlankOrSame(srcCp.getDerivativeLabelFormat(), tgtCp.getDerivativeLabelFormat(), DERIVATIVE_LABEL_MSG, notSameLabels);
		ensureBlankOrSame(srcCp.getAliquotLabelFormat(), tgtCp.getAliquotLabelFormat(), ALIQUOT_LABEL_MSG, notSameLabels);

		if (!notSameLabels.isEmpty()) {
			throw OpenSpecimenException.userError(
				CpErrorCode.CANNOT_MERGE_FMT_DIFFERS,
				srcCp.getShortTitle(),
				tgtCp.getShortTitle(),
				notSameLabels);
		}
	}

	private void ensureBlankOrSame(String srcLabelFmt, String tgtLabelFmt, String labelKey, List<String> notSameLabels) {
		if (!StringUtils.isBlank(tgtLabelFmt) && !tgtLabelFmt.equals(srcLabelFmt)) {
			notSameLabels.add(getMsg(labelKey));
		}
	}
	
	private boolean forceDeleteCp(final CollectionProtocol cp)
	throws Exception {
		final Authentication auth = AuthUtil.getAuth();

		Future<Boolean> result = taskExecutor.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				SecurityContextHolder.getContext().setAuthentication(auth);

				boolean success = false;
				String stackTrace = null;
				try {
					while (deleteRegistrations(cp));
					deleteCp(cp);
					success = true;
				} catch (Exception ex) {
					success = false;
					stackTrace = ExceptionUtils.getStackTrace(ex);
					throw OpenSpecimenException.serverError(ex);
				} finally {
					sendEmail(cp, success, stackTrace);
				}

				return true;
			}
		});
			
		boolean completed = false;
		try {
			completed = result.get(30, TimeUnit.SECONDS);
		} catch (TimeoutException ex) {
			completed = false;
		}

		return completed;
	}
	
	@PlusTransactional
	private boolean deleteCp(CollectionProtocol cp) {
		//
		// refresh cp, as it could have been fetched in another transaction
		// if in same transaction, then it will be obtained from session
		//
		cp = daoFactory.getCollectionProtocolDao().getById(cp.getId());

		removeContainerRestrictions(cp);
		removeDefaultPiRoles(cp, cp.getPrincipalInvestigator());
		removeDefaultCoordinatorRoles(cp, cp.getCoordinators());
		removeCpRoles(cp);
		cp.delete();
		return true;
	}
	
	@PlusTransactional
	private boolean deleteRegistrations(CollectionProtocol cp) {
		List<CollectionProtocolRegistration> cprs = daoFactory.getCprDao().getCprsByCpId(cp.getId(), 0, 10);
		cprs.forEach(cpr -> cpr.delete(false));
		return cprs.size() == 10;
	}

	private void removeContainerRestrictions(CollectionProtocol cp) {
		Set<StorageContainer> containers = cp.getStorageContainers();
		for (StorageContainer container : containers) {
			container.removeCpRestriction(cp);
		}
		
		cp.setStorageContainers(Collections.EMPTY_SET);
	}

	private void removeCpRoles(CollectionProtocol cp) {
		rbacSvc.removeCpRoles(cp.getId());
	}

	private void sendEmail(CollectionProtocol cp, boolean success, String stackTrace) {
		User currentUser = AuthUtil.getCurrentUser();
		String[] rcpts = {currentUser.getEmailAddress(), cp.getPrincipalInvestigator().getEmailAddress()};

		Map<String, Object> props = new HashMap<>();
		props.put("cp", cp);
		props.put("$subject", new String[] {cp.getShortTitle()});
		props.put("user", currentUser);
		props.put("error", stackTrace);

		String tmpl = success ? CP_DELETE_SUCCESS_EMAIL_TMPL : CP_DELETE_FAILED_EMAIL_TMPL;
		emailService.sendEmail(tmpl, rcpts, props);
	}

	private String getMsg(String code) {
		return MessageUtil.getInstance().getMessage(code);
	}

	private String getSopDocDir() {
		String defDir = ConfigUtil.getInstance().getDataDir() + File.separator + "cp-sop-documents";
		String dir = ConfigUtil.getInstance().getStrSetting(ConfigParams.MODULE, ConfigParams.CP_SOP_DOCS_DIR, defDir);
		new File(dir).mkdirs();
		return dir + File.separator;
	}

	private CpReportSettings getReportSetting(CpQueryCriteria crit) {
		CpReportSettings settings = null;
		if (crit.getId() != null) {
			settings = daoFactory.getCpReportSettingsDao().getByCp(crit.getId());
		} else if (StringUtils.isNotBlank(crit.getShortTitle())) {
			settings = daoFactory.getCpReportSettingsDao().getByCp(crit.getShortTitle());
		}

		return settings;
	}

	private ListConfig getSpecimenListConfig(Map<String, Object> listReq) {
		ListConfig cfg = getListConfig(listReq, "specimen-list-view", "Specimen");
		if (cfg == null) {
			return null;
		}

		Column id = new Column();
		id.setExpr("Specimen.id");
		id.setCaption("specimenId");
		cfg.setPrimaryColumn(id);

		Column type = new Column();
		type.setExpr("Specimen.type");
		type.setCaption("specimenType");

		Column specimenClass = new Column();
		specimenClass.setExpr("Specimen.class");
		specimenClass.setCaption("specimenClass");

		List<Column> hiddenColumns = new ArrayList<>();
		hiddenColumns.add(id);
		hiddenColumns.add(type);
		hiddenColumns.add(specimenClass);
		cfg.setHiddenColumns(hiddenColumns);

		Long cpId = (Long)listReq.get("cpId");
		List<Pair<Long, Long>> siteCps = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps(cpId);
		if (siteCps == null) {
			//
			// Admin; hence no additional restrictions
			//
			return cfg;
		}

		if (siteCps.isEmpty()) {
			throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
		}

		Set<Long> siteIds = siteCps.stream().map(siteCp -> siteCp.first()).collect(Collectors.toSet());
		cfg.setRestriction(getListRestriction(siteIds));
		cfg.setDistinct(true);
		return cfg;
	}

	private ListConfig getParticipantsListConfig(Map<String, Object> listReq) {
		ListConfig cfg = getListConfig(listReq, "participant-list-view", "Participant");
		if (cfg == null) {
			return null;
		}

		Column id = new Column();
		id.setExpr("Participant.id");
		id.setCaption("cprId");
		cfg.setPrimaryColumn(id);
		cfg.setHiddenColumns(Collections.singletonList(id));

		Long cpId = (Long)listReq.get("cpId");
		ParticipantReadAccess access = AccessCtrlMgr.getInstance().getParticipantReadAccess(cpId);
		if (access.admin) {
			return cfg;
		}

		if (access.siteCps == null || access.siteCps.isEmpty()) {
			throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
		}

		Set<Long> siteIds = new HashSet<>();
		for (Pair<Set<Long>, Long> siteCp : access.siteCps) {
			siteIds.addAll(siteCp.first());
		}

		cfg.setRestriction(getListRestriction(siteIds));
		cfg.setDistinct(true);
		return cfg;
	}

	private String getListRestriction(Collection<Long> siteIds) {
		StringBuilder restriction = new StringBuilder();

		String siteIdsStr = siteIds.stream().map(siteId -> siteId.toString()).collect(Collectors.joining(", "));
		if (AccessCtrlMgr.getInstance().isAccessRestrictedBasedOnMrn()) {
			restriction.append("(").append("(Participant.medicalRecord.mrnSiteId exists")
				.append(" and Participant.medicalRecord.mrnSiteId in (").append(siteIdsStr)
				.append(")) or (Participant.medicalRecord.mrnSiteId not exists ")
				.append(" and CollectionProtocol.cpSites.siteId in (").append(siteIdsStr).append(")))");
		} else {
			restriction.append("(CollectionProtocol.cpSites.siteId in (").append(siteIdsStr).append("))");
		}

		return restriction.insert(0, "(").append(")").toString();
	}

	private ListConfig getListConfig(Map<String, Object> listReq, String listName, String drivingForm) {
		Long cpId = (Long)listReq.get("cpId");
		Workflow workflow = getWorkFlow(cpId, listName);
		if (workflow == null) {
			return null;
		}

		ListConfig listCfg = new ObjectMapper().convertValue(workflow.getData(), ListConfig.class);
		listCfg.setCpId(cpId);
		listCfg.setDrivingForm(drivingForm);
		setListLimit(listReq, listCfg);

		Boolean includeCount = (Boolean)listReq.get("includeCount");
		listCfg.setIncludeCount(includeCount == null ? false : includeCount);
		return listCfg;
	}

	private Workflow getWorkFlow(Long cpId, String name) {
		Workflow workflow = null;

		CpWorkflowConfig cfg = daoFactory.getCollectionProtocolDao().getCpWorkflows(cpId);
		if (cfg != null) {
			workflow = cfg.getWorkflows().get(name);
		}

		if (workflow == null) {
			workflow = getSysWorkflow(name);
		}

		return workflow;
	}

	private Workflow getSysWorkflow(String name) {
		return WorkflowUtil.getInstance().getSysWorkflow(name);
	}

	private void setListLimit(Map<String, Object> listReq, ListConfig cfg) {
		Integer startAt = (Integer)listReq.get("startAt");
		if (startAt == null) {
			startAt = 0;
		}

		Integer maxResults = (Integer)listReq.get("maxResults");
		if (maxResults == null) {
			maxResults = 100;
		}

		cfg.setStartAt(startAt);
		cfg.setMaxResults(maxResults);
	}

	private static final String PPID_MSG                     = "cp_ppid";

	private static final String VISIT_NAME_MSG               = "cp_visit_name";

	private static final String SPECIMEN_LABEL_MSG           = "cp_specimen_label";

	private static final String DERIVATIVE_LABEL_MSG         = "cp_derivative_label";

	private static final String ALIQUOT_LABEL_MSG            = "cp_aliquot_label";
	
	private static final String CP_DELETE_SUCCESS_EMAIL_TMPL = "cp_delete_success";
	
	private static final String CP_DELETE_FAILED_EMAIL_TMPL  = "cp_delete_failed";
}
