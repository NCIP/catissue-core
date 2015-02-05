package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.common.repository.impl.AbstractListCriteria;

public class StorageContainerListCriteria extends AbstractListCriteria<StorageContainerListCriteria> {

	private boolean onlyFreeContainers;
	
	private Long parentContainerId;
	
	@Override
	public StorageContainerListCriteria self() {
		return this;
	}
	
	public boolean onlyFreeContainers() {
		return onlyFreeContainers;
	}
	
	public StorageContainerListCriteria onlyFreeContainers(boolean onlyFreeContainers) {
		this.onlyFreeContainers = onlyFreeContainers;
		return self();
	}
	
	public Long parentContainerId() {
		return parentContainerId;
	}
	
	public StorageContainerListCriteria parentContainerId(Long parentContainerId) {
		this.parentContainerId = parentContainerId;
		return self();
	}
}
