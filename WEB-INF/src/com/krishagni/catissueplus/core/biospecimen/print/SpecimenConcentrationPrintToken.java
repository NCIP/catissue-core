package com.krishagni.catissueplus.core.biospecimen.print;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class SpecimenConcentrationPrintToken implements LabelTmplToken {

	@Override
	public String getName() {
		return "specimen_concentration";
	}

	@Override
	public String getReplacement(Object object) {
		Specimen specimen = (Specimen)object;
		Double dbl = specimen.getConcentrationInMicrogramPerMicroliter();
		return dbl != null ? dbl.toString() : null;
	}
}
