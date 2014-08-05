
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class ContainerLabel implements LabelToken<StorageContainer> {

	private static String EMPTY_CONTAINER_LABEL = "";

	@Override
	public String getTokenValue(StorageContainer storageContainer) {
		if (storageContainer.getName() == null) {
			return storageContainer.getName();
		}
		return EMPTY_CONTAINER_LABEL;
	}

}