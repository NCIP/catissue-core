package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.PvAttributes;

public class SpecimenPathologyStatusToken extends AbstractSpmnAbbrLabelToken {

	public SpecimenPathologyStatusToken() {
		this.name = "SP_PATH_STATUS";
	}
	
	@Override
	public String getLabel(Specimen specimen) {
		return getLabel(PvAttributes.PATH_STATUS, specimen.getPathologicalStatus());
	}
}
