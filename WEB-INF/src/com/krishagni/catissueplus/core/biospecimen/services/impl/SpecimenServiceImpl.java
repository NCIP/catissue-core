
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.barcodegenerator.BarcodeGenerator;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.events.AliquotCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateAliquotEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.PatchSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenSummary;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.de.services.SavedQueryErrorCode;
import com.krishagni.catissueplus.core.labelgenerator.LabelGenerator;
import com.krishagni.catissueplus.core.printer.printService.factory.SpecimenLabelPrinterFactory;
import com.krishagni.catissueplus.core.privileges.services.PrivilegeService;

import edu.wustl.security.global.Permissions;

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

	@Override
	@PlusTransactional
	public SpecimensSummaryEvent getSpecimens(ReqSpecimensEvent req) {
		List<Specimen> specimens = null; 
		Long count = null;
		
		if(req.getSpecimenLabels() != null) {
			specimens = daoFactory.getSpecimenDao().getSpecimensByLabel(req.getSpecimenLabels());
			if(req.isCountReq()) {
				count = (long) specimens.size();
			}
		}
		else{
			if (req.getStartAt() < 0 || req.getMaxRecords() <= 0) {
				String msg = SavedQueryErrorCode.INVALID_PAGINATION_FILTER.message();
				return SpecimensSummaryEvent.badRequest(msg, null);
			}
			specimens = daoFactory.getSpecimenDao().getAllSpecimens(
					req.getStartAt(), req.getMaxRecords(), 
					req.getSearchString());
		}
		
		List<SpecimenSummary> result = new ArrayList<SpecimenSummary>();
		for (Specimen specimen : specimens) {
			Long cpId = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getId();
			if(privilegeSvc.hasPrivilege(req.getSessionDataBean().getUserId(), cpId, Permissions.SPECIMEN_PROCESSING)) {
				result.add(SpecimenSummary.from(specimen));
			}
		}
		
		if (req.isCountReq() && count == null) {
			count = daoFactory.getSpecimenDao().getSpecimensCount(req.getSearchString());
		}

		return SpecimensSummaryEvent.ok(result,count);
	}
	

	@Override
	@PlusTransactional
	public SpecimenCreatedEvent createSpecimen(CreateSpecimenEvent event) {
		try {
			Specimen specimen = specimenFactory.createSpecimen(event.getSpecimenDetail());
			ObjectCreationException errorHandler = new ObjectCreationException();
			setLabel(event.getSpecimenDetail().getLabel(), specimen, errorHandler);
			setBarcode(event.getSpecimenDetail().getBarcode(), specimen, errorHandler);
			ensureUniqueBarocde(specimen.getBarcode(), errorHandler);
			ensureUniqueLabel(specimen.getLabel(), errorHandler);
			errorHandler.checkErrorAndThrow();
			daoFactory.getSpecimenDao().saveOrUpdate(specimen);
			if (event.getSpecimenDetail().isPrintLabelsEnabled()) {
				specLabelPrinterFact.printLabel(specimen, event.getSessionDataBean().getIpAddress(), event.getSessionDataBean()
						.getUserName(), specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
						.getCollectionProtocol().getShortTitle());
			}
			return SpecimenCreatedEvent.ok(SpecimenDetail.fromDomain(specimen));
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
	public SpecimenUpdatedEvent updateSpecimen(UpdateSpecimenEvent event) {
		try {
			Long specimenId = event.getId();
			Specimen oldSpecimen = daoFactory.getSpecimenDao().getSpecimen(specimenId);
			if (oldSpecimen == null) {
				return SpecimenUpdatedEvent.notFound(specimenId);
			}
			Specimen specimen = specimenFactory.createSpecimen(event.getSpecimenDetail());
			ObjectCreationException errorHandler = new ObjectCreationException();
			updateLabel(event.getSpecimenDetail().getLabel(), specimen, oldSpecimen, errorHandler);
			updateBarcode(event.getSpecimenDetail().getBarcode(), specimen, oldSpecimen, errorHandler);
			validateLabelBarcodeUnique(oldSpecimen, specimen, errorHandler);
			errorHandler.checkErrorAndThrow();
			oldSpecimen.update(specimen);
			daoFactory.getSpecimenDao().saveOrUpdate(oldSpecimen);
			return SpecimenUpdatedEvent.ok(SpecimenDetail.fromDomain(specimen));
		}
		catch (ObjectCreationException oce) {
			return SpecimenUpdatedEvent.invalidRequest(ScgErrorCode.ERRORS.message(), oce.getErroneousFields());
		}
		catch (Exception ex) {
			return SpecimenUpdatedEvent.serverError(ex);
		}
	}

	@Override
	@PlusTransactional
	public SpecimenUpdatedEvent patchSpecimen(PatchSpecimenEvent event) {
		try {
			Long specimenId = event.getId();
			Specimen oldSpecimen = daoFactory.getSpecimenDao().getSpecimen(specimenId);
			if (oldSpecimen == null) {
				return SpecimenUpdatedEvent.notFound(specimenId);
			}
			Specimen specimen = specimenFactory.patch(oldSpecimen, event.getDetail());

			ObjectCreationException errorHandler = new ObjectCreationException();
			if (event.getDetail().isLabelModified()) {
				updateLabel(event.getDetail().getLabel(), specimen, oldSpecimen, errorHandler);

			}
			if (event.getDetail().isBarcodeModified()) {
				updateBarcode(event.getDetail().getBarcode(), specimen, oldSpecimen, errorHandler);
			}
			validateLabelBarcodeUnique(oldSpecimen, specimen, errorHandler);
			errorHandler.checkErrorAndThrow();
			oldSpecimen.update(specimen);
			daoFactory.getSpecimenDao().saveOrUpdate(oldSpecimen);
			return SpecimenUpdatedEvent.ok(SpecimenDetail.fromDomain(specimen));
		}
		catch (ObjectCreationException oce) {
			return SpecimenUpdatedEvent.invalidRequest(ScgErrorCode.ERRORS.message(), oce.getErroneousFields());
		}
		catch (Exception ex) {
			return SpecimenUpdatedEvent.serverError(ex);
		}
	}

	@Override
	@PlusTransactional
	public SpecimenDeletedEvent delete(DeleteSpecimenEvent event) {
		try {
			Specimen specimen = daoFactory.getSpecimenDao().getSpecimen(event.getId());
			if (specimen == null) {
				return SpecimenDeletedEvent.notFound(event.getId());
			}
			specimen.delete(event.isIncludeChildren());

			daoFactory.getSpecimenDao().saveOrUpdate(specimen);
			return SpecimenDeletedEvent.ok();
		}
		catch (CatissueException ce) {
			return SpecimenDeletedEvent.invalidRequest(ce.getMessage());
		}
		catch (Exception e) {
			return SpecimenDeletedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public AliquotCreatedEvent createAliquot(CreateAliquotEvent createAliquotEvent) {

		try {
			if (createAliquotEvent.getAliquotDetail().getNoOfAliquots() < 1) {
				ObjectCreationException.raiseError(SpecimenErrorCode.INVALID_ALIQUOT_COUNT, ALIQUOT_COUNT);
			}
			Specimen specimen = daoFactory.getSpecimenDao().getSpecimen(createAliquotEvent.getSpecimenId());
			if (specimen == null) {
				return AliquotCreatedEvent.notFound(createAliquotEvent.getSpecimenId());
			}
			if (specimen.getAvailableQuantity() == 0) {
				ObjectCreationException.raiseError(SpecimenErrorCode.INSUFFICIENT_SPECIMEN_QTY, SPECIMEN_AVAILABLE_QUANTITY);
			}

			Set<Specimen> aliquots = specimenFactory.createAliquots(specimen, createAliquotEvent.getAliquotDetail());
			Set<Specimen> childCollection = specimen.getChildCollection();
			childCollection.addAll(aliquots);
			daoFactory.getSpecimenDao().saveOrUpdate(specimen);

			List<SpecimenDetail> aliquotList = new ArrayList<SpecimenDetail>();
			for (Specimen aliquot : aliquots) {
				aliquotList.add(SpecimenDetail.fromDomain(aliquot));
			}
			return AliquotCreatedEvent.ok(aliquotList);
		}
		catch (ObjectCreationException oce) {
			return AliquotCreatedEvent.invalidRequest(SpecimenErrorCode.ERRORS.message(), oce.getErroneousFields());
		}
		catch (Exception ex) {
			return AliquotCreatedEvent.serverError(ex);
		}
	}

	private void validateLabelBarcodeUnique(Specimen oldSpecimen, Specimen newSpecimen,
			ObjectCreationException errorHandler) {
		if (!isBlank(newSpecimen.getBarcode()) && !newSpecimen.getBarcode().equals(oldSpecimen.getBarcode())) {
			ensureUniqueBarocde(newSpecimen.getBarcode(), errorHandler);
		}

		if (!isBlank(newSpecimen.getLabel()) && !newSpecimen.getLabel().equals(oldSpecimen.getLabel())) {
			ensureUniqueLabel(newSpecimen.getLabel(), errorHandler);
		}
	}

	private void ensureUniqueLabel(String label, ObjectCreationException errorHandler) {
		if (!daoFactory.getSpecimenDao().isLabelUnique(label)) {
			errorHandler.addError(SpecimenErrorCode.DUPLICATE_LABEL, LABEL);
		}
	}

	private void ensureUniqueBarocde(String barcode, ObjectCreationException errorHandler) {
		if (!daoFactory.getSpecimenDao().isBarcodeUnique(barcode)) {
			errorHandler.addError(SpecimenErrorCode.DUPLICATE_BARCODE, BARCODE);
		}
	}

	private void setLabel(String label, Specimen specimen, ObjectCreationException errorHandler) {
		String specimenLabelFormat = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
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

	private void updateLabel(String label, Specimen specimen, Specimen oldSpecimen, ObjectCreationException errorHandler) {
		String specimenLabelFormat = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
				.getCollectionProtocol().getSpecimenLabelFormat();

		if (isBlank(specimenLabelFormat)) {
			if (isBlank(label)) {
				errorHandler.addError(SpecimenErrorCode.MISSING_ATTR_VALUE, LABEL);
				return;
			}
			specimen.setLabel(label);
		}
		else if (!oldSpecimen.getLabel().equalsIgnoreCase(label)) {
			errorHandler.addError(SpecimenErrorCode.AUTO_GENERATED_LABEL, LABEL);
			return;
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

	private void updateBarcode(String barcode, Specimen specimen, Specimen oldSpecimen,
			ObjectCreationException errorHandler) {
		//TODO: Get Barcode Format 
		//		String barcodeFormat = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
		//				.getCollectionProtocol();

		String barcodeFormat = null;
		if (isBlank(barcodeFormat)) {
			if (isBlank(barcode)) {
				errorHandler.addError(SpecimenErrorCode.MISSING_ATTR_VALUE, BARCODE);
				return;
			}
			specimen.setBarcode(barcode);
		}
		else if (!specimen.getBarcode().equalsIgnoreCase(oldSpecimen.getBarcode())) {
			errorHandler.addError(SpecimenErrorCode.AUTO_GENERATED_BARCODE, BARCODE);
			return;
		}
	}
	
	private static final String LABEL = "label";

	private static final String BARCODE = "barcode";

	private static final String ALIQUOT_COUNT = "Aliquot Count";

	private static final String SPECIMEN_AVAILABLE_QUANTITY = "Specimen available quantity ";

	private static final String DEFAULT_BARCODE_TOKEN = "SPECIMEN_LABEL";

}
