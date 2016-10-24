
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitFactory;
import com.krishagni.catissueplus.core.biospecimen.events.FileDetail;
import com.krishagni.catissueplus.core.biospecimen.events.LabelPrintJobSummary;
import com.krishagni.catissueplus.core.biospecimen.events.PrintVisitNameDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SprDetail;
import com.krishagni.catissueplus.core.biospecimen.events.FileDownloadDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SprLockDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.DocumentDeIdentifier;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.biospecimen.services.SprPdfGenerator;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.domain.LabelPrintJob;
import com.krishagni.catissueplus.core.common.domain.PrintItem;
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
import com.krishagni.catissueplus.core.common.service.ObjectStateParamsResolver;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.Utility;

public class VisitServiceImpl implements VisitService, ObjectStateParamsResolver {
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
			boolean allowPhi = AccessCtrlMgr.getInstance().ensureReadVisitRights(visit);
			return ResponseEvent.response(VisitDetail.from(visit, false, !allowPhi));
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
		List<Visit> visits = new ArrayList<>();
		boolean hasPhiFields = false;

		if (StringUtils.isNotEmpty(crit.name())) {
			visits.add(getVisit(null, crit.name()));
		} else if (StringUtils.isNotEmpty(crit.sprNumber())) {
			visits.addAll(daoFactory.getVisitsDao().getBySpr(crit.sprNumber()));
			hasPhiFields = true;
		}

