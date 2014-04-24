
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenCollectionGroupFactory;
import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenGroupsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.PatchScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent.ObjectType;
import com.krishagni.catissueplus.core.biospecimen.events.ScgCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ScgUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenCollGroupService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

@Service(value = "specimenCollGroupService")
public class SpecimenCollGroupServiceImpl implements SpecimenCollGroupService {

	private DaoFactory daoFactory;

	private SpecimenCollectionGroupFactory scgFactory;

	private static final String NAME = "name";

	private static final String BARCODE = "barcode";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setScgFactory(SpecimenCollectionGroupFactory scgFactory) {
		this.scgFactory = scgFactory;
	}

	@Override
	@PlusTransactional
	public AllSpecimensSummaryEvent getSpecimensList(ReqSpecimenSummaryEvent req) {
System.out.println();
		try {
			if(ObjectType.CPE.getName().equals(req.getObjectType()))
			{
				return AllSpecimensSummaryEvent.ok(daoFactory.getScgDao().getSpecimensListFromCpe(req.getId()));
				
			}
			else{
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
			SpecimenCollectionGroup scg = scgFactory.patch(oldScg,scgEvent.getScgProps());
			ObjectCreationException errorHandler = new ObjectCreationException();
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
}
