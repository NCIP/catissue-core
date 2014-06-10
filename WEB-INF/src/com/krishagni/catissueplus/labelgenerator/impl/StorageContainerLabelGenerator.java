
package com.krishagni.catissueplus.labelgenerator.impl;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.labelgenerator.LabelGenerator;

public class StorageContainerLabelGenerator extends AbstractLabelGenerator<StorageContainer>
		implements
			LabelGenerator<StorageContainer> {

	private static String CONTAINER_UNIQUE_NUMBER = "CONTAINER_UNIQUE_NUMBER";

	@Override
	public String generateLabel(StorageContainer container) {
		return getUniqueId(CONTAINER_UNIQUE_NUMBER);
	}
}
