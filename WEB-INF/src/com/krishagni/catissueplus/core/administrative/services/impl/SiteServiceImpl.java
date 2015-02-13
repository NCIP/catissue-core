
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.ArrayList;
import java.util.List;

import krishagni.catissueplus.util.CommonUtil;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteFactory;
import com.krishagni.catissueplus.core.administrative.events.ListSiteCriteria;
import com.krishagni.catissueplus.core.administrative.events.SiteDetail;
import com.krishagni.catissueplus.core.administrative.events.SiteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.services.SiteService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.Status;

public class SiteServiceImpl implements SiteService {
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
	public ResponseEvent<List<SiteDetail>> getSites(RequestEvent<ListSiteCriteria> req) {
		List<Site> sites = daoFactory.getSiteDao().getSites(req.getPayload().maxResults());
		
		List<SiteDetail> result = new ArrayList<SiteDetail>();
		for (Site site : sites) {
			result.add(SiteDetail.fromDomain(site));
		}
		
		return ResponseEvent.response(result);
	}

	@Override
	@PlusTransactional		
	public ResponseEvent<SiteDetail> getSite(RequestEvent<SiteQueryCriteria> req) {
		SiteQueryCriteria crit = req.getPayload();
		Site site = null;
		
		if (crit.getId() != null) {
			site = daoFactory.getSiteDao().getSite(crit.getId());
		} else if (crit.getName() != null) {
			site = daoFactory.getSiteDao().getSite(crit.getName());
		}
		
		if (site == null) {
			return ResponseEvent.userError(SiteErrorCode.NOT_FOUND);
		}
		
		return ResponseEvent.response(SiteDetail.fromDomain(site));
	}

	@Override
	@PlusTransactional	
	public ResponseEvent<SiteDetail> createSite(RequestEvent<SiteDetail> req) {
		try {	
			Site site = siteFactory.createSite(req.getPayload());
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueSiteName(site.getName(), ose);
			ensureUniqueSiteCode(null, site.getCode(), ose);
			ose.checkAndThrow();

			daoFactory.getSiteDao().saveOrUpdate(site);
			return ResponseEvent.response(SiteDetail.fromDomain(site));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional		
	public ResponseEvent<SiteDetail> updateSite(RequestEvent<SiteDetail> req) {
		try {
			Site existing = null;
			SiteDetail detail = req.getPayload();

			if (detail.getId() != null) {
				existing = daoFactory.getSiteDao().getSite(detail.getId());
			} else if (detail.getName() != null) {
				existing = daoFactory.getSiteDao().getSite(detail.getName());
			}
			
			if (existing == null) {
				return ResponseEvent.userError(SiteErrorCode.NOT_FOUND);
			}

			Site site = siteFactory.createSite(detail);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);						
			checkSiteName(existing.getName(), site.getName(), ose);
			ensureUniqueSiteCode(existing.getCode(), site.getCode(), ose);			
			ose.checkAndThrow();
			
			existing.update(site);
			daoFactory.getSiteDao().saveOrUpdate(existing);
			return ResponseEvent.response(SiteDetail.fromDomain(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}		
	}

	@Override
	@PlusTransactional	
	public ResponseEvent<SiteDetail> deleteSite(RequestEvent<SiteQueryCriteria> req) {
		try {
			Site existing = null;
			SiteQueryCriteria crit = req.getPayload();

			if (crit.getId() != null) {
				existing = daoFactory.getSiteDao().getSite(crit.getId());
			} else if (crit.getName() != null) {
				existing = daoFactory.getSiteDao().getSite(crit.getName());
			}
			
			if (existing == null) {
				return ResponseEvent.userError(SiteErrorCode.NOT_FOUND);
			}
						
			existing.delete();
			daoFactory.getSiteDao().saveOrUpdate(existing);
			return ResponseEvent.response(SiteDetail.fromDomain(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private void checkActivityStatus(Site site) {
		if (site.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED.getStatus())) {
			site.setName(CommonUtil.appendTimestamp(site.getName()));
		}
	}

	private void checkSiteName(String oldName, String newName, OpenSpecimenException ose) {
		if (!oldName.equals(newName)) {
			ensureUniqueSiteName(newName, ose);
		}
	}

	private void ensureUniqueSiteName(String name, OpenSpecimenException ose) {
		if (!daoFactory.getSiteDao().isUniqueSiteName(name)) {
			ose.addError(SiteErrorCode.DUP_SITE_NAME);
		}
	}
	
	private void ensureUniqueSiteCode(String oldCode, String newCode, OpenSpecimenException ose) {
		if (newCode == null || newCode.equals(oldCode)) {
			return;
		}
		
		if (!daoFactory.getSiteDao().isUniqueSiteCode(newCode)) {
			ose.addError(SiteErrorCode.DUP_SITE_CODE);
		}
	}
}