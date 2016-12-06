
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

	public String getSpecimenClass(String type);
	
	public PermissibleValue getByConceptCode(String attribute, String conceptCode);
	
	public PermissibleValue getByValue(String attribute, String value);
	
	public List<PermissibleValue> getByPropertyKeyValue(String attribute,String propName, String propValue);

	public boolean exists(String attribute, Collection<String> values);
	
	public boolean exists(String attribute, String parentValue, Collection<String> values);
	
	public boolean exists(String attribute, Collection<String> values, boolean leafLevelCheck);
	
	public boolean exists(String attribute, int depth, Collection<String> values);
	
	public boolean exists(String attribute, int depth, Collection<String> values, boolean anyLevel);
}
