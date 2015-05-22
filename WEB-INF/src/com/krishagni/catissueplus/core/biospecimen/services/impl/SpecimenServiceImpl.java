
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenLabelPrintJob;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.events.PrintSpecimenLabelDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenLabelPrintJobSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenDao;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenLabelPrinter;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.CommonErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;

public class SpecimenServiceImpl implements SpecimenService {

	private DaoFactory daoFactory;

	private SpecimenFactory specimenFactory;
	
	private ConfigurationService cfgSvc;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSpecimenFactory(SpecimenFactory specimenFactory) {
		this.specimenFactory = specimenFactory;
	}
	
	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenDetail> getSpecimen(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();
			
			Specimen specimen = null;
			if (crit.getId() != null) {
				specimen = daoFactory.getSpecimenDao().getById(crit.getId());
			} else if (crit.getName() != null) {
				specimen = daoFactory.getSpecimenDao().getByLabel(crit.getName());
			}
			
			if (specimen == null) {
				return ResponseEvent.userError(SpecimenErrorCode.NOT_FOUND);
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
			
			List<Specimen> specimens = daoFactory.getSpecimenDao().getSpecimensByLabels(labels);
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
	public ResponseEvent<SpecimenDetail> patchSpecimen(RequestEvent<SpecimenDetail> req) {
		return updateSpecimen(req.getPayload(), true);
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
	public ResponseEvent<Boolean> doesSpecimenExists(RequestEvent<String> req) {
		return ResponseEvent.response(daoFactory.getSpecimenDao().getByLabel(req.getPayload()) != null);
	}
	
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenLabelPrintJobSummary> printSpecimenLabels(RequestEvent<PrintSpecimenLabelDetail> req) {
		SpecimenLabelPrinter printer = getLabelPrinter();
		if (printer == null) {
			return ResponseEvent.serverError(SpecimenErrorCode.NO_PRINTER_CONFIGURED);
		}
				
		List<Specimen> specimens = getSpecimensToPrint(req.getPayload());
		if (CollectionUtils.isEmpty(specimens)) {
			return ResponseEvent.userError(SpecimenErrorCode.NO_SPECIMENS_TO_PRINT);
		}
		
		SpecimenLabelPrintJob job = printer.print(specimens);
		if (job == null) {
			return ResponseEvent.userError(SpecimenErrorCode.PRINT_ERROR);
		}
		
		return ResponseEvent.response(SpecimenLabelPrintJobSummary.from(job));
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<Map<String, Long>> getCprAndVisitIds(RequestEvent<Long> req) {
		try {
			Map<String, Long> ids = daoFactory.getSpecimenDao().getCprAndVisitIds(req.getPayload());
			if (ids == null || ids.isEmpty()) {
				return ResponseEvent.userError(SpecimenErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(ids);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
		
	private void ensureUniqueLabel(String label, OpenSpecimenException ose) {
		if (StringUtils.isBlank(label)) {
			return;
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
				throw OpenSpecimenException.userError(SpecimenErrorCode.NOT_FOUND);
			}
		}
		
		Specimen specimen = saveOrUpdate(detail, existing, parent, false);
		if (detail.getChildren() != null) {
			for (SpecimenDetail childDetail : detail.getChildren()) {
				collectSpecimen(childDetail, specimen);
			}
		}
		
		return specimen;
	}
	
	private ResponseEvent<SpecimenDetail> updateSpecimen(SpecimenDetail detail, boolean partial) {
		try {
			Specimen existing = null;
			if (detail.getId() != null) {
				existing = daoFactory.getSpecimenDao().getById(detail.getId());
			} else if (StringUtils.isNotBlank(detail.getLabel())) {
				existing = daoFactory.getSpecimenDao().getByLabel(detail.getLabel());
			}
				
			if (existing == null) {
				throw OpenSpecimenException.userError(SpecimenErrorCode.NOT_FOUND);
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
		Specimen specimen = null;		
		if (partial) {
			specimen = specimenFactory.createSpecimen(existing, detail, parent);
		} else {
			specimen = specimenFactory.createSpecimen(detail, parent);
		}
		
		AccessCtrlMgr.getInstance().ensureCreateOrUpdateSpecimenRights(specimen);

		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		if (existing == null || // no specimen before 
			StringUtils.isBlank(existing.getLabel()) || // no label was specified before 
			!existing.getLabel().equals(specimen.getLabel())) { // new label differs from existing
			
			ensureUniqueLabel(specimen.getLabel(), ose); // check for label uniqueness
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
	
	private SpecimenLabelPrinter getLabelPrinter() {
		String labelPrinterBean = cfgSvc.getStrSetting(
				ConfigParams.MODULE, 
				ConfigParams.LABEL_PRINTER, 
				"defaultSpecimenLabelPrinter");
		return (SpecimenLabelPrinter)OpenSpecimenAppCtxProvider
				.getAppCtx()
				.getBean(labelPrinterBean);
	}
	
	private List<Specimen> getSpecimensToPrint(PrintSpecimenLabelDetail detail) {
		SpecimenDao specimenDao = daoFactory.getSpecimenDao();
		
		List<Specimen> specimens = null;
		if (CollectionUtils.isNotEmpty(detail.getSpecimenIds())) {
			specimens = specimenDao.getSpecimensByIds(detail.getSpecimenIds());
		} else if (CollectionUtils.isNotEmpty(detail.getSpecimenLabels())) {
			specimens = specimenDao.getSpecimensByLabels(detail.getSpecimenLabels());
		} else if (detail.getVisitId() != null) {
			specimens = specimenDao.getSpecimensByVisitId(detail.getVisitId());
		} else if (StringUtils.isNotBlank(detail.getVisitName())) {
			specimens = specimenDao.getSpecimensByVisitName(detail.getVisitName());
		}
		
		CollectionUtils.filter(specimens, new Predicate() {			
			@Override
			public boolean evaluate(Object obj) {
				return ((Specimen)obj).isCollected();
			}
		});
		
		return specimens;		
	}
}