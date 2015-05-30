
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.io.File;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitFactory;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SprDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.DocumentDeIdentifier;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.PdfDocument;
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

public class VisitServiceImpl implements VisitService {
	private DaoFactory daoFactory;

	private VisitFactory visitFactory;
	
	private SpecimenService specimenSvc;
	
	private ConfigurationService cfgSvc;

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
	public ResponseEvent<String> uploadSpr(RequestEvent<SprDetail> req) {
		try {
			SprDetail sprDetail = req.getPayload();
			
			Visit visit = daoFactory.getVisitsDao().getById(sprDetail.getVisitId());
			if (visit == null) {
				throw OpenSpecimenException.userError(VisitErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureCreateOrUpdateVisitRights(visit);
			
			String spr = PdfDocument.getText(sprDetail.getSpr());
			DocumentDeIdentifier reportsDeIdentifier = getSprDeIdentifier(); 
			if (reportsDeIdentifier != null) {
				spr = reportsDeIdentifier.deIdentify(spr, sprDetail.getVisitId());
			} 

			String path = getSprDirPath() + File.separator + visit.getId();
			PdfDocument.create(spr, path, "Surgical-Pathology-Report");
 			
			visit.updateSprName(sprDetail.getSprName()); 
			return new ResponseEvent<String>(sprDetail.getSprName());
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
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
	
	private String getSprDirPath() {
		String path = cfgSvc.getStrSetting(ConfigParams.MODULE, ConfigParams.SPR_DIR);
		if (path == null) {
			path = ConfigUtil.getInstance().getDataDir() + File.separator + "visit-sprs";
		}
		return path + File.separator; 
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
}
