
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.Calendar;

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

	@Override
	public User createUser(UserDetail detail) {
		User user = new User();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
				
		setAdmin(detail, user, ose);
		setFirstName(detail, user, ose);
		setLastName(detail, user, ose);
		setLoginName(detail, user, ose);
		setActivityStatus(detail, user, ose);
		setEmailAddress(detail, user, ose);
		setPhoneNumber(detail, user, ose);
		setDepartment(detail, user, ose);
		setAddress(detail, user, ose);
		setAuthDomain(detail, user, ose);
		user.setCreationDate(Calendar.getInstance().getTime());
		ose.checkAndThrow();
		return user;
	}
	
	@Override
	public User createUser(User existing, UserDetail detail) {
		User user = new User();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		user.setId(existing.getId());
		setAdmin(detail, existing, user, ose);
		setFirstName(detail, existing, user, ose);
		setLastName(detail, existing, user, ose);
		setLoginName(detail, existing, user, ose);
		setActivityStatus(detail, existing, user, ose);
		setEmailAddress(detail, existing, user, ose);
		setPhoneNumber(detail, existing, user, ose);
		setDepartment(detail, existing, user, ose);
		setAddress(detail, existing, user, ose);
		setAuthDomain(detail, existing, user, ose);
		ose.checkAndThrow();
		return user;		
	}
	
	private void setAdmin(UserDetail detail, User user, OpenSpecimenException ose) {
		user.setAdmin(detail.getAdmin());
	}
	
	private void setAdmin(UserDetail detail, User existing, User user, OpenSpecimenException ose) {
		if (detail.isAttrModified("admin")) {
			setAdmin(detail, user, ose);
		} else {
			user.setAdmin(existing.getAdmin());
		}
	}
	
	private void setFirstName(UserDetail detail, User user, OpenSpecimenException ose) {
		String firstName = detail.getFirstName();
		if (StringUtils.isBlank(firstName)) {
			ose.addError(UserErrorCode.FIRST_NAME_REQUIRED);
			return;
		}
		
		user.setFirstName(firstName);
	}
	
	private void setFirstName(UserDetail detail, User existing, User user, OpenSpecimenException ose) {
		if (detail.isAttrModified("firstName")) {
			setFirstName(detail, user, ose);
		} else {
			user.setFirstName(existing.getFirstName());
		}
	}
	
	private void setLastName(UserDetail detail, User user, OpenSpecimenException ose) {
		String lastName = detail.getLastName();
		if (StringUtils.isBlank(lastName)) {
			ose.addError(UserErrorCode.LAST_NAME_REQUIRED);
			return;
		}
		
		user.setLastName(lastName);
	}

	private void setLastName(UserDetail detail, User existing, User user, OpenSpecimenException ose) {
		if (detail.isAttrModified("lastName")) {
			setLastName(detail, user, ose);
		} else {
			user.setLastName(existing.getLastName());
		}
	}
	
	private void setLoginName(UserDetail detail, User user, OpenSpecimenException ose) {
		String loginName = detail.getLoginName();
		if (StringUtils.isBlank(loginName)) {
			ose.addError(UserErrorCode.LOGIN_NAME_REQUIRED);
			return;
		}
		
		user.setLoginName(loginName);
	}
	
	private void setLoginName(UserDetail detail, User existing, User user, OpenSpecimenException ose) {
		if (detail.isAttrModified("loginName")) {
			setLoginName(detail, user, ose);
		} else {
			user.setLoginName(existing.getLoginName());
		}
	}
	
	private void setDepartment(UserDetail detail, User user, OpenSpecimenException ose) {
		String departmentName = detail.getDeptName();
		if (StringUtils.isBlank(departmentName)) {
			ose.addError(UserErrorCode.DEPT_REQUIRED);
			return;
		}
		
		Department department = daoFactory.getInstituteDao()
				.getDeptByNameAndInstitute(detail.getDeptName(), detail.getInstituteName());
		if (department == null) {
			ose.addError(InstituteErrorCode.DEPT_NOT_FOUND);
			return;
		}
		
		user.setDepartment(department);
	}
	
	private void setDepartment(UserDetail detail, User existing, User user, OpenSpecimenException ose) {
		if (detail.isAttrModified("deptName")) {
			setDepartment(detail, user, ose);
		} else {
			user.setDepartment(existing.getDepartment());
		}
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

	private void setActivityStatus(UserDetail detail, User existing, User user, OpenSpecimenException ose) {
		if (detail.isAttrModified("activityStatus")) {
			setActivityStatus(detail, user, ose);
		} else {
			user.setActivityStatus(existing.getActivityStatus());
		}
	}
	
	private void setEmailAddress(UserDetail detail, User user, OpenSpecimenException ose) {
		String email = detail.getEmailAddress();
		if (!CommonValidator.isEmailValid(email)) {
			ose.addError(UserErrorCode.INVALID_EMAIL);
			return;
		}
		
		user.setEmailAddress(email);
	}

	private void setEmailAddress(UserDetail detail, User existing, User user, OpenSpecimenException ose) {
		if (detail.isAttrModified("emailAddress")) {
			setEmailAddress(detail, user, ose);
		} else {
			user.setEmailAddress(existing.getEmailAddress());
		}
	}

	private void setPhoneNumber(UserDetail detail, User user, OpenSpecimenException ose) {
		user.setPhoneNumber(detail.getPhoneNumber());
	}

	private void setPhoneNumber(UserDetail detail, User existing, User user, OpenSpecimenException ose) {
		if (detail.isAttrModified("phoneNumber")) {
			setPhoneNumber(detail, user, ose);
		} else {
			user.setPhoneNumber(existing.getPhoneNumber());
		}
	}

	private void setAddress(UserDetail detail, User user, OpenSpecimenException ose) {
		user.setAddress(detail.getAddress());
	}

	private void setAddress(UserDetail detail, User existing, User user, OpenSpecimenException ose) {
		if (detail.isAttrModified("address")) {
			setAddress(detail, user, ose);
		} else {
			user.setAddress(existing.getAddress());
		}
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

	private void setAuthDomain(UserDetail detail, User existing, User user, OpenSpecimenException ose) {
		if (detail.isAttrModified("domainName")) {
			setAuthDomain(detail, user, ose);
		} else {
			user.setAuthDomain(existing.getAuthDomain());
		}
	}	
}
