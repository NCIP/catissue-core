
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

public class AliquotDetail {

	private Integer noOfAliquots;

	private Double quantityPerAliquot;

	private List<StorageLocation> storageLocations;

	public Integer getNoOfAliquots() {
		return noOfAliquots;
	}

	public void setNoOfAliquots(Integer count) {
		this.noOfAliquots = count;
	}

	public Double getQuantityPerAliquot() {
		return quantityPerAliquot;
	}

	public void setQuantityPerAliquot(Double quantityPerAliquot) {
		this.quantityPerAliquot = quantityPerAliquot;
	}

	public List<StorageLocation> getStorageLocations() {
		return storageLocations;
	}

	public void setStorageLocations(List<StorageLocation> storageLocations) {
		this.storageLocations = storageLocations;
	}

}
