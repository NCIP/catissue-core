
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchStorageContainerEvent extends RequestEvent {

	private Long storageContainerId;

	private StorageContainerPatchDetails storageContainerDetails;

	public Long getStorageContainerId() {
		return storageContainerId;
	}

	public void setStorageContainerId(Long storageContainerId) {
		this.storageContainerId = storageContainerId;
	}

	public StorageContainerPatchDetails getStorageContainerDetails() {
		return storageContainerDetails;
	}

	public void setStorageContainerDetails(StorageContainerPatchDetails storageContainerDetails) {
		this.storageContainerDetails = storageContainerDetails;
	}

}