package com.krishagni.catissueplus.core.biospecimen.print;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class VisitNamePrintToken extends AbstractLabelTmplToken implements LabelTmplToken {

	@Override
	public String getName() {		
		return "visit_name";
	}

	@Override
	public String getReplacement(Object object) {
		if (object instanceof Specimen){
			return ((Specimen)object).getVisit().getName();
		} else {
			return ((Visit)object).getName();
		}
	}
}
