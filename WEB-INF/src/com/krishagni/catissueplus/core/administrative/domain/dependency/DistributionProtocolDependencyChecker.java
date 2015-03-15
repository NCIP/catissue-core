package com.krishagni.catissueplus.core.administrative.domain.dependency;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;

public class DistributionProtocolDependencyChecker implements EntityDependencyChecker<DistributionProtocol> {

	@Override
	public Map<String, List> getDependencies(DistributionProtocol dp) {
		//TODO: Revisit to check and return dependency
		
		return Collections.<String, List>emptyMap();
	}

}
