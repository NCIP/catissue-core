package com.krishagni.catissueplus.core.administrative.events;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.ListenAttributeChanges;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@ListenAttributeChanges
public class StorageContainerDetail extends StorageContainerSummary {
	private Double temperature;

	private String cellDisplayProp;

	private String comments;

	private Set<String> allowedSpecimenClasses = new HashSet<String>();
	
	private Set<String> calcAllowedSpecimenClasses = new HashSet<String>();
	
	private Set<String> allowedSpecimenTypes = new HashSet<String>();
	
	private Set<String> calcAllowedSpecimenTypes = new HashSet<String>();

	private Set<String> allowedCollectionProtocols = new HashSet<String>();
	
	private Set<String> calcAllowedCollectionProtocols = new HashSet<String>();

	private Set<Integer> occupiedPositions = new HashSet<Integer>();

	private Map<String, Integer> specimensByType;

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public String getCellDisplayProp() {
		return cellDisplayProp;
	}

	public void setCellDisplayProp(String cellDisplayProp) {
		this.cellDisplayProp = cellDisplayProp;
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

	public Set<String> getCalcAllowedSpecimenClasses() {
		return calcAllowedSpecimenClasses;
	}

	public void setCalcAllowedSpecimenClasses(Set<String> calcAllowedSpecimenClasses) {
		this.calcAllowedSpecimenClasses = calcAllowedSpecimenClasses;
	}

	public Set<String> getAllowedSpecimenTypes() {
		return allowedSpecimenTypes;
	}

	public void setAllowedSpecimenTypes(Set<String> allowedSpecimenTypes) {
		this.allowedSpecimenTypes = allowedSpecimenTypes;
	}

	public Set<String> getCalcAllowedSpecimenTypes() {
		return calcAllowedSpecimenTypes;
	}

	public void setCalcAllowedSpecimenTypes(Set<String> calcAllowedSpecimenTypes) {
		this.calcAllowedSpecimenTypes = calcAllowedSpecimenTypes;
	}

	public Set<String> getAllowedCollectionProtocols() {
		return allowedCollectionProtocols;
	}

	public void setAllowedCollectionProtocols(Set<String> allowedCollectionProtocols) {
		this.allowedCollectionProtocols = allowedCollectionProtocols;
	}

	public Set<String> getCalcAllowedCollectionProtocols() {
		return calcAllowedCollectionProtocols;
	}

	public void setCalcAllowedCollectionProtocols(Set<String> calcAllowedCollectionProtocols) {
		this.calcAllowedCollectionProtocols = calcAllowedCollectionProtocols;
	}
	
	public Set<Integer> getOccupiedPositions() {
		return occupiedPositions;
	}

	public void setOccupiedPositions(Set<Integer> occupiedPositions) {
		this.occupiedPositions = occupiedPositions;
	}

	public Map<String, Integer> getSpecimensByType() {
		return specimensByType;
	}

	public void setSpecimensByType(Map<String, Integer> specimensByType) {
		this.specimensByType = specimensByType;
	}

	public static StorageContainerDetail from(StorageContainer container) {
		StorageContainerDetail result = new StorageContainerDetail();
		StorageContainerDetail.transform(container, result);

		result.setTemperature(container.getTemperature());
		result.setComments(container.getComments());
		if (container.getCellDisplayProp() != null) {
			result.setCellDisplayProp(container.getCellDisplayProp().name());
		} else {
			result.setCellDisplayProp(StorageContainer.CellDisplayProp.SPECIMEN_LABEL.name());
		}

		result.setAllowedSpecimenClasses(new HashSet<>(container.getAllowedSpecimenClasses()));
		result.setCalcAllowedSpecimenClasses(new HashSet<>(container.getCompAllowedSpecimenClasses()));

		result.setAllowedSpecimenTypes(new HashSet<>(container.getAllowedSpecimenTypes()));
		result.setCalcAllowedSpecimenTypes(new HashSet<>(container.getCompAllowedSpecimenTypes()));
		
		result.setAllowedCollectionProtocols(getCpNames(container.getAllowedCps()));		
		result.setCalcAllowedCollectionProtocols(getCpNames(container.getCompAllowedCps()));
		
		result.setOccupiedPositions(container.occupiedPositionsOrdinals());
		return result;
	}
	
	private static Set<String> getCpNames(Collection<CollectionProtocol> cps) {
		return cps.stream().map(cp -> cp.getShortTitle()).collect(Collectors.toSet());
	}
}
