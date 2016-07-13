
package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.SiteDetail;
import com.krishagni.catissueplus.core.administrative.events.SiteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.SiteSummary;
import com.krishagni.catissueplus.core.administrative.repository.SiteListCriteria;
import com.krishagni.catissueplus.core.common.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface SiteService {
	public ResponseEvent<List<SiteSummary>> getSites(RequestEvent<SiteListCriteria> req);

	public ResponseEvent<Long> getSitesCount(RequestEvent<SiteListCriteria> req);

	public ResponseEvent<SiteDetail> getSite(RequestEvent<SiteQueryCriteria> req);

	public ResponseEvent<SiteDetail> createSite(RequestEvent<SiteDetail> req);

	public ResponseEvent<SiteDetail> updateSite(RequestEvent<SiteDetail> req);
	
	public ResponseEvent<SiteDetail> patchSite(RequestEvent<SiteDetail> req);
	
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req);

	public ResponseEvent<SiteDetail> deleteSite(RequestEvent<DeleteEntityOp> req);
	
}
