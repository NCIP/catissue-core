package com.krishagni.catissueplus.core.administrative.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.de.domain.DeObject;

@Configurable
public class SiteExtension extends DeObject {
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
	
	@Override
	public Long getCpId() {
		return -1L;
	}
	
	@Override
	public void setAttrValues(Map<String, Object> attrValues) {
		// TODO Auto-generated method stub
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
