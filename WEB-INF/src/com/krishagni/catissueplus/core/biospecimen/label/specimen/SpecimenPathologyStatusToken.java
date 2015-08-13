package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;

public class SpecimenPathologyStatusToken extends AbstractSpmnAbbrLabelToken {

	public SpecimenPathologyStatusToken() {
		this.name = "SP_PATH_STATUS";
	}

	@Override
	protected String getAbbrFileConfigParam() {
		return ConfigParams.SP_PATH_STATUS_ABBR_MAP;
	}
	
	@Override
	public String getLabel(Specimen specimen) {
		return super.getLabel(
			specimen.getPathologicalStatus(), 
			SpecimenErrorCode.NO_PATH_STATUS_ABBR);
	}
}
