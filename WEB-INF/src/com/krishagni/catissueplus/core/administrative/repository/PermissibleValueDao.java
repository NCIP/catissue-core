
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.Collection;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.events.ListPvCriteria;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface PermissibleValueDao extends Dao<PermissibleValue>{
	public List<PermissibleValue> getPvs(ListPvCriteria crit);	
	
	public List<String> getSpecimenClasses();
	
	public List<String> getSpecimenTypes(Collection<String> specimenClasses);
}
