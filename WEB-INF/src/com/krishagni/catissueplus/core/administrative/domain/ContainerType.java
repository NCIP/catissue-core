package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.krishagni.catissueplus.core.administrative.domain.factory.ContainerTypeErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

@Audited
public class ContainerType extends BaseEntity {
	private static final String ENTITY_NAME = "container_type";
	
	private String name;

	private String nameFormat;

	private int noOfColumns;
	
	private int noOfRows;
	
	private String columnLabelingScheme;
	
	private String rowLabelingScheme;
	
	private Double temperature;
	
	private boolean storeSpecimenEnabled;
	
	private ContainerType canHold;
	
	private String activityStatus;
	
	private Set<StorageContainer> containers = new HashSet<StorageContainer>();

	private Set<ContainerType> canBeStoredIn = new HashSet<ContainerType>();

	public static String getEntityName() {
		return ENTITY_NAME;
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

	public ContainerType getCanHold() {
		return canHold;
	}

	public void setCanHold(ContainerType canHold) {
		this.canHold = canHold;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	@NotAudited
	public Set<StorageContainer> getContainers() {
		return containers;
	}

	public void setContainers(Set<StorageContainer> containers) {
		this.containers = containers;
	}

	@NotAudited
	public Set<ContainerType> getCanBeStoredIn() {
		return canBeStoredIn;
	}

	public void setCanBeStoredIn(Set<ContainerType> canBeStoredIn) {
		this.canBeStoredIn = canBeStoredIn;
	}


	public void update(ContainerType containerType) {
		setName(containerType.getName());
		setNameFormat(containerType.getNameFormat());
		setNoOfColumns(containerType.getNoOfColumns());
		setNoOfRows(containerType.getNoOfRows());
		setColumnLabelingScheme(containerType.getColumnLabelingScheme());
		setRowLabelingScheme(containerType.getRowLabelingScheme());
		setTemperature(containerType.getTemperature());
		setStoreSpecimenEnabled(containerType.isStoreSpecimenEnabled());
		setActivityStatus(containerType.getActivityStatus());
		
		updateCanHold(containerType.getCanHold());
	}
	
	public List<DependentEntityDetail> getDependentEntities() {
		return DependentEntityDetail
				.listBuilder()
				.add(ContainerType.getEntityName(), getCanBeStoredIn().size())
				.add(StorageContainer.getEntityName(), getContainers().size())
				.build();
	}
	
	public void delete() {
		List<DependentEntityDetail> dependentEntities = getDependentEntities();
		if (!dependentEntities.isEmpty()) {
			throw OpenSpecimenException.userError(ContainerTypeErrorCode.REF_ENTITY_FOUND);
		}

		setName(Utility.getDisabledValue(getName(), 64));
		setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}

	private void updateCanHold(ContainerType canHold) {
		if (cycleExistsInHierarchy(canHold)) {
			throw OpenSpecimenException.userError(ContainerTypeErrorCode.CYCLES_NOT_ALLOWED, getName(), canHold.getName());
		}
		
		setCanHold(canHold);
	}

	private boolean cycleExistsInHierarchy(ContainerType canHold) {
		ContainerType other = canHold;
		while (other != null) {
			if (this.equals(other)) {
				return true;
			}

			other = other.getCanHold();
		}
		
		return false;
	}
}
