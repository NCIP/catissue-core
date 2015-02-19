
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
import com.krishagni.catissueplus.core.common.SetUpdater;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

public class UserFactoryImpl implements UserFactory {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public User createUser(UserDetail details) {
		User user = new User();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		setUserSites(user, details.getUserSiteNames(), ose);
//		setUserCPRoles(user, details.getUserCPRoles(), ose);
		setLoginName(user, details.getLoginName(), ose);
		setFirstName(user, details.getFirstName(), ose);
		setLastName(user, details.getLastName(), ose);
		setActivityStatus(user, details.getActivityStatus(), ose);
		setEmailAddress(user, details.getEmailAddress(), ose);
		setDepartment(details, user, ose);
		setAuthDomain(user, details.getDomainName(), ose);

		ose.checkAndThrow();
		return user;
	}

//	private void setUserCPRoles(User user, List<UserCPRoleDetails> userCPRoleDetails,
//			OpenSpecimenException ose) {
//		// TODO: Fix this
//		
////		if (userCPRoleDetails.isEmpty()) {
////			return;
////		}
////
////		Set<UserCPRole> userCpRoles = new HashSet<UserCPRole>();
////		for (UserCPRoleDetails ucrDetails : userCPRoleDetails) {
////			UserCPRole userCpRole = new UserCPRole();
////			CollectionProtocol collectionProtocol = daoFactory.getCollectionProtocolDao().getCPByTitle(
////					ucrDetails.getCpTitle());
////			if (collectionProtocol == null) {
////				ose.addError(UserErrorCode.INVALID_ATTR_VALUE, COLLECTION_PROTOCOL);
////				return;
////			}
////			userCpRole.setCollectionProtocol(collectionProtocol);
////			userCpRole.setUser(user);
////
////			Role role = daoFactory.getRoleDao().getRoleByName(ucrDetails.getRoleName());
////			if (role == null) {
////				ose.addError(UserErrorCode.INVALID_ATTR_VALUE, ROLE);
////				return;
////			}
////			userCpRole.setId(ucrDetails.getId());
////			userCpRole.setRole(role);
////			userCpRoles.add(userCpRole);
////		}
////		SetUpdater.<UserCPRole> newInstance().update(user.getUserCPRoles(), userCpRoles);
//	}

	private void setUserSites(User user, List<String> userSiteNames, OpenSpecimenException ose) {
		if (userSiteNames.isEmpty()) {
			return;
		}
		Set<Site> userSites = new HashSet<Site>();
		for (String siteName : userSiteNames) {
			Site site = daoFactory.getSiteDao().getSite(siteName);
			if (site == null) {
				ose.addError(SiteErrorCode.NOT_FOUND);
				return;
			}

			userSites.add(site);
		}
		SetUpdater.<Site> newInstance().update(user.getUserSites(), userSites);
	}

	private void setFirstName(User user, String firstName, OpenSpecimenException ose) {
		if (StringUtils.isBlank(firstName)) {
			ose.addError(UserErrorCode.FIRST_NAME_REQUIRED);
			return; 
		}
		
		user.setFirstName(firstName);
	}

	private void setLastName(User user, String lastName, OpenSpecimenException ose) {
		if (StringUtils.isBlank(lastName)) {
			ose.addError(UserErrorCode.LAST_NAME_REQUIRED);
			return;
		}
		
		user.setLastName(lastName);
	}

	private void setLoginName(User user, String loginName, OpenSpecimenException ose) {
		if (StringUtils.isBlank(loginName)) {
			ose.addError(UserErrorCode.LOGIN_NAME_REQUIRED);
			return;
		}
		
		user.setLoginName(loginName);
	}

	private void setDepartment(UserDetail detail, User user, OpenSpecimenException ose) {
		Department department = daoFactory.getInstituteDao()
				.getDeptByNameAndInstitute(detail.getDeptName(), detail.getInstituteName());
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
		if (StringUtils.isBlank(email)) {
			ose.addError(UserErrorCode.EMAIL_REQUIRED);
			return;
		}

		if (!CommonValidator.isEmailValid(email)) {
			ose.addError(UserErrorCode.INVALID_EMAIL);
			return;
		}
		
		user.setEmailAddress(email);
	}

	private void setAuthDomain(User user, String domainName, OpenSpecimenException ose) {
		if (StringUtils.isBlank(domainName)) {
			ose.addError(UserErrorCode.DOMAIN_NAME_REQUIRED);
			return;
		}

		AuthDomain authDomain = daoFactory.getDomainDao().getAuthDomainByName(domainName);
		if (authDomain == null) {
			ose.addError(UserErrorCode.DOMAIN_NOT_FOUND);
			return;
		}
		
		user.setAuthDomain(authDomain);
	}
}
