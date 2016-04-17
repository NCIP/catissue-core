
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface StorageContainerDao extends Dao<StorageContainer> {
	public List<StorageContainer> getStorageContainers(StorageContainerListCriteria listCrit);

	public StorageContainer getByName(String name);
	
	public StorageContainer getByBarcode(String barcode);
	
	public void delete(StorageContainerPosition position);

	public Map<String, Object> getContainerIds(String key, Object value);

	public int getStorageContainersCount(StorageContainerListCriteria listCrit);

	public Set<String> getRestrictedSpecimenClasses(Long containerId);

	public Set<String> getRestrictedSpecimenTypes(Long containerId);

	public Set<CollectionProtocol> getRestrictedCps(Long containerId);
}
	