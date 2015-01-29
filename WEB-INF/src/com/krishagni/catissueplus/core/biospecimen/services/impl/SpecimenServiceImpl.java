
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.barcodegenerator.BarcodeGenerator;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.events.CollectSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimensCollectedEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.labelgenerator.LabelGenerator;
import com.krishagni.catissueplus.core.printer.printService.factory.SpecimenLabelPrinterFactory;
import com.krishagni.catissueplus.core.privileges.services.PrivilegeService;

public class SpecimenServiceImpl implements SpecimenService {

	private DaoFactory daoFactory;

	private SpecimenFactory specimenFactory;

	private LabelGenerator<Specimen> specimenLabelGenerator;

	private BarcodeGenerator<Specimen> specimenBarcodeGenerator;
	
	private PrivilegeService privilegeSvc;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSpecimenFactory(SpecimenFactory specimenFactory) {
		this.specimenFactory = specimenFactory;
	}

	@Autowired
	private SpecimenLabelPrinterFactory specLabelPrinterFact;

	public void setSpecLabelPrinterFact(SpecimenLabelPrinterFactory specLabelPrinterFact) {
		this.specLabelPrinterFact = specLabelPrinterFact;
	}

	public void setSpecimenLabelGenerator(LabelGenerator<Specimen> specimenLabelGenerator) {
		this.specimenLabelGenerator = specimenLabelGenerator;
	}

	public void setSpecimenBarcodeGenerator(BarcodeGenerator<Specimen> specimenBarcodeGenerator) {
		this.specimenBarcodeGenerator = specimenBarcodeGenerator;
	}
	
	public void setPrivilegeSvc(PrivilegeService privilegeSvc) {
		this.privilegeSvc = privilegeSvc;
	}

	//
	// TODO: refactor
	//
	@Override
	@PlusTransactional
	public SpecimenCreatedEvent createSpecimen(CreateSpecimenEvent event) {
		try {
			Specimen specimen = specimenFactory.createSpecimen(event.getSpecimen());
			ObjectCreationException errorHandler = new ObjectCreationException();
			setLabel(event.getSpecimen().getLabel(), specimen, errorHandler);
			setBarcode(event.getSpecimen().getBarcode(), specimen, errorHandler);
			ensureUniqueBarcode(specimen.getBarcode(), errorHandler);
			ensureUniqueLabel(specimen.getLabel(), errorHandler);
			errorHandler.checkErrorAndThrow();
			daoFactory.getSpecimenDao().saveOrUpdate(specimen);
			return SpecimenCreatedEvent.ok(SpecimenDetail.from(specimen));
		}
		catch (ObjectCreationException oce) {
			return SpecimenCreatedEvent.invalidRequest(ScgErrorCode.ERRORS.message(), oce.getErroneousFields());
		}
		catch (Exception ex) {
			return SpecimenCreatedEvent.serverError(ex);
		}
	}
	
	@Override
	@PlusTransactional
	public SpecimensCollectedEvent collectSpecimens(CollectSpecimensEvent req) {
		try {
			List<SpecimenDetail> result = new ArrayList<SpecimenDetail>();
			for (SpecimenDetail detail : req.getSpecimens()) {
				Specimen specimen = collectSpecimen(detail, null);
				result.add(SpecimenDetail.from(specimen));
			}
			
			return SpecimensCollectedEvent.ok(result);
		} catch (IllegalArgumentException iae) {
			return SpecimensCollectedEvent.badRequest(iae);
		} catch (ObjectCreationException oce) {
			return SpecimensCollectedEvent.badRequest(oce);
		} catch (Exception e) {
			return SpecimensCollectedEvent.badRequest(e);
		}
	}
	
	@Override
	@PlusTransactional
	public boolean doesSpecimenExists(String label) {
		return daoFactory.getSpecimenDao().doesSpecimenExistsByLabel(label);
	}
	
	private void ensureUniqueLabel(String label, ObjectCreationException oce) {
		if (daoFactory.getSpecimenDao().getSpecimenByLabel(label) != null) {
			oce.addError(SpecimenErrorCode.DUPLICATE_LABEL, LABEL);
		}
	}

	private void ensureUniqueBarcode(String barcode, ObjectCreationException oce) {
		if (daoFactory.getSpecimenDao().getSpecimenByBarcode(barcode) != null) {
			oce.addError(SpecimenErrorCode.DUPLICATE_BARCODE, BARCODE);
		}
	}

	private void setLabel(String label, Specimen specimen, ObjectCreationException errorHandler) {
		String specimenLabelFormat = specimen.getVisit().getRegistration()
				.getCollectionProtocol().getSpecimenLabelFormat();
		if (isBlank(specimenLabelFormat)) {
			if (isBlank(label)) {
				errorHandler.addError(SpecimenErrorCode.MISSING_ATTR_VALUE, LABEL);
				return;
			}
			specimen.setLabel(label);
		}
		else {
			if (!isBlank(label)) {
				errorHandler.addError(SpecimenErrorCode.AUTO_GENERATED_LABEL, LABEL);
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
	private void setBarcode(String barcode, Specimen specimen, ObjectCreationException errorHandler) {
		//TODO: Get Barcode Format 
		//		String barcodeFormat = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
		//				.getCollectionProtocol();

		String barcodeFormat = null;
		if (isBlank(barcodeFormat)) {
			if (isBlank(barcode)) {
				specimen.setBarcode(specimenBarcodeGenerator.generateBarcode(DEFAULT_BARCODE_TOKEN, specimen));
				return;
			}
			else {
				specimen.setBarcode(barcode);
			}
		}
		else {
			if (!isBlank(barcode)) {
				errorHandler.addError(SpecimenErrorCode.AUTO_GENERATED_BARCODE, BARCODE);
				return;
			}
			specimen.setBarcode(specimenBarcodeGenerator.generateBarcode(barcodeFormat, specimen));
		}

	}
	
	private Specimen collectSpecimen(SpecimenDetail detail, Specimen parent) {
		Specimen specimen = specimenFactory.createSpecimen(detail);
		
		ObjectCreationException oce = new ObjectCreationException();
		ensureUniqueLabel(specimen.getLabel(), oce);
		ensureUniqueBarcode(specimen.getBarcode(), oce);
		
		oce.checkErrorAndThrow();
		
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

	private static final String LABEL = "label";

	private static final String BARCODE = "barcode";

	private static final String DEFAULT_BARCODE_TOKEN = "SPECIMEN_LABEL";
}
