package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDetails;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetails;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class InstituteFactoryImpl implements InstituteFactory {

	private static final String INSTITUTE_NAME = "institute name";
	
	private static final String DEPARTMENT_IDENTIFIER = "Department Identifier";

	private static final String ACTIVITY_STATUS = "activity_status";
	
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public Institute createInstitute(InstituteDetails details) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		Institute institute = new Institute();
		setInstituteName(institute, details.getName(), exceptionHandler);
		setActivityStatus(institute, details.getActivityStatus(), exceptionHandler);
		setDepartmentCollection(institute, details.getDepartments(), exceptionHandler);
		exceptionHandler.checkErrorAndThrow();
		return institute;
	}

	private void setDepartmentCollection(Institute institute, List<DepartmentDetails> departments,
			ObjectCreationException exceptionHandler) {
		
		Set<Department> departmentSet = new HashSet<Department>();
		
		for(DepartmentDetails deptDetail : departments) {			
			if(deptDetail.getId() == null) {
				Department department = new Department();
				department.setName(deptDetail.getName());
				department.setInstitute(institute);
				departmentSet.add(department);
			} else {
				Department department = daoFactory.getDepartmentDao().getDepartment(deptDetail.getId());
				if(department == null) {
					exceptionHandler.addError(UserErrorCode.NOT_FOUND, DEPARTMENT_IDENTIFIER);
				}
				department.setName(deptDetail.getName());
				departmentSet.add(department);
			}				
		}
		institute.setDepartmentCollection(departmentSet);
	}

	private void setInstituteName(Institute institute, String name, ObjectCreationException exceptionHandler) {
		if (isBlank(name)) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, INSTITUTE_NAME);
			return;
		}
		institute.setName(name);
	}

	private void setActivityStatus(Institute institute, String activityStatus, ObjectCreationException exceptionHandler) {
		if (!CommonValidator.isValidPv(activityStatus, ACTIVITY_STATUS)) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, ACTIVITY_STATUS);
			return;
		}

		institute.setActivityStatus(activityStatus);
	}

}