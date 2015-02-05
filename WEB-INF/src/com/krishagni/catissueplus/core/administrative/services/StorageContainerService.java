package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.ContainerOccupiedPositionsEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqContainerOccupiedPositionsEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqStorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateStorageContainerEvent;

public interface StorageContainerService {
	public StorageContainersEvent getStorageContainers(ReqStorageContainersEvent req);
	
	public StorageContainerEvent getStorageContainer(ReqStorageContainerEvent req);
	
	public ContainerOccupiedPositionsEvent getOccupiedPositions(ReqContainerOccupiedPositionsEvent req);
	
	public StorageContainerCreatedEvent createStorageContainer(CreateStorageContainerEvent req);
	
	public StorageContainerUpdatedEvent updateStorageContainer(UpdateStorageContainerEvent req);
}
