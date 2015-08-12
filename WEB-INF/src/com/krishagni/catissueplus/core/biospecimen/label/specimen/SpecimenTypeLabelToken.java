package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;

public class SpecimenTypeLabelToken extends AbstractSpmnAbbrLabelToken {

	public SpecimenTypeLabelToken() {
		this.name = "SP_TYPE";
	}

	@Override
	protected String getAbbrFileConfigParam() {		
		return ConfigParams.SP_TYPE_ABBR_MAP;
	}

	@Override
	public String getLabel(Specimen specimen) {
		return super.getLabel(specimen.getSpecimenType(), SpecimenErrorCode.NO_TYPE_ABBR);
	}
}
