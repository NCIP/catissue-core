package com.krishagni.catissueplus.core.administrative.events;

import java.util.Date;

public class ContainerSelectorCriteria {
	private String specimenClass;

	private String type;

	private Long cpId;

	private int minFreePositions;

	private Date reservedLaterThan;

	private int numContainers;

	public ContainerSelectorCriteria() {

	}

	public ContainerSelectorCriteria specimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
		return this;
	}

	public String specimenClass() {
		return specimenClass;
	}

	public ContainerSelectorCriteria type(String type) {
		this.type = type;
		return this;
	}

	public String type() {
		return type;
	}

	public ContainerSelectorCriteria cpId(Long cpId) {
		this.cpId = cpId;
		return this;
	}

	public Long cpId() {
		return cpId;
	}

	public ContainerSelectorCriteria minFreePositions(int minFreePositions) {
		this.minFreePositions = minFreePositions;
		return this;
	}

	public int minFreePositions() {
		return minFreePositions;
	}

	public ContainerSelectorCriteria reservedLaterThan(Date reservedLaterThan) {
		this.reservedLaterThan = reservedLaterThan;
		return this;
	}

	public Date reservedLaterThan() {
		return reservedLaterThan;
	}

	public int numContainers() {
		return numContainers <= 0 ? 1 : numContainers;
	}

	public ContainerSelectorCriteria numContainers(int numContainers) {
		this.numContainers = numContainers;
		return this;
	}
}
