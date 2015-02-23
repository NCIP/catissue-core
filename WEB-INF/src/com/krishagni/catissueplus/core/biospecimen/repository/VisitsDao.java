
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSummary;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface VisitsDao extends Dao<Visit> {
	
	public List<VisitSummary> getVisits(VisitsListCriteria crit);
	
	public Visit getVisitByName(String name);
	
	//
	// TODO: Requires review
	//
	public List<Specimen> getSpecimensList(Long scgId);

	

	public Visit getScgByBarcode(String barcode);

	public Visit getscg(Long scgId);

	public List<Visit> getAllScgs(int startAt, int maxRecords, String... searchString);

	public Long getScgsCount(String... searchString);
}
