package com.krishagni.catissueplus.core.biospecimen.print;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;

public class VisitDatePrintToken extends AbstractLabelTmplToken implements LabelTmplToken {
	@Override
	public String getName() {		
		return "visit_date";
	}

	@Override
	public String getReplacement(Object object) {
		Date date = null;
		if (object instanceof Specimen) {
			date = ((Specimen) object).getVisit().getVisitDate();
		} else if (object instanceof Visit) {
			date = ((Visit) object).getVisitDate();
		}

		if (date == null) {
			return StringUtils.EMPTY;
		}

		return new SimpleDateFormat(ConfigUtil.getInstance().getDeDateFmt()).format(date);
	}
}