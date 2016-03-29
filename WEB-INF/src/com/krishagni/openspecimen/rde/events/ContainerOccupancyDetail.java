package com.krishagni.openspecimen.rde.events;

import java.util.ArrayList;
import java.util.List;

public class ContainerOccupancyDetail {
	
	private String containerName;
	
	private List<Position> positions = new ArrayList<Position>();
		
	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}

	public static class Position {
		private String label;
		
		private String cpShortTitle;
		
		private String type;
		
		private String specimenClass;
		
		private String positionX;
		
		private String positionY;
		
		private String errorCode;
		
		private String errorMsg;
		
		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getCpShortTitle() {
			return cpShortTitle;
		}

		public void setCpShortTitle(String cpShortTitle) {
			this.cpShortTitle = cpShortTitle;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getSpecimenClass() {
			return specimenClass;
		}

		public void setSpecimenClass(String specimenClass) {
			this.specimenClass = specimenClass;
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

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		public String getErrorMsg() {
			return errorMsg;
		}

		public void setErrorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
		}		
	}
}
