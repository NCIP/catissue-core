package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.ContainerType;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.events.ContainerHierarchyDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;

public interface StorageContainerFactory {
	public StorageContainer createStorageContainer(StorageContainerDetail detail);
	
	public StorageContainer createStorageContainer(StorageContainer existing, StorageContainerDetail detail);
	
	public StorageContainer createStorageContainer(String name, ContainerHierarchyDetail hierarchyDetail);
	
	public StorageContainer createStorageContainer(String name, ContainerType containerType, StorageContainer parentContainer);
}
