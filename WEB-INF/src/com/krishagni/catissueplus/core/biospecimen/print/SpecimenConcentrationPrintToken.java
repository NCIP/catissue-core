package com.krishagni.catissueplus.core.biospecimen.print;

import java.math.BigDecimal;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class SpecimenConcentrationPrintToken extends AbstractLabelTmplToken implements LabelTmplToken {

	@Override
	public String getName() {
		return "specimen_concentration";
	}

	@Override
	public String getReplacement(Object object) {
		Specimen specimen = (Specimen)object;
		BigDecimal dbl = specimen.getConcentration();
		return dbl != null ? dbl.toString() : null;
	}
}
