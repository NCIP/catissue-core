
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Map;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchSpecimenEvent extends RequestEvent {

	private Long id;

	private Map<String, Object> specimenProps;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<String, Object> getSpecimenProps() {
		return specimenProps;
	}

	public void setSpecimenProps(Map<String, Object> specimenProps) {
		this.specimenProps = specimenProps;
	}

}
