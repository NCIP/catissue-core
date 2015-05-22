package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import java.util.Calendar;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class SpecimenYocLabelToken extends AbstractSpecimenLabelToken {
	
	public SpecimenYocLabelToken() {
		this.name = "YR_OF_COLL";
	}
	
	@Override
	public String getLabel(Specimen specimen) {
		SpecimenCollectionEvent collEvent = specimen.getCollectionEvent();		
		Calendar cal = Calendar.getInstance();
		cal.setTime(collEvent.getTime());
		return "" + cal.get(Calendar.YEAR);
	}
}