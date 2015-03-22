package com.krishagni.catissueplus.core.administrative.domain.dependency;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDependencyChecker<T> implements EntityDependencyChecker<T>{
	
	public void setStat(List<Object[]> stats, List<Map<String, Object>> dependencyStat) {
		for (Object[] stat: stats) {
			String entityName = (String) stat[0];
			int count = ((Number)stat[1]).intValue();
			setStat(entityName, count, dependencyStat); 
		}
	}

	public void setStat(String entityName, int count, List<Map<String, Object>> dependencyStat) {
		if (count == 0) {
			return;
		}
		
		Map<String, Object> stat = new HashMap<String, Object>();
		stat.put("entityName", entityName);
		stat.put("count", count);
		
		dependencyStat.add(stat);
	}
}
