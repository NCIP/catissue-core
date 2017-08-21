package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;

public class SrCodeLabelToken extends AbstractSpecimenLabelToken {

	public SrCodeLabelToken() {
		this.name = "SR_CODE";
	}

	@Override
	public String getLabel(Specimen specimen) {
		SpecimenRequirement sr = specimen.getSpecimenRequirement();
		
		String code = null;
		if (sr != null) {
			code = sr.getCode();
		}
		
		if (StringUtils.isBlank(code)) {
			code = StringUtils.EMPTY;
		}

		return code;
	}
}