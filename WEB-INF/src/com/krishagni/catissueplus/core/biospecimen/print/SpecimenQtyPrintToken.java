package com.krishagni.catissueplus.core.biospecimen.print;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class SpecimenQtyPrintToken implements LabelTmplToken {

	@Override
	public String getName() {
		return "specimen_quantity";
	}

	@Override
	public String getReplacement(Object object) {
		Specimen specimen = (Specimen)object;
		Double dbl = specimen.getInitialQuantity();
		return dbl != null ? dbl.toString() : null;
	}
}
