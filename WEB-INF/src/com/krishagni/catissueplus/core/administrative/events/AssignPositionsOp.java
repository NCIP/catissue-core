package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;

public class AssignPositionsOp {
	private Long containerId;
	
	private String containerName;
	
	private boolean vacateOccupant;
	
	private List<StorageContainerPositionDetail> positions = new ArrayList<StorageContainerPositionDetail>();

	public Long getContainerId() {
		return containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	
	public List<StorageContainerPositionDetail> getPositions() {
		return positions;
	}

	public void setPositions(List<StorageContainerPositionDetail> positions) {
		this.positions = positions;
	}

	public boolean getVacateOccupant() {
		return vacateOccupant;
	}

	public void setVacateOccupant(boolean vacateOccupant) {
		this.vacateOccupant = vacateOccupant;
	}
}
