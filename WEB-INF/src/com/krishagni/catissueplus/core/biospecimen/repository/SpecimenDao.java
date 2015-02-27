
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface SpecimenDao extends Dao<Specimen> {

	public Long getScgId(Long specimenId);

	public Specimen getSpecimen(Long id);
	
	public Specimen getByLabel(String label);

	public Specimen getByBarcode(String barcode);
	
	public List<Specimen> getByLabels(List<String> labels);

	public List<Specimen> getAllSpecimens(int startAt, int maxRecords, String... searchString);

	public Long getSpecimensCount(String... searchString);
}
