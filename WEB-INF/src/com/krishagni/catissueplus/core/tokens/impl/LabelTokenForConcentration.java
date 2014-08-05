
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForConcentration implements LabelToken<Specimen> {

	private static String EMPTY_CONCENTRATION = "";

	@Override
	public String getTokenValue(Specimen specimen) {
		if (specimen.getConcentrationInMicrogramPerMicroliter() != null) {
			return specimen.getConcentrationInMicrogramPerMicroliter().toString();
		}
		return EMPTY_CONCENTRATION;
	}
}
