
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.core.barcodegenerator.BarcodeGenerator;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenCollectionGroupFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenGroupsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.GetScgReportEvent;
import com.krishagni.catissueplus.core.biospecimen.events.PatchScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent.ObjectType;
import com.krishagni.catissueplus.core.biospecimen.events.ScgCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgReportEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenCollGroupService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.labelgenerator.LabelGenerator;

@Service(value = "specimenCollGroupService")
public class SpecimenCollGroupServiceImpl implements SpecimenCollGroupService {

	private DaoFactory daoFactory;

	private SpecimenCollectionGroupFactory scgFactory;

	private LabelGenerator<SpecimenCollectionGroup> scgLabelGenerator;

	private BarcodeGenerator<SpecimenCollectionGroup> scgBarcodeGenerator;

	private static final String NAME = "name";

	private static final String BARCODE = "barcode";

	private static final String DEFAULT_BARCODE_TOKEN = "SCG_LABEL";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setScgFactory(SpecimenCollectionGroupFactory scgFactory) {
		this.scgFactory = scgFactory;
	}

	public void setScgLabelGenerator(LabelGenerator<SpecimenCollectionGroup> scgLabelGenerator) {
		this.scgLabelGenerator = scgLabelGenerator;
	}

	public void setScgBarcodeGenerator(BarcodeGenerator<SpecimenCollectionGroup> scgBarcodeGenerator) {
		this.scgBarcodeGenerator = scgBarcodeGenerator;
	}

