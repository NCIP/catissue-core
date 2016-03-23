package com.krishagni.catissueplus.core.administrative.label.container;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public abstract class AbstractContainerLabelToken extends AbstractLabelTmplToken implements LabelTmplToken {
	protected String name = StringUtils.EMPTY;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getReplacement(Object object) {
		if (!(object instanceof StorageContainer)) {
			throw new RuntimeException("Invalid input object type");
		}

		return getLabel((StorageContainer) object);
	}

	public abstract String getLabel(StorageContainer container);
}
