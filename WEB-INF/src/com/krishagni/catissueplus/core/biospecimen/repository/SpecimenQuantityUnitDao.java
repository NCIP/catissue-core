package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenQuantityUnit;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface SpecimenQuantityUnitDao extends Dao<SpecimenQuantityUnit> {
	public List<SpecimenQuantityUnit> listAll();
	
	public SpecimenQuantityUnit getByClassAndType(String specimenClass, String type);
}
