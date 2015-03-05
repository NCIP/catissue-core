package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteFactory;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDetail;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

public class InstituteFactoryImpl implements InstituteFactory {
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public Institute createInstitute(InstituteDetail details) {
		Institute institute = new Institute();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		setInstituteName(details, institute, ose);
		setDepartments(details, institute, ose);
		setActivityStatus(details, institute, ose);
		
		ose.checkAndThrow();
		return institute;
	}

	private void setInstituteName(InstituteDetail detail, Institute institute, OpenSpecimenException ose) {
		String name = detail.getName();
		if (StringUtils.isBlank(name)) {
			ose.addError(InstituteErrorCode.NAME_REQUIRED);
			return;
		}
		
		institute.setName(name);
	}

	private void setDepartments(InstituteDetail detail, Institute institute, OpenSpecimenException ose) {		
		Set<Department> departments = new HashSet<Department>();
		
		for (DepartmentDetail deptDetail : detail.getDepartments()) {
			Department department = null;
			if (deptDetail.getId() != null && detail.getId() != null) {	
				department = daoFactory.getInstituteDao().getDepartment(deptDetail.getId(), detail.getId());
				if(department == null) {
					ose.addError(InstituteErrorCode.DEPT_NOT_FOUND);
					return;
				}
				
				department.setName(deptDetail.getName());
			} else {
				department = new Department();
				department.setName(deptDetail.getName());
				department.setInstitute(institute);
			}
			
			departments.add(department);
		}
		
		institute.setDepartments(departments);
	}
	
	private void setActivityStatus(InstituteDetail detail, Institute institute, OpenSpecimenException ose) {
		String activityStatus = detail.getActivityStatus();
		if (StringUtils.isBlank(activityStatus)) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}
		
		if (!Status.isValidActivityStatus(activityStatus)) {
			ose.addError(ActivityStatusErrorCode.INVALID);
			return;
		}

		institute.setActivityStatus(activityStatus);
	}
}