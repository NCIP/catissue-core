
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateStorageContainerEvent extends RequestEvent {

	private StorageContainerDetails details;
	
	public CreateStorageContainerEvent(StorageContainerDetails details) {
		setDetails(details);
	}

	public StorageContainerDetails getDetails() {
		return details;
	}

	public void setDetails(StorageContainerDetails details) {
		this.details = details;
	}

}
