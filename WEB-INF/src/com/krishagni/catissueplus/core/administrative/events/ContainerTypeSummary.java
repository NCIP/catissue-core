package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.administrative.domain.ContainerType;

public class ContainerTypeSummary {
	private Long id;
	
	private String name;

	private String nameFormat;
	
	private int noOfColumns;
	
	private int noOfRows;

	private String columnLabelingScheme;

	private String rowLabelingScheme;

	private Double temperature;

	private boolean storeSpecimenEnabled;

	private String activityStatus;

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

	public String getNameFormat() {
		return nameFormat;
	}

	public void setNameFormat(String nameFormat) {
		this.nameFormat = nameFormat;
	}

	public int getNoOfColumns() {
		return noOfColumns;
	}

	public void setNoOfColumns(int noOfColumns) {
		this.noOfColumns = noOfColumns;
	}

	public int getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}

	public String getColumnLabelingScheme() {
		return columnLabelingScheme;
	}

	public void setColumnLabelingScheme(String columnLabelingScheme) {
		this.columnLabelingScheme = columnLabelingScheme;
	}

	public String getRowLabelingScheme() {
		return rowLabelingScheme;
	}

	public void setRowLabelingScheme(String rowLabelingScheme) {
		this.rowLabelingScheme = rowLabelingScheme;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public boolean isStoreSpecimenEnabled() {
		return storeSpecimenEnabled;
	}

	public void setStoreSpecimenEnabled(boolean storeSpecimenEnabled) {
		this.storeSpecimenEnabled = storeSpecimenEnabled;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public static <T extends ContainerTypeSummary> void copy(ContainerType containerType, T detail) {
		detail.setId(containerType.getId());
		detail.setName(containerType.getName());
		detail.setNameFormat(containerType.getNameFormat());
		detail.setNoOfColumns(containerType.getNoOfColumns());
		detail.setNoOfRows(containerType.getNoOfRows());
		detail.setColumnLabelingScheme(containerType.getColumnLabelingScheme());
		detail.setRowLabelingScheme(containerType.getRowLabelingScheme());
		detail.setTemperature(containerType.getTemperature());
		detail.setStoreSpecimenEnabled(containerType.isStoreSpecimenEnabled());
		detail.setActivityStatus(containerType.getActivityStatus());
	}
	
	public static ContainerTypeSummary from(ContainerType containerType) {
		if (containerType == null) {
			return null;
		}
		
		ContainerTypeSummary result = new ContainerTypeSummary();
		copy(containerType, result);
		return result;
	}
	
	public static List<ContainerTypeSummary> from(List<ContainerType> containerTypes) {
		return containerTypes.stream().map(ContainerTypeSummary::from).collect(Collectors.toList());
	}
}
