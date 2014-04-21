
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Map;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchScgEvent extends RequestEvent {

	private Long id;

	private Map<String, Object> scgProps;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<String, Object> getScgProps() {
		return scgProps;
	}

	public void setScgProps(Map<String, Object> scgProps) {
		this.scgProps = scgProps;
	}
}
