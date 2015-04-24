package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;

public interface StorageContainerFactory {
	public StorageContainer createStorageContainer(StorageContainerDetail detail);
	
	public StorageContainer createStorageContainer(StorageContainer existing, StorageContainerDetail detail);
}
