package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteFactory;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDetails;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class InstituteFactoryImpl implements InstituteFactory {
	private static final String ACTIVITY_STATUS = "activity_status";
	
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public Institute createInstitute(InstituteDetail details) {
		Institute institute = new Institute();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		setInstituteName(institute, details.getName(), ose);
		setActivityStatus(institute, details.getActivityStatus(), ose);
		setDepartmentCollection(institute, details.getDepartments(), ose);
		
		ose.checkAndThrow();
		return institute;
	}

	private void setDepartmentCollection(Institute institute, List<DepartmentDetails> departments,	OpenSpecimenException ose) {		
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
					ose.addError(InstituteErrorCode.DEPT_NOT_FOUND);
				}
				department.setName(deptDetail.getName());
				departmentSet.add(department);
			}				
		}
		
		institute.setDepartmentCollection(departmentSet);
	}

	private void setInstituteName(Institute institute, String name, OpenSpecimenException ose) {
		if (StringUtils.isBlank(name)) {
			ose.addError(InstituteErrorCode.NAME_REQUIRED);
			return;
		}
		
		institute.setName(name);
	}

	private void setActivityStatus(Institute institute, String activityStatus, OpenSpecimenException ose) {
		if (!CommonValidator.isValidPv(activityStatus, ACTIVITY_STATUS)) {
			ose.addError(ActivityStatusErrorCode.INVALID);
			return;
		}

		institute.setActivityStatus(activityStatus);
	}
}