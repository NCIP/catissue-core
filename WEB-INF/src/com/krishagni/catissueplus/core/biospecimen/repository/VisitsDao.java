
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSummary;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface VisitsDao extends Dao<Visit> {
	
	public List<VisitSummary> getVisits(VisitsListCriteria crit);
	
	public Visit getByName(String name);
	
	public List<Visit> getByName(Collection<String> names);

	public List<Visit> getByIds(Collection<Long> ids);

	public List<Visit> getBySpr(String sprNumber);

	public Map<String, Object> getCprVisitIds(String key, Object value);
}
