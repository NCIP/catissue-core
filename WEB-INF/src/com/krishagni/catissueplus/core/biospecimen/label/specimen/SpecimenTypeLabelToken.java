package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.PvAttributes;
import com.krishagni.catissueplus.core.common.util.PvUtil;

public class SpecimenTypeLabelToken extends AbstractSpecimenLabelToken {

	public SpecimenTypeLabelToken() {
		this.name = "SP_TYPE";
	}

	@Override
	public String getLabel(Specimen specimen) {
		return PvUtil.getInstance().getAbbr(PvAttributes.SPECIMEN_CLASS, specimen.getSpecimenType(), StringUtils.EMPTY);
	}
}
