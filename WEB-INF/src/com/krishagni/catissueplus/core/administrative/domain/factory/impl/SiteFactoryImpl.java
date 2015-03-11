
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.SiteDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.Status;

public class SiteFactoryImpl implements SiteFactory {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Site createSite(SiteDetail detail) {
		Site site = new Site();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		site.setId(detail.getId());
		setName(detail, site, ose);
		setCode(detail, site);
		setCoordinators(detail, site, ose);
		setType(detail, site, ose);
		setAddress(detail, site);
		setActivityStatus(detail, site, ose);
		
		ose.checkAndThrow();
		return site;
	}
	

	private void setName(SiteDetail detail, Site site, OpenSpecimenException ose) {
		if (StringUtils.isBlank(detail.getName())) {
			ose.addError(SiteErrorCode.NAME_REQUIRED);
			return;
		}
		
		site.setName(detail.getName());
	}
	
	private void setCode(SiteDetail detail, Site site) {
		site.setCode(detail.getCode());
	}


	private void setCoordinators(SiteDetail detail, Site site, OpenSpecimenException ose) {		
		Set<User> result = new HashSet<User>();
		
		for (UserSummary userSummary : detail.getCoordinators()) {
			User user = daoFactory.getUserDao().getById(userSummary.getId());			
			if (user == null) {
				ose.addError(UserErrorCode.NOT_FOUND);
				return;
			}
			
			result.add(user);
		}

		site.setCoordinators(result);
	}
	
	private void setType(SiteDetail detail, Site site, OpenSpecimenException ose) {
		String siteType = detail.getType();
		if (StringUtils.isBlank(siteType)) {
			ose.addError(SiteErrorCode.TYPE_REQUIRED);
			return;
		}
		
		if (!CommonValidator.isValidPv(siteType, "site type")) {
			ose.addError(SiteErrorCode.INVALID_TYPE);
		}
		
		site.setType(siteType);
	}

	private void setAddress(SiteDetail detail, Site site) {
		site.setAddress(detail.getAddress());
	}
	
	private void setActivityStatus(SiteDetail detail, Site site, OpenSpecimenException ose) {
		String activityStatus = detail.getActivityStatus();
		if (StringUtils.isBlank(activityStatus)) {
			site.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
			return;
		}
		
		if (!Status.isValidActivityStatus(activityStatus)) {
			ose.addError(ActivityStatusErrorCode.INVALID);
			return;
		}
		
		site.setActivityStatus(activityStatus);
	}
}
