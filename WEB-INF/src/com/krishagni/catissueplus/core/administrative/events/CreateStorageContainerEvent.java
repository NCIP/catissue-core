package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateStorageContainerEvent extends RequestEvent {
	private StorageContainerDetail container;

	public StorageContainerDetail getContainer() {
		return container;
	}

	public void setContainer(StorageContainerDetail container) {
		this.container = container;
	}
}
