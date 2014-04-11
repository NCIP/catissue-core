package com.krishagni.catissueplus.core.de.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;

public interface SavedQueryDao extends Dao<SavedQuery>{
	
	public List<SavedQuery> getQueries(int startAt, int maxRecords);
	
	public SavedQuery getQuery(Long queryId);

}
