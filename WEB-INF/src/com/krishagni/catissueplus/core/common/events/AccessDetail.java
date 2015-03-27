package com.krishagni.catissueplus.core.common.events;

import java.util.HashSet;
import java.util.Set;

public class AccessDetail {
	public static AccessDetail ACCESS_TO_ALL = new AccessDetail(true);
	
	private Boolean accessAll;
	
	private Set<Long> ids = new HashSet<Long>();

	public AccessDetail() {
		
	}
	
	public AccessDetail(boolean canAccessAll) {
		accessAll = canAccessAll;
	}

	public Boolean canAccessAll() {
		return accessAll;
	}

	public void setAccessAll(Boolean accessAll) {
		this.accessAll = accessAll;
	}

	public Set<Long> getIds() {
		return ids;
	}

	public void setIds(Set<Long> ids) {
		this.ids = ids;
	}
	
	public boolean isAccessAllowed() {
		return accessAll || !ids.isEmpty();
	}
}
