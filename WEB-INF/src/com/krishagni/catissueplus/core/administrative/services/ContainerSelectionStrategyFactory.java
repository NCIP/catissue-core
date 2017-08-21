package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

public interface ContainerSelectionStrategyFactory {
	ContainerSelectionStrategy getStrategy(String name);

	List<String> getStrategyNames();
}
