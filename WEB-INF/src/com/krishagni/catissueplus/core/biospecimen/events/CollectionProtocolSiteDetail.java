package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolSite;

@JsonFilter("withoutId")
public class CollectionProtocolSiteDetail {
	private Long id;
	
	private String siteName;
	
	private String code;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public static CollectionProtocolSiteDetail from(CollectionProtocolSite cpSite) {
		CollectionProtocolSiteDetail detail = new CollectionProtocolSiteDetail();
		detail.setId(cpSite.getId());
		detail.setSiteName(cpSite.getSite().getName());
		detail.setCode(cpSite.getCode());
		
		return detail;
	}
	
	public static List<CollectionProtocolSiteDetail> from(Collection<CollectionProtocolSite> cpSites) {
		List<CollectionProtocolSiteDetail> result = new ArrayList<CollectionProtocolSiteDetail>();
		for (CollectionProtocolSite cpSite: cpSites) {
			result.add(from(cpSite));
		}
		
		return result;
	}
	
}
