
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitFactory;
import com.krishagni.catissueplus.core.biospecimen.events.FileDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SprDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SprLockDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.DocumentDeIdentifier;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.Utility;

public class VisitServiceImpl implements VisitService {
	private DaoFactory daoFactory;

	private VisitFactory visitFactory;
	
	private SpecimenService specimenSvc;
	
	private ConfigurationService cfgSvc;
	
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

	@Override
	@PlusTransactional
	public ResponseEvent<VisitDetail> getVisit(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();			
			Visit visit = getVisit(crit.getId(), crit.getName());
			AccessCtrlMgr.getInstance().ensureReadVisitRights(visit);
			return ResponseEvent.response(VisitDetail.from(visit));			
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}	
	
	@Override
	@PlusTransactional
	public ResponseEvent<VisitDetail> addOrUpdateVisit(RequestEvent<VisitDetail> req) {
		try {
			VisitDetail respPayload = saveOrUpdateVisit(req.getPayload(), false);
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
			VisitDetail respPayload = saveOrUpdateVisit(req.getPayload(), true);
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
			VisitDetail visit = saveOrUpdateVisit(req.getPayload().getVisit(), false);			
			
			List<SpecimenDetail> specimens = req.getPayload().getSpecimens();
			setVisitId(visit.getId(), specimens);
						
			RequestEvent<List<SpecimenDetail>> collectSpecimensReq = new RequestEvent<List<SpecimenDetail>>(specimens);
			ResponseEvent<List<SpecimenDetail>> collectSpecimensResp = specimenSvc.collectSpecimens(collectSpecimensReq);
			collectSpecimensResp.throwErrorIfUnsuccessful();
			
			VisitSpecimenDetail resp = new VisitSpecimenDetail();
			resp.setVisit(visit);
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
	public ResponseEvent<FileDetail> getSpr(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();
			Visit visit = getVisit(crit.getId(), crit.getName());
			
			AccessCtrlMgr.getInstance().ensureReadSprRights(visit);
		
			String fileName = visit.getSprName();
			if (StringUtils.isBlank(fileName)) {
				return ResponseEvent.userError(VisitErrorCode.NO_SPR_UPLOADED);
			}
			
			File file = getSprFile(visit.getId());
			if (!file.exists()) {
				return ResponseEvent.serverError(VisitErrorCode.UNABLE_TO_LOCATE_SPR);
			}
			FileDetail detail = new FileDetail();
			detail.setFile(file);
			detail.setFileName(fileName);
			return ResponseEvent.response(detail);
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
			
			AccessCtrlMgr.getInstance().ensureCreateOrUpdateSprRights(visit);
			
			String sprText = Utility.getFileText(detail.getInputStream(), detail.getFileContentType());
			
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
			return new ResponseEvent<String>(detail.getName());
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
			AccessCtrlMgr.getInstance().ensureCreateOrUpdateSprRights(visit);
			
			if (visit.isSprLocked()) {
				return ResponseEvent.serverError(VisitErrorCode.LOCKED_SPR);
			}
			
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
		
			AccessCtrlMgr.getInstance().ensureCreateOrUpdateSprRights(visit);
		
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
	public ResponseEvent<Boolean> lockSpr(RequestEvent<SprLockDetail> req) {
		SprLockDetail detail = req.getPayload();
		Visit visit = getVisit(detail.getVisitId(), detail.getVisitName());
		
		if (detail.getLock()) {
			AccessCtrlMgr.getInstance().ensureLockSprRights(visit);
		} else {
			AccessCtrlMgr.getInstance().ensureUnlockSprRights(visit);
		}
		
		visit.setSprLock(detail.getLock());

		return ResponseEvent.response(true);
	}

	private VisitDetail saveOrUpdateVisit(VisitDetail input, boolean partial) {		
		Visit existing = null;
		
		if (input.getId() != null || StringUtils.isNotEmpty(input.getName())) {
			existing = getVisit(input.getId(), input.getName());
			AccessCtrlMgr.getInstance().ensureCreateOrUpdateVisitRights(existing);
		}
		
		if (partial && existing == null) {
			throw OpenSpecimenException.userError(VisitErrorCode.NOT_FOUND);
		}
							
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		Visit visit = null;
		if (partial) {
			visit = visitFactory.createVisit(existing, input);
		} else {
			visit = visitFactory.createVisit(input);
		}		
		AccessCtrlMgr.getInstance().ensureCreateOrUpdateVisitRights(visit);
		
		if (existing == null || !existing.getName().equals(visit.getName())) {
			ensureUniqueVisitName(visit.getName(), ose);
		}
		
		ose.checkAndThrow();
		if (existing != null) {
			existing.update(visit);
		} else {
			existing = visit;
		}
		
		existing.setNameIfEmpty();
		daoFactory.getVisitsDao().saveOrUpdate(existing);
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
	
	private void ensureUniqueVisitName(String visitName, OpenSpecimenException ose) {
		if (daoFactory.getVisitsDao().getByName(visitName) != null) {
			ose.addError(VisitErrorCode.DUP_NAME);
		}		
	}
	
	private void setVisitId(Long visitId, List<SpecimenDetail> specimens) {
		if (CollectionUtils.isEmpty(specimens)) {
			return;
		}
		
		for (SpecimenDetail specimen : specimens) {
			specimen.setVisitId(visitId);
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

}
