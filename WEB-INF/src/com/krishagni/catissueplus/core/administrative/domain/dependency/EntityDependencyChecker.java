package com.krishagni.catissueplus.core.administrative.domain.dependency;

import java.util.List;
import java.util.Map;

public interface EntityDependencyChecker<T> {
	public List<Map<String, Object>> getDependencyStat(T t);
	
}
