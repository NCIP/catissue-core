package com.krishagni.catissueplus.core.biospecimen.label.cpr;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public abstract  class AbstractPpidToken extends AbstractLabelTmplToken implements LabelTmplToken  {
	protected String name = "";
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getReplacement(Object object) {
		return getReplacement(object);
	};

	@Override
	public String getReplacement(Object object, String... args) {
		if (!(object instanceof CollectionProtocolRegistration)) {
			throw new RuntimeException("Invalid input object type");
		}
		
		return getLabel((CollectionProtocolRegistration)object, args);
	}
	
	public abstract String getLabel(CollectionProtocolRegistration cpr, String... args);
}
