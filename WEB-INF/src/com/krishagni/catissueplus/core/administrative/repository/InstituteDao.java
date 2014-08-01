package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.common.repository.Dao;


public interface InstituteDao extends Dao<Institute> {
	
	public Institute getInstituteByName(String name);

	public Institute getInstitute(Long instituteId);

	public List<Institute> getAllInstitutes(int maxResults);

}
