package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;

public class StorageLocationSummary {
	private Long id;
	
	private String name;
	
	private String positionX;
	
	private String positionY;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPositionX() {
		return positionX;
	}

	public void setPositionX(String positionX) {
		this.positionX = positionX;
	}

	public String getPositionY() {
		return positionY;
	}

	public void setPositionY(String positionY) {
		this.positionY = positionY;
	}
	
	public static StorageLocationSummary from(StorageContainerPosition position) {
		if (position == null) {
			return null;
		}
		
		StorageLocationSummary storageLocation = new StorageLocationSummary();
		storageLocation.setName(position.getContainer().getName());
		storageLocation.setId(position.getContainer().getId());
		storageLocation.setPositionX(position.getPosOne());
		storageLocation.setPositionY(position.getPosTwo());
		return storageLocation;
	}
}
