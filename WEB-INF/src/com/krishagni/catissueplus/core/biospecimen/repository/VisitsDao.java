
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSummary;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface VisitsDao extends Dao<SpecimenCollectionGroup> {
	
	public List<VisitSummary> getVisits(VisitsListCriteria crit);

	//
	// TODO: Requires review
	//
	public List<Specimen> getSpecimensList(Long scgId);

	public SpecimenCollectionGroup getScgByName(String name);

	public SpecimenCollectionGroup getScgByBarcode(String barcode);

	public SpecimenCollectionGroup getscg(Long scgId);

	public List<SpecimenCollectionGroup> getAllScgs(int startAt, int maxRecords, String... searchString);

	public Long getScgsCount(String... searchString);
}
