
package com.krishagni.catissueplus.core.biospecimen.services.impl;

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
		try{
		SpecimenCollectionGroup scg = scgFactory.createScg(scgEvent.getScgDetail(),exceptionHandler);
		ensureUniqueBarcode(scg);
		ensureUniqueName(scg);
		exceptionHandler.checkErrorAndThrow();
		daoFactory.getScgDao().saveOrUpdate(scg);
		return ScgCreatedEvent.ok(ScgDetail.fromDomain(scg));
		}
		catch(ObjectCreationException oce){
			return ScgCreatedEvent.invalidRequest(ScgErrorCode.ERRORS.message(),oce.getErroneousFields());
		}
		catch(Exception ex)
		{
			return ScgCreatedEvent.serverError(ex);
		}
	}


	private void ensureUniqueName(SpecimenCollectionGroup scg) {
		if(!daoFactory.getScgDao().isNameUnique(scg.getName()))
		{
			exceptionHandler.addError(ScgErrorCode.DUPLICATE_NAME, NAME);
		}
	}

	private void ensureUniqueBarcode(SpecimenCollectionGroup scg) {
		if(!daoFactory.getScgDao().isBarcodeUnique(scg.getBarcode()))
		{
			exceptionHandler.addError(ScgErrorCode.DUPLICATE_BARCODE, BARCODE);
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
			return ScgDeletedEvent.invalidRequest(ce.getMessage() + " : " + ce.getErroneousFields());
		}
		catch (Exception e) {
			return ScgDeletedEvent.serverError(e);
		}
	}

}
