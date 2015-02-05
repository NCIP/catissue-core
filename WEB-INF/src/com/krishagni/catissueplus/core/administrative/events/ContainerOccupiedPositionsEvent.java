package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ContainerOccupiedPositionsEvent extends ResponseEvent {
	private List<StorageContainerPositionDetail> occupiedPositions;

	public List<StorageContainerPositionDetail> getOccupiedPositions() {
		return occupiedPositions;
	}

	public void setOccupiedPositions(
			List<StorageContainerPositionDetail> occupiedPositions) {
		this.occupiedPositions = occupiedPositions;
	}
	
	public static ContainerOccupiedPositionsEvent ok(List<StorageContainerPositionDetail> occupiedPositions) {
		ContainerOccupiedPositionsEvent resp = new ContainerOccupiedPositionsEvent();
		resp.setStatus(EventStatus.OK);
		resp.setOccupiedPositions(occupiedPositions);
		return resp;
	}
	
	public static ContainerOccupiedPositionsEvent notFound() {
		ContainerOccupiedPositionsEvent resp = new ContainerOccupiedPositionsEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static ContainerOccupiedPositionsEvent serverError(Exception e) {
		ContainerOccupiedPositionsEvent resp = new ContainerOccupiedPositionsEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;
	}	
}
