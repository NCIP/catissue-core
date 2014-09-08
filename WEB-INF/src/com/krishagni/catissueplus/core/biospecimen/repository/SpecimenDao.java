
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface SpecimenDao extends Dao<Specimen> {

	public Long getScgId(Long specimenId);

	public Specimen getSpecimen(Long id);
	
	public Specimen getSpecimenByLabel(String label);

	public boolean isLabelUnique(String label);

	public boolean isBarcodeUnique(String barcode);
	
	public List<Specimen> getSpecimensByLabel(List<String> labels);

	public List<Specimen> getAllSpecimens(int startAt, int maxRecords, String... searchString);

	public Long getSpecimensCount(String... searchString);
}
