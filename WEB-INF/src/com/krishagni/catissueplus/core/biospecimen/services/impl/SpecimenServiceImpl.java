
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.StorageLocationSummary;
import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CpEntityDeleteCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.LabelPrintJobSummary;
import com.krishagni.catissueplus.core.biospecimen.events.PrintSpecimenLabelDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ReceivedEventDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenAliquotsSpec;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenStatusDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenResolver;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.domain.LabelPrintJob;
import com.krishagni.catissueplus.core.common.domain.PrintItem;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.CommonErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.service.ConfigChangeListener;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.service.LabelGenerator;
import com.krishagni.catissueplus.core.common.service.LabelPrinter;
import com.krishagni.catissueplus.core.common.service.ObjectStateParamsResolver;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.NumUtil;
import com.krishagni.catissueplus.core.common.util.Status;

public class SpecimenServiceImpl implements SpecimenService, ObjectStateParamsResolver, ConfigChangeListener, InitializingBean {

	private DaoFactory daoFactory;

	private SpecimenFactory specimenFactory;

	private SpecimenResolver specimenResolver;
	
	private ConfigurationService cfgSvc;

	private LabelGenerator labelGenerator;

	private int precision = 6;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSpecimenFactory(SpecimenFactory specimenFactory) {
		this.specimenFactory = specimenFactory;
	}

