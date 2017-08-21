package com.krishagni.catissueplus.core.biospecimen.print;

import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;

public class SpecimenCreatedOnPrintToken extends AbstractLabelTmplToken implements LabelTmplToken {

	@Override
	public String getName() {
		return "specimen_created_on";
	}

	@Override
	public String getReplacement(Object object) {
		Specimen specimen = (Specimen)object;
		if (specimen.getCreatedOn() == null) {
			return StringUtils.EMPTY;
		}

		return new SimpleDateFormat(ConfigUtil.getInstance().getDeDateFmt()).format(specimen.getCreatedOn());
	}
}
