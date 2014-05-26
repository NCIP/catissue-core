
package com.krishagni.catissueplus.core.administrative.services.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CreateInstituteEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetails;
import com.krishagni.catissueplus.core.administrative.events.InstituteUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateInstituteEvent;
import com.krishagni.catissueplus.core.administrative.services.InstituteService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class InstituteServiceImpl implements InstituteService {

	private static final String INSTITUTE_NAME = "institute name";

	@Autowired
	private DaoFactory daoFactory;

	@Autowired
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
			Institute institute = instituteFactory.createInstitute(event.getInstituteDetails());
			ObjectCreationException exceptionHandler = new ObjectCreationException();
			ensureUniqueInstituteName(institute.getName(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();

			daoFactory.getInstituteDao().saveOrUpdate(institute);
			return InstituteCreatedEvent.ok(InstituteDetails.fromDomain(institute));
		}
		catch (ObjectCreationException ce) {
			return InstituteCreatedEvent.invalidRequest(UserErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return InstituteCreatedEvent.serverError(e);
		}
	}

	private void ensureUniqueInstituteName(String name, ObjectCreationException exceptionHandler) {
		Institute institute = daoFactory.getInstituteDao().getInstituteByName(name);

		if (institute != null) {
			exceptionHandler.addError(UserErrorCode.DUPLICATE_INSTITUTE_NAME, INSTITUTE_NAME);
		}
	}

	@Override
	@PlusTransactional
	public InstituteUpdatedEvent updateInstitute(UpdateInstituteEvent event) {
		try {
			Long instituteId = event.getInstituteDetails().getId();
			Institute oldInstitute = daoFactory.getInstituteDao().getInstitute(instituteId);
			if (oldInstitute == null) {
				return InstituteUpdatedEvent.notFound(instituteId);
			}
			Institute institute = instituteFactory.createInstitute(event.getInstituteDetails());
			oldInstitute.update(institute);
			daoFactory.getInstituteDao().saveOrUpdate(oldInstitute);
			return InstituteUpdatedEvent.ok(InstituteDetails.fromDomain(oldInstitute));
		}
		catch (ObjectCreationException ce) {
			return InstituteUpdatedEvent.invalidRequest(UserErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return InstituteUpdatedEvent.serverError(e);
		}
	}

}
