package com.krishagni.catissueplus.core.biospecimen.print;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class VisitDatePrintToken extends AbstractLabelTmplToken implements LabelTmplToken {
	
	@Autowired
	private MessageSource messageSource;

	@Override
	public String getName() {		
		return "visit_date";
	}

	@Override
	public String getReplacement(Object object) {
		String fmt = messageSource.getMessage("common_de_be_date_fmt", null, Locale.getDefault());
		return new SimpleDateFormat(fmt).format(((Visit)object).getVisitDate());
	}
}