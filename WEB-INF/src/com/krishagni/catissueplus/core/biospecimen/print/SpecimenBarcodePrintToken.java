package com.krishagni.catissueplus.core.biospecimen.print;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class SpecimenBarcodePrintToken extends AbstractLabelTmplToken implements LabelTmplToken {

	@Override
	public String getName() {
		return "specimen_barcode";
	}

	@Override
	public String getReplacement(Object object) {
		Specimen specimen = (Specimen)object;
		return specimen.getBarcode();
	}
}
