package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetails;


public interface StorageContainerFactory {

	public StorageContainer createStorageContainer (StorageContainerDetails details);
	
	public StorageContainer patchStorageContainer(StorageContainer oldStorageContainer, StorageContainerDetails details);

}
