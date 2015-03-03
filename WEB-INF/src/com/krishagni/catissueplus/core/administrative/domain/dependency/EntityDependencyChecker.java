package com.krishagni.catissueplus.core.administrative.domain.dependency;

import java.util.List;
import java.util.Map;

public interface EntityDependencyChecker<T> {

	public Map<String, List> getDependencies(T t);
}
