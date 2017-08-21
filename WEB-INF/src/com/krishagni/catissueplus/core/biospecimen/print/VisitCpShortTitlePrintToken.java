package com.krishagni.catissueplus.core.biospecimen.print;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class VisitCpShortTitlePrintToken extends AbstractLabelTmplToken implements LabelTmplToken {
	@Override
	public String getName() {		
		return "visit_cp_title";
	}

	@Override
	public String getReplacement(Object object) {
		return ((Visit)object).getCollectionProtocol().getShortTitle();
	}
}