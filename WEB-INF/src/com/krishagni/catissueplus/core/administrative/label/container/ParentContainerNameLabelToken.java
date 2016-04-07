package com.krishagni.catissueplus.core.administrative.label.container;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;

public class ParentContainerNameLabelToken extends AbstractContainerLabelToken {

	public ParentContainerNameLabelToken() {
		this.name = "PCONT_NAME";
	}

	@Override
	public String getLabel(StorageContainer container) {
		StorageContainer parent = container.getParentContainer();
		return parent == null ? StringUtils.EMPTY : parent.getName();
	}
}
