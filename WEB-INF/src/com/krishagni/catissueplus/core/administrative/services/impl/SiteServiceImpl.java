
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.impl.SiteDependencyChecker;
import com.krishagni.catissueplus.core.administrative.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.administrative.events.SiteDetail;
import com.krishagni.catissueplus.core.administrative.events.SiteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.repository.SiteListCriteria;
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
	
	private SiteDependencyChecker siteDependencyChecker;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSiteFactory(SiteFactory siteFactory) {
		this.siteFactory = siteFactory;
	}
	
	public void setSiteDependencyChecker(SiteDependencyChecker siteDependencyCheker) {
		this.siteDependencyChecker = siteDependencyCheker;
	}

	@Override
	@PlusTransactional	
	public ResponseEvent<List<SiteDetail>> getSites(RequestEvent<SiteListCriteria> req) {
		try {
			List<Site> sites = daoFactory.getSiteDao().getSites(req.getPayload());
			return ResponseEvent.response(SiteDetail.from(sites));
		} catch(OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch(Exception ex) {
			return ResponseEvent.serverError(ex);
		}
	}

	@Override
	@PlusTransactional		
	public ResponseEvent<SiteDetail> getSite(RequestEvent<SiteQueryCriteria> req) {
		SiteQueryCriteria crit = req.getPayload();
		Site site = null;
		
		if (crit.getId() != null) {
			site = daoFactory.getSiteDao().getById(crit.getId());
		} else if (crit.getName() != null) {
			site = daoFactory.getSiteDao().getSiteByName(crit.getName());
		}
		
		if (site == null) {
			return ResponseEvent.userError(SiteErrorCode.NOT_FOUND);
		}
		
		return ResponseEvent.response(SiteDetail.from(site));
	}

	@Override
	@PlusTransactional	
	public ResponseEvent<SiteDetail> createSite(RequestEvent<SiteDetail> req) {
		try {	
			Site site = siteFactory.createSite(req.getPayload());
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueConstraint(site, ose);
			ose.checkAndThrow();
			
			daoFactory.getSiteDao().saveOrUpdate(site, true);
			
			return ResponseEvent.response(SiteDetail.from(site));
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
			SiteDetail detail = req.getPayload();

			Site existing = daoFactory.getSiteDao().getById(detail.getId());
			if (existing == null) {
				return ResponseEvent.userError(SiteErrorCode.NOT_FOUND);
			}

			Site site = siteFactory.createSite(detail);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueConstraint(site, ose);
			
			if (!existing.getActivityStatus().equals(site.getActivityStatus()) &&
				 site.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED.getStatus())) {
				// If Activity Status is changed and status is Disabled then check site dependencies 
				Map<String, List> dependencies = siteDependencyChecker.getDependencies(site);
				if (!dependencies.isEmpty()) {
					ose.addError(SiteErrorCode.REF_ENTITY_FOUND);
				}
			}
			
			ose.checkAndThrow();
			
			existing.update(site);
			daoFactory.getSiteDao().saveOrUpdate(existing);
			
			return ResponseEvent.response(SiteDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}		
	}

	@Override
	@PlusTransactional	
	public ResponseEvent<Map<String, List>> deleteSite(RequestEvent<DeleteEntityOp> req) {
		try {
			DeleteEntityOp deleteOp = req.getPayload();

			Site site = daoFactory.getSiteDao().getById(deleteOp.getId());
			if (site == null) {
				return ResponseEvent.userError(SiteErrorCode.NOT_FOUND);
			}
			
			boolean close = deleteOp.isClose();
			if (!close) {
				Map<String, List> dependencies = siteDependencyChecker.getDependencies(site);
				if (!dependencies.isEmpty()) {
					return ResponseEvent.response(dependencies);
				}
			}
			
			site.delete(close);
			daoFactory.getSiteDao().saveOrUpdate(site);
			return ResponseEvent.response(Collections.<String, List>emptyMap());
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private void ensureUniqueConstraint(Site site, OpenSpecimenException ose) {
		if (!isUniqueName(site)) {
			ose.addError(SiteErrorCode.DUP_SITE_NAME);
		}
		
		if(!isUniqueCode(site)) {
			ose.addError(SiteErrorCode.DUP_SITE_CODE);
		}
	}

	private boolean isUniqueName(Site site) {
		Site existing = daoFactory.getSiteDao().getSiteByName(site.getName());
		if (existing == null) {
			return true; // no site by this name
		} else if (site.getId() == null) {
			return false; // a different site by this name exists 
		} else if (existing.getId().equals(site.getId())) {
			return true; // same site
		} else {
			return false;
		}
	}
	
	private boolean isUniqueCode(Site site) {
		String code = site.getCode();
		if (StringUtils.isBlank(code)) {
			return true;
		}
		
		Site existing = daoFactory.getSiteDao().getSiteByCode(site.getCode());
		if (existing == null) {
			return true; // no site by this name
		} else if (site.getId() == null) {
			return false; // a different site by this name exists 
		} else if (existing.getId().equals(site.getId())) {
			return true; // same site
		} else {
			return false;
		}

	}
}