
package com.krishagni.catissueplus.core.administrative.services.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.factory.DepartmentFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CreateDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDetails;
import com.krishagni.catissueplus.core.administrative.events.DepartmentUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.services.DepartmentService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class DepartmentServiceImpl implements DepartmentService {

	private static final String DEPARTMENT_NAME = "department name";

	@Autowired
	private DaoFactory daoFactory;

	@Autowired
	private DepartmentFactory departmentFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setDepartmentFactory(DepartmentFactory departmentFactory) {
		this.departmentFactory = departmentFactory;
	}

	@Override
	@PlusTransactional
	public DepartmentCreatedEvent createDepartment(CreateDepartmentEvent event) {
		try {
			Department department = departmentFactory.createDepartment(event.getDepartmentDetails());

			ObjectCreationException exceptionHandler = new ObjectCreationException();
			ensureUniqueDepartmentName(department.getName(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();

			daoFactory.getDepartmentDao().saveOrUpdate(department);
			return DepartmentCreatedEvent.ok(DepartmentDetails.fromDomain(department));
		}
		catch (ObjectCreationException ce) {
			return DepartmentCreatedEvent.invalidRequest(UserErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return DepartmentCreatedEvent.serverError(e);
		}
	}

	private void ensureUniqueDepartmentName(String name, ObjectCreationException exceptionHandler) {
		Department department = daoFactory.getDepartmentDao().getDepartmentByName(name);

		if (department != null) {
			exceptionHandler.addError(UserErrorCode.DUPLICATE_DEPARTMENT_NAME, DEPARTMENT_NAME);
		}
	}

	@Override
	@PlusTransactional
	public DepartmentUpdatedEvent updateDepartment(UpdateDepartmentEvent event) {
		try {
			Long departmentId = event.getDepartmentDetails().getId();
			Department oldDepartment = daoFactory.getDepartmentDao().getDepartment(departmentId);
			if (oldDepartment == null) {
				return DepartmentUpdatedEvent.notFound(departmentId);
			}
			Department department = departmentFactory.createDepartment(event.getDepartmentDetails());
			oldDepartment.update(department);
			daoFactory.getDepartmentDao().saveOrUpdate(oldDepartment);
			return DepartmentUpdatedEvent.ok(DepartmentDetails.fromDomain(oldDepartment));
		}
		catch (ObjectCreationException ce) {
			return DepartmentUpdatedEvent.invalidRequest(UserErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return DepartmentUpdatedEvent.serverError(e);
		}
	}

}
