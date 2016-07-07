package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public abstract class AbstractSpmnAbbrLabelToken extends AbstractSpecimenLabelToken {
	
	@Autowired
	private DaoFactory daoFactory;

	protected String getLabel(String attrName, String value) {
		PermissibleValue pv = daoFactory.getPermissibleValueDao().getByValue(attrName, value);
		if (pv == null) {
			return StringUtils.EMPTY;
		}

		String label = pv.getProps().get(ABBREVIATION);
		if (StringUtils.isBlank(label)) {
			label = StringUtils.EMPTY;
		}

		return label;
	}

	private static final String ABBREVIATION = "abbreviation";
}