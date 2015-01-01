
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteFactory;
import com.krishagni.catissueplus.core.administrative.events.SiteDetails;
import com.krishagni.catissueplus.core.administrative.events.SitePatchDetails;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.Status;

public class SiteFactoryImpl implements SiteFactory {

	private static final String SITE_NAME = "site name";

	private static final String SITE_TYPE = "site type";

	private static final String USER_NAME = "user name";

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Site createSite(SiteDetails details) {
		Site site = new Site();
		
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		setName(site, details.getName(), exceptionHandler);
		setCode(site, details.getCode());
		setCoordinatorCollection(site, details.getCoordinatorCollection(), exceptionHandler);
		setType(site, details.getType(), exceptionHandler);
		setAddress(site, details.getAddress());
		setActivityStatus(site, details.getActivityStatus(), exceptionHandler);
		exceptionHandler.checkErrorAndThrow();
		return site;
	}
	

	private void setName(Site site, String siteName, ObjectCreationException exceptionHandler) {
		if (isBlank(siteName)) {
			exceptionHandler.addError(SiteErrorCode.MISSING_ATTR_VALUE, SITE_NAME);
			return;
		}
		site.setName(siteName);
	}
	
	
	private void setCode(Site site, String code) {
		site.setCode(code);
	}

	private void setActivityStatus(Site site, String activityStatus, ObjectCreationException exceptionHandler) {
        if (StringUtils.isBlank(activityStatus)) {
            site.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
            return;
        }

        site.setActivityStatus(activityStatus);
	}

	private void setCoordinatorCollection(Site site, List<UserSummary> coordinatorCollection,
			ObjectCreationException exceptionHandler) {
		Set<User> userCollection = new HashSet<User>();
		for (UserSummary userSummary : coordinatorCollection) {

			User user = daoFactory.getUserDao().getUser(userSummary.getId());
			if (user == null) {
				exceptionHandler.addError(SiteErrorCode.INVALID_ATTR_VALUE, USER_NAME);
				return;
			}
			userCollection.add(user);
		}

		site.setCoordinatorCollection(userCollection);
	}
	

	private void setType(Site site, String siteType, ObjectCreationException exceptionHandler) {

		if (isBlank(siteType)) {
			exceptionHandler.addError(SiteErrorCode.MISSING_ATTR_VALUE, SITE_TYPE);
			return;
		}
		if (!CommonValidator.isValidPv(siteType, SITE_TYPE)) {
			exceptionHandler.addError(SiteErrorCode.INVALID_ATTR_VALUE, SITE_TYPE);
		}
		site.setType(siteType);
	}

	private void setAddress(Site site, String address) {
		site.setAddress(address);
	}

	@Override
	public Site patchSite(Site site, SitePatchDetails details) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		if (details.isSiteNameModified()) {
			setName(site, details.getName(), exceptionHandler);
		}

		if (details.isSiteTypeModified()) {
			setType(site, details.getType(), exceptionHandler);
		}

		if (details.isCoordinatorCollectionModified()) {
			setCoordinatorCollection(site, details.getCoordinatorCollection(), exceptionHandler);
		}

		if (details.isActivityStatusModified()) {
			setActivityStatus(site, details.getActivityStatus(), exceptionHandler);
		}

		exceptionHandler.checkErrorAndThrow();
		return site;
	}

}
