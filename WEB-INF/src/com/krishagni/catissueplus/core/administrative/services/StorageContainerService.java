
package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.CreateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDisabledEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateStorageContainerEvent;

public interface StorageContainerService {

	public StorageContainerCreatedEvent createStorageContainer(CreateStorageContainerEvent event);
	
	public StorageContainerUpdatedEvent updateStorageContainer(UpdateStorageContainerEvent event);

	public StorageContainerDisabledEvent disableStorageContainer(DisableStorageContainerEvent event);
	
	public StorageContainerUpdatedEvent patchStorageContainer(PatchStorageContainerEvent event);
}
