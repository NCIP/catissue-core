
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.factory.DepartmentFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDetails;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class DepartmentFactoryImpl implements DepartmentFactory {

	private static final String DEPARTMENT_NAME = "department name";

	private static final String INSTITUTE = "institute";

	private static final String ACTIVITY_STATUS = "activity status";

	private DaoFactory daoFactory; 

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Department createDepartment(DepartmentDetails details) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		Department department = new Department();
		setDepartmentName(department, details.getName(), exceptionHandler);
		setInstitute(department, details.getInstituteName(), exceptionHandler);
		setActivityStatus(department, details.getActivityStatus(), exceptionHandler);
		exceptionHandler.checkErrorAndThrow();
		return department;
	}

	private void setActivityStatus(Department department, String activityStatus, ObjectCreationException exceptionHandler) {
		if (!CommonValidator.isValidPv(activityStatus, ACTIVITY_STATUS)) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, ACTIVITY_STATUS);
			return;
		}
		department.setActivityStatus(activityStatus);
	}

	private void setInstitute(Department department, String instituteName, ObjectCreationException exceptionHandler) {

		Institute institute = daoFactory.getInstituteDao().getInstituteByName(instituteName);
		if (institute == null) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, INSTITUTE);
			return;
		}
		department.setInstitute(institute);
	}

	private void setDepartmentName(Department department, String name, ObjectCreationException exceptionHandler) {
		if (isBlank(name)) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, DEPARTMENT_NAME);
			return;
		}
		department.setName(name);
	}
}
