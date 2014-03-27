
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenCollectionGroupFactory;
import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenGroupsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;
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
import com.krishagni.catissueplus.core.common.errors.ObjectUpdationException;

@Service(value = "specimenCollGroupService")
public class SpecimenCollGroupServiceImpl implements SpecimenCollGroupService {

	private DaoFactory daoFactory;

	private SpecimenCollectionGroupFactory scgFactory;

	private static final String NAME = "name";

	private static final String BARCODE = "barcode";
	
	private static final String CPR_CPE= "registraion and event point refering to different protocols.";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setScgFactory(SpecimenCollectionGroupFactory scgFactory) {
		this.scgFactory = scgFactory;
	}

	ObjectCreationException exceptionHandler;

	@Override
	@PlusTransactional
	public AllSpecimensSummaryEvent getSpecimensList(ReqSpecimenSummaryEvent req) {

		try {
			return AllSpecimensSummaryEvent.ok(daoFactory.getScgDao().getSpecimensList(req.getScgId()));
		}
		catch (CatissueException e) {
			return AllSpecimensSummaryEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ScgCreatedEvent createScg(CreateScgEvent scgEvent) {
		exceptionHandler = new ObjectCreationException();
		try {
			SpecimenCollectionGroup scg = scgFactory.createScg(scgEvent.getScgDetail(), exceptionHandler);
			
			ensureUniqueBarcode(scg.getBarcode());
			ensureUniqueName(scg.getName());
			validateCprAndCpe(scg);//this should be part of factory
			exceptionHandler.checkErrorAndThrow();
			daoFactory.getScgDao().saveOrUpdate(scg);
			return ScgCreatedEvent.ok(ScgDetail.fromDomain(scg));
		}
		catch (ObjectCreationException oce) {
			return ScgCreatedEvent.invalidRequest(ScgErrorCode.ERRORS.message(), oce.getErroneousFields());
		}
//		catch (Uniqu oce) {
//			return ScgCreatedEvent.invalidRequest(ScgErrorCode.ERRORS.message(), oce.getErroneousFields());
//		}
		catch (Exception ex) {
			return ScgCreatedEvent.serverError(ex);
		}
	}

	private void validateCprAndCpe(SpecimenCollectionGroup scg) {
		if(scg.getCollectionProtocolRegistration() != null && scg.getCollectionProtocolEvent() != null)
		{
			if(!scg.getCollectionProtocolRegistration().getCollectionProtocol().getId().equals(scg.getCollectionProtocolEvent().getCollectionProtocol().getId()))
			{
				exceptionHandler.addError(ScgErrorCode.INVALID_CPR_CPE, CPR_CPE);
			}
		}
	}

	@Override
	@PlusTransactional
	public ScgUpdatedEvent updateScg(UpdateScgEvent scgEvent) {
		exceptionHandler = new ObjectUpdationException();
		try {
			SpecimenCollectionGroup oldScg = daoFactory.getScgDao().getscg(scgEvent.getId());
			if (oldScg == null) {
				ScgUpdatedEvent.notFound(scgEvent.getId());
			}
			SpecimenCollectionGroup scg = scgFactory.createScg(scgEvent.getScgDetail(), exceptionHandler);
			validateBarcode(oldScg.getBarcode(), scg.getBarcode());
			validateName(oldScg.getName(), scg.getName());
			exceptionHandler.checkErrorAndThrow();
			oldScg.update(scg);
			daoFactory.getScgDao().saveOrUpdate(oldScg);
			return ScgUpdatedEvent.ok(ScgDetail.fromDomain(scg));
		}
		catch (ObjectUpdationException oce) {
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

	private void ensureUniqueName(String name) {
		if (!daoFactory.getScgDao().isNameUnique(name)) {
			exceptionHandler.addError(ScgErrorCode.DUPLICATE_NAME, NAME);
		}
	}

	private void ensureUniqueBarcode(String barcode) {
		if (!daoFactory.getScgDao().isBarcodeUnique(barcode)) {
			exceptionHandler.addError(ScgErrorCode.DUPLICATE_BARCODE, BARCODE);
		}
	}

	private void validateName(String oldName, String newName) {
		if (!isBlank(newName) && !newName.equals(oldName)) {
			ensureUniqueName(newName);
		}
	}

	private void validateBarcode(String oldBarcode, String newBarcode) {
		if (!isBlank(newBarcode) && !newBarcode.equals(oldBarcode)) {
			ensureUniqueBarcode(newBarcode);
		}
	}
}
