package com.krishagni.catissueplus.core.de.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;

public interface SavedQueryDao extends Dao<SavedQuery>{
	
	public List<SavedQuerySummary> getQueries(Long userId, int startAt, int maxRecords);
		
	public SavedQuery getQuery(Long queryId);
	
	public List<SavedQuery> getQueriesByIds(List<Long> queries);
	
	public List<SavedQuerySummary> getQueriesByFolderId(Long folderId);

}
