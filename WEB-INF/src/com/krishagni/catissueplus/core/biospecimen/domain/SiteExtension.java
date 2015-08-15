package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.Site;

@Configurable
public class SiteExtension extends ExtensionForm {
	private Site site;
	
	public SiteExtension(Site site) {
		this.site = site;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}
	
	@Override
	public Long getObjectId() {
		return site.getId();
	}

	@Override
	public String getEntityType() {
		return "Site";
	}

	@Override
	public String getFormName() {
		return "Site";
	}
	
	public static SiteExtension getFor(Site site) {
		SiteExtension extension = new SiteExtension(site);
		if (site.getId() == null) {
			return extension;
		}
		
		List<Long> recIds = extension.getRecordIds();
		if (!CollectionUtils.isEmpty(recIds)) {
			extension.setId(recIds.iterator().next());
		}
		
		return extension;
	}

}
