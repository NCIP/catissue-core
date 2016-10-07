package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.PvAttributes;
import com.krishagni.catissueplus.core.common.util.PvUtil;

public class SpecimenPathologyStatusToken extends AbstractSpecimenLabelToken {

	public SpecimenPathologyStatusToken() {
		this.name = "SP_PATH_STATUS";
	}
	
	@Override
	public String getLabel(Specimen specimen) {
		return PvUtil.getInstance().getAbbr(PvAttributes.PATH_STATUS, specimen.getPathologicalStatus(), StringUtils.EMPTY);
	}
}
