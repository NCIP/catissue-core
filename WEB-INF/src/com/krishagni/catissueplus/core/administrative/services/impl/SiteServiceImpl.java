
package com.krishagni.catissueplus.core.administrative.services.impl;

import krishagni.catissueplus.util.CommonUtil;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteFactory;
import com.krishagni.catissueplus.core.administrative.events.CreateSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteDetails;
import com.krishagni.catissueplus.core.administrative.events.SiteUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateSiteEvent;
import com.krishagni.catissueplus.core.administrative.services.SiteService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.util.Status;

public class SiteServiceImpl implements SiteService {

	private static final String SITE_NAME = "site name";

	private SiteFactory siteFactory;

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSiteFactory(SiteFactory siteFactory) {
		this.siteFactory = siteFactory;
	}

	@Override
	@PlusTransactional
	public SiteCreatedEvent createSite(CreateSiteEvent createSiteEvent) {
		try {
			Site site = siteFactory.createSite(createSiteEvent.getSiteDetails());
			ObjectCreationException exceptionHandler = new ObjectCreationException();
			ensureUniqueSiteName(site.getName(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();

			daoFactory.getSiteDao().saveOrUpdate(site);
			return SiteCreatedEvent.ok(SiteDetails.fromDomain(site));
		}
		catch (ObjectCreationException ex) {
			return SiteCreatedEvent.invalidRequest(ex.getMessage(), ex.getErroneousFields());
		}
		catch (Exception e) {
			return SiteCreatedEvent.serverError(e);
		}

	}

	@Override
	@PlusTransactional
	public SiteUpdatedEvent updateSite(UpdateSiteEvent updateEvent) {
		try {
			Site oldSite = null;

			if (updateEvent.getSiteDetails().getId() != null) {
				Long siteId = updateEvent.getSiteDetails().getId();
				oldSite = daoFactory.getSiteDao().getSite(siteId);
				if (oldSite == null) {
					return SiteUpdatedEvent.notFound(siteId);
				}
			}
			else {
				String siteName = updateEvent.getSiteName();
				oldSite = daoFactory.getSiteDao().getSite(siteName);
				if (oldSite == null) {
					return SiteUpdatedEvent.notFound(siteName);
				}
			}

			ObjectCreationException exceptionHandler = new ObjectCreationException();
			Site site = siteFactory.createSite(updateEvent.getSiteDetails());
			checkSiteName(oldSite.getName(), site.getName(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();
			oldSite.update(site);
			daoFactory.getSiteDao().saveOrUpdate(oldSite);
			return SiteUpdatedEvent.ok(SiteDetails.fromDomain(oldSite));
		}
		catch (ObjectCreationException ce) {
			return SiteUpdatedEvent.invalidRequest(SiteErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return SiteUpdatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public SiteUpdatedEvent patchSite(PatchSiteEvent event) {
		try {
			Site oldSite;

			if (event.getSiteId() != null) {
				Long siteId = event.getSiteId();
				oldSite = daoFactory.getSiteDao().getSite(siteId);
				if (oldSite == null) {
					return SiteUpdatedEvent.notFound(siteId);
				}
			}
			else {
				String siteName = event.getSiteName();
				oldSite = daoFactory.getSiteDao().getSite(siteName);
				if (oldSite == null) {
					return SiteUpdatedEvent.notFound(siteName);
				}
			}

			Site site = siteFactory.patchSite(oldSite, event.getSiteDetails());
			ObjectCreationException exceptionHandler = new ObjectCreationException();
			checkSiteName(site.getName(), event.getSiteDetails().getName(), exceptionHandler);
			
			if (event.getSiteDetails().isActivityStatusModified()) {
				checkActivityStatus(site);
			}
			
			exceptionHandler.checkErrorAndThrow();

			daoFactory.getSiteDao().saveOrUpdate(site);
			return SiteUpdatedEvent.ok(SiteDetails.fromDomain(site));
		}
		catch (ObjectCreationException exception) {
			return SiteUpdatedEvent.invalidRequest(SiteErrorCode.ERRORS.message(), exception.getErroneousFields());
		}
		catch (Exception e) {
			return SiteUpdatedEvent.serverError(e);
		}
	}

	private void checkActivityStatus(Site site) {
		if(site.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED.getStatus()))
		{
			site.setName(CommonUtil.appendTimestamp(site.getName()));
		}
	}

	private void checkSiteName(String oldName, String newName, ObjectCreationException exceptionHandler) {
		if (!(oldName.equals(newName))) {
			ensureUniqueSiteName(newName, exceptionHandler);
		}

	}

	private void ensureUniqueSiteName(String name, ObjectCreationException exceptionHandler) {
		if (!daoFactory.getSiteDao().isUniqueSiteName(name)) {
			exceptionHandler.addError(SiteErrorCode.DUPLICATE_SITE_NAME, SITE_NAME);
		}
	}
}