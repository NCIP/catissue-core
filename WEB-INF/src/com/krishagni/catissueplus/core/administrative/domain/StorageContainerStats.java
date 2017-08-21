package com.krishagni.catissueplus.core.administrative.domain;

public class StorageContainerStats {
	private StorageContainer container;
	
	private int freePositions;

	public Long getId() {
		return container == null ? null : container.getId();
	}
	
	public void setId(Long id) {
		
	}
	
	public StorageContainer getContainer() {
		return container;
	}

	public void setContainer(StorageContainer container) {
		this.container = container;
	}

	public int getFreePositions() {
		return freePositions;
	}

	public void setFreePositions(int freePositions) {
		this.freePositions = freePositions;
	}
}