	public void setSpecimenResolver(SpecimenResolver specimenResolver) {
		this.specimenResolver = specimenResolver;
	}

	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}

	public void setLabelGenerator(LabelGenerator labelGenerator) {
		this.labelGenerator = labelGenerator;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenDetail> getSpecimen(RequestEvent<SpecimenQueryCriteria> req) {
		try {
			SpecimenQueryCriteria crit = req.getPayload();
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			Specimen specimen = getSpecimen(crit.getId(), crit.getCpShortTitle(), crit.getName(), ose);
			if (specimen == null) {
				return ResponseEvent.error(ose);
			}
			
			boolean allowPhi = AccessCtrlMgr.getInstance().ensureReadSpecimenRights(specimen);
			SpecimenDetail detail = SpecimenDetail.from(specimen, false, !allowPhi);
			setDistributionStatus(detail);
			return ResponseEvent.response(detail);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenInfo>> getSpecimens(RequestEvent<List<String>> req) {
		try {
			List<Specimen> specimens = getSpecimensByLabel(req.getPayload());
			return ResponseEvent.response(SpecimenInfo.from(Specimen.sortByLabels(specimens, req.getPayload())));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenInfo>> getSpecimensById(RequestEvent<List<Long>> req) {
		try {
			List<Specimen> specimens = getSpecimensById(req.getPayload());
			return ResponseEvent.response(SpecimenInfo.from(Specimen.sortByIds(specimens, req.getPayload())));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenInfo>> getPrimarySpecimensByCp(RequestEvent<Long> req) {
		try {
			Long cpId = req.getPayload();
			if (cpId == null) {
				return ResponseEvent.response(Collections.emptyList());
			}

			SpecimenListCriteria crit = new SpecimenListCriteria()
				.cpId(cpId)
				.lineages(new String[] {Specimen.NEW})
				.collectionStatuses(new String[] {Specimen.COLLECTED})
				.limitItems(true);
			return ResponseEvent.response(SpecimenInfo.from(getSpecimens(crit)));
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
			Specimen specimen = saveOrUpdate(detail, null, null);
			return ResponseEvent.response(SpecimenDetail.from(specimen, false, false));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception ex) {
			return ResponseEvent.serverError(ex);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenDetail> updateSpecimen(RequestEvent<SpecimenDetail> req) {
		try {
			SpecimenDetail detail = req.getPayload();
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			Specimen existing = getSpecimen(detail.getId(), detail.getCpShortTitle(), detail.getLabel(), ose);
			if (existing == null) {
				return ResponseEvent.error(ose);
			}
			
			AccessCtrlMgr.getInstance().ensureCreateOrUpdateSpecimenRights(existing);
			saveOrUpdate(detail, existing, null);
			return ResponseEvent.response(SpecimenDetail.from(existing, false, false));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenDetail>> updateSpecimensStatus(RequestEvent<List<SpecimenStatusDetail>> req) {
		try {
			List<SpecimenDetail> result = new ArrayList<>();

			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			for (SpecimenStatusDetail detail : req.getPayload()) {
				Specimen specimen = getSpecimen(detail.getId(), detail.getCpShortTitle(), detail.getName(), ose);
				User user = getUser(detail.getUser(), ose);
				Date date = getDisposalDate(specimen, detail.getDate(), ose);
				ose.checkAndThrow();

				AccessCtrlMgr.getInstance().ensureCreateOrUpdateSpecimenRights(specimen, false);
				specimen.updateStatus(detail.getStatus(), user, date, detail.getReason(), detail.isForceUpdate());
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
	public ResponseEvent<List<SpecimenInfo>> deleteSpecimens(RequestEvent<List<CpEntityDeleteCriteria>> request) {
		List<SpecimenInfo> result = new ArrayList<>();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		for (CpEntityDeleteCriteria criteria : request.getPayload()) {
			Specimen specimen = getSpecimen(criteria.getId(), criteria.getCpShortTitle(), criteria.getName(), ose);
			if (specimen == null) {
				continue;
			}

			AccessCtrlMgr.getInstance().ensureDeleteSpecimenRights(specimen);
			specimen.disable(!criteria.isForceDelete());
			result.add(SpecimenInfo.from(specimen));
		}

		return ResponseEvent.response(result);
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<SpecimenQueryCriteria> req) {
		try {
			SpecimenQueryCriteria crit = req.getPayload();
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			Specimen specimen = getSpecimen(crit.getId(), crit.getCpShortTitle(), crit.getName(), ose);
			if (specimen == null) {
				return ResponseEvent.error(ose);
			}
			
			AccessCtrlMgr.getInstance().ensureReadSpecimenRights(specimen, false);
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
			Collection<Specimen> specimens = new ArrayList<Specimen>();
			for (SpecimenDetail detail : req.getPayload()) {
				//
				// Pre-populate specimen interaction objects with
				// appropriate created on time
				//
				setCreatedOn(detail);

				Specimen specimen = collectSpecimen(detail, null);
				specimens.add(specimen);
			}


			getLabelPrinter().print(getSpecimenPrintItems(specimens));
			return ResponseEvent.response(SpecimenDetail.from(specimens));
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
			Specimen parentSpecimen = getSpecimen(spec.getParentId(), spec.getCpShortTitle(), spec.getParentLabel(), ose);
			ose.checkAndThrow();
			
			if (!parentSpecimen.isCollected()) {
				return ResponseEvent.userError(SpecimenErrorCode.NOT_COLLECTED, parentSpecimen.getLabel());
			}

			Integer count = spec.getNoOfAliquots();
			BigDecimal aliquotQty = spec.getQtyPerAliquot();

			if (count != null && aliquotQty != null) {
				if (count <= 0 || NumUtil.lessThanEqualsZero(aliquotQty)) {
					return ResponseEvent.userError(SpecimenErrorCode.INVALID_QTY_OR_CNT);
				}
			} else if (parentSpecimen.getAvailableQuantity() == null) {
				return ResponseEvent.userError(SpecimenErrorCode.ALIQUOT_CNT_N_QTY_REQ);
			} else if (count != null && count > 0) {
				aliquotQty = NumUtil.divide(parentSpecimen.getAvailableQuantity(), count, precision);
			} else if (NumUtil.greaterThanZero(aliquotQty)) {
				count = parentSpecimen.getAvailableQuantity().divide(aliquotQty).intValue();
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
				aliquot.setFreezeThawCycles(spec.getFreezeThawCycles());
				aliquot.setIncrParentFreezeThaw(spec.getIncrParentFreezeThaw());
				aliquot.setExtensionDetail(spec.getExtensionDetail());
				
				StorageLocationSummary location = new StorageLocationSummary();
				location.setName(spec.getContainerName());
				if (i == 0) {
					if (spec.getPosition() != 0) {
						location.setPosition(spec.getPosition());
					} else if (spec.getPositionX() != null && spec.getPositionY() != null) {
						location.setPositionX(spec.getPositionX());
						location.setPositionY(spec.getPositionY());
					}
				}
				aliquot.setStorageLocation(location);
				aliquots.add(aliquot);
			}

			ResponseEvent<List<SpecimenDetail>> resp = collectSpecimens(new RequestEvent<List<SpecimenDetail>>(aliquots));
			if (resp.isSuccessful() && spec.closeParent()) {
				parentSpecimen.close(AuthUtil.getCurrentUser(), new Date(), "");
			}

			return resp;
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenDetail> createDerivative(RequestEvent<SpecimenDetail> derivedReq) {
		try {
			SpecimenDetail spmnDetail = derivedReq.getPayload();
			spmnDetail.setLineage(Specimen.DERIVED);
			spmnDetail.setStatus(Specimen.COLLECTED);

			ResponseEvent<SpecimenDetail> resp = createSpecimen(new RequestEvent<SpecimenDetail>(spmnDetail));
			if (resp.isSuccessful() && spmnDetail.closeParent()) {
				Specimen parent = getSpecimen(spmnDetail.getParentId(), spmnDetail.getCpShortTitle(), spmnDetail.getParentLabel(), null);
				parent.close(AuthUtil.getCurrentUser(), new Date(), "");
			}

			return resp;
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> doesSpecimenExists(RequestEvent<SpecimenQueryCriteria> req) {
		SpecimenQueryCriteria crit = req.getPayload();
		return ResponseEvent.response(getSpecimen(crit.getCpShortTitle(), crit.getName()) != null);
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

		LabelPrintJob job = printer.print(PrintItem.make(specimens, printDetail.getNumCopies()));
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
	public ResponseEvent<Map<String, Object>> getCprAndVisitIds(RequestEvent<Long> req) {
		try {
			Map<String, Object> ids = resolve("id", req.getPayload());
			if (ids == null || ids.isEmpty()) {
				return ResponseEvent.userError(SpecimenErrorCode.NOT_FOUND, req.getPayload());
			}
			
			return ResponseEvent.response(ids);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public List<Specimen> getSpecimensByLabel(List<String> labels) {
		if (CollectionUtils.isEmpty(labels)) {
			throw OpenSpecimenException.userError(CommonErrorCode.INVALID_REQUEST);
		}

		return getSpecimens(new SpecimenListCriteria().labels(labels));
	}

	@Override
	@PlusTransactional
	public List<Specimen> getSpecimensById(List<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			throw OpenSpecimenException.userError(CommonErrorCode.INVALID_REQUEST);
		}

		return getSpecimens(new SpecimenListCriteria().ids(ids));
	}

	@Override
	public String getObjectName() {
		return "specimen";
	}

	@Override
	@PlusTransactional
	public Map<String, Object> resolve(String key, Object value) {
		if (key.equals("id")) {
			value = Long.valueOf(value.toString());
		}

		return daoFactory.getSpecimenDao().getCprAndVisitIds(key, value);
	}

	@Override
	public void onConfigChange(String name, String value) {
		if (name.equals(ConfigParams.UNIQUE_SPMN_LABEL_PER_CP) && !"true".equalsIgnoreCase(value)) {
			boolean dupSpmnLabels = daoFactory.getSpecimenDao().areDuplicateLabelsPresent();
			if (dupSpmnLabels) {
				throw OpenSpecimenException.userError(SpecimenErrorCode.UQ_LBL_CP_CHG_NA);
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		cfgSvc.registerChangeListener(ConfigParams.MODULE, this);
	}

	private List<Specimen> getSpecimens(SpecimenListCriteria crit) {
		List<Pair<Long, Long>> siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps(crit.cpId());
		if (siteCpPairs != null && siteCpPairs.isEmpty()) {
			return Collections.<Specimen>emptyList();
		}

		crit.siteCps(siteCpPairs);
		crit.useMrnSites(AccessCtrlMgr.getInstance().isAccessRestrictedBasedOnMrn());
		return daoFactory.getSpecimenDao().getSpecimens(crit);
	}

	private void setCreatedOn(SpecimenDetail detail) {
		//
		// 1. If primary specimen, copy received time, if available, to created on.
		//    Otherwise use current time for both created on and receive time
		// 2. If not primary specimen and parent is already collected, use current time
		// 3. Copy parent's created on to its children
		//
		Date createdOn = Calendar.getInstance().getTime();
		if (Specimen.NEW.equals(detail.getLineage())) {
			if (detail.getReceivedEvent() != null && detail.getReceivedEvent().getTime() != null) {
				createdOn = detail.getReceivedEvent().getTime();
			} else {
				ReceivedEventDetail receivedEvent = detail.getReceivedEvent();
				if (receivedEvent == null) {
					receivedEvent = new ReceivedEventDetail();
				}

				receivedEvent.setTime(createdOn);
				detail.setReceivedEvent(receivedEvent);
			}

			setCreatedOn(detail.getSpecimensPool(), createdOn);
		}

		setCreatedOn(detail.getChildren(), createdOn);
	}

	private void setCreatedOn(List<SpecimenDetail> details, Date createdOn) {
		if (CollectionUtils.isEmpty(details)) {
			return;
		}

		for (SpecimenDetail detail : details) {
			if (detail.getCreatedOn() == null) {
				detail.setCreatedOn(createdOn);
			}

			setCreatedOn(detail.getChildren(), detail.getCreatedOn());
		}
	}

	private void setDistributionStatus(SpecimenDetail specimen) {
		setDistributionStatus(flattenSpecimenTree(specimen));
	}

	private void setDistributionStatus(List<SpecimenInfo> specimens) {
		List<Long> spmnIds = specimens.stream().map(spmn -> spmn.getId()).collect(Collectors.toList());
		Map<Long, String> distStatuses = daoFactory.getSpecimenDao().getDistributionStatus(spmnIds);
		specimens.stream().forEach(spmn -> spmn.setDistributionStatus(distStatuses.get(spmn.getId())));
	}

	private List<SpecimenInfo> flattenSpecimenTree(SpecimenDetail specimen) {
		return flattenSpecimenTree(specimen, new ArrayList<>());
	}

	private List<SpecimenInfo> flattenSpecimenTree(SpecimenDetail specimen, List<SpecimenInfo> result) {
		result.add(specimen);

		if (CollectionUtils.isNotEmpty(specimen.getChildren())) {
			specimen.getChildren().stream().forEach(child -> flattenSpecimenTree(child, result));
		}

		return result;
	}

	private void ensureEditAllowed(SpecimenDetail detail, Specimen existing) {
		if (existing == null || existing.isActive()) {
			return;
		}
		
		String status = detail.getActivityStatus();
		if (StringUtils.isNotBlank(status) && !Status.isValidActivityStatus(status))  {
			throw OpenSpecimenException.userError(ActivityStatusErrorCode.INVALID);
		}
		
		if (StringUtils.isNotBlank(status) && Status.ACTIVITY_STATUS_ACTIVE.getStatus().equals(status)) {
			return;
		}
		
		throw OpenSpecimenException.userError(SpecimenErrorCode.EDIT_NOT_ALLOWED, existing.getLabel());
	}

	private void ensureValidAndUniqueLabel(Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing != null && 
			StringUtils.isNotBlank(existing.getLabel()) &&
			existing.getLabel().equals(specimen.getLabel())) {
			return;
		}
		
		CollectionProtocol cp = specimen.getCollectionProtocol();
		String labelTmpl = specimen.getLabelTmpl();
		
		String label = specimen.getLabel();		
		if (StringUtils.isBlank(label)) {
			boolean labelReq = cp.isManualSpecLabelEnabled() || StringUtils.isBlank(labelTmpl);
			if (labelReq && specimen.isCollected()) {
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

		if (getSpecimen(cp.getShortTitle(), label) != null) {
			if (areLabelsUniquePerCp()) {
				ose.addError(SpecimenErrorCode.DUP_LABEL_IN_CP, label, cp.getShortTitle());
			} else {
				ose.addError(SpecimenErrorCode.DUP_LABEL, label);
			}
		}
	}

	private void ensureUniqueBarcode(Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (StringUtils.isBlank(specimen.getBarcode())) {
			return;
		}
		
		if (existing != null && specimen.getBarcode().equals(existing.getBarcode())) {
			return;
		}
		
		if (daoFactory.getSpecimenDao().getByBarcode(specimen.getBarcode()) != null) {
			ose.addError(SpecimenErrorCode.DUP_BARCODE, specimen.getBarcode());
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
			existing = collectPoolSpecimens(detail, existing);
			specimen = saveOrUpdate(detail, existing, parent);
		} else {
			collectPoolSpecimens(detail, existing);
		}

		if (CollectionUtils.isNotEmpty(detail.getChildren())) {
			for (SpecimenDetail childDetail : detail.getChildren()) {
				collectSpecimen(childDetail, specimen);
			}
		}

		if (BooleanUtils.isTrue(detail.getCloseAfterChildrenCreation())) {
			specimen.close(AuthUtil.getCurrentUser(), Calendar.getInstance().getTime(), "");
		}

		return specimen;
	}

	private Specimen collectPoolSpecimens(SpecimenDetail detail, Specimen existing) {
		// If not pooled specimen then return existing specimen
		if (CollectionUtils.isEmpty(detail.getSpecimensPool())) {
			return existing;
		}

		Set<Specimen> specimensPool = new HashSet<Specimen>();
		for (SpecimenDetail poolSpmnDetail : detail.getSpecimensPool()) {
			specimensPool.add(collectSpecimen(poolSpmnDetail, null));
		}

		if (existing == null) {
			existing = specimensPool.iterator().next().getPooledSpecimen();
		}

		existing.getSpecimensPool().addAll(specimensPool);
		return existing;
	}

	private Specimen saveOrUpdate(SpecimenDetail detail, Specimen existing, Specimen parent) {
		ensureEditAllowed(detail, existing);

		Specimen specimen = null;
		if (existing != null) {
			specimen = specimenFactory.createSpecimen(existing, detail, parent);
		} else {
			specimen = specimenFactory.createSpecimen(detail, parent);
		}
		
		AccessCtrlMgr.getInstance().ensureCreateOrUpdateSpecimenRights(specimen);

		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		ensureValidAndUniqueLabel(existing, specimen, ose);
		ensureUniqueBarcode(existing, specimen, ose);
		ose.checkAndThrow();

		if (existing != null) {
			existing.update(specimen);
			specimen = existing;
		} else if (specimen.getParentSpecimen() != null) {
			if (!specimen.getParentSpecimen().isActive()) {
				throw OpenSpecimenException.userError(SpecimenErrorCode.EDIT_NOT_ALLOWED, specimen.getParentSpecimen().getLabel());
			}

			specimen.getParentSpecimen().addChildSpecimen(specimen);
		} else {
			specimen.occupyPosition();
			specimen.checkPoolStatusConstraints();
		}

		incrParentFreezeThawCycles(detail, specimen);

		specimen.setLabelIfEmpty();
		daoFactory.getSpecimenDao().saveOrUpdate(specimen);

		specimen.addOrUpdateCollRecvEvents();
		specimen.addOrUpdateExtension();		
		return specimen;
	}

	/**
	 * Returns list of specimens based on input specification
	 * The input specification could be
	 * 1. List of specimen IDs or specimen labels
	 * 2. Flattened list of specimens of a visit identified by either visit name or visit ID
	 */
	private List<Specimen> getSpecimensToPrint(PrintSpecimenLabelDetail detail) {
		List<Specimen> specimens = null;
		if (CollectionUtils.isNotEmpty(detail.getSpecimenIds())) {
			specimens = daoFactory.getSpecimenDao().getSpecimensByIds(detail.getSpecimenIds());
			specimens = Specimen.sortByIds(specimens, detail.getSpecimenIds());
		} else if (CollectionUtils.isNotEmpty(detail.getSpecimenLabels())) {
			specimens = daoFactory.getSpecimenDao().getSpecimens(new SpecimenListCriteria().labels(detail.getSpecimenLabels()));
			specimens = Specimen.sortByLabels(specimens, detail.getSpecimenLabels());
		} else if (detail.getVisitId() != null) {
			Visit visit = daoFactory.getVisitsDao().getById(detail.getVisitId());
			if (visit == null) {
				throw OpenSpecimenException.userError(VisitErrorCode.NOT_FOUND, detail.getVisitId());
			}

			specimens = getFlattenedSpecimens(visit.getTopLevelSpecimens());
		} else if (StringUtils.isNotBlank(detail.getVisitName())) {
			Visit visit = daoFactory.getVisitsDao().getByName(detail.getVisitName());
			if (visit == null) {
				throw OpenSpecimenException.userError(VisitErrorCode.NOT_FOUND, detail.getVisitName());
			}

			specimens = getFlattenedSpecimens(visit.getTopLevelSpecimens());
		}
		
		return specimens;		
	}

	/**
	 * Filters input collection of specimens based on printLabel flag
	 */
	private List<PrintItem<Specimen>> getSpecimenPrintItems(Collection<Specimen> specimens) {
		List<PrintItem<Specimen>> printItems = new ArrayList<PrintItem<Specimen>>();
		for (Specimen specimen : specimens) {
			Integer copies = null;
			if (specimen.getSpecimenRequirement() != null) {
				copies = specimen.getSpecimenRequirement().getLabelPrintCopiesToUse();
			}

			if (specimen.isPrintLabel()) {
				printItems.add(PrintItem.make(specimen, copies));
			}

			if (CollectionUtils.isNotEmpty(specimen.getSpecimensPool())) {
				printItems.addAll(getSpecimenPrintItems(specimen.getSpecimensPool()));
			}

			if (CollectionUtils.isNotEmpty(specimen.getChildCollection())) {
				printItems.addAll(getSpecimenPrintItems(specimen.getChildCollection()));
			}
		}

		return printItems;
	}

	private List<Specimen> getFlattenedSpecimens(Collection<Specimen> specimens) {
		List<Specimen> sortedSpecimens = Specimen.sort(specimens);

		List<Specimen> result = new ArrayList<Specimen>();
		for (Specimen specimen : sortedSpecimens) {
			result.add(specimen);
			result.addAll(getFlattenedSpecimens(specimen.getSpecimensPool()));
			result.addAll(getFlattenedSpecimens(specimen.getChildCollection()));
		}

		return result;
	}

	private Specimen getSpecimen(Long specimenId, String cpShortTitle, String label, OpenSpecimenException ose) {
		return specimenResolver.getSpecimen(specimenId, cpShortTitle, label, ose);
	}

	private Specimen getSpecimen(String cpShortTitle, String label) {
		return specimenResolver.getSpecimen(cpShortTitle, label);
	}

	private boolean areLabelsUniquePerCp() {
		return cfgSvc.getBoolSetting(ConfigParams.MODULE, ConfigParams.UNIQUE_SPMN_LABEL_PER_CP, false);
	}

	private void incrParentFreezeThawCycles(SpecimenDetail detail, Specimen spec) {
		if (spec.getParentSpecimen() == null) {
			return;
		}

		spec.getParentSpecimen().incrementFreezeThaw(detail.getIncrParentFreezeThaw());
	}

	private User getUser(UserSummary detail, OpenSpecimenException ose) {
		Long userId = null;
		String emailAddress = null;
		if (detail != null) {
			userId = detail.getId();
			emailAddress = detail.getEmailAddress();
		}

		User user;
		if (userId != null) {
			user = daoFactory.getUserDao().getById(userId);
		} else if (StringUtils.isNotBlank(emailAddress)) {
			user = daoFactory.getUserDao().getUserByEmailAddress(emailAddress);
		} else {
			user = AuthUtil.getCurrentUser();
		}

		if (user == null) {
			ose.addError(UserErrorCode.NOT_FOUND);
		}

		return user;
	}

	private Date getDisposalDate(Specimen specimen, Date disposalDate, OpenSpecimenException ose) {
		if (disposalDate == null) {
			return Calendar.getInstance().getTime();
		}

		if (specimen.isPrimary() && disposalDate.before(specimen.getCollectionEvent().getTime())) {
			ose.addError(SpecimenErrorCode.DISPOSAL_DT_LT_COLL_DT, disposalDate, specimen.getCollectionEvent().getTime());
		} else if (!specimen.isPrimary() && disposalDate.before(specimen.getCreatedOn())) {
			ose.addError(SpecimenErrorCode.DISPOSAL_DT_LT_CREATED_ON, disposalDate, specimen.getCreatedOn());
		}

		return disposalDate;
	}
}
