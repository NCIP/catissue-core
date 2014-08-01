
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;

public class PvInfo {

	private String value;

	private String attribute;

	private Long id;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getAttribute() {
		return attribute;
	}

	
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public static PvInfo fromDomain(PermissibleValue pv) {
		PvInfo info = new PvInfo();
		info.setId(pv.getId());
		info.setAttribute(pv.getAttribute());
		info.setValue(pv.getValue());
		return info;
	}
}
