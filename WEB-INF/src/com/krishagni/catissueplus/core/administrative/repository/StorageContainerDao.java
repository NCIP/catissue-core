
package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface StorageContainerDao extends Dao<StorageContainer> {

	public StorageContainer getStorageContainerByName(String name);

	public Boolean isUniqueContainerName(String anyString);

	public StorageContainer getStorageContainer(Long id);

	public boolean isUniqueBarcode(String barcode);
}