		Iterator<Visit> iterator = visits.iterator();
		while (iterator.hasNext()) {
			Visit visit = iterator.next();
			try {
				boolean phiAccess = AccessCtrlMgr.getInstance().ensureReadVisitRights(visit, hasPhiFields);
				if (hasPhiFields && !phiAccess) {
					iterator.remove();
				}
			} catch (OpenSpecimenException ose) {
				if (ose.getErrorType().equals(ErrorType.USER_ERROR)) {
					iterator.remove();
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
			AccessCtrlMgr.getInstance().ensureReadVisitRights(visit, false);
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
			//
			// Step 1: Create visit
			//
			VisitDetail inputVisit = req.getPayload().getVisit();
			VisitDetail savedVisit = saveOrUpdateVisit(inputVisit, inputVisit.getId() != null, false);			
			
			List<SpecimenDetail> specimens = req.getPayload().getSpecimens();
			setVisitId(savedVisit.getId(), specimens);
			
			// 
			// Step 2: Set IDs of specimens that are pre-created for the visit
			// 
			Visit visit = daoFactory.getVisitsDao().getById(savedVisit.getId());
			Map<Long, Specimen> reqSpecimenMap = visit.getSpecimens().stream()
				.collect(Collectors.toMap(s -> s.getSpecimenRequirement().getId(), s -> s));
			setSpecimenIds(specimens, reqSpecimenMap);
			
			// 
			// Step 3: Collect specimens
			//
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
	public ResponseEvent<LabelPrintJobSummary> printVisitNames(RequestEvent<PrintVisitNameDetail> req) {
		LabelPrinter<Visit> printer = getLabelPrinter();
		if (printer == null) {
			return ResponseEvent.serverError(VisitErrorCode.NO_PRINTER_CONFIGURED);
		}

		PrintVisitNameDetail printDetail = req.getPayload();
		LabelPrintJob job = printer.print(PrintItem.make(getVisitsToPrint(printDetail), printDetail.getCopies()));
		if (job == null) {
			return ResponseEvent.userError(VisitErrorCode.PRINT_ERROR);
		}

		return ResponseEvent.response(LabelPrintJobSummary.from(job));
	}

	@Override
	@PlusTransactional
	public ResponseEvent<FileDetail> getSpr(RequestEvent<FileDownloadDetail> req) {
		try {
			FileDownloadDetail detail = req.getPayload();
			Visit visit = getVisit(detail.getId(), detail.getName());
			
			AccessCtrlMgr.getInstance().ensureReadSprRights(visit);
			
			if (StringUtils.isBlank(visit.getSprName())) {
				throw OpenSpecimenException.userError(VisitErrorCode.NO_SPR_UPLOADED);
			}
			
			File file = getSprFile(visit.getId());
			if (file == null) {
				throw OpenSpecimenException.serverError(VisitErrorCode.UNABLE_TO_LOCATE_SPR);
			}
			
			String fileExtension = file.getName().substring(file.getName().lastIndexOf('.'));
			if (isPdfType(detail.getType()) && isTextFile(file)) {
				Map<String, Object> props = Collections.<String, Object>singletonMap("visit", visit);
				file = sprText2PdfGenerator.generate(file, props);
				fileExtension = ".pdf";
			}

			FileDetail fileDetail = new FileDetail();
			fileDetail.setFileOut(file);
			fileDetail.setFilename(visit.getName() + fileExtension);
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
			Visit visit = getVisit(detail.getId(), null);
			
			ensureUpdateSprRights(visit);
			
			String filename = detail.getFilename();
			if (detail.isTextContent() || (detail.isPdfContent() && isExtractSprTextEnabled(visit))) {
				String sprText = getTextFromReq(detail);

				File sprFile = new File(getSprDirPath(visit.getId()) + File.separator + "spr.txt");
				FileUtils.writeStringToFile(sprFile, sprText, (String) null, false);
				
				filename = filename.substring(0, filename.lastIndexOf(".")) + ".txt";
				visit.updateSprName(filename);
			} else {
				String extension = filename.substring(filename.lastIndexOf('.'));
				File sprFile = new File(getSprDirPath(visit.getId()) + File.separator + "spr" + extension);
				FileUtils.copyInputStreamToFile(detail.getFileIn(), sprFile);
				visit.updateSprName(filename);
			}
			
			return new ResponseEvent<>(filename);
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
			Visit visit = getVisit(detail.getId(), null);
			
			ensureUpdateSprRights(visit);
			
			File file = getSprFile(detail.getId());
			if (file == null) {
				return ResponseEvent.serverError(VisitErrorCode.UNABLE_TO_LOCATE_SPR);
			}
			
			if (!isTextFile(file)) {
				return ResponseEvent.userError(VisitErrorCode.NON_TEXT_SPR);
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
		
			AccessCtrlMgr.getInstance().ensureDeleteSprRights(visit);
			
			if (visit.isSprLocked()) {
				return ResponseEvent.userError(VisitErrorCode.LOCKED_SPR);
			}
			
			if (StringUtils.isBlank(visit.getSprName())) {
				return ResponseEvent.userError(VisitErrorCode.NO_SPR_UPLOADED);
			}
			
			File file = getSprFile(visit.getId());
			if (file == null) {
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
		
		File sprFile = getSprFile(visit.getId());
		if (sprFile == null) {
			return ResponseEvent.userError(VisitErrorCode.NO_SPR_UPLOADED);
		}
		
		if (!isTextFile(sprFile)) {
			return ResponseEvent.userError(VisitErrorCode.NON_TEXT_SPR);
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

	@PlusTransactional
	@Override
	public List<Visit> getVisitsByName(List<String> visitNames) {
		List<Visit> visits = daoFactory.getVisitsDao().getByName(visitNames);
		for (Visit visit : visits) {
			AccessCtrlMgr.getInstance().ensureReadVisitRights(visit);
		}

		return visits;
	}

	@PlusTransactional
	@Override
	public List<Visit> getSpecimenVisits(List<String> specimenLabels) {
		List<Pair<Long, Long>> siteCps = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
		if (siteCps != null && siteCps.isEmpty()) {
			return Collections.emptyList();
		}

		SpecimenListCriteria crit = new SpecimenListCriteria()
			.labels(specimenLabels)
			.siteCps(siteCps)
			.useMrnSites(AccessCtrlMgr.getInstance().isAccessRestrictedBasedOnMrn());
		return daoFactory.getSpecimenDao().getSpecimenVisits(crit);
	}

	@Override
	public String getObjectName() {
		return "visit";
	}

	@Override
	@PlusTransactional
	public Map<String, Object> resolve(String key, Object value) {
		if (key.equals("id")) {
			value = Long.valueOf(value.toString());
		}

		return daoFactory.getVisitsDao().getCprVisitIds(key, value);
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
		existing.printLabels(prevStatus);
		return VisitDetail.from(existing, false, false);
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
		String path = getSprDirPath(visitId);
		File dir = new File(path);
		if (!dir.exists()) {
			return null;
		}
		
		File[] files = dir.listFiles();
		if (files.length == 0) {
			return null;
		}
		
		return files[0];
	}
	
	private String getSprDirPath(Long visitId) {
		String path = cfgSvc.getStrSetting(ConfigParams.MODULE, ConfigParams.SPR_DIR, getDefaultVisitSprDir());
		return path + File.separator + visitId;
	}
	
	private String getTextFromReq(SprDetail detail) {
		String text = Utility.getString(detail.getFileIn(), detail.getContentType());
		
		DocumentDeIdentifier deIdentifier = getSprDeIdentifier();
		if (deIdentifier != null) {
			Map<String, Object> props = Collections.singletonMap("visitId", detail.getId());
			text = deIdentifier.deIdentify(text, props);
		}
		
		return text;
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
	
	private boolean isTextFile(File file) {
		String contentType = Utility.getContentType(file);
		return contentType.startsWith("text/") || contentType.equals("application/pdf");
	}
	
	private boolean isPdfType(FileType type) {
		return type != null && type.equals(FileType.PDF);
	}
	
	private void setSpecimenIds(List<SpecimenDetail> inputSpecimens, Map<Long, Specimen> reqSpecimenMap) {
		if (reqSpecimenMap.isEmpty()) {
			return;
		}
		
		for (SpecimenDetail specimenDetail : inputSpecimens) {
			if (specimenDetail.getReqId() != null) {
				Specimen specimen = reqSpecimenMap.get(specimenDetail.getReqId());
				if (specimen == null) {
					//
					// Anticipated specimen not yet created; therefore none of its children either
					//
					continue;
				}

				specimenDetail.setId(specimen.getId());
				if (StringUtils.isBlank(specimenDetail.getLabel())) {
					specimenDetail.setLabel(specimen.getLabel());
				}
			}

			if (CollectionUtils.isNotEmpty(specimenDetail.getSpecimensPool())) {
				setSpecimenIds(specimenDetail.getSpecimensPool(), reqSpecimenMap);
			}
			
			if (CollectionUtils.isNotEmpty(specimenDetail.getChildren())) {
				setSpecimenIds(specimenDetail.getChildren(), reqSpecimenMap);
			}
		}
	}

	private List<Visit> getVisitsToPrint(PrintVisitNameDetail printDetail) {
		List<Visit> visits = null;
		Object key = null;

		if (CollectionUtils.isNotEmpty(printDetail.getVisitIds())) {
			visits = daoFactory.getVisitsDao().getByIds(printDetail.getVisitIds());
			key = printDetail.getVisitIds();
		} else if (CollectionUtils.isNotEmpty(printDetail.getVisitNames())) {
			visits = daoFactory.getVisitsDao().getByName(printDetail.getVisitNames());
			key = printDetail.getVisitNames();
		}

		if (CollectionUtils.isEmpty(visits)) {
			throw OpenSpecimenException.userError(VisitErrorCode.NO_VISITS_TO_PRINT, key);
		}

		return visits;
	}

	private boolean isExtractSprTextEnabled(Visit visit) {
		Boolean extractSprText = visit.getCollectionProtocol().getExtractSprText();
		if (extractSprText == null) {
			extractSprText = ConfigUtil.getInstance().getBoolSetting(
				ConfigParams.MODULE, ConfigParams.EXTRACT_SPR_TEXT, false);
		}

		return extractSprText;
	}
}
