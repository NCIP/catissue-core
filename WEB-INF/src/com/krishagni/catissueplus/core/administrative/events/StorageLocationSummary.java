package com.krishagni.catissueplus.core.administrative.events;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class StorageLocationSummary implements Serializable {
	
	private static final long serialVersionUID = 3492284917328450439L;

	private Long id;
	
	private String name;

	private String mode;
	
	private String positionX;
	
	private String positionY;

	private Integer position;

	private String reservationId;

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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
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

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public static StorageLocationSummary from(StorageContainerPosition position) {
		if (position == null) {
			return null;
		}
		
		StorageLocationSummary storageLocation = new StorageLocationSummary();
		storageLocation.setId(position.getContainer().getId());
		storageLocation.setName(position.getContainer().getName());
		storageLocation.setMode(position.getContainer().getPositionLabelingMode().name());
		storageLocation.setPositionX(position.getPosOne());
		storageLocation.setPositionY(position.getPosTwo());
		storageLocation.setPosition(position.getPosition());
		storageLocation.setReservationId(position.getReservationId());
		return storageLocation;
	}

	public static List<StorageLocationSummary> from(List<StorageContainerPosition> positions) {
		return positions.stream().map(StorageLocationSummary::from).collect(Collectors.toList());
	}
}
