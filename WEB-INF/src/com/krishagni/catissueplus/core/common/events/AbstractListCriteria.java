package com.krishagni.catissueplus.core.common.events;

import java.util.HashSet;
import java.util.Set;


public abstract class AbstractListCriteria<T extends ListCriteria<T>> implements ListCriteria<T> {	
	private int startAt;
	
	private int maxResults;
	
	private boolean includePhi;
	
	private String query;
	
	private boolean exactMatch;
	
	private boolean includeStat;
	
	private Set<Long> ids = new HashSet<Long>();
	
	@Override
	public int startAt() {
		return startAt <= 0 ? 0 : startAt;
	}

	@Override
	public T startAt(int startAt) {
		this.startAt = startAt;
		return self();
	}

	@Override
	public int maxResults() {		
		return maxResults <= 0 ? 100 : maxResults;
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
	public boolean exactMatch() {
		return exactMatch;
	}
	
	@Override
	public T exactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
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
		
	@Override
	public Set<Long> ids() {
		return ids;
	}
	
	@Override
	public T ids(Set<Long> ids) {
		this.ids = ids;
		return self();
	}
	
	public abstract T self();
}
