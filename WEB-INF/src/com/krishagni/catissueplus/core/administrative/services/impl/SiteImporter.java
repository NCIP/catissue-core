package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.events.SiteDetail;
import com.krishagni.catissueplus.core.administrative.services.SiteService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class SiteImporter implements ObjectImporter<SiteDetail, SiteDetail> {
	
	private SiteService siteSvc;
	
	public void setSiteSvc(SiteService siteSvc) {
		this.siteSvc = siteSvc;
	}

	@Override
	public ResponseEvent<SiteDetail> importObject(RequestEvent<ImportObjectDetail<SiteDetail>> req) {
		try {
			ImportObjectDetail<SiteDetail> detail = req.getPayload();
			RequestEvent<SiteDetail> siteReq = new RequestEvent<SiteDetail>(detail.getObject());
			if (detail.isCreate()) {
				return siteSvc.createSite(siteReq);
			} else {
				return siteSvc.patchSite(siteReq);
			}			
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
