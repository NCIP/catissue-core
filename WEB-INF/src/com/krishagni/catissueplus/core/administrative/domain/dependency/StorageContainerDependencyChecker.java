package com.krishagni.catissueplus.core.administrative.domain.dependency;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;

public class StorageContainerDependencyChecker implements EntityDependencyChecker<StorageContainer>{

	@Override
	public Map<String, List> getDependencies(StorageContainer container) {
		// TODO Auto-generated method stub
		return Collections.<String, List>emptyMap();
	}

}
