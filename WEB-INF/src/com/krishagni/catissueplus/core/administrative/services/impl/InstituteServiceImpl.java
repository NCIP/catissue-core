package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CreateInstituteEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableInstituteEvent;
import com.krishagni.catissueplus.core.administrative.events.GetAllInstitutesEvent;
import com.krishagni.catissueplus.core.administrative.events.GetInstituteEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetails;
import com.krishagni.catissueplus.core.administrative.events.InstituteDisabledEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteGotEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllInstitutesEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateInstituteEvent;
import com.krishagni.catissueplus.core.administrative.services.InstituteService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class InstituteServiceImpl implements InstituteService {

	private static final String INSTITUTE_NAME = "institute name";

	private DaoFactory daoFactory;

	private InstituteFactory instituteFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setInstituteFactory(InstituteFactory instituteFactory) {
		this.instituteFactory = instituteFactory;
	}

	@Override
	@PlusTransactional
	public InstituteCreatedEvent createInstitute(CreateInstituteEvent event) {
		try {
			Institute institute = instituteFactory.createInstitute(event
					.getInstituteDetails());
			ObjectCreationException exceptionHandler = new ObjectCreationException();
			ensureUniqueInstituteName(institute.getName(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();

			daoFactory.getInstituteDao().saveOrUpdate(institute);
			return InstituteCreatedEvent.ok(InstituteDetails
					.fromDomain(institute));
		} catch (ObjectCreationException ce) {
			return InstituteCreatedEvent.invalidRequest(
					UserErrorCode.ERRORS.message(), ce.getErroneousFields());
		} catch (Exception e) {
			return InstituteCreatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public InstituteUpdatedEvent updateInstitute(UpdateInstituteEvent event) {
		try {
			Institute oldInstitute;
			if (event.getName() != null) {
				oldInstitute = daoFactory.getInstituteDao().getInstituteByName(
						event.getName());
				if (oldInstitute == null) {
					return InstituteUpdatedEvent.notFound(event.getName());
				}
			} else {
				oldInstitute = daoFactory.getInstituteDao().getInstitute(
						event.getId());
				if (oldInstitute == null) {
					return InstituteUpdatedEvent.notFound(event.getId());
				}
			}
			Institute institute = instituteFactory.createInstitute(event
					.getInstituteDetails());
			oldInstitute.update(institute);
			daoFactory.getInstituteDao().saveOrUpdate(oldInstitute);
			return InstituteUpdatedEvent.ok(InstituteDetails
					.fromDomain(oldInstitute));
		} catch (ObjectCreationException ce) {
			return InstituteUpdatedEvent.invalidRequest(
					UserErrorCode.ERRORS.message(), ce.getErroneousFields());
		} catch (Exception e) {
			return InstituteUpdatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public InstituteDisabledEvent deleteInstitute(DisableInstituteEvent event) {
		try {
			Institute institute = null;
			if (event.getName() != null) {
				institute = daoFactory.getInstituteDao().getInstituteByName(event.getName());
				if (institute == null) {
					return InstituteDisabledEvent.notFound(event.getName());
				}
			} else {
				institute = daoFactory.getInstituteDao().getInstitute(event.getId());
				if (institute == null) {
					return InstituteDisabledEvent.notFound(event.getId());
				}
			}
			institute.delete();
			daoFactory.getInstituteDao().saveOrUpdate(institute);
			return InstituteDisabledEvent.ok();
		} catch (Exception e) {
			return InstituteDisabledEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public InstituteGotEvent getInstitute(GetInstituteEvent event) {
		try {
			Institute institute = null;
			if (event.getName() != null) {
				institute = daoFactory.getInstituteDao().getInstituteByName(event.getName());
				if (institute == null) {
					return InstituteGotEvent.notFound(event.getName());
				}
			} else {
				institute = daoFactory.getInstituteDao().getInstitute(event.getInstId());
				if (institute == null) {
					return InstituteGotEvent.notFound(event.getInstId());
				}
			}
			return InstituteGotEvent.ok(InstituteDetails.fromDomain(institute));
		} catch (Exception e) {
			return InstituteGotEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public GetAllInstitutesEvent getInstitutes(ReqAllInstitutesEvent event) {
		List<Institute> instList = daoFactory.getInstituteDao().getAllInstitutes(event.getMaxResults());
		List<InstituteDetails> details = new ArrayList<InstituteDetails>();
		for (Institute institute : instList) {
			details.add(InstituteDetails.fromDomain(institute));
		}
		return GetAllInstitutesEvent.ok(details);
	}

	private void ensureUniqueInstituteName(String name,
			ObjectCreationException exceptionHandler) {
		Institute institute = daoFactory.getInstituteDao().getInstituteByName(
				name);

		if (institute != null) {
			exceptionHandler.addError(UserErrorCode.DUPLICATE_INSTITUTE_NAME,
					INSTITUTE_NAME);
		}
	}
}
