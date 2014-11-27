package com.krishagni.catissueplus.core.biospecimen.domain;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;

public class SpecimenPosition extends AbstractPosition {
	private Specimen specimen;
	
	private StorageContainer storageContainer;

	public Specimen getSpecimen() {
		return specimen;
	}

	public void setSpecimen(Specimen specimen) {
		this.specimen = specimen;
	}

	public StorageContainer getStorageContainer() {
		return storageContainer;
	}

	public void setStorageContainer(StorageContainer storageContainer) {
		this.storageContainer = storageContainer;
	}
	
}
