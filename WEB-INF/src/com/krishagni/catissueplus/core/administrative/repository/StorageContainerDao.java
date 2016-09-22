
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.events.ContainerSelectorCriteria;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface StorageContainerDao extends Dao<StorageContainer> {
	public List<StorageContainer> getStorageContainers(StorageContainerListCriteria listCrit);

	public StorageContainer getByName(String name);
	
	public StorageContainer getByBarcode(String barcode);
	
	public void delete(StorageContainerPosition position);

	public Map<String, Object> getContainerIds(String key, Object value);

	public Long getStorageContainersCount(StorageContainerListCriteria listCrit);

	public List<String> getNonCompliantContainers(ContainerRestrictionsCriteria crit);

	public List<String> getNonCompliantSpecimens(ContainerRestrictionsCriteria crit);

	public int getSpecimensCount(Long containerId);

	public StorageContainerSummary getAncestorsHierarchy(Long containerId);

	public List<StorageContainerSummary> getChildContainers(Long containerId, int noOfColumns);

	public List<Long> getLeastEmptyContainerId(ContainerSelectorCriteria crit);

	public int deleteReservedPositions(List<String> reservationIds);

	public int deleteReservedPositionsOlderThan(Date expireTime);
}
	