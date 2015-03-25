package com.krishagni.catissueplus.core.common.events;

import java.util.List;

public class DependentEntityDetail {
	private String name;
	
	private int count;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public static DependentEntityDetail from(String name, int count) {
		DependentEntityDetail detail = new DependentEntityDetail();
		detail.setName(name);
		detail.setCount(count);
		
		return detail;
	}
	
	public static void setDependentEntities(String name, int count, List<DependentEntityDetail> dependentEntities) {
		if (count == 0) {
			return;
		}
		
		dependentEntities.add(from(name, count));
	}
}
