package com.krishagni.catissueplus.core.common.events;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.MatchMode;


public abstract class AbstractListCriteria<T extends ListCriteria<T>> implements ListCriteria<T> {	
	private int startAt;
	
	private int maxResults;

	private boolean limitItems;
	
	private boolean includePhi;
	
	private String query;
	
	private boolean exactMatch;
	
	private boolean includeStat;
	
	private List<Long> ids = new ArrayList<>();
	
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
	public boolean limitItems() {
		return limitItems;
	}

	@Override
	public T limitItems(boolean limitItems) {
		this.limitItems = limitItems;
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

	public MatchMode matchMode() {
		return exactMatch() ? MatchMode.EXACT : MatchMode.ANYWHERE;
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
	public List<Long> ids() {
		return ids;
	}
	
	@Override
	public T ids(List<Long> ids) {
		this.ids = ids;
		return self();
	}
	
	public abstract T self();
}
