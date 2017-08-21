package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class CpCodeLabelToken extends AbstractSpecimenLabelToken {

	public CpCodeLabelToken() {
		this.name = "CP_CODE";
	}

	@Override
	public String getLabel(Specimen specimen) {
		String code = specimen.getCollectionProtocol().getCode();
		if (StringUtils.isBlank(code)) {
			code = StringUtils.EMPTY;
		}

		return code;
	}
}