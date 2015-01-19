
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitFactory;
import com.krishagni.catissueplus.core.biospecimen.events.AddVisitEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AllCollectionGroupsDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenGroupsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.GetScgReportEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgReportEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitAddedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.de.services.SavedQueryErrorCode;

public class VisitServiceImpl implements VisitService {
	private DaoFactory daoFactory;

	private VisitFactory visitFactory;

	private static final String NAME = "name";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setVisitFactory(VisitFactory visitFactory) {
		this.visitFactory = visitFactory;
	}

	@Override
	@PlusTransactional
	public AllCollectionGroupsDetailEvent getAllCollectionGroups(ReqAllScgEvent req) {
		if (req.getStartAt() < 0 || req.getMaxRecords() <= 0) {
			String msg = SavedQueryErrorCode.INVALID_PAGINATION_FILTER.message();
			return AllCollectionGroupsDetailEvent.badRequest(msg, null);
		}

		List<Visit> scgs = daoFactory.getVisitsDao().getAllScgs(
						req.getStartAt(), req.getMaxRecords(), 
						req.getSearchString());
		List<VisitDetail> result = new ArrayList<VisitDetail>();
		for (Visit scg : scgs) {
			result.add(VisitDetail.from(scg));
		}
		
		Long count = null;
		if (req.isCountReq()) {
			count = daoFactory.getVisitsDao().getScgsCount(req.getSearchString());
		}

			return AllCollectionGroupsDetailEvent.ok(result,count);
	}
	
	@Override
	@PlusTransactional
	public VisitAddedEvent addVisit(AddVisitEvent req) {
		try {
			Visit visit = visitFactory.createVisit(req.getVisit());
			setName(visit);

			daoFactory.getVisitsDao().saveOrUpdate(visit);
			return VisitAddedEvent.ok(VisitDetail.from(visit));
		} catch (ObjectCreationException oce) {
			return VisitAddedEvent.invalidRequest(ScgErrorCode.ERRORS.message(), oce.getErroneousFields());
		} catch (Exception ex) {
			return VisitAddedEvent.serverError(ex);
		}
	}

	@Override
	@PlusTransactional
	public ScgUpdatedEvent updateScg(UpdateScgEvent scgEvent) {
		try {
			Visit oldScg = daoFactory.getVisitsDao().getscg(scgEvent.getId());
			if (oldScg == null) {
				ScgUpdatedEvent.notFound(scgEvent.getId());
			}
			Visit scg = visitFactory.createVisit(scgEvent.getScgDetail());
			ObjectCreationException errorHandler = new ObjectCreationException();
			updateName(scgEvent.getScgDetail().getName(), scg, oldScg, errorHandler);
			validateName(oldScg.getName(), scg.getName(), errorHandler);
			errorHandler.checkErrorAndThrow();
			oldScg.update(scg);
			daoFactory.getVisitsDao().saveOrUpdate(oldScg);
			return ScgUpdatedEvent.ok(VisitDetail.from(oldScg));
		}
		catch (ObjectCreationException oce) {
			return ScgUpdatedEvent.invalidRequest(ScgErrorCode.ERRORS.message(), oce.getErroneousFields());
		}
		catch (Exception ex) {
			return ScgUpdatedEvent.serverError(ex);
		}
	}

//	@Override
//	@PlusTransactional
//	public ScgUpdatedEvent patchScg(PatchScgEvent scgEvent) {
//		try {
//			SpecimenCollectionGroup oldScg = daoFactory.getVisitsDao().getscg(scgEvent.getId());
//			if (oldScg == null) {
//				ScgUpdatedEvent.notFound(scgEvent.getId());
//			}
//			SpecimenCollectionGroup scg = scgFactory.patch(oldScg, scgEvent.getScgProps());
//			ObjectCreationException errorHandler = new ObjectCreationException();
//			if (scgEvent.getScgProps().containsKey("name")) {
//				String name = (String) scgEvent.getScgProps().get("name");
//				updateName(name, scg, oldScg, errorHandler);
//			}
//			if (scgEvent.getScgProps().containsKey("barcode")) {
//				String barcode = (String) scgEvent.getScgProps().get("barcode");
//				updateBarcode(barcode, scg, oldScg, errorHandler);
//			}
//			validateBarcode(oldScg.getBarcode(), scg.getBarcode(), errorHandler);
//			validateName(oldScg.getName(), scg.getName(), errorHandler);
//			errorHandler.checkErrorAndThrow();
//			oldScg.update(scg);
//			daoFactory.getVisitsDao().saveOrUpdate(oldScg);
//			return ScgUpdatedEvent.ok(VisitDetail.fromDomain(scg));
//		}
//		catch (ObjectCreationException oce) {
//			return ScgUpdatedEvent.invalidRequest(ScgErrorCode.ERRORS.message(), oce.getErroneousFields());
//		}
//		catch (Exception ex) {
//			return ScgUpdatedEvent.serverError(ex);
//		}
//	}

	@Override
	public ScgDeletedEvent delete(DeleteSpecimenGroupsEvent event) {
		try {
			Visit scg = daoFactory.getVisitsDao().getscg(event.getId());
			if (scg == null) {
				return ScgDeletedEvent.notFound(event.getId());
			}
			scg.delete(event.isIncludeChildren());

			daoFactory.getVisitsDao().saveOrUpdate(scg);
			return ScgDeletedEvent.ok();
		}
		catch (CatissueException ce) {
			return ScgDeletedEvent.invalidRequest(ce.getMessage());
		}
		catch (Exception e) {
			return ScgDeletedEvent.serverError(e);
		}
	}

	private void setName(Visit visit) {
		visit.setName(UUID.randomUUID().toString()); // TODO: replace with actual logic
	}
	
	
	private void ensureUniqueName(String name, ObjectCreationException errorHandler) {
		if (daoFactory.getVisitsDao().getScgByName(name) != null) {
			errorHandler.addError(ScgErrorCode.DUPLICATE_NAME, NAME);
		}
	}

	private void validateName(String oldName, String newName, ObjectCreationException errorHandler) {
		if (!isBlank(newName) && !newName.equals(oldName)) {
			ensureUniqueName(newName, errorHandler);
		}
	}

	private void updateName(String name, Visit scg, Visit oldScg,
			ObjectCreationException errorHandler) {
		// TODO: get SCG Label Format
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
	
	@Override
	@PlusTransactional
	public ScgReportUpdatedEvent updateScgReport(UpdateScgReportEvent event) {
		try {
			Visit oldScg = daoFactory.getVisitsDao().getscg(event.getId());
			if (oldScg == null) {
				return ScgReportUpdatedEvent.notFound(event.getId());
			}
			Visit scg = visitFactory.updateReports(oldScg, event.getDetail());
			oldScg.updateReports(scg);
			daoFactory.getVisitsDao().saveOrUpdate(oldScg);
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
		Visit scg = daoFactory.getVisitsDao().getscg(event.getId());
		if (scg == null) {
			return ScgReportUpdatedEvent.notFound(event.getId());
		}
		return ScgReportUpdatedEvent.ok(ScgReportDetail.fromDomain(scg));
	}
}
