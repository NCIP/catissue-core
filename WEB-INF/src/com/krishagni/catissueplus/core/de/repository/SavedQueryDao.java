package com.krishagni.catissueplus.core.de.repository;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;

public interface SavedQueryDao extends Dao<SavedQuery>{
	public Long getQueriesCount(Long userId, String ... searchString);
	
	public List<SavedQuerySummary> getQueries(Long userId, int startAt, int maxRecords, String ... searchString);
		
	public SavedQuery getQuery(Long queryId);
	
	public List<SavedQuery> getQueriesByIds(List<Long> queries);
	
	public Long getQueriesCountByFolderId(Long folderId, String ... searchString);
	
	public List<SavedQuerySummary> getQueriesByFolderId(Long folderId, int startAt, int maxRecords, String ... searchString);
	
	public boolean isQuerySharedWithUser(Long queryId, Long userId);
	
	public Map<String, Object> getQueryChangeLogDetails(String file);
	
	public void insertQueryChangeLog(String file, String digest, String status, Long queryId);

}
