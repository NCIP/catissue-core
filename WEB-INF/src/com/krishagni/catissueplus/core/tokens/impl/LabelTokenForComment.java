
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForComment implements LabelToken<Specimen> {

	private static String EMPTY_COMMENT = "";

	@Override
	public String getTokenValue(Specimen specimen) {
		if (specimen.getComment() != null) {
			return specimen.getComment();
		}
		return EMPTY_COMMENT;
	}

}
