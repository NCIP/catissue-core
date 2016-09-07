package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;

public class ContainerReplicationDetail {
	private String sourceContainerName;
	
	private Long sourceContainerId;
	
	private List<DestinationDetail> destinations = new ArrayList<DestinationDetail>();

	public String getSourceContainerName() {
		return sourceContainerName;
	}

	public void setSourceContainerName(String sourceContainerName) {
		this.sourceContainerName = sourceContainerName;
	}

	public Long getSourceContainerId() {
		return sourceContainerId;
	}

	public void setSourceContainerId(Long sourceContainerId) {
		this.sourceContainerId = sourceContainerId;
	}

	public List<DestinationDetail> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<DestinationDetail> destinations) {
		this.destinations = destinations;
	}

	public static class DestinationDetail {
		private String name;
		
		private String siteName;
		
		private String parentContainerName;
		
		private Long parentContainerId;
		
		private String posOne;
		
		private String posTwo;

		private int position;
		
		public String getSiteName() {
			return siteName;
		}

		public void setSiteName(String siteName) {
			this.siteName = siteName;
		}

		public String getParentContainerName() {
			return parentContainerName;
		}

		public void setParentContainerName(String parentContainerName) {
			this.parentContainerName = parentContainerName;
		}

		public Long getParentContainerId() {
			return parentContainerId;
		}

		public void setParentContainerId(Long parentContainerId) {
			this.parentContainerId = parentContainerId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPosOne() {
			return posOne;
		}

		public void setPosOne(String posOne) {
			this.posOne = posOne;
		}

		public String getPosTwo() {
			return posTwo;
		}

		public void setPosTwo(String posTwo) {
			this.posTwo = posTwo;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}
	}
}