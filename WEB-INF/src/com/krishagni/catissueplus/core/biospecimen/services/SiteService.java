package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.administrative.events.CreateSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateSiteEvent;


public interface SiteService {

	public SiteCreatedEvent createSite(CreateSiteEvent reqEvent);

	public SiteUpdatedEvent updateSite(UpdateSiteEvent reqEvent);

	public SiteUpdatedEvent patchSite(PatchSiteEvent event);
}
