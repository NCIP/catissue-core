
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface SpecimenDao extends Dao<Specimen> {
	public List<Specimen> getSpecimens(SpecimenListCriteria crit);
	
	public Specimen getByLabel(String label);

	public Specimen getByLabelAndCp(String cpShortTitle, String label);

	public Specimen getByBarcode(String barcode);
	
	public List<Specimen> getSpecimensByIds(List<Long> specimenIds);
	
	public List<Specimen> getSpecimensByVisitId(Long visitId);
	
	public List<Specimen> getSpecimensByVisitName(String visitName);
	
	public Specimen getSpecimenByVisitAndSr(Long visitId, Long srId);

	public Specimen getParentSpecimenByVisitAndSr(Long visitId, Long srId);

	public Map<String, Object> getCprAndVisitIds(String key, Object value);
	
	public Map<Long, Set<Long>> getSpecimenSites(Set<Long> specimenIds);

	public Map<Long, String> getDistributionStatus(List<Long> specimenIds);

	public String getDistributionStatus(Long specimenId);

	public List<Visit> getSpecimenVisits(SpecimenListCriteria crit);

	public boolean areDuplicateLabelsPresent();

	public Map<Long, Long> getSpecimenStorageSite(Set<Long> specimenIds);
}
