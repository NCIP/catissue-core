
package com.krishagni.catissueplus.core.biospecimen.events;

public class StorageLocation {

	private String containerName;

	private String positionX;

	private String positionY;

	public String getContainerName() {
		return containerName;
	}

	public String getPositionX() {
		return positionX;
	}

	public String getPositionY() {
		return positionY;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public void setPositionX(String positionX) {
		this.positionX = positionX;
	}

	public void setPositionY(String positionY) {
		this.positionY = positionY;
	}

}
