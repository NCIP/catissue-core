package com.krishagni.catissueplus.core.common.repository.impl;

import com.krishagni.catissueplus.core.common.repository.ListCriteria;

public abstract class AbstractListCriteria<T extends ListCriteria<T>> implements ListCriteria<T> {	
	private int startAt;
	
	private int maxResults;
	
	private boolean includePhi;
	
	private String query;
	
	private boolean includeStat;
		
	@Override
	public int startAt() {
		return startAt;
	}

	@Override
	public T startAt(int startAt) {
		this.startAt = startAt;
		return self();
	}

	@Override
	public int maxResults() {		
		return maxResults;
	}

	@Override
	public T maxResults(int maxResults) {
		this.maxResults = maxResults;
		return self();
	}

	@Override
	public boolean includePhi() {
		return includePhi;
	}

	@Override
	public T includePhi(boolean includePhi) {
		this.includePhi = includePhi;
		return self();
	}

	@Override
	public String query() {
		return query;
	}

	@Override
	public T query(String query) {
		this.query = query;
		return self();
	}
	
	@Override
	public boolean includeStat() {
		return includeStat;
	}

	@Override
	public T includeStat(boolean includeStat) {
		this.includeStat = includeStat;
		return self();
	}
		
	public abstract T self();
}
