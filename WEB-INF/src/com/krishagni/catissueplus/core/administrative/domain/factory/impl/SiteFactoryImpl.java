
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.HashSet;
import java.util.List;
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
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.Status;

public class SiteFactoryImpl implements SiteFactory {

	private static final String SITE_TYPE = "site type";

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Site createSite(SiteDetail details) {
		Site site = new Site();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
				
		setName(site, details.getName(), ose);
		setCode(site, details.getCode());
		setCoordinatorCollection(site, details.getCoordinatorCollection(), ose);
		setType(site, details.getType(), ose);
		setAddress(site, details.getAddress());
		setActivityStatus(site, details.getActivityStatus(), ose);
		
		ose.checkAndThrow();
		return site;
	}
	

	private void setName(Site site, String siteName, OpenSpecimenException ose) {
		if (StringUtils.isBlank(siteName)) {
			ose.addError(SiteErrorCode.NAME_REQUIRED);
			return;
		}
		
		site.setName(siteName);
	}
	
	
	private void setCode(Site site, String code) {
		site.setCode(code);
	}

	private void setActivityStatus(Site site, String activityStatus, OpenSpecimenException ose) {
        if (StringUtils.isBlank(activityStatus)) {
            site.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
            return;
        }

        site.setActivityStatus(activityStatus);
	}

	private void setCoordinatorCollection(Site site, List<UserSummary> coordinatorCollection, OpenSpecimenException ose) {		
		Set<User> userCollection = new HashSet<User>();
		
		for (UserSummary userSummary : coordinatorCollection) {
			User user = daoFactory.getUserDao().getUser(userSummary.getId());			
			if (user == null) {
				ose.addError(UserErrorCode.NOT_FOUND);
				return;
			}
			
			userCollection.add(user);
		}

		site.setCoordinatorCollection(userCollection);
	}
	
	private void setType(Site site, String siteType, OpenSpecimenException ose) {
		if (StringUtils.isBlank(siteType)) {
			ose.addError(SiteErrorCode.TYPE_REQUIRED);
			return;
		}
		
		if (!CommonValidator.isValidPv(siteType, SITE_TYPE)) {
			ose.addError(SiteErrorCode.INVALID_TYPE);
		}
		
		site.setType(siteType);
	}

	private void setAddress(Site site, String address) {
		site.setAddress(address);
	}
}
