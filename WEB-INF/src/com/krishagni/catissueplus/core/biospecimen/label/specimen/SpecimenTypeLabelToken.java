package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.PvAttributes;

public class SpecimenTypeLabelToken extends AbstractSpmnAbbrLabelToken {

	public SpecimenTypeLabelToken() {
		this.name = "SP_TYPE";
	}

	@Override
	public String getLabel(Specimen specimen) {
		return getLabel(PvAttributes.SPECIMEN_CLASS, specimen.getSpecimenType());
	}
}
