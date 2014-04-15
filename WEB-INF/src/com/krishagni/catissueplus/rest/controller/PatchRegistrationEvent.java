
package com.krishagni.catissueplus.rest.controller;

import java.util.Map;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchRegistrationEvent extends RequestEvent {

	private Long id;

	private Map<String, Object> registrationProps;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<String, Object> getRegistrationProps() {
		return registrationProps;
	}

	public void setRegistrationProps(Map<String, Object> registrationProps) {
		this.registrationProps = registrationProps;
	}

}
