package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class StorageContainerListCriteria extends AbstractListCriteria<StorageContainerListCriteria> {

	private boolean onlyFreeContainers;
	
	private Long parentContainerId;
	
	private String siteName;
	
	private boolean anyLevelContainers;
	
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
	
	public String siteName() {
		return siteName;
	}
	
	public StorageContainerListCriteria siteName(String siteName) {
		this.siteName = siteName;
		return self();
	}
	
	public boolean anyLevelContainers() {
		return anyLevelContainers;
	}
	
	public StorageContainerListCriteria anyLevelContainers(boolean anyLevelContainers) {
		this.anyLevelContainers = anyLevelContainers;
		return self();
	}
}
