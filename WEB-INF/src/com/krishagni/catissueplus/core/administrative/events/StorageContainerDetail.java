package com.krishagni.catissueplus.core.administrative.events;

import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;

public class StorageContainerDetail extends StorageContainerSummary {
	private Double temperature;
	
	private String dimensionOneLabelingScheme;
	
	private String dimensionTwoLabelingScheme;
	
	private String comments;
	
	private Set<String> allowedSpecimenClasses = new HashSet<String>(); 
	
	private Set<String> allowedSpecimenTypes = new HashSet<String>();

	private Set<String> allowedCollectionProtocols = new HashSet<String>();
	
	private StorageContainerPositionDetail position;
	
	private Set<Integer> occupiedPositions = new HashSet<Integer>();

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public String getDimensionOneLabelingScheme() {
		return dimensionOneLabelingScheme;
	}

	public void setDimensionOneLabelingScheme(String dimensionOneLabelingScheme) {
		this.dimensionOneLabelingScheme = dimensionOneLabelingScheme;
	}

	public String getDimensionTwoLabelingScheme() {
		return dimensionTwoLabelingScheme;
	}

	public void setDimensionTwoLabelingScheme(String dimensionTwoLabelingScheme) {
		this.dimensionTwoLabelingScheme = dimensionTwoLabelingScheme;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Set<String> getAllowedSpecimenClasses() {
		return allowedSpecimenClasses;
	}

	public void setAllowedSpecimenClasses(Set<String> allowedSpecimenClasses) {
		this.allowedSpecimenClasses = allowedSpecimenClasses;
	}

	public Set<String> getAllowedSpecimenTypes() {
		return allowedSpecimenTypes;
	}

	public void setAllowedSpecimenTypes(Set<String> allowedSpecimenTypes) {
		this.allowedSpecimenTypes = allowedSpecimenTypes;
	}

	public Set<String> getAllowedCollectionProtocols() {
		return allowedCollectionProtocols;
	}

	public void setAllowedCollectionProtocols(Set<String> allowedCollectionProtocols) {
		this.allowedCollectionProtocols = allowedCollectionProtocols;
	}

	public StorageContainerPositionDetail getPosition() {
		return position;
	}

	public void setPosition(StorageContainerPositionDetail position) {
		this.position = position;
	}

	public Set<Integer> getOccupiedPositions() {
		return occupiedPositions;
	}

	public void setOccupiedPositions(Set<Integer> occupiedPositions) {
		this.occupiedPositions = occupiedPositions;
	}
	
	public static StorageContainerDetail from(StorageContainer container) {
		StorageContainerDetail result = new StorageContainerDetail();
		StorageContainerDetail.transform(container, result);
		
		result.setTemperature(container.getTemperature());
		result.setDimensionOneLabelingScheme(container.getDimensionOneLabelingScheme());
		result.setDimensionTwoLabelingScheme(container.getDimensionTwoLabelingScheme());
		result.setComments(container.getComments());
		result.setAllowedSpecimenClasses(new HashSet<String>(container.getAllowedSpecimenClasses()));
		result.setAllowedSpecimenTypes(new HashSet<String>(container.getAllowedSpecimenTypes()));
		
		Set<String> cpNames = new HashSet<String>();
		for (CollectionProtocol cp : container.getAllowedCps()) {
			cpNames.add(cp.getTitle());
		}
		result.setAllowedCollectionProtocols(cpNames);
		
		result.setPosition(StorageContainerPositionDetail.from(container.getPosition()));
		result.setOccupiedPositions(container.occupiedPositionsOrdinals());
		return result;
	}
}
