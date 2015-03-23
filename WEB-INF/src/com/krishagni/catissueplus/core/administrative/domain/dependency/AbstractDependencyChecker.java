package com.krishagni.catissueplus.core.administrative.domain.dependency;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDependencyChecker<T> implements EntityDependencyChecker<T>{
	
	public void setDependentEntities(List<Object[]> stats, List<Map<String, Object>> dependentEntitites) {
		for (Object[] stat: stats) {
			String entityName = (String) stat[0];
			int count = ((Number)stat[1]).intValue();
			setDependentEntities(entityName, count, dependentEntitites); 
		}
	}

	public void setDependentEntities(String entityName, int count, List<Map<String, Object>> dependentEntities) {
		if (count == 0) {
			return;
		}
		
		Map<String, Object> stat = new HashMap<String, Object>();
		stat.put("name", entityName);
		stat.put("count", count);
		
		dependentEntities.add(stat);
	}
}
