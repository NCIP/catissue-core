
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqSpecimenSummaryEvent extends RequestEvent {

	public enum ObjectType {
		SCG("scg"), CPE("cpe");

		private String objectType;

		private ObjectType(String name) {
			objectType = name;
		}

		public String getName() {
			return objectType;
		}

	}

	private String objectType;

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long scgId) {
		this.id = scgId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

}
