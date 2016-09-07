package com.krishagni.catissueplus.core.administrative.events;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.ListenAttributeChanges;

@ListenAttributeChanges
public class StorageContainerDetail extends StorageContainerSummary {
	private Double temperature;
	
	private String comments;
	
	private Set<String> allowedSpecimenClasses = new HashSet<String>();
	
	private Set<String> calcAllowedSpecimenClasses = new HashSet<String>();
	
	private Set<String> allowedSpecimenTypes = new HashSet<String>();
	
	private Set<String> calcAllowedSpecimenTypes = new HashSet<String>();

	private Set<String> allowedCollectionProtocols = new HashSet<String>();
	
	private Set<String> calcAllowedCollectionProtocols = new HashSet<String>();
	
	private Set<Integer> occupiedPositions = new HashSet<Integer>();

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
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
	
	public static StorageContainerDetail from(StorageContainer container) {
		StorageContainerDetail result = new StorageContainerDetail();
		StorageContainerDetail.transform(container, result);
		
		result.setTemperature(container.getTemperature());
		result.setComments(container.getComments());
		result.setAllowedSpecimenClasses(new HashSet<String>(container.getAllowedSpecimenClasses()));
		result.setCalcAllowedSpecimenClasses(new HashSet<String>(container.getCompAllowedSpecimenClasses()));		
		result.setAllowedSpecimenTypes(new HashSet<String>(container.getAllowedSpecimenTypes()));
		result.setCalcAllowedSpecimenTypes(new HashSet<String>(container.getCompAllowedSpecimenTypes()));
		
		result.setAllowedCollectionProtocols(getCpNames(container.getAllowedCps()));		
		result.setCalcAllowedCollectionProtocols(getCpNames(container.getCompAllowedCps()));
		
		result.setOccupiedPositions(container.occupiedPositionsOrdinals());
		return result;
	}
	
	private static Set<String> getCpNames(Collection<CollectionProtocol> cps) {
		Set<String> cpNames = new HashSet<String>();
		for (CollectionProtocol cp : cps) {
			cpNames.add(cp.getShortTitle());
		}
		return cpNames;
	}
}
