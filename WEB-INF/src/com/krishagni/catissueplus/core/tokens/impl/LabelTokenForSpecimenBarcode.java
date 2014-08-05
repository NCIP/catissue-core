
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForSpecimenBarcode implements LabelToken<Specimen> {

	private static String EMPTY_BARCODE = "";

	@Override
	public String getTokenValue(Specimen specimen) {
		if (specimen.getBarcode() != null) {
			return specimen.getBarcode();
		}
		return EMPTY_BARCODE;
	}

}
