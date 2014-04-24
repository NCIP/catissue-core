
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.PatchSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class SpecimenServiceImpl implements SpecimenService {

	private static final String LABEL = "label";

	private static final String BARCODE = "barcode";

	private DaoFactory daoFactory;

	private SpecimenFactory specimenFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSpecimenFactory(SpecimenFactory specimenFactory) {
		this.specimenFactory = specimenFactory;
	}

	@Override
	@PlusTransactional
	public AllSpecimensSummaryEvent getSpecimensList(ReqSpecimenSummaryEvent event) {
		try {
			return AllSpecimensSummaryEvent.ok(daoFactory.getSpecimenDao().getSpecimensList(event.getId()));
		}
		catch (CatissueException e) {
			return AllSpecimensSummaryEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public SpecimenCreatedEvent createSpecimen(CreateSpecimenEvent event) {
		try {
			Specimen specimen = specimenFactory.createSpecimen(event.getSpecimenDetail());
			ObjectCreationException errorHandler = new ObjectCreationException();
			ensureUniqueBarocde(specimen.getBarcode(), errorHandler);
			ensureUniqueLabel(specimen.getLabel(), errorHandler);
			errorHandler.checkErrorAndThrow();
			daoFactory.getSpecimenDao().saveOrUpdate(specimen);
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
	public SpecimenUpdatedEvent patchSpecimen(PatchSpecimenEvent event) {
		try {
			Long specimenId = event.getId();
			Specimen oldSpecimen = daoFactory.getSpecimenDao().getSpecimen(specimenId);
			if (oldSpecimen == null) {
				return SpecimenUpdatedEvent.notFound(specimenId);
			}
			Specimen specimen = specimenFactory.patch(oldSpecimen,event.getDetail());
			ObjectCreationException errorHandler = new ObjectCreationException();
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

}
