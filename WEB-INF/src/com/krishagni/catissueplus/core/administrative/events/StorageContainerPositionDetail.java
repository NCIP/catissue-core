package com.krishagni.catissueplus.core.administrative.events;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;

public class StorageContainerPositionDetail implements Comparable<StorageContainerPositionDetail> {
	private Long id;

	private String mode;

	private String posOne;
	
	private String posTwo;
	
	private Integer posOneOrdinal;
	
	private Integer posTwoOrdinal;

	private int position;
	
	private Long containerId;
	
	private String containerName;

	private String occuypingEntity;
	
	private Long occupyingEntityId;
	
	private String occupyingEntityName;

	private String cpShortTitle;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
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

	public Integer getPosOneOrdinal() {
		return posOneOrdinal;
	}

	public void setPosOneOrdinal(Integer posOneOrdinal) {
		this.posOneOrdinal = posOneOrdinal;
	}

	public Integer getPosTwoOrdinal() {
		return posTwoOrdinal;
	}

	public void setPosTwoOrdinal(Integer posTwoOrdinal) {
		this.posTwoOrdinal = posTwoOrdinal;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

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
	
	public String getOccuypingEntity() {
		return occuypingEntity;
	}

	public void setOccuypingEntity(String occuypingEntity) {
		this.occuypingEntity = occuypingEntity;
	}

	public Long getOccupyingEntityId() {
		return occupyingEntityId;
	}

	public void setOccupyingEntityId(Long occupyingEntityId) {
		this.occupyingEntityId = occupyingEntityId;
	}

	public String getOccupyingEntityName() {
		return occupyingEntityName;
	}

	public void setOccupyingEntityName(String occupyingEntityName) {
		this.occupyingEntityName = occupyingEntityName;
	}

	public String getCpShortTitle() {
		return cpShortTitle;
	}

	public void setCpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
	}

	@Override
	public int compareTo(StorageContainerPositionDetail other) {
		if (posTwoOrdinal < other.posTwoOrdinal) {
			return -1;
		} else if (posTwoOrdinal > other.posTwoOrdinal) {
			return 1;
		} else {
			return posOneOrdinal - other.posOneOrdinal;
		}		
	}
	
	public static StorageContainerPositionDetail from(StorageContainerPosition position) {
		if (position == null) {
			return null;
		}
		
		StorageContainerPositionDetail result = new StorageContainerPositionDetail();
		result.setId(position.getId());
		result.setPosOne(position.getPosOne());
		result.setPosTwo(position.getPosTwo());
		result.setPosOneOrdinal(position.getPosOneOrdinal());
		result.setPosTwoOrdinal(position.getPosTwoOrdinal());
		result.setPosition(position.getPosition());
		result.setContainerId(position.getContainer().getId());
		result.setContainerName(position.getContainer().getName());
		result.setMode(position.getContainer().getPositionLabelingMode().name());
		
		if (position.getOccupyingSpecimen() != null) {
			result.setOccuypingEntity("specimen");
			result.setOccupyingEntityId(position.getOccupyingSpecimen().getId());
			result.setOccupyingEntityName(position.getOccupyingSpecimen().getLabel());
		} else if (position.getOccupyingContainer() != null) {
			result.setOccuypingEntity("container");
			result.setOccupyingEntityId(position.getOccupyingContainer().getId());
			result.setOccupyingEntityName(position.getOccupyingContainer().getName());
		}
		
		return result;
	}
	
	public static List<StorageContainerPositionDetail> from(Collection<StorageContainerPosition> positions) {
		return positions.stream().map(StorageContainerPositionDetail::from).sorted().collect(Collectors.toList());
	}
}
