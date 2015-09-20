package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenUnit;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface SpecimenUnitDao extends Dao<SpecimenUnit> {
	public List<SpecimenUnit> listAll();
	
	public SpecimenUnit getByClassAndType(String specimenClass, String type);
}
