
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface SpecimenDao extends Dao<Specimen> {

	public Long getScgId(Long specimenId);

	public Specimen getSpecimen(Long id);
	
	public Specimen getByLabel(String label);

	public Specimen getByBarcode(String barcode);
	
	public List<Specimen> getSpecimensByIds(List<Long> specimenIds);
	
	public List<Specimen> getSpecimensByLabels(List<String> labels);
	
	public List<Specimen> getSpecimensByVisitId(Long visitId);
	
	public List<Specimen> getSpecimensByVisitName(String visitName);
	
	public Specimen getParentSpecimenByVisitAndSr(Long visitId, Long srId);

	public List<Specimen> getAllSpecimens(int startAt, int maxRecords, String... searchString);

	public Long getSpecimensCount(String... searchString);
	
	public Map<String, Long> getCprAndVisitIds(Long specimenId);
}
