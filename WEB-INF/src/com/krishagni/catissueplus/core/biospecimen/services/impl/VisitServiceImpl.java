
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitFactory;
import com.krishagni.catissueplus.core.biospecimen.events.FileDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SprDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SprFileDownloadDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SprLockDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.DocumentDeIdentifier;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.biospecimen.services.SprPdfGenerator;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.FileType;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.service.LabelGenerator;
import com.krishagni.catissueplus.core.common.service.LabelPrinter;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.Utility;

public class VisitServiceImpl implements VisitService {
	private DaoFactory daoFactory;

	private VisitFactory visitFactory;
	
	private SpecimenService specimenSvc;
	
	private ConfigurationService cfgSvc;
	
	private LabelGenerator visitNameGenerator;
	
	private SprPdfGenerator sprText2PdfGenerator;
	
	private static String defaultVisitSprDir;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setVisitFactory(VisitFactory visitFactory) {
		this.visitFactory = visitFactory;
	}
	
	public void setSpecimenSvc(SpecimenService specimenSvc) {
		this.specimenSvc = specimenSvc;
	}
	
	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}
	
	public void setVisitNameGenerator(LabelGenerator visitNameGenerator) {
		this.visitNameGenerator = visitNameGenerator;
	}
	
	public void setSprText2PdfGenerator(SprPdfGenerator sprText2PdfGenerator) {
		this.sprText2PdfGenerator = sprText2PdfGenerator;
	}	

	@Override
	@PlusTransactional
	public ResponseEvent<VisitDetail> getVisit(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();			
			Visit visit = getVisit(crit.getId(), crit.getName());
			AccessCtrlMgr.getInstance().ensureReadVisitRights(visit);
			return ResponseEvent.response(VisitDetail.from(visit));			
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@PlusTransactional
	@Override
	public ResponseEvent<List<VisitDetail>> getVisits(RequestEvent<VisitsListCriteria> criteria) {
		VisitsListCriteria crit = criteria.getPayload();
		List<Visit> visits = new ArrayList<Visit>();

		if (StringUtils.isNotEmpty(crit.name())) {
			visits.add(getVisit(null, crit.name()));
		} else if (StringUtils.isNotEmpty(crit.sprNumber())) {
			visits.addAll(daoFactory.getVisitsDao().getBySpr(crit.sprNumber()));
		}

		Iterator<Visit> iterator = visits.iterator();
		while (iterator.hasNext()) {
			Visit visit = iterator.next();
			try {
				AccessCtrlMgr.getInstance().ensureReadVisitRights(visit);
			} catch (OpenSpecimenException ose) {
				if (ose.getErrorType().equals(ErrorType.USER_ERROR)) {
					visits.remove(visit);
				}
			}
		}

		return ResponseEvent.response(VisitDetail.from(visits));
	}

	@Override
	@PlusTransactional
	public ResponseEvent<VisitDetail> addVisit(RequestEvent<VisitDetail> req) {
		try {
			VisitDetail respPayload = saveOrUpdateVisit(req.getPayload(), false, false);
			return ResponseEvent.response(respPayload);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<VisitDetail> updateVisit(RequestEvent<VisitDetail> req) {
		try {
			VisitDetail respPayload = saveOrUpdateVisit(req.getPayload(), true, false);
			return ResponseEvent.response(respPayload);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<VisitDetail> patchVisit(RequestEvent<VisitDetail> req) {
		try {
			VisitDetail respPayload = saveOrUpdateVisit(req.getPayload(), true, true);
			return ResponseEvent.response(respPayload);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();
			Visit visit = getVisit(crit.getId(), crit.getName());
			AccessCtrlMgr.getInstance().ensureReadVisitRights(visit);
			return ResponseEvent.response(visit.getDependentEntities());
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<VisitDetail> deleteVisit(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();
			Visit visit = getVisit(crit.getId(), crit.getName());
			AccessCtrlMgr.getInstance().ensureDeleteVisitRights(visit);
			visit.delete();
			return ResponseEvent.response(VisitDetail.from(visit));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<VisitSpecimenDetail> collectVisitAndSpecimens(RequestEvent<VisitSpecimenDetail> req) {		
		try {
			VisitDetail inputVisit = req.getPayload().getVisit();
			VisitDetail savedVisit = saveOrUpdateVisit(inputVisit, inputVisit.getId() != null, false);			
			
			List<SpecimenDetail> specimens = req.getPayload().getSpecimens();
			setVisitId(savedVisit.getId(), specimens);
						
			RequestEvent<List<SpecimenDetail>> collectSpecimensReq = new RequestEvent<List<SpecimenDetail>>(specimens);
			ResponseEvent<List<SpecimenDetail>> collectSpecimensResp = specimenSvc.collectSpecimens(collectSpecimensReq);
			collectSpecimensResp.throwErrorIfUnsuccessful();
			
			VisitSpecimenDetail resp = new VisitSpecimenDetail();
			resp.setVisit(savedVisit);
			resp.setSpecimens(collectSpecimensResp.getPayload());
			return ResponseEvent.response(resp);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<FileDetail> getSpr(RequestEvent<SprFileDownloadDetail> req) {
		try {
			SprFileDownloadDetail detail = req.getPayload();
			Visit visit = getVisit(detail.getVisitId(), detail.getVisitName());
			
			AccessCtrlMgr.getInstance().ensureReadSprRights(visit);
		
			if (StringUtils.isBlank(visit.getSprName())) {
				throw OpenSpecimenException.userError(VisitErrorCode.NO_SPR_UPLOADED);
			}
			
			File file = getSprFile(visit.getId());
			if (!file.exists()) {
				throw OpenSpecimenException.serverError(VisitErrorCode.UNABLE_TO_LOCATE_SPR);
			}
			
			String fileExtension = ".txt";
			if (detail.getType() != null && detail.getType().equals(FileType.PDF)) {
				Map<String, Object> props = Collections.<String, Object>singletonMap("visit", visit);
				file = sprText2PdfGenerator.generate(file, props);
				fileExtension = ".pdf";
			}

			FileDetail fileDetail = new FileDetail();
			fileDetail.setFile(file);
			fileDetail.setFileName(visit.getName() + fileExtension);
			return ResponseEvent.response(fileDetail);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<String> uploadSprFile(RequestEvent<SprDetail> req) {
		try {
			SprDetail detail = req.getPayload();
			Visit visit = getVisit(detail.getVisitId(), null);
			ensureUpdateSprRights(visit);
			String sprText = Utility.getString(detail.getInputStream(), detail.getContentType());
			
			DocumentDeIdentifier deIdentifier = getSprDeIdentifier(); 
			if (deIdentifier != null) {
				Map<String, Object> props = Collections.<String, Object>singletonMap("visitId", detail.getVisitId());
				sprText = deIdentifier.deIdentify(sprText, props);
			} 

			File sprFile = getSprFile(visit.getId());
			FileUtils.writeStringToFile(sprFile, sprText, (String) null, false);
			
			String sprName = detail.getName(); 
			sprName = sprName.substring(0, sprName.lastIndexOf(".")) + ".txt";
			visit.updateSprName(sprName);
			return new ResponseEvent<String>(sprName);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<String> updateSprText(RequestEvent<SprDetail> req) {
		try {
			SprDetail detail = req.getPayload();
			Visit visit = getVisit(detail.getVisitId(), null);
			
			ensureUpdateSprRights(visit);
			
			File file = getSprFile(detail.getVisitId());
			if (!file.exists()) {
				return ResponseEvent.serverError(VisitErrorCode.UNABLE_TO_LOCATE_SPR);
			}
			FileUtils.writeStringToFile(file, detail.getSprText(), (String) null, false);
			return ResponseEvent.response(detail.getSprText());
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> deleteSprFile(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();
			Visit visit = getVisit(crit.getId(), crit.getName());
		
			if (visit.isSprLocked()) {
				return ResponseEvent.userError(VisitErrorCode.LOCKED_SPR);
			}
			AccessCtrlMgr.getInstance().ensureDeleteSprRights(visit);
		
			if (StringUtils.isBlank(visit.getSprName())) {
				return ResponseEvent.userError(VisitErrorCode.NO_SPR_UPLOADED);
			}
		
			File file = getSprFile(visit.getId());
			if (!file.exists()) {
				return ResponseEvent.serverError(VisitErrorCode.UNABLE_TO_LOCATE_SPR);
			}
		
			boolean isFileDeleted = file.delete();
			if (isFileDeleted) {
				visit.setSprName(null);
			}
			return ResponseEvent.response(isFileDeleted);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@PlusTransactional
	@Override
	public ResponseEvent<SprLockDetail> updateSprLockStatus(RequestEvent<SprLockDetail> req) {
		SprLockDetail detail = req.getPayload();
		Visit visit = getVisit(detail.getVisitId(), detail.getVisitName());
		if (detail.isLocked()) {
			AccessCtrlMgr.getInstance().ensureLockSprRights(visit);
		} else {
			AccessCtrlMgr.getInstance().ensureUnlockSprRights(visit);
		}
		visit.setSprLocked(detail.isLocked());
		return ResponseEvent.response(detail);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public LabelPrinter<Visit> getLabelPrinter() {
		String beanName = cfgSvc.getStrSetting(
				ConfigParams.MODULE, 
				ConfigParams.VISIT_LABEL_PRINTER, 
				"defaultVisitLabelPrinter");
		
		return (LabelPrinter<Visit>)OpenSpecimenAppCtxProvider.getAppCtx().getBean(beanName);
	}

	private VisitDetail saveOrUpdateVisit(VisitDetail input, boolean update, boolean partial) {		
		Visit existing = null;
		String prevStatus = null;   
		
		if (update && (input.getId() != null || StringUtils.isNotBlank(input.getName()))) {
			existing = getVisit(input.getId(), input.getName());
			AccessCtrlMgr.getInstance().ensureCreateOrUpdateVisitRights(existing);
			prevStatus = existing.getStatus();
		}
		
		if (update && existing == null) {
			throw OpenSpecimenException.userError(VisitErrorCode.NOT_FOUND);
		}

		Visit visit = null;		
		if (partial) {
			visit = visitFactory.createVisit(existing, input);
		} else {
			visit = visitFactory.createVisit(input);
		}
		
		AccessCtrlMgr.getInstance().ensureCreateOrUpdateVisitRights(visit);
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		ensureValidAndUniqueVisitName(existing, visit, ose);
		ose.checkAndThrow();
		
		if (existing == null) {
			if (visit.isMissed()) {
				visit.createMissedSpecimens();
			}
			
			existing = visit;
		} else {
			existing.update(visit);
		}
		
		existing.setNameIfEmpty();
		daoFactory.getVisitsDao().saveOrUpdate(existing);
		existing.addOrUpdateExtension();
		existing.prePrintLabels(prevStatus);
		return VisitDetail.from(existing);		
	}
	
	private Visit getVisit(Long visitId, String visitName) {
		Visit visit = null;
		
		if (visitId != null) {
			visit = daoFactory.getVisitsDao().getById(visitId);
		} else if (StringUtils.isNotBlank(visitName)) {
			visit = daoFactory.getVisitsDao().getByName(visitName);
		}
		
		if (visit == null) {
			throw OpenSpecimenException.userError(VisitErrorCode.NOT_FOUND);
		}
		
		return visit;
	}
	
	private void ensureValidAndUniqueVisitName(Visit existing, Visit visit, OpenSpecimenException ose) {
		if (existing != null && 
			StringUtils.isNotBlank(existing.getName()) && 
			existing.getName().equals(visit.getName())) {
			return;
		}
		
		CollectionProtocol cp = visit.getCollectionProtocol();
		String name = visit.getName();
		
		if (StringUtils.isBlank(name)) {
			if (cp.isManualVisitNameEnabled() && visit.isCompleted()) {
				ose.addError(VisitErrorCode.NAME_REQUIRED);
			}
			
			return;
		}
		
		if (StringUtils.isNotBlank(cp.getVisitNameFormat())) {
			//
			// Visit name format is specified
			//
			
			if (!cp.isManualVisitNameEnabled()) {
				ose.addError(VisitErrorCode.MANUAL_NAME_NOT_ALLOWED);
				return;
			}


			if (!visitNameGenerator.validate(cp.getVisitNameFormat(), visit, name)) {
				ose.addError(VisitErrorCode.INVALID_NAME, name);
				return;
			}
		}
		
		if (daoFactory.getVisitsDao().getByName(name) != null) {
			ose.addError(VisitErrorCode.DUP_NAME, name);
		}		
	}
	
	private void setVisitId(Long visitId, List<SpecimenDetail> specimens) {
		if (CollectionUtils.isEmpty(specimens)) {
			return;
		}
		
		for (SpecimenDetail specimen : specimens) {
			specimen.setVisitId(visitId);
			setVisitId(visitId, specimen.getSpecimensPool());
			setVisitId(visitId, specimen.getChildren());
		}
	}
	
	private File getSprFile(Long visitId) {
		String path = cfgSvc.getStrSetting(
				ConfigParams.MODULE, 
				ConfigParams.SPR_DIR, 
				getDefaultVisitSprDir());
		path = path + File.separator + visitId + File.separator + "spr.txt";
		
		return new File(path);
	}

	private DocumentDeIdentifier getSprDeIdentifier() {
		String sprDeidentifierBean = cfgSvc.getStrSetting(ConfigParams.MODULE, ConfigParams.SPR_DEIDENTIFIER);
		if (StringUtils.isBlank(sprDeidentifierBean)) {
			return null;
		}
		
		return (DocumentDeIdentifier) OpenSpecimenAppCtxProvider
				.getAppCtx()
				.getBean(sprDeidentifierBean);
	}
	
	private static String getDefaultVisitSprDir() {
		if (StringUtils.isBlank(defaultVisitSprDir)) {
			defaultVisitSprDir = ConfigUtil.getInstance().getDataDir() + File.separator + "visit-sprs";
		}
		return defaultVisitSprDir;
	}
	
	private void ensureUpdateSprRights(Visit visit) {
		if (visit.isSprLocked()) {
			throw OpenSpecimenException.userError(VisitErrorCode.LOCKED_SPR);
		}
		
		AccessCtrlMgr.getInstance().ensureCreateOrUpdateSprRights(visit);
	}
}