	@Override
	@PlusTransactional
	public AllSpecimensSummaryEvent getSpecimensList(ReqSpecimenSummaryEvent req) {
		System.out.println();
		try {
			if (ObjectType.CPE.getName().equals(req.getObjectType())) {
				return AllSpecimensSummaryEvent.ok(daoFactory.getScgDao().getSpecimensListFromCpe(req.getId()));

			}
			else {
				return AllSpecimensSummaryEvent.ok(daoFactory.getScgDao().getSpecimensList(req.getId()));
			}

		}
		catch (CatissueException e) {
			return AllSpecimensSummaryEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ScgCreatedEvent createScg(CreateScgEvent scgEvent) {
		try {
			SpecimenCollectionGroup scg = scgFactory.createScg(scgEvent.getScgDetail());
			ObjectCreationException errorHandler = new ObjectCreationException();
			setName(scgEvent.getScgDetail().getName(), scg, errorHandler);
			setBarcode(scgEvent.getScgDetail().getBarcode(), scg, errorHandler);
			ensureUniqueBarcode(scg.getBarcode(), errorHandler);
			ensureUniqueName(scg.getName(), errorHandler);
			errorHandler.checkErrorAndThrow();
			daoFactory.getScgDao().saveOrUpdate(scg);
			return ScgCreatedEvent.ok(ScgDetail.fromDomain(scg));
		}
		catch (ObjectCreationException oce) {
			return ScgCreatedEvent.invalidRequest(ScgErrorCode.ERRORS.message(), oce.getErroneousFields());
		}
		catch (Exception ex) {
			return ScgCreatedEvent.serverError(ex);
		}
	}

	@Override
	@PlusTransactional
	public ScgUpdatedEvent updateScg(UpdateScgEvent scgEvent) {
		try {
			SpecimenCollectionGroup oldScg = daoFactory.getScgDao().getscg(scgEvent.getId());
			if (oldScg == null) {
				ScgUpdatedEvent.notFound(scgEvent.getId());
			}
			SpecimenCollectionGroup scg = scgFactory.createScg(scgEvent.getScgDetail());
			ObjectCreationException errorHandler = new ObjectCreationException();
			updateName(scgEvent.getScgDetail().getName(), scg, oldScg, errorHandler);
			updateBarcode(scgEvent.getScgDetail().getBarcode(), scg, oldScg, errorHandler);
			validateBarcode(oldScg.getBarcode(), scg.getBarcode(), errorHandler);
			validateName(oldScg.getName(), scg.getName(), errorHandler);
			errorHandler.checkErrorAndThrow();
			oldScg.update(scg);
			daoFactory.getScgDao().saveOrUpdate(oldScg);
			return ScgUpdatedEvent.ok(ScgDetail.fromDomain(scg));
		}
		catch (ObjectCreationException oce) {
			return ScgUpdatedEvent.invalidRequest(ScgErrorCode.ERRORS.message(), oce.getErroneousFields());
		}
		catch (Exception ex) {
			return ScgUpdatedEvent.serverError(ex);
		}
	}

	@Override
	@PlusTransactional
	public ScgUpdatedEvent patchScg(PatchScgEvent scgEvent) {
		try {
			SpecimenCollectionGroup oldScg = daoFactory.getScgDao().getscg(scgEvent.getId());
			if (oldScg == null) {
				ScgUpdatedEvent.notFound(scgEvent.getId());
			}
			SpecimenCollectionGroup scg = scgFactory.patch(oldScg, scgEvent.getScgProps());
			ObjectCreationException errorHandler = new ObjectCreationException();
			if (scgEvent.getScgProps().containsKey("name")) {
				String name = (String) scgEvent.getScgProps().get("name");
				updateName(name, scg, oldScg, errorHandler);
			}
			if (scgEvent.getScgProps().containsKey("barcode")) {
				String barcode = (String) scgEvent.getScgProps().get("barcode");
				updateBarcode(barcode, scg, oldScg, errorHandler);
			}
			validateBarcode(oldScg.getBarcode(), scg.getBarcode(), errorHandler);
			validateName(oldScg.getName(), scg.getName(), errorHandler);
			errorHandler.checkErrorAndThrow();
			oldScg.update(scg);
			daoFactory.getScgDao().saveOrUpdate(oldScg);
			return ScgUpdatedEvent.ok(ScgDetail.fromDomain(scg));
		}
		catch (ObjectCreationException oce) {
			return ScgUpdatedEvent.invalidRequest(ScgErrorCode.ERRORS.message(), oce.getErroneousFields());
		}
		catch (Exception ex) {
			return ScgUpdatedEvent.serverError(ex);
		}
	}

	@Override
	public ScgDeletedEvent delete(DeleteSpecimenGroupsEvent event) {
		try {
			SpecimenCollectionGroup scg = daoFactory.getScgDao().getscg(event.getId());
			if (scg == null) {
				return ScgDeletedEvent.notFound(event.getId());
			}
			scg.delete(event.isIncludeChildren());

			daoFactory.getScgDao().saveOrUpdate(scg);
			return ScgDeletedEvent.ok();
		}
		catch (CatissueException ce) {
			return ScgDeletedEvent.invalidRequest(ce.getMessage());
		}
		catch (Exception e) {
			return ScgDeletedEvent.serverError(e);
		}
	}

	private void ensureUniqueName(String name, ObjectCreationException errorHandler) {
		if (!daoFactory.getScgDao().isNameUnique(name)) {
			errorHandler.addError(ScgErrorCode.DUPLICATE_NAME, NAME);
		}
	}

	private void ensureUniqueBarcode(String barcode, ObjectCreationException errorHandler) {
		if (!daoFactory.getScgDao().isBarcodeUnique(barcode)) {
			errorHandler.addError(ScgErrorCode.DUPLICATE_BARCODE, BARCODE);
		}
	}

	private void validateName(String oldName, String newName, ObjectCreationException errorHandler) {
		if (!isBlank(newName) && !newName.equals(oldName)) {
			ensureUniqueName(newName, errorHandler);
		}
	}

	private void validateBarcode(String oldBarcode, String newBarcode, ObjectCreationException errorHandler) {
		if (!isBlank(newBarcode) && !newBarcode.equals(oldBarcode)) {
			ensureUniqueBarcode(newBarcode, errorHandler);
		}
	}

	private void setName(String name, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		//TODO: get SCG Label Format 
		String scgLabelFormat = null;
		if (isBlank(scgLabelFormat)) {
			if (!scg.isCompleted() && isBlank(name)) {
				errorHandler.addError(ScgErrorCode.MISSING_ATTR_VALUE, NAME);
				return;
			}
			scg.setName(name);
		}
		else {
			if (!isBlank(name)) {
				errorHandler.addError(ScgErrorCode.AUTO_GENERATED_LABEL, NAME);
			}
			scg.setName(scgLabelGenerator.generateLabel(scgLabelFormat, scg));
		}
	}

	/**
	 * If Barcode Format is null then set user provided barcode & if user does not provided barcode then 
	 * set Specimen Label as barcode.
	 * @param barcode
	 * @param scg
	 * @param errorHandler
	 */
	private void setBarcode(String barcode, SpecimenCollectionGroup scg, ObjectCreationException errorHandler) {
		//TODO:get Barcode Format from CP.
		String barcodeFormat = null;
		if (isBlank(barcodeFormat)) {
			if (isBlank(barcode)) {
				scg.setBarcode(scgBarcodeGenerator.generateBarcode(DEFAULT_BARCODE_TOKEN, scg));
			}
			else {
				scg.setBarcode(barcode);
			}
		}
		else {
			if (!isBlank(barcode)) {
				errorHandler.addError(SpecimenErrorCode.AUTO_GENERATED_BARCODE, BARCODE);
				return;
			}
			scg.setBarcode(scgBarcodeGenerator.generateBarcode(barcodeFormat, scg));
		}

	}

	private void updateName(String name, SpecimenCollectionGroup scg, SpecimenCollectionGroup oldScg,
			ObjectCreationException errorHandler) {
		//TODO: get SCG Label Format
		String scgLabelFormat = null;
		if (isBlank(scgLabelFormat)) {
			if (isBlank(name)) {
				errorHandler.addError(ScgErrorCode.MISSING_ATTR_VALUE, NAME);
				return;
			}
			scg.setName(name);

		}
		else if (oldScg.getName().equalsIgnoreCase(scg.getName())) {
			errorHandler.addError(ScgErrorCode.AUTO_GENERATED_LABEL, NAME);
			return;
		}

	}

	private void updateBarcode(String barcode, SpecimenCollectionGroup scg, SpecimenCollectionGroup oldScg,
			ObjectCreationException errorHandler) {
		//TODO:get Barcode Format
		String barcodeFormat = null;
		if (isBlank(barcodeFormat)) {
			if (isBlank(barcode)) {
				errorHandler.addError(SpecimenErrorCode.MISSING_ATTR_VALUE, BARCODE);
				return;
			}
			scg.setBarcode(barcode);
		}
		else if (oldScg.getBarcode().equals(scg.getBarcode())) {
			errorHandler.addError(SpecimenErrorCode.AUTO_GENERATED_BARCODE, BARCODE);
			return;
		}
	}

	@Override
	@PlusTransactional
	public ScgReportUpdatedEvent updateScgReport(UpdateScgReportEvent event) {
		try {
			SpecimenCollectionGroup oldScg = daoFactory.getScgDao().getscg(event.getId());
			if (oldScg == null) {
				return ScgReportUpdatedEvent.notFound(event.getId());
			}
			SpecimenCollectionGroup scg = scgFactory.updateReports(oldScg, event.getDetail());
			oldScg.updateReports(scg);
			daoFactory.getScgDao().saveOrUpdate(oldScg);
			return ScgReportUpdatedEvent.ok(ScgReportDetail.fromDomain(oldScg));
		}
		catch (ObjectCreationException oce) {
			return ScgReportUpdatedEvent.invalidRequest(ScgErrorCode.ERRORS.message(), oce.getErroneousFields());
		}
		catch (Exception ex) {
			return ScgReportUpdatedEvent.serverError(ex);
		}
	}

	@Override
	@PlusTransactional
	public ScgReportUpdatedEvent getScgReport(GetScgReportEvent event) {
		SpecimenCollectionGroup scg = daoFactory.getScgDao().getscg(event.getId());
		if (scg == null) {
			return ScgReportUpdatedEvent.notFound(event.getId());
		}
		return ScgReportUpdatedEvent.ok(ScgReportDetail.fromDomain(scg));
	}
}
