
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface StorageContainerDao extends Dao<StorageContainer> {
	public List<StorageContainer> getStorageContainers(StorageContainerListCriteria listCrit);

	public StorageContainer getStorageContainerByName(String name);
	
	public StorageContainer getStorageContainerByBarcode(String barcode);

	public StorageContainer getStorageContainer(Long id);
	
	public void delete(StorageContainerPosition position);
}
	