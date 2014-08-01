
package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.CreateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.GetAllStorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.GetStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllStorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDisabledEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerGotEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateStorageContainerEvent;

public interface StorageContainerService {

	public StorageContainerCreatedEvent createStorageContainer(CreateStorageContainerEvent event);
	
	public StorageContainerUpdatedEvent updateStorageContainer(UpdateStorageContainerEvent event);

	public StorageContainerDisabledEvent disableStorageContainer(DisableStorageContainerEvent event);
	
	public StorageContainerUpdatedEvent patchStorageContainer(PatchStorageContainerEvent event);

	public GetAllStorageContainersEvent getStorageContainers(ReqAllStorageContainersEvent event);
	
	public StorageContainerGotEvent getStorageContainer(GetStorageContainerEvent event);
}
