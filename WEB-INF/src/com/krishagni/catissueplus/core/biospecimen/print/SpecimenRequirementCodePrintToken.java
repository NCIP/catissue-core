package com.krishagni.catissueplus.core.biospecimen.print;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class SpecimenRequirementCodePrintToken extends AbstractLabelTmplToken implements LabelTmplToken {
	@Override
	public String getName() {
		return "specimen_requirement_code";
	}

	@Override
	public String getReplacement(Object object) {
		Specimen specimen = (Specimen) object;
		SpecimenRequirement req = specimen.getSpecimenRequirement();
		if (req == null || StringUtils.isBlank(req.getCode())) {
			return StringUtils.EMPTY;
		}

		return req.getCode();
	}
}
