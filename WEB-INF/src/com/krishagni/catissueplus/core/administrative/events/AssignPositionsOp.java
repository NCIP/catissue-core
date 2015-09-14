package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;

public class AssignPositionsOp {
	private Long containerId;
	
	private String containerName;
	
	private boolean overwritePosition;
	
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
	
	public boolean getOverwritePosition() {
		return overwritePosition;
	}

	public void setOverwritePosition(boolean overwritePosition) {
		this.overwritePosition = overwritePosition;
	}

	public List<StorageContainerPositionDetail> getPositions() {
		return positions;
	}

	public void setPositions(List<StorageContainerPositionDetail> positions) {
		this.positions = positions;
	}
	
}
