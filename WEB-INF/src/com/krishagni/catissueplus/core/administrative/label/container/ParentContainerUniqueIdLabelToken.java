package com.krishagni.catissueplus.core.administrative.label.container;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class ParentContainerUniqueIdLabelToken extends AbstractContainerLabelToken {

	@Autowired
	private DaoFactory daoFactory;

	public ParentContainerUniqueIdLabelToken() {
		this.name = "PCONT_UID";
	}

	@Override
	public String getLabel(StorageContainer container) {
		StorageContainer parent = container.getParentContainer();
		if (parent == null) {
			return StringUtils.EMPTY;
		}

		Long uniqueId = daoFactory.getUniqueIdGenerator().getUniqueId(name, parent.getName());
		return uniqueId.toString();
	}
}
