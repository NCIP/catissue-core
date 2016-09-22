package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.events.TenantDetail;

public interface ContainerSelectionStrategy {
	StorageContainer getContainer(TenantDetail criteria, Boolean aliquotsInSameContainer);
}
