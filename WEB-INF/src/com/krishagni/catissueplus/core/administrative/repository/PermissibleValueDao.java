
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface PermissibleValueDao extends Dao<PermissibleValue>{

	public PermissibleValue getPermissibleValue(Long id);

	public Boolean isUniqueValueInAttribute(String value, String attribute);
	
	public PermissibleValue getPvByValueAndAttribute(String value, String attribute);

	public Boolean isUniqueConceptCode(String conceptCode);
	
	public List<PermissibleValue> getAllPVsByAttribute (String attribute, String searchString, int maxResults);

	public List<String> getAllValuesByAttribute(String attribute);

	public Boolean isPvAvailable(String attribute, String parentValue, String value);

}
