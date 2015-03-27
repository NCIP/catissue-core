package com.krishagni.catissueplus.core.common.events;

import java.util.Set;

public interface ListCriteria<T extends ListCriteria<T>> {
	public int startAt();
	
	public T startAt(int startAt);
	
	public int maxResults();
	
	public T maxResults(int maxResults);
	
	public boolean includePhi();
	
	public T includePhi(boolean includePhi);
	
	public String query();
	
	public T query(String query);
	
	public boolean exactMatch();
	
	public T exactMatch(boolean exactMatch);
	
	public boolean includeStat();
	
	public T includeStat(boolean includeStat);
	
	public Set<Long> ids();
	
	public T ids(Set<Long> ids);
}
