package com.krishagni.catissueplus.core.biospecimen.label.visit;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public abstract class AbstractVisitLabelToken extends AbstractLabelTmplToken implements LabelTmplToken {
	protected String name = "";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getReplacement(Object object) {
		return getReplacement(object, null);
	}

	@Override
	public String getReplacement(Object object, String... args) {
		if (!(object instanceof Visit)) {
			throw new RuntimeException("Invalid input object type");
		}

		return getLabel((Visit)object, args);
	}
	
	public abstract String getLabel(Visit visit, String... args);
}
