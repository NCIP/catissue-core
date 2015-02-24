
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.barcodegenerator.BarcodeGenerator;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.labelgenerator.LabelGenerator;

public class SpecimenServiceImpl implements SpecimenService {

	private DaoFactory daoFactory;

	private SpecimenFactory specimenFactory;

	private LabelGenerator<Specimen> specimenLabelGenerator;

	private BarcodeGenerator<Specimen> specimenBarcodeGenerator;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSpecimenFactory(SpecimenFactory specimenFactory) {
		this.specimenFactory = specimenFactory;
	}

	public void setSpecimenLabelGenerator(LabelGenerator<Specimen> specimenLabelGenerator) {
		this.specimenLabelGenerator = specimenLabelGenerator;
	}

	public void setSpecimenBarcodeGenerator(BarcodeGenerator<Specimen> specimenBarcodeGenerator) {
		this.specimenBarcodeGenerator = specimenBarcodeGenerator;
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
				specimen = daoFactory.getSpecimenDao().getSpecimenByLabel(crit.getName());
			}
			
			if (specimen == null) {
				return ResponseEvent.userError(SpecimenErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(SpecimenDetail.from(specimen));			
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	//
	// TODO: refactor
	//
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenDetail> createSpecimen(RequestEvent<SpecimenDetail> req) {
		try {
			SpecimenDetail detail = req.getPayload();
			Specimen specimen = specimenFactory.createSpecimen(detail);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			setLabel(detail.getLabel(), specimen, ose);
			setBarcode(detail.getBarcode(), specimen, ose);			
			ensureUniqueBarcode(specimen.getBarcode(), ose);
			ensureUniqueLabel(specimen.getLabel(), ose);
			ose.checkAndThrow();
			
			daoFactory.getSpecimenDao().saveOrUpdate(specimen);
			return ResponseEvent.response(SpecimenDetail.from(specimen));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception ex) {
			return ResponseEvent.serverError(ex);
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
	public ResponseEvent<Boolean> doesSpecimenExists(RequestEvent<String> req) {
		return ResponseEvent.response(daoFactory.getSpecimenDao().doesSpecimenExistsByLabel(req.getPayload()));
	}
	
	private void ensureUniqueLabel(String label, OpenSpecimenException ose) {
		if (daoFactory.getSpecimenDao().getSpecimenByLabel(label) != null) {
			ose.addError(SpecimenErrorCode.DUP_LABEL);
		}
	}

	private void ensureUniqueBarcode(String barcode, OpenSpecimenException ose) {
		if (daoFactory.getSpecimenDao().getSpecimenByBarcode(barcode) != null) {
			ose.addError(SpecimenErrorCode.DUP_BARCODE);
		}
	}

	private void setLabel(String label, Specimen specimen, OpenSpecimenException errorHandler) {
		String specimenLabelFormat = specimen.getVisit().getRegistration()
				.getCollectionProtocol().getSpecimenLabelFormat();
		if (StringUtils.isBlank(specimenLabelFormat)) {
			if (StringUtils.isBlank(label)) {
				errorHandler.addError(SpecimenErrorCode.LABEL_REQUIRED);
				return;
			}
			specimen.setLabel(label);
		}
		else {
			if (!StringUtils.isBlank(label)) {
				errorHandler.addError(SpecimenErrorCode.LABEL_AUTO_GENERATED);
				return;
			}
			specimen.setLabel(specimenLabelGenerator.generateLabel(specimenLabelFormat, specimen));
		}
	}


	/**
	 * If Barcode Format is null then set user provided barcode & if user does not provided barcode then 
	 * set Specimen Label as barcode.
	 * 
	 * @param barcode
	 * @param specimen
	 * @param errorHandler
	 */
	private void setBarcode(String barcode, Specimen specimen, OpenSpecimenException errorHandler) {
		//TODO: Get Barcode Format 
		//		String barcodeFormat = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
		//				.getCollectionProtocol();

		String barcodeFormat = null;
		if (StringUtils.isBlank(barcodeFormat)) {
			if (StringUtils.isBlank(barcode)) {
				specimen.setBarcode(specimenBarcodeGenerator.generateBarcode(DEFAULT_BARCODE_TOKEN, specimen));
				return;
			}
			else {
				specimen.setBarcode(barcode);
			}
		}
		else {
			if (!StringUtils.isBlank(barcode)) {
				errorHandler.addError(SpecimenErrorCode.BARCODE_AUTO_GENERATED);
				return;
			}
			specimen.setBarcode(specimenBarcodeGenerator.generateBarcode(barcodeFormat, specimen));
		}

	}
	
	private Specimen collectSpecimen(SpecimenDetail detail, Specimen parent) {
		Specimen specimen = specimenFactory.createSpecimen(detail);
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		ensureUniqueLabel(specimen.getLabel(), ose);
		ensureUniqueBarcode(specimen.getBarcode(), ose);
		
		ose.checkAndThrow();
		
		if (parent == null) {
			parent = specimen.getParentSpecimen();
		}
		
		if (parent == null) {
			specimen.getVisit().addSpecimen(specimen);
		} else {
			parent.addSpecimen(specimen);
		}
		
		if (detail.getChildren() != null) {
			for (SpecimenDetail child : detail.getChildren()) {
				collectSpecimen(child, specimen);
			}
		}
		
		return specimen;
	}

	private static final String DEFAULT_BARCODE_TOKEN = "SPECIMEN_LABEL";
}
