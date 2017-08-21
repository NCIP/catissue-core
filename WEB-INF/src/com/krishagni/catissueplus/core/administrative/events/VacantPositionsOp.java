package com.krishagni.catissueplus.core.administrative.events;

public class VacantPositionsOp {
	private Long containerId;

	private String containerName;

	private String startRow;

	private String startColumn;

	private Integer startPosition;

	private int requestedPositions = 1;

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

	public String getStartRow() {
		return startRow;
	}

	public void setStartRow(String startRow) {
		this.startRow = startRow;
	}

	public String getStartColumn() {
		return startColumn;
	}

	public void setStartColumn(String startColumn) {
		this.startColumn = startColumn;
	}

	public Integer getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(Integer startPosition) {
		this.startPosition = startPosition;
	}

	public int getRequestedPositions() {
		return requestedPositions;
	}

	public void setRequestedPositions(int requestedPositions) {
		this.requestedPositions = requestedPositions;
	}
}
