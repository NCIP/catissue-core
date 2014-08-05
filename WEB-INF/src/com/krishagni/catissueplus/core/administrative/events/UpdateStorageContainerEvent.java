
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateStorageContainerEvent extends RequestEvent {

	private StorageContainerDetails details;

	public UpdateStorageContainerEvent(StorageContainerDetails details, Long containerId) {
		setDetails(details);
		this.details.setId(containerId);
	}

	public StorageContainerDetails getDetails() {
		return details;
	}

	public void setDetails(StorageContainerDetails details) {
		this.details = details;
	}

}
	