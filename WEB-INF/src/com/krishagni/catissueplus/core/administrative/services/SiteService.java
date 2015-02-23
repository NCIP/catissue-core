
package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.administrative.events.SiteDetail;
import com.krishagni.catissueplus.core.administrative.events.SiteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.repository.SiteListCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface SiteService {
	public ResponseEvent<List<SiteDetail>> getSites(RequestEvent<SiteListCriteria> req);

	public ResponseEvent<SiteDetail> getSite(RequestEvent<SiteQueryCriteria> req);

	public ResponseEvent<SiteDetail> createSite(RequestEvent<SiteDetail> req);

	public ResponseEvent<SiteDetail> updateSite(RequestEvent<SiteDetail> req);

	public ResponseEvent<Map<String, List>> deleteSite(RequestEvent<DeleteEntityOp> req);
}
