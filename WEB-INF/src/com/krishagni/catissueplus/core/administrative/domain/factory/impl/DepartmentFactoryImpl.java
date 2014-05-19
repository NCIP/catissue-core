
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.factory.DepartmentFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDetails;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class DepartmentFactoryImpl implements DepartmentFactory {

	private static final String DEPARTMENT_NAME = "department name";
	
	private static final String INSTITUTE = "institute";
	
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Department createDepartment(DepartmentDetails details) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		Department department = new Department();
		setDepartmentName(department, details.getName(), exceptionHandler);
		setInstitutes(department, details.getInstituteNames(), exceptionHandler);
		exceptionHandler.checkErrorAndThrow();
		return department;
	}

	private void setInstitutes(Department department, List<String> instituteNames,
			ObjectCreationException exceptionHandler) {
		Set<Institute> institutes = new HashSet<Institute>(); 
		for(String instituteName : instituteNames) {
			Institute institute = daoFactory.getInstituteDao().getInstituteByName(instituteName);
			
			if (institute == null) {
				exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, INSTITUTE);
				return;
			}
			institutes.add(institute);
		}
		department.setInstitutes(institutes);
	}

	private void setDepartmentName(Department department, String name, ObjectCreationException exceptionHandler) {
		if (isBlank(name)) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, DEPARTMENT_NAME);
			return;
		}
		department.setName(name);
	}

}
