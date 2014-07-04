
package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.CreateSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateSiteEvent;

public interface SiteService {

	public SiteCreatedEvent createSite(CreateSiteEvent reqEvent);

	public SiteUpdatedEvent updateSite(UpdateSiteEvent reqEvent);

	public SiteUpdatedEvent patchSite(PatchSiteEvent event);

	public SiteDeletedEvent deleteSite(DeleteSiteEvent reqEvent);

}
