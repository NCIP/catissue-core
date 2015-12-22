package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface SpecimenListDao extends Dao<SpecimenList> {
	public List<SpecimenList> getSpecimenLists();
	
	public List<SpecimenList> getUserSpecimenLists(Long userId);
	
	public SpecimenList getSpecimenList(Long listId);
	
	public SpecimenList getSpecimenListByName(String name);

	public SpecimenList getDefaultSpecimenList(Long userId);
	
	public Long getListSpecimensCount(Long listId);
	
	public void deleteSpecimenList(SpecimenList listId);
}
