
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.events.LabelPrintJobSummary;
import com.krishagni.catissueplus.core.biospecimen.events.PrintSpecimenLabelDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenAliquotsSpec;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo.StorageLocationSummary;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenStatusDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenDao;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.domain.LabelPrintJob;
import com.krishagni.catissueplus.core.common.errors.CommonErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.service.LabelGenerator;
import com.krishagni.catissueplus.core.common.service.LabelPrinter;
import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class SpecimenServiceImpl implements SpecimenService {

	private DaoFactory daoFactory;

	private SpecimenFactory specimenFactory;
	
	private ConfigurationService cfgSvc;
	
	private LabelGenerator labelGenerator;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSpecimenFactory(SpecimenFactory specimenFactory) {
		this.specimenFactory = specimenFactory;
	}
	
	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}
	
	public void setLabelGenerator(LabelGenerator labelGenerator) {
		this.labelGenerator = labelGenerator;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenDetail> getSpecimen(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			Specimen specimen = getSpecimen(crit.getId(), crit.getName(), ose);
			if (specimen == null) {
				return ResponseEvent.error(ose);
			}
			
			AccessCtrlMgr.getInstance().ensureReadSpecimenRights(specimen);
			return ResponseEvent.response(SpecimenDetail.from(specimen));			
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenInfo>> getSpecimens(RequestEvent<List<String>> req) {
		try {
			List<String> labels = req.getPayload();
			if (CollectionUtils.isEmpty(labels)) {
				return ResponseEvent.userError(CommonErrorCode.INVALID_REQUEST);
			}
			
			List<Pair<Long, Long>> siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
			if (siteCpPairs != null && siteCpPairs.isEmpty()) {
				return ResponseEvent.response(Collections.<SpecimenInfo>emptyList());
			}
			
			SpecimenListCriteria crit = new SpecimenListCriteria()
				.labels(labels)
				.siteCps(siteCpPairs);
			
			List<Specimen> specimens = daoFactory.getSpecimenDao().getSpecimens(crit);
			List<SpecimenInfo> result = SpecimenInfo.from(specimens);
			SpecimenInfo.orderByLabels(result, labels);
			return ResponseEvent.response(result);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenDetail> createSpecimen(RequestEvent<SpecimenDetail> req) {
		try {
			SpecimenDetail detail = req.getPayload();
			Specimen specimen = saveOrUpdate(detail, null, null, false);
			return ResponseEvent.response(SpecimenDetail.from(specimen));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception ex) {
			return ResponseEvent.serverError(ex);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenDetail> updateSpecimen(RequestEvent<SpecimenDetail> req) {
		return updateSpecimen(req.getPayload(), false);
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenDetail> patchSpecimen(RequestEvent<SpecimenDetail> req) {
		return updateSpecimen(req.getPayload(), true);
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenDetail> updateSpecimenStatus(RequestEvent<SpecimenStatusDetail> req) {
		try {
			SpecimenStatusDetail detail = req.getPayload();
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			Specimen specimen = getSpecimen(detail.getId(), detail.getLabel(), ose);
			if (specimen == null) {
				return ResponseEvent.error(ose);
			}
			
			AccessCtrlMgr.getInstance().ensureCreateOrUpdateSpecimenRights(specimen);
			specimen.updateStatus(detail.getStatus(), detail.getReason());
			return ResponseEvent.response(SpecimenDetail.from(specimen));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenDetail> deleteSpecimen(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();

			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			Specimen specimen = getSpecimen(crit.getId(), crit.getName(), ose);
			if (specimen == null) {
				return ResponseEvent.error(ose);
			}
			
			AccessCtrlMgr.getInstance().ensureDeleteSpecimenRights(specimen);
			specimen.disable();
			return ResponseEvent.response(SpecimenDetail.from(specimen));
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
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			Specimen specimen = getSpecimen(crit.getId(), crit.getName(), ose);
			if (specimen == null) {
				return ResponseEvent.error(ose);
			}
			
			AccessCtrlMgr.getInstance().ensureReadSpecimenRights(specimen);
			return ResponseEvent.response(specimen.getDependentEntities());
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
			
	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenDetail>> collectSpecimens(RequestEvent<List<SpecimenDetail>> req) {
		try {
			List<SpecimenDetail> result = new ArrayList<SpecimenDetail>();
			for (SpecimenDetail detail : req.getPayload()) {
				Specimen specimen = collectSpecimen(detail, null);
				result.add(SpecimenDetail.from(specimen));
			}
			
			return ResponseEvent.response(result);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenDetail>> createAliquots(RequestEvent<SpecimenAliquotsSpec> req) {
		try {
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			
			SpecimenAliquotsSpec spec = req.getPayload();
			Specimen parentSpecimen = getSpecimen(spec.getParentId(), spec.getParentLabel(), ose);
			ose.checkAndThrow();
			
			if (!parentSpecimen.isCollected()) {
				return ResponseEvent.userError(SpecimenErrorCode.NOT_COLLECTED, parentSpecimen.getLabel());
			}
									
			Integer count = spec.getNoOfAliquots();
			Double aliquotQty = spec.getQtyPerAliquot();
			double reqQty = 0;
						
			if (count != null && aliquotQty != null) {
				if (count <= 0 || aliquotQty <= 0) {
					return ResponseEvent.userError(SpecimenErrorCode.INVALID_QTY_OR_CNT);
				}
				
				reqQty = count * aliquotQty;
				if (reqQty > parentSpecimen.getAvailableQuantity()) {
					return ResponseEvent.userError(SpecimenErrorCode.INSUFFICIENT_QTY);
				}
			} else if (count != null && count > 0) {				
				aliquotQty =  Math.round(parentSpecimen.getAvailableQuantity() / count * 10000) / 10000.0; 
			} else if (aliquotQty != null && aliquotQty > 0) {
				count = (int)Math.floor(parentSpecimen.getAvailableQuantity() / aliquotQty);
			} else {
				return ResponseEvent.userError(SpecimenErrorCode.INVALID_QTY_OR_CNT);
			}
			
			List<SpecimenDetail> aliquots = new ArrayList<SpecimenDetail>();
			for (int i = 0; i < count; ++i) {
				SpecimenDetail aliquot = new SpecimenDetail();
				aliquot.setLineage(Specimen.ALIQUOT);
				aliquot.setInitialQty(aliquotQty);
				aliquot.setAvailableQty(aliquotQty);
				aliquot.setParentLabel(parentSpecimen.getLabel());
				aliquot.setParentId(parentSpecimen.getId());
				aliquot.setCreatedOn(spec.getCreatedOn());
				
				StorageLocationSummary location = new StorageLocationSummary();
				location.name = spec.getContainerName();
				aliquot.setStorageLocation(location);
				
				aliquots.add(aliquot);				
			}
			
			return collectSpecimens(new RequestEvent<List<SpecimenDetail>>(aliquots));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> doesSpecimenExists(RequestEvent<String> req) {
		return ResponseEvent.response(daoFactory.getSpecimenDao().getByLabel(req.getPayload()) != null);
	}
	
	
	@Override
	@PlusTransactional
	public ResponseEvent<LabelPrintJobSummary> printSpecimenLabels(RequestEvent<PrintSpecimenLabelDetail> req) {
		PrintSpecimenLabelDetail printDetail = req.getPayload();
		
		LabelPrinter<Specimen> printer = getLabelPrinter();
		if (printer == null) {
			return ResponseEvent.serverError(SpecimenErrorCode.NO_PRINTER_CONFIGURED);
		}
				
		List<Specimen> specimens = getSpecimensToPrint(printDetail);
		if (CollectionUtils.isEmpty(specimens)) {
			return ResponseEvent.userError(SpecimenErrorCode.NO_SPECIMENS_TO_PRINT);
		}
		
		LabelPrintJob job = printer.print(specimens, printDetail.getNumCopies());
		if (job == null) {
			return ResponseEvent.userError(SpecimenErrorCode.PRINT_ERROR);
		}
		
		return ResponseEvent.response(LabelPrintJobSummary.from(job));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public LabelPrinter<Specimen> getLabelPrinter() {
		String labelPrinterBean = cfgSvc.getStrSetting(
				ConfigParams.MODULE, 
				ConfigParams.SPECIMEN_LABEL_PRINTER, 
				"defaultSpecimenLabelPrinter");
		
		return (LabelPrinter<Specimen>)OpenSpecimenAppCtxProvider.getAppCtx().getBean(labelPrinterBean);
	}
		
	@Override
	@PlusTransactional
	public ResponseEvent<Map<String, Long>> getCprAndVisitIds(RequestEvent<Long> req) {
		try {
			Map<String, Long> ids = daoFactory.getSpecimenDao().getCprAndVisitIds(req.getPayload());
			if (ids == null || ids.isEmpty()) {
				return ResponseEvent.userError(SpecimenErrorCode.NOT_FOUND, req.getPayload());
			}
			
			return ResponseEvent.response(ids);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
		
	private void ensureValidAndUniqueLabel(Specimen specimen, OpenSpecimenException ose) {
		CollectionProtocol cp = specimen.getCollectionProtocol();
		String labelTmpl = specimen.getLabelTmpl();
		
		String label = specimen.getLabel();		
		if (StringUtils.isBlank(label)) {
			boolean labelReq = cp.isManualSpecLabelEnabled() || StringUtils.isBlank(labelTmpl);
			if (labelReq) {
				ose.addError(SpecimenErrorCode.LABEL_REQUIRED);
			}
			
			return;
		}

		if (StringUtils.isNotBlank(labelTmpl)) {
			if (!cp.isManualSpecLabelEnabled()) {
				ose.addError(SpecimenErrorCode.MANUAL_LABEL_NOT_ALLOWED);
				return;
			}
			
			if (!labelGenerator.validate(labelTmpl, specimen, label)) {
				ose.addError(SpecimenErrorCode.INVALID_LABEL, label);
				return;
			}
		}
		
		if (daoFactory.getSpecimenDao().getByLabel(label) != null) {
			ose.addError(SpecimenErrorCode.DUP_LABEL, label);
		}
	}

	private void ensureUniqueBarcode(String barcode, OpenSpecimenException ose) {
		if (daoFactory.getSpecimenDao().getByBarcode(barcode) != null) {
			ose.addError(SpecimenErrorCode.DUP_BARCODE, barcode);
		}
	}

	private Specimen collectSpecimen(SpecimenDetail detail, Specimen parent) {
		Specimen existing = null;
		if (detail.getId() != null) {
			existing = daoFactory.getSpecimenDao().getById(detail.getId());
			if (existing == null) {
				throw OpenSpecimenException.userError(SpecimenErrorCode.NOT_FOUND, detail.getId());
			}
		}
		
		Specimen specimen = existing;
		if (existing == null || !existing.isCollected()) {
			specimen = saveOrUpdate(detail, existing, parent, false);
		}
				
		if (detail.getChildren() != null) {
			for (SpecimenDetail childDetail : detail.getChildren()) {
				collectSpecimen(childDetail, specimen);
			}
		}
		
		boolean closeAfterChildrenCreation = false;
		if (detail.getCloseAfterChildrenCreation() != null) {
			closeAfterChildrenCreation = detail.getCloseAfterChildrenCreation();
		}
		
		if (closeAfterChildrenCreation) {
			specimen.close(AuthUtil.getCurrentUser(), Calendar.getInstance().getTime(), "");
		}
		
		return specimen;
	}
	
	private ResponseEvent<SpecimenDetail> updateSpecimen(SpecimenDetail detail, boolean partial) {
		try {
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			Specimen existing = getSpecimen(detail.getId(), detail.getLabel(), ose);
			if (existing == null) {
				return ResponseEvent.error(ose);
			}
			
			AccessCtrlMgr.getInstance().ensureCreateOrUpdateSpecimenRights(existing);
			saveOrUpdate(detail, existing, null, partial);
			return ResponseEvent.response(SpecimenDetail.from(existing));			
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private Specimen saveOrUpdate(SpecimenDetail detail, Specimen existing, Specimen parent, boolean partial) {
		if (existing != null && !existing.isActive()) {
			throw OpenSpecimenException.userError(SpecimenErrorCode.EDIT_NOT_ALLOWED, existing.getLabel());
		}
		
		Specimen specimen = null;		
		if (partial) {
			specimen = specimenFactory.createSpecimen(existing, detail, parent);
		} else {
			specimen = specimenFactory.createSpecimen(detail, parent);
		}
		
		AccessCtrlMgr.getInstance().ensureCreateOrUpdateSpecimenRights(specimen);

		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		if (existing == null || StringUtils.isBlank(existing.getLabel())) {			
			ensureValidAndUniqueLabel(specimen, ose);
		}

		String barcode = specimen.getBarcode();
		if (StringUtils.isNotBlank(barcode) &&
			(existing == null || !barcode.equals(existing.getBarcode()))) {
			ensureUniqueBarcode(specimen.getBarcode(), ose);
		}

		ose.checkAndThrow();

		boolean newSpecimen = true;
		if (existing != null) {
			existing.update(specimen);
			specimen = existing;
			newSpecimen = false;
		} else if (specimen.getParentSpecimen() != null) {
			if (!specimen.getParentSpecimen().isActive()) {
				throw OpenSpecimenException.userError(SpecimenErrorCode.EDIT_NOT_ALLOWED, specimen.getParentSpecimen().getLabel());
			}
			
			specimen.getParentSpecimen().addSpecimen(specimen);
		} else {
			specimen.checkQtyConstraints(); // TODO: Should we be calling this at all?
			specimen.occupyPosition();
		}

		specimen.setLabelIfEmpty();
		daoFactory.getSpecimenDao().saveOrUpdate(specimen);
		if (newSpecimen) {
			addEvents(specimen);
		}
		return specimen;
	}
	
	private void addEvents(Specimen specimen) {
		if (!specimen.isCollected()) {
			return;
		}
		
		specimen.addCollRecvEvents();
	}
	
	private List<Specimen> getSpecimensToPrint(PrintSpecimenLabelDetail detail) {
		SpecimenDao specimenDao = daoFactory.getSpecimenDao();
		
		List<Specimen> specimens = null;
		if (CollectionUtils.isNotEmpty(detail.getSpecimenIds())) {
			specimens = specimenDao.getSpecimensByIds(detail.getSpecimenIds());
		} else if (CollectionUtils.isNotEmpty(detail.getSpecimenLabels())) {
			specimens = specimenDao.getSpecimens(new SpecimenListCriteria().labels(detail.getSpecimenLabels()));
		} else if (detail.getVisitId() != null) {
			specimens = specimenDao.getSpecimensByVisitId(detail.getVisitId());
		} else if (StringUtils.isNotBlank(detail.getVisitName())) {
			specimens = specimenDao.getSpecimensByVisitName(detail.getVisitName());
		}
		
		return specimens;		
	}
	
	private Specimen getSpecimen(Long specimenId, String label, OpenSpecimenException ose) {
		Specimen specimen = null;
		Object key = null;
		
		if (specimenId != null) {
			key = specimenId;
			specimen = daoFactory.getSpecimenDao().getById(specimenId);
		} else if (StringUtils.isNotBlank(label)) {
			key = label;
			specimen = daoFactory.getSpecimenDao().getByLabel(label);
		}
		
		if (specimen == null) {
			ose.addError(SpecimenErrorCode.NOT_FOUND, key);
		}
		
		return specimen;
	}
}
