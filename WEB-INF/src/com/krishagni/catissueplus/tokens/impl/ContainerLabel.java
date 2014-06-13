
package com.krishagni.catissueplus.tokens.impl;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.tokens.LabelToken;

public class ContainerLabel implements LabelToken<StorageContainer> {

	@Override
	public String getTokenValue(StorageContainer storageContainer) {
		return storageContainer.getName();
	}

}
