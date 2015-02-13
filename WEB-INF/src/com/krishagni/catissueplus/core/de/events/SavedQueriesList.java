package com.krishagni.catissueplus.core.de.events;

import java.util.List;

public class SavedQueriesList {
	
	private Long count;
	
	private List<SavedQuerySummary> queries;

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public List<SavedQuerySummary> getQueries() {
		return queries;
	}

	public void setQueries(List<SavedQuerySummary> savedQueries) {
		this.queries = savedQueries;
	}
	
	public static SavedQueriesList create(List<SavedQuerySummary> queries, Long count) {
		SavedQueriesList resp = new SavedQueriesList();
		resp.setQueries(queries);
		resp.setCount(count);
		return resp;
	}	
}
