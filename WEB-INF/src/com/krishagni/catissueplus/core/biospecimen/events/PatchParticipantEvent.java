
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Map;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchParticipantEvent extends RequestEvent {

	private Map<String, Object> participantAttributes;

	private Long id;

	public Map<String, Object> getParticipantAttributes() {
		return participantAttributes;
	}

	public void setParticipantAttributes(Map<String, Object> participantAttributes) {
		this.participantAttributes = participantAttributes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
