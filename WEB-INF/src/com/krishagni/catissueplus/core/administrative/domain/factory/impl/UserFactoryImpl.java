
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
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
		user.setFirstName(ensureNotEmpty(detail.getFirstName(), UserErrorCode.FIRST_NAME_REQUIRED, ose));
		user.setLastName(ensureNotEmpty(detail.getLastName(), UserErrorCode.LAST_NAME_REQUIRED, ose));
		user.setLoginName(ensureNotEmpty(detail.getLoginName(), UserErrorCode.LOGIN_NAME_REQUIRED, ose));
		setActivityStatus(user, detail.getActivityStatus(), ose);
		setEmailAddress(user, detail.getEmailAddress(), ose);
		setDepartment(user, detail.getDeptName(), ose);
		setAuthDomain(user, detail.getDomainName(), ose);
		setUserSites(user, detail.getSitesName(), ose);

		ose.checkAndThrow();
		return user;
	}

	private void setUserSites(User user, List<String> userSiteNames, OpenSpecimenException ose) {
		if (userSiteNames.isEmpty()) {
			return;
		}
		
		Set<Site> sites = new HashSet<Site>();
		for (String siteName : userSiteNames) {
			Site site = daoFactory.getSiteDao().getSite(siteName);
			if (site == null) {
				ose.addError(SiteErrorCode.NOT_FOUND);
				return;
			}

			sites.add(site);
		}
		user.setSites(sites);
		//SetUpdater.<Site> newInstance().update(user.getSites(), sites);
	}

	private void setDepartment(User user, String departmentName, OpenSpecimenException ose) {
		if (ensureNotEmpty(departmentName, UserErrorCode.DEPT_REQUIRED, ose) == null) {
			return;
		}
		
		Department department = daoFactory.getDepartmentDao().getDepartmentByName(departmentName);
		if (department == null) {
			ose.addError(InstituteErrorCode.DEPT_NOT_FOUND);
			return;
		}
		
		user.setDepartment(department);
	}

	private void setActivityStatus(User user, String activityStatus, OpenSpecimenException ose) {
        activityStatus = (activityStatus == null) ? Status.ACTIVITY_STATUS_ACTIVE.getStatus() : activityStatus;
		user.setActivityStatus(activityStatus);
	}

	private void setEmailAddress(User user, String email, OpenSpecimenException ose) {
		if (ensureNotEmpty(email, UserErrorCode.EMAIL_REQUIRED, ose) == null ||
				!CommonValidator.isEmailValid(email)) {
			ose.addError(UserErrorCode.INVALID_EMAIL);
			return;
		}
		
		user.setEmailAddress(email);
	}

	private void setAuthDomain(User user, String domainName, OpenSpecimenException ose) {
		if (ensureNotEmpty(domainName, UserErrorCode.DOMAIN_NAME_REQUIRED, ose) == null) {
			return;
		}

		AuthDomain authDomain = daoFactory.getDomainDao().getAuthDomainByName(domainName);
		if (authDomain == null) {
			ose.addError(UserErrorCode.DOMAIN_NOT_FOUND);
			return;
		}
		
		user.setAuthDomain(authDomain);
	}
	
	private String ensureNotEmpty(String value, UserErrorCode errorCode, OpenSpecimenException ose) {
		if (StringUtils.isBlank(value)) {
			ose.addError(errorCode);
			return null;
		}
		
		return value;
	}
}
