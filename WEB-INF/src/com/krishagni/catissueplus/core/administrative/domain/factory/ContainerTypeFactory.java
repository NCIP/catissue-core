package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.ContainerType;
import com.krishagni.catissueplus.core.administrative.events.ContainerTypeDetail;

public interface ContainerTypeFactory {
	public ContainerType createContainerType(ContainerTypeDetail detail);
	
}
