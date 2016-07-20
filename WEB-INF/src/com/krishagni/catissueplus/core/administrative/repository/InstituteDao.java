package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.common.repository.Dao;


public interface InstituteDao extends Dao<Institute> {
	public List<InstituteDetail> getInstitutes(InstituteListCriteria listCrit);
	
	public Long getInstitutesCount(InstituteListCriteria listCrit);

	public List<Institute> getInstituteByNames(List<String> names);

	public Institute getInstituteByName(String name);

}
