
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserFactory;
import com.krishagni.catissueplus.core.administrative.events.UserDetail;
import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

public class UserFactoryImpl implements UserFactory {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public User createUser(UserDetail detail) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		User user = new User();
		setFirstName(detail, user, ose);
		setLastName(detail, user, ose);
		setLoginName(detail, user, ose);
		setActivityStatus(detail, user, ose);
		setEmailAddress(detail, user, ose);
		setDepartment(detail, user, ose);
		setAuthDomain(detail, user, ose);

		ose.checkAndThrow();
		return user;
	}
	
	private void setFirstName(UserDetail detail, User user, OpenSpecimenException ose) {
		String firstName = detail.getFirstName();
		if (StringUtils.isBlank(firstName)) {
			ose.addError(UserErrorCode.FIRST_NAME_REQUIRED);
			return;
		}
		
		user.setFirstName(firstName);
	}
	
	private void setLastName(UserDetail detail, User user, OpenSpecimenException ose) {
		String lastName = detail.getLastName();
		if (StringUtils.isBlank(lastName)) {
			ose.addError(UserErrorCode.LAST_NAME_REQUIRED);
			return;
		}
		
		user.setLastName(lastName);
	}

	private void setLoginName(UserDetail detail, User user, OpenSpecimenException ose) {
		String loginName = detail.getLoginName();
		if (StringUtils.isBlank(loginName)) {
			ose.addError(UserErrorCode.LOGIN_NAME_REQUIRED);
			return;
		}
		
		user.setLoginName(loginName);
	}

	private void setDepartment(UserDetail detail, User user, OpenSpecimenException ose) {
		String departmentName = detail.getDeptName();
		if (StringUtils.isBlank(departmentName)) {
			ose.addError(UserErrorCode.DEPT_REQUIRED);
			return;
		}
		
		Department department = daoFactory.getDepartmentDao().getDepartmentByName(departmentName);
		if (department == null) {
			ose.addError(InstituteErrorCode.DEPT_NOT_FOUND);
			return;
		}
		
		user.setDepartment(department);
	}

	private void setActivityStatus(UserDetail detail, User user, OpenSpecimenException ose) {
		String activityStatus = detail.getActivityStatus();
		if (activityStatus == null) {
			activityStatus =  Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}
		
		if (activityStatus.equals(Status.ACTIVITY_STATUS_CLOSED.getStatus()) || 
				activityStatus.equals(Status.ACTIVITY_STATUS_DISABLED)) {
			ose.addError(UserErrorCode.STATUS_CHANGE_NOT_ALLOWED);
			return;
		}
		user.setActivityStatus(activityStatus);
	}

	private void setEmailAddress(UserDetail detail, User user, OpenSpecimenException ose) {
		String email = detail.getEmailAddress();
		if (!CommonValidator.isEmailValid(email)) {
			ose.addError(UserErrorCode.INVALID_EMAIL);
			return;
		}
		
		user.setEmailAddress(email);
	}

	private void setAuthDomain(UserDetail detail, User user, OpenSpecimenException ose) {
		String domainName = detail.getDomainName();
		if (StringUtils.isBlank(domainName)) {
			ose.addError(UserErrorCode.DOMAIN_NAME_REQUIRED);
			return;
		}

		AuthDomain authDomain = daoFactory.getAuthDao().getAuthDomainByName(domainName);
		if (authDomain == null) {
			ose.addError(UserErrorCode.DOMAIN_NOT_FOUND);
			return;
		}
		
		user.setAuthDomain(authDomain);
	}
}
