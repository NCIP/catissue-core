
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchStorageContainerEvent extends RequestEvent {

	private Long storageContainerId;

	private StorageContainerDetails storageContainerDetails;

	public Long getStorageContainerId() {
		return storageContainerId;
	}

	public void setStorageContainerId(Long storageContainerId) {
		this.storageContainerId = storageContainerId;
	}

	public StorageContainerDetails getStorageContainerDetails() {
		return storageContainerDetails;
	}

	public void setStorageContainerDetails(StorageContainerDetails storageContainerDetails) {
		this.storageContainerDetails = storageContainerDetails;
	}

}