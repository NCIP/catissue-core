package com.krishagni.catissueplus.core.biospecimen.print;

import java.text.SimpleDateFormat;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class SpecimenCreatedOnPrintToken extends AbstractLabelTmplToken implements LabelTmplToken {

	@Override
	public String getName() {
		return "specimen_created_on";
	}

	@Override
	public String getReplacement(Object object) { // TODO: date format from locale
		Specimen specimen = (Specimen)object;
		return new SimpleDateFormat("MM-dd-yyyy").format(specimen.getCreatedOn());
	}
}
