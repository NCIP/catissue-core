package com.krishagni.catissueplus.core.administrative.label.container;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class SiteUniqueIdLabelToken extends AbstractContainerLabelToken {

	@Autowired
	private DaoFactory daoFactory;

	public SiteUniqueIdLabelToken() {
		this.name = "SITE_UID";
	}

	@Override
	public String getLabel(StorageContainer container) {
		Long uniqueId = daoFactory.getUniqueIdGenerator().getUniqueId(getName(), container.getSite().getId().toString());
		return uniqueId.toString();
	}
}
