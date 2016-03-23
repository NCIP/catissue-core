package com.krishagni.catissueplus.core.administrative.label.container;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.util.SchemeOrdinalConverterUtil;
import com.krishagni.catissueplus.core.common.util.Utility;

public class ParentContainerAlphabetSeqLabelToken extends AbstractContainerLabelToken {

	@Autowired
	private DaoFactory daoFactory;

	public ParentContainerAlphabetSeqLabelToken() {
		this.name = "PCONT_ALPHA_SEQ";
	}

	@Override
	public String getLabel(StorageContainer container) {
		StorageContainer parent = container.getParentContainer();
		if (parent == null) {
			return StringUtils.EMPTY;
		}

		Long uniqueId = daoFactory.getUniqueIdGenerator().getUniqueId(getName(), parent.getName());
		return SchemeOrdinalConverterUtil.fromOrdinal(
			StorageContainer.UPPER_CASE_ALPHA_LABELING_SCHEME,
			uniqueId.intValue());
	}
}
